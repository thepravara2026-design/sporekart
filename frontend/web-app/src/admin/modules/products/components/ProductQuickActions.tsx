import React from 'react';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { PermissionGate } from '../../../permissions/PermissionGate';

const PRODUCT_RESOURCE = 'products';

export const ProductQuickActions = React.memo(function ProductQuickActions() {
  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-component-gap)' }} role="toolbar" aria-label="Product quick actions">
      <PermissionGate action="create" resource={PRODUCT_RESOURCE}>
        <Button variant="primary" leftIcon={<Icon name="plus" size={16} />}>
          Create Product
        </Button>
      </PermissionGate>
      <PermissionGate action="import" resource={PRODUCT_RESOURCE}>
        <Button variant="outline" leftIcon={<Icon name="download" size={16} />}>
          Import
        </Button>
      </PermissionGate>
      <PermissionGate action="export" resource={PRODUCT_RESOURCE}>
        <Button variant="outline" leftIcon={<Icon name="upload" size={16} />}>
          Export
        </Button>
      </PermissionGate>
      <PermissionGate action="bulk_actions" resource={PRODUCT_RESOURCE}>
        <Button variant="secondary" leftIcon={<Icon name="layers" size={16} />}>
          Bulk Actions
        </Button>
      </PermissionGate>
    </div>
  );
});

export default ProductQuickActions;
