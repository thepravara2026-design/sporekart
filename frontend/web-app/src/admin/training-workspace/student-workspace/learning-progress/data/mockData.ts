import type { LearningProgress, Competency, SkillMatrix, Milestone, LearningTimelineEvent, CertificationReadiness, LearningHealthDashboard, LearningAnalytics, ProgressStatus, CompetencyCategory, SkillCategory } from '../types';

const STUDENTS = Array.from({ length: 25 }, (_, i) => ({ id: `stu-${i + 1}`, name: ['Aarav Sharma','Priya Patel','Rahul Singh','Ananya Gupta','Vikram Joshi','Neha Kapoor','Arjun Mehta','Kavita Reddy','Rohan Desai','Ishita Verma','Amit Kumar','Sneha Agarwal','Deepak Tiwari','Pooja Nair','Karan Malhotra','Divya Bhat','Suresh Iyer','Meera Choudhury','Nitin Saxena','Lakshmi Rajan','Rajesh Kumar','Anjali Sinha','Vivek Mishra','Pallavi Rao','Aditya Khanna'][i] }));

const COURSES = [
  { id: 'course-1', name: 'Full Stack Web Development' },
  { id: 'course-2', name: 'Data Science & Analytics' },
  { id: 'course-3', name: 'Cloud Architecture' },
  { id: 'course-4', name: 'Mobile App Development' },
  { id: 'course-5', name: 'DevOps Engineering' },
  { id: 'course-6', name: 'UI/UX Design' },
];

const MODULES = [
  { id: 'mod-1', name: 'Frontend Fundamentals' },
  { id: 'mod-2', name: 'Backend Development' },
  { id: 'mod-3', name: 'Database Management' },
  { id: 'mod-4', name: 'Data Structures' },
  { id: 'mod-5', name: 'Cloud Services' },
];

const LESSONS = [
  { id: 'lesson-1', name: 'HTML & CSS Basics' },
  { id: 'lesson-2', name: 'JavaScript Fundamentals' },
  { id: 'lesson-3', name: 'React Components' },
  { id: 'lesson-4', name: 'Node.js APIs' },
  { id: 'lesson-5', name: 'SQL Queries' },
];

function randomFrom<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randomDate(daysAgo: number): string {
  const d = new Date(); d.setDate(d.getDate() - Math.floor(Math.random() * daysAgo));
  return d.toISOString().split('T')[0];
}

function generateProgress(): LearningProgress[] {
  const statuses: ProgressStatus[] = ['not-started', 'started', 'in-progress', 'paused', 'behind-schedule', 'on-track', 'completed', 'certified'];
  const stages = ['orientation', 'foundation', 'intermediate', 'advanced', 'practical', 'field-work', 'assessment', 'certification', 'completed'] as const;
  return STUDENTS.flatMap((stu) => {
    const course = randomFrom(COURSES);
    const mod = randomFrom(MODULES);
    const lesson = randomFrom(LESSONS);
    const status = randomFrom(statuses);
    const pct = status === 'not-started' ? 0 : status === 'started' ? Math.floor(Math.random() * 15) + 5 : status === 'completed' || status === 'certified' ? 100 : Math.floor(Math.random() * 70) + 15;
    return {
      id: `prog-${stu.id}-${course.id}`,
      progressCode: `PRG-${stu.id.toUpperCase()}-${course.id.toUpperCase()}`,
      studentId: stu.id, studentName: stu.name,
      enrollmentId: `enr-${stu.id}-${course.id}`,
      courseId: course.id, courseName: course.name,
      moduleId: mod.id, moduleName: mod.name,
      lessonId: lesson.id, lessonName: lesson.name,
      progressPercent: pct,
      status,
      currentStage: status === 'completed' || status === 'certified' ? 'completed' : randomFrom(stages as unknown as any[]),
      estimatedCompletionDate: randomDate(-60),
      learningHours: Math.floor(Math.random() * 120) + 5,
      lastActivityDate: randomDate(7),
      assignmentStatus: pct >= 80 ? 'completed' : pct >= 40 ? 'in-progress' : 'pending',
      assessmentStatus: pct >= 80 ? 'completed' : pct >= 40 ? 'in-progress' : 'pending',
      attendanceStatus: pct >= 75 ? 'on-track' : pct >= 50 ? 'at-risk' : 'critical',
      competencyStatus: pct >= 80 ? 'advanced' : pct >= 50 ? 'developing' : 'foundation',
      createdDate: randomDate(120),
      lastUpdated: randomDate(7),
    };
  });
}

function generateCompetencies(progress: LearningProgress[]): Competency[] {
  const cats: CompetencyCategory[] = ['knowledge', 'practical-skills', 'business-skills', 'communication', 'problem-solving', 'leadership', 'critical-thinking', 'innovation', 'industry-readiness'];
  return progress.slice(0, 60).flatMap((p) => {
    return cats.slice(0, Math.floor(Math.random() * 4) + 2).map((cat, i) => ({
      id: `comp-${p.id}-${i}`,
      competencyCode: `COMP-${String(i + 1).padStart(3, '0')}`,
      studentId: p.studentId, studentName: p.studentName,
      category: cat,
      name: `${cat.split('-').map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')}`,
      description: `Demonstrated ${cat.replace('-', ' ')} competency through course activities`,
      level: Math.floor(Math.random() * 5) + 1, maxLevel: 5,
      percentage: Math.floor(Math.random() * 60) + 40,
      status: Math.random() > 0.4 ? (Math.random() > 0.5 ? 'achieved' : 'mastered') : 'in-progress',
      earnedDate: Math.random() > 0.3 ? randomDate(60) : null,
      courseId: p.courseId, courseName: p.courseName,
    }));
  });
}

function generateSkillMatrices(progress: LearningProgress[]): SkillMatrix[] {
  const cats: SkillCategory[] = ['technical', 'soft', 'industry', 'agriculture', 'business', 'certification'];
  return progress.slice(0, 30).map((p) => ({
    id: `skill-${p.id}`,
    studentId: p.studentId, studentName: p.studentName,
    category: randomFrom(cats),
    skills: Array.from({ length: Math.floor(Math.random() * 4) + 3 }, (_, i) => ({
      name: ['JavaScript','React','Node.js','Python','AWS','Docker','Git','SQL','TypeScript','CSS'][i],
      level: Math.floor(Math.random() * 4) + 1, maxLevel: 5,
    })),
    overallPercentage: Math.floor(Math.random() * 60) + 40,
  }));
}

function generateMilestones(progress: LearningProgress[]): Milestone[] {
  const types = ['course-started', 'progress-25', 'progress-50', 'progress-75', 'progress-100', 'assignment-milestone', 'assessment-milestone', 'attendance-milestone', 'competency-milestone', 'certification-ready', 'completed'] as const;
  return progress.slice(0, 40).flatMap((p) => {
    const maxTypes = Math.floor(Math.random() * 5) + 2;
    return types.slice(0, maxTypes).map((type, i) => ({
      id: `mile-${p.id}-${i}`,
      milestoneCode: `MIL-${String(i + 1).padStart(3, '0')}`,
      studentId: p.studentId, studentName: p.studentName,
      courseId: p.courseId, courseName: p.courseName,
      type,
      label: type.split('-').map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join(' '),
      description: `Achieved ${type.replace('-', ' ')} milestone`,
      achieved: Math.random() > 0.3,
      achievedDate: Math.random() > 0.3 ? randomDate(60) : null,
      progressPercent: type === 'course-started' ? 0 : type === 'progress-25' ? 25 : type === 'progress-50' ? 50 : type === 'progress-75' ? 75 : type === 'progress-100' ? 100 : Math.floor(Math.random() * 80) + 20,
    }));
  });
}

function generateTimeline(): LearningTimelineEvent[] {
  const types: LearningTimelineEvent['type'][] = ['enrollment', 'lesson-started', 'lesson-completed', 'assignment-submitted', 'assessment-passed', 'competency-achieved', 'milestone-earned', 'certification-eligible', 'course-completed'];
  return STUDENTS.slice(0, 15).flatMap((stu) => types.map((type, i) => ({
    id: `tl-${stu.id}-${i}`,
    studentId: stu.id,
    type,
    label: type.split('-').map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join(' '),
    description: `${stu.name} completed ${type.replace('-', ' ')}`,
    date: randomDate(90),
    completed: Math.random() > 0.2,
  })));
}

function generateCertificationReadiness(progress: LearningProgress[]): CertificationReadiness[] {
  return progress.slice(0, 30).map((p, i) => {
    const attPct = Math.floor(Math.random() * 30) + 70;
    const assnPct = Math.floor(Math.random() * 30) + 70;
    const asmtScore = Math.floor(Math.random() * 30) + 70;
    const compCount = Math.floor(Math.random() * 4) + 2;
    const pending: string[] = [];
    if (attPct < 80) pending.push('Attendance minimum 80%');
    if (assnPct < 80) pending.push('Assignment submission 80%');
    if (asmtScore < 70) pending.push('Assessment score minimum 70%');
    const overall = Math.round((attPct + assnPct + asmtScore + (compCount / 5) * 100) / 4);
    return {
      id: `cert-ready-${i}`,
      studentId: p.studentId, studentName: p.studentName,
      courseId: p.courseId, courseName: p.courseName,
      batchId: `batch-${(i % 3) + 1}`, batchName: ['Morning Batch','Evening Batch','Weekend Batch'][i % 3],
      attendancePercent: attPct, attendanceRequirement: 80,
      assignmentCompletion: assnPct, assignmentRequirement: 80,
      assessmentScore: asmtScore, assessmentRequirement: 70,
      competencyCount: compCount, competencyRequirement: 3,
      overallEligibilityPercent: overall,
      isReady: pending.length === 0,
      pendingRequirements: pending,
    };
  });
}

function generateHealthDashboard(progress: LearningProgress[]): LearningHealthDashboard {
  const total = progress.length;
  const onTrack = progress.filter((p) => p.status === 'on-track' || p.status === 'completed' || p.status === 'certified').length;
  const behind = progress.filter((p) => p.status === 'behind-schedule' || p.status === 'paused').length;
  const avg = Math.round(progress.reduce((s, p) => s + p.progressPercent, 0) / total);
  const completed = progress.filter((p) => p.status === 'completed' || p.status === 'certified').length;
  const hours = progress.reduce((s, p) => s + p.learningHours, 0);
  const atRisk = progress.filter((p) => p.status === 'behind-schedule').length;
  return {
    totalStudents: total, studentsOnTrack: onTrack, studentsBehind: behind,
    averageProgress: avg, completionPercent: Math.round((completed / total) * 100),
    attendanceHealth: 78, assignmentCompletion: 82, assessmentCompletion: 74,
    competencyCoverage: 65, totalLearningHours: hours, atRiskCount: atRisk,
  };
}

function generateAnalytics(progress: LearningProgress[]): LearningAnalytics {
  const completed = progress.filter((p) => p.status === 'completed' || p.status === 'certified');
  const months = ['Jan','Feb','Mar','Apr','May','Jun'];
  const ranges = ['0-25%','26-50%','51-75%','76-100%'];
  return {
    courseCompletionPercent: Math.round((completed.length / progress.length) * 100),
    moduleCompletionPercent: Math.floor(Math.random() * 30) + 60,
    lessonCompletionPercent: Math.floor(Math.random() * 25) + 65,
    averageLearningHours: Math.round(progress.reduce((s, p) => s + p.learningHours, 0) / progress.length),
    assessmentSuccessRate: Math.floor(Math.random() * 15) + 75,
    assignmentSuccessRate: Math.floor(Math.random() * 15) + 78,
    competencyGrowth: Math.floor(Math.random() * 20) + 60,
    monthlyActiveStudents: months.map((m) => ({ month: m, count: Math.floor(Math.random() * 20) + 5 })),
    progressDistribution: ranges.map((r) => ({ range: r, count: Math.floor(Math.random() * 15) + 5 })),
  };
}

const progress = generateProgress();
const competencies = generateCompetencies(progress);
const skillMatrices = generateSkillMatrices(progress);
const milestones = generateMilestones(progress);
const timelineEvents = generateTimeline();
const certificationReadiness = generateCertificationReadiness(progress);
const healthDashboard = generateHealthDashboard(progress);
const analytics = generateAnalytics(progress);

export function getProgress(): LearningProgress[] { return progress; }
export function getCompetencies(): Competency[] { return competencies; }
export function getSkillMatrices(): SkillMatrix[] { return skillMatrices; }
export function getMilestones(): Milestone[] { return milestones; }
export function getTimelineEvents(): LearningTimelineEvent[] { return timelineEvents; }
export function getCertificationReadiness(): CertificationReadiness[] { return certificationReadiness; }
export function getHealthDashboard(): LearningHealthDashboard { return healthDashboard; }
export function getAnalytics(): LearningAnalytics { return analytics; }
