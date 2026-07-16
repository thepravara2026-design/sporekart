import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { useTaxonomyContext } from '../../state/TaxonomyContext';

export function KnowledgeMapPlaceholder() {
  const { state } = useTaxonomyContext();

  const domains = state.tree.map((n) => n.name);
  const counts = state.tree.map((n) => n.courseCount);

  return (
    <Card variant="outlined" padding="lg">
      <Stack gap="md">
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Knowledge Map</span>
          <Badge variant="warning" size="sm">Planned</Badge>
        </div>
        <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
          Future heat-map visualization of knowledge coverage across domains and competencies.
        </div>
        <Grid columns={3} gap="md">
          {domains.map((domain, i) => (
            <Card key={domain} variant="ghost" padding="md">
              <Stack gap="xs">
                <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>{domain}</div>
                <div
                  style={{
                    height: 60,
                    borderRadius: 'var(--radius-sm)',
                    background: `linear-gradient(135deg, var(--color-primary-500) ${Math.min(100, counts[i] * 2)}%, var(--color-bg-tertiary))`,
                  }}
                />
                <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                  {counts[i]} courses
                </div>
              </Stack>
            </Card>
          ))}
        </Grid>
      </Stack>
    </Card>
  );
}
