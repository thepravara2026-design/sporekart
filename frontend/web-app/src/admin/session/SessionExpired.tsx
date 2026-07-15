interface SessionExpiredProps {
  onReauthenticate?: () => void;
  savedPage?: string | null;
}

export function SessionExpired({ onReauthenticate, savedPage }: SessionExpiredProps) {
  return (
    <div
      role="alert"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 48,
        textAlign: 'center',
        minHeight: 300,
      }}
    >
      <div style={{ width: 56, height: 56, borderRadius: '50%', background: 'var(--color-error-alpha, rgba(239,68,68,0.1))', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: 16 }}>
        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" style={{ color: 'var(--color-error)' }}>
          <circle cx="12" cy="12" r="10" />
          <polyline points="12 6 12 12 16 14" />
        </svg>
      </div>
      <h2 style={{ margin: '0 0 4px', fontSize: 'var(--text-h3)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
        Session Expired
      </h2>
      <p style={{ margin: '0 0 4px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', maxWidth: 360 }}>
        Your session has ended due to inactivity.
      </p>
      {savedPage && (
        <p style={{ margin: '0 0 16px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          You were on: {savedPage}
        </p>
      )}
      <button
        onClick={onReauthenticate}
        style={{
          padding: '10px 24px', border: 'none', borderRadius: 'var(--radius-md)',
          background: 'var(--color-primary)', color: '#fff', cursor: 'pointer',
          fontSize: 'var(--text-body)', fontWeight: 500,
        }}
      >
        Sign in again
      </button>
    </div>
  );
}
