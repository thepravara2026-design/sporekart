import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthLayout from '../AuthLayout';
import AuthAlert from '../components/AuthAlert';
import { Button } from '../../../design-system/components/core/Button';
import { Input } from '../../../design-system/components/core/Input';
import { Checkbox } from '../../../design-system/components/core/Checkbox';
import { Select } from '../../../design-system/components/composite/Select';
import { Icon } from '../../../design-system/icons/Icon';
import { authClient } from '../authClient';
import '../auth.css';

type Status = 'idle' | 'loading' | 'error' | 'success';

export default function RegisterPage() {
  const navigate = useNavigate();
  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('customer');
  const [consent, setConsent] = useState(false);
  const [privacy, setPrivacy] = useState(false);
  const [status, setStatus] = useState<Status>('idle');
  const [error, setError] = useState('');
  const [errors, setErrors] = useState<Record<string, string>>({});
  const firstRef = useRef<HTMLInputElement>(null);

  const validate = (): boolean => {
    const next: Record<string, string> = {};
    if (!fullName.trim()) next.fullName = 'Full name is required.';
    if (!/^[+]?[\d\s()-]{8,15}$/.test(phone.trim())) next.phone = 'Enter a valid phone number.';
    if (email.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim()))
      next.email = 'Enter a valid email address.';
    if (!consent) next.consent = 'Please confirm the consent statement.';
    if (!privacy) next.privacy = 'You must accept the Privacy Policy.';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    if (!validate()) {
      firstRef.current?.focus();
      return;
    }
    setStatus('loading');
    try {
      await authClient.sendOtp('phone', phone.trim());
      setStatus('success');
      navigate('/verify-otp', {
        state: {
          channel: 'phone',
          destination: phone.trim(),
          flow: 'register',
          profile: { fullName: fullName.trim(), phone: phone.trim(), email: email.trim() || undefined, role },
        },
      });
    } catch (err) {
      setStatus('error');
      setError(err instanceof Error ? err.message : 'Could not start registration.');
    }
  };

  return (
    <AuthLayout brandHeading="Join SporeKart." brandSubheading="Create your account to order cultivars, enrol in training and track every shipment.">
      <header className="auth-card__header">
        <span className="auth-card__eyebrow">
          <Icon name="user-plus" size={14} color="currentColor" /> Create account
        </span>
        <h2 className="auth-card__title">Set up your profile</h2>
        <p className="auth-card__subtitle">
          We’ll send a one-time code to verify your phone. It takes under a minute.
        </p>
      </header>

      <form onSubmit={handleSubmit} noValidate>
        <div className="auth-fields">
          <Input
            ref={firstRef}
            label="Full name"
            placeholder="Jane Growell"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            error={errors.fullName}
            prefix={<Icon name="user" size={18} color="currentColor" />}
            autoComplete="name"
            fullWidth
            required
          />
          <Input
            label="Phone number"
            type="tel"
            placeholder="+1 555 000 1234"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            error={errors.phone}
            prefix={<Icon name="smartphone" size={18} color="currentColor" />}
            autoComplete="tel"
            fullWidth
            required
          />
          <Input
            label="Email (optional)"
            type="email"
            placeholder="you@company.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={errors.email}
            prefix={<Icon name="mail" size={18} color="currentColor" />}
            autoComplete="email"
            fullWidth
          />
          <Select
            label="I am registering as"
            options={[
              { value: 'customer', label: 'Customer' },
              { value: 'grower', label: 'Grower (invite only)', disabled: true },
              { value: 'partner', label: 'Partner (invite only)', disabled: true },
            ]}
            value={role}
            onChange={setRole}
            helperText="Customers can self-register. Other roles are provisioned by administrators."
            fullWidth
          />

          <Checkbox
            label="I consent to receive verification codes and service communications on this number."
            checked={consent}
            onChange={(e) => setConsent(e.target.checked)}
            error={errors.consent ? true : undefined}
          />
          {errors.consent && (
            <AuthAlert type="error">{errors.consent}</AuthAlert>
          )}
          <Checkbox
            label="I have read and accept the Privacy Policy."
            checked={privacy}
            onChange={(e) => setPrivacy(e.target.checked)}
            error={errors.privacy ? true : undefined}
          />
          {errors.privacy && (
            <AuthAlert type="error">{errors.privacy}</AuthAlert>
          )}

          {status === 'error' && error && <AuthAlert type="error">{error}</AuthAlert>}

          <div className="auth-submit">
            <Button type="submit" size="lg" fullWidth loading={status === 'loading'}>
              {status === 'loading' ? 'Creating account…' : 'Create account'}
            </Button>
          </div>
        </div>
      </form>

      <p className="auth-alt">
        Already have an account? <a href="/login">Sign in</a>
      </p>
    </AuthLayout>
  );
}
