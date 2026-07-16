import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { MOCK_COMPETENCIES } from '../../data/taxonomyMockData';

const categoryColors: Record<string, string> = {
  technical: 'primary',
  practical: 'info',
  business: 'success',
  laboratory: 'warning',
  equipment: 'neutral',
  quality: 'danger',
  marketing: 'primary',
  entrepreneurship: 'success',
  farm: 'info',
} as any;

export function CompetenciesPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Competency Framework</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Reusable competency architecture. Courses map to competencies to support future matrix and certification alignment.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {MOCK_COMPETENCIES.map((comp) => (
          <Card key={comp.id} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{comp.name}</span>
                <Badge variant={categoryColors[comp.category] as any} size="sm">{comp.category}</Badge>
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{comp.description}</div>
              <Badge variant="default" size="sm">{comp.level}</Badge>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
