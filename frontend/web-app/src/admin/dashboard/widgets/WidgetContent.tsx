import { Icon } from '../../../design-system/icons/Icon';

interface WidgetContentProps {
  type: string;
}

export function WidgetContent({ type }: WidgetContentProps) {
  switch (type) {
    case 'sales-overview':
      return <SalesOverviewWidget />;
    case 'recent-orders':
      return <RecentOrdersWidget />;
    case 'inventory-status':
      return <InventoryStatusWidget />;
    case 'customer-growth':
      return <CustomerGrowthWidget />;
    case 'revenue':
      return <RevenueWidget />;
    case 'training-overview':
      return <TrainingOverviewWidget />;
    case 'tasks':
      return <TasksWidget />;
    case 'calendar':
      return <CalendarWidget />;
    case 'notifications':
      return <NotificationsWidget />;
    case 'quick-stats':
      return <QuickStatsWidget />;
    default:
      return <PlaceholderWidget title={type} />;
  }
}

function PlaceholderWidget({ title }: { title: string }) {
  return (
    <div style={{ textAlign: 'center', color: 'var(--color-text-tertiary)', padding: 24 }}>
      <Icon name="layout" size={32} />
      <p style={{ margin: '8px 0 0', fontSize: 'var(--text-body)' }}>{title}</p>
    </div>
  );
}

function SalesOverviewWidget() {
  return (
    <div style={{ width: '100%', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((day) => (
          <div key={day} style={{ textAlign: 'center', flex: 1 }}>
            <div
              style={{
                height: `${40 + Math.random() * 60}px`,
                width: '60%',
                margin: '0 auto 6px',
                background: 'var(--color-primary-alpha)',
                borderRadius: 'var(--radius-sm)',
                transition: 'height 0.3s',
              }}
            />
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{day}</span>
          </div>
        ))}
      </div>
      <div style={{ textAlign: 'center', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Weekly sales trend placeholder
      </div>
    </div>
  );
}

function RecentOrdersWidget() {
  const items = [
    { id: '#10492', customer: 'Acme Corp', status: 'Shipped', amount: '$1,240' },
    { id: '#10491', customer: 'TechCo', status: 'Processing', amount: '$850' },
    { id: '#10490', customer: 'Global Inc', status: 'Pending', amount: '$2,100' },
    { id: '#10489', customer: 'StartupXYZ', status: 'Shipped', amount: '$430' },
    { id: '#10488', customer: 'MegaCorp', status: 'Delivered', amount: '$3,670' },
  ];
  return (
    <div style={{ width: '100%' }}>
      {items.map((item) => (
        <div
          key={item.id}
          style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}
        >
          <div>
            <span style={{ fontWeight: 500 }}>{item.id}</span>
            <span style={{ color: 'var(--color-text-tertiary)', marginLeft: 8 }}>{item.customer}</span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{item.status}</span>
            <span style={{ fontWeight: 600 }}>{item.amount}</span>
          </div>
        </div>
      ))}
    </div>
  );
}

function InventoryStatusWidget() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ position: 'relative', width: 100, height: 100, margin: '0 auto 12px' }}>
        <svg viewBox="0 0 36 36" style={{ width: '100%', height: '100%' }}>
          <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" fill="none" stroke="var(--color-border)" strokeWidth="3" />
          <path d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" fill="none" stroke="var(--color-success)" strokeWidth="3" strokeDasharray="72, 100" />
        </svg>
        <div style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', fontSize: 'var(--text-h3)', fontWeight: 700 }}>72%</div>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Inventory utilization</div>
    </div>
  );
}

function CustomerGrowthWidget() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ display: 'flex', justifyContent: 'center', gap: 4, alignItems: 'flex-end', height: 80, margin: '0 auto 12px' }}>
        {[60, 75, 68, 82, 78, 95].map((h, i) => (
          <div key={i} style={{ width: 20, height: `${h}%`, background: 'var(--color-info)', borderRadius: 'var(--radius-sm) 0 0 0', opacity: 0.6 + i * 0.07 }} />
        ))}
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>+12.5% this quarter</div>
    </div>
  );
}

function RevenueWidget() {
  return (
    <div style={{ width: '100%' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 12 }}>
        <div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 700 }}>$284.5K</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>This month</div>
        </div>
        <div style={{ textAlign: 'right' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 700, color: 'var(--color-success)' }}>+12.5%</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>vs last month</div>
        </div>
      </div>
      <div style={{ width: '100%', height: 4, background: 'var(--color-border)', borderRadius: 2 }}>
        <div style={{ width: '72%', height: '100%', background: 'var(--color-primary)', borderRadius: 2 }} />
      </div>
    </div>
  );
}

function TrainingOverviewWidget() {
  return (
    <div style={{ width: '100%', display: 'flex', flexDirection: 'column', gap: 8 }}>
      {[
        { name: 'React Fundamentals', progress: 85 },
        { name: 'TypeScript Advanced', progress: 60 },
        { name: 'UI Design Basics', progress: 40 },
        { name: 'Node.js API', progress: 25 },
      ].map((t) => (
        <div key={t.name}>
          <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', marginBottom: 2 }}>
            <span>{t.name}</span>
            <span style={{ color: 'var(--color-text-tertiary)' }}>{t.progress}%</span>
          </div>
          <div style={{ width: '100%', height: 6, background: 'var(--color-border)', borderRadius: 3 }}>
            <div style={{ width: `${t.progress}%`, height: '100%', background: 'var(--color-accent)', borderRadius: 3 }} />
          </div>
        </div>
      ))}
    </div>
  );
}

function TasksWidget() {
  return (
    <div style={{ width: '100%' }}>
      {[
        { task: 'Review Q4 report', done: true },
        { task: 'Update inventory', done: true },
        { task: 'Approve pending orders', done: false },
        { task: 'Schedule team meeting', done: false },
        { task: 'Deploy hotfix', done: false },
      ].map((t) => (
        <label key={t.task} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '4px 0', cursor: 'pointer', fontSize: 'var(--text-body)' }}>
          <input type="checkbox" defaultChecked={t.done} style={{ accentColor: 'var(--color-primary)' }} />
          <span style={{ textDecoration: t.done ? 'line-through' : 'none', color: t.done ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)' }}>
            {t.task}
          </span>
        </label>
      ))}
    </div>
  );
}

function CalendarWidget() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ fontSize: 'var(--text-h1)', fontWeight: 700, lineHeight: 1 }}>14</div>
      <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)', marginBottom: 8 }}>Tuesday, July 2026</div>
      <div style={{ display: 'flex', justifyContent: 'center', gap: 4 }}>
        {['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'].map((d) => (
          <span key={d} style={{ fontSize: 'var(--text-caption)', padding: '2px 4px', color: d === 'Tu' ? 'var(--color-primary)' : 'var(--color-text-tertiary)', fontWeight: d === 'Tu' ? 700 : 400 }}>{d}</span>
        ))}
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 8 }}>3 events today</div>
    </div>
  );
}

function NotificationsWidget() {
  return (
    <div style={{ width: '100%' }}>
      {[
        { text: 'New order received', time: '2m ago', type: 'success' },
        { text: 'Payment failed', time: '15m ago', type: 'error' },
        { text: 'System update available', time: '1h ago', type: 'warning' },
      ].map((n, i) => (
        <div key={i} style={{ display: 'flex', gap: 8, padding: '6px 0', borderBottom: i < 2 ? '1px solid var(--color-border)' : 'none', fontSize: 'var(--text-body)' }}>
          <div style={{ width: 8, height: 8, borderRadius: '50%', marginTop: 6, background: n.type === 'error' ? 'var(--color-error)' : n.type === 'warning' ? 'var(--color-warning)' : 'var(--color-success)', flexShrink: 0 }} />
          <div>
            <div>{n.text}</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{n.time}</div>
          </div>
        </div>
      ))}
    </div>
  );
}

function QuickStatsWidget() {
  return (
    <div style={{ width: '100%', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
      {[
        { label: 'Avg. Order', value: '$154' },
        { label: 'Conversion', value: '3.2%' },
        { label: 'Bounce Rate', value: '28%' },
        { label: 'Session', value: '4m 12s' },
      ].map((s) => (
        <div key={s.label} style={{ textAlign: 'center', padding: '8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
          <div style={{ fontSize: 'var(--text-h4)', fontWeight: 700 }}>{s.value}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{s.label}</div>
        </div>
      ))}
    </div>
  );
}
