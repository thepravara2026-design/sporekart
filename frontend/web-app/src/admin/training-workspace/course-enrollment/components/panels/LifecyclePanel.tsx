import { Card } from '../../../../../design-system/components/composite/Card';
import { StatusBadge } from '../visualization/StatusBadge';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import {
  REGISTRATION_LIFECYCLE,
  REGISTRATION_STATUS_LABELS,
  type RegistrationStatus,
} from '../../data/enrollmentMockData';

const STAGE_DESCRIPTIONS: Record<RegistrationStatus, string> = {
  'not-open': 'Registration not yet opened for the course.',
  open: 'Registration window is live; applicants may apply.',
  pending: 'Application submitted, awaiting review.',
  approved: 'Application approved by governing policy.',
  confirmed: 'Seat confirmed and allocated to applicant.',
  waitlisted: 'No seat available; queued on waitlist.',
  rejected: 'Application declined per eligibility/policy.',
  cancelled: 'Enrollment cancelled by applicant or admin.',
  completed: 'Training completed successfully.',
  expired: 'Offer/seat expired without confirmation.',
  archived: 'Record archived for historical reference.',
};

export function LifecyclePanel() {
  const { state } = useEnrollmentContext();

  const counts = REGISTRATION_LIFECYCLE.reduce<Record<string, number>>((acc, s) => {
    acc[s] = state.requests.filter((r) => r.status === s).length;
    return acc;
  }, {});

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Registration Lifecycle</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        The canonical enrollment state machine. Requests transition through these stages under policy governance.
      </p>

      <ol style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {REGISTRATION_LIFECYCLE.map((stage, i) => (
          <li key={stage}>
            <Card variant="outlined" padding="sm">
              <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                <span style={{ width: 26, height: 26, borderRadius: '50%', background: 'var(--color-primary-100)', color: 'var(--color-primary-700)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 12, fontWeight: 700 }}>{i + 1}</span>
                <div style={{ width: 110 }}><StatusBadge status={stage} size="md" /></div>
                <span style={{ flex: 1, fontSize: 12, color: 'var(--color-text-tertiary)' }}>{STAGE_DESCRIPTIONS[stage]}</span>
                <span style={{ fontSize: 12, fontWeight: 600 }}>{counts[stage]} {counts[stage] === 1 ? 'request' : 'requests'}</span>
              </div>
            </Card>
          </li>
        ))}
      </ol>

      <p style={{ marginTop: 'var(--space-3)', fontSize: 12, color: 'var(--color-text-tertiary)' }}>
        Stage labels: {REGISTRATION_LIFECYCLE.map((s) => REGISTRATION_STATUS_LABELS[s]).join(' \u2192 ')}
      </p>
    </div>
  );
}
