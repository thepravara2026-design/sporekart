import React from 'react';
import { MOCK_KPI_CARDS, MOCK_CHART_GROWTH } from '../mock/mockAnalytics';
import { MOCK_ANALYTICS_ACTIVITY } from '../mock/mockActivity';
import { ScoreCard } from './charts/ScoreCard';
import { LineChart } from './charts/LineChart';

const styles: Record<string, React.CSSProperties> = {
  container: { display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' },
  header: { margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)', fontWeight: 600 },
  kpiGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' },
  section: { display: 'flex', flexDirection: 'column', gap: 8 },
  sectionTitle: { margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 },
  activityList: { display: 'flex', flexDirection: 'column', gap: 6 },
  activityItem: { display: 'flex', alignItems: 'center', gap: 10, padding: '10px 12px', borderRadius: 'var(--radius-md)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border)' },
  activityIcon: { width: 28, height: 28, display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-raised)', fontSize: 14 },
  activityContent: { display: 'flex', flexDirection: 'column', gap: 2, flex: 1, minWidth: 0 },
  activityMessage: { margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' },
  activityMeta: { margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' },
  row: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' },
};

export const ExecutiveDashboard: React.FC = React.memo(() => {
  const recentActivity = MOCK_ANALYTICS_ACTIVITY.slice(0, 5);

  return (
    <div style={styles.container} role="region" aria-label="Executive Dashboard">
      <h2 style={styles.header}>Executive Dashboard</h2>
      <div style={styles.kpiGrid} role="list" aria-label="Key performance indicators">
        {MOCK_KPI_CARDS.map((kpi) => (
          <ScoreCard key={kpi.id} label={kpi.label} value={kpi.value} unit={kpi.unit} trend={kpi.trend} trendValue={kpi.trendValue} icon={kpi.icon} color={kpi.color} />
        ))}
      </div>
      <div style={styles.row}>
        <div style={styles.section}>
          <h3 style={styles.sectionTitle}>Growth Trend</h3>
          <LineChart config={MOCK_CHART_GROWTH} />
        </div>
        <div style={styles.section}>
          <h3 style={styles.sectionTitle}>Recent Activity</h3>
          <div style={styles.activityList} role="list" aria-label="Recent analytics activity">
            {recentActivity.map((a) => (
              <div key={a.id} style={styles.activityItem} role="listitem">
                <div style={styles.activityIcon} aria-hidden="true">{a.icon === 'file-text' ? '📄' : a.icon === 'refresh' ? '🔄' : a.icon === 'lightbulb' ? '💡' : a.icon === 'calendar' ? '📅' : a.icon === 'download' ? '⬇️' : a.icon === 'shopping-cart' ? '🛒' : '•'}</div>
                <div style={styles.activityContent}>
                  <p style={styles.activityMessage}>{a.message}</p>
                  <p style={styles.activityMeta}>{a.user} &middot; {new Date(a.timestamp).toLocaleDateString()}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
});

export default ExecutiveDashboard;
