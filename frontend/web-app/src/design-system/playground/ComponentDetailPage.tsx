import { useMemo, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import { componentManifest, ComponentEntry } from './catalog/componentManifest';
import { CodeBlock } from './components/CodeBlock';
import { ComponentPreview } from './components/ComponentPreview';
import { ResponsivePreview } from './components/ResponsivePreview';
import { ThemePreview } from './components/ThemePreview';

const categoryColors: Record<string, string> = {
  core: 'var(--color-info-500, #2196F3)',
  forms: 'var(--color-success-500, #4CAF50)',
  display: 'var(--color-dataviz-categorical-2, #7B1FA2)',
  navigation: 'var(--color-warning-500, #FF9800)',
  feedback: 'var(--color-danger-500, #EF5350)',
  charts: 'var(--color-dataviz-categorical-1, #009688)',
  layout: 'var(--color-neutral-500, #6D6D6D)',
};

const statusBadgeStyles: Record<string, React.CSSProperties> = {
  stable: { background: 'var(--color-success-50, #E8F5E9)', color: 'var(--color-success-600, #2E7D32)', border: '1px solid var(--color-success-500, #4CAF50)' },
  beta: { background: 'var(--color-warning-50, #FFF8E1)', color: 'var(--color-warning-600, #F57F17)', border: '1px solid var(--color-warning-500, #FFB300)' },
  deprecated: { background: 'var(--color-danger-50, #FBE9E7)', color: 'var(--color-danger-600, #C62828)', border: '1px solid var(--color-danger-500, #EF5350)' },
};

const approvalBadgeStyles: Record<string, React.CSSProperties> = {
  approved: { background: 'var(--color-success-50, #E8F5E9)', color: 'var(--color-success-600, #2E7D32)' },
  pending: { background: 'var(--color-warning-50, #FFF8E1)', color: 'var(--color-warning-600, #F57F17)' },
  'in-review': { background: 'var(--color-info-50, #E3F2FD)', color: 'var(--color-info-600, #1565C0)' },
  'changes-requested': { background: 'var(--color-danger-50, #FBE9E7)', color: 'var(--color-danger-600, #C62828)' },
};

const accessibilityIcons: Record<string, string> = {
  pass: '\u2705',
  partial: '\uD83D\uDD36',
  'needs-review': '\u274C',
};

const responsiveLabels: Record<string, string> = {
  pass: '\u2713 Pass',
  partial: '\u25D0 Partial',
  'not-tested': '\u2717 Not Tested',
};

function getExampleCode(entry: ComponentEntry): { label: string; code: string }[] {
  const importPath = `../../design-system/components/${entry.category}/${entry.id}`;
  const componentTag = entry.name;
  const base = [
    {
      label: 'Basic Usage',
      code: `import { ${componentTag} } from '${importPath}';\n\n<${componentTag}\n  variant="${entry.variants[0] || 'default'}"\n  size="medium"\n>\n  ${componentTag} Content\n</${componentTag}>`,
    },
    {
      label: 'With Variants',
      code: `import { ${componentTag} } from '${importPath}';\n\n<${componentTag}\n  variant="${entry.variants[1] || entry.variants[0] || 'default'}"\n  size="large"\n  disabled={false}\n>\n  ${componentTag} Variant\n</${componentTag}>`,
    },
  ];

  if (entry.states.length > 0) {
    base.push({
      label: 'State Example',
      code: `import { ${componentTag} } from '${importPath}';\n\n<${componentTag}\n  variant="${entry.variants[0] || 'default'}"\n  status="${entry.states[0]}"\n>\n  ${entry.states[0]} State\n</${componentTag}>`,
    });
  }

  return base;
}

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default, #FFFFFF)',
  borderRadius: 'var(--radius-card, 8px)',
  padding: 'var(--space-card-padding, 24px)',
  boxShadow: 'var(--shadow-card, 0 1px 2px rgba(29,43,34,0.08))',
  border: '1px solid var(--color-border-default, #E3E6E3)',
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h3, 1.5rem)',
  fontWeight: 600,
  margin: 0,
  color: 'var(--color-text-primary, #1D2B22)',
};

const chipStyle: React.CSSProperties = {
  display: 'inline-block',
  padding: '4px 12px',
  fontSize: 'var(--text-body-sm, 0.875rem)',
  background: 'var(--color-bg-surface, #F7F8F7)',
  border: '1px solid var(--color-border-default, #E3E6E3)',
  borderRadius: 'var(--radius-pill, 9999px)',
  color: 'var(--color-text-primary, #1D2B22)',
};

const detailRowStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  padding: '8px 0',
  borderBottom: '1px solid var(--color-border-default, #E3E6E3)',
  fontSize: 'var(--text-body-sm, 0.875rem)',
};

export default function ComponentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const component = useMemo<ComponentEntry | undefined>(() => {
    if (!id) return undefined;
    return componentManifest.find((c) => c.id.toLowerCase() === id.toLowerCase());
  }, [id]);

  const handleCopyImport = useCallback(() => {
    if (!component) return;
    const text = `import { ${component.name} } from '../../design-system/components/${component.category}/${component.id}';`;
    navigator.clipboard.writeText(text);
  }, [component]);

  const handleCopyUsage = useCallback(() => {
    if (!component) return;
    const text = `<${component.name}\n  variant="${component.variants[0] || 'default'}"\n  size="medium"\n>\n  ${component.name} Content\n</${component.name}>`;
    navigator.clipboard.writeText(text);
  }, [component]);

  if (!component) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh', gap: '16px', padding: 'var(--space-page-y, 48px) var(--space-page-x, 48px)' }}>
        <h1 style={{ fontSize: 'var(--text-h1, 2.25rem)', fontWeight: 700, margin: 0, color: 'var(--color-text-primary, #1D2B22)' }}>Component Not Found</h1>
        <p style={{ color: 'var(--color-text-secondary, #6D6D6D)', fontSize: 'var(--text-body, 1rem)', margin: 0 }}>
          The component "{id}" does not exist in the catalog.
        </p>
        <Link
          to="/design-system/catalog"
          style={{
            padding: '8px 20px',
            background: 'var(--color-bg-primary-default, #2F6F4F)',
            color: '#fff',
            borderRadius: 'var(--radius-button, 4px)',
            textDecoration: 'none',
            fontWeight: 500,
            fontSize: 'var(--text-body, 1rem)',
          }}
        >
          Back to Catalog
        </Link>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap, 32px)', padding: 'var(--space-page-y, 48px) var(--space-page-x, 48px)' }}>
      {/* Back Link */}
      <Link
        to="/design-system/catalog"
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: '6px',
          color: 'var(--color-text-link, #2F6F4F)',
          textDecoration: 'none',
          fontSize: 'var(--text-body-sm, 0.875rem)',
          fontWeight: 500,
        }}
      >
        {'\u2190'} Back to Catalog
      </Link>

      {/* Two Column Layout */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 360px',
        gap: 'var(--spacing-xl, 32px)',
        alignItems: 'start',
      }}>
        <style>{`
          @media (max-width: 1024px) {
            .detail-layout { grid-template-columns: 1fr !important; }
            .detail-sidebar { order: -1; }
          }
        `}</style>
        <div className="detail-layout" style={{ display: 'contents' }}>
          {/* ─── LEFT COLUMN ─── */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-xl, 32px)' }}>
            {/* Header Card */}
            <div style={cardStyle}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '12px', flexWrap: 'wrap' }}>
                <span style={{
                  display: 'inline-block',
                  padding: '2px 10px',
                  borderRadius: 'var(--radius-pill, 9999px)',
                  fontSize: 'var(--text-caption, 0.75rem)',
                  fontWeight: 500,
                  color: '#fff',
                  background: categoryColors[component.category] || 'var(--color-neutral-500, #6D6D6D)',
                  textTransform: 'capitalize',
                }}>
                  {component.category}
                </span>
                <h1 style={{ fontSize: 'var(--text-h1, 2.25rem)', fontWeight: 700, margin: 0, color: 'var(--color-text-primary, #1D2B22)' }}>
                  {component.name}
                </h1>
                <span style={{ fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-disabled, #A8A8A8)' }}>
                  v{component.version}
                </span>
              </div>

              <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', alignItems: 'center' }}>
                <span style={statusBadgeStyles[component.status] || statusBadgeStyles.beta}>{component.status}</span>
                <span style={{ ...approvalBadgeStyles[component.approvalStatus] || approvalBadgeStyles.pending, display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-sm, 4px)', fontSize: 'var(--text-caption, 0.75rem)', fontWeight: 500, textTransform: 'capitalize' }}>
                  {component.approvalStatus}
                </span>
                <span style={{ fontSize: 'var(--text-caption, 0.75rem)' }}>
                  {accessibilityIcons[component.accessibilityStatus]} a11y
                </span>
                <span style={{ fontSize: 'var(--text-caption, 0.75rem)', fontWeight: 500 }}>
                  {responsiveLabels[component.responsiveStatus]}
                </span>
              </div>
            </div>

            {/* Overview */}
            <div style={cardStyle}>
              <h2 style={sectionTitleStyle}>Overview</h2>
              <div style={{ marginTop: '16px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div>
                  <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 4px', color: 'var(--color-text-primary, #1D2B22)' }}>Design Purpose</h3>
                  <p style={{ fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-secondary, #6D6D6D)', margin: 0, lineHeight: 1.6 }}>{component.designPurpose}</p>
                </div>
                <div>
                  <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 4px', color: 'var(--color-text-primary, #1D2B22)' }}>Business Usage</h3>
                  <p style={{ fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-secondary, #6D6D6D)', margin: 0, lineHeight: 1.6 }}>{component.businessUsage}</p>
                </div>
              </div>
            </div>

            {/* Variants */}
            <div style={cardStyle}>
              <h2 style={sectionTitleStyle}>Variants</h2>
              <div style={{ marginTop: '12px', display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                {component.variants.map((v) => (
                  <span key={v} style={chipStyle}>{v}</span>
                ))}
              </div>
            </div>

            {/* States */}
            <div style={cardStyle}>
              <h2 style={sectionTitleStyle}>States</h2>
              <div style={{ marginTop: '12px', display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                {component.states.map((s) => (
                  <span key={s} style={chipStyle}>{s}</span>
                ))}
              </div>
            </div>

            {/* Design Tokens */}
            <div style={cardStyle}>
              <h2 style={sectionTitleStyle}>Design Tokens Used</h2>
              <div style={{ marginTop: '12px', display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                {component.tokens.map((t) => (
                  <span
                    key={t}
                    style={{
                      ...chipStyle,
                      fontFamily: 'monospace',
                      fontSize: 'var(--text-caption, 0.75rem)',
                    }}
                  >
                    {t}
                  </span>
                ))}
              </div>
            </div>

            {/* Code Usage Examples */}
            <div style={cardStyle}>
              <h2 style={sectionTitleStyle}>Code Usage Examples</h2>
              <div style={{ marginTop: '16px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {getExampleCode(component).map((ex, i) => (
                  <div key={i}>
                    <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 8px', color: 'var(--color-text-primary, #1D2B22)' }}>{ex.label}</h3>
                    <CodeBlock code={ex.code} language="tsx" />
                  </div>
                ))}
              </div>
            </div>

            {/* Keyboard Shortcuts */}
            {component.keyboardShortcuts.length > 0 && (
              <div style={cardStyle}>
                <h2 style={sectionTitleStyle}>Keyboard Shortcuts</h2>
                <div style={{ marginTop: '12px', overflowX: 'auto' }}>
                  <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm, 0.875rem)' }}>
                    <thead>
                      <tr style={{ borderBottom: '2px solid var(--color-border-default, #E3E6E3)' }}>
                        <th style={{ textAlign: 'left', padding: '8px 12px', fontWeight: 600, color: 'var(--color-text-primary, #1D2B22)' }}>Key</th>
                        <th style={{ textAlign: 'left', padding: '8px 12px', fontWeight: 600, color: 'var(--color-text-primary, #1D2B22)' }}>Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      {component.keyboardShortcuts.map((ks, i) => (
                        <tr key={i} style={{ borderBottom: '1px solid var(--color-border-default, #E3E6E3)' }}>
                          <td style={{ padding: '8px 12px', fontFamily: 'monospace', fontWeight: 500 }}>{ks.key}</td>
                          <td style={{ padding: '8px 12px', color: 'var(--color-text-secondary, #6D6D6D)' }}>{ks.description}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}

            {/* ARIA Roles */}
            {component.ariaRoles.length > 0 && (
              <div style={cardStyle}>
                <h2 style={sectionTitleStyle}>ARIA Roles</h2>
                <div style={{ marginTop: '12px', display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                  {component.ariaRoles.map((role) => (
                    <span key={role} style={{ ...chipStyle, fontFamily: 'monospace' }}>{role}</span>
                  ))}
                </div>
              </div>
            )}

            {/* Known Limitations */}
            {component.knownLimitations.length > 0 && (
              <div style={cardStyle}>
                <h2 style={sectionTitleStyle}>Known Limitations</h2>
                <ul style={{ margin: '12px 0 0', paddingLeft: '20px', fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-secondary, #6D6D6D)', lineHeight: 1.8 }}>
                  {component.knownLimitations.map((lim, i) => (
                    <li key={i}>{lim}</li>
                  ))}
                </ul>
              </div>
            )}

            {/* Future Enhancements */}
            {component.futureEnhancements.length > 0 && (
              <div style={cardStyle}>
                <h2 style={sectionTitleStyle}>Future Enhancements</h2>
                <ul style={{ margin: '12px 0 0', paddingLeft: '20px', fontSize: 'var(--text-body-sm, 0.875rem)', color: 'var(--color-text-secondary, #6D6D6D)', lineHeight: 1.8 }}>
                  {component.futureEnhancements.map((fe, i) => (
                    <li key={i}>{fe}</li>
                  ))}
                </ul>
              </div>
            )}
          </div>

          {/* ─── RIGHT COLUMN (Sidebar) ─── */}
          <div className="detail-sidebar" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--spacing-lg, 24px)' }}>
            {/* Component Preview */}
            <div style={cardStyle}>
              <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 12px', color: 'var(--color-text-primary, #1D2B22)' }}>Component Preview</h3>
              <ComponentPreview title={component.name} description={component.description} component={<div style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-secondary, #6D6D6D)' }}>Interactive preview available in sandbox mode</div>} />
            </div>

            {/* Responsive Preview */}
            <div style={cardStyle}>
              <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 12px', color: 'var(--color-text-primary, #1D2B22)' }}>Responsive Preview</h3>
              <ResponsivePreview><div style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-secondary, #6D6D6D)' }}>Responsive preview of {component.name}</div></ResponsivePreview>
            </div>

            {/* Theme Preview */}
            <div style={cardStyle}>
              <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 12px', color: 'var(--color-text-primary, #1D2B22)' }}>Theme Preview</h3>
              <ThemePreview><div style={{ padding: '24px', textAlign: 'center', color: 'var(--color-text-secondary, #6D6D6D)' }}>Theme preview of {component.name}</div></ThemePreview>
            </div>

            {/* Quick Info */}
            <div style={cardStyle}>
              <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 12px', color: 'var(--color-text-primary, #1D2B22)' }}>Quick Info</h3>
              <div style={detailRowStyle}>
                <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Version</span>
                <span style={{ fontWeight: 500 }}>{component.version}</span>
              </div>
              <div style={detailRowStyle}>
                <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Owner</span>
                <span style={{ fontWeight: 500 }}>{component.owner}</span>
              </div>
              <div style={detailRowStyle}>
                <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Last Updated</span>
                <span style={{ fontWeight: 500 }}>{component.lastUpdated}</span>
              </div>
              <div style={detailRowStyle}>
                <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Dependencies</span>
                <span style={{ fontWeight: 500 }}>{component.dependencies.length > 0 ? component.dependencies.join(', ') : 'None'}</span>
              </div>
              <div style={{ ...detailRowStyle, borderBottom: 'none' }}>
                <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Related</span>
                <span style={{ fontWeight: 500, display: 'flex', gap: '4px', flexWrap: 'wrap', justifyContent: 'flex-end' }}>
                  {component.relatedComponents.map((rc) => (
                    <Link
                      key={rc}
                      to={`/design-system/component/${rc.toLowerCase().replace(/\s+/g, '-')}`}
                      style={{ color: 'var(--color-text-link, #2F6F4F)', textDecoration: 'none' }}
                    >
                      {rc}
                    </Link>
                  ))}
                </span>
              </div>
            </div>

            {/* Review Status */}
            <div style={cardStyle}>
              <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 12px', color: 'var(--color-text-primary, #1D2B22)' }}>Review Status</h3>
              <div style={detailRowStyle}>
                <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Status</span>
                <span style={{ ...approvalBadgeStyles[component.reviewStatus] || approvalBadgeStyles.pending, display: 'inline-block', padding: '2px 8px', borderRadius: 'var(--radius-sm, 4px)', fontSize: 'var(--text-caption, 0.75rem)', fontWeight: 500, textTransform: 'capitalize' }}>
                  {component.reviewStatus}
                </span>
              </div>
              {component.reviewer && (
                <div style={detailRowStyle}>
                  <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Reviewer</span>
                  <span style={{ fontWeight: 500 }}>{component.reviewer}</span>
                </div>
              )}
              {component.reviewDate && (
                <div style={detailRowStyle}>
                  <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Review Date</span>
                  <span style={{ fontWeight: 500 }}>{component.reviewDate}</span>
                </div>
              )}
              {component.approvalDate && (
                <div style={detailRowStyle}>
                  <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Approval Date</span>
                  <span style={{ fontWeight: 500 }}>{component.approvalDate}</span>
                </div>
              )}
              {component.pendingIssues.length > 0 && (
                <div style={detailRowStyle}>
                  <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Pending Issues</span>
                  <span style={{ fontWeight: 500, textAlign: 'right', maxWidth: '180px' }}>{component.pendingIssues.join('; ')}</span>
                </div>
              )}
              {component.knownBugs.length > 0 && (
                <div style={{ ...detailRowStyle, borderBottom: 'none' }}>
                  <span style={{ color: 'var(--color-text-secondary, #6D6D6D)' }}>Known Bugs</span>
                  <span style={{ fontWeight: 500, textAlign: 'right', maxWidth: '180px' }}>{component.knownBugs.join('; ')}</span>
                </div>
              )}
            </div>

            {/* Developer Experience */}
            <div style={cardStyle}>
              <h3 style={{ fontSize: 'var(--text-body, 1rem)', fontWeight: 600, margin: '0 0 12px', color: 'var(--color-text-primary, #1D2B22)' }}>Developer Experience</h3>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <button
                  onClick={handleCopyImport}
                  style={{
                    width: '100%',
                    padding: '10px 14px',
                    fontSize: 'var(--text-body-sm, 0.875rem)',
                    fontWeight: 500,
                    background: 'var(--color-bg-primary-default, #2F6F4F)',
                    color: '#fff',
                    border: 'none',
                    borderRadius: 'var(--radius-button, 4px)',
                    cursor: 'pointer',
                    transition: 'background var(--transition-fast, 100ms)',
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.background = 'var(--color-bg-primary-hover, #265D3F)';
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.background = 'var(--color-bg-primary-default, #2F6F4F)';
                  }}
                >
                  Copy Import Path
                </button>
                <button
                  onClick={handleCopyUsage}
                  style={{
                    width: '100%',
                    padding: '10px 14px',
                    fontSize: 'var(--text-body-sm, 0.875rem)',
                    fontWeight: 500,
                    background: 'transparent',
                    color: 'var(--color-text-primary, #1D2B22)',
                    border: '1px solid var(--color-border-default, #E3E6E3)',
                    borderRadius: 'var(--radius-button, 4px)',
                    cursor: 'pointer',
                    transition: 'all var(--transition-fast, 100ms)',
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.borderColor = 'var(--color-border-hover, #A5D6A7)';
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.borderColor = 'var(--color-border-default, #E3E6E3)';
                  }}
                >
                  Copy Usage Snippet
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
