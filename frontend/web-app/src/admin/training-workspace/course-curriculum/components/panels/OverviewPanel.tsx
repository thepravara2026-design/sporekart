import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { CurriculumDashboardWidgets } from '../widgets/CurriculumDashboardWidgets';
import { CurriculumTree } from '../visualization/CurriculumTree';

export function OverviewPanel() {
  return (
    <Stack gap="lg">
      <CurriculumDashboardWidgets />
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Curriculum Hierarchy</span>
          <CurriculumTree />
        </Stack>
      </Card>
    </Stack>
  );
}
