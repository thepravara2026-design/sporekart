import type { TrainingBatch } from './types/trainer';

const statusColors: Record<string, string> = {
  UPCOMING: '#1565c0', IN_PROGRESS: '#2e7d32', COMPLETED: '#757575', CANCELLED: '#c62828',
};

export function TrainerBatchCard({ batch }: { batch: TrainingBatch }) {
  const pct = batch.capacity > 0 ? Math.round((batch.enrolledCount / batch.capacity) * 100) : 0;
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: 16, background: 'var(--cp-color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
        <div>
          <h3 style={{ margin: 0, fontSize: 16, color: 'var(--cp-color-text)' }}>{batch.batchName}</h3>
          <div style={{ fontSize: 12, color: 'var(--cp-color-text-muted)', marginTop: 2 }}>{batch.courseName}</div>
        </div>
        <span style={{ background: statusColors[batch.status] || '#888', color: '#fff', padding: '2px 10px', borderRadius: 4, fontSize: 11, fontWeight: 600, whiteSpace: 'nowrap' }}>{batch.status.replace('_', ' ')}</span>
      </div>
      <div style={{ marginBottom: 12 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 12, color: 'var(--cp-color-text-secondary)', marginBottom: 4 }}>
          <span>{batch.enrolledCount} / {batch.capacity} enrolled</span>
          <span>{pct}%</span>
        </div>
        <div style={{ height: 6, background: 'var(--cp-color-border)', borderRadius: 3, overflow: 'hidden' }}>
          <div style={{ height: '100%', width: `${pct}%`, background: pct >= 90 ? 'var(--cp-color-danger)' : pct >= 75 ? 'var(--cp-color-warning)' : 'var(--cp-color-primary)', borderRadius: 3, transition: 'width 0.3s' }} />
        </div>
      </div>
      <div style={{ fontSize: 12, color: 'var(--cp-color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 2 }}>
        <span>Trainer: {batch.trainerName}</span>
        <span>Location: {batch.location}</span>
        <span>{batch.startDate} → {batch.endDate}</span>
      </div>
    </div>
  );
}
