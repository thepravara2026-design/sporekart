import { memo, type ReactNode } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

interface SectionHeaderProps {
  title: string;
  description?: string;
  sublabel?: string;
  icon?: string;
  actions?: ReactNode;
}

export const SectionHeader = memo(function SectionHeader({ title, description, sublabel, icon, actions }: SectionHeaderProps) {
  return (
    <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16, flexWrap: 'wrap', marginBottom: 16 }}>
      <div style={{ display: 'flex', alignItems: 'flex-start', gap: 12 }}>
        {icon && (
          <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
            <Icon name={icon} size={20} />
          </div>
        )}
        <div>
          {sublabel && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 4 }}>{sublabel}</div>}
          <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>{title}</h1>
          {description && <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{description}</p>}
        </div>
      </div>
      {actions && <div style={{ display: 'flex', gap: 8, flexShrink: 0 }}>{actions}</div>}
    </div>
  );
});

