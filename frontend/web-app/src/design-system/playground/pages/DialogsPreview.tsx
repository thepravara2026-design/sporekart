import React, { useState } from 'react';
import { ConfirmationDialog, AlertDialog, InformationDialog, SuccessDialog, WarningDialog, ErrorDialog, LoadingDialog, FullscreenDialog, ResponsiveDialog, NestedDialog } from '../../components/feedback';

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

export default function DialogsPreview() {
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [alertOpen, setAlertOpen] = useState(false);
  const [infoOpen, setInfoOpen] = useState(false);
  const [successOpen, setSuccessOpen] = useState(false);
  const [warningOpen, setWarningOpen] = useState(false);
  const [errorOpen, setErrorOpen] = useState(false);
  const [loadingOpen, setLoadingOpen] = useState(false);
  const [fullscreenOpen, setFullscreenOpen] = useState(false);
  const [responsiveOpen, setResponsiveOpen] = useState(false);
  const [nestedOpen, setNestedOpen] = useState(false);
  const [childOpen, setChildOpen] = useState(false);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Dialogs</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Dialog variants, sizes, and states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Confirm & Alert Dialogs</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Confirmation Dialog">
            <button style={btnStyle} onClick={() => setConfirmOpen(true)}>Open Confirm</button>
            <ConfirmationDialog open={confirmOpen} onClose={() => setConfirmOpen(false)} onConfirm={() => { setConfirmOpen(false); }} title="Confirm Action" message="Are you sure you want to proceed with this action?" />
          </StateCard>
          <StateCard label="Alert Dialog">
            <button style={btnStyle} onClick={() => setAlertOpen(true)}>Open Alert</button>
            <AlertDialog open={alertOpen} onClose={() => setAlertOpen(false)} title="Alert" message="This is an alert dialog message." />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Contextual Dialogs</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Information Dialog">
            <button style={btnStyle} onClick={() => setInfoOpen(true)}>Open Info</button>
            <InformationDialog open={infoOpen} onClose={() => setInfoOpen(false)} title="Information" message="Here is some useful information for you." />
          </StateCard>
          <StateCard label="Success Dialog">
            <button style={btnStyle} onClick={() => setSuccessOpen(true)}>Open Success</button>
            <SuccessDialog open={successOpen} onClose={() => setSuccessOpen(false)} title="Success" message="Your changes have been saved successfully." />
          </StateCard>
          <StateCard label="Warning Dialog">
            <button style={{ ...btnStyle, background: 'var(--color-bg-warning-default, #f59e0b)' }} onClick={() => setWarningOpen(true)}>Open Warning</button>
            <WarningDialog open={warningOpen} onClose={() => setWarningOpen(false)} title="Warning" message="This action may have unintended consequences." />
          </StateCard>
          <StateCard label="Error Dialog">
            <button style={{ ...btnStyle, background: 'var(--color-bg-danger-default, #dc2626)' }} onClick={() => setErrorOpen(true)}>Open Error</button>
            <ErrorDialog open={errorOpen} onClose={() => setErrorOpen(false)} title="Error" message="Something went wrong. Please try again." />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Special Dialogs</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Loading Dialog">
            <button style={btnSecondary} onClick={() => setLoadingOpen(true)}>Open Loading</button>
            <LoadingDialog open={loadingOpen} onClose={() => setLoadingOpen(false)} title="Processing" message="Please wait while we process your request..." />
          </StateCard>
          <StateCard label="Fullscreen Dialog">
            <button style={btnSecondary} onClick={() => setFullscreenOpen(true)}>Open Fullscreen</button>
            <FullscreenDialog open={fullscreenOpen} onClose={() => setFullscreenOpen(false)} title="Fullscreen Dialog">
              <p style={{ margin: 0 }}>This dialog takes up the full viewport. Use for immersive experiences.</p>
            </FullscreenDialog>
          </StateCard>
          <StateCard label="Responsive Dialog">
            <button style={btnSecondary} onClick={() => setResponsiveOpen(true)}>Open Responsive</button>
            <ResponsiveDialog open={responsiveOpen} onClose={() => setResponsiveOpen(false)} title="Responsive Dialog">
              <p style={{ margin: 0 }}>On mobile this becomes a bottom sheet. Resize the viewport to see the behavior.</p>
            </ResponsiveDialog>
          </StateCard>
          <StateCard label="Nested Dialog">
            <button style={btnSecondary} onClick={() => setNestedOpen(true)}>Open Nested</button>
            <NestedDialog open={nestedOpen} onClose={() => setNestedOpen(false)} title="Parent Dialog" childDialog={childOpen ? { title: 'Child Dialog', children: <p>This is a nested child dialog.</p> } : undefined} onChildClose={() => setChildOpen(false)}>
              <p style={{ margin: 0 }}>Click the button below to open a child dialog on top.</p>
              <button style={{ ...btnStyle, marginTop: '12px' }} onClick={() => setChildOpen(true)}>Open Child</button>
            </NestedDialog>
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Keyboard Navigation</h2>
        <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
          <ul style={{ margin: 0, padding: '0 0 0 20px', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <li><strong>Escape</strong> - Close the currently open dialog</li>
            <li><strong>Tab / Shift+Tab</strong> - Cycle focus through focusable elements within the dialog</li>
            <li><strong>Enter</strong> - Confirm the primary action</li>
            <li>Focus is trapped within the dialog while open</li>
            <li>Upon closing, focus returns to the element that triggered the dialog</li>
          </ul>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Responsive Behavior</h2>
        <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
          <ul style={{ margin: 0, padding: '0 0 0 20px', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <li>Standard dialogs maintain width and center alignment on all viewports</li>
            <li>Responsive dialogs render as bottom sheets on viewports &lt; 768px</li>
            <li>Fullscreen dialogs occupy the entire viewport regardless of screen size</li>
            <li>All dialogs cap their height to prevent overflow and scroll content</li>
            <li>Nested dialogs overlay on top of their parent with increased z-index</li>
          </ul>
        </div>
      </section>
    </div>
  );
}
