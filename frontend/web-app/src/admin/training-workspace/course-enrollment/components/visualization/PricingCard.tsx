import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { CapacityMeter } from './CapacityMeter';
import { StatusBadge } from './StatusBadge';
import {
  PRICING_MODEL_LABELS,
  formatCurrency,
  type CourseCommerce,
} from '../../data/enrollmentMockData';

interface PricingCardProps {
  course: CourseCommerce;
  onSelect?: (id: string) => void;
}

function PriceLine({ label, value }: { label: string; value: string }) {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 12 }}>
      <span style={{ color: 'var(--color-text-tertiary)' }}>{label}</span>
      <span style={{ fontWeight: 500 }}>{value}</span>
    </div>
  );
}

export function PricingCard({ course, onSelect }: PricingCardProps) {
  const p = course.pricing;
  const isFree = p.baseFee === 0;

  return (
    <Card variant="elevated" padding="md" hoverable onClick={() => onSelect?.(course.id)} style={{ cursor: onSelect ? 'pointer' : 'default' }}>
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 8 }}>
        <div style={{ minWidth: 0 }}>
          <div style={{ fontWeight: 700, fontSize: 'var(--font-size-sm)' }}>{course.courseName}</div>
          <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{course.code} · {course.category}</div>
        </div>
        <Badge variant={isFree ? 'success' : 'primary'} size="sm">{PRICING_MODEL_LABELS[p.model]}</Badge>
      </div>

      <div style={{ fontSize: 26, fontWeight: 800, margin: '10px 0 4px' }}>{formatCurrency(p.baseFee, p.currency)}</div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 3, marginBottom: 10 }}>
        <PriceLine label="Enrollment Fee" value={formatCurrency(p.enrollmentFee, p.currency)} />
        <PriceLine label="Registration Fee" value={formatCurrency(p.registrationFee, p.currency)} />
        <PriceLine label="GST (placeholder)" value={`${p.gstPlaceholder}%`} />
        {p.earlyBirdPlaceholder > 0 && <PriceLine label="Early Bird" value={`- ${formatCurrency(p.earlyBirdPlaceholder, p.currency)}`} />}
      </div>

      <CapacityMeter capacity={course.capacity} showLegend={false} />
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 8 }}>
        <span style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{course.capacity.availableSeats} of {course.capacity.maxSeats} seats</span>
        <StatusBadge status={course.registrationStatus} />
      </div>
    </Card>
  );
}
