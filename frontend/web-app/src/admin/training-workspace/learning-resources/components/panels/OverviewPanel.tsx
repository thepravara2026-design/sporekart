import { ResourceDashboardWidgets } from '../widgets/ResourceDashboardWidgets';

export function OverviewPanel() {
  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Learning Resource Management</h2>
      <p style={{ color: 'var(--color-text-tertiary)', marginBottom: 'var(--space-4)' }}>
        Centralized Digital Knowledge Repository for all learning assets — SOPs, manuals, research papers,
        videos, templates and interactive content. Running in Mock Mode.
      </p>
      <ResourceDashboardWidgets />
    </div>
  );
}
