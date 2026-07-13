import React, { useState, useCallback } from 'react';
import { ToastContainer } from '../../components/feedback';
import type { ToastItem, ToastPosition } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const btnStyle: React.CSSProperties = { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 };
const btnSecondary: React.CSSProperties = { ...btnStyle, background: 'var(--color-bg-secondary-default)' };

const positions: ToastPosition[] = ['top-left', 'top-center', 'top-right', 'bottom-left', 'bottom-center', 'bottom-right'];

export default function ToastsPreview() {
  const [toasts, setToasts] = useState<ToastItem[]>([]);
  const [position, setPosition] = useState<ToastPosition>('bottom-right');

  const addToast = useCallback((type: ToastItem['type'], title: string, message?: string, duration?: number, action?: ToastItem['action']) => {
    const id = `t-${Date.now()}-${Math.random().toString(36).slice(2, 6)}`;
    setToasts((prev) => [...prev, { id, type, title, message, duration, action }]);
    if (duration !== 0) {
      setTimeout(() => setToasts((prev) => prev.filter((t) => t.id !== id)), (duration || 5000) + 300);
    }
  }, []);

  const handleClose = useCallback((id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Toasts</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Toast types, durations, and positions</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Toast Types</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Success">
            <button style={{ ...btnStyle, background: 'var(--color-bg-success-default, #16a34a)' }} onClick={() => addToast('success', 'Saved successfully', 'Your changes have been saved.', 4000)}>Trigger Success</button>
          </StateCard>
          <StateCard label="Error">
            <button style={{ ...btnStyle, background: 'var(--color-bg-danger-default, #dc2626)' }} onClick={() => addToast('error', 'Operation failed', 'Something went wrong. Please try again.', 5000)}>Trigger Error</button>
          </StateCard>
          <StateCard label="Warning">
            <button style={{ ...btnStyle, background: 'var(--color-bg-warning-default, #f59e0b)' }} onClick={() => addToast('warning', 'Low disk space', 'Your disk is running low on space.', 5000)}>Trigger Warning</button>
          </StateCard>
          <StateCard label="Info">
            <button style={{ ...btnStyle, background: 'var(--color-bg-secondary-default)' }} onClick={() => addToast('info', 'New update available', 'Version 2.0 is ready to install.', 4000)}>Trigger Info</button>
          </StateCard>
          <StateCard label="Loading (persistent)">
            <button style={btnSecondary} onClick={() => addToast('loading', 'Uploading...', 'Please wait while we process your file.', 0)}>Trigger Loading</button>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Different Durations</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Short (2s)">
            <button style={btnStyle} onClick={() => addToast('success', 'Quick toast', 'Disappears in 2 seconds.', 2000)}>2 seconds</button>
          </StateCard>
          <StateCard label="Medium (4s)">
            <button style={btnStyle} onClick={() => addToast('info', 'Standard toast', 'Default duration.', 4000)}>4 seconds</button>
          </StateCard>
          <StateCard label="Long (8s)">
            <button style={btnStyle} onClick={() => addToast('warning', 'Long toast', 'Stays for 8 seconds.', 8000)}>8 seconds</button>
          </StateCard>
          <StateCard label="Persistent (0)">
            <button style={btnStyle} onClick={() => addToast('info', 'Persistent toast', 'Must be dismissed manually.', 0)}>Never auto-dismiss</button>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Action Buttons</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="With Action">
            <button style={btnStyle} onClick={() => addToast('warning', 'Delete confirmation', 'Are you sure you want to delete this item?', 6000, { label: 'Undo', onClick: () => {} })}>With Undo</button>
          </StateCard>
          <StateCard label="With Dismiss">
            <button style={btnStyle} onClick={() => addToast('error', 'Connection lost', 'Check your internet connection.', 0, { label: 'Retry', onClick: () => {} })}>With Retry</button>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Queue & Position</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Queue Multiple">
            <button style={btnSecondary} onClick={() => { addToast('success', 'Batch 1', 'First toast'); addToast('warning', 'Batch 2', 'Second toast'); addToast('error', 'Batch 3', 'Third toast'); }}>Send 3 at once</button>
          </StateCard>
          <StateCard label="Position">
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '4px' }}>
              {positions.map((p) => (
                <button key={p} style={{ ...btnSecondary, fontSize: 'var(--text-caption)', padding: '4px 8px', background: position === p ? 'var(--color-bg-primary-default)' : 'var(--color-bg-secondary-default)' }} onClick={() => setPosition(p)}>{p.replace('-', ' ')}</button>
              ))}
            </div>
          </StateCard>
        </div>
      </section>

      <ToastContainer toasts={toasts} onClose={handleClose} position={position} />
    </div>
  );
}
