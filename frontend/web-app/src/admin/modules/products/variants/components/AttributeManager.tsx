import React from 'react';
import type { AttributeDefinition, AttributeGroup } from '../types';
import { ATTRIBUTE_GROUP_LABELS } from '../types';
import { getAttributesByGroup } from '../mock/mockAttributes';

interface AttributeManagerProps {
  definitions: AttributeDefinition[];
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };

export const AttributeManager: React.FC<AttributeManagerProps> = React.memo(({ definitions }) => {
  const byGroup = getAttributesByGroup();
  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Attribute Definitions ({definitions.length})
      </h2>
      {Object.entries(byGroup).map(([group, attrs]) => (
        <div key={group}>
          <h3 style={{ fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600, margin: '16px 0 8px' }}>
            {ATTRIBUTE_GROUP_LABELS[group as AttributeGroup] ?? group}
          </h3>
          <div style={grid}>
            {attrs.map((a) => (
              <div key={a.id} style={card}>
                <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{a.label}</div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>{a.name}</div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4, marginTop: 8 }}>
                  <Tag>{a.type}</Tag>
                  {a.required && <Tag color="var(--color-accent-orange)">Required</Tag>}
                  {a.filterable && <Tag color="var(--color-accent-blue)">Filterable</Tag>}
                  {a.comparable && <Tag color="var(--color-accent-green)">Comparable</Tag>}
                </div>
                {a.options && a.options.length > 0 && (
                  <div style={{ marginTop: 8, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                    Options: {a.options.join(', ')}
                  </div>
                )}
                {a.description && (
                  <div style={{ marginTop: 6, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>
                    {a.description}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
});

function Tag({ children, color }: { children: React.ReactNode; color?: string }) {
  const c = color ?? 'var(--color-accent-gray)';
  return (
    <span style={{ padding: '2px 6px', borderRadius: 8, fontSize: 'var(--text-body-xs)', fontWeight: 600, background: `${c}20`, color: c }}>
      {children}
    </span>
  );
}
