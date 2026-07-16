import { useState } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Button } from '../../../../../design-system/components/core/Button';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { useTaxonomyContext } from '../../state/TaxonomyContext';

export function TopicsPanel() {
  const { state, addTopic, removeTopic } = useTaxonomyContext();
  const [newName, setNewName] = useState('');
  const [newGroup, setNewGroup] = useState('Foundations');
  const [newPriority, setNewPriority] = useState<string>('medium');

  const groups = Array.from(new Set(state.topics.map((t) => t.group)));

  const handleAdd = () => {
    if (!newName.trim()) return;
    addTopic({
      id: `topic-${Date.now()}`,
      name: newName.trim(),
      group: newGroup,
      description: '',
      priority: newPriority as any,
      order: state.topics.length + 1,
      dependsOn: [],
      relatedTopics: [],
    });
    setNewName('');
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Add Topic</span>
          <Grid columns={3} gap="md">
            <Input label="Topic Name" value={newName} onChange={(e) => setNewName(e.target.value)} placeholder="e.g. Composting" fullWidth />
            <Select
              label="Group"
              options={groups.map((g) => ({ value: g, label: g }))}
              value={newGroup}
              onChange={(v) => setNewGroup(v)}
            />
            <Select
              label="Priority"
              options={[
                { value: 'low', label: 'Low' },
                { value: 'medium', label: 'Medium' },
                { value: 'high', label: 'High' },
                { value: 'critical', label: 'Critical' },
              ]}
              value={newPriority}
              onChange={(v) => setNewPriority(v)}
            />
          </Grid>
          <Button variant="primary" size="sm" onClick={handleAdd} disabled={!newName.trim()}>Add Topic</Button>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Topic Library ({state.topics.length})</span>
          <Grid columns={2} gap="md">
            {state.topics.map((topic) => (
              <Card key={topic.id} variant="outlined" padding="sm">
                <Inline gap="sm" align="center">
                  <div style={{ flex: 1 }}>
                    <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500, display: 'flex', alignItems: 'center', gap: 6 }}>
                      {topic.name}
                      <Badge
                        variant={topic.priority === 'critical' ? 'danger' : topic.priority === 'high' ? 'warning' : 'default'}
                        size="sm"
                      >
                        {topic.priority}
                      </Badge>
                    </div>
                    <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>
                      {topic.group} &middot; {topic.dependsOn.length} deps &middot; {topic.relatedTopics.length} related
                    </div>
                  </div>
                  <button
                    onClick={() => removeTopic(topic.id)}
                    aria-label={`Remove ${topic.name}`}
                    style={{ border: 'none', background: 'none', cursor: 'pointer', color: 'var(--color-danger-500)', fontSize: 14, padding: 4 }}
                  >
                    \u2716
                  </button>
                </Inline>
              </Card>
            ))}
          </Grid>
        </Stack>
      </Card>
    </Stack>
  );
}
