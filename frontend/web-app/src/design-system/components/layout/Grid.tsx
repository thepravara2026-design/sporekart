import React from 'react';

export interface GridProps {
  children: React.ReactNode;
  columns?: number | 'auto-fit' | 'auto-fill' | string;
  minColumnWidth?: string;
  gap?: string | number;
  className?: string;
  style?: React.CSSProperties;
  as?: 'div' | 'section' | 'article';
}

function toGap(v: string | number | undefined, fallback: string): string {
  if (v === undefined) return fallback;
  return typeof v === 'number' ? `${v}px` : v;
}

export const Grid: React.FC<GridProps> = ({
  children,
  columns = 'auto-fit',
  minColumnWidth = '280px',
  gap,
  className = '',
  style,
  as: Tag = 'div',
}) => {
  const templateColumns =
    typeof columns === 'number'
      ? `repeat(${columns}, 1fr)`
      : columns === 'auto-fit'
        ? `repeat(auto-fit, minmax(${minColumnWidth}, 1fr))`
        : columns === 'auto-fill'
          ? `repeat(auto-fill, minmax(${minColumnWidth}, 1fr))`
          : columns;

  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: templateColumns,
    gap: toGap(gap, 'var(--space-component-gap)'),
    ...style,
  };

  return (
    <Tag className={`sk-grid ${className}`.trim()} style={gridStyle}>
      {children}
    </Tag>
  );
};

Grid.displayName = 'Grid';
export default Grid;
