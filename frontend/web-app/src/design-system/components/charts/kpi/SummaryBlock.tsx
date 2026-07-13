import React from 'react';
import { MetricTile } from './MetricTile';

export interface SummaryBlockMetric {
  label: string;
  value: string | number;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  color?: string;
}

export interface SummaryBlockProps {
  title?: string;
  metrics: SummaryBlockMetric[];
  columns?: 2 | 3 | 4;
  className?: string;
  style?: React.CSSProperties;
}

const gridColsMap: Record<number, string> = {
  2: 'repeat(2, 1fr)',
  3: 'repeat(3, 1fr)',
  4: 'repeat(4, 1fr)',
};

export const SummaryBlock: React.FC<SummaryBlockProps> = ({
  title,
  metrics,
  columns = 3,
  className = '',
  style,
}) => {
  return (
    <div
      className={`sk-summary-block ${className}`.trim()}
      style={{
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-4)',
        ...style,
      }}
    >
      {title && (
        <h3
          style={{
            fontSize: 'var(--text-h4)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: 0,
          }}
        >
          {title}
        </h3>
      )}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: gridColsMap[columns],
          gap: 'var(--space-component-gap)',
        }}
      >
        {metrics.map((metric, index) => (
          <MetricTile
            key={index}
            title={metric.label}
            value={metric.value}
            trend={metric.trend}
            trendValue={metric.trendValue}
            color={metric.color}
            size="sm"
          />
        ))}
      </div>
    </div>
  );
};

SummaryBlock.displayName = 'SummaryBlock';
export default SummaryBlock;
