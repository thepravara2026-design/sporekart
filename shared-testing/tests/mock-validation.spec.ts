import { test, expect } from '@playwright/test';

test.describe('Mock Mode Isolation Validation', () => {
  test('MOCK_MODE is enabled', () => {
    const mockMode = process.env.MOCK_MODE || 'true';
    expect(mockMode).toBe('true');
  });

  test('No production API keys are set', () => {
    const envKeys = Object.keys(process.env);
    const prodIndicators = envKeys.filter(
      (k) =>
        k.includes('PROD') ||
        k.includes('LIVE') ||
        (k.includes('KEY') && !k.includes('MOCK')),
    );
    expect(prodIndicators.length).toBe(0);
  });

  test('Mock Razorpay key does not contain production values', () => {
    const razorpayKey = process.env.MOCK_RAZORPAY_KEY_ID || '';
    expect(razorpayKey).not.toContain('rzp_live');
  });

  test('Mock Shiprocket key does not contain production values', () => {
    const shiprocketKey = process.env.MOCK_SHIPROCKET_API_KEY || '';
    expect(shiprocketKey).not.toContain('prod');
  });

  test('FF_PRODUCTION_MODE is false', () => {
    const prodMode = process.env.FF_PRODUCTION_MODE || 'false';
    expect(prodMode).toBe('false');
  });
});
