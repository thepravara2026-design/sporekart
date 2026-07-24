import { check, sleep } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const responseTrend = new Trend('soak_response_duration_ms');
const errorRate = new Rate('soak_errors');
const memoryTrend = new Trend('soak_memory_leak_indicator');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    soak_test: {
      executor: 'constant-vus',
      vus: 200,
      duration: '4h',
    },
  },
  thresholds: {
    soak_response_duration_ms: [
      'p(95) < 500',
      'p(99) < 1000',
      { threshold: 'avg < 200', abortOnFail: true },
    ],
    soak_errors: ['rate < 0.01'],
  },
};

const endpoints = [
  () => http.get(`${BASE_URL}/api/v1/products?page=0&size=20`),
  () => http.get(`${BASE_URL}/api/v1/categories`),
  () => http.get(`${BASE_URL}/api/v1/actuator/health`),
  () => http.get(`${BASE_URL}/api/v1/actuator/metrics`),
];

function detectDegradation(currentLatency, baselineLatency) {
  if (baselineLatency > 0 && currentLatency > baselineLatency * 2) {
    console.warn(`Latency degradation detected: ${currentLatency}ms vs baseline ${baselineLatency}ms`);
  }
}

export default function () {
  const startTime = Date.now();

  const endpointFn = endpoints[Math.floor(Math.random() * endpoints.length)];
  const res = endpointFn();

  const duration = Date.now() - startTime;
  responseTrend.add(res.timings.duration);

  if (__ITER > 0 && __ITER % 100 === 0) {
    detectDegradation(res.timings.duration, responseTrend.avg);
  }

  check(res, {
    'status is 2xx': (r) => r.status >= 200 && r.status < 300,
    'response time < 500ms': (r) => r.timings.duration < 500,
  }) || errorRate.add(1);

  sleep(2);
}
