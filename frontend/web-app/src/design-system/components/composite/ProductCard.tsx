import React from 'react';
import { Card } from './Card';

export interface ProductCardProps {
  title: string;
  price: string | number;
  originalPrice?: string | number;
  image?: string;
  rating?: number;
  badge?: string;
  onAddToCart?: () => void;
  loading?: boolean;
}

export const ProductCard: React.FC<ProductCardProps> = ({
  title,
  price,
  originalPrice,
  image,
  rating,
  badge,
  onAddToCart,
  loading = false,
}) => {
  return (
    <Card variant="default" padding="none" loading={loading}>
      <div
        className="sk-productcard__image-wrapper"
        style={{
          position: 'relative',
          overflow: 'hidden',
          borderRadius: 'var(--radius-card) var(--radius-card) 0 0',
        }}
      >
        {image ? (
          <img
            src={image}
            alt={title}
            className="sk-productcard__image"
            style={{
              width: '100%',
              height: 200,
              objectFit: 'cover',
              display: 'block',
            }}
          />
        ) : (
          <div
            className="sk-productcard__image-placeholder"
            style={{
              width: '100%',
              height: 200,
              background: 'var(--color-bg-skeleton-base)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--color-text-disabled)',
              fontSize: 'var(--text-caption)',
            }}
          >
            No Image
          </div>
        )}
        {badge && (
          <span
            className="sk-productcard__badge"
            style={{
              position: 'absolute',
              top: 'var(--space-2)',
              left: 'var(--space-2)',
              background: 'var(--color-bg-primary-default)',
              color: 'var(--color-text-on-primary)',
              fontSize: 'var(--text-caption)',
              fontWeight: 'var(--weight-semibold)',
              padding: '2px var(--space-2)',
              borderRadius: 'var(--radius-tag)',
            }}
          >
            {badge}
          </span>
        )}
      </div>
      <div
        className="sk-productcard__body"
        style={{
          padding: 'var(--space-3)',
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
        }}
      >
        <h3
          className="sk-productcard__title"
          style={{
            fontSize: 'var(--text-body)',
            fontWeight: 'var(--weight-medium)',
            color: 'var(--color-text-primary)',
            margin: 0,
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
          }}
        >
          {title}
        </h3>
        <div
          className="sk-productcard__pricing"
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-sm)',
          }}
        >
          <span
            className="sk-productcard__price"
            style={{
              fontSize: 'var(--text-h5)',
              fontWeight: 'var(--weight-bold)',
              color: 'var(--color-text-primary)',
            }}
          >
            {price}
          </span>
          {originalPrice && (
            <span
              className="sk-productcard__original-price"
              style={{
                fontSize: 'var(--text-body-sm)',
                color: 'var(--color-text-disabled)',
                textDecoration: 'line-through',
              }}
            >
              {originalPrice}
            </span>
          )}
        </div>
        {rating != null && (
          <div
            className="sk-productcard__rating"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-inline-xs)',
              fontSize: 'var(--text-caption)',
              color: 'var(--color-text-secondary)',
            }}
          >
            <span style={{ color: 'var(--color-warning)' }}>{'\u2605'}</span>
            <span>{rating}</span>
          </div>
        )}
        {onAddToCart && (
          <button
            className="sk-productcard__add-to-cart"
            style={{
              width: '100%',
              padding: 'var(--space-2) var(--space-3)',
              background: 'var(--color-bg-primary-default)',
              color: 'var(--color-text-on-primary)',
              border: 'none',
              borderRadius: 'var(--radius-btn)',
              fontWeight: 'var(--weight-semibold)',
              fontSize: 'var(--text-button)',
              cursor: 'pointer',
              marginTop: 'var(--space-stack-xs)',
              transition: 'all var(--duration-fast) var(--easing-standard)',
            }}
            onClick={onAddToCart}
          >
            Add to Cart
          </button>
        )}
      </div>
    </Card>
  );
};

ProductCard.displayName = 'ProductCard';

export default ProductCard;
