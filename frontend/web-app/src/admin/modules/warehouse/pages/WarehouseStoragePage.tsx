import { memo, useMemo } from 'react';
import { useWarehouseStorageNodes } from '../hooks/useWarehouseData';
import { SectionHeader } from '../components';
import { MetricCard } from '../components';
import { SummaryCard } from '../components';
import { StorageHierarchy } from '../components';
import { EmptyState } from '../components';
import { SkeletonTable } from '../components';

export const WarehouseStoragePage = memo(function WarehouseStoragePage() {
  const storageState = useWarehouseStorageNodes();
  const nodes = storageState.data ?? [];

  const counts = useMemo(() => {
    const c: Record<string, number> = {};
    nodes.forEach((n) => { c[n.level] = (c[n.level] ?? 0) + 1; });
    return c;
  }, [nodes]);

  if (storageState.loading) return <SkeletonTable rows={6} />;
  if (nodes.length === 0) return <EmptyState stateKey="no_storage" />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <SectionHeader title="Storage Hierarchy" description="Warehouse → Building → Floor → Zone → Rack → Shelf → Bin" icon="git-branch" sublabel="Warehouses / Storage" />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))', gap: 12 }}>
        {['warehouse', 'building', 'floor', 'zone', 'rack', 'shelf', 'bin'].map((lvl) => (
          <MetricCard key={lvl} metric={{ id: lvl, label: lvl.charAt(0).toUpperCase() + lvl.slice(1) + 's', value: String(counts[lvl] ?? 0), trend: 'neutral', icon: 'layers' }} />
        ))}
      </div>

      <SummaryCard title="Full Hierarchy" description="Expandable tree of all storage nodes" icon="list">
        <StorageHierarchy nodes={nodes} />
      </SummaryCard>
    </div>
  );
});


