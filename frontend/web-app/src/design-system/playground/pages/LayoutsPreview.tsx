const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const PreviewBox = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const coloredBox = (color: string, height?: string): React.CSSProperties => ({
  background: color,
  borderRadius: 'var(--radius-sm)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: 'var(--text-xs)',
  color: 'hsla(0,0%,100%,0.9)',
  fontWeight: 'var(--weight-medium)',
  height: height || '100%',
  padding: '8px',
});

function PublicLayoutDemo() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', fontFamily: 'var(--font-family)', minHeight: '200px' }}>
      <div style={coloredBox('var(--color-bg-primary-default)', '36px')}>Header (brand + nav)</div>
      <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)', padding: '16px', minHeight: '100px' }}>
        <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>Content area</span>
      </div>
      <div style={coloredBox('var(--color-bg-secondary-default)', '32px')}>Footer</div>
    </div>
  );
}

function AuthenticatedLayoutDemo() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', fontFamily: 'var(--font-family)', minHeight: '220px' }}>
      <div style={coloredBox('var(--color-bg-primary-default)', '36px')}>Header</div>
      <div style={{ display: 'flex', gap: '4px', flex: 1 }}>
        <div style={{ ...coloredBox('var(--color-bg-secondary-default)'), width: '80px', writingMode: 'vertical-lr', textOrientation: 'mixed' }}>Sidebar</div>
        <div style={{ flex: 1, background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-secondary)', fontSize: 'var(--text-sm)' }}>Content</div>
      </div>
    </div>
  );
}

function DashboardLayoutDemo() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', fontFamily: 'var(--font-family)', minHeight: '240px' }}>
      <div style={coloredBox('var(--color-bg-primary-default)', '36px')}>Header</div>
      <div style={{ display: 'flex', gap: '4px', flex: 1 }}>
        <div style={{ ...coloredBox('var(--color-bg-secondary-default)'), width: '60px', writingMode: 'vertical-lr', textOrientation: 'mixed', fontSize: 'var(--text-xs)' }}>Sidebar</div>
        <div style={{ flex: 1, display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '4px', padding: '4px', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)' }}>
          <div style={coloredBox('var(--color-bg-surface-default)', '60px')}>Widget 1</div>
          <div style={coloredBox('var(--color-bg-surface-default)', '60px')}>Widget 2</div>
          <div style={coloredBox('var(--color-bg-surface-default)', '60px')}>Widget 3</div>
          <div style={coloredBox('var(--color-bg-surface-default)', '60px')}>Widget 4</div>
        </div>
      </div>
    </div>
  );
}

function ContentLayoutDemo() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', fontFamily: 'var(--font-family)', minHeight: '200px' }}>
      <div style={{ ...coloredBox('var(--color-bg-primary-default)', '32px'), fontSize: 'var(--text-xs)' }}>Header</div>
      <div style={{ flex: 1, display: 'flex', justifyContent: 'center', padding: '8px', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)' }}>
        <div style={{ maxWidth: '60%', width: '100%', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', padding: '12px', display: 'flex', flexDirection: 'column', gap: '6px' }}>
          <div style={{ height: '8px', width: '50%', background: 'var(--color-bg-subtle)', borderRadius: '2px' }} />
          <div style={{ height: '8px', width: '80%', background: 'var(--color-bg-subtle)', borderRadius: '2px' }} />
          <div style={{ height: '8px', width: '40%', background: 'var(--color-bg-subtle)', borderRadius: '2px' }} />
        </div>
      </div>
      <div style={coloredBox('var(--color-bg-secondary-default)', '28px')}>Footer</div>
    </div>
  );
}

function SplitLayoutDemo() {
  return (
    <div style={{ display: 'flex', gap: '4px', fontFamily: 'var(--font-family)', minHeight: '180px' }}>
      <div style={{ flex: 1, background: 'var(--color-bg-primary-default)', borderRadius: 'var(--radius-sm)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: 'var(--text-sm)', opacity: 0.8 }}>Panel A (50%)</div>
      <div style={{ flex: 1, background: 'var(--color-bg-secondary-default)', borderRadius: 'var(--radius-sm)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: 'var(--text-sm)', opacity: 0.8 }}>Panel B (50%)</div>
    </div>
  );
}

function CenteredLayoutDemo() {
  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', fontFamily: 'var(--font-family)', minHeight: '200px', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)' }}>
      <div style={{ width: '60%', maxWidth: '300px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '8px' }}>
        <div style={{ height: '8px', width: '40%', background: 'var(--color-bg-subtle)', borderRadius: '2px' }} />
        <div style={{ height: '32px', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)' }} />
        <div style={{ height: '32px', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)' }} />
        <div style={{ height: '28px', width: '50%', background: 'var(--color-bg-primary-default)', borderRadius: 'var(--radius-sm)', alignSelf: 'flex-end' }} />
      </div>
    </div>
  );
}

function BlankLayoutDemo() {
  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', fontFamily: 'var(--font-family)', minHeight: '200px' }}>
      <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-tertiary)' }}>Minimal — no chrome, just content</span>
    </div>
  );
}

function ErrorLayoutDemo() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: '12px', fontFamily: 'var(--font-family)', minHeight: '200px', background: 'var(--color-bg-subtle)', borderRadius: 'var(--radius-sm)' }}>
      <div style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', opacity: 0.2 }}>404</div>
      <span style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-semibold)' }}>Page not found</span>
      <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-secondary)' }}>The page you're looking for doesn't exist.</span>
      <div style={{ padding: '8px 24px', background: 'var(--color-bg-primary-default)', borderRadius: 'var(--radius-sm)', color: '#fff', fontSize: 'var(--text-sm)', cursor: 'pointer' }}>Go home</div>
    </div>
  );
}

export default function LayoutsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Layout Templates</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Layout template demos — colored placeholder areas show the layout structure</p>
      </div>

      <Section label="Layout Variants">
        <PreviewBox label="PublicLayout — simple header + content + footer">
          <PublicLayoutDemo />
        </PreviewBox>
        <PreviewBox label="AuthenticatedLayout — header + sidebar + content">
          <AuthenticatedLayoutDemo />
        </PreviewBox>
        <PreviewBox label="DashboardLayout — header + sidebar + widget grid">
          <DashboardLayoutDemo />
        </PreviewBox>
        <PreviewBox label="ContentLayout — centered prose content">
          <ContentLayoutDemo />
        </PreviewBox>
        <PreviewBox label="SplitLayout — 50-50 panels">
          <SplitLayoutDemo />
        </PreviewBox>
        <PreviewBox label="CenteredLayout — centered form">
          <CenteredLayoutDemo />
        </PreviewBox>
        <PreviewBox label="BlankLayout — minimal">
          <BlankLayoutDemo />
        </PreviewBox>
        <PreviewBox label="ErrorLayout — centered error">
          <ErrorLayoutDemo />
        </PreviewBox>
      </Section>
    </div>
  );
}
