import { check, sleep, group } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const queryTrend = new Trend('db_query_duration_ms');
const writeTrend = new Trend('db_write_duration_ms');
const joinTrend = new Trend('db_join_query_duration_ms');
const connectionTrend = new Trend('db_connection_acquire_duration_ms');
const errorRate = new Rate('db_errors');

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
    query_load: {
      executor: 'constant-vus',
      vus: 100,
      duration: '15m',
    },
    write_load: {
      executor: 'per-vu-iterations',
      vus: 50,
      iterations: 200,
      startTime: '15m',
    },
  },
  thresholds: {
    db_query_duration_ms: ['p(95) < 50', 'p(99) < 100'],
    db_write_duration_ms: ['p(95) < 100', 'p(99) < 200'],
    db_join_query_duration_ms: ['p(95) < 100', 'p(99) < 200'],
    db_errors: ['rate < 0.001'],
  },
};

export default function () {
  group('Simple Queries', function () {
    const productRes = http.get(`${BASE_URL}/api/v1/products?page=0&size=10`, params);
    queryTrend.add(productRes.timings.duration);
    check(productRes, { 'product query < 50ms': (r) => r.timings.duration < 50 }) || errorRate.add(1);
  });

  group('Join Queries', function () {
    const orderRes = http.get(`${BASE_URL}/api/v1/orders?page=0&size=10&include=items,payments`, params);
    joinTrend.add(orderRes.timings.duration);
    check(orderRes, { 'order join query < 100ms': (r) => r.timings.duration < 100 }) || errorRate.add(1);
  });

  group('Write Operations', function () {
    const cartRes = http.post(
      `${BASE_URL}/api/v1/cart/items`,
      JSON.stringify({
        productId: 'PROD-001',
        quantity: 1,
      }),
      params,
    );
    writeTrend.add(cartRes.timings.duration);
    check(cartRes, { 'cart write < 100ms': (r) => r.timings.duration < 100 }) || errorRate.add(1);
  });

  sleep(1);
}
