import { memo } from 'react';

interface ShareOption {
  label: string;
  icon: string;
  placeholder: string;
}

const SHARE_OPTIONS: ShareOption[] = [
  { label: 'Download', icon: '⬇️', placeholder: 'PDF download placeholder' },
  { label: 'Share Link', icon: '🔗', placeholder: 'Share link generation placeholder' },
  { label: 'LinkedIn', icon: '💼', placeholder: 'LinkedIn share placeholder' },
  { label: 'WhatsApp', icon: '📱', placeholder: 'WhatsApp share placeholder' },
  { label: 'Email', icon: '📧', placeholder: 'Email share placeholder' },
  { label: 'Resume', icon: '📄', placeholder: 'Resume attachment placeholder' },
  { label: 'Portfolio', icon: '🎨', placeholder: 'Portfolio embed placeholder' },
  { label: 'Government Portal', icon: '🏛️', placeholder: 'Government portal share placeholder' },
];

export const CredentialSharePanel = memo(function CredentialSharePanel() {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
    }}>
      <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Share Credential</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))', gap: 8 }}>
        {SHARE_OPTIONS.map((opt) => (
          <div
            key={opt.label}
            title={opt.placeholder}
            style={{
              padding: '12px 8px', borderRadius: 'var(--radius-sm)',
              border: '1px dashed var(--color-border-default)',
              display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4,
              cursor: 'not-allowed', opacity: 0.6,
            }}
          >
            <span style={{ fontSize: 20 }}>{opt.icon}</span>
            <span style={{ fontSize: 'var(--text-caption)', textAlign: 'center' }}>{opt.label}</span>
          </div>
        ))}
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 8, textAlign: 'center' }}>
        Sharing features are placeholders — will be implemented in future sprints
      </div>
    </div>
  );
});
