import { Button } from '../../../../../design-system/components/core/Button';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useResourceContext } from '../../state/ResourceContext';
import { RESOURCE_TYPE_ICONS } from '../../data/resourceMockData';

export function ArchivedPanel() {
  const { state, restoreResource, removeResource } = useResourceContext();
  const archived = state.resources.filter((r) => r.status === 'archived');

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Archived</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>Retired resources retained for reference ({archived.length}).</p>
      {archived.length === 0 ? (
        <div style={{ padding: 'var(--space-6)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No archived resources.</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
          {archived.map((r) => (
            <Card key={r.id} variant="outlined" padding="sm">
              <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                <span style={{ fontSize: 22 }}>{RESOURCE_TYPE_ICONS[r.type]}</span>
                <div style={{ flex: 1 }}>
                  <div style={{ fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{r.name}</div>
                  <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)' }}>{r.code} · archived</div>
                </div>
                <Badge variant="neutral" size="sm">{r.category}</Badge>
                <Button size="sm" variant="outline" onClick={() => restoreResource(r.id)}>Restore</Button>
                <Button size="sm" variant="destructive" onClick={() => removeResource(r.id)}>Delete</Button>
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
