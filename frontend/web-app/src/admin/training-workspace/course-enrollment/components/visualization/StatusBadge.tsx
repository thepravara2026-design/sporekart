import { Badge } from '../../../../../design-system/components/display/Badge';
import { REGISTRATION_STATUS_LABELS, STATUS_BADGE_VARIANT, type RegistrationStatus } from '../../data/enrollmentMockData';

export function StatusBadge({ status, size = 'sm' }: { status: RegistrationStatus; size?: 'sm' | 'md' | 'lg' }) {
  return (
    <Badge variant={STATUS_BADGE_VARIANT[status]} size={size}>
      {REGISTRATION_STATUS_LABELS[status]}
    </Badge>
  );
}
