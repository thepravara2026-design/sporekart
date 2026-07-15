import { memo } from 'react';
import { IntelligenceWorkspaceProvider } from './contexts/IntelligenceWorkspaceContext';
import { IntelligenceLayout } from './components/IntelligenceLayout';
import { IntelligenceDashboard } from './components/IntelligenceDashboard';

export const IntelligenceWorkspace = memo(function IntelligenceWorkspace() {
  return (
    <IntelligenceWorkspaceProvider>
      <IntelligenceLayout>
        <IntelligenceDashboard />
      </IntelligenceLayout>
    </IntelligenceWorkspaceProvider>
  );
});
