import { memo } from 'react';
import type { Batch } from '../types';
import { BATCH_STATUS_LABELS } from '../types';

interface BatchCardProps {
  batch: Batch;
}

const statusColors: Record<string, string> = {
  active: '#2563eb', filling: '#ca8a04', full: '#dc2626', completed: '#16a34a', cancelled: '#6b7280',
};

export const BatchCard = memo(function BatchCard({ batch }: BatchCardProps) {
  const fillPercent = Math.round((batch.filledSeats / batch.capacity) * 100);

  return (
    <div className="batch-card" style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{batch.batchName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{batch.batchId}</div>
        </div>
        <span style={{
          padding: '2px 8px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)',
          fontWeight: 'var(--weight-medium)', color: '#fff',
          backgroundColor: statusColors[batch.status],
        }}>
          {BATCH_STATUS_LABELS[batch.status]}
        </span>
      </div>

      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{batch.courseName}</div>

      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{batch.slot}</span>
        <span>&middot;</span>
        <span style={{ textTransform: 'capitalize' }}>{batch.trainingMode}</span>
        <span>&middot;</span>
        <span>{batch.startDate} – {batch.endDate}</span>
      </div>

      <div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', marginBottom: 4 }}>
          <span>Capacity</span>
          <span>{batch.filledSeats}/{batch.capacity} ({fillPercent}%)</span>
        </div>
        <div style={{ height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
          <div style={{
            height: '100%', borderRadius: 3,
            background: fillPercent >= 100 ? '#dc2626' : fillPercent >= 80 ? '#ca8a04' : '#2563eb',
            width: `${fillPercent}%`, transition: 'width var(--duration-normal)',
          }} />
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 4 }}>
          <span>{batch.availableSeats} available</span>
          <span>{batch.reservedSeats} reserved</span>
        </div>
      </div>
    </div>
  );
});
