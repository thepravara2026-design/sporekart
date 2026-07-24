import { check, sleep, group } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const responseTrend = new Trend('response_duration_ms');
const errorRate = new Rate('errors');
const throughputRate = new Rate('throughput');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    constant_load: {
      executor: 'constant-vus',
      vus: 50,
      duration: '1h',
    },
  },
  thresholds: {
    response_duration_ms: ['p(95) < 200', 'p(99) < 500'],
    errors: ['rate < 0.001'],
  },
};

const endpoints = [
  { method: 'GET', url: '/api/v1/actuator/health', weight: 5 },
  { method: 'GET', url: '/api/v1/products', weight: 4 },
  { method: 'GET', url: '/api/v1/categories', weight: 3 },
  { method: 'GET', url: '/api/v1/actuator/metrics', weight: 2 },
];

function weightedRandom(items) {
  const totalWeight = items.reduce((sum, item) => sum + item.weight, 0);
  let random = Math.random() * totalWeight;
  for (const item of items) {
    random -= item.weight;
    if (random <= 0) return item;
  }
  return items[0];
}

export default function () {
  throughputRate.add(1);
  const endpoint = weightedRandom(endpoints);

  const res = http.get(`${BASE_URL}${endpoint.url}`);
  responseTrend.add(res.timings.duration);

  check(res, {
    'status is 2xx': (r) => r.status >= 200 && r.status < 300,
    'response time < 200ms': (r) => r.timings.duration < 200,
  }) || errorRate.add(1);

  sleep(Math.random() * 2 + 0.5);
}
