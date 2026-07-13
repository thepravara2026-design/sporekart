import React, { useState } from 'react';
import { Alert, InlineAlert, PageAlert, DismissibleAlert, SuccessAlert, WarningAlert, InformationAlert, ErrorAlert, PersistentAlert } from '../../components/feedback';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

export default function AlertsPreview() {
  const [dismissibleVisible, setDismissibleVisible] = useState(true);

  if (!dismissibleVisible) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
        <div>
          <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alerts</h1>
          <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Alert types, variants, and states</p>
        </div>
        <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', textAlign: 'center' }}>
          <p style={{ margin: '0 0 12px', color: 'var(--color-text-secondary)' }}>Alert was dismissed.</p>
          <button style={{ background: 'var(--color-bg-primary-default)', color: '#fff', border: 'none', padding: '8px 16px', fontSize: 'var(--text-button)', borderRadius: 'var(--radius-btn)', cursor: 'pointer', fontWeight: 500 }} onClick={() => setDismissibleVisible(true)}>Reset</button>
        </div>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alerts</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All Alert types, variants, and states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Alert Types</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Success Alert">
            <Alert type="success" title="Success" message="Operation completed successfully." />
          </StateCard>
          <StateCard label="Warning Alert">
            <Alert type="warning" title="Warning" message="Please review your settings before proceeding." />
          </StateCard>
          <StateCard label="Info Alert">
            <Alert type="info" title="Information" message="A new software update is available." />
          </StateCard>
          <StateCard label="Error Alert">
            <Alert type="error" title="Error" message="An unexpected error occurred. Please try again." />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Alert Variants</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Inline Alert">
            <InlineAlert type="info" message="Your session will expire in 5 minutes." />
          </StateCard>
          <StateCard label="Page Alert">
            <PageAlert type="warning" title="Notice" message="This page is using deprecated APIs." />
          </StateCard>
          <StateCard label="Dismissible Alert">
            {dismissibleVisible && (
              <DismissibleAlert type="info" title="Tip" message="You can dismiss this alert." onClose={() => setDismissibleVisible(false)} />
            )}
          </StateCard>
          <StateCard label="Persistent Alert">
            <PersistentAlert type="warning" title="Important" message="This alert cannot be dismissed." />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Contextual Alerts</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="SuccessAlert">
            <SuccessAlert title="Changes saved" message="Your profile has been updated." />
          </StateCard>
          <StateCard label="WarningAlert">
            <WarningAlert title="Low disk space" message="You have less than 1GB remaining." />
          </StateCard>
          <StateCard label="InformationAlert">
            <InformationAlert title="Did you know?" message="You can customize your dashboard layout." />
          </StateCard>
          <StateCard label="ErrorAlert">
            <ErrorAlert title="Connection lost" message="Please check your internet connection." />
          </StateCard>
        </div>
      </section>
    </div>
  );
}
