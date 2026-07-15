import { memo, type ReactNode, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

export interface SummaryAction {
  id: string;
  label: string;
  icon?: string;
  onClick?: () => void;
  href?: string;
  variant?: 'primary' | 'secondary' | 'danger';
}

interface SummaryCardProps {
  title: string;
  description?: string;
  icon?: string;
  actions?: SummaryAction[];
  children?: ReactNode;
  footer?: ReactNode;
  loading?: boolean;
}

const variantStyle: Record<string, CSSProperties> = {
  primary: { background: 'var(--color-primary)', color: '#fff', border: '1px solid var(--color-primary)' },
  secondary: { background: 'var(--color-surface)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border)' },
  danger: { background: 'var(--color-danger)', color: '#fff', border: '1px solid var(--color-danger)' },
};

export const SummaryCard = memo(function SummaryCard({ title, description, icon, actions, children, footer, loading }: SummaryCardProps) {
  if (loading) {
    return (
      <section style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 24, display: 'flex', flexDirection: 'column', gap: 16 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-surface-hover)', animation: 'shimmer 1.5s infinite' }} />
          <div style={{ flex: 1, height: 16, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
        </div>
        <div style={{ height: 12, width: '80%', background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
        <div style={{ height: 12, width: '60%', background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
      </section>
    );
  }

  return (
    <section style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 24, display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 12 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          {icon && (
            <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
              <Icon name={icon} size={20} />
            </div>
          )}
          <div>
            <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{title}</h3>
            {description && <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{description}</p>}
          </div>
        </div>
        {actions && actions.length > 0 && (
          <div style={{ display: 'flex', gap: 8, flexShrink: 0, flexWrap: 'wrap' }}>
            {actions.map((a) => (
              <button
                key={a.id}
                type="button"
                onClick={a.onClick}
                style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)', cursor: a.onClick ? 'pointer' : 'default', fontSize: 'var(--text-body)', fontWeight: 500, ...variantStyle[a.variant ?? 'secondary'] }}
              >
                {a.icon && <Icon name={a.icon} size={14} />}
                {a.label}
              </button>
            ))}
          </div>
        )}
      </div>
      {children && <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{children}</div>}
      {footer && <div style={{ borderTop: '1px solid var(--color-border)', paddingTop: 12 }}>{footer}</div>}
    </section>
  );
});

