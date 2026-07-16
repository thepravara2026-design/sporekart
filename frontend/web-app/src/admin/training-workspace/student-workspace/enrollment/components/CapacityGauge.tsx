import { memo } from 'react';
import type { CapacityInfo } from '../types';

interface CapacityGaugeProps {
  capacity: CapacityInfo;
}

export const CapacityGauge = memo(function CapacityGauge({ capacity }: CapacityGaugeProps) {
  const color = capacity.utilizationPercent >= 90 ? '#dc2626' : capacity.utilizationPercent >= 75 ? '#ca8a04' : '#16a34a';

  return (
    <div className="capacity-gauge" style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
    }}>
      <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', marginBottom: 8 }}>{capacity.batchName}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 8 }}>
        <div style={{ position: 'relative', width: 60, height: 60 }}>
          <svg width="60" height="60" viewBox="0 0 60 60">
            <circle cx="30" cy="30" r="24" fill="none" stroke="var(--color-bg-skeleton-base)" strokeWidth="6" />
            <circle
              cx="30" cy="30" r="24" fill="none" stroke={color} strokeWidth="6"
              strokeDasharray={`${(capacity.utilizationPercent / 100) * 150.8} 150.8`}
              transform="rotate(-90, 30, 30)"
              style={{ transition: 'stroke-dasharray var(--duration-normal)' }}
            />
            <text x="30" y="30" textAnchor="middle" dominantBaseline="central" fontSize="14" fontWeight="bold" fill={color}>
              {capacity.utilizationPercent}%
            </text>
          </svg>
        </div>
        <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, fontSize: 'var(--text-caption)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <span style={{ color: 'var(--color-text-tertiary)' }}>Max</span>
            <span>{capacity.maxCapacity}</span>
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <span style={{ color: 'var(--color-text-tertiary)' }}>Occupied</span>
            <span>{capacity.occupiedSeats}</span>
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <span style={{ color: 'var(--color-text-tertiary)' }}>Reserved</span>
            <span>{capacity.reservedSeats}</span>
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <span style={{ color: 'var(--color-text-tertiary)' }}>Available</span>
            <span style={{ fontWeight: 'var(--weight-semibold)', color: capacity.availableSeats > 0 ? '#16a34a' : '#dc2626' }}>{capacity.availableSeats}</span>
          </div>
        </div>
      </div>
      <div style={{ height: 4, background: 'var(--color-bg-skeleton-base)', borderRadius: 2, overflow: 'hidden' }}>
        <div style={{
          height: '100%', borderRadius: 2, background: color,
          width: `${capacity.utilizationPercent}%`, transition: 'width var(--duration-normal)',
        }} />
      </div>
    </div>
  );
});
