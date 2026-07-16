export type PerformanceLevel = 'excellent' | 'good' | 'average' | 'needs-attention' | 'critical' | 'outstanding' | 'top-performer';

export interface StudentAnalytics {
  id: string;
  studentId: string;
  studentName: string;
  enrollmentId: string;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  attendanceScore: number;
  assignmentScore: number;
  assessmentScore: number;
  learningProgress: number;
  competencyIndex: number;
  certificationReadiness: number;
  learningHours: number;
  engagementScore: number;
  activityFrequency: number;
  achievementCount: number;
  riskIndicator: string;
  performanceLevel: PerformanceLevel;
  lastActivityDate: string;
}

export interface CourseMetrics {
  courseId: string;
  courseName: string;
  enrollmentCount: number;
  completionCount: number;
  completionPercent: number;
  averageAttendance: number;
  averageAssignmentScore: number;
  averageAssessmentScore: number;
  averageLearningHours: number;
  certificationPercent: number;
  competencyGrowth: number;
  enrollmentTrend: { month: string; count: number }[];
  completionTrend: { month: string; count: number }[];
  assessmentResults: { range: string; count: number }[];
}

export interface TrainerMetrics {
  trainerId: string;
  trainerName: string;
  coursesDelivered: number;
  totalStudents: number;
  averageAttendance: number;
  studentSuccessRate: number;
  averageAssessmentScore: number;
  completionPercent: number;
  certificationRate: number;
  averageLearningHours: number;
  courseNames: string[];
}

export interface BatchMetrics {
  batchId: string;
  batchName: string;
  totalStudents: number;
  completionPercent: number;
  averageAttendance: number;
  averageAssignmentScore: number;
  averageAssessmentScore: number;
  certificationRate: number;
  averageCompetencies: number;
  totalLearningHours: number;
  activeStudents: number;
  atRiskCount: number;
}

export interface ExecutiveDashboard {
  activeStudents: number;
  totalCourses: number;
  activeTrainers: number;
  activeBatches: number;
  courseCompletionPercent: number;
  averageAttendance: number;
  averageAssessmentScore: number;
  averageAssignmentScore: number;
  certificationRate: number;
  averageLearningHours: number;
  studentGrowthPercent: number;
  enrollmentTrend: { month: string; count: number }[];
  completionTrend: { month: string; count: number }[];
  recentActivity: { date: string; event: string; count: number }[];
}

export interface KPIData {
  label: string;
  value: string | number;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  icon: string;
  variant: 'default' | 'success' | 'warning' | 'danger' | 'info';
}

export interface AcademicInsights {
  topStudents: StudentAnalytics[];
  highPerformers: StudentAnalytics[];
  lowPerformers: StudentAnalytics[];
  attendanceRisks: StudentAnalytics[];
  completionRisks: StudentAnalytics[];
  achievementTrends: { month: string; count: number }[];
  learningGaps: { skill: string; studentsAffected: number }[];
}

export interface LearningIntelligence {
  studentTimeline: { studentId: string; studentName: string; events: { date: string; type: string; label: string }[] }[];
  engagementAnalysis: { studentId: string; studentName: string; engagementScore: number; trend: 'improving' | 'stable' | 'declining' }[];
  performanceAnalysis: { studentId: string; studentName: string; scores: { category: string; score: number }[] }[];
  competencyAnalysis: { category: string; averageLevel: number; maxLevel: number }[];
  achievementAnalysis: { type: string; count: number }[];
  learningHealth: { metric: string; value: number; status: 'healthy' | 'warning' | 'critical' }[];
  completionForecast: { month: string; predicted: number; actual: number }[];
  certificationForecast: { month: string; predicted: number; actual: number }[];
}

export interface ChartDataPoint {
  label: string;
  value: number;
  color?: string;
}

export interface ChartSeries {
  name: string;
  data: ChartDataPoint[];
  color?: string;
}

export interface AnalyticsDashboard {
  executive: ExecutiveDashboard;
  studentMetrics: StudentAnalytics[];
  courseMetrics: CourseMetrics[];
  trainerMetrics: TrainerMetrics[];
  batchMetrics: BatchMetrics[];
  insights: AcademicInsights;
  intelligence: LearningIntelligence;
  kpis: KPIData[];
}

export const PERFORMANCE_LABELS: Record<PerformanceLevel, string> = {
  'excellent': 'Excellent', 'good': 'Good', 'average': 'Average',
  'needs-attention': 'Needs Attention', 'critical': 'Critical',
  'outstanding': 'Outstanding', 'top-performer': 'Top Performer',
};

export const PERFORMANCE_VARIANTS: Record<PerformanceLevel, 'success' | 'info' | 'warning' | 'danger' | 'default'> = {
  'excellent': 'success', 'good': 'info', 'average': 'warning',
  'needs-attention': 'warning', 'critical': 'danger',
  'outstanding': 'success', 'top-performer': 'success',
};

export const ANALYTICS_NAV_ITEMS = [
  { id: 'analytics', label: 'Executive Dashboard', icon: 'layout', description: 'Enterprise KPI overview' },
  { id: 'analytics/students', label: 'Student Analytics', icon: 'users', description: 'Per-student performance' },
  { id: 'analytics/courses', label: 'Course Analytics', icon: 'book', description: 'Course-level metrics' },
  { id: 'analytics/trainers', label: 'Trainer Analytics', icon: 'user-check', description: 'Trainer effectiveness' },
  { id: 'analytics/batches', label: 'Batch Analytics', icon: 'layers', description: 'Batch comparison' },
  { id: 'analytics/academic', label: 'Academic Intelligence', icon: 'brain', description: 'Insights & trends' },
  { id: 'analytics/learning-intelligence', label: 'Learning Intelligence', icon: 'activity', description: 'Deep learning analysis' },
];

export const EMPTY_STATE_TYPES = [
  'noAnalytics', 'noReports', 'noDashboardData',
  'noLearningMetrics', 'noStudentActivity', 'noSearchResults',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];
