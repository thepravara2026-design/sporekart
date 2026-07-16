import type {
  Course,
  CourseLevel,
  DeliveryMode,
  TrainingCategory,
} from '../../../admin/training-workspace/courses/data/courseMockData';
import {
  CATEGORY_LABELS,
  CATEGORY_COLORS,
  LEVEL_LABELS,
  DELIVERY_LABELS,
} from '../../../admin/training-workspace/courses/data/courseMockData';

export interface CourseBadgeKind {
  kind: 'featured' | 'new' | 'trending' | 'corporate' | 'government' | 'popular';
  label: string;
}

export interface DiscoveryCourse {
  course: Course;
  price: number;
  currency: string;
  originalPrice?: number;
  availableSeats: number;
  totalSeats: number;
  badges: CourseBadgeKind[];
  trainerNames: string[];
  ratingPlaceholder: number;
  enrollmentCountPlaceholder: number;
  nextBatchDate: string;
  featured: boolean;
  trending: boolean;
  recommended: boolean;
  upcoming: boolean;
  recentlyAdded: boolean;
  faqs: { question: string; answer: string }[];
  curriculumHighlights: string[];
  relatedIds: string[];
}

export const CATEGORY_LABEL = CATEGORY_LABELS;
export const CATEGORY_COLOR = CATEGORY_COLORS;
export const LEVEL_LABEL = LEVEL_LABELS;
export const DELIVERY_LABEL = DELIVERY_LABELS;

export function categoryLabel(category: TrainingCategory): string {
  return CATEGORY_LABELS[category];
}

export function categoryColor(category: TrainingCategory): string {
  return CATEGORY_COLORS[category];
}

export function levelLabel(level: CourseLevel): string {
  return LEVEL_LABELS[level];
}

export function deliveryLabel(mode: DeliveryMode): string {
  return DELIVERY_LABELS[mode];
}

const TRAINERS: Record<string, string[]> = {
  'crs-001': ['Dr. Rajesh Kumar', 'Meena Iyer'],
  'crs-002': ['Prof. Sunita Patel', 'Dr. Arvind Rao'],
  'crs-003': ['Dr. Anand Desai', 'Kavya Nair'],
  'crs-004': ['Chef Lakshmi Menon', 'Dr. Anand Desai'],
  'crs-005': ['Dr. Priya Nandan', 'Rohit Verma'],
  'crs-006': ['Prof. Sunita Patel', 'Dr. Priya Nandan'],
  'crs-007': ['Dr. Arvind Rao', 'Meena Iyer'],
  'crs-008': ['Kavya Nair', 'Rohit Verma'],
  'crs-009': ['Dr. Rajesh Kumar', 'Dr. Priya Nandan'],
  'crs-010': ['Dr. Anand Desai', 'Prof. Sunita Patel'],
  'crs-011': ['Meena Iyer', 'Dr. Arvind Rao'],
  'crs-012': ['Dr. Priya Nandan', 'Kavya Nair'],
};

const BADGES: Record<string, CourseBadgeKind[]> = {
  'crs-001': [{ kind: 'featured', label: 'Featured' }, { kind: 'popular', label: 'Popular' }],
  'crs-002': [{ kind: 'trending', label: 'Trending' }, { kind: 'corporate', label: 'Corporate' }],
  'crs-003': [{ kind: 'featured', label: 'Featured' }, { kind: 'government', label: 'Government' }],
  'crs-004': [{ kind: 'new', label: 'New' }],
  'crs-005': [{ kind: 'trending', label: 'Trending' }],
  'crs-006': [{ kind: 'corporate', label: 'Corporate' }],
  'crs-007': [{ kind: 'government', label: 'Government' }],
  'crs-008': [{ kind: 'new', label: 'New' }],
  'crs-009': [{ kind: 'popular', label: 'Popular' }],
  'crs-010': [{ kind: 'corporate', label: 'Corporate' }, { kind: 'featured', label: 'Featured' }],
  'crs-011': [{ kind: 'government', label: 'Government' }],
  'crs-012': [{ kind: 'trending', label: 'Trending' }, { kind: 'new', label: 'New' }],
};

function makeFaqs(course: Course): { question: string; answer: string }[] {
  return [
    {
      question: `Who should enroll in ${course.name}?`,
      answer:
        'This program is designed for ' +
        (course.targetAudience.length ? course.targetAudience.join(', ').toLowerCase() : 'learners interested in this domain') +
        '. No prior enterprise experience is required unless listed in prerequisites.',
    },
    {
      question: 'What is the delivery format?',
      answer: `The course is delivered via ${DELIVERY_LABELS[course.deliveryMode].toLowerCase()} mode over ${course.duration.toLowerCase()}. Sessions are recorded and available for review.`,
    },
    {
      question: 'Will I receive a certificate?',
      answer: 'Yes. A verifiable SporeKart certificate of completion is issued after successfully finishing all modules and assessments.',
    },
    {
      question: 'Are group or corporate enrollments supported?',
      answer: 'Corporate and institutional cohort enrollments are supported. Contact our enterprise team for bulk seats and private batches.',
    },
  ];
}

export function buildDiscoveryCourse(course: Course, allCourses: Course[]): DiscoveryCourse {
  const seed = parseInt(course.code.replace(/\D/g, '') || '1', 10);
  const price = 1990 + (seed % 9) * 540;
  const originalPrice = seed % 3 === 0 ? Math.round(price * 1.25) : undefined;
  const totalSeats = 30 + (seed % 5) * 10;
  const availableSeats = Math.max(2, totalSeats - (course.enrollmentCount % totalSeats));
  const badges = BADGES[course.id] ?? [];
  const trainerNames = TRAINERS[course.id] ?? [course.createdBy];
  const relatedIds = allCourses
    .filter((c) => c.id !== course.id && c.category === course.category)
    .slice(0, 4)
    .map((c) => c.id);
  if (relatedIds.length < 3) {
    allCourses
      .filter((c) => c.id !== course.id && !relatedIds.includes(c.id))
      .slice(0, 3 - relatedIds.length)
      .forEach((c) => relatedIds.push(c.id));
  }

  return {
    course,
    price,
    currency: 'INR',
    originalPrice,
    availableSeats,
    totalSeats,
    badges,
    trainerNames,
    ratingPlaceholder: course.rating,
    enrollmentCountPlaceholder: course.enrollmentCount,
    nextBatchDate: course.publishedAt
      ? new Date(new Date(course.publishedAt).getTime() + 1000 * 60 * 60 * 24 * 21).toISOString().split('T')[0]
      : course.updatedAt,
    featured: badges.some((b) => b.kind === 'featured') || course.pinned,
    trending: badges.some((b) => b.kind === 'trending') || seed % 4 === 0,
    recommended: seed % 3 === 0,
    upcoming: course.lifecycle === 'scheduled',
    recentlyAdded: seed % 5 === 0,
    faqs: makeFaqs(course),
    curriculumHighlights: course.learningObjectives.slice(0, 5),
    relatedIds,
  };
}

export function buildDiscoveryCatalog(courses: Course[]): DiscoveryCourse[] {
  return courses.map((course) => buildDiscoveryCourse(course, courses));
}
