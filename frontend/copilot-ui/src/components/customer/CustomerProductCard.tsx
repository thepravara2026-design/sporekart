import React from 'react';
import { ProductItem } from '../../types/customer';

interface CustomerProductCardProps {
  product: ProductItem;
  onAddToCart?: (product: ProductItem) => void;
  onViewDetails?: (product: ProductItem) => void;
}

const statusColors: Record<string, string> = {
  Available: '#22c55e',
  'Low Stock': '#f59e0b',
  'Out of Stock': '#ef4444',
};

function getStockStatus(product: ProductItem): { label: string; color: string } {
  if (!product.isAvailable) return { label: 'Out of Stock', color: statusColors['Out of Stock'] };
  if (product.stockLevel <= 0) return { label: 'Out of Stock', color: statusColors['Out of Stock'] };
  if (product.stockLevel < 10) return { label: 'Low Stock', color: statusColors['Low Stock'] };
  return { label: 'Available', color: statusColors['Available'] };
}

function renderStars(rating: number): string {
  const full = Math.floor(rating);
  const half = rating - full >= 0.5 ? 1 : 0;
  const empty = 5 - full - half;
  return '★'.repeat(full) + (half ? '½' : '') + '☆'.repeat(empty);
}

export default function CustomerProductCard({ product, onAddToCart, onViewDetails }: CustomerProductCardProps) {
  const stock = getStockStatus(product);

  return (
    <div style={{
      border: '1px solid #e5e7eb',
      borderRadius: '8px',
      overflow: 'hidden',
      background: '#fff',
      maxWidth: '280px',
      fontFamily: 'system-ui, sans-serif',
    }}>
      <div style={{
        height: '160px',
        background: '#f3f4f6',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#9ca3af',
        fontSize: '14px',
      }}>
        {product.imageUrl ? (
          <img src={product.imageUrl} alt={product.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
        ) : (
          <span>No Image</span>
        )}
      </div>

      <div style={{ padding: '12px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '6px' }}>
          <span style={{
            background: '#dbeafe',
            color: '#1d4ed8',
            fontSize: '11px',
            padding: '2px 8px',
            borderRadius: '4px',
            fontWeight: 600,
          }}>
            {product.category}
          </span>
          <span style={{ fontSize: '13px', color: '#f59e0b' }}>
            {renderStars(product.rating)} {product.rating.toFixed(1)}
          </span>
        </div>

        <h3 style={{ margin: '0 0 4px', fontSize: '15px', fontWeight: 600, color: '#111827' }}>
          {product.name}
        </h3>

        <p style={{ margin: '0 0 8px', fontSize: '13px', color: '#6b7280', lineHeight: '1.4' }}>
          {product.description}
        </p>

        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '8px' }}>
          <span style={{ fontSize: '18px', fontWeight: 700, color: '#111827' }}>
            {product.currency} {product.price.toFixed(2)}
          </span>
          <span style={{ fontSize: '12px', color: stock.color, fontWeight: 500 }}>
            {stock.label}
          </span>
        </div>

        <div style={{ display: 'flex', gap: '8px' }}>
          {onAddToCart && (
            <button
              onClick={() => onAddToCart(product)}
              disabled={!product.isAvailable || product.stockLevel <= 0}
              style={{
                flex: 1,
                padding: '8px 12px',
                border: 'none',
                borderRadius: '6px',
                background: !product.isAvailable || product.stockLevel <= 0 ? '#d1d5db' : '#2563eb',
                color: '#fff',
                fontWeight: 600,
                fontSize: '13px',
                cursor: !product.isAvailable || product.stockLevel <= 0 ? 'not-allowed' : 'pointer',
              }}
            >
              Add to Cart
            </button>
          )}
          {onViewDetails && (
            <button
              onClick={() => onViewDetails(product)}
              style={{
                padding: '8px 12px',
                border: '1px solid #d1d5db',
                borderRadius: '6px',
                background: '#fff',
                color: '#374151',
                fontWeight: 500,
                fontSize: '13px',
                cursor: 'pointer',
              }}
            >
              View Details
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
