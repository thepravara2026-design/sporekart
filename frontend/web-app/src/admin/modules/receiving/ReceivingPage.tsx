import { memo } from 'react';
import { ReceivingWorkspaceProvider } from './contexts/ReceivingWorkspaceContext';
import { ReceivingWorkspace } from './workspace/ReceivingWorkspace';

export const ReceivingPage = memo(function ReceivingPage() {
  return (
    <ReceivingWorkspaceProvider>
      <ReceivingWorkspace />
    </ReceivingWorkspaceProvider>
  );
});
