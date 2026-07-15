import { useState, useCallback, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { WidgetConfig } from '../types';

interface WidgetCardProps {
  widget: WidgetConfig;
  children: React.ReactNode;
  onPin?: (id: string) => void;
  onRemove?: (id: string) => void;
  loading?: boolean;
}

export const WidgetCard = memo(function WidgetCard({ widget, children, onPin, onRemove, loading }: WidgetCardProps) {
  const [menuOpen, setMenuOpen] = useState(false);

  const handlePin = useCallback(() => { onPin?.(widget.id); setMenuOpen(false); }, [onPin, widget.id]);
  const handleRemove = useCallback(() => { onRemove?.(widget.id); setMenuOpen(false); }, [onRemove, widget.id]);

  return (
    <div
      style={{
        background: 'var(--color-surface)',
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-lg)',
        overflow: 'hidden',
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        transition: 'box-shadow 0.2s',
      }}
    >
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          padding: '14px 16px',
          borderBottom: '1px solid var(--color-border)',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <Icon name={widget.icon} size={16} />
          <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>
            {widget.title}
          </span>
          {widget.pinned && (
            <Icon name="pin" size={12} style={{ color: 'var(--color-text-tertiary)' }} />
          )}
        </div>
        <div style={{ position: 'relative' }}>
          <button
            onClick={() => setMenuOpen(!menuOpen)}
            aria-label={`${widget.title} menu`}
            style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-text-tertiary)', padding: 4, display: 'flex' }}
          >
            <Icon name="more-horizontal" size={16} />
          </button>
          {menuOpen && (
            <div
              style={{
                position: 'absolute',
                top: '100%',
                right: 0,
                marginTop: 4,
                background: 'var(--color-surface)',
                border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)',
                boxShadow: 'var(--elevation-md)',
                zIndex: 10,
                minWidth: 140,
                padding: 4,
              }}
            >
              <button onClick={handlePin} style={menuItemStyle}>
                <Icon name="pin" size={14} /> {widget.pinned ? 'Unpin' : 'Pin'}
              </button>
              <button onClick={handleRemove} style={{ ...menuItemStyle, color: 'var(--color-error)' }}>
                <Icon name="x" size={14} /> Remove
              </button>
            </div>
          )}
        </div>
      </div>
      <div style={{ flex: 1, padding: 16, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        {loading ? (
          <div style={{ textAlign: 'center', color: 'var(--color-text-tertiary)' }}>
            <div style={{ width: 40, height: 40, border: '3px solid var(--color-border)', borderTopColor: 'var(--color-primary)', borderRadius: '50%', animation: 'spin 0.8s linear infinite', margin: '0 auto 12px' }} />
            <span style={{ fontSize: 'var(--text-caption)' }}>Loading...</span>
          </div>
        ) : (
          children
        )}
      </div>
    </div>
  );
});

const menuItemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 8,
  width: '100%',
  padding: '8px 12px',
  border: 'none',
  background: 'transparent',
  cursor: 'pointer',
  textAlign: 'left',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body)',
  borderRadius: 'var(--radius-sm)',
};
