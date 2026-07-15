import { memo } from 'react';
import { InventoryWorkspaceProvider } from '../contexts/InventoryWorkspaceContext';
import { InventoryDashboard } from '../dashboard/InventoryDashboard';

export const InventoryPreviewDashboard = memo(function InventoryPreviewDashboard() {
  return (
    <InventoryWorkspaceProvider>
      <div style={{ padding: 16 }}>
        <InventoryDashboard />
      </div>
    </InventoryWorkspaceProvider>
  );
});

export default InventoryPreviewDashboard;

