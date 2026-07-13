import React from 'react';

export interface ChartLegendItem {
  label: string;
  color: string;
  active?: boolean;
}

export interface ChartLegendProps {
  items: ChartLegendItem[];
  onToggle?: (index: number) => void;
  orientation?: 'horizontal' | 'vertical';
  className?: string;
  style?: React.CSSProperties;
}

export const ChartLegend: React.FC<ChartLegendProps> = ({
  items,
  onToggle,
  orientation = 'horizontal',
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: orientation === 'vertical' ? 'column' : 'row',
    flexWrap: 'wrap',
    gap: orientation === 'vertical' ? 'var(--space-2)' : 'var(--space-3)',
    padding: 'var(--space-2) 0',
    ...style,
  };

  const dotStyle = (color: string, active: boolean): React.CSSProperties => ({
    width: 10,
    height: 10,
    borderRadius: '50%',
    backgroundColor: color,
    opacity: active ? 1 : 0.3,
    flexShrink: 0,
  });

  const labelStyle = (active: boolean): React.CSSProperties => ({
    fontSize: 'var(--text-body-sm)',
    color: active ? 'var(--color-text-secondary)' : 'var(--color-text-disabled)',
    whiteSpace: 'nowrap',
  });

  return (
    <div className={className} style={containerStyle} role="list" aria-label="Chart legend">
      {items.map((item, index) => {
        const active = item.active ?? true;
        return (
          <button
            key={index}
            role="listitem"
            type="button"
            disabled={!onToggle}
            onClick={() => onToggle?.(index)}
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: 'var(--space-1)',
              cursor: onToggle ? 'pointer' : 'default',
              background: 'none',
              border: 'none',
              padding: 0,
              font: 'inherit',
            }}
            aria-pressed={active}
          >
            <span style={dotStyle(item.color, active)} />
            <span style={labelStyle(active)}>{item.label}</span>
          </button>
        );
      })}
    </div>
  );
};

ChartLegend.displayName = 'ChartLegend';
export default ChartLegend;
