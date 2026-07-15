import { useCallback, useRef, useState, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { ColumnConfig } from '../data-grid/types';

interface ColumnHeaderProps {
  column: ColumnConfig;
  onSort?: () => void;
  sortDirection: 'asc' | 'desc' | null;
  onResize?: (width: number) => void;
  sticky?: boolean;
}

export const ColumnHeader = memo(function ColumnHeader({ column, onSort, sortDirection, onResize, sticky }: ColumnHeaderProps) {
  const [resizing, setResizing] = useState(false);
  const thRef = useRef<HTMLTableCellElement>(null);
  const startXRef = useRef(0);
  const startWidthRef = useRef(0);

  const handleMouseDown = useCallback(
    (e: React.MouseEvent) => {
      if (!onResize) return;
      e.preventDefault();
      setResizing(true);
      startXRef.current = e.clientX;
      startWidthRef.current = thRef.current?.offsetWidth ?? 100;

      const handleMouseMove = (ev: MouseEvent) => {
        const diff = ev.clientX - startXRef.current;
        const newWidth = Math.max(60, startWidthRef.current + diff);
        onResize(newWidth);
      };

      const handleMouseUp = () => {
        setResizing(false);
        document.removeEventListener('mousemove', handleMouseMove);
        document.removeEventListener('mouseup', handleMouseUp);
        document.body.style.cursor = '';
        document.body.style.userSelect = '';
      };

      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleMouseUp);
      document.body.style.cursor = 'col-resize';
      document.body.style.userSelect = 'none';
    },
    [onResize]
  );

  const pinnedStyle: React.CSSProperties = {};
  if (column.pinned === 'left') {
    pinnedStyle.position = 'sticky';
    pinnedStyle.left = 0;
    pinnedStyle.zIndex = 3;
    pinnedStyle.background = 'var(--color-surface)';
  } else if (column.pinned === 'right') {
    pinnedStyle.position = 'sticky';
    pinnedStyle.right = 0;
    pinnedStyle.zIndex = 3;
    pinnedStyle.background = 'var(--color-surface)';
  }

  return (
    <th
      ref={thRef}
      onClick={onSort}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSort) { e.preventDefault(); onSort(); } }}
      tabIndex={onSort ? 0 : undefined}
      style={{
        padding: '12px 16px',
        textAlign: 'left',
        borderBottom: '2px solid var(--color-border)',
        fontWeight: 600,
        fontSize: 'var(--text-caption)',
        color: 'var(--color-text-tertiary)',
        textTransform: 'uppercase',
        letterSpacing: '0.05em',
        whiteSpace: 'nowrap',
        cursor: onSort ? 'pointer' : undefined,
        userSelect: 'none',
        position: 'relative',
        width: column.width,
        minWidth: column.width ?? 80,
        ...(sticky ? { position: 'sticky', top: 0, zIndex: 2, background: 'var(--color-surface)' } : {}),
        ...pinnedStyle,
      }}
      aria-sort={sortDirection === 'asc' ? 'ascending' : sortDirection === 'desc' ? 'descending' : undefined}
    >
      <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
        <span>{column.header}</span>
        {sortDirection && (
          <Icon name={sortDirection === 'asc' ? 'chevron-up' : 'chevron-down'} size={12} />
        )}
      </div>
      {onResize && (
        <div
          onMouseDown={handleMouseDown}
          style={{
            position: 'absolute',
            right: 0,
            top: 0,
            bottom: 0,
            width: 4,
            cursor: 'col-resize',
            background: resizing ? 'var(--color-primary)' : 'transparent',
          }}
        />
      )}
    </th>
  );
});
