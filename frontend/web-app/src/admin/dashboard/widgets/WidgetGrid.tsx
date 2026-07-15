import { memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { WidgetCard } from './WidgetCard';
import { WidgetContent } from './WidgetContent';
import type { WidgetConfig } from '../types';

interface WidgetGridProps {
  widgets: WidgetConfig[];
  onPin?: (id: string) => void;
  onRemove?: (id: string) => void;
  loading?: boolean;
}

const GRID_COLUMNS = 4;

function getWidgetSpan(width: number): string {
  if (width >= GRID_COLUMNS) return `${GRID_COLUMNS}`;
  if (width === 2) return 'span 2';
  if (width === 3) return 'span 3';
  return 'span 1';
}

export const WidgetGrid = memo(function WidgetGrid({ widgets, onPin, onRemove, loading }: WidgetGridProps) {
  const visibleWidgets = widgets.filter((w) => w.visible);

  if (visibleWidgets.length === 0) {
    return (
      <div
        style={{
          textAlign: 'center',
          padding: '48px 24px',
          color: 'var(--color-text-tertiary)',
          border: '2px dashed var(--color-border)',
          borderRadius: 'var(--radius-lg)',
        }}
      >
        <Icon name="grid" size={40} />
        <h3 style={{ margin: '12px 0 4px', color: 'var(--color-text-secondary)' }}>No widgets configured</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body)' }}>Add widgets to customize your dashboard.</p>
      </div>
    );
  }

  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: 'var(--space-component-gap)',
        gridAutoRows: 'minmax(200px, auto)',
      }}
    >
      {visibleWidgets.map((widget) => (
        <div
          key={widget.id}
          style={{
            gridColumn: getWidgetSpan(widget.width),
            gridRow: widget.height === 2 ? 'span 2' : 'span 1',
          }}
        >
          <WidgetCard widget={widget} onPin={onPin} onRemove={onRemove} loading={loading}>
            <WidgetContent type={widget.type} />
          </WidgetCard>
        </div>
      ))}
    </div>
  );
});


