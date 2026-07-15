import { useEffect, useState, memo } from 'react';

interface SessionTimeoutWarningProps {
  open: boolean;
  onExtend: () => void;
  onLogout: () => void;
  warningDuration?: number;
}

export const SessionTimeoutWarning = memo(function SessionTimeoutWarning({ open, onExtend, onLogout, warningDuration = 120 }: SessionTimeoutWarningProps) {
  const [countdown, setCountdown] = useState(warningDuration);

  useEffect(() => {
    if (!open) { setCountdown(warningDuration); return; }
    const interval = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) { clearInterval(interval); onLogout(); return 0; }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(interval);
  }, [open, warningDuration, onLogout]);

  if (!open) return null;

  return (
    <div
      role="alertdialog"
      aria-modal="true"
      aria-label="Session timeout warning"
      style={{
        position: 'fixed',
        inset: 0,
        zIndex: 10000,
        background: 'rgba(0,0,0,0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <div
        style={{
          background: 'var(--color-surface)',
          borderRadius: 'var(--radius-xl)',
          padding: 32,
          maxWidth: 400,
          width: '90%',
          boxShadow: 'var(--elevation-xl)',
          textAlign: 'center',
        }}
      >
        <div style={{ width: 48, height: 48, borderRadius: '50%', background: 'var(--color-warning-alpha, rgba(245,158,11,0.1))', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px' }}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" style={{ color: 'var(--color-warning)' }}>
            <path d="M12 9v4m0 4h.01M21 12c0-4.97-4.03-9-9-9s-9 4.03-9 9 4.03 9 9 9 9-4.03 9-9z" />
          </svg>
        </div>
        <h2 style={{ margin: '0 0 4px', fontSize: 'var(--text-h3)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
          Session Expiring Soon
        </h2>
        <p style={{ margin: '0 0 20px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
          Your session will expire in <strong>{countdown}</strong> seconds due to inactivity.
        </p>
        <div style={{ display: 'flex', gap: 8, justifyContent: 'center' }}>
          <button
            onClick={onExtend}
            style={{
              padding: '10px 24px', border: 'none', borderRadius: 'var(--radius-md)',
              background: 'var(--color-primary)', color: '#fff', cursor: 'pointer',
              fontSize: 'var(--text-body)', fontWeight: 500,
            }}
          >
            Stay signed in
          </button>
          <button
            onClick={onLogout}
            style={{
              padding: '10px 24px', border: '1px solid var(--color-border)',
              borderRadius: 'var(--radius-md)', background: 'transparent',
              color: 'var(--color-text-secondary)', cursor: 'pointer',
              fontSize: 'var(--text-body)',
            }}
          >
            Sign out
          </button>
        </div>
      </div>
    </div>
  );
});
