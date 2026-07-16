import { useState, useCallback } from 'react';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import { AuthenticatedLayout } from '../design-system/components/layout/AuthenticatedLayout';
import { PermissionProvider } from './permissions/PermissionProvider';
import { Sidebar } from '../design-system/components/navigation/Sidebar';
import { TopNav } from '../design-system/components/navigation/TopNav';
import { Breadcrumb } from '../design-system/components/navigation/Breadcrumb';
import { PageHeader } from '../design-system/components/layout/PageHeader';
import { PageFooter } from '../design-system/components/layout/PageFooter';
import { Icon } from '../design-system/icons/Icon';
import { useApp } from '../context';
import {
  getFilteredSidebarItems,
  ADMIN_TOP_NAV,
  buildAdminBreadcrumbs,
  getAdminActiveId,
} from './config/adminNavigation';
import type { SidebarItemData } from '../design-system/components/navigation/SidebarItem';
import type { TopNavItem } from '../design-system/components/navigation/NavItem';
import './admin.css';

export default function AdminLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { activeRole } = useApp();
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [pinnedIds, setPinnedIds] = useState<string[]>([]);

  const sidebarItems = getFilteredSidebarItems(activeRole);

  const handleSidebarNavigate = useCallback((item: SidebarItemData) => {
    if (item.href) {
      navigate(item.href);
      if (window.innerWidth < 768) setSidebarOpen(false);
    }
  }, [navigate]);

  const handleTopNavNavigate = useCallback((item: TopNavItem) => {
    if (item.href) navigate(item.href);
  }, [navigate]);

  const handleTogglePin = useCallback((id: string) => {
    setPinnedIds((prev) =>
      prev.includes(id) ? prev.filter((pid) => pid !== id) : [...prev, id],
    );
  }, []);

  const crumbs = buildAdminBreadcrumbs(location.pathname);
  const activeId = getAdminActiveId(location.pathname);

  return (
    <PermissionProvider initialRole={activeRole || 'viewer'}>
    <AuthenticatedLayout
      header={
        <div className="admin-header" style={{
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          height: '100%', padding: '0 var(--space-page-x)',
          borderBottom: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
            <button
              type="button"
              onClick={() => setSidebarOpen(true)}
              style={{
                display: 'none', background: 'none', border: 'none',
                cursor: 'pointer', color: 'var(--color-text-primary)', padding: 4,
              }}
              className="admin-header__menu-btn"
              aria-label="Open sidebar"
            >
              <Icon name="menu" size={20} color="currentColor" />
            </button>
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
              <span style={{
                width: 28, height: 28, borderRadius: 'var(--radius-sm)',
                background: 'var(--color-primary)',
                color: 'var(--color-text-on-primary)',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                flexShrink: 0,
              }}>
                <Icon name="shield" size={16} color="currentColor" />
              </span>
              <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body-lg)' }}>Admin</span>
            </div>

            <div className="admin-header__search" style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
              marginLeft: 'var(--space-inline-md)',
              padding: '6px 12px',
              borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-background)',
              border: '1px solid var(--color-border-default)',
              color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-caption)',
              cursor: 'pointer',
            }} aria-label="Search placeholder">
              <Icon name="search" size={14} color="currentColor" />
              <span>Search admin...</span>
              <span style={{
                marginLeft: 'var(--space-inline-sm)',
                padding: '1px 4px', borderRadius: 'var(--radius-xs)',
                border: '1px solid var(--color-border-default)',
                fontSize: 10, lineHeight: '14px',
              }}>Ctrl+K</span>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
            <div className="admin-header__actions" style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
              marginRight: 'var(--space-inline-sm)',
            }}>
              <button type="button" style={{
                background: 'none', border: 'none', cursor: 'pointer',
                color: 'var(--color-text-secondary)', padding: 6,
                borderRadius: 'var(--radius-sm)',
              }} aria-label="Workspace actions">
                <Icon name="grid" size={18} color="currentColor" />
              </button>
              <button type="button" style={{
                background: 'none', border: 'none', cursor: 'pointer',
                color: 'var(--color-text-secondary)', padding: 6,
                borderRadius: 'var(--radius-sm)',
                position: 'relative',
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
              }} aria-label="Toggle theme">
                <Icon name="moon" size={18} color="currentColor" />
              </button>
            </div>
            <TopNav
              items={ADMIN_TOP_NAV}
              activeId={activeId}
              onNavigate={handleTopNavNavigate}
              variant="primary"
              aria-label="Admin actions"
            />
          </div>
        </div>
      }
      sidebar={
        <Sidebar
          items={sidebarItems}
          variant="primary"
          collapsed={sidebarCollapsed}
          onCollapse={setSidebarCollapsed}
          onNavigate={handleSidebarNavigate}
          activeId={activeId}
          pinnedIds={pinnedIds}
          onTogglePin={handleTogglePin}
          header={
            <div style={{
              display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)',
              width: '100%',
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
                <span style={{
                  width: 32, height: 32, borderRadius: 'var(--radius-sm)',
                  background: 'var(--color-primary)',
                  color: 'var(--color-text-on-primary)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
                }}>
                  <Icon name="shield" size={18} color="currentColor" />
                </span>
                <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body-lg)' }}>SporeKart</span>
              </div>
              <div className="admin-sidebar-search" style={{
                display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
                padding: '6px 10px',
                borderRadius: 'var(--radius-input)',
                background: 'var(--color-bg-background)',
                border: '1px solid var(--color-border-default)',
                color: 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }} aria-label="Search admin sidebar">
                <Icon name="search" size={14} color="currentColor" />
                <span>Search...</span>
              </div>
            </div>
          }
          footer={
            <div style={{
              padding: 'var(--space-stack-sm) var(--space-page-x)',
              borderTop: '1px solid var(--color-border-default)',
              fontSize: 'var(--text-caption)',
              color: 'var(--color-text-secondary)', textAlign: 'center',
            }}>
              v1.0.0 · Admin Workspace
            </div>
          }
          responsive
          open={sidebarOpen}
          onClose={() => setSidebarOpen(false)}
          aria-label="Admin workspace navigation"
        />
      }
    >
      <div className="admin-workspace" style={{
        display: 'flex', flexDirection: 'column', minHeight: '100%',
        padding: 'var(--space-section-gap) var(--space-page-x)',
      }}>
        <PageHeader
          title={crumbs[crumbs.length - 1]?.label || 'Dashboard'}
          description={`Enterprise Administration · ${crumbs[0]?.label || ''}`}
        />
        <div style={{ marginTop: 'var(--space-stack-xs)', marginBottom: 'var(--space-section-gap)' }}>
          <Breadcrumb crumbs={crumbs} maxItems={4} collapsedLabel="\u2026" aria-label="Breadcrumb" />
        </div>
        <div id="main" tabIndex={-1} style={{ flex: 1 }}>
          <Outlet />
        </div>
        <PageFooter>
          <span>&copy; {new Date().getFullYear()} SporeKart. All rights reserved.</span>
          <nav style={{ display: 'flex', gap: 'var(--space-inline-md)' }}>
            <a href="/privacy-policy" style={{ color: 'var(--color-text-secondary)' }}>Privacy</a>
            <a href="/terms-and-conditions" style={{ color: 'var(--color-text-secondary)' }}>Terms</a>
          </nav>
        </PageFooter>
      </div>
    </AuthenticatedLayout>
    </PermissionProvider>
  );
}
