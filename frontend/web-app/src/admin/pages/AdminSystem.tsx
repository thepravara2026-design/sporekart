import { Card } from '../../design-system/components/composite/Card';

const SYSTEM_SECTIONS = [
  { title: 'System Health', description: 'Service uptime, latency metrics, and error rates.', status: 'Operational' },
  { title: 'Performance', description: 'CPU, memory, disk, and network utilization.', status: 'Good' },
  { title: 'Logs', description: 'Application and audit log viewer with filtering.', status: 'Available' },
  { title: 'Maintenance', description: 'Scheduled maintenance windows and system updates.', status: 'None scheduled' },
];

export default function AdminSystem() {
  return (
    <div>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        {SYSTEM_SECTIONS.map((s) => (
          <Card key={s.title} variant="elevated" padding="lg">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>{s.title}</h3>
              <span style={{
                fontSize: 'var(--text-caption)', padding: '2px 8px',
                borderRadius: 'var(--radius-full)',
                background: 'var(--color-bg-success-weak)',
                color: 'var(--color-success)',
                fontWeight: 'var(--weight-medium)',
              }}>
                {s.status}
              </span>
            </div>
            <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {s.description}
            </p>
          </Card>
        ))}
      </div>
    </div>
  );
}
