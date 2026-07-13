import React, { useState, useEffect } from 'react';
import { LinearProgress, CircularProgress, StepProgress, IndeterminateProgress, UploadProgress, TaskProgress } from '../../components/feedback';
import type { StepProgressStep } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

function ProgressDemo() {
  const [val, setVal] = useState(0);
  useEffect(() => {
    const t = setInterval(() => setVal((p) => (p >= 100 ? 0 : p + 1)), 100);
    return () => clearInterval(t);
  }, []);
  return <LinearProgress value={val} size="md" label="Progressing..." showValue />;
}

  const steps: StepProgressStep[] = [
  { label: 'Cart', completed: true },
  { label: 'Shipping', completed: true },
  { label: 'Payment', active: true },
  { label: 'Confirmation' },
  { label: 'Delivery' },
];

const uploadTasks = [
  { id: '1', label: 'Initialize build', status: 'completed' as const },
  { id: '2', label: 'Install dependencies', status: 'completed' as const },
  { id: '3', label: 'Run linter', status: 'processing' as const },
  { id: '4', label: 'Run tests', status: 'pending' as const },
  { id: '5', label: 'Build artifacts', status: 'pending' as const },
  { id: '6', label: 'Deploy to staging', status: 'pending' as const },
];

export default function ProgressPreview() {
  const [uploadProgress, setUploadProgress] = useState(45);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Progress</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Progress types, states, and configurations</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Linear Progress</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Determinate (animated)">
            <ProgressDemo />
          </StateCard>
          <StateCard label="Different Sizes">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', width: '100%' }}>
              <LinearProgress value={60} size="sm" label="Small" />
              <LinearProgress value={60} size="md" label="Medium" />
              <LinearProgress value={60} size="lg" label="Large" />
            </div>
          </StateCard>
          <StateCard label="Different Colors">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', width: '100%' }}>
              <LinearProgress value={60} color="primary" label="Primary" />
              <LinearProgress value={60} color="success" label="Success" />
              <LinearProgress value={60} color="warning" label="Warning" />
              <LinearProgress value={60} color="danger" label="Danger" />
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Circular Progress</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Determinate">
            <CircularProgress value={70} size={64} showValue />
          </StateCard>
          <StateCard label="Indeterminate">
            <CircularProgress size={48} />
          </StateCard>
          <StateCard label="Custom Sizes">
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <CircularProgress value={50} size={32} />
              <CircularProgress value={75} size={48} showValue />
              <CircularProgress value={90} size={64} showValue />
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Step Progress</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Horizontal Steps">
            <StepProgress steps={steps} orientation="horizontal" size="md" />
          </StateCard>
          <StateCard label="Vertical Steps">
            <StepProgress steps={steps} orientation="vertical" size="sm" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Special Progress Types</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Indeterminate">
            <IndeterminateProgress size="md" />
          </StateCard>
          <StateCard label="Upload Progress">
            <UploadProgress fileName="report-q2-2026.pdf" fileSize="2.4 MB" progress={uploadProgress} onCancel={() => setUploadProgress(0)} />
          </StateCard>
          <StateCard label="Task Progress">
            <TaskProgress tasks={uploadTasks} overallProgress={40} />
          </StateCard>
        </div>
      </section>
    </div>
  );
}
