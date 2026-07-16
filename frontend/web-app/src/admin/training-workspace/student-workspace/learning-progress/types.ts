export type ProgressStatus = 'not-started' | 'started' | 'in-progress' | 'paused' | 'behind-schedule' | 'on-track' | 'completed' | 'certified' | 'archived' | 're-enrolled';

export type LearningStage = 'orientation' | 'foundation' | 'intermediate' | 'advanced' | 'practical' | 'field-work' | 'assessment' | 'certification' | 'completed' | 'alumni';

export type CompetencyCategory = 'knowledge' | 'practical-skills' | 'business-skills' | 'communication' | 'problem-solving' | 'leadership' | 'critical-thinking' | 'innovation' | 'industry-readiness' | 'farmer-readiness' | 'entrepreneur-readiness' | 'ai-competency';

export type SkillCategory = 'technical' | 'soft' | 'industry' | 'agriculture' | 'business' | 'certification';

export type MilestoneType = 'course-started' | 'progress-25' | 'progress-50' | 'progress-75' | 'progress-100' | 'assignment-milestone' | 'assessment-milestone' | 'attendance-milestone' | 'competency-milestone' | 'certification-ready' | 'completed';

export interface LearningProgress {
  id: string;
  progressCode: string;
  studentId: string;
  studentName: string;
  enrollmentId: string;
  courseId: string;
  courseName: string;
  moduleId: string;
  moduleName: string;
  lessonId: string;
  lessonName: string;
  progressPercent: number;
  status: ProgressStatus;
  currentStage: LearningStage;
  estimatedCompletionDate: string;
  learningHours: number;
  lastActivityDate: string;
  assignmentStatus: string;
  assessmentStatus: string;
  attendanceStatus: string;
  competencyStatus: string;
  createdDate: string;
  lastUpdated: string;
}

export interface Competency {
  id: string;
  competencyCode: string;
  studentId: string;
  studentName: string;
  category: CompetencyCategory;
  name: string;
  description: string;
  level: number;
  maxLevel: number;
  percentage: number;
  status: 'not-started' | 'in-progress' | 'achieved' | 'mastered';
  earnedDate: string | null;
  courseId: string;
  courseName: string;
}

export interface SkillMatrix {
  id: string;
  studentId: string;
  studentName: string;
  category: SkillCategory;
  skills: { name: string; level: number; maxLevel: number }[];
  overallPercentage: number;
}

export interface Milestone {
  id: string;
  milestoneCode: string;
  studentId: string;
  studentName: string;
  courseId: string;
  courseName: string;
  type: MilestoneType;
  label: string;
  description: string;
  achieved: boolean;
  achievedDate: string | null;
  progressPercent: number;
}

export interface LearningTimelineEvent {
  id: string;
  studentId: string;
  type: 'enrollment' | 'lesson-started' | 'lesson-completed' | 'assignment-submitted' | 'assessment-passed' | 'competency-achieved' | 'milestone-earned' | 'certification-eligible' | 'course-completed';
  label: string;
  description: string;
  date: string;
  completed: boolean;
}

export interface CertificationReadiness {
  id: string;
  studentId: string;
  studentName: string;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  attendancePercent: number;
  attendanceRequirement: number;
  assignmentCompletion: number;
  assignmentRequirement: number;
  assessmentScore: number;
  assessmentRequirement: number;
  competencyCount: number;
  competencyRequirement: number;
  overallEligibilityPercent: number;
  isReady: boolean;
  pendingRequirements: string[];
}

export interface LearningHealthDashboard {
  totalStudents: number;
  studentsOnTrack: number;
  studentsBehind: number;
  averageProgress: number;
  completionPercent: number;
  attendanceHealth: number;
  assignmentCompletion: number;
  assessmentCompletion: number;
  competencyCoverage: number;
  totalLearningHours: number;
  atRiskCount: number;
}

export interface LearningAnalytics {
  courseCompletionPercent: number;
  moduleCompletionPercent: number;
  lessonCompletionPercent: number;
  averageLearningHours: number;
  assessmentSuccessRate: number;
  assignmentSuccessRate: number;
  competencyGrowth: number;
  monthlyActiveStudents: { month: string; count: number }[];
  progressDistribution: { range: string; count: number }[];
}

export const PROGRESS_STATUS_LABELS: Record<ProgressStatus, string> = {
  'not-started': 'Not Started',
  'started': 'Started',
  'in-progress': 'In Progress',
  'paused': 'Paused',
  'behind-schedule': 'Behind Schedule',
  'on-track': 'On Track',
  'completed': 'Completed',
  'certified': 'Certified',
  'archived': 'Archived',
  're-enrolled': 'Re-Enrolled',
};

export const PROGRESS_STATUS_VARIANTS: Record<ProgressStatus, 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'not-started': 'default',
  'started': 'info',
  'in-progress': 'info',
  'paused': 'warning',
  'behind-schedule': 'danger',
  'on-track': 'success',
  'completed': 'success',
  'certified': 'success',
  'archived': 'neutral',
  're-enrolled': 'info',
};

export const LEARNING_STAGE_LABELS: Record<LearningStage, string> = {
  'orientation': 'Orientation',
  'foundation': 'Foundation',
  'intermediate': 'Intermediate',
  'advanced': 'Advanced',
  'practical': 'Practical',
  'field-work': 'Field Work',
  'assessment': 'Assessment',
  'certification': 'Certification',
  'completed': 'Completed',
  'alumni': 'Alumni',
};

export const COMPETENCY_CATEGORY_LABELS: Record<CompetencyCategory, string> = {
  'knowledge': 'Knowledge',
  'practical-skills': 'Practical Skills',
  'business-skills': 'Business Skills',
  'communication': 'Communication',
  'problem-solving': 'Problem Solving',
  'leadership': 'Leadership',
  'critical-thinking': 'Critical Thinking',
  'innovation': 'Innovation',
  'industry-readiness': 'Industry Readiness',
  'farmer-readiness': 'Farmer Readiness',
  'entrepreneur-readiness': 'Entrepreneur Readiness',
  'ai-competency': 'AI Competency',
};

export const SKILL_CATEGORY_LABELS: Record<SkillCategory, string> = {
  'technical': 'Technical Skills',
  'soft': 'Soft Skills',
  'industry': 'Industry Skills',
  'agriculture': 'Agriculture Skills',
  'business': 'Business Skills',
  'certification': 'Certification Skills',
};

export const MILESTONE_TYPE_LABELS: Record<MilestoneType, string> = {
  'course-started': 'Course Started',
  'progress-25': '25% Complete',
  'progress-50': '50% Complete',
  'progress-75': '75% Complete',
  'progress-100': '100% Complete',
  'assignment-milestone': 'Assignment Milestone',
  'assessment-milestone': 'Assessment Milestone',
  'attendance-milestone': 'Attendance Milestone',
  'competency-milestone': 'Competency Milestone',
  'certification-ready': 'Certification Ready',
  'completed': 'Completed',
};

export const LEARNING_PROGRESS_NAV_ITEMS = [
  { id: 'learning-progress', label: 'Learning Dashboard', icon: 'layout', description: 'Learning health overview' },
  { id: 'learning-progress/students', label: 'Student Progress', icon: 'users', description: 'Individual student progress' },
  { id: 'learning-progress/courses', label: 'Course Progress', icon: 'book', description: 'Course-level progress' },
  { id: 'learning-progress/modules', label: 'Module Progress', icon: 'layers', description: 'Module-level progress' },
  { id: 'learning-progress/competencies', label: 'Competency Center', icon: 'award', description: 'Competency tracking' },
  { id: 'learning-progress/milestones', label: 'Milestone Center', icon: 'flag', description: 'Milestone achievements' },
  { id: 'learning-progress/timeline', label: 'Learning Timeline', icon: 'clock', description: 'Academic journey timeline' },
  { id: 'learning-progress/certification', label: 'Certification Readiness', icon: 'check-circle', description: 'Certification eligibility' },
  { id: 'learning-progress/analytics', label: 'Learning Analytics', icon: 'bar-chart', description: 'Progress analytics' },
];

export const EMPTY_STATE_TYPES = [
  'noProgress',
  'noMilestones',
  'noCompetencies',
  'noLearningHours',
  'noTimeline',
  'noSearchResults',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];
