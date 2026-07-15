import { InventoryWorkspaceProvider } from './contexts/InventoryWorkspaceContext';
import { InventoryWorkspaceLayout } from './layouts/InventoryWorkspaceLayout';

export function InventoryPage() {
  return (
    <InventoryWorkspaceProvider>
      <InventoryWorkspaceLayout />
    </InventoryWorkspaceProvider>
  );
}

export default InventoryPage;
