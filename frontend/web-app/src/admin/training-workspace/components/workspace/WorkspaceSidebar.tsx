import { memo, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Icon } from '../../../../design-system/icons/Icon';
import { NAV_GROUPS, getTrainingActiveId } from '../../data/navigation';

interface WorkspaceSidebarProps {
  collapsed: boolean;
  onCollapse: (v: boolean) => void;
  open: boolean;
  onClose: () => void;
}

export const WorkspaceSidebar = memo(function WorkspaceSidebar({
  collapsed,
  onCollapse,
  open,
  onClose,
}: WorkspaceSidebarProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const activeId = getTrainingActiveId(location.pathname);

  const handleNavigate = useCallback(
    (href: string) => {
      navigate(href);
      if (window.innerWidth < 768) onClose();
    },
    [navigate, onClose]
  );

  return (
    <>
      {open && (
        <div
          onClick={onClose}
          style={{
            position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.3)',
            zIndex: 40, display: 'none',
          }}
          className="training-sidebar-overlay"
        />
      )}
      <aside
        className={`training-sidebar ${collapsed ? 'training-sidebar--collapsed' : ''} ${open ? 'training-sidebar--open' : ''}`}
        style={{
          width: collapsed ? 64 : 280,
          display: 'flex', flexDirection: 'column',
          background: 'var(--color-bg-surface-default)',
          borderRight: '1px solid var(--color-border-default)',
          transition: 'width var(--duration-normal) var(--easing-standard)',
          overflow: 'hidden', flexShrink: 0,
          position: 'relative', zIndex: 50,
        }}
        role="navigation"
        aria-label="Training workspace navigation"
      >
        <div style={{
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          padding: 'var(--space-3) var(--space-4)',
          borderBottom: '1px solid var(--color-border-default)',
          minHeight: 56,
        }}>
          {!collapsed && (
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
              <span style={{
                width: 28, height: 28, borderRadius: 'var(--radius-sm)',
                background: 'var(--color-primary)',
                color: 'var(--color-text-on-primary)',
                display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
              }}>
                <Icon name="book-open" size={16} color="currentColor" />
              </span>
              <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body-lg)' }}>
                LMS
              </span>
            </div>
          )}
          <button
            type="button"
            onClick={() => onCollapse(!collapsed)}
            style={{
              background: 'none', border: 'none', cursor: 'pointer',
              color: 'var(--color-text-secondary)', padding: 4,
              borderRadius: 'var(--radius-sm)',
              transform: collapsed ? 'rotate(180deg)' : 'none',
              transition: 'transform var(--duration-normal) var(--easing-standard)',
            }}
            aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
          >
            <Icon name="chevron-left" size={18} color="currentColor" />
          </button>
        </div>

        <div style={{ flex: 1, overflowY: 'auto', padding: 'var(--space-2) 0' }}>
          {NAV_GROUPS.map((group) => (
            <div key={group.id} style={{ marginBottom: 'var(--space-2)' }}>
              {!collapsed && (
                <div style={{
                  padding: 'var(--space-2) var(--space-4)',
                  fontSize: 'var(--text-caption)',
                  color: 'var(--color-text-tertiary)',
                  fontWeight: 'var(--weight-semibold)',
                  textTransform: 'uppercase',
                  letterSpacing: 'var(--tracking-wide)',
                }}>
                  {group.label}
                </div>
              )}
              {group.items.map((item) => (
                <button
                  key={item.id}
                  type="button"
                  onClick={() => handleNavigate(item.href)}
                  style={{
                    display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
                    width: '100%', padding: collapsed ? '10px 0' : '8px var(--space-4)',
                    justifyContent: collapsed ? 'center' : 'flex-start',
                    background: activeId === item.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
                    border: 'none', cursor: 'pointer',
                    color: activeId === item.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                    borderRadius: 0,
                    transition: 'all var(--duration-fast) var(--easing-standard)',
                    fontSize: 'var(--text-body-sm)',
                    position: 'relative',
                  }}
                  aria-current={activeId === item.id ? 'page' : undefined}
                  title={collapsed ? item.label : undefined}
                >
                  {activeId === item.id && !collapsed && (
                    <span style={{
                      position: 'absolute', left: 0, top: 4, bottom: 4, width: 3,
                      background: 'var(--color-primary)', borderRadius: '0 var(--radius-sm) var(--radius-sm) 0',
                    }} />
                  )}
                  <Icon name={item.icon} size={18} color="currentColor" />
                  {!collapsed && <span style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{item.label}</span>}
                </button>
              ))}
            </div>
          ))}
        </div>

        <div style={{
          padding: collapsed ? 'var(--space-3) 0' : 'var(--space-3) var(--space-4)',
          borderTop: '1px solid var(--color-border-default)',
          fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)',
          textAlign: collapsed ? 'center' : 'left',
        }}>
          {collapsed ? 'v1' : 'LMS Workspace v1.0.0'}
        </div>
      </aside>

      <style>{`
        @media (max-width: 767px) {
          .training-sidebar {
            position: fixed !important;
            left: -280px;
            height: 100vh;
            z-index: 50 !important;
          }
          .training-sidebar--open {
            left: 0 !important;
          }
          .training-sidebar-overlay {
            display: block !important;
          }
        }
        @media (min-width: 768px) {
          .training-sidebar-overlay {
            display: none !important;
          }
        }
      `}</style>
    </>
  );
});
