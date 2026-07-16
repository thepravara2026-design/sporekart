import { useState } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { TemplateCard } from '../components/TemplateCard';
import { MetricCard } from '../components/MetricCard';
import { COMMUNICATION_TYPE_LABELS } from '../types';
import type { CommunicationType } from '../types';

export default function NotificationTemplates() {
  const { templates } = useCommunication();
  const [selectedType, setSelectedType] = useState<CommunicationType | 'all'>('all');

  const filtered = selectedType === 'all' ? templates : templates.filter((t) => t.type === selectedType);
  const types = [...new Set(templates.map((t) => t.type))];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Notification Templates</h1>
        <SharedFilters currentPage="communication/templates" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Templates" value={templates.length} icon="📋" />
        <MetricCard label="Active" value={templates.filter((t) => t.isActive).length} icon="✅" color="#16a34a" />
        <MetricCard label="Inactive" value={templates.filter((t) => !t.isActive).length} icon="⏸️" color="#6b7280" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        <button onClick={() => setSelectedType('all')} style={{
          padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
          background: selectedType === 'all' ? 'var(--color-bg-primary-subtle)' : 'transparent',
          color: selectedType === 'all' ? 'var(--color-primary)' : 'var(--color-text-secondary)',
          fontWeight: selectedType === 'all' ? 'var(--weight-semibold)' : 'var(--weight-normal)',
          fontSize: 'var(--text-body-sm)',
        }}>All ({templates.length})</button>
        {types.map((type) => (
          <button key={type} onClick={() => setSelectedType(type)} style={{
            padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
            background: selectedType === type ? 'var(--color-bg-primary-subtle)' : 'transparent',
            color: selectedType === type ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            fontWeight: selectedType === type ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            fontSize: 'var(--text-body-sm)',
          }}>{COMMUNICATION_TYPE_LABELS[type]}</button>
        ))}
      </div>

      <DashboardWidget title="Templates" subtitle={`${filtered.length} templates`}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 8 }}>
          {filtered.map((t) => <TemplateCard key={t.id} template={t} />)}
        </div>
      </DashboardWidget>
    </main>
  );
}
