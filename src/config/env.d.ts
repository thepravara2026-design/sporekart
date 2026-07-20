/* ==========================================================================
   SporeKart — Vite Environment Variable Type Augmentation
   Sprint E (Production Hardening) — P0-04
   ========================================================================== */

/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_SUPABASE_URL: string;
  readonly VITE_SUPABASE_ANON_KEY: string;
  readonly VITE_API_BASE_URL: string;
  readonly VITE_STRIPE_PUBLISHABLE_KEY: string;
  readonly VITE_SENTRY_DSN: string;
  readonly VITE_SENTRY_ENVIRONMENT: string;
  readonly VITE_SENTRY_TRACES_SAMPLE_RATE: string;
  readonly VITE_LOG_LEVEL: string;
  readonly VITE_FF_MOCK_MODE: string;
  readonly VITE_FF_PRODUCTION_MODE: string;
  readonly VITE_FF_PAYMENT_GATEWAY: string;
  readonly VITE_FF_SHIPPING_PROVIDER: string;
  readonly VITE_FF_EMAIL_PROVIDER: string;
  readonly VITE_FF_SMS_PROVIDER: string;
  readonly VITE_FF_STORAGE_PROVIDER: string;
  readonly VITE_FF_AUTH_PROVIDER: string;
  readonly VITE_IDENTITY_SERVICE: string;
  readonly VITE_CATALOG_SERVICE: string;
  readonly VITE_CART_SERVICE: string;
  readonly VITE_ORDER_SERVICE: string;
  readonly VITE_PAYMENT_SERVICE: string;
  readonly VITE_NOTIFICATION_SERVICE: string;
  readonly VITE_INVENTORY_SERVICE: string;
  readonly VITE_FULFILLMENT_SERVICE: string;
  readonly VITE_SEARCH_SERVICE: string;
  readonly VITE_ANALYTICS_SERVICE: string;
  readonly VITE_AI_SERVICE: string;
  readonly VITE_CONTENT_SERVICE: string;
  readonly VITE_RISK_SERVICE: string;
  readonly VITE_SUPPORT_SERVICE: string;
  readonly VITE_ADMIN_SERVICE: string;
  readonly VITE_TRAINING_SERVICE: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
