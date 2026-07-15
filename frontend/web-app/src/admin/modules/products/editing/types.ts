import type { ProductWizardData, WizardErrors } from '../creation/types';
import type { ProductLifecycleState } from '../types';

export type EditLifecycleState = ProductLifecycleState;

export interface ProductVersion {
  id: string;
  version: number;
  status: EditLifecycleState;
  modifiedBy: string;
  createdAt: string;
  reason?: string;
  summary?: string;
  data: ProductWizardData;
}

export type EditActivityType =
  | 'created'
  | 'edited'
  | 'draft_saved'
  | 'review_requested'
  | 'approved'
  | 'rejected'
  | 'published'
  | 'scheduled'
  | 'archived'
  | 'restored'
  | 'duplicated'
  | 'deleted'
  | 'viewed'
  | 'comment';

export interface EditActivityEvent {
  id: string;
  type: EditActivityType;
  message: string;
  actor: string;
  timestamp: string;
}

export interface EditingSettings {
  requireApproval: boolean;
  autoPublish: boolean;
  lockOnReview: boolean;
  notifyOnPublish: boolean;
}

export type EditSectionId =
  | 'overview'
  | 'basic'
  | 'classification'
  | 'packaging'
  | 'pricing'
  | 'seo'
  | 'publishing'
  | 'activity'
  | 'history'
  | 'settings';

export interface ChangedField {
  field: keyof ProductWizardData;
  label: string;
  section: EditSectionId;
  oldValue: unknown;
  newValue: unknown;
}

export interface EditingState {
  data: ProductWizardData;
  original: ProductWizardData;
  errors: WizardErrors;
  lifecycle: EditLifecycleState;
  versions: ProductVersion[];
  activity: EditActivityEvent[];
  settings: EditingSettings;
  changed: ChangedField[];
  unsaved: boolean;
}
