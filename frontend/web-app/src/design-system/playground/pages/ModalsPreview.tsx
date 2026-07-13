import React, { useState } from 'react';
import { StandardModal, LargeModal, FullscreenModal, ImageModal, VideoModal, ScrollableModal, ResponsiveModal, PersistentModal, WizardModal } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

const btnStyle: React.CSSProperties = { background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 };

export default function ModalsPreview() {
  const [standardOpen, setStandardOpen] = useState(false);
  const [largeOpen, setLargeOpen] = useState(false);
  const [fullscreenOpen, setFullscreenOpen] = useState(false);
  const [imageOpen, setImageOpen] = useState(false);
  const [videoOpen, setVideoOpen] = useState(false);
  const [scrollableOpen, setScrollableOpen] = useState(false);
  const [responsiveOpen, setResponsiveOpen] = useState(false);
  const [persistentOpen, setPersistentOpen] = useState(false);
  const [wizardOpen, setWizardOpen] = useState(false);

  const longContent = Array.from({ length: 20 }).map((_, i) => (
    <p key={i} style={{ margin: '0 0 8px' }}>This is scrollable content paragraph number {i + 1}. It demonstrates how the modal handles overflow with a scrollable body.</p>
  ));

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Modals</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Modal variants, sizes, and states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Size Variants</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Standard Modal (sm)">
            <button style={btnStyle} onClick={() => setStandardOpen(true)}>Open Standard</button>
            <StandardModal open={standardOpen} onClose={() => setStandardOpen(false)} title="Standard Modal">
              <p style={{ margin: 0 }}>A compact modal for simple interactions and confirmations.</p>
            </StandardModal>
          </StateCard>
          <StateCard label="Large Modal (lg)">
            <button style={btnStyle} onClick={() => setLargeOpen(true)}>Open Large</button>
            <LargeModal open={largeOpen} onClose={() => setLargeOpen(false)} title="Large Modal">
              <p style={{ margin: 0 }}>A wider modal suitable for forms and detailed content.</p>
            </LargeModal>
          </StateCard>
          <StateCard label="Fullscreen Modal">
            <button style={btnStyle} onClick={() => setFullscreenOpen(true)}>Open Fullscreen</button>
            <FullscreenModal open={fullscreenOpen} onClose={() => setFullscreenOpen(false)} title="Fullscreen Modal">
              <p style={{ margin: 0 }}>Takes the full viewport for immersive editing experiences.</p>
            </FullscreenModal>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Content Modals</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Image Modal">
            <button style={btnStyle} onClick={() => setImageOpen(true)}>Open Image</button>
            <ImageModal open={imageOpen} onClose={() => setImageOpen(false)} src="https://picsum.photos/800/500" alt="Sample image" />
          </StateCard>
          <StateCard label="Video Modal">
            <button style={btnStyle} onClick={() => setVideoOpen(true)}>Open Video</button>
            <VideoModal open={videoOpen} onClose={() => setVideoOpen(false)} title="Video Player" src="https://www.w3schools.com/html/mov_bbb.mp4" />
          </StateCard>
          <StateCard label="Scrollable Modal">
            <button style={btnStyle} onClick={() => setScrollableOpen(true)}>Open Scrollable</button>
            <ScrollableModal open={scrollableOpen} onClose={() => setScrollableOpen(false)} title="Scrollable Content">
              {longContent}
            </ScrollableModal>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Special Modals</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Responsive Modal">
            <button style={btnStyle} onClick={() => setResponsiveOpen(true)}>Open Responsive</button>
            <ResponsiveModal open={responsiveOpen} onClose={() => setResponsiveOpen(false)} title="Responsive Modal">
              <p style={{ margin: 0 }}>Becomes a bottom sheet on mobile viewports.</p>
            </ResponsiveModal>
          </StateCard>
          <StateCard label="Persistent Modal">
            <button style={btnStyle} onClick={() => setPersistentOpen(true)}>Open Persistent</button>
            <PersistentModal open={persistentOpen} onClose={() => setPersistentOpen(false)} title="Persistent Modal">
              <p style={{ margin: 0 }}>Cannot be closed by clicking the overlay or pressing Escape.</p>
            </PersistentModal>
          </StateCard>
          <StateCard label="Wizard Modal">
            <button style={btnStyle} onClick={() => setWizardOpen(true)}>Open Wizard</button>
            <WizardModal open={wizardOpen} onClose={() => setWizardOpen(false)} steps={[{ title: 'Step 1', content: <p>Start</p> }, { title: 'Step 2', content: <p>Middle</p> }, { title: 'Step 3', content: <p>Almost done</p> }, { title: 'Step 4', content: <p>Review</p> }, { title: 'Step 5', content: <p>Finish</p> }]} onFinish={() => setWizardOpen(false)} />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Size Comparison</h2>
        <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
            <div><strong style={{ color: 'var(--color-text-primary)' }}>Standard (sm):</strong> 480px width — Best for simple confirmations and quick actions</div>
            <div><strong style={{ color: 'var(--color-text-primary)' }}>Large (lg):</strong> 800px width — Suitable for forms, detailed views, and content</div>
            <div><strong style={{ color: 'var(--color-text-primary)' }}>Fullscreen:</strong> 100vw x 100vh — Immersive experiences and complex workflows</div>
            <div><strong style={{ color: 'var(--color-text-primary)' }}>Responsive:</strong> Adapts between centered dialog and bottom sheet at 767px breakpoint</div>
            <div><strong style={{ color: 'var(--color-text-primary)' }}>Image/Video:</strong> Optimized for media content with centered framing</div>
          </div>
        </div>
      </section>
    </div>
  );
}
