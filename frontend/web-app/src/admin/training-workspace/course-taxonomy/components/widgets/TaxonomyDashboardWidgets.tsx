import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useTaxonomyContext } from '../../state/TaxonomyContext';
import type { TaxonomyNode } from '../../data/taxonomyMockData';

function countAll(nodes: TaxonomyNode[]): number {
  return nodes.reduce((acc, n) => acc + 1 + countAll(n.children), 0);
}

function countSubcategories(nodes: TaxonomyNode[]): number {
  return nodes.reduce((acc, n) => acc + n.children.length + countSubcategories(n.children), 0);
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

export function TaxonomyDashboardWidgets() {
  const { state } = useTaxonomyContext();

  const totalCategories = countAll(state.tree);
  const totalSubcategories = countSubcategories(state.tree);
  const totalTopics = state.topics.length;
  const totalTags = state.tags.length;
  const activeCategories = (() => {
    const walk = (nodes: TaxonomyNode[]): number =>
      nodes.reduce((acc, n) => acc + (n.status === 'active' ? 1 : 0) + walk(n.children), 0);
    return walk(state.tree);
  })();
  const unusedCategories = (() => {
    const walk = (nodes: TaxonomyNode[]): number =>
      nodes.reduce((acc, n) => acc + (n.courseCount === 0 ? 1 : 0) + walk(n.children), 0);
    return walk(state.tree);
  })();

  const popularSkills = state.tags.filter((t) => t.popular).slice(0, 6);
  const mostUsedTags = [...state.tags].sort((a, b) => b.usageCount - a.usageCount).slice(0, 8);
  const trendingTags = state.tags.filter((t) => t.trending);

  return (
    <Stack gap="lg">
      <Grid columns={4} gap="md">
        <StatCard label="Categories" value={totalCategories} />
        <StatCard label="Subcategories" value={totalSubcategories} />
        <StatCard label="Topics" value={totalTopics} />
        <StatCard label="Tags" value={totalTags} />
        <StatCard label="Active" value={activeCategories} />
        <StatCard label="Languages" value={7} />
        <StatCard label="Competencies" value={9} />
        <StatCard label="Unused" value={unusedCategories} />
      </Grid>

      <Grid columns={2} gap="md">
        <Card variant="outlined" padding="md">
          <Stack gap="sm">
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Popular Skills / Tags</span>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
              {popularSkills.map((t) => <Badge key={t.id} variant="primary" size="sm">{t.label}</Badge>)}
            </div>
          </Stack>
        </Card>
        <Card variant="outlined" padding="md">
          <Stack gap="sm">
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Trending Tags</span>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
              {trendingTags.map((t) => <Badge key={t.id} variant="success" size="sm">{t.label}</Badge>)}
            </div>
          </Stack>
        </Card>
        <Card variant="outlined" padding="md">
          <Stack gap="sm">
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Most Used Tags</span>
            {mostUsedTags.map((t) => (
              <div key={t.id} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ flex: 1, fontSize: 'var(--font-size-sm)' }}>{t.label}</span>
                <div style={{ width: 80, height: 6, background: 'var(--color-bg-tertiary)', borderRadius: 3, overflow: 'hidden' }}>
                  <div style={{ width: `${(t.usageCount / mostUsedTags[0].usageCount) * 100}%`, height: '100%', background: 'var(--color-primary-500)' }} />
                </div>
                <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)', width: 24, textAlign: 'right' }}>{t.usageCount}</span>
              </div>
            ))}
          </Stack>
        </Card>
        <Card variant="outlined" padding="md">
          <Stack gap="sm">
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Unused Categories</span>
            {unusedCategories === 0 ? (
              <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>All categories have courses.</span>
            ) : (
              <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>{unusedCategories} categories with 0 courses.</span>
            )}
            <Badge variant="info" size="sm">{activeCategories} active</Badge>
          </Stack>
        </Card>
      </Grid>
    </Stack>
  );
}
