function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

import { Timeline } from '../../components/charts/timeline/Timeline';
import { ActivityTimeline } from '../../components/charts/timeline/ActivityTimeline';
import { OrderTimeline } from '../../components/charts/timeline/OrderTimeline';
import { TrainingTimeline } from '../../components/charts/timeline/TrainingTimeline';
import { AuditTimeline } from '../../components/charts/timeline/AuditTimeline';

const now = new Date();
const daysAgo = (n: number) => { const d = new Date(now); d.setDate(d.getDate() - n); return d; };
const weekLater = (n: number) => { const d = new Date(now); d.setDate(d.getDate() + n); return d; };

const verticalItems = [
  { id: '1', title: 'Project Kickoff', description: 'Initial planning and stakeholder alignment', timestamp: daysAgo(7), status: 'completed' as const },
  { id: '2', title: 'Design Phase', description: 'UI/UX wireframes and prototypes', timestamp: daysAgo(3), status: 'completed' as const },
  { id: '3', title: 'Development', description: 'Sprint 1 implementation in progress', timestamp: daysAgo(0), status: 'active' as const },
  { id: '4', title: 'Testing', description: 'QA and user acceptance testing', timestamp: weekLater(5), status: 'pending' as const },
  { id: '5', title: 'Deployment', description: 'Production release', timestamp: weekLater(12), status: 'pending' as const },
];

const horizontalItems = [
  { id: 'h1', title: 'Research', description: 'Market analysis', timestamp: daysAgo(14), status: 'completed' as const },
  { id: 'h2', title: 'Strategy', description: 'Define roadmap', timestamp: daysAgo(7), status: 'completed' as const },
  { id: 'h3', title: 'Execute', description: 'Build & iterate', timestamp: daysAgo(0), status: 'active' as const },
  { id: 'h4', title: 'Launch', description: 'Ship to users', timestamp: weekLater(10), status: 'pending' as const },
];

const activities = [
  { id: 'a1', user: { name: 'Alice Johnson' }, action: 'created', target: 'Project Roadmap', timestamp: daysAgo(0), type: 'create' as const },
  { id: 'a2', user: { name: 'Bob Smith' }, action: 'commented on', target: 'Design Review', timestamp: daysAgo(0), type: 'comment' as const },
  { id: 'a3', user: { name: 'Carol Davis' }, action: 'updated', target: 'User Profile Page', timestamp: daysAgo(1), type: 'update' as const },
  { id: 'a4', user: { name: 'Dave Wilson' }, action: 'uploaded', target: 'Q2 Report.pdf', timestamp: daysAgo(1), type: 'upload' as const },
  { id: 'a5', user: { name: 'Eve Martin' }, action: 'deleted', target: 'Old Draft.docx', timestamp: daysAgo(2), type: 'delete' as const },
];

const trainingSessions = [
  { id: 't1', title: 'React Fundamentals', date: daysAgo(1), duration: '2h 30m', instructor: 'John Doe', status: 'completed' as const, progress: 100 },
  { id: 't2', title: 'TypeScript Deep Dive', date: daysAgo(0), duration: '3h', instructor: 'Jane Roe', status: 'in-progress' as const, progress: 65 },
  { id: 't3', title: 'GraphQL API Design', date: weekLater(3), duration: '1h 45m', instructor: 'Sam Lee', status: 'upcoming' as const, progress: 0 },
  { id: 't4', title: 'Docker & Kubernetes', date: weekLater(7), duration: '4h', status: 'upcoming' as const, progress: 0 },
];

const auditEntries = [
  { id: 'ad1', user: 'admin@co.com', action: 'Modified', resource: 'User #1042 role', timestamp: daysAgo(0), ip: '192.168.1.10', severity: 'info' as const },
  { id: 'ad2', user: 'sysbot', action: 'Failed login', resource: 'SSH endpoint', timestamp: daysAgo(0), ip: '10.0.0.55', severity: 'warning' as const, details: '3 consecutive failed attempts from unknown IP' },
  { id: 'ad3', user: 'jane@co.com', action: 'Deleted', resource: 'Invoice #8821', timestamp: daysAgo(1), ip: '192.168.1.22', severity: 'error' as const, details: 'Financial record deletion triggered compliance alert' },
  { id: 'ad4', user: 'admin@co.com', action: 'Exported', resource: 'Customer list', timestamp: daysAgo(1), ip: '192.168.1.10', severity: 'info' as const },
];

export default function TimelinesPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Timelines</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Timeline variants, activity feeds, order status, training, and audit trails</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Timeline (Vertical & Horizontal)</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '16px' }}>
          <StateCard label="Vertical Timeline">
            <Timeline items={verticalItems} orientation="vertical" size="md" />
          </StateCard>
          <StateCard label="Horizontal Timeline">
            <div style={{ width: '100%' }}>
              <Timeline items={horizontalItems} orientation="horizontal" size="sm" />
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>ActivityTimeline</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '16px' }}>
          <StateCard label="Recent Activity">
            <ActivityTimeline items={activities} />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>OrderTimeline</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '16px' }}>
          <StateCard label="Delivered order">
            <div style={{ width: '100%' }}>
              <OrderTimeline status="delivered" />
            </div>
          </StateCard>
          <StateCard label="Processing order">
            <div style={{ width: '100%' }}>
              <OrderTimeline status="processing" />
            </div>
          </StateCard>
          <StateCard label="Cancelled order">
            <div style={{ width: '100%' }}>
              <OrderTimeline status="cancelled" />
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>TrainingTimeline</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(420px, 1fr))', gap: '16px' }}>
          <StateCard label="Training Sessions">
            <TrainingTimeline sessions={trainingSessions} />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>AuditTimeline</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(480px, 1fr))', gap: '16px' }}>
          <StateCard label="Audit Trail Entries">
            <AuditTimeline entries={auditEntries} />
          </StateCard>
        </div>
      </section>
    </div>
  );
}
