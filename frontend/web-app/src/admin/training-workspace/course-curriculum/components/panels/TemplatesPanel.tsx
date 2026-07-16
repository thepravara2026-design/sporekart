import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';

export function TemplatesPanel() {
  const { state, setSelectedTemplate } = useCurriculumContext();

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Curriculum Templates</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Reusable starting structures for different program types.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {state.templates.map((tpl) => (
          <Card key={tpl.id} variant="outlined" padding="md" hoverable onClick={() => setSelectedTemplate(tpl.id)} aria-label={`Select ${tpl.name}`}>
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{tpl.name}</span>
                {tpl.future ? <Badge variant="warning" size="sm">future</Badge> : <Badge variant="success" size="sm">available</Badge>}
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{tpl.description}</div>
              <Badge variant="default" size="sm">{tpl.moduleCount} modules</Badge>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
