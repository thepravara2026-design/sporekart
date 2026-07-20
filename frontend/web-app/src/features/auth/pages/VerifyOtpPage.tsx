import { useCallback, useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AuthLayout from '../AuthLayout';
import AuthAlert from '../components/AuthAlert';
import { OtpInput } from '../../../design-system/components/core/OtpInput';
import { Button } from '../../../design-system/components/core/Button';
import { Icon } from '../../../design-system/icons/Icon';
import { authClient, type AuthChannel } from '../authClient';
import { useCountdown } from '../hooks/useCountdown';
import '../auth.css';

type Status = 'idle' | 'verifying' | 'error' | 'success';

interface OtpState {
  channel: AuthChannel;
  destination: string;
  flow?: 'login' | 'register';
  profile?: Record<string, unknown>;
  remember?: boolean;
}

const DEMO_STATE: OtpState = {
  channel: 'phone',
  destination: '+1 555 0100',
  flow: 'login',
};

function mask(destination: string): string {
  const v = destination.trim();
  if (v.includes('@')) {
    const [u, d] = v.split('@');
    return (u.length > 2 ? u[0] + '•••' + u.slice(-1) : u) + '@' + d;
  }
  return '•••• ' + v.slice(-4);
}

export default function VerifyOtpPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const demo = new URLSearchParams(location.search).get('demo') === '1';
  const state = (location.state as OtpState | null) ?? (demo ? DEMO_STATE : null);

  const [otp, setOtp] = useState('');
  const [status, setStatus] = useState<Status>('idle');
  const [error, setError] = useState('');
  const [shakeKey, setShakeKey] = useState(0);
  const countdown = useCountdown(30);
  const verifyingRef = useRef(false);

  useEffect(() => {
    if (!state) {
      navigate('/login', { replace: true });
    } else if (!countdown.active) {
      countdown.start(30);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const verify = useCallback(
    async (code: string) => {
      if (verifyingRef.current || !state) return;
      verifyingRef.current = true;
      setStatus('verifying');
      setError('');
      try {
        await authClient.verifyOtp(state.channel, state.destination, code);
        if (state.flow === 'register' && state.profile) {
          await authClient.register(state.profile as never);
        }
        setStatus('success');
        if (demo) return;
        window.setTimeout(() => {
          navigate('/auth/loading', { replace: true });
        }, 1400);
      } catch (err) {
        verifyingRef.current = false;
        setStatus('error');
        setError(err instanceof Error ? err.message : 'Verification failed.');
        setShakeKey((k) => k + 1);
      }
    },
    [state, navigate],
  );

  const handleResend = async () => {
    if (!state || countdown.active) return;
    setError('');
    setOtp('');
    try {
      await authClient.sendOtp(state.channel, state.destination);
      countdown.start(30);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Could not resend code.');
    }
  };

  if (!state) return null;

  const heading = state.flow === 'register' ? 'Verify your phone' : 'Enter your code';

  return (
    <AuthLayout brandHeading="Almost there." brandSubheading="Enter the secure code we just sent. It expires shortly for your safety." showFeatures={false}>
      <header className="auth-card__header">
        <span className="auth-card__eyebrow">
          <Icon name="shield" size={14} color="currentColor" /> Verification
        </span>
        <h2 className="auth-card__title">{heading}</h2>
        <p className="auth-card__subtitle">
          We sent a 6-digit code to <strong>{mask(state.destination)}</strong> via{' '}
          {state.channel === 'phone' ? 'SMS' : 'email'}.
        </p>
      </header>

      {status === 'success' ? (
        <div className="auth-fields" style={{ textAlign: 'center' }}>
          <span className="auth-status__icon auth-status__icon--success" aria-hidden="true">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <path className="auth-check" d="M5 13l4 4 10-10" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </span>
          <p className="auth-card__subtitle" style={{ marginTop: 'var(--space-stack-sm)' }}>
            Verified! Taking you to your workspace…
          </p>
        </div>
      ) : (
        <div className="auth-fields">
          <div key={shakeKey} className={status === 'error' ? 'auth-shake' : ''}>
            <OtpInput
              length={6}
              value={otp}
              onChange={(v) => {
                setOtp(v);
                if (status === 'error') setStatus('idle');
              }}
              onComplete={(v) => verify(v)}
              error={status === 'error'}
              disabled={status === 'verifying'}
              aria-label="One-time verification code"
            />
          </div>

          <div className="auth-otp__meta">
            <span aria-live="polite">
              {countdown.active
                ? `Resend available in ${countdown.seconds}s`
                : 'Did not get the code?'}
            </span>
            <button
              type="button"
              className="auth-otp__resend"
              onClick={handleResend}
              disabled={countdown.active || status === 'verifying'}
            >
              Resend code
            </button>
          </div>

          {status === 'error' && error && <AuthAlert type="error">{error}</AuthAlert>}

          <div className="auth-submit">
            <Button
              size="lg"
              fullWidth
              loading={status === 'verifying'}
              disabled={otp.length < 6}
              onClick={() => verify(otp)}
            >
              {status === 'verifying' ? 'Verifying…' : 'Verify & continue'}
            </Button>
          </div>

          <p className="auth-alt">
            Wrong contact? <a href="/login">Start over</a>
          </p>
        </div>
      )}
    </AuthLayout>
  );
}
