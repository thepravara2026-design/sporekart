export type AssignmentStatus = 'draft' | 'published' | 'open' | 'in-progress' | 'submitted' | 'late' | 'under-review' | 'reviewed' | 'completed' | 'archived' | 'cancelled';

export type AssignmentType = 'homework' | 'project' | 'case-study' | 'lab-exercise' | 'field-work' | 'research' | 'portfolio' | 'presentation' | 'group-assignment' | 'capstone' | 'ai-assignment';

export type Difficulty = 'beginner' | 'intermediate' | 'advanced' | 'expert';

export type SubmissionType = 'text' | 'document' | 'pdf' | 'image' | 'spreadsheet' | 'presentation' | 'video' | 'git-repository' | 'external-link' | 'portfolio';

export type ProjectType = 'individual' | 'team' | 'research' | 'commercial' | 'field' | 'laboratory' | 'innovation' | 'startup';

export type EvaluationStatus = 'pending' | 'in-progress' | 'completed' | 'appealed';

export type SubmissionStatus = 'not-started' | 'draft' | 'submitted' | 'late' | 'under-review' | 'reviewed' | 'resubmitted' | 'accepted' | 'rejected';

export type SortField = 'newest' | 'oldest' | 'deadline' | 'difficulty' | 'name' | 'course' | 'status';

export interface Assignment {
  id: string;
  assignmentCode: string;
  title: string;
  description: string;
  courseId: string;
  courseName: string;
  moduleId: string;
  moduleName: string;
  lessonId: string;
  lessonName: string;
  category: string;
  assignmentType: AssignmentType;
  difficulty: Difficulty;
  learningObjectives: string[];
  estimatedDurationMinutes: number;
  dueDate: string;
  status: AssignmentStatus;
  createdBy: string;
  batchId: string;
  batchName: string;
  attachments: string[];
  maxMarks: number;
  passingMarks: number;
  submissionType: SubmissionType;
  createdDate: string;
  lastUpdated: string;
}

export interface Project {
  id: string;
  projectCode: string;
  title: string;
  description: string;
  projectType: ProjectType;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  status: AssignmentStatus;
  maxMarks: number;
  passingMarks: number;
  dueDate: string;
  teamSize: number | null;
  members: string[];
  createdDate: string;
  lastUpdated: string;
}

export interface Submission {
  id: string;
  submissionCode: string;
  assignmentId: string;
  assignmentTitle: string;
  studentId: string;
  studentName: string;
  batchId: string;
  batchName: string;
  courseId: string;
  courseName: string;
  submissionType: SubmissionType;
  content: string;
  attachments: string[];
  externalLinks: string[];
  status: SubmissionStatus;
  submittedDate: string | null;
  wordCount: number;
  plagiarismScore: number | null;
  createdDate: string;
  lastUpdated: string;
}

export interface Evaluation {
  id: string;
  submissionId: string;
  assignmentId: string;
  studentId: string;
  studentName: string;
  evaluatorId: string;
  evaluatorName: string;
  maxMarks: number;
  scoredMarks: number | null;
  passingMarks: number;
  grade: string | null;
  comments: string;
  suggestions: string;
  evaluationStatus: EvaluationStatus;
  evaluatedAt: string | null;
  createdDate: string;
  lastUpdated: string;
}

export interface AssignmentTimelineEvent {
  id: string;
  assignmentId: string;
  type: 'created' | 'published' | 'viewed' | 'started' | 'submitted' | 'reviewed' | 'completed';
  label: string;
  description: string;
  date: string;
  completed: boolean;
}

export interface SubmissionTimelineEvent {
  id: string;
  submissionId: string;
  type: 'opened' | 'work-started' | 'draft-saved' | 'submitted' | 'late-submission' | 'under-review' | 'reviewed' | 'accepted' | 'rejected' | 'resubmitted';
  label: string;
  description: string;
  date: string;
  completed: boolean;
}

export interface AssignmentDashboardStats {
  totalAssignments: number;
  activeAssignments: number;
  draftAssignments: number;
  completedAssignments: number;
  pendingSubmissions: number;
  lateSubmissions: number;
  totalProjects: number;
  practicalTasks: number;
  courseWise: { course: string; count: number }[];
  batchWise: { batch: string; count: number }[];
  upcomingDeadlines: { id: string; title: string; dueDate: string; batchName: string }[];
}

export interface AssignmentAnalytics {
  totalAssignments: number;
  submissionRate: number;
  completionRate: number;
  lateSubmissionPercent: number;
  averageCompletionTimeDays: number;
  assignmentDistribution: { type: AssignmentType; count: number }[];
  courseWise: { course: string; count: number }[];
  batchWise: { batch: string; count: number }[];
  monthlyTrend: { month: string; count: number }[];
}

export interface ApplicationError {
  type: 'missing' | 'permission-denied' | 'unexpected' | 'offline';
  title: string;
  message: string;
}

export const ASSIGNMENT_STATUS_LABELS: Record<AssignmentStatus, string> = {
  'draft': 'Draft',
  'published': 'Published',
  'open': 'Open',
  'in-progress': 'In Progress',
  'submitted': 'Submitted',
  'late': 'Late',
  'under-review': 'Under Review',
  'reviewed': 'Reviewed',
  'completed': 'Completed',
  'archived': 'Archived',
  'cancelled': 'Cancelled',
};

export const ASSIGNMENT_STATUS_VARIANTS: Record<AssignmentStatus, 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'draft': 'default',
  'published': 'info',
  'open': 'info',
  'in-progress': 'info',
  'submitted': 'warning',
  'late': 'danger',
  'under-review': 'warning',
  'reviewed': 'info',
  'completed': 'success',
  'archived': 'neutral',
  'cancelled': 'neutral',
};

export const ASSIGNMENT_TYPE_LABELS: Record<AssignmentType, string> = {
  'homework': 'Homework',
  'project': 'Project',
  'case-study': 'Case Study',
  'lab-exercise': 'Lab Exercise',
  'field-work': 'Field Work',
  'research': 'Research',
  'portfolio': 'Portfolio',
  'presentation': 'Presentation',
  'group-assignment': 'Group Assignment',
  'capstone': 'Capstone',
  'ai-assignment': 'AI Assignment',
};

export const DIFFICULTY_LABELS: Record<Difficulty, string> = {
  'beginner': 'Beginner',
  'intermediate': 'Intermediate',
  'advanced': 'Advanced',
  'expert': 'Expert',
};

export const SUBMISSION_TYPE_LABELS: Record<SubmissionType, string> = {
  'text': 'Text Response',
  'document': 'Document',
  'pdf': 'PDF',
  'image': 'Image',
  'spreadsheet': 'Spreadsheet',
  'presentation': 'Presentation',
  'video': 'Video',
  'git-repository': 'Git Repository',
  'external-link': 'External Link',
  'portfolio': 'Portfolio',
};

export const PROJECT_TYPE_LABELS: Record<ProjectType, string> = {
  'individual': 'Individual Project',
  'team': 'Team Project',
  'research': 'Research Project',
  'commercial': 'Commercial Project',
  'field': 'Field Project',
  'laboratory': 'Laboratory Project',
  'innovation': 'Innovation Project',
  'startup': 'Startup Project',
};

export const EVALUATION_STATUS_LABELS: Record<EvaluationStatus, string> = {
  'pending': 'Pending',
  'in-progress': 'In Progress',
  'completed': 'Completed',
  'appealed': 'Appealed',
};

export const SUBMISSION_STATUS_LABELS: Record<SubmissionStatus, string> = {
  'not-started': 'Not Started',
  'draft': 'Draft',
  'submitted': 'Submitted',
  'late': 'Late',
  'under-review': 'Under Review',
  'reviewed': 'Reviewed',
  'resubmitted': 'Resubmitted',
  'accepted': 'Accepted',
  'rejected': 'Rejected',
};

export const ASSIGNMENT_NAV_ITEMS = [
  { id: 'assignments', label: 'Assignment Dashboard', icon: 'layout', description: 'Assignment overview' },
  { id: 'assignments/registry', label: 'Assignment Registry', icon: 'list', description: 'All assignments' },
  { id: 'assignments/projects', label: 'Projects', icon: 'folder', description: 'Project management' },
  { id: 'assignments/submissions', label: 'Submission Queue', icon: 'upload', description: 'Pending submissions' },
  { id: 'assignments/evaluations', label: 'Evaluation Queue', icon: 'check-square', description: 'Pending evaluations' },
  { id: 'assignments/timeline', label: 'Assignment Timeline', icon: 'clock', description: 'Assignment lifecycle' },
  { id: 'assignments/analytics', label: 'Assignment Analytics', icon: 'bar-chart', description: 'Assignment analytics' },
  { id: 'assignments/archived', label: 'Archived', icon: 'archive', description: 'Archived assignments' },
];

export const EMPTY_STATE_TYPES = [
  'noAssignments',
  'noProjects',
  'noSubmissions',
  'noDrafts',
  'noActiveAssignments',
  'noSearchResults',
  'noEvaluations',
  'noTimelineEvents',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];

export const ERROR_STATES: Record<string, ApplicationError> = {
  assignmentMissing: { type: 'missing', title: 'Assignment Not Found', message: 'The requested assignment could not be found.' },
  submissionMissing: { type: 'missing', title: 'Submission Not Found', message: 'The requested submission could not be found.' },
  permissionDenied: { type: 'permission-denied', title: 'Permission Denied', message: 'You do not have permission to access this resource.' },
  unexpected: { type: 'unexpected', title: 'Unexpected Error', message: 'An unexpected error occurred. Please try again.' },
  offline: { type: 'offline', title: 'You Are Offline', message: 'Please check your internet connection and try again.' },
};
