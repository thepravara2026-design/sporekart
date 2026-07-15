import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { MovementTimelineComponent } from '../components/MovementTimeline';

export const HistoryPage = memo(function HistoryPage() {
  const { timeline, loading } = useMovementData();

  if (loading) return <div>Loading...</div>;
  if (timeline.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No history events recorded yet.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Movement History ({timeline.length} events)</h3>
      <MovementTimelineComponent events={timeline} />
    </div>
  );
});
