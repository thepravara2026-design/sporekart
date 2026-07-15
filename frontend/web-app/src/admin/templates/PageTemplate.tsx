import { memo } from 'react';
import { DataGrid } from '../components/data-grid/DataGrid';
import { PermissionGate } from '../permissions/PermissionGate';
import { FeatureGate } from '../feature-flags/FeatureGate';
import { KPIGrid } from '../dashboard/kpi/KPIGrid';
import type { ModuleConfig } from './types';
import type { ReactNode } from 'react';

interface PageTemplateProps<T> {
  config: ModuleConfig<T>;
}

function PageTemplateInner<T extends Record<string, any>>({ config }: PageTemplateProps<T>): ReactNode {
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

          {config.kpis && config.kpis.length > 0 && <KPIGrid kpis={config.kpis} columns={4} />}

          <DataGrid<T>
            columns={config.columns}
            data={config.data}
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
}

export const PageTemplate = memo(PageTemplateInner) as unknown as <T extends Record<string, any>>(
  props: PageTemplateProps<T>
) => ReactNode;
