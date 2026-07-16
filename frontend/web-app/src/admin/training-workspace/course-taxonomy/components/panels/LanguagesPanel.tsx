import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { MOCK_LANGUAGES } from '../../data/taxonomyMockData';

export function LanguagesPanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Language Management</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Supported course languages. Future multilingual expansion uses the same architecture.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {MOCK_LANGUAGES.map((lang) => (
          <Card key={lang.id} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 24 }}>{lang.nativeLabel}</span>
                <div style={{ flex: 1 }}>
                  <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>{lang.label}</div>
                  <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>{lang.code}</div>
                </div>
                <Badge variant="success" size="sm">active</Badge>
              </div>
            </Stack>
          </Card>
        ))}
        <Card variant="ghost" padding="md">
          <Stack gap="xs">
            <Badge variant="warning" size="sm">Planned</Badge>
            <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>Multilingual Expansion</div>
            <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
              Additional regional and international languages will extend this list.
            </div>
          </Stack>
        </Card>
      </Grid>
    </Stack>
  );
}
