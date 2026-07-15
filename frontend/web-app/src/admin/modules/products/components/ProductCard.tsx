import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { LifecycleBadge } from './LifecycleBadge';
import type { Product } from '../types';

interface ProductCardProps {
  product: Product;
  onClick?: (product: Product) => void;
}

export const ProductCard = React.memo(function ProductCard({ product, onClick }: ProductCardProps) {
  const thumbnail = product.media.thumbnail ?? product.media.images[0];

  return (
    <Card
      variant="default"
      padding="sm"
      hoverable={!!onClick}
      onClick={onClick ? () => onClick(product) : undefined}
      aria-label={`Product ${product.name}`}
      style={{ minWidth: 0 }}
    >
      <div style={{ position: 'relative', width: '100%', aspectRatio: '4 / 3', borderRadius: 'var(--radius-md)', overflow: 'hidden', marginBottom: 'var(--space-stack-xs)' }}>
        {thumbnail ? (
          <img
            src={thumbnail}
            alt={product.name}
            style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
            onError={(e) => {
              (e.currentTarget.style.display = 'none');
            }}
          />
        ) : (
          <Skeleton variant="rounded" width="100%" height="100%" />
        )}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-inline-xs)' }}>
        <LifecycleBadge state={product.lifecycleState} />
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{product.unit}</span>
      </div>
      <h3 style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', margin: 'var(--space-stack-xs) 0 2px', fontWeight: 'var(--weight-semibold)' }}>{product.name}</h3>
      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>SKU: {product.sku}</p>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', margin: 'var(--space-stack-xs) 0 0', fontWeight: 'var(--weight-bold)' }}>
        ₹{product.pricing.sellingPrice}
        {product.pricing.mrp > product.pricing.sellingPrice && (
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textDecoration: 'line-through', marginLeft: 6 }}>₹{product.pricing.mrp}</span>
        )}
      </p>
    </Card>
  );
});

export default ProductCard;
