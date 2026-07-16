import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { useBuilderContext } from '../../state/BuilderContext';

const resourceIcons: Record<string, string> = {
  pdf: '\uD83D\uDCC4',
  image: '\uD83D\uDDBC',
  document: '\uD83D\uDCC4',
  reference: '\uD83D\uDCD6',
  sop: '\u2699\uFE0F',
  research: '\uD83D\uDD2C',
  download: '\u2B07\uFE0F',
};

const typeLabels: Record<string, string> = {
  pdf: 'PDF',
  image: 'Image',
  document: 'Document',
  reference: 'Reference',
  sop: 'SOP',
  research: 'Research',
  download: 'Download',
};

export function ResourcesPanel() {
  const { state } = useBuilderContext();

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Resource Attachments</div>
          <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            File upload will be available in a future update. These placeholders define the resource requirements.
          </div>
        </Stack>
      </Card>

      <Grid columns={2} gap="md">
        {(state.resources.length > 0 ? state.resources : [
          { id: 'placeholder-syllabus', type: 'pdf', label: 'Course Syllabus', description: 'Complete course syllabus and schedule' },
          { id: 'placeholder-sop', type: 'sop', label: 'Substrate Preparation SOP', description: 'Standard operating procedure' },
          { id: 'placeholder-research', type: 'research', label: 'Cultivation Research', description: 'Latest research papers' },
          { id: 'placeholder-checklist', type: 'document', label: 'Equipment Checklist', description: 'Complete equipment list' },
        ]).map((res) => (
          <Card key={res.id} variant="outlined" padding="md">
            <Inline gap="md" align="center">
              <span style={{ fontSize: 32 }}>{resourceIcons[res.type] || '\uD83D\uDCC1'}</span>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
                <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>{res.label}</div>
                <Badge variant="info" size="sm">{typeLabels[res.type] || res.type}</Badge>
                {'description' in res && res.description && (
                  <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>
                    {res.description}
                  </div>
                )}
                {'fileName' in res && res.fileName && (
                  <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                    {(res as any).fileName} {(res as any).fileSize ? `(${(res as any).fileSize})` : ''}
                  </div>
                )}
              </div>
            </Inline>
          </Card>
        ))}
      </Grid>
    </Stack>
  );
}
