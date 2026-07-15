import { Icon } from '../../design-system/icons/Icon';

interface ReconnectNoticeProps {
  show: boolean;
}

export function ReconnectNotice({ show }: ReconnectNoticeProps) {
  if (!show) return null;

  return (
    <div
      role="status"
      aria-live="polite"
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        padding: '10px 16px',
      background: 'var(--color-success)',
      color: '#000',
        fontSize: 'var(--text-body)',
        fontWeight: 500,
        animation: 'slideDown 0.3s ease-out',
      }}
    >
      <Icon name="wifi" size={16} />
      <span>Connection restored. You are back online.</span>
    </div>
  );
}
