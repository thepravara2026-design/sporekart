import { EnrollmentProvider, useEnrollmentContext } from '../state/EnrollmentContext';
import { EnrollmentSidebar } from './shared/EnrollmentSidebar';
import { OverviewPanel } from './panels/OverviewPanel';
import { PricingPanel } from './panels/PricingPanel';
import { EnrollmentPanel } from './panels/EnrollmentPanel';
import { CapacityPanel } from './panels/CapacityPanel';
import { PoliciesPanel } from './panels/PoliciesPanel';
import { EligibilityPanel } from './panels/EligibilityPanel';
import { LifecyclePanel } from './panels/LifecyclePanel';
import { WaitlistPanel } from './panels/WaitlistPanel';
import { PaymentReadinessPanel } from './panels/PaymentReadinessPanel';
import type { EnrollmentSection } from '../data/enrollmentMockData';

const PANEL_COMPONENTS: Record<EnrollmentSection, React.FC> = {
  overview: OverviewPanel,
  pricing: PricingPanel,
  enrollment: EnrollmentPanel,
  capacity: CapacityPanel,
  policies: PoliciesPanel,
  eligibility: EligibilityPanel,
  lifecycle: LifecyclePanel,
  waitlist: WaitlistPanel,
  payment: PaymentReadinessPanel,
};

function PanelContent() {
  const { state } = useEnrollmentContext();
  const PanelComponent = PANEL_COMPONENTS[state.section] || OverviewPanel;
  return <PanelComponent />;
}

function EnrollmentHeader() {
  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: 'var(--space-2) var(--space-4)',
        borderBottom: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-primary)',
      }}
    >
      <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>Pricing &amp; Enrollment</span>
      <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>Training Commerce &amp; Capacity · Mock Mode</span>
    </div>
  );
}

export function EnrollmentLayout() {
  return (
    <EnrollmentProvider>
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
        <EnrollmentHeader />
        <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
          <EnrollmentSidebar />
          <main
            style={{ flex: 1, overflowY: 'auto', padding: 'var(--space-4)', background: 'var(--color-bg-secondary)' }}
            aria-label="Pricing and enrollment panel content"
            role="tabpanel"
          >
            <div style={{ maxWidth: 1200, margin: '0 auto' }}>
              <PanelContent />
            </div>
          </main>
        </div>
      </div>
    </EnrollmentProvider>
  );
}
