import React, { useState, useRef, useEffect, useCallback } from 'react';

export type ExportFormat = 'csv' | 'excel' | 'pdf' | 'png' | 'print';

export interface ExportMenuProps {
  onExport: (format: ExportFormat) => void;
  disabled?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

interface MenuItem {
  format: ExportFormat;
  label: string;
  icon: string;
}

const MENU_ITEMS: MenuItem[] = [
  { format: 'csv', label: 'CSV', icon: '\u2693' },
  { format: 'excel', label: 'Excel', icon: '\u2709' },
  { format: 'pdf', label: 'PDF', icon: '\u25B6' },
  { format: 'png', label: 'PNG', icon: '\u272A' },
  { format: 'print', label: 'Print', icon: '\u2399' },
];

export const ExportMenu: React.FC<ExportMenuProps> = ({
  onExport,
  disabled = false,
  className = '',
  style,
}) => {
  const [open, setOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') setOpen(false);
    };
    if (open) {
      document.addEventListener('mousedown', handleClickOutside);
      document.addEventListener('keydown', handleEsc);
    }
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.removeEventListener('keydown', handleEsc);
    };
  }, [open]);

  const handleSelect = useCallback(
    (format: ExportFormat) => {
      if (disabled) return;
      onExport(format);
      setOpen(false);
    },
    [disabled, onExport]
  );

  const triggerStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    height: 'var(--btn-height-md)',
    padding: '0 var(--space-3)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-btn)',
    background: 'var(--color-bg-surface-default)',
    color: 'var(--color-text-primary)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-button)',
    fontWeight: 'var(--weight-medium)',
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 'var(--opacity-disabled)' : undefined,
    userSelect: 'none',
    transition: 'all var(--duration-fast) var(--easing-standard)',
  };

  const menuStyle: React.CSSProperties = {
    position: 'absolute',
    top: 'calc(100% + 4px)',
    right: 0,
    zIndex: 'var(--z-dropdown)',
    background: 'var(--color-bg-surface-overlay)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-dropdown)',
    boxShadow: 'var(--shadow-3)',
    padding: 'var(--space-1)',
    minWidth: 160,
  };

  const itemStyle = (_index: number): React.CSSProperties => ({
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-2) var(--space-3)',
    border: 'none',
    borderRadius: 'var(--radius-xs)',
    background: 'transparent',
    color: 'var(--color-text-primary)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-body-sm)',
    cursor: 'pointer',
    width: '100%',
    textAlign: 'left',
    transition: 'background var(--duration-fast) var(--easing-standard)',
  });

  return (
    <div
      ref={containerRef}
      className={`sk-export-menu ${className}`}
      style={{ position: 'relative', display: 'inline-block', ...style }}
    >
      <button
        type="button"
        style={triggerStyle}
        onClick={() => !disabled && setOpen((prev) => !prev)}
        disabled={disabled}
        aria-haspopup="menu"
        aria-expanded={open}
        aria-label="Export data"
      >
        <span aria-hidden="true">{'\u21E9'}</span>
        <span>Export</span>
      </button>
      {open && (
        <div style={menuStyle} role="menu" aria-label="Export format">
          {MENU_ITEMS.map((item, index) => (
            <button
              key={item.format}
              type="button"
              role="menuitem"
              style={itemStyle(index)}
              onClick={() => handleSelect(item.format)}
              onMouseEnter={(e) => {
                e.currentTarget.style.background = 'var(--color-bg-background)';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.background = 'transparent';
              }}
              aria-label={item.label}
            >
              <span aria-hidden="true" style={{ fontSize: 'var(--text-body)', width: 20, textAlign: 'center' }}>
                {item.icon}
              </span>
              <span>{item.label}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

ExportMenu.displayName = 'ExportMenu';

export default ExportMenu;
