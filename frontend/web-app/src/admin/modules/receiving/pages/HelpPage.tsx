const FAQ_ITEMS = [
  { q: 'How do I create a goods receipt?', a: 'Click "+ New Receipt" in the workspace header. Fill in supplier, product, quantity, warehouse, and receiving zone. The receipt is created in "draft" status.' },
  { q: 'What is the receiving workflow?', a: 'Receipt Created → Goods Arrived → Inspection → Acceptance/Rejection → Batch Assignment → Warehouse Allocation → Inventory Activation → Completed.' },
  { q: 'How does inspection work?', a: 'Navigate to the Inspection section. Each receipt can have visual, quality, packaging, quantity, and documentation checks. Inspectors mark each check as pass/fail.' },
  { q: 'What happens when goods are rejected?', a: 'Rejected goods are flagged with a reason (damage, expiry, packaging failure, etc.). Options include return to supplier, disposal, or re-inspection.' },
  { q: 'How is warehouse allocation managed?', a: 'Each receipt can be allocated to a warehouse zone, rack, shelf, and bin. Storage types include ambient, cold storage, dry, and hazardous.' },
  { q: 'How are batches assigned?', a: 'In the Batch Assignment section, receipts can be linked to existing batches or trigger new batch creation. Each assignment includes lot code, expiry date, and shelf life.' },
  { q: 'What validation rules are checked?', a: '9 rules: duplicate receipts, missing warehouse/product/inventory item, missing inspection, missing acceptance, missing batch, missing allocation, invalid status transitions.' },
  { q: 'Can I view the audit trail?', a: 'Yes. The Timeline section shows the event history for each receipt. The Audit section shows field-level changes.' },
];

import { memo } from 'react';

export const HelpPage = memo(function HelpPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Help & FAQ</h3>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
        {FAQ_ITEMS.map((item, idx) => (
          <details key={idx} style={{ borderBottom: idx < FAQ_ITEMS.length - 1 ? '1px solid var(--color-border)' : 'none' }}>
            <summary style={{ padding: '14px 16px', cursor: 'pointer', fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{item.q}</summary>
            <p style={{ padding: '0 16px 14px', margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>{item.a}</p>
          </details>
        ))}
      </div>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <h4 style={{ margin: 0, fontSize: 'var(--text-h4, 16px)', fontWeight: 600 }}>Need more help?</h4>
        <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Contact your system administrator or refer to the documentation in <code style={{ background: 'var(--color-surface-hover)', padding: '2px 6px', borderRadius: 3 }}>docs/phase-10/</code>.</p>
      </div>
    </div>
  );
});
