import React from 'react';

type AdminAction =
  | 'business_overview'
  | 'sales_report'
  | 'inventory_check'
  | 'customer_insights'
  | 'training_status'
  | 'generate_report'
  | 'view_alerts'
  | 'platform_health';

interface AdminQuickActionsProps {
  onAction: (action: AdminAction) => void;
}

interface ActionItem {
  id: AdminAction;
  label: string;
  icon: string;
  description: string;
}

const actions: ActionItem[] = [
  {
    id: 'business_overview',
    label: 'Business Overview',
    icon: '\uD83D\uDCCA',
    description: 'View key metrics at a glance'
  },
  {
    id: 'sales_report',
    label: 'Sales Report',
    icon: '\uD83D\uDCC8',
    description: 'Revenue and order analysis'
  },
  {
    id: 'inventory_check',
    label: 'Inventory Check',
    icon: '\uD83D\uDCE6',
    description: 'Stock levels and alerts'
  },
  {
    id: 'customer_insights',
    label: 'Customer Insights',
    icon: '\uD83D\uDC65',
    description: 'Customer behavior and segments'
  },
  {
    id: 'training_status',
    label: 'Training Status',
    icon: '\uD83C\uDF93',
    description: 'Training progress and completion'
  },
  {
    id: 'generate_report',
    label: 'Generate Report',
    icon: '\uD83D\uDCC4',
    description: 'Create custom reports'
  },
  {
    id: 'view_alerts',
    label: 'View Alerts',
    icon: '\uD83D\uDD14',
    description: 'Active alerts and notifications'
  },
  {
    id: 'platform_health',
    label: 'Platform Health',
    icon: '\u2699\uFE0F',
    description: 'System status and uptime'
  }
];

const AdminQuickActions: React.FC<AdminQuickActionsProps> = ({ onAction }) => {
  return (
    <div style={{
      backgroundColor: '#ffffff',
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e5e7eb',
      display: 'flex',
      flexDirection: 'column',
      gap: '12px'
    }}>
      <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>
        Quick Actions
      </h3>
      <p style={{ fontSize: '13px', color: '#6b7280', margin: 0 }}>
        Common admin tasks and reports
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px' }}>
        {actions.map((action) => (
          <button
            key={action.id}
            onClick={() => onAction(action.id)}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '10px',
              padding: '10px 14px',
              backgroundColor: '#f9fafb',
              border: '1px solid #e5e7eb',
              borderRadius: '8px',
              cursor: 'pointer',
              textAlign: 'left',
              transition: 'all 0.15s',
              width: '100%'
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.backgroundColor = '#f3f4f6';
              e.currentTarget.style.borderColor = '#d1d5db';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.backgroundColor = '#f9fafb';
              e.currentTarget.style.borderColor = '#e5e7eb';
            }}
          >
            <span style={{ fontSize: '20px', lineHeight: 1 }}>{action.icon}</span>
            <div>
              <div style={{ fontSize: '13px', fontWeight: 600, color: '#111827' }}>{action.label}</div>
              <div style={{ fontSize: '11px', color: '#9ca3af', marginTop: '1px' }}>{action.description}</div>
            </div>
          </button>
        ))}
      </div>
    </div>
  );
};

export default AdminQuickActions;
export type { AdminAction };
