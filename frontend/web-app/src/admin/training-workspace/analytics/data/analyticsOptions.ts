import type { CourseLevel, DeliveryMode, TrainingCategory } from '../../courses/data/courseMockData';
import {
  CATEGORY_LABELS,
  DELIVERY_LABELS,
  LEVEL_LABELS,
} from '../../courses/data/courseMockData';

export type AnalyticsScope =
  | 'executive'
  | 'course'
  | 'enrollment'
  | 'curriculum'
  | 'resource'
  | 'student'
  | 'trainer'
  | 'financial';

export interface AnalyticsFilters {
  search: string;
  category: TrainingCategory | '';
  level: CourseLevel | '';
  language: string;
  deliveryMode: DeliveryMode | '';
  trainingType: string;
  trainer: string;
  student: string;
  dateRange: '7d' | '30d' | '90d' | '12m' | 'all';
}

export const ANALYTICS_CATEGORY_OPTIONS: { value: TrainingCategory | ''; label: string }[] = [
  { value: '', label: 'All Categories' },
  ...(Object.keys(CATEGORY_LABELS) as TrainingCategory[]).map((c) => ({ value: c, label: CATEGORY_LABELS[c] })),
];

export const ANALYTICS_LEVEL_OPTIONS: { value: CourseLevel | ''; label: string }[] = [
  { value: '', label: 'All Levels' },
  ...(Object.keys(LEVEL_LABELS) as CourseLevel[]).map((l) => ({ value: l, label: LEVEL_LABELS[l] })),
];

export const ANALYTICS_DELIVERY_OPTIONS: { value: DeliveryMode | ''; label: string }[] = [
  { value: '', label: 'All Modes' },
  ...(Object.keys(DELIVERY_LABELS) as DeliveryMode[]).map((d) => ({ value: d, label: DELIVERY_LABELS[d] })),
];

export const ANALYTICS_LANGUAGE_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Languages' },
  { value: 'English', label: 'English' },
  { value: 'Hindi', label: 'Hindi' },
  { value: 'Marathi', label: 'Marathi' },
  { value: 'Tamil', label: 'Tamil' },
];

export const ANALYTICS_TRAINING_TYPE_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Types' },
  { value: 'self-paced', label: 'Self-paced' },
  { value: 'instructor-led', label: 'Instructor-led' },
  { value: 'workshop', label: 'Workshop' },
  { value: 'masterclass', label: 'Masterclass' },
  { value: 'certification', label: 'Certification' },
];

export const ANALYTICS_TRAINER_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Trainers' },
  { value: 'dr-rajesh-kumar', label: 'Dr. Rajesh Kumar' },
  { value: 'prof-sunita-patel', label: 'Prof. Sunita Patel' },
  { value: 'dr-anand-desai', label: 'Dr. Anand Desai' },
  { value: 'chef-lakshmi-menon', label: 'Chef Lakshmi Menon' },
];

export const ANALYTICS_STUDENT_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Students' },
  { value: 'new', label: 'New Learners' },
  { value: 'returning', label: 'Returning Learners' },
  { value: 'completed', label: 'Completed' },
];

export const ANALYTICS_DATE_RANGE_OPTIONS: { value: AnalyticsFilters['dateRange']; label: string }[] = [
  { value: '7d', label: 'Last 7 days' },
  { value: '30d', label: 'Last 30 days' },
  { value: '90d', label: 'Last 90 days' },
  { value: '12m', label: 'Last 12 months' },
  { value: 'all', label: 'All time' },
];

export const DEFAULT_ANALYTICS_FILTERS: AnalyticsFilters = {
  search: '',
  category: '',
  level: '',
  language: '',
  deliveryMode: '',
  trainingType: '',
  trainer: '',
  student: '',
  dateRange: '12m',
};

export const ANALYTICS_SCOPES: { value: AnalyticsScope; label: string }[] = [
  { value: 'executive', label: 'Executive' },
  { value: 'course', label: 'Course' },
  { value: 'enrollment', label: 'Enrollment' },
  { value: 'curriculum', label: 'Curriculum' },
  { value: 'resource', label: 'Resource' },
  { value: 'student', label: 'Student (future)' },
  { value: 'trainer', label: 'Trainer (future)' },
  { value: 'financial', label: 'Financial (future)' },
];
