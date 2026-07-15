import { memo } from 'react';
import { InventoryWorkspaceProvider } from '../contexts/InventoryWorkspaceContext';
import { InventoryWorkspace } from '../workspace/InventoryWorkspace';

export const InventoryPreviewWorkspace = memo(function InventoryPreviewWorkspace() {
  return (
    <InventoryWorkspaceProvider>
      <div style={{ padding: 16 }}>
        <InventoryWorkspace />
      </div>
    </InventoryWorkspaceProvider>
  );
});

export default InventoryPreviewWorkspace;

