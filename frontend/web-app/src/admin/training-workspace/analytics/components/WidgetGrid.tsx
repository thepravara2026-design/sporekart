import { memo } from 'react';
import type { ReactNode } from 'react';

export interface WidgetGridProps {
  children: ReactNode;
  minColWidth?: number;
}

const WidgetGrid = memo(function WidgetGrid({ children, minColWidth = 320 }: WidgetGridProps) {
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(auto-fill, minmax(${minColWidth}px, 1fr))`,
        gap: 'var(--space-4)',
        alignItems: 'stretch',
      }}
    >
      {children}
    </div>
  );
});

export default WidgetGrid;
