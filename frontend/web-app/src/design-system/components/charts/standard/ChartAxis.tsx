import React from 'react';

export interface ChartAxisProps {
  type: 'x' | 'y';
  labels: string[];
  gridLines?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

export const ChartAxis: React.FC<ChartAxisProps> = ({
  type,
  labels,
  gridLines = true,
  className = '',
  style,
}) => {
  const isX = type === 'x';

  const axisStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: isX ? 'row' : 'column-reverse',
    justifyContent: 'space-between',
    position: 'relative',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    ...style,
  };

  const labelStyle = (): React.CSSProperties => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1,
    padding: isX ? 'var(--space-1) 0 0' : '0 var(--space-1)',
  });

  return (
    <div className={className} style={axisStyle} role="list" aria-label={`${isX ? 'X' : 'Y'} axis`}>
      {labels.map((label, index) => (
        <div key={index} style={{ position: 'relative', flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          {gridLines && (
            <div
              style={{
                position: 'absolute',
                ...(isX
                  ? { top: 0, left: '50%', height: '100%', borderLeft: '1px solid var(--color-border-default)' }
                  : { left: 0, top: '50%', width: '100%', borderTop: '1px solid var(--color-border-default)' }),
                pointerEvents: 'none',
              }}
            />
          )}
          <span style={labelStyle()}>{label}</span>
        </div>
      ))}
    </div>
  );
};

ChartAxis.displayName = 'ChartAxis';
export default ChartAxis;
