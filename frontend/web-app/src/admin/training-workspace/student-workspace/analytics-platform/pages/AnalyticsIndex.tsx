import { Link } from 'react-router-dom';
import { ANALYTICS_NAV_ITEMS } from '../types';
import { DashboardSkeleton } from '../components/Skeletons';

const ICONS: Record<string, string> = {
  layout: '📊', users: '👥', book: '📚', 'user-check': '✅',
  layers: '🗂️', brain: '🧠', activity: '📈',
};

export default function AnalyticsIndex() {
  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Analytics & Learning Intelligence</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)', margin: '4px 0 0' }}>
          Enterprise-wide dashboards and deep learning insights
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {ANALYTICS_NAV_ITEMS.map((item) => (
          <Link key={item.id} to={item.id} style={{ textDecoration: 'none', color: 'inherit' }}>
            <div style={{
              padding: 'var(--space-4)', borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)',
              background: 'var(--color-bg-surface-default)',
              display: 'flex', flexDirection: 'column', gap: 8,
              transition: 'border-color 0.15s, box-shadow 0.15s',
              cursor: 'pointer',
            }} onMouseEnter={(e) => { e.currentTarget.style.borderColor = 'var(--color-border-hover)'; e.currentTarget.style.boxShadow = 'var(--shadow-sm)'; }} onMouseLeave={(e) => { e.currentTarget.style.borderColor = 'var(--color-border-default)'; e.currentTarget.style.boxShadow = 'none'; }}>
              <span style={{ fontSize: 28 }}>{ICONS[item.icon] || '📊'}</span>
              <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{item.label}</h3>
              <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: 0 }}>{item.description}</p>
            </div>
          </Link>
        ))}
      </div>

      <DashboardSkeleton />
    </main>
  );
}
