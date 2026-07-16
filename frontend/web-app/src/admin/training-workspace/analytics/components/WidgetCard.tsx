import { memo } from 'react';
import type { ReactNode } from 'react';
import { ChartSkeleton } from '../../../../design-system/components/charts';

export interface WidgetCardProps {
  title: string;
  subtitle?: string;
  actions?: ReactNode;
  loading?: boolean;
  span?: 1 | 2 | 3 | 4;
  children: ReactNode;
}

const WidgetCard = memo(function WidgetCard({
  title,
  subtitle,
  actions,
  loading = false,
  span = 1,
  children,
}: WidgetCardProps) {
  return (
    <section
      aria-label={title}
      style={{
        gridColumn: `span ${span}`,
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-3)',
        padding: 'var(--space-4)',
        background: 'var(--color-bg-surface-raised)',
        border: '1px solid var(--color-border-default)',
        borderRadius: 'var(--radius-lg)',
        boxShadow: 'var(--shadow-1)',
        minWidth: 0,
      }}
    >
      <header style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 'var(--space-2)' }}>
        <div style={{ minWidth: 0 }}>
          <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
            {title}
          </h3>
          {subtitle && (
            <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              {subtitle}
            </p>
          )}
        </div>
        {actions && <div style={{ flexShrink: 0 }}>{actions}</div>}
      </header>
      <div style={{ minWidth: 0 }}>
        {loading ? <ChartSkeleton type="bar" /> : children}
      </div>
    </section>
  );
});

export default WidgetCard;
