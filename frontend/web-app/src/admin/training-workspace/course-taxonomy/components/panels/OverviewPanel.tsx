import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { TaxonomyDashboardWidgets } from '../widgets/TaxonomyDashboardWidgets';
import { HierarchyTree } from '../visualization/HierarchyTree';

export function OverviewPanel() {
  return (
    <Stack gap="lg">
      <TaxonomyDashboardWidgets />
      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Taxonomy Hierarchy Preview</span>
          <HierarchyTree />
        </Stack>
      </Card>
    </Stack>
  );
}
