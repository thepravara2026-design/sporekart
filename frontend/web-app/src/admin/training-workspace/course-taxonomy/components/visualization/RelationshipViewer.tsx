import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { useTaxonomyContext } from '../../state/TaxonomyContext';

export function RelationshipViewer() {
  const { state, setSelectedTopic } = useTaxonomyContext();

  return (
    <Card variant="outlined" padding="lg">
      <Stack gap="md">
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Topic Relationships</span>
          <Badge variant="info" size="sm">{state.topics.length} topics</Badge>
        </div>
        <Grid columns={2} gap="md">
          {state.topics.map((topic) => (
            <Card
              key={topic.id}
              variant="ghost"
              padding="md"
              hoverable
              onClick={() => setSelectedTopic(topic.id)}
              aria-label={`View ${topic.name}`}
            >
              <Stack gap="xs">
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <span style={{ fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{topic.name}</span>
                  <Badge
                    variant={topic.priority === 'critical' ? 'danger' : topic.priority === 'high' ? 'warning' : 'default'}
                    size="sm"
                  >
                    {topic.priority}
                  </Badge>
                </div>
                <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{topic.group}</div>
                {topic.dependsOn.length > 0 && (
                  <div style={{ fontSize: 'var(--font-size-xs)' }}>
                    <span style={{ color: 'var(--color-text-tertiary)' }}>Depends on: </span>
                    {topic.dependsOn.map((d) => {
                      const dep = state.topics.find((t) => t.id === d);
                      return <span key={d} style={{ marginRight: 6 }}>{dep?.name || d}</span>;
                    })}
                  </div>
                )}
                {topic.relatedTopics.length > 0 && (
                  <div style={{ fontSize: 'var(--font-size-xs)' }}>
                    <span style={{ color: 'var(--color-text-tertiary)' }}>Related: </span>
                    {topic.relatedTopics.map((r) => {
                      const rel = state.topics.find((t) => t.id === r);
                      return <span key={r} style={{ marginRight: 6 }}>{rel?.name || r}</span>;
                    })}
                  </div>
                )}
              </Stack>
            </Card>
          ))}
        </Grid>
      </Stack>
    </Card>
  );
}
