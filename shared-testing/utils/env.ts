export function getBaseUrl(): string {
  return process.env.BASE_URL || 'http://localhost:5174';
}

export function getMockAuthToken(): string {
  return process.env.MOCK_AUTH_TOKEN || 'mock-token-sporekart-qa';
}

export function isMockMode(): boolean {
  return process.env.NODE_ENV === 'mock' || process.env.MOCK_MODE === 'true';
}

export function getTimeout(): number {
  return Number(process.env.QA_TEST_TIMEOUT) || 30000;
}
