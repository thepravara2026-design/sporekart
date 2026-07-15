import { memo } from 'react';
import { StockWorkspaceLayout } from '../layouts/StockWorkspaceLayout';
import { StockDashboard } from '../dashboard/StockDashboard';

export const StockWorkspace = memo(function StockWorkspace() {
  return (
    <StockWorkspaceLayout>
      <StockDashboard />
    </StockWorkspaceLayout>
  );
});
