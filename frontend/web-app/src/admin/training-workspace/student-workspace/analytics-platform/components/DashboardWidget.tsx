import type { ReactNode, JSX } from 'react';

interface DashboardWidgetProps {
  title: string;
  subtitle?: string;
  children: ReactNode;
  actions?: ReactNode;
  headerRight?: ReactNode;
}

export function DashboardWidget({ title, subtitle, children, actions, headerRight }: DashboardWidgetProps) {
  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', overflow: 'hidden', display: 'flex', flexDirection: 'column' } as JSX.IntrinsicElements['div']}>
      <div style={{ padding: 'var(--space-3) var(--space-3) 0', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 8 }}>
        <div>
          <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{title}</h3>
          {subtitle && <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: '2px 0 0' }}>{subtitle}</p>}
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          {headerRight}
          {actions}
        </div>
      </div>
      <div style={{ padding: 'var(--space-3)' }}>
        {children}
      </div>
    </div>
  );
}
