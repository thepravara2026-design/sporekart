import { memo } from 'react';
import { WorkspaceProvider } from './state/WorkspaceContext';
import TrainingWorkspaceLayout from './TrainingWorkspaceLayout';

const TrainingWorkspaceRoute = memo(function TrainingWorkspaceRoute() {
  return (
    <WorkspaceProvider>
      <TrainingWorkspaceLayout />
    </WorkspaceProvider>
  );
});

export default TrainingWorkspaceRoute;
