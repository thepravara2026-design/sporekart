import { memo, type ReactNode } from 'react';

interface ChartContainerProps {
  title: string; subtitle?: string; children: ReactNode; height?: number;
}

export const ChartContainer = memo(function ChartContainer({ title, subtitle, children, height = 280 }: ChartContainerProps) {
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20 }}>
      <div style={{ marginBottom: 16 }}>
        <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{title}</span>
        {subtitle && <span style={{ marginLeft: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{subtitle}</span>}
      </div>
      <div style={{ minHeight: height, display: 'flex', alignItems: 'flex-end', justifyContent: 'center' }}>
        {children}
      </div>
    </div>
  );
});
