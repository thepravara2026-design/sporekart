import React from 'react';
import { useSeoAnalytics } from '../state/useSeoAnalytics';
import { MOCK_SEO_ACTIVITY } from '../mock/mockActivity';

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 24 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 4 };
const cl = { fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.5px' } as const;
const cv = { fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', fontWeight: 700 } as const;

export const SeoDashboard: React.FC = React.memo(() => {
  const a = useSeoAnalytics();
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>SEO Dashboard</h2>
      <div style={grid}>
        <div style={card}><span style={cl}>Total Products</span><span style={cv}>{a.total}</span></div>
        <div style={card}><span style={cl}>Published</span><span style={cv}>{a.published}</span></div>
        <div style={card}><span style={cl}>Draft</span><span style={cv}>{a.draft}</span></div>
        <div style={card}><span style={cl}>Ready SEO</span><span style={cv}>{a.readySeo}</span></div>
        <div style={card}><span style={cl}>Avg SEO Score</span><span style={cv}>{a.avgSeoScore}%</span></div>
        <div style={card}><span style={cl}>Avg AI Readiness</span><span style={cv}>{a.avgAiScore}%</span></div>
        <div style={card}><span style={cl}>Avg Marketplace</span><span style={cv}>{a.avgMpScore}%</span></div>
        <div style={card}><span style={cl}>Marketplace Ready</span><span style={cv}>{a.marketplaceReady}</span></div>
        <div style={card}><span style={cl}>With Errors</span><span style={cv}>{a.withErrors}</span></div>
        <div style={card}><span style={cl}>Publishing Events</span><span style={cv}>{a.totalEvents}</span></div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 }}>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 }}>Publishing Status</div>
          {a.statusDist.map((s) => {
            const pct = a.total > 0 ? Math.round((s.count / a.total) * 100) : 0;
            return (
              <div key={s.status} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)', minWidth: 80 }}>{s.status}</span>
                <div style={{ flex: 1, height: 20, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
                  <div style={{ height: '100%', width: `${pct}%`, borderRadius: 'var(--radius-xs)', background: s.color, transition: 'width 0.3s' }} />
                </div>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)', minWidth: 30, textAlign: 'right' }}>{s.count}</span>
              </div>
            );
          })}
        </div>

        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 }}>Score Overview</div>
          <ScoreBar label="SEO Health" score={a.avgSeoScore} color="var(--color-accent-green)" />
          <ScoreBar label="AI Readiness" score={a.avgAiScore} color="var(--color-accent-blue)" />
          <ScoreBar label="Marketplace" score={a.avgMpScore} color="var(--color-accent-orange)" />
        </div>

        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 }}>Recent Activity</div>
          {MOCK_SEO_ACTIVITY.slice(0, 7).map((ev) => (
            <div key={ev.id} style={{ display: 'flex', gap: 10, padding: '5px 0', borderBottom: '1px solid var(--color-border)' }}>
              <span style={{ fontSize: 14, opacity: 0.5 }} aria-hidden="true">
                {ev.icon === 'search' && '🔍'}{ev.icon === 'file-text' && '📄'}{ev.icon === 'code' && '</>'}{ev.icon === 'link' && '🔗'}
                {ev.icon === 'send' && '📤'}{ev.icon === 'trending-up' && '📈'}{ev.icon === 'shopping-cart' && '🛒'}{ev.icon === 'archive' && '📁'}
                {ev.icon === 'cpu' && '🤖'}{ev.icon === 'calendar' && '📅'}
              </span>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-primary)' }}>{ev.message}</div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{ev.user} · {new Date(ev.timestamp).toLocaleDateString()}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});

function ScoreBar({ label, score, color }: { label: string; score: number; color: string }) {
  return (
    <div style={{ marginBottom: 12 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-xs)', marginBottom: 4 }}>
        <span style={{ color: 'var(--color-text-secondary)' }}>{label}</span>
        <span style={{ fontWeight: 700, color }}>{score}%</span>
      </div>
      <div style={{ height: 8, borderRadius: 4, background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
        <div style={{ height: '100%', width: `${score}%`, borderRadius: 4, background: color, transition: 'width 0.5s' }} />
      </div>
    </div>
  );
}
