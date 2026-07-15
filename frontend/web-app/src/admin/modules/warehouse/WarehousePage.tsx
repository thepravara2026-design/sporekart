import { memo } from 'react';
import { WarehouseWorkspaceProvider } from './contexts/WarehouseWorkspaceContext';
import { WarehouseWorkspaceLayout } from './layouts/WarehouseWorkspaceLayout';

export const WarehousePage = memo(function WarehousePage() {
  return (
    <WarehouseWorkspaceProvider initialRole="administrator">
      <WarehouseWorkspaceLayout />
    </WarehouseWorkspaceProvider>
  );
});

