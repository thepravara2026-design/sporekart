import React, { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { Button } from '../../../../design-system/components/core/Button';

interface EmptyStateShellProps {
  icon: string;
  title: string;
  message: string;
  tone?: 'default' | 'danger' | 'warning' | 'info';
  action?: React.ReactNode;
}

const toneColor: Record<NonNullable<EmptyStateShellProps['tone']>, string> = {
  default: 'var(--color-text-tertiary)',
  danger: 'var(--color-danger)',
  warning: 'var(--color-warning)',
  info: 'var(--color-info)',
};

const EmptyStateShell = memo(function EmptyStateShell({ icon, title, message, tone = 'default', action }: EmptyStateShellProps) {
  return (
    <div
      role="status"
      aria-label={title}
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        gap: 'var(--space-stack-xs)',
        padding: '64px 24px',
        minHeight: 280,
      }}
    >
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 64,
          height: 64,
          borderRadius: 'var(--radius-lg)',
          background: 'var(--color-bg-surface-raised)',
          color: toneColor[tone],
          marginBottom: 'var(--space-stack-xs)',
        }}
      >
        <Icon name={icon} size={30} />
      </div>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-semibold)' }}>
        {title}
      </h3>
      <p style={{ margin: 0, maxWidth: 420, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{message}</p>
      {action && <div style={{ marginTop: 'var(--space-component-gap)' }}>{action}</div>}
    </div>
  );
});

const NoProducts = memo(function NoProducts() {
  return (
    <EmptyStateShell
      icon="package"
      title="No products yet"
      message="Your catalog is empty. New products will appear here once they are created."
    />
  );
});

interface WithClearProps {
  onClear?: () => void;
}

const NoSearchResults = memo(function NoSearchResults({ onClear }: WithClearProps) {
  return (
    <EmptyStateShell
      icon="search"
      title="No matching products"
      message="We couldn't find any products matching your search. Try different keywords."
      action={onClear ? (
        <Button variant="outline" size="sm" onClick={onClear} leftIcon={<Icon name="x" size={14} />}>
          Clear search
        </Button>
      ) : undefined}
    />
  );
});

const NoFilterResults = memo(function NoFilterResults({ onClear }: WithClearProps) {
  return (
    <EmptyStateShell
      icon="filter"
      title="No products match your filters"
      message="Try adjusting or clearing your filters to see more products."
      action={onClear ? (
        <Button variant="outline" size="sm" onClick={onClear} leftIcon={<Icon name="refresh-cw" size={14} />}>
          Clear all filters
        </Button>
      ) : undefined}
    />
  );
});

const PermissionDenied = memo(function PermissionDenied() {
  return (
    <EmptyStateShell
      icon="shield"
      title="Access restricted"
      message="You don't have permission to view the product catalog. Contact an administrator for access."
      tone="danger"
    />
  );
});

const Offline = memo(function Offline() {
  return (
    <EmptyStateShell
      icon="globe"
      title="You're offline"
      message="The product catalog can't load right now. Check your connection and try again."
      tone="warning"
    />
  );
});

const Maintenance = memo(function Maintenance() {
  return (
    <EmptyStateShell
      icon="settings"
      title="Under maintenance"
      message="The product catalog is temporarily unavailable while we perform maintenance."
      tone="info"
    />
  );
});

const FeatureDisabled = memo(function FeatureDisabled() {
  return (
    <EmptyStateShell
      icon="sliders"
      title="Feature not available"
      message="This part of the catalog experience is currently disabled by a feature flag."
      tone="info"
    />
  );
});

export const CatalogEmptyStates = {
  NoProducts,
  NoSearchResults,
  NoFilterResults,
  PermissionDenied,
  Offline,
  Maintenance,
  FeatureDisabled,
};

export default CatalogEmptyStates;
