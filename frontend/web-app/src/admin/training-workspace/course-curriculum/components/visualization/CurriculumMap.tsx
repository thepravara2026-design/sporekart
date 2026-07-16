import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_ICONS, type CurriculumNode } from '../../data/curriculumMockData';

function collect(nodes: CurriculumNode[], type: CurriculumNode['type']): CurriculumNode[] {
  return nodes.flatMap((n) => (n.type === type ? [n, ...collect(n.children, type)] : collect(n.children, type)));
}

export function CurriculumMap() {
  const { state } = useCurriculumContext();
  const modules = collect(state.tree, 'module');
  const lessons = collect(state.tree, 'lesson');
  const assessments = collect(state.tree, 'assessment');

  return (
    <Card variant="outlined" padding="lg">
      <Stack gap="md">
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Curriculum Map</span>
          <Badge variant="warning" size="sm">Planned</Badge>
        </div>
        <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
          Future visual learning-flow map of modules, lessons and assessments.
        </div>
        <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
          <div><strong>{modules.length}</strong> modules</div>
          <div><strong>{lessons.length}</strong> lessons</div>
          <div><strong>{assessments.length}</strong> assessments</div>
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {modules.map((m) => (
            <div key={m.id} style={{ padding: '8px 12px', borderLeft: '3px solid var(--color-primary-500)', background: 'var(--color-bg-secondary)', borderRadius: 'var(--radius-sm)' }}>
              {NODE_TYPE_ICONS[m.type]} {m.name} <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>({m.durationHours}h)</span>
            </div>
          ))}
        </div>
      </Stack>
    </Card>
  );
}
