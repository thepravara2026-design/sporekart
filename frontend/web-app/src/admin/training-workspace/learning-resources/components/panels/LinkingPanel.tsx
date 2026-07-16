import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { RESOURCE_TYPE_ICONS } from '../../data/resourceMockData';
import { useResourceContext } from '../../state/ResourceContext';

const LINK_TARGETS = [
  { id: 'course', label: 'Courses', icon: '\ud83d\udcd8', count: 15, description: 'Attach resources to course registry entries.' },
  { id: 'curriculum', label: 'Curriculum Modules', icon: '\ud83d\uddd2\ufe0f', count: 24, description: 'Map resources to lessons and modules.' },
  { id: 'lesson', label: 'Lessons', icon: '\ud83c\udfaf', count: 62, description: 'Embed resources inside individual lessons.' },
  { id: 'assessment', label: 'Assessments', icon: '\ud83d\udcdd', count: 18, description: 'Reference material for assessments.' },
  { id: 'batch', label: 'Batches', icon: '\ud83d\udc65', count: 9, description: 'Share resources with a training batch.' },
];

export function LinkingPanel() {
  const { state } = useResourceContext();
  const linkable = state.resources.filter((r) => r.status === 'active').slice(0, 6);

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Resource Linking Architecture</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>
        Central resources link to any learning entity. Mock associations shown below (no persistence).
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-3)', marginBottom: 'var(--space-5)' }}>
        {LINK_TARGETS.map((t) => (
          <Card key={t.id} variant="outlined" padding="md">
            <div style={{ fontSize: 28 }}>{t.icon}</div>
            <div style={{ fontWeight: 700, marginTop: 6 }}>{t.label}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)', marginBottom: 8 }}>{t.description}</div>
            <Badge variant="info" size="sm">{t.count} linkable</Badge>
          </Card>
        ))}
      </div>

      <h3>Recently Linkable Resources</h3>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {linkable.map((r) => (
          <Card key={r.id} variant="outlined" padding="sm">
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <span style={{ fontSize: 20 }}>{RESOURCE_TYPE_ICONS[r.type]}</span>
              <span style={{ flex: 1, fontWeight: 600, fontSize: 'var(--font-size-sm)' }}>{r.name}</span>
              <Badge variant="neutral" size="sm">{r.category}</Badge>
              <Badge variant="success" size="sm">linkable</Badge>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
}
