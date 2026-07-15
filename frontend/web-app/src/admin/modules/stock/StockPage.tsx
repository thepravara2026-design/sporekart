import { memo } from 'react';
import { StockWorkspaceProvider } from './contexts/StockWorkspaceContext';
import { StockWorkspaceLayout } from './layouts/StockWorkspaceLayout';

export const StockPage = memo(function StockPage() {
  return (
    <StockWorkspaceProvider initialRole="administrator">
      <StockWorkspaceLayout />
    </StockWorkspaceProvider>
  );
});
