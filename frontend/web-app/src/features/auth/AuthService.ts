import { useEnv } from '../../config/env';
import { logger } from '../../lib/logger';
import { signInWithOtp, verifyOtp as supabaseVerifyOtp, signOut, createMockSession } from '../../lib/supabase';
import type { AuthChannel, AuthResult, SendOtpResult } from './types';

export const authService = {
  async sendOtp(channel: AuthChannel, destination: string): Promise<SendOtpResult> {
    const env = useEnv();
    const isMock = env.featureFlags.authProvider === 'mock';
    if (isMock) {
      return {
        channel,
        destination,
        expiresInSeconds: 300,
        resendInSeconds: 30,
      };
    }
    await signInWithOtp(channel, destination);
    logger.info('[AuthService] OTP sent:', { channel, destination });
    return {
      channel,
      destination,
      expiresInSeconds: 300,
      resendInSeconds: 30,
    };
  },

  async verifyOtp(_channel: AuthChannel, _destination: string, code: string): Promise<AuthResult> {
    const env = useEnv();
    const isMock = env.featureFlags.authProvider === 'mock';
    if (isMock) {
      if (code !== '123456') {
        throw new Error('Incorrect code. Please try again.');
      }
      createMockSession(_destination, 'customer');
      return { ok: true, message: 'Verified' };
    }
    await supabaseVerifyOtp(_channel, _destination, code);
    return { ok: true, message: 'Verified' };
  },

  async register(payload: { fullName: string; phone: string; email?: string; role?: string }): Promise<AuthResult> {
    const env = useEnv();
    const isMock = env.featureFlags.authProvider === 'mock';
    if (isMock) {
      if (!payload.fullName.trim()) {
        throw new Error('Full name is required.');
      }
      return { ok: true, message: 'Registration submitted' };
    }
    if (!payload.email) {
      throw new Error('Email is required for registration.');
    }
    const m = await import('../../lib/supabase');
    await m.signUp(payload.email, `${payload.phone}_${Date.now()}`, {
      full_name: payload.fullName,
      phone: payload.phone,
      role: payload.role || 'customer',
    });
    logger.info('[AuthService] Registration successful:', { email: payload.email });
    return { ok: true, message: 'Registration submitted' };
  },

  async login(_channel: AuthChannel, destination: string): Promise<AuthResult> {
    const env = useEnv();
    const isMock = env.featureFlags.authProvider === 'mock';
    if (isMock) {
      if (!destination.trim()) {
        throw new Error('Enter your identifier to continue.');
      }
      return { ok: true, message: 'Login successful' };
    }
    await signInWithOtp(_channel, destination);
    return { ok: true, message: 'OTP sent' };
  },

  async forgotPassword(channel: AuthChannel, destination: string): Promise<AuthResult> {
    const env = useEnv();
    const isMock = env.featureFlags.authProvider === 'mock';
    if (isMock) {
      return { ok: true, message: 'Recovery instructions sent' };
    }
    const { getSupabaseClient } = await import('../../lib/supabase');
    const client = getSupabaseClient();
    if (!client) throw new Error('Supabase not configured');
    const { error } = await client.auth.resetPasswordForEmail(
      channel === 'email' ? destination : `${destination}@placeholder.com`,
    );
    if (error) throw error;
    return { ok: true, message: 'Recovery instructions sent' };
  },

  async socialLogin(_provider: 'google' | 'apple' | 'facebook'): Promise<AuthResult> {
    const env = useEnv();
    const isMock = env.featureFlags.authProvider === 'mock';
    if (isMock) {
      throw new Error('Social login is not enabled yet.');
    }
    const { getSupabaseClient } = await import('../../lib/supabase');
    const client = getSupabaseClient();
    if (!client) throw new Error('Supabase not configured');
    const { error } = await client.auth.signInWithOAuth({ provider: _provider });
    if (error) throw error;
    return { ok: true, message: 'Redirecting to provider' };
  },

  async signOut(): Promise<void> {
    await signOut();
  },
};
