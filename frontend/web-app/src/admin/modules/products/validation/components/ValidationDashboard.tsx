import React from 'react';
import { useValidationAnalytics } from '../state/useValidationAnalytics';
import { MOCK_VALIDATION_ACTIVITY } from '../mock/mockActivity';

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 24 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 4 };
const cl = { fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.5px' } as const;
const cv = { fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', fontWeight: 700 } as const;
const panel: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };
const panelTitle: React.CSSProperties = { fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 };

export const ValidationDashboard: React.FC = React.memo(() => {
  const a = useValidationAnalytics();

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Validation Overview</h2>

      <div style={grid}>
        <div style={card} aria-label="Total Products"><span style={cl}>Total Products</span><span style={cv}>{a.total}</span></div>
        <div style={card} aria-label="Average Score"><span style={cl}>Avg Score</span><span style={cv}>{a.avgOverall}%</span></div>
        <div style={card} aria-label="Low Risk"><span style={cl}>Low Risk</span><span style={cv}>{a.lowRisk}</span></div>
        <div style={card} aria-label="Medium Risk"><span style={cl}>Medium Risk</span><span style={cv}>{a.mediumRisk}</span></div>
        <div style={card} aria-label="High Risk"><span style={cl}>High Risk</span><span style={cv}>{a.highRisk}</span></div>
        <div style={card} aria-label="Critical Risk"><span style={cl}>Critical</span><span style={cv}>{a.criticalRisk}</span></div>
        <div style={card} aria-label="Certified"><span style={cl}>Certified</span><span style={cv}>{a.certified}</span></div>
        <div style={card} aria-label="Needs Improvement"><span style={cl}>Needs Improvement</span><span style={cv}>{a.needsImprovement}</span></div>
        <div style={card} aria-label="Published"><span style={cl}>Published</span><span style={cv}>{a.published}</span></div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 }}>
        <div style={panel} aria-label="Risk distribution">
          <div style={panelTitle}>Risk Distribution</div>
          {a.riskDist.map((r) => {
            const pct = a.total > 0 ? Math.round((r.count / a.total) * 100) : 0;
            return (
              <div key={r.label} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)', minWidth: 80 }}>{r.label}</span>
                <div style={{ flex: 1, height: 20, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
                  <div style={{ height: '100%', width: `${pct}%`, borderRadius: 'var(--radius-xs)', background: r.color, transition: 'width 0.3s' }} />
                </div>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)', minWidth: 24, textAlign: 'right' }}>{r.count}</span>
              </div>
            );
          })}
        </div>

        <div style={panel} aria-label="Certification distribution">
          <div style={panelTitle}>Certification Distribution</div>
          {a.certDist.map((c) => {
            const pct = a.total > 0 ? Math.round((c.count / a.total) * 100) : 0;
            return (
              <div key={c.label} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)', minWidth: 80 }}>{c.label}</span>
                <div style={{ flex: 1, height: 20, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
                  <div style={{ height: '100%', width: `${pct}%`, borderRadius: 'var(--radius-xs)', background: c.color, transition: 'width 0.3s' }} />
                </div>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)', minWidth: 24, textAlign: 'right' }}>{c.count}</span>
              </div>
            );
          })}
        </div>

        <div style={panel} aria-label="Recent activity">
          <div style={panelTitle}>Recent Activity</div>
          {MOCK_VALIDATION_ACTIVITY.slice(0, 6).map((ev) => (
            <div key={ev.id} style={{ display: 'flex', gap: 10, padding: '6px 0', borderBottom: '1px solid var(--color-border)' }}>
              <span style={{ fontSize: 14, opacity: 0.5 }} aria-hidden="true">
                {ev.icon === 'check-circle' && '✅'}{ev.icon === 'award' && '🏆'}{ev.icon === 'shield' && '🛡️'}
                {ev.icon === 'thumbs-up' && '👍'}{ev.icon === 'x-circle' && '❌'}{ev.icon === 'file-text' && '📄'}
                {ev.icon === 'layers' && '📦'}{ev.icon === 'search' && '🔍'}{ev.icon === 'shopping-cart' && '🛒'}
              </span>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-primary)' }}>{ev.message}</div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{ev.user} &middot; {new Date(ev.timestamp).toLocaleDateString()}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
