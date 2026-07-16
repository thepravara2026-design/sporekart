import { useState } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Button } from '../../../../../design-system/components/core/Button';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { useBuilderContext } from '../../state/BuilderContext';
import type { Prerequisite } from '../../data/builderMockData';

const PREREQ_TYPE_OPTIONS = [
  { value: 'knowledge', label: 'Required Knowledge' },
  { value: 'equipment', label: 'Required Equipment' },
  { value: 'reading', label: 'Recommended Reading' },
  { value: 'course', label: 'Previous Course' },
  { value: 'experience', label: 'Experience Level' },
];

export function PrerequisitesPanel() {
  const { state, addPrerequisite, removePrerequisite } = useBuilderContext();
  const [newLabel, setNewLabel] = useState('');
  const [newDescription, setNewDescription] = useState('');
  const [newType, setNewType] = useState<Prerequisite['type']>('knowledge');

  const handleAdd = () => {
    if (!newLabel.trim()) return;
    addPrerequisite({
      id: `pre-${Date.now()}`,
      type: newType,
      label: newLabel.trim(),
      description: newDescription.trim(),
    });
    setNewLabel('');
    setNewDescription('');
  };

  const typeLabels: Record<string, string> = {
    knowledge: 'Knowledge',
    equipment: 'Equipment',
    reading: 'Reading',
    course: 'Course',
    experience: 'Experience',
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Add Prerequisite</div>
          <Grid columns={3} gap="md">
            <Input
              label="Prerequisite"
              value={newLabel}
              onChange={(e) => setNewLabel(e.target.value)}
              placeholder="e.g. Basic Biology"
              fullWidth
            />
            <Input
              label="Description"
              value={newDescription}
              onChange={(e) => setNewDescription(e.target.value)}
              placeholder="Brief description"
              fullWidth
            />
            <Select
              label="Type"
              options={PREREQ_TYPE_OPTIONS}
              value={newType}
              onChange={(v) => setNewType(v as Prerequisite['type'])}
            />
          </Grid>
          <Button variant="primary" size="sm" onClick={handleAdd} disabled={!newLabel.trim()}>
            Add Prerequisite
          </Button>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>
            Prerequisites ({state.prerequisites.length})
          </div>
          {state.prerequisites.length === 0 && (
            <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--font-size-sm)' }}>
              No prerequisites added yet.
            </div>
          )}
          {state.prerequisites.map((pre) => (
            <Card key={pre.id} variant="outlined" padding="sm">
              <Inline gap="md" align="center">
                <Badge variant="info" size="sm">{typeLabels[pre.type]}</Badge>
                <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
                  <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>{pre.label}</span>
                  {pre.description && (
                    <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{pre.description}</span>
                  )}
                </div>
                <button
                  onClick={() => removePrerequisite(pre.id)}
                  aria-label={`Remove ${pre.label}`}
                  style={{ border: 'none', background: 'none', cursor: 'pointer', padding: 4, fontSize: 14, color: 'var(--color-danger-500)' }}
                >
                  \u2716
                </button>
              </Inline>
            </Card>
          ))}
        </Stack>
      </Card>
    </Stack>
  );
}
