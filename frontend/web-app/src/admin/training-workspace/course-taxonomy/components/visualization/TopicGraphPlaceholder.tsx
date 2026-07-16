import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useTaxonomyContext } from '../../state/TaxonomyContext';

export function TopicGraphPlaceholder() {
  const { state } = useTaxonomyContext();

  return (
    <Card variant="outlined" padding="lg">
      <Stack gap="md">
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Topic Relationship Graph</span>
          <Badge variant="warning" size="sm">Planned</Badge>
        </div>
        <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
          Future interactive graph showing topic dependencies and relationships. Preview of detected relationships:
        </div>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 12 }}>
          {state.topics.slice(0, 6).map((topic) => (
            <div
              key={topic.id}
              style={{
                padding: '8px 12px',
                border: `2px solid ${topic.priority === 'critical' ? 'var(--color-danger-500)' : 'var(--color-primary-300)'}`,
                borderRadius: 'var(--radius-md)',
                background: 'var(--color-bg-secondary)',
                fontSize: 'var(--font-size-sm)',
              }}
            >
              <div style={{ fontWeight: 600 }}>{topic.name}</div>
              {topic.dependsOn.length > 0 && (
                <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                  depends on: {topic.dependsOn.length}
                </div>
              )}
              {topic.relatedTopics.length > 0 && (
                <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                  related: {topic.relatedTopics.length}
                </div>
              )}
            </div>
          ))}
        </div>
      </Stack>
    </Card>
  );
}
