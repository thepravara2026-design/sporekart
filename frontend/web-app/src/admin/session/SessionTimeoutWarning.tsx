import { useEffect, useRef, useState, memo } from 'react';

interface SessionTimeoutWarningProps {
  open: boolean;
  onExtend: () => void;
  onLogout: () => void;
  warningDuration?: number;
}

export const SessionTimeoutWarning = memo(function SessionTimeoutWarning({ open, onExtend, onLogout, warningDuration = 120 }: SessionTimeoutWarningProps) {
  const [countdown, setCountdown] = useState(warningDuration);
  const dialogRef = useRef<HTMLDivElement>(null);
  const previouslyFocused = useRef<HTMLElement | null>(null);
  const intervalRef = useRef<ReturnType<typeof setInterval>>();
  const loggedOutRef = useRef(false);

  /**
   * BUG-PERF-002: the previous implementation created a `setInterval` inside a
   * `setState` updater and relied on `clearInterval(interval)` being reached
   * before calling `onLogout`. That pairing was fragile under React StrictMode
   * double-invocation and could leave a dangling timer. We now hold the handle
   * in a ref, guard `onLogout` so it fires exactly once, and guarantee cleanup
   * on every open/close/unmount.
   */
  useEffect(() => {
    if (!open) {
      setCountdown(warningDuration);
      loggedOutRef.current = false;
      return;
    }
    loggedOutRef.current = false;
    intervalRef.current = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) {
          if (!loggedOutRef.current) {
            loggedOutRef.current = true;
            onLogout();
          }
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
  }, [open, warningDuration, onLogout]);

  useEffect(() => {
    if (!open) return;
    previouslyFocused.current = document.activeElement as HTMLElement | null;
    const dialog = dialogRef.current;
    if (!dialog) return;
    const focusable = () =>
      Array.from(
        dialog.querySelectorAll<HTMLElement>(
          'button:not([disabled]), [href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"])',
        ),
      );
    const first = focusable()[0];
    first?.focus();
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key !== 'Tab') return;
      const items = focusable();
      if (items.length === 0) return;
      const firstEl = items[0];
      const lastEl = items[items.length - 1];
      if (e.shiftKey && document.activeElement === firstEl) {
        e.preventDefault();
        lastEl.focus();
      } else if (!e.shiftKey && document.activeElement === lastEl) {
        e.preventDefault();
        firstEl.focus();
      }
    };
    dialog.addEventListener('keydown', handleKeyDown);
    return () => {
      dialog.removeEventListener('keydown', handleKeyDown);
      previouslyFocused.current?.focus?.();
    };
  }, [open]);

  if (!open) return null;

  return (
    <div
      ref={dialogRef}
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
