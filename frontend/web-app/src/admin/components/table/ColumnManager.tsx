import { useState, useRef, useEffect, useCallback, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import { useDataGrid } from '../data-grid/DataGridProvider';

export const ColumnManager = memo(function ColumnManager() {
  const { orderedColumns, setColumnConfig } = useDataGrid();
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const toggleColumn = useCallback(
    (key: string) => {
      setColumnConfig((prev) => prev.map((c) => (c.key === key ? { ...c, visible: !c.visible } : c)));
    },
    [setColumnConfig]
  );

  const moveColumn = useCallback(
    (key: string, direction: 'up' | 'down') => {
      setColumnConfig((prev) => {
        const idx = prev.findIndex((c) => c.key === key);
        if (idx === -1) return prev;
        const targetIdx = direction === 'up' ? idx - 1 : idx + 1;
        if (targetIdx < 0 || targetIdx >= prev.length) return prev;
        const next = [...prev];
        const temp = next[idx].order;
        next[idx] = { ...next[idx], order: next[targetIdx].order };
        next[targetIdx] = { ...next[targetIdx], order: temp };
        return next.sort((a, b) => a.order - b.order);
      });
    },
    [setColumnConfig]
  );

  const togglePin = useCallback(
    (key: string) => {
      setColumnConfig((prev) => {
        const col = prev.find((c) => c.key === key);
        if (!col) return prev;
        const nextPin = col.pinned === false ? 'left' : col.pinned === 'left' ? 'right' : false;
        return prev.map((c) => (c.key === key ? { ...c, pinned: nextPin } : c));
      });
    },
    [setColumnConfig]
  );

  const resetLayout = useCallback(() => {
    setColumnConfig((prev) =>
      prev.map((c, i) => ({
        ...c,
        visible: true,
        order: i,
        pinned: false as const,
        width: undefined,
      }))
    );
  }, [setColumnConfig]);

  return (
    <div ref={ref} style={{ position: 'relative' }}>
      <Button variant="outline" size="sm" onClick={() => setOpen(!open)} aria-haspopup="dialog" aria-expanded={open}>
        <Icon name="columns" size={14} /> Columns
      </Button>
      {open && (
        <div
          role="dialog"
          aria-label="Column manager"
          style={{
            position: 'absolute',
            top: '100%',
            right: 0,
            marginTop: 4,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            boxShadow: 'var(--elevation-lg)',
            zIndex: 100,
            minWidth: 220,
            padding: 8,
          }}
        >
          <div
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '4px 8px 8px',
              borderBottom: '1px solid var(--color-border)',
              marginBottom: 4,
            }}
          >
            <span style={{ fontWeight: 500, fontSize: 'var(--text-body)' }}>Columns</span>
            <Button variant="ghost" size="sm" onClick={resetLayout}>
              Reset
            </Button>
          </div>
          {orderedColumns.map((col) => (
            <div
              key={col.key}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 4,
                padding: '4px 8px',
                borderRadius: 'var(--radius-sm)',
              }}
            >
              <input
                type="checkbox"
                checked={col.visible}
                onChange={() => toggleColumn(col.key)}
                id={`col-${col.key}`}
                style={{ accentColor: 'var(--color-primary)', cursor: 'pointer' }}
              />
              <label
                htmlFor={`col-${col.key}`}
                style={{
                  flex: 1,
                  fontSize: 'var(--text-body)',
                  color: 'var(--color-text-primary)',
                  cursor: 'pointer',
                  padding: '2px 0',
                }}
              >
                {col.header}
              </label>
              <button
                onClick={() => moveColumn(col.key, 'up')}
                aria-label={`Move ${col.header} up`}
                style={iconBtnStyle}
              >
                <Icon name="chevron-up" size={12} />
              </button>
              <button
                onClick={() => moveColumn(col.key, 'down')}
                aria-label={`Move ${col.header} down`}
                style={iconBtnStyle}
              >
                <Icon name="chevron-down" size={12} />
              </button>
              <button
                onClick={() => togglePin(col.key)}
                aria-label={`${col.pinned ? 'Unpin' : 'Pin'} ${col.header}`}
                title={col.pinned ? `Pinned ${col.pinned}` : 'Click to pin'}
                style={{
                  ...iconBtnStyle,
                  color: col.pinned ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
                }}
              >
                <Icon name="pin" size={12} />
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
});

const iconBtnStyle: React.CSSProperties = {
  width: 24,
  height: 24,
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  border: 'none',
  background: 'transparent',
  cursor: 'pointer',
  color: 'var(--color-text-tertiary)',
  borderRadius: 'var(--radius-sm)',
  padding: 0,
};
