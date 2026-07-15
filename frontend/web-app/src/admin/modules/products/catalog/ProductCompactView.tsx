import React, { memo } from 'react';
import { Checkbox } from '../../../../design-system/components/core/Checkbox';
import { Icon } from '../../../../design-system/icons/Icon';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { StatusBadge } from '../../../components/status';
import { LifecycleBadge } from '../components/LifecycleBadge';
import {
  STOCK_LABELS,
  brandNameById,
  categoryNameById,
  type CatalogProduct,
} from '../mock/catalogMock';
import { formatPrice } from './catalogColumns';
import type { CardViewProps } from './ProductCardView';

const stockVariant: Record<CatalogProduct['stockStatus'], 'success' | 'warning' | 'danger' | 'info'> = {
  in_stock: 'success',
  low_stock: 'warning',
  out_of_stock: 'danger',
  preorder: 'info',
};

const CompactRow = memo(function CompactRow({
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
      onClick={() => onPreview(product)}
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 'var(--space-inline-xs)',
        padding: '8px 12px',
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-sm)',
        background: selected ? 'var(--color-primary-alpha)' : 'var(--color-bg-surface-default)',
        cursor: 'pointer',
      }}
    >
      <div onClick={(e) => e.stopPropagation()} style={{ display: 'flex' }}>
        <Checkbox size="sm" checked={selected} onChange={() => onToggleSelect(product.id)} aria-label={`Select ${product.name}`} />
      </div>
      {product.hasImages && product.thumbUrl ? (
        <img
          src={product.thumbUrl}
          alt={product.name}
          loading="lazy"
          style={{ width: 28, height: 28, borderRadius: 'var(--radius-sm)', objectFit: 'cover', flexShrink: 0 }}
          onError={(e) => {
            e.currentTarget.style.display = 'none';
          }}
        />
      ) : (
        <Skeleton variant="rounded" width={28} height={28} />
      )}
      <span
        style={{
          flex: 2,
          minWidth: 0,
          fontSize: 'var(--text-body-sm)',
          fontWeight: 'var(--weight-medium)',
          color: 'var(--color-text-primary)',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          whiteSpace: 'nowrap',
        }}
      >
        {product.name}
      </span>
      <span style={{ flex: 1, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
        {categoryNameById[product.categoryId]}
      </span>
      <span style={{ flex: 1, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
        {brandNameById[product.brandId]}
      </span>
      <span style={{ flexShrink: 0 }}>
        <StatusBadge status={STOCK_LABELS[product.stockStatus]} variant={stockVariant[product.stockStatus]} />
      </span>
      <span style={{ flexShrink: 0 }}>
        <LifecycleBadge state={product.lifecycleState} />
      </span>
      <span style={{ flexShrink: 0, width: 80, textAlign: 'right', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
        {formatPrice(product.pricing.sellingPrice)}
      </span>
      <button
        type="button"
        aria-label={`Preview ${product.name}`}
        onClick={(e) => {
          e.stopPropagation();
          onPreview(product);
        }}
        style={{ flexShrink: 0, background: 'transparent', border: 'none', cursor: 'pointer', color: 'var(--color-text-secondary)', display: 'inline-flex', padding: 4 }}
      >
        <Icon name="eye" size={15} />
      </button>
    </div>
  );
});

export const ProductCompactView: React.FC<CardViewProps> = memo(function ProductCompactView({ products, selectedIds, onToggleSelect, onPreview }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
      {products.map((p) => (
        <CompactRow key={p.id} product={p} selected={selectedIds.has(p.id)} onToggleSelect={onToggleSelect} onPreview={onPreview} />
      ))}
    </div>
  );
});

export default ProductCompactView;
