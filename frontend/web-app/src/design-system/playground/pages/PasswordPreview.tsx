import { useState } from 'react';

const EyeIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
);

const EyeOffIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19M14.12 14.12a3 3 0 11-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
);

interface StrengthMeterProps {
  level: 'empty' | 'weak' | 'fair' | 'good' | 'strong';
}

function StrengthMeter({ level }: StrengthMeterProps) {
  const levels = ['weak', 'fair', 'good', 'strong'];
  const levelIndex = levels.indexOf(level);
  const barColors = ['var(--color-danger, #dc2626)', 'var(--color-warning, #f59e0b)', 'var(--color-success, #16a34a)', 'var(--color-brand, #2563eb)'];
  const labelColors: Record<string, string> = {
    empty: 'var(--color-text-secondary)',
    weak: 'var(--color-danger, #dc2626)',
    fair: 'var(--color-warning, #f59e0b)',
    good: 'var(--color-success, #16a34a)',
    strong: 'var(--color-brand, #2563eb)',
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
      <div style={{ display: 'flex', gap: '4px' }}>
        {levels.map((l, i) => (
          <div
            key={l}
            style={{
              height: 4,
              flex: 1,
              borderRadius: 2,
              background: i <= levelIndex ? barColors[i] : 'var(--color-border-default)',
              transition: 'background 0.2s',
            }}
          />
        ))}
      </div>
      <span style={{ fontSize: 'var(--text-caption)', color: labelColors[level] || 'var(--color-text-secondary)', textTransform: 'capitalize' }}>
        {level === 'empty' ? 'No password' : `${level} password`}
      </span>
    </div>
  );
}

function PasswordDemo({ label, initialValue, strength }: { label: string; initialValue: string; strength: StrengthMeterProps['level'] }) {
  const [visible, setVisible] = useState(false);

  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '8px 12px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-input, #fff)' }}>
        <input
          type={visible ? 'text' : 'password'}
          defaultValue={initialValue}
          placeholder="Enter password"
          style={{ border: 'none', background: 'transparent', outline: 'none', flex: 1, fontSize: 'var(--text-base)', width: '100%' }}
        />
        <button onClick={() => setVisible((v) => !v)} style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 0, color: 'var(--color-text-secondary)', display: 'flex' }}>
          {visible ? <EyeOffIcon /> : <EyeIcon />}
        </button>
      </div>
      <StrengthMeter level={strength} />
    </div>
  );
}

export default function PasswordPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Password</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Password input with visibility toggle and strength indicator</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Strength Levels</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <PasswordDemo label="Empty" initialValue="" strength="empty" />
          <PasswordDemo label="Weak" initialValue="abc" strength="weak" />
          <PasswordDemo label="Fair" initialValue="abc123" strength="fair" />
          <PasswordDemo label="Good" initialValue="Abc123!@" strength="good" />
          <PasswordDemo label="Strong" initialValue="Str0ng!Pass#2024" strength="strong" />
        </div>
      </section>
    </div>
  );
}
