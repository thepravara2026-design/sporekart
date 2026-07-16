import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

interface WorkspaceHeaderProps {
  onMenuToggle: () => void;
}

export const WorkspaceHeader = memo(function WorkspaceHeader({
  onMenuToggle,
}: WorkspaceHeaderProps) {
  return (
    <div
      className="training-header"
      style={{
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        height: '100%', padding: '0 var(--space-page-x)',
        borderBottom: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-surface-default)',
        gap: 'var(--space-inline-md)',
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
        <button
          type="button"
          onClick={onMenuToggle}
          className="training-header__menu-btn"
          style={{
            display: 'none', background: 'none', border: 'none',
            cursor: 'pointer', color: 'var(--color-text-primary)', padding: 4,
          }}
          aria-label="Open training navigation"
        >
          <Icon name="menu" size={20} color="currentColor" />
        </button>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
          <span style={{
            width: 24, height: 24, borderRadius: 'var(--radius-sm)',
            background: 'var(--color-primary)',
            color: 'var(--color-text-on-primary)',
            display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
          }}>
            <Icon name="book-open" size={14} color="currentColor" />
          </span>
          <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body-lg)' }}>
            Training Workspace
          </span>
        </div>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
        <button type="button" style={{
          background: 'none', border: 'none', cursor: 'pointer',
          color: 'var(--color-text-secondary)', padding: 6,
          borderRadius: 'var(--radius-sm)',
        }} aria-label="Notifications">
          <Icon name="bell" size={18} color="currentColor" />
          <span style={{
            position: 'absolute', top: 2, right: 2,
            width: 8, height: 8, borderRadius: 'var(--radius-full)',
            background: 'var(--color-danger)',
          }} />
        </button>
        <button type="button" style={{
          background: 'none', border: 'none', cursor: 'pointer',
          color: 'var(--color-text-secondary)', padding: 6,
          borderRadius: 'var(--radius-sm)',
        }} aria-label="Help">
          <Icon name="help-circle" size={18} color="currentColor" />
        </button>
      </div>

      <style>{`
        @media (max-width: 767px) {
          .training-header__menu-btn {
            display: flex !important;
          }
        }
      `}</style>
    </div>
  );
});
