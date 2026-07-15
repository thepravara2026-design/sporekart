import { memo } from 'react';
import { ReceivingDashboard } from '../../dashboard/ReceivingDashboard';
import { ReceivingTable } from '../../components/ReceivingTable';
import { useReceivingData } from '../../hooks/useReceivingData';
import { ReceivingTimeline } from '../../components/ReceivingTimeline';
import { InspectionPage } from '../../pages/InspectionPage';
import { AnalyticsPage } from '../../pages/AnalyticsPage';

export const PreviewDashboardPage = memo(function PreviewDashboardPage() {
  return <ReceivingDashboard />;
});

export const PreviewRegistryPage = memo(function PreviewRegistryPage() {
  const { receipts, loading } = useReceivingData();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Goods Receipt Registry ({receipts.length})</h3>
      <ReceivingTable receipts={receipts} loading={loading} />
    </div>
  );
});

export const PreviewInspectionPage = memo(function PreviewInspectionPage() {
  return <InspectionPage />;
});

export const PreviewTimelinePage = memo(function PreviewTimelinePage() {
  const { timeline } = useReceivingData();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Receiving Timeline</h3>
      <ReceivingTimeline events={timeline.slice(0, 8)} />
    </div>
  );
});

export const PreviewAnalyticsPage = memo(function PreviewAnalyticsPage() {
  return <AnalyticsPage />;
});

export const PreviewWorkflowPage = memo(function PreviewWorkflowPage() {
  const steps = [
    { label: 'Receipt Created', status: 'completed', desc: 'Goods receipt initialized' },
    { label: 'Goods Arrived', status: 'completed', desc: 'Goods at warehouse dock' },
    { label: 'Inspection', status: 'active', desc: 'Quality inspection in progress' },
    { label: 'Acceptance / Rejection', status: 'pending', desc: 'Accept or reject received goods' },
    { label: 'Batch Assignment', status: 'pending', desc: 'Link batch/lot to receipt' },
    { label: 'Warehouse Allocation', status: 'pending', desc: 'Assign storage location' },
    { label: 'Inventory Activation', status: 'pending', desc: 'Goods become operational stock' },
    { label: 'Completed', status: 'pending', desc: 'Receipt finalized' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Receiving Workflow</h3>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
        {steps.map((step, idx) => (
          <div key={step.label} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '8px 0' }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <div style={{ width: 28, height: 28, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
                background: step.status === 'completed' ? 'var(--color-success)' : step.status === 'active' ? 'var(--color-primary)' : 'var(--color-border)',
                color: '#fff', fontWeight: 700, fontSize: 12 }}>{idx + 1}</div>
              {idx < steps.length - 1 && <div style={{ width: 2, height: 24, background: step.status === 'completed' ? 'var(--color-success)' : 'var(--color-border)' }} />}
            </div>
            <div style={{ flex: 1 }}>
              <div style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: step.status === 'pending' ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)' }}>{step.label}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{step.desc}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
