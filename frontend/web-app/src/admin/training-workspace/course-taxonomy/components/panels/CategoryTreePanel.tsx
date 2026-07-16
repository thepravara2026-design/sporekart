import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { CategoryExplorer } from '../visualization/CategoryExplorer';

export function CategoryTreePanel() {
  return (
    <Stack gap="lg">
      <Card variant="outlined" padding="lg">
        <Stack gap="sm">
          <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>Category Management</span>
          <span style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
            Manage categories with unlimited nesting depth. Select a node to view details or add subcategories.
          </span>
        </Stack>
      </Card>
      <CategoryExplorer />
    </Stack>
  );
}
