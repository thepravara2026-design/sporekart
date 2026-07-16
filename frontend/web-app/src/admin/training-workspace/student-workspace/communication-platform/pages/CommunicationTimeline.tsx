import { useState } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { TimelineEvent } from '../components/TimelineEvent';
import { EmptyState } from '../components/EmptyStates';

export default function CommunicationTimeline() {
  const { timeline } = useCommunication();
  const stages = [...new Set(timeline.map((e) => e.stage))];
  const [selectedStage, setSelectedStage] = useState('all');

  const filtered = selectedStage === 'all' ? timeline : timeline.filter((e) => e.stage === selectedStage);

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Communication Timeline</h1>
        <SharedFilters currentPage="communication/timeline" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        <button onClick={() => setSelectedStage('all')} style={{
          padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
          background: selectedStage === 'all' ? 'var(--color-bg-primary-subtle)' : 'transparent',
          color: selectedStage === 'all' ? 'var(--color-primary)' : 'var(--color-text-secondary)',
          fontWeight: selectedStage === 'all' ? 'var(--weight-semibold)' : 'var(--weight-normal)',
          fontSize: 'var(--text-body-sm)',
        }}>All ({timeline.length})</button>
        {stages.map((stage) => (
          <button key={stage} onClick={() => setSelectedStage(stage)} style={{
            padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
            background: selectedStage === stage ? 'var(--color-bg-primary-subtle)' : 'transparent',
            color: selectedStage === stage ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            fontWeight: selectedStage === stage ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            fontSize: 'var(--text-body-sm)', textTransform: 'capitalize',
          }}>{stage.replace(/-/g, ' ')}</button>
        ))}
      </div>

      <DashboardWidget title="Student Communication Lifecycle" subtitle={`${filtered.length} events`}>
        {filtered.length === 0 ? <EmptyState type="noCommunicationHistory" /> : (
          <div style={{ maxWidth: 600 }}>
            {filtered.map((event, i) => (
              <TimelineEvent key={event.id} event={event} isLast={i === filtered.length - 1} />
            ))}
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
