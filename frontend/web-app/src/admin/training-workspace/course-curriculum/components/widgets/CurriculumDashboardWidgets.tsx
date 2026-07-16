import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';
import type { CurriculumNode } from '../../data/curriculumMockData';

function collect(nodes: CurriculumNode[], type: CurriculumNode['type']): CurriculumNode[] {
  return nodes.flatMap((n) => (n.type === type ? [n, ...collect(n.children, type)] : collect(n.children, type)));
}

function StatCard({ label, value, suffix }: { label: string; value: number; suffix?: string }) {
  return (
    <Card variant="outlined" padding="md">
      <Stack gap="xs">
        <div style={{ fontSize: 'var(--font-size-2xl)', fontWeight: 700 }}>{value}{suffix}</div>
        <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>{label}</div>
      </Stack>
    </Card>
  );
}

export function CurriculumDashboardWidgets() {
  const { state } = useCurriculumContext();
  const tree = state.tree;

  const curriculums = collect(tree, 'curriculum');
  const modules = collect(tree, 'module');
  const lessons = collect(tree, 'lesson');
  const topics = collect(tree, 'topic');
  const activities = collect(tree, 'activity');
  const assignments = collect(tree, 'assignment');
  const assessments = collect(tree, 'assessment');
  const resources = collect(tree, 'topic').length + modules.length;

  const published = curriculums.filter((c) => c.status === 'published').length;
  const draft = curriculums.filter((c) => c.status === 'draft').length;

  const totalHours = (() => {
    const walk = (nodes: CurriculumNode[]): number => nodes.reduce((a, n) => a + n.durationHours + walk(n.children), 0);
    return Math.round(walk(tree));
  })();
  const publishedHours = totalHours; // simplified
  const completion = publishedHours > 0 ? Math.round((published / (published + draft || 1)) * 100) : 0;

  return (
    <Stack gap="lg">
      <Grid columns={4} gap="md">
        <StatCard label="Curriculums" value={curriculums.length} />
        <StatCard label="Modules" value={modules.length} />
        <StatCard label="Lessons" value={lessons.length} />
        <StatCard label="Topics" value={topics.length} />
        <StatCard label="Activities" value={activities.length} />
        <StatCard label="Assignments" value={assignments.length} />
        <StatCard label="Assessments" value={assessments.length} />
        <StatCard label="Resources" value={resources} />
        <StatCard label="Completion" value={completion} suffix="%" />
        <StatCard label="Total Hours" value={totalHours} suffix="h" />
        <StatCard label="Published" value={published} />
        <StatCard label="Draft" value={draft} />
      </Grid>
      <Grid columns={2} gap="md">
        <Card variant="outlined" padding="md">
          <Stack gap="sm">
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Status</span>
            <Inline gap="sm" align="center">
              <Badge variant="success" size="sm">{published} published</Badge>
              <Badge variant="warning" size="sm">{draft} draft</Badge>
            </Inline>
          </Stack>
        </Card>
        <Card variant="outlined" padding="md">
          <Stack gap="sm">
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Module Breakdown</span>
            {modules.map((m) => (
              <div key={m.id} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ flex: 1, fontSize: 'var(--font-size-sm)' }}>{m.name}</span>
                <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>{m.durationHours}h</span>
              </div>
            ))}
          </Stack>
        </Card>
      </Grid>
    </Stack>
  );
}
