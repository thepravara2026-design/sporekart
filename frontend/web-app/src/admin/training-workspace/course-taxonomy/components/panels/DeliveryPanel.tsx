import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { MOCK_DELIVERY } from '../../data/taxonomyMockData';

export function DeliveryPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Delivery Classification</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Training delivery modes. VR and AR are marked as future modes.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {MOCK_DELIVERY.map((mode) => (
          <Card key={mode.id} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{mode.label}</span>
                {mode.future ? <Badge variant="warning" size="sm">future</Badge> : <Badge variant="success" size="sm">active</Badge>}
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{mode.description}</div>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
