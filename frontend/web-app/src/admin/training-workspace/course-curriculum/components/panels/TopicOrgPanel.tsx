import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_ICONS, type CurriculumNode } from '../../data/curriculumMockData';

function collectTopics(nodes: CurriculumNode[]): CurriculumNode[] {
  return nodes.flatMap((n) => (n.type === 'topic' ? [n, ...collectTopics(n.children)] : collectTopics(n.children)));
}

export function TopicOrgPanel() {
  const { state } = useCurriculumContext();
  const topics = collectTopics(state.tree);

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Topic Organization</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Topics within lessons support learning goals, priority, order, and dependency relationships.
          </span>
        </Stack>
      </Card>
      <Grid columns={2} gap="md">
        {topics.length === 0 && (
          <Card variant="outlined" padding="md">
            <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>No topics yet. Add topics under lessons in the builder.</span>
          </Card>
        )}
        {topics.map((topic) => (
          <Card key={topic.id} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 16 }}>{NODE_TYPE_ICONS[topic.type]}</span>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{topic.name}</span>
                <Badge variant="default" size="sm">{topic.durationHours}h</Badge>
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{topic.learningGoal || topic.description}</div>
              {topic.prerequisites && topic.prerequisites.length > 0 && (
                <div style={{ fontSize: 'var(--font-size-xs)' }}>
                  <span style={{ color: 'var(--color-text-tertiary)' }}>Prerequisites: </span>
                  {topic.prerequisites.join(', ')}
                </div>
              )}
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
