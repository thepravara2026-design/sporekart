import { memo, useState, type CSSProperties } from 'react';
import { ListPageTemplate, DeleteConfirmationDialog } from '../CrudTemplates';
import type { ModuleConfig } from '../types';
import type { DataGridColumn } from '../../components/data-grid/types';

const columns: DataGridColumn<Record<string, any>>[] = [
  { key: 'id', header: 'ID', sortable: true, width: '120px' },
  { key: 'name', header: 'Name', sortable: true, filterable: true, filterType: 'dropdown' },
  { key: 'status', header: 'Status', sortable: true, filterable: true, filterType: 'status', align: 'center' },
];

const data: Record<string, any>[] = [
  { id: 'R1', name: 'Acme Corp', status: 'active' },
  { id: 'R2', name: 'Globex', status: 'pending' },
  { id: 'R3', name: 'Initech', status: 'archived' },
];

const config: ModuleConfig = {
  id: 'crud-demo',
  label: 'CRUD Demo',
  description: 'Demonstrates a list page composed with bulk selection and a delete dialog.',
  columns,
  data,
  permissionAction: 'view',
  featureKey: 'crud-demo',
};

export const CrudModulePreview = memo(function CrudModulePreview() {
  const [open, setOpen] = useState(false);
  const headerStyle: CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 'var(--space-4)',
  };
  const buttonStyle: CSSProperties = {
    padding: 'var(--space-2) var(--space-4)',
    borderRadius: 'var(--radius-input)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-surface-default)',
    color: 'var(--color-text-primary)',
    cursor: 'pointer',
    fontSize: 'var(--text-body)',
  };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
      <div style={headerStyle}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>CRUD Module</h1>
        <button type="button" style={buttonStyle} onClick={() => setOpen(true)}>
          New Record
        </button>
      </div>
      <ListPageTemplate config={config} />
      <DeleteConfirmationDialog open={open} onClose={() => setOpen(false)} onConfirm={() => setOpen(false)} />
    </div>
  );
});
