const boxStyle: React.CSSProperties = {
  width: 40,
  height: 48,
  textAlign: 'center',
  fontSize: 'var(--text-lg)',
  fontWeight: 'var(--weight-semibold)',
  borderRadius: 'var(--radius-md)',
  outline: 'none',
  padding: 0,
  boxSizing: 'border-box',
};

interface OtpRowProps {
  label: string;
  length: number;
  error?: boolean;
  filled?: boolean;
}

function OtpRow({ label, length, error, filled }: OtpRowProps) {
  const inputs = Array.from({ length }, (_, i) => {
    const val = filled ? String(i + 1) : '';
    return (
      <input
        key={i}
        type="text"
        maxLength={1}
        value={val}
        readOnly
        style={{
          ...boxStyle,
          border: error ? '2px solid var(--color-border-danger, #dc2626)' : '1px solid var(--color-border-default)',
          background: 'var(--color-bg-input, #fff)',
          color: 'var(--color-text-primary)',
        }}
      />
    );
  });

  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', gap: '8px' }}>{inputs}</div>
      {error && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger, #dc2626)' }}>Invalid code. Please try again.</span>}
    </div>
  );
}

export default function OtpPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>OTP Input</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>One-time password input with various states and lengths</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Length: 4</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <OtpRow label="Default (empty)" length={4} />
          <OtpRow label="Filled" length={4} filled />
          <OtpRow label="Error" length={4} error />
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Length: 6</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <OtpRow label="Default (empty)" length={6} />
          <OtpRow label="Filled" length={6} filled />
          <OtpRow label="Error" length={6} error />
        </div>
      </section>
    </div>
  );
}
