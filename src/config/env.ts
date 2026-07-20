/* ==========================================================================
   SporeKart — Environment Configuration Validation
   Sprint E (Production Hardening) — P0-04
   ========================================================================== */

export interface EnvConfig {
  mode: string;
  logLevel: string;
  supabaseUrl: string;
  supabaseAnonKey: string;
  apiBaseUrl: string;
  stripePublishableKey: string;
  sentryDsn: string;
  sentryEnvironment: string;
  sentryTracesSampleRate: number;
  featureFlags: {
    mockMode: boolean;
    productionMode: boolean;
    paymentGateway: string;
    shippingProvider: string;
    emailProvider: string;
    smsProvider: string;
    storageProvider: string;
    authProvider: string;
  };
}

const MISSING_REQUIRED: string[] = [];

function str(key: string, fallback = ''): string {
  const val = (import.meta as Record<string, any>).env?.[key];
  if (val === undefined || val === '') {
    return fallback;
  }
  return val as string;
}

function bool(key: string, fallback = false): boolean {
  const val = (import.meta as Record<string, any>).env?.[key];
  if (val === undefined) return fallback;
  return val === 'true' || val === '1';
}

function num(key: string, fallback = 0): number {
  const val = (import.meta as Record<string, any>).env?.[key];
  if (val === undefined) return fallback;
  const n = Number(val);
  return Number.isNaN(n) ? fallback : n;
}

export function getEnv(): EnvConfig {
  const mode = str('MODE', 'development');
  const isProduction = mode === 'production';

  const supabaseUrl = str('VITE_SUPABASE_URL');
  const supabaseAnonKey = str('VITE_SUPABASE_ANON_KEY');
  const sentryDsn = str('VITE_SENTRY_DSN');

  if (isProduction) {
    if (!supabaseUrl) MISSING_REQUIRED.push('VITE_SUPABASE_URL');
    if (!supabaseAnonKey) MISSING_REQUIRED.push('VITE_SUPABASE_ANON_KEY');
    if (!sentryDsn) MISSING_REQUIRED.push('VITE_SENTRY_DSN');
  }

  return {
    mode,
    logLevel: str('VITE_LOG_LEVEL', isProduction ? 'warn' : 'debug'),
    supabaseUrl,
    supabaseAnonKey,
    apiBaseUrl: str('VITE_API_BASE_URL', 'http://localhost:5173'),
    stripePublishableKey: str('VITE_STRIPE_PUBLISHABLE_KEY', ''),
    sentryDsn,
    sentryEnvironment: str('VITE_SENTRY_ENVIRONMENT', mode),
    sentryTracesSampleRate: num('VITE_SENTRY_TRACES_SAMPLE_RATE', isProduction ? 0.1 : 1.0),
    featureFlags: {
      mockMode: bool('VITE_FF_MOCK_MODE', !isProduction),
      productionMode: bool('VITE_FF_PRODUCTION_MODE', isProduction),
      paymentGateway: str('VITE_FF_PAYMENT_GATEWAY', isProduction ? 'stripe' : 'mock'),
      shippingProvider: str('VITE_FF_SHIPPING_PROVIDER', isProduction ? 'shiprocket' : 'mock'),
      emailProvider: str('VITE_FF_EMAIL_PROVIDER', isProduction ? 'sendgrid' : 'mock'),
      smsProvider: str('VITE_FF_SMS_PROVIDER', isProduction ? 'twilio' : 'mock'),
      storageProvider: str('VITE_FF_STORAGE_PROVIDER', isProduction ? 's3' : 'local'),
      authProvider: str('VITE_FF_AUTH_PROVIDER', isProduction ? 'supabase' : 'mock'),
    },
  };
}

export function validateEnv(): { valid: boolean; errors: string[] } {
  getEnv();
  return {
    valid: MISSING_REQUIRED.length === 0,
    errors: [...MISSING_REQUIRED],
  };
}

let _env: EnvConfig | null = null;

export function useEnv(): EnvConfig {
  if (!_env) {
    _env = getEnv();
  }
  return _env;
}
