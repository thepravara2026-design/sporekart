import { memo } from 'react';
import type { CommunicationPreference } from '../types';

interface PreferenceCardProps {
  preference: CommunicationPreference;
  onToggle: (field: keyof CommunicationPreference) => void;
}

export const PreferenceCard = memo(function PreferenceCard({ preference, onToggle }: PreferenceCardProps) {
  const channels: { key: keyof CommunicationPreference; label: string }[] = [
    { key: 'email', label: 'Email' }, { key: 'sms', label: 'SMS' },
    { key: 'whatsapp', label: 'WhatsApp' }, { key: 'push', label: 'Push' },
    { key: 'inApp', label: 'In-App' },
  ];
  const types: { key: keyof CommunicationPreference; label: string }[] = [
    { key: 'announcements', label: 'Announcements' }, { key: 'reminders', label: 'Reminders' },
    { key: 'marketing', label: 'Marketing' }, { key: 'academic', label: 'Academic' },
  ];

  const Toggle = ({ value, onChange }: { value: boolean; onChange: () => void }) => (
    <button onClick={onChange} aria-pressed={value} style={{
      width: 36, height: 20, borderRadius: 10, border: 'none', cursor: 'pointer', position: 'relative',
      background: value ? '#2563eb' : '#d1d5db', transition: 'background 0.2s', padding: 0,
    }}>
      <span style={{
        position: 'absolute', top: 2, width: 16, height: 16, borderRadius: '50%', background: '#fff',
        left: value ? 18 : 2, transition: 'left 0.2s',
      }} />
    </button>
  );

  return (
    <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 }}>
      <h4 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{preference.studentName}</h4>
      <div>
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Channels</div>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          {channels.map((ch) => (
            <label key={ch.key} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-sm)', cursor: 'pointer' }}>
              <Toggle value={Boolean(preference[ch.key])} onChange={() => onToggle(ch.key)} />
              {ch.label}
            </label>
          ))}
        </div>
      </div>
      <div>
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Notification Types</div>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          {types.map((t) => (
            <label key={t.key} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-sm)', cursor: 'pointer' }}>
              <Toggle value={Boolean(preference[t.key])} onChange={() => onToggle(t.key)} />
              {t.label}
            </label>
          ))}
        </div>
      </div>
      {preference.quietHoursStart && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Quiet Hours: {preference.quietHoursStart} - {preference.quietHoursEnd}
        </div>
      )}
    </div>
  );
});
