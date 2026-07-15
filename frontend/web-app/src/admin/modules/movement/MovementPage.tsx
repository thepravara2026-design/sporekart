import { memo } from 'react';
import { MovementWorkspaceProvider } from './contexts/MovementWorkspaceContext';
import { MovementWorkspace } from './workspace/MovementWorkspace';

export const MovementPage = memo(function MovementPage() {
  return (
    <MovementWorkspaceProvider>
      <MovementWorkspace />
    </MovementWorkspaceProvider>
  );
});
