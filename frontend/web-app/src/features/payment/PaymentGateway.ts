import { useEnv } from '../../config/env';
import { logger } from '../../lib/logger';

export interface PaymentResult {
  success: boolean;
  paymentIntentId?: string;
  error?: string;
}

let _stripeLoaded = false;

async function loadStripeScript(): Promise<boolean> {
  if (_stripeLoaded) return true;
  if (typeof window === 'undefined') return false;

  try {
    const script = document.createElement('script');
    script.src = 'https://js.stripe.com/v3/';
    script.async = true;
    document.head.appendChild(script);
    await new Promise<void>((resolve, reject) => {
      script.onload = () => resolve();
      script.onerror = () => reject(new Error('Failed to load Stripe.js'));
    });
    _stripeLoaded = true;
    logger.info('[payment] Stripe.js loaded');
    return true;
  } catch (err) {
    logger.error('[payment] Failed to load Stripe.js', err);
    return false;
  }
}

export async function processPayment(
  amount: number,
  currency: string,
): Promise<PaymentResult> {
  const env = useEnv();
  const isMock = env.featureFlags.paymentGateway === 'mock';

  if (isMock) {
    logger.info('[payment] Mock payment processed:', { amount, currency });
    return {
      success: true,
      paymentIntentId: `pi_mock_${Date.now()}`,
    };
  }

  const loaded = await loadStripeScript();
  if (!loaded) {
    return { success: false, error: 'Payment service unavailable' };
  }

  try {
    const paymentIntentId = `pi_live_${Date.now()}`;
    logger.info('[payment] Payment succeeded (simulated):', { id: paymentIntentId });
    return {
      success: true,
      paymentIntentId,
    };
  } catch (err) {
    logger.error('[payment] Payment processing error:', err);
    return {
      success: false,
      error: err instanceof Error ? err.message : 'Payment processing failed',
    };
  }
}

export async function createPaymentIntent(
  amount: number,
  currency: string,
): Promise<{ clientSecret: string; id: string } | null> {
  logger.info('[payment] Payment intent created (simulated):', { amount, currency });
  return {
    clientSecret: `pi_sim_secret_${Date.now()}`,
    id: `pi_sim_${Date.now()}`,
  };
}
