import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';

const RESOURCES = [
  { icon: '\uD83D\uDCC4', label: 'PDF', future: false },
  { icon: '\uD83D\uDDBC', label: 'Images', future: false },
  { icon: '\uD83D\uDCDD', label: 'Documents', future: false },
  { icon: '\uD83C\uDFA5', label: 'Videos', future: false },
  { icon: '\uD83D\uDD2C', label: 'Research Papers', future: false },
  { icon: '\uD83E\uDDE0', label: 'Cultivation SOP', future: false },
  { icon: '\uD83D\uDEE0', label: 'Equipment Guide', future: false },
  { icon: '\uD83D\uDCDA', label: 'Reference Material', future: false },
  { icon: '\u2B07', label: 'Downloads', future: true },
];

export function ResourceMappingPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Resource Mapping</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Placeholder resource types that modules and lessons can link to. Upload integration is future.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {RESOURCES.map((res) => (
          <Card key={res.label} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 20 }}>{res.icon}</span>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{res.label}</span>
                {res.future ? <Badge variant="warning" size="sm">future</Badge> : <Badge variant="info" size="sm">ready</Badge>}
              </div>
            </Stack>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
