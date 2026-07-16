import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';

const RULES = [
  { key: 'mandatory', label: 'Mandatory Lessons', desc: 'Required lessons for completion' },
  { key: 'optional', label: 'Optional Lessons', desc: 'Elective lessons' },
  { key: 'completion-pct', label: 'Completion %', desc: 'Threshold percentage required' },
  { key: 'min-score', label: 'Minimum Score', desc: 'Assessment passing score' },
  { key: 'practical', label: 'Practical Completion', desc: 'Hands-on task sign-off' },
  { key: 'attendance', label: 'Attendance', desc: 'Session attendance requirement' },
  { key: 'cert-eligibility', label: 'Certificate Eligibility', desc: 'Criteria for certificate issue' },
];

export function CompletionRulesPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Completion Rules</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Architecture-only. These rule hooks drive future certificate and progression logic.
          </span>
        </Stack>
      </Card>
      <Grid columns={2} gap="md">
        {RULES.map((rule) => (
          <Card key={rule.key} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{rule.label}</span>
                <Badge variant="warning" size="sm">planned</Badge>
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{rule.desc}</div>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
