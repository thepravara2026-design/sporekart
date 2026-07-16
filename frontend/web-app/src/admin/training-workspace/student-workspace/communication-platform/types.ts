export type CommunicationType =
  | 'announcement'
  | 'general-notification'
  | 'academic-notification'
  | 'assignment-reminder'
  | 'assessment-reminder'
  | 'attendance-alert'
  | 'certificate-issued'
  | 'enrollment-confirmation'
  | 'course-completion'
  | 'batch-notification'
  | 'holiday-notice'
  | 'schedule-update'
  | 'trainer-announcement'
  | 'learning-reminder'
  | 'achievement-notification'
  | 'system-notification'
  | 'maintenance-notification'
  | 'ai-recommendation';

export type MessageStatus =
  | 'draft'
  | 'scheduled'
  | 'queued'
  | 'sent'
  | 'delivered'
  | 'read'
  | 'archived'
  | 'expired'
  | 'cancelled'
  | 'failed';

export type Priority = 'critical' | 'high' | 'medium' | 'low' | 'informational';

export type AnnouncementCategory =
  | 'organization'
  | 'course'
  | 'batch'
  | 'trainer'
  | 'academic-alert'
  | 'holiday'
  | 'system-maintenance'
  | 'emergency'
  | 'government';

export type NotificationCategory =
  | 'unread'
  | 'read'
  | 'archived'
  | 'history'
  | 'analytics';

export type InboxCategory = 'inbox' | 'important' | 'starred' | 'unread' | 'read' | 'archived' | 'pinned';

export type ChannelType = 'email' | 'sms' | 'whatsapp' | 'push' | 'in-app';

export type TimelineStage =
  | 'enrollment'
  | 'course-assigned'
  | 'assignment-reminder'
  | 'attendance-alert'
  | 'assessment-reminder'
  | 'achievement'
  | 'certificate-issued'
  | 'completion'
  | 'alumni';

export interface CommunicationMessage {
  id: string;
  notificationId: string;
  type: CommunicationType;
  priority: Priority;
  category: string;
  audience: string;
  title: string;
  subtitle: string;
  description: string;
  body: string;
  createdDate: string;
  scheduledDate: string | null;
  deliveryStatus: MessageStatus;
  readStatus: MessageStatus;
  expiryDate: string | null;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  studentId: string;
  studentName: string;
  sender: string;
  studentPhoto: string;
}

export interface Announcement {
  id: string;
  announcementId: string;
  type: CommunicationType;
  category: AnnouncementCategory;
  title: string;
  subtitle: string;
  body: string;
  priority: Priority;
  audience: string;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  createdDate: string;
  scheduledDate: string | null;
  status: MessageStatus;
  createdBy: string;
  attachments: number;
  views: number;
  expiresAt: string | null;
}

export interface InboxItem {
  id: string;
  messageId: string;
  studentId: string;
  studentName: string;
  studentPhoto: string;
  type: CommunicationType;
  title: string;
  subtitle: string;
  body: string;
  priority: Priority;
  isRead: boolean;
  isStarred: boolean;
  isImportant: boolean;
  isPinned: boolean;
  receivedDate: string;
  readDate: string | null;
  category: string;
  sender: string;
  courseId: string;
  courseName: string;
}

export interface TimelineEvent {
  id: string;
  studentId: string;
  studentName: string;
  stage: TimelineStage;
  title: string;
  description: string;
  date: string;
  type: CommunicationType;
  status: string;
  icon: string;
}

export interface CommunicationPreference {
  id: string;
  studentId: string;
  studentName: string;
  email: boolean;
  sms: boolean;
  whatsapp: boolean;
  push: boolean;
  inApp: boolean;
  announcements: boolean;
  reminders: boolean;
  marketing: boolean;
  academic: boolean;
  quietHoursStart: string | null;
  quietHoursEnd: string | null;
  createdDate: string;
  lastUpdated: string;
}

export interface NotificationTemplate {
  id: string;
  templateId: string;
  name: string;
  type: CommunicationType;
  subject: string;
  body: string;
  variables: string[];
  isActive: boolean;
  createdDate: string;
  lastUpdated: string;
}

export interface CommunicationAnalytics {
  totalSent: number;
  totalDelivered: number;
  totalRead: number;
  totalFailed: number;
  readRate: number;
  unreadRate: number;
  distributionByType: { type: string; count: number }[];
  distributionByPriority: { priority: string; count: number }[];
  courseNotifications: { courseName: string; count: number }[];
  batchNotifications: { batchName: string; count: number }[];
  announcementTrends: { month: string; count: number }[];
  notificationTrends: { month: string; count: number }[];
  engagementRate: number;
  openRate: number;
}

export interface EngagementDashboard {
  notificationsSent: number;
  announcementsCount: number;
  unreadMessages: number;
  readPercentage: number;
  studentEngagement: number;
  openRate: number;
  courseNotifications: number;
  batchNotifications: number;
  systemAlerts: number;
  activeTemplates: number;
  recentMessages: CommunicationMessage[];
  recentAnnouncements: Announcement[];
  priorityDistribution: { priority: string; count: number }[];
}

export interface CommunicationState {
  messages: CommunicationMessage[];
  announcements: Announcement[];
  inbox: InboxItem[];
  timeline: TimelineEvent[];
  preferences: CommunicationPreference[];
  templates: NotificationTemplate[];
  analytics: CommunicationAnalytics;
  dashboard: EngagementDashboard;
}

export const COMMUNICATION_TYPE_LABELS: Record<CommunicationType, string> = {
  'announcement': 'Announcement',
  'general-notification': 'General Notification',
  'academic-notification': 'Academic Notification',
  'assignment-reminder': 'Assignment Reminder',
  'assessment-reminder': 'Assessment Reminder',
  'attendance-alert': 'Attendance Alert',
  'certificate-issued': 'Certificate Issued',
  'enrollment-confirmation': 'Enrollment Confirmation',
  'course-completion': 'Course Completion',
  'batch-notification': 'Batch Notification',
  'holiday-notice': 'Holiday Notice',
  'schedule-update': 'Schedule Update',
  'trainer-announcement': 'Trainer Announcement',
  'learning-reminder': 'Learning Reminder',
  'achievement-notification': 'Achievement Notification',
  'system-notification': 'System Notification',
  'maintenance-notification': 'Maintenance Notification',
  'ai-recommendation': 'AI Recommendation',
};

export const MESSAGE_STATUS_LABELS: Record<MessageStatus, string> = {
  'draft': 'Draft',
  'scheduled': 'Scheduled',
  'queued': 'Queued',
  'sent': 'Sent',
  'delivered': 'Delivered',
  'read': 'Read',
  'archived': 'Archived',
  'expired': 'Expired',
  'cancelled': 'Cancelled',
  'failed': 'Failed',
};

export const PRIORITY_LABELS: Record<Priority, string> = {
  'critical': 'Critical',
  'high': 'High',
  'medium': 'Medium',
  'low': 'Low',
  'informational': 'Informational',
};

export const PRIORITY_VARIANTS: Record<Priority, 'danger' | 'warning' | 'info' | 'default' | 'success'> = {
  'critical': 'danger',
  'high': 'warning',
  'medium': 'info',
  'low': 'default',
  'informational': 'success',
};

export const ANNOUNCEMENT_CATEGORY_LABELS: Record<AnnouncementCategory, string> = {
  'organization': 'Organization',
  'course': 'Course',
  'batch': 'Batch',
  'trainer': 'Trainer',
  'academic-alert': 'Academic Alert',
  'holiday': 'Holiday',
  'system-maintenance': 'System Maintenance',
  'emergency': 'Emergency',
  'government': 'Government',
};

export const TIMELINE_STAGE_LABELS: Record<TimelineStage, string> = {
  'enrollment': 'Enrollment',
  'course-assigned': 'Course Assigned',
  'assignment-reminder': 'Assignment Reminder',
  'attendance-alert': 'Attendance Alert',
  'assessment-reminder': 'Assessment Reminder',
  'achievement': 'Achievement',
  'certificate-issued': 'Certificate Issued',
  'completion': 'Completion',
  'alumni': 'Alumni',
};

export const COMMUNICATION_NAV_ITEMS = [
  { id: 'communication', label: 'Engagement Dashboard', icon: 'layout', description: 'Communication overview & KPIs' },
  { id: 'communication/notifications', label: 'Notification Center', icon: 'bell', description: 'All platform notifications' },
  { id: 'communication/announcements', label: 'Announcement Center', icon: 'megaphone', description: 'Organization & course announcements' },
  { id: 'communication/inbox', label: 'Student Inbox', icon: 'mail', description: 'Student message inbox' },
  { id: 'communication/timeline', label: 'Communication Timeline', icon: 'clock', description: 'Student communication lifecycle' },
  { id: 'communication/templates', label: 'Notification Templates', icon: 'file-text', description: 'Reusable notification templates' },
  { id: 'communication/analytics', label: 'Communication Analytics', icon: 'bar-chart', description: 'Communication metrics & trends' },
  { id: 'communication/preferences', label: 'Preferences', icon: 'settings', description: 'Communication preferences' },
];

export const EMPTY_STATE_TYPES = [
  'noNotifications', 'noAnnouncements', 'noMessages', 'noInbox',
  'noCommunicationHistory', 'noSearchResults',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];
