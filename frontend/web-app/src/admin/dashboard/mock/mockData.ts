import type { KPIData, WidgetConfig, ActivityItemData, AnnouncementData, SystemStatusData, QuickActionData } from '../types';

export const MOCK_KPIS: KPIData[] = [
  { id: 'revenue', title: 'Total Revenue', value: '$284,500', trend: 'up', percentage: 12.5, comparison: 'vs last month', icon: 'dollar-sign', color: 'var(--color-success)' },
  { id: 'orders', title: 'Orders', value: '1,847', trend: 'up', percentage: 8.3, comparison: 'vs last month', icon: 'shopping-cart', color: 'var(--color-primary)' },
  { id: 'customers', title: 'Customers', value: '12,430', trend: 'up', percentage: 5.2, comparison: 'vs last month', icon: 'users', color: 'var(--color-info)' },
  { id: 'products', title: 'Products', value: '3,892', trend: 'neutral', percentage: 0, comparison: 'vs last month', icon: 'package', color: 'var(--color-warning)' },
  { id: 'inventory', title: 'Inventory Items', value: '28,450', trend: 'down', percentage: 2.1, comparison: 'vs last month', icon: 'archive', color: 'var(--color-text-secondary)' },
  { id: 'training', title: 'Training Modules', value: '156', trend: 'up', percentage: 15.0, comparison: 'vs last month', icon: 'book-open', color: 'var(--color-accent)' },
  { id: 'support', title: 'Support Tickets', value: '42', trend: 'down', percentage: 18.6, comparison: 'vs last month', icon: 'headphones', color: 'var(--color-error)' },
  { id: 'visitors', title: 'Visitors Today', value: '3,241', trend: 'up', percentage: 7.8, comparison: 'vs yesterday', icon: 'eye', color: 'var(--color-primary)' },
];

export const MOCK_WIDGETS: WidgetConfig[] = [
  { id: 'sales-overview', title: 'Sales Overview', type: 'sales-overview', icon: 'trending-up', width: 2, height: 1, pinned: true, visible: true },
  { id: 'recent-orders', title: 'Recent Orders', type: 'recent-orders', icon: 'shopping-cart', width: 2, height: 1, pinned: true, visible: true },
  { id: 'inventory-status', title: 'Inventory Status', type: 'inventory-status', icon: 'archive', width: 1, height: 1, visible: true },
  { id: 'customer-growth', title: 'Customer Growth', type: 'customer-growth', icon: 'users', width: 1, height: 1, visible: true },
  { id: 'revenue-chart', title: 'Revenue', type: 'revenue', icon: 'dollar-sign', width: 2, height: 1, visible: true },
  { id: 'training-overview', title: 'Training Overview', type: 'training-overview', icon: 'book-open', width: 1, height: 1, visible: true },
  { id: 'tasks', title: 'Tasks', type: 'tasks', icon: 'check-square', width: 1, height: 1, visible: true },
  { id: 'calendar', title: 'Calendar', type: 'calendar', icon: 'calendar', width: 1, height: 1, visible: true },
  { id: 'notifications', title: 'Notifications', type: 'notifications', icon: 'bell', width: 1, height: 1, visible: true },
  { id: 'quick-stats', title: 'Quick Statistics', type: 'quick-stats', icon: 'bar-chart', width: 1, height: 1, visible: true },
];

export const MOCK_ACTIVITY: ActivityItemData[] = Array.from({ length: 12 }, (_, i) => ({
  id: `activity-${i}`,
  user: ['Alice', 'Bob', 'Carol', 'David', 'Eve', 'Frank'][i % 6],
  action: ['created', 'updated', 'deleted', 'approved', 'rejected', 'commented on'][i % 6],
  target: ['Order #10492', 'Product "Running Shoes"', 'User Account', 'Training Module', 'Inventory Batch', 'Support Ticket #8921'][i % 6],
  type: ['create', 'update', 'delete', 'success', 'warning', 'system'][i % 6] as any,
  timestamp: `${i + 1}m ago`,
  status: i % 5 === 0 ? 'failed' : i % 7 === 0 ? 'pending' : 'completed',
  details: i % 3 === 0 ? `This is additional detail for activity item ${i + 1}. It provides more context about what happened.` : undefined,
}));

export const MOCK_ANNOUNCEMENTS: AnnouncementData[] = [
  { id: 'ann-1', title: 'System Maintenance Tonight', message: 'The platform will undergo scheduled maintenance from 2:00 AM to 4:00 AM EST. Some services may be temporarily unavailable.', priority: 'high', category: 'maintenance', read: false, timestamp: '2h ago', author: 'System Admin' },
  { id: 'ann-2', title: 'New Feature: Bulk Import', message: 'You can now import products in bulk using CSV files. Check the documentation for the required format.', priority: 'medium', category: 'feature', read: false, timestamp: '1d ago', author: 'Product Team' },
  { id: 'ann-3', title: 'Q4 Performance Report Available', message: 'The quarterly performance report is now available in the Reports section.', priority: 'low', category: 'update', read: true, timestamp: '3d ago', author: 'Analytics Team' },
  { id: 'ann-4', title: 'Critical Security Update', message: 'Please update your passwords before the end of the week. All users are required to enable two-factor authentication.', priority: 'critical', category: 'alert', read: false, timestamp: '5h ago', author: 'Security Team' },
];

export const MOCK_SYSTEM_STATUS: SystemStatusData[] = [
  { id: 'app', label: 'Application', status: 'operational', uptime: '99.9%', icon: 'globe' },
  { id: 'database', label: 'Database', status: 'operational', uptime: '99.95%', icon: 'database' },
  { id: 'api', label: 'API', status: 'operational', uptime: '99.8%', icon: 'zap' },
  { id: 'storage', label: 'Storage', status: 'degraded', uptime: '98.5%', icon: 'hard-drive' },
  { id: 'backup', label: 'Backup', status: 'operational', uptime: '100%', icon: 'cloud' },
  { id: 'email', label: 'Email Service', status: 'operational', uptime: '99.7%', icon: 'mail' },
  { id: 'payment', label: 'Payment Service', status: 'operational', uptime: '99.99%', icon: 'credit-card' },
  { id: 'shipping', label: 'Shipping Service', status: 'maintenance', uptime: '96.2%', icon: 'truck' },
];

export const MOCK_QUICK_ACTIONS: QuickActionData[] = [
  { id: 'add-product', label: 'Add Product', icon: 'plus', path: '/admin/products/new', color: 'var(--color-primary)' },
  { id: 'create-category', label: 'Create Category', icon: 'folder-plus', path: '/admin/categories/new', color: 'var(--color-success)' },
  { id: 'create-training', label: 'Create Training', icon: 'book', path: '/admin/training/new', color: 'var(--color-accent)' },
  { id: 'manage-orders', label: 'Manage Orders', icon: 'shopping-cart', path: '/admin/orders', color: 'var(--color-warning)' },
  { id: 'manage-inventory', label: 'Manage Inventory', icon: 'archive', path: '/admin/inventory', color: 'var(--color-info)' },
  { id: 'view-reports', label: 'View Reports', icon: 'bar-chart', path: '/admin/reports', color: 'var(--color-text-secondary)' },
  { id: 'customer-mgmt', label: 'Customer Management', icon: 'users', path: '/admin/customers', color: 'var(--color-primary)' },
  { id: 'settings', label: 'Settings', icon: 'settings', path: '/admin/settings', color: 'var(--color-text-tertiary)' },
];
