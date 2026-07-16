export interface TrainingNavItem {
  id: string;
  label: string;
  icon: string;
  href: string;
  description: string;
  children?: TrainingNavItem[];
}

export const TRAINING_NAV_ITEMS: TrainingNavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: 'layout', href: '/admin/training/dashboard', description: 'Training operations overview' },
  { id: 'courses', label: 'Courses', icon: 'book-open', href: '/admin/training/courses', description: 'Manage training courses' },
  { id: 'curriculum', label: 'Curriculum', icon: 'layers', href: '/admin/training/curriculum', description: 'Curriculum management' },
  { id: 'batches', label: 'Training Batches', icon: 'calendar', href: '/admin/training/batches', description: 'Batch scheduling and management' },
  { id: 'students', label: 'Students', icon: 'users', href: '/admin/training/students', description: 'Student enrollment and profiles' },
  { id: 'trainers', label: 'Trainers', icon: 'user-check', href: '/admin/training/trainers', description: 'Trainer management' },
  { id: 'attendance', label: 'Attendance', icon: 'check-circle', href: '/admin/training/attendance', description: 'Session attendance tracking' },
  { id: 'assignments', label: 'Assignments', icon: 'file', href: '/admin/training/assignments', description: 'Assignment management' },
  { id: 'assessments', label: 'Assessments', icon: 'target', href: '/admin/training/assessments', description: 'Assessment and evaluation' },
  { id: 'certificates', label: 'Certificates', icon: 'award', href: '/admin/training/certificates', description: 'Certificate issuance and verification' },
  { id: 'resources', label: 'Learning Resources', icon: 'folder', href: '/admin/training/resources', description: 'Learning materials and resources' },
  { id: 'announcements', label: 'Announcements', icon: 'message-circle', href: '/admin/training/announcements', description: 'Platform announcements' },
  { id: 'reports', label: 'Reports', icon: 'bar-chart', href: '/admin/training/reports', description: 'Training reports' },
  { id: 'analytics', label: 'Analytics', icon: 'trending-up', href: '/admin/training/analytics', description: 'Training analytics and insights' },
  { id: 'settings', label: 'Settings', icon: 'settings', href: '/admin/training/settings', description: 'Workspace settings' },
];

export const FUTURE_NAV_ITEMS: TrainingNavItem[] = [
  { id: 'ai-assistant', label: 'AI Assistant', icon: 'sparkles', href: '/admin/training/ai-assistant', description: 'AI-powered training assistant (Coming Soon)' },
  { id: 'community', label: 'Community', icon: 'message-square', href: '/admin/training/community', description: 'Training community (Coming Soon)' },
  { id: 'discussions', label: 'Discussion Board', icon: 'message-circle', href: '/admin/training/discussions', description: 'Discussion forums (Coming Soon)' },
];

export function getTrainingNavGroup(items: TrainingNavItem[], _group: string): TrainingNavItem[] {
  return items.filter(() => true);
}

export const NAV_GROUPS: { id: string; label: string; items: TrainingNavItem[] }[] = [
  { id: 'overview', label: 'Overview', items: TRAINING_NAV_ITEMS.filter((i) => i.id === 'dashboard') },
  { id: 'management', label: 'Management', items: TRAINING_NAV_ITEMS.filter((i) => ['courses', 'curriculum', 'batches', 'students', 'trainers'].includes(i.id)) },
  { id: 'operations', label: 'Operations', items: TRAINING_NAV_ITEMS.filter((i) => ['attendance', 'assignments', 'assessments', 'certificates', 'resources', 'announcements'].includes(i.id)) },
  { id: 'intelligence', label: 'Intelligence', items: TRAINING_NAV_ITEMS.filter((i) => ['reports', 'analytics'].includes(i.id)) },
  { id: 'settings', label: 'Settings', items: TRAINING_NAV_ITEMS.filter((i) => i.id === 'settings') },
  { id: 'future', label: 'Coming Soon', items: FUTURE_NAV_ITEMS },
];

const LABEL_MAP: Record<string, string> = {
  dashboard: 'Dashboard',
  courses: 'Courses',
  curriculum: 'Curriculum',
  batches: 'Training Batches',
  students: 'Students',
  trainers: 'Trainers',
  attendance: 'Attendance',
  assignments: 'Assignments',
  assessments: 'Assessments',
  certificates: 'Certificates',
  resources: 'Learning Resources',
  announcements: 'Announcements',
  reports: 'Reports',
  analytics: 'Analytics',
  settings: 'Settings',
  'ai-assistant': 'AI Assistant',
  community: 'Community',
  discussions: 'Discussion Board',
};

export function getTrainingLabel(section: string): string {
  return LABEL_MAP[section] || section;
}

export function getTrainingDescription(section: string): string {
  const item = [...TRAINING_NAV_ITEMS, ...FUTURE_NAV_ITEMS].find((i) => i.id === section);
  return item?.description || '';
}

export function getTrainingActiveId(pathname: string): string {
  const segments = pathname.replace('/admin/training', '').split('/').filter(Boolean);
  return segments[0] || 'dashboard';
}

export interface TrainingCrumb {
  label: string;
  href?: string;
}

export function buildTrainingBreadcrumbs(pathname: string): TrainingCrumb[] {
  const crumbs: TrainingCrumb[] = [
    { label: 'Admin', href: '/admin/dashboard' },
    { label: 'Training', href: '/admin/training/dashboard' },
  ];
  const segments = pathname.replace('/admin/training', '').split('/').filter(Boolean);
  if (segments.length > 0 && segments[0] !== 'dashboard') {
    crumbs.push({ label: getTrainingLabel(segments[0]), href: `/admin/training/${segments[0]}` });
  }
  if (segments.length > 1) {
    crumbs.push({ label: segments.slice(1).join(' / ') });
  }
  return crumbs;
}
