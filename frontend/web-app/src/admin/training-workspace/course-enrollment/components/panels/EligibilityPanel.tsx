import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import { ELIGIBILITY_LABELS } from '../../data/enrollmentMockData';

const RESULT_VARIANT: Record<string, 'success' | 'danger' | 'warning'> = {
  pass: 'success',
  fail: 'danger',
  warning: 'warning',
};

export function EligibilityPanel() {
  const { state } = useEnrollmentContext();
  const rules = state.eligibilityRules;
  const passed = rules.filter((r) => r.mockResult === 'pass').length;

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3)', flexWrap: 'wrap', gap: 8 }}>
        <h2 style={{ margin: 0 }}>Eligibility Rules Engine</h2>
        <Badge variant="info" size="md">{passed}/{rules.length} passing (mock)</Badge>
      </div>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        Reusable, criterion-based eligibility rules. Validation results shown here are mock evaluations only.
      </p>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {rules.map((rule) => (
          <Card key={rule.id} variant="outlined" padding="sm">
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, flexWrap: 'wrap' }}>
              <Badge variant="neutral" size="sm">{ELIGIBILITY_LABELS[rule.criterion]}</Badge>
              <span style={{ flex: 1, minWidth: 160, fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{rule.label}</span>
              <code style={{ fontSize: 12, color: 'var(--color-text-tertiary)', background: 'var(--color-bg-secondary)', padding: '2px 6px', borderRadius: 4 }}>{rule.condition}</code>
              {rule.required ? <Badge variant="primary" size="sm">Required</Badge> : <Badge variant="neutral" size="sm">Optional</Badge>}
              <Badge variant={RESULT_VARIANT[rule.mockResult]} size="sm">{rule.mockResult}</Badge>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}
