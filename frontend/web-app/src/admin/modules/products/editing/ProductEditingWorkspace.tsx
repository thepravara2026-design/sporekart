import React from 'react';
import Card from '../../../../design-system/components/composite/Card';
import Button from '../../../../design-system/components/core/Button';
import Icon from '../../../../design-system/icons/Icon';
import StatusBadge from '../../../components/status/StatusBadge';
import { type Crumb } from '../../../../design-system/components/navigation/Breadcrumb';
import { ProductLayout } from '../layout/ProductLayout';
import { lifecycleLabel, lifecycleToBadge } from '../lifecycle';
import BasicInfoStep from '../creation/steps/BasicInfoStep';
import ClassificationStep from '../creation/steps/ClassificationStep';
import PackagingStep from '../creation/steps/PackagingStep';
import PricingStep from '../creation/steps/PricingStep';
import SeoStep from '../creation/steps/SeoStep';
import { useProductEditState, CURRENT_ROLE } from './useProductEditState';
import { lifecycleTransitions } from './useProductEditState';
import { CompareView } from './CompareView';
import LifecyclePanel from './LifecyclePanel';
import PublishingPanel from './PublishingPanel';
import { VersionHistory } from './VersionHistory';
import ActivityTimeline from './ActivityTimeline';
import { ReviewPanel } from './ReviewPanel';
import { ChangeDetectionPanel } from './ChangeDetectionPanel';
import UnsavedChangesGuard, { useUnsavedGuard } from './UnsavedChangesGuard';
import type { EditSectionId } from './types';
import { MOCK_PRODUCT_ID, MOCK_PRODUCT_SKU } from './mockEditData';

interface EditingSectionDef {
  id: EditSectionId;
  label: string;
  icon: string;
  editable: boolean;
}

const EDITING_SECTIONS: EditingSectionDef[] = [
  { id: 'overview', label: 'Overview', icon: 'grid', editable: false },
  { id: 'basic', label: 'Basic Info', icon: 'info', editable: true },
  { id: 'classification', label: 'Classification', icon: 'layers', editable: true },
  { id: 'packaging', label: 'Packaging', icon: 'package', editable: true },
  { id: 'pricing', label: 'Pricing', icon: 'dollar-sign', editable: true },
  { id: 'seo', label: 'SEO', icon: 'search', editable: true },
  { id: 'publishing', label: 'Publishing', icon: 'upload', editable: false },
  { id: 'activity', label: 'Activity', icon: 'activity', editable: false },
  { id: 'history', label: 'Version History', icon: 'history', editable: false },
  { id: 'settings', label: 'Settings', icon: 'settings', editable: false },
];

function EditingNav({
  active,
  onChange,
  modifiedSections,
}: {
  active: EditSectionId;
  onChange: (s: EditSectionId) => void;
  modifiedSections: EditSectionId[];
}) {
  const guard = useUnsavedGuard();
  return (
    <nav aria-label="Product editing sections" style={{ display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' }}>
      {EDITING_SECTIONS.map((section) => {
        const isActive = section.id === active;
        const isModified = modifiedSections.includes(section.id);
        return (
          <button
            key={section.id}
            type="button"
            aria-current={isActive ? 'page' : undefined}
            onClick={() =>
              guard.requestLeave(() => onChange(section.id))
            }
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--space-inline-xs)',
              padding: '8px 12px',
              borderRadius: 'var(--radius-sm)',
              border: 'none',
              cursor: 'pointer',
              textAlign: 'left',
              fontFamily: 'var(--font-family-sans)',
              fontSize: 'var(--text-body-sm)',
              background: isActive ? 'var(--color-primary-alpha)' : 'transparent',
              color: isActive ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: isActive ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            }}
          >
            <Icon name={section.icon} size={16} />
            <span style={{ flex: 1 }}>{section.label}</span>
            {section.editable && isModified && (
              <span
                aria-label="modified"
                style={{ width: 8, height: 8, borderRadius: 'var(--radius-full)', background: 'var(--color-warning)' }}
              />
            )}
          </button>
        );
      })}
    </nav>
  );
}

function UnsavedBanner({ onSave, onDiscard }: { onSave: () => void; onDiscard: () => void }) {
  return (
    <div
      role="alert"
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 'var(--space-3)',
        padding: 'var(--space-2) var(--space-4)',
        background: 'var(--color-bg-warning-weak)',
        border: 'var(--border-width-thin) solid var(--color-border-warning)',
        borderRadius: 'var(--radius-md)',
        fontSize: 'var(--text-body-sm)',
        color: 'var(--color-text-primary)',
      }}
    >
      <Icon name="AlertTriangle" size={16} style={{ color: 'var(--color-warning)' }} />
      <span style={{ flex: 1, fontWeight: 'var(--weight-medium)' }}>You have unsaved changes.</span>
      <Button variant="primary" size="sm" onClick={onSave} leftIcon={<Icon name="Save" size={16} />}>
        Save Draft
      </Button>
      <Button variant="ghost" size="sm" onClick={onDiscard} leftIcon={<Icon name="Undo" size={16} />}>
        Discard
      </Button>
    </div>
  );
}

function SettingsSection({ settings, role }: { settings: Record<string, boolean>; role: string }) {
  return (
    <Card variant="default" padding="lg">
      <h3 style={{ margin: '0 0 var(--space-3)', color: 'var(--color-text-primary)', fontSize: 'var(--text-lg)' }}>Editing Settings</h3>
      <p style={{ margin: '0 0 var(--space-4)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-sm)' }}>
        These preferences control the draft &amp; publish workflow for this product. They are mock-only in this sprint.
      </p>
      <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {Object.entries(settings).map(([key, value]) => (
          <li key={key} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: 'var(--space-2) 0', borderBottom: 'var(--border-width-thin) solid var(--color-border)' }}>
            <span style={{ color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', textTransform: 'capitalize' }}>{key.replace(/([A-Z])/g, ' $1')}</span>
            <StatusBadge status={value ? 'On' : 'Off'} variant={value ? 'success' : 'neutral'} size="sm" />
          </li>
        ))}
      </ul>
      <h4 style={{ margin: 'var(--space-4) 0 var(--space-2)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)' }}>Active Role (Mock)</h4>
      <StatusBadge status={role} variant="info" size="sm" />
    </Card>
  );
}

const ProductEditingWorkspaceBase: React.FC = () => {
  const state = useProductEditState();
  const [compareVersionId, setCompareVersionId] = React.useState<string | null>(null);
  const [duplicatedName, setDuplicatedName] = React.useState<string>('');

  const crumbs: Crumb[] = [
    { label: 'Admin', href: '/admin/products' },
    { label: 'Products', href: '/admin/products' },
    { label: 'Edit', href: `/preview/products/edit` },
  ];

  const available = lifecycleTransitions(state.lifecycle);

  const compareVersion = React.useMemo(
    () => state.versions.find((v) => v.id === compareVersionId) ?? null,
    [state.versions, compareVersionId],
  );

  const title = state.data.name;

  const headerActions = (
    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', flexWrap: 'wrap' }}>
      <StatusBadge status={lifecycleLabel(state.lifecycle)} variant={lifecycleToBadge(state.lifecycle)} size="sm" />
      <Button variant="secondary" size="sm" onClick={state.saveDraft} leftIcon={<Icon name="Save" size={16} />}>
        Save Draft
      </Button>
      <Button
        variant="primary"
        size="sm"
        disabled={Object.keys(state.errors).length > 0}
        onClick={state.submit}
        leftIcon={<Icon name="Check" size={16} />}
      >
        Save Changes
      </Button>
    </div>
  );

  const renderSection = () => {
    switch (state.section) {
      case 'overview':
        return (
          <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1.4fr) minmax(0, 1fr)', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
              <ChangeDetectionPanel
                changed={state.changed}
                original={state.original as unknown as Record<string, unknown>}
                current={state.data as unknown as Record<string, unknown>}
                onDiscardAll={state.discardChanges}
                onResetSection={state.resetSection}
              />
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
            </div>
            <ReviewPanel data={state.data} errors={state.errors} changed={state.changed} modifiedSections={state.modifiedSections} />
          </div>
        );
      case 'basic':
      case 'classification':
      case 'packaging':
      case 'pricing':
      case 'seo': {
        const stepProps = { data: state.data, errors: state.errors, setField: state.setField };
        const editable = state.can('update');
        const banner = !editable ? (
          <div style={{ marginBottom: 'var(--space-3)', padding: 'var(--space-2) var(--space-4)', background: 'var(--color-bg-surface-raised)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
            <Icon name="Lock" size={14} /> You have view-only access. Editing is disabled for your role.
          </div>
        ) : null;
        return (
          <div>
            {banner}
            <div style={editable ? undefined : { opacity: 0.7, pointerEvents: 'none' }}>
              {state.section === 'basic' && <BasicInfoStep {...stepProps} />}
              {state.section === 'classification' && <ClassificationStep {...stepProps} />}
              {state.section === 'packaging' && <PackagingStep {...stepProps} />}
              {state.section === 'pricing' && <PricingStep {...stepProps} />}
              {state.section === 'seo' && <SeoStep {...stepProps} />}
            </div>
          </div>
        );
      }
      case 'publishing':
        return (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
            <LifecyclePanel
              state={state.lifecycle}
              available={available}
              canApprove={state.can('approve')}
              canArchive={state.can('archive')}
              canRestore={state.can('restore')}
              canDelete={state.can('delete')}
              onTransition={(target) => state.applyTransition(target)}
            />
            <PublishingPanel
              lifecycle={state.lifecycle}
              requireApproval={state.settings.requireApproval}
              canPublish={state.can('publish')}
              canApprove={state.can('approve')}
              canArchive={state.can('archive')}
              canRestore={state.can('restore')}
              canDelete={state.can('delete')}
              onSaveDraft={state.saveDraft}
              onSubmitForReview={state.submitForReview}
              onApprove={state.approve}
              onReject={state.reject}
              onSchedule={state.schedulePublish}
              onPublish={state.publish}
              onUnpublish={state.unpublish}
              onArchive={state.archive}
              onRestore={state.restore}
              onDelete={state.remove}
            />
          </div>
        );
      case 'activity':
        return (
          <div style={{ maxWidth: 720 }}>
            <ActivityTimeline events={state.activity} />
          </div>
        );
      case 'history':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
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
            <Card variant="outlined" padding="md">
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', flexWrap: 'wrap' }}>
                <Icon name="GitCompare" size={18} />
                <label htmlFor="compare-select" style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-medium)' }}>
                  Compare current version with:
                </label>
                <select
                  id="compare-select"
                  value={compareVersionId ?? ''}
                  onChange={(e) => setCompareVersionId(e.target.value || null)}
                  style={{
                    padding: '8px 12px',
                    fontFamily: 'var(--font-family-sans)',
                    fontSize: 'var(--text-body-sm)',
                    color: 'var(--color-text-primary)',
                    background: 'var(--color-bg-background)',
                    border: '1px solid var(--color-border-default)',
                    borderRadius: 'var(--radius-input)',
                  }}
                >
                  <option value="">Select a version…</option>
                  {state.versions.map((v) => (
                    <option key={v.id} value={v.id}>
                      Version {v.version} · {v.reason ?? v.summary ?? '—'}
                    </option>
                  ))}
                </select>
              </div>
            </Card>
            {compareVersion && (
              <CompareView current={state.data} compare={compareVersion.data} compareLabel={`Version ${compareVersion.version}`} />
            )}
          </div>
        );
      case 'settings':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', maxWidth: 720 }}>
            <SettingsSection settings={state.settings as unknown as Record<string, boolean>} role={CURRENT_ROLE} />
            <Card variant="outlined" padding="lg">
              <h3 style={{ margin: '0 0 var(--space-3)', color: 'var(--color-text-primary)', fontSize: 'var(--text-lg)' }}>Duplicate Product</h3>
              {!state.duplicated ? (
                <Button variant="secondary" onClick={state.duplicateProduct} leftIcon={<Icon name="Copy" size={16} />}>
                  Duplicate this product
                </Button>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
                  <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                    A copy was created with id <code>{state.duplicated.id}</code>.
                  </div>
                  <div style={{ display: 'flex', gap: 'var(--space-2)', alignItems: 'center', flexWrap: 'wrap' }}>
                    <input
                      aria-label="Duplicate product name"
                      value={duplicatedName || state.duplicated.name}
                      onChange={(e) => setDuplicatedName(e.target.value)}
                      placeholder="Rename copy"
                      style={{
                        padding: '8px 12px',
                        fontFamily: 'var(--font-family-sans)',
                        fontSize: 'var(--text-body-sm)',
                        color: 'var(--color-text-primary)',
                        background: 'var(--color-bg-background)',
                        border: '1px solid var(--color-border-default)',
                        borderRadius: 'var(--radius-input)',
                        minWidth: 240,
                      }}
                    />
                    <Button variant="primary" size="sm" onClick={() => undefined} leftIcon={<Icon name="ExternalLink" size={16} />}>
                      Open Duplicate
                    </Button>
                    <Button variant="ghost" size="sm" onClick={() => undefined} leftIcon={<Icon name="Eye" size={16} />}>
                      Preview Duplicate
                    </Button>
                  </div>
                  <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
                    Mock only — inventory and marketplace references are placeholders for future integration.
                  </p>
                </div>
              )}
            </Card>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <UnsavedChangesGuard unsaved={state.unsaved} title={title} onDiscard={state.discardChanges}>
      <ProductLayout title={`Edit Product · ${title}`} breadcrumbs={crumbs} actions={headerActions}>
        {state.unsaved && (
          <div style={{ marginBottom: 'var(--space-section-gap)' }}>
            <UnsavedBanner onSave={state.saveDraft} onDiscard={state.discardChanges} />
          </div>
        )}

        <div style={{ display: 'grid', gridTemplateColumns: 'minmax(200px, 240px) 1fr', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
          <aside style={{ position: 'sticky', top: 'var(--space-component-gap)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <EditingNav active={state.section} onChange={state.setSection} modifiedSections={state.modifiedSections} />
          </aside>
          <div style={{ minWidth: 0 }}>{renderSection()}</div>
        </div>

        <footer style={{ marginTop: 'var(--space-section-gap)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Product ID {MOCK_PRODUCT_ID} · SKU {MOCK_PRODUCT_SKU} · Role: {CURRENT_ROLE} · Mock Mode — no persistence.
        </footer>
      </ProductLayout>
    </UnsavedChangesGuard>
  );
};

export const ProductEditingWorkspace = React.memo(ProductEditingWorkspaceBase);
export default ProductEditingWorkspace;
