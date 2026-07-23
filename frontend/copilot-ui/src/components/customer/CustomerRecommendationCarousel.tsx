import React, { useRef, useState } from 'react';
import { ProductRecommendation } from '../../types/customer';
import CustomerProductCard from './CustomerProductCard';

interface CustomerRecommendationCarouselProps {
  recommendations: ProductRecommendation[];
  onProductSelect?: (recommendation: ProductRecommendation) => void;
  onShowMore?: () => void;
}

export default function CustomerRecommendationCarousel({
  recommendations,
  onProductSelect,
  onShowMore,
}: CustomerRecommendationCarouselProps) {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [canScrollLeft, setCanScrollLeft] = useState(false);
  const [canScrollRight, setCanScrollRight] = useState(true);

  function updateScrollState() {
    const el = scrollRef.current;
    if (!el) return;
    setCanScrollLeft(el.scrollLeft > 0);
    setCanScrollRight(el.scrollLeft + el.clientWidth < el.scrollWidth - 1);
  }

  function scrollLeft() {
    scrollRef.current?.scrollBy({ left: -300, behavior: 'smooth' });
  }

  function scrollRight() {
    scrollRef.current?.scrollBy({ left: 300, behavior: 'smooth' });
  }

  if (!recommendations || recommendations.length === 0) {
    return null;
  }

  return (
    <div style={{ fontFamily: 'system-ui, sans-serif' }}>
      {recommendations.length > 0 && recommendations[0].reason && (
        <p style={{ fontSize: '13px', color: '#6b7280', marginBottom: '8px', fontStyle: 'italic' }}>
          {recommendations[0].reason}
        </p>
      )}

      <div style={{ position: 'relative' }}>
        {canScrollLeft && (
          <button
            onClick={scrollLeft}
            style={{
              position: 'absolute',
              left: '-12px',
              top: '50%',
              transform: 'translateY(-50%)',
              zIndex: 10,
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              border: '1px solid #e5e7eb',
              background: '#fff',
              cursor: 'pointer',
              boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
              fontSize: '16px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#374151',
            }}
            aria-label="Scroll left"
          >
            ‹
          </button>
        )}

        <div
          ref={scrollRef}
          onScroll={updateScrollState}
          style={{
            display: 'flex',
            gap: '12px',
            overflowX: 'auto',
            scrollBehavior: 'smooth',
            padding: '8px 4px',
            scrollbarWidth: 'thin',
          }}
        >
          {recommendations.map((rec) => (
            <div
              key={rec.product.id}
              onClick={() => onProductSelect?.(rec)}
              style={{ flexShrink: 0, cursor: onProductSelect ? 'pointer' : 'default' }}
            >
              <CustomerProductCard
                product={rec.product}
                onAddToCart={(p) => onProductSelect?.({ ...rec, product: p })}
                onViewDetails={(p) => onProductSelect?.({ ...rec, product: p })}
              />
            </div>
          ))}
        </div>

        {canScrollRight && (
          <button
            onClick={scrollRight}
            style={{
              position: 'absolute',
              right: '-12px',
              top: '50%',
              transform: 'translateY(-50%)',
              zIndex: 10,
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              border: '1px solid #e5e7eb',
              background: '#fff',
              cursor: 'pointer',
              boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
              fontSize: '16px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#374151',
            }}
            aria-label="Scroll right"
          >
            ›
          </button>
        )}
      </div>

      {onShowMore && (
        <div style={{ textAlign: 'center', marginTop: '8px' }}>
          <button
            onClick={onShowMore}
            style={{
              background: 'none',
              border: 'none',
              color: '#2563eb',
              fontWeight: 600,
              fontSize: '13px',
              cursor: 'pointer',
              padding: '4px 12px',
            }}
          >
            Show more recommendations →
          </button>
        </div>
      )}
    </div>
  );
}
