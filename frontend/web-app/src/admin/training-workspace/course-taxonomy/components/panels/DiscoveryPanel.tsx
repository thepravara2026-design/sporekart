import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useTaxonomyContext } from '../../state/TaxonomyContext';

const DISCOVERY_TYPES = [
  { key: 'recommendations', label: 'Course Recommendations', desc: 'Map courses to learner profiles via taxonomy' },
  { key: 'related', label: 'Related Courses', desc: 'Shared categories and tags drive relatedness' },
  { key: 'popular', label: 'Popular Categories', desc: 'Course-count ranking per category' },
  { key: 'suggested', label: 'Suggested Learning', desc: 'Skill progression path suggestions' },
  { key: 'trending', label: 'Trending Topics', desc: 'Usage-weighted topic surfacing' },
  { key: 'ai', label: 'AI Recommendations', desc: 'Future: ML-driven personalized discovery' },
];

export function DiscoveryPanel() {
  const { state } = useTaxonomyContext();
  const popularCategories = (() => {
    const walk: any = (nodes: any[]): any[] =>
      nodes.flatMap((n) => [{ name: n.name, count: n.courseCount }, ...walk(n.children)]);
    return walk(state.tree).sort((a: any, b: any) => b.count - a.count).slice(0, 5);
  })();

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Discovery Metadata</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Taxonomy powers course discovery. These metadata hooks are prepared for future recommendation and search-ranking engines.
          </span>
        </Stack>
      </Card>
      <Grid columns={3} gap="md">
        {DISCOVERY_TYPES.map((d) => (
          <Card key={d.key} variant="outlined" padding="md">
            <Stack gap="xs">
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, flex: 1 }}>{d.label}</span>
                {d.key === 'ai' ? <Badge variant="warning" size="sm">future</Badge> : <Badge variant="info" size="sm">ready</Badge>}
              </div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>{d.desc}</div>
            </Stack>
          </Card>
        ))}
      </Grid>
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Popular Categories (by course count)</span>
          {popularCategories.map((c: any) => (
            <div key={c.name} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span style={{ flex: 1, fontSize: 'var(--font-size-sm)' }}>{c.name}</span>
              <div style={{ width: 120, height: 6, background: 'var(--color-bg-tertiary)', borderRadius: 3, overflow: 'hidden' }}>
                <div style={{ width: `${(c.count / popularCategories[0].count) * 100}%`, height: '100%', background: 'var(--color-primary-500)' }} />
              </div>
              <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)', width: 28, textAlign: 'right' }}>{c.count}</span>
            </div>
          ))}
        </Stack>
      </Card>
    </Stack>
  );
}
