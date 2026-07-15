import { memo } from 'react';
import { useMovementWorkspace } from '../contexts/MovementWorkspaceContext';
import { MovementWorkspaceLayout } from '../layouts/MovementWorkspaceLayout';
import { MovementDashboard } from '../dashboard/MovementDashboard';
import { TransactionsPage } from '../pages/TransactionsPage';
import { MovementsPage } from '../pages/MovementsPage';
import { TransfersPage } from '../pages/TransfersPage';
import { AdjustmentsPage } from '../pages/AdjustmentsPage';
import { GoodsReceiptPage } from '../pages/GoodsReceiptPage';
import { GoodsIssuePage } from '../pages/GoodsIssuePage';
import { HistoryPage } from '../pages/HistoryPage';
import { ValidationPage } from '../pages/ValidationPage';
import { AnalyticsPage } from '../pages/AnalyticsPage';
import { ReportsPage } from '../pages/ReportsPage';
import { AuditPage } from '../pages/AuditPage';
import { SettingsPage } from '../pages/SettingsPage';
import { HelpPage } from '../pages/HelpPage';

const PAGE_MAP: Record<string, React.ComponentType> = {
  overview: MovementDashboard,
  transactions: TransactionsPage,
  movements: MovementsPage,
  transfers: TransfersPage,
  adjustments: AdjustmentsPage,
  'goods-receipt': GoodsReceiptPage,
  'goods-issue': GoodsIssuePage,
  history: HistoryPage,
  validation: ValidationPage,
  analytics: AnalyticsPage,
  reports: ReportsPage,
  audit: AuditPage,
  settings: SettingsPage,
  help: HelpPage,
};

export const MovementWorkspace = memo(function MovementWorkspace() {
  const { activeSection, setActiveSection, searchQuery, setSearchQuery } = useMovementWorkspace();
  const PageComponent = PAGE_MAP[activeSection] ?? MovementDashboard;

  return (
    <MovementWorkspaceLayout activeSection={activeSection} onSectionChange={setActiveSection} searchQuery={searchQuery} onSearchChange={setSearchQuery}>
      <PageComponent />
    </MovementWorkspaceLayout>
  );
});
