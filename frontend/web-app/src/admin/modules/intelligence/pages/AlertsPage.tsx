import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { AlertCard } from '../components/AlertCard';

export const AlertsPage = memo(function AlertsPage() {
  const { alerts, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Alerts</h2>
      <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>System alerts and notifications requiring attention</p>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        {alerts.map((alert) => <AlertCard key={alert.id} alert={alert} />)}
      </div>
    </div>
  );
});
