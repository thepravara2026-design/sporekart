import { Stack } from '../../../../../design-system/components/layout/Stack';
import { CategoryExplorer } from '../visualization/CategoryExplorer';
import { KnowledgeMapPlaceholder } from '../visualization/KnowledgeMapPlaceholder';

export function HierarchyExplorerPanel() {
  return (
    <Stack gap="lg">
      <CategoryExplorer />
      <KnowledgeMapPlaceholder />
    </Stack>
  );
}
