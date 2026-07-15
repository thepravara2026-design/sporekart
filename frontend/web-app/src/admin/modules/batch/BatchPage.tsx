import { memo } from 'react';
import { BatchWorkspaceProvider } from './contexts/BatchWorkspaceContext';
import { BatchWorkspaceLayout } from './layouts/BatchWorkspaceLayout';

export const BatchPage = memo(function BatchPage() {
  return (
    <BatchWorkspaceProvider>
      <BatchWorkspaceLayout />
    </BatchWorkspaceProvider>
  );
});
