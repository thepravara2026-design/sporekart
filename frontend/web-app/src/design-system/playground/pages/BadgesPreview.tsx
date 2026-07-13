const BellIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 1a5 5 0 00-5 5c0 4-2 5-2 5h14s-2-1-2-5a5 5 0 00-5-5z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/><path d="M6.5 12a1.5 1.5 0 003 0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>);
const MailIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><rect x="1" y="3" width="14" height="10" rx="1" stroke="currentColor" strokeWidth="1.5"/><path d="M1 4l7 5 7-5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const CheckIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M3 8l3 3 7-7" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);
const ShieldIcon = () => (<svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 1l6 2v5c0 3.5-6 6-6 6s-6-2.5-6-6V3l6-2z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>);

const badgeVariants = ['default', 'primary', 'success', 'warning', 'danger', 'info', 'neutral'] as const;
const badgeSizes = ['sm', 'md', 'lg'] as const;

const variantColors: Record<string, React.CSSProperties> = {
  default: { background: 'var(--color-bg-subtle, #f3f4f6)', color: 'var(--color-text-primary)' },
  primary: { background: 'var(--color-bg-primary-default)', color: '#fff' },
  success: { background: 'var(--color-bg-success-default, #16a34a)', color: '#fff' },
  warning: { background: 'var(--color-bg-warning-default, #f59e0b)', color: '#fff' },
  danger: { background: 'var(--color-bg-danger-default, #dc2626)', color: '#fff' },
  info: { background: 'var(--color-bg-info-default, #0ea5e9)', color: '#fff' },
  neutral: { background: 'var(--color-bg-neutral-default, #6b7280)', color: '#fff' },
};

const sizeStyles: Record<string, React.CSSProperties> = {
  sm: { padding: '1px 6px', fontSize: 'var(--text-xs)', borderRadius: 'var(--radius-sm)' },
  md: { padding: '2px 8px', fontSize: 'var(--text-caption)', borderRadius: 'var(--radius-md)' },
  lg: { padding: '4px 12px', fontSize: 'var(--text-sm)', borderRadius: 'var(--radius-md)' },
};

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const CardFrame = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
  </div>
);

export default function BadgesPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Badges</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All badge variants and sizes</p>
      </div>

      <Section label="Status Badges">
        <CardFrame label="All variant colors">
          {badgeVariants.map((v) => (
            <span key={v} style={{ ...variantColors[v], ...sizeStyles.md, fontWeight: 'var(--weight-medium)', textTransform: 'capitalize' }}>{v}</span>
          ))}
        </CardFrame>
      </Section>

      <Section label="Count Badges">
        <CardFrame label="Numbers and overflow">
          <span style={{ ...variantColors.primary, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>3</span>
          <span style={{ ...variantColors.primary, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>12</span>
          <span style={{ ...variantColors.primary, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>99+</span>
          <span style={{ ...variantColors.danger, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>99+</span>
          <span style={{ ...variantColors.warning, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>5</span>
        </CardFrame>
      </Section>

      <Section label="Notification Badges">
        <CardFrame label="Dot + pulse">
          <div style={{ position: 'relative', display: 'inline-flex' }}>
            <BellIcon />
            <span style={{ position: 'absolute', top: -2, right: -2, width: 8, height: 8, borderRadius: '50%', background: 'var(--color-danger, #dc2626)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
          </div>
          <div style={{ position: 'relative', display: 'inline-flex' }}>
            <MailIcon />
            <span style={{ position: 'absolute', top: -4, right: -4, ...variantColors.danger, ...sizeStyles.sm, fontWeight: 'var(--weight-medium)', lineHeight: 1, borderRadius: '50%', minWidth: 16, height: 16, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>3</span>
          </div>
        </CardFrame>
      </Section>

      <Section label="Verification Badges">
        <CardFrame label="Verified / Trusted">
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', ...variantColors.primary, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>
            <CheckIcon /> Verified
          </span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', ...variantColors.success, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>
            <ShieldIcon /> Trusted
          </span>
        </CardFrame>
      </Section>

      <Section label="Progress Badges">
        <CardFrame label="Completion indicators">
          <span style={{ ...variantColors.success, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>Complete</span>
          <span style={{ ...variantColors.warning, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>In Progress</span>
          <span style={{ ...variantColors.neutral, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>Pending</span>
          <span style={{ ...variantColors.danger, ...sizeStyles.md, fontWeight: 'var(--weight-medium)' }}>Failed</span>
        </CardFrame>
      </Section>

      <Section label="Sizes">
        <CardFrame label="sm, md, lg">
          {badgeSizes.map((s) => (
            <span key={s} style={{ ...variantColors.primary, ...sizeStyles[s], fontWeight: 'var(--weight-medium)', textTransform: 'uppercase' }}>{s}</span>
          ))}
        </CardFrame>
      </Section>

      <Section label="Badge on Buttons">
        <CardFrame label="With inline buttons">
          <button style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', borderRadius: 'var(--radius-md)', padding: '8px 16px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>
            Notifications <span style={{ ...variantColors.danger, ...sizeStyles.sm, fontWeight: 'var(--weight-medium)' }}>3</span>
          </button>
          <button style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-md)', padding: '8px 16px', cursor: 'pointer', fontSize: 'var(--text-sm)' }}>
            Messages <span style={{ ...variantColors.primary, ...sizeStyles.sm, fontWeight: 'var(--weight-medium)' }}>12</span>
          </button>
        </CardFrame>
      </Section>
    </div>
  );
}
