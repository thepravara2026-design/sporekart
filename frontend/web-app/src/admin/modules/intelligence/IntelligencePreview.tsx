import { memo } from 'react';
import { IntelligenceWorkspaceProvider, useIntelligenceWorkspace } from './contexts/IntelligenceWorkspaceContext';
import { IntelligenceLayout } from './components/IntelligenceLayout';
import { IntelligenceDashboard } from './components/IntelligenceDashboard';

const PREVIEW_TABS = [
  { id: 'overview', label: 'Overview' },
  { id: 'kpis', label: 'Executive KPIs' },
  { id: 'warehouse', label: 'Warehouse' },
  { id: 'forecast', label: 'Forecast' },
  { id: 'reports', label: 'Reports' },
  { id: 'insights', label: 'Insights' },
];

const PreviewInner = memo(function PreviewInner() {
  const { activeSection, setActiveSection } = useIntelligenceWorkspace();
  return (
    <div style={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
      <div style={{ display: 'flex', gap: 0, borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', padding: '0 16px' }}>
        <span style={{ padding: '10px 16px', fontWeight: 700, color: 'var(--color-text-primary)', whiteSpace: 'nowrap', display: 'flex', alignItems: 'center', gap: 6 }}>
          <span style={{ color: 'var(--color-primary)' }}>SPOREKART</span>
          <span style={{ color: 'var(--color-text-tertiary)', fontWeight: 400 }}>/</span>
          <span>Intelligence Preview</span>
        </span>
        <div style={{ flex: 1 }} />
        {PREVIEW_TABS.map((t) => (
          <button key={t.id} onClick={() => setActiveSection(t.id)}
            style={{ padding: '10px 14px', border: 'none', background: activeSection === t.id ? 'var(--color-primary-alpha, rgba(99,102,241,0.1))' : 'transparent', color: activeSection === t.id ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontWeight: activeSection === t.id ? 600 : 400, cursor: 'pointer', whiteSpace: 'nowrap', fontSize: 'var(--text-caption)', borderBottom: activeSection === t.id ? '2px solid var(--color-primary)' : '2px solid transparent' }}>
            {t.label}
          </button>
        ))}
      </div>
      <div style={{ flex: 1, overflow: 'auto' }}>
        <IntelligenceLayout>
          <IntelligenceDashboard />
        </IntelligenceLayout>
      </div>
    </div>
  );
});

export const IntelligencePreview = memo(function IntelligencePreview() {
  return (
    <IntelligenceWorkspaceProvider initialSection="overview">
      <PreviewInner />
    </IntelligenceWorkspaceProvider>
  );
});
