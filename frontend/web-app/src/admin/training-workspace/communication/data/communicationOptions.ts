// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Filter/Search Options & Presentation Maps
// Sprint 26 · Part 10. Static, derived from taxonomy. No backend.
// ---------------------------------------------------------------------------

import {
  AUDIENCE_LABELS,
  CATEGORY_LABELS,
  CHANNEL_LABELS,
  COMMUNICATION_STATUS_LABELS,
  PRIORITY_LABELS,
  TEMPLATE_KIND_LABELS,
} from './communicationTypes';
import type {
  AudienceScope,
  CommunicationCategory,
  CommunicationPriority,
  CommunicationStatus,
  DeliveryChannel,
  TemplateKind,
} from './communicationTypes';

export interface SelectOption<T extends string = string> {
  value: T;
  label: string;
}

function toOptions<T extends string>(map: Record<T, string>): SelectOption<T>[] {
  return (Object.keys(map) as T[]).map((value) => ({ value, label: map[value] }));
}

export const STATUS_OPTIONS: SelectOption<CommunicationStatus>[] = toOptions(COMMUNICATION_STATUS_LABELS);
export const PRIORITY_OPTIONS: SelectOption<CommunicationPriority>[] = toOptions(PRIORITY_LABELS);
export const CATEGORY_OPTIONS: SelectOption<CommunicationCategory>[] = toOptions(CATEGORY_LABELS);
export const CHANNEL_OPTIONS: SelectOption<DeliveryChannel>[] = toOptions(CHANNEL_LABELS);
export const AUDIENCE_OPTIONS: SelectOption<AudienceScope>[] = toOptions(AUDIENCE_LABELS);
export const TEMPLATE_KIND_OPTIONS: SelectOption<TemplateKind>[] = toOptions(TEMPLATE_KIND_LABELS);

// ---- Sort options ----------------------------------------------------------
export type AnnouncementSortKey = 'newest' | 'oldest' | 'priority' | 'title' | 'views';

export const ANNOUNCEMENT_SORT_OPTIONS: SelectOption<AnnouncementSortKey>[] = [
  { value: 'newest', label: 'Newest first' },
  { value: 'oldest', label: 'Oldest first' },
  { value: 'priority', label: 'Priority (high → low)' },
  { value: 'title', label: 'Title (A → Z)' },
  { value: 'views', label: 'Most viewed' },
];

// ---- Priority weighting (for sort) -----------------------------------------
export const PRIORITY_WEIGHT: Record<CommunicationPriority, number> = {
  critical: 5,
  urgent: 4,
  high: 3,
  normal: 2,
  low: 1,
};

// ---- Presentation intent maps (map to design tokens, not raw colors) -------
export type ToneIntent = 'neutral' | 'info' | 'success' | 'warning' | 'danger';

export const PRIORITY_TONE: Record<CommunicationPriority, ToneIntent> = {
  low: 'neutral',
  normal: 'info',
  high: 'warning',
  urgent: 'warning',
  critical: 'danger',
};

export const STATUS_TONE: Record<CommunicationStatus, ToneIntent> = {
  draft: 'neutral',
  scheduled: 'info',
  published: 'success',
  archived: 'neutral',
  expired: 'danger',
};

export const CATEGORY_ICON: Record<CommunicationCategory, string> = {
  general: 'info',
  academic: 'book-open',
  enrollment: 'user-check',
  batch: 'calendar',
  certificate: 'star',
  system: 'settings',
  emergency: 'alert-triangle',
  marketing: 'zap',
  event: 'calendar',
};

export const CHANNEL_ICON: Record<DeliveryChannel, string> = {
  'in-app': 'bell',
  email: 'mail',
  whatsapp: 'message-circle',
  sms: 'message-square',
  push: 'smartphone',
};

// ---- Page size options -----------------------------------------------------
export const PAGE_SIZE_OPTIONS: number[] = [10, 20, 50];
export const DEFAULT_PAGE_SIZE = 10;
