import React from 'react';

export interface NotificationPriorityProps {
  priority: 'low' | 'normal' | 'high' | 'urgent';
  className?: string;
  style?: React.CSSProperties;
}

const priorityColors: Record<string, string> = {
  low: 'var(--color-neutral-400)',
  normal: 'var(--color-info-500)',
  high: 'var(--color-warning-500)',
  urgent: 'var(--color-danger-500)',
};

const priorityLabels: Record<string, string> = {
  low: 'Low priority',
  normal: 'Normal priority',
  high: 'High priority',
  urgent: 'Urgent priority',
};

export const NotificationPriority: React.FC<NotificationPriorityProps> = ({
  priority,
  className = '',
  style,
}) => {
  const dotStyle: React.CSSProperties = {
    width: 8,
    height: 8,
    borderRadius: 'var(--radius-full)',
    background: priorityColors[priority] || 'var(--color-neutral-400)',
    display: 'inline-block',
    flexShrink: 0,
    ...style,
  };

  return (
    <span
      className={`sk-notification-priority sk-notification-priority--${priority} ${className}`.trim()}
      style={dotStyle}
      role="img"
      aria-label={priorityLabels[priority]}
    />
  );
};

NotificationPriority.displayName = 'NotificationPriority';
export default NotificationPriority;
