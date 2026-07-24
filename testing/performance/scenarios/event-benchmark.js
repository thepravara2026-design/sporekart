import { check, sleep, group } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const publishTrend = new Trend('event_publish_duration_ms');
const dispatchTrend = new Trend('event_dispatch_duration_ms');
const handlerTrend = new Trend('event_handler_execution_duration_ms');
const errorRate = new Rate('event_errors');

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
    constant_event_load: {
      executor: 'constant-vus',
      vus: 50,
      duration: '10m',
    },
    event_spike: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 500 },
        { duration: '1m', target: 500 },
        { duration: '30s', target: 0 },
      ],
      startTime: '10m',
    },
  },
  thresholds: {
    event_publish_duration_ms: ['p(95) < 5', 'p(99) < 10'],
    event_dispatch_duration_ms: ['p(95) < 10', 'p(99) < 20'],
    event_handler_execution_duration_ms: ['p(95) < 50', 'p(99) < 100'],
    event_errors: ['rate < 0.001'],
  },
};

const events = [
  { type: 'order.created', payload: { orderId: 'ORD-001', amount: 250.00, customerId: 'CUST-001' } },
  { type: 'inventory.updated', payload: { sku: 'SKU-001', quantity: 100, warehouse: 'primary' } },
  { type: 'payment.processed', payload: { transactionId: 'TXN-001', status: 'completed', amount: 99.99 } },
  { type: 'notification.sent', payload: { channel: 'email', template: 'order-confirmation', recipient: 'test@example.com' } },
  { type: 'user.activity', payload: { userId: 'USR-001', action: 'page_view', resource: '/products' } },
];

export default function () {
  const event = events[Math.floor(Math.random() * events.length)];

  group('Event Publishing', function () {
    const publishRes = http.post(
      `${BASE_URL}/api/v1/events/publish`,
      JSON.stringify({
        type: event.type,
        source: 'performance-test',
        payload: event.payload,
        correlationId: `perf-${Date.now()}`,
      }),
      params,
    );

    publishTrend.add(publishRes.timings.duration);
    check(publishRes, {
      'publish status 202': (r) => r.status === 202,
      'publish duration < 5ms': (r) => r.timings.duration < 5,
    }) || errorRate.add(1);
  });

  group('Event Dispatch', function () {
    const dispatchRes = http.post(
      `${BASE_URL}/api/v1/events/dispatch`,
      JSON.stringify({
        eventType: event.type,
        maxHandlers: 3,
      }),
      params,
    );

    dispatchTrend.add(dispatchRes.timings.duration);
    check(dispatchRes, {
      'dispatch status 200': (r) => r.status === 200,
      'dispatch duration < 10ms': (r) => r.timings.duration < 10,
    }) || errorRate.add(1);
  });

  group('Event Status', function () {
    const statusRes = http.get(`${BASE_URL}/api/v1/events/status?type=${event.type}&last=10`);
    handlerTrend.add(statusRes.timings.duration);
    check(statusRes, {
      'status ok': (r) => r.status === 200,
    }) || errorRate.add(1);
  });

  sleep(0.5);
}
