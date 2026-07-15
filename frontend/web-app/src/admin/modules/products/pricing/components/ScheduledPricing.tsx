import React from 'react';
import type { PriceSchedule } from '../types';

interface ScheduledPricingProps {
  schedules: PriceSchedule[];
}

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const cardStyle: React.CSSProperties = {
  padding: 16,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  display: 'flex',
  flexDirection: 'column',
  gap: 8,
};

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))',
  gap: 12,
};

const detailRow: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  padding: '4px 0',
  fontSize: 'var(--text-body-xs)',
  borderBottom: '1px solid var(--color-border)',
};

const statusBadge: React.CSSProperties = {
  padding: '2px 8px',
  borderRadius: 10,
  fontSize: 'var(--text-body-xs)',
  fontWeight: 600,
};

const actionBtn: React.CSSProperties = {
  padding: '4px 12px',
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-body-xs)',
  cursor: 'pointer',
};

const scheduleStatus: Record<string, { label: string; color: string }> = {
  draft: { label: 'Draft', color: 'var(--color-accent-yellow)' },
  active: { label: 'Active', color: 'var(--color-accent-green)' },
  scheduled: { label: 'Scheduled', color: 'var(--color-accent-blue)' },
  expired: { label: 'Expired', color: 'var(--color-accent-gray)' },
  cancelled: { label: 'Cancelled', color: 'var(--color-accent-red)' },
};

const scheduleTypes: Record<string, string> = {
  campaign: 'Campaign',
  seasonal: 'Seasonal',
  limited_time: 'Limited Time',
  flash_sale: 'Flash Sale',
  weekend_offer: 'Weekend Offer',
  festival: 'Festival',
  launch: 'Launch',
};

function formatPrice(amount: number): string {
  return `₹${amount.toFixed(2)}`;
}

export const ScheduledPricing: React.FC<ScheduledPricingProps> = React.memo(({ schedules }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Scheduled Pricing</h2>
      <div style={gridStyle}>
        {schedules.length === 0 && (
          <div style={{ gridColumn: '1 / -1', textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
            No pricing schedules created
          </div>
        )}
        {schedules.map((s) => {
          const st = scheduleStatus[s.status] ?? { label: s.status, color: 'var(--color-accent-gray)' };
          return (
            <div key={s.id} style={cardStyle}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
                    {s.name}
                  </div>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                    {scheduleTypes[s.type] ?? s.type} · {s.pricing.entityType}
                  </div>
                </div>
                <span style={{ ...statusBadge, background: `${st.color}20`, color: st.color }}>{st.label}</span>
              </div>

              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Start</span>
                <span style={{ fontWeight: 500 }}>{s.startDate}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>End</span>
                <span style={{ fontWeight: 500 }}>{s.endDate}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Priority</span>
                <span style={{ fontWeight: 500 }}>{s.priority}</span>
              </div>
              {s.campaign && (
                <div style={detailRow}>
                  <span style={{ color: 'var(--color-text-tertiary)' }}>Campaign</span>
                  <span style={{ fontWeight: 500 }}>{s.campaign}</span>
                </div>
              )}

              {s.pricing.prices.length > 0 && (
                <div>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>
                    Scheduled Prices:
                  </div>
                  {s.pricing.prices.map((p, i) => (
                    <div key={i} style={detailRow}>
                      <span style={{ color: 'var(--color-text-tertiary)', textTransform: 'capitalize' }}>{p.tier}</span>
                      <span style={{ fontWeight: 500 }}>{formatPrice(p.amount)}</span>
                    </div>
                  ))}
                </div>
              )}

              {s.notes && (
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', fontStyle: 'italic', marginTop: 4 }}>
                  {s.notes}
                </div>
              )}

              <div style={{ display: 'flex', gap: 8, marginTop: 8, paddingTop: 8, borderTop: '1px solid var(--color-border)' }}>
                <button style={actionBtn}>Preview</button>
                <button style={actionBtn}>Edit</button>
                <button style={actionBtn}>Duplicate</button>
                {s.status === 'draft' && <button style={{ ...actionBtn, color: 'var(--color-accent-green)', fontWeight: 600 }}>Activate</button>}
                {s.status === 'active' && <button style={{ ...actionBtn, color: 'var(--color-accent-red)' }}>Cancel</button>}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
});
