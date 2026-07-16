import type { CapacityConfig } from '../../data/enrollmentMockData';

interface CapacityMeterProps {
  capacity: CapacityConfig;
  showLegend?: boolean;
}

const SEGMENTS: { key: keyof CapacityConfig; label: string; color: string }[] = [
  { key: 'occupiedSeats', label: 'Occupied', color: 'var(--color-primary-500)' },
  { key: 'reservedSeats', label: 'Reserved', color: 'var(--color-warning)' },
  { key: 'blockedSeats', label: 'Blocked', color: 'var(--color-danger)' },
  { key: 'availableSeats', label: 'Available', color: 'var(--color-success)' },
];

export function CapacityMeter({ capacity, showLegend = true }: CapacityMeterProps) {
  const total = capacity.maxSeats || 1;

  return (
    <div>
      <div
        role="img"
        aria-label={`Capacity: ${capacity.occupiedSeats} occupied, ${capacity.reservedSeats} reserved, ${capacity.blockedSeats} blocked, ${capacity.availableSeats} available of ${capacity.maxSeats} seats`}
        style={{ display: 'flex', height: 14, borderRadius: 7, overflow: 'hidden', background: 'var(--color-bg-secondary)' }}
      >
        {SEGMENTS.map((seg) => {
          const value = capacity[seg.key] as number;
          const pct = (value / total) * 100;
          if (pct <= 0) return null;
          return <div key={seg.key} style={{ width: `${pct}%`, background: seg.color }} title={`${seg.label}: ${value}`} />;
        })}
      </div>
      {showLegend && (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 12, marginTop: 8 }}>
          {SEGMENTS.map((seg) => (
            <span key={seg.key} style={{ display: 'flex', alignItems: 'center', gap: 5, fontSize: 12, color: 'var(--color-text-tertiary)' }}>
              <span style={{ width: 10, height: 10, borderRadius: 2, background: seg.color }} />
              {seg.label} {capacity[seg.key] as number}
            </span>
          ))}
        </div>
      )}
    </div>
  );
}
