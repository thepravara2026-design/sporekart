export type TaxonomySection =
  | 'overview'
  | 'categories'
  | 'tags'
  | 'topics'
  | 'skills'
  | 'competencies'
  | 'languages'
  | 'delivery'
  | 'explorer'
  | 'relationships'
  | 'discovery'
  | 'learning-paths';

export type CategoryStatus = 'active' | 'draft' | 'archived';
export type CategoryVisibility = 'public' | 'private' | 'internal';

export interface TaxonomyNode {
  id: string;
  name: string;
  description: string;
  icon: string;
  color: string;
  status: CategoryStatus;
  visibility: CategoryVisibility;
  order: number;
  featured: boolean;
  parentId: string | null;
  children: TaxonomyNode[];
  courseCount: number;
  tags: string[];
}

export interface TaxonomyTag {
  id: string;
  label: string;
  type: 'course' | 'industry' | 'technology' | 'business' | 'skill' | 'agriculture' | 'market';
  usageCount: number;
  trending: boolean;
  popular: boolean;
  suggested: boolean;
}

export interface TopicNode {
  id: string;
  name: string;
  group: string;
  description: string;
  priority: 'low' | 'medium' | 'high' | 'critical';
  order: number;
  dependsOn: string[];
  relatedTopics: string[];
}

export interface SkillLevel {
  id: string;
  label: string;
  rank: number;
  description: string;
}

export interface Competency {
  id: string;
  name: string;
  category: 'technical' | 'practical' | 'business' | 'laboratory' | 'equipment' | 'quality' | 'marketing' | 'entrepreneurship' | 'farm';
  description: string;
  level: 'foundational' | 'intermediate' | 'advanced';
}

export interface LanguageOption {
  id: string;
  code: string;
  label: string;
  nativeLabel: string;
  rtl: boolean;
}

export interface DeliveryMode {
  id: string;
  label: string;
  description: string;
  future: boolean;
}

export const PANEL_LABELS: Record<TaxonomySection, string> = {
  overview: 'Overview',
  categories: 'Categories',
  tags: 'Tags',
  topics: 'Topics',
  skills: 'Skills',
  competencies: 'Competencies',
  languages: 'Languages',
  delivery: 'Delivery Modes',
  explorer: 'Hierarchy Explorer',
  relationships: 'Relationships',
  discovery: 'Discovery',
  'learning-paths': 'Learning Paths',
};

export const TAXONOMY_SECTIONS: TaxonomySection[] = [
  'overview', 'categories', 'tags', 'topics', 'skills', 'competencies',
  'languages', 'delivery', 'explorer', 'relationships', 'discovery', 'learning-paths',
];

export const MOCK_TAXONOMY_TREE: TaxonomyNode[] = [
  {
    id: 'cat-agriculture',
    name: 'Agriculture',
    description: 'All agricultural training programs',
    icon: '\uD83C\uDF31',
    color: '#2e7d32',
    status: 'active',
    visibility: 'public',
    order: 1,
    featured: true,
    parentId: null,
    courseCount: 48,
    tags: ['agriculture', 'farming'],
    children: [
      {
        id: 'cat-mushroom',
        name: 'Mushroom Cultivation',
        description: 'Mushroom growing and processing',
        icon: '\uD83C\uDF44',
        color: '#558b2f',
        status: 'active',
        visibility: 'public',
        order: 1,
        featured: true,
        parentId: 'cat-agriculture',
        courseCount: 24,
        tags: ['mushroom', 'cultivation'],
        children: [
          {
            id: 'cat-oyster',
            name: 'Oyster Mushroom',
            description: 'Oyster mushroom specialization',
            icon: '\uD83C\uDF42',
            color: '#7cb342',
            status: 'active',
            visibility: 'public',
            order: 1,
            featured: false,
            parentId: 'cat-mushroom',
            courseCount: 8,
            tags: ['oyster', 'pleurotus'],
            children: [
              {
                id: 'cat-spawn',
                name: 'Spawn Production',
                description: 'Spawn and substrate production',
                icon: '\uD83E\uDDEC',
                color: '#9ccc65',
                status: 'active',
                visibility: 'public',
                order: 1,
                featured: false,
                parentId: 'cat-oyster',
                courseCount: 4,
                tags: ['spawn', 'substrate'],
                children: [
                  {
                    id: 'cat-adv-spawn',
                    name: 'Advanced Spawn Techniques',
                    description: 'Advanced laboratory spawn techniques',
                    icon: '\uD83E\uDDEA',
                    color: '#c0ca33',
                    status: 'active',
                    visibility: 'internal',
                    order: 1,
                    featured: false,
                    parentId: 'cat-spawn',
                    courseCount: 2,
                    tags: ['advanced', 'laboratory'],
                    children: [
                      {
                        id: 'cat-lab-ops',
                        name: 'Laboratory Operations',
                        description: 'Sterile lab operations and quality control',
                        icon: '\uD83E\uDDEA',
                        color: '#fdd835',
                        status: 'draft',
                        visibility: 'internal',
                        order: 1,
                        featured: false,
                        parentId: 'cat-adv-spawn',
                        courseCount: 0,
                        tags: ['lab', 'quality'],
                        children: [],
                      },
                    ],
                  },
                ],
              },
            ],
          },
        ],
      },
    ],
  },
  {
    id: 'cat-business',
    name: 'Business Training',
    description: 'Entrepreneurship and business skills',
    icon: '\uD83D\uDCBC',
    color: '#1565c0',
    status: 'active',
    visibility: 'public',
    order: 2,
    featured: true,
    parentId: null,
    courseCount: 18,
    tags: ['business', 'entrepreneurship'],
    children: [
      {
        id: 'cat-marketing',
        name: 'Marketing',
        description: 'Agricultural product marketing',
        icon: '\uD83D\uDCC8',
        color: '#1976d2',
        status: 'active',
        visibility: 'public',
        order: 1,
        featured: false,
        parentId: 'cat-business',
        courseCount: 6,
        tags: ['marketing', 'sales'],
        children: [],
      },
    ],
  },
];

export const MOCK_TAGS: TaxonomyTag[] = [
  { id: 'tag-mushroom', label: 'mushroom', type: 'agriculture', usageCount: 42, trending: true, popular: true, suggested: false },
  { id: 'tag-spawn', label: 'spawn', type: 'technology', usageCount: 28, trending: true, popular: true, suggested: false },
  { id: 'tag-organic', label: 'organic', type: 'industry', usageCount: 19, trending: false, popular: true, suggested: false },
  { id: 'tag-lab', label: 'laboratory', type: 'skill', usageCount: 11, trending: false, popular: false, suggested: true },
  { id: 'tag-startup', label: 'startup', type: 'business', usageCount: 15, trending: true, popular: false, suggested: false },
  { id: 'tag-quality', label: 'quality-control', type: 'skill', usageCount: 9, trending: false, popular: false, suggested: true },
  { id: 'tag-farm', label: 'farm-management', type: 'agriculture', usageCount: 22, trending: false, popular: true, suggested: false },
  { id: 'tag-vr', label: 'virtual-reality', type: 'technology', usageCount: 2, trending: false, popular: false, suggested: true },
];

export const MOCK_TOPICS: TopicNode[] = [
  { id: 'topic-bio', name: 'Mushroom Biology', group: 'Foundations', description: 'Lifecycle and biology of fungi', priority: 'high', order: 1, dependsOn: [], relatedTopics: ['topic-lab', 'topic-substrate'] },
  { id: 'topic-substrate', name: 'Substrate Preparation', group: 'Cultivation', description: 'Preparing growing substrates', priority: 'high', order: 2, dependsOn: ['topic-bio'], relatedTopics: ['topic-spawn'] },
  { id: 'topic-spawn', name: 'Spawn Inoculation', group: 'Cultivation', description: 'Inoculating spawn into substrate', priority: 'high', order: 3, dependsOn: ['topic-substrate'], relatedTopics: ['topic-incubation'] },
  { id: 'topic-incubation', name: 'Incubation & Fruiting', group: 'Cultivation', description: 'Environmental control for fruiting', priority: 'medium', order: 4, dependsOn: ['topic-spawn'], relatedTopics: [] },
  { id: 'topic-lab', name: 'Laboratory Techniques', group: 'Advanced', description: 'Aseptic laboratory operations', priority: 'critical', order: 5, dependsOn: ['topic-bio'], relatedTopics: ['topic-quality'] },
  { id: 'topic-quality', name: 'Quality Control', group: 'Advanced', description: 'Quality assurance and testing', priority: 'medium', order: 6, dependsOn: ['topic-lab'], relatedTopics: [] },
];

export const MOCK_SKILLS: SkillLevel[] = [
  { id: 'skill-beginner', label: 'Beginner', rank: 1, description: 'New to the subject' },
  { id: 'skill-intermediate', label: 'Intermediate', rank: 2, description: 'Some practical experience' },
  { id: 'skill-advanced', label: 'Advanced', rank: 3, description: 'Proficient practitioner' },
  { id: 'skill-expert', label: 'Expert', rank: 4, description: 'Deep domain expertise' },
  { id: 'skill-master', label: 'Master', rank: 5, description: 'Authority in the field' },
  { id: 'skill-commercial', label: 'Commercial', rank: 6, description: 'Commercial production ready' },
  { id: 'skill-professional', label: 'Professional', rank: 7, description: 'Industry professional' },
  { id: 'skill-trainer', label: 'Trainer', rank: 8, description: 'Certified to train others' },
  { id: 'skill-consultant', label: 'Consultant', rank: 9, description: 'Advisory capability' },
];

export const MOCK_COMPETENCIES: Competency[] = [
  { id: 'comp-technical', name: 'Technical Skills', category: 'technical', description: 'Hands-on technical ability', level: 'intermediate' },
  { id: 'comp-practical', name: 'Practical Skills', category: 'practical', description: 'Applied field practice', level: 'foundational' },
  { id: 'comp-business', name: 'Business Skills', category: 'business', description: 'Commercial and business acumen', level: 'intermediate' },
  { id: 'comp-lab', name: 'Laboratory Skills', category: 'laboratory', description: 'Aseptic lab operations', level: 'advanced' },
  { id: 'comp-equipment', name: 'Equipment Handling', category: 'equipment', description: 'Safe equipment operation', level: 'foundational' },
  { id: 'comp-quality', name: 'Quality Control', category: 'quality', description: 'QA and testing', level: 'advanced' },
  { id: 'comp-marketing', name: 'Marketing', category: 'marketing', description: 'Product marketing', level: 'intermediate' },
  { id: 'comp-entrepreneurship', name: 'Entrepreneurship', category: 'entrepreneurship', description: 'Venture creation', level: 'intermediate' },
  { id: 'comp-farm', name: 'Farm Management', category: 'farm', description: 'Operational farm management', level: 'advanced' },
];

export const MOCK_LANGUAGES: LanguageOption[] = [
  { id: 'lang-en', code: 'en', label: 'English', nativeLabel: 'English', rtl: false },
  { id: 'lang-kn', code: 'kn', label: 'Kannada', nativeLabel: 'ಕನ್ನಡ', rtl: false },
  { id: 'lang-hi', code: 'hi', label: 'Hindi', nativeLabel: 'हिन्दी', rtl: false },
  { id: 'lang-ta', code: 'ta', label: 'Tamil', nativeLabel: 'தமிழ்', rtl: false },
  { id: 'lang-te', code: 'te', label: 'Telugu', nativeLabel: 'తెలుగు', rtl: false },
  { id: 'lang-ml', code: 'ml', label: 'Malayalam', nativeLabel: 'മലയാളം', rtl: false },
  { id: 'lang-mr', code: 'mr', label: 'Marathi', nativeLabel: 'मराठी', rtl: false },
];

export const MOCK_DELIVERY: DeliveryMode[] = [
  { id: 'del-offline', label: 'Offline', description: 'In-person training', future: false },
  { id: 'del-online', label: 'Online', description: 'Live online sessions', future: false },
  { id: 'del-hybrid', label: 'Hybrid', description: 'Mixed online and offline', future: false },
  { id: 'del-workshop', label: 'Workshop', description: 'Hands-on workshop', future: false },
  { id: 'del-bootcamp', label: 'Bootcamp', description: 'Intensive short program', future: false },
  { id: 'del-selfpaced', label: 'Self-paced', description: 'Self-guided learning', future: false },
  { id: 'del-instructor', label: 'Instructor-led', description: 'Trainer-led sessions', future: false },
  { id: 'del-corporate', label: 'Corporate', description: 'Enterprise training', future: false },
  { id: 'del-institutional', label: 'Institutional', description: 'Academic institution', future: false },
  { id: 'del-franchise', label: 'Franchise', description: 'Franchise academy', future: false },
  { id: 'del-vr', label: 'VR', description: 'Virtual reality training', future: true },
  { id: 'del-ar', label: 'AR', description: 'Augmented reality training', future: true },
];

export const TAG_TYPE_OPTIONS = [
  { value: 'course', label: 'Course' },
  { value: 'industry', label: 'Industry' },
  { value: 'technology', label: 'Technology' },
  { value: 'business', label: 'Business' },
  { value: 'skill', label: 'Skill' },
  { value: 'agriculture', label: 'Agriculture' },
  { value: 'market', label: 'Market' },
];

export const CATEGORY_STATUS_OPTIONS = [
  { value: 'active', label: 'Active' },
  { value: 'draft', label: 'Draft' },
  { value: 'archived', label: 'Archived' },
];

export const CATEGORY_VISIBILITY_OPTIONS = [
  { value: 'public', label: 'Public' },
  { value: 'private', label: 'Private' },
  { value: 'internal', label: 'Internal' },
];

export const SKILL_CATEGORY_OPTIONS = [
  { value: 'technical', label: 'Technical' },
  { value: 'practical', label: 'Practical' },
  { value: 'business', label: 'Business' },
  { value: 'laboratory', label: 'Laboratory' },
  { value: 'equipment', label: 'Equipment' },
  { value: 'quality', label: 'Quality' },
  { value: 'marketing', label: 'Marketing' },
  { value: 'entrepreneurship', label: 'Entrepreneurship' },
  { value: 'farm', label: 'Farm' },
];
