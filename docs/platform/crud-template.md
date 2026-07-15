# CRUD Template — Reusable Operation Skeletons

These are **patterns, not business logic**. Each skeleton uses verified APIs. Copy the relevant skeleton into `modules/<id>/` and fill in data/types/services.

## List Page

```tsx
<DataGrid
  columns={columns}
  data={rows}
  sortable searchable exportable filterable stickyHeader
  pageSize={10}
  rowKey="id"
  loading={loading}
  emptyMessage="No records" emptyDescription="Create one to begin"
  cardViewBreakpoint={768}
  renderCard={(row) => <Card row={row} />}
/>
```

## Details Page

```tsx
<PermissionGate action="view" resource={moduleId}>
  <FeatureGate flag={moduleId}>
    <h1 style={{ fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>{record.name}</h1>
    {/* render fields; use FormField readOnly for a consistent layout */}
  </FeatureGate>
</PermissionGate>
```

## Create Page

```tsx
<FormLayout>
  <FormSection title="New Record">
    <FormField label="Name" required error={errors.name}>
      <Textarea name="name" value={value} onChange={onChange} />
    </FormField>
  </FormSection>
  <FormActions>
    <Button onClick={onSubmit}>Create</Button>
    <Button variant="ghost" onClick={onCancel}>Cancel</Button>
  </FormActions>
</FormLayout>
```
See [`form-template.md`](./form-template.md) for the full skeleton.

## Edit Page

Same as Create, but the form is prefilled from `record` and the submit action is `update`. Guard the save button:

```tsx
<PermissionGate action="update" resource={moduleId}>
  <Button onClick={onSave}>Save</Button>
</PermissionGate>
```

## Delete Confirmation

```tsx
import { Dialog } from '../../../design-system/components/feedback/Dialog';

<Dialog
  open={confirmOpen}
  onClose={() => setConfirmOpen(false)}
  title="Delete record?"
  size="sm"
  actions={
    <>
      <Button variant="ghost" onClick={() => setConfirmOpen(false)}>Cancel</Button>
      <Button variant="danger" onClick={onConfirmDelete}>Delete</Button>
    </>
  }
>
  This action cannot be undone.
</Dialog>
```

## Bulk Operations

```tsx
<PermissionGate action="bulk_actions" resource={moduleId}>
  <DataGrid
    columns={columns}
    data={rows}
    selectable
    onRowClick={undefined}
    /* selection driven by internal state; read selected via DataGrid */
  />
  <BulkActionBar
    onDelete={() => setBulkConfirmOpen(true)}
    onExport={() => {/* DataGrid exportable handles export */}}
  />
</PermissionGate>
```

## Import

```tsx
<PermissionGate action="import" resource={moduleId}>
  <FormLayout>
    <FormSection title="Import">
      <FormField label="File" helperText="CSV or Excel">
        <UploadInput /* from design-system */ />
      </FormField>
    </FormSection>
    <FormActions>
      <Button onClick={onImport}>Import</Button>
    </FormActions>
  </FormLayout>
</PermissionGate>
```

## Export

`DataGrid` with `exportable` renders the export control automatically. For a custom trigger gated by permission:

```tsx
<PermissionGate action="export" resource={moduleId}>
  <Button onClick={onExport}>Export CSV</Button>
</PermissionGate>
```

## Preview

```tsx
<Dialog open={previewOpen} onClose={close} title="Preview" size="lg">
  <RenderPreview row={row} />
</Dialog>
```

## Archive

```tsx
<PermissionGate action="archive" resource={moduleId}>
  <Button onClick={() => setArchiveOpen(true)}>Archive</Button>
</PermissionGate>
<Dialog open={archiveOpen} onClose={close} title="Archive record?" size="sm" actions={/* confirm */}>
  Archived records move to the archive view.
</Dialog>
```

## Restore

```tsx
<PermissionGate action="update" resource={moduleId}>
  <Button onClick={onRestore}>Restore</Button>
</PermissionGate>
```
