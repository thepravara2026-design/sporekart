import { memo } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import { Icon } from '../../../../design-system/icons/Icon';
import { ANALYTICS_SCOPES } from '../data/analyticsOptions';

const SCOPE_ROUTES: Record<string, { path: string; icon: string; enabled: boolean }> = {
  executive: { path: '/admin/training/lms-analytics/executive', icon: 'sliders', enabled: true },
  course: { path: '/admin/training/lms-analytics/course', icon: 'book', enabled: true },
  enrollment: { path: '/admin/training/lms-analytics/enrollment', icon: 'user-check', enabled: true },
  curriculum: { path: '/admin/training/lms-analytics/curriculum', icon: 'file', enabled: true },
  resource: { path: '/admin/training/lms-analytics/resource', icon: 'database', enabled: true },
  student: { path: '/admin/training/lms-analytics/student', icon: 'user-check', enabled: false },
  trainer: { path: '/admin/training/lms-analytics/trainer', icon: 'star', enabled: false },
  financial: { path: '/admin/training/lms-analytics/financial', icon: 'tag', enabled: false },
};

const AnalyticsWorkspaceRoute = memo(function AnalyticsWorkspaceRoute() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)', minWidth: 0 }}>
      <header style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
        <Icon name="sliders" size={20} color="var(--color-primary)" />
        <div>
          <h1 style={{ margin: 0, fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)' }}>
            LMS Analytics
          </h1>
          <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
            Executive & operational insights (Mock Mode)
          </p>
        </div>
      </header>

      <nav
        aria-label="Analytics sections"
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: 'var(--space-2)',
          borderBottom: '1px solid var(--color-border-default)',
          paddingBottom: 'var(--space-2)',
        }}
      >
        {ANALYTICS_SCOPES.map((scope) => {
          const meta = SCOPE_ROUTES[scope.value];
          if (!meta.enabled) {
            return (
              <span
                key={scope.value}
                title="Coming soon"
                style={{
                  display: 'inline-flex',
                  alignItems: 'center',
                  gap: 4,
                  padding: 'var(--space-1) var(--space-3)',
                  fontSize: 'var(--text-body-sm)',
                  color: 'var(--color-text-disabled)',
                  borderRadius: 'var(--radius-md)',
                  cursor: 'default',
                }}
              >
                <Icon name={meta.icon} size={14} color="currentColor" />
                {scope.label}
              </span>
            );
          }
          return (
            <NavLink
              key={scope.value}
              to={meta.path}
              style={({ isActive }) => ({
                display: 'inline-flex',
                alignItems: 'center',
                gap: 4,
                padding: 'var(--space-1) var(--space-3)',
                fontSize: 'var(--text-body-sm)',
                textDecoration: 'none',
                borderRadius: 'var(--radius-md)',
                background: isActive ? 'var(--color-bg-primary-weak)' : 'transparent',
                color: isActive ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontWeight: isActive ? 600 : 400,
              })}
            >
              <Icon name={meta.icon} size={14} color="currentColor" />
              {scope.label}
            </NavLink>
          );
        })}
      </nav>

      <Outlet />
    </div>
  );
});

export default AnalyticsWorkspaceRoute;
