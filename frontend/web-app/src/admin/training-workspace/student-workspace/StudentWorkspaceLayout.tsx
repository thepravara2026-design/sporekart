import { memo, useState, useCallback } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import { NAV_GROUPS, getStudentActiveId, getStudentLabel } from './data/navigation';

const StudentWorkspaceLayout = memo(function StudentWorkspaceLayout() {
  const location = useLocation();
  const navigate = useNavigate();
  const activeId = getStudentActiveId(location.pathname);
  const [navOpen, setNavOpen] = useState(false);

  const handleNavigate = useCallback(
    (href: string) => {
      navigate(href);
      setNavOpen(false);
    },
    [navigate]
  );

  const segments = location.pathname.replace('/admin/training/student-workspace', '').split('/').filter(Boolean);
  const sectionId = segments[0] || 'overview';
  const sectionLabel = getStudentLabel(sectionId);

  return (
    <div style={{ display: 'flex', gap: 0, height: '100%', position: 'relative' }}>
      {navOpen && (
        <div
          onClick={() => setNavOpen(false)}
          style={{
            position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.3)',
            zIndex: 100, display: 'none',
          }}
          className="student-nav-overlay"
        />
      )}

      <nav
        className={`student-nav-rail ${navOpen ? 'student-nav-rail--open' : ''}`}
        style={{
          width: 220, flexShrink: 0,
          background: 'var(--color-bg-surface-default)',
          borderRight: '1px solid var(--color-border-default)',
          overflowY: 'auto', display: 'flex', flexDirection: 'column',
        }}
        aria-label="Student workspace navigation"
      >
        <div style={{
          display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
          padding: 'var(--space-3) var(--space-4)',
          borderBottom: '1px solid var(--color-border-default)',
          minHeight: 48,
        }}>
          <span style={{
            width: 24, height: 24, borderRadius: 'var(--radius-sm)',
            background: 'var(--color-primary)',
            color: 'var(--color-text-on-primary)',
            display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
          }}>
            <Icon name="users" size={14} color="currentColor" />
          </span>
          <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body-sm)' }}>
            Students
          </span>
        </div>

        <div style={{ flex: 1, padding: 'var(--space-2) 0' }}>
          {NAV_GROUPS.map((group) => (
            <div key={group.id} style={{ marginBottom: 'var(--space-2)' }}>
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
              {group.items.map((item) => {
                const isActive = activeId === item.id;
                const isFuture = group.id === 'future';
                return (
                  <button
                    key={item.id}
                    type="button"
                    onClick={() => handleNavigate(item.href)}
                    style={{
                      display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
                      width: '100%', padding: '6px var(--space-4)',
                      background: isActive ? 'var(--color-bg-primary-subtle)' : 'transparent',
                      border: 'none', cursor: isFuture ? 'default' : 'pointer',
                      color: isActive
                        ? 'var(--color-primary)'
                        : isFuture
                          ? 'var(--color-text-tertiary)'
                          : 'var(--color-text-secondary)',
                      borderRadius: 0, fontSize: 'var(--text-body-sm)',
                      position: 'relative', opacity: isFuture ? 0.6 : 1,
                    }}
                    aria-current={isActive ? 'page' : undefined}
                    title={item.label}
                  >
                    {isActive && (
                      <span style={{
                        position: 'absolute', left: 0, top: 2, bottom: 2, width: 3,
                        background: 'var(--color-primary)',
                        borderRadius: '0 var(--radius-sm) var(--radius-sm) 0',
                      }} />
                    )}
                    <Icon name={item.icon} size={16} color="currentColor" />
                    <span style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                      {item.label}
                    </span>
                    {isFuture && (
                      <span style={{
                        marginLeft: 'auto', fontSize: 8, padding: '1px 4px',
                        borderRadius: 'var(--radius-xs)',
                        background: 'var(--color-bg-skeleton-base)',
                        color: 'var(--color-text-tertiary)',
                        whiteSpace: 'nowrap',
                      }}>
                        Soon
                      </span>
                    )}
                  </button>
                );
              })}
            </div>
          ))}
        </div>
      </nav>

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', minWidth: 0 }}>
        <div className="student-mobile-header" style={{
          display: 'none', alignItems: 'center', gap: 'var(--space-inline-sm)',
          padding: 'var(--space-3) var(--space-4)',
          borderBottom: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <button
            type="button"
            onClick={() => setNavOpen(true)}
            style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 4 }}
            aria-label="Open student navigation"
          >
            <Icon name="menu" size={20} color="currentColor" />
          </button>
          <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>
            {sectionLabel}
          </span>
        </div>

        <div style={{ padding: 'var(--space-4)', flex: 1 }}>
          <Outlet />
        </div>
      </div>

      <style>{`
        @media (max-width: 767px) {
          .student-nav-rail {
            position: fixed !important;
            left: -220px;
            top: 0;
            bottom: 0;
            z-index: 110 !important;
            transition: left var(--duration-normal) var(--easing-standard);
            height: 100vh;
          }
          .student-nav-rail--open {
            left: 0 !important;
          }
          .student-nav-overlay {
            display: block !important;
          }
          .student-mobile-header {
            display: flex !important;
          }
        }
      `}</style>
    </div>
  );
});

export default StudentWorkspaceLayout;
