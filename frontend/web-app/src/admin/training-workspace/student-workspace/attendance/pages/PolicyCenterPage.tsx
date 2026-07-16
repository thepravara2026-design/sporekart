import { useAttendance } from '../state/AttendanceContext';
import { PolicyCard } from '../components/PolicyCard';
import { EmptyState } from '../components/EmptyStates';

export function PolicyCenterPage() {
  const { policies } = useAttendance();

  if (policies.length === 0) return <EmptyState type="noPolicies" />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Policy Center</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Attendance policies and compliance rules
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(380px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {policies.map((policy) => (
          <PolicyCard key={policy.id} policy={policy} />
        ))}
      </div>
    </div>
  );
}
