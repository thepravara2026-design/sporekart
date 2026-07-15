import React from 'react';
import { usePricingAnalytics } from '../state/usePricingAnalytics';
import { MOCK_PRICING_ACTIVITY } from '../mock/mockActivity';

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 24,
};

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))',
  gap: 12,
};

const cardStyle: React.CSSProperties = {
  padding: 16,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  display: 'flex',
  flexDirection: 'column',
  gap: 4,
};

const cardLabel: React.CSSProperties = {
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-tertiary)',
  textTransform: 'uppercase',
  letterSpacing: '0.5px',
};

const cardValue: React.CSSProperties = {
  fontSize: 'var(--text-h3)',
  color: 'var(--color-text-primary)',
  fontWeight: 700,
};

const subGrid: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
  gap: 16,
};

const panelStyle: React.CSSProperties = {
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  padding: 16,
};

const panelTitle: React.CSSProperties = {
  fontSize: 'var(--text-h5)',
  color: 'var(--color-text-primary)',
  fontWeight: 600,
  marginBottom: 12,
};

const barContainer: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 8,
};

const barRow: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 8,
};

const barLabel: React.CSSProperties = {
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-secondary)',
  minWidth: 80,
};

const barTrack: React.CSSProperties = {
  flex: 1,
  height: 20,
  borderRadius: 'var(--radius-xs)',
  background: 'var(--color-bg-surface-raised)',
  overflow: 'hidden',
};

const barFill: React.CSSProperties = {
  height: '100%',
  borderRadius: 'var(--radius-xs)',
  transition: 'width 0.3s',
};

const activityList: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 8,
};

const activityItem: React.CSSProperties = {
  display: 'flex',
  alignItems: 'flex-start',
  gap: 10,
  padding: '8px 0',
  borderBottom: '1px solid var(--color-border)',
};

const activityIcon: React.CSSProperties = {
  fontSize: 16,
  width: 24,
  textAlign: 'center',
  marginTop: 2,
};

const activityContent: React.CSSProperties = {
  flex: 1,
};

const activityMessage: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-primary)',
};

const activityMeta: React.CSSProperties = {
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-tertiary)',
  marginTop: 2,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

export const PricingDashboard: React.FC = React.memo(() => {
  const analytics = usePricingAnalytics();

  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Pricing Dashboard</h2>

      <div style={gridStyle}>
        <div style={cardStyle}>
          <span style={cardLabel}>Total Products</span>
          <span style={cardValue}>{analytics.totalProducts}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Active</span>
          <span style={cardValue}>{analytics.activeProducts}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Pending Approval</span>
          <span style={cardValue}>{analytics.pendingApproval}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Avg MRP</span>
          <span style={cardValue}>₹{analytics.avgMrp}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Avg Selling</span>
          <span style={cardValue}>₹{analytics.avgSelling}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Avg Discount</span>
          <span style={cardValue}>{analytics.avgDiscount}%</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Discount Rules</span>
          <span style={cardValue}>{analytics.totalDiscountRules}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Active Campaigns</span>
          <span style={cardValue}>{analytics.activeCampaigns}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Active Schedules</span>
          <span style={cardValue}>{analytics.activeSchedules}</span>
        </div>
        <div style={cardStyle}>
          <span style={cardLabel}>Commercial Rules</span>
          <span style={cardValue}>{analytics.totalRules}</span>
        </div>
      </div>

      <div style={subGrid}>
        <div style={panelStyle}>
          <div style={panelTitle}>Status Distribution</div>
          <div style={barContainer}>
            {analytics.statusDistribution.map((s) => {
              const pct = analytics.totalProducts > 0 ? Math.round((s.count / analytics.totalProducts) * 100) : 0;
              return (
                <div key={s.status} style={barRow}>
                  <span style={barLabel}>{s.status}</span>
                  <div style={barTrack}>
                    <div style={{ ...barFill, width: `${pct}%`, background: s.color }} />
                  </div>
                  <span style={{ ...barLabel, minWidth: 40, textAlign: 'right' }}>{s.count}</span>
                </div>
              );
            })}
          </div>
        </div>

        <div style={panelStyle}>
          <div style={panelTitle}>GST Distribution</div>
          <div style={barContainer}>
            {analytics.gstDistribution.map((g) => {
              const pct = analytics.totalProducts > 0 ? Math.round((g.count / analytics.totalProducts) * 100) : 0;
              return (
                <div key={g.percentage} style={barRow}>
                  <span style={barLabel}>{g.percentage}%</span>
                  <div style={barTrack}>
                    <div style={{ ...barFill, width: `${pct}%`, background: 'var(--color-accent-blue)' }} />
                  </div>
                  <span style={{ ...barLabel, minWidth: 40, textAlign: 'right' }}>{g.count}</span>
                </div>
              );
            })}
          </div>
        </div>

        <div style={panelStyle}>
          <div style={panelTitle}>Recent Activity</div>
          <div style={activityList}>
            {MOCK_PRICING_ACTIVITY.slice(0, 8).map((a) => (
              <div key={a.id} style={activityItem}>
                <div style={activityIcon} aria-hidden="true">
                  {a.icon === 'tag' && '🏷️'}
                  {a.icon === 'percent' && '💯'}
                  {a.icon === 'megaphone' && '📢'}
                  {a.icon === 'multiple' && '📦'}
                  {a.icon === 'check' && '✅'}
                  {a.icon === 'calendar' && '📅'}
                  {a.icon === 'currency-rupee' && '💰'}
                  {a.icon === 'code' && '📋'}
                </div>
                <div style={activityContent}>
                  <div style={activityMessage}>{a.message}</div>
                  <div style={activityMeta}>{a.user} · {new Date(a.timestamp).toLocaleDateString()}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
});
