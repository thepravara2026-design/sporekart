import React from 'react';
import { RadialProgress } from './RadialProgress';

export interface CircularKPIProps {
  value: string | number;
  label: string;
  progress?: number;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  color?: string;
  size?: number;
  className?: string;
  style?: React.CSSProperties;
}

export const CircularKPI: React.FC<CircularKPIProps> = ({
  value, label, progress = 0,
  trend, trendValue, color,
  size = 140, className = '', style,
}) => {
  const pct = Math.min(100, Math.max(0, progress));

  const trendArrow = {
    up: (
      <polyline points="-6,4 0,-4 6,4" fill="none" stroke="var(--color-success-500)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    ),
    down: (
      <polyline points="-6,-4 0,4 6,-4" fill="none" stroke="var(--color-danger-500)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    ),
    flat: (
      <line x1="-6" y1="0" x2="6" y2="0" stroke="var(--color-neutral-400)" strokeWidth="2" strokeLinecap="round" />
    ),
  };

  const resolvedColor = color ?? 'var(--color-data-viz-1)';

  return (
    <div
      className={className}
      style={{
        position: 'relative',
        display: 'inline-flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: size,
        height: size,
        ...style,
      }}
      role="figure"
      aria-label={`${label}: ${value}${trend ? `, trend ${trend}` : ''}`}
    >
      <RadialProgress
        value={pct}
        size={size}
        color={resolvedColor}
        showValue={false}
      />

      <div
        style={{
          position: 'absolute',
          top: 0, left: 0, right: 0, bottom: 0,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          gap: '2px',
        }}
      >
        <span
          style={{
            fontSize: size * 0.18,
            fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-primary)',
            fontFamily: 'var(--font-family-sans)',
            lineHeight: 1,
          }}
        >
          {value}
        </span>
        <span
          style={{
            fontSize: size * 0.09,
            color: 'var(--color-text-secondary)',
            fontFamily: 'var(--font-family-sans)',
            textAlign: 'center',
            lineHeight: 1.2,
            maxWidth: size * 0.7,
          }}
        >
          {label}
        </span>
        {trend && (
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '4px',
              marginTop: '2px',
            }}
          >
            <svg width="14" height="10" viewBox="-7 -5 14 10" aria-label={`Trend: ${trend}`}>
              {trendArrow[trend]}
            </svg>
            {trendValue && (
              <span
                style={{
                  fontSize: size * 0.08,
                  fontWeight: 'var(--weight-medium)',
                  color: trend === 'up'
                    ? 'var(--color-success-500)'
                    : trend === 'down'
                      ? 'var(--color-danger-500)'
                      : 'var(--color-text-secondary)',
                  fontFamily: 'var(--font-family-sans)',
                }}
              >
                {trendValue}
              </span>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

CircularKPI.displayName = 'CircularKPI';
export default CircularKPI;
