const FAQ_ITEMS = [
  { q: 'What is a movement?', a: 'A movement is any inventory change — receipt, issue, transfer, adjustment, etc. Each movement type tracks the item, quantity, warehouse, and status.' },
  { q: 'How do I create a goods receipt?', a: 'Navigate to Goods Receipt in the sidebar and click "+ New Movement". Fill in product, quantity, warehouse, and supplier details.' },
  { q: 'How are adjustments recorded?', a: 'Adjustments track the previous vs. new quantity with a reason code. All adjustments are logged in the audit trail.' },
  { q: 'Can I export transaction data?', a: 'Yes. Use the Export button on any page or go to Reports to generate a comprehensive report in CSV, Excel, or PDF.' },
  { q: 'What validation rules are enforced?', a: 'See the Validation section for the full list. Key rules include quantity non-negative, warehouse existence, and duplicate reference checks.' },
  { q: 'How do I view the audit trail?', a: 'The Audit Trail page shows every action with timestamp, user, IP, and details. Records are read-only.' },
  { q: 'What permissions control movement actions?', a: 'Permissions are role-based: View (read-only), Edit (create/edit), Approve (approve/reject), Admin (full access).' },
  { q: 'How are transfers tracked?', a: 'Transfers move stock between warehouses. Each transfer creates an outgoing issue and incoming receipt, both visible in the timeline.' },
];

import { memo } from 'react';

export const HelpPage = memo(function HelpPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Help & FAQ</h3>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
        {FAQ_ITEMS.map((item, idx) => (
          <details key={idx} style={{ borderBottom: idx < FAQ_ITEMS.length - 1 ? '1px solid var(--color-border)' : 'none' }}>
            <summary style={{ padding: '14px 16px', cursor: 'pointer', fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>
              {item.q}
            </summary>
            <p style={{ padding: '0 16px 14px', margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>
              {item.a}
            </p>
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
