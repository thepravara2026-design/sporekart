import type { CommunicationMessage, Announcement, InboxItem, TimelineEvent, CommunicationPreference, NotificationTemplate, CommunicationAnalytics, EngagementDashboard, CommunicationType, Priority, MessageStatus, AnnouncementCategory, TimelineStage } from '../types';

const STU = [
  { id: 'stu-1', name: 'Aarav Sharma' }, { id: 'stu-2', name: 'Priya Patel' }, { id: 'stu-3', name: 'Rahul Singh' },
  { id: 'stu-4', name: 'Ananya Gupta' }, { id: 'stu-5', name: 'Vikram Joshi' }, { id: 'stu-6', name: 'Neha Kapoor' },
  { id: 'stu-7', name: 'Arjun Mehta' }, { id: 'stu-8', name: 'Kavita Reddy' }, { id: 'stu-9', name: 'Rohan Desai' },
  { id: 'stu-10', name: 'Ishita Verma' }, { id: 'stu-11', name: 'Amit Kumar' }, { id: 'stu-12', name: 'Sneha Agarwal' },
  { id: 'stu-13', name: 'Deepak Tiwari' }, { id: 'stu-14', name: 'Pooja Nair' }, { id: 'stu-15', name: 'Karan Malhotra' },
  { id: 'stu-16', name: 'Divya Bhat' }, { id: 'stu-17', name: 'Suresh Iyer' }, { id: 'stu-18', name: 'Meera Choudhury' },
  { id: 'stu-19', name: 'Nitin Saxena' }, { id: 'stu-20', name: 'Lakshmi Rajan' },
  { id: 'stu-21', name: 'Rajesh Kumar' }, { id: 'stu-22', name: 'Anjali Sinha' }, { id: 'stu-23', name: 'Vivek Mishra' },
  { id: 'stu-24', name: 'Pallavi Rao' }, { id: 'stu-25', name: 'Aditya Khanna' },
];

const COURSES = [
  { id: 'course-1', name: 'Full Stack Web Development' }, { id: 'course-2', name: 'Data Science & Analytics' },
  { id: 'course-3', name: 'Cloud Architecture' }, { id: 'course-4', name: 'Mobile App Development' },
  { id: 'course-5', name: 'DevOps Engineering' },
];

const BATCHES = [
  { id: 'batch-1', name: 'Batch A' }, { id: 'batch-2', name: 'Batch B' },
  { id: 'batch-3', name: 'Batch C' }, { id: 'batch-4', name: 'Batch D' },
];

const TYPES: CommunicationType[] = ['announcement', 'general-notification', 'academic-notification', 'assignment-reminder', 'assessment-reminder', 'attendance-alert', 'certificate-issued', 'enrollment-confirmation', 'course-completion', 'batch-notification', 'holiday-notice', 'schedule-update', 'trainer-announcement', 'learning-reminder', 'achievement-notification', 'system-notification', 'maintenance-notification', 'ai-recommendation'];

const PRIORITIES: Priority[] = ['critical', 'high', 'medium', 'low', 'informational'];

const STATUSES: MessageStatus[] = ['draft', 'scheduled', 'queued', 'sent', 'delivered', 'read', 'archived', 'expired', 'cancelled', 'failed'];

const ANN_CATS: AnnouncementCategory[] = ['organization', 'course', 'batch', 'trainer', 'academic-alert', 'holiday', 'system-maintenance', 'emergency', 'government'];

const STAGES: TimelineStage[] = ['enrollment', 'course-assigned', 'assignment-reminder', 'attendance-alert', 'assessment-reminder', 'achievement', 'certificate-issued', 'completion', 'alumni'];

const PHOTOS = ['/photos/student-1.jpg', '/photos/student-2.jpg', '/photos/student-3.jpg', '/photos/student-4.jpg', '/photos/student-5.jpg'];
const SENDERS = ['Admin Office', 'Academic Office', 'Training Department', 'System', 'Trainer Office'];
const AUDIENCES = ['All Students', 'Course Students', 'Batch Students', 'Individual'];

function pick<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randomDate(daysBack: number): string {
  const d = new Date(); d.setDate(d.getDate() - Math.floor(Math.random() * daysBack));
  return d.toISOString().split('T')[0];
}

function titleForType(t: CommunicationType): string {
  const map: Record<string, string> = {
    'announcement': 'Important Announcement',
    'general-notification': 'General Notification',
    'academic-notification': 'Academic Notification',
    'assignment-reminder': 'Assignment Due Reminder',
    'assessment-reminder': 'Upcoming Assessment',
    'attendance-alert': 'Attendance Alert',
    'certificate-issued': 'Certificate Issued',
    'enrollment-confirmation': 'Enrollment Confirmed',
    'course-completion': 'Course Completed',
    'batch-notification': 'Batch Update',
    'holiday-notice': 'Holiday Notice',
    'schedule-update': 'Schedule Updated',
    'trainer-announcement': 'Trainer Announcement',
    'learning-reminder': 'Learning Reminder',
    'achievement-notification': 'Achievement Unlocked',
    'system-notification': 'System Notification',
    'maintenance-notification': 'System Maintenance',
    'ai-recommendation': 'AI Recommendation',
  };
  return map[t] || 'Notification';
}

function subtitleForType(t: CommunicationType): string {
  const map: Record<string, string> = {
    'announcement': 'Please read the latest announcement',
    'general-notification': 'You have a new notification',
    'academic-notification': 'Academic update for your courses',
    'assignment-reminder': 'Your assignment is due soon',
    'assessment-reminder': 'Assessment scheduled',
    'attendance-alert': 'Your attendance needs attention',
    'certificate-issued': 'Your certificate is ready',
    'enrollment-confirmation': 'You are now enrolled',
    'course-completion': 'Congratulations on completing the course',
    'batch-notification': 'Update for your batch',
    'holiday-notice': 'Upcoming holiday schedule',
    'schedule-update': 'Schedule has been modified',
    'trainer-announcement': 'Message from your trainer',
    'learning-reminder': 'Continue your learning journey',
    'achievement-notification': 'You earned a new achievement',
    'system-notification': 'System update',
    'maintenance-notification': 'Scheduled maintenance',
    'ai-recommendation': 'Personalized AI recommendation',
  };
  return map[t] || 'Notification message';
}

function generateMessages(count: number): CommunicationMessage[] {
  return Array.from({ length: count }, (_, i) => {
    const t = pick(TYPES);
    const stu = pick(STU);
    const course = pick(COURSES);
    const batch = pick(BATCHES);
    return {
      id: `msg-${i + 1}`, notificationId: `notif-${i + 1}`, type: t, priority: pick(PRIORITIES),
      category: pick(['academic', 'system', 'announcement', 'reminder', 'achievement']),
      audience: pick(AUDIENCES), title: titleForType(t), subtitle: subtitleForType(t),
      description: `This is a ${t.replace(/-/g, ' ')} message for ${stu.name}.`,
      body: `Dear ${stu.name},\n\nThis is a ${t.replace(/-/g, ' ')} regarding ${course.name} (${batch.name}).\n\nPlease take appropriate action.\n\nRegards,\n${pick(SENDERS)}`,
      createdDate: randomDate(90), scheduledDate: Math.random() > 0.8 ? randomDate(30) : null,
      deliveryStatus: pick(STATUSES), readStatus: pick(STATUSES), expiryDate: Math.random() > 0.7 ? randomDate(60) : null,
      courseId: course.id, courseName: course.name, batchId: batch.id, batchName: batch.name,
      studentId: stu.id, studentName: stu.name, sender: pick(SENDERS), studentPhoto: pick(PHOTOS),
    };
  });
}

function generateAnnouncements(count: number): Announcement[] {
  return Array.from({ length: count }, (_, i) => {
    const course = pick(COURSES); const batch = pick(BATCHES);
    const cat = pick(ANN_CATS);
    return {
      id: `ann-${i + 1}`, announcementId: `announce-${i + 1}`, type: 'announcement', category: cat,
      title: `${cat.charAt(0).toUpperCase() + cat.slice(1)} Announcement ${i + 1}`,
      subtitle: `Important ${cat} update`,
      body: `This is an important ${cat.replace(/-/g, ' ')} announcement.\n\nDetails: Please review the updated information for ${course.name} - ${batch.name}.\n\nAction required for all students.`,
      priority: pick(PRIORITIES), audience: pick(AUDIENCES),
      courseId: course.id, courseName: course.name, batchId: batch.id, batchName: batch.name,
      createdDate: randomDate(60), scheduledDate: Math.random() > 0.8 ? randomDate(15) : null,
      status: pick(STATUSES), createdBy: pick(SENDERS), attachments: Math.floor(Math.random() * 3),
      views: Math.floor(Math.random() * 500), expiresAt: Math.random() > 0.6 ? randomDate(90) : null,
    };
  });
}

function generateInbox(count: number): InboxItem[] {
  return Array.from({ length: count }, (_, i) => {
    const t = pick(TYPES); const stu = pick(STU); const course = pick(COURSES);
    return {
      id: `inbox-${i + 1}`, messageId: `msg-${Math.floor(Math.random() * 100) + 1}`,
      studentId: stu.id, studentName: stu.name, studentPhoto: pick(PHOTOS),
      type: t, title: titleForType(t), subtitle: subtitleForType(t),
      body: `Message body for ${stu.name} regarding ${course.name}.`,
      priority: pick(PRIORITIES), isRead: Math.random() > 0.5, isStarred: Math.random() > 0.8,
      isImportant: Math.random() > 0.85, isPinned: Math.random() > 0.9,
      receivedDate: randomDate(30), readDate: Math.random() > 0.5 ? randomDate(15) : null,
      category: pick(['academic', 'system', 'announcement', 'reminder']),
      sender: pick(SENDERS), courseId: course.id, courseName: course.name,
    };
  });
}

function generateTimeline(count: number): TimelineEvent[] {
  return Array.from({ length: count }, (_, i) => {
    const stu = pick(STU); const stage = STAGES[i % STAGES.length];
    return {
      id: `tl-${i + 1}`, studentId: stu.id, studentName: stu.name,
      stage, title: stage.replace(/-/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase()),
      description: `${stu.name} ${stage.replace(/-/g, ' ')} event`,
      date: randomDate(180), type: pick(TYPES), status: pick(STATUSES),
      icon: stage === 'enrollment' ? 'user-plus' : stage === 'completion' ? 'check-circle' : stage === 'achievement' ? 'star' : stage === 'certificate-issued' ? 'award' : stage === 'alumni' ? 'graduation-cap' : 'bell',
    };
  });
}

function generatePreferences(): CommunicationPreference[] {
  return STU.map((stu) => ({
    id: `pref-${stu.id}`, studentId: stu.id, studentName: stu.name,
    email: true, sms: Math.random() > 0.3, whatsapp: Math.random() > 0.5,
    push: true, inApp: true,
    announcements: true, reminders: true, marketing: Math.random() > 0.6,
    academic: true, quietHoursStart: Math.random() > 0.7 ? '22:00' : null,
    quietHoursEnd: Math.random() > 0.7 ? '07:00' : null,
    createdDate: randomDate(365), lastUpdated: randomDate(30),
  }));
}

function generateTemplates(): NotificationTemplate[] {
  return TYPES.map((t, i) => ({
    id: `tpl-${i + 1}`, templateId: `template-${i + 1}`, name: `${titleForType(t)} Template`,
    type: t, subject: `{{studentName}} - ${titleForType(t)}`,
    body: `Dear {{studentName}},\n\nThis is a ${t.replace(/-/g, ' ')} regarding {{courseName}}.\n\n{{messageBody}}\n\nRegards,\n{{sender}}`,
    variables: ['studentName', 'courseName', 'messageBody', 'sender'],
    isActive: Math.random() > 0.2, createdDate: randomDate(365), lastUpdated: randomDate(60),
  }));
}

function generateAnalytics(messages: CommunicationMessage[], announcements: Announcement[]): CommunicationAnalytics {
  const total = messages.length;
  const delivered = messages.filter((m) => m.deliveryStatus === 'delivered' || m.deliveryStatus === 'read').length;
  const read = messages.filter((m) => m.readStatus === 'read').length;
  const failed = messages.filter((m) => m.deliveryStatus === 'failed').length;

  const typeCount: Record<string, number> = {};
  const priorityCount: Record<string, number> = {};
  messages.forEach((m) => { typeCount[m.type] = (typeCount[m.type] || 0) + 1; priorityCount[m.priority] = (priorityCount[m.priority] || 0) + 1; });

  const courseMap: Record<string, number> = {};
  const batchMap: Record<string, number> = {};
  messages.forEach((m) => { courseMap[m.courseName] = (courseMap[m.courseName] || 0) + 1; batchMap[m.batchName] = (batchMap[m.batchName] || 0) + 1; });

  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  const announcementTrends = months.map((m, i) => ({ month: m, count: announcements.filter((a) => a.createdDate.startsWith(`2026-${String(i + 1).padStart(2, '0')}`)).length || Math.floor(Math.random() * 10) }));
  const notificationTrends = months.map((m, i) => ({ month: m, count: messages.filter((msg) => msg.createdDate.startsWith(`2026-${String(i + 1).padStart(2, '0')}`)).length || Math.floor(Math.random() * 20) }));

  return {
    totalSent: total, totalDelivered: delivered, totalRead: read, totalFailed: failed,
    readRate: total ? Math.round((read / total) * 100) : 0, unreadRate: total ? Math.round(((total - read) / total) * 100) : 0,
    distributionByType: Object.entries(typeCount).map(([type, count]) => ({ type, count })),
    distributionByPriority: Object.entries(priorityCount).map(([priority, count]) => ({ priority, count })),
    courseNotifications: Object.entries(courseMap).map(([courseName, count]) => ({ courseName, count })),
    batchNotifications: Object.entries(batchMap).map(([batchName, count]) => ({ batchName, count })),
    announcementTrends, notificationTrends, engagementRate: Math.floor(Math.random() * 40) + 40, openRate: Math.floor(Math.random() * 30) + 50,
  };
}

function generateDashboard(messages: CommunicationMessage[], announcements: Announcement[]): EngagementDashboard {
  const unread = messages.filter((m) => m.readStatus !== 'read').length;
  const read = messages.filter((m) => m.readStatus === 'read').length;
  const total = messages.length;
  const priorityCount: Record<string, number> = {};
  messages.forEach((m) => { priorityCount[m.priority] = (priorityCount[m.priority] || 0) + 1; });

  const courseNotifs = new Set(messages.map((m) => m.courseId)).size;
  const batchNotifs = new Set(messages.map((m) => m.batchId)).size;

  return {
    notificationsSent: total, announcementsCount: announcements.length, unreadMessages: unread,
    readPercentage: total ? Math.round((read / total) * 100) : 0, studentEngagement: Math.floor(Math.random() * 30) + 55,
    openRate: Math.floor(Math.random() * 30) + 50, courseNotifications: courseNotifs, batchNotifications: batchNotifs,
    systemAlerts: messages.filter((m) => m.type === 'system-notification' || m.type === 'maintenance-notification').length,
    activeTemplates: TYPES.length, recentMessages: messages.slice(0, 5), recentAnnouncements: announcements.slice(0, 5),
    priorityDistribution: Object.entries(priorityCount).map(([priority, count]) => ({ priority, count })),
  };
}

const messages = generateMessages(60);
const announcements = generateAnnouncements(20);
const inbox = generateInbox(40);
const timeline = generateTimeline(27);
const preferences = generatePreferences();
const templates = generateTemplates();
const analytics = generateAnalytics(messages, announcements);
const dashboard = generateDashboard(messages, announcements);

export function getMessages(): CommunicationMessage[] { return messages; }
export function getAnnouncements(): Announcement[] { return announcements; }
export function getInbox(): InboxItem[] { return inbox; }
export function getTimeline(): TimelineEvent[] { return timeline; }
export function getPreferences(): CommunicationPreference[] { return preferences; }
export function getTemplates(): NotificationTemplate[] { return templates; }
export function getAnalytics(): CommunicationAnalytics { return analytics; }
export function getDashboard(): EngagementDashboard { return dashboard; }
