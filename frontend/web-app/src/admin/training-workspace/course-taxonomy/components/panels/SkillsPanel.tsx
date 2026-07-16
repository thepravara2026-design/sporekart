import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { MOCK_SKILLS } from '../../data/taxonomyMockData';

export function SkillsPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Skill Classification</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Ordered skill levels from Beginner to Consultant. Used for course difficulty mapping and learner progression.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {MOCK_SKILLS.map((skill, idx) => (
          <Card key={skill.id} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 24, fontWeight: 700, color: 'var(--color-primary-500)' }}>{skill.rank}</span>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{skill.label}</span>
                {idx < MOCK_SKILLS.length - 3 && <Badge variant="info" size="sm">standard</Badge>}
                {idx >= MOCK_SKILLS.length - 3 && <Badge variant="warning" size="sm">future</Badge>}
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{skill.description}</div>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
