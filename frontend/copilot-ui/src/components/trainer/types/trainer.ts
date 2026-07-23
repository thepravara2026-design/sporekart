export interface TrainingBatch {
  batchId: string; batchName: string; courseName: string; startDate: string; endDate: string;
  status: 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  capacity: number; enrolledCount: number; trainerName: string; location: string;
}

export interface TrainingModule {
  moduleId: string; moduleName: string; description: string; durationHours: number;
  moduleType: 'THEORY' | 'PRACTICAL' | 'LAB' | 'ASSESSMENT';
  learningObjectives: string[];
}

export interface Lesson {
  lessonId: string; moduleId: string; title: string; content: string;
  lessonType: 'LECTURE' | 'DEMONSTRATION' | 'LAB_EXERCISE' | 'WORKSHOP' | 'REVIEW';
  durationMinutes: number; learningObjectives: string[];
  materials: string[]; activities: string[];
  difficulty: 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
}

export interface Question {
  questionId: string; questionText: string; questionType: string;
  options?: string[]; correctAnswer: string; marks: number; explanation: string;
}

export interface Assessment {
  assessmentId: string; moduleId: string; title: string;
  type: 'MCQ' | 'SHORT_ANSWER' | 'LONG_ANSWER' | 'SCENARIO' | 'PRACTICAL' | 'LAB' | 'CERTIFICATION';
  difficulty: string; questions: Question[]; totalMarks: number; passingMarks: number;
}

export interface StudentProgress {
  studentId: string; studentName: string; batchId: string;
  attendancePercent: number; overallScore: number;
  completedAssignments: number; totalAssignments: number;
  completedPracticals: number; totalPracticals: number;
  weakTopics: string[]; strongTopics: string[];
}

export interface Certification {
  certificationId: string; studentId: string; studentName: string;
  courseName: string; overallScore: number;
  recommendation: 'PASS' | 'NEEDS_IMPROVEMENT' | 'ADDITIONAL_TRAINING';
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
}

export interface PracticalGuide {
  guideId: string; title: string;
  category: string; difficulty: string;
  steps: string[]; requiredMaterials: string[];
  safetyPrecautions: string[]; commonMistakes: string[];
}

export interface KnowledgeResult {
  id: string; title: string; content: string; source: string;
  relevanceScore: number; citation: string;
}

export interface ChatMessage {
  id: string; role: 'user' | 'assistant'; content: string;
  timestamp: string; suggestions?: Suggestion[];
}

export interface Suggestion {
  label: string; action: string; payload?: Record<string, unknown>;
}

export interface BatchAnalytics {
  batchId: string; batchName: string; avgAttendance: number;
  avgScore: number; completedModules: number; totalModules: number;
  topStudents: StudentProgress[]; atRiskStudents: StudentProgress[];
}

export interface AnalyticsSummary {
  totalStudents: number; totalBatches: number; activeBatches: number;
  completedBatches: number; avgAttendanceAcrossBatches: number;
  avgScoreAcrossBatches: number; atRiskStudents: number;
  totalCertificationsIssued: number; pendingCertifications: number;
}
