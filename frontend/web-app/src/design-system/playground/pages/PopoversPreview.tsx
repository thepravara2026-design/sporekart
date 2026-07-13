import React, { useState, useRef } from 'react';
import { Popover, InformationPopover, ActionPopover, ContextPopover, InteractivePopover, NestedPopover } from '../../components/feedback';
import type { PopoverPosition } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const btnStyle: React.CSSProperties = { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 };

export default function PopoversPreview() {
  const [infoOpen, setInfoOpen] = useState(false);
  const [actionOpen, setActionOpen] = useState(false);
  const [contextOpen, setContextOpen] = useState(false);
  const [interactiveOpen, setInteractiveOpen] = useState(false);
  const [nestedOpen, setNestedOpen] = useState(false);

  const infoRef = useRef<HTMLButtonElement>(null);
  const actionRef = useRef<HTMLButtonElement>(null);
  const contextRef = useRef<HTMLButtonElement>(null);
  const interactiveRef = useRef<HTMLButtonElement>(null);
  const nestedRef = useRef<HTMLButtonElement>(null);

  const [posOpen, setPosOpen] = useState<Record<string, boolean>>({});
  const posRefs = {
    top: useRef<HTMLButtonElement>(null),
    bottom: useRef<HTMLButtonElement>(null),
    left: useRef<HTMLButtonElement>(null),
    right: useRef<HTMLButtonElement>(null),
    auto: useRef<HTMLButtonElement>(null),
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Popovers</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Popover variants, positions, and trigger interactions</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Popover Variants</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Information Popover">
            <button ref={infoRef} style={btnStyle} onClick={() => setInfoOpen((p) => !p)}>Toggle Info</button>
            <InformationPopover open={infoOpen} onClose={() => setInfoOpen(false)} anchorEl={infoRef.current} title="Information">
              <p style={{ margin: 0, fontSize: 'var(--text-body-sm)' }}>This popover provides helpful information about a feature.</p>
            </InformationPopover>
          </StateCard>
          <StateCard label="Action Popover">
            <button ref={actionRef} style={btnStyle} onClick={() => setActionOpen((p) => !p)}>Toggle Actions</button>
            <ActionPopover open={actionOpen} onClose={() => setActionOpen(false)} anchorEl={actionRef.current} items={[
              { label: 'Edit', onClick: () => setActionOpen(false) },
              { label: 'Duplicate', onClick: () => setActionOpen(false) },
              { label: 'Delete', onClick: () => setActionOpen(false), destructive: true },
            ]} />
          </StateCard>
          <StateCard label="Context Popover">
            <button ref={contextRef} style={btnStyle} onClick={() => setContextOpen((p) => !p)}>Toggle Context</button>
            <ContextPopover open={contextOpen} onClose={() => setContextOpen(false)} anchorEl={contextRef.current}>
              <p style={{ margin: 0, fontSize: 'var(--text-body-sm)' }}>Context-specific actions and information.</p>
            </ContextPopover>
          </StateCard>
          <StateCard label="Interactive Popover">
            <button ref={interactiveRef} style={btnStyle} onClick={() => setInteractiveOpen((p) => !p)}>Toggle Interactive</button>
            <InteractivePopover open={interactiveOpen} onClose={() => setInteractiveOpen(false)} anchorEl={interactiveRef.current}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', padding: '4px 0' }}>
                <input placeholder="Type here..." style={{ padding: '8px', borderRadius: 'var(--radius-input)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)' }} />
                <button style={btnStyle} onClick={() => setInteractiveOpen(false)}>Submit</button>
              </div>
            </InteractivePopover>
          </StateCard>
          <StateCard label="Nested Popover">
            <button ref={nestedRef} style={btnStyle} onClick={() => setNestedOpen((p) => !p)}>Toggle Nested</button>
            <NestedPopover open={nestedOpen} onClose={() => setNestedOpen(false)} anchorEl={nestedRef.current}>
              <p style={{ margin: 0, fontSize: 'var(--text-body-sm)' }}>Nested popovers with parent-child relationships.</p>
            </NestedPopover>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Popover Positions</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          {(Object.keys(posRefs) as PopoverPosition[]).map((pos) => (
            <StateCard key={pos} label={`Position: ${pos}`}>
              <button ref={posRefs[pos]} style={btnStyle} onClick={() => setPosOpen((p) => ({ ...p, [pos]: !p[pos] }))}>Toggle {pos}</button>
              <Popover open={!!posOpen[pos]} onClose={() => setPosOpen((p) => ({ ...p, [pos]: false }))} anchorEl={posRefs[pos].current} position={pos === 'auto' ? 'auto' : pos}>
                <div style={{ padding: '12px', fontSize: 'var(--text-body-sm)' }}>Popover on the {pos}</div>
              </Popover>
            </StateCard>
          ))}
        </div>
      </section>
    </div>
  );
}
