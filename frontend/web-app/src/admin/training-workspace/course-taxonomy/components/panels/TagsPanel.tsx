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
import { TAG_TYPE_OPTIONS } from '../../data/taxonomyMockData';

export function TagsPanel() {
  const { state, addTag, removeTag, setFilter } = useTaxonomyContext();
  const [newLabel, setNewLabel] = useState('');
  const [newType, setNewType] = useState<string>('course');

  const filtered = state.filterType === 'all'
    ? state.tags
    : state.tags.filter((t) => t.type === state.filterType);

  const handleAdd = () => {
    if (!newLabel.trim()) return;
    addTag({
      id: `tag-${Date.now()}`,
      label: newLabel.trim().toLowerCase().replace(/\s+/g, '-'),
      type: newType as any,
      usageCount: 0,
      trending: false,
      popular: false,
      suggested: true,
    });
    setNewLabel('');
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Add Tag</span>
          <Grid columns={3} gap="md">
            <Input label="Tag Label" value={newLabel} onChange={(e) => setNewLabel(e.target.value)} placeholder="e.g. compost" fullWidth />
            <Select label="Type" options={TAG_TYPE_OPTIONS} value={newType} onChange={(v) => setNewType(v)} />
            <div style={{ display: 'flex', alignItems: 'flex-end' }}>
              <Button variant="primary" size="sm" onClick={handleAdd} disabled={!newLabel.trim()}>Add Tag</Button>
            </div>
          </Grid>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8, flexWrap: 'wrap' }}>
            <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Tags ({filtered.length})</span>
            <Select
              label=""
              options={[{ value: 'all', label: 'All Types' }, ...TAG_TYPE_OPTIONS]}
              value={state.filterType}
              onChange={(v) => setFilter(v)}
            />
          </div>
          <Grid columns={2} gap="md">
            {filtered.map((tag) => (
              <Card key={tag.id} variant="outlined" padding="sm">
                <Inline gap="sm" align="center">
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
                    <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>{tag.label}</div>
                    <div style={{ display: 'flex', gap: 4 }}>
                      <Badge variant="info" size="sm">{tag.type}</Badge>
                      {tag.trending && <Badge variant="success" size="sm">trending</Badge>}
                      {tag.popular && <Badge variant="warning" size="sm">popular</Badge>}
                      {tag.suggested && <Badge variant="default" size="sm">suggested</Badge>}
                    </div>
                  </div>
                  <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>{tag.usageCount}</span>
                  <button
                    onClick={() => removeTag(tag.id)}
                    aria-label={`Remove ${tag.label}`}
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
