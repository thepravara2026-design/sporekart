import { memo } from 'react';
import { SectionHeader, SummaryCard } from '../../../inventory/components';
import { StockStateBadge, StockHealthBadge, AvailabilityBadge } from '../../components';
import { STOCK_STATES, STOCK_HEALTH_LEVELS, AVAILABILITY_LEVELS, RESERVATION_TYPES } from '../../constants';

export const PreviewStatesPage = memo(function PreviewStatesPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Stock State Framework" description="All stock states, health levels, availability levels and reservation types supported by the Stock State Engine." />

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Stock States (15)</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 8 }}>
          {STOCK_STATES.map((s) => (
            <SummaryCard key={s.value} title={s.label}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
                <StockStateBadge state={s.value} />
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{s.description}</span>
              </div>
            </SummaryCard>
          ))}
        </div>
      </div>

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Health Levels (8)</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 8 }}>
          {STOCK_HEALTH_LEVELS.map((h) => (
            <SummaryCard key={h.value} title={h.label}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
                <StockHealthBadge health={h.value} />
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{h.description}</span>
              </div>
            </SummaryCard>
          ))}
        </div>
      </div>

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Availability Levels (5)</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 8 }}>
          {AVAILABILITY_LEVELS.map((a) => (
            <SummaryCard key={a.value} title={a.label}>
              <AvailabilityBadge level={a.value} />
            </SummaryCard>
          ))}
        </div>
      </div>

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Reservation Types (5)</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 8 }}>
          {RESERVATION_TYPES.map((r) => (
            <SummaryCard key={r.value} title={r.label}>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{r.description}</span>
            </SummaryCard>
          ))}
        </div>
      </div>
    </div>
  );
});
