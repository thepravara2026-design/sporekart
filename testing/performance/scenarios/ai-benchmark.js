import { check, sleep, group } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const promptProcessingTrend = new Trend('prompt_processing_duration_ms');
const knowledgeRetrievalTrend = new Trend('knowledge_retrieval_duration_ms');
const ragRetrievalTrend = new Trend('rag_retrieval_duration_ms');
const contextBuildTrend = new Trend('context_build_duration_ms');
const responseStreamTrend = new Trend('response_stream_duration_ms');
const aiRequestTrend = new Trend('ai_request_duration_ms');
const errorRate = new Rate('ai_errors');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const AUTH_TOKEN = __ENV.AUTH_TOKEN || '';

const params = {
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${AUTH_TOKEN}`,
  },
};

export const options = {
  scenarios: {
    constant_ai_load: {
      executor: 'constant-vus',
      vus: 20,
      duration: '10m',
    },
    ai_spike: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: 100 },
        { duration: '2m', target: 100 },
        { duration: '1m', target: 0 },
      ],
      startTime: '10m',
    },
  },
  thresholds: {
    ai_request_duration_ms: ['p(95) < 3000', 'p(99) < 5000'],
    prompt_processing_duration_ms: ['p(95) < 100', 'p(99) < 200'],
    knowledge_retrieval_duration_ms: ['p(95) < 200', 'p(99) < 500'],
    rag_retrieval_duration_ms: ['p(95) < 200', 'p(99) < 500'],
    context_build_duration_ms: ['p(95) < 100', 'p(99) < 200'],
    ai_errors: ['rate < 0.01'],
  },
};

const prompts = [
  { query: 'What products are available for organic farming?', context: 'customer-support' },
  { query: 'Explain the checkout process for wholesale orders', context: 'sales' },
  { query: 'What are the latest inventory updates for fertilizers?', context: 'inventory' },
  { query: 'Compare prices between different seed brands', context: 'comparison' },
  { query: 'How do I set up a recurring order?', context: 'account' },
];

export default function () {
  const prompt = prompts[Math.floor(Math.random() * prompts.length)];

  group('AI Prompt Processing', function () {
    const startTime = Date.now();

    const completionRes = http.post(
      `${BASE_URL}/api/v1/ai/completion`,
      JSON.stringify({
        prompt: prompt.query,
        context: prompt.context,
        stream: false,
        maxTokens: 512,
      }),
      params,
    );

    const duration = Date.now() - startTime;
    aiRequestTrend.add(duration);
    promptProcessingTrend.add(completionRes.timings.duration);

    check(completionRes, {
      'ai completion status 200': (r) => r.status === 200,
      'ai request < 3s': () => duration < 3000,
    }) || errorRate.add(1);
  });

  group('Knowledge Retrieval', function () {
    const knowledgeRes = http.post(
      `${BASE_URL}/api/v1/knowledge/search`,
      JSON.stringify({
        query: prompt.query,
        topK: 5,
        threshold: 0.7,
      }),
      params,
    );

    knowledgeRetrievalTrend.add(knowledgeRes.timings.duration);
    check(knowledgeRes, {
      'knowledge search status 200': (r) => r.status === 200,
      'knowledge retrieval < 200ms': (r) => r.timings.duration < 200,
    }) || errorRate.add(1);
  });

  group('RAG Retrieval', function () {
    const ragRes = http.post(
      `${BASE_URL}/api/v1/rag/retrieve`,
      JSON.stringify({
        query: prompt.query,
        sources: ['products', 'inventory', 'knowledge'],
        maxDocuments: 3,
      }),
      params,
    );

    ragRetrievalTrend.add(ragRes.timings.duration);
    check(ragRes, {
      'rag retrieval status 200': (r) => r.status === 200,
      'rag retrieval < 200ms': (r) => r.timings.duration < 200,
    }) || errorRate.add(1);
  });

  sleep(3);
}
