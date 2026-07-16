import { memo } from 'react';
import { EmptyState } from '../../../../design-system/components/display/EmptyState';

interface StudentEmptyStateProps {
  type: 'noStudents' | 'noSearchResults' | 'noActive' | 'noPending' | 'noArchived' | 'noFiltersApplied';
  onClearFilters?: () => void;
  onAddStudent?: () => void;
}

const StudentEmptyState = memo(function StudentEmptyState({
  type,
  onClearFilters,
  onAddStudent,
}: StudentEmptyStateProps) {
  switch (type) {
    case 'noStudents':
      return (
        <EmptyState
          type="noData"
          title="No students registered"
          description="The student registry is empty. Add your first student to get started."
          action={onAddStudent ? { label: 'Add Student', onClick: onAddStudent } : undefined}
        />
      );
    case 'noSearchResults':
      return (
        <EmptyState
          type="searchEmpty"
          title="No students match your search"
          description="Try adjusting your search terms or filters."
          action={onClearFilters ? { label: 'Clear Filters', onClick: onClearFilters } : undefined}
        />
      );
    case 'noActive':
      return (
        <EmptyState
          type="filterEmpty"
          title="No active students"
          description="There are currently no active students in the registry."
          action={onClearFilters ? { label: 'View All Students', onClick: onClearFilters } : undefined}
        />
      );
    case 'noPending':
      return (
        <EmptyState
          type="filterEmpty"
          title="No pending approvals"
          description="All student applications have been reviewed."
        />
      );
    case 'noArchived':
      return (
        <EmptyState
          type="noData"
          title="No archived students"
          description="The archive is empty. Students will appear here when archived."
        />
      );
    case 'noFiltersApplied':
      return (
        <EmptyState
          type="filterEmpty"
          title="No filters applied"
          description="Showing all students. Use the filters above to narrow down results."
        />
      );
  }
});

export default StudentEmptyState;
