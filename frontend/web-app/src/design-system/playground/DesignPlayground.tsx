import { useNavigate } from 'react-router-dom';
import { componentManifest } from './catalog/componentManifest';
import { tokenManifest } from './catalog/tokenManifest';
import TokenDisplay from './components/TokenDisplay';

const categories = ['core', 'forms', 'display', 'navigation', 'feedback', 'charts', 'layout'] as const;

const categoryColors: Record<string, string> = {
  core: 'var(--color-dataviz-categorical-1, #2F6F4F)',
  forms: 'var(--color-dataviz-categorical-2, #1565C0)',
  display: 'var(--color-dataviz-categorical-3, #F57F17)',
  navigation: 'var(--color-dataviz-categorical-4, #C62828)',
  feedback: 'var(--color-info-500, #2196F3)',
  charts: 'var(--color-success-500, #4CAF50)',
  layout: 'var(--color-neutral-600, #6D6D6D)',
};

const quickLinks = [
  { label: 'Component Catalog', path: '/design-system/catalog' },
  { label: 'Token Explorer', path: '/design-system/tokens' },
  { label: 'Icon Library', path: '/design-system/icons' },
  { label: 'Accessibility Center', path: '/design-system/accessibility' },
  { label: 'Documentation Center', path: '/design-system/docs' },
  { label: 'Quality Dashboard', path: '/design-system/quality' },
];

const pipelineSteps = ['Design', 'Implement', 'Preview', 'Review', 'Approve', 'Freeze', 'Document'];

const recentUpdates = [
  { version: 'v2.0.0', date: '2026-07-13', description: 'Design Playground, Component Catalog, Token Explorer' },
  { version: 'v1.7.0', date: '2026-07-10', description: 'Sprint 20 Part 7 — Data Visualization & Analytics' },
  { version: 'v1.6.0', date: '2026-07-07', description: 'Sprint 20 Part 6 — Feedback & Overlay System' },
  { version: 'v1.5.0', date: '2026-07-04', description: 'Sprint 20 Part 5 — Navigation & Layout System' },
  { version: 'v1.4.0', date: '2026-07-01', description: 'Sprint 20 Part 4 — Display Library' },
];

const tokenCategories = ['color', 'typography', 'spacing', 'radius', 'elevation', 'animation'] as const;

function getTokenExamples(cat: typeof tokenCategories[number]): typeof tokenManifest {
  const catTokens = tokenManifest.filter((t) => t.category === cat && t.type === 'semantic');
  return catTokens.slice(0, 3);
}

function TotalComponentsStat({ count }: { count: number }) {
  return (
    <div style={statCardStyle}>
      <span style={statValueStyle}>{count}</span>
      <span style={statLabelStyle}>Total Components</span>
    </div>
  );
}

function StatCard({ value, label }: { value: string; label: string }) {
  return (
    <div style={statCardStyle}>
      <span style={statValueStyle}>{value}</span>
      <span style={statLabelStyle}>{label}</span>
    </div>
  );
}

function CategoryBadge({ category }: { category: string }) {
  const color = categoryColors[category] || 'var(--color-neutral-400)';
  return (
    <span
      style={{
        display: 'inline-block',
        padding: '2px 8px',
        borderRadius: 'var(--radius-pill)',
        background: `${color}1A`,
        color,
        fontSize: 'var(--text-caption)',
        fontWeight: 'var(--font-weight-semibold)',
        letterSpacing: 'var(--letter-spacing-wide)',
        textTransform: 'uppercase',
      }}
    >
      {category}
    </span>
  );
}

const pageStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap, var(--spacing-2xl))',
  padding: 'var(--space-page-y, var(--spacing-3xl)) var(--space-page-x, var(--spacing-2xl))',
  maxWidth: '1280px',
  margin: '0 auto',
  width: '100%',
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2)',
  fontWeight: 'var(--font-weight-bold)',
  margin: '0 0 var(--spacing-lg)',
  color: 'var(--color-text-primary)',
};

const heroSectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-lg)',
  padding: 'var(--spacing-2xl) var(--spacing-xl)',
  background: 'linear-gradient(135deg, var(--color-bg-primary-default, #2F6F4F) 0%, #1a4a33 100%)',
  borderRadius: 'var(--radius-xl)',
  color: '#fff',
};

const heroTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h1)',
  fontWeight: 'var(--font-weight-bold)',
  margin: 0,
  lineHeight: 'var(--line-height-xs)',
};

const heroSubtitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-lg)',
  margin: 'var(--spacing-xs) 0 0',
  opacity: 0.9,
};

const badgeRowStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: 'var(--spacing-sm)',
  alignItems: 'center',
};

const badgeStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--spacing-2xs)',
  padding: '4px 12px',
  borderRadius: 'var(--radius-pill)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--font-weight-semibold)',
  background: 'rgba(255,255,255,0.15)',
  backdropFilter: 'blur(4px)',
};

const statsRowStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))',
  gap: 'var(--spacing-md)',
  marginTop: 'var(--spacing-sm)',
};

const statCardStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-2xs)',
  background: 'rgba(255,255,255,0.1)',
  borderRadius: 'var(--radius-card)',
  padding: 'var(--spacing-md) var(--spacing-lg)',
  backdropFilter: 'blur(4px)',
};

const statValueStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2)',
  fontWeight: 'var(--font-weight-bold)',
  lineHeight: 1,
};

const statLabelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  opacity: 0.85,
};

const sectionCardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  border: '1px solid var(--color-border-default)',
  padding: 'var(--spacing-xl)',
  boxShadow: 'var(--shadow-card, var(--elevation-1))',
};

const pipelineRowStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: 'var(--spacing-xs)',
  alignItems: 'center',
  marginTop: 'var(--spacing-md)',
};

const pipelineStepStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--spacing-2xs)',
  padding: '6px 14px',
  borderRadius: 'var(--radius-pill)',
  fontSize: 'var(--text-body-sm)',
  fontWeight: 'var(--font-weight-medium)',
  background: 'var(--color-bg-primary-default)',
  color: '#fff',
};

const pipelineArrowStyle: React.CSSProperties = {
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-body-sm)',
  fontWeight: 'var(--font-weight-bold)',
};

const navGridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
  gap: 'var(--spacing-sm)',
  marginTop: 'var(--spacing-md)',
};

const navButtonStyle: React.CSSProperties = {
  padding: '12px 20px',
  borderRadius: 'var(--radius-button)',
  border: '1px solid var(--color-border-default)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)',
  fontWeight: 'var(--font-weight-medium)',
  cursor: 'pointer',
  transition: 'all var(--duration-fast) var(--easing-standard)',
  textAlign: 'left',
};

const categoryGridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(3, 1fr)',
  gap: 'var(--spacing-md)',
};

const categoryCardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-card)',
  border: '1px solid var(--color-border-default)',
  padding: 'var(--spacing-lg)',
  cursor: 'pointer',
  transition: 'all var(--duration-fast) var(--easing-standard)',
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-sm)',
};

const quickLinksGridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))',
  gap: 'var(--spacing-sm)',
};

const quickLinkStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--spacing-xs)',
  padding: '12px 16px',
  borderRadius: 'var(--radius-button)',
  border: '1px solid var(--color-border-default)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body-sm)',
  fontWeight: 'var(--font-weight-medium)',
  cursor: 'pointer',
  transition: 'all var(--duration-fast) var(--easing-standard)',
  textDecoration: 'none',
};

const tokenGridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(3, 1fr)',
  gap: 'var(--spacing-md)',
};

const tokenCategorySectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--spacing-sm)',
};

const tokenCategoryTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h3)',
  fontWeight: 'var(--font-weight-semibold)',
  margin: 0,
  color: 'var(--color-text-primary)',
  textTransform: 'capitalize',
};

const updateCardStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'baseline',
  gap: 'var(--spacing-md)',
  padding: 'var(--spacing-md) 0',
  borderBottom: '1px solid var(--color-border-default)',
};

const updateCardLastStyle: React.CSSProperties = {
  ...updateCardStyle,
  borderBottom: 'none',
};

const updateVersionStyle: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  fontWeight: 'var(--font-weight-bold)',
  color: 'var(--color-bg-primary-default)',
  fontFamily: 'var(--font-mono, "Cascadia Code", "Fira Code", monospace)',
  whiteSpace: 'nowrap',
  minWidth: '70px',
};

const updateDateStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  whiteSpace: 'nowrap',
  minWidth: '90px',
};

const updateDescStyle: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-primary)',
  margin: 0,
};

export default function DesignPlayground() {
  const navigate = useNavigate();

  const totalComponents = componentManifest.length;
  const componentsByCategory = categories.reduce<Record<string, typeof componentManifest>>((acc, cat) => {
    acc[cat] = componentManifest.filter((c) => c.category === cat);
    return acc;
  }, {});

  return (
    <div style={pageStyle}>
      <section style={heroSectionStyle}>
        <div>
          <h1 style={heroTitleStyle}>SporeKart Enterprise Design System</h1>
          <p style={heroSubtitleStyle}>The official review environment for every component</p>
        </div>
        <div style={badgeRowStyle}>
          <span style={badgeStyle}>v2.0.0</span>
          <span style={badgeStyle}>Build 2026.07.13</span>
          <span style={{ ...badgeStyle, background: 'rgba(74, 222, 128, 0.2)' }}>
            {'✅'} Approved
          </span>
        </div>
        <div style={statsRowStyle}>
          <TotalComponentsStat count={totalComponents} />
          <StatCard value="~130" label="Design Tokens" />
          <StatCard value="85%" label="Documentation Coverage" />
          <StatCard value="AA" label="Accessibility Target" />
        </div>
      </section>

      <section style={sectionCardStyle}>
        <h2 style={sectionTitleStyle}>Overview</h2>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', lineHeight: 'var(--line-height-md)', margin: 0 }}>
          SporeKart Design System provides a unified, accessible, and performant component library
          for building enterprise-grade e-commerce experiences. Every component follows a rigorous
          review pipeline before being marked as stable and approved for production use.
        </p>
        <div style={pipelineRowStyle}>
          {pipelineSteps.map((step, i) => (
            <span key={step} style={{ display: 'flex', alignItems: 'center', gap: 'var(--spacing-2xs)' }}>
              <span style={pipelineStepStyle}>{step}</span>
              {i < pipelineSteps.length - 1 && <span style={pipelineArrowStyle}>{'→'}</span>}
            </span>
          ))}
        </div>
        <div style={navGridStyle}>
          {[
            { label: 'Component Catalog', path: '/design-system/catalog' },
            { label: 'Token Explorer', path: '/design-system/tokens' },
            { label: 'Icon Library', path: '/design-system/icons' },
            { label: 'Accessibility Center', path: '/design-system/accessibility' },
          ].map(({ label, path }) => (
            <button
              key={path}
              style={navButtonStyle}
              onClick={() => navigate(path)}
              onMouseEnter={(e) => {
                const el = e.currentTarget;
                el.style.borderColor = 'var(--color-border-focus)';
                el.style.boxShadow = 'var(--shadow-card-hover, var(--elevation-2))';
              }}
              onMouseLeave={(e) => {
                const el = e.currentTarget;
                el.style.borderColor = 'var(--color-border-default)';
                el.style.boxShadow = 'none';
              }}
            >
              {label} {'→'}
            </button>
          ))}
        </div>
      </section>

      <section>
        <h2 style={sectionTitleStyle}>Component Categories</h2>
        <div style={categoryGridStyle}>
          {categories.map((cat) => {
            const comps = componentsByCategory[cat] || [];
            return (
              <div
                key={cat}
                style={categoryCardStyle}
                onClick={() => navigate(`/design-system/catalog?category=${cat}`)}
                onMouseEnter={(e) => {
                  const el = e.currentTarget;
                  el.style.borderColor = categoryColors[cat];
                  el.style.boxShadow = 'var(--shadow-card-hover, var(--elevation-2))';
                  el.style.transform = 'translateY(-2px)';
                }}
                onMouseLeave={(e) => {
                  const el = e.currentTarget;
                  el.style.borderColor = 'var(--color-border-default)';
                  el.style.boxShadow = 'none';
                  el.style.transform = 'none';
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--spacing-sm)' }}>
                  <CategoryBadge category={cat} />
                  <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginLeft: 'auto' }}>
                    {comps.length} component{comps.length !== 1 ? 's' : ''}
                  </span>
                </div>
                <h3 style={{
                  fontSize: 'var(--text-h3)',
                  fontWeight: 'var(--font-weight-semibold)',
                  margin: 0,
                  textTransform: 'capitalize',
                }}>
                  {cat}
                </h3>
                <ul style={{
                  margin: 0,
                  padding: 0,
                  listStyle: 'none',
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '2px',
                }}>
                  {comps.slice(0, 8).map((c) => (
                    <li key={c.id} style={{
                      fontSize: 'var(--text-body-sm)',
                      color: 'var(--color-text-secondary)',
                      padding: '2px 0',
                    }}>
                      {c.name}
                    </li>
                  ))}
                  {comps.length > 8 && (
                    <li style={{
                      fontSize: 'var(--text-caption)',
                      color: 'var(--color-text-secondary)',
                      fontStyle: 'italic',
                    }}>
                      +{comps.length - 8} more
                    </li>
                  )}
                </ul>
              </div>
            );
          })}
        </div>
      </section>

      <section style={sectionCardStyle}>
        <h2 style={sectionTitleStyle}>Quick Links</h2>
        <div style={quickLinksGridStyle}>
          {quickLinks.map(({ label, path }) => (
            <button
              key={path}
              style={quickLinkStyle}
              onClick={() => navigate(path)}
              onMouseEnter={(e) => {
                const el = e.currentTarget;
                el.style.borderColor = 'var(--color-border-focus)';
                el.style.background = 'var(--color-bg-primary-default)';
                el.style.color = '#fff';
              }}
              onMouseLeave={(e) => {
                const el = e.currentTarget;
                el.style.borderColor = 'var(--color-border-default)';
                el.style.background = 'var(--color-bg-surface-default)';
                el.style.color = 'var(--color-text-primary)';
              }}
            >
              {'→'} {label}
            </button>
          ))}
        </div>
      </section>

      <section>
        <h2 style={sectionTitleStyle}>Token Summary</h2>
        <div style={tokenGridStyle}>
          {tokenCategories.map((cat) => {
            const examples = getTokenExamples(cat);
            return (
              <div key={cat} style={tokenCategorySectionStyle}>
                <h3 style={tokenCategoryTitleStyle}>{cat}</h3>
                {examples.map((token) => (
                  <TokenDisplay
                    key={token.id}
                    name={token.name}
                    value={token.value}
                    category={token.category}
                    usageCount={token.usage}
                    deprecated={token.deprecated}
                    replacement={token.replacement}
                  />
                ))}
              </div>
            );
          })}
        </div>
      </section>

      <section style={sectionCardStyle}>
        <h2 style={sectionTitleStyle}>Recent Updates</h2>
        <div>
          {recentUpdates.map((update, i) => (
            <div key={update.version} style={i < recentUpdates.length - 1 ? updateCardStyle : updateCardLastStyle}>
              <span style={updateVersionStyle}>{update.version}</span>
              <span style={updateDateStyle}>{update.date}</span>
              <p style={updateDescStyle}>{update.description}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
