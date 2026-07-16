export type StudentStatus =
  | 'prospective'
  | 'applied'
  | 'pending-approval'
  | 'approved'
  | 'enrolled'
  | 'active'
  | 'inactive'
  | 'completed'
  | 'certified'
  | 'alumni'
  | 'archived';

export type LearningMode = 'online' | 'offline' | 'hybrid';

export type StudentViewMode = 'table' | 'grid' | 'compact' | 'card' | 'grouped';

export type StudentSortField =
  | 'name'
  | 'email'
  | 'status'
  | 'course'
  | 'registrationDate'
  | 'language'
  | 'category';

export type SortDirection = 'asc' | 'desc';

export interface StudentSortConfig {
  field: StudentSortField;
  direction: SortDirection;
}

export interface Student {
  id: string;
  studentId: string;
  enrollmentId: string | null;
  fullName: string;
  preferredName: string;
  profilePhotoUrl: string | null;
  email: string;
  phone: string;
  gender: 'male' | 'female' | 'other' | 'not-specified';
  dateOfBirth: string;
  nationality: string;
  state: string;
  district: string;
  villageCity: string;
  registrationDate: string;
  status: StudentStatus;
  currentBatch: string | null;
  course: string | null;
  learningMode: LearningMode;
  language: string;
  category: string;
  tags: string[];
}

export interface StudentDashboardStats {
  totalStudents: number;
  activeStudents: number;
  pendingApprovals: number;
  newRegistrations: number;
  inactiveStudents: number;
  archivedStudents: number;
  studentsByCourse: { course: string; count: number }[];
  studentsByLanguage: { language: string; count: number }[];
  studentsByLearningMode: { mode: LearningMode; count: number }[];
  futureAlumniCount: number;
  futureCertifiedCount: number;
}

export interface StudentQuickAction {
  id: string;
  label: string;
  description: string;
  icon: string;
}

export interface StudentFilters {
  status?: StudentStatus[];
  course?: string[];
  language?: string[];
  learningMode?: LearningMode[];
  registrationDateFrom?: string;
  registrationDateTo?: string;
  category?: string[];
  state?: string[];
  district?: string[];
  tags?: string[];
}

export interface StudentCategory {
  id: string;
  label: string;
  count: number;
  icon: string;
  color: string;
}

export const STUDENT_STATUS_ORDER: StudentStatus[] = [
  'prospective',
  'applied',
  'pending-approval',
  'approved',
  'enrolled',
  'active',
  'inactive',
  'completed',
  'certified',
  'alumni',
  'archived',
];

export const STUDENT_STATUS_LABELS: Record<StudentStatus, string> = {
  'prospective': 'Prospective',
  'applied': 'Applied',
  'pending-approval': 'Pending Approval',
  'approved': 'Approved',
  'enrolled': 'Enrolled',
  'active': 'Active',
  'inactive': 'Inactive',
  'completed': 'Completed',
  'certified': 'Certified',
  'alumni': 'Alumni',
  'archived': 'Archived',
};

export const STUDENT_STATUS_VARIANTS: Record<StudentStatus, 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'prospective': 'neutral',
  'applied': 'info',
  'pending-approval': 'warning',
  'approved': 'primary',
  'enrolled': 'info',
  'active': 'success',
  'inactive': 'neutral',
  'completed': 'primary',
  'certified': 'success',
  'alumni': 'info',
  'archived': 'default',
};

export const STUDENT_CATEGORIES: StudentCategory[] = [
  { id: 'individual', label: 'Individual Learner', count: 0, icon: 'user', color: 'var(--color-primary)' },
  { id: 'corporate', label: 'Corporate Trainee', count: 0, icon: 'users', color: 'var(--color-success)' },
  { id: 'institutional', label: 'Institutional', count: 0, icon: 'home', color: 'var(--color-info)' },
  { id: 'franchise', label: 'Franchise Trainee', count: 0, icon: 'users', color: 'var(--color-warning)' },
  { id: 'government', label: 'Government Sponsored', count: 0, icon: 'shield', color: 'var(--color-danger)' },
  { id: 'scholarship', label: 'Scholarship', count: 0, icon: 'star', color: 'var(--color-primary)' },
];
