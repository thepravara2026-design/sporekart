import { useMemo } from 'react';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { CommerceToolbar } from '../shared/CommerceToolbar';
import { EnrollmentTable } from '../visualization/EnrollmentTable';

export function EnrollmentPanel() {
  const { state, setRequestStatus } = useEnrollmentContext();

  const filtered = useMemo(() => {
    const q = state.search.toLowerCase();
    return state.requests.filter((r) => {
      if (state.filters.pricingType !== 'all' && r.pricingModel !== state.filters.pricingType) return false;
      if (state.filters.registrationStatus !== 'all' && r.status !== state.filters.registrationStatus) return false;
      if (q && !`${r.applicantName} ${r.courseName}`.toLowerCase().includes(q)) return false;
      return true;
    });
  }, [state.requests, state.search, state.filters]);

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3)', flexWrap: 'wrap', gap: 8 }}>
        <h2 style={{ margin: 0 }}>Enrollment Dashboard</h2>
        <Badge variant="info" size="md">{filtered.length} requests</Badge>
      </div>

      <CommerceToolbar showAvailability={false} />
      <EnrollmentTable requests={filtered} onStatusChange={setRequestStatus} />
    </div>
  );
}
