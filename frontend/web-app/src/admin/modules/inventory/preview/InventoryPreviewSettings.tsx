import { memo } from 'react';
import { InventoryWorkspaceProvider } from '../contexts/InventoryWorkspaceContext';
import { InventorySettings } from '../settings/InventorySettings';

export const InventoryPreviewSettings = memo(function InventoryPreviewSettings() {
  return (
    <InventoryWorkspaceProvider>
      <div style={{ padding: 16 }}>
        <InventorySettings />
      </div>
    </InventoryWorkspaceProvider>
  );
});

export default InventoryPreviewSettings;

