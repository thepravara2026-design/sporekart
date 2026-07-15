import React from 'react';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';

interface EmptyStateProps {
  onClear?: () => void;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
  gap: 'var(--space-stack-md)', padding: 'var(--space-16) var(--space-4)',
  textAlign: 'center',
};

function NoSearchResults({ onClear }: EmptyStateProps) {
  return (
    <div style={wrapperStyle}>
      <Icon name="search" size={48} style={{ color: 'var(--color-text-tertiary)' }} />
      <div>
        <h3 style={{ margin: '0 0 4px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>No search results</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
          Try adjusting your search terms or filters.
        </p>
      </div>
      {onClear && (
        <Button variant="outline" size="sm" onClick={onClear}>
          Clear search
        </Button>
      )}
    </div>
  );
}

function NoFilterResults({ onClear }: EmptyStateProps) {
  return (
    <div style={wrapperStyle}>
      <Icon name="filter" size={48} style={{ color: 'var(--color-text-tertiary)' }} />
      <div>
        <h3 style={{ margin: '0 0 4px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>No matching assets</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
          No assets match the current filter criteria.
        </p>
      </div>
      {onClear && (
        <Button variant="outline" size="sm" onClick={onClear}>
          Clear all filters
        </Button>
      )}
    </div>
  );
}

function EmptyLibrary() {
  return (
    <div style={wrapperStyle}>
      <Icon name="image" size={48} style={{ color: 'var(--color-text-tertiary)' }} />
      <div>
        <h3 style={{ margin: '0 0 4px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Your library is empty</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
          Upload your first asset to start building your digital library.
        </p>
      </div>
    </div>
  );
}

export const MediaEmptyState = {
  NoSearchResults,
  NoFilterResults,
  EmptyLibrary,
};

export default MediaEmptyState;
