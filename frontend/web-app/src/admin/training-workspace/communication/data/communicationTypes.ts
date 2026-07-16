// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Domain Types & Taxonomy
// Sprint 26 · Part 10. Mock Mode only: no backend, API, or provider integration.
// Provider-agnostic contracts designed for future multi-channel messaging.
// ---------------------------------------------------------------------------

// ---- Status ----------------------------------------------------------------
export type CommunicationStatus =
  | 'draft'
  | 'scheduled'
  | 'published'
  | 'archived'
  | 'expired';

export const COMMUNICATION_STATUS_LABELS: Record<CommunicationStatus, string> = {
  draft: 'Draft',
  scheduled: 'Scheduled',
  published: 'Published',
  archived: 'Archived',
  expired: 'Expired',
};

// ---- Priority --------------------------------------------------------------
export type CommunicationPriority = 'low' | 'normal' | 'high' | 'urgent' | 'critical';

export const PRIORITY_LABELS: Record<CommunicationPriority, string> = {
  low: 'Low',
  normal: 'Normal',
  high: 'High',
  urgent: 'Urgent',
  critical: 'Critical',
};

// ---- Visibility ------------------------------------------------------------
export type CommunicationVisibility = 'public' | 'internal' | 'private';

export const VISIBILITY_LABELS: Record<CommunicationVisibility, string> = {
  public: 'Public',
  internal: 'Internal',
  private: 'Private',
};

// ---- Categories ------------------------------------------------------------
export type CommunicationCategory =
  | 'general'
  | 'academic'
  | 'enrollment'
  | 'batch'
  | 'certificate'
  | 'system'
  | 'emergency'
  | 'marketing'
  | 'event';

export const CATEGORY_LABELS: Record<CommunicationCategory, string> = {
  general: 'General',
  academic: 'Academic',
  enrollment: 'Enrollment',
  batch: 'Batch',
  certificate: 'Certificate',
  system: 'System',
  emergency: 'Emergency',
  marketing: 'Marketing',
  event: 'Event',
};

// ---- Audience Targeting ----------------------------------------------------
export type AudienceScope =
  | 'everyone'
  | 'students'
  | 'trainers'
  | 'admins'
  | 'managers'
  | 'batch'
  | 'course'
  | 'category'
  // Future targeting scopes (architecture only)
  | 'role'
  | 'geography'
  | 'organization';

export const AUDIENCE_LABELS: Record<AudienceScope, string> = {
  everyone: 'Everyone',
  students: 'Students',
  trainers: 'Trainers',
  admins: 'Admins',
  managers: 'Managers',
  batch: 'Specific Batch',
  course: 'Specific Course',
  category: 'Specific Category',
  role: 'Role-based (future)',
  geography: 'Geography (future)',
  organization: 'Organization (future)',
};

export const FUTURE_AUDIENCE_SCOPES: AudienceScope[] = ['role', 'geography', 'organization'];

export interface AudienceTarget {
  scope: AudienceScope;
  refId?: string;
  refLabel?: string;
}

// ---- Notification Taxonomy -------------------------------------------------
export type NotificationType =
  | 'enrollment-approved'
  | 'enrollment-pending'
  | 'enrollment-rejected'
  | 'course-published'
  | 'course-updated'
  | 'batch-scheduled'
  | 'batch-cancelled'
  | 'trainer-assigned'
  | 'certificate-ready'
  | 'assignment-reminder'
  | 'assessment-reminder'
  | 'system-maintenance'
  | 'general-update'
  | 'emergency-notice';

export interface NotificationTypeMeta {
  type: NotificationType;
  label: string;
  category: CommunicationCategory;
  defaultPriority: CommunicationPriority;
  icon: string;
}

export const NOTIFICATION_TYPES: NotificationTypeMeta[] = [
  { type: 'enrollment-approved', label: 'Enrollment Approved', category: 'enrollment', defaultPriority: 'normal', icon: 'check-circle' },
  { type: 'enrollment-pending', label: 'Enrollment Pending', category: 'enrollment', defaultPriority: 'normal', icon: 'clock' },
  { type: 'enrollment-rejected', label: 'Enrollment Rejected', category: 'enrollment', defaultPriority: 'high', icon: 'x-circle' },
  { type: 'course-published', label: 'Course Published', category: 'academic', defaultPriority: 'normal', icon: 'book-open' },
  { type: 'course-updated', label: 'Course Updated', category: 'academic', defaultPriority: 'low', icon: 'refresh-cw' },
  { type: 'batch-scheduled', label: 'Batch Scheduled', category: 'batch', defaultPriority: 'normal', icon: 'calendar' },
  { type: 'batch-cancelled', label: 'Batch Cancelled', category: 'batch', defaultPriority: 'high', icon: 'x-circle' },
  { type: 'trainer-assigned', label: 'Trainer Assigned', category: 'batch', defaultPriority: 'normal', icon: 'user-check' },
  { type: 'certificate-ready', label: 'Certificate Ready', category: 'certificate', defaultPriority: 'normal', icon: 'star' },
  { type: 'assignment-reminder', label: 'Assignment Reminder', category: 'academic', defaultPriority: 'normal', icon: 'file' },
  { type: 'assessment-reminder', label: 'Assessment Reminder', category: 'academic', defaultPriority: 'high', icon: 'target' },
  { type: 'system-maintenance', label: 'System Maintenance', category: 'system', defaultPriority: 'high', icon: 'settings' },
  { type: 'general-update', label: 'General Update', category: 'general', defaultPriority: 'low', icon: 'info' },
  { type: 'emergency-notice', label: 'Emergency Notice', category: 'emergency', defaultPriority: 'critical', icon: 'alert-triangle' },
];

// ---- Delivery Channels (provider-agnostic; future integration) -------------
export type DeliveryChannel = 'in-app' | 'email' | 'whatsapp' | 'sms' | 'push';

export const CHANNEL_LABELS: Record<DeliveryChannel, string> = {
  'in-app': 'In-App',
  email: 'Email',
  whatsapp: 'WhatsApp',
  sms: 'SMS',
  push: 'Push',
};

export const FUTURE_CHANNELS: DeliveryChannel[] = ['email', 'whatsapp', 'sms', 'push'];

// ---- Template Kinds --------------------------------------------------------
export type TemplateKind =
  | 'announcement'
  | 'reminder'
  | 'certificate'
  | 'enrollment'
  | 'batch'
  | 'marketing'
  | 'system-alert'
  // Channel-specific placeholders (future)
  | 'email'
  | 'whatsapp'
  | 'sms'
  | 'push';

export const TEMPLATE_KIND_LABELS: Record<TemplateKind, string> = {
  announcement: 'Announcement',
  reminder: 'Reminder',
  certificate: 'Certificate',
  enrollment: 'Enrollment',
  batch: 'Batch',
  marketing: 'Marketing',
  'system-alert': 'System Alert',
  email: 'Email (future)',
  whatsapp: 'WhatsApp (future)',
  sms: 'SMS (future)',
  push: 'Push (future)',
};

export const FUTURE_TEMPLATE_KINDS: TemplateKind[] = ['email', 'whatsapp', 'sms', 'push'];

// ---- Timeline / Activity ---------------------------------------------------
export type TimelineEventType =
  | 'created'
  | 'scheduled'
  | 'published'
  | 'archived'
  | 'viewed'
  | 'delivered'
  | 'failed'
  | 'retry';

export const TIMELINE_EVENT_LABELS: Record<TimelineEventType, string> = {
  created: 'Created',
  scheduled: 'Scheduled',
  published: 'Published',
  archived: 'Archived',
  viewed: 'Viewed',
  delivered: 'Delivered',
  failed: 'Failed',
  retry: 'Retry Queued',
};

export interface TimelineEvent {
  id: string;
  type: TimelineEventType;
  label: string;
  actor: string;
  timestamp: string;
  detail?: string;
  placeholder?: boolean;
}

// ---- Attachment (placeholder metadata; no uploads) -------------------------
export interface AttachmentPlaceholder {
  id: string;
  name: string;
  kind: 'document' | 'image' | 'video' | 'link';
  sizeLabel: string;
}

// ---- Announcement ----------------------------------------------------------
export interface Announcement {
  id: string;
  title: string;
  summary: string;
  body: string;
  category: CommunicationCategory;
  priority: CommunicationPriority;
  status: CommunicationStatus;
  visibility: CommunicationVisibility;
  audience: AudienceTarget[];
  channels: DeliveryChannel[];
  author: string;
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
  scheduledFor?: string;
  expiresAt?: string;
  pinned: boolean;
  featured: boolean;
  viewsPlaceholder: number;
  reachPlaceholder: number;
  attachments: AttachmentPlaceholder[];
  timeline: TimelineEvent[];
}

// ---- Notification ----------------------------------------------------------
export interface NotificationItem {
  id: string;
  type: NotificationType;
  title: string;
  message: string;
  priority: CommunicationPriority;
  category: CommunicationCategory;
  audience: AudienceTarget[];
  channels: DeliveryChannel[];
  createdAt: string;
  read: boolean;
}

// ---- Scheduled Message -----------------------------------------------------
export interface ScheduledMessage {
  id: string;
  title: string;
  kind: TemplateKind;
  audience: AudienceTarget[];
  channels: DeliveryChannel[];
  scheduledFor: string;
  status: 'scheduled' | 'paused';
  createdBy: string;
}

// ---- Template --------------------------------------------------------------
export interface MessageTemplate {
  id: string;
  name: string;
  kind: TemplateKind;
  subject: string;
  body: string;
  variables: string[];
  channels: DeliveryChannel[];
  updatedAt: string;
  usageCountPlaceholder: number;
  future: boolean;
}

// ---- Delivery Queue (placeholder) ------------------------------------------
export interface DeliveryQueueItem {
  id: string;
  title: string;
  channel: DeliveryChannel;
  audienceLabel: string;
  state: 'queued' | 'processing' | 'delivered' | 'failed' | 'retry';
  attempts: number;
  queuedAt: string;
}

// ---- Statistics ------------------------------------------------------------
export interface CommunicationStats {
  totalAnnouncements: number;
  published: number;
  draft: number;
  archived: number;
  scheduled: number;
  pending: number;
  expired: number;
  totalNotifications: number;
  templates: number;
  audienceReachPlaceholder: number;
  deliveredPlaceholder: number;
  failedPlaceholder: number;
}

// ---- Future Integration Providers (interfaces only) ------------------------
export type IntegrationCategory = 'email' | 'whatsapp' | 'sms' | 'push' | 'calendar' | 'crm' | 'erp' | 'commerce';

export interface IntegrationProvider {
  id: string;
  name: string;
  category: IntegrationCategory;
  status: 'planned';
  description: string;
}
