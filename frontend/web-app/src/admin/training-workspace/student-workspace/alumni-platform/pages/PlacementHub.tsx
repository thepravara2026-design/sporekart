import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { PlacementDriveCard } from '../components/PlacementDriveCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';
import { DRIVE_STAGE_LABELS } from '../types';
import type { PlacementDriveStage } from '../types';

export default function PlacementHub() {
  const { placementDrives, getFilteredDrives, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<PlacementDriveStage | 'all'>('all');

  const stages = useMemo(() => {
    const counts: Record<string, number> = {};
    placementDrives.forEach((d) => { counts[d.stage] = (counts[d.stage] || 0) + 1; });
    return counts;
  }, [placementDrives]);

  const tabs = useMemo(() => [
    { key: 'all', label: 'All', count: placementDrives.length },
    ...Object.entries(DRIVE_STAGE_LABELS).map(([key, label]) => ({
      key, label, count: stages[key] || 0,
    })),
  ], [placementDrives.length, stages]);

  const filtered = useMemo(() => {
    let items = getFilteredDrives();
    if (activeTab !== 'all') items = items.filter((d) => d.stage === activeTab);
    return items;
  }, [getFilteredDrives, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const activeCount = placementDrives.filter((d) => d.stage !== 'completed' && d.stage !== 'cancelled').length;

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Placement Hub</h1>
        <SharedFilters currentPage="alumni/placement" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Drives" value={placementDrives.length} icon="🚀" />
        <MetricCard label="Active" value={activeCount} icon="✅" color="#16a34a" />
        <MetricCard label="Completed" value={placementDrives.filter((d) => d.stage === 'completed').length} icon="✅" color="#6b7280" />
        <MetricCard label="Total Registrations" value={placementDrives.reduce((s, d) => s + d.registeredStudents, 0)} icon="📝" />
        <MetricCard label="Total Selected" value={placementDrives.reduce((s, d) => s + d.selectedStudents, 0)} icon="🎯" color="#059669" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key as PlacementDriveStage | 'all'); setPage(1); }}
            style={{
              padding: '4px 12px', borderRadius: 'var(--radius-full)', border: '1px solid var(--color-border-default)',
              cursor: 'pointer', fontSize: 'var(--text-caption)', whiteSpace: 'nowrap',
              background: activeTab === tab.key ? 'var(--color-primary)' : 'transparent',
              color: activeTab === tab.key ? 'var(--color-text-on-primary)' : 'var(--color-text-secondary)',
              fontWeight: activeTab === tab.key ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            }}
          >
            {tab.label} ({tab.count})
          </button>
        ))}
      </div>

      <DashboardWidget title="Placement Drives" subtitle={`${filtered.length} drives found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noPlacements" onClearFilters={() => { setActiveTab('all'); }} />
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayItems.map((d) => <PlacementDriveCard key={d.id} drive={d} />)}
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
