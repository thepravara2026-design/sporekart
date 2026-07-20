import * as Sentry from '@sentry/react';
import { useEnv } from '../config/env';

let initialized = false;

export function initSentry(): void {
  if (initialized) return;

  const env = useEnv();
  if (!env.sentryDsn) {
    console.info('[sentry] Skipping Sentry init — no DSN configured');
    return;
  }

  Sentry.init({
    dsn: env.sentryDsn,
    environment: env.sentryEnvironment,
    tracesSampleRate: env.sentryTracesSampleRate,
    integrations: [Sentry.browserTracingIntegration(), Sentry.replayIntegration()],
    replaysSessionSampleRate: 0.1,
    replaysOnErrorSampleRate: 1.0,
  });

  initialized = true;
  console.info('[sentry] Initialized for environment:', env.sentryEnvironment);
}

export function captureError(error: unknown, context?: Record<string, unknown>): void {
  if (!initialized) {
    console.error('[sentry] Error captured (Sentry not initialized):', error, context);
    return;
  }
  Sentry.captureException(error, { extra: context });
}

export function captureMessage(message: string, level: Sentry.SeverityLevel = 'info'): void {
  if (!initialized) {
    console.info('[sentry] Message captured (Sentry not initialized):', message);
    return;
  }
  Sentry.captureMessage(message, level);
}

export function setUser(userId: string, email?: string): void {
  if (!initialized) return;
  Sentry.setUser({ id: userId, email });
}

export function clearUser(): void {
  if (!initialized) return;
  Sentry.setUser(null);
}
