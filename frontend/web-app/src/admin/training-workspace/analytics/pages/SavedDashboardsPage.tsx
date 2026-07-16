import { memo } from 'react';
import { Link } from 'react-router-dom';
import { Icon } from '../../../../design-system/icons/Icon';
import WidgetGrid from '../components/WidgetGrid';
import { SAVED_DASHBOARDS, type SavedDashboard } from '../data/analyticsMockData';

const SECTIONS: { kind: SavedDashboard['kind']; label: string; icon: string }[] = [
  { kind: 'pinned', label: 'Pinned Dashboards', icon: 'sliders' },
  { kind: 'favorite', label: 'Favorites', icon: 'star' },
  { kind: 'recent', label: 'Recently Viewed', icon: 'file' },
];

const SavedDashboardsPage = memo(function SavedDashboardsPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-5)', minWidth: 0 }}>
      {SECTIONS.map((section) => {
        const items = SAVED_DASHBOARDS.filter((d) => d.kind === section.kind);
        return (
          <section key={section.kind} aria-label={section.label}>
            <h2 style={{ display: 'flex', alignItems: 'center', gap: 6, margin: '0 0 var(--space-3)', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>
              <Icon name={section.icon} size={16} color="var(--color-primary)" />
              {section.label}
            </h2>
            <WidgetGrid minColWidth={260}>
              {items.map((item) => (
                <Link
                  key={item.id}
                  to={item.route}
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 4,
                    padding: 'var(--space-4)',
                    textDecoration: 'none',
                    background: 'var(--color-bg-surface-raised)',
                    border: '1px solid var(--color-border-default)',
                    borderRadius: 'var(--radius-lg)',
                    boxShadow: 'var(--shadow-1)',
                  }}
                >
                  <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{item.title}</span>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
                    Updated {item.updatedAt}
                  </span>
                </Link>
              ))}
            </WidgetGrid>
          </section>
        );
      })}
    </div>
  );
});

export default SavedDashboardsPage;
