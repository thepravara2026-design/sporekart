import type { KPIData } from '../dashboard/types';

export interface ModuleKPIs {
  moduleId: string;
  moduleLabel: string;
  kpis: KPIData[];
}

export const MODULE_KPIS: ModuleKPIs[] = [
  {
    moduleId: 'products',
    moduleLabel: 'Products',
    kpis: [
      { id: 'products-total', title: 'Total Products', value: '1,284', trend: 'up', percentage: 12.5, comparison: 'vs last month', icon: 'package', color: '#2f6f4f' },
      { id: 'products-active', title: 'Active Products', value: '1,142', trend: 'up', percentage: 8.1, comparison: 'vs last month', icon: 'check-circle', color: '#1d9bf0' },
      { id: 'products-categories', title: 'Categories', value: '24', trend: 'neutral', percentage: 0, comparison: 'vs last month', icon: 'grid', color: '#7c3aed' },
      { id: 'products-avg-price', title: 'Avg Price', value: '$48.20', trend: 'down', percentage: 3.2, comparison: 'vs last month', icon: 'dollar-sign', color: '#d97706' },
    ],
  },
  {
    moduleId: 'inventory',
    moduleLabel: 'Inventory',
    kpis: [
      { id: 'inventory-total', title: 'Total Items', value: '8,942', trend: 'up', percentage: 5.4, comparison: 'vs last week', icon: 'archive', color: '#2f6f4f' },
      { id: 'inventory-low', title: 'Low Stock', value: '37', trend: 'up', percentage: 14.2, comparison: 'vs last week', icon: 'alert-triangle', color: '#ef4444' },
      { id: 'inventory-warehouses', title: 'Warehouses', value: '6', trend: 'neutral', percentage: 0, comparison: 'vs last month', icon: 'building', color: '#1d9bf0' },
      { id: 'inventory-value', title: 'Stock Value', value: '$2.4M', trend: 'up', percentage: 9.8, comparison: 'vs last month', icon: 'dollar-sign', color: '#7c3aed' },
    ],
  },
  {
    moduleId: 'orders',
    moduleLabel: 'Orders',
    kpis: [
      { id: 'orders-total', title: 'Total Orders', value: '3,471', trend: 'up', percentage: 18.3, comparison: 'vs last month', icon: 'shopping-cart', color: '#2f6f4f' },
      { id: 'orders-pending', title: 'Pending Orders', value: '142', trend: 'down', percentage: 6.1, comparison: 'vs last week', icon: 'clock', color: '#d97706' },
      { id: 'orders-revenue', title: 'Revenue', value: '$182K', trend: 'up', percentage: 22.4, comparison: 'vs last month', icon: 'dollar-sign', color: '#1d9bf0' },
      { id: 'orders-aov', title: 'Avg Order Value', value: '$52.40', trend: 'up', percentage: 3.4, comparison: 'vs last month', icon: 'trending-up', color: '#7c3aed' },
    ],
  },
  {
    moduleId: 'customers',
    moduleLabel: 'Customers',
    kpis: [
      { id: 'customers-total', title: 'Total Customers', value: '12,938', trend: 'up', percentage: 11.2, comparison: 'vs last month', icon: 'users', color: '#2f6f4f' },
      { id: 'customers-active', title: 'Active (30d)', value: '4,201', trend: 'up', percentage: 7.8, comparison: 'vs last month', icon: 'activity', color: '#1d9bf0' },
      { id: 'customers-vip', title: 'VIP Customers', value: '318', trend: 'up', percentage: 4.5, comparison: 'vs last month', icon: 'star', color: '#d97706' },
      { id: 'customers-churn', title: 'Churn Rate', value: '2.1%', trend: 'down', percentage: 0.8, comparison: 'vs last month', icon: 'trending-down', color: '#ef4444' },
    ],
  },
  {
    moduleId: 'crm',
    moduleLabel: 'CRM',
    kpis: [
      { id: 'crm-leads', title: 'Open Leads', value: '526', trend: 'up', percentage: 13.6, comparison: 'vs last month', icon: 'user-plus', color: '#2f6f4f' },
      { id: 'crm-pipeline', title: 'Pipeline Value', value: '$1.8M', trend: 'up', percentage: 19.2, comparison: 'vs last month', icon: 'dollar-sign', color: '#1d9bf0' },
      { id: 'crm-won', title: 'Deals Won', value: '89', trend: 'up', percentage: 8.9, comparison: 'vs last month', icon: 'check-circle', color: '#7c3aed' },
      { id: 'crm-conv', title: 'Conversion', value: '24.3%', trend: 'up', percentage: 2.1, comparison: 'vs last month', icon: 'target', color: '#d97706' },
    ],
  },
  {
    moduleId: 'training',
    moduleLabel: 'Training',
    kpis: [
      { id: 'training-courses', title: 'Active Courses', value: '47', trend: 'up', percentage: 6.7, comparison: 'vs last month', icon: 'book-open', color: '#2f6f4f' },
      { id: 'training-enrolled', title: 'Enrollments', value: '2,184', trend: 'up', percentage: 15.3, comparison: 'vs last month', icon: 'user-plus', color: '#1d9bf0' },
      { id: 'training-completion', title: 'Completion Rate', value: '78%', trend: 'up', percentage: 4.2, comparison: 'vs last month', icon: 'check-circle', color: '#7c3aed' },
      { id: 'training-hours', title: 'Avg Hours', value: '12.5h', trend: 'neutral', percentage: 0, comparison: 'vs last month', icon: 'clock', color: '#d97706' },
    ],
  },
  {
    moduleId: 'shipping',
    moduleLabel: 'Shipping',
    kpis: [
      { id: 'shipping-active', title: 'Active Shipments', value: '642', trend: 'up', percentage: 9.4, comparison: 'vs last week', icon: 'truck', color: '#2f6f4f' },
      { id: 'shipping-delivered', title: 'Delivered (7d)', value: '3,128', trend: 'up', percentage: 11.1, comparison: 'vs last week', icon: 'check-circle', color: '#1d9bf0' },
      { id: 'shipping-exception', title: 'Exceptions', value: '18', trend: 'down', percentage: 22.5, comparison: 'vs last week', icon: 'alert-triangle', color: '#ef4444' },
      { id: 'shipping-ontime', title: 'On-Time Rate', value: '96.2%', trend: 'up', percentage: 1.3, comparison: 'vs last week', icon: 'trending-up', color: '#7c3aed' },
    ],
  },
  {
    moduleId: 'finance',
    moduleLabel: 'Finance',
    kpis: [
      { id: 'finance-revenue', title: 'Revenue (MTD)', value: '$842K', trend: 'up', percentage: 16.8, comparison: 'vs last month', icon: 'dollar-sign', color: '#2f6f4f' },
      { id: 'finance-expenses', title: 'Expenses (MTD)', value: '$214K', trend: 'up', percentage: 4.2, comparison: 'vs last month', icon: 'credit-card', color: '#d97706' },
      { id: 'finance-profit', title: 'Net Profit', value: '$628K', trend: 'up', percentage: 21.5, comparison: 'vs last month', icon: 'trending-up', color: '#1d9bf0' },
      { id: 'finance-margin', title: 'Margin', value: '74.6%', trend: 'up', percentage: 3.1, comparison: 'vs last month', icon: 'percent', color: '#7c3aed' },
    ],
  },
  {
    moduleId: 'reports',
    moduleLabel: 'Reports',
    kpis: [
      { id: 'reports-generated', title: 'Reports Generated', value: '1,204', trend: 'up', percentage: 28.4, comparison: 'vs last month', icon: 'bar-chart', color: '#2f6f4f' },
      { id: 'reports-scheduled', title: 'Scheduled', value: '42', trend: 'up', percentage: 5.6, comparison: 'vs last month', icon: 'calendar', color: '#1d9bf0' },
      { id: 'reports-avg-time', title: 'Avg Gen Time', value: '3.2s', trend: 'down', percentage: 12.1, comparison: 'vs last month', icon: 'clock', color: '#7c3aed' },
      { id: 'reports-failed', title: 'Failed', value: '3', trend: 'down', percentage: 40.0, comparison: 'vs last week', icon: 'alert-triangle', color: '#ef4444' },
    ],
  },
  {
    moduleId: 'analytics',
    moduleLabel: 'Analytics',
    kpis: [
      { id: 'analytics-visitors', title: 'Unique Visitors', value: '284K', trend: 'up', percentage: 14.7, comparison: 'vs last month', icon: 'users', color: '#2f6f4f' },
      { id: 'analytics-views', title: 'Page Views', value: '1.2M', trend: 'up', percentage: 19.3, comparison: 'vs last month', icon: 'eye', color: '#1d9bf0' },
      { id: 'analytics-conv', title: 'Conversion Rate', value: '3.8%', trend: 'up', percentage: 0.6, comparison: 'vs last month', icon: 'target', color: '#7c3aed' },
      { id: 'analytics-bounce', title: 'Bounce Rate', value: '41.2%', trend: 'down', percentage: 3.4, comparison: 'vs last month', icon: 'trending-down', color: '#d97706' },
    ],
  },
];

export function getAllModuleKPIs(): KPIData[] {
  return MODULE_KPIS.flatMap((m) => m.kpis);
}
