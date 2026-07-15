import { useState } from 'react';
import { CommandPalette } from '../command-palette/CommandPalette';
import { Icon } from '../../../design-system/icons/Icon';
import type { CommandItem } from '../types';

const MOCK_COMMANDS: CommandItem[] = [
  { id: 'go-dashboard', label: 'Go to Dashboard', category: 'Navigation', icon: 'layout-dashboard', href: '/admin/dashboard', shortcut: 'G D' },
  { id: 'go-orders', label: 'View Orders', category: 'Navigation', icon: 'shopping-cart', href: '/admin/orders', shortcut: 'G O' },
  { id: 'go-products', label: 'Browse Products', category: 'Navigation', icon: 'package', href: '/admin/products', shortcut: 'G P' },
  { id: 'go-customers', label: 'Manage Customers', category: 'Navigation', icon: 'users', href: '/admin/customers', shortcut: 'G C' },
  { id: 'go-analytics', label: 'View Analytics', category: 'Navigation', icon: 'chart-bar', href: '/admin/analytics', shortcut: 'G A' },
  { id: 'go-settings', label: 'Open Settings', category: 'Navigation', icon: 'settings', href: '/admin/settings', shortcut: 'G S' },
  { id: 'go-inventory', label: 'View Inventory', category: 'Navigation', icon: 'archive', href: '/admin/inventory', keywords: ['stock', 'warehouse'] },
  { id: 'go-crm', label: 'Open CRM', category: 'Navigation', icon: 'user-plus', href: '/admin/crm', keywords: ['leads', 'pipeline'] },
  { id: 'go-training', label: 'Open Training', category: 'Navigation', icon: 'book-open', href: '/admin/training', keywords: ['courses', 'lms'] },
  { id: 'go-shipping', label: 'View Shipping', category: 'Navigation', icon: 'truck', href: '/admin/shipping', keywords: ['logistics', 'delivery'] },
  { id: 'go-finance', label: 'Open Finance', category: 'Navigation', icon: 'dollar-sign', href: '/admin/finance', keywords: ['revenue', 'billing'] },
  { id: 'go-reports', label: 'View Reports', category: 'Navigation', icon: 'bar-chart', href: '/admin/reports', keywords: ['analytics', 'export'] },
  { id: 'new-order', label: 'Create New Order', category: 'Actions', icon: 'plus-circle', keywords: ['create order', 'add order'] },
  { id: 'new-product', label: 'Add Product', category: 'Actions', icon: 'plus-circle', keywords: ['create product', 'new item'] },
  { id: 'new-customer', label: 'Register Customer', category: 'Actions', icon: 'user-plus', keywords: ['create customer', 'add user'] },
  { id: 'export-csv', label: 'Export as CSV', category: 'Export', icon: 'file-text', keywords: ['download', 'csv', 'spreadsheet'] },
  { id: 'export-pdf', label: 'Export as PDF', category: 'Export', icon: 'file-text', keywords: ['download', 'pdf'] },
  { id: 'export-excel', label: 'Export as Excel', category: 'Export', icon: 'file-spreadsheet', keywords: ['download', 'xlsx', 'excel'] },
  { id: 'print-page', label: 'Print Page', category: 'Export', icon: 'printer', keywords: ['print'] },
  { id: 'dark-mode', label: 'Toggle Dark Mode', category: 'System', icon: 'moon', shortcut: 'Ctrl+D' },
  { id: 'fullscreen', label: 'Toggle Fullscreen', category: 'System', icon: 'maximize', shortcut: 'F11' },
  { id: 'search', label: 'Global Search', category: 'System', icon: 'search', shortcut: 'Ctrl+K' },
];

export function CommandPalettePreview() {
  const [open, setOpen] = useState(false);
  const [lastCommand, setLastCommand] = useState<string | null>(null);

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0 }}>Part 5 — Command Palette</h2>

      <div style={{ padding: 24, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)' }}>
        <p style={{ margin: '0 0 16px', color: 'var(--color-text-secondary)' }}>
          Press <kbd style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)' }}>Ctrl+K</kbd> or click the button below to open the command palette.
          Navigate with <kbd style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)' }}>↑</kbd><kbd style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)' }}>↓</kbd>, select with <kbd style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)' }}>Enter</kbd>.
        </p>

        <button
          onClick={() => setOpen(true)}
          style={{
            padding: '10px 24px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)',
            background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)',
            display: 'inline-flex', alignItems: 'center', gap: 8, fontSize: 'var(--text-body)',
          }}
        >
          <Icon name="command" size={16} />
          <span>Open Command Palette</span>
          <kbd style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '2px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)' }}>Ctrl+K</kbd>
        </button>

        {lastCommand && (
          <div style={{ marginTop: 12, padding: '8px 12px', background: 'var(--color-primary-alpha)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-caption)', color: 'var(--color-primary)' }}>
            Last executed: {lastCommand}
          </div>
        )}
      </div>

      <section>
        <h3 style={{ marginBottom: 8 }}>Available Commands ({MOCK_COMMANDS.length})</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          {Object.entries(
            MOCK_COMMANDS.reduce<Record<string, CommandItem[]>>((acc, cmd) => {
              if (!acc[cmd.category]) acc[cmd.category] = [];
              acc[cmd.category].push(cmd);
              return acc;
            }, {})
          ).map(([category, cmds]) => (
            <div key={category}>
              <div style={{ padding: '6px 12px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: 600 }}>
                {category}
              </div>
              {cmds.map((cmd) => (
                <div key={cmd.id} style={{ display: 'flex', alignItems: 'center', gap: 10, padding: '4px 12px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
                  {cmd.icon && <Icon name={cmd.icon} size={14} style={{ color: 'var(--color-text-tertiary)' }} />}
                  <span style={{ flex: 1 }}>{cmd.label}</span>
                  {cmd.shortcut && <kbd style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>{cmd.shortcut}</kbd>}
                </div>
              ))}
            </div>
          ))}
        </div>
      </section>

      <CommandPalette
        commands={MOCK_COMMANDS}
        open={open}
        onClose={() => setOpen(false)}
        onExecute={(cmd) => setLastCommand(`"${cmd.label}" — ${cmd.description || ''}`)}
      />
    </div>
  );
}
