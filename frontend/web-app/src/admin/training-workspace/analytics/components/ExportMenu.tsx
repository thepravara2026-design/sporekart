import { memo, useState } from 'react';
import { EXPORT_FORMATS, type ExportFormat } from '../data/analyticsMockData';

export interface ExportMenuProps {
  label?: string;
  onExport?: (format: ExportFormat) => void;
}

const ExportMenu = memo(function ExportMenu({ label = 'Export', onExport }: ExportMenuProps) {
  const [open, setOpen] = useState(false);

  return (
    <div style={{ position: 'relative' }}>
      <button
        type="button"
        aria-haspopup="menu"
        aria-expanded={open}
        onClick={() => setOpen((v) => !v)}
        style={{
          padding: 'var(--space-1) var(--space-3)',
          fontSize: 'var(--text-body-sm)',
          background: 'var(--color-bg-surface-default)',
          border: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-md)',
          color: 'var(--color-text-primary)',
          cursor: 'pointer',
        }}
      >
        {label}
      </button>
      {open && (
        <ul
          role="menu"
          style={{
            position: 'absolute',
            right: 0,
            top: 'calc(100% + 4px)',
            listStyle: 'none',
            margin: 0,
            padding: 'var(--space-1)',
            background: 'var(--color-bg-surface-default)',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-md)',
            boxShadow: 'var(--shadow-2)',
            zIndex: 20,
            minWidth: 140,
          }}
        >
          {EXPORT_FORMATS.map((fmt) => (
            <li key={fmt} role="none">
              <button
                type="button"
                role="menuitem"
                onClick={() => {
                  onExport?.(fmt);
                  setOpen(false);
                }}
                style={{
                  display: 'block',
                  width: '100%',
                  textAlign: 'left',
                  padding: 'var(--space-1) var(--space-2)',
                  background: 'transparent',
                  border: 'none',
                  borderRadius: 'var(--radius-sm)',
                  color: 'var(--color-text-primary)',
                  fontSize: 'var(--text-body-sm)',
                  cursor: 'pointer',
                }}
              >
                {fmt}
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
});

export default ExportMenu;
