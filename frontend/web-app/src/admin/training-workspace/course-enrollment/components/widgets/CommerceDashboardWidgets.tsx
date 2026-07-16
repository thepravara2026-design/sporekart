import { useMemo } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { formatCurrency } from '../../data/enrollmentMockData';

function StatCard({ label, value, hint, accent }: { label: string; value: string | number; hint?: string; accent?: string }) {
  return (
    <Card variant="elevated" padding="md">
      <div style={{ fontSize: 11, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>{label}</div>
      <div style={{ fontSize: 26, fontWeight: 800, color: accent }}>{value}</div>
      {hint && <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{hint}</div>}
    </Card>
  );
}

export function CommerceDashboardWidgets() {
  const { state } = useEnrollmentContext();

  const stats = useMemo(() => {
    const courses = state.courses;
    const paid = courses.filter((c) => c.pricing.baseFee > 0).length;
    const free = courses.filter((c) => c.pricing.baseFee === 0).length;
    const availableSeats = courses.reduce((s, c) => s + c.capacity.availableSeats, 0);
    const requests = courses.reduce((s, c) => s + c.enrollmentRequests, 0);
    const approved = courses.reduce((s, c) => s + c.approvedCount, 0);
    const pending = courses.reduce((s, c) => s + c.pendingCount, 0);
    const waitlisted = courses.reduce((s, c) => s + c.waitlistedCount, 0);
    const cancelled = courses.reduce((s, c) => s + c.cancelledCount, 0);
    const revenue = courses.reduce((s, c) => s + c.revenuePlaceholder, 0);
    const popular = [...courses].sort((a, b) => b.popularity - a.popularity).slice(0, 5);
    const upcoming = courses.filter((c) => c.registrationStatus === 'open' || c.registrationStatus === 'not-open').slice(0, 5);
    return { paid, free, availableSeats, requests, approved, pending, waitlisted, cancelled, revenue, popular, upcoming };
  }, [state.courses]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: 'var(--space-3)' }}>
        <StatCard label="Paid Courses" value={stats.paid} hint="Revenue-generating" />
        <StatCard label="Free Courses" value={stats.free} hint="No fee" />
        <StatCard label="Available Seats" value={stats.availableSeats} hint="Across catalog" />
        <StatCard label="Enrollment Requests" value={stats.requests} hint="Total received" />
        <StatCard label="Approved" value={stats.approved} accent="var(--color-success)" />
        <StatCard label="Pending" value={stats.pending} accent="var(--color-warning)" />
        <StatCard label="Waitlisted" value={stats.waitlisted} accent="var(--color-warning)" />
        <StatCard label="Cancelled" value={stats.cancelled} accent="var(--color-danger)" />
        <StatCard label="Revenue (placeholder)" value={formatCurrency(stats.revenue)} hint="Mock — no real transactions" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 'var(--space-4)' }}>
        <Card variant="outlined" padding="md">
          <h3 style={{ marginTop: 0 }}>Popular Courses</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {stats.popular.map((c, i) => (
              <div key={c.id} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <Badge variant="primary" size="sm">{i + 1}</Badge>
                <span style={{ flex: 1, fontSize: 'var(--font-size-sm)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{c.courseName}</span>
                <Badge variant="info" size="sm">{c.popularity}%</Badge>
              </div>
            ))}
          </div>
        </Card>

        <Card variant="outlined" padding="md">
          <h3 style={{ marginTop: 0 }}>Upcoming Registration</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {stats.upcoming.length === 0 && <span style={{ color: 'var(--color-text-tertiary)', fontSize: 12 }}>None scheduled.</span>}
            {stats.upcoming.map((c) => (
              <div key={c.id} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ flex: 1, fontSize: 'var(--font-size-sm)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{c.courseName}</span>
                <span style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{c.policy.registrationDeadline}</span>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
}
