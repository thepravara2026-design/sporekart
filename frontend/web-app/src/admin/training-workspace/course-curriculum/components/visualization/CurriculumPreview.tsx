import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_ICONS, type CurriculumNode } from '../../data/curriculumMockData';

const DEVICE_WIDTHS: Record<string, number> = { desktop: 1100, tablet: 768, mobile: 375 };

function flattenLessons(nodes: CurriculumNode[]): CurriculumNode[] {
  return nodes.flatMap((n) => {
    if (n.type === 'lesson') return [n, ...flattenLessons(n.children)];
    return flattenLessons(n.children);
  });
}

export function CurriculumPreview() {
  const { state, setPreviewDevice, setPreviewRole } = useCurriculumContext();
  const { tree, previewDevice, previewRole } = state;

  const lessons = flattenLessons(tree);
  const totalHours = (() => {
    const walk = (nodes: CurriculumNode[]): number => nodes.reduce((a, n) => a + n.durationHours + walk(n.children), 0);
    return walk(tree);
  })();

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="md">
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8, flexWrap: 'wrap' }}>
          <Inline gap="sm">
            <Button variant={previewDevice === 'desktop' ? 'primary' : 'outline'} size="sm" onClick={() => setPreviewDevice('desktop')}>Desktop</Button>
            <Button variant={previewDevice === 'tablet' ? 'primary' : 'outline'} size="sm" onClick={() => setPreviewDevice('tablet')}>Tablet</Button>
            <Button variant={previewDevice === 'mobile' ? 'primary' : 'outline'} size="sm" onClick={() => setPreviewDevice('mobile')}>Mobile</Button>
          </Inline>
          <Inline gap="sm">
            <Button variant={previewRole === 'student' ? 'primary' : 'outline'} size="sm" onClick={() => setPreviewRole('student')}>Student</Button>
            <Button variant={previewRole === 'trainer' ? 'primary' : 'outline'} size="sm" onClick={() => setPreviewRole('trainer')}>Trainer</Button>
          </Inline>
        </div>
      </Card>

      <Card variant="elevated" padding="none">
        <div style={{ display: 'flex', justifyContent: 'center', padding: 'var(--space-4)', background: 'var(--color-bg-secondary)', minHeight: 400 }}>
          <div
            style={{
              width: DEVICE_WIDTHS[previewDevice],
              maxWidth: '100%',
              background: 'var(--color-bg-primary)',
              borderRadius: previewDevice === 'mobile' ? 16 : 8,
              boxShadow: previewDevice !== 'desktop' ? '0 4px 16px rgba(0,0,0,0.12)' : 'none',
              overflow: 'hidden',
              transition: 'width 0.3s ease',
              fontSize: previewDevice === 'mobile' ? 'var(--font-size-xs)' : 'var(--font-size-sm)',
            }}
          >
            <div style={{ padding: previewDevice === 'mobile' ? 12 : 24 }}>
              <Stack gap="md">
                <div>
                  <div style={{ fontSize: previewDevice === 'mobile' ? 18 : 26, fontWeight: 700 }}>Curriculum Preview</div>
                  <Badge variant={previewRole === 'student' ? 'info' : 'warning'} size="sm">{previewRole} view</Badge>
                  <span style={{ marginLeft: 8, fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>{totalHours}h total</span>
                </div>
                <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: 12 }}>
                  <div style={{ fontWeight: 600, marginBottom: 8 }}>Lessons ({lessons.length})</div>
                  {lessons.map((lesson) => (
                    <div key={lesson.id} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '6px 0', borderBottom: '1px solid var(--color-border-subtle)' }}>
                      <span style={{ fontSize: 16 }}>{NODE_TYPE_ICONS[lesson.type]}</span>
                      <span style={{ flex: 1 }}>{lesson.name}</span>
                      {previewRole === 'trainer' && lesson.lessonType && (
                        <Badge variant="default" size="sm">{lesson.lessonType}</Badge>
                      )}
                      <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>{lesson.durationHours}h</span>
                    </div>
                  ))}
                </div>
              </Stack>
            </div>
          </div>
        </div>
      </Card>
    </Stack>
  );
}
