import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';

const PAYMENT_PROVIDERS = [
  { id: 'razorpay', label: 'Razorpay', icon: '\uD83D\uDCB3', note: 'Primary gateway adapter (interface only)' },
  { id: 'upi', label: 'UPI', icon: '\uD83D\uDCF1', note: 'UPI intent/collect flow (interface only)' },
  { id: 'netbanking', label: 'Net Banking', icon: '\uD83C\uDFE6', note: 'Bank redirect flow (interface only)' },
  { id: 'card', label: 'Credit / Debit Card', icon: '\uD83D\uDCB3', note: 'Card tokenization (interface only)' },
  { id: 'corporate', label: 'Corporate Billing', icon: '\uD83C\uDFE2', note: 'Invoice-based billing (interface only)' },
];

const COMMERCE_SERVICES = [
  { id: 'gst', label: 'GST Engine', note: 'Tax computation & compliance' },
  { id: 'invoice', label: 'Invoice Generator', note: 'GST-compliant invoices' },
  { id: 'coupons', label: 'Coupons', note: 'Discount code validation' },
  { id: 'promo', label: 'Promo Codes', note: 'Campaign promotions' },
  { id: 'scholarship', label: 'Scholarship Engine', note: 'Sponsored & waived fees' },
  { id: 'crm', label: 'CRM', note: 'Applicant relationship sync' },
  { id: 'erp', label: 'ERP', note: 'Finance & accounting sync' },
  { id: 'notify', label: 'Notification Service', note: 'Email / SMS / WhatsApp' },
];

interface InterfaceDef { name: string; methods: string[]; }

const INTERFACES: InterfaceDef[] = [
  { name: 'PaymentProvider', methods: ['createOrder(config)', 'capture(orderId)', 'refund(txnId, amount)', 'verifySignature(payload)', 'getStatus(txnId)'] },
  { name: 'TaxEngine', methods: ['computeGST(amount, hsn)', 'getBreakdown(order)', 'validateGSTIN(gstin)'] },
  { name: 'DiscountEngine', methods: ['applyCoupon(code, order)', 'applyScholarship(id, order)', 'validate(code)'] },
  { name: 'InvoiceService', methods: ['generate(order)', 'getPdfUrl(invoiceId)', 'void(invoiceId)'] },
  { name: 'NotificationChannel', methods: ['send(channel, template, payload)'] },
];

export function PaymentReadinessPanel() {
  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 'var(--space-2)', flexWrap: 'wrap' }}>
        <h2 style={{ margin: 0 }}>Payment & Commerce Readiness</h2>
        <Badge variant="warning" size="md">Interfaces only · Not implemented</Badge>
      </div>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        The platform is payment-provider agnostic. Below are the extension points prepared for future
        integration. No payment gateway, APIs, database or real transactions exist in this build.
      </p>

      <h3>Payment Providers</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-3)', marginBottom: 'var(--space-5)' }}>
        {PAYMENT_PROVIDERS.map((p) => (
          <Card key={p.id} variant="outlined" padding="md">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 24 }}>{p.icon}</span>
              <Badge variant="neutral" size="sm">planned</Badge>
            </div>
            <div style={{ fontWeight: 700, marginTop: 8 }}>{p.label}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{p.note}</div>
          </Card>
        ))}
      </div>

      <h3>Commerce & Integration Services</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 'var(--space-3)', marginBottom: 'var(--space-5)' }}>
        {COMMERCE_SERVICES.map((s) => (
          <Card key={s.id} variant="ghost" padding="sm">
            <div style={{ fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{s.label}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{s.note}</div>
          </Card>
        ))}
      </div>

      <h3>Extension Interfaces (design)</h3>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {INTERFACES.map((i) => (
          <Card key={i.name} variant="outlined" padding="sm">
            <div style={{ fontWeight: 700, fontFamily: 'var(--font-family-mono, monospace)' }}>interface {i.name}</div>
            <ul style={{ margin: '6px 0 0', paddingLeft: 18, color: 'var(--color-text-secondary)', fontSize: 12, lineHeight: 1.8 }}>
              {i.methods.map((m) => <li key={m}><code>{m}</code></li>)}
            </ul>
          </Card>
        ))}
      </div>
    </div>
  );
}
