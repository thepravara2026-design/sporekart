const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const CardFrame = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const SkeletonBlock = ({ width, height, borderRadius }: { width?: string; height?: string; borderRadius?: string }) => (
  <div style={{ width: width || '100%', height: height || '16px', borderRadius: borderRadius || '4px', background: 'var(--color-bg-subtle, #f3f4f6)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
);

export default function SkeletonsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Skeletons</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All skeleton types</p>
      </div>

      <Section label="Skeleton Primitives">
        <CardFrame label="Text, circular, rectangular, rounded">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <SkeletonBlock width="60%" height="14px" />
            <SkeletonBlock width="80%" height="14px" />
            <SkeletonBlock width="40%" height="14px" />
          </div>
          <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
            <SkeletonBlock width="40px" height="40px" borderRadius="50%" />
            <SkeletonBlock width="100px" height="40px" borderRadius="8px" />
            <SkeletonBlock width="60px" height="40px" borderRadius="4px" />
          </div>
        </CardFrame>
      </Section>

      <Section label="Card Skeleton">
        <CardFrame label="With and without image">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <SkeletonBlock width="100%" height="120px" borderRadius="8px" />
            <SkeletonBlock width="70%" height="16px" />
            <SkeletonBlock width="50%" height="12px" />
            <SkeletonBlock width="30%" height="12px" />
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <SkeletonBlock width="70%" height="16px" />
            <SkeletonBlock width="50%" height="12px" />
            <SkeletonBlock width="40%" height="12px" />
          </div>
        </CardFrame>
      </Section>

      <Section label="Table Skeleton">
        <CardFrame label="4 columns, 5 rows">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            <div style={{ display: 'flex', gap: '12px' }}>
              {Array.from({ length: 4 }).map((_, j) => (
                <SkeletonBlock key={j} width="25%" height="16px" />
              ))}
            </div>
            {Array.from({ length: 5 }).map((_, i) => (
              <div key={i} style={{ display: 'flex', gap: '12px' }}>
                {Array.from({ length: 4 }).map((_, j) => (
                  <SkeletonBlock key={j} width="25%" height="12px" />
                ))}
              </div>
            ))}
          </div>
        </CardFrame>
      </Section>

      <Section label="List Skeleton">
        <CardFrame label="5 items with icons and descriptions">
          {Array.from({ length: 5 }).map((_, i) => (
            <div key={i} style={{ display: 'flex', gap: '12px', alignItems: 'center', padding: '8px 0' }}>
              <SkeletonBlock width="32px" height="32px" borderRadius="50%" />
              <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '4px' }}>
                <SkeletonBlock width="60%" height="14px" />
                <SkeletonBlock width="40%" height="10px" />
              </div>
            </div>
          ))}
        </CardFrame>
      </Section>

      <Section label="Form Skeleton">
        <CardFrame label="4 fields">
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {Array.from({ length: 4 }).map((_, i) => (
              <div key={i} style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
                <SkeletonBlock width="30%" height="12px" />
                <SkeletonBlock width="100%" height="36px" borderRadius="6px" />
              </div>
            ))}
            <SkeletonBlock width="120px" height="36px" borderRadius="6px" />
          </div>
        </CardFrame>
      </Section>

      <Section label="Avatar Skeleton">
        <CardFrame label="All sizes">
          <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
            {['24px', '32px', '40px', '48px', '56px', '64px'].map((s, i) => (
              <div key={i} style={{ width: s, height: s, borderRadius: '50%', background: 'var(--color-bg-subtle, #f3f4f6)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            ))}
          </div>
        </CardFrame>
      </Section>

      <Section label="Dashboard Skeleton">
        <CardFrame label="Grid of card skeletons">
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '12px' }}>
            {Array.from({ length: 4 }).map((_, i) => (
              <div key={i} style={{ display: 'flex', flexDirection: 'column', gap: '8px', padding: '16px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', boxShadow: 'var(--shadow-1)' }}>
                <SkeletonBlock width="50%" height="14px" />
                <SkeletonBlock width="80%" height="24px" />
                <SkeletonBlock width="30%" height="10px" />
              </div>
            ))}
          </div>
        </CardFrame>
      </Section>

      <Section label="Product Grid Skeleton">
        <CardFrame label="Grid of product skeletons">
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '12px' }}>
            {Array.from({ length: 4 }).map((_, i) => (
              <div key={i} style={{ display: 'flex', flexDirection: 'column', gap: '8px', padding: '12px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', boxShadow: 'var(--shadow-1)' }}>
                <SkeletonBlock width="100%" height="120px" borderRadius="8px" />
                <SkeletonBlock width="70%" height="14px" />
                <SkeletonBlock width="40%" height="12px" />
                <SkeletonBlock width="50%" height="20px" />
              </div>
            ))}
          </div>
        </CardFrame>
      </Section>
    </div>
  );
}
