import { useState } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Button } from '../../../../../design-system/components/core/Button';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { useBuilderContext } from '../../state/BuilderContext';
import type { LearningObjective } from '../../data/builderMockData';

export function LearningObjectivesPanel() {
  const { state, addObjective, updateObjective, removeObjective, reorderObjectives } = useBuilderContext();
  const [newText, setNewText] = useState('');
  const [newPriority, setNewPriority] = useState<'high' | 'medium' | 'low'>('medium');
  const [newCategory, setNewCategory] = useState<'knowledge' | 'skill' | 'competency'>('knowledge');
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editText, setEditText] = useState('');

  const handleAdd = () => {
    if (!newText.trim()) return;
    addObjective({
      id: `obj-${Date.now()}`,
      text: newText.trim(),
      priority: newPriority,
      category: newCategory,
    });
    setNewText('');
  };

  const handleStartEdit = (obj: LearningObjective) => {
    setEditingId(obj.id);
    setEditText(obj.text);
  };

  const handleSaveEdit = (id: string) => {
    if (!editText.trim()) return;
    updateObjective(id, { text: editText.trim() });
    setEditingId(null);
    setEditText('');
  };

  const handleMoveUp = (index: number) => {
    if (index === 0) return;
    const updated = [...state.objectives];
    [updated[index - 1], updated[index]] = [updated[index], updated[index - 1]];
    reorderObjectives(updated);
  };

  const handleMoveDown = (index: number) => {
    if (index === state.objectives.length - 1) return;
    const updated = [...state.objectives];
    [updated[index], updated[index + 1]] = [updated[index + 1], updated[index]];
    reorderObjectives(updated);
  };

  const priorityColor = (p: string) => {
    switch (p) {
      case 'high': return 'danger';
      case 'medium': return 'warning';
      case 'low': return 'info';
      default: return 'default';
    }
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Add Learning Objective</div>
          <Grid columns={3} gap="md">
            <Input
              label="Objective Text"
              value={newText}
              onChange={(e) => setNewText(e.target.value)}
              placeholder="e.g. Understand mushroom biology"
              fullWidth
            />
            <Select
              label="Priority"
              options={[
                { value: 'high', label: 'High' },
                { value: 'medium', label: 'Medium' },
                { value: 'low', label: 'Low' },
              ]}
              value={newPriority}
              onChange={(v) => setNewPriority(v as 'high' | 'medium' | 'low')}
            />
            <Select
              label="Category"
              options={[
                { value: 'knowledge', label: 'Knowledge' },
                { value: 'skill', label: 'Skill' },
                { value: 'competency', label: 'Competency' },
              ]}
              value={newCategory}
              onChange={(v) => setNewCategory(v as 'knowledge' | 'skill' | 'competency')}
            />
          </Grid>
          <Button variant="primary" size="sm" onClick={handleAdd} disabled={!newText.trim()}>
            Add Objective
          </Button>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>
            Objectives ({state.objectives.length})
          </div>
          {state.objectives.length === 0 && (
            <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--font-size-sm)' }}>
              No objectives added yet. Add your first learning objective above.
            </div>
          )}
          {state.objectives.map((obj, index) => (
            <Card key={obj.id} variant="outlined" padding="sm">
              <Inline gap="md" align="center">
                <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--font-size-xs)', width: 20 }}>
                  {index + 1}.
                </span>
                <div style={{ flex: 1 }}>
                  {editingId === obj.id ? (
                    <Inline gap="sm">
                      <Input
                        value={editText}
                        onChange={(e) => setEditText(e.target.value)}
                        fullWidth
                      />
                      <Button variant="primary" size="sm" onClick={() => handleSaveEdit(obj.id)}>Save</Button>
                      <Button variant="ghost" size="sm" onClick={() => setEditingId(null)}>Cancel</Button>
                    </Inline>
                  ) : (
                    <span style={{ fontSize: 'var(--font-size-sm)' }}>{obj.text}</span>
                  )}
                </div>
                <Inline gap="xs">
                  <Badge variant={priorityColor(obj.priority) as any} size="sm">
                    {obj.priority}
                  </Badge>
                  <Badge variant="default" size="sm">
                    {obj.category}
                  </Badge>
                </Inline>
                <Inline gap="xs">
                  <button
                    onClick={() => handleMoveUp(index)}
                    disabled={index === 0}
                    aria-label="Move up"
                    style={{ border: 'none', background: 'none', cursor: index === 0 ? 'not-allowed' : 'pointer', padding: 4, fontSize: 16, opacity: index === 0 ? 0.3 : 1 }}
                  >
                    \u25B2
                  </button>
                  <button
                    onClick={() => handleMoveDown(index)}
                    disabled={index === state.objectives.length - 1}
                    aria-label="Move down"
                    style={{ border: 'none', background: 'none', cursor: index === state.objectives.length - 1 ? 'not-allowed' : 'pointer', padding: 4, fontSize: 16, opacity: index === state.objectives.length - 1 ? 0.3 : 1 }}
                  >
                    \u25BC
                  </button>
                  <button
                    onClick={() => handleStartEdit(obj)}
                    aria-label="Edit objective"
                    style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4, fontSize: 14, color: 'var(--color-primary-500)' }}
                  >
                    \u270F
                  </button>
                  <button
                    onClick={() => removeObjective(obj.id)}
                    aria-label="Remove objective"
                    style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4, fontSize: 14, color: 'var(--color-danger-500)' }}
                  >
                    \u2716
                  </button>
                </Inline>
              </Inline>
            </Card>
          ))}
        </Stack>
      </Card>
    </Stack>
  );
}
