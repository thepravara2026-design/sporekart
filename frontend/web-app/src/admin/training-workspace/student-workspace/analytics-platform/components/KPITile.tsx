import { memo } from 'react';
import type { KPIData } from '../types';

const variantColors: Record<string, { bg: string; border: string; text: string }> = {
  default: { bg: 'var(--color-bg-surface-default)', border: 'var(--color-border-default)', text: 'var(--color-text-primary)' },
  success: { bg: '#f0fdf4', border: '#16a34a', text: '#16a34a' },
  warning: { bg: '#fefce8', border: '#ca8a04', text: '#ca8a04' },
  danger: { bg: '#fef2f2', border: '#dc2626', text: '#dc2626' },
  info: { bg: '#eff6ff', border: '#2563eb', text: '#2563eb' },
};

export const KPITile = memo(function KPITile({ data }: { data: KPIData }) {
  const colors = variantColors[data.variant] || variantColors.default;
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${colors.border}`, background: colors.bg,
      display: 'flex', flexDirection: 'column', gap: 6,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{data.label}</span>
        <span style={{ fontSize: 16 }}>{data.icon}</span>
      </div>
      <div style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', color: colors.text }}>{data.value}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 4, fontSize: 'var(--text-caption)' }}>
        <span style={{ color: data.changeType === 'increase' ? '#16a34a' : data.changeType === 'decrease' ? '#dc2626' : '#6b7280' }}>
          {data.changeType === 'increase' ? '↑' : data.changeType === 'decrease' ? '↓' : '→'} {Math.abs(data.change)}%
        </span>
        <span style={{ color: 'var(--color-text-tertiary)' }}>vs last month</span>
      </div>
    </div>
  );
});
