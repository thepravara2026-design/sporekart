import { CommerceDashboardWidgets } from '../widgets/CommerceDashboardWidgets';

export function OverviewPanel() {
  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Course Commerce Overview</h2>
      <p style={{ color: 'var(--color-text-tertiary)', marginBottom: 'var(--space-4)' }}>
        Single source of truth for training pricing, enrollment governance and seat allocation.
        Payment-provider agnostic. Running in Mock Mode — no real transactions.
      </p>
      <CommerceDashboardWidgets />
    </div>
  );
}
