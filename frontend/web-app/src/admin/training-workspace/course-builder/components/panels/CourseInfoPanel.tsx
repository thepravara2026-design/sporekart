import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Card } from '../../../../../design-system/components/composite/Card';
import { Grid } from '../../../../../design-system/components/layout/Grid';
import { Input } from '../../../../../design-system/components/core/Input';
import { Select } from '../../../../../design-system/components/composite/Select';
import { useBuilderContext } from '../../state/BuilderContext';
import {
  DIFFICULTY_OPTIONS,
  LANGUAGE_OPTIONS,
  DELIVERY_OPTIONS,
  CATEGORY_OPTIONS,
} from '../../data/builderMockData';

export function CourseInfoPanel() {
  const { state, setInfo } = useBuilderContext();
  const { info } = state;

  const handleArrayChange = (field: 'targetAudience' | 'tags' | 'keywords', value: string) => {
    const items = value.split(',').map((s) => s.trim()).filter(Boolean);
    setInfo({ [field]: items });
  };

  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Basic Information</div>

          <Grid columns={2} gap="md">
            <Input
              label="Course Title"
              value={info.title}
              onChange={(e) => setInfo({ title: e.target.value })}
              placeholder="Enter course title"
              required
              fullWidth
            />
            <Input
              label="Course Code"
              value={info.code}
              onChange={(e) => setInfo({ code: e.target.value })}
              placeholder="e.g. MC-101"
              fullWidth
            />
          </Grid>

          <Input
            label="Short Description"
            value={info.shortDescription}
            onChange={(e) => setInfo({ shortDescription: e.target.value })}
            placeholder="Brief summary (max 200 characters)"
            characterLimit={200}
            fullWidth
          />

          <div style={{ position: 'relative' }}>
            <label style={{ display: 'block', fontSize: 'var(--font-size-sm)', fontWeight: 500, marginBottom: 4 }}>
              Detailed Description
            </label>
            <textarea
              value={info.detailedDescription}
              onChange={(e) => setInfo({ detailedDescription: e.target.value })}
              placeholder="Full course description..."
              rows={5}
              style={{
                width: '100%',
                padding: '8px 12px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-sm)',
                fontSize: 'var(--font-size-sm)',
                fontFamily: 'inherit',
                resize: 'vertical',
                background: 'var(--color-bg-primary)',
                color: 'var(--color-text-primary)',
              }}
              aria-label="Detailed Description"
            />
          </div>
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Classification</div>

          <Grid columns={2} gap="md">
            <Select
              label="Difficulty"
              options={DIFFICULTY_OPTIONS}
              value={info.difficulty}
              onChange={(v) => setInfo({ difficulty: v })}
            />
            <Select
              label="Delivery Mode"
              options={DELIVERY_OPTIONS}
              value={info.deliveryMode}
              onChange={(v) => setInfo({ deliveryMode: v })}
            />
          </Grid>

          <Grid columns={2} gap="md">
            <Select
              label="Language"
              options={LANGUAGE_OPTIONS}
              value={info.language}
              onChange={(v) => setInfo({ language: v })}
            />
            <Select
              label="Category"
              options={CATEGORY_OPTIONS}
              value={info.category}
              onChange={(v) => setInfo({ category: v })}
            />
          </Grid>

          <Grid columns={2} gap="md">
            <Input
              label="Duration"
              value={info.duration}
              onChange={(e) => setInfo({ duration: e.target.value })}
              placeholder="e.g. 6 weeks"
              fullWidth
            />
            <Input
              label="Duration (Hours)"
              type="number"
              value={info.durationHours.toString()}
              onChange={(e) => setInfo({ durationHours: parseInt(e.target.value) || 0 })}
              fullWidth
            />
          </Grid>

          <Input
            label="Target Audience"
            value={info.targetAudience.join(', ')}
            onChange={(e) => handleArrayChange('targetAudience', e.target.value)}
            placeholder="Comma-separated list (e.g. Farmers, Entrepreneurs)"
            helperText="Separate multiple entries with commas"
            fullWidth
          />

          <Input
            label="Tags"
            value={info.tags.join(', ')}
            onChange={(e) => handleArrayChange('tags', e.target.value)}
            placeholder="Comma-separated tags"
            helperText="Separate multiple entries with commas"
            fullWidth
          />

          <Input
            label="Keywords"
            value={info.keywords.join(', ')}
            onChange={(e) => handleArrayChange('keywords', e.target.value)}
            placeholder="Comma-separated keywords"
            helperText="Separate multiple entries with commas"
            fullWidth
          />

          <Input
            label="Version Number"
            value={info.versionNumber}
            onChange={(e) => setInfo({ versionNumber: e.target.value })}
            placeholder="1.0"
            helperText="Semantic version for this course iteration"
            fullWidth
          />
        </Stack>
      </Card>
    </Stack>
  );
}
