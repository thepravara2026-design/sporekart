import { useState } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Button } from '../../../../../design-system/components/core/Button';
import { CurriculumTree } from '../visualization/CurriculumTree';
import { CurriculumBreadcrumbs } from '../visualization/CurriculumBreadcrumbs';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_LABELS, type CurriculumNode, type CurriculumNodeType } from '../../data/curriculumMockData';

const CHILD_TYPES: { parent: CurriculumNodeType; allowed: CurriculumNodeType[] }[] = [
  { parent: 'course', allowed: ['curriculum'] },
  { parent: 'curriculum', allowed: ['module', 'completion'] },
  { parent: 'module', allowed: ['lesson'] },
  { parent: 'lesson', allowed: ['topic', 'activity', 'assignment', 'assessment'] },
  { parent: 'topic', allowed: ['activity'] },
];

function getAllowedChildren(type: CurriculumNodeType | undefined): CurriculumNodeType[] {
  if (!type) return ['curriculum', 'module'];
  const found = CHILD_TYPES.find((c) => c.parent === type);
  return found ? found.allowed : [];
}

export function CurriculumBuilderPanel() {
  const { state, addNode, setSelected, updateNode, removeNode } = useCurriculumContext();
  const [newName, setNewName] = useState('');
  const [newType, setNewType] = useState<CurriculumNodeType>('module');

  const selected = (() => {
    const find = (nodes: CurriculumNode[]): CurriculumNode | null =>
      nodes.reduce<CurriculumNode | null>((acc, n) => {
        if (acc) return acc;
        if (n.id === state.selectedNodeId) return n;
        return find(n.children);
      }, null);
    return find(state.tree);
  })();

  const allowed = getAllowedChildren(selected?.type);

  const handleAdd = () => {
    if (!newName.trim()) return;
    const parentId = selected ? selected.id : null;
    addNode(
      {
        id: `node-${Date.now()}`,
        type: newType,
        name: newName.trim(),
        description: '',
        status: 'draft',
        durationHours: 0,
        order: 99,
        children: [],
      },
      parentId
    );
    setNewName('');
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Curriculum Builder</span>
          <CurriculumBreadcrumbs />
          <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', alignItems: 'flex-end' }}>
            <div style={{ flex: 1, minWidth: 200 }}>
              <Select
                label="New node type"
                options={allowed.map((t) => ({ value: t, label: NODE_TYPE_LABELS[t] }))}
                value={newType}
                onChange={(v) => setNewType(v as CurriculumNodeType)}
              />
            </div>
            <input
              value={newName}
              onChange={(e) => setNewName(e.target.value)}
              placeholder={`Name for new ${NODE_TYPE_LABELS[newType].toLowerCase()}`}
              style={{ flex: 2, minWidth: 200, padding: '8px 12px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', fontSize: 'var(--font-size-sm)' }}
              aria-label="New node name"
            />
            <Button variant="primary" size="sm" onClick={handleAdd} disabled={!newName.trim()}>Add</Button>
          </div>
          {selected ? (
            <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
              <Button variant="outline" size="sm" onClick={() => updateNode(selected.id, { status: 'published' })}>Publish</Button>
              <Button variant="outline" size="sm" onClick={() => updateNode(selected.id, { status: 'draft' })}>Unpublish</Button>
              <Button variant="destructive" size="sm" onClick={() => { removeNode(selected.id); setSelected(null); }}>Delete</Button>
            </div>
          ) : (
            <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
              Select a node to add children, or add a top-level curriculum.
            </span>
          )}
        </Stack>
      </Card>
      <Card variant="outlined" padding="sm" style={{ maxHeight: '60vh', overflow: 'auto' }}>
        <CurriculumTree />
      </Card>
    </Stack>
  );
}
