import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { useBuilderContext } from '../../state/BuilderContext';

const mediaIcons: Record<string, string> = {
  thumbnail: '\uD83D\uDDBC',
  banner: '\uD83D\uDDBC',
  'promo-video': '\uD83C\uDFA5',
  'lesson-video': '\uD83C\uDFA5',
  gallery: '\uD83D\uDDBC',
  pdf: '\uD83D\uDCC4',
  presentation: '\uD83D\uDCCA',
  document: '\uD83D\uDCC4',
};

const typeLabels: Record<string, string> = {
  thumbnail: 'Thumbnail',
  banner: 'Banner',
  'promo-video': 'Promo Video',
  'lesson-video': 'Lesson Video',
  gallery: 'Gallery',
  pdf: 'PDF',
  presentation: 'Presentation',
  document: 'Document',
};

export function MediaPlaceholdersPanel() {
  const { state } = useBuilderContext();

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Media Placeholders</div>
          <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Media storage integration will be available in a future update. These placeholders define the media requirements.
          </div>
        </Stack>
      </Card>

      <Grid columns={2} gap="md">
        {(state.mediaPlaceholders.length > 0 ? state.mediaPlaceholders : [
          { id: 'placeholder-thumb', type: 'thumbnail', label: 'Course Thumbnail' },
          { id: 'placeholder-banner', type: 'banner', label: 'Course Banner' },
          { id: 'placeholder-promo', type: 'promo-video', label: 'Promotional Video' },
          { id: 'placeholder-gallery', type: 'gallery', label: 'Gallery Images' },
        ]).map((media) => (
          <Card key={media.id} variant="outlined" padding="md">
            <Inline gap="md" align="center">
              <span style={{ fontSize: 32 }}>{mediaIcons[media.type] || '\uD83D\uDCC1'}</span>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
                <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>{media.label}</div>
                <Badge variant="info" size="sm">{typeLabels[media.type] || media.type}</Badge>
                {'fileName' in media && media.fileName && (
                  <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
                    {(media as any).fileName} {(media as any).fileSize ? `(${(media as any).fileSize})` : ''}
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
