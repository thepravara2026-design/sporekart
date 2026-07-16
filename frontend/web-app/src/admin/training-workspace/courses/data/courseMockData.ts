export type CourseLifecycle =
  | 'draft'
  | 'pending-review'
  | 'approved'
  | 'published'
  | 'scheduled'
  | 'closed'
  | 'archived'
  | 'retired'
  | 'future-version';

export type CourseLevel =
  | 'beginner'
  | 'intermediate'
  | 'advanced'
  | 'workshop'
  | 'masterclass'
  | 'corporate'
  | 'institution'
  | 'certification'
  | 'self-paced'
  | 'instructor-led'
  | 'hybrid'
  | 'future-ai-assisted';

export type DeliveryMode =
  | 'offline'
  | 'online'
  | 'hybrid'
  | 'recorded'
  | 'live'
  | 'future-vr'
  | 'future-ar';

export type TrainingCategory =
  | 'mushroom-cultivation'
  | 'spawn-production'
  | 'commercial-farming'
  | 'value-added-products'
  | 'business-training'
  | 'corporate-training'
  | 'institutional-programs'
  | 'franchise-programs'
  | 'future-ai-courses';

export interface Course {
  id: string;
  code: string;
  name: string;
  shortDescription: string;
  longDescription: string;
  learningObjectives: string[];
  targetAudience: string[];
  prerequisites: string[];
  category: TrainingCategory;
  level: CourseLevel;
  deliveryMode: DeliveryMode;
  language: string;
  duration: string;
  durationHours: number;
  lifecycle: CourseLifecycle;
  visibility: 'public' | 'private' | 'internal';
  seoTitle: string;
  seoDescription: string;
  slug: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
  thumbnailUrl?: string;
  featuredImageUrl?: string;
  coverImageUrl?: string;
  previewBannerUrl?: string;
  pinned: boolean;
  favorite: boolean;
  instructorCount: number;
  enrollmentCount: number;
  batchCount: number;
  moduleCount: number;
  rating: number;
  reviewCount: number;
}

export const LIFECYCLE_LABELS: Record<CourseLifecycle, string> = {
  draft: 'Draft',
  'pending-review': 'Pending Review',
  approved: 'Approved',
  published: 'Published',
  scheduled: 'Scheduled',
  closed: 'Closed',
  archived: 'Archived',
  retired: 'Retired',
  'future-version': 'Future Version',
};

export const LIFECYCLE_VARIANTS: Record<CourseLifecycle, 'neutral' | 'warning' | 'info' | 'success' | 'danger'> = {
  draft: 'neutral',
  'pending-review': 'warning',
  approved: 'info',
  published: 'success',
  scheduled: 'info',
  closed: 'neutral',
  archived: 'neutral',
  retired: 'danger',
  'future-version': 'info',
};

export const CATEGORY_LABELS: Record<TrainingCategory, string> = {
  'mushroom-cultivation': 'Mushroom Cultivation',
  'spawn-production': 'Spawn Production',
  'commercial-farming': 'Commercial Farming',
  'value-added-products': 'Value Added Products',
  'business-training': 'Business Training',
  'corporate-training': 'Corporate Training',
  'institutional-programs': 'Institutional Programs',
  'franchise-programs': 'Franchise Programs',
  'future-ai-courses': 'Future AI Courses',
};

export const LEVEL_LABELS: Record<CourseLevel, string> = {
  beginner: 'Beginner',
  intermediate: 'Intermediate',
  advanced: 'Advanced',
  workshop: 'Workshop',
  masterclass: 'Masterclass',
  corporate: 'Corporate',
  institution: 'Institution',
  certification: 'Certification',
  'self-paced': 'Self-paced',
  'instructor-led': 'Instructor-led',
  hybrid: 'Hybrid',
  'future-ai-assisted': 'AI Assisted',
};

export const DELIVERY_LABELS: Record<DeliveryMode, string> = {
  offline: 'Offline',
  online: 'Online',
  hybrid: 'Hybrid',
  recorded: 'Recorded',
  live: 'Live',
  'future-vr': 'VR Training',
  'future-ar': 'AR Training',
};

export const CATEGORY_COLORS: Record<TrainingCategory, string> = {
  'mushroom-cultivation': '#22c55e',
  'spawn-production': '#a855f7',
  'commercial-farming': '#f59e0b',
  'value-added-products': '#3b82f6',
  'business-training': '#ec4899',
  'corporate-training': '#14b8a6',
  'institutional-programs': '#8b5cf6',
  'franchise-programs': '#f97316',
  'future-ai-courses': '#06b6d4',
};

export interface CourseDashboardStats {
  totalCourses: number;
  publishedCourses: number;
  draftCourses: number;
  archivedCourses: number;
  pendingReview: number;
  upcomingLaunches: number;
  popularCategories: { category: TrainingCategory; count: number }[];
  recentlyUpdated: number;
}

function makeDate(daysAgo: number): string {
  const d = new Date();
  d.setDate(d.getDate() - daysAgo);
  return d.toISOString().split('T')[0];
}

export const MOCK_COURSES: Course[] = [
  {
    id: 'crs-001',
    code: 'MC-101',
    name: 'Mushroom Cultivation Fundamentals',
    shortDescription: 'Learn the complete process of commercial mushroom cultivation from spawn to harvest.',
    longDescription: 'This comprehensive course covers every aspect of mushroom cultivation including substrate preparation, spawning, casing, pinning, harvesting, and post-harvest handling. Suitable for beginners and experienced growers alike.',
    learningObjectives: ['Understand mushroom biology and growth cycle', 'Prepare and sterilize substrate', 'Manage spawn run and pinning', 'Harvest and package mushrooms', 'Identify and control common diseases'],
    targetAudience: ['New farmers', 'Agricultural students', 'Home growers', 'Entrepreneurs'],
    prerequisites: [],
    category: 'mushroom-cultivation',
    level: 'beginner',
    deliveryMode: 'offline',
    language: 'English',
    duration: '4 Weeks',
    durationHours: 40,
    lifecycle: 'published',
    visibility: 'public',
    seoTitle: 'Mushroom Cultivation Fundamentals Course | SporeKart',
    seoDescription: 'Learn commercial mushroom cultivation from experts. 4-week comprehensive training program.',
    slug: 'mushroom-cultivation-fundamentals',
    createdBy: 'Dr. Rajesh Kumar',
    createdAt: makeDate(90),
    updatedAt: makeDate(2),
    publishedAt: makeDate(85),
    pinned: true,
    favorite: true,
    instructorCount: 3,
    enrollmentCount: 245,
    batchCount: 6,
    moduleCount: 8,
    rating: 4.7,
    reviewCount: 89,
  },
  {
    id: 'crs-002',
    code: 'SP-201',
    name: 'Advanced Spawn Production',
    shortDescription: 'Master the science of spawn production for large-scale mushroom farming.',
    longDescription: 'Deep dive into spawn production techniques including grain spawn preparation, mother culture maintenance, sterile technique, and quality control. Ideal for spawn production facilities.',
    learningObjectives: ['Prepare grain spawn', 'Maintain mother cultures', 'Master sterile techniques', 'Implement quality control', 'Scale production'],
    targetAudience: ['Spawn producers', 'Lab technicians', 'Commercial farmers', 'Quality control staff'],
    prerequisites: ['MC-101 or equivalent experience'],
    category: 'spawn-production',
    level: 'advanced',
    deliveryMode: 'hybrid',
    language: 'English',
    duration: '6 Weeks',
    durationHours: 60,
    lifecycle: 'published',
    visibility: 'public',
    seoTitle: 'Advanced Spawn Production Course | SporeKart',
    seoDescription: 'Master spawn production for commercial mushroom farming. 6-week hybrid training.',
    slug: 'advanced-spawn-production',
    createdBy: 'Prof. Sunita Patel',
    createdAt: makeDate(120),
    updatedAt: makeDate(5),
    publishedAt: makeDate(115),
    pinned: false,
    favorite: true,
    instructorCount: 2,
    enrollmentCount: 120,
    batchCount: 4,
    moduleCount: 10,
    rating: 4.9,
    reviewCount: 45,
  },
  {
    id: 'crs-003',
    code: 'CF-301',
    name: 'Commercial Oyster Farming',
    shortDescription: 'End-to-end training for setting up and running a commercial oyster mushroom farm.',
    longDescription: 'From farm setup to market distribution, this course covers everything needed to run a profitable oyster mushroom farming operation. Includes business planning, production management, and marketing strategies.',
    learningObjectives: ['Plan commercial farm setup', 'Manage production cycles', 'Control pests and diseases', 'Develop marketing strategy', 'Calculate profitability'],
    targetAudience: ['Entrepreneurs', 'Farm managers', 'Agribusiness professionals', 'Investors'],
    prerequisites: ['MC-101'],
    category: 'commercial-farming',
    level: 'advanced',
    deliveryMode: 'offline',
    language: 'English',
    duration: '8 Weeks',
    durationHours: 80,
    lifecycle: 'published',
    visibility: 'public',
    seoTitle: 'Commercial Oyster Farming Course | SporeKart',
    seoDescription: 'Set up and run a profitable commercial oyster mushroom farm. 8-week intensive program.',
    slug: 'commercial-oyster-farming',
    createdBy: 'Dr. Anand Desai',
    createdAt: makeDate(150),
    updatedAt: makeDate(3),
    publishedAt: makeDate(145),
    pinned: false,
    favorite: false,
    instructorCount: 4,
    enrollmentCount: 180,
    batchCount: 5,
    moduleCount: 12,
    rating: 4.8,
    reviewCount: 67,
  },
  {
    id: 'crs-004',
    code: 'VA-101',
    name: 'Value-Added Mushroom Products',
    shortDescription: 'Learn to create and market value-added products from mushrooms.',
    longDescription: 'Transform fresh mushrooms into high-value products including powders, extracts, supplements, and packaged foods. Covers processing, preservation, packaging, and regulatory compliance.',
    learningObjectives: ['Process mushroom powder and extracts', 'Develop product recipes', 'Understand food safety regulations', 'Design packaging', 'Create marketing materials'],
    targetAudience: ['Food processors', 'Product developers', 'Entrepreneurs', 'Chefs'],
    prerequisites: [],
    category: 'value-added-products',
    level: 'intermediate',
    deliveryMode: 'online',
    language: 'English',
    duration: '4 Weeks',
    durationHours: 30,
    lifecycle: 'published',
    visibility: 'public',
    seoTitle: 'Value-Added Mushroom Products Course | SporeKart',
    seoDescription: 'Create and market value-added mushroom products. 4-week online program.',
    slug: 'value-added-mushroom-products',
    createdBy: 'Dr. Meera Nair',
    createdAt: makeDate(60),
    updatedAt: makeDate(1),
    publishedAt: makeDate(55),
    pinned: false,
    favorite: false,
    instructorCount: 2,
    enrollmentCount: 95,
    batchCount: 3,
    moduleCount: 6,
    rating: 4.5,
    reviewCount: 32,
  },
  {
    id: 'crs-005',
    code: 'BT-101',
    name: 'Mushroom Business & Entrepreneurship',
    shortDescription: 'Build a sustainable mushroom business from the ground up.',
    longDescription: 'Business planning, financial management, market analysis, and growth strategies for mushroom enterprises. Includes case studies from successful mushroom businesses.',
    learningObjectives: ['Write a business plan', 'Manage finances', 'Analyze markets', 'Develop growth strategies', 'Navigate regulations'],
    targetAudience: ['Startup founders', 'Small business owners', 'Farm entrepreneurs', 'Students'],
    prerequisites: [],
    category: 'business-training',
    level: 'beginner',
    deliveryMode: 'online',
    language: 'English',
    duration: '6 Weeks',
    durationHours: 45,
    lifecycle: 'published',
    visibility: 'public',
    seoTitle: 'Mushroom Business Course | SporeKart',
    seoDescription: 'Build a sustainable mushroom business. 6-week entrepreneurship program.',
    slug: 'mushroom-business-entrepreneurship',
    createdBy: 'Mr. Vikram Joshi',
    createdAt: makeDate(45),
    updatedAt: makeDate(4),
    publishedAt: makeDate(40),
    pinned: false,
    favorite: false,
    instructorCount: 2,
    enrollmentCount: 210,
    batchCount: 4,
    moduleCount: 8,
    rating: 4.6,
    reviewCount: 55,
  },
  {
    id: 'crs-006',
    code: 'CT-201',
    name: 'Corporate Mushroom Training Program',
    shortDescription: 'Tailored training for corporate teams entering the mushroom industry.',
    longDescription: 'Comprehensive corporate training program covering mushroom science, production technology, quality management, and business operations for organizations entering or expanding in the mushroom sector.',
    learningObjectives: ['Understand corporate mushroom operations', 'Implement quality systems', 'Manage supply chains', 'Train team members', 'Ensure compliance'],
    targetAudience: ['Corporate teams', 'HR & L&D professionals', 'Operations managers', 'Quality managers'],
    prerequisites: [],
    category: 'corporate-training',
    level: 'intermediate',
    deliveryMode: 'hybrid',
    language: 'English',
    duration: '12 Weeks',
    durationHours: 120,
    lifecycle: 'approved',
    visibility: 'internal',
    seoTitle: 'Corporate Mushroom Training Program | SporeKart',
    seoDescription: 'Tailored corporate training for mushroom industry. 12-week comprehensive program.',
    slug: 'corporate-mushroom-training',
    createdBy: 'Dr. Rajesh Kumar',
    createdAt: makeDate(30),
    updatedAt: makeDate(1),
    pinned: true,
    favorite: false,
    instructorCount: 5,
    enrollmentCount: 0,
    batchCount: 0,
    moduleCount: 16,
    rating: 0,
    reviewCount: 0,
  },
  {
    id: 'crs-007',
    code: 'IP-301',
    name: 'Institutional Mushroom Research Program',
    shortDescription: 'Advanced research-oriented program for academic institutions and research centers.',
    longDescription: 'Designed for universities and research institutions, this program covers advanced mycology, strain development, genetic improvement, and research methodology in mushroom science.',
    learningObjectives: ['Conduct mycology research', 'Develop new strains', 'Apply genetic improvement', 'Design experiments', 'Publish research findings'],
    targetAudience: ['Researchers', 'PhD students', 'University faculty', 'Lab directors'],
    prerequisites: ['MC-101', 'SP-201'],
    category: 'institutional-programs',
    level: 'advanced',
    deliveryMode: 'hybrid',
    language: 'English',
    duration: '16 Weeks',
    durationHours: 160,
    lifecycle: 'pending-review',
    visibility: 'private',
    seoTitle: 'Institutional Mushroom Research Program | SporeKart',
    seoDescription: 'Advanced research program for academic institutions. 16-week comprehensive curriculum.',
    slug: 'institutional-mushroom-research',
    createdBy: 'Prof. Sunita Patel',
    createdAt: makeDate(20),
    updatedAt: makeDate(1),
    pinned: false,
    favorite: true,
    instructorCount: 3,
    enrollmentCount: 0,
    batchCount: 0,
    moduleCount: 20,
    rating: 0,
    reviewCount: 0,
  },
  {
    id: 'crs-008',
    code: 'FP-101',
    name: 'Franchise Mushroom Farm Operations',
    shortDescription: 'Standardized training for franchise mushroom farm operators.',
    longDescription: 'Complete operational training for franchise partners covering SporeKart\'s standardized processes, quality protocols, brand guidelines, and operational procedures.',
    learningObjectives: ['Follow franchise standards', 'Manage farm operations', 'Maintain quality protocols', 'Apply brand guidelines', 'Use SporeKart systems'],
    targetAudience: ['Franchise partners', 'Farm operators', 'Regional managers', 'Quality auditors'],
    prerequisites: ['MC-101'],
    category: 'franchise-programs',
    level: 'intermediate',
    deliveryMode: 'offline',
    language: 'English',
    duration: '8 Weeks',
    durationHours: 96,
    lifecycle: 'draft',
    visibility: 'private',
    seoTitle: 'Franchise Mushroom Farm Operations | SporeKart',
    seoDescription: 'Standardized training for franchise mushroom farm operators. 8-week program.',
    slug: 'franchise-mushroom-farm-operations',
    createdBy: 'Mr. Vikram Joshi',
    createdAt: makeDate(10),
    updatedAt: makeDate(0),
    pinned: false,
    favorite: false,
    instructorCount: 0,
    enrollmentCount: 0,
    batchCount: 0,
    moduleCount: 14,
    rating: 0,
    reviewCount: 0,
  },
  {
    id: 'crs-009',
    code: 'MC-201',
    name: 'Advanced Composting & Substrate',
    shortDescription: 'Master the art and science of mushroom substrate preparation.',
    longDescription: 'In-depth exploration of substrate formulation, composting techniques, pasteurization, and supplementation for different mushroom species. Covers both small-scale and industrial methods.',
    learningObjectives: ['Formulate substrates', 'Master composting techniques', 'Optimize pasteurization', 'Supplement substrates', 'Troubleshoot substrate issues'],
    targetAudience: ['Experienced growers', 'Farm managers', 'Substrate producers', 'R&D staff'],
    prerequisites: ['MC-101'],
    category: 'mushroom-cultivation',
    level: 'advanced',
    deliveryMode: 'recorded',
    language: 'English',
    duration: '4 Weeks',
    durationHours: 35,
    lifecycle: 'scheduled',
    visibility: 'public',
    seoTitle: 'Advanced Composting & Substrate Course | SporeKart',
    seoDescription: 'Master mushroom substrate preparation. 4-week advanced recorded program.',
    slug: 'advanced-composting-substrate',
    createdBy: 'Dr. Anand Desai',
    createdAt: makeDate(25),
    updatedAt: makeDate(2),
    publishedAt: makeDate(20),
    pinned: false,
    favorite: false,
    instructorCount: 1,
    enrollmentCount: 45,
    batchCount: 1,
    moduleCount: 6,
    rating: 4.4,
    reviewCount: 12,
  },
  {
    id: 'crs-010',
    code: 'QC-101',
    name: 'Mushroom Quality Control & Food Safety',
    shortDescription: 'Ensure highest quality standards in mushroom production and processing.',
    longDescription: 'Comprehensive training on quality control protocols, food safety standards (FSSAI, HACCP), testing methods, and certification requirements for mushroom production facilities.',
    learningObjectives: ['Implement QC protocols', 'Apply HACCP principles', 'Conduct quality tests', 'Manage documentation', 'Prepare for audits'],
    targetAudience: ['Quality managers', 'Production supervisors', 'Food safety officers', 'Lab technicians'],
    prerequisites: [],
    category: 'value-added-products',
    level: 'intermediate',
    deliveryMode: 'hybrid',
    language: 'English',
    duration: '3 Weeks',
    durationHours: 24,
    lifecycle: 'draft',
    visibility: 'internal',
    seoTitle: 'Mushroom Quality Control Course | SporeKart',
    seoDescription: 'Ensure quality standards in mushroom production. 3-week hybrid program.',
    slug: 'mushroom-quality-control',
    createdBy: 'Dr. Meera Nair',
    createdAt: makeDate(5),
    updatedAt: makeDate(0),
    pinned: false,
    favorite: false,
    instructorCount: 0,
    enrollmentCount: 0,
    batchCount: 0,
    moduleCount: 5,
    rating: 0,
    reviewCount: 0,
  },
  {
    id: 'crs-011',
    code: 'MC-301',
    name: 'Mushroom Disease Management & IPM',
    shortDescription: 'Identify, prevent, and manage mushroom diseases using integrated pest management.',
    longDescription: 'Expert-level training on mushroom disease identification, prevention strategies, biological control, and integrated pest management approaches for commercial operations.',
    learningObjectives: ['Identify common diseases', 'Implement preventive measures', 'Apply biological controls', 'Design IPM programs', 'Manage outbreaks'],
    targetAudience: ['Farm managers', 'Plant protection officers', 'Extension officers', 'Consultants'],
    prerequisites: ['MC-101', 'MC-201'],
    category: 'mushroom-cultivation',
    level: 'advanced',
    deliveryMode: 'live',
    language: 'English',
    duration: '3 Weeks',
    durationHours: 24,
    lifecycle: 'scheduled',
    visibility: 'public',
    seoTitle: 'Mushroom Disease Management Course | SporeKart',
    seoDescription: 'Expert training on mushroom disease management and IPM. 3-week live program.',
    slug: 'mushroom-disease-management',
    createdBy: 'Dr. Rajesh Kumar',
    createdAt: makeDate(40),
    updatedAt: makeDate(3),
    publishedAt: makeDate(35),
    pinned: false,
    favorite: true,
    instructorCount: 2,
    enrollmentCount: 78,
    batchCount: 2,
    moduleCount: 7,
    rating: 4.6,
    reviewCount: 28,
  },
  {
    id: 'crs-012',
    code: 'SP-101',
    name: 'Introduction to Spawn Production',
    shortDescription: 'Foundation course for spawn production techniques and sterile laboratory practices.',
    longDescription: 'Essential training for anyone involved in spawn production. Covers laboratory setup, sterile techniques, strain selection, and quality assessment of spawn.',
    learningObjectives: ['Set up a spawn lab', 'Practice sterile technique', 'Select appropriate strains', 'Assess spawn quality', 'Troubleshoot contamination'],
    targetAudience: ['Lab assistants', 'Spawn technicians', 'Farm staff', 'Students'],
    prerequisites: [],
    category: 'spawn-production',
    level: 'beginner',
    deliveryMode: 'offline',
    language: 'English',
    duration: '2 Weeks',
    durationHours: 20,
    lifecycle: 'published',
    visibility: 'public',
    seoTitle: 'Introduction to Spawn Production Course | SporeKart',
    seoDescription: 'Foundation training for spawn production. 2-week offline program.',
    slug: 'introduction-spawn-production',
    createdBy: 'Prof. Sunita Patel',
    createdAt: makeDate(200),
    updatedAt: makeDate(10),
    publishedAt: makeDate(195),
    pinned: false,
    favorite: false,
    instructorCount: 2,
    enrollmentCount: 320,
    batchCount: 8,
    moduleCount: 5,
    rating: 4.5,
    reviewCount: 105,
  },
  {
    id: 'crs-013',
    code: 'AI-101',
    name: 'AI-Powered Mushroom Farm Management',
    shortDescription: 'Leverage artificial intelligence for modern mushroom farm management.',
    longDescription: 'Cutting-edge course on applying AI and machine learning to mushroom farming including predictive analytics, automated monitoring, smart climate control, and yield optimization.',
    learningObjectives: ['Apply AI to farm management', 'Use predictive analytics', 'Implement smart monitoring', 'Optimize climate control', 'Analyze yield data'],
    targetAudience: ['Tech-savvy farmers', 'Agritech professionals', 'Farm managers', 'Innovation leads'],
    prerequisites: [],
    category: 'future-ai-courses',
    level: 'future-ai-assisted',
    deliveryMode: 'future-vr',
    language: 'English',
    duration: 'TBD',
    durationHours: 0,
    lifecycle: 'future-version',
    visibility: 'private',
    seoTitle: 'AI-Powered Mushroom Farm Management | SporeKart',
    seoDescription: 'Leverage AI for mushroom farm management. Future course.',
    slug: 'ai-mushroom-farm-management',
    createdBy: 'Dr. Anand Desai',
    createdAt: makeDate(3),
    updatedAt: makeDate(0),
    pinned: false,
    favorite: false,
    instructorCount: 0,
    enrollmentCount: 0,
    batchCount: 0,
    moduleCount: 0,
    rating: 0,
    reviewCount: 0,
  },
  {
    id: 'crs-014',
    code: 'MC-401',
    name: 'Mushroom Harvest & Post-Harvest Technology',
    shortDescription: 'Optimize harvest techniques and post-harvest handling for maximum quality and shelf life.',
    longDescription: 'Advanced training on harvest timing, handling methods, cold chain management, packaging technologies, and shelf-life extension for fresh mushrooms.',
    learningObjectives: ['Optimize harvest timing', 'Implement cold chain', 'Apply packaging technologies', 'Extend shelf life', 'Reduce post-harvest losses'],
    targetAudience: ['Harvest managers', 'Post-harvest specialists', 'Supply chain managers', 'Export managers'],
    prerequisites: ['MC-101'],
    category: 'mushroom-cultivation',
    level: 'advanced',
    deliveryMode: 'recorded',
    language: 'English',
    duration: '2 Weeks',
    durationHours: 16,
    lifecycle: 'archived',
    visibility: 'public',
    seoTitle: 'Mushroom Harvest & Post-Harvest Technology | SporeKart',
    seoDescription: 'Optimize mushroom harvest and post-harvest handling. 2-week recorded program.',
    slug: 'mushroom-harvest-post-harvest',
    createdBy: 'Dr. Meera Nair',
    createdAt: makeDate(300),
    updatedAt: makeDate(180),
    publishedAt: makeDate(295),
    pinned: false,
    favorite: false,
    instructorCount: 2,
    enrollmentCount: 156,
    batchCount: 4,
    moduleCount: 5,
    rating: 4.3,
    reviewCount: 42,
  },
  {
    id: 'crs-015',
    code: 'BT-201',
    name: 'Mushroom Export & International Trade',
    shortDescription: 'Navigate the complexities of international mushroom trade and export regulations.',
    longDescription: 'Comprehensive training on export documentation, international quality standards, logistics, market access, and trade regulations for mushroom products in global markets.',
    learningObjectives: ['Understand export regulations', 'Prepare export documentation', 'Meet international standards', 'Manage international logistics', 'Access global markets'],
    targetAudience: ['Export managers', 'Trade professionals', 'Business owners', 'Compliance officers'],
    prerequisites: ['BT-101'],
    category: 'business-training',
    level: 'advanced',
    deliveryMode: 'online',
    language: 'English',
    duration: '4 Weeks',
    durationHours: 32,
    lifecycle: 'retired',
    visibility: 'public',
    seoTitle: 'Mushroom Export & International Trade | SporeKart',
    seoDescription: 'Navigate international mushroom trade. 4-week online program.',
    slug: 'mushroom-export-international-trade',
    createdBy: 'Mr. Vikram Joshi',
    createdAt: makeDate(400),
    updatedAt: makeDate(365),
    publishedAt: makeDate(395),
    pinned: false,
    favorite: false,
    instructorCount: 2,
    enrollmentCount: 89,
    batchCount: 3,
    moduleCount: 6,
    rating: 4.2,
    reviewCount: 31,
  },
];

export function getCourseById(id: string): Course | undefined {
  return MOCK_COURSES.find((c) => c.id === id);
}

export function getCoursesByLifecycle(lifecycle: CourseLifecycle): Course[] {
  return MOCK_COURSES.filter((c) => c.lifecycle === lifecycle);
}

export function getFilteredCourses(
  courses: Course[],
  filters: {
    search?: string;
    status?: string;
    category?: string;
    level?: string;
    language?: string;
    deliveryMode?: string;
    type?: string;
  }
): Course[] {
  let result = [...courses];
  if (filters.search) {
    const q = filters.search.toLowerCase();
    result = result.filter(
      (c) =>
        c.name.toLowerCase().includes(q) ||
        c.code.toLowerCase().includes(q) ||
        c.shortDescription.toLowerCase().includes(q)
    );
  }
  if (filters.status) result = result.filter((c) => c.lifecycle === filters.status);
  if (filters.category) result = result.filter((c) => c.category === filters.category);
  if (filters.level) result = result.filter((c) => c.level === filters.level);
  if (filters.language) result = result.filter((c) => c.language === filters.language);
  if (filters.deliveryMode) result = result.filter((c) => c.deliveryMode === filters.deliveryMode);
  return result;
}

export function computeCourseStats(courses: Course[]): CourseDashboardStats {
  const published = courses.filter((c) => c.lifecycle === 'published');
  const drafts = courses.filter((c) => c.lifecycle === 'draft');
  const archived = courses.filter((c) => c.lifecycle === 'archived');
  const pending = courses.filter((c) => c.lifecycle === 'pending-review');
  const scheduled = courses.filter((c) => c.lifecycle === 'scheduled');
  const categoryCounts: Record<string, number> = {};
  courses.forEach((c) => {
    categoryCounts[c.category] = (categoryCounts[c.category] || 0) + 1;
  });
  const popular = Object.entries(categoryCounts)
    .sort(([, a], [, b]) => b - a)
    .slice(0, 5)
    .map(([cat, count]) => ({ category: cat as TrainingCategory, count }));

  const now = new Date();
  const recentThreshold = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
  const recentlyUpdated = courses.filter((c) => new Date(c.updatedAt) >= recentThreshold).length;

  return {
    totalCourses: courses.length,
    publishedCourses: published.length,
    draftCourses: drafts.length,
    archivedCourses: archived.length,
    pendingReview: pending.length,
    upcomingLaunches: scheduled.length,
    popularCategories: popular,
    recentlyUpdated,
  };
}
