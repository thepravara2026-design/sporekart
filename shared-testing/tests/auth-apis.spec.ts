import { test, expect } from '@playwright/test';
import { authClient } from '../../frontend/web-app/src/features/auth/authClient';

test.describe('Part 5 — Authentication APIs Validation', () => {

  test('sendOtp returns correct mock payload structure', async () => {
    const result = await authClient.sendOtp('email', 'test@sporekart.mock');
    expect(result).toEqual({
      channel: 'email',
      destination: 'test@sporekart.mock',
      expiresInSeconds: 300,
      resendInSeconds: 30,
    });
  });

  test('sendOtp enforces simulated network latency', async () => {
    const startTime = Date.now();
    await authClient.sendOtp('phone', '5551234567');
    const duration = Date.now() - startTime;
    // Expected latency is ~900ms. We verify it takes at least 800ms.
    expect(duration).toBeGreaterThanOrEqual(800);
  });

  test('verifyOtp handles success and failure stubs correctly', async () => {
    // 1. Success stub
    const successResult = await authClient.verifyOtp('phone', '5551234567', '123456');
    expect(successResult).toEqual({ ok: true, message: 'Verified' });

    // 2. Failure stub (code 000000)
    await expect(authClient.verifyOtp('phone', '5551234567', '000000')).rejects.toThrow(
      'Incorrect code. Please try again.'
    );
  });

  test('register validates payload name', async () => {
    // 1. Success validation
    const result = await authClient.register({
      fullName: 'John Doe',
      phone: '5551234567',
      role: 'customer',
    });
    expect(result.ok).toBe(true);

    // 2. Empty name validation
    await expect(
      authClient.register({
        fullName: '',
        phone: '5551234567',
      })
    ).rejects.toThrow('Full name is required.');
  });
});
