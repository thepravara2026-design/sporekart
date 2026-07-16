import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { useEnrollmentContext } from '../../state/EnrollmentContext';

const PRIORITY_VARIANT: Record<string, 'danger' | 'warning' | 'neutral'> = {
  high: 'danger',
  medium: 'warning',
  low: 'neutral',
};

export function WaitlistPanel() {
  const { state, promoteWaitlist } = useEnrollmentContext();

  const grouped = state.waitlist.reduce<Record<string, typeof state.waitlist>>((acc, entry) => {
    (acc[entry.courseName] ||= []).push(entry);
    return acc;
  }, {});

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3)', flexWrap: 'wrap', gap: 8 }}>
        <h2 style={{ margin: 0 }}>Waitlist Management</h2>
        <Badge variant="info" size="md">{state.waitlist.length} queued</Badge>
      </div>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        Waiting queue architecture with priority, expiration and automatic/manual promotion. Promotion is mock-only.
      </p>

      {Object.keys(grouped).length === 0 && (
        <div style={{ padding: 'var(--space-6)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>Waitlist is empty.</div>
      )}

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
        {Object.entries(grouped).map(([courseName, entries]) => (
          <Card key={courseName} variant="outlined" padding="md">
            <h3 style={{ marginTop: 0, marginBottom: 'var(--space-2)' }}>{courseName}</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
              {entries.sort((a, b) => a.position - b.position).map((e) => (
                <div key={e.id} style={{ display: 'flex', alignItems: 'center', gap: 12, flexWrap: 'wrap' }}>
                  <span style={{ width: 26, height: 26, borderRadius: '50%', background: 'var(--color-bg-secondary)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 12, fontWeight: 700 }}>#{e.position}</span>
                  <span style={{ flex: 1, minWidth: 140, fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{e.applicantName}</span>
                  <Badge variant={PRIORITY_VARIANT[e.priority]} size="sm">{e.priority}</Badge>
                  <Badge variant="neutral" size="sm">{e.promotionMode}</Badge>
                  <span style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>Expires {e.expiresDate}</span>
                  <Button size="sm" variant="outline" onClick={() => promoteWaitlist(e.id)} aria-label={`Promote ${e.applicantName}`}>Promote</Button>
                </div>
              ))}
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}
