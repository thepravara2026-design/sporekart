import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';

const PROVIDERS = [
  { id: 'local', label: 'Local Mock Store', status: 'active', icon: '\ud83d\udcbe', note: 'In-memory mock data (current)' },
  { id: 's3', label: 'AWS S3', status: 'planned', icon: '\u2601\ufe0f', note: 'Object storage adapter (readiness only)' },
  { id: 'azure', label: 'Azure Blob', status: 'planned', icon: '\u2601\ufe0f', note: 'Blob container adapter (readiness only)' },
  { id: 'gcs', label: 'Google Cloud Storage', status: 'planned', icon: '\u2601\ufe0f', note: 'GCS bucket adapter (readiness only)' },
  { id: 'cdn', label: 'CDN Delivery', status: 'planned', icon: '\ud83d\udef0\ufe0f', note: 'Edge caching for assets (readiness only)' },
];

export function StoragePanel() {
  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Cloud Storage Readiness</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        Storage abstraction layer designed for future cloud integration. All operations run in Mock Mode — no real uploads or cloud connectivity.
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-3)', marginBottom: 'var(--space-5)' }}>
        {PROVIDERS.map((p) => (
          <Card key={p.id} variant="outlined" padding="md">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <span style={{ fontSize: 26 }}>{p.icon}</span>
              <Badge variant={p.status === 'active' ? 'success' : 'neutral'} size="sm">{p.status}</Badge>
            </div>
            <div style={{ fontWeight: 700, marginTop: 8 }}>{p.label}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{p.note}</div>
          </Card>
        ))}
      </div>

      <Card variant="ghost" padding="md">
        <h3 style={{ marginTop: 0 }}>Storage Abstraction Interface (design)</h3>
        <ul style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--font-size-sm)', lineHeight: 1.8 }}>
          <li><code>upload(file, path)</code> — persist an asset (mock: no-op)</li>
          <li><code>download(id)</code> — retrieve an asset URL (mock: placeholder)</li>
          <li><code>delete(id)</code> — remove an asset (mock: state only)</li>
          <li><code>getSignedUrl(id)</code> — time-limited access (mock: static)</li>
          <li><code>listVersions(id)</code> — version history (mock: single version)</li>
        </ul>
      </Card>
    </div>
  );
}
