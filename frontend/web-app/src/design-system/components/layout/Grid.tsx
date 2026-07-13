import React from 'react';

export interface GridProps {
  children: React.ReactNode;
  columns?: number | 'auto-fit' | 'auto-fill';
  minColumnWidth?: string;
  gap?: string | number;
  className?: string;
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
  as: Tag = 'div',
}) => {
  const templateColumns =
    typeof columns === 'number'
      ? `repeat(${columns}, 1fr)`
      : columns === 'auto-fit'
        ? `repeat(auto-fit, minmax(${minColumnWidth}, 1fr))`
        : `repeat(auto-fill, minmax(${minColumnWidth}, 1fr))`;

  const style: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: templateColumns,
    gap: toGap(gap, 'var(--space-component-gap)'),
  };

  return (
    <Tag className={`sk-grid ${className}`.trim()} style={style}>
      {children}
    </Tag>
  );
};

Grid.displayName = 'Grid';
export default Grid;
