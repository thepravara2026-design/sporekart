export type ProfileStatus = 'incomplete' | 'draft' | 'pending-verification' | 'verified' | 'active' | 'inactive' | 'suspended' | 'archived';

export type MaritalStatus = 'single' | 'married' | 'divorced' | 'widowed' | 'not-specified';

export type LearningTimePreference = 'morning' | 'afternoon' | 'evening' | 'weekend' | 'flexible';

export type ProfessionType = 'business-owner' | 'farmer' | 'student' | 'employee' | 'entrepreneur' | 'consultant' | 'trainer' | 'unemployed' | 'other';

export type Relationship = 'father' | 'mother' | 'spouse' | 'brother' | 'sister' | 'uncle' | 'aunt' | 'grandparent' | 'friend' | 'other';

export interface Address {
  village: string;
  taluk: string;
  district: string;
  state: string;
  country: string;
  postalCode: string;
}

export interface PersonalInfo {
  bloodGroup: string | null;
  maritalStatus: MaritalStatus;
  alternatePhone: string | null;
  governmentId: string | null;
  currentAddress: Address;
  permanentAddress: Address;
  sameAsCurrent: boolean;
  employer: string | null;
  businessName: string | null;
  annualExperience: string | null;
}

export interface AcademicInfo {
  highestQualification: string;
  specialization: string;
  institution: string;
  graduationYear: number | null;
  currentEducation: string | null;
  previousCourses: string[];
  learningInterests: string[];
  academicNotes: string | null;
}

export interface ProfessionalInfo {
  currentProfession: string;
  professionType: ProfessionType;
  industry: string | null;
  yearsOfExperience: number;
  skills: string[];
  certifications: string[];
  isBusinessOwner: boolean;
  isFarmer: boolean;
  isStudent: boolean;
}

export interface LearningPreferences {
  preferredLanguage: string;
  secondaryLanguages: string[];
  preferredMode: 'online' | 'offline' | 'hybrid';
  timePreferences: LearningTimePreference[];
  learningGoals: string[];
  learningInterests: string[];
  preferredTrainingCategory: string;
}

export interface GuardianInfo {
  name: string;
  relationship: Relationship;
  phone: string;
  email: string | null;
  occupation: string | null;
  address: string | null;
}

export interface EmergencyContact {
  primaryName: string;
  primaryPhone: string;
  primaryRelationship: Relationship;
  secondaryName: string | null;
  secondaryPhone: string | null;
  secondaryRelationship: Relationship | null;
  email: string | null;
}

export interface DocumentMetadata {
  id: string;
  type: 'identity-proof' | 'address-proof' | 'education-certificate' | 'photograph' | 'resume' | 'business-registration' | 'farmer-id' | 'government-scheme-document';
  label: string;
  status: 'not-uploaded' | 'uploaded' | 'verified' | 'rejected';
  uploadedAt: string | null;
  verifiedAt: string | null;
  notes: string | null;
}

export interface TimelineEvent {
  id: string;
  type: 'registration' | 'profile-created' | 'enrollment' | 'attendance' | 'assignment' | 'assessment' | 'certificate' | 'placement' | 'alumni' | 'profile-update' | 'document-upload' | 'verification';
  label: string;
  description: string;
  date: string;
  completed: boolean;
  icon: string;
}

export interface ProfileCompleteness {
  overall: number;
  sections: {
    personal: { completed: number; total: number; percent: number };
    academic: { completed: number; total: number; percent: number };
    professional: { completed: number; total: number; percent: number };
    learningPreferences: { completed: number; total: number; percent: number };
    guardian: { completed: number; total: number; percent: number };
    emergencyContact: { completed: number; total: number; percent: number };
    documents: { completed: number; total: number; percent: number };
  };
  suggestions: string[];
}

export interface StudentProfile {
  id: string;
  studentId: string;
  enrollmentNumber: string | null;
  profilePhotoUrl: string | null;
  fullName: string;
  preferredName: string;
  gender: 'male' | 'female' | 'other' | 'not-specified';
  dateOfBirth: string;
  age: number;
  bloodGroup: string | null;
  nationality: string;
  primaryLanguage: string;
  secondaryLanguages: string[];
  email: string;
  phone: string;
  alternatePhone: string | null;
  governmentId: string | null;
  profileStatus: ProfileStatus;
  registrationDate: string;
  lastUpdated: string;
  personalInfo: PersonalInfo;
  academicInfo: AcademicInfo;
  professionalInfo: ProfessionalInfo;
  learningPreferences: LearningPreferences;
  guardian: GuardianInfo | null;
  emergencyContact: EmergencyContact | null;
  documents: DocumentMetadata[];
  timeline: TimelineEvent[];
  completeness: ProfileCompleteness;
}

export const PROFILE_STATUS_LABELS: Record<ProfileStatus, string> = {
  'incomplete': 'Incomplete',
  'draft': 'Draft',
  'pending-verification': 'Pending Verification',
  'verified': 'Verified',
  'active': 'Active',
  'inactive': 'Inactive',
  'suspended': 'Suspended',
  'archived': 'Archived',
};

export const PROFILE_STATUS_VARIANTS: Record<ProfileStatus, 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'incomplete': 'default',
  'draft': 'neutral',
  'pending-verification': 'warning',
  'verified': 'success',
  'active': 'success',
  'inactive': 'neutral',
  'suspended': 'danger',
  'archived': 'default',
};

export const DOCUMENT_TYPES: { value: DocumentMetadata['type']; label: string; icon: string }[] = [
  { value: 'identity-proof', label: 'Identity Proof', icon: 'credit-card' },
  { value: 'address-proof', label: 'Address Proof', icon: 'map-pin' },
  { value: 'education-certificate', label: 'Education Certificate', icon: 'book-open' },
  { value: 'photograph', label: 'Photograph', icon: 'image' },
  { value: 'resume', label: 'Resume', icon: 'file' },
  { value: 'business-registration', label: 'Business Registration', icon: 'briefcase' },
  { value: 'farmer-id', label: 'Farmer ID', icon: 'leaf' },
  { value: 'government-scheme-document', label: 'Govt Scheme Document', icon: 'shield' },
];

export const PROFILE_NAV_ITEMS = [
  { id: 'profile', label: 'Profile Overview', icon: 'user', description: 'Student profile overview' },
  { id: 'profile/edit', label: 'Edit Profile', icon: 'edit', description: 'Edit student profile' },
  { id: 'profile/academic', label: 'Academic Info', icon: 'book-open', description: 'Academic information' },
  { id: 'profile/professional', label: 'Professional Info', icon: 'briefcase', description: 'Professional profile' },
  { id: 'profile/learning', label: 'Learning Profile', icon: 'book', description: 'Learning preferences' },
  { id: 'profile/guardian', label: 'Guardian Info', icon: 'users', description: 'Guardian details' },
  { id: 'profile/documents', label: 'Documents', icon: 'folder', description: 'Document metadata' },
  { id: 'profile/timeline', label: 'Timeline', icon: 'clock', description: 'Student activity timeline' },
];
