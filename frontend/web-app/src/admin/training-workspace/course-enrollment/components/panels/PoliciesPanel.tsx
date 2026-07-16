import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { ENROLLMENT_POLICY_LABELS, ELIGIBILITY_LABELS, type EnrollmentPolicyType } from '../../data/enrollmentMockData';

const POLICY_DESCRIPTIONS: Record<EnrollmentPolicyType, string> = {
  open: 'Anyone may self-enroll while seats and window are open.',
  'admin-approval': 'Requests require training admin review before confirmation.',
  invitation: 'Enrollment restricted to invited applicants only.',
  'corporate-approval': 'Corporate sponsor must approve applicant enrollment.',
  'institution-approval': 'Partner institution approves applicant enrollment.',
};

export function PoliciesPanel() {
  const { state } = useEnrollmentContext();

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Enrollment Policies</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        Governance rules controlling how applicants may enroll. Each course binds one policy with an enrollment window and deadline.
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-3)', marginBottom: 'var(--space-5)' }}>
        {(Object.keys(ENROLLMENT_POLICY_LABELS) as EnrollmentPolicyType[]).map((p) => (
          <Card key={p} variant="outlined" padding="md">
            <div style={{ fontWeight: 700 }}>{ENROLLMENT_POLICY_LABELS[p]}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)', marginTop: 4 }}>{POLICY_DESCRIPTIONS[p]}</div>
          </Card>
        ))}
      </div>

      <h3>Course Policy Bindings</h3>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {state.courses.map((c) => (
          <Card key={c.id} variant="outlined" padding="sm">
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, flexWrap: 'wrap' }}>
              <span style={{ flex: 1, minWidth: 160, fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{c.courseName}</span>
              <Badge variant="primary" size="sm">{ENROLLMENT_POLICY_LABELS[c.policy.type]}</Badge>
              <span style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>Window: {c.policy.enrollmentWindowStart} → {c.policy.enrollmentWindowEnd}</span>
              <span style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>Deadline: {c.policy.registrationDeadline}</span>
              {c.policy.prerequisiteValidation && <Badge variant="warning" size="sm">Prereq check</Badge>}
              <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
                {c.policy.eligibilityRules.map((r) => (
                  <Badge key={r} variant="neutral" size="sm">{ELIGIBILITY_LABELS[r as keyof typeof ELIGIBILITY_LABELS] ?? r}</Badge>
                ))}
              </div>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}
