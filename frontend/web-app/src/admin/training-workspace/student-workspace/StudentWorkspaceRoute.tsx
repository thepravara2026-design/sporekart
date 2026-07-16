import { memo } from 'react';
import { StudentWorkspaceProvider } from './state/WorkspaceContext';
import StudentWorkspaceLayout from './StudentWorkspaceLayout';

const StudentWorkspaceRoute = memo(function StudentWorkspaceRoute() {
  return (
    <StudentWorkspaceProvider>
      <StudentWorkspaceLayout />
    </StudentWorkspaceProvider>
  );
});

export default StudentWorkspaceRoute;
