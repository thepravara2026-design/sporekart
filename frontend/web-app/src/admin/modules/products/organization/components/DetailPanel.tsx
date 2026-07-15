import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';

interface DetailPanelProps {
  title: string;
  subtitle?: string;
  icon?: string;
  children: React.ReactNode;
}

export const DetailPanel = React.memo(function DetailPanel({ title, subtitle, icon, children }: DetailPanelProps) {
  return (
    <div style={{
      padding: 'var(--space-4)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 12 }}>
        {icon && <Icon name={icon} size={20} style={{ color: 'var(--color-primary)' }} />}
        <div>
          <h4 style={{ margin: 0, fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{title}</h4>
          {subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{subtitle}</span>}
        </div>
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        {children}
      </div>
    </div>
  );
});

export default DetailPanel;
