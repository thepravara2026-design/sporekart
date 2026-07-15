import type { DataGridColumn } from '../components/data-grid/types';

export interface MockModule {
  id: string;
  label: string;
  icon: string;
  description: string;
  columns: DataGridColumn<any>[];
  data: Record<string, any>[];
}

function mockCol(key: string, header: string, width = 140): DataGridColumn<any> {
  return { key, header, width: `${width}px` };
}

export const MOCK_MODULES: Record<string, MockModule> = {
  products: {
    id: 'products', label: 'Products', icon: 'package', description: 'Manage product catalog',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('category', 'Category', 120), mockCol('price', 'Price', 100), mockCol('stock', 'Stock', 80),
      mockCol('sku', 'SKU', 120), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `PRD-${1000 + i}`, name: `Product ${i + 1}`, status: ['Active', 'Draft', 'Archived'][i % 3],
      category: ['Electronics', 'Clothing', 'Food', 'Books', 'Sports'][i % 5],
      price: `$${(Math.random() * 1000 + 10).toFixed(2)}`, stock: Math.floor(Math.random() * 500),
      sku: `SKU-${String(100000 + i).slice(1)}`,
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  inventory: {
    id: 'inventory', label: 'Inventory', icon: 'archive', description: 'Track stock levels and warehouse inventory',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('warehouse', 'Warehouse', 140), mockCol('stockLevel', 'Stock Level', 100), mockCol('reorderPoint', 'Reorder Pt', 100),
      mockCol('location', 'Location', 140), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `INV-${2000 + i}`, name: `Inventory Item ${i + 1}`, status: ['In Stock', 'Low Stock', 'Out of Stock'][i % 3],
      warehouse: ['Warehouse A', 'Warehouse B', 'Warehouse C'][i % 3],
      stockLevel: Math.floor(Math.random() * 1000), reorderPoint: Math.floor(Math.random() * 100 + 10),
      location: `Aisle ${(i % 20) + 1}, Shelf ${(i % 10) + 1}`,
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  orders: {
    id: 'orders', label: 'Orders', icon: 'shopping-cart', description: 'View and manage customer orders',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('customer', 'Customer', 160), mockCol('total', 'Total', 100), mockCol('items', 'Items', 60),
      mockCol('payment', 'Payment', 120), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `ORD-${3000 + i}`, name: `Order #${3000 + i}`,
      status: ['Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled'][i % 5],
      customer: [`Alice Johnson`, `Bob Smith`, `Carol Davis`, `David Wilson`, `Eve Martin`][i % 5],
      total: `$${(Math.random() * 500 + 20).toFixed(2)}`, items: Math.floor(Math.random() * 10 + 1),
      payment: ['Credit Card', 'PayPal', 'Bank Transfer', 'COD'][i % 4],
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  customers: {
    id: 'customers', label: 'Customers', icon: 'users', description: 'Customer directory and profiles',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('email', 'Email', 200), mockCol('phone', 'Phone', 140), mockCol('orders', 'Orders', 60),
      mockCol('totalSpent', 'Total Spent', 120), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `CUST-${4000 + i}`,
      name: [`Alice Johnson`, `Bob Smith`, `Carol Davis`, `David Wilson`, `Eve Martin`, `Frank Brown`, `Grace Lee`, `Henry Kim`, `Ivy Chen`, `Jack Taylor`][i % 10],
      status: ['Active', 'Inactive', 'VIP'][i % 3],
      email: `user${i + 1}@example.com`, phone: `+1-555-${String(1000 + i).slice(1)}`,
      orders: Math.floor(Math.random() * 20), totalSpent: `$${(Math.random() * 5000 + 50).toFixed(2)}`,
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  crm: {
    id: 'crm', label: 'CRM', icon: 'heart', description: 'Lead and opportunity management',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('company', 'Company', 160), mockCol('stage', 'Stage', 120), mockCol('value', 'Value', 120),
      mockCol('assignedTo', 'Assigned To', 140), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `LEAD-${5000 + i}`, name: `Opportunity ${i + 1}`,
      status: ['New', 'Contacted', 'Qualified', 'Proposal', 'Negotiation', 'Closed Won', 'Closed Lost'][i % 7],
      company: [`Acme Corp`, `Globex Inc`, `Initech`, `Umbrella Corp`, `Cyberdyne`][i % 5],
      stage: ['Discovery', 'Demo', 'Proposal', 'Negotiation', 'Closed'][i % 5],
      value: `$${(Math.random() * 100000 + 1000).toFixed(2)}`,
      assignedTo: [`Alice`, `Bob`, `Carol`, `David`][i % 4],
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  training: {
    id: 'training', label: 'Training', icon: 'book-open', description: 'Course catalog and training materials',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('duration', 'Duration', 80), mockCol('enrolled', 'Enrolled', 80), mockCol('instructor', 'Instructor', 140),
      mockCol('level', 'Level', 100), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `CRS-${6000 + i}`, name: [`Introduction to`, `Advanced`, `Professional`][i % 3] + ` Course ${i + 1}`,
      status: ['Active', 'Coming Soon', 'Archived'][i % 3],
      duration: `${Math.floor(Math.random() * 40 + 2)}h`, enrolled: Math.floor(Math.random() * 200),
      instructor: [`Dr. Smith`, `Prof. Johnson`, `Ms. Davis`, `Mr. Wilson`][i % 4],
      level: ['Beginner', 'Intermediate', 'Advanced'][i % 3],
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  shipping: {
    id: 'shipping', label: 'Shipping', icon: 'truck', description: 'Shipping and delivery management',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('carrier', 'Carrier', 120), mockCol('tracking', 'Tracking', 160), mockCol('eta', 'ETA', 100),
      mockCol('destination', 'Destination', 180), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `SHP-${7000 + i}`, name: `Shipment #${7000 + i}`,
      status: ['Label Created', 'Picked Up', 'In Transit', 'Out for Delivery', 'Delivered', 'Exception'][i % 6],
      carrier: ['FedEx', 'UPS', 'DHL', 'USPS'][i % 4],
      tracking: `TRK${String(80000000 + i).slice(1)}`,
      eta: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      destination: [`New York, NY`, `Los Angeles, CA`, `Chicago, IL`, `Houston, TX`, `Phoenix, AZ`][i % 5],
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  finance: {
    id: 'finance', label: 'Finance', icon: 'credit-card', description: 'Financial transactions and reporting',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('type', 'Type', 100), mockCol('amount', 'Amount', 120), mockCol('account', 'Account', 140),
      mockCol('reference', 'Reference', 140), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `FIN-${8000 + i}`, name: `Transaction ${8000 + i}`,
      status: ['Completed', 'Pending', 'Failed', 'Refunded'][i % 4],
      type: ['Revenue', 'Expense', 'Refund', 'Transfer'][i % 4],
      amount: `$${(Math.random() * 10000 + 10).toFixed(2)}`,
      account: [`Checking`, `Savings`, `Credit Card`, `PayPal`][i % 4],
      reference: `REF-${String(90000000 + i).slice(1)}`,
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  reports: {
    id: 'reports', label: 'Reports', icon: 'bar-chart', description: 'Business reports and analytics',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('category', 'Category', 120), mockCol('generatedBy', 'Generated By', 140), mockCol('format', 'Format', 80),
      mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `RPT-${9000 + i}`, name: [`Monthly Sales`, `Inventory Summary`, `Customer Analysis`, `Financial Report`, `Shipping Log`][i % 5],
      status: ['Ready', 'Generating', 'Failed'][i % 3],
      category: ['Sales', 'Inventory', 'Customer', 'Financial', 'Shipping'][i % 5],
      generatedBy: [`Alice`, `Bob`, `Carol`, `System`][i % 4],
      format: ['PDF', 'CSV', 'Excel', 'HTML'][i % 4],
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  analytics: {
    id: 'analytics', label: 'Analytics', icon: 'trending-up', description: 'Platform analytics and insights',
    columns: [
      mockCol('id', 'ID', 80), mockCol('name', 'Name', 200), mockCol('status', 'Status', 100),
      mockCol('metric', 'Metric', 140), mockCol('value', 'Value', 120), mockCol('change', 'Change', 100),
      mockCol('period', 'Period', 120), mockCol('createdAt', 'Created', 120), mockCol('updatedAt', 'Updated', 120),
    ],
    data: Array.from({ length: 50 }, (_, i) => ({
      id: `ANL-${10000 + i}`, name: `Analytics ${10000 + i}`,
      status: ['Active', 'Updating', 'Paused'][i % 3],
      metric: [`Page Views`, `Unique Visitors`, `Conversion Rate`, `Avg. Session`, `Bounce Rate`][i % 5],
      value: i % 5 === 2 ? `${(Math.random() * 10 + 1).toFixed(1)}%` : i % 5 === 3 ? `${Math.floor(Math.random() * 300 + 60)}s` : `${Math.floor(Math.random() * 10000 + 100)}`,
      change: `${(Math.random() > 0.5 ? '+' : '-')}${(Math.random() * 25).toFixed(1)}%`,
      period: ['Today', 'This Week', 'This Month', 'This Quarter'][i % 4],
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    })),
  },
  settings: {
    id: 'settings', label: 'Settings', icon: 'settings', description: 'System and application settings',
    columns: [
      mockCol('id', 'ID', 80), mockCol('setting', 'Setting', 200), mockCol('value', 'Value', 200),
      mockCol('group', 'Group', 140), mockCol('updatedAt', 'Last Updated', 120),
    ],
    data: Array.from({ length: 30 }, (_, i) => ({
      id: `SET-${11000 + i}`,
      setting: [`Site Name`, `Default Language`, `Timezone`, `Currency`, `Max Upload Size`, `Session Timeout`, `Password Policy`, `Two-Factor Auth`, `Email Provider`, `SMTP Host`, `SMTP Port`, `Maintenance Mode`, `Debug Mode`, `API Rate Limit`, `Cache TTL`, `Log Retention`, `Max Login Attempts`, `Lockout Duration`, `Min Password Length`, `Password Expiry`, `Default Role`, `Registration Enabled`, `Notification Email`, `Backup Schedule`, `Backup Retention`, `Storage Provider`, `CDN URL`, `Analytics ID`, `GDPR Enabled`, `Cookie Consent`][i],
      value: [`SporeKart`, `English`, `UTC`, `USD`, `50MB`, `3600`, `Strong`, `Disabled`, `SMTP`, `smtp.example.com`, `587`, `Disabled`, `Disabled`, `100`, `3600`, `90`, `5`, `900`, `8`, `90`, `User`, `Enabled`, `admin@example.com`, `Daily`, `30`, `S3`, `cdn.example.com`, `UA-XXXXX`, `Enabled`, `Enabled`][i],
      group: [`General`, `General`, `General`, `General`, `System`, `Security`, `Security`, `Security`, `Email`, `Email`, `Email`, `System`, `System`, `System`, `System`, `System`, `Security`, `Security`, `Security`, `Security`, `Users`, `Users`, `Email`, `Backup`, `Backup`, `Storage`, `Storage`, `Analytics`, `Privacy`, `Privacy`][i],
      updatedAt: `2026-07-${String((i % 14) + 1).padStart(2, '0')}`,
    })),
  },
};
