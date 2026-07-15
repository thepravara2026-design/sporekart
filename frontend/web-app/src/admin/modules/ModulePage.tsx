import { memo } from 'react';
import { DataGrid } from '../components/data-grid/DataGrid';
import { PermissionGate } from '../permissions/PermissionGate';
import { FeatureGate } from '../feature-flags/FeatureGate';
import type { MockModule } from './moduleData';

interface ModulePageProps {
  module: MockModule;
}

export const ModulePage = memo(function ModulePage({ module }: ModulePageProps) {
  return (
    <PermissionGate action="view" resource={module.id}>
      <FeatureGate flag={module.id}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
          <div>
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
              {module.label}
            </h1>
            <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
              {module.description}
            </p>
          </div>

          <DataGrid
            columns={module.columns}
            data={module.data}
            sortable
            searchable
            exportable
            stickyHeader
            pageSize={10}
          />
        </div>
      </FeatureGate>
    </PermissionGate>
  );
});
