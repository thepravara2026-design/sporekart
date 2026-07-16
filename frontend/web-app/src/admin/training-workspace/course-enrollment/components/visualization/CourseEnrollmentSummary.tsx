import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { CapacityMeter } from './CapacityMeter';
import { StatusBadge } from './StatusBadge';
import {
  PRICING_MODEL_LABELS,
  formatCurrency,
  type CourseCommerce,
} from '../../data/enrollmentMockData';

interface CourseEnrollmentSummaryProps {
  course: CourseCommerce;
  onClose?: () => void;
}

function SummaryCard({ label, value, hint }: { label: string; value: React.ReactNode; hint?: string }) {
  return (
    <Card variant="outlined" padding="sm">
      <div style={{ fontSize: 11, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>{label}</div>
      <div style={{ fontSize: 'var(--font-size-md)', fontWeight: 700, marginTop: 2 }}>{value}</div>
      {hint && <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{hint}</div>}
    </Card>
  );
}

export function CourseEnrollmentSummary({ course, onClose }: CourseEnrollmentSummaryProps) {
  const p = course.pricing;
  const c = course.capacity;

  return (
    <Card variant="elevated" padding="md" aria-label={`Enrollment summary for ${course.courseName}`}>
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 8, marginBottom: 'var(--space-3)', flexWrap: 'wrap' }}>
        <div>
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>{course.courseName}</div>
          <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{course.code} · {course.category}</div>
        </div>
        <div style={{ display: 'flex', gap: 6, alignItems: 'center' }}>
          <Badge variant="primary" size="sm">{PRICING_MODEL_LABELS[p.model]}</Badge>
          <StatusBadge status={course.registrationStatus} size="md" />
          {onClose && <Button size="sm" variant="ghost" onClick={onClose} aria-label="Close summary">Close</Button>}
        </div>
      </div>

      <div style={{ marginBottom: 'var(--space-3)' }}>
        <CapacityMeter capacity={c} />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: 'var(--space-2)' }}>
        <SummaryCard label="Course Capacity" value={`${c.maxSeats} seats`} hint={`min ${c.minSeats}`} />
        <SummaryCard label="Current Enrollment" value={c.occupiedSeats} hint={`${c.reservedSeats} reserved`} />
        <SummaryCard label="Remaining Seats" value={c.availableSeats} hint={c.availableSeats === 0 ? 'Full' : 'Available'} />
        <SummaryCard label="Enrollment Deadline" value={course.policy.registrationDeadline} />
        <SummaryCard label="Registration Status" value={<StatusBadge status={course.registrationStatus} size="md" />} />
        <SummaryCard label="Delivery Mode" value={course.deliveryMode} />
        <SummaryCard label="Training Duration" value={`${course.durationWeeks} weeks`} />
        <SummaryCard label="Price Summary" value={formatCurrency(p.baseFee, p.currency)} hint={`+ ${formatCurrency(p.enrollmentFee + p.registrationFee, p.currency)} fees · GST ${p.gstPlaceholder}%`} />
      </div>
    </Card>
  );
}
