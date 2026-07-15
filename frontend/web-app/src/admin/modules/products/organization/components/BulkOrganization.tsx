import React, { useState } from 'react';
import Button from '../../../../../design-system/components/core/Button';
import Icon from '../../../../../design-system/icons/Icon';

const operationGroups = [
  {
    label: 'Assignments', operations: [
      { id: 'assign-category', label: 'Assign Category', icon: 'tag', description: 'Assign selected entities to a category' },
      { id: 'assign-collection', label: 'Assign Collection', icon: 'bookmark', description: 'Add to a collection' },
      { id: 'assign-brand', label: 'Assign Brand', icon: 'shield', description: 'Assign a brand' },
      { id: 'assign-tags', label: 'Assign Tags', icon: 'pricetag', description: 'Add tags to selected entities' },
    ],
  },
  {
    label: 'Organization', operations: [
      { id: 'move', label: 'Move', icon: 'move', description: 'Move to a different parent/location' },
      { id: 'remove', label: 'Remove Assignments', icon: 'trash-2', description: 'Remove existing assignments' },
      { id: 'replace', label: 'Replace', icon: 'refresh-cw', description: 'Replace current assignments with new ones' },
    ],
  },
  {
    label: 'Management', operations: [
      { id: 'merge-categories', label: 'Merge Categories', icon: 'git-merge', description: 'Combine two or more categories' },
      { id: 'merge-brands', label: 'Merge Brands', icon: 'git-merge', description: 'Combine two or more brands' },
      { id: 'merge-tags', label: 'Merge Tags', icon: 'git-merge', description: 'Combine two or more tags' },
      { id: 'archive', label: 'Archive', icon: 'archive', description: 'Archive selected entities' },
      { id: 'restore', label: 'Restore', icon: 'rotate-ccw', description: 'Restore from archive' },
    ],
  },
];

export const BulkOrganization = React.memo(function BulkOrganization() {
  const [selectedOp, setSelectedOp] = useState<string | null>(null);
  const [confirming, setConfirming] = useState(false);

  if (confirming && selectedOp) {
    const op = operationGroups.flatMap((g) => g.operations).find((o) => o.id === selectedOp);
    return (
      <div style={{ maxWidth: 480, margin: '0 auto', padding: 'var(--space-8)', textAlign: 'center' }}>
        <Icon name="check-circle" size={48} style={{ color: 'var(--color-success)', marginBottom: 16 }} />
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)' }}>Mock Action: {op?.label}</h3>
        <p style={{ margin: '0 0 16px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
          This would {op?.description?.toLowerCase()} in production. No data was modified.
        </p>
        <Button variant="primary" onClick={() => { setConfirming(false); setSelectedOp(null); }}>
          Done
        </Button>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-warning-weak)' }}>
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', display: 'flex', alignItems: 'center', gap: 8 }}>
          <Icon name="info" size={16} style={{ color: 'var(--color-warning)' }} />
          Bulk operations are in Mock Mode. No data will be modified. Select an operation below to preview its behavior.
        </p>
      </div>

      {operationGroups.map((group) => (
        <div key={group.label}>
          <h4 style={{ margin: '0 0 8px', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)' }}>{group.label}</h4>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {group.operations.map((op) => {
              const isActive = selectedOp === op.id;
              return (
                <button
                  key={op.id}
                  type="button"
                  onClick={() => { setSelectedOp(op.id); setConfirming(true); }}
                  style={{
                    display: 'flex', flexDirection: 'column', alignItems: 'flex-start', gap: 6,
                    padding: 'var(--space-4)', borderRadius: 'var(--radius-card)',
                    border: isActive ? '2px solid var(--color-primary)' : '1px solid var(--color-border-default)',
                    background: 'var(--color-bg-surface-default)', cursor: 'pointer',
                    textAlign: 'left', fontFamily: 'var(--font-family-sans)',
                    transition: 'border-color var(--duration-fast) var(--easing-standard)',
                  }}
                >
                  <Icon name={op.icon} size={20} style={{ color: 'var(--color-primary)' }} />
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{op.label}</span>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{op.description}</span>
                </button>
              );
            })}
          </div>
        </div>
      ))}
    </div>
  );
});

export default BulkOrganization;
