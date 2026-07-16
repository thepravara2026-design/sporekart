import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';

const LEVELS = [
  { icon: '\uD83D\uDCDA', name: 'Course', children: 'Modules', badge: 'Top Level' },
  { icon: '\uD83D\uDCC2', name: 'Modules', children: 'Lessons', badge: 'Future' },
  { icon: '\uD7A3', name: 'Lessons', children: 'Learning Activities', badge: 'Future' },
  { icon: '\uD83C\uDFAF', name: 'Learning Activities', children: 'Resources', badge: 'Future' },
  { icon: '\uD83D\uDCC1', name: 'Resources', children: 'Assessments', badge: 'Future' },
  { icon: '\u2705', name: 'Assessment', children: 'Completion', badge: 'Future' },
  { icon: '\uD83C\uDFC1', name: 'Completion', children: 'Certificate', badge: 'Future' },
];

export function CurriculumStructurePanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Curriculum Structure</div>
          <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            This panel defines the architectural hierarchy for the course curriculum. Full builder implementation will be available in a future update.
          </div>
        </Stack>
      </Card>

      <Card variant="elevated" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-md)', fontWeight: 600 }}>Course Hierarchy (Architecture)</div>

          <Stack gap="sm">
            {LEVELS.map((level, index) => (
              <div
                key={level.name}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 12,
                  padding: '8px 12px',
                  borderLeft: index < LEVELS.length - 1 ? '2px solid var(--color-primary-300)' : '2px solid transparent',
                  marginLeft: index * 24,
                  background: index === 0 ? 'var(--color-primary-50)' : 'transparent',
                  borderRadius: 'var(--radius-sm)',
                }}
              >
                <span style={{ fontSize: 20, width: 28, textAlign: 'center' }}>{level.icon}</span>
                <span style={{ fontWeight: 500, flex: 1 }}>{level.name}</span>
                <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                  Contains: {level.children}
                </span>
                {level.badge === 'Future' ? (
                  <Badge variant="warning" size="sm">Planned</Badge>
                ) : (
                  <Badge variant="success" size="sm">{level.badge}</Badge>
                )}
              </div>
            ))}
          </Stack>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-md)', fontWeight: 600 }}>Future Curriculum Builder</div>
          <Grid columns={3} gap="md">
            {['Module Builder', 'Lesson Builder', 'Activity Builder', 'Assessment Engine', 'Certificate Engine', 'Progress Tracking'].map((feature) => (
              <Card key={feature} variant="ghost" padding="md">
                <Stack gap="xs">
                  <Badge variant="warning" size="sm">Planned</Badge>
                  <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>{feature}</div>
                  <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                    Will extend the course builder with structured content authoring.
                  </div>
                </Stack>
              </Card>
            ))}
          </Grid>
        </Stack>
      </Card>
    </Stack>
  );
}
