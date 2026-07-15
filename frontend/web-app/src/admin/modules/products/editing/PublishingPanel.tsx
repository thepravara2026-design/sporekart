import React, { useState } from 'react';
import type { ProductLifecycleState } from '../types';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import Dialog from '../../../../design-system/components/feedback/Dialog';

export interface PublishingPanelProps {
  lifecycle: ProductLifecycleState;
  requireApproval: boolean;
  canPublish: boolean;
  canApprove: boolean;
  canArchive: boolean;
  canRestore: boolean;
  canDelete: boolean;
  onSaveDraft: () => void;
  onSubmitForReview: () => void;
  onApprove: () => void;
  onReject: () => void;
  onSchedule: () => void;
  onPublish: () => void;
  onUnpublish: () => void;
  onArchive: () => void;
  onRestore: () => void;
  onDelete: () => void;
}

type DestructiveAction = 'archive' | 'restore' | 'delete' | null;

const PublishingPanel: React.FC<PublishingPanelProps> = (props) => {
  const { lifecycle } = props;
  const [confirm, setConfirm] = useState<DestructiveAction>(null);

  const submitEnabled =
    lifecycle !== 'under_review' && lifecycle !== 'deleted';
  const approveDisabled = !props.canApprove || lifecycle !== 'under_review';
  const rejectDisabled = lifecycle !== 'under_review';
  const scheduleDisabled = !props.canPublish;
  const publishDisabled =
    !props.canPublish || lifecycle === 'published' || lifecycle === 'active';
  const unpublishDisabled =
    lifecycle !== 'published' && lifecycle !== 'active';
  const archiveDisabled =
    !props.canArchive || lifecycle === 'archived' || lifecycle === 'deleted';
  const restoreDisabled =
    !props.canRestore ||
    (lifecycle !== 'archived' && lifecycle !== 'deleted');
  const deleteDisabled = !props.canDelete || lifecycle === 'deleted';

  const destructiveConfig: Record<
    Exclude<DestructiveAction, null>,
    { title: string; message: string; handler: () => void; confirmLabel: string }
  > = {
    archive: {
      title: 'Archive product',
      message:
        'Archiving this product will hide it from customers but keep its data. You can restore it later.',
      handler: props.onArchive,
      confirmLabel: 'Archive',
    },
    restore: {
      title: 'Restore product',
      message:
        'Restoring will return this product to an editable state. Continue?',
      handler: props.onRestore,
      confirmLabel: 'Restore',
    },
    delete: {
      title: 'Delete product',
      message:
        'Deleting this product is destructive and may be irreversible. Continue?',
      handler: props.onDelete,
      confirmLabel: 'Delete',
    },
  };

  const activeConfig = confirm ? destructiveConfig[confirm] : null;

  return (
    <Card>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-4)',
        }}
      >
        <h3
          style={{
            margin: 0,
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-lg)',
            fontWeight: 'var(--weight-semibold)',
          }}
        >
          Publishing Workflow
        </h3>

        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: 'var(--space-2)',
          }}
        >
        <Button variant="secondary" onClick={props.onSaveDraft}>
          Save Draft
        </Button>
        <Button
          variant="primary"
          disabled={!submitEnabled}
          onClick={props.onSubmitForReview}
        >
          Submit for Review
        </Button>
        <Button
          variant="primary"
          disabled={approveDisabled}
          leftIcon={<Icon name="Check" size={16} />}
          onClick={props.onApprove}
        >
          Approve
        </Button>
        <Button
          variant="ghost"
          disabled={rejectDisabled}
          leftIcon={<Icon name="X" size={16} />}
          onClick={props.onReject}
        >
          Reject
        </Button>
        <Button
          variant="secondary"
          disabled={scheduleDisabled}
          leftIcon={<Icon name="Clock" size={16} />}
          onClick={props.onSchedule}
        >
          Schedule Publish
        </Button>
        <Button
          variant="primary"
          disabled={publishDisabled}
          leftIcon={<Icon name="Upload" size={16} />}
          onClick={props.onPublish}
        >
          Publish
        </Button>
        <Button
          variant="ghost"
          disabled={unpublishDisabled}
          onClick={props.onUnpublish}
        >
          Unpublish
        </Button>
        <Button
          variant="ghost"
          disabled={archiveDisabled}
          leftIcon={<Icon name="Archive" size={16} />}
          onClick={() => setConfirm('archive')}
        >
          Archive
        </Button>
        <Button
          variant="ghost"
          disabled={restoreDisabled}
          leftIcon={<Icon name="RefreshCw" size={16} />}
          onClick={() => setConfirm('restore')}
        >
          Restore
        </Button>
        <Button
          variant="destructive"
          disabled={deleteDisabled}
          leftIcon={<Icon name="Trash" size={16} />}
          onClick={() => setConfirm('delete')}
        >
          Delete
        </Button>
      </div>

      <Dialog
        open={activeConfig !== null}
        onClose={() => setConfirm(null)}
        title={activeConfig?.title ?? ''}
        actions={
          <div
            style={{
              display: 'flex',
              justifyContent: 'flex-end',
              gap: 'var(--space-2)',
            }}
          >
            <Button variant="ghost" onClick={() => setConfirm(null)}>
              Cancel
            </Button>
            <Button
              variant={
                confirm === 'delete' ? 'destructive' : 'primary'
              }
              onClick={() => {
                activeConfig?.handler();
                setConfirm(null);
              }}
            >
              {activeConfig?.confirmLabel ?? 'Confirm'}
            </Button>
          </div>
        }
      >
        <p
          style={{
            margin: 0,
            color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-sm)',
            lineHeight: 1.5,
          }}
        >
          {activeConfig?.message}
        </p>
      </Dialog>
      </div>
    </Card>
  );
};

export default React.memo(PublishingPanel);
