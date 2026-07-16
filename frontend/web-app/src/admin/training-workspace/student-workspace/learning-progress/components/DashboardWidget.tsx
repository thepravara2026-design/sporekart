import { memo } from 'react';

interface DashboardWidgetProps {
  label: string;
  value: string | number;
  variant?: 'default' | 'success' | 'warning' | 'danger' | 'info';
  subtitle?: string;
}

const variantStyles: Record<string, { bg: string; border: string }> = {
  default: { bg: 'var(--color-bg-surface-default)', border: 'var(--color-border-default)' },
  success: { bg: '#f0fdf4', border: '#16a34a' },
  warning: { bg: '#fefce8', border: '#ca8a04' },
  danger: { bg: '#fef2f2', border: '#dc2626' },
  info: { bg: '#eff6ff', border: '#2563eb' },
};

export const DashboardWidget = memo(function DashboardWidget({ label, value, variant = 'default', subtitle }: DashboardWidgetProps) {
  const styles = variantStyles[variant];
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${styles.border}`, background: styles.bg,
      display: 'flex', flexDirection: 'column', gap: 4,
    }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{label}</span>
      <span style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', lineHeight: 1.2 }}>{value}</span>
      {subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{subtitle}</span>}
    </div>
  );
});
