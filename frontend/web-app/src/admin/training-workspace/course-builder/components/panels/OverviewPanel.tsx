import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { useBuilderContext } from '../../state/BuilderContext';

export function OverviewPanel() {
  const { state, setPanel } = useBuilderContext();

  const sections = [
    { panel: 'information', label: 'Basic Information', count: state.info.title ? 1 : 0, total: 1, icon: '\u270F\uFE0F' },
    { panel: 'objectives', label: 'Learning Objectives', count: state.objectives.length, total: 1, icon: '\uD83C\uDFAF' },
    { panel: 'prerequisites', label: 'Prerequisites', count: state.prerequisites.length, total: 1, icon: '\u2705' },
    { panel: 'media', label: 'Media', count: state.mediaPlaceholders.length, total: 1, icon: '\uD83D\uDDBC\uFE0F' },
    { panel: 'resources', label: 'Resources', count: state.resources.length, total: 1, icon: '\uD83D\uDCC1' },
    { panel: 'seo', label: 'SEO', count: state.seo.seoTitle ? 1 : 0, total: 1, icon: '\uD83D\uDD0D' },
    { panel: 'settings', label: 'Settings', count: 1, total: 1, icon: '\u2699\uFE0F' },
  ];

  const completed = sections.filter((s) => s.count >= s.total).length;

  return (
    <Stack gap="lg">
      <Card variant="elevated" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-xl)', fontWeight: 700 }}>
            Course Builder Overview
          </div>
          <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            {state.info.title || 'Untitled Course'} &middot; {completed}/{sections.length} sections complete
          </div>
          <div
            role="progressbar"
            aria-valuenow={(completed / sections.length) * 100}
            aria-valuemin={0}
            aria-valuemax={100}
            style={{
              height: 6,
              background: 'var(--color-bg-tertiary)',
              borderRadius: 3,
              overflow: 'hidden',
            }}
          >
            <div
              style={{
                height: '100%',
                width: `${(completed / sections.length) * 100}%`,
                background: 'var(--color-primary-500)',
                borderRadius: 3,
                transition: 'width 0.3s ease',
              }}
            />
          </div>
        </Stack>
      </Card>

      <Grid columns={2} gap="md">
        {sections.map((section) => {
          const isComplete = section.count >= section.total;
          return (
            <Card
              key={section.panel}
              variant="outlined"
              padding="md"
              hoverable
              onClick={() => setPanel(section.panel as any)}
              aria-label={`Go to ${section.label}`}
              style={{ cursor: 'pointer' }}
            >
              <Inline gap="sm" align="center">
                <span style={{ fontSize: 20 }}>{section.icon}</span>
                <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
                  <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>{section.label}</div>
                  <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-secondary)' }}>
                    {isComplete ? 'Complete' : 'Not started'}
                  </div>
                </div>
                {isComplete ? (
                  <Badge variant="success" size="sm">
                    Done
                  </Badge>
                ) : (
                  <Badge variant="warning" size="sm">
                    Pending
                  </Badge>
                )}
              </Inline>
            </Card>
          );
        })}
      </Grid>
    </Stack>
  );
}
