import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { PreferenceCard } from '../components/PreferenceCard';
import { MetricCard } from '../components/MetricCard';

export default function CommunicationPreferences() {
  const { preferences, updatePreference } = useCommunication();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Communication Preferences</h1>
        <SharedFilters currentPage="communication/preferences" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Students" value={preferences.length} icon="👥" />
        <MetricCard label="SMS Enabled" value={preferences.filter((p) => p.sms).length} icon="📱" color="#2563eb" />
        <MetricCard label="WhatsApp Enabled" value={preferences.filter((p) => p.whatsapp).length} icon="💬" color="#16a34a" />
        <MetricCard label="In-App Enabled" value={preferences.filter((p) => p.inApp).length} icon="🔔" />
      </div>

      <DashboardWidget title="Student Preferences" subtitle={`${preferences.length} students`}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {preferences.map((pref) => (
            <PreferenceCard key={pref.id} preference={pref} onToggle={(field) => updatePreference(pref.id, { [field]: !pref[field] })} />
          ))}
        </div>
      </DashboardWidget>
    </main>
  );
}
