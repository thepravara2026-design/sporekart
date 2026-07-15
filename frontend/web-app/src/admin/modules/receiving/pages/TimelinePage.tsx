import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { ReceivingTimeline } from '../components/ReceivingTimeline';

export const TimelinePage = memo(function TimelinePage() {
  const { timeline, loading } = useReceivingData();

  if (loading) return <div>Loading...</div>;
  if (timeline.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No timeline events recorded yet.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Receiving Timeline ({timeline.length} events)</h3>
      <ReceivingTimeline events={timeline} />
    </div>
  );
});
