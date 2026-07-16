import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Input } from '../../../../../design-system/components/core/Input';
import { useBuilderContext } from '../../state/BuilderContext';

export function SeoPanel() {
  const { state, setSeo } = useBuilderContext();
  const { seo } = state;

  const handleKeywordsChange = (value: string) => {
    setSeo({ metaKeywords: value.split(',').map((s) => s.trim()).filter(Boolean) });
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>SEO Configuration</div>

          <Grid columns={2} gap="md">
            <Input
              label="SEO Title"
              value={seo.seoTitle}
              onChange={(e) => setSeo({ seoTitle: e.target.value })}
              placeholder="SEO-optimized title"
              fullWidth
              helperText={seo.seoTitle ? `${60 - seo.seoTitle.length} characters remaining` : undefined}
            />
            <Input
              label="Slug"
              value={seo.slug}
              onChange={(e) => setSeo({ slug: e.target.value })}
              placeholder="course-url-slug"
              fullWidth
            />
          </Grid>

          <Grid columns={2} gap="md">
            <Input
              label="Canonical URL"
              value={seo.canonicalUrl}
              onChange={(e) => setSeo({ canonicalUrl: e.target.value })}
              placeholder="https://sporekart.com/courses/..."
              fullWidth
            />
            <Input
              label="Meta Keywords"
              value={seo.metaKeywords.join(', ')}
              onChange={(e) => handleKeywordsChange(e.target.value)}
              placeholder="Comma-separated keywords"
              helperText="Separate multiple keywords with commas"
              fullWidth
            />
          </Grid>

          <div style={{ position: 'relative' }}>
            <label style={{ display: 'block', fontSize: 'var(--font-size-sm)', fontWeight: 500, marginBottom: 4 }}>
              SEO Description
            </label>
            <textarea
              value={seo.seoDescription}
              onChange={(e) => setSeo({ seoDescription: e.target.value })}
              placeholder="Meta description for search engines"
              rows={3}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-sm)',
                fontSize: 'var(--font-size-sm)',
                fontFamily: 'inherit',
                resize: 'vertical',
                background: 'var(--color-bg-primary)',
                color: 'var(--color-text-primary)',
              }}
              aria-label="SEO Description"
            />
            <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)', marginTop: 4 }}>
              {160 - seo.seoDescription.length} characters remaining
            </div>
          </div>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Social Preview</div>

          <Grid columns={2} gap="md">
            <Input
              label="Open Graph Title"
              value={seo.ogTitle}
              onChange={(e) => setSeo({ ogTitle: e.target.value })}
              placeholder="Title for social sharing"
              fullWidth
            />
            <Input
              label="Open Graph Description"
              value={seo.ogDescription}
              onChange={(e) => setSeo({ ogDescription: e.target.value })}
              placeholder="Description for social sharing"
              fullWidth
            />
          </Grid>

          <Grid columns={2} gap="md">
            <Input
              label="Social Preview Title"
              value={seo.socialPreview.title}
              onChange={(e) => setSeo({ socialPreview: { ...seo.socialPreview, title: e.target.value } })}
              placeholder="Social preview title"
              fullWidth
            />
            <Input
              label="Social Preview Image URL"
              value={seo.socialPreview.imageUrl}
              onChange={(e) => setSeo({ socialPreview: { ...seo.socialPreview, imageUrl: e.target.value } })}
              placeholder="https://..."
              fullWidth
            />
          </Grid>

          <div style={{ position: 'relative' }}>
            <label style={{ display: 'block', fontSize: 'var(--font-size-sm)', fontWeight: 500, marginBottom: 4 }}>
              Social Preview Description
            </label>
            <textarea
              value={seo.socialPreview.description}
              onChange={(e) => setSeo({ socialPreview: { ...seo.socialPreview, description: e.target.value } })}
              placeholder="Social preview description"
              rows={2}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-sm)',
                fontSize: 'var(--font-size-sm)',
                fontFamily: 'inherit',
                resize: 'vertical',
                background: 'var(--color-bg-primary)',
                color: 'var(--color-text-primary)',
              }}
              aria-label="Social Preview Description"
            />
          </div>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Structured Data</div>
          <div style={{ position: 'relative' }}>
            <label style={{ display: 'block', fontSize: 'var(--font-size-sm)', fontWeight: 500, marginBottom: 4 }}>
              Structured Data (JSON-LD)
            </label>
            <textarea
              value={seo.structuredData}
              onChange={(e) => setSeo({ structuredData: e.target.value })}
              placeholder='{\n  "@context": "https://schema.org",\n  "@type": "Course",\n  ...\n}'
              rows={6}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-sm)',
                fontSize: 'var(--font-size-sm)',
                fontFamily: 'monospace',
                resize: 'vertical',
                background: 'var(--color-bg-code)',
                color: 'var(--color-text-primary)',
              }}
              aria-label="Structured Data"
            />
          </div>
          <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
            Future: JSON-LD structured data for rich search results
          </div>
        </Stack>
      </Card>
    </Stack>
  );
}
