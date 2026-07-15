import React, { useState } from 'react';
import type { BulkPricingOperation } from '../types';

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const groupStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 8,
};

const groupLabel: React.CSSProperties = {
  fontSize: 'var(--text-h5)',
  fontWeight: 600,
  color: 'var(--color-text-primary)',
  marginBottom: 4,
};

const opsGrid: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
  gap: 8,
};

const opCard: React.CSSProperties = {
  padding: 12,
  borderRadius: 'var(--radius-sm)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  cursor: 'pointer',
  textAlign: 'center',
  transition: 'border-color 0.15s, box-shadow 0.15s',
};

const opIcon: React.CSSProperties = {
  fontSize: 24,
  marginBottom: 4,
};

const opName: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  fontWeight: 600,
  color: 'var(--color-text-primary)',
};

const opDesc: React.CSSProperties = {
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-tertiary)',
  marginTop: 2,
};

const modalOverlay: React.CSSProperties = {
  position: 'fixed',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  background: 'rgba(0,0,0,0.4)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  zIndex: 1000,
};

const modalStyle: React.CSSProperties = {
  background: 'var(--color-bg-surface-default)',
  borderRadius: 'var(--radius-lg)',
  border: '1px solid var(--color-border)',
  padding: 24,
  width: '90%',
  maxWidth: 440,
  display: 'flex',
  flexDirection: 'column',
  gap: 12,
};

const BULK_OPERATIONS: BulkPricingOperation[] = [
  { id: 'bulk-update-price', name: 'Bulk Price Update', type: 'update_price', description: 'Update prices for multiple products', icon: '💰' },
  { id: 'bulk-apply-discount', name: 'Bulk Discount', type: 'apply_discount', description: 'Apply discount to multiple products', icon: '💯' },
  { id: 'bulk-update-gst', name: 'Bulk GST Update', type: 'update_gst', description: 'Update GST for multiple products', icon: '🧾' },
  { id: 'bulk-update-hsn', name: 'Bulk HSN Update', type: 'update_hsn', description: 'Update HSN codes for multiple products', icon: '📋' },
  { id: 'bulk-approve', name: 'Bulk Approve', type: 'approve', description: 'Approve multiple pending prices', icon: '✅' },
  { id: 'bulk-archive', name: 'Bulk Archive', type: 'archive', description: 'Archive outdated pricing', icon: '📦' },
  { id: 'bulk-publish', name: 'Bulk Publish', type: 'publish', description: 'Publish scheduled pricing', icon: '🚀' },
  { id: 'bulk-schedule', name: 'Bulk Schedule', type: 'schedule', description: 'Schedule multiple price changes', icon: '📅' },
];

const operationGroups = [
  { label: 'Price Assignments', operations: BULK_OPERATIONS.slice(0, 2) },
  { label: 'Tax & Classification', operations: BULK_OPERATIONS.slice(2, 4) },
  { label: 'Workflow Management', operations: BULK_OPERATIONS.slice(4) },
];

export const BulkPricing: React.FC = React.memo(() => {
  const [confirming, setConfirming] = useState<BulkPricingOperation | null>(null);

  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Bulk Pricing Operations</h2>
      {operationGroups.map((group) => (
        <div key={group.label} style={groupStyle}>
          <div style={groupLabel}>{group.label}</div>
          <div style={opsGrid}>
            {group.operations.map((op) => (
              <div
                key={op.id}
                style={opCard}
                onClick={() => setConfirming(op)}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => { if (e.key === 'Enter') setConfirming(op); }}
                onMouseEnter={(e) => { e.currentTarget.style.borderColor = 'var(--color-accent-blue)'; e.currentTarget.style.boxShadow = '0 0 0 1px var(--color-accent-blue)'; }}
                onMouseLeave={(e) => { e.currentTarget.style.borderColor = 'var(--color-border)'; e.currentTarget.style.boxShadow = 'none'; }}
              >
                <div style={opIcon}>{op.icon}</div>
                <div style={opName}>{op.name}</div>
                <div style={opDesc}>{op.description}</div>
              </div>
            ))}
          </div>
        </div>
      ))}

      {confirming && (
        <div style={modalOverlay} onClick={() => setConfirming(null)}>
          <div style={modalStyle} onClick={(e) => e.stopPropagation()}>
            <div style={{ fontSize: 32, textAlign: 'center' }}>{confirming.icon}</div>
            <div style={{ fontSize: 'var(--text-h4)', fontWeight: 600, color: 'var(--color-text-primary)', textAlign: 'center' }}>
              {confirming.name}
            </div>
            <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', textAlign: 'center' }}>
              {confirming.description}. This operation will affect all selected products.
            </div>
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textAlign: 'center', fontStyle: 'italic' }}>
              Mock mode — no changes will be persisted.
            </div>
            <div style={{ display: 'flex', gap: 8, justifyContent: 'center', marginTop: 8 }}>
              <button
                style={{
                  padding: '8px 20px',
                  borderRadius: 'var(--radius-sm)',
                  border: '1px solid var(--color-border)',
                  background: 'var(--color-bg-surface-default)',
                  color: 'var(--color-text-secondary)',
                  cursor: 'pointer',
                  fontSize: 'var(--text-body-sm)',
                }}
                onClick={() => setConfirming(null)}
              >
                Cancel
              </button>
              <button
                style={{
                  padding: '8px 20px',
                  borderRadius: 'var(--radius-sm)',
                  border: 'none',
                  background: 'var(--color-accent-blue)',
                  color: '#fff',
                  fontWeight: 600,
                  cursor: 'pointer',
                  fontSize: 'var(--text-body-sm)',
                }}
                onClick={() => setConfirming(null)}
              >
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
});
