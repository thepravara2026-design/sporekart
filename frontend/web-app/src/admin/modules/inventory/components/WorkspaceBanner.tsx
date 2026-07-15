import { memo, type ReactNode, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

type BannerVariant = 'info' | 'success' | 'warning' | 'offline' | 'maintenance';

interface WorkspaceBannerProps {
  variant?: BannerVariant;
  icon?: string;
  title: string;
  message?: string;
  action?: ReactNode;
}

const variantStyle: Record<BannerVariant, CSSProperties> = {
  info: { background: 'var(--color-bg-info-weak)', borderColor: 'var(--color-info)', color: 'var(--color-text-info)' },
  success: { background: 'var(--color-bg-success-weak)', borderColor: 'var(--color-success)', color: 'var(--color-text-success)' },
  warning: { background: 'var(--color-bg-warning-weak)', borderColor: 'var(--color-warning)', color: 'var(--color-text-warning)' },
  offline: { background: 'var(--color-bg-danger-weak)', borderColor: 'var(--color-danger)', color: 'var(--color-text-danger)' },
  maintenance: { background: 'var(--color-bg-warning-weak)', borderColor: 'var(--color-warning)', color: 'var(--color-text-warning)' },
};

const variantIcon: Record<BannerVariant, string> = {
  info: 'info', success: 'check-circle', warning: 'alert-triangle', offline: 'wifi-off', maintenance: 'tool',
};

export const WorkspaceBanner = memo(function WorkspaceBanner({ variant = 'info', icon, title, message, action }: WorkspaceBannerProps) {
  const style = variantStyle[variant];
  return (
    <div role={variant === 'offline' || variant === 'maintenance' ? 'alert' : 'status'} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', borderRadius: 'var(--radius-lg)', border: '1px solid', ...style, flexWrap: 'wrap' }}>
      <Icon name={icon ?? variantIcon[variant]} size={18} />
      <div style={{ flex: 1, minWidth: 200 }}>
        <div style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{title}</div>
        {message && <div style={{ fontSize: 'var(--text-caption)' }}>{message}</div>}
      </div>
      {action}
    </div>
  );
});

