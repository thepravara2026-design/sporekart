import { useEnv } from '../../config/env';
import { authService } from './AuthService';
import type { AuthChannel, AuthResult, SendOtpResult } from './types';

function isValidPhone(value: string): boolean {
  return /^[+]?[\d\s()-]{8,15}$/.test(value.trim());
}

function isValidEmail(value: string): boolean {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim());
}

function validateStub(channel: AuthChannel, destination: string): void {
  if (channel === 'phone' && !isValidPhone(destination)) {
    throw new Error('Please enter a valid phone number.');
  }
  if (channel === 'email' && !isValidEmail(destination)) {
    throw new Error('Please enter a valid email address.');
  }
}

const LATENCY = 900;

function delay<T>(value: T, ms = LATENCY): Promise<T> {
  return new Promise((resolve) => setTimeout(() => resolve(value), ms));
}

async function stubOrReal<T>(stub: () => T, real: () => Promise<T>): Promise<T> {
  const env = useEnv();
  if (env.featureFlags.authProvider === 'mock') {
    return delay(stub());
  }
  return real();
}

export const authClient = {
  async sendOtp(channel: AuthChannel, destination: string): Promise<SendOtpResult> {
    validateStub(channel, destination);
    return stubOrReal(
      () => ({
        channel,
        destination,
        expiresInSeconds: 300,
        resendInSeconds: 30,
      }),
      () => authService.sendOtp(channel, destination),
    );
  },

  async verifyOtp(channel: AuthChannel, destination: string, code: string): Promise<AuthResult> {
    return stubOrReal(
      () => {
        if (code.length < 4) throw new Error('Enter the full code.');
        if (code !== '123456') throw new Error('Incorrect code. Please try again.');
        return { ok: true, message: 'Verified' };
      },
      () => authService.verifyOtp(channel, destination, code),
    );
  },

  async register(payload: { fullName: string; phone: string; email?: string; role?: string }): Promise<AuthResult> {
    return stubOrReal(
      () => {
        if (!payload.fullName.trim()) throw new Error('Full name is required.');
        return { ok: true, message: 'Registration submitted' };
      },
      () => authService.register(payload),
    );
  },

  async login(channel: AuthChannel, destination: string): Promise<AuthResult> {
    return stubOrReal(
      () => {
        if (!destination.trim()) throw new Error('Enter your identifier to continue.');
        return { ok: true, message: 'Login successful' };
      },
      () => authService.login(channel, destination),
    );
  },

  async forgotPassword(channel: AuthChannel, destination: string): Promise<AuthResult> {
    return stubOrReal(
      () => {
        if (channel === 'email' && !isValidEmail(destination)) throw new Error('Enter a valid email address.');
        if (channel === 'phone' && !isValidPhone(destination)) throw new Error('Enter a valid phone number.');
        return { ok: true, message: 'Recovery instructions sent' };
      },
      () => authService.forgotPassword(channel, destination),
    );
  },

  async socialLogin(provider: 'google' | 'apple' | 'facebook'): Promise<AuthResult> {
    return stubOrReal(
      () => {
        throw new Error('Social login is not enabled yet.');
      },
      () => authService.socialLogin(provider),
    );
  },
};

export type { AuthChannel, AuthResult, SendOtpResult };
