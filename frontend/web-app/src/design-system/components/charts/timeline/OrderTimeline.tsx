import React from 'react';

export interface OrderTimelineStep {
  label: string;
  date?: string | Date;
  completed?: boolean;
  active?: boolean;
}

export interface OrderTimelineProps {
  status: 'pending' | 'confirmed' | 'processing' | 'shipped' | 'delivered' | 'cancelled';
  steps?: OrderTimelineStep[];
  className?: string;
  style?: React.CSSProperties;
}

const defaultSteps: OrderTimelineStep[] = [
  { label: 'Pending', completed: false, active: false },
  { label: 'Confirmed', completed: false, active: false },
  { label: 'Processing', completed: false, active: false },
  { label: 'Shipped', completed: false, active: false },
  { label: 'Delivered', completed: false, active: false },
];

const statusOrder: Record<string, number> = {
  pending: 0,
  confirmed: 1,
  processing: 2,
  shipped: 3,
  delivered: 4,
  cancelled: -1,
};

function formatDate(d: string | Date | undefined): string | undefined {
  if (!d) return undefined;
  const dt = d instanceof Date ? d : new Date(d);
  return dt.toLocaleDateString();
}

export const OrderTimeline: React.FC<OrderTimelineProps> = ({
  status,
  steps,
  className = '',
  style,
}) => {
  const currentIdx = statusOrder[status] ?? -1;
  const isCancelled = status === 'cancelled';
  const resolvedSteps = steps || defaultSteps.map((s, i) => ({
    ...s,
    completed: !isCancelled ? i < currentIdx : false,
    active: !isCancelled ? i === currentIdx : false,
  }));

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 0,
    position: 'relative',
    padding: 'var(--space-4) 0',
    ...style,
  };

  const stepStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    flex: 1,
    position: 'relative',
  };

  const connectorBase: React.CSSProperties = {
    position: 'absolute',
    top: 14,
    height: 2,
    zIndex: 0,
  };

  const dotBase: React.CSSProperties = {
    width: 14,
    height: 14,
    borderRadius: 'var(--radius-full)',
    zIndex: 1,
    position: 'relative',
    border: '2px solid var(--color-bg-surface-default)',
  };

  const labelStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    textAlign: 'center',
    marginTop: 'var(--space-2)',
    fontWeight: 'var(--weight-medium)',
  };

  const dateStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    textAlign: 'center',
    color: 'var(--color-text-disabled)',
    marginTop: 'var(--space-1)',
  };

  return (
    <div
      className={`sk-order-timeline ${className}`.trim()}
      style={containerStyle}
      role="list"
      aria-label={`Order status: ${status}`}
    >
      {resolvedSteps.map((step, idx) => {
        const isCompleted = step.completed || (!isCancelled && idx < currentIdx);
        const isActive = step.active || (!isCancelled && idx === currentIdx);
        const isLast = idx === resolvedSteps.length - 1;

        let dotColor = 'var(--color-border-default)';
        let labelColor = 'var(--color-text-disabled)';

        if (isCancelled) {
          if (idx <= currentIdx) {
            dotColor = 'var(--color-danger-500)';
            labelColor = 'var(--color-text-danger)';
          }
        } else if (isCompleted) {
          dotColor = 'var(--color-success-500)';
          labelColor = 'var(--color-text-success)';
        } else if (isActive) {
          dotColor = 'var(--color-bg-primary-default)';
          labelColor = 'var(--color-text-primary)';
        }

        return (
          <div key={idx} style={{ ...stepStyle, ...(isLast ? {} : {}) }} role="listitem">
            {idx < resolvedSteps.length - 1 && (
              <div
                style={{
                  ...connectorBase,
                  left: '50%',
                  right: '-50%',
                  width: '100%',
                  backgroundColor:
                    !isCancelled && (isCompleted || isActive)
                      ? 'var(--color-success-500)'
                      : 'var(--color-border-default)',
                }}
                aria-hidden="true"
              />
            )}
            <span
              style={{
                ...dotBase,
                backgroundColor: dotColor,
                ...(isActive
                  ? {
                      boxShadow: `0 0 0 4px color-mix(in srgb, var(--color-bg-primary-default) 30%, transparent)`,
                      animation: 'sk-order-pulse 2s ease-in-out infinite',
                    }
                  : {}),
              }}
              aria-current={isActive ? 'step' : undefined}
            />
            <div style={{ ...labelStyle, color: labelColor }}>{step.label}</div>
            {step.date && <div style={dateStyle}>{formatDate(step.date)}</div>}
          </div>
        );
      })}
      <style>{`@keyframes sk-order-pulse { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.3); } }`}</style>
    </div>
  );
};

OrderTimeline.displayName = 'OrderTimeline';
