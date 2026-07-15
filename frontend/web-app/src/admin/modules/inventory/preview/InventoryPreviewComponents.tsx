import type React from 'react';
import { memo, useState } from 'react';
import { SearchBar } from '../../../components/search/SearchBar';
import { InventoryWorkspaceProvider } from '../contexts/InventoryWorkspaceContext';
import { useInventoryItems, useInventoryActivities } from '../hooks/useInventoryMockData';
import { useInventoryFilters } from '../hooks/useInventoryFilters';
import { INVENTORY_EMPTY_STATES, INVENTORY_MOCK_METRICS, INVENTORY_QUICK_ACTIONS, INVENTORY_SAVED_FILTERS } from '../constants';

import { MetricCard } from '../components/MetricCard';
import { HealthCard } from '../components/HealthCard';
import { StatusCard } from '../components/StatusCard';
import { SummaryCard } from '../components/SummaryCard';
import { EmptyState } from '../components/EmptyState';
import {
  SkeletonDashboard,
  SkeletonMetricCards,
  SkeletonTable,
  SkeletonSearch,
  SkeletonFilters,
} from '../components/InventorySkeleton';
import { InventoryTable } from '../components/InventoryTable';
import { Toolbar, ActionBar } from '../components/Toolbar';
import { PermissionActionBar } from '../components/ActionBar';
import { FilterPanel } from '../components/FilterPanel';
import { SearchComponent } from '../components/SearchComponent';
import { StatisticsGrid } from '../components/StatisticsGrid';
import { WorkspaceBanner } from '../components/WorkspaceBanner';
import { SectionHeader } from '../components/SectionHeader';
import { QuickActionCard } from '../components/QuickActionCard';
import { RecentActivityCard } from '../components/RecentActivityCard';
import { PermissionPlaceholder } from '../components/PermissionPlaceholder';

const Section: React.FC<{ title: string; children: React.ReactNode }> = ({ title, children }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
    <h2 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{title}</h2>
    {children}
  </section>
);

const grid = (min = 240): React.CSSProperties => ({
  display: 'grid',
  gridTemplateColumns: `repeat(auto-fit, minmax(${min}px, 1fr))`,
  gap: 16,
});

const MetricDemo: React.FC = () => (
  <div style={grid()}>
    <MetricCard metric={{ id: 'total', title: 'Total Items', value: '4820', trend: 'up', percentage: 4.2, comparison: 'vs last month', icon: 'package', color: 'var(--color-primary)' }} />
    <MetricCard metric={{ id: 'low', title: 'Low Stock', value: '142', trend: 'down', percentage: -3, comparison: 'vs last week', icon: 'alert-triangle', color: 'var(--color-warning)' }} />
    <MetricCard loading />
  </div>
);

const HealthDemo: React.FC = () => (
  <div style={grid(200)}>
    <HealthCard health={{ id: 'avail', label: 'Availability', score: 92 }} />
    <HealthCard health={{ id: 'acc', label: 'Accuracy', score: 42 }} loading />
  </div>
);

const StatusDemo: React.FC = () => (
  <div style={grid()}>
    <StatusCard status={{ id: 'low', label: 'Low Stock', count: 142, variant: 'warning' }} />
    <StatusCard status={{ id: 'out', label: 'Out of Stock', count: 37, variant: 'danger' }} />
    <StatusCard status={{ id: 'exp', label: 'Expired', count: 12, variant: 'danger' }} />
    <StatusCard status={{ id: 'dmg', label: 'Damaged', count: 28, variant: 'warning' }} />
  </div>
);

const SummaryDemo: React.FC = () => (
  <div style={grid()}>
    <SummaryCard title="Warehouses" description="12 facilities" icon="home" actions={[{ id: 'view', label: 'View', icon: 'eye' }]}>
      Manage warehouse locations and zones.
    </SummaryCard>
    <SummaryCard title="Loading" loading />
  </div>
);

const EmptyDemo: React.FC = () => {
  const keys = Object.keys(INVENTORY_EMPTY_STATES) as (keyof typeof INVENTORY_EMPTY_STATES)[];
  return (
    <div style={grid()}>
      {keys.map((k) => (
        <EmptyState key={k} stateKey={k} />
      ))}
    </div>
  );
};

const SkeletonDemo: React.FC = () => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
    <SkeletonDashboard />
    <SkeletonMetricCards count={4} />
    <SkeletonTable rows={4} />
    <SkeletonSearch />
    <SkeletonFilters count={4} />
  </div>
);

const TableDemo: React.FC = () => {
  const { data, loading } = useInventoryItems();
  return <InventoryTable data={data ?? []} loading={loading} emptyKey="no_inventory" />;
};

const ToolbarDemo: React.FC = () => {
  const [q, setQ] = useState('');
  return (
    <Toolbar
      title="Inventory Items"
      search={<SearchBar value={q} onChange={setQ} placeholder="Search inventory..." />}
      actions={<ActionBar actions={[{ id: 'add', label: 'New Item', icon: 'plus', variant: 'primary' }]} />}
    />
  );
};

const SearchDemo: React.FC = () => {
  const [q, setQ] = useState('');
  const [fields, setFields] = useState<string[]>([]);
  return (
    <SearchComponent
      query={q}
      onQueryChange={setQ}
      activeFields={fields}
      onToggleField={(f) => setFields((prev) => (prev.includes(f) ? prev.filter((x) => x !== f) : [...prev, f]))}
    />
  );
};

const FilterDemo: React.FC = () => {
  const { data: items } = useInventoryItems();
  const { state, toggle, clear, activeCount } = useInventoryFilters(items ?? []);
  return <FilterPanel state={state} onToggle={toggle} onClear={clear} activeCount={activeCount} />;
};

const StatsDemo: React.FC = () => (
  <StatisticsGrid
    metrics={INVENTORY_MOCK_METRICS}
    health={[{ id: 'avail', label: 'Availability', score: 92 }]}
    statusCounts={[
      { id: 'low', label: 'Low Stock', count: 142, variant: 'warning' },
      { id: 'out', label: 'Out', count: 37, variant: 'danger' },
    ]}
  />
);

const BannerDemo: React.FC = () => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
    <WorkspaceBanner variant="info" title="No new notifications" message="No new notifications." icon="bell" />
    <WorkspaceBanner variant="success" title="Inventory synced" message="Inventory synced." icon="check-circle" />
    <WorkspaceBanner variant="warning" title="Low stock" message="3 items below reorder point." />
    <WorkspaceBanner variant="offline" title="You are offline" message="You are offline." />
  </div>
);

const SectionHeaderDemo: React.FC = () => (
  <SectionHeader
    title="Inventory Items"
    description="Browse and manage items."
    icon="package"
    sublabel="Inventory / Items"
    actions={<ActionBar actions={[{ id: 'export', label: 'Export', icon: 'download' }]} />}
  />
);

const QuickActionDemo: React.FC = () => (
  <div style={grid(140)}>
    {INVENTORY_QUICK_ACTIONS.map((a) => (
      <QuickActionCard key={a.id} action={a} />
    ))}
  </div>
);

const RecentDemo: React.FC = () => {
  const { data } = useInventoryActivities();
  return <RecentActivityCard activities={data ?? []} />;
};

const ActionBarDemo: React.FC = () => (
  <PermissionActionBar
    actions={[
      { id: 'a', label: 'New Item', icon: 'plus', variant: 'primary', permission: 'create' },
      { id: 'b', label: 'Adjust', icon: 'sliders', permission: 'edit' },
      { id: 'c', label: 'Settings', icon: 'settings', permission: 'settings' },
    ]}
  />
);

const PermissionDemo: React.FC = () => (
  <PermissionPlaceholder permission="edit">
    <div style={{ padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)' }}>
      Visible content when edit permission is granted.
    </div>
  </PermissionPlaceholder>
);

export const InventoryPreviewComponents = memo(function InventoryPreviewComponents() {
  return (
    <InventoryWorkspaceProvider>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 28, padding: 16 }}>
        <Section title="Metric Cards"><MetricDemo /></Section>
        <Section title="Health Card"><HealthDemo /></Section>
        <Section title="Status Cards"><StatusDemo /></Section>
        <Section title="Summary Cards"><SummaryDemo /></Section>
        <Section title="Empty States"><EmptyDemo /></Section>
        <Section title="Skeletons"><SkeletonDemo /></Section>
        <Section title="Inventory Table"><TableDemo /></Section>
        <Section title="Toolbar"><ToolbarDemo /></Section>
        <Section title="Search"><SearchDemo /></Section>
        <Section title="Filter Panel"><FilterDemo /></Section>
        <Section title="Statistics Grid"><StatsDemo /></Section>
        <Section title="Banners"><BannerDemo /></Section>
        <Section title="Section Header"><SectionHeaderDemo /></Section>
        <Section title="Quick Actions"><QuickActionDemo /></Section>
        <Section title="Recent Activity"><RecentDemo /></Section>
        <Section title="Action Bar (role-dependent)"><ActionBarDemo /></Section>
        <Section title="Permission Placeholder"><PermissionDemo /></Section>
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Saved filters available: {INVENTORY_SAVED_FILTERS.map((f) => f.label).join(', ')}
        </div>
      </div>
    </InventoryWorkspaceProvider>
  );
});

export default InventoryPreviewComponents;
