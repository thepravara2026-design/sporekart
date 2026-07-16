import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';

const FEATURES = [
  { key: 'move-mod', label: 'Move Modules', desc: 'Reorder modules within curriculum' },
  { key: 'move-les', label: 'Move Lessons', desc: 'Reorder lessons within modules' },
  { key: 'move-top', label: 'Move Topics', desc: 'Reorder topics within lessons' },
  { key: 'move-act', label: 'Move Activities', desc: 'Reorder activities within topics' },
  { key: 'dup', label: 'Duplicate Structure', desc: 'Clone subtrees' },
  { key: 'collapse', label: 'Collapse / Expand', desc: 'Tree node visibility' },
  { key: 'undo', label: 'Undo', desc: 'Revert structural change' },
  { key: 'redo', label: 'Redo', desc: 'Re-apply structural change' },
];

export function DragDropPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Drag & Drop Infrastructure</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Drag handles are present on tree nodes. Full persistence and reordering logic is prepared as a future capability.
          </span>
        </Stack>
      </Card>
      <Grid columns={2} gap="md">
        {FEATURES.map((f) => (
          <Card key={f.key} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{f.label}</span>
                <Badge variant="warning" size="sm">planned</Badge>
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{f.desc}</div>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
