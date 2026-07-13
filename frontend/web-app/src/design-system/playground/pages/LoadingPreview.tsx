import React, { useState } from 'react';
import { GlobalLoadingOverlay, SectionLoader, InlineLoader, PageLoader, Spinner, ShimmerLoader, ProgressiveLoader } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const btnStyle: React.CSSProperties = { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 };

export default function LoadingPreview() {
  const [globalLoading, setGlobalLoading] = useState(false);
  const [progProgress, setProgProgress] = useState(35);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Loading</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Loading states, spinners, and loaders</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Global Loading Overlay</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Toggle Overlay">
            <button style={btnStyle} onClick={() => setGlobalLoading(true)}>Show Overlay</button>
            <button style={{ ...btnStyle, background: 'var(--color-bg-secondary-default)' }} onClick={() => setGlobalLoading(false)}>Hide Overlay</button>
            <GlobalLoadingOverlay open={globalLoading} message="Loading your data..." spinner blur />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Section Loader</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Card (3 rows)">
            <SectionLoader type="card" rows={3} />
          </StateCard>
          <StateCard label="List">
            <SectionLoader type="list" />
          </StateCard>
          <StateCard label="Table">
            <SectionLoader type="table" />
          </StateCard>
          <StateCard label="Form">
            <SectionLoader type="form" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Inline & Page Loaders</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Inline Loader">
            <div style={{ display: 'flex', gap: '16px' }}>
              <InlineLoader size="sm" label="Small" />
              <InlineLoader size="md" label="Medium" />
            </div>
          </StateCard>
          <StateCard label="Page Loader (default)">
            <div style={{ maxHeight: 300, overflow: 'hidden', borderRadius: 'var(--radius-card)' }}>
              <PageLoader variant="default" />
            </div>
          </StateCard>
          <StateCard label="Page Loader (card)">
            <div style={{ maxHeight: 300, overflow: 'hidden', borderRadius: 'var(--radius-card)' }}>
              <PageLoader variant="card" />
            </div>
          </StateCard>
          <StateCard label="Page Loader (table)">
            <div style={{ maxHeight: 300, overflow: 'hidden', borderRadius: 'var(--radius-card)' }}>
              <PageLoader variant="table" />
            </div>
          </StateCard>
          <StateCard label="Page Loader (form)">
            <div style={{ maxHeight: 300, overflow: 'hidden', borderRadius: 'var(--radius-card)' }}>
              <PageLoader variant="form" />
            </div>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Spinner</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Sizes">
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <Spinner size="sm" />
              <Spinner size="md" />
              <Spinner size="lg" />
              <Spinner size="xl" />
            </div>
          </StateCard>
          <StateCard label="Colors">
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <Spinner size="md" color="var(--color-bg-primary-default)" />
              <Spinner size="md" color="var(--color-success-500)" />
              <Spinner size="md" color="var(--color-warning-500)" />
              <Spinner size="md" color="var(--color-danger-500)" />
            </div>
          </StateCard>
          <StateCard label="Speeds">
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
              <Spinner size="md" speed="slow" />
              <Spinner size="md" speed="normal" />
              <Spinner size="md" speed="fast" />
            </div>
          </StateCard>
          <StateCard label="With Label">
            <Spinner size="md" label="Loading..." />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Shimmer Loader</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Text variant">
            <ShimmerLoader variant="text" width="80%" count={3} />
          </StateCard>
          <StateCard label="Circle variant">
            <ShimmerLoader variant="circle" />
          </StateCard>
          <StateCard label="Rect variant">
            <ShimmerLoader variant="rect" height={80} />
          </StateCard>
          <StateCard label="Card variant">
            <ShimmerLoader variant="card" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Progressive Loader</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="With stages">
            <ProgressiveLoader
              progress={progProgress}
              message={`Processing step ${Math.floor(progProgress / 25) + 1} of 4...`}
              stages={[
                { label: 'Validating input', progress: 100 },
                { label: 'Processing data', progress: progProgress > 25 ? 100 : Math.max(0, progProgress * 3) },
                { label: 'Generating output', progress: progProgress > 50 ? Math.min(100, (progProgress - 50) * 4) : 0 },
                { label: 'Finalizing', progress: progProgress > 75 ? Math.min(100, (progProgress - 75) * 4) : 0 },
              ]}
            />
          </StateCard>
          <StateCard label="Controls">
            <div style={{ display: 'flex', gap: '8px', width: '100%' }}>
              <button style={btnStyle} onClick={() => setProgProgress((p) => Math.min(100, p + 5))}>+5%</button>
              <button style={{ ...btnStyle, background: 'var(--color-bg-secondary-default)' }} onClick={() => setProgProgress(0)}>Reset</button>
            </div>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Progress: {progProgress}%</span>
          </StateCard>
        </div>
      </section>
    </div>
  );
}
