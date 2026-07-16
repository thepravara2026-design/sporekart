export type AssessmentStatus = 'draft' | 'scheduled' | 'published' | 'open' | 'active' | 'paused' | 'completed' | 'under-evaluation' | 'evaluated' | 'archived' | 'cancelled';

export type AssessmentType = 'quiz' | 'objective-test' | 'subjective-test' | 'practical-assessment' | 'laboratory-assessment' | 'field-assessment' | 'case-study-evaluation' | 'project-evaluation' | 'assignment-evaluation' | 'oral-viva' | 'interview' | 'mock-test' | 'practice-assessment' | 'final-examination' | 'certification-assessment' | 'corporate-skill-assessment' | 'government-assessment' | 'ai-adaptive-assessment';

export type Difficulty = 'beginner' | 'intermediate' | 'advanced' | 'expert';

export type QuestionType = 'multiple-choice' | 'multiple-select' | 'true-false' | 'fill-blank' | 'short-answer' | 'essay' | 'practical-task' | 'case-study' | 'matching' | 'ordering' | 'image-based' | 'video-based' | 'file-upload' | 'coding';

export type EvaluationMethod = 'auto-graded' | 'manual-graded' | 'hybrid' | 'ai-evaluated';

export type PerformanceBand = 'excellent' | 'good' | 'average' | 'below-average' | 'poor';

export type CompetencyLevel = 'novice' | 'beginner' | 'competent' | 'proficient' | 'expert';

export type ResultStatus = 'pass' | 'fail' | 'incomplete' | 'under-review' | 'invalid';

export interface Assessment {
  id: string;
  assessmentCode: string;
  title: string;
  description: string;
  courseId: string;
  courseName: string;
  moduleId: string;
  moduleName: string;
  lessonId: string;
  lessonName: string;
  assessmentType: AssessmentType;
  difficulty: Difficulty;
  durationMinutes: number;
  maxMarks: number;
  passingMarks: number;
  totalQuestions: number;
  attemptLimit: number;
  evaluationMethod: EvaluationMethod;
  availabilityStart: string;
  availabilityEnd: string;
  batchId: string;
  batchName: string;
  status: AssessmentStatus;
  instructions: string;
  createdDate: string;
  lastUpdated: string;
}

export interface QuestionCategory {
  id: string;
  name: string;
  description: string;
  difficulty: Difficulty;
  learningOutcomes: string[];
  tags: string[];
  questionCount: number;
  totalMarks: number;
}

export interface Result {
  id: string;
  resultCode: string;
  assessmentId: string;
  assessmentTitle: string;
  studentId: string;
  studentName: string;
  batchId: string;
  batchName: string;
  courseId: string;
  courseName: string;
  maxMarks: number;
  passingMarks: number;
  scoredMarks: number;
  percentage: number;
  grade: string | null;
  resultStatus: ResultStatus;
  performanceBand: PerformanceBand;
  competencyLevel: CompetencyLevel;
  attemptNumber: number;
  totalAttempts: number;
  startedAt: string;
  submittedAt: string;
  evaluatedAt: string | null;
  timeTakenMinutes: number;
}

export interface AssessmentTimelineEvent {
  id: string;
  assessmentId: string;
  type: 'created' | 'published' | 'scheduled' | 'attempt-started' | 'submitted' | 'evaluated' | 'result-generated' | 'certificate-eligible';
  label: string;
  description: string;
  date: string;
  completed: boolean;
}

export interface AcademicAnalytics {
  totalAssessments: number;
  upcomingAssessments: number;
  completedAssessments: number;
  averageScore: number;
  highestScore: number;
  lowestScore: number;
  passPercent: number;
  failPercent: number;
  coursePerformance: { course: string; avgScore: number }[];
  batchPerformance: { batch: string; avgScore: number }[];
  questionDifficultyDistribution: { difficulty: Difficulty; count: number }[];
  assessmentCompletionPercent: number;
  monthlyTrend: { month: string; count: number }[];
}

export interface AssessmentDashboardStats {
  totalAssessments: number;
  upcomingAssessments: number;
  completedAssessments: number;
  averageScore: number;
  highestScore: number;
  lowestScore: number;
  passPercent: number;
  failPercent: number;
  coursePerformance: { course: string; avgScore: number }[];
  batchPerformance: { batch: string; avgScore: number }[];
  recentResults: Result[];
}

export const ASSESSMENT_STATUS_LABELS: Record<AssessmentStatus, string> = {
  'draft': 'Draft',
  'scheduled': 'Scheduled',
  'published': 'Published',
  'open': 'Open',
  'active': 'Active',
  'paused': 'Paused',
  'completed': 'Completed',
  'under-evaluation': 'Under Evaluation',
  'evaluated': 'Evaluated',
  'archived': 'Archived',
  'cancelled': 'Cancelled',
};

export const ASSESSMENT_STATUS_VARIANTS: Record<AssessmentStatus, 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'draft': 'default',
  'scheduled': 'info',
  'published': 'info',
  'open': 'info',
  'active': 'info',
  'paused': 'warning',
  'completed': 'success',
  'under-evaluation': 'warning',
  'evaluated': 'success',
  'archived': 'neutral',
  'cancelled': 'neutral',
};

export const ASSESSMENT_TYPE_LABELS: Record<AssessmentType, string> = {
  'quiz': 'Quiz',
  'objective-test': 'Objective Test',
  'subjective-test': 'Subjective Test',
  'practical-assessment': 'Practical Assessment',
  'laboratory-assessment': 'Laboratory Assessment',
  'field-assessment': 'Field Assessment',
  'case-study-evaluation': 'Case Study Evaluation',
  'project-evaluation': 'Project Evaluation',
  'assignment-evaluation': 'Assignment Evaluation',
  'oral-viva': 'Oral Viva',
  'interview': 'Interview',
  'mock-test': 'Mock Test',
  'practice-assessment': 'Practice Assessment',
  'final-examination': 'Final Examination',
  'certification-assessment': 'Certification Assessment',
  'corporate-skill-assessment': 'Corporate Skill Assessment',
  'government-assessment': 'Government Assessment',
  'ai-adaptive-assessment': 'AI Adaptive Assessment',
};

export const DIFFICULTY_LABELS: Record<Difficulty, string> = {
  'beginner': 'Beginner',
  'intermediate': 'Intermediate',
  'advanced': 'Advanced',
  'expert': 'Expert',
};

export const QUESTION_TYPE_LABELS: Record<QuestionType, string> = {
  'multiple-choice': 'Multiple Choice',
  'multiple-select': 'Multiple Select',
  'true-false': 'True / False',
  'fill-blank': 'Fill in the Blank',
  'short-answer': 'Short Answer',
  'essay': 'Essay',
  'practical-task': 'Practical Task',
  'case-study': 'Case Study',
  'matching': 'Matching',
  'ordering': 'Ordering',
  'image-based': 'Image Based',
  'video-based': 'Video Based',
  'file-upload': 'File Upload',
  'coding': 'Coding',
};

export const EVALUATION_METHOD_LABELS: Record<EvaluationMethod, string> = {
  'auto-graded': 'Auto Graded',
  'manual-graded': 'Manual Graded',
  'hybrid': 'Hybrid',
  'ai-evaluated': 'AI Evaluated',
};

export const PERFORMANCE_BAND_LABELS: Record<PerformanceBand, string> = {
  'excellent': 'Excellent',
  'good': 'Good',
  'average': 'Average',
  'below-average': 'Below Average',
  'poor': 'Poor',
};

export const COMPETENCY_LEVEL_LABELS: Record<CompetencyLevel, string> = {
  'novice': 'Novice',
  'beginner': 'Beginner',
  'competent': 'Competent',
  'proficient': 'Proficient',
  'expert': 'Expert',
};

export const RESULT_STATUS_LABELS: Record<ResultStatus, string> = {
  'pass': 'Pass',
  'fail': 'Fail',
  'incomplete': 'Incomplete',
  'under-review': 'Under Review',
  'invalid': 'Invalid',
};

export const ASSESSMENT_NAV_ITEMS = [
  { id: 'assessments', label: 'Assessment Dashboard', icon: 'layout', description: 'Assessment overview' },
  { id: 'assessments/registry', label: 'Assessment Registry', icon: 'list', description: 'All assessments' },
  { id: 'assessments/builder', label: 'Assessment Builder', icon: 'edit', description: 'Create & manage assessments' },
  { id: 'assessments/question-bank', label: 'Question Bank', icon: 'database', description: 'Question categories' },
  { id: 'assessments/examinations', label: 'Examination Center', icon: 'clock', description: 'Manage exam sessions' },
  { id: 'assessments/results', label: 'Result Center', icon: 'check-square', description: 'Assessment results' },
  { id: 'assessments/analytics', label: 'Academic Analytics', icon: 'bar-chart', description: 'Performance analytics' },
  { id: 'assessments/archived', label: 'Archived', icon: 'archive', description: 'Archived assessments' },
];

export const EMPTY_STATE_TYPES = [
  'noAssessments',
  'noResults',
  'noQuestionCategories',
  'noScheduledExams',
  'noCompletedExams',
  'noSearchResults',
  'noTimelineEvents',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];

export const ERROR_STATES: Record<string, { type: string; title: string; message: string }> = {
  assessmentMissing: { type: 'missing', title: 'Assessment Not Found', message: 'The requested assessment could not be found.' },
  resultMissing: { type: 'missing', title: 'Result Not Found', message: 'The requested result could not be found.' },
  permissionDenied: { type: 'permission-denied', title: 'Permission Denied', message: 'You do not have permission to access this resource.' },
  unexpected: { type: 'unexpected', title: 'Unexpected Error', message: 'An unexpected error occurred. Please try again.' },
  offline: { type: 'offline', title: 'You Are Offline', message: 'Please check your internet connection and try again.' },
};
