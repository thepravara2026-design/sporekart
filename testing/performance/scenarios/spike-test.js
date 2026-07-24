import { check, sleep } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const responseTrend = new Trend('spike_response_duration_ms');
const errorRate = new Rate('spike_errors');
const recoveryRate = new Rate('recovery_success');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    spike_traffic: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '2m', target: 100 },
        { duration: '5m', target: 100 },
        { duration: '10s', target: 5000 },
        { duration: '30s', target: 5000 },
        { duration: '30s', target: 0 },
      ],
    },
    recovery: {
      executor: 'constant-vus',
      vus: 10,
      duration: '5m',
      startTime: '8m10s',
    },
  },
  thresholds: {
    spike_response_duration_ms: ['p(95) < 1000', 'p(99) < 2000'],
    spike_errors: ['rate < 0.05'],
    recovery_success: ['rate > 0.99'],
  },
};

export default function () {
  const res = http.get(`${BASE_URL}/api/v1/products?page=0&size=10`);
  responseTrend.add(res.timings.duration);

  const success = res.status >= 200 && res.status < 500;
  check(res, {
    'status acceptable': () => success,
    'response time < 1s': (r) => r.timings.duration < 1000,
  }) || errorRate.add(1);

  if (success && res.timings.duration < 1000) {
    recoveryRate.add(1);
  }

  sleep(0.1);
}
