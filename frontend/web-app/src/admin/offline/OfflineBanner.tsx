import { memo } from 'react';
import { Icon } from '../../design-system/icons/Icon';

interface OfflineBannerProps {
  show: boolean;
  onRetry?: () => void;
  message?: string;
}

export const OfflineBanner = memo(function OfflineBanner({ show, onRetry, message }: OfflineBannerProps) {
  if (!show) return null;

  return (
    <div
      role="alert"
      aria-live="assertive"
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        padding: '10px 16px',
      background: 'var(--color-warning)',
      color: '#000',
        fontSize: 'var(--text-body)',
        fontWeight: 500,
      }}
    >
      <Icon name="wifi-off" size={16} />
      <span>{message ?? 'You are currently offline. Some features may be unavailable.'}</span>
      {onRetry && (
        <button
          onClick={onRetry}
          style={{
            marginLeft: 8,
            padding: '4px 12px',
            border: '1px solid rgba(255,255,255,0.5)',
            borderRadius: 'var(--radius-sm)',
            background: 'transparent',
            color: '#fff',
            cursor: 'pointer',
            fontSize: 'var(--text-caption)',
          }}
        >
          Retry
        </button>
      )}
    </div>
  );
});
