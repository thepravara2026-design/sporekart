import React, { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Checkbox } from '../../../../design-system/components/core/Checkbox';
import { Icon } from '../../../../design-system/icons/Icon';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { LifecycleBadge } from '../components/LifecycleBadge';
import { brandNameById, type CatalogProduct } from '../mock/catalogMock';
import { formatPrice } from './catalogColumns';

export interface CardViewProps {
  products: CatalogProduct[];
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onPreview: (product: CatalogProduct) => void;
}

const overlayBtn: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 30,
  height: 30,
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-secondary)',
  cursor: 'pointer',
};

const ProductGridCard = memo(function ProductGridCard({
  product,
  selected,
  onToggleSelect,
  onPreview,
}: {
  product: CatalogProduct;
  selected: boolean;
  onToggleSelect: (id: string) => void;
  onPreview: (product: CatalogProduct) => void;
}) {
  return (
    <div
      className="sk-cat-card"
      style={{
        position: 'relative',
        transition: 'transform 0.15s ease, box-shadow 0.15s ease',
        borderRadius: 'var(--radius-card)',
      }}
    >
      <div
        style={{ position: 'absolute', top: 8, left: 8, zIndex: 2 }}
        onClick={(e) => e.stopPropagation()}
      >
        <span
          style={{
            display: 'inline-flex',
            padding: 2,
            borderRadius: 'var(--radius-sm)',
            background: 'var(--color-bg-surface-default)',
            border: '1px solid var(--color-border)',
          }}
        >
          <Checkbox size="sm" checked={selected} onChange={() => onToggleSelect(product.id)} aria-label={`Select ${product.name}`} />
        </span>
      </div>

      {product.featured && (
        <div style={{ position: 'absolute', top: 8, right: 8, zIndex: 2, color: 'var(--color-warning)' }}>
          <Icon name="star" size={18} />
        </div>
      )}

      <Card variant="outlined" padding="sm" hoverable onClick={() => onPreview(product)} aria-label={`Product ${product.name}`} style={{ minWidth: 0 }}>
        <div style={{ position: 'relative', width: '100%', aspectRatio: '4 / 3', borderRadius: 'var(--radius-md)', overflow: 'hidden', marginBottom: 'var(--space-stack-xs)', background: 'var(--color-bg-surface-raised)' }}>
          {product.hasImages && product.thumbUrl ? (
            <img
              src={product.thumbUrl}
              alt={product.name}
              loading="lazy"
              style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
              onError={(e) => {
                e.currentTarget.style.display = 'none';
              }}
            />
          ) : (
            <Skeleton variant="rounded" width="100%" height="100%" />
          )}
        </div>

        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-inline-xs)' }}>
          <LifecycleBadge state={product.lifecycleState} />
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{brandNameById[product.brandId]}</span>
        </div>

        <h3 style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', margin: 'var(--space-stack-xs) 0 2px', fontWeight: 'var(--weight-semibold)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {product.name}
        </h3>
        <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>SKU: {product.sku}</p>

        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 'var(--space-stack-xs)' }}>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-bold)' }}>
            {formatPrice(product.pricing.sellingPrice)}
          </span>
          <div style={{ display: 'flex', gap: 4 }} onClick={(e) => e.stopPropagation()}>
            <button type="button" style={overlayBtn} aria-label={`Preview ${product.name}`} onClick={() => onPreview(product)}>
              <Icon name="eye" size={15} />
            </button>
            <button type="button" style={overlayBtn} aria-label={`More actions for ${product.name}`} onClick={() => onPreview(product)}>
              <Icon name="more-horizontal" size={15} />
            </button>
          </div>
        </div>
      </Card>
    </div>
  );
});

export const ProductCardView: React.FC<CardViewProps> = memo(function ProductCardView({ products, selectedIds, onToggleSelect, onPreview }) {
  return (
    <div>
      <style>{`.sk-cat-card:hover { transform: translateY(-2px); box-shadow: var(--shadow-3); }`}</style>
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
          gap: 'var(--space-component-gap)',
        }}
      >
        {products.map((p) => (
          <ProductGridCard key={p.id} product={p} selected={selectedIds.has(p.id)} onToggleSelect={onToggleSelect} onPreview={onPreview} />
        ))}
      </div>
    </div>
  );
});

export default ProductCardView;
