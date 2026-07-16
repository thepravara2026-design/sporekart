import { MOCK_COURSES, type Course, type TrainingCategory, type CourseLevel, type DeliveryMode } from '../../courses/data/courseMockData';
import { CATEGORY_LABELS, LEVEL_LABELS, DELIVERY_LABELS } from '../../courses/data/courseMockData';

// ---------------------------------------------------------------------------
// Analytics Foundation — Mock Data Layer (simulated, deterministic datasets)
// Single source of truth for all dashboards. No backend / API / DB.
// ---------------------------------------------------------------------------

function seeded(seed: number): () => number {
  let s = seed % 2147483647;
  if (s <= 0) s += 2147483646;
  return () => {
    s = (s * 16807) % 2147483647;
    return (s - 1) / 2147483646;
  };
}

export interface ExecutiveKpis {
  totalCourses: number;
  publishedCourses: number;
  activeCourses: number;
  archivedCourses: number;
  enrollmentRequests: number;
  approvedEnrollments: number;
  pendingEnrollments: number;
  revenuePlaceholder: number;
  trainingCapacity: number;
  upcomingTrainings: number;
  completedTrainings: number;
  activeTrainersPlaceholder: number;
  activeStudentsPlaceholder: number;
  certificatesPlaceholder: number;
  learningHoursPlaceholder: number;
}

export interface TimePoint {
  label: string;
  enrollments: number;
  completions: number;
  revenue: number;
}

export interface DistributionSlice {
  label: string;
  value: number;
  color?: string;
}

export interface DashboardMetric {
  id: string;
  title: string;
  value: number | string;
  unit?: string;
  trend?: 'up' | 'down' | 'flat';
  trendValue?: string;
  format?: 'number' | 'currency' | 'percent';
  drillRoute?: string;
}

const VIZ = [
  'var(--color-data-viz-1)', 'var(--color-data-viz-2)', 'var(--color-data-viz-3)',
  'var(--color-data-viz-4)', 'var(--color-data-viz-5)', 'var(--color-data-viz-6)',
  'var(--color-data-viz-7)', 'var(--color-data-viz-8)',
];

export function computeExecutiveKpis(courses: Course[] = MOCK_COURSES): ExecutiveKpis {
  const totalCourses = courses.length;
  const publishedCourses = courses.filter((c) => c.lifecycle === 'published').length;
  const activeCourses = courses.filter((c) => ['published', 'scheduled'].includes(c.lifecycle)).length;
  const archivedCourses = courses.filter((c) => c.lifecycle === 'archived').length;
  const enrollmentRequests = courses.reduce((s, c) => s + Math.round(c.enrollmentCount * 1.4), 0);
  const approvedEnrollments = courses.reduce((s, c) => s + c.enrollmentCount, 0);
  const pendingEnrollments = courses.reduce((s, c) => s + Math.round(c.enrollmentCount * 0.35), 0);
  const revenuePlaceholder = courses.reduce((s, c) => s + c.enrollmentCount * 2990, 0);
  const trainingCapacity = courses.reduce((s, c) => s + c.batchCount * 30, 0);
  const upcomingTrainings = courses.filter((c) => c.lifecycle === 'scheduled').length + 4;
  const completedTrainings = Math.round(approvedEnrollments * 0.62);
  const activeTrainersPlaceholder = courses.reduce((s, c) => s + c.instructorCount, 0);
  const activeStudentsPlaceholder = approvedEnrollments;
  const certificatesPlaceholder = completedTrainings;
  const learningHoursPlaceholder = courses.reduce((s, c) => s + c.durationHours * c.enrollmentCount, 0);

  return {
    totalCourses,
    publishedCourses,
    activeCourses,
    archivedCourses,
    enrollmentRequests,
    approvedEnrollments,
    pendingEnrollments,
    revenuePlaceholder,
    trainingCapacity,
    upcomingTrainings,
    completedTrainings,
    activeTrainersPlaceholder,
    activeStudentsPlaceholder,
    certificatesPlaceholder,
    learningHoursPlaceholder,
  };
}

export function buildEnrollmentTrend(months = 12, seed = 7): TimePoint[] {
  const rand = seeded(seed);
  const now = new Date();
  const points: TimePoint[] = [];
  for (let i = months - 1; i >= 0; i--) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const base = 40 + Math.round(rand() * 60) + (months - i) * 4;
    const enrollments = Math.max(8, base);
    const completions = Math.round(enrollments * (0.55 + rand() * 0.3));
    const revenue = enrollments * (2400 + Math.round(rand() * 900));
    points.push({
      label: d.toLocaleString('en-US', { month: 'short' }),
      enrollments,
      completions,
      revenue,
    });
  }
  return points;
}

export function buildDailyEnrollments(days = 30, seed = 21): { label: string; value: number }[] {
  const rand = seeded(seed);
  const now = new Date();
  return Array.from({ length: days }, (_, i) => {
    const d = new Date(now.getFullYear(), now.getMonth(), now.getDate() - (days - 1 - i));
    return { label: `${d.getDate()}`, value: Math.max(1, Math.round(rand() * 12 + 2)) };
  });
}

export function categoryDistribution(courses: Course[] = MOCK_COURSES): DistributionSlice[] {
  const counts = new Map<TrainingCategory, number>();
  courses.forEach((c) => counts.set(c.category, (counts.get(c.category) ?? 0) + 1));
  return Array.from(counts.entries()).map(([cat, value], i) => ({
    label: CATEGORY_LABELS[cat],
    value,
    color: VIZ[i % VIZ.length],
  }));
}

export function difficultyDistribution(courses: Course[] = MOCK_COURSES): DistributionSlice[] {
  const counts = new Map<CourseLevel, number>();
  courses.forEach((c) => counts.set(c.level, (counts.get(c.level) ?? 0) + 1));
  return Array.from(counts.entries()).map(([lvl, value], i) => ({
    label: LEVEL_LABELS[lvl],
    value,
    color: VIZ[i % VIZ.length],
  }));
}

export function languageDistribution(courses: Course[] = MOCK_COURSES): DistributionSlice[] {
  const counts = new Map<string, number>();
  courses.forEach((c) => counts.set(c.language, (counts.get(c.language) ?? 0) + 1));
  return Array.from(counts.entries()).map(([lang, value], i) => ({
    label: lang,
    value,
    color: VIZ[i % VIZ.length],
  }));
}

export function deliveryDistribution(courses: Course[] = MOCK_COURSES): DistributionSlice[] {
  const counts = new Map<DeliveryMode, number>();
  courses.forEach((c) => counts.set(c.deliveryMode, (counts.get(c.deliveryMode) ?? 0) + 1));
  return Array.from(counts.entries()).map(([mode, value], i) => ({
    label: DELIVERY_LABELS[mode],
    value,
    color: VIZ[i % VIZ.length],
  }));
}

export function coursePopularity(courses: Course[] = MOCK_COURSES): { label: string; value: number; slug: string }[] {
  return [...courses]
    .sort((a, b) => b.enrollmentCount - a.enrollmentCount)
    .map((c) => ({ label: c.name, value: c.enrollmentCount, slug: c.slug }));
}

export interface ResourceUsage {
  name: string;
  type: 'Document' | 'Video' | 'Tool';
  used: number;
  unused: boolean;
}

export function resourceUsage(seed = 33): ResourceUsage[] {
  const rand = seeded(seed);
  const names: string[] = [
    'Cultivation Starter Guide', 'Substrate Prep Manual', 'Spawn Handling Video',
    'Contamination Control Deck', 'Harvest Checklist', 'Post-Harvest Handling',
    'Business Plan Template', 'Pest Management Guide', 'Climate Control Video',
    'Certification Workbook', 'Marketplace Playbook', 'Trainer Slide Kit',
  ];
  return names.map((name, i) => ({
    name,
    type: i % 3 === 0 ? 'Video' : i % 3 === 1 ? 'Document' : 'Tool',
    used: Math.round(rand() * 400 + 40),
    unused: rand() > 0.8,
  }));
}

export interface CurriculumStat {
  label: string;
  value: number;
}

export function curriculumStats(courses: Course[] = MOCK_COURSES): CurriculumStat[] {
  return [
    { label: 'Modules', value: courses.reduce((s, c) => s + c.moduleCount, 0) },
    { label: 'Lessons', value: courses.reduce((s, c) => s + c.moduleCount * 6, 0) },
    { label: 'Learning Activities', value: courses.reduce((s, c) => s + c.moduleCount * 3, 0) },
    { label: 'Assignments', value: courses.reduce((s, c) => s + Math.round(c.moduleCount * 1.5), 0) },
    { label: 'Assessments', value: courses.reduce((s, c) => s + c.moduleCount, 0) },
  ];
}

export function enrollmentFunnel(): { label: string; value: number }[] {
  const k = computeExecutiveKpis();
  return [
    { label: 'Visited', value: Math.round(k.enrollmentRequests * 1.6) },
    { label: 'Started', value: k.enrollmentRequests },
    { label: 'Applied', value: k.approvedEnrollments + k.pendingEnrollments },
    { label: 'Approved', value: k.approvedEnrollments },
    { label: 'Completed', value: k.completedTrainings },
  ];
}

export function capacityUtilization(courses: Course[] = MOCK_COURSES): { label: string; value: number }[] {
  return courses.slice(0, 8).map((c) => ({
    label: c.code,
    value: Math.min(100, Math.round((c.enrollmentCount % 30) / 30 * 100)),
  }));
}

// ---- Saved dashboards (placeholder architecture) ----
export interface SavedDashboard {
  id: string;
  title: string;
  kind: 'pinned' | 'favorite' | 'recent';
  route: string;
  updatedAt: string;
}

export const SAVED_DASHBOARDS: SavedDashboard[] = [
  { id: 'd1', title: 'Executive Overview', kind: 'pinned', route: '/admin/training/lms-analytics/executive', updatedAt: '2026-07-14' },
  { id: 'd2', title: 'Enrollment Funnel', kind: 'pinned', route: '/admin/training/lms-analytics/enrollment', updatedAt: '2026-07-13' },
  { id: 'd3', title: 'Course Popularity', kind: 'favorite', route: '/admin/training/lms-analytics/course', updatedAt: '2026-07-12' },
  { id: 'd4', title: 'Resource Engagement', kind: 'favorite', route: '/admin/training/lms-analytics/resource', updatedAt: '2026-07-11' },
  { id: 'd5', title: 'Curriculum Coverage', kind: 'recent', route: '/admin/training/lms-analytics/curriculum', updatedAt: '2026-07-15' },
  { id: 'd6', title: 'Capacity Utilization', kind: 'recent', route: '/admin/training/lms-analytics/enrollment', updatedAt: '2026-07-10' },
];

export const EXPORT_FORMATS = ['PDF', 'Excel', 'CSV', 'Print', 'Email', 'Share'] as const;
export type ExportFormat = (typeof EXPORT_FORMATS)[number];

export function formatNumber(value: number): string {
  if (Math.abs(value) >= 1_000_000) return `${(value / 1_000_000).toFixed(1)}M`;
  if (Math.abs(value) >= 1_000) return `${(value / 1_000).toFixed(1)}K`;
  return value.toLocaleString('en-IN');
}

export function formatCurrency(value: number): string {
  return `₹${formatNumber(value)}`;
}
