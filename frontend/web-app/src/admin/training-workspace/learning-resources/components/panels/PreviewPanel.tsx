import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { useResourceContext } from '../../state/ResourceContext';
import { RESOURCE_TYPE_ICONS, RESOURCE_TYPE_LABELS } from '../../data/resourceMockData';

function Field({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <div style={{ fontSize: 11, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>{label}</div>
      <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>{value}</div>
    </div>
  );
}

export function PreviewPanel() {
  const { state, setPreviewResource, toggleFavorite, archiveResource, duplicateResource } = useResourceContext();
  const resource = state.resources.find((r) => r.id === state.previewResourceId) || state.resources[0];

  if (!resource) {
    return <div style={{ color: 'var(--color-text-tertiary)' }}>Select a resource to preview.</div>;
  }

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Resource Preview</h2>
      <Card variant="elevated" padding="lg">
        <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 'var(--space-4)' }}>
          <span style={{ width: 64, height: 64, borderRadius: 12, background: resource.color, color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 32 }}>
            {RESOURCE_TYPE_ICONS[resource.type]}
          </span>
          <div style={{ flex: 1 }}>
            <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>{resource.name}</div>
            <div style={{ color: 'var(--color-text-tertiary)' }}>{resource.code} · {RESOURCE_TYPE_LABELS[resource.type]}</div>
          </div>
          <Badge variant="success" size="md">{resource.status}</Badge>
        </div>

        <div
          style={{
            height: 180,
            borderRadius: 8,
            border: '1px dashed var(--color-border-default)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'var(--color-text-tertiary)',
            marginBottom: 'var(--space-4)',
          }}
        >
          Mock preview — {RESOURCE_TYPE_LABELS[resource.type]} ({resource.fileSize})
        </div>

        <p style={{ marginTop: 0 }}>{resource.description}</p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-3)', marginBottom: 'var(--space-4)' }}>
          <Field label="Author" value={resource.author} />
          <Field label="Department" value={resource.department} />
          <Field label="Category" value={resource.category} />
          <Field label="Language" value={resource.language} />
          <Field label="Version" value={`v${resource.version}`} />
          <Field label="Visibility" value={resource.visibility} />
          <Field label="Created" value={resource.createdDate} />
          <Field label="Updated" value={resource.updatedDate} />
          <Field label="Usage" value={`${resource.usageCount} uses`} />
        </div>

        <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap', marginBottom: 'var(--space-4)' }}>
          {resource.tags.map((t) => <Badge key={t} variant="neutral" size="sm">{t}</Badge>)}
        </div>

        <div style={{ display: 'flex', gap: 8 }}>
          <Button size="sm" onClick={() => toggleFavorite(resource.id)}>{resource.favorite ? 'Unfavorite' : 'Favorite'}</Button>
          <Button size="sm" variant="outline" onClick={() => duplicateResource(resource.id)}>Duplicate</Button>
          <Button size="sm" variant="destructive" onClick={() => archiveResource(resource.id)}>Archive</Button>
          <Button size="sm" variant="ghost" onClick={() => setPreviewResource(null)}>Clear</Button>
        </div>
      </Card>
    </div>
  );
}
