import { Stack } from '../../../../../design-system/components/layout/Stack';
import { RelationshipViewer } from '../visualization/RelationshipViewer';
import { TopicGraphPlaceholder } from '../visualization/TopicGraphPlaceholder';

export function RelationshipsPanel() {
  return (
    <Stack gap="lg">
      <RelationshipViewer />
      <TopicGraphPlaceholder />
    </Stack>
  );
}
