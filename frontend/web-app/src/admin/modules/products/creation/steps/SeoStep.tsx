import React from 'react';
import { FormLayout, FormSection, FormField } from '../../../../../design-system/components/forms';
import { Textarea, TagSelector } from '../../../../components/forms';
import ValidatedField from '../ValidatedField';
import ErrorSummary from '../ErrorSummary';
import { CHARACTER_LIMITS, slugify, computeSeoScore } from '../validation';
import type { ProductWizardData } from '../types';
import type { StepProps } from './BasicInfoStep';

const SeoStep: React.FC<StepProps> = ({ data, errors, setField }) => {
  const d = data as ProductWizardData;
  const seoScore = computeSeoScore(d);

  return (
    <FormLayout>
      <FormSection title="Search & Discovery" description="Optimize how the product appears in search engines and social shares.">
        <ErrorSummary errors={errors} steps={['seo']} compact />

        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            gap: 'var(--space-inline-sm)',
            padding: 'var(--space-stack-sm) var(--space-inline-md)',
            border: '1px dashed var(--color-border-default)',
            borderRadius: 'var(--radius-md)',
            marginBottom: 'var(--space-component-gap)',
          }}
        >
          <div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>SEO Score (placeholder)</div>
            <div style={{ fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-semibold)' }}>{seoScore}/100</div>
          </div>
          <div style={{ flex: 1, maxWidth: 240 }}>
            <div style={{ height: 8, borderRadius: 999, background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
              <div style={{ width: `${seoScore}%`, height: '100%', background: 'var(--color-primary)' }} />
            </div>
          </div>
        </div>

        <ValidatedField name="metaTitle" label="Meta Title" data={d} errors={errors} characterLimit={CHARACTER_LIMITS.metaTitle} helperText="Recommended length: 50–70 characters.">
          <Textarea value={d.metaTitle} onChange={(v) => setField('metaTitle', v)} placeholder="Appears as the browser tab and search result title" rows={1} />
        </ValidatedField>

        <ValidatedField name="metaDescription" label="Meta Description" data={d} errors={errors} characterLimit={CHARACTER_LIMITS.metaDescription} helperText="Recommended length: 120–320 characters.">
          <Textarea value={d.metaDescription} onChange={(v) => setField('metaDescription', v)} placeholder="Short summary shown under the title in search results" rows={3} />
        </ValidatedField>

        <ValidatedField name="slug" label="URL Slug" data={d} errors={errors} helperText="Lowercase letters, numbers, and hyphens only.">
          <div style={{ display: 'flex', gap: 'var(--space-inline-xs)', alignItems: 'flex-start' }}>
            <div style={{ flex: 1 }}>
              <Textarea value={d.slug} onChange={(v) => setField('slug', slugify(v))} placeholder="product-url-slug" rows={1} />
            </div>
            <button
              type="button"
              onClick={() => setField('slug', slugify(d.name || d.metaTitle))}
              style={{
                padding: '10px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-input)',
                background: 'var(--color-bg-surface-default)',
                color: 'var(--color-text-secondary)',
                cursor: 'pointer',
                fontFamily: 'var(--font-family-sans)',
                fontSize: 'var(--text-body-sm)',
                whiteSpace: 'nowrap',
              }}
            >
              Generate
            </button>
          </div>
        </ValidatedField>

        <ValidatedField name="canonicalUrl" label="Canonical URL" data={d} errors={errors} characterLimit={CHARACTER_LIMITS.canonicalUrl} helperText="Preferred URL for search engines.">
          <Textarea value={d.canonicalUrl} onChange={(v) => setField('canonicalUrl', v)} placeholder="https://sporekart.com/products/example" rows={1} />
        </ValidatedField>

        <FormField label="Preview Card">
          <div style={{ border: '1px solid var(--color-border-subtle)', borderRadius: 'var(--radius-md)', padding: 'var(--space-stack-sm)', background: 'var(--color-bg-surface-default)' }}>
            <div style={{ color: 'var(--color-text-link)', fontSize: 'var(--text-body)' }}>{d.metaTitle || d.name || 'Product title'}</div>
            <div style={{ color: 'var(--color-text-success)', fontSize: 'var(--text-caption)' }}>
              https://sporekart.com/products/{d.slug || 'product-slug'}
            </div>
            <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {d.metaDescription || d.shortDescription || 'No meta description provided yet.'}
            </div>
          </div>
        </FormField>

        <ValidatedField name="ogTitle" label="Open Graph Title" data={d} errors={errors} characterLimit={CHARACTER_LIMITS.ogTitle}>
          <Textarea value={d.ogTitle} onChange={(v) => setField('ogTitle', v)} placeholder="Title shown when shared on social media" rows={1} />
        </ValidatedField>

        <ValidatedField name="ogDescription" label="Open Graph Description" data={d} errors={errors} characterLimit={CHARACTER_LIMITS.ogDescription}>
          <Textarea value={d.ogDescription} onChange={(v) => setField('ogDescription', v)} placeholder="Description shown in social share cards" rows={2} />
        </ValidatedField>

        <ValidatedField name="keywords" label="Keywords" data={d} errors={errors}>
          <TagSelector
            tags={d.keywords}
            onChange={(tags) => setField('keywords', tags)}
            placeholder="Add keyword + Enter"
            suggestions={['mushroom', 'organic', 'growkit', 'spore', 'indoor']}
          />
        </ValidatedField>
      </FormSection>
    </FormLayout>
  );
};

export default React.memo(SeoStep);
