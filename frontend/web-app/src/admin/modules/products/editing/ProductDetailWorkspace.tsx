import React from 'react';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import StatusBadge from '../../../components/status/StatusBadge';
import { type Crumb } from '../../../../design-system/components/navigation/Breadcrumb';
import { ProductLayout } from '../layout/ProductLayout';
import { lifecycleLabel, lifecycleToBadge } from '../lifecycle';
import { useProductEditState } from './useProductEditState';
import ActivityTimeline from './ActivityTimeline';
import { FIELD_LABELS } from '../creation/validation';
import { formatValue } from './changeDetection';
import type { ProductWizardData } from '../creation/types';

function DetailGroup({ title, fields }: { title: string; fields: { key: string; value: unknown }[] }) {
  return (
    <Card variant="outlined" padding="lg">
      <h3 style={{ margin: '0 0 var(--space-3)', color: 'var(--color-text-primary)', fontSize: 'var(--text-lg)' }}>{title}</h3>
      <dl style={{ margin: 0, display: 'grid', gridTemplateColumns: 'max-content 1fr', gap: 'var(--space-2) var(--space-4)' }}>
        {fields.map((f) => (
          <React.Fragment key={f.key}>
            <dt style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>{FIELD_LABELS[f.key as keyof ProductWizardData] ?? f.key}</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', wordBreak: 'break-word' }}>{formatValue(f.value)}</dd>
          </React.Fragment>
        ))}
      </dl>
    </Card>
  );
}

const ProductDetailWorkspaceBase: React.FC = () => {
  const state = useProductEditState();
  const d = state.data;

  const crumbs: Crumb[] = [
    { label: 'Admin', href: '/admin/products' },
    { label: 'Products', href: '/admin/products' },
    { label: 'Details' },
  ];

  return (
    <ProductLayout
      title="Product Details"
      breadcrumbs={crumbs}
      actions={
        <div style={{ display: 'flex', gap: 'var(--space-2)', alignItems: 'center' }}>
          <StatusBadge status={lifecycleLabel(state.lifecycle)} variant={lifecycleToBadge(state.lifecycle)} size="sm" />
          <Button variant="primary" size="sm" onClick={() => undefined} leftIcon={<Icon name="Edit" size={16} />}>
            Edit Product
          </Button>
        </div>
      }
    >
      <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1.6fr) minmax(0, 1fr)', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
          <Card variant="default" padding="lg">
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', flexWrap: 'wrap' }}>
              <div
                aria-hidden="true"
                style={{ width: 48, height: 48, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}
              >
                <Icon name="package" size={24} style={{ color: 'var(--color-primary)' }} />
              </div>
              <div>
                <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>{d.name}</h2>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>SKU {d.sku} · {d.category}</span>
              </div>
            </div>
            <p style={{ marginTop: 'var(--space-3)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', lineHeight: 1.6 }}>{d.description}</p>
          </Card>

          <DetailGroup
            title="Basic Information"
            fields={[
              { key: 'name', value: d.name },
              { key: 'shortDescription', value: d.shortDescription },
              { key: 'productType', value: d.productType },
              { key: 'brand', value: d.brand },
              { key: 'manufacturer', value: d.manufacturer },
              { key: 'sku', value: d.sku },
              { key: 'barcode', value: d.barcode },
              { key: 'category', value: d.category },
              { key: 'collection', value: d.collection },
              { key: 'tags', value: d.tags },
            ]}
          />
          <DetailGroup
            title="Classification"
            fields={[
              { key: 'productFamily', value: d.productFamily },
              { key: 'productGroup', value: d.productGroup },
              { key: 'mushroomType', value: d.mushroomType },
              { key: 'growingMethod', value: d.growingMethod },
              { key: 'season', value: d.season },
              { key: 'productNature', value: d.productNature },
              { key: 'attributes', value: d.attributes },
            ]}
          />
          <DetailGroup
            title="Packaging & Physical"
            fields={[
              { key: 'packagingType', value: d.packagingType },
              { key: 'packageSize', value: d.packageSize },
              { key: 'unitsPerPack', value: d.unitsPerPack },
              { key: 'weight', value: `${d.weight} ${d.weightUnit}` },
              { key: 'dimensions', value: d.dimensions },
              { key: 'packageWeight', value: `${d.packageWeight} ${d.packageWeightUnit}` },
              { key: 'shelfLife', value: d.shelfLife },
              { key: 'storageConditions', value: d.storageConditions },
              { key: 'countryOfOrigin', value: d.countryOfOrigin },
              { key: 'gst', value: d.gst },
              { key: 'hsnCode', value: d.hsnCode },
            ]}
          />
          <DetailGroup
            title="Pricing (Mock)"
            fields={[
              { key: 'mrp', value: d.mrp },
              { key: 'price', value: d.price },
              { key: 'wholesalePrice', value: d.wholesalePrice },
              { key: 'discount', value: d.discount },
              { key: 'cost', value: d.cost },
              { key: 'currency', value: d.currency },
              { key: 'taxClass', value: d.taxClass },
              { key: 'stockKeepingUnit', value: d.stockKeepingUnit },
              { key: 'priceNotes', value: d.priceNotes },
            ]}
          />
          <DetailGroup
            title="SEO"
            fields={[
              { key: 'metaTitle', value: d.metaTitle },
              { key: 'metaDescription', value: d.metaDescription },
              { key: 'slug', value: d.slug },
              { key: 'keywords', value: d.keywords },
              { key: 'canonicalUrl', value: d.canonicalUrl },
              { key: 'ogTitle', value: d.ogTitle },
              { key: 'ogDescription', value: d.ogDescription },
            ]}
          />
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
          <ActivityTimeline events={state.activity} />
        </div>
      </div>
    </ProductLayout>
  );
};

export const ProductDetailWorkspace = React.memo(ProductDetailWorkspaceBase);
export default ProductDetailWorkspace;
