const ExternalIcon = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
    <path d="M11 7v4a1 1 0 01-1 1H3a1 1 0 01-1-1V4a1 1 0 011-1h4M7 1h4v4M6 8l5-5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const linkVariants = [
  { id: 'inline', label: 'Inline' },
  { id: 'navigation', label: 'Navigation' },
  { id: 'external', label: 'External' },
  { id: 'text', label: 'Text' },
  { id: 'disabled', label: 'Disabled' },
] as const;

const sizes = ['sm', 'md', 'lg'] as const;

const variantLinkStyle: Record<string, React.CSSProperties> = {
  inline: { color: 'var(--color-text-link, #2563eb)', textDecoration: 'underline', cursor: 'pointer' },
  navigation: { color: 'var(--color-text-primary)', textDecoration: 'none', cursor: 'pointer', fontWeight: 500 },
  external: { color: 'var(--color-text-link, #2563eb)', textDecoration: 'underline', cursor: 'pointer' },
  text: { color: 'inherit', textDecoration: 'underline', cursor: 'pointer', textDecorationStyle: 'dotted' },
  disabled: { color: 'var(--color-text-disabled, #9ca3af)', textDecoration: 'none', cursor: 'not-allowed', pointerEvents: 'none' as const },
};

const sizeFontSize: Record<string, string> = {
  sm: 'var(--text-sm)',
  md: 'var(--text-base)',
  lg: 'var(--text-lg)',
};

const states = ['Default', 'Hover', 'Focus', 'Active'] as const;

export default function LinksPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Links</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Link variants with sizes and states</p>
      </div>

      {linkVariants.map((v) => (
        <section key={v.id} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{v.label}</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
            {sizes.map((size) => (
              <div key={size} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Size: {size}</span>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '16px', alignItems: 'center' }}>
                  {states.map((state) => {
                    const base: React.CSSProperties = { ...variantLinkStyle[v.id], fontSize: sizeFontSize[size], display: 'inline-flex', alignItems: 'center', gap: '4px' };
                    if (state === 'Hover') base.textDecoration = 'underline';
                    if (state === 'Focus') base.outline = '2px solid var(--color-focus-ring, #3b82f6)', base.outlineOffset = '2px';
                    if (state === 'Active') base.color = 'var(--color-text-link-active, #1d4ed8)';
                    return (
                      <span key={state} style={base}>
                        {state} Link {v.id === 'external' ? <ExternalIcon /> : null}
                      </span>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>
          {v.id === 'external' && (
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>
              External links show an external link indicator icon
            </p>
          )}
        </section>
      ))}
    </div>
  );
}
