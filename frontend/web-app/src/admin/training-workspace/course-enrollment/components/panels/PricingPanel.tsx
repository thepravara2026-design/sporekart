import { Badge } from '../../../../../design-system/components/display/Badge';
import Pagination from '../../../../components/navigation/Pagination';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { CommerceToolbar } from '../shared/CommerceToolbar';
import { useFilteredCourses } from '../shared/useFilteredCourses';
import { PricingCard } from '../visualization/PricingCard';
import { CourseEnrollmentSummary } from '../visualization/CourseEnrollmentSummary';

export function PricingPanel() {
  const { state, setSelectedCourse, setPage } = useEnrollmentContext();
  const { paged, total, page, pageSize } = useFilteredCourses();
  const selectedCourse = state.courses.find((c) => c.id === state.selectedCourseId) || null;

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3)', flexWrap: 'wrap', gap: 8 }}>
        <h2 style={{ margin: 0 }}>Pricing Dashboard</h2>
        <Badge variant="info" size="md">{total} courses</Badge>
      </div>

      {selectedCourse && (
        <div style={{ marginBottom: 'var(--space-4)' }}>
          <CourseEnrollmentSummary course={selectedCourse} onClose={() => setSelectedCourse(null)} />
        </div>
      )}

      <CommerceToolbar />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-3)' }}>
        {paged.map((c) => (
          <PricingCard key={c.id} course={c} onSelect={setSelectedCourse} />
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
