export type BuilderPanel =
  | 'overview'
  | 'information'
  | 'curriculum'
  | 'objectives'
  | 'prerequisites'
  | 'resources'
  | 'media'
  | 'seo'
  | 'preview'
  | 'publishing'
  | 'settings';

export interface LearningObjective {
  id: string;
  text: string;
  priority: 'high' | 'medium' | 'low';
  category: 'knowledge' | 'skill' | 'competency';
}

export interface Prerequisite {
  id: string;
  type: 'knowledge' | 'equipment' | 'reading' | 'course' | 'experience';
  label: string;
  description: string;
}

export interface BuilderCourseInfo {
  title: string;
  code: string;
  shortDescription: string;
  detailedDescription: string;
  targetAudience: string[];
  difficulty: string;
  duration: string;
  durationHours: number;
  language: string;
  deliveryMode: string;
  category: string;
  tags: string[];
  trainingLevel: string;
  keywords: string[];
  versionNumber: string;
}

export interface MediaPlaceholder {
  id: string;
  type: 'thumbnail' | 'banner' | 'promo-video' | 'lesson-video' | 'gallery' | 'pdf' | 'presentation' | 'document';
  label: string;
  fileName?: string;
  fileSize?: string;
}

export interface ResourceAttachment {
  id: string;
  type: 'pdf' | 'image' | 'document' | 'reference' | 'sop' | 'research' | 'download';
  label: string;
  description: string;
  fileName?: string;
  fileSize?: string;
}

export interface SeoConfig {
  seoTitle: string;
  seoDescription: string;
  slug: string;
  canonicalUrl: string;
  metaKeywords: string[];
  socialPreview: { title: string; description: string; imageUrl: string };
  ogTitle: string;
  ogDescription: string;
  structuredData: string;
}

export interface CourseSettingsData {
  visibility: 'public' | 'private' | 'internal';
  enrollmentStatus: 'open' | 'closed' | 'coming-soon';
  status: 'draft' | 'published' | 'archived';
  featured: boolean;
  language: string;
  difficulty: string;
}

export interface BuilderState {
  panel: BuilderPanel;
  info: BuilderCourseInfo;
  objectives: LearningObjective[];
  prerequisites: Prerequisite[];
  mediaPlaceholders: MediaPlaceholder[];
  resources: ResourceAttachment[];
  seo: SeoConfig;
  settings: CourseSettingsData;
  unsavedChanges: boolean;
  lastSavedAt: string | null;
  versionNumber: number;
  previewDevice: 'desktop' | 'tablet' | 'mobile';
  previewTheme: 'light' | 'dark';
}

export const MOCK_INITIAL_INFO: BuilderCourseInfo = {
  title: '',
  code: '',
  shortDescription: '',
  detailedDescription: '',
  targetAudience: [],
  difficulty: 'beginner',
  duration: '',
  durationHours: 0,
  language: 'English',
  deliveryMode: 'offline',
  category: 'mushroom-cultivation',
  tags: [],
  trainingLevel: 'beginner',
  keywords: [],
  versionNumber: '1.0',
};

export const MOCK_INITIAL_SEO: SeoConfig = {
  seoTitle: '',
  seoDescription: '',
  slug: '',
  canonicalUrl: '',
  metaKeywords: [],
  socialPreview: { title: '', description: '', imageUrl: '' },
  ogTitle: '',
  ogDescription: '',
  structuredData: '',
};

export const MOCK_INITIAL_SETTINGS: CourseSettingsData = {
  visibility: 'private',
  enrollmentStatus: 'open',
  status: 'draft',
  featured: false,
  language: 'English',
  difficulty: 'beginner',
};

export const MOCK_OBJECTIVES: LearningObjective[] = [
  { id: 'obj-1', text: 'Understand mushroom biology and growth cycle', priority: 'high', category: 'knowledge' },
  { id: 'obj-2', text: 'Prepare and sterilize substrate materials', priority: 'high', category: 'skill' },
  { id: 'obj-3', text: 'Manage spawn run and pinning conditions', priority: 'medium', category: 'skill' },
  { id: 'obj-4', text: 'Harvest and package mushrooms for market', priority: 'medium', category: 'skill' },
  { id: 'obj-5', text: 'Identify and control common mushroom diseases', priority: 'low', category: 'competency' },
];

export const MOCK_PREREQUISITES: Prerequisite[] = [
  { id: 'pre-1', type: 'knowledge', label: 'Basic Biology', description: 'Understanding of basic biological processes' },
  { id: 'pre-2', type: 'equipment', label: 'Sterilization Equipment', description: 'Access to pressure cooker or autoclave' },
  { id: 'pre-3', type: 'reading', label: 'Mushroom Growers Handbook', description: 'Recommended pre-reading material' },
  { id: 'pre-4', type: 'course', label: 'Introduction to Agriculture', description: 'Completion of introductory agriculture course' },
];

export const MOCK_MEDIA: MediaPlaceholder[] = [
  { id: 'med-1', type: 'thumbnail', label: 'Course Thumbnail', fileName: 'course-thumb.jpg', fileSize: '120 KB' },
  { id: 'med-2', type: 'banner', label: 'Course Banner', fileName: 'course-banner.jpg', fileSize: '450 KB' },
  { id: 'med-3', type: 'promo-video', label: 'Promotional Video', fileName: 'promo.mp4', fileSize: '24 MB' },
  { id: 'med-4', type: 'lesson-video', label: 'Lesson 1 Video', fileName: 'lesson-1.mp4', fileSize: '156 MB' },
  { id: 'med-5', type: 'gallery', label: 'Gallery Images (8)', fileName: 'gallery.zip', fileSize: '3.2 MB' },
];

export const MOCK_RESOURCES: ResourceAttachment[] = [
  { id: 'res-1', type: 'pdf', label: 'Course Syllabus', description: 'Complete course syllabus and schedule', fileName: 'syllabus.pdf', fileSize: '240 KB' },
  { id: 'res-2', type: 'sop', label: 'Substrate Preparation SOP', description: 'Standard operating procedure for substrate prep', fileName: 'sop-substrate.pdf', fileSize: '560 KB' },
  { id: 'res-3', type: 'research', label: 'Mushroom Cultivation Research', description: 'Latest research papers on commercial cultivation', fileName: 'research-papers.pdf', fileSize: '2.1 MB' },
  { id: 'res-4', type: 'document', label: 'Equipment Checklist', description: 'Complete equipment list for course', fileName: 'equipment-checklist.pdf', fileSize: '180 KB' },
  { id: 'res-5', type: 'reference', label: 'Disease Identification Guide', description: 'Visual guide for common mushroom diseases', fileName: 'disease-guide.pdf', fileSize: '4.5 MB' },
];

export const PANEL_LABELS: Record<BuilderPanel, string> = {
  overview: 'Overview',
  information: 'Basic Information',
  curriculum: 'Curriculum Structure',
  objectives: 'Learning Objectives',
  prerequisites: 'Prerequisites',
  resources: 'Resources',
  media: 'Media',
  seo: 'SEO Configuration',
  preview: 'Live Preview',
  publishing: 'Publishing',
  settings: 'Settings',
};

export const PANEL_ICONS: Record<BuilderPanel, string> = {
  overview: 'info',
  information: 'edit',
  curriculum: 'layers',
  objectives: 'target',
  prerequisites: 'check-circle',
  resources: 'folder',
  media: 'image',
  seo: 'search',
  preview: 'eye',
  publishing: 'send',
  settings: 'settings',
};

export const DIFFICULTY_OPTIONS = [
  { value: 'beginner', label: 'Beginner' },
  { value: 'intermediate', label: 'Intermediate' },
  { value: 'advanced', label: 'Advanced' },
  { value: 'all-levels', label: 'All Levels' },
];

export const LANGUAGE_OPTIONS = [
  { value: 'English', label: 'English' },
  { value: 'Hindi', label: 'Hindi' },
  { value: 'Marathi', label: 'Marathi' },
  { value: 'Tamil', label: 'Tamil' },
  { value: 'Telugu', label: 'Telugu' },
  { value: 'Kannada', label: 'Kannada' },
];

export const DELIVERY_OPTIONS = [
  { value: 'offline', label: 'Offline' },
  { value: 'online', label: 'Online' },
  { value: 'hybrid', label: 'Hybrid' },
  { value: 'recorded', label: 'Recorded' },
  { value: 'live', label: 'Live' },
];

export const CATEGORY_OPTIONS = [
  { value: 'mushroom-cultivation', label: 'Mushroom Cultivation' },
  { value: 'spawn-production', label: 'Spawn Production' },
  { value: 'commercial-farming', label: 'Commercial Farming' },
  { value: 'value-added-products', label: 'Value Added Products' },
  { value: 'business-training', label: 'Business Training' },
  { value: 'corporate-training', label: 'Corporate Training' },
  { value: 'institutional-programs', label: 'Institutional Programs' },
  { value: 'franchise-programs', label: 'Franchise Programs' },
];

export const VISIBILITY_OPTIONS = [
  { value: 'public', label: 'Public' },
  { value: 'private', label: 'Private' },
  { value: 'internal', label: 'Internal Only' },
];

export const ENROLLMENT_OPTIONS = [
  { value: 'open', label: 'Open' },
  { value: 'closed', label: 'Closed' },
  { value: 'coming-soon', label: 'Coming Soon' },
];
