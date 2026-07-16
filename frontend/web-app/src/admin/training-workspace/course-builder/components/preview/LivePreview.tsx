import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { useBuilderContext } from '../../state/BuilderContext';

const DEVICE_WIDTHS: Record<string, number> = {
  desktop: 1280,
  tablet: 768,
  mobile: 375,
};

export function LivePreview() {
  const { state, setPreviewDevice, setPreviewTheme } = useBuilderContext();
  const { info, objectives, prerequisites, seo, settings, previewDevice, previewTheme } = state;

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="md">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 16, flexWrap: 'wrap' }}>
          <Inline gap="sm">
            <Button
              variant={previewDevice === 'desktop' ? 'primary' : 'outline'}
              size="sm"
              onClick={() => setPreviewDevice('desktop')}
            >
              Desktop
            </Button>
            <Button
              variant={previewDevice === 'tablet' ? 'primary' : 'outline'}
              size="sm"
              onClick={() => setPreviewDevice('tablet')}
            >
              Tablet
            </Button>
            <Button
              variant={previewDevice === 'mobile' ? 'primary' : 'outline'}
              size="sm"
              onClick={() => setPreviewDevice('mobile')}
            >
              Mobile
            </Button>
          </Inline>
          <Inline gap="sm">
            <Button
              variant={previewTheme === 'light' ? 'primary' : 'outline'}
              size="sm"
              onClick={() => setPreviewTheme('light')}
            >
              Light
            </Button>
            <Button
              variant={previewTheme === 'dark' ? 'primary' : 'outline'}
              size="sm"
              onClick={() => setPreviewTheme('dark')}
            >
              Dark
            </Button>
          </Inline>
        </div>
      </Card>

      <Card variant="elevated" padding="none">
        <div
          style={{
            display: 'flex',
            justifyContent: 'center',
            padding: 'var(--space-4)',
            background: previewTheme === 'dark' ? '#1a1a2e' : '#f5f5f5',
            minHeight: 500,
          }}
        >
          <div
            style={{
              width: DEVICE_WIDTHS[previewDevice],
              maxWidth: '100%',
              background: previewTheme === 'dark' ? '#16213e' : '#ffffff',
              color: previewTheme === 'dark' ? '#e0e0e0' : '#1a1a1a',
              borderRadius: previewDevice === 'mobile' ? 20 : 8,
              boxShadow: previewDevice !== 'desktop'
                ? '0 4px 20px rgba(0,0,0,0.15)'
                : 'none',
              overflow: 'hidden',
              transition: 'width 0.3s ease',
              fontSize: previewDevice === 'mobile' ? 'var(--font-size-xs)' : 'var(--font-size-sm)',
            }}
          >
            <div style={{ padding: previewDevice === 'mobile' ? '12px' : '24px' }}>
              <Stack gap={previewDevice === 'mobile' ? 'sm' : 'md'}>
                {info.title && (
                  <div>
                    <div style={{
                      fontSize: previewDevice === 'mobile' ? 18 : 28,
                      fontWeight: 700,
                      marginBottom: 4,
                    }}>
                      {info.title}
                    </div>
                    {info.code && (
                      <Badge variant="info" size="sm">{info.code}</Badge>
                    )}
                    <span style={{ marginLeft: 8 }}>
                      <Badge
                        variant={settings.status === 'published' ? 'success' : 'warning'}
                        size="sm"
                      >
                        {settings.status}
                      </Badge>
                    </span>
                  </div>
                )}

                {info.shortDescription && (
                  <div style={{ color: previewTheme === 'dark' ? '#aaa' : '#666', lineHeight: 1.5 }}>
                    {info.shortDescription}
                  </div>
                )}

                <Inline gap="sm" wrap>
                  {info.difficulty && <Badge variant="default" size="sm">{info.difficulty}</Badge>}
                  {info.deliveryMode && <Badge variant="default" size="sm">{info.deliveryMode}</Badge>}
                  {info.duration && <Badge variant="default" size="sm">{info.duration}</Badge>}
                  {info.language && <Badge variant="default" size="sm">{info.language}</Badge>}
                </Inline>

                {objectives.length > 0 && (
                  <div>
                    <div style={{ fontWeight: 600, marginBottom: 8, fontSize: previewDevice === 'mobile' ? 14 : 16 }}>
                      Learning Objectives
                    </div>
                    <ul style={{ margin: 0, paddingLeft: 20 }}>
                      {objectives.map((obj) => (
                        <li key={obj.id} style={{ marginBottom: 4, lineHeight: 1.5 }}>{obj.text}</li>
                      ))}
                    </ul>
                  </div>
                )}

                {prerequisites.length > 0 && (
                  <div>
                    <div style={{ fontWeight: 600, marginBottom: 8, fontSize: previewDevice === 'mobile' ? 14 : 16 }}>
                      Prerequisites
                    </div>
                    <ul style={{ margin: 0, paddingLeft: 20 }}>
                      {prerequisites.map((pre) => (
                        <li key={pre.id} style={{ marginBottom: 4, lineHeight: 1.5 }}>{pre.label}</li>
                      ))}
                    </ul>
                  </div>
                )}

                {seo.seoTitle && (
                  <div style={{
                    borderTop: `1px solid ${previewTheme === 'dark' ? '#333' : '#e0e0e0'}`,
                    paddingTop: 12,
                    marginTop: 8,
                  }}>
                    <div style={{ fontSize: 'var(--font-size-xs)', color: '#1a73e8', marginBottom: 2 }}>{seo.slug || 'course-url'}</div>
                    <div style={{ fontWeight: 500, color: '#1a0dab' }}>{seo.seoTitle}</div>
                    <div style={{ fontSize: 'var(--font-size-xs)', color: '#545454' }}>{seo.seoDescription}</div>
                  </div>
                )}
              </Stack>
            </div>
          </div>
        </div>
      </Card>
    </Stack>
  );
}
