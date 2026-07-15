import React, { memo, useState } from 'react';
import { Dialog } from '../../../../design-system/components/feedback/Dialog';
import { Button } from '../../../../design-system/components/core/Button';
import { ALL_BRANDS, ALL_CATEGORIES } from '../mock/catalogMock';

export type BulkActionKind =
  | 'archive'
  | 'delete'
  | 'publish'
  | 'unpublish'
  | 'export'
  | 'assign_category'
  | 'assign_brand'
  | null;

interface ConfirmDialogProps {
  open: boolean;
  count: number;
  onConfirm: () => void;
  onCancel: () => void;
}

const selectStyle: React.CSSProperties = {
  width: '100%',
  padding: '8px 10px',
  marginTop: 'var(--space-stack-xs)',
  border: '1px solid var(--color-border)',
  borderRadius: 'var(--radius-md)',
  background: 'var(--color-surface)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-body)',
  fontFamily: 'var(--font-family-sans)',

};

const bodyText: React.CSSProperties = {
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-secondary)',
  margin: 0,
};

function Actions({
  onConfirm,
  onCancel,
  confirmLabel,
  variant,
}: {
  onConfirm: () => void;
  onCancel: () => void;
  confirmLabel: string;
  variant?: 'primary' | 'destructive' | 'success';
}) {
  return (
    <>
      <Button variant="ghost" size="sm" onClick={onCancel}>
        Cancel
      </Button>
      <Button variant={variant ?? 'primary'} size="sm" onClick={onConfirm}>
        {confirmLabel}
      </Button>
    </>
  );
}

export const BulkArchiveDialog = memo(function BulkArchiveDialog({ open, count, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Archive products"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Archive" variant="primary" />}
    >
      <p style={bodyText}>
        Archive <strong>{count}</strong> selected product{count === 1 ? '' : 's'}? Archived products are hidden from the
        storefront but can be restored later. <em>(Mock Mode — no changes are persisted.)</em>
      </p>
    </Dialog>
  );
});

export const BulkDeleteDialog = memo(function BulkDeleteDialog({ open, count, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Delete products"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Delete" variant="destructive" />}
    >
      <p style={bodyText}>
        Delete <strong>{count}</strong> selected product{count === 1 ? '' : 's'}? This is a soft-delete and can be undone.
        <em> (Mock Mode — no changes are persisted.)</em>
      </p>
    </Dialog>
  );
});

export const BulkPublishDialog = memo(function BulkPublishDialog({ open, count, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Publish products"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Publish" variant="success" />}
    >
      <p style={bodyText}>
        Publish <strong>{count}</strong> selected product{count === 1 ? '' : 's'} to the storefront?
        <em> (Mock Mode — no changes are persisted.)</em>
      </p>
    </Dialog>
  );
});

export const BulkUnpublishDialog = memo(function BulkUnpublishDialog({ open, count, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Unpublish products"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Unpublish" variant="primary" />}
    >
      <p style={bodyText}>
        Unpublish <strong>{count}</strong> selected product{count === 1 ? '' : 's'}? They will be hidden from customers.
        <em> (Mock Mode — no changes are persisted.)</em>
      </p>
    </Dialog>
  );
});

export const BulkExportDialog = memo(function BulkExportDialog({ open, count, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Export products"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Export" variant="primary" />}
    >
      <p style={bodyText}>
        Export <strong>{count}</strong> selected product{count === 1 ? '' : 's'} as CSV?
        <em> (Mock Mode — no file is generated.)</em>
      </p>
    </Dialog>
  );
});

export const BulkAssignCategoryDialog = memo(function BulkAssignCategoryDialog({
  open,
  count,
  onConfirm,
  onCancel,
}: ConfirmDialogProps) {
  const [value, setValue] = useState(ALL_CATEGORIES[0] ?? '');
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Assign category"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Assign" variant="primary" />}
    >
      <p style={bodyText}>
        Assign a category to <strong>{count}</strong> selected product{count === 1 ? '' : 's'}.
      </p>
      <label style={{ display: 'block', marginTop: 'var(--space-component-gap)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
        Category
        <select style={selectStyle} value={value} onChange={(e) => setValue(e.target.value)} aria-label="Category">
          {ALL_CATEGORIES.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>
      </label>
    </Dialog>
  );
});

export const BulkAssignBrandDialog = memo(function BulkAssignBrandDialog({
  open,
  count,
  onConfirm,
  onCancel,
}: ConfirmDialogProps) {
  const [value, setValue] = useState(ALL_BRANDS[0] ?? '');
  return (
    <Dialog
      open={open}
      onClose={onCancel}
      title="Assign brand"
      size="sm"
      actions={<Actions onConfirm={onConfirm} onCancel={onCancel} confirmLabel="Assign" variant="primary" />}
    >
      <p style={bodyText}>
        Assign a brand to <strong>{count}</strong> selected product{count === 1 ? '' : 's'}.
      </p>
      <label style={{ display: 'block', marginTop: 'var(--space-component-gap)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
        Brand
        <select style={selectStyle} value={value} onChange={(e) => setValue(e.target.value)} aria-label="Brand">
          {ALL_BRANDS.map((b) => (
            <option key={b} value={b}>
              {b}
            </option>
          ))}
        </select>
      </label>
    </Dialog>
  );
});

interface BulkActionDialogsProps {
  active: BulkActionKind;
  count: number;
  onConfirm: () => void;
  onCancel: () => void;
}

export const BulkActionDialogs = memo(function BulkActionDialogs({ active, count, onConfirm, onCancel }: BulkActionDialogsProps) {
  return (
    <>
      <BulkArchiveDialog open={active === 'archive'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
      <BulkDeleteDialog open={active === 'delete'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
      <BulkPublishDialog open={active === 'publish'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
      <BulkUnpublishDialog open={active === 'unpublish'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
      <BulkExportDialog open={active === 'export'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
      <BulkAssignCategoryDialog open={active === 'assign_category'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
      <BulkAssignBrandDialog open={active === 'assign_brand'} count={count} onConfirm={onConfirm} onCancel={onCancel} />
    </>
  );
});
