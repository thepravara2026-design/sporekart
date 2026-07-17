/* ==========================================================================
   SporeKart — Authentication client (UX stub)
   --------------------------------------------------------------------------
   IMPORTANT: This module is a UI-ONLY stub for the Authentication Experience.
   It simulates network latency and outcomes so the screens, flows, validation,
   loading and error states can be built and reviewed end-to-end.

   It MUST be replaced by the real Supabase Auth + RBAC integration. No backend
   logic, OTP logic, or session handling is implemented or modified here. The
   real implementation belongs in the platform auth service; this file only
   satisfies the "premium experience" surface described in Sprint 21 Part 7.
   ========================================================================== */

export type AuthChannel = 'phone' | 'email';

export interface SendOtpResult {
  channel: AuthChannel;
  destination: string;
  expiresInSeconds: number;
  resendInSeconds: number;
}

export interface AuthResult {
  ok: boolean;
  message?: string;
}

const LATENCY = 900;

function delay<T>(value: T, ms = LATENCY): Promise<T> {
  return new Promise((resolve) => setTimeout(() => resolve(value), ms));
}

function isValidPhone(value: string): boolean {
  return /^[+]?[\d\s()-]{8,15}$/.test(value.trim());
}

function isValidEmail(value: string): boolean {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim());
}

export const authClient = {
  /** Request an OTP to the given channel (phone or email). */
  async sendOtp(channel: AuthChannel, destination: string): Promise<SendOtpResult> {
    if (channel === 'phone' && !isValidPhone(destination)) {
      return delay(
        { channel, destination, expiresInSeconds: 0, resendInSeconds: 0 },
        LATENCY,
      ).then(() => {
        throw new Error('Please enter a valid phone number.');
      });
    }
    if (channel === 'email' && !isValidEmail(destination)) {
      return delay(
        { channel, destination, expiresInSeconds: 0, resendInSeconds: 0 },
        LATENCY,
      ).then(() => {
        throw new Error('Please enter a valid email address.');
      });
    }
    return delay({
      channel,
      destination,
      expiresInSeconds: 300,
      resendInSeconds: 30,
    });
  },

  /** Verify the OTP code. */
  async verifyOtp(_channel: AuthChannel, _destination: string, code: string): Promise<AuthResult> {
    if (code.length < 4) {
      return delay({ ok: false, message: 'Enter the full code.' }).then(() => {
        throw new Error('Enter the full code.');
      });
    }
    // UX stub: a real OTP check is performed by the platform auth service. To
    // avoid the previous "any code is accepted" behaviour (BUG-SEC-011), the
    // stub now requires a specific demo PIN so the flow cannot be bypassed
    // with an arbitrary value. This remains a front-end simulation only.
    if (code !== '123456') {
      return delay({ ok: false, message: 'Incorrect code. Please try again.' }).then(() => {
        throw new Error('Incorrect code. Please try again.');
      });
    }
    return delay({ ok: true, message: 'Verified' });
  },

  /** Complete registration with collected profile. */
  async register(payload: {
    fullName: string;
    phone: string;
    email?: string;
    role?: string;
  }): Promise<AuthResult> {
    if (!payload.fullName.trim()) {
      return delay({ ok: false }).then(() => {
        throw new Error('Full name is required.');
      });
    }
    return delay({ ok: true, message: 'Registration submitted' });
  },

  /** Passwordless / OTP login. */
  async login(_channel: AuthChannel, destination: string): Promise<AuthResult> {
    if (!destination.trim()) {
      return delay({ ok: false }).then(() => {
        throw new Error('Enter your identifier to continue.');
      });
    }
    return delay({ ok: true, message: 'Login successful' });
  },

  /** Request a password / recovery reset link. */
  async forgotPassword(channel: AuthChannel, destination: string): Promise<AuthResult> {
    if (channel === 'email' && !isValidEmail(destination)) {
      return delay({ ok: false }).then(() => {
        throw new Error('Enter a valid email address.');
      });
    }
    if (channel === 'phone' && !isValidPhone(destination)) {
      return delay({ ok: false }).then(() => {
        throw new Error('Enter a valid phone number.');
      });
    }
    return delay({ ok: true, message: 'Recovery instructions sent' });
  },

  /** Placeholder for future social providers (Google / Apple / Facebook). */
  async socialLogin(_provider: 'google' | 'apple' | 'facebook'): Promise<AuthResult> {
    return delay({ ok: false, message: 'Social login is not enabled yet.' }).then(() => {
      throw new Error('Social login is not enabled yet.');
    });
  },
};
