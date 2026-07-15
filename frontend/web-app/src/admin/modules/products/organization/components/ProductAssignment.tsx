import React, { useState } from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import Button from '../../../../../design-system/components/core/Button';
import { MOCK_CATEGORIES } from '../mock/mockCategories';
import { MOCK_COLLECTIONS } from '../mock/mockCollections';
import { MOCK_BRANDS } from '../mock/mockBrands';
import { MOCK_TAGS } from '../mock/mockTags';

const MOCK_PRODUCTS = [
  { id: 'PRD-001', name: 'Organic White Mushrooms', sku: 'SKU-WHITE-001', categories: ['cat-fresh-white'], collections: ['org-col-004'], brands: ['brd-001'], tags: ['tag-001', 'tag-002', 'tag-021'] },
  { id: 'PRD-002', name: 'Fresh Shiitake Mushrooms', sku: 'SKU-SHII-001', categories: ['cat-fresh-shiitake'], collections: ['org-col-006'], brands: ['brd-002'], tags: ['tag-002', 'tag-004', 'tag-006'] },
  { id: 'PRD-003', name: 'Enoki Mushroom 200g', sku: 'SKU-ENOKI-001', categories: ['cat-fresh-enoki'], collections: ['org-col-004'], brands: ['brd-001'], tags: ['tag-002', 'tag-008'] },
  { id: 'PRD-004', name: 'Home Growing Kit', sku: 'SKU-KIT-001', categories: ['cat-spawn-kit'], collections: ['org-col-009'], brands: ['brd-005'], tags: ['tag-005', 'tag-022'] },
  { id: 'PRD-005', name: 'King Oyster Mushroom', sku: 'SKU-KING-001', categories: ['cat-fresh-king'], collections: ['org-col-014'], brands: ['brd-003'], tags: ['tag-002', 'tag-006', 'tag-025'] },
];

export const ProductAssignment = React.memo(function ProductAssignment() {
  const [selectedProduct, setSelectedProduct] = useState<string | null>(null);
  const [mode, setMode] = useState<'single' | 'bulk'>('single');

  const product = selectedProduct ? MOCK_PRODUCTS.find((p) => p.id === selectedProduct) ?? null : null;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
        <div style={toggleGroupStyle}>
          <button type="button" onClick={() => setMode('single')} style={toggleBtnStyle(mode === 'single')}>Single Assignment</button>
          <button type="button" onClick={() => setMode('bulk')} style={toggleBtnStyle(mode === 'bulk')}>Bulk Assignment</button>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
        <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
          <div style={{ padding: '8px 12px', borderBottom: '1px solid var(--color-border-default)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>
            Select Product
          </div>
          {MOCK_PRODUCTS.map((p) => (
            <button
              key={p.id}
              type="button"
              onClick={() => setSelectedProduct(p.id)}
              style={{
                display: 'flex', flexDirection: 'column', gap: 2,
                padding: '8px 12px', border: 'none', borderBottom: '1px solid var(--color-border-weak)',
                background: selectedProduct === p.id ? 'var(--color-primary-alpha)' : 'transparent',
                cursor: 'pointer', textAlign: 'left', fontFamily: 'var(--font-family-sans)', width: '100%',
              }}
            >
              <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)' }}>{p.name}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{p.sku}</span>
            </button>
          ))}
        </div>

        <div>
          {product ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
              <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
                <h4 style={{ margin: '0 0 4px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{product.name}</h4>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{product.sku} · ID: {product.id}</span>
              </div>

              <AssignmentSection title="Categories" items={product.categories} allItems={MOCK_CATEGORIES.map((c) => ({ id: c.id, name: c.name }))} />
              <AssignmentSection title="Collections" items={product.collections} allItems={MOCK_COLLECTIONS.map((c) => ({ id: c.id, name: c.name }))} />
              <AssignmentSection title="Brands" items={product.brands} allItems={MOCK_BRANDS.map((b) => ({ id: b.id, name: b.name }))} />
              <AssignmentSection title="Tags" items={product.tags} allItems={MOCK_TAGS.map((t) => ({ id: t.id, name: t.name }))} />

              {mode === 'bulk' && (
                <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)' }}>
                  <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', display: 'flex', alignItems: 'center', gap: 8 }}>
                    <Icon name="info" size={16} />
                    Bulk mode: Changes would apply to all selected products. Mock only — no backend execution.
                  </p>
                </div>
              )}
            </div>
          ) : (
            <div style={{ padding: 'var(--space-12)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>
              <Icon name="link" size={48} style={{ marginBottom: 8 }} />
              <p>Select a product to view and manage its assignments</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
});

function AssignmentSection({ title, items, allItems }: { title: string; items: string[]; allItems: { id: string; name: string }[] }) {
  const [editing, setEditing] = useState(false);
  const unassigned = allItems.filter((i) => !items.includes(i.id));

  return (
    <div style={{ padding: 'var(--space-3) var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
        <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{title}</span>
        <Button variant="ghost" size="sm" onClick={() => setEditing(!editing)}>
          {editing ? 'Done' : 'Edit'}
        </Button>
      </div>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
        {items.length > 0 ? (items.map((id) => {
          const item = allItems.find((i) => i.id === id);
          return <span key={id} style={{ padding: '2px 8px', borderRadius: 'var(--radius-full)', background: 'var(--color-bg-primary-weak)', fontSize: 'var(--text-caption)', color: 'var(--color-primary)' }}>{item?.name ?? id}</span>;
        })) : (
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>None assigned</span>
        )}
      </div>
      {editing && unassigned.length > 0 && (
        <div style={{ marginTop: 8, borderTop: '1px solid var(--color-border-weak)', paddingTop: 8 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Available to assign:</span>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
            {unassigned.map((item) => (
              <button
                key={item.id}
                type="button"
                style={{
                  padding: '2px 8px', borderRadius: 'var(--radius-full)', border: '1px dashed var(--color-border-default)',
                  background: 'transparent', cursor: 'pointer', fontFamily: 'var(--font-family-sans)',
                  fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
                }}
                disabled
                title="Mock Mode — assignment disabled"
              >
                + {item.name}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

const toggleGroupStyle: React.CSSProperties = {
  display: 'inline-flex', borderRadius: 'var(--radius-input)', overflow: 'hidden', border: '1px solid var(--color-border-default)',
};

const toggleBtnStyle = (active: boolean): React.CSSProperties => ({
  padding: '6px 14px', border: 'none', cursor: 'pointer', fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body-sm)', fontWeight: active ? 'var(--weight-semibold)' : 'var(--weight-normal)',
  background: active ? 'var(--color-primary)' : 'var(--color-bg-surface-default)',
  color: active ? '#fff' : 'var(--color-text-secondary)',
});

export default ProductAssignment;
