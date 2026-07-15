import { memo } from 'react';
import { InventoryItemWorkspaceProvider } from './contexts/InventoryItemWorkspaceContext';
import { InventoryItemWorkspaceLayout } from './layouts/InventoryItemWorkspaceLayout';

export const InventoryItemPage = memo(function InventoryItemPage() {
  return (
    <InventoryItemWorkspaceProvider initialRole="administrator">
      <InventoryItemWorkspaceLayout />
    </InventoryItemWorkspaceProvider>
  );
});
