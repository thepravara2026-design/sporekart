import { check, sleep, group } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const apiResponseTrend = new Trend('api_response_duration_ms');
const repositoryReadTrend = new Trend('repository_read_duration_ms');
const repositoryWriteTrend = new Trend('repository_write_duration_ms');
const errorRate = new Rate('api_errors');
const throughputRate = new Rate('requests_per_second');

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
    constant_api_load: {
      executor: 'constant-vus',
      vus: 200,
      duration: '10m',
    },
    api_spike: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: 1000 },
        { duration: '2m', target: 1000 },
        { duration: '1m', target: 0 },
      ],
      startTime: '10m',
    },
  },
  thresholds: {
    api_response_duration_ms: ['p(95) < 200', 'p(99) < 500'],
    repository_read_duration_ms: ['p(95) < 50', 'p(99) < 100'],
    repository_write_duration_ms: ['p(95) < 100', 'p(99) < 200'],
    api_errors: ['rate < 0.001'],
  },
};

export default function () {
  throughputRate.add(1);

  group('Customer APIs', function () {
    const profileRes = http.get(`${BASE_URL}/api/v1/customers/me`, params);
    apiResponseTrend.add(profileRes.timings.duration);
    check(profileRes, { 'profile status 200': (r) => r.status === 200 }) || errorRate.add(1);

    const updateRes = http.put(
      `${BASE_URL}/api/v1/customers/me`,
      JSON.stringify({ name: 'Performance Test', preferences: { theme: 'dark' } }),
      params,
    );
    repositoryWriteTrend.add(updateRes.timings.duration);
    check(updateRes, { 'update status 200': (r) => r.status === 200 }) || errorRate.add(1);
  });

  group('Product APIs', function () {
    const productsRes = http.get(`${BASE_URL}/api/v1/products?page=0&size=20`, params);
    apiResponseTrend.add(productsRes.timings.duration);
    check(productsRes, {
      'products status 200': (r) => r.status === 200,
      'products duration < 200ms': (r) => r.timings.duration < 200,
    }) || errorRate.add(1);

    if (productsRes.status === 200) {
      const products = productsRes.json('content');
      if (products && products.length > 0) {
        const productId = products[0].id;
        const detailRes = http.get(`${BASE_URL}/api/v1/products/${productId}`, params);
        apiResponseTrend.add(detailRes.timings.duration);
        check(detailRes, { 'product detail status 200': (r) => r.status === 200 }) || errorRate.add(1);
        repositoryReadTrend.add(detailRes.timings.duration);
      }
    }
  });

  group('Inventory APIs', function () {
    const inventoryRes = http.get(`${BASE_URL}/api/v1/inventory/summary?warehouse=primary`, params);
    apiResponseTrend.add(inventoryRes.timings.duration);
    check(inventoryRes, { 'inventory status 200': (r) => r.status === 200 }) || errorRate.add(1);
  });

  group('Order APIs', function () {
    const ordersRes = http.get(`${BASE_URL}/api/v1/orders?page=0&size=10`, params);
    apiResponseTrend.add(ordersRes.timings.duration);
    check(ordersRes, { 'orders status 200': (r) => r.status === 200 }) || errorRate.add(1);
  });

  sleep(0.5);
}
