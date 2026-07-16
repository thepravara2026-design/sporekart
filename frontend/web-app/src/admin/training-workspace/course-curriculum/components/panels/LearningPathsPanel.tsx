import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';

const PATH_LEVELS = [
  { icon: '\uD83C\uDFDB\uFE0F', name: 'Learning Path', children: 'Courses' },
  { icon: '\uD83C\uDFAC', name: 'Course', children: 'Curricula' },
  { icon: '\uD83D\uDCDA', name: 'Curriculum', children: 'Modules' },
  { icon: '\uD83D\uDCC2', name: 'Module', children: 'Lessons' },
  { icon: '\uD83C\uDFAF', name: 'Lesson', children: 'Assessment' },
  { icon: '\uD83C\uDFC5', name: 'Certificate', children: 'Completion' },
];

export function LearningPathsPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Learning Path Architecture</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Architecture-only. Learning paths compose curricula into structured learner journeys ending in certification.
          </span>
        </Stack>
      </Card>
      <Card variant="elevated" padding="lg">
        <Stack gap="sm">
          {PATH_LEVELS.map((level, idx) => (
            <div
              key={level.name}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 12,
                padding: '8px 12px',
                borderLeft: idx < PATH_LEVELS.length - 1 ? '2px solid var(--color-primary-300)' : '2px solid transparent',
                marginLeft: idx * 24,
                background: idx === 0 ? 'var(--color-primary-50)' : 'transparent',
                borderRadius: 'var(--radius-sm)',
              }}
            >
              <span style={{ fontSize: 20, width: 28, textAlign: 'center' }}>{level.icon}</span>
              <span style={{ fontWeight: 500, flex: 1 }}>{level.name}</span>
              <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>Contains: {level.children}</span>
              <Badge variant={idx === PATH_LEVELS.length - 1 ? 'success' : 'default'} size="sm">{idx === PATH_LEVELS.length - 1 ? 'End' : 'Level ' + (idx + 1)}</Badge>
            </div>
          ))}
        </Stack>
      </Card>
    </Stack>
  );
}
