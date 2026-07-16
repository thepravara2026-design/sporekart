import { Badge } from '../../../../../design-system/components/display/Badge';
import { Card } from '../../../../../design-system/components/composite/Card';
import Pagination from '../../../../components/navigation/Pagination';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { CommerceToolbar } from '../shared/CommerceToolbar';
import { useFilteredCourses } from '../shared/useFilteredCourses';
import { CapacityMeter } from '../visualization/CapacityMeter';
import { StatusBadge } from '../visualization/StatusBadge';

export function CapacityPanel() {
  const { setPage } = useEnrollmentContext();
  const { paged, total, page, pageSize } = useFilteredCourses();

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3)', flexWrap: 'wrap', gap: 8 }}>
        <h2 style={{ margin: 0 }}>Capacity Dashboard</h2>
        <Badge variant="info" size="md">{total} courses</Badge>
      </div>

      <CommerceToolbar showStatus={false} />

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
        {paged.map((c) => (
          <Card key={c.id} variant="outlined" padding="md">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8, flexWrap: 'wrap', gap: 8 }}>
              <div>
                <div style={{ fontWeight: 700, fontSize: 'var(--font-size-sm)' }}>{c.courseName}</div>
                <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{c.code} · min {c.capacity.minSeats} / max {c.capacity.maxSeats} seats</div>
              </div>
              <div style={{ display: 'flex', gap: 6, alignItems: 'center' }}>
                <StatusBadge status={c.registrationStatus} />
                {c.capacity.availableSeats === 0 && <Badge variant="danger" size="sm">Full</Badge>}
              </div>
            </div>
            <CapacityMeter capacity={c.capacity} />
          </Card>
        ))}
      </div>

      {total > pageSize && (
        <div style={{ marginTop: 'var(--space-4)' }}>
          <Pagination page={page} pageSize={pageSize} total={total} onPageChange={setPage} />
        </div>
      )}
    </div>
  );
}
