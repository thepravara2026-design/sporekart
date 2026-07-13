import React from 'react';

export interface TimelineItemData {
  id: string;
  title: string;
  description?: string;
  timestamp: string | Date;
  icon?: React.ReactNode;
  color?: string;
  status?: 'completed' | 'active' | 'pending' | 'error';
}

export interface TimelineProps {
  items: TimelineItemData[];
  orientation?: 'vertical' | 'horizontal';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  style?: React.CSSProperties;
}

const sizeConfig = {
  sm: { dot: 10, line: 2, gap: 'var(--space-2)', padding: 'var(--space-2)' },
  md: { dot: 14, line: 2, gap: 'var(--space-3)', padding: 'var(--space-3)' },
  lg: { dot: 18, line: 3, gap: 'var(--space-4)', padding: 'var(--space-4)' },
};

const statusColors: Record<string, string> = {
  completed: 'var(--color-success-500)',
  active: 'var(--color-bg-primary-default)',
  pending: 'var(--color-neutral-300)',
  error: 'var(--color-danger-500)',
};

export const Timeline: React.FC<TimelineProps> = ({
  items,
  orientation = 'vertical',
  size = 'md',
  className = '',
  style,
}) => {
  const cfg = sizeConfig[size];
  const isVertical = orientation === 'vertical';

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: isVertical ? 'column' : 'row',
    gap: 0,
    position: 'relative',
    padding: isVertical ? `${cfg.padding} 0` : `0 ${cfg.padding}`,
    ...style,
  };

  const lineStyle: React.CSSProperties = {
    position: 'absolute',
    backgroundColor: 'var(--color-border-default)',
    ...(isVertical
      ? { left: cfg.dot / 2 - cfg.line / 2, top: 0, bottom: 0, width: cfg.line }
      : { top: cfg.dot / 2 - cfg.line / 2, left: 0, right: 0, height: cfg.line }),
  };

  const itemStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: isVertical ? 'row' : 'column',
    gap: isVertical ? cfg.gap : cfg.gap,
    position: 'relative',
    flex: isVertical ? undefined : 1,
    paddingBottom: isVertical ? cfg.padding : undefined,
    paddingRight: isVertical ? undefined : cfg.padding,
    alignItems: isVertical ? 'flex-start' : 'center',
  };

  const dotStyle: React.CSSProperties = {
    width: cfg.dot,
    height: cfg.dot,
    borderRadius: 'var(--radius-full)',
    flexShrink: 0,
    zIndex: 1,
    position: 'relative',
    marginTop: isVertical ? 4 : 0,
    border: `2px solid var(--color-bg-surface-default)`,
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
  };

  const descStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-1)',
  };

  const timeStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    marginTop: 'var(--space-1)',
  };

  const iconWrapStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 'var(--icon-sm)',
    color: 'var(--color-text-on-primary)',
    width: cfg.dot,
    height: cfg.dot,
  };

  return (
    <div
      className={`sk-timeline ${className}`.trim()}
      style={containerStyle}
      role="list"
      aria-label="Timeline"
    >
      <div style={lineStyle} aria-hidden="true" />
      {items.map((item) => {
        const dotColor = item.color || statusColors[item.status || 'pending'] || 'var(--color-border-default)';
        const isActive = item.status === 'active';
        const formattedTime = item.timestamp instanceof Date
          ? item.timestamp.toLocaleString()
          : String(item.timestamp);

        return (
          <div
            key={item.id}
            style={itemStyle}
            role="listitem"
            aria-label={`${item.title} - ${formattedTime}`}
          >
            <span
              style={{
                ...dotStyle,
                backgroundColor: dotColor,
                ...(isActive
                  ? {
                      boxShadow: `0 0 0 4px color-mix(in srgb, ${dotColor} 30%, transparent)`,
                      animation: 'sk-timeline-pulse 2s ease-in-out infinite',
                    }
                  : {}),
              }}
            >
              {item.icon && <span style={iconWrapStyle}>{item.icon}</span>}
            </span>
            <div style={contentStyle}>
              <div style={titleStyle}>{item.title}</div>
              {item.description && <div style={descStyle}>{item.description}</div>}
              <div style={timeStyle}>{formattedTime}</div>
            </div>
          </div>
        );
      })}
      <style>{`@keyframes sk-timeline-pulse { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.3); } }`}</style>
    </div>
  );
};

Timeline.displayName = 'Timeline';
