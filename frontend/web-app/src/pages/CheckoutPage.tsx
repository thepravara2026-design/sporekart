import { useState, useRef } from 'react';
import { useCart } from '../features/cart/CartContext';
import { processPayment } from '../features/payment/PaymentGateway';
import { logger } from '../lib/logger';
import type { CartItem } from '../features/cart/types';

interface OrderRecord {
  id: string;
  items: CartItem[];
  total: number;
  paymentId: string;
  status: 'confirmed';
  createdAt: string;
}

const ORDERS_KEY = 'sk_orders';
const IDEMPOTENCY_KEY = 'sk_checkout_idempotency';

function generateIdempotencyKey(): string {
  return `ck_${Date.now()}_${Math.random().toString(36).substring(2, 10)}`;
}

function loadUsedKeys(): Set<string> {
  try {
    const stored = sessionStorage.getItem(IDEMPOTENCY_KEY);
    if (stored) return new Set(JSON.parse(stored));
  } catch {}
  return new Set();
}

function markKeyUsed(key: string): void {
  try {
    sessionStorage.setItem(IDEMPOTENCY_KEY, JSON.stringify([...loadUsedKeys(), key]));
  } catch {}
}

function saveOrder(order: OrderRecord): void {
  try {
    const stored = localStorage.getItem(ORDERS_KEY);
    const orders: OrderRecord[] = stored ? JSON.parse(stored) : [];
    orders.push(order);
    localStorage.setItem(ORDERS_KEY, JSON.stringify(orders));
  } catch {}
}

export default function CheckoutPage() {
  const { state, count, total, clearCart } = useCart();

  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const [paymentId, setPaymentId] = useState<string | null>(null);
  const [orderId, setOrderId] = useState<string | null>(null);
  const idempotencyKey = useRef<string | null>(null);

  const [form, setForm] = useState({
    email: '',
    fullName: '',
    addressLine1: '',
    addressCity: '',
    addressState: '',
    addressPostalCode: '',
    addressCountry: 'US',
  });

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (state.items.length === 0) return;

    const key = generateIdempotencyKey();
    const used = loadUsedKeys();
    if (idempotencyKey.current && used.has(idempotencyKey.current)) {
      setError('This checkout attempt has already been processed.');
      return;
    }
    idempotencyKey.current = key;
    markKeyUsed(key);

    setProcessing(true);
    setError(null);

    try {
      const result = await processPayment(total, 'USD');
      if (result.success && result.paymentIntentId) {
        logger.info('[checkout] Payment succeeded:', { id: result.paymentIntentId });
        const order: OrderRecord = {
          id: `ord_${Date.now()}_${Math.random().toString(36).substring(2, 6)}`,
          items: [...state.items],
          total,
          paymentId: result.paymentIntentId,
          status: 'confirmed',
          createdAt: new Date().toISOString(),
        };
        saveOrder(order);
        setPaymentId(result.paymentIntentId);
        setOrderId(order.id);
        setSuccess(true);
        clearCart();
      } else {
        setError(result.error || 'Payment failed. Please try again.');
      }
    } catch (err) {
      const msg = err instanceof Error ? err.message : 'An unexpected error occurred';
      logger.error('[checkout] Checkout error:', err);
      setError(msg);
    } finally {
      setProcessing(false);
    }
  }

  if (success) {
    return (
      <div style={{ maxWidth: 600, margin: '0 auto', padding: 48, textAlign: 'center' }}>
        <h1 style={{ fontSize: 24, fontWeight: 700, marginBottom: 16 }}>Order Confirmed!</h1>
        <p style={{ marginBottom: 8 }}>Thank you for your purchase.</p>
        {orderId && (
          <p style={{ fontSize: 14, color: 'var(--color-text-secondary)', marginBottom: 4 }}>
            Order ID: {orderId}
          </p>
        )}
        {paymentId && (
          <p style={{ fontSize: 14, color: 'var(--color-text-secondary)', marginBottom: 24 }}>
            Payment ID: {paymentId}
          </p>
        )}
        <a href="/" style={{ color: 'var(--color-primary)' }}>Continue shopping</a>
      </div>
    );
  }

  if (state.items.length === 0 && !success) {
    return (
      <div style={{ maxWidth: 600, margin: '0 auto', padding: 48, textAlign: 'center' }}>
        <h1 style={{ fontSize: 24, fontWeight: 700, marginBottom: 16 }}>Your cart is empty</h1>
        <a href="/products" style={{ color: 'var(--color-primary)' }}>Browse products</a>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 800, margin: '0 auto', padding: '24px 16px' }}>
      <h1 style={{ margin: '0 0 8px', fontSize: 24, fontWeight: 700 }}>Checkout</h1>
      <p style={{ margin: '0 0 24px', color: 'var(--color-text-secondary)' }}>
        {count} item{count !== 1 ? 's' : ''} · Total ${total.toFixed(2)}
      </p>

      {error && (
        <div style={{ padding: 12, marginBottom: 16, background: 'var(--color-danger-bg)', color: 'var(--color-danger)', borderRadius: 6 }}>{error}</div>
      )}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>
            Full Name
          </label>
          <input
            type="text"
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
            required
            style={{
              width: '100%',
              padding: '10px 12px',
              border: '1px solid var(--color-border-default)',
              borderRadius: 6,
              fontSize: 14,
            }}
          />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>
            Email
          </label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
            style={{
              width: '100%',
              padding: '10px 12px',
              border: '1px solid var(--color-border-default)',
              borderRadius: 6,
              fontSize: 14,
            }}
          />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>
            Address Line 1
          </label>
          <input
            type="text"
            name="addressLine1"
            value={form.addressLine1}
            onChange={handleChange}
            required
            style={{
              width: '100%',
              padding: '10px 12px',
              border: '1px solid var(--color-border-default)',
              borderRadius: 6,
              fontSize: 14,
            }}
          />
        </div>
        <div style={{ display: 'flex', gap: 12 }}>
          <div style={{ flex: 1 }}>
            <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>City</label>
            <input
              type="text"
              name="addressCity"
              value={form.addressCity}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 6,
                fontSize: 14,
              }}
            />
          </div>
          <div style={{ flex: 1 }}>
            <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>State</label>
            <input
              type="text"
              name="addressState"
              value={form.addressState}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 6,
                fontSize: 14,
              }}
            />
          </div>
        </div>
        <div style={{ display: 'flex', gap: 12 }}>
          <div style={{ flex: 1 }}>
            <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>Postal Code</label>
            <input
              type="text"
              name="addressPostalCode"
              value={form.addressPostalCode}
              onChange={handleChange}
              required
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 6,
                fontSize: 14,
              }}
            />
          </div>
          <div style={{ flex: 1 }}>
            <label style={{ display: 'block', marginBottom: 4, fontWeight: 600, fontSize: 14 }}>Country</label>
            <select
              name="addressCountry"
              value={form.addressCountry}
              onChange={handleChange}
              style={{
                width: '100%',
                padding: '10px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 6,
                fontSize: 14,
              }}
            >
              <option value="US">United States</option>
              <option value="CA">Canada</option>
              <option value="GB">United Kingdom</option>
              <option value="AU">Australia</option>
              <option value="IN">India</option>
            </select>
          </div>
        </div>

        <div style={{ padding: 16, background: 'var(--color-bg-surface-raised)', borderRadius: 8, border: '1px solid var(--color-border-weak)' }}>
          <h3 style={{ margin: '0 0 8px', fontSize: 16 }}>Order Summary</h3>
          {state.items.map((item) => (
            <div key={item.id} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 14, marginBottom: 4 }}>
              <span>{item.name} × {item.quantity}</span>
              <span>${(item.price * item.quantity).toFixed(2)}</span>
            </div>
          ))}
          <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 700, fontSize: 16, marginTop: 8, paddingTop: 8, borderTop: '1px solid var(--color-border-weak)' }}>
            <span>Total</span>
            <span>${total.toFixed(2)}</span>
          </div>
        </div>

        <button
          type="submit"
          disabled={processing}
          style={{
            padding: '14px',
            background: processing ? 'var(--color-bg-disabled)' : 'var(--color-primary)',
            color: processing ? 'var(--color-text-disabled)' : '#fff',
            border: 'none',
            borderRadius: 6,
            fontSize: 16,
            fontWeight: 700,
            cursor: processing ? 'not-allowed' : 'pointer',
          }}
        >
          {processing ? 'Processing payment...' : `Pay $${total.toFixed(2)}`}
        </button>
      </form>
    </div>
  );
}
