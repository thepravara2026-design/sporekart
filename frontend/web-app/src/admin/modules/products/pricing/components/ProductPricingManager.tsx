import React from 'react';
import type { PricingEntity } from '../types';
import { PricingTable } from './PricingTable';
import { PriceCard } from './PriceCard';

interface ProductPricingManagerProps {
  entities: PricingEntity[];
  selectedId: string | null;
  onSelect: (id: string) => void;
  viewMode?: 'table' | 'card';
  onViewModeChange?: (mode: 'table' | 'card') => void;
}

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const headerRow: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const toggleBtn: React.CSSProperties = {
  padding: '6px 12px',
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  color: 'var(--color-text-secondary)',
  fontSize: 'var(--text-body-xs)',
  cursor: 'pointer',
};

const cardGrid: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
  gap: 12,
};

export const ProductPricingManager: React.FC<ProductPricingManagerProps> = React.memo(
  ({ entities, selectedId, onSelect, viewMode = 'table', onViewModeChange }) => {
    return (
      <div style={sectionStyle}>
        <div style={headerRow}>
          <h2 style={h2Style}>Product Pricing</h2>
          {onViewModeChange && (
            <button
              style={toggleBtn}
              onClick={() => onViewModeChange(viewMode === 'table' ? 'card' : 'table')}
            >
              {viewMode === 'table' ? 'Card View' : 'Table View'}
            </button>
          )}
        </div>

        {entities.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
            No products with pricing found
          </div>
        ) : viewMode === 'table' ? (
          <PricingTable entities={entities} selectedId={selectedId} onSelect={onSelect} />
        ) : (
          <div style={cardGrid}>
            {entities.map((e) => (
              <PriceCard key={e.id} entity={e} isSelected={e.id === selectedId} onSelect={onSelect} />
            ))}
          </div>
        )}
      </div>
    );
  }
);
