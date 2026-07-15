import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { ProductCard } from '../../../../design-system/components/composite/ProductCard';
import { StatusBadge } from '../../../components/status';
import { LifecycleBadge } from '../components/LifecycleBadge';
import type { ProductWizardData } from './types';
import { formatCurrency, formatDimensions, formatWeight } from './validation';

export interface ProductPreviewPanelProps {
  data: ProductWizardData;
}

function Field({ label, value }: { label: string; value: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)' }}>
        {value || <span style={{ color: 'var(--color-text-tertiary)' }}>—</span>}
      </span>
    </div>
  );
}

const ProductPreviewPanel: React.FC<ProductPreviewPanelProps> = ({ data }) => {
  const slug = data.slug || (data.name ? `${data.name.toLowerCase().replace(/\s+/g, '-')}` : 'product-slug');
  const title = data.name || 'Untitled Product';

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)' }}>Live Preview</h2>
        <LifecycleBadge state="draft" />
      </div>

      <Card variant="outlined" padding="sm">
          <ProductCard
            title={title}
            price={formatCurrency(data.price, data.currency)}
            originalPrice={data.mrp ? formatCurrency(data.mrp, data.currency) : undefined}
            badge={data.category || undefined}
          />
      </Card>

      <Card variant="outlined" padding="md">
        <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h5)' }}>Catalog Row</h3>
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: '2fr 1fr 1fr 1fr',
            gap: 'var(--space-inline-sm)',
            fontSize: 'var(--text-body-sm)',
            padding: '8px 0',
            borderBottom: '1px solid var(--color-border-subtle)',
          }}
        >
          <span style={{ color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)' }}>{title}</span>
          <span style={{ color: 'var(--color-text-secondary)' }}>{data.sku || '—'}</span>
          <span style={{ color: 'var(--color-text-primary)' }}>{formatCurrency(data.price, data.currency)}</span>
          <span>
            <StatusBadge status="Draft" variant="neutral" />
          </span>
        </div>
      </Card>

      <Card variant="outlined" padding="md">
        <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h5)' }}>Summary</h3>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-stack-sm)' }}>
          <Field label="Category" value={data.category} />
          <Field label="Brand" value={data.brand} />
          <Field label="Packaging" value={data.packagingType} />
          <Field label="Units / Pack" value={data.unitsPerPack} />
          <Field label="Weight" value={formatWeight(data.weight, data.weightUnit)} />
          <Field label="Dimensions" value={formatDimensions(data.dimensions)} />
        </div>
      </Card>

      <Card variant="outlined" padding="md">
        <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h5)' }}>Search Snippet</h3>
        <div style={{ fontFamily: 'var(--font-family-sans)' }}>
          <div style={{ color: 'var(--color-text-link)', fontSize: 'var(--text-body)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
            {data.metaTitle || title}
          </div>
          <div style={{ color: 'var(--color-text-success)', fontSize: 'var(--text-caption)' }}>
            https://sporekart.com/products/{slug}
          </div>
          <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            {data.metaDescription || data.shortDescription || 'No meta description provided yet.'}
          </div>
        </div>
      </Card>

      <Card
        variant="ghost"
        padding="md"
        style={{ border: '1px dashed var(--color-border-default)', textAlign: 'center' }}
      >
        <StatusBadge status="Placeholder" variant="neutral" />
        <p style={{ margin: 'var(--space-stack-xs) 0 0', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>
          Future Marketplace Preview — storefront card rendering will appear here.
        </p>
      </Card>
    </div>
  );
};

export default React.memo(ProductPreviewPanel);
