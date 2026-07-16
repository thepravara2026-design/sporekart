import type { CourseLevel, DeliveryMode, TrainingCategory } from '../../../admin/training-workspace/courses/data/courseMockData';
import {
  CATEGORY_LABELS,
  DELIVERY_LABELS,
  LEVEL_LABELS,
} from '../../../admin/training-workspace/courses/data/courseMockData';

export type CatalogViewMode = 'grid' | 'list' | 'compact' | 'featured' | 'carousel';

export type CatalogSortField =
  | 'popular'
  | 'newest'
  | 'alphabetical'
  | 'duration'
  | 'price'
  | 'difficulty'
  | 'featured'
  | 'trending'
  | 'updated';

export interface CatalogFilters {
  search: string;
  category: TrainingCategory | '';
  level: CourseLevel | '';
  language: string;
  deliveryMode: DeliveryMode | '';
  trainingType: string;
  priceBand: 'all' | 'free' | 'low' | 'mid' | 'high';
  durationBand: 'all' | 'short' | 'medium' | 'long';
  availability: 'all' | 'open' | 'limited' | 'full';
  skillLevel: string;
  status: 'all' | 'published' | 'scheduled';
}

export const CATALOG_SORT_OPTIONS: { value: CatalogSortField; label: string }[] = [
  { value: 'popular', label: 'Most Popular' },
  { value: 'newest', label: 'Newest' },
  { value: 'trending', label: 'Trending' },
  { value: 'featured', label: 'Featured' },
  { value: 'updated', label: 'Recently Updated' },
  { value: 'alphabetical', label: 'Alphabetical' },
  { value: 'duration', label: 'Duration' },
  { value: 'price', label: 'Price' },
  { value: 'difficulty', label: 'Difficulty' },
];

export const CATALOG_CATEGORY_OPTIONS: { value: TrainingCategory | ''; label: string }[] = [
  { value: '', label: 'All Categories' },
  ...(Object.keys(CATEGORY_LABELS) as TrainingCategory[]).map((c) => ({
    value: c as TrainingCategory | '',
    label: CATEGORY_LABELS[c],
  })),
];

export const CATALOG_LEVEL_OPTIONS: { value: CourseLevel | ''; label: string }[] = [
  { value: '', label: 'All Levels' },
  ...(Object.keys(LEVEL_LABELS) as CourseLevel[]).map((l) => ({
    value: l as CourseLevel | '',
    label: LEVEL_LABELS[l],
  })),
];

export const CATALOG_DELIVERY_OPTIONS: { value: DeliveryMode | ''; label: string }[] = [
  { value: '', label: 'All Modes' },
  ...(Object.keys(DELIVERY_LABELS) as DeliveryMode[]).map((d) => ({
    value: d as DeliveryMode | '',
    label: DELIVERY_LABELS[d],
  })),
];

export const CATALOG_LANGUAGE_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Languages' },
  { value: 'English', label: 'English' },
  { value: 'Hindi', label: 'Hindi' },
  { value: 'Marathi', label: 'Marathi' },
  { value: 'Tamil', label: 'Tamil' },
  { value: 'Kannada', label: 'Kannada' },
];

export const CATALOG_TRAINING_TYPE_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Types' },
  { value: 'self-paced', label: 'Self-paced' },
  { value: 'instructor-led', label: 'Instructor-led' },
  { value: 'workshop', label: 'Workshop' },
  { value: 'masterclass', label: 'Masterclass' },
  { value: 'certification', label: 'Certification' },
];

export const CATALOG_SKILL_OPTIONS: { value: string; label: string }[] = [
  { value: '', label: 'All Skill Levels' },
  { value: 'beginner', label: 'Beginner' },
  { value: 'intermediate', label: 'Intermediate' },
  { value: 'advanced', label: 'Advanced' },
];

export const CATALOG_PRICE_BANDS: { value: CatalogFilters['priceBand']; label: string }[] = [
  { value: 'all', label: 'Any Price' },
  { value: 'free', label: 'Free' },
  { value: 'low', label: 'Under ₹2,500' },
  { value: 'mid', label: '₹2,500 – ₹5,000' },
  { value: 'high', label: 'Above ₹5,000' },
];

export const CATALOG_DURATION_BANDS: { value: CatalogFilters['durationBand']; label: string }[] = [
  { value: 'all', label: 'Any Duration' },
  { value: 'short', label: 'Under 4 weeks' },
  { value: 'medium', label: '4 – 6 weeks' },
  { value: 'long', label: '6+ weeks' },
];

export const CATALOG_AVAILABILITY_OPTIONS: { value: CatalogFilters['availability']; label: string }[] = [
  { value: 'all', label: 'Any Availability' },
  { value: 'open', label: 'Seats Open' },
  { value: 'limited', label: 'Limited Seats' },
  { value: 'full', label: 'Waitlist' },
];

export const CATALOG_STATUS_OPTIONS: { value: CatalogFilters['status']; label: string }[] = [
  { value: 'all', label: 'All Courses' },
  { value: 'published', label: 'Enrolling Now' },
  { value: 'scheduled', label: 'Upcoming' },
];

export const DIFFICULTY_RANK: Record<CourseLevel, number> = {
  beginner: 1,
  'self-paced': 1,
  intermediate: 2,
  workshop: 2,
  'instructor-led': 2,
  advanced: 3,
  masterclass: 3,
  corporate: 2,
  institution: 2,
  certification: 2,
  hybrid: 2,
  'future-ai-assisted': 3,
};

export const DEFAULT_CATALOG_FILTERS: CatalogFilters = {
  search: '',
  category: '',
  level: '',
  language: '',
  deliveryMode: '',
  trainingType: '',
  priceBand: 'all',
  durationBand: 'all',
  availability: 'all',
  skillLevel: '',
  status: 'all',
};
