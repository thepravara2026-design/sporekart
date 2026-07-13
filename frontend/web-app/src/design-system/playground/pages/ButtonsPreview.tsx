const LinkIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M7 3H3a1 1 0 00-1 1v8a1 1 0 001 1h8a1 1 0 001-1V7M9 1h4v4M6 10l7-7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const DownloadIcon = () => (
  <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
    <path d="M8 1v9M4 7l4 4 4-4M2 13v1h12v-1" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const variants = ['primary', 'secondary', 'outline', 'ghost', 'destructive', 'success', 'warning', 'link'] as const;
const sizes = ['sm', 'md', 'lg'] as const;

const variantStyles: Record<string, React.CSSProperties> = {
  primary: { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none' },
  secondary: { background: 'var(--color-bg-secondary-default)', color: '#fff', border: 'none' },
  outline: { background: 'transparent', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)' },
  ghost: { background: 'transparent', color: 'var(--color-text-primary)', border: 'none' },
  destructive: { background: 'var(--color-bg-danger-default, #dc2626)', color: '#fff', border: 'none' },
  success: { background: 'var(--color-bg-success-default, #16a34a)', color: '#fff', border: 'none' },
  warning: { background: 'var(--color-bg-warning-default, #f59e0b)', color: '#fff', border: 'none' },
  link: { background: 'transparent', color: 'var(--color-text-link, #2563eb)', border: 'none', padding: '0', textDecoration: 'underline' },
};

const sizeStyles: Record<string, React.CSSProperties> = {
  sm: { padding: '4px 12px', fontSize: 'var(--text-sm)', borderRadius: 'var(--radius-sm)' },
  md: { padding: '8px 16px', fontSize: 'var(--text-base)', borderRadius: 'var(--radius-md)' },
  lg: { padding: '12px 24px', fontSize: 'var(--text-lg)', borderRadius: 'var(--radius-lg)' },
};

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

export default function ButtonsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Buttons</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Button variants, sizes, and states</p>
      </div>

      {variants.map((variant) => (
        <section key={variant} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0, textTransform: 'capitalize' }}>{variant}</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
            {sizes.map((size) => (
              <StateCard key={size} label={`Size: ${size}`}>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px' }}>Default</button>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px', filter: 'brightness(0.9)' }}>Hover</button>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px', outline: '2px solid var(--color-focus-ring, #3b82f6)', outlineOffset: '2px' }}>Focus</button>
                <button disabled style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'not-allowed', opacity: 0.5, fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px' }}>Disabled</button>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px', position: 'relative' }}>
                  <span style={{ visibility: 'hidden' }}>Loading</span>
                  <span style={{ position: 'absolute', width: 14, height: 14, border: '2px solid currentColor', borderTopColor: 'transparent', borderRadius: '50%', animation: 'sk-spin 0.6s linear infinite' }} />
                </button>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px', width: '100%', justifyContent: 'center' }}>
                  <LinkIcon /> Full
                </button>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px' }}>
                  <DownloadIcon /> Icon L
                </button>
                <button style={{ ...variantStyles[variant], ...sizeStyles[size], cursor: 'pointer', fontWeight: 500, display: 'inline-flex', alignItems: 'center', gap: '6px' }}>
                  Icon R <DownloadIcon />
                </button>
              </StateCard>
            ))}
          </div>
        </section>
      ))}
    </div>
  );
}
