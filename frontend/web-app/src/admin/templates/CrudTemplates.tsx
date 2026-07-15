import { memo, type CSSProperties } from 'react';
import { DataGrid } from '../components/data-grid/DataGrid';
import { PermissionGate } from '../permissions/PermissionGate';
import { FeatureGate } from '../feature-flags/FeatureGate';
import { Dialog } from '../../design-system/components/feedback/Dialog';
import { ExampleForm } from './FormTemplate';
import type { ModuleConfig } from './types';
import type { ReactNode } from 'react';

interface ListPageTemplateProps<T> {
  config: ModuleConfig<T>;
}

function ListPageTemplateInner<T extends Record<string, any>>({ config }: ListPageTemplateProps<T>): ReactNode {
  return (
    <PermissionGate action={config.permissionAction} resource={config.id}>
      <FeatureGate flag={config.featureKey}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
          <div>
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
              {config.label}
            </h1>
            <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
              {config.description}
            </p>
          </div>

          <DataGrid<T>
            columns={config.columns}
            data={config.data}
            sortable
            searchable
            exportable
            selectable
            stickyHeader
            pageSize={10}
          />
        </div>
      </FeatureGate>
    </PermissionGate>
  );
}

export const ListPageTemplate = memo(ListPageTemplateInner) as unknown as <T extends Record<string, any>>(
  props: ListPageTemplateProps<T>
) => ReactNode;

export const DetailsPageTemplate = memo(function DetailsPageTemplate({
  title = 'Details',
  data = {},
}: {
  title?: string;
  data?: Record<string, any>;
}) {
  const cardStyle: CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-4)',
    padding: 'var(--space-6)',
    borderRadius: 'var(--radius-lg)',
    backgroundColor: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
  };
  return (
    <div style={cardStyle}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>{title}</h2>
      <dl style={{ margin: 0, display: 'grid', gridTemplateColumns: '160px 1fr', rowGap: 'var(--space-3)' }}>
        {Object.entries(data).map(([key, value]) => (
          <div key={key} style={{ display: 'contents' }}>
            <dt style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{key}</dt>
            <dd style={{ margin: 0, color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}>
              {String(value)}
            </dd>
          </div>
        ))}
      </dl>
    </div>
  );
});

export const CreatePageTemplate = memo(function CreatePageTemplate() {
  return <ExampleForm />;
});

export const EditPageTemplate = memo(function EditPageTemplate() {
  return <ExampleForm />;
});

interface DeleteConfirmationDialogProps {
  open: boolean;
  onClose: () => void;
  onConfirm?: () => void;
  title?: string;
  message?: string;
}

export const DeleteConfirmationDialog = memo(function DeleteConfirmationDialog({
  open,
  onClose,
  onConfirm,
  title = 'Confirm Delete',
  message = 'Are you sure you want to delete this record? This action cannot be undone.',
}: DeleteConfirmationDialogProps) {
  const dangerButtonStyle: CSSProperties = {
    padding: 'var(--space-2) var(--space-4)',
    borderRadius: 'var(--radius-input)',
    border: '1px solid #ef4444',
    backgroundColor: '#ef4444',
    color: 'var(--color-bg-surface-default)',
    cursor: 'pointer',
    fontSize: 'var(--text-body)',
    fontWeight: 'var(--weight-medium)',
  };
  const neutralButtonStyle: CSSProperties = {
    padding: 'var(--space-2) var(--space-4)',
    borderRadius: 'var(--radius-input)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-surface-default)',
    color: 'var(--color-text-primary)',
    cursor: 'pointer',
    fontSize: 'var(--text-body)',
  };
  return (
    <Dialog
      open={open}
      onClose={onClose}
      title={title}
      actions={
        <>
          <button type="button" style={neutralButtonStyle} onClick={onClose}>
            Cancel
          </button>
          <button type="button" style={dangerButtonStyle} onClick={onConfirm}>
            Delete
          </button>
        </>
      }
    >
      <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{message}</p>
    </Dialog>
  );
});

interface BulkOperationsBarProps {
  count: number;
  onDelete?: () => void;
  onExport?: () => void;
}

export const BulkOperationsBar = memo(function BulkOperationsBar({
  count,
  onDelete,
  onExport,
}: BulkOperationsBarProps) {
  const barStyle: CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-3)',
    padding: 'var(--space-3) var(--space-4)',
    borderRadius: 'var(--radius-lg)',
    backgroundColor: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
  };
  const buttonStyle: CSSProperties = {
    padding: 'var(--space-1) var(--space-3)',
    borderRadius: 'var(--radius-input)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-background)',
    color: 'var(--color-text-primary)',
    cursor: 'pointer',
    fontSize: 'var(--text-caption)',
  };
  return (
    <div style={barStyle}>
      <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)' }}>
        {count} selected
      </span>
      <button type="button" style={buttonStyle} onClick={onExport}>
        Export
      </button>
      <button type="button" style={buttonStyle} onClick={onDelete}>
        Delete
      </button>
    </div>
  );
});
