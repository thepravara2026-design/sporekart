// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Mock Data (single source of truth)
// Sprint 26 · Part 10. Deterministic, seeded generators. No backend/API.
// All values are illustrative placeholders (…Placeholder fields) — never real.
// ---------------------------------------------------------------------------

import type {
  Announcement,
  AttachmentPlaceholder,
  AudienceTarget,
  CommunicationCategory,
  CommunicationPriority,
  CommunicationStats,
  CommunicationStatus,
  DeliveryChannel,
  DeliveryQueueItem,
  IntegrationProvider,
  MessageTemplate,
  NotificationItem,
  NotificationType,
  ScheduledMessage,
  TemplateKind,
  TimelineEvent,
  TimelineEventType,
} from './communicationTypes';

// ---- Deterministic PRNG (mulberry32) ---------------------------------------
function createRng(seed: number): () => number {
  let a = seed >>> 0;
  return function next(): number {
    a |= 0;
    a = (a + 0x6d2b79f5) | 0;
    let t = Math.imul(a ^ (a >>> 15), 1 | a);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

function pick<T>(rng: () => number, arr: readonly T[]): T {
  return arr[Math.floor(rng() * arr.length)];
}

function intBetween(rng: () => number, min: number, max: number): number {
  return Math.floor(rng() * (max - min + 1)) + min;
}

// Fixed base date for deterministic timelines (mock world "now").
const BASE_NOW = new Date('2026-07-16T09:00:00.000Z').getTime();

function isoOffsetDays(days: number, hoursJitter = 0): string {
  return new Date(BASE_NOW + days * 86400000 + hoursJitter * 3600000).toISOString();
}

// ---- Static vocab used by generators ---------------------------------------
const AUTHORS = [
  'Priya Menon',
  'Rahul Desai',
  'Aisha Khan',
  'Vikram Rao',
  'Sneha Iyer',
  'Admin Team',
];

const CATEGORIES: CommunicationCategory[] = [
  'general', 'academic', 'enrollment', 'batch', 'certificate', 'system', 'emergency', 'marketing', 'event',
];

const PRIORITIES: CommunicationPriority[] = ['low', 'normal', 'high', 'urgent', 'critical'];

const STATUSES: CommunicationStatus[] = ['draft', 'scheduled', 'published', 'archived', 'expired'];

const TITLE_STEMS = [
  'New Mushroom Cultivation Batch Opens',
  'Certificate Distribution Schedule Update',
  'Platform Maintenance Window Notice',
  'Enrollment Deadline Reminder',
  'Trainer Assignment Announcement',
  'Advanced Spawn Production Workshop',
  'Holiday Schedule for Training Centers',
  'Updated Assessment Guidelines',
  'Community Mentorship Program Launch',
  'Emergency Weather Advisory',
  'Course Curriculum Revision Notice',
  'Monthly Progress Review Meeting',
];

const BODY_STEMS = [
  'We are pleased to share important updates regarding upcoming training activities. Please review the details below and reach out to your program coordinator with any questions.',
  'This communication contains time-sensitive information. Kindly ensure all enrolled participants are informed and prepared accordingly.',
  'As part of our continuous improvement initiative, the following changes will take effect. We appreciate your cooperation and understanding.',
  'The training operations team has scheduled the following activity. Attendance and preparation guidelines are outlined for your reference.',
];

// ---- Audience helpers ------------------------------------------------------
const COURSE_REFS: AudienceTarget[] = [
  { scope: 'course', refId: 'crs-oyster-101', refLabel: 'Oyster Mushroom Fundamentals' },
  { scope: 'course', refId: 'crs-button-201', refLabel: 'Button Mushroom Commercial Scale' },
  { scope: 'course', refId: 'crs-spawn-301', refLabel: 'Spawn Lab Production' },
];

const BATCH_REFS: AudienceTarget[] = [
  { scope: 'batch', refId: 'batch-2026-a', refLabel: 'Batch 2026-A' },
  { scope: 'batch', refId: 'batch-2026-b', refLabel: 'Batch 2026-B' },
];

const CATEGORY_REFS: AudienceTarget[] = [
  { scope: 'category', refId: 'cat-cultivation', refLabel: 'Cultivation Techniques' },
  { scope: 'category', refId: 'cat-business', refLabel: 'Agri-Business' },
];

const BROAD_AUDIENCES: AudienceTarget[] = [
  { scope: 'everyone' },
  { scope: 'students' },
  { scope: 'trainers' },
  { scope: 'managers' },
];

function buildAudience(rng: () => number): AudienceTarget[] {
  const roll = rng();
  if (roll < 0.4) return [pick(rng, BROAD_AUDIENCES)];
  if (roll < 0.6) return [pick(rng, COURSE_REFS)];
  if (roll < 0.8) return [pick(rng, BATCH_REFS)];
  return [pick(rng, CATEGORY_REFS)];
}

function buildChannels(rng: () => number): DeliveryChannel[] {
  const channels: DeliveryChannel[] = ['in-app'];
  if (rng() > 0.6) channels.push('email');
  if (rng() > 0.85) channels.push('push');
  return channels;
}

// ---- Timeline generator ----------------------------------------------------
function buildTimeline(rng: () => number, status: CommunicationStatus, createdDay: number): TimelineEvent[] {
  const events: TimelineEvent[] = [];
  let seq = 0;
  const push = (type: TimelineEventType, label: string, dayOffset: number, detail?: string, placeholder = false) => {
    events.push({
      id: `tl-${createdDay}-${seq++}`,
      type,
      label,
      actor: pick(rng, AUTHORS),
      timestamp: isoOffsetDays(createdDay + dayOffset, intBetween(rng, 0, 8)),
      detail,
      placeholder,
    });
  };
  push('created', 'Draft created', 0);
  if (status === 'scheduled' || status === 'published' || status === 'archived' || status === 'expired') {
    push('scheduled', 'Scheduled for delivery', 0.2, 'Delivery time set (mock)');
  }
  if (status === 'published' || status === 'archived' || status === 'expired') {
    push('published', 'Published to audience', 0.5);
    push('delivered', 'In-app delivery completed', 0.6, `${intBetween(rng, 40, 320)} recipients (placeholder)`, true);
    if (rng() > 0.7) push('failed', 'Email delivery skipped', 0.65, 'Provider not configured (mock)', true);
    push('viewed', 'Views recorded', 0.9, `${intBetween(rng, 10, 200)} views (placeholder)`, true);
  }
  if (status === 'archived') push('archived', 'Archived', 3);
  return events;
}

function buildAttachments(rng: () => number): AttachmentPlaceholder[] {
  if (rng() > 0.55) return [];
  const kinds: AttachmentPlaceholder['kind'][] = ['document', 'image', 'video', 'link'];
  const count = intBetween(rng, 1, 2);
  const items: AttachmentPlaceholder[] = [];
  for (let i = 0; i < count; i += 1) {
    const kind = pick(rng, kinds);
    items.push({
      id: `att-${Math.floor(rng() * 1e6)}`,
      name: `${kind}-resource-${i + 1}`,
      kind,
      sizeLabel: kind === 'link' ? '—' : `${intBetween(rng, 120, 4800)} KB`,
    });
  }
  return items;
}

// ---- Announcements ---------------------------------------------------------
function generateAnnouncements(count: number): Announcement[] {
  const rng = createRng(20260710);
  const items: Announcement[] = [];
  for (let i = 0; i < count; i += 1) {
    const status = i < 3 ? 'published' : pick(rng, STATUSES);
    const category = pick(rng, CATEGORIES);
    const priority = category === 'emergency' ? 'critical' : pick(rng, PRIORITIES);
    const createdDay = -intBetween(rng, 1, 90);
    const title = `${pick(rng, TITLE_STEMS)}`;
    const publishedAt = status === 'published' || status === 'archived' || status === 'expired'
      ? isoOffsetDays(createdDay + 0.5)
      : undefined;
    const scheduledFor = status === 'scheduled'
      ? isoOffsetDays(intBetween(rng, 1, 21))
      : undefined;
    items.push({
      id: `ann-${String(i + 1).padStart(4, '0')}`,
      title,
      summary: `${title} — key details and action items for the training community.`,
      body: `<p>${pick(rng, BODY_STEMS)}</p><p>Please contact the training operations desk for clarifications.</p>`,
      category,
      priority,
      status,
      visibility: pick(rng, ['public', 'internal', 'private'] as const),
      audience: buildAudience(rng),
      channels: buildChannels(rng),
      author: pick(rng, AUTHORS),
      createdAt: isoOffsetDays(createdDay),
      updatedAt: isoOffsetDays(createdDay + 0.3),
      publishedAt,
      scheduledFor,
      expiresAt: status === 'expired' ? isoOffsetDays(-intBetween(rng, 1, 10)) : undefined,
      pinned: rng() > 0.85,
      featured: rng() > 0.8,
      viewsPlaceholder: publishedAt ? intBetween(rng, 25, 1200) : 0,
      reachPlaceholder: publishedAt ? intBetween(rng, 50, 2400) : 0,
      attachments: buildAttachments(rng),
      timeline: buildTimeline(rng, status, createdDay),
    });
  }
  return items;
}

// ---- Notifications ---------------------------------------------------------
const NOTIFICATION_TYPE_POOL: NotificationType[] = [
  'enrollment-approved', 'enrollment-pending', 'course-published', 'batch-scheduled',
  'trainer-assigned', 'certificate-ready', 'assignment-reminder', 'assessment-reminder',
  'system-maintenance', 'general-update', 'emergency-notice', 'course-updated', 'batch-cancelled',
];

function generateNotifications(count: number): NotificationItem[] {
  const rng = createRng(20260711);
  const items: NotificationItem[] = [];
  for (let i = 0; i < count; i += 1) {
    const type = pick(rng, NOTIFICATION_TYPE_POOL);
    items.push({
      id: `ntf-${String(i + 1).padStart(4, '0')}`,
      type,
      title: type.split('-').map((w) => w[0].toUpperCase() + w.slice(1)).join(' '),
      message: `${pick(rng, BODY_STEMS).slice(0, 90)}…`,
      priority: type === 'emergency-notice' ? 'critical' : pick(rng, PRIORITIES),
      category: type === 'emergency-notice' ? 'emergency' : pick(rng, CATEGORIES),
      audience: buildAudience(rng),
      channels: buildChannels(rng),
      createdAt: isoOffsetDays(-intBetween(rng, 0, 30), intBetween(rng, 0, 23)),
      read: rng() > 0.55,
    });
  }
  return items.sort((a, b) => (a.createdAt < b.createdAt ? 1 : -1));
}

// ---- Scheduled Messages ----------------------------------------------------
function generateScheduled(count: number): ScheduledMessage[] {
  const rng = createRng(20260712);
  const kinds: TemplateKind[] = ['announcement', 'reminder', 'certificate', 'enrollment', 'batch', 'marketing'];
  const items: ScheduledMessage[] = [];
  for (let i = 0; i < count; i += 1) {
    items.push({
      id: `sch-${String(i + 1).padStart(4, '0')}`,
      title: `${pick(rng, TITLE_STEMS)}`,
      kind: pick(rng, kinds),
      audience: buildAudience(rng),
      channels: buildChannels(rng),
      scheduledFor: isoOffsetDays(intBetween(rng, 1, 30), intBetween(rng, 0, 23)),
      status: rng() > 0.2 ? 'scheduled' : 'paused',
      createdBy: pick(rng, AUTHORS),
    });
  }
  return items.sort((a, b) => (a.scheduledFor > b.scheduledFor ? 1 : -1));
}

// ---- Templates -------------------------------------------------------------
const TEMPLATE_SEED: Array<Pick<MessageTemplate, 'name' | 'kind' | 'subject' | 'body' | 'variables' | 'future'>> = [
  {
    name: 'Enrollment Approved',
    kind: 'enrollment',
    subject: 'Welcome to {{course_name}}, {{student_name}}!',
    body: '<p>Dear {{student_name}},</p><p>Your enrollment in <strong>{{course_name}}</strong> (Batch {{batch_code}}) has been approved. Your first session begins on {{start_date}}.</p>',
    variables: ['student_name', 'course_name', 'batch_code', 'start_date'],
    future: false,
  },
  {
    name: 'Assessment Reminder',
    kind: 'reminder',
    subject: 'Upcoming assessment for {{course_name}}',
    body: '<p>Hi {{student_name}},</p><p>This is a reminder that your assessment for {{course_name}} is due on {{due_date}}.</p>',
    variables: ['student_name', 'course_name', 'due_date'],
    future: false,
  },
  {
    name: 'Certificate Ready',
    kind: 'certificate',
    subject: 'Your certificate for {{course_name}} is ready',
    body: '<p>Congratulations {{student_name}}! Your certificate for {{course_name}} is now available for download.</p>',
    variables: ['student_name', 'course_name'],
    future: false,
  },
  {
    name: 'Batch Schedule Update',
    kind: 'batch',
    subject: 'Schedule change for Batch {{batch_code}}',
    body: '<p>The schedule for Batch {{batch_code}} has been updated. New timing: {{new_schedule}}.</p>',
    variables: ['batch_code', 'new_schedule'],
    future: false,
  },
  {
    name: 'General Announcement',
    kind: 'announcement',
    subject: '{{title}}',
    body: '<p>{{body}}</p>',
    variables: ['title', 'body'],
    future: false,
  },
  {
    name: 'System Maintenance Alert',
    kind: 'system-alert',
    subject: 'Scheduled maintenance on {{date}}',
    body: '<p>The platform will be unavailable on {{date}} from {{start_time}} to {{end_time}} for scheduled maintenance.</p>',
    variables: ['date', 'start_time', 'end_time'],
    future: false,
  },
  {
    name: 'Promotional Campaign',
    kind: 'marketing',
    subject: 'New courses now open — {{offer}}',
    body: '<p>Explore our latest cultivation programs. {{offer}}</p>',
    variables: ['offer'],
    future: false,
  },
  {
    name: 'Email Welcome (Future Channel)',
    kind: 'email',
    subject: '[Email] {{subject}}',
    body: '<p>Email channel template — activated once an email provider is integrated.</p>',
    variables: ['subject'],
    future: true,
  },
  {
    name: 'WhatsApp Reminder (Future Channel)',
    kind: 'whatsapp',
    subject: '—',
    body: 'WhatsApp channel template — activated once a WhatsApp Business provider is integrated.',
    variables: ['student_name', 'due_date'],
    future: true,
  },
  {
    name: 'SMS Alert (Future Channel)',
    kind: 'sms',
    subject: '—',
    body: 'SMS channel template — activated once an SMS gateway is integrated.',
    variables: ['message'],
    future: true,
  },
  {
    name: 'Push Notification (Future Channel)',
    kind: 'push',
    subject: '{{title}}',
    body: 'Push channel template — activated once a push provider is integrated.',
    variables: ['title', 'body'],
    future: true,
  },
];

function generateTemplates(): MessageTemplate[] {
  const rng = createRng(20260713);
  return TEMPLATE_SEED.map((seed, i) => {
    const channels: DeliveryChannel[] = seed.future
      ? [seed.kind as DeliveryChannel]
      : ['in-app', ...(rng() > 0.5 ? (['email'] as DeliveryChannel[]) : [])];
    return {
      id: `tpl-${String(i + 1).padStart(4, '0')}`,
      name: seed.name,
      kind: seed.kind,
      subject: seed.subject,
      body: seed.body,
      variables: seed.variables,
      channels,
      updatedAt: isoOffsetDays(-intBetween(rng, 1, 120)),
      usageCountPlaceholder: seed.future ? 0 : intBetween(rng, 3, 240),
      future: seed.future,
    };
  });
}

// ---- Delivery Queue --------------------------------------------------------
function generateDeliveryQueue(count: number): DeliveryQueueItem[] {
  const rng = createRng(20260714);
  const channels: DeliveryChannel[] = ['in-app', 'email', 'whatsapp', 'sms', 'push'];
  const states: DeliveryQueueItem['state'][] = ['queued', 'processing', 'delivered', 'failed', 'retry'];
  const items: DeliveryQueueItem[] = [];
  for (let i = 0; i < count; i += 1) {
    const channel = pick(rng, channels);
    const state = channel === 'in-app' ? pick(rng, ['queued', 'processing', 'delivered'] as const) : pick(rng, states);
    items.push({
      id: `dq-${String(i + 1).padStart(4, '0')}`,
      title: `${pick(rng, TITLE_STEMS)}`,
      channel,
      audienceLabel: pick(rng, ['Everyone', 'Students', 'Batch 2026-A', 'Trainers', 'Oyster Mushroom Fundamentals']),
      state,
      attempts: state === 'retry' || state === 'failed' ? intBetween(rng, 1, 3) : intBetween(rng, 0, 1),
      queuedAt: isoOffsetDays(-intBetween(rng, 0, 5), intBetween(rng, 0, 23)),
    });
  }
  return items;
}

// ---- Integration Providers (future roadmap; interfaces only) ---------------
export const INTEGRATION_PROVIDERS: IntegrationProvider[] = [
  { id: 'int-email', name: 'Email (SMTP / Provider)', category: 'email', status: 'planned', description: 'Transactional and bulk email delivery once an email provider is configured.' },
  { id: 'int-whatsapp', name: 'WhatsApp Business', category: 'whatsapp', status: 'planned', description: 'Rich messaging via a WhatsApp Business API provider.' },
  { id: 'int-sms', name: 'SMS Gateway', category: 'sms', status: 'planned', description: 'Short text alerts through an SMS aggregator.' },
  { id: 'int-push', name: 'Push Notifications', category: 'push', status: 'planned', description: 'Web and mobile push via a push service.' },
  { id: 'int-calendar', name: 'Calendar Sync', category: 'calendar', status: 'planned', description: 'Publish batch and event schedules to external calendars.' },
  { id: 'int-crm', name: 'CRM Sync', category: 'crm', status: 'planned', description: 'Sync audiences and engagement with a CRM system.' },
];

// ---- Frozen datasets (single source) ---------------------------------------
export const MOCK_ANNOUNCEMENTS: readonly Announcement[] = Object.freeze(generateAnnouncements(48));
export const MOCK_NOTIFICATIONS: readonly NotificationItem[] = Object.freeze(generateNotifications(64));
export const MOCK_SCHEDULED: readonly ScheduledMessage[] = Object.freeze(generateScheduled(18));
export const MOCK_TEMPLATES: readonly MessageTemplate[] = Object.freeze(generateTemplates());
export const MOCK_DELIVERY_QUEUE: readonly DeliveryQueueItem[] = Object.freeze(generateDeliveryQueue(30));

// ---- Derived statistics ----------------------------------------------------
export function computeStats(): CommunicationStats {
  const byStatus = (s: CommunicationStatus) => MOCK_ANNOUNCEMENTS.filter((a) => a.status === s).length;
  return {
    totalAnnouncements: MOCK_ANNOUNCEMENTS.length,
    published: byStatus('published'),
    draft: byStatus('draft'),
    archived: byStatus('archived'),
    scheduled: byStatus('scheduled'),
    pending: MOCK_SCHEDULED.filter((s) => s.status === 'scheduled').length,
    expired: byStatus('expired'),
    totalNotifications: MOCK_NOTIFICATIONS.length,
    templates: MOCK_TEMPLATES.filter((t) => !t.future).length,
    audienceReachPlaceholder: MOCK_ANNOUNCEMENTS.reduce((sum, a) => sum + a.reachPlaceholder, 0),
    deliveredPlaceholder: MOCK_DELIVERY_QUEUE.filter((d) => d.state === 'delivered').length,
    failedPlaceholder: MOCK_DELIVERY_QUEUE.filter((d) => d.state === 'failed').length,
  };
}

export const MOCK_STATS: CommunicationStats = computeStats();
