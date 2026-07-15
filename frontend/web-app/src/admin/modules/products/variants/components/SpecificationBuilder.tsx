import React from 'react';
import type { Specification, AttributeGroup } from '../types';
import { ATTRIBUTE_GROUP_LABELS } from '../types';

interface SpecificationBuilderProps {
  specifications: Record<string, Specification[]>;
  selectedVariantId: string | null;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };

export const SpecificationBuilder: React.FC<SpecificationBuilderProps> = React.memo(({ specifications, selectedVariantId }) => {
  const specs = selectedVariantId ? specifications[selectedVariantId] : [];
  const byGroup: Record<string, Specification[]> = {};
  for (const s of specs ?? []) {
    if (!byGroup[s.group]) byGroup[s.group] = [];
    byGroup[s.group].push(s);
  }

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Specifications
      </h2>
      {!selectedVariantId ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
          Select a variant to view specifications
        </div>
      ) : specs && specs.length > 0 ? (
        Object.entries(byGroup).map(([group, groupSpecs]) => (
          <div key={group}>
            <h3 style={{ fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600, margin: '16px 0 8px' }}>
              {ATTRIBUTE_GROUP_LABELS[group as AttributeGroup] ?? group} Specifications
            </h3>
            <div style={grid}>
              {groupSpecs.map((s) => (
                <div key={s.id} style={card}>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>{s.name}</div>
                  <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{s.value}</div>
                </div>
              ))}
            </div>
          </div>
        ))
      ) : (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
          No specifications defined for this variant
        </div>
      )}
    </div>
  );
});
