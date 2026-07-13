import React, { useCallback, useEffect, useRef } from 'react';

export interface MegaMenuColumn {
  title: string;
  items: { label: string; href?: string; description?: string; icon?: React.ReactNode }[];
}

export interface MegaMenuProps {
  open: boolean;
  onClose: () => void;
  columns: MegaMenuColumn[];
  className?: string;
}

const overlayStyle: React.CSSProperties = {
  position: 'fixed',
  inset: 0,
  zIndex: 'var(--z-dropdown)',
};

const menuStyle: React.CSSProperties = {
  position: 'absolute',
  top: '100%',
  left: 0,
  right: 0,
  display: 'flex',
  gap: 'var(--space-inline-lg)',
  padding: 'var(--space-stack-lg) var(--space-page-x)',
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-dropdown)',
  boxShadow: 'var(--shadow-lg)',
  zIndex: 'var(--z-dropdown)',
  animation: 'megamenu-fadeIn var(--duration-normal) var(--easing-standard)',
  maxHeight: 'calc(100vh - 80px)',
  overflowY: 'auto',
};

const columnStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-stack-md)',
  minWidth: 160,
  flex: 1,
};

const columnTitleStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-semibold)',
  color: 'var(--color-text-secondary)',
  textTransform: 'uppercase',
  letterSpacing: 'var(--tracking-wide)',
  margin: 0,
};

const itemListStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-stack-xs)',
  listStyle: 'none',
  margin: 0,
  padding: 0,
};

const itemStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 2,
  padding: 'var(--space-stack-xs) 0',
  cursor: 'pointer',
  borderRadius: 'var(--radius-sm)',
  transition: 'background var(--duration-fast) var(--easing-standard)',
};

const itemLabelStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-medium)',
  color: 'var(--color-text-primary)',
  textDecoration: 'none',
};

const itemDescStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  lineHeight: 'var(--leading-normal)',
};

export const MegaMenu: React.FC<MegaMenuProps> = ({
  open,
  onClose,
  columns,
  className = '',
}) => {
  const menuRef = useRef<HTMLDivElement>(null);

  const handleKeyDown = useCallback((e: KeyboardEvent) => {
    if (e.key === 'Escape') {
      onClose();
    }
  }, [onClose]);

  useEffect(() => {
    if (!open) return;
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [open, handleKeyDown]);

  useEffect(() => {
    if (!open) return;
    const handleClickOutside = (e: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        onClose();
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <>
      <div style={overlayStyle} aria-hidden="true" />
      <div ref={menuRef} className={className} style={menuStyle} role="menu">
        {columns.map((column, colIdx) => (
          <div key={colIdx} style={columnStyle}>
            <h3 style={columnTitleStyle}>{column.title}</h3>
            <ul style={itemListStyle}>
              {column.items.map((item, itemIdx) => (
                <li key={itemIdx} style={itemStyle}>
                  <div
                    style={itemLabelStyle}
                    role="menuitem"
                    tabIndex={0}
                    onClick={onClose}
                    onKeyDown={(e) => { if (e.key === 'Enter') onClose(); }}
                  >
                    {item.icon && <span style={{ flexShrink: 0, fontSize: 16 }}>{item.icon}</span>}
                    <span>{item.label}</span>
                  </div>
                  {item.description && <span style={itemDescStyle}>{item.description}</span>}
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
      <style>{`@keyframes megamenu-fadeIn { from { opacity: 0; transform: translateY(-8px); } to { opacity: 1; transform: translateY(0); } }`}</style>
    </>
  );
};

MegaMenu.displayName = 'MegaMenu';

export default MegaMenu;
