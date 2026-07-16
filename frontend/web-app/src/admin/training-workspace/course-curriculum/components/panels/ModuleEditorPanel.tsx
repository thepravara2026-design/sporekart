import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_LABELS, STATUS_OPTIONS, DIFFICULTY_OPTIONS, type CurriculumNode } from '../../data/curriculumMockData';

function findNode(nodes: CurriculumNode[], id: string | null): CurriculumNode | null {
  if (!id) return null;
  for (const n of nodes) {
    if (n.id === id) return n;
    const found = findNode(n.children, id);
    if (found) return found;
  }
  return null;
}

export function ModuleEditorPanel() {
  const { state, updateNode } = useCurriculumContext();
  const node = findNode(state.tree, state.selectedNodeId);

  if (!node) {
    return (
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Module Editor</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Select a node from the builder to edit its details.
          </span>
        </Stack>
      </Card>
    );
  }

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Edit {NODE_TYPE_LABELS[node.type]}</span>
            <Badge variant="default" size="sm">{NODE_TYPE_LABELS[node.type]}</Badge>
          </div>
          <Grid columns={2} gap="md">
            <Input label="Name" value={node.name} onChange={(e) => updateNode(node.id, { name: e.target.value })} fullWidth />
            <Select label="Status" options={STATUS_OPTIONS} value={node.status} onChange={(v) => updateNode(node.id, { status: v as any })} />
          </Grid>
          <div style={{ position: 'relative' }}>
            <label style={{ display: 'block', fontSize: 'var(--font-size-sm)', fontWeight: 500, marginBottom: 4 }}>Description</label>
            <textarea
              value={node.description}
              onChange={(e) => updateNode(node.id, { description: e.target.value })}
              rows={3}
              style={{ width: '100%', padding: '8px 12px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', fontSize: 'var(--font-size-sm)', fontFamily: 'inherit', resize: 'vertical' }}
              aria-label="Description"
            />
          </div>
          <Grid columns={2} gap="md">
            <Input label="Learning Goal" value={node.learningGoal || ''} onChange={(e) => updateNode(node.id, { learningGoal: e.target.value })} fullWidth />
            <Input label="Duration (hours)" type="number" value={node.durationHours.toString()} onChange={(e) => updateNode(node.id, { durationHours: parseFloat(e.target.value) || 0 })} fullWidth />
          </Grid>
          <Grid columns={2} gap="md">
            <Select
              label="Difficulty"
              options={DIFFICULTY_OPTIONS}
              value={node.difficulty || 'beginner'}
              onChange={(v) => updateNode(node.id, { difficulty: v })}
            />
            {(node.type === 'lesson') && (
              <Input label="Lesson Type" value={node.lessonType || ''} onChange={(e) => updateNode(node.id, { lessonType: e.target.value })} fullWidth />
            )}
            {(node.type === 'activity') && (
              <Input label="Activity Type" value={node.activityType || ''} onChange={(e) => updateNode(node.id, { activityType: e.target.value })} fullWidth />
            )}
          </Grid>
          <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
            Reorder, duplicate and archive actions are available directly in the builder tree.
          </div>
        </Stack>
      </Card>
    </Stack>
  );
}
