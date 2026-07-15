import { memo } from 'react';
import { InventoryItemWorkspaceLayout } from '../layouts/InventoryItemWorkspaceLayout';
import { InventoryItemDashboard } from '../dashboard/InventoryItemDashboard';

export const InventoryItemWorkspace = memo(function InventoryItemWorkspace() {
  return (
    <InventoryItemWorkspaceLayout>
      <InventoryItemDashboard />
    </InventoryItemWorkspaceLayout>
  );
});
