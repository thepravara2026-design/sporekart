import React from 'react';
import type { DraftMeta, ProductWizardData, SubmittedProduct, WizardErrors, WizardStepId } from './types';
import {
  DRAFT_STORAGE_KEY,
  STEP_ORDER,
  validateStep,
  validateAll,
} from './validation';

function createDefaultData(): ProductWizardData {
  return {
    name: '',
    description: '',
    shortDescription: '',
    productType: '',
    brand: '',
    manufacturer: '',
    sku: '',
    barcode: '',
    category: '',
    collection: [],
    tags: [],
    status: 'draft',
    productFamily: '',
    productGroup: '',
    mushroomType: '',
    growingMethod: '',
    season: 'all',
    productNature: 'retail',
    attributes: [],
    packagingType: 'box',
    packageSize: '',
    unitsPerPack: 1,
    weight: 0,
    weightUnit: 'g',
    dimensions: { length: 0, width: 0, height: 0, unit: 'cm' },
    packageWeight: 0,
    packageWeightUnit: 'g',
    shelfLife: '',
    storageConditions: '',
    countryOfOrigin: '',
    packagingNotes: '',
    mrp: 0,
    price: 0,
    wholesalePrice: 0,
    discount: 0,
    cost: 0,
    currency: 'INR',
    taxClass: 'standard',
    stockKeepingUnit: '',
    hsnCode: '',
    gst: 0,
    priceNotes: '',
    metaTitle: '',
    metaDescription: '',
    slug: '',
    keywords: [],
    canonicalUrl: '',
    ogTitle: '',
    ogDescription: '',
  };
}

function generateDraftId(): string {
  const rand = Math.random().toString(36).slice(2, 8).toUpperCase();
  return `SK-DRAFT-${rand}`;
}

function generateProductId(): string {
  const rand = Math.floor(1000 + Math.random() * 9000);
  return `SK-PROD-${rand}`;
}

interface UseWizardStateOptions {
  initialStep?: WizardStepId;
  submittedProduct?: SubmittedProduct | null;
}

export interface WizardState {
  data: ProductWizardData;
  step: WizardStepId;
  visited: Set<WizardStepId>;
  errors: WizardErrors;
  isDraft: boolean;
  draftId: string | null;
  draftMeta: DraftMeta | null;
  unsaved: boolean;
  previewOpen: boolean;
  submitting: boolean;
  initialLoading: boolean;
  submittedProduct: SubmittedProduct | null;
  setField: (name: keyof ProductWizardData, value: unknown) => void;
  setData: (updater: (prev: ProductWizardData) => ProductWizardData) => void;
  goToStep: (id: WizardStepId) => void;
  next: () => void;
  prev: () => void;
  saveDraft: () => void;
  loadDraft: () => boolean;
  hasDraft: () => boolean;
  discardDraft: () => void;
  submit: () => string | null;
  reset: () => void;
  setPreviewOpen: (open: boolean) => void;
  togglePreview: () => void;
}

export function useWizardState(options: UseWizardStateOptions = {}): WizardState {
  const { initialStep = 'basic', submittedProduct = null } = options;

  const [data, setData] = React.useState<ProductWizardData>(createDefaultData);
  const [step, setStep] = React.useState<WizardStepId>(initialStep);
  const [visited, setVisited] = React.useState<Set<WizardStepId>>(new Set([initialStep]));
  const [errors, setErrors] = React.useState<WizardErrors>({});
  const [isDraft, setIsDraft] = React.useState(false);
  const [draftId, setDraftId] = React.useState<string | null>(null);
  const [draftMeta, setDraftMeta] = React.useState<DraftMeta | null>(null);
  const [unsaved, setUnsaved] = React.useState(false);
  const [previewOpen, setPreviewOpen] = React.useState(false);
  const [submitting, setSubmitting] = React.useState(false);
  const [initialLoading, setInitialLoading] = React.useState(true);
  const [submitted, setSubmitted] = React.useState<SubmittedProduct | null>(submittedProduct);

  React.useEffect(() => {
    const t = setTimeout(() => setInitialLoading(false), 450);
    return () => clearTimeout(t);
  }, []);

  const setField = React.useCallback((name: keyof ProductWizardData, value: unknown) => {
    setData((prev) => ({ ...prev, [name]: value }));
    setUnsaved(true);
    setErrors((prevErrors) => {
      if (!prevErrors[name]) return prevErrors;
      const next = { ...prevErrors };
      delete next[name];
      return next;
    });
  }, []);

  const setDataRaw = React.useCallback((updater: (prev: ProductWizardData) => ProductWizardData) => {
    setData(updater);
    setUnsaved(true);
  }, []);

  const markVisited = React.useCallback((id: WizardStepId) => {
    setVisited((prev) => {
      if (prev.has(id)) return prev;
      const next = new Set(prev);
      next.add(id);
      return next;
    });
  }, []);

  const goToStep = React.useCallback(
    (id: WizardStepId) => {
      setStep(id);
      markVisited(id);
    },
    [markVisited],
  );

  const next = React.useCallback(() => {
    const stepErrors = validateStep(step, data);
    setErrors((prev) => ({ ...prev, ...stepErrors }));
    const idx = STEP_ORDER.indexOf(step);
    if (Object.keys(stepErrors).length > 0) return;
    const nextIdx = Math.min(idx + 1, STEP_ORDER.length - 1);
    const nextId = STEP_ORDER[nextIdx];
    setStep(nextId);
    markVisited(nextId);
  }, [step, data, markVisited]);

  const prev = React.useCallback(() => {
    const idx = STEP_ORDER.indexOf(step);
    const prevIdx = Math.max(idx - 1, 0);
    const prevId = STEP_ORDER[prevIdx];
    setStep(prevId);
    markVisited(prevId);
  }, [step, markVisited]);

  const saveDraft = React.useCallback(() => {
    const id = draftId ?? generateDraftId();
    const meta: DraftMeta = { draftId: id, savedAt: new Date().toISOString(), step };
    const payload = { data, step, meta };
    try {
      sessionStorage.setItem(DRAFT_STORAGE_KEY, JSON.stringify(payload));
    } catch {
      /* Mock mode: ignore storage failures */
    }
    setDraftId(id);
    setDraftMeta(meta);
    setIsDraft(true);
    setUnsaved(false);
  }, [data, step, draftId]);

  const readDraft = React.useCallback((): { data: ProductWizardData; step: WizardStepId; meta: DraftMeta } | null => {
    try {
      const raw = sessionStorage.getItem(DRAFT_STORAGE_KEY);
      if (!raw) return null;
      const parsed = JSON.parse(raw);
      if (!parsed || !parsed.data) return null;
      return parsed;
    } catch {
      return null;
    }
  }, []);

  const hasDraft = React.useCallback(() => readDraft() !== null, [readDraft]);

  const loadDraft = React.useCallback((): boolean => {
    const parsed = readDraft();
    if (!parsed) return false;
    setData({ ...createDefaultData(), ...parsed.data });
    setStep(parsed.step ?? 'basic');
    setDraftId(parsed.meta?.draftId ?? null);
    setDraftMeta(parsed.meta ?? null);
    setIsDraft(true);
    setUnsaved(false);
    markVisited(parsed.step ?? 'basic');
    return true;
  }, [readDraft, markVisited]);

  const discardDraft = React.useCallback(() => {
    try {
      sessionStorage.removeItem(DRAFT_STORAGE_KEY);
    } catch {
      /* ignore */
    }
    setDraftId(null);
    setDraftMeta(null);
    setIsDraft(false);
    setUnsaved(false);
    setData(createDefaultData());
    setStep('basic');
    setVisited(new Set(['basic']));
    setErrors({});
    setSubmitted(null);
  }, []);

  const submit = React.useCallback((): string | null => {
    const allErrors = validateAll(data);
    setErrors(allErrors);
    if (Object.keys(allErrors).length > 0) return null;
    const id = generateProductId();
    setSubmitting(true);
    setTimeout(() => {
      setSubmitted({
        id,
        name: data.name,
        sku: data.sku,
        status: 'draft',
        createdAt: new Date().toISOString(),
      });
      setStep('confirmation');
      markVisited('confirmation');
      setIsDraft(false);
      setSubmitting(false);
      try {
        sessionStorage.removeItem(DRAFT_STORAGE_KEY);
      } catch {
        /* ignore */
      }
    }, 600);
    return id;
  }, [data, markVisited]);

  const reset = React.useCallback(() => {
    setData(createDefaultData());
    setStep(initialStep);
    setVisited(new Set([initialStep]));
    setErrors({});
    setIsDraft(false);
    setDraftId(null);
    setDraftMeta(null);
    setUnsaved(false);
    setSubmitted(null);
  }, [initialStep]);

  const togglePreview = React.useCallback(() => setPreviewOpen((p) => !p), []);

  return {
    data,
    step,
    visited,
    errors,
    isDraft,
    draftId,
    draftMeta,
    unsaved,
    previewOpen,
    submitting,
    initialLoading,
    submittedProduct: submitted,
    setField,
    setData: setDataRaw,
    goToStep,
    next,
    prev,
    saveDraft,
    loadDraft,
    hasDraft,
    discardDraft,
    submit,
    reset,
    setPreviewOpen,
    togglePreview,
  };
}
