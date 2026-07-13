import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Input } from '../../../design-system/components/core/Input';
import { Checkbox } from '../../../design-system/components/core/Checkbox';
import { Button } from '../../../design-system/components/core/Button';
import { SuccessAlert } from '../../../design-system/components/feedback/SuccessAlert';
import { ErrorAlert } from '../../../design-system/components/feedback/ErrorAlert';
import { Icon } from '../../../design-system/icons/Icon';
import { CONTACT_CATEGORIES } from '../business-info';

interface FormState {
  name: string;
  email: string;
  phone: string;
  category: string;
  subject: string;
  message: string;
  consent: boolean;
}

type Errors = Partial<Record<keyof FormState, string>>;
type Status = 'idle' | 'loading' | 'success' | 'error';

const TEXTAREA_STYLE: React.CSSProperties = {
  width: '100%',
  minHeight: 140,
  padding: 'var(--space-3, 12px) var(--space-4, 16px)',
  borderRadius: 'var(--radius-md, 8px)',
  border: '1px solid var(--color-border-default, #e5e7eb)',
  backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
  fontSize: 'var(--text-body-md, 16px)',
  color: 'var(--color-text-primary, #1f2933)',
  fontFamily: 'inherit',
  lineHeight: 1.6,
  resize: 'vertical',
};

const SELECT_STYLE: React.CSSProperties = {
  width: '100%',
  padding: 'var(--space-3, 12px) var(--space-4, 16px)',
  borderRadius: 'var(--radius-md, 8px)',
  border: '1px solid var(--color-border-default, #e5e7eb)',
  backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
  fontSize: 'var(--text-body-md, 16px)',
  color: 'var(--color-text-primary, #1f2933)',
  fontFamily: 'inherit',
  cursor: 'pointer',
};

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function ContactForm() {
  const [params] = useSearchParams();
  const subjectParam = params.get('subject') ?? '';

  const [form, setForm] = useState<FormState>({
    name: '',
    email: '',
    phone: '',
    category: 'general',
    subject: subjectParam,
    message: '',
    consent: false,
  });
  const [errors, setErrors] = useState<Errors>({});
  const [status, setStatus] = useState<Status>('idle');

  const update = <K extends keyof FormState>(key: K, value: FormState[K]) => {
    setForm((f) => ({ ...f, [key]: value }));
    if (errors[key]) setErrors((e) => ({ ...e, [key]: undefined }));
  };

  const validate = (): Errors => {
    const e: Errors = {};
    if (!form.name.trim()) e.name = 'Please enter your name.';
    if (!form.email.trim()) e.email = 'Please enter your email.';
    else if (!EMAIL_RE.test(form.email)) e.email = 'Enter a valid email address.';
    if (form.phone.trim() && !/^[+\d][\d\s-]{6,}$/.test(form.phone)) e.phone = 'Enter a valid phone number.';
    if (!form.message.trim()) e.message = 'Please enter a message.';
    else if (form.message.trim().length < 10) e.message = 'Message is too short.';
    if (!form.consent) e.consent = 'Please accept the consent terms.';
    return e;
  };

  const handleSubmit = (ev: React.FormEvent) => {
    ev.preventDefault();
    const e = validate();
    setErrors(e);
    if (Object.keys(e).length > 0) {
      setStatus('idle');
      return;
    }
    setStatus('loading');
    // Placeholder submit — backend integration ready (replace with API call).
    window.setTimeout(() => {
      setStatus('success');
    }, 900);
  };

  return (
    <form onSubmit={handleSubmit} noValidate style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}>
      {status === 'success' && (
        <SuccessAlert title="Message received" message="Thanks! This is a demo form — no message was actually sent. Our team typically replies within 1–2 business days." />
      )}
      {status === 'error' && (
        <ErrorAlert title="Couldn’t send" message="Something went wrong. Please try again or email us directly." />
      )}

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-4, 16px)' }}>
        <Input
          label="Name"
          required
          value={form.name}
          onChange={(e) => update('name', e.target.value)}
          error={errors.name}
          placeholder="Your name"
          fullWidth
        />
        <Input
          label="Email"
          type="email"
          required
          value={form.email}
          onChange={(e) => update('email', e.target.value)}
          error={errors.email}
          placeholder="you@farm.example"
          fullWidth
        />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-4, 16px)' }}>
        <Input
          label="Phone"
          type="tel"
          value={form.phone}
          onChange={(e) => update('phone', e.target.value)}
          error={errors.phone}
          placeholder="+91 …"
          helperText="Optional"
          fullWidth
        />
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
          <label style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>Category</label>
          <select
            value={form.category}
            onChange={(e) => update('category', e.target.value)}
            style={SELECT_STYLE}
            aria-label="Enquiry category"
          >
            {CONTACT_CATEGORIES.map((c) => (
              <option key={c.value} value={c.value}>{c.label}</option>
            ))}
          </select>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
        <label style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>Subject</label>
        <input
          type="text"
          value={form.subject}
          onChange={(e) => update('subject', e.target.value)}
          placeholder="How can we help?"
          style={SELECT_STYLE}
          aria-label="Subject"
        />
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
        <label style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>Message</label>
        <textarea
          value={form.message}
          onChange={(e) => update('message', e.target.value)}
          style={{ ...TEXTAREA_STYLE, borderColor: errors.message ? 'var(--color-border-error, #dc2626)' : undefined }}
          aria-invalid={!!errors.message}
          aria-describedby={errors.message ? 'msg-err' : undefined}
          placeholder="Tell us about your enquiry…"
        />
        {errors.message && (
          <span id="msg-err" role="alert" style={{ fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-error, #dc2626)' }}>{errors.message}</span>
        )}
      </div>

      <Checkbox
        label="I agree to be contacted by SporeKart about this enquiry and accept the Privacy Policy."
        checked={form.consent}
        onChange={(e) => update('consent', (e.target as HTMLInputElement).checked)}
        error={!!errors.consent}
      />
      {errors.consent && (
        <span role="alert" style={{ fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-error, #dc2626)', marginTop: '-var(--space-3, 12px)' }}>{errors.consent}</span>
      )}

      {/* Spam protection placeholder (honeypot) */}
      <div aria-hidden="true" style={{ position: 'absolute', left: -9999, width: 1, height: 1, overflow: 'hidden' }}>
        <label>Leave this empty<input type="text" tabIndex={-1} autoComplete="off" /></label>
      </div>
      <p style={{ margin: 0, fontSize: 'var(--text-body-xs, 12px)', color: 'var(--color-text-muted, #9ca3af)' }}>
        Spam protection enabled (placeholder). Backend integration ready.
      </p>

      <div>
        <Button type="submit" size="lg" loading={status === 'loading'} rightIcon={status === 'loading' ? undefined : <Icon name="arrow-right" size={18} aria-label="Send" />}>
          {status === 'loading' ? 'Sending…' : 'Send message'}
        </Button>
      </div>
    </form>
  );
}
