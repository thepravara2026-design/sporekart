import React from 'react';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';

export type BulkActionKind = 'delete' | 'archive' | 'publish' | 'download' | 'move' | null;

interface BulkActionDialogsProps {
  active: BulkActionKind;
  count: number;
  onConfirm: () => void;
  onCancel: () => void;
}

const ACTION_LABELS: Record<NonNullable<BulkActionKind>, { title: string; description: string; confirmLabel: string; variant: 'destructive' | 'primary' }> = {
  delete: {
    title: 'Delete Assets',
    description: 'Are you sure you want to delete the selected assets? This action cannot be undone.',
    confirmLabel: 'Delete',
    variant: 'destructive',
  },
  archive: {
    title: 'Archive Assets',
    description: 'Archive the selected assets? They will be moved out of the active library.',
    confirmLabel: 'Archive',
    variant: 'primary',
  },
  publish: {
    title: 'Publish Assets',
    description: 'Publish the selected assets to make them available across the platform.',
    confirmLabel: 'Publish',
    variant: 'primary',
  },
  download: {
    title: 'Download Assets',
    description: 'Prepare a ZIP archive of the selected assets for download.',
    confirmLabel: 'Download',
    variant: 'primary',
  },
  move: {
    title: 'Move Assets',
    description: 'Move the selected assets to a different collection.',
    confirmLabel: 'Move',
    variant: 'primary',
  },
};

export const BulkActionDialogs = React.memo(function BulkActionDialogs({ active, count, onConfirm, onCancel }: BulkActionDialogsProps) {
  if (!active) return null;

  const action = ACTION_LABELS[active];

  const overlayStyle: React.CSSProperties = {
    position: 'fixed', inset: 0, zIndex: 1100,
    background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center',
    justifyContent: 'center', padding: 'var(--space-4)',
  };

  const dialogStyle: React.CSSProperties = {
    background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-modal)',
    maxWidth: 420, width: '100%', padding: 'var(--space-5)',
    boxShadow: 'var(--shadow-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)',
  };

  const iconColor = active === 'delete' ? 'var(--color-text-danger)' : 'var(--color-primary)';

  return (
    <div style={overlayStyle} onClick={onCancel} role="dialog" aria-modal="true" aria-label={action.title}>
      <div style={dialogStyle} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
          <Icon
            name={active === 'delete' ? 'alert-triangle' : active === 'archive' ? 'archive' : active === 'publish' ? 'upload' : active === 'download' ? 'download' : 'folder'}
            size={24}
            style={{ color: iconColor }}
          />
          <span style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{action.title}</span>
        </div>
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          {action.description}
        </p>
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)' }}>
          {count} asset{count === 1 ? '' : 's'} selected.
        </p>
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 'var(--space-inline-xs)' }}>
          <Button variant="ghost" size="sm" onClick={onCancel}>Cancel</Button>
          <Button
            variant={action.variant}
            size="sm"
            onClick={onConfirm}
            leftIcon={
              <Icon
                name={active === 'delete' ? 'trash-2' : active === 'archive' ? 'archive' : active === 'publish' ? 'upload' : active === 'download' ? 'download' : 'folder'}
                size={14}
              />
            }
          >
            {action.confirmLabel}
          </Button>
        </div>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Mock Mode — no data will be modified.
        </span>
      </div>
    </div>
  );
});

export default BulkActionDialogs;
