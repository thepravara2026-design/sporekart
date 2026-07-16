export type ResourceSection =
  | 'overview'
  | 'library'
  | 'collections'
  | 'folders'
  | 'favorites'
  | 'recent'
  | 'archived'
  | 'preview'
  | 'linking'
  | 'storage';

export type ResourceView = 'grid' | 'list' | 'compact' | 'table' | 'tree';

export type ResourceType =
  | 'pdf' | 'video' | 'image' | 'presentation' | 'spreadsheet' | 'word'
  | 'research' | 'journal' | 'equipment-manual' | 'gov-guideline'
  | 'cultivation-sop' | 'spawn-sop' | 'safety-sop' | 'business-template'
  | 'marketing-asset' | 'interactive' | 'vr' | 'ar';

export type ResourceStatus = 'active' | 'draft' | 'archived';
export type ResourceVisibility = 'public' | 'private' | 'internal';

export interface ResourceItem {
  id: string;
  name: string;
  code: string;
  description: string;
  type: ResourceType;
  category: string;
  subcategory: string;
  tags: string[];
  language: string;
  author: string;
  department: string;
  createdDate: string;
  updatedDate: string;
  version: string;
  status: ResourceStatus;
  visibility: ResourceVisibility;
  color: string;
  favorite: boolean;
  pinned: boolean;
  starred: boolean;
  usageCount: number;
  fileSize: string;
  folderId: string | null;
}

export interface ResourceFolder {
  id: string;
  name: string;
  parentId: string | null;
  children: ResourceFolder[];
}

export interface ResourceCollection {
  id: string;
  name: string;
  description: string;
  icon: string;
  resourceIds: string[];
}

export interface ResourceFilters {
  type: string;
  category: string;
  department: string;
  language: string;
  status: string;
  author: string;
  visibility: string;
}

export const PANEL_LABELS: Record<ResourceSection, string> = {
  overview: 'Overview',
  library: 'Library Explorer',
  collections: 'Collections',
  folders: 'Folders',
  favorites: 'Favorites',
  recent: 'Recent',
  archived: 'Archived',
  preview: 'Preview',
  linking: 'Linking',
  storage: 'Storage',
};

export const RESOURCE_SECTIONS: ResourceSection[] = [
  'overview', 'library', 'collections', 'folders', 'favorites', 'recent', 'archived', 'preview', 'linking', 'storage',
];

export const RESOURCE_TYPE_LABELS: Record<ResourceType, string> = {
  pdf: 'PDF',
  video: 'Video',
  image: 'Image',
  presentation: 'Presentation',
  spreadsheet: 'Spreadsheet',
  word: 'Word Document',
  research: 'Research Paper',
  journal: 'Scientific Journal',
  'equipment-manual': 'Equipment Manual',
  'gov-guideline': 'Government Guideline',
  'cultivation-sop': 'Cultivation SOP',
  'spawn-sop': 'Spawn SOP',
  'safety-sop': 'Safety SOP',
  'business-template': 'Business Template',
  'marketing-asset': 'Marketing Asset',
  interactive: 'Interactive Content',
  vr: 'VR Asset',
  ar: 'AR Asset',
};

export const RESOURCE_TYPE_ICONS: Record<ResourceType, string> = {
  pdf: '\uD83D\uDCC4',
  video: '\uD83C\uDFA5',
  image: '\uD83D\uDDBC',
  presentation: '\uD83D\uDCCA',
  spreadsheet: '\uD83D\uDCC0',
  word: '\uD83D\uDCC3',
  research: '\uD83D\uDD2C',
  journal: '\uD83D\uDCDA',
  'equipment-manual': '\uD83D\uDEE0',
  'gov-guideline': '\uD83D\uDCC4',
  'cultivation-sop': '\uD83E\uDDE0',
  'spawn-sop': '\uD83E\uDDEC',
  'safety-sop': '\uD83D\uDEF1',
  'business-template': '\uD83D\uDCCB',
  'marketing-asset': '\uD83D\uCEB1',
  interactive: '\uD83D\uDDF3',
  vr: '\uD83D\uDC40',
  ar: '\uD83D\uDD2D',
};

export const STATUS_OPTIONS = [
  { value: 'active', label: 'Active' },
  { value: 'draft', label: 'Draft' },
  { value: 'archived', label: 'Archived' },
];

export const VISIBILITY_OPTIONS = [
  { value: 'public', label: 'Public' },
  { value: 'private', label: 'Private' },
  { value: 'internal', label: 'Internal' },
];

export const LANGUAGE_OPTIONS = [
  { value: 'English', label: 'English' },
  { value: 'Kannada', label: 'Kannada' },
  { value: 'Hindi', label: 'Hindi' },
  { value: 'Tamil', label: 'Tamil' },
  { value: 'Telugu', label: 'Telugu' },
  { value: 'Marathi', label: 'Marathi' },
];

export const DEPARTMENT_OPTIONS = [
  { value: 'Training', label: 'Training' },
  { value: 'Laboratory', label: 'Laboratory' },
  { value: 'Research', label: 'Research' },
  { value: 'Operations', label: 'Operations' },
  { value: 'Marketing', label: 'Marketing' },
  { value: 'Quality', label: 'Quality Control' },
];

export const CATEGORY_OPTIONS = [
  { value: 'Cultivation', label: 'Cultivation' },
  { value: 'Spawn Production', label: 'Spawn Production' },
  { value: 'Laboratory', label: 'Laboratory' },
  { value: 'Business', label: 'Business' },
  { value: 'Safety', label: 'Safety' },
  { value: 'Government', label: 'Government' },
];

export const MOCK_FOLDERS: ResourceFolder[] = [
  {
    id: 'f-cult',
    name: 'Cultivation',
    parentId: null,
    children: [
      { id: 'f-cult-sop', name: 'SOPs', parentId: 'f-cult', children: [] },
      { id: 'f-cult-vid', name: 'Videos', parentId: 'f-cult', children: [] },
    ],
  },
  {
    id: 'f-lab',
    name: 'Laboratory',
    parentId: null,
    children: [
      { id: 'f-lab-man', name: 'Manuals', parentId: 'f-lab', children: [] },
    ],
  },
  {
    id: 'f-biz',
    name: 'Business',
    parentId: null,
    children: [],
  },
];

export const MOCK_COLLECTIONS: ResourceCollection[] = [
  { id: 'col-starter', name: 'Mushroom Starter Kit', description: 'Essentials for beginners', icon: '\uD83C\uDF44', resourceIds: ['r-1', 'r-2', 'r-3'] },
  { id: 'col-spawn', name: 'Spawn Production Library', description: 'Advanced spawn resources', icon: '\uD83E\uDDEC', resourceIds: ['r-4', 'r-5'] },
  { id: 'col-safety', name: 'Safety & Compliance', description: 'Safety SOPs and guidelines', icon: '\uD83D\uDEF1', resourceIds: ['r-6', 'r-7'] },
  { id: 'col-research', name: 'Research Papers', description: 'Academic references', icon: '\uD83D\uDD2C', resourceIds: ['r-8', 'r-9'] },
];

export const MOCK_RESOURCES: ResourceItem[] = [
  { id: 'r-1', name: 'Mushroom Cultivation Guide', code: 'MC-G-001', description: 'Complete beginner cultivation manual', type: 'pdf', category: 'Cultivation', subcategory: 'Guide', tags: ['mushroom', 'beginner'], language: 'English', author: 'Dr. Rao', department: 'Training', createdDate: '2025-01-10', updatedDate: '2025-06-01', version: '2.1', status: 'active', visibility: 'public', color: '#2e7d32', favorite: true, pinned: true, starred: false, usageCount: 142, fileSize: '4.2 MB', folderId: 'f-cult' },
  { id: 'r-2', name: 'Substrate Preparation Video', code: 'MC-V-002', description: 'Step-by-step substrate prep', type: 'video', category: 'Cultivation', subcategory: 'Video', tags: ['substrate', 'video'], language: 'Kannada', author: 'Trainer Kumar', department: 'Training', createdDate: '2025-02-15', updatedDate: '2025-05-20', version: '1.0', status: 'active', visibility: 'public', color: '#558b2f', favorite: false, pinned: false, starred: true, usageCount: 98, fileSize: '156 MB', folderId: 'f-cult-vid' },
  { id: 'r-3', name: 'Cultivation SOP', code: 'CUL-SOP-001', description: 'Standard operating procedure', type: 'cultivation-sop', category: 'Cultivation', subcategory: 'SOP', tags: ['sop', 'cultivation'], language: 'English', author: 'Lab Team', department: 'Quality', createdDate: '2025-01-05', updatedDate: '2025-06-10', version: '3.0', status: 'active', visibility: 'internal', color: '#7cb342', favorite: true, pinned: false, starred: false, usageCount: 76, fileSize: '560 KB', folderId: 'f-cult-sop' },
  { id: 'r-4', name: 'Spawn Production Manual', code: 'SP-M-001', description: 'Advanced spawn techniques', type: 'equipment-manual', category: 'Spawn Production', subcategory: 'Manual', tags: ['spawn', 'advanced'], language: 'English', author: 'Dr. Rao', department: 'Laboratory', createdDate: '2025-03-01', updatedDate: '2025-06-15', version: '1.2', status: 'active', visibility: 'internal', color: '#9ccc65', favorite: false, pinned: true, starred: false, usageCount: 54, fileSize: '8.1 MB', folderId: 'f-lab-man' },
  { id: 'r-5', name: 'Spawn SOP', code: 'SPN-SOP-001', description: 'Spawn inoculation SOP', type: 'spawn-sop', category: 'Spawn Production', subcategory: 'SOP', tags: ['sop', 'spawn'], language: 'English', author: 'Lab Team', department: 'Quality', createdDate: '2025-03-10', updatedDate: '2025-06-12', version: '2.0', status: 'active', visibility: 'internal', color: '#c0ca33', favorite: false, pinned: false, starred: true, usageCount: 41, fileSize: '480 KB', folderId: 'f-lab-man' },
  { id: 'r-6', name: 'Laboratory Safety SOP', code: 'SAF-SOP-001', description: 'Aseptic lab safety', type: 'safety-sop', category: 'Safety', subcategory: 'SOP', tags: ['safety', 'lab'], language: 'English', author: 'Safety Officer', department: 'Quality', createdDate: '2025-01-20', updatedDate: '2025-05-30', version: '4.1', status: 'active', visibility: 'internal', color: '#fdd835', favorite: true, pinned: false, starred: false, usageCount: 88, fileSize: '620 KB', folderId: null },
  { id: 'r-7', name: 'Government Cultivation Guideline', code: 'GOV-G-001', description: 'National guideline for mushroom farming', type: 'gov-guideline', category: 'Government', subcategory: 'Guideline', tags: ['government', 'guideline'], language: 'Hindi', author: 'Ministry', department: 'Operations', createdDate: '2025-02-01', updatedDate: '2025-04-15', version: '1.0', status: 'active', visibility: 'public', color: '#1565c0', favorite: false, pinned: false, starred: false, usageCount: 33, fileSize: '2.4 MB', folderId: null },
  { id: 'r-8', name: 'Oyster Mushroom Yield Research', code: 'RES-P-001', description: 'Peer-reviewed yield study', type: 'research', category: 'Cultivation', subcategory: 'Research', tags: ['research', 'yield'], language: 'English', author: 'Dr. Sharma', department: 'Research', createdDate: '2025-04-01', updatedDate: '2025-04-01', version: '1.0', status: 'active', visibility: 'public', color: '#6a1b9a', favorite: false, pinned: false, starred: true, usageCount: 27, fileSize: '3.1 MB', folderId: null },
  { id: 'r-9', name: 'Marketing Brochure Template', code: 'MKT-T-001', description: 'Editable marketing template', type: 'business-template', category: 'Business', subcategory: 'Template', tags: ['marketing', 'template'], language: 'English', author: 'Marketing Team', department: 'Marketing', createdDate: '2025-05-01', updatedDate: '2025-05-25', version: '1.3', status: 'draft', visibility: 'private', color: '#ad1457', favorite: false, pinned: false, starred: false, usageCount: 12, fileSize: '1.8 MB', folderId: 'f-biz' },
  { id: 'r-10', name: 'Equipment Calibration Guide', code: 'EQ-M-002', description: 'Pressure cooker calibration', type: 'equipment-manual', category: 'Laboratory', subcategory: 'Manual', tags: ['equipment', 'calibration'], language: 'English', author: 'Lab Team', department: 'Laboratory', createdDate: '2025-03-15', updatedDate: '2025-06-05', version: '1.1', status: 'archived', visibility: 'internal', color: '#00838f', favorite: false, pinned: false, starred: false, usageCount: 19, fileSize: '900 KB', folderId: 'f-lab-man' },
];
