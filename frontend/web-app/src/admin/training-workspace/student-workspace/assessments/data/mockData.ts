import type { Assessment, QuestionCategory, Result, AssessmentTimelineEvent, AssessmentDashboardStats, AcademicAnalytics, AssessmentStatus, AssessmentType, Difficulty } from '../types';

const BATCHES = [
  { id: 'batch-1', name: 'Morning Batch 2026' },
  { id: 'batch-2', name: 'Evening Batch 2026' },
  { id: 'batch-3', name: 'Weekend Batch 2026' },
  { id: 'batch-4', name: 'Corporate Batch Q1' },
  { id: 'batch-5', name: 'Online Batch 2026' },
  { id: 'batch-6', name: 'Government Scheme Batch' },
];

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
  { id: 'mod-6', name: 'Testing & QA' },
];

const LESSONS = [
  { id: 'lesson-1', name: 'HTML & CSS Basics' },
  { id: 'lesson-2', name: 'JavaScript Fundamentals' },
  { id: 'lesson-3', name: 'React Components' },
  { id: 'lesson-4', name: 'Node.js APIs' },
  { id: 'lesson-5', name: 'SQL Queries' },
  { id: 'lesson-6', name: 'Docker Basics' },
];

const STUDENTS = [
  { id: 'stu-1', name: 'Aarav Sharma' },
  { id: 'stu-2', name: 'Priya Patel' },
  { id: 'stu-3', name: 'Rahul Singh' },
  { id: 'stu-4', name: 'Ananya Gupta' },
  { id: 'stu-5', name: 'Vikram Joshi' },
  { id: 'stu-6', name: 'Neha Kapoor' },
  { id: 'stu-7', name: 'Arjun Mehta' },
  { id: 'stu-8', name: 'Kavita Reddy' },
  { id: 'stu-9', name: 'Rohan Desai' },
  { id: 'stu-10', name: 'Ishita Verma' },
  { id: 'stu-11', name: 'Amit Kumar' },
  { id: 'stu-12', name: 'Sneha Agarwal' },
  { id: 'stu-13', name: 'Deepak Tiwari' },
  { id: 'stu-14', name: 'Pooja Nair' },
  { id: 'stu-15', name: 'Karan Malhotra' },
  { id: 'stu-16', name: 'Divya Bhat' },
  { id: 'stu-17', name: 'Suresh Iyer' },
  { id: 'stu-18', name: 'Meera Choudhury' },
  { id: 'stu-19', name: 'Nitin Saxena' },
  { id: 'stu-20', name: 'Lakshmi Rajan' },
];

const ASSESSMENT_STATUSES: AssessmentStatus[] = ['draft', 'scheduled', 'published', 'open', 'active', 'paused', 'completed', 'under-evaluation', 'evaluated', 'archived', 'cancelled'];

const ASSESSMENT_TYPES: AssessmentType[] = ['quiz', 'objective-test', 'subjective-test', 'practical-assessment', 'laboratory-assessment', 'field-assessment', 'case-study-evaluation', 'project-evaluation', 'assignment-evaluation', 'oral-viva', 'interview', 'mock-test', 'practice-assessment', 'final-examination', 'certification-assessment', 'corporate-skill-assessment', 'government-assessment', 'ai-adaptive-assessment'];

function randomFrom<T>(arr: T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

function randomDate(startDaysAgo: number, endDaysAhead: number): string {
  const d = new Date();
  d.setDate(d.getDate() + Math.floor(Math.random() * (endDaysAhead + startDaysAgo)) - startDaysAgo);
  return d.toISOString().split('T')[0];
}

function generateAssessments(): Assessment[] {
  const titles = [
    'JavaScript Fundamentals Quiz', 'React Component Assessment', 'Node.js API Test',
    'Database Design Practical', 'Cloud Architecture Exam', 'UI/UX Design Evaluation',
    'Data Structures Final', 'DevOps Pipeline Assessment', 'Mobile App Practical',
    'Full Stack Capstone', 'Security Best Practices Quiz', 'Agile Methodology Test',
    'System Design Interview', 'Git Workflow Assessment', 'Docker & K8s Practical',
    'SQL Query Optimization', 'REST API Design Exam', 'Testing Strategies Quiz',
    'Performance Tuning Assessment', 'CI/CD Pipeline Practical', 'Microservices Architecture',
    'GraphQL Implementation', 'State Management Quiz', 'TypeScript Fundamentals',
    'Web Accessibility Exam', 'Testing Automation Assessment', 'Monitoring & Logging',
    'Event-Driven Architecture', 'API Security Assessment', 'Coding Standards Quiz',
  ];

  return titles.map((title, i) => {
    const batch = randomFrom(BATCHES);
    const course = randomFrom(COURSES);
    const mod = randomFrom(MODULES);
    const lesson = randomFrom(LESSONS);
    const type = randomFrom(ASSESSMENT_TYPES);
    const status = randomFrom(ASSESSMENT_STATUSES);
    const qCount = Math.floor(Math.random() * 40) + 10;
    return {
      id: `assess-${i + 1}`,
      assessmentCode: `ASMT-${String(i + 1).padStart(4, '0')}`,
      title,
      description: `Comprehensive ${title.toLowerCase()} covering all key concepts and practical applications.`,
      courseId: course.id,
      courseName: course.name,
      moduleId: mod.id,
      moduleName: mod.name,
      lessonId: lesson.id,
      lessonName: lesson.name,
      assessmentType: type,
      difficulty: randomFrom(['beginner', 'intermediate', 'advanced', 'expert']),
      durationMinutes: Math.floor(Math.random() * 120) + 30,
      maxMarks: qCount,
      passingMarks: Math.ceil(qCount * 0.4),
      totalQuestions: qCount,
      attemptLimit: Math.random() > 0.7 ? Math.floor(Math.random() * 3) + 1 : 1,
      evaluationMethod: randomFrom(['auto-graded', 'manual-graded', 'hybrid', 'ai-evaluated']),
      availabilityStart: randomDate(30, 0),
      availabilityEnd: randomDate(0, 60),
      batchId: batch.id,
      batchName: batch.name,
      status,
      instructions: 'Read each question carefully. You can navigate between questions freely. Submit only when you are ready.',
      createdDate: randomDate(60, 0),
      lastUpdated: randomDate(7, 0),
    };
  });
}

function generateQuestionCategories(): QuestionCategory[] {
  const categories = [
    { name: 'JavaScript', desc: 'Core JavaScript concepts and ES6+ features', outcomes: ['Understand closures', 'Master async/await', 'Implement design patterns'] },
    { name: 'React', desc: 'React component architecture and hooks', outcomes: ['Build reusable components', 'Manage state effectively', 'Optimize performance'] },
    { name: 'Node.js', desc: 'Server-side JavaScript runtime', outcomes: ['Create REST APIs', 'Handle middleware', 'Manage databases'] },
    { name: 'Databases', desc: 'SQL and NoSQL database design', outcomes: ['Write complex queries', 'Design schemas', 'Optimize indexes'] },
    { name: 'Cloud Services', desc: 'AWS, Azure and GCP fundamentals', outcomes: ['Deploy applications', 'Manage infrastructure', 'Implement security'] },
    { name: 'DevOps', desc: 'CI/CD, Docker, and orchestration', outcomes: ['Set up pipelines', 'Containerize apps', 'Monitor systems'] },
    { name: 'Data Structures', desc: 'Algorithms and data structures', outcomes: ['Solve complex problems', 'Optimize time/space', 'Implement sort/search'] },
    { name: 'System Design', desc: 'Distributed system architecture', outcomes: ['Design scalable systems', 'Handle concurrency', 'Plan capacity'] },
    { name: 'Security', desc: 'Application and network security', outcomes: ['Implement authentication', 'Prevent vulnerabilities', 'Secure APIs'] },
    { name: 'Testing', desc: 'Automated testing strategies', outcomes: ['Write unit tests', 'Integration testing', 'E2E test automation'] },
  ];

  return categories.map((cat, i) => ({
    id: `qcat-${i + 1}`,
    name: cat.name,
    description: cat.desc,
    difficulty: randomFrom(['beginner', 'intermediate', 'advanced', 'expert']),
    learningOutcomes: cat.outcomes,
    tags: [cat.name.toLowerCase(), 'assessment', 'evaluation'],
    questionCount: Math.floor(Math.random() * 50) + 10,
    totalMarks: Math.floor(Math.random() * 100) + 50,
  }));
}

function generateResults(assessments: Assessment[]): Result[] {
  const results: Result[] = [];
  assessments.slice(0, 20).forEach((assess) => {
    const count = Math.floor(Math.random() * 8) + 3;
    for (let j = 0; j < count; j++) {
      const student = randomFrom(STUDENTS);
      const scored = Math.floor(Math.random() * assess.maxMarks);
      const pct = Math.round((scored / assess.maxMarks) * 100);
      const passed = scored >= assess.passingMarks;
      results.push({
        id: `res-${results.length + 1}`,
        resultCode: `RES-${String(results.length + 1).padStart(4, '0')}`,
        assessmentId: assess.id,
        assessmentTitle: assess.title,
        studentId: student.id,
        studentName: student.name,
        batchId: assess.batchId,
        batchName: assess.batchName,
        courseId: assess.courseId,
        courseName: assess.courseName,
        maxMarks: assess.maxMarks,
        passingMarks: assess.passingMarks,
        scoredMarks: scored,
        percentage: pct,
        grade: pct >= 85 ? 'A' : pct >= 70 ? 'B' : pct >= 55 ? 'C' : pct >= 40 ? 'D' : 'F',
        resultStatus: passed ? 'pass' : 'fail',
        performanceBand: pct >= 85 ? 'excellent' : pct >= 70 ? 'good' : pct >= 55 ? 'average' : pct >= 40 ? 'below-average' : 'poor',
        competencyLevel: pct >= 85 ? 'expert' : pct >= 70 ? 'proficient' : pct >= 55 ? 'competent' : pct >= 40 ? 'beginner' : 'novice',
        attemptNumber: Math.floor(Math.random() * 2) + 1,
        totalAttempts: assess.attemptLimit,
        startedAt: randomDate(15, 0),
        submittedAt: randomDate(10, 0),
        evaluatedAt: randomDate(5, 0),
        timeTakenMinutes: Math.floor(Math.random() * assess.durationMinutes) + 10,
      });
    }
  });
  return results;
}

function generateTimelines(assessments: Assessment[]): AssessmentTimelineEvent[] {
  const events: AssessmentTimelineEvent[] = [];
  assessments.forEach((assess) => {
    const stages: { type: AssessmentTimelineEvent['type']; label: string; description: string; active: boolean }[] = [
      { type: 'created', label: 'Assessment Created', description: 'Assessment created by instructor', active: true },
      { type: 'published', label: 'Published', description: 'Assessment published to students', active: assess.status !== 'draft' },
      { type: 'scheduled', label: 'Scheduled', description: 'Assessment scheduled for a date', active: ['scheduled', 'open', 'active', 'completed', 'under-evaluation', 'evaluated'].includes(assess.status) },
      { type: 'attempt-started', label: 'Attempt Started', description: 'Students have started the assessment', active: ['active', 'completed', 'under-evaluation', 'evaluated'].includes(assess.status) },
      { type: 'submitted', label: 'Submitted', description: 'Students have submitted their responses', active: ['completed', 'under-evaluation', 'evaluated'].includes(assess.status) },
      { type: 'evaluated', label: 'Evaluated', description: 'Responses have been evaluated', active: assess.status === 'evaluated' },
      { type: 'result-generated', label: 'Result Generated', description: 'Results have been published', active: assess.status === 'evaluated' },
      { type: 'certificate-eligible', label: 'Certificate Eligible', description: 'Students are eligible for certification', active: false },
    ];
    stages.forEach((stage, si) => {
      events.push({
        id: `tl-assess-${assess.id}-${si}`,
        assessmentId: assess.id,
        type: stage.type,
        label: stage.label,
        description: stage.description,
        date: assess.createdDate,
        completed: stage.active,
      });
    });
  });
  return events;
}

function generateDashboardStats(assessments: Assessment[], results: Result[]): AssessmentDashboardStats {
  const upcoming = assessments.filter((a) => a.status === 'scheduled' || a.status === 'published' || a.status === 'open').length;
  const completed = assessments.filter((a) => a.status === 'completed' || a.status === 'evaluated').length;
  const courseMap: Record<string, number[]> = {};
  results.forEach((r) => {
    if (!courseMap[r.courseName]) courseMap[r.courseName] = [];
    courseMap[r.courseName].push(r.percentage);
  });
  const batchMap: Record<string, number[]> = {};
  results.forEach((r) => {
    if (!batchMap[r.batchName]) batchMap[r.batchName] = [];
    batchMap[r.batchName].push(r.percentage);
  });
  const scores = results.map((r) => r.percentage);
  const avg = scores.length > 0 ? Math.round(scores.reduce((a, b) => a + b, 0) / scores.length) : 0;
  const high = scores.length > 0 ? Math.max(...scores) : 0;
  const low = scores.length > 0 ? Math.min(...scores) : 0;
  const passed = results.filter((r) => r.resultStatus === 'pass').length;
  const total = results.length;
  const passPct = total > 0 ? Math.round((passed / total) * 100) : 0;
  return {
    totalAssessments: assessments.length,
    upcomingAssessments: upcoming,
    completedAssessments: completed,
    averageScore: avg,
    highestScore: high,
    lowestScore: low,
    passPercent: passPct,
    failPercent: 100 - passPct,
    coursePerformance: Object.entries(courseMap).map(([course, scs]) => ({ course, avgScore: Math.round(scs.reduce((a, b) => a + b, 0) / scs.length) })),
    batchPerformance: Object.entries(batchMap).map(([batch, scs]) => ({ batch, avgScore: Math.round(scs.reduce((a, b) => a + b, 0) / scs.length) })),
    recentResults: results.slice(-10).reverse(),
  };
}

function generateAnalytics(assessments: Assessment[], results: Result[]): AcademicAnalytics {
  const upcoming = assessments.filter((a) => a.status === 'scheduled' || a.status === 'published' || a.status === 'open').length;
  const completed = assessments.filter((a) => a.status === 'completed' || a.status === 'evaluated').length;
  const scores = results.map((r) => r.percentage);
  const avg = scores.length > 0 ? Math.round(scores.reduce((a, b) => a + b, 0) / scores.length) : 0;
  const high = scores.length > 0 ? Math.max(...scores) : 0;
  const low = scores.length > 0 ? Math.min(...scores) : 0;
  const passed = results.filter((r) => r.resultStatus === 'pass').length;
  const total = results.length;
  const passPct = total > 0 ? Math.round((passed / total) * 100) : 0;
  const courseMap: Record<string, number[]> = {};
  results.forEach((r) => {
    if (!courseMap[r.courseName]) courseMap[r.courseName] = [];
    courseMap[r.courseName].push(r.percentage);
  });
  const batchMap: Record<string, number[]> = {};
  results.forEach((r) => {
    if (!batchMap[r.batchName]) batchMap[r.batchName] = [];
    batchMap[r.batchName].push(r.percentage);
  });
  const diffMap: Record<string, number> = {};
  assessments.forEach((a) => { diffMap[a.difficulty] = (diffMap[a.difficulty] || 0) + 1; });
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const monthlyTrend = months.slice(0, 6).map((month) => ({ month, count: Math.floor(Math.random() * 5) + 1 }));
  const compPct = assessments.length > 0 ? Math.round((completed / assessments.length) * 100) : 0;
  return {
    totalAssessments: assessments.length,
    upcomingAssessments: upcoming,
    completedAssessments: completed,
    averageScore: avg,
    highestScore: high,
    lowestScore: low,
    passPercent: passPct,
    failPercent: 100 - passPct,
    coursePerformance: Object.entries(courseMap).map(([course, scs]) => ({ course, avgScore: Math.round(scs.reduce((a, b) => a + b, 0) / scs.length) })),
    batchPerformance: Object.entries(batchMap).map(([batch, scs]) => ({ batch, avgScore: Math.round(scs.reduce((a, b) => a + b, 0) / scs.length) })),
    questionDifficultyDistribution: Object.entries(diffMap).map(([difficulty, count]) => ({ difficulty: difficulty as Difficulty, count })),
    assessmentCompletionPercent: compPct,
    monthlyTrend,
  };
}

const assessments = generateAssessments();
const questionCategories = generateQuestionCategories();
const results = generateResults(assessments);
const timelineEvents = generateTimelines(assessments);
const dashboardStats = generateDashboardStats(assessments, results);
const analytics = generateAnalytics(assessments, results);

export function getAssessments(): Assessment[] { return assessments; }
export function getQuestionCategories(): QuestionCategory[] { return questionCategories; }
export function getResults(): Result[] { return results; }
export function getTimelineEvents(): AssessmentTimelineEvent[] { return timelineEvents; }
export function getDashboardStats(): AssessmentDashboardStats { return dashboardStats; }
export function getAnalytics(): AcademicAnalytics { return analytics; }
