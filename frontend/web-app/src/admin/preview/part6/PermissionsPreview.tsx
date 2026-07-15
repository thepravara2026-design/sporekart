import { PermissionProvider, PermissionGate, PermissionGateAll, PermissionGateAny, usePermissions, DEFAULT_PERMISSION_CONFIG } from '../../permissions';
import { OperationalStateDisplay } from '../../operational-states';

function PermissionMatrix() {
  const { can, role, setRole } = usePermissions();
  const resources = ['dashboard', 'orders', 'products', 'customers', 'users', 'settings', 'reports', 'audit'];
  const actions = ['view', 'create', 'update', 'delete', 'export', 'import', 'approve', 'publish', 'archive', 'bulk_actions'];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div>
        <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Select Role</label>
        <select value={role} onChange={(e) => setRole(e.target.value)} style={{ padding: '8px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}>
          {DEFAULT_PERMISSION_CONFIG.map((c) => (
            <option key={c.role} value={c.role}>{c.role.replace(/_/g, ' ').replace(/\b\w/g, (l) => l.toUpperCase())}</option>
          ))}
        </select>
      </div>

      <div style={{ overflowX: 'auto' }}>
        <table style={{ borderCollapse: 'collapse', width: '100%', fontSize: 'var(--text-caption)' }}>
          <thead>
            <tr>
              <th style={{ textAlign: 'left', padding: '8px 12px', borderBottom: '2px solid var(--color-border)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>Resource</th>
              {actions.map((a) => (
                <th key={a} style={{ padding: '8px 6px', borderBottom: '2px solid var(--color-border)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', textAlign: 'center', fontSize: 10 }}>{a}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {resources.map((res) => (
              <tr key={res}>
                <td style={{ padding: '6px 12px', borderBottom: '1px solid var(--color-border)', fontWeight: 500, color: 'var(--color-text-primary)' }}>{res}</td>
                {actions.map((act) => (
                  <td key={`${res}-${act}`} style={{ padding: '6px 6px', borderBottom: '1px solid var(--color-border)', textAlign: 'center', color: can(act as any, res) ? 'var(--color-success)' : 'var(--color-text-tertiary)' }}>
                    {can(act as any, res) ? '✓' : '—'}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export function PermissionsPreview() {
  return (
    <PermissionProvider initialRole="super_admin">
      <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
        <h2 style={{ margin: 0 }}>Permissions Framework</h2>

        <PermissionMatrix />

        <section>
          <h3 style={{ marginBottom: 8 }}>PermissionGate Usage</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
            <PermissionGate action="create" resource="orders" fallback={<OperationalStateDisplay state="permission_denied" compact />}>
              <div style={{ padding: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                Order creation form (visible only with create permission)
              </div>
            </PermissionGate>
            <PermissionGateAll actions={['view', 'export']} resource="reports" fallback={<div style={{ color: 'var(--color-text-tertiary)', padding: 8 }}>Reports access restricted</div>}>
              <div style={{ padding: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                Reports dashboard (visible with view + export permissions)
              </div>
            </PermissionGateAll>
            <PermissionGateAny actions={['view', 'create']} resource="customers" fallback={<div style={{ color: 'var(--color-text-tertiary)', padding: 8 }}>Customer access restricted</div>}>
              <div style={{ padding: 12, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)' }}>
                Customer section (visible with view OR create permission)
              </div>
            </PermissionGateAny>
          </div>
        </section>
      </div>
    </PermissionProvider>
  );
}
