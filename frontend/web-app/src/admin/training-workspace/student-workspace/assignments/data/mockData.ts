import type { Assignment, Project, Submission, Evaluation, AssignmentTimelineEvent, AssignmentDashboardStats, AssignmentAnalytics, AssignmentStatus, AssignmentType, SubmissionStatus, ProjectType } from '../types';

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

const ASSIGNMENT_STATUSES: AssignmentStatus[] = ['draft', 'published', 'open', 'in-progress', 'submitted', 'late', 'under-review', 'reviewed', 'completed', 'archived', 'cancelled'];

const ASSIGNMENT_TYPES: AssignmentType[] = ['homework', 'project', 'case-study', 'lab-exercise', 'field-work', 'research', 'portfolio', 'presentation', 'group-assignment', 'capstone', 'ai-assignment'];

const PROJECT_TYPES: ProjectType[] = ['individual', 'team', 'research', 'commercial', 'field', 'laboratory', 'innovation', 'startup'];

function randomFrom<T>(arr: T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

function randomDate(startDaysAgo: number, endDaysAhead: number): string {
  const d = new Date();
  d.setDate(d.getDate() + Math.floor(Math.random() * (endDaysAhead + startDaysAgo)) - startDaysAgo);
  return d.toISOString().split('T')[0];
}

function generateAssignments(): Assignment[] {
  const titles = [
    'Build a REST API with Express', 'Create a React Dashboard', 'Database Schema Design',
    'Implement Authentication Flow', 'Deploy Microservices on AWS', 'Design System Component Library',
    'API Integration Testing', 'CI/CD Pipeline Setup', 'Mobile App Prototype',
    'Data Visualization Dashboard', 'Performance Optimization Report', 'Cloud Migration Strategy',
    'Container Orchestration with K8s', 'GraphQL API Implementation', 'Real-time Chat Application',
    'E-commerce Checkout Flow', 'Search Engine Implementation', 'Monitoring & Logging Setup',
    'Security Audit Report', 'Machine Learning Pipeline', 'IoT Data Collection System',
    'Blockchain Smart Contract', 'Progressive Web App', 'State Management Architecture',
    'Test Coverage Analysis', 'API Documentation Portal', 'Serverless Function Development',
    'Load Testing Strategy', 'Database Migration Plan', 'Code Review Automation',
    'Edge Computing Solution', 'Micro Frontend Architecture', 'Event-Driven Architecture',
  ];

  return titles.map((title, i) => {
    const batch = randomFrom(BATCHES);
    const course = randomFrom(COURSES);
    const mod = randomFrom(MODULES);
    const lesson = randomFrom(LESSONS);
    const status = randomFrom(ASSIGNMENT_STATUSES);
    const type = randomFrom(ASSIGNMENT_TYPES);
    return {
      id: `assgn-${i + 1}`,
      assignmentCode: `ASG-${String(i + 1).padStart(4, '0')}`,
      title,
      description: `Complete the ${title.toLowerCase()} assignment. Follow the guidelines and submit before the deadline. Ensure all requirements are met and the submission is thorough.`,
      courseId: course.id,
      courseName: course.name,
      moduleId: mod.id,
      moduleName: mod.name,
      lessonId: lesson.id,
      lessonName: lesson.name,
      category: course.name.split(' ')[0],
      assignmentType: type,
      difficulty: randomFrom(['beginner', 'intermediate', 'advanced', 'expert']),
      learningObjectives: ['Understand core concepts', 'Apply best practices', 'Implement production-ready code', 'Document architecture decisions'],
      estimatedDurationMinutes: Math.floor(Math.random() * 480) + 60,
      dueDate: randomDate(0, 30),
      status,
      createdBy: 'System Administrator',
      batchId: batch.id,
      batchName: batch.name,
      attachments: [],
      maxMarks: Math.floor(Math.random() * 50) + 50,
      passingMarks: 35,
      submissionType: randomFrom(['text', 'document', 'pdf', 'image', 'spreadsheet', 'presentation', 'video', 'git-repository', 'external-link', 'portfolio']),
      createdDate: randomDate(60, 0),
      lastUpdated: randomDate(7, 0),
    };
  });
}

function generateProjects(): Project[] {
  const titles = [
    'E-commerce Platform', 'Learning Management System', 'Inventory Management',
    'Customer Relationship Dashboard', 'Real-time Analytics Engine', 'Mobile Payment Gateway',
    'Healthcare Monitoring System', 'Smart Campus Portal', 'Supply Chain Tracker',
    'Social Media Analytics', 'Cloud Cost Optimizer', 'DevOps Automation Suite',
  ];

  return titles.map((title, i) => {
    const batch = randomFrom(BATCHES);
    const course = randomFrom(COURSES);
    return {
      id: `proj-${i + 1}`,
      projectCode: `PRJ-${String(i + 1).padStart(4, '0')}`,
      title,
      description: `Enterprise-grade ${title.toLowerCase()} project demonstrating full-stack development capabilities.`,
      projectType: randomFrom(PROJECT_TYPES),
      courseId: course.id,
      courseName: course.name,
      batchId: batch.id,
      batchName: batch.name,
      status: randomFrom(ASSIGNMENT_STATUSES),
      maxMarks: 100,
      passingMarks: 40,
      dueDate: randomDate(0, 60),
      teamSize: Math.random() > 0.5 ? Math.floor(Math.random() * 4) + 2 : null,
      members: [],
      createdDate: randomDate(90, 0),
      lastUpdated: randomDate(14, 0),
    };
  });
}

function generateSubmissions(assignments: Assignment[]): Submission[] {
  const submissions: Submission[] = [];
  const submittedStatuses: SubmissionStatus[] = ['submitted', 'late', 'under-review', 'reviewed', 'resubmitted', 'accepted', 'rejected'];

  assignments.slice(0, 25).forEach((assgn, _i) => {
    const count = Math.floor(Math.random() * 3) + 1;
    for (let j = 0; j < count; j++) {
      const student = randomFrom(STUDENTS);
      const isLate = Math.random() > 0.7;
      submissions.push({
        id: `sub-${submissions.length + 1}`,
        submissionCode: `SUB-${String(submissions.length + 1).padStart(4, '0')}`,
        assignmentId: assgn.id,
        assignmentTitle: assgn.title,
        studentId: student.id,
        studentName: student.name,
        batchId: assgn.batchId,
        batchName: assgn.batchName,
        courseId: assgn.courseId,
        courseName: assgn.courseName,
        submissionType: assgn.submissionType,
        content: `This is the submission for "${assgn.title}" by ${student.name}. The work covers all required objectives.`,
        attachments: [],
        externalLinks: [],
        status: isLate ? 'late' : randomFrom(submittedStatuses),
        submittedDate: isLate ? assgn.dueDate : randomDate(5, -5),
        wordCount: Math.floor(Math.random() * 2000) + 200,
        plagiarismScore: Math.random() > 0.8 ? Math.floor(Math.random() * 30) + 5 : null,
        createdDate: randomDate(30, 0),
        lastUpdated: randomDate(5, 0),
      });
    }
  });
  return submissions;
}

function generateEvaluations(submissions: Submission[]): Evaluation[] {
  return submissions.filter(s => s.status === 'under-review' || s.status === 'reviewed' || s.status === 'accepted' || s.status === 'rejected').map((sub, i) => {
    const scored = Math.floor(Math.random() * 40) + 60;
    return {
      id: `eval-${i + 1}`,
      submissionId: sub.id,
      assignmentId: sub.assignmentId,
      studentId: sub.studentId,
      studentName: sub.studentName,
      evaluatorId: 'eval-1',
      evaluatorName: 'Dr. Sunita Verma',
      maxMarks: 100,
      scoredMarks: scored,
      passingMarks: 35,
      grade: scored >= 85 ? 'A' : scored >= 70 ? 'B' : scored >= 50 ? 'C' : 'D',
      comments: 'Good work overall. Some areas need improvement in code structure and documentation.',
      suggestions: 'Consider using design patterns, add more unit tests, improve API documentation.',
      evaluationStatus: Math.random() > 0.3 ? 'completed' : 'in-progress',
      evaluatedAt: randomDate(10, 0),
      createdDate: randomDate(15, 0),
      lastUpdated: randomDate(5, 0),
    };
  });
}

function generateTimelines(assignments: Assignment[]): AssignmentTimelineEvent[] {
  const events: AssignmentTimelineEvent[] = [];
  assignments.forEach((assgn) => {
    const stages: { type: AssignmentTimelineEvent['type']; label: string; description: string; active: boolean }[] = [
      { type: 'created', label: 'Assignment Created', description: 'Assignment created by instructor', active: true },
      { type: 'published', label: 'Published', description: 'Assignment published to students', active: assgn.status !== 'draft' },
      { type: 'viewed', label: 'Viewed by Students', description: 'Students have viewed the assignment', active: ['open', 'in-progress', 'submitted', 'late', 'under-review', 'reviewed', 'completed'].includes(assgn.status) },
      { type: 'started', label: 'Work Started', description: 'Students have started working', active: ['in-progress', 'submitted', 'late', 'under-review', 'reviewed', 'completed'].includes(assgn.status) },
      { type: 'submitted', label: 'Submitted', description: 'Students have submitted their work', active: ['submitted', 'late', 'under-review', 'reviewed', 'completed'].includes(assgn.status) },
      { type: 'reviewed', label: 'Under Review', description: 'Submissions are being evaluated', active: ['under-review', 'reviewed', 'completed'].includes(assgn.status) },
      { type: 'completed', label: 'Completed', description: 'Assignment workflow completed', active: assgn.status === 'completed' },
    ];
    stages.forEach((stage, si) => {
      events.push({
        id: `tl-assgn-${assgn.id}-${si}`,
        assignmentId: assgn.id,
        type: stage.type,
        label: stage.label,
        description: stage.description,
        date: assgn.createdDate,
        completed: stage.active,
      });
    });
  });
  return events;
}

function generateDashboardStats(assignments: Assignment[], submissions: Submission[], projects: Project[]): AssignmentDashboardStats {
  const courseWiseMap: Record<string, number> = {};
  assignments.forEach((a) => { courseWiseMap[a.courseName] = (courseWiseMap[a.courseName] || 0) + 1; });

  const batchWiseMap: Record<string, number> = {};
  assignments.forEach((a) => { batchWiseMap[a.batchName] = (batchWiseMap[a.batchName] || 0) + 1; });

  const upcoming = assignments
    .filter((a) => a.status === 'open' || a.status === 'in-progress')
    .sort((a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime())
    .slice(0, 5)
    .map((a) => ({ id: a.id, title: a.title, dueDate: a.dueDate, batchName: a.batchName }));

  return {
    totalAssignments: assignments.length,
    activeAssignments: assignments.filter((a) => a.status === 'open' || a.status === 'in-progress').length,
    draftAssignments: assignments.filter((a) => a.status === 'draft').length,
    completedAssignments: assignments.filter((a) => a.status === 'completed').length,
    pendingSubmissions: submissions.filter((s) => s.status === 'submitted' || s.status === 'late').length,
    lateSubmissions: submissions.filter((s) => s.status === 'late').length,
    totalProjects: projects.length,
    practicalTasks: assignments.filter((a) => a.assignmentType === 'lab-exercise' || a.assignmentType === 'field-work').length,
    courseWise: Object.entries(courseWiseMap).map(([course, count]) => ({ course, count })),
    batchWise: Object.entries(batchWiseMap).map(([batch, count]) => ({ batch, count })),
    upcomingDeadlines: upcoming,
  };
}

function generateAnalytics(assignments: Assignment[], submissions: Submission[]): AssignmentAnalytics {
  const total = assignments.length;
  const submitted = submissions.length;
  const completed = assignments.filter((a) => a.status === 'completed').length;
  const late = submissions.filter((s) => s.status === 'late').length;

  const typeMap: Record<string, number> = {};
  assignments.forEach((a) => { typeMap[a.assignmentType] = (typeMap[a.assignmentType] || 0) + 1; });

  const courseMap: Record<string, number> = {};
  assignments.forEach((a) => { courseMap[a.courseName] = (courseMap[a.courseName] || 0) + 1; });

  const batchMap: Record<string, number> = {};
  assignments.forEach((a) => { batchMap[a.batchName] = (batchMap[a.batchName] || 0) + 1; });

  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  const monthlyTrend = months.map((month) => ({
    month,
    count: Math.floor(Math.random() * 8) + 2,
  }));

  return {
    totalAssignments: total,
    submissionRate: total > 0 ? Math.round((submitted / total) * 100) : 0,
    completionRate: total > 0 ? Math.round((completed / total) * 100) : 0,
    lateSubmissionPercent: submitted > 0 ? Math.round((late / submitted) * 100) : 0,
    averageCompletionTimeDays: Math.floor(Math.random() * 7) + 3,
    assignmentDistribution: Object.entries(typeMap).map(([type, count]) => ({ type: type as AssignmentType, count })),
    courseWise: Object.entries(courseMap).map(([course, count]) => ({ course, count })),
    batchWise: Object.entries(batchMap).map(([batch, count]) => ({ batch, count })),
    monthlyTrend,
  };
}

const assignments = generateAssignments();
const projects = generateProjects();
const submissions = generateSubmissions(assignments);
const evaluations = generateEvaluations(submissions);
const timelineEvents = generateTimelines(assignments);
const dashboardStats = generateDashboardStats(assignments, submissions, projects);
const analytics = generateAnalytics(assignments, submissions);

export function getAssignments(): Assignment[] { return assignments; }
export function getProjects(): Project[] { return projects; }
export function getSubmissions(): Submission[] { return submissions; }
export function getEvaluations(): Evaluation[] { return evaluations; }
export function getTimelineEvents(): AssignmentTimelineEvent[] { return timelineEvents; }
export function getDashboardStats(): AssignmentDashboardStats { return dashboardStats; }
export function getAnalytics(): AssignmentAnalytics { return analytics; }
export function getCourses() { return COURSES; }
export function getBatches() { return BATCHES; }
export function getStudents() { return STUDENTS; }
