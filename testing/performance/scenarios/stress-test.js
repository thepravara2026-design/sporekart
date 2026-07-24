import { check, sleep, group } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const responseTrend = new Trend('stress_response_duration_ms');
const errorRate = new Rate('stress_errors');
const systemStableRate = new Rate('system_stable');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    ramp_up: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '5m', target: 100 },
        { duration: '5m', target: 500 },
        { duration: '5m', target: 1000 },
        { duration: '5m', target: 2000 },
        { duration: '5m', target: 5000 },
      ],
    },
    burst: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 10000 },
        { duration: '1m', target: 10000 },
        { duration: '30s', target: 0 },
      ],
      startTime: '25m',
    },
    recovery_check: {
      executor: 'constant-vus',
      vus: 10,
      duration: '5m',
      startTime: '27m',
    },
  },
  thresholds: {
    stress_response_duration_ms: ['p(95) < 2000'],
    stress_errors: ['rate < 0.10'],
    system_stable: ['rate > 0.95'],
  },
};

export default function () {
  group('Stress endpoint', function () {
    const res = http.get(`${BASE_URL}/api/v1/products?page=0&size=20`);
    responseTrend.add(res.timings.duration);

    const stable = res.status >= 200 && res.status < 500 && res.timings.duration < 3000;
    if (stable) systemStableRate.add(1);

    check(res, {
      'status acceptable': (r) => r.status >= 200 && r.status < 500,
    }) || errorRate.add(1);
  });

  sleep(0.05);
}
