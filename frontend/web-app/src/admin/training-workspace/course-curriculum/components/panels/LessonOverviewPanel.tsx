import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { LESSON_TYPES } from '../../data/curriculumMockData';

export function LessonOverviewPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Lesson Types</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Architecture-only. The Lesson Builder (future) will use these types to structure content delivery.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {LESSON_TYPES.map((type, idx) => (
          <Card key={type} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{type}</span>
                {idx >= LESSON_TYPES.length - 3 ? <Badge variant="warning" size="sm">future</Badge> : <Badge variant="info" size="sm">ready</Badge>}
              </div>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
