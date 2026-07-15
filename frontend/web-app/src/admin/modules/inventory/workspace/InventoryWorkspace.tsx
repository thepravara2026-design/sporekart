import { memo, useMemo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_QUICK_ACTIONS, INVENTORY_SECTIONS } from '../constants';
import { useInventoryWorkspace } from '../contexts/InventoryWorkspaceContext';
import { useInventoryPermissions } from '../hooks/useInventoryPermissions';
import { useInventoryDashboard } from '../hooks/useInventoryMockData';
import { SectionHeader } from '../components/SectionHeader';
import { StatisticsGrid } from '../components/StatisticsGrid';
import { QuickActionCard } from '../components/QuickActionCard';
import { SummaryCard } from '../components/SummaryCard';
import { WorkspaceBanner } from '../components/WorkspaceBanner';
import { SkeletonMetricCards } from '../components/InventorySkeleton';

export const InventoryWorkspace = memo(function InventoryWorkspace() {
  const { setActiveSection } = useInventoryWorkspace();
  const { can } = useInventoryPermissions();
  const metricsState = useInventoryDashboard();

  const visibleActions = useMemo(
    () => INVENTORY_QUICK_ACTIONS.filter((a) => !a.permission || can(a.permission)),
    [can],
  );

  const panels = useMemo(() => INVENTORY_SECTIONS.filter((s) => s.id !== 'overview'), []);

  const quickActionSection: Record<string, string> = {
    new_item: 'items',
    receive: 'receiving',
    transfer: 'transfers',
    adjust: 'adjustments',
    new_warehouse: 'warehouses',
    validate: 'validation',
  };

  const handleSection = (id: string) => {
    setActiveSection(id);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <SectionHeader
        title="Inventory Workspace"
        description="Manage warehouses, stock, movements, and inventory operations."
        icon="grid"
        sublabel="Inventory / Overview"
      />

      <WorkspaceBanner variant="info" title="Inventory workspace hub" message="Sections are extensible placeholders for upcoming sprints." />

      {metricsState.loading ? (
        <SkeletonMetricCards count={6} />
      ) : (
        <StatisticsGrid metrics={(metricsState.data?.metrics ?? []).slice(0, 6)} />
      )}

      <section aria-label="Quick actions">
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Quick Actions</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(140px, 1fr))', gap: 12 }}>
          {visibleActions.map((a) => (
            <QuickActionCard key={a.id} action={a} onClick={() => handleSection(quickActionSection[a.id] ?? 'overview')} />
          ))}
        </div>
      </section>

      <section aria-label="Inventory sections">
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Sections</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 16 }}>
          {panels.map((s) => (
            <SummaryCard
              key={s.id}
              title={s.label}
              description={s.description}
              icon={s.icon}
              actions={[{ id: `${s.id}-view`, label: 'View', icon: 'arrow-right', onClick: () => handleSection(s.id) }]}
            >
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>
                <Icon name="layers" size={14} /> Extensible placeholder panel
              </span>
            </SummaryCard>
          ))}
        </div>
      </section>
    </div>
  );
});
