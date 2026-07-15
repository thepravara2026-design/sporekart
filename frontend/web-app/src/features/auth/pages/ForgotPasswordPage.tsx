import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthLayout from '../AuthLayout';
import AuthAlert from '../components/AuthAlert';
import { Button } from '../../../design-system/components/core/Button';
import { Input } from '../../../design-system/components/core/Input';
import { Icon } from '../../../design-system/icons/Icon';
import { authClient, type AuthChannel } from '../authClient';
import '../auth.css';

type Status = 'idle' | 'loading' | 'error' | 'success';

export default function ForgotPasswordPage() {
  const navigate = useNavigate();
  const [channel, setChannel] = useState<AuthChannel>('email');
  const [destination, setDestination] = useState('');
  const [status, setStatus] = useState<Status>('idle');
  const [error, setError] = useState('');
  const [fieldError, setFieldError] = useState('');
  const [sentTo, setSentTo] = useState('');
  const ref = useRef<HTMLInputElement>(null);

  const validate = (): string | null => {
    const v = destination.trim();
    if (!v) return 'Enter your ' + (channel === 'email' ? 'email' : 'phone') + '.';
    if (channel === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)) return 'Enter a valid email address.';
    if (channel === 'phone' && !/^[+]?[\d\s()-]{8,15}$/.test(v)) return 'Enter a valid phone number.';
    return null;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFieldError('');
    const local = validate();
    if (local) {
      setFieldError(local);
      ref.current?.focus();
      return;
    }
    setStatus('loading');
    setError('');
    try {
      await authClient.forgotPassword(channel, destination.trim());
      setSentTo(destination.trim());
      setStatus('success');
    } catch (err) {
      setStatus('error');
      setError(err instanceof Error ? err.message : 'Could not send recovery instructions.');
    }
  };

  return (
    <AuthLayout brandHeading="We’ve got you." brandSubheading="Recover access to your SporeKart workspace in a few simple steps." showFeatures={false}>
      <header className="auth-card__header">
        <span className="auth-card__eyebrow">
          <Icon name="key" size={14} color="currentColor" /> Account recovery
        </span>
        <h2 className="auth-card__title">Recover your access</h2>
        <p className="auth-card__subtitle">
          Enter where we should send recovery instructions. This screen is ready
          for the password-reset backend.
        </p>
      </header>

      {status === 'success' ? (
        <div className="auth-fields">
          <AuthAlert type="success">
            We sent recovery instructions to <strong>{sentTo}</strong>. Check your
            inbox or messages and follow the link to reset access.
          </AuthAlert>
          <div className="auth-status__actions" style={{ marginTop: 'var(--space-stack-lg)' }}>
            <Button variant="primary" size="lg" fullWidth onClick={() => navigate('/login')}>
              Back to sign in
            </Button>
            <button
              type="button"
              className="auth-otp__resend"
              onClick={() => {
                setStatus('idle');
                setDestination('');
                setSentTo('');
              }}
            >
              Use a different contact
            </button>
          </div>
        </div>
      ) : (
        <form onSubmit={handleSubmit} noValidate>
          <div className="auth-fields">
            <div role="radiogroup" aria-label="Recovery method" style={{ display: 'flex', gap: 'var(--space-inline-sm)' }}>
              {(['email', 'phone'] as AuthChannel[]).map((c) => (
                <button
                  key={c}
                  type="button"
                  role="radio"
                  aria-checked={channel === c}
                  onClick={() => {
                    setChannel(c);
                    setFieldError('');
                  }}
                  className="auth-social__btn"
                  style={{
                    flex: 1,
                    borderColor: channel === c ? 'var(--color-bg-primary-default)' : 'var(--color-border-default)',
                    color: channel === c ? 'var(--color-bg-primary-default)' : 'var(--color-text-primary)',
                    background: channel === c ? 'var(--color-bg-primary-weak)' : 'var(--color-bg-surface-default)',
                  }}
                >
                  <Icon name={c === 'phone' ? 'smartphone' : 'mail'} size={18} color="currentColor" />
                  {c === 'phone' ? 'Phone' : 'Email'}
                </button>
              ))}
            </div>

            <Input
              ref={ref}
              type={channel === 'email' ? 'email' : 'tel'}
              label={channel === 'email' ? 'Email address' : 'Phone number'}
              placeholder={channel === 'email' ? 'you@company.com' : '+1 555 000 1234'}
              value={destination}
              onChange={(e) => setDestination(e.target.value)}
              error={fieldError || undefined}
              prefix={<Icon name={channel === 'email' ? 'mail' : 'smartphone'} size={18} color="currentColor" />}
              autoComplete={channel === 'email' ? 'email' : 'tel'}
              fullWidth
              required
            />

            {status === 'error' && error && <AuthAlert type="error">{error}</AuthAlert>}

            <div className="auth-submit">
              <Button type="submit" size="lg" fullWidth loading={status === 'loading'}>
                {status === 'loading' ? 'Sending…' : 'Send recovery instructions'}
              </Button>
            </div>
          </div>
        </form>
      )}

      <p className="auth-alt">
        Remembered? <a href="/login">Back to sign in</a>
      </p>
    </AuthLayout>
  );
}
