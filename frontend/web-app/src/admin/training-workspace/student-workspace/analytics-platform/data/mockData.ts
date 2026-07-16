import type { StudentAnalytics, CourseMetrics, TrainerMetrics, BatchMetrics, ExecutiveDashboard, KPIData, AcademicInsights, LearningIntelligence, PerformanceLevel } from '../types';

const STUDENTS = Array.from({ length: 25 }, (_, i) => ({ id: `stu-${i + 1}`, name: ['Aarav Sharma','Priya Patel','Rahul Singh','Ananya Gupta','Vikram Joshi','Neha Kapoor','Arjun Mehta','Kavita Reddy','Rohan Desai','Ishita Verma','Amit Kumar','Sneha Agarwal','Deepak Tiwari','Pooja Nair','Karan Malhotra','Divya Bhat','Suresh Iyer','Meera Choudhury','Nitin Saxena','Lakshmi Rajan','Rajesh Kumar','Anjali Sinha','Vivek Mishra','Pallavi Rao','Aditya Khanna'][i] }));

const COURSES = [
  { id: 'course-1', name: 'Full Stack Web Development' },
  { id: 'course-2', name: 'Data Science & Analytics' },
  { id: 'course-3', name: 'Cloud Architecture' },
  { id: 'course-4', name: 'Mobile App Development' },
  { id: 'course-5', name: 'DevOps Engineering' },
  { id: 'course-6', name: 'UI/UX Design' },
];

const BATCHES = [
  { id: 'batch-1', name: 'Morning Batch' },
  { id: 'batch-2', name: 'Evening Batch' },
  { id: 'batch-3', name: 'Weekend Batch' },
];

const TRAINERS = [
  { id: 'tr-1', name: 'Dr. Suresh Kumar' },
  { id: 'tr-2', name: 'Prof. Meena Iyer' },
  { id: 'tr-3', name: 'Mr. Ravi Deshmukh' },
  { id: 'tr-4', name: 'Ms. Neelam Sharma' },
  { id: 'tr-5', name: 'Dr. Arun Prakash' },
];

function randomFrom<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randomInt(min: number, max: number): number { return Math.floor(Math.random() * (max - min + 1)) + min; }

function randomDate(daysAgo: number): string {
  const d = new Date(); d.setDate(d.getDate() - Math.floor(Math.random() * daysAgo));
  return d.toISOString().split('T')[0];
}



function generateStudentAnalytics(): StudentAnalytics[] {
  return STUDENTS.flatMap((stu) => {
    const course = randomFrom(COURSES);
    const batch = randomFrom(BATCHES);
    const attendance = randomInt(60, 100);
    const assignment = randomInt(55, 100);
    const assessment = randomInt(50, 100);
    const progress = randomInt(20, 100);
    const avg = (attendance + assignment + assessment + progress) / 4;
    const level: PerformanceLevel = avg >= 90 ? 'outstanding' : avg >= 80 ? 'excellent' : avg >= 70 ? 'good' : avg >= 60 ? 'average' : avg >= 45 ? 'needs-attention' : 'critical';
    return {
      id: `sa-${stu.id}-${course.id}`,
      studentId: stu.id,
      studentName: stu.name,
      enrollmentId: `enr-${stu.id}-${course.id}`,
      courseId: course.id,
      courseName: course.name,
      batchId: batch.id,
      batchName: batch.name,
      attendanceScore: attendance,
      assignmentScore: assignment,
      assessmentScore: assessment,
      learningProgress: progress,
      competencyIndex: randomInt(30, 100),
      certificationReadiness: randomInt(40, 100),
      learningHours: randomInt(20, 150),
      engagementScore: randomInt(40, 100),
      activityFrequency: randomInt(5, 50),
      achievementCount: randomInt(0, 12),
      riskIndicator: level === 'critical' || level === 'needs-attention' ? 'At Risk' : level === 'average' ? 'Monitor' : 'Stable',
      performanceLevel: level,
      lastActivityDate: randomDate(7),
    };
  });
}

function generateCourseMetrics(analytics: StudentAnalytics[]): CourseMetrics[] {
  return COURSES.map((course) => {
    const courseStudents = analytics.filter((a) => a.courseId === course.id);
    const total = courseStudents.length;
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
    return {
      courseId: course.id,
      courseName: course.name,
      enrollmentCount: total,
      completionCount: courseStudents.filter((s) => s.learningProgress >= 80).length,
      completionPercent: total ? Math.round((courseStudents.filter((s) => s.learningProgress >= 80).length / total) * 100) : 0,
      averageAttendance: total ? Math.round(courseStudents.reduce((s, a) => s + a.attendanceScore, 0) / total) : 0,
      averageAssignmentScore: total ? Math.round(courseStudents.reduce((s, a) => s + a.assignmentScore, 0) / total) : 0,
      averageAssessmentScore: total ? Math.round(courseStudents.reduce((s, a) => s + a.assessmentScore, 0) / total) : 0,
      averageLearningHours: total ? Math.round(courseStudents.reduce((s, a) => s + a.learningHours, 0) / total) : 0,
      certificationPercent: total ? Math.round((courseStudents.filter((s) => s.certificationReadiness >= 80).length / total) * 100) : 0,
      competencyGrowth: randomInt(40, 95),
      enrollmentTrend: months.map((m) => ({ month: m, count: randomInt(5, 20) })),
      completionTrend: months.map((m) => ({ month: m, count: randomInt(2, 15) })),
      assessmentResults: [
        { range: '0-40%', count: randomInt(0, 3) },
        { range: '41-60%', count: randomInt(1, 5) },
        { range: '61-80%', count: randomInt(3, 8) },
        { range: '81-100%', count: randomInt(2, 10) },
      ],
    };
  });
}

function generateTrainerMetrics(): TrainerMetrics[] {
  return TRAINERS.map((tr) => ({
    trainerId: tr.id,
    trainerName: tr.name,
    coursesDelivered: randomInt(2, 6),
    totalStudents: randomInt(30, 120),
    averageAttendance: randomInt(70, 98),
    studentSuccessRate: randomInt(65, 95),
    averageAssessmentScore: randomInt(68, 95),
    completionPercent: randomInt(60, 92),
    certificationRate: randomInt(55, 88),
    averageLearningHours: randomInt(40, 100),
    courseNames: COURSES.slice(0, randomInt(2, 4)).map((c) => c.name),
  }));
}

function generateBatchMetrics(analytics: StudentAnalytics[]): BatchMetrics[] {
  return BATCHES.map((batch) => {
    const batchStudents = analytics.filter((a) => a.batchId === batch.id);
    const total = batchStudents.length;
    return {
      batchId: batch.id,
      batchName: batch.name,
      totalStudents: total,
      completionPercent: total ? Math.round((batchStudents.filter((s) => s.learningProgress >= 80).length / total) * 100) : 0,
      averageAttendance: total ? Math.round(batchStudents.reduce((s, a) => s + a.attendanceScore, 0) / total) : 0,
      averageAssignmentScore: total ? Math.round(batchStudents.reduce((s, a) => s + a.assignmentScore, 0) / total) : 0,
      averageAssessmentScore: total ? Math.round(batchStudents.reduce((s, a) => s + a.assessmentScore, 0) / total) : 0,
      certificationRate: total ? Math.round((batchStudents.filter((s) => s.certificationReadiness >= 80).length / total) * 100) : 0,
      averageCompetencies: total ? Math.round(batchStudents.reduce((s, a) => s + a.competencyIndex, 0) / total / 20) : 0,
      totalLearningHours: batchStudents.reduce((s, a) => s + a.learningHours, 0),
      activeStudents: batchStudents.filter((s) => s.performanceLevel !== 'critical').length,
      atRiskCount: batchStudents.filter((s) => s.performanceLevel === 'critical' || s.performanceLevel === 'needs-attention').length,
    };
  });
}

function generateExecutiveDashboard(analytics: StudentAnalytics[], courseMetrics: CourseMetrics[], _trainerMetrics: TrainerMetrics[], _batchMetrics: BatchMetrics[]): ExecutiveDashboard {
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  return {
    activeStudents: analytics.length,
    totalCourses: COURSES.length,
    activeTrainers: TRAINERS.length,
    activeBatches: BATCHES.length,
    courseCompletionPercent: Math.round(courseMetrics.reduce((s, c) => s + c.completionPercent, 0) / courseMetrics.length),
    averageAttendance: Math.round(analytics.reduce((s, a) => s + a.attendanceScore, 0) / analytics.length),
    averageAssessmentScore: Math.round(analytics.reduce((s, a) => s + a.assessmentScore, 0) / analytics.length),
    averageAssignmentScore: Math.round(analytics.reduce((s, a) => s + a.assignmentScore, 0) / analytics.length),
    certificationRate: Math.round(analytics.filter((a) => a.certificationReadiness >= 80).length / analytics.length * 100),
    averageLearningHours: Math.round(analytics.reduce((s, a) => s + a.learningHours, 0) / analytics.length),
    studentGrowthPercent: randomInt(5, 25),
    enrollmentTrend: months.map((m) => ({ month: m, count: randomInt(15, 40) })),
    completionTrend: months.map((m) => ({ month: m, count: randomInt(8, 30) })),
    recentActivity: months.slice(-3).map((m) => ({ date: m, event: 'Student Activity', count: randomInt(50, 200) })),
  };
}

function generateKPIs(exec: ExecutiveDashboard): KPIData[] {
  return [
    { label: 'Active Students', value: exec.activeStudents, change: 12, changeType: 'increase', icon: 'users', variant: 'info' },
    { label: 'Course Completion', value: `${exec.courseCompletionPercent}%`, change: 5, changeType: 'increase', icon: 'check-circle', variant: 'success' },
    { label: 'Average Attendance', value: `${exec.averageAttendance}%`, change: -2, changeType: 'decrease', icon: 'clock', variant: exec.averageAttendance >= 75 ? 'success' : 'warning' },
    { label: 'Assessment Score', value: `${exec.averageAssessmentScore}%`, change: 3, changeType: 'increase', icon: 'award', variant: 'info' },
    { label: 'Certification Rate', value: `${exec.certificationRate}%`, change: 8, changeType: 'increase', icon: 'shield', variant: 'success' },
    { label: 'Active Courses', value: exec.totalCourses, change: 0, changeType: 'neutral', icon: 'book', variant: 'default' },
    { label: 'Active Trainers', value: exec.activeTrainers, change: 2, changeType: 'increase', icon: 'user-check', variant: 'info' },
    { label: 'Learning Hours', value: exec.averageLearningHours, change: 15, changeType: 'increase', icon: 'clock', variant: 'default' },
  ];
}

function generateAcademicInsights(analytics: StudentAnalytics[]): AcademicInsights {
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  const sorted = [...analytics].sort((a, b) => (b.assessmentScore + b.attendanceScore) - (a.assessmentScore + a.attendanceScore));
  return {
    topStudents: sorted.slice(0, 5),
    highPerformers: sorted.filter((s) => s.performanceLevel === 'outstanding' || s.performanceLevel === 'excellent'),
    lowPerformers: sorted.filter((s) => s.performanceLevel === 'needs-attention' || s.performanceLevel === 'critical'),
    attendanceRisks: analytics.filter((a) => a.attendanceScore < 65).slice(0, 10),
    completionRisks: analytics.filter((a) => a.learningProgress < 40).slice(0, 10),
    achievementTrends: months.map((m) => ({ month: m, count: randomInt(5, 25) })),
    learningGaps: [
      { skill: 'Advanced JavaScript', studentsAffected: randomInt(5, 15) },
      { skill: 'Data Structures', studentsAffected: randomInt(3, 12) },
      { skill: 'Cloud Deployment', studentsAffected: randomInt(4, 10) },
      { skill: 'API Design', studentsAffected: randomInt(2, 8) },
    ],
  };
}

function generateLearningIntelligence(analytics: StudentAnalytics[]): LearningIntelligence {
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  return {
    studentTimeline: analytics.slice(0, 5).map((a) => ({
      studentId: a.studentId,
      studentName: a.studentName,
      events: Array.from({ length: 5 }, () => ({ date: randomDate(60), type: randomFrom(['lesson', 'assignment', 'assessment', 'achievement']), label: randomFrom(['Started Module', 'Submitted Project', 'Passed Exam', 'Earned Badge', 'Completed Course']) })),
    })),
    engagementAnalysis: analytics.slice(0, 10).map((a) => ({
      studentId: a.studentId,
      studentName: a.studentName,
      engagementScore: a.engagementScore,
      trend: randomFrom(['improving', 'stable', 'declining']),
    })),
    performanceAnalysis: analytics.slice(0, 10).map((a) => ({
      studentId: a.studentId,
      studentName: a.studentName,
      scores: [
        { category: 'Attendance', score: a.attendanceScore },
        { category: 'Assignments', score: a.assignmentScore },
        { category: 'Assessments', score: a.assessmentScore },
        { category: 'Progress', score: a.learningProgress },
        { category: 'Competency', score: a.competencyIndex },
      ],
    })),
    competencyAnalysis: [
      { category: 'Technical', averageLevel: randomInt(3, 5), maxLevel: 5 },
      { category: 'Soft Skills', averageLevel: randomInt(2, 4), maxLevel: 5 },
      { category: 'Domain Knowledge', averageLevel: randomInt(3, 4), maxLevel: 5 },
      { category: 'Practical', averageLevel: randomInt(2, 4), maxLevel: 5 },
    ],
    achievementAnalysis: [
      { type: 'Course Completed', count: randomInt(10, 25) },
      { type: 'Perfect Attendance', count: randomInt(5, 15) },
      { type: 'Top Performer', count: randomInt(3, 10) },
      { type: 'Outstanding Project', count: randomInt(4, 12) },
    ],
    learningHealth: [
      { metric: 'Attendance Health', value: randomInt(65, 95), status: randomInt(70, 95) >= 70 ? 'healthy' : 'warning' },
      { metric: 'Assignment Completion', value: randomInt(60, 90), status: randomInt(60, 90) >= 65 ? 'healthy' : 'warning' },
      { metric: 'Assessment Pass Rate', value: randomInt(65, 95), status: randomInt(65, 95) >= 70 ? 'healthy' : 'warning' },
      { metric: 'Competency Coverage', value: randomInt(40, 80), status: randomInt(40, 80) >= 50 ? 'warning' : 'critical' },
      { metric: 'Certification Readiness', value: randomInt(50, 85), status: randomInt(50, 85) >= 60 ? 'healthy' : 'warning' },
    ],
    completionForecast: months.map((m) => ({ month: m, predicted: randomInt(10, 30), actual: randomInt(8, 25) })),
    certificationForecast: months.map((m) => ({ month: m, predicted: randomInt(5, 20), actual: randomInt(3, 15) })),
  };
}

const studentAnalytics = generateStudentAnalytics();
const courseMetrics = generateCourseMetrics(studentAnalytics);
const trainerMetrics = generateTrainerMetrics();
const batchMetrics = generateBatchMetrics(studentAnalytics);
const executiveDashboard = generateExecutiveDashboard(studentAnalytics, courseMetrics, trainerMetrics, batchMetrics);
const kpis = generateKPIs(executiveDashboard);
const academicInsights = generateAcademicInsights(studentAnalytics);
const learningIntelligence = generateLearningIntelligence(studentAnalytics);

export function getStudentAnalytics(): StudentAnalytics[] { return studentAnalytics; }
export function getCourseMetrics(): CourseMetrics[] { return courseMetrics; }
export function getTrainerMetrics(): TrainerMetrics[] { return trainerMetrics; }
export function getBatchMetrics(): BatchMetrics[] { return batchMetrics; }
export function getExecutiveDashboard(): ExecutiveDashboard { return executiveDashboard; }
export function getKPIs(): KPIData[] { return kpis; }
export function getAcademicInsights(): AcademicInsights { return academicInsights; }
export function getLearningIntelligence(): LearningIntelligence { return learningIntelligence; }
export function getStudentById(id: string): StudentAnalytics | undefined { return studentAnalytics.find((s) => s.studentId === id); }
export function getCourseById(id: string): CourseMetrics | undefined { return courseMetrics.find((c) => c.courseId === id); }
export function getBatchById(id: string): BatchMetrics | undefined { return batchMetrics.find((b) => b.batchId === id); }
export function getTrainerById(id: string): TrainerMetrics | undefined { return trainerMetrics.find((t) => t.trainerId === id); }
