import React, { useState } from 'react';
import type { BulkVariantOperation } from '../types';

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 };
const opCard: React.CSSProperties = { padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', textAlign: 'center', transition: 'border-color 0.15s, box-shadow 0.15s' };

const BULK_OPS: BulkVariantOperation[] = [
  { id: 'bvo-1', name: 'Create Variants', type: 'create_variants', description: 'Create multiple variants from a template', icon: '➕' },
  { id: 'bvo-2', name: 'Delete Variants', type: 'delete_variants', description: 'Remove selected variants', icon: '🗑️' },
  { id: 'bvo-3', name: 'Duplicate Variants', type: 'duplicate_variants', description: 'Copy selected variants', icon: '📋' },
  { id: 'bvo-4', name: 'Assign Packaging', type: 'assign_packaging', description: 'Assign packaging to variants', icon: '📦' },
  { id: 'bvo-5', name: 'Assign Attributes', type: 'assign_attributes', description: 'Bulk attribute assignment', icon: '🏷️' },
  { id: 'bvo-6', name: 'Generate SKU', type: 'generate_sku', description: 'Auto-generate SKUs for variants', icon: '🔢' },
  { id: 'bvo-7', name: 'Export', type: 'export', description: 'Export variant data', icon: '📤' },
  { id: 'bvo-8', name: 'Archive', type: 'archive', description: 'Archive selected variants', icon: '📁' },
  { id: 'bvo-9', name: 'Restore', type: 'restore', description: 'Restore archived variants', icon: '↩️' },
];

const opGroups = [
  { label: 'Variant Creation', ops: BULK_OPS.slice(0, 3) },
  { label: 'Assignment', ops: BULK_OPS.slice(3, 6) },
  { label: 'Data Management', ops: BULK_OPS.slice(6) },
];

export const BulkVariantOperations: React.FC = React.memo(() => {
  const [confirming, setConfirming] = useState<BulkVariantOperation | null>(null);

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Bulk Variant Operations</h2>
      {opGroups.map((g) => (
        <div key={g.label}>
          <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>{g.label}</div>
          <div style={grid}>
            {g.ops.map((op) => (
              <div key={op.id} style={opCard} onClick={() => setConfirming(op)} role="button" tabIndex={0}
                onKeyDown={(e) => { if (e.key === 'Enter') setConfirming(op); }}
                onMouseEnter={(e) => { e.currentTarget.style.borderColor = 'var(--color-accent-blue)'; e.currentTarget.style.boxShadow = '0 0 0 1px var(--color-accent-blue)'; }}
                onMouseLeave={(e) => { e.currentTarget.style.borderColor = 'var(--color-border)'; e.currentTarget.style.boxShadow = 'none'; }}>
                <div style={{ fontSize: 24, marginBottom: 4 }}>{op.icon}</div>
                <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{op.name}</div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{op.description}</div>
              </div>
            ))}
          </div>
        </div>
      ))}

      {confirming && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}
          onClick={() => setConfirming(null)}>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 24, width: '90%', maxWidth: 400, display: 'flex', flexDirection: 'column', gap: 12, textAlign: 'center' }}
            onClick={(e) => e.stopPropagation()}>
            <div style={{ fontSize: 32 }}>{confirming.icon}</div>
            <div style={{ fontSize: 'var(--text-h4)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{confirming.name}</div>
            <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{confirming.description}. This operation will affect selected variants.</div>
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>Mock mode — no changes will be persisted.</div>
            <div style={{ display: 'flex', gap: 8, justifyContent: 'center', marginTop: 8 }}>
              <button style={{ padding: '8px 20px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}
                onClick={() => setConfirming(null)}>Cancel</button>
              <button style={{ padding: '8px 20px', borderRadius: 'var(--radius-sm)', border: 'none', background: 'var(--color-accent-blue)', color: '#fff', fontWeight: 600, cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}
                onClick={() => setConfirming(null)}>Confirm</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
});
