import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthLayout from '../AuthLayout';
import SocialLogin from '../components/SocialLogin';
import AuthAlert from '../components/AuthAlert';
import { Button } from '../../../design-system/components/core/Button';
import { Input } from '../../../design-system/components/core/Input';
import { Checkbox } from '../../../design-system/components/core/Checkbox';
import { Icon } from '../../../design-system/icons/Icon';
import { authClient, type AuthChannel } from '../authClient';
import '../auth.css';

type Status = 'idle' | 'loading' | 'error' | 'success';

function validateIdentifier(channel: AuthChannel, value: string): string | null {
  const v = value.trim();
  if (!v) return 'Enter your ' + (channel === 'phone' ? 'phone number' : 'email address') + ' to continue.';
  if (channel === 'phone' && !/^[+]?[\d\s()-]{8,15}$/.test(v)) return 'Enter a valid phone number.';
  if (channel === 'email' && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)) return 'Enter a valid email address.';
  return null;
}

export default function LoginPage() {
  const navigate = useNavigate();
  const [channel, setChannel] = useState<AuthChannel>('phone');
  const [identifier, setIdentifier] = useState('');
  const [remember, setRemember] = useState(true);
  const [accepted, setAccepted] = useState(false);
  const [status, setStatus] = useState<Status>('idle');
  const [error, setError] = useState('');
  const [fieldError, setFieldError] = useState('');
  const [termsError, setTermsError] = useState('');
  const identifierRef = useRef<HTMLInputElement>(null);

  const switchChannel = (next: AuthChannel) => {
    if (next === channel) return;
    setChannel(next);
    setFieldError('');
    setError('');
    setIdentifier('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setTermsError('');
    setFieldError('');

    const local = validateIdentifier(channel, identifier);
    if (local) {
      setFieldError(local);
      identifierRef.current?.focus();
      return;
    }
    if (!accepted) {
      setTermsError('Please accept the Terms & Privacy Policy to continue.');
      return;
    }

    setStatus('loading');
    setError('');
    try {
      await authClient.sendOtp(channel, identifier.trim());
      setStatus('success');
      navigate('/verify-otp', {
        state: { channel, destination: identifier.trim(), remember, flow: 'login' },
      });
    } catch (err) {
      setStatus('error');
      setError(err instanceof Error ? err.message : 'Something went wrong. Please try again.');
    }
  };

  const handleSocial = (provider: 'google' | 'apple' | 'facebook') => {
    setError('');
    setStatus('error');
    authClient
      .socialLogin(provider)
      .catch((err) => setError(err instanceof Error ? err.message : 'Unavailable.'));
  };

  return (
    <AuthLayout brandHeading="Welcome back to SporeKart." brandSubheading="Sign in to manage cultivars, training and orders — securely and in seconds.">
      <header className="auth-card__header">
        <span className="auth-card__eyebrow">
          <Icon name="log-in" size={14} color="currentColor" /> Sign in
        </span>
        <h2 className="auth-card__title">Access your workspace</h2>
        <p className="auth-card__subtitle">
          Use your phone or email to receive a secure one-time code.
        </p>
      </header>

      <form onSubmit={handleSubmit} noValidate>
        <div className="auth-fields">
          <div
            role="radiogroup"
            aria-label="Sign in method"
            style={{ display: 'flex', gap: 'var(--space-inline-sm)' }}
          >
            {(['phone', 'email'] as AuthChannel[]).map((c) => (
              <button
                key={c}
                type="button"
                role="radio"
                aria-checked={channel === c}
                onClick={() => switchChannel(c)}
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
            ref={identifierRef}
            type={channel === 'phone' ? 'tel' : 'email'}
            label={channel === 'phone' ? 'Phone number' : 'Email address'}
            placeholder={channel === 'phone' ? '+1 555 000 1234' : 'you@company.com'}
            value={identifier}
            onChange={(e) => setIdentifier(e.target.value)}
            error={fieldError || undefined}
            prefix={<Icon name={channel === 'phone' ? 'smartphone' : 'mail'} size={18} color="currentColor" />}
            autoComplete={channel === 'phone' ? 'tel' : 'email'}
            fullWidth
            required
          />

          <div className="auth-row">
            <Checkbox
              label="Remember me"
              checked={remember}
              onChange={(e) => setRemember(e.target.checked)}
            />
            <a className="auth-link" href="/forgot-password">
              Forgot access?
            </a>
          </div>

          <Checkbox
            label="I agree to the Terms of Service and Privacy Policy"
            checked={accepted}
            onChange={(e) => setAccepted(e.target.checked)}
            error={termsError ? true : undefined}
          />

          {status === 'error' && error && <AuthAlert type="error">{error}</AuthAlert>}

          <div className="auth-submit">
            <Button type="submit" size="lg" fullWidth loading={status === 'loading'}>
              {status === 'loading' ? 'Sending code…' : 'Send secure code'}
            </Button>
          </div>
        </div>
      </form>

      <div className="auth-divider" role="separator" aria-label="or">or</div>

      <SocialLogin onSelect={handleSocial} disabled={status === 'loading'} />

      <p className="auth-alt">
        New to SporeKart? <a href="/register">Create an account</a>
      </p>
      <p className="auth-consent">
        Need help? Visit <a href="/support">Support</a> or review our{' '}
        <a href="/privacy-policy">Privacy Policy</a>.
      </p>
    </AuthLayout>
  );
}
