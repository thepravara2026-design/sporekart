import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';

const PATH_LEVELS = [
  { icon: '\uD83C\uDFDB\uFE0F', name: 'Learning Path', children: 'Courses', badge: 'Top Level' },
  { icon: '\uD83C\uDFAC', name: 'Course', children: 'Modules', badge: 'From Registry' },
  { icon: '\uD83D\uDCC2', name: 'Module', children: 'Lessons', badge: 'Future' },
  { icon: '\uD83C\uDFAF', name: 'Lesson', children: 'Assessment', badge: 'Future' },
  { icon: '\uD83D\uDCCA', name: 'Assessment', children: 'Certificate', badge: 'Future' },
  { icon: '\uD83C\uDFC5', name: 'Certificate', children: 'Completion', badge: 'Future' },
];

export function LearningPathsPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Learning Path Architecture</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Architecture-only. Learning paths compose categories, courses and competencies into structured learner journeys.
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
              {level.badge === 'Future' ? <Badge variant="warning" size="sm">Planned</Badge> : <Badge variant="success" size="sm">{level.badge}</Badge>}
            </div>
          ))}
        </Stack>
      </Card>
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-md)', fontWeight: 600 }}>Future Learning Path Engine</span>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
            {['Path Builder', 'Prerequisite Engine', 'Competency Mapping', 'Progress Tracking', 'Certificate Issuance', 'AI Path Suggestion'].map((f) => (
              <Badge key={f} variant="warning" size="sm">{f}</Badge>
            ))}
          </div>
        </Stack>
      </Card>
    </Stack>
  );
}
