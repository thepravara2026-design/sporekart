import { check, sleep } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from 'k6/metrics';

const loginTrend = new Trend('login_duration_ms');
const tokenRefreshTrend = new Trend('token_refresh_duration_ms');
const validateTrend = new Trend('token_validate_duration_ms');
const errorRate = new Rate('auth_errors');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
    constant_auth_load: {
      executor: 'constant-vus',
      vus: 100,
      duration: '5m',
    },
    auth_spike: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 500 },
        { duration: '1m', target: 500 },
        { duration: '30s', target: 0 },
      ],
      startTime: '5m',
    },
  },
  thresholds: {
    login_duration_ms: ['p(95) < 100', 'p(99) < 200'],
    token_refresh_duration_ms: ['p(95) < 50', 'p(99) < 100'],
    token_validate_duration_ms: ['p(95) < 10', 'p(99) < 20'],
    auth_errors: ['rate < 0.01'],
  },
};

const credentials = [
  { username: 'test_user_1', password: 'Password123!' },
  { username: 'test_user_2', password: 'Password123!' },
  { username: 'test_user_3', password: 'Password123!' },
  { username: 'test_user_4', password: 'Password123!' },
  { username: 'test_user_5', password: 'Password123!' },
];

export default function () {
  const cred = credentials[Math.floor(Math.random() * credentials.length)];

  const loginPayload = JSON.stringify({
    username: cred.username,
    password: cred.password,
  });

  const loginRes = http.post(`${BASE_URL}/api/v1/auth/login`, loginPayload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { operation: 'login' },
  });

  loginTrend.add(loginRes.timings.duration);
  check(loginRes, {
    'login status 200': (r) => r.status === 200,
    'login duration < 100ms': (r) => r.timings.duration < 100,
  }) || errorRate.add(1);

  if (loginRes.status === 200) {
    const token = loginRes.json('token');

    const validateRes = http.get(`${BASE_URL}/api/v1/auth/validate`, {
      headers: { Authorization: `Bearer ${token}` },
      tags: { operation: 'validate' },
    });

    validateTrend.add(validateRes.timings.duration);
    check(validateRes, {
      'validate status 200': (r) => r.status === 200,
      'validate duration < 10ms': (r) => r.timings.duration < 10,
    }) || errorRate.add(1);

    const refreshRes = http.post(`${BASE_URL}/api/v1/auth/refresh`, JSON.stringify({ token }), {
      headers: { 'Content-Type': 'application/json' },
      tags: { operation: 'refresh' },
    });

    tokenRefreshTrend.add(refreshRes.timings.duration);
    check(refreshRes, {
      'refresh status 200': (r) => r.status === 200,
      'refresh duration < 50ms': (r) => r.timings.duration < 50,
    }) || errorRate.add(1);
  }

  sleep(1);
}
