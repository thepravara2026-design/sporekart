import { Stack } from '../../../../../design-system/components/layout/Stack';
import { CurriculumPreview } from '../visualization/CurriculumPreview';
import { CurriculumMap } from '../visualization/CurriculumMap';

export function PreviewPanel() {
  return (
    <Stack gap="lg">
      <CurriculumPreview />
      <CurriculumMap />
    </Stack>
  );
}
