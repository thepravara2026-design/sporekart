import React from 'react';
import { componentManifest, ComponentEntry } from '../catalog/componentManifest';

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap)',
  padding: 'var(--space-page-y) var(--space-page-x)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  flexWrap: 'wrap',
  gap: '12px',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h1)',
  fontWeight: 'var(--weight-bold)',
  margin: 0,
};

const dateStyle: React.CSSProperties = {
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-secondary)',
};

const statsRowStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))',
  gap: '12px',
};

const statCardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-card)',
  padding: '16px',
  display: 'flex',
  flexDirection: 'column',
  gap: '4px',
};

const statValueStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2)',
  fontWeight: 'var(--weight-bold)',
  color: 'var(--color-text-primary)',
  margin: 0,
};

const statLabelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

const sectionStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
};

const sectionTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h2)',
  fontWeight: 'var(--weight-semibold)',
  margin: 0,
};

const cardStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-card)',
  padding: '20px',
};

const tallyRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '16px',
  flexWrap: 'wrap',
  marginBottom: '12px',
};

const tallyItemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '6px',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-primary)',
};

const tallyDotStyle = (color: string): React.CSSProperties => ({
  width: '12px',
  height: '12px',
  borderRadius: '50%',
  background: color,
  flexShrink: 0,
});

const progressBarBgStyle: React.CSSProperties = {
  width: '100%',
  height: '8px',
  background: 'var(--color-bg-surface-raised)',
  borderRadius: 'var(--radius-full)',
  overflow: 'hidden',
};

const progressBarFillStyle = (width: string, color: string): React.CSSProperties => ({
  width,
  height: '100%',
  background: color,
  borderRadius: 'var(--radius-full)',
  transition: 'width 0.3s',
});

const barRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '8px',
  padding: '4px 0',
};

const barLabelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  minWidth: '100px',
  flexShrink: 0,
};

const barCountStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-primary)',
  fontWeight: 'var(--weight-semibold)',
  minWidth: '30px',
  textAlign: 'right',
};

const listStyle: React.CSSProperties = {
  margin: 0,
  paddingLeft: '20px',
  color: 'var(--color-text-secondary)',
  lineHeight: '1.8',
  fontSize: 'var(--text-body)',
};

function formatDate(): string {
  const d = new Date();
  return d.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
}

function pct(n: number, total: number): string {
  if (total === 0) return '0%';
  return `${Math.round((n / total) * 100)}%`;
}

const categoryColors: Record<string, string> = {
  core: 'var(--color-dataViz-1)',
  forms: 'var(--color-dataViz-2)',
  display: 'var(--color-dataViz-3)',
  navigation: 'var(--color-dataViz-4)',
  feedback: 'var(--color-dataViz-5)',
  charts: 'var(--color-dataViz-6)',
  layout: 'var(--color-text-secondary)',
};

export default function QualityDashboard() {
  const total = componentManifest.length;
  const withDocs = componentManifest.filter((c: ComponentEntry) => c.docsPath).length;
  const a11yPass = componentManifest.filter((c: ComponentEntry) => c.accessibilityStatus === 'pass').length;
  const a11yPartial = componentManifest.filter((c: ComponentEntry) => c.accessibilityStatus === 'partial').length;
  const a11yNeedsReview = componentManifest.filter((c: ComponentEntry) => c.accessibilityStatus === 'needs-review').length;
  const responsivePass = componentManifest.filter((c: ComponentEntry) => c.responsiveStatus === 'pass').length;
  const responsivePartial = componentManifest.filter((c: ComponentEntry) => c.responsiveStatus === 'partial').length;
  const responsiveNotTested = componentManifest.filter((c: ComponentEntry) => c.responsiveStatus === 'not-tested').length;
  const approved = componentManifest.filter((c: ComponentEntry) => c.approvalStatus === 'approved').length;
  const inReview = componentManifest.filter((c: ComponentEntry) => c.approvalStatus === 'in-review').length;
  const reviewed = componentManifest.filter((c: ComponentEntry) => c.reviewStatus === 'approved').length;
  const changesRequested = componentManifest.filter((c: ComponentEntry) => c.reviewStatus === 'changes-requested').length;
  const reviewPending = componentManifest.filter((c: ComponentEntry) => c.reviewStatus === 'pending').length;

  const missingDocs = componentManifest.filter(
    (c: ComponentEntry) => !c.docsPath || c.docsPath.trim() === ''
  );

  const nonPassingA11y = componentManifest.filter(
    (c: ComponentEntry) => c.accessibilityStatus !== 'pass'
  );

  const nonPassingResponsive = componentManifest.filter(
    (c: ComponentEntry) => c.responsiveStatus !== 'pass'
  );

  const categories = [...new Set(componentManifest.map((c: ComponentEntry) => c.category))];
  const categoryCounts = categories.map((cat) => ({
    category: cat,
    count: componentManifest.filter((c: ComponentEntry) => c.category === cat).length,
  }));

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h1 style={titleStyle}>Quality Dashboard</h1>
        <span style={dateStyle}>{formatDate()}</span>
      </div>

      <div style={statsRowStyle}>
        <div style={statCardStyle}>
          <p style={statValueStyle}>{total}</p>
          <span style={statLabelStyle}>Total Components</span>
        </div>
        <div style={statCardStyle}>
          <p style={statValueStyle}>{pct(withDocs, total)}</p>
          <span style={statLabelStyle}>Documented Coverage</span>
        </div>
        <div style={statCardStyle}>
          <p style={statValueStyle}>{pct(a11yPass, total)}</p>
          <span style={statLabelStyle}>Accessibility Pass Rate</span>
        </div>
        <div style={statCardStyle}>
          <p style={statValueStyle}>{pct(responsivePass, total)}</p>
          <span style={statLabelStyle}>Responsive Pass Rate</span>
        </div>
        <div style={statCardStyle}>
          <p style={statValueStyle}>{pct(reviewed, total)}</p>
          <span style={statLabelStyle}>Review Progress</span>
        </div>
        <div style={statCardStyle}>
          <p style={statValueStyle}>{pct(approved, total)}</p>
          <span style={statLabelStyle}>Approval Percentage</span>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <div style={sectionStyle}>
          <h2 style={sectionTitleStyle}>Accessibility Status</h2>
          <div style={cardStyle}>
            <div style={tallyRowStyle}>
              <div style={tallyItemStyle}>
                <span style={tallyDotStyle('var(--color-success-500)')} />
                <span>Pass: {a11yPass}</span>
              </div>
              <div style={tallyItemStyle}>
                <span style={tallyDotStyle('var(--color-warning-500)')} />
                <span>Partial: {a11yPartial}</span>
              </div>
              <div style={tallyItemStyle}>
                <span style={tallyDotStyle('var(--color-danger-500)')} />
                <span>Needs Review: {a11yNeedsReview}</span>
              </div>
            </div>
            {nonPassingA11y.length > 0 && (
              <>
                <p style={{ margin: '8px 0 4px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  Non-passing components:
                </p>
                <ul style={listStyle}>
                  {nonPassingA11y.map((c: ComponentEntry) => (
                    <li key={c.id}>
                      {c.name} — {c.accessibilityStatus}
                    </li>
                  ))}
                </ul>
              </>
            )}
          </div>
        </div>

        <div style={sectionStyle}>
          <h2 style={sectionTitleStyle}>Responsive Status</h2>
          <div style={cardStyle}>
            <div style={tallyRowStyle}>
              <div style={tallyItemStyle}>
                <span style={tallyDotStyle('var(--color-success-500)')} />
                <span>Pass: {responsivePass}</span>
              </div>
              <div style={tallyItemStyle}>
                <span style={tallyDotStyle('var(--color-warning-500)')} />
                <span>Partial: {responsivePartial}</span>
              </div>
              <div style={tallyItemStyle}>
                <span style={tallyDotStyle('var(--color-danger-500)')} />
                <span>Not Tested: {responsiveNotTested}</span>
              </div>
            </div>
            {nonPassingResponsive.length > 0 && (
              <>
                <p style={{ margin: '8px 0 4px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  Non-passing components:
                </p>
                <ul style={listStyle}>
                  {nonPassingResponsive.map((c: ComponentEntry) => (
                    <li key={c.id}>
                      {c.name} — {c.responsiveStatus}
                    </li>
                  ))}
                </ul>
              </>
            )}
          </div>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Documentation Coverage</h2>
        <div style={cardStyle}>
          <div style={barRowStyle}>
            <span style={barLabelStyle}>With docs</span>
            <div style={progressBarBgStyle}>
              <div style={progressBarFillStyle(pct(withDocs, total), 'var(--color-success-500)')} />
            </div>
            <span style={barCountStyle}>{withDocs}</span>
          </div>
          <div style={barRowStyle}>
            <span style={barLabelStyle}>Missing docs</span>
            <div style={progressBarBgStyle}>
              <div style={progressBarFillStyle(pct(missingDocs.length, total), 'var(--color-danger-500)')} />
            </div>
            <span style={barCountStyle}>{missingDocs.length}</span>
          </div>
          {missingDocs.length > 0 && (
            <>
              <p style={{ margin: '8px 0 4px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                Components missing docs:
              </p>
              <ul style={listStyle}>
                {missingDocs.map((c: ComponentEntry) => (
                  <li key={c.id}>{c.name}</li>
                ))}
              </ul>
            </>
          )}
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Review Progress</h2>
        <div style={cardStyle}>
          <div style={tallyRowStyle}>
            <div style={tallyItemStyle}>
              <span style={tallyDotStyle('var(--color-success-500)')} />
              <span>Approved: {reviewed}</span>
            </div>
            <div style={tallyItemStyle}>
              <span style={tallyDotStyle('var(--color-warning-500)')} />
              <span>Pending: {reviewPending}</span>
            </div>
            <div style={tallyItemStyle}>
              <span style={tallyDotStyle('var(--color-danger-500)')} />
              <span>Changes Requested: {changesRequested}</span>
            </div>
          </div>
          <div style={barRowStyle}>
            <span style={barLabelStyle}>Approved</span>
            <div style={progressBarBgStyle}>
              <div style={progressBarFillStyle(pct(reviewed, total), 'var(--color-success-500)')} />
            </div>
            <span style={barCountStyle}>{reviewed}</span>
          </div>
          <div style={barRowStyle}>
            <span style={barLabelStyle}>Pending</span>
            <div style={progressBarBgStyle}>
              <div style={progressBarFillStyle(pct(reviewPending, total), 'var(--color-warning-500)')} />
            </div>
            <span style={barCountStyle}>{reviewPending}</span>
          </div>
          <div style={barRowStyle}>
            <span style={barLabelStyle}>In Review</span>
            <div style={progressBarBgStyle}>
              <div style={progressBarFillStyle(pct(inReview, total), 'var(--color-info-500)')} />
            </div>
            <span style={barCountStyle}>{inReview}</span>
          </div>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2 style={sectionTitleStyle}>Component Coverage by Category</h2>
        <div style={cardStyle}>
          {categoryCounts.map(({ category, count }) => (
            <div key={category} style={barRowStyle}>
              <span style={barLabelStyle}>{category}</span>
              <div style={progressBarBgStyle}>
                <div
                  style={progressBarFillStyle(
                    pct(count, total),
                    categoryColors[category] || 'var(--color-text-secondary)'
                  )}
                />
              </div>
              <span style={barCountStyle}>{count}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
