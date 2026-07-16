import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Button } from '../../../../../design-system/components/core/Button';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { useBuilderContext } from '../../state/BuilderContext';

export function PublishingPanel() {
  const { state, setSettings, markSaved } = useBuilderContext();
  const { info, objectives, prerequisites } = state;

  const checks = [
    { label: 'Course Title', passed: !!info.title },
    { label: 'Course Code', passed: !!info.code },
    { label: 'Short Description', passed: !!info.shortDescription },
    { label: 'Learning Objectives', passed: objectives.length > 0 },
    { label: 'Prerequisites', passed: prerequisites.length > 0 },
    { label: 'SEO Title', passed: !!state.seo.seoTitle },
    { label: 'SEO Description', passed: !!state.seo.seoDescription },
    { label: 'Language Set', passed: true },
    { label: 'Difficulty Set', passed: true },
    { label: 'Visibility Configured', passed: true },
  ];

  const passed = checks.filter((c) => c.passed).length;

  const handlePublish = () => {
    setSettings({ status: 'published' });
    markSaved();
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Publishing Checklist</div>
          <div
            role="progressbar"
            aria-valuenow={(passed / checks.length) * 100}
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
                width: `${(passed / checks.length) * 100}%`,
                background: passed === checks.length ? 'var(--color-success-500)' : 'var(--color-warning-500)',
                borderRadius: 3,
                transition: 'width 0.3s',
              }}
            />
          </div>
          <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            {passed}/{checks.length} checks passed
          </div>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <Grid columns={2} gap="sm">
            {checks.map((check) => (
              <Inline key={check.label} gap="sm" align="center">
                <span style={{ color: check.passed ? 'var(--color-success-500)' : 'var(--color-danger-500)' }}>
                  {check.passed ? '\u2705' : '\u274C'}
                </span>
                <span style={{ fontSize: 'var(--font-size-sm)' }}>{check.label}</span>
              </Inline>
            ))}
          </Grid>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Actions</div>
          <Inline gap="sm">
            <Button
              variant="success"
              onClick={handlePublish}
              disabled={passed < checks.length}
            >
              {passed === checks.length ? 'Publish Course' : 'Complete all checks to publish'}
            </Button>
            <Button
              variant="outline"
              onClick={() => setSettings({ status: 'draft' })}
            >
              Save as Draft
            </Button>
          </Inline>
          <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
            Future: Schedule publish date, notify enrolled students, batch publish to multiple programs.
          </div>
        </Stack>
      </Card>
    </Stack>
  );
}
