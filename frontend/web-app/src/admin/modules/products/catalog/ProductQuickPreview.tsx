import React, { memo, useEffect } from 'react';
import { createPortal } from 'react-dom';
import { Icon } from '../../../../design-system/icons/Icon';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { StatusBadge } from '../../../components/status';
import { LifecycleBadge } from '../components/LifecycleBadge';
import {
  STOCK_LABELS,
  TYPE_LABELS,
  brandNameById,
  categoryNameById,
  type CatalogProduct,
} from '../mock/catalogMock';
import { formatDate, formatPrice } from './catalogColumns';

interface ProductQuickPreviewProps {
  product: CatalogProduct | null;
  onClose: () => void;
}

const rowStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  gap: 'var(--space-component-gap)',
  padding: '8px 0',
  borderBottom: '1px solid var(--color-border)',
  fontSize: 'var(--text-body-sm)',
};

const labelStyle: React.CSSProperties = { color: 'var(--color-text-tertiary)' };
const valueStyle: React.CSSProperties = { color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)', textAlign: 'right' };

const sectionTitle: React.CSSProperties = {
  margin: '0 0 var(--space-stack-xs)',
  fontSize: 'var(--text-caption)',
  textTransform: 'uppercase',
  letterSpacing: '0.06em',
  color: 'var(--color-text-tertiary)',
  fontWeight: 'var(--weight-semibold)',
};

const FutureRow = memo(function FutureRow({ icon, label }: { icon: string; label: string }) {
  return (
    <div
      aria-disabled="true"
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 'var(--space-inline-xs)',
        padding: '10px 12px',
        borderRadius: 'var(--radius-sm)',
        border: '1px dashed var(--color-border)',
        color: 'var(--color-text-tertiary)',
        opacity: 0.75,
        fontSize: 'var(--text-body-sm)',
      }}
    >
      <Icon name={icon} size={16} />
      <span>{label}</span>
      <span style={{ marginLeft: 'auto' }}>
        <StatusBadge status="Coming soon" variant="neutral" size="sm" />
      </span>
    </div>
  );
});

export const ProductQuickPreview: React.FC<ProductQuickPreviewProps> = memo(function ProductQuickPreview({ product, onClose }) {
  useEffect(() => {
    if (!product) return;
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handler);
    return () => document.removeEventListener('keydown', handler);
  }, [product, onClose]);

  if (!product) return null;

  const category = categoryNameById[product.categoryId] ?? '—';
  const brand = brandNameById[product.brandId] ?? '—';

  const drawer = (
    <div style={{ position: 'fixed', inset: 0, zIndex: 1200 }}>
      <div
        onClick={onClose}
        style={{ position: 'absolute', inset: 0, background: 'var(--color-bg-overlay)' }}
        aria-hidden="true"
      />
      <aside
        role="dialog"
        aria-modal="true"
        aria-label={`Quick preview: ${product.name}`}
        style={{
          position: 'absolute',
          top: 0,
          right: 0,
          height: '100%',
          width: 'min(420px, 100vw)',
          background: 'var(--color-bg-surface-default)',
          borderLeft: '1px solid var(--color-border)',
          boxShadow: 'var(--shadow-4)',
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden',
        }}
      >
        <header
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            gap: 'var(--space-inline-xs)',
            padding: '14px 16px',
            borderBottom: '1px solid var(--color-border)',
          }}
        >
          <span style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
            Product preview
          </span>
          <button
            type="button"
            onClick={onClose}
            aria-label="Close preview"
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              justifyContent: 'center',
              background: 'transparent',
              border: 'none',
              cursor: 'pointer',
              color: 'var(--color-text-secondary)',
              padding: 4,
              borderRadius: 'var(--radius-sm)',
            }}
          >
            <Icon name="x" size={18} />
          </button>
        </header>

        <div style={{ overflowY: 'auto', padding: '16px', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
          <div style={{ width: '100%', aspectRatio: '16 / 10', borderRadius: 'var(--radius-md)', overflow: 'hidden', background: 'var(--color-bg-surface-raised)' }}>
            {product.hasImages && product.thumbUrl ? (
              <img
                src={product.thumbUrl}
                alt={product.name}
                style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
                onError={(e) => {
                  e.currentTarget.style.display = 'none';
                }}
              />
            ) : (
              <Skeleton variant="rectangular" width="100%" height="100%" />
            )}
          </div>

          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', marginBottom: 6 }}>
              <LifecycleBadge state={product.lifecycleState} size="md" />
              {product.featured && <StatusBadge status="Featured" variant="warning" />}
            </div>
            <h2 style={{ margin: '0 0 4px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-bold)' }}>
              {product.name}
            </h2>
            <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{product.shortDescription}</p>
          </div>

          <section>
            <h4 style={sectionTitle}>Basic Info</h4>
            <div style={rowStyle}><span style={labelStyle}>SKU</span><span style={valueStyle}>{product.sku}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Barcode</span><span style={valueStyle}>{product.barcode}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Type</span><span style={valueStyle}>{TYPE_LABELS[product.productType] ?? product.productType}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Stock</span><span style={valueStyle}>{STOCK_LABELS[product.stockStatus]}</span></div>
          </section>

          <section>
            <h4 style={sectionTitle}>Pricing Summary</h4>
            <div style={rowStyle}><span style={labelStyle}>MRP</span><span style={valueStyle}>{formatPrice(product.pricing.mrp)}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Selling</span><span style={valueStyle}>{formatPrice(product.pricing.sellingPrice)}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Wholesale</span><span style={valueStyle}>{formatPrice(product.pricing.wholesalePrice)}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Discount</span><span style={valueStyle}>{product.pricing.discount}%</span></div>
          </section>

          <section>
            <h4 style={sectionTitle}>Classification</h4>
            <div style={rowStyle}><span style={labelStyle}>Category</span><span style={valueStyle}>{category}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Brand</span><span style={valueStyle}>{brand}</span></div>
          </section>

          <section>
            <h4 style={sectionTitle}>Tags</h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-inline-xs)' }}>
              {product.tags.length ? (
                product.tags.map((t) => <StatusBadge key={t} status={t} variant="neutral" />)
              ) : (
                <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No tags</span>
              )}
            </div>
          </section>

          <section>
            <h4 style={sectionTitle}>Timeline</h4>
            <div style={rowStyle}><span style={labelStyle}>Created</span><span style={valueStyle}>{formatDate(product.createdAt)}</span></div>
            <div style={rowStyle}><span style={labelStyle}>Updated</span><span style={valueStyle}>{formatDate(product.updatedAt)}</span></div>
          </section>

          <section>
            <h4 style={sectionTitle}>Coming Soon</h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
              <FutureRow icon="box" label="Future: Inventory" />
              <FutureRow icon="package" label="Future: Orders" />
              <FutureRow icon="bar-chart" label="Future: Analytics" />
            </div>
          </section>
        </div>
      </aside>
    </div>
  );

  return createPortal(drawer, document.body);
});

export default ProductQuickPreview;
