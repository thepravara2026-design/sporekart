import { memo, useState, type CSSProperties } from 'react';
import { Dialog } from '../../../design-system/components/feedback/Dialog';

export const DialogModulePreview = memo(function DialogModulePreview() {
  const [open, setOpen] = useState(false);
  const triggerStyle: CSSProperties = {
    padding: 'var(--space-2) var(--space-4)',
    borderRadius: 'var(--radius-input)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-surface-default)',
    color: 'var(--color-text-primary)',
    cursor: 'pointer',
    fontSize: 'var(--text-body)',
  };
  const closeStyle: CSSProperties = {
    padding: 'var(--space-2) var(--space-4)',
    borderRadius: 'var(--radius-input)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-background)',
    color: 'var(--color-text-primary)',
    cursor: 'pointer',
    fontSize: 'var(--text-body)',
  };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
      <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Dialog Module</h1>
      <button type="button" style={triggerStyle} onClick={() => setOpen(true)}>
        Open Dialog
      </button>
      <Dialog
        open={open}
        onClose={() => setOpen(false)}
        title="Sample Dialog"
        actions={
          <button type="button" style={closeStyle} onClick={() => setOpen(false)}>
            Close
          </button>
        }
      >
        <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
          This is a sample dialog rendered with the design-system Dialog primitive.
        </p>
      </Dialog>
    </div>
  );
});
