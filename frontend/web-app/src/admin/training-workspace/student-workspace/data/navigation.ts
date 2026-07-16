export interface StudentNavItem {
  id: string;
  label: string;
  icon: string;
  href: string;
  description: string;
}

export const STUDENT_NAV_ITEMS: StudentNavItem[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', href: '/admin/training/student-workspace/overview', description: 'Student workspace overview' },
  { id: 'registry', label: 'Student Registry', icon: 'database', href: '/admin/training/student-workspace/registry', description: 'Master student registry' },
  { id: 'directory', label: 'Student Directory', icon: 'users', href: '/admin/training/student-workspace/directory', description: 'Browse student directory' },
  { id: 'archived', label: 'Archived Students', icon: 'archive', href: '/admin/training/student-workspace/archived', description: 'View archived students' },
];

export const STUDENT_FUTURE_ITEMS: StudentNavItem[] = [
  { id: 'profile', label: 'Student Profile', icon: 'user', href: '/admin/training/student-workspace/profile', description: 'Student profile management (Coming Soon)' },
  { id: 'analytics', label: 'Student Analytics', icon: 'trending-up', href: '/admin/training/student-workspace/analytics', description: 'Student analytics (Coming Soon)' },
  { id: 'reports', label: 'Student Reports', icon: 'bar-chart', href: '/admin/training/student-workspace/reports', description: 'Student reports (Coming Soon)' },
  { id: 'alumni', label: 'Alumni', icon: 'award', href: '/admin/training/student-workspace/alumni', description: 'Alumni management (Coming Soon)' },
];

export const NAV_GROUPS: { id: string; label: string; items: StudentNavItem[] }[] = [
  { id: 'main', label: 'Student Management', items: STUDENT_NAV_ITEMS },
  { id: 'future', label: 'Coming Soon', items: STUDENT_FUTURE_ITEMS },
];

const LABEL_MAP: Record<string, string> = {
  overview: 'Overview',
  registry: 'Student Registry',
  directory: 'Student Directory',
  archived: 'Archived Students',
  profile: 'Student Profile',
  analytics: 'Student Analytics',
  reports: 'Student Reports',
  alumni: 'Alumni',
};

export function getStudentLabel(section: string): string {
  return LABEL_MAP[section] || section;
}

export function getStudentActiveId(pathname: string): string {
  const base = '/admin/training/student-workspace';
  const segments = pathname.replace(base, '').split('/').filter(Boolean);
  return segments[0] || 'overview';
}
