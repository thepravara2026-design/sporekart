export type CurriculumSection =
  | 'overview'
  | 'builder'
  | 'module'
  | 'lesson'
  | 'topic'
  | 'activities'
  | 'templates'
  | 'learning-paths'
  | 'completion'
  | 'resources'
  | 'dragdrop'
  | 'preview';

export type CurriculumNodeType =
  | 'course'
  | 'curriculum'
  | 'module'
  | 'lesson'
  | 'topic'
  | 'activity'
  | 'assignment'
  | 'assessment'
  | 'completion';

export type NodeStatus = 'draft' | 'published' | 'archived';

export interface CurriculumNode {
  id: string;
  type: CurriculumNodeType;
  name: string;
  description: string;
  status: NodeStatus;
  durationHours: number;
  order: number;
  learningGoal?: string;
  prerequisites?: string[];
  difficulty?: string;
  resources?: string[];
  lessonType?: string;
  activityType?: string;
  children: CurriculumNode[];
}

export interface CurriculumTemplate {
  id: string;
  name: string;
  description: string;
  level: 'beginner' | 'intermediate' | 'advanced' | 'corporate' | 'institution' | 'workshop' | 'certification' | 'franchise' | 'government';
  moduleCount: number;
  future: boolean;
}

export const PANEL_LABELS: Record<CurriculumSection, string> = {
  overview: 'Overview',
  builder: 'Curriculum Builder',
  module: 'Module Editor',
  lesson: 'Lesson Overview',
  topic: 'Topic Organization',
  activities: 'Learning Activities',
  templates: 'Templates',
  'learning-paths': 'Learning Paths',
  completion: 'Completion Rules',
  resources: 'Resources',
  dragdrop: 'Drag & Drop',
  preview: 'Live Preview',
};

export const CURRICULUM_SECTIONS: CurriculumSection[] = [
  'overview', 'builder', 'module', 'lesson', 'topic', 'activities',
  'templates', 'learning-paths', 'completion', 'resources', 'dragdrop', 'preview',
];

export const NODE_TYPE_LABELS: Record<CurriculumNodeType, string> = {
  course: 'Course',
  curriculum: 'Curriculum',
  module: 'Module',
  lesson: 'Lesson',
  topic: 'Topic',
  activity: 'Activity',
  assignment: 'Assignment',
  assessment: 'Assessment',
  completion: 'Completion',
};

export const NODE_TYPE_ICONS: Record<CurriculumNodeType, string> = {
  course: '\uD83C\uDFDB\uFE0F',
  curriculum: '\uD83D\uDCDA',
  module: '\uD83D\uDCC2',
  lesson: '\uD83C\uDFAF',
  topic: '\uD83D\uDCDD',
  activity: '\uD83E\uDDE0',
  assignment: '\uD83D\uDCC3',
  assessment: '\uD83C\uDFC5',
  completion: '\uD83C\uDFC1',
};

export const LESSON_TYPES = [
  'Theory', 'Practical', 'Workshop', 'Video', 'Reading', 'Discussion',
  'Case Study', 'Laboratory', 'Business Practice', 'Live Class', 'Webinar', 'AI Lesson',
];

export const ACTIVITY_TYPES = [
  'Reading', 'Video', 'Practical Task', 'Observation', 'Field Visit',
  'Laboratory Practice', 'Discussion', 'Exercise', 'Project', 'AI Activity',
];

export const TOPIC_PRIORITIES = ['low', 'medium', 'high', 'critical'] as const;

export const TEMPLATE_OPTIONS = [
  { value: 'beginner', label: 'Beginner' },
  { value: 'intermediate', label: 'Intermediate' },
  { value: 'advanced', label: 'Advanced' },
  { value: 'corporate', label: 'Corporate' },
  { value: 'institution', label: 'Institution' },
  { value: 'workshop', label: 'Workshop' },
  { value: 'certification', label: 'Certification' },
  { value: 'franchise', label: 'Franchise' },
  { value: 'government', label: 'Government' },
];

export const STATUS_OPTIONS = [
  { value: 'draft', label: 'Draft' },
  { value: 'published', label: 'Published' },
  { value: 'archived', label: 'Archived' },
];

export const DIFFICULTY_OPTIONS = [
  { value: 'beginner', label: 'Beginner' },
  { value: 'intermediate', label: 'Intermediate' },
  { value: 'advanced', label: 'Advanced' },
  { value: 'all-levels', label: 'All Levels' },
];

export const MOCK_TEMPLATES: CurriculumTemplate[] = [
  { id: 'tpl-beginner', name: 'Beginner Template', description: 'Foundational structure for new learners', level: 'beginner', moduleCount: 4, future: false },
  { id: 'tpl-intermediate', name: 'Intermediate Template', description: 'Structured multi-module program', level: 'intermediate', moduleCount: 6, future: false },
  { id: 'tpl-advanced', name: 'Advanced Template', description: 'Deep technical curriculum', level: 'advanced', moduleCount: 8, future: false },
  { id: 'tpl-corporate', name: 'Corporate Template', description: 'Enterprise training framework', level: 'corporate', moduleCount: 5, future: false },
  { id: 'tpl-institution', name: 'Institution Template', description: 'Academic program structure', level: 'institution', moduleCount: 7, future: false },
  { id: 'tpl-workshop', name: 'Workshop Template', description: 'Hands-on intensive', level: 'workshop', moduleCount: 3, future: false },
  { id: 'tpl-certification', name: 'Certification Template', description: 'Certification-ready curriculum', level: 'certification', moduleCount: 6, future: false },
  { id: 'tpl-franchise', name: 'Franchise Template', description: 'Academy franchise program', level: 'franchise', moduleCount: 5, future: false },
  { id: 'tpl-government', name: 'Government Training Template', description: 'Skill mission program', level: 'government', moduleCount: 8, future: true },
];

export const MOCK_CURRICULUM_TREE: CurriculumNode[] = [
  {
    id: 'cur-mushroom',
    type: 'curriculum',
    name: 'Mushroom Cultivation Mastery',
    description: 'Complete cultivation curriculum',
    status: 'published',
    durationHours: 40,
    order: 1,
    children: [
      {
        id: 'mod-1',
        type: 'module',
        name: 'Module 1: Foundations',
        description: 'Introduction to mushroom biology',
        status: 'published',
        durationHours: 8,
        order: 1,
        learningGoal: 'Understand fungal biology',
        prerequisites: [],
        difficulty: 'beginner',
        resources: ['syl-1'],
        children: [
          {
            id: 'les-1-1',
            type: 'lesson',
            name: 'Lesson 1.1: Mushroom Biology',
            description: 'Lifecycle of fungi',
            status: 'published',
            durationHours: 2,
            order: 1,
            lessonType: 'Theory',
            children: [
              {
                id: 'top-1-1-1',
                type: 'topic',
                name: 'Topic: Fungal Cell Structure',
                description: 'Cell anatomy',
                status: 'published',
                durationHours: 1,
                order: 1,
                learningGoal: 'Identify cell parts',
                children: [
                  {
                    id: 'act-1-1-1-1',
                    type: 'activity',
                    name: 'Activity: Diagram Labeling',
                    description: 'Label cell diagram',
                    status: 'published',
                    durationHours: 0.5,
                    order: 1,
                    activityType: 'Exercise',
                    children: [],
                  },
                ],
              },
            ],
          },
          {
            id: 'les-1-2',
            type: 'lesson',
            name: 'Lesson 1.2: Substrate Basics',
            description: 'Substrate materials',
            status: 'published',
            durationHours: 2,
            order: 2,
            lessonType: 'Practical',
            children: [],
          },
        ],
      },
      {
        id: 'mod-2',
        type: 'module',
        name: 'Module 2: Spawn Production',
        description: 'Spawn and substrate production',
        status: 'draft',
        durationHours: 12,
        order: 2,
        learningGoal: 'Produce quality spawn',
        prerequisites: ['mod-1'],
        difficulty: 'intermediate',
        resources: ['sop-1'],
        children: [
          {
            id: 'les-2-1',
            type: 'lesson',
            name: 'Lesson 2.1: Spawn Inoculation',
            description: 'Inoculation techniques',
            status: 'draft',
            durationHours: 3,
            order: 1,
            lessonType: 'Laboratory',
            children: [],
          },
          {
            id: 'ass-2-1',
            type: 'assessment',
            name: 'Assessment: Spawn Quality',
            description: 'Practical assessment',
            status: 'draft',
            durationHours: 2,
            order: 2,
            children: [],
          },
        ],
      },
      {
        id: 'cmp-1',
        type: 'completion',
        name: 'Completion & Certificate',
        description: 'Final completion rule',
        status: 'published',
        durationHours: 0,
        order: 99,
        children: [],
      },
    ],
  },
];
