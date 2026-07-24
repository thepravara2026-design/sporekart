export const BASE_CONFIG = {
  hosts: {
    'localhost:8080': __ENV.TARGET_HOST || 'localhost:8080',
  },
  thresholds: {
    http_req_duration: ['p(95) < 200', 'p(99) < 500'],
    http_req_failed: ['rate < 0.001'],
  },
};

export const ENVIRONMENTS = {
  local: {
    baseUrl: 'http://localhost:8080',
    vus: 10,
    duration: '1m',
  },
  ci: {
    baseUrl: 'http://localhost:8080',
    vus: 20,
    duration: '2m',
  },
  staging: {
    baseUrl: 'https://staging.sporekart.com',
    vus: 100,
    duration: '10m',
  },
  production: {
    baseUrl: 'https://api.sporekart.com',
    vus: 500,
    duration: '30m',
  },
};

export function getEnvironment(name) {
  return ENVIRONMENTS[name] || ENVIRONMENTS.local;
}
