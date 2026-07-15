import { memo } from 'react';
import { BatchWorkspaceLayout } from '../layouts/BatchWorkspaceLayout';
import { BatchDashboard } from '../dashboard/BatchDashboard';

export const BatchWorkspace = memo(function BatchWorkspace() {
  return (
    <BatchWorkspaceLayout>
      <BatchDashboard />
    </BatchWorkspaceLayout>
  );
});
