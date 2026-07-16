import { useState } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Select } from '../../../../../design-system/components/composite/Select';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { useBuilderContext } from '../../state/BuilderContext';
import {
  VISIBILITY_OPTIONS,
  ENROLLMENT_OPTIONS,
  DIFFICULTY_OPTIONS,
  LANGUAGE_OPTIONS,
} from '../../data/builderMockData';

export function SettingsPanel() {
  const { state, setSettings, markSaved } = useBuilderContext();
  const { settings } = state;
  const [showConfirmPublish, setShowConfirmPublish] = useState(false);

  const handlePublish = () => {
    setSettings({ status: 'published' });
    markSaved();
    setShowConfirmPublish(false);
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Visibility & Enrollment</div>

          <Grid columns={2} gap="md">
            <Select
              label="Visibility"
              options={VISIBILITY_OPTIONS}
              value={settings.visibility}
              onChange={(v) => setSettings({ visibility: v as 'public' | 'private' | 'internal' })}
            />
            <Select
              label="Enrollment Status"
              options={ENROLLMENT_OPTIONS}
              value={settings.enrollmentStatus}
              onChange={(v) => setSettings({ enrollmentStatus: v as 'open' | 'closed' | 'coming-soon' })}
            />
          </Grid>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Course Status</div>

          <Inline gap="md" align="center">
            <Badge
              variant={settings.status === 'published' ? 'success' : settings.status === 'archived' ? 'danger' : 'warning'}
              size="md"
            >
              {settings.status === 'draft' ? 'Draft' : settings.status === 'published' ? 'Published' : 'Archived'}
            </Badge>
            <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
              {settings.status === 'draft'
                ? 'This course is in draft mode and not visible to students.'
                : settings.status === 'published'
                  ? 'This course is published and visible to enrolled students.'
                  : 'This course has been archived.'}
            </span>
          </Inline>

          <Inline gap="sm">
            {settings.status === 'draft' && (
              <>
                <Button variant="success" size="sm" onClick={() => setShowConfirmPublish(true)}>
                  Publish Course
                </Button>
                <Button variant="ghost" size="sm" onClick={() => setSettings({ status: 'archived' })}>
                  Archive
                </Button>
              </>
            )}
            {settings.status === 'published' && (
              <Button variant="warning" size="sm" onClick={() => setSettings({ status: 'draft' })}>
                Unpublish
              </Button>
            )}
            {settings.status === 'archived' && (
              <Button variant="primary" size="sm" onClick={() => setSettings({ status: 'draft' })}>
                Restore to Draft
              </Button>
            )}
          </Inline>

          <Inline gap="sm" align="center">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setSettings({ featured: !settings.featured })}
            >
              {settings.featured ? 'Remove Featured' : 'Mark as Featured'}
            </Button>
            {settings.featured && <Badge variant="warning">Featured</Badge>}
          </Inline>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Course Configuration</div>

          <Grid columns={2} gap="md">
            <Select
              label="Language"
              options={LANGUAGE_OPTIONS}
              value={settings.language}
              onChange={(v) => setSettings({ language: v })}
            />
            <Select
              label="Difficulty"
              options={DIFFICULTY_OPTIONS}
              value={settings.difficulty}
              onChange={(v) => setSettings({ difficulty: v })}
            />
          </Grid>
        </Stack>
      </Card>

      {showConfirmPublish && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            background: 'rgba(0,0,0,0.5)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
          }}
          onClick={() => setShowConfirmPublish(false)}
        >
          <Card
            variant="elevated"
            padding="lg"
            style={{ maxWidth: 400, width: '90%' }}
            onClick={() => {}}
          >
            <Stack gap="md">
              <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Confirm Publish</div>
              <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
                Are you sure you want to publish this course? It will become visible to students based on your visibility settings.
              </div>
              <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                <Button variant="ghost" size="sm" onClick={() => setShowConfirmPublish(false)}>Cancel</Button>
                <Button variant="success" size="sm" onClick={handlePublish}>Confirm Publish</Button>
              </div>
            </Stack>
          </Card>
        </div>
      )}
    </Stack>
  );
}
