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
