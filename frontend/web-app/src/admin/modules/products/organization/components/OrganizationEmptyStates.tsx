import React from 'react';
import Button from '../../../../../design-system/components/core/Button';
import Icon from '../../../../../design-system/icons/Icon';

interface EmptyStateProps {
  onClear?: () => void;
  onAction?: () => void;
  actionLabel?: string;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
  gap: 'var(--space-stack-md)', padding: 'var(--space-12) var(--space-4)', textAlign: 'center',
};

function NoSearchResults({ onClear }: EmptyStateProps) {
  return (
    <div style={wrapperStyle}>
      <Icon name="search" size={48} style={{ color: 'var(--color-text-tertiary)' }} />
      <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>No search results</h3>
      <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Try different keywords or adjust your search.</p>
      {onClear && <Button variant="outline" size="sm" onClick={onClear}>Clear search</Button>}
    </div>
  );
}

function emptyState(icon: string, title: string, description: string, action?: EmptyStateProps) {
  return () => (
    <div style={wrapperStyle}>
      <Icon name={icon} size={48} style={{ color: 'var(--color-text-tertiary)' }} />
      <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{title}</h3>
      <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{description}</p>
      {action?.actionLabel && <Button variant="outline" size="sm" onClick={action.onAction}>{action.actionLabel}</Button>}
    </div>
  );
}

function PermissionDenied() {
  return (
    <div style={wrapperStyle}>
      <Icon name="shield-off" size={48} style={{ color: 'var(--color-text-danger)' }} />
      <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Permission Denied</h3>
      <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>You do not have permission to access this section.</p>
    </div>
  );
}

export const OrganizationEmptyStates = {
  NoSearchResults,
  NoCategories: emptyState('tag', 'No categories', 'Create your first category to organize products.'),
  NoCollections: emptyState('bookmark', 'No collections', 'Create a collection to group products.'),
  NoBrands: emptyState('shield', 'No brands', 'Add a brand to start building your catalog.'),
  NoTags: emptyState('pricetag', 'No tags', 'Tags help customers find products faster.'),
  PermissionDenied,
};

export default OrganizationEmptyStates;
