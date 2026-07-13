import React from 'react';

/* ─── KeyValue ─── */
export interface KeyValueProps {
  label: string;
  value: React.ReactNode;
  size?: 'sm' | 'md' | 'lg';
  direction?: 'horizontal' | 'vertical';
  className?: string;
}

const kvLabelFontMap: Record<string, string> = {
  sm: 'var(--text-xs)',
  md: 'var(--text-sm)',
  lg: 'var(--text-base)',
};

const kvValueFontMap: Record<string, string> = {
  sm: 'var(--text-sm)',
  md: 'var(--text-base)',
  lg: 'var(--text-lg)',
};

export const KeyValue: React.FC<KeyValueProps> = ({
  label,
  value,
  size = 'md',
  direction = 'horizontal',
  className = '',
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: direction === 'horizontal' ? 'row' : 'column',
    gap: direction === 'horizontal' ? 'var(--space-md)' : 'var(--space-xs)',
    alignItems: direction === 'horizontal' ? 'baseline' : 'flex-start',
  };

  const labelStyle: React.CSSProperties = {
    fontSize: kvLabelFontMap[size],
    color: 'var(--color-text-secondary)',
    fontWeight: 'var(--weight-medium)',
    flexShrink: 0,
  };

  const valueStyle: React.CSSProperties = {
    fontSize: kvValueFontMap[size],
    color: 'var(--color-text-primary)',
    fontWeight: 'var(--weight-semibold)',
  };

  return (
    <span className={`sk-key-value ${className}`.trim()} style={containerStyle}>
      <span style={labelStyle}>{label}</span>
      <span style={valueStyle}>{value}</span>
    </span>
  );
};

KeyValue.displayName = 'KeyValue';

/* ─── DefinitionList ─── */
export interface DefinitionListProps {
  items: { term: string; description: React.ReactNode }[];
  columns?: 1 | 2;
  className?: string;
}

export const DefinitionList: React.FC<DefinitionListProps> = ({
  items,
  columns = 1,
  className = '',
}) => {
  const dlStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: columns === 2 ? '1fr 1fr' : '1fr',
    gap: 'var(--space-md) var(--space-xl)',
    margin: 0,
  };

  const termStyle: React.CSSProperties = {
    fontSize: 'var(--text-sm)',
    color: 'var(--color-text-secondary)',
    fontWeight: 'var(--weight-medium)',
    marginBottom: 'var(--space-xs)',
  };

  const ddStyle: React.CSSProperties = {
    fontSize: 'var(--text-base)',
    color: 'var(--color-text-primary)',
    fontWeight: 'var(--weight-semibold)',
    margin: 0,
  };

  const groupStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
  };

  return (
    <dl className={`sk-definition-list ${className}`.trim()} style={dlStyle}>
      {items.map((item, i) => (
        <div key={i} style={groupStyle}>
          <dt style={termStyle}>{item.term}</dt>
          <dd style={ddStyle}>{item.description}</dd>
        </div>
      ))}
    </dl>
  );
};

DefinitionList.displayName = 'DefinitionList';

/* ─── Timeline ─── */
export interface TimelineItem {
  id: string;
  title: string;
  description?: string;
  timestamp?: string;
  icon?: React.ReactNode;
  status?: 'default' | 'active' | 'completed' | 'error';
  color?: string;
}

export interface TimelineProps {
  items: TimelineItem[];
  orientation?: 'vertical' | 'horizontal';
  loading?: boolean;
  empty?: boolean;
  className?: string;
}

const timelineDotColors: Record<string, string> = {
  default: 'var(--color-neutral-300)',
  active: 'var(--color-bg-primary-default)',
  completed: 'var(--color-success)',
  error: 'var(--color-danger)',
};

export const Timeline: React.FC<TimelineProps> = ({
  items,
  orientation = 'vertical',
  loading = false,
  empty = false,
  className = '',
}) => {
  const isVertical = orientation === 'vertical';

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: isVertical ? 'column' : 'row',
    gap: 0,
    position: 'relative',
    padding: isVertical ? 'var(--space-md) 0' : '0 var(--space-md)',
  };

  const lineStyle: React.CSSProperties = {
    position: 'absolute',
    backgroundColor: 'var(--color-neutral-200)',
    ...(isVertical
      ? { left: 15, top: 0, bottom: 0, width: 2 }
      : { top: 15, left: 0, right: 0, height: 2 }),
  };

  const itemStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: isVertical ? 'row' : 'column',
    gap: isVertical ? 'var(--space-md)' : 'var(--space-sm)',
    position: 'relative',
    flex: isVertical ? undefined : 1,
    paddingBottom: isVertical ? 'var(--space-lg)' : undefined,
    paddingRight: isVertical ? undefined : 'var(--space-lg)',
  };

  const dotBaseStyle: React.CSSProperties = {
    width: 14,
    height: 14,
    borderRadius: 'var(--radius-full)',
    flexShrink: 0,
    zIndex: 1,
    position: 'relative',
    marginTop: isVertical ? 4 : 0,
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
    paddingTop: isVertical ? 0 : 'var(--space-md)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-base)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
  };

  const descStyle: React.CSSProperties = {
    fontSize: 'var(--text-sm)',
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-xs)',
  };

  const timeStyle: React.CSSProperties = {
    fontSize: 'var(--text-xs)',
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-xs)',
  };

  const skeletonPulse: React.CSSProperties = {
    borderRadius: 'var(--radius-sm)',
    backgroundColor: 'var(--color-neutral-200)',
    animation: 'sk-pulse-tl 1.5s ease-in-out infinite',
  };

  const emptyStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 'var(--space-xl)',
    color: 'var(--color-text-secondary)',
    fontSize: 'var(--text-base)',
    width: '100%',
  };

  if (loading) {
    return (
      <div className={`sk-timeline sk-timeline--loading ${className}`.trim()} style={containerStyle}>
        {Array.from({ length: 3 }).map((_, i) => (
          <div key={i} style={itemStyle}>
            <span style={{ ...dotBaseStyle, ...skeletonPulse }} />
            <div style={contentStyle}>
              <div style={{ ...skeletonPulse, height: 14, width: '50%', marginBottom: 'var(--space-xs)' }} />
              <div style={{ ...skeletonPulse, height: 10, width: '30%' }} />
            </div>
          </div>
        ))}
        <style>{`@keyframes sk-pulse-tl { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`}</style>
      </div>
    );
  }

  if (empty || items.length === 0) {
    return (
      <div className={`sk-timeline sk-timeline--empty ${className}`.trim()} style={containerStyle}>
        <div style={emptyStyle}>No timeline entries</div>
      </div>
    );
  }

  return (
    <div className={`sk-timeline ${className}`.trim()} style={containerStyle}>
      <div style={lineStyle} />
      {items.map((item) => {
        const dotColor = item.color || timelineDotColors[item.status || 'default'] || timelineDotColors.default;
        const isActive = item.status === 'active';
        return (
          <div key={item.id} style={itemStyle}>
            <span
              style={{
                ...dotBaseStyle,
                backgroundColor: dotColor,
                ...(isActive ? {
                  boxShadow: `0 0 0 4px color-mix(in srgb, ${dotColor} 30%, transparent)`,
                  animation: 'sk-pulse-dot 2s ease-in-out infinite',
                } : {}),
              }}
            />
            <div style={contentStyle}>
              <div style={titleStyle}>{item.title}</div>
              {item.description && <div style={descStyle}>{item.description}</div>}
              {item.timestamp && <div style={timeStyle}>{item.timestamp}</div>}
            </div>
            <style>{`@keyframes sk-pulse-dot { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.3); } }`}</style>
          </div>
        );
      })}
    </div>
  );
};

Timeline.displayName = 'Timeline';

/* ─── ActivityItem ─── */
export interface ActivityItemProps {
  title: string;
  description?: string;
  timestamp?: string;
  icon?: React.ReactNode;
  type?: 'info' | 'success' | 'warning' | 'error';
  onClick?: () => void;
  className?: string;
}

const activityTypeColors: Record<string, string> = {
  info: 'var(--color-bg-primary-default)',
  success: 'var(--color-success)',
  warning: 'var(--color-warning)',
  error: 'var(--color-danger)',
};

export const ActivityItem: React.FC<ActivityItemProps> = ({
  title,
  description,
  timestamp,
  icon,
  type = 'info',
  onClick,
  className = '',
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-md)',
    padding: 'var(--space-md) var(--space-lg)',
    alignItems: 'flex-start',
    cursor: onClick ? 'pointer' : undefined,
    transition: `background-color var(--duration-fast) var(--easing-standard)`,
    borderRadius: 'var(--radius-sm)',
  };

  const iconContainerStyle: React.CSSProperties = {
    flexShrink: 0,
    width: 36,
    height: 36,
    borderRadius: 'var(--radius-full)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: activityTypeColors[type],
    color: '#fff',
    fontSize: 'var(--icon-sm)',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-base)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
  };

  const descStyle: React.CSSProperties = {
    fontSize: 'var(--text-sm)',
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-xs)',
  };

  const timeStyle: React.CSSProperties = {
    fontSize: 'var(--text-xs)',
    color: 'var(--color-text-disabled)',
    marginTop: 'var(--space-xs)',
  };

  return (
    <div
      className={`sk-activity-item ${className}`.trim()}
      style={containerStyle}
      onClick={onClick}
      role={onClick ? 'button' : undefined}
      tabIndex={onClick ? 0 : undefined}
    >
      {icon && <span style={iconContainerStyle}>{icon}</span>}
      <div style={contentStyle}>
        <div style={titleStyle}>{title}</div>
        {description && <div style={descStyle}>{description}</div>}
        {timestamp && <div style={timeStyle}>{timestamp}</div>}
      </div>
    </div>
  );
};

ActivityItem.displayName = 'ActivityItem';

/* ─── Metric ─── */
export interface MetricProps {
  label: string;
  value: string | number;
  unit?: string;
  previousValue?: string | number;
  changePercent?: number;
  changeType?: 'increase' | 'decrease' | 'neutral';
  icon?: React.ReactNode;
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
  error?: string;
  className?: string;
}

const metricValueFontMap: Record<string, string> = {
  sm: 'var(--text-lg)',
  md: 'var(--text-2xl)',
  lg: 'var(--text-3xl)',
};

const metricLabelFontMap: Record<string, string> = {
  sm: 'var(--text-xs)',
  md: 'var(--text-sm)',
  lg: 'var(--text-base)',
};

export const Metric: React.FC<MetricProps> = ({
  label,
  value,
  unit,
  previousValue,
  changePercent,
  changeType = 'neutral',
  icon,
  size = 'md',
  loading = false,
  error: metricError,
  className = '',
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-xs)',
  };

  const iconAndLabelStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-sm)',
  };

  const labelStyle: React.CSSProperties = {
    fontSize: metricLabelFontMap[size],
    color: 'var(--color-text-secondary)',
    fontWeight: 'var(--weight-medium)',
  };

  const valueRowStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'baseline',
    gap: 'var(--space-sm)',
  };

  const valueStyle: React.CSSProperties = {
    fontSize: metricValueFontMap[size],
    fontWeight: 'var(--weight-bold)',
    color: 'var(--color-text-primary)',
    lineHeight: 1.1,
  };

  const unitStyle: React.CSSProperties = {
    fontSize: metricLabelFontMap[size],
    color: 'var(--color-text-secondary)',
  };

  const changeColors: Record<string, string> = {
    increase: 'var(--color-success)',
    decrease: 'var(--color-danger)',
    neutral: 'var(--color-text-secondary)',
  };

  const changeStyle: React.CSSProperties | undefined = changePercent !== undefined ? {
    fontSize: metricLabelFontMap[size],
    color: changeColors[changeType],
    fontWeight: 'var(--weight-semibold)',
    display: 'flex',
    alignItems: 'center',
    gap: 2,
  } : undefined;

  const skeletonPulse: React.CSSProperties = {
    borderRadius: 'var(--radius-sm)',
    backgroundColor: 'var(--color-neutral-200)',
    animation: 'sk-pulse-m 1.5s ease-in-out infinite',
  };

  const errorStyle: React.CSSProperties = {
    fontSize: metricLabelFontMap[size],
    color: 'var(--color-danger)',
  };

  if (loading) {
    return (
      <div className={`sk-metric sk-metric--loading ${className}`.trim()} style={containerStyle}>
        <div style={{ ...skeletonPulse, height: 12, width: '40%' }} />
        <div style={{ ...skeletonPulse, height: 28, width: '60%', marginTop: 'var(--space-xs)' }} />
        <style>{`@keyframes sk-pulse-m { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`}</style>
      </div>
    );
  }

  if (metricError) {
    return (
      <div className={`sk-metric sk-metric--error ${className}`.trim()} style={containerStyle}>
        <div style={labelStyle}>{label}</div>
        <div style={errorStyle}>{metricError}</div>
      </div>
    );
  }

  const changeArrow = changeType === 'increase' ? '\u25B2' : changeType === 'decrease' ? '\u25BC' : '';

  return (
    <div className={`sk-metric ${className}`.trim()} style={containerStyle}>
      <div style={iconAndLabelStyle}>
        {icon && <span style={{ fontSize: 'var(--icon-md)', color: 'var(--color-text-secondary)' }}>{icon}</span>}
        <span style={labelStyle}>{label}</span>
      </div>
      <div style={valueRowStyle}>
        <span style={valueStyle}>
          {value}{unit && <span style={unitStyle}>{unit}</span>}
        </span>
        {changeStyle && (
          <span style={changeStyle}>
            {changeArrow && <span>{changeArrow}</span>}
            {changePercent !== undefined && `${changePercent > 0 ? '+' : ''}${changePercent}%`}
          </span>
        )}
        {previousValue !== undefined && (
          <span style={{ fontSize: metricLabelFontMap[size], color: 'var(--color-text-secondary)' }}>
            vs {previousValue}{unit || ''}
          </span>
        )}
      </div>
    </div>
  );
};

Metric.displayName = 'Metric';

/* ─── ProgressBar ─── */
export interface ProgressBarProps {
  value?: number;
  max?: number;
  size?: 'sm' | 'md' | 'lg';
  variant?: 'default' | 'success' | 'warning' | 'danger';
  showLabel?: boolean;
  label?: string;
  indeterminate?: boolean;
  className?: string;
}

const progressSizeMap: Record<string, number> = {
  sm: 4,
  md: 8,
  lg: 12,
};

const progressVariantColors: Record<string, string> = {
  default: 'var(--color-bg-primary-default)',
  success: 'var(--color-success)',
  warning: 'var(--color-warning)',
  danger: 'var(--color-danger)',
};

export const ProgressBar: React.FC<ProgressBarProps> = ({
  value = 0,
  max = 100,
  size = 'md',
  variant = 'default',
  showLabel = false,
  label,
  indeterminate = false,
  className = '',
}) => {
  const thickness = progressSizeMap[size];
  const barColor = progressVariantColors[variant];
  const pct = max > 0 ? Math.min(Math.max((value / max) * 100, 0), 100) : 0;

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-sm)',
    width: '100%',
  };

  const trackStyle: React.CSSProperties = {
    flex: 1,
    height: thickness,
    borderRadius: 'var(--radius-full)',
    backgroundColor: 'var(--color-neutral-200)',
    overflow: 'hidden',
    position: 'relative',
  };

  const fillStyle: React.CSSProperties = {
    height: '100%',
    borderRadius: 'var(--radius-full)',
    backgroundColor: barColor,
    transition: `width var(--duration-fast) var(--easing-standard)`,
    ...(indeterminate
      ? {
          width: '40%',
          animation: 'sk-progress-indeterminate 1.4s ease-in-out infinite',
        }
      : { width: `${pct}%` }),
  };

  const labelOuterStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-xs)',
    width: '100%',
  };

  const labelRowStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  };

  const labelTextStyle: React.CSSProperties = {
    fontSize: 'var(--text-sm)',
    color: 'var(--color-text-secondary)',
    fontWeight: 'var(--weight-medium)',
  };

  const pctStyle: React.CSSProperties = {
    fontSize: 'var(--text-sm)',
    color: 'var(--color-text-primary)',
    fontWeight: 'var(--weight-semibold)',
  };

  const inner = (
    <div style={trackStyle}>
      <div style={fillStyle} />
    </div>
  );

  return (
    <div className={`sk-progress-bar ${className}`.trim()} style={label ? labelOuterStyle : containerStyle}>
      {(label || (showLabel && !indeterminate)) && (
        <div style={label ? labelRowStyle : undefined}>
          {label && <span style={labelTextStyle}>{label}</span>}
          {showLabel && !indeterminate && <span style={pctStyle}>{Math.round(pct)}%</span>}
        </div>
      )}
      {label ? <div style={containerStyle}>{inner}</div> : inner}
      <style>{`@keyframes sk-progress-indeterminate { 0% { transform: translateX(-100%); } 100% { transform: translateX(350%); } }`}</style>
    </div>
  );
};

ProgressBar.displayName = 'ProgressBar';
