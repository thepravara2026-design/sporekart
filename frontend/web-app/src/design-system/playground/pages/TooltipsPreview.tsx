import React from 'react';
import { Tooltip, RichTooltip, IconTooltip, DelayedTooltip, TooltipProvider } from '../../components/feedback';
import type { TooltipPosition } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const btnStyle: React.CSSProperties = { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 };

const positions: TooltipPosition[] = ['top', 'bottom', 'left', 'right'];

export default function TooltipsPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Tooltips</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Tooltip variants, positions, and triggers</p>
      </div>

      <TooltipProvider>
        <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Tooltip Variants</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
            <StateCard label="Standard Tooltip">
              <Tooltip content="This is a standard tooltip">
                <button style={btnStyle}>Hover me</button>
              </Tooltip>
            </StateCard>
            <StateCard label="Rich Tooltip">
              <RichTooltip content={<div><strong>Rich content</strong><p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)' }}>With additional description text and formatting.</p></div>}>
                <button style={btnStyle}>Hover for rich</button>
              </RichTooltip>
            </StateCard>
            <StateCard label="Icon Tooltip">
              <IconTooltip content="Settings" />
            </StateCard>
            <StateCard label="Delayed Tooltip">
              <DelayedTooltip content="Appears after 1 second" showDelay={1000}>
                <button style={btnStyle}>Hover (1s delay)</button>
              </DelayedTooltip>
            </StateCard>
          </div>
        </section>

        <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Tooltip Positions</h2>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
            {positions.map((pos) => (
              <StateCard key={pos} label={`Position: ${pos}`}>
                <Tooltip content={`This tooltip appears on the ${pos}`} position={pos}>
                  <button style={btnStyle}>Hover ({pos})</button>
                </Tooltip>
              </StateCard>
            ))}
          </div>
        </section>
      </TooltipProvider>
    </div>
  );
}
