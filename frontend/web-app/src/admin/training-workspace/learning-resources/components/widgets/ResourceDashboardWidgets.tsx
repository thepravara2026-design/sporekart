import { useMemo } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useResourceContext } from '../../state/ResourceContext';
import { RESOURCE_TYPE_LABELS, type ResourceType } from '../../data/resourceMockData';

function StatCard({ label, value, hint }: { label: string; value: string | number; hint?: string }) {
  return (
    <Card variant="elevated" padding="md">
      <div style={{ fontSize: 11, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>{label}</div>
      <div style={{ fontSize: 28, fontWeight: 800 }}>{value}</div>
      {hint && <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{hint}</div>}
    </Card>
  );
}

export function ResourceDashboardWidgets() {
  const { state } = useResourceContext();

  const stats = useMemo(() => {
    const total = state.resources.length;
    const active = state.resources.filter((r) => r.status === 'active').length;
    const archived = state.resources.filter((r) => r.status === 'archived').length;
    const favorites = state.resources.filter((r) => r.favorite).length;
    const totalUses = state.resources.reduce((sum, r) => sum + r.usageCount, 0);

    const byType = state.resources.reduce<Record<string, number>>((acc, r) => {
      acc[r.type] = (acc[r.type] || 0) + 1;
      return acc;
    }, {});

    const topUsed = [...state.resources].sort((a, b) => b.usageCount - a.usageCount).slice(0, 5);

    return { total, active, archived, favorites, totalUses, byType, topUsed };
  }, [state.resources]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-3)' }}>
        <StatCard label="Total Resources" value={stats.total} hint="Across all categories" />
        <StatCard label="Active" value={stats.active} hint="Published & available" />
        <StatCard label="Favorites" value={stats.favorites} hint="Starred by team" />
        <StatCard label="Archived" value={stats.archived} hint="Retained for reference" />
        <StatCard label="Collections" value={state.collections.length} hint="Curated bundles" />
        <StatCard label="Total Usage" value={stats.totalUses} hint="Cumulative opens" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-4)' }}>
        <Card variant="outlined" padding="md">
          <h3 style={{ marginTop: 0 }}>Resources by Type</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {Object.entries(stats.byType).map(([type, count]) => (
              <div key={type} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ flex: 1, fontSize: 'var(--font-size-sm)' }}>{RESOURCE_TYPE_LABELS[type as ResourceType]}</span>
                <div style={{ flex: 2, height: 8, background: 'var(--color-bg-secondary)', borderRadius: 4, overflow: 'hidden' }}>
                  <div style={{ width: `${(count / stats.total) * 100}%`, height: '100%', background: 'var(--color-primary-500)' }} />
                </div>
                <Badge variant="neutral" size="sm">{count}</Badge>
              </div>
            ))}
          </div>
        </Card>

        <Card variant="outlined" padding="md">
          <h3 style={{ marginTop: 0 }}>Most Used Resources</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {stats.topUsed.map((r, i) => (
              <div key={r.id} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <Badge variant="primary" size="sm">{i + 1}</Badge>
                <span style={{ flex: 1, fontSize: 'var(--font-size-sm)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{r.name}</span>
                <Badge variant="info" size="sm">{r.usageCount}</Badge>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
}
