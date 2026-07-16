import { memo } from 'react';
import type { StudentStatus } from '../types';
import { STUDENT_STATUS_LABELS, STUDENT_STATUS_VARIANTS } from '../types';
import { Badge } from '../../../../design-system/components/display/Badge';

interface StudentStatusBadgeProps {
  status: StudentStatus;
  size?: 'sm' | 'md' | 'lg';
}

const StudentStatusBadge = memo(function StudentStatusBadge({ status, size = 'sm' }: StudentStatusBadgeProps) {
  return (
    <Badge variant={STUDENT_STATUS_VARIANTS[status]} size={size}>
      {STUDENT_STATUS_LABELS[status]}
    </Badge>
  );
});

export default StudentStatusBadge;
