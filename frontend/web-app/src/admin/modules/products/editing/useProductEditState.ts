import React from 'react';
import type { ProductWizardData, WizardErrors } from '../creation/types';
import { validateAll } from '../creation/validation';
import { canProduct, type ProductRole } from '../permissions';
import { PRODUCT_LIFECYCLE_STATES } from '../lifecycle';
import type { ProductLifecycleState } from '../types';
import {
  mockCurrentProduct,
  mockVersions,
  mockActivity,
  mockSettings,
  MOCK_PRODUCT_ID,
  MOCK_MODIFIED_BY,
} from './mockEditData';
import { diffProducts, modifiedSections, FIELD_SECTIONS as FIELD_SECTION_MAP } from './changeDetection';
import type { ChangedField, EditActivityEvent, EditSectionId, EditingSettings, ProductVersion } from './types';

export const CURRENT_ROLE: ProductRole = 'manager';

const DRAFT_STORAGE_KEY = `sporekart:product-edit:${MOCK_PRODUCT_ID}`;

export function lifecycleTransitions(state: ProductLifecycleState): ProductLifecycleState[] {
  const map: Record<ProductLifecycleState, ProductLifecycleState[]> = {
    draft: ['under_review', 'deleted'],
    under_review: ['approved', 'draft'],
    approved: ['published', 'scheduled', 'archived'],
    scheduled: ['published', 'draft'],
    published: ['active', 'inactive', 'archived', 'deleted'],
    active: ['inactive', 'archived', 'deleted'],
    inactive: ['active', 'archived', 'deleted'],
    archived: ['draft', 'deleted'],
    deleted: ['draft'],
  };
  return map[state] ?? [];
}

function uid(prefix: string): string {
  return `${prefix}-${Math.random().toString(36).slice(2, 8)}`;
}

export interface UseProductEditStateOptions {
  productId?: string;
}

export interface ProductEditState {
  productId: string;
  data: ProductWizardData;
  original: ProductWizardData;
  errors: WizardErrors;
  changed: ChangedField[];
  modifiedSections: EditSectionId[];
  unsaved: boolean;
  lifecycle: ProductLifecycleState;
  versions: ProductVersion[];
  activity: EditActivityEvent[];
  settings: EditingSettings;
  section: EditSectionId;
  previewVersionId: string | null;
  compareVersionId: string | null;
  duplicated: { id: string; name: string } | null;
  can: (action: Parameters<typeof canProduct>[1]) => boolean;
  setField: (name: keyof ProductWizardData, value: unknown) => void;
  setSection: (section: EditSectionId) => void;
  saveDraft: () => void;
  discardChanges: () => void;
  resetSection: (section: EditSectionId) => void;
  submit: () => void;
  applyTransition: (target: ProductLifecycleState, note?: string) => void;
  submitForReview: () => void;
  approve: () => void;
  reject: () => void;
  schedulePublish: (when?: string) => void;
  publish: () => void;
  unpublish: () => void;
  archive: () => void;
  restore: () => void;
  remove: () => void;
  previewVersion: (id: string | null) => void;
  restoreVersion: (id: string) => void;
  duplicateVersion: (id: string) => void;
  duplicateProduct: () => void;
}

export function useProductEditState(options: UseProductEditStateOptions = {}): ProductEditState {
  const productId = options.productId ?? MOCK_PRODUCT_ID;

  const [data, setData] = React.useState<ProductWizardData>(mockCurrentProduct);
  const [original, setOriginal] = React.useState<ProductWizardData>(mockCurrentProduct);
  const [lifecycle, setLifecycle] = React.useState<ProductLifecycleState>('published');
  const [versions, setVersions] = React.useState<ProductVersion[]>(mockVersions);
  const [activity, setActivity] = React.useState<EditActivityEvent[]>(mockActivity);
  const [settings] = React.useState<EditingSettings>(mockSettings);
  const [section, setSection] = React.useState<EditSectionId>('overview');
  const [previewVersionId, setPreviewVersionId] = React.useState<string | null>(null);
  const [compareVersionId] = React.useState<string | null>(null);
  const [duplicated, setDuplicated] = React.useState<{ id: string; name: string } | null>(null);

  const errors = React.useMemo(() => validateAll(data), [data]);
  const changed = React.useMemo(() => diffProducts(original, data), [original, data]);
  const unsaved = changed.length > 0;
  const modifiedSectionsList = React.useMemo(() => modifiedSections(changed), [changed]);

  const addActivity = React.useCallback((type: EditActivityEvent['type'], message: string) => {
    setActivity((prev) => [
      { id: uid('act'), type, message, actor: MOCK_MODIFIED_BY, timestamp: new Date().toISOString() },
      ...prev,
    ]);
  }, []);

  const setField = React.useCallback((name: keyof ProductWizardData, value: unknown) => {
    setData((prev) => ({ ...prev, [name]: value }));
  }, []);

  const saveDraft = React.useCallback(() => {
    try {
      sessionStorage.setItem(DRAFT_STORAGE_KEY, JSON.stringify({ data, lifecycle, savedAt: new Date().toISOString() }));
    } catch {
      /* mock: ignore */
    }
    addActivity('draft_saved', 'Draft saved');
  }, [data, lifecycle, addActivity]);

  const discardChanges = React.useCallback(() => {
    setData(original);
  }, [original]);

  const resetSection = React.useCallback(
    (sec: EditSectionId) => {
      setData((prev) => {
        const next = { ...prev };
        const keys = Object.keys(next) as (keyof ProductWizardData)[];
        for (const k of keys) {
          if (FIELD_SECTION_MAP[k] === sec) {
            (next as Record<string, unknown>)[k] = original[k];
          }
        }
        return next;
      });
    },
    [original],
  );

  const submit = React.useCallback(() => {
    const allErrors = validateAll(data);
    if (Object.keys(allErrors).length > 0) return;
    const newVersion: ProductVersion = {
      id: uid('ver'),
      version: versions.length + 1,
      status: lifecycle,
      modifiedBy: MOCK_MODIFIED_BY,
      createdAt: new Date().toISOString(),
      reason: 'Manual edit',
      summary: `${changed.length} field(s) updated`,
      data: { ...data },
    };
    setVersions((prev) => [...prev, newVersion]);
    setOriginal({ ...data });
    addActivity('edited', `Updated ${changed.length} field(s)`);
  }, [data, lifecycle, versions, changed.length, addActivity]);

  const applyTransition = React.useCallback(
    (target: ProductLifecycleState, note?: string) => {
      setLifecycle(target);
      const meta = PRODUCT_LIFECYCLE_STATES.find((s) => s.state === target);
      addActivity('edited', note ?? `Lifecycle changed to ${meta?.label ?? target}`);
    },
    [addActivity],
  );

  const submitForReview = React.useCallback(() => {
    if (settings.requireApproval) {
      applyTransition('under_review', 'Submitted for review');
      addActivity('review_requested', 'Submitted for review');
    } else {
      applyTransition('published', 'Published (auto-approve)');
      addActivity('published', 'Published');
    }
  }, [settings.requireApproval, applyTransition, addActivity]);

  const approve = React.useCallback(() => {
    applyTransition('approved', 'Approved');
    addActivity('approved', 'Approved');
  }, [applyTransition, addActivity]);

  const reject = React.useCallback(() => {
    applyTransition('draft', 'Rejected — returned to draft');
    addActivity('rejected', 'Changes rejected');
  }, [applyTransition, addActivity]);

  const schedulePublish = React.useCallback(
    (when?: string) => {
      applyTransition('scheduled', when ? `Scheduled to publish ${when}` : 'Scheduled to publish');
      addActivity('scheduled', 'Scheduled for publishing');
    },
    [applyTransition, addActivity],
  );

  const publish = React.useCallback(() => {
    applyTransition('published', 'Published');
    addActivity('published', 'Published');
  }, [applyTransition, addActivity]);

  const unpublish = React.useCallback(() => {
    applyTransition('inactive', 'Unpublished');
    addActivity('edited', 'Unpublished');
  }, [applyTransition, addActivity]);

  const archive = React.useCallback(() => {
    applyTransition('archived', 'Archived');
    addActivity('archived', 'Archived');
  }, [applyTransition, addActivity]);

  const restore = React.useCallback(() => {
    applyTransition('draft', 'Restored to draft');
    addActivity('restored', 'Restored');
  }, [applyTransition, addActivity]);

  const remove = React.useCallback(() => {
    applyTransition('deleted', 'Deleted');
    addActivity('deleted', 'Deleted');
  }, [applyTransition, addActivity]);

  const previewVersion = React.useCallback((id: string | null) => setPreviewVersionId(id), []);

  const restoreVersion = React.useCallback(
    (id: string) => {
      const v = versions.find((x) => x.id === id);
      if (!v) return;
      setData({ ...v.data });
      addActivity('restored', `Restored from version ${v.version}`);
    },
    [versions, addActivity],
  );

  const duplicateVersion = React.useCallback(
    (id: string) => {
      const v = versions.find((x) => x.id === id);
      if (!v) return;
      const copy: ProductVersion = {
        id: uid('ver'),
        version: versions.length + 1,
        status: v.status,
        modifiedBy: MOCK_MODIFIED_BY,
        createdAt: new Date().toISOString(),
        reason: `Duplicate of v${v.version}`,
        summary: `Copied from version ${v.version}`,
        data: { ...v.data },
      };
      setVersions((prev) => [...prev, copy]);
      addActivity('duplicated', `Duplicated version ${v.version}`);
    },
    [versions, addActivity],
  );

  const duplicateProduct = React.useCallback(() => {
    const newId = uid('SK-PROD');
    const copyName = `${data.name} (Copy)`;
    setDuplicated({ id: newId, name: copyName });
    addActivity('duplicated', `Duplicated product as ${copyName}`);
  }, [data.name, addActivity]);

  const can = React.useCallback((action: Parameters<typeof canProduct>[1]) => canProduct(CURRENT_ROLE, action), []);

  return {
    productId,
    data,
    original,
    errors,
    changed,
    modifiedSections: modifiedSectionsList,
    unsaved,
    lifecycle,
    versions,
    activity,
    settings,
    section,
    previewVersionId,
    compareVersionId,
    duplicated,
    can,
    setField,
    setSection,
    saveDraft,
    discardChanges,
    resetSection,
    submit,
    applyTransition,
    submitForReview,
    approve,
    reject,
    schedulePublish,
    publish,
    unpublish,
    archive,
    restore,
    remove,
    previewVersion,
    restoreVersion,
    duplicateVersion,
    duplicateProduct,
  };
}
