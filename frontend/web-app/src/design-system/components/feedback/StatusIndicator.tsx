import React from 'react';

export type StatusType = 'online' | 'offline' | 'busy' | 'pending' | 'processing' | 'completed' | 'failed' | 'queued' | 'draft' | 'archived';

export interface StatusIndicatorProps {
  status: StatusType;
  size?: 'sm' | 'md' | 'lg';
  label?: boolean | string;
  animated?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const statusColorMap: Record<StatusType, string> = {
  online: 'var(--color-success-500)',
  offline: 'var(--color-neutral-400)',
  busy: 'var(--color-warning-500)',
  pending: 'var(--color-warning-500)',
  processing: 'var(--color-info-500)',
  completed: 'var(--color-success-500)',
  failed: 'var(--color-danger-500)',
  queued: 'var(--color-info-500)',
  draft: 'var(--color-neutral-400)',
  archived: 'var(--color-neutral-300)',
};

const statusLabelMap: Record<StatusType, string> = {
  online: 'Online',
  offline: 'Offline',
  busy: 'Busy',
  pending: 'Pending',
  processing: 'Processing',
  completed: 'Completed',
  failed: 'Failed',
  queued: 'Queued',
  draft: 'Draft',
  archived: 'Archived',
};

const dotSizeMap = { sm: 8, md: 10, lg: 12 };

const keyframes = `
  @keyframes statusPulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.4; }
  }
`;

export const StatusIndicator: React.FC<StatusIndicatorProps> = ({
  status,
  size = 'md',
  label: labelProp = false,
  animated = true,
  className = '',
  style,
}) => {
  const dotSize = dotSizeMap[size];
  const bgColor = statusColorMap[status];
  const statusLabel = statusLabelMap[status];

  const shouldAnimate = animated && (status === 'processing' || status === 'pending');

  const containerStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    fontSize: size === 'lg' ? 'var(--text-body)' : 'var(--text-body-sm)',
    color: 'var(--color-text-primary)',
    ...style,
  };

  const dotStyle: React.CSSProperties = {
    width: dotSize,
    height: dotSize,
    borderRadius: 'var(--radius-full)',
    backgroundColor: bgColor,
    flexShrink: 0,
    animation: shouldAnimate ? 'statusPulse 1.5s ease-in-out infinite' : undefined,
  };

  const resolvedLabel = typeof labelProp === 'string' ? labelProp : labelProp === true ? statusLabel : undefined;

  return (
    <span
      className={`sk-status-indicator sk-status-indicator--${status} sk-status-indicator--${size} ${className}`.trim()}
      style={containerStyle}
      aria-label={resolvedLabel || statusLabel}
    >
      <style>{keyframes}</style>
      <span style={dotStyle} aria-hidden="true" />
      {resolvedLabel && <span>{resolvedLabel}</span>}
    </span>
  );
};

StatusIndicator.displayName = 'StatusIndicator';
export default StatusIndicator;
