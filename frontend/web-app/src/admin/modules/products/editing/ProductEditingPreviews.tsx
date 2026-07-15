import React from 'react';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import { type Crumb } from '../../../../design-system/components/navigation/Breadcrumb';
import { ProductLayout } from '../layout/ProductLayout';
import { PRODUCT_LIFECYCLE_STATES, lifecycleLabel } from '../lifecycle';
import { LifecycleBadge } from '../components/LifecycleBadge';
import { useProductEditState } from './useProductEditState';
import { VersionHistory } from './VersionHistory';
import ActivityTimeline from './ActivityTimeline';
import { CompareView } from './CompareView';
import { MOCK_PRODUCT_SKU } from './mockEditData';
import type { ProductVersion } from './types';

function PreviewNote({ title }: { title: string }) {
  return (
    <Card variant="outlined" padding="sm" as="section" aria-label={`${title} notes`} style={{ marginTop: 'var(--space-component-gap)' }}>
      <h3 style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', margin: '0 0 4px', fontWeight: 'var(--weight-semibold)' }}>{title}</h3>
      <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 2 }}>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Responsive: Desktop (≥1280px), Tablet (768–1279px), Mobile (&lt;768px) supported.</li>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Theme: Honors Dark/Light via design tokens; no hardcoded colors.</li>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Accessibility: Semantic landmarks, aria-labels, keyboard focus states.</li>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Performance: Memoized components, no network calls (Mock Mode).</li>
      </ul>
    </Card>
  );
}

export const EditPreview: React.FC = () => {
  return (
    <ProductLayout title="Edit Product" breadcrumbs={[{ label: 'Admin', href: '/admin/products' }, { label: 'Products' }, { label: 'Edit' }]}>
      <Card variant="outlined" padding="md">
        <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>
          The full editing workspace is available at <code>/preview/products/edit</code>. It reuses the Product Creation form architecture, change detection, lifecycle, publishing, version history and activity timeline.
        </p>
        <div style={{ marginTop: 'var(--space-3)' }}>
          <a href="/preview/products/edit">
            <Button variant="primary" leftIcon={<Icon name="Edit" size={16} />}>Open Editing Workspace</Button>
          </a>
        </div>
      </Card>
    </ProductLayout>
  );
};

export const HistoryPreview: React.FC = () => {
  const state = useProductEditState();
  return (
    <ProductLayout title="Version History" breadcrumbs={[{ label: 'Admin' }, { label: 'Products' }, { label: 'History' }]}>
      <VersionHistory
        versions={state.versions}
        previewVersionId={state.previewVersionId}
        canRestore={state.can('restore')}
        canDuplicate={state.can('update')}
        onPreview={(id) => state.previewVersion(id)}
        onClosePreview={() => state.previewVersion(null)}
        onRestore={state.restoreVersion}
        onDuplicate={state.duplicateVersion}
      />
      <PreviewNote title="Version History Notes" />
    </ProductLayout>
  );
};

export const TimelinePreview: React.FC = () => {
  const state = useProductEditState();
  return (
    <ProductLayout title="Activity Timeline" breadcrumbs={[{ label: 'Admin' }, { label: 'Products' }, { label: 'Timeline' }]}>
      <div style={{ maxWidth: 760 }}>
        <ActivityTimeline events={state.activity} />
      </div>
      <PreviewNote title="Activity Timeline Notes" />
    </ProductLayout>
  );
};

export const LifecyclePreviewPage: React.FC = () => {
  const state = useProductEditState();
  const crumbs: Crumb[] = [{ label: 'Admin' }, { label: 'Products' }, { label: 'Lifecycle' }];
  return (
    <ProductLayout title="Product Lifecycle" breadcrumbs={crumbs}>
      <Card variant="default" padding="md" style={{ marginBottom: 'var(--space-section-gap)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', flexWrap: 'wrap' }}>
          <LifecycleBadge state={state.lifecycle} size="md" />
          <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-sm)' }}>
            Current state: <strong style={{ color: 'var(--color-text-primary)' }}>{lifecycleLabel(state.lifecycle)}</strong> · SKU {MOCK_PRODUCT_SKU}
          </span>
        </div>
        <p style={{ margin: 'var(--space-3) 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>
          Lifecycle transitions are mock-only. Available actions are gated by mock role permissions. The available transitions from this state are listed below.
        </p>
      </Card>
      <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
        {PRODUCT_LIFECYCLE_STATES.map((meta) => {
          const isCurrent = meta.state === state.lifecycle;
          return (
            <li
              key={meta.state}
              style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', padding: '10px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: isCurrent ? 'var(--color-primary-alpha)' : 'transparent' }}
            >
              <LifecycleBadge state={meta.state} size="sm" />
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', minWidth: 140 }}>{meta.state}{isCurrent ? ' (current)' : ''}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{meta.description}</span>
            </li>
          );
        })}
      </ul>
      <PreviewNote title="Lifecycle Notes" />
    </ProductLayout>
  );
};

export const ComparePreview: React.FC = () => {
  const state = useProductEditState();
  const [compareId, setCompareId] = React.useState<string>('');
  const compareVersion = state.versions.find((v) => v.id === compareId) as ProductVersion | undefined;

  return (
    <ProductLayout title="Compare Versions" breadcrumbs={[{ label: 'Admin' }, { label: 'Products' }, { label: 'Compare' }]}>
      <Card variant="outlined" padding="md" style={{ marginBottom: 'var(--space-section-gap)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', flexWrap: 'wrap' }}>
          <Icon name="GitCompare" size={18} />
          <label htmlFor="compare-page-select" style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)' }}>
            Compare current with version:
          </label>
          <select
            id="compare-page-select"
            value={compareId}
            onChange={(e) => setCompareId(e.target.value)}
            style={{ padding: '8px 12px', fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', background: 'var(--color-bg-background)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-input)' }}
          >
            <option value="">Select a version…</option>
            {state.versions.map((v) => (
              <option key={v.id} value={v.id}>Version {v.version} · {v.reason ?? v.summary ?? '—'}</option>
            ))}
          </select>
        </div>
      </Card>
      {compareVersion ? (
        <CompareView current={state.data} compare={compareVersion.data} compareLabel={`Version ${compareVersion.version}`} />
      ) : (
        <Card variant="ghost" padding="md">
          <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Select a version above to see a side-by-side, highlighted comparison.</p>
        </Card>
      )}
      <PreviewNote title="Comparison Notes" />
    </ProductLayout>
  );
};
