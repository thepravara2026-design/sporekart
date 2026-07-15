import { memo, type ComponentType } from 'react';
import { useReceivingWorkspace } from '../contexts/ReceivingWorkspaceContext';
import { ReceivingWorkspaceLayout } from '../layouts/ReceivingWorkspaceLayout';
import { ReceivingDashboard } from '../dashboard/ReceivingDashboard';
import { GoodsReceiptPage } from '../pages/GoodsReceiptPage';
import { ReceivingQueuePage } from '../pages/ReceivingQueuePage';
import { InspectionPage } from '../pages/InspectionPage';
import { AcceptancePage } from '../pages/AcceptancePage';
import { RejectionPage } from '../pages/RejectionPage';
import { PendingReceiptsPage } from '../pages/PendingReceiptsPage';
import { AllocationPage } from '../pages/AllocationPage';
import { BatchAssignmentPage } from '../pages/BatchAssignmentPage';
import { TimelinePage } from '../pages/TimelinePage';
import { ValidationPage } from '../pages/ValidationPage';
import { AnalyticsPage } from '../pages/AnalyticsPage';
import { ReportsPage } from '../pages/ReportsPage';
import { SettingsPage } from '../pages/SettingsPage';
import { HelpPage } from '../pages/HelpPage';

const PAGE_MAP: Record<string, ComponentType> = {
  overview: ReceivingDashboard,
  'goods-receipt': GoodsReceiptPage,
  'receiving-queue': ReceivingQueuePage,
  inspection: InspectionPage,
  acceptance: AcceptancePage,
  rejection: RejectionPage,
  pending: PendingReceiptsPage,
  allocation: AllocationPage,
  batch: BatchAssignmentPage,
  timeline: TimelinePage,
  validation: ValidationPage,
  analytics: AnalyticsPage,
  reports: ReportsPage,
  settings: SettingsPage,
  help: HelpPage,
};

export const ReceivingWorkspace = memo(function ReceivingWorkspace() {
  const { activeSection, setActiveSection, searchQuery, setSearchQuery } = useReceivingWorkspace();
  const PageComponent = PAGE_MAP[activeSection] ?? ReceivingDashboard;

  return (
    <ReceivingWorkspaceLayout activeSection={activeSection} onSectionChange={setActiveSection} searchQuery={searchQuery} onSearchChange={setSearchQuery}>
      <PageComponent />
    </ReceivingWorkspaceLayout>
  );
});
