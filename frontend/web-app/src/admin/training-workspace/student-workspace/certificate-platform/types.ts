export type CertificateType =
  | 'course-completion'
  | 'professional'
  | 'skill'
  | 'workshop'
  | 'training'
  | 'internship'
  | 'government-skill'
  | 'corporate-training'
  | 'achievement'
  | 'participation'
  | 'faculty'
  | 'trainer'
  | 'excellence-award'
  | 'merit'
  | 'ai';

export type CertificateStatus =
  | 'draft'
  | 'pending-approval'
  | 'approved'
  | 'generated'
  | 'issued'
  | 'shared'
  | 'verified'
  | 'expired'
  | 'revoked'
  | 'archived';

export type AchievementType =
  | 'course-completed'
  | 'perfect-attendance'
  | 'top-performer'
  | 'fast-learner'
  | 'outstanding-project'
  | 'highest-marks'
  | 'innovation-award'
  | 'research-excellence'
  | 'industry-ready'
  | 'leadership-award'
  | 'community-contributor'
  | 'ai';

export type BadgeType =
  | 'course'
  | 'skill'
  | 'competency'
  | 'assessment'
  | 'attendance'
  | 'leadership'
  | 'trainer'
  | 'corporate'
  | 'government'
  | 'nft';

export type VerificationStatus = 'unverified' | 'pending' | 'verified' | 'failed' | 'expired';

export type CredentialStatus = 'active' | 'shared' | 'revoked' | 'expired' | 'archived';

export interface Certificate {
  id: string;
  certificateNumber: string;
  credentialId: string;
  studentId: string;
  studentName: string;
  enrollmentId: string;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  certificateType: CertificateType;
  title: string;
  description: string;
  issueDate: string;
  expiryDate: string | null;
  status: CertificateStatus;
  verificationStatus: VerificationStatus;
  credentialStatus: CredentialStatus;
  achievementLevel: string;
  template: string;
  issuer: string;
  issuerLogo: string;
  verificationUrl: string;
  qrCode: string;
  digitalSignature: string;
  createdDate: string;
  lastUpdated: string;
}

export interface DigitalBadge {
  id: string;
  badgeCode: string;
  badgeType: BadgeType;
  name: string;
  description: string;
  imageUrl: string;
  criteria: string;
  issuer: string;
  studentId: string;
  studentName: string;
  issueDate: string;
  expiryDate: string | null;
  status: CredentialStatus;
  verificationStatus: VerificationStatus;
  verificationUrl: string;
  shareCount: number;
  isNft: boolean;
  createdDate: string;
}

export interface Achievement {
  id: string;
  achievementCode: string;
  achievementType: AchievementType;
  name: string;
  description: string;
  studentId: string;
  studentName: string;
  courseId: string;
  courseName: string;
  dateAchieved: string;
  iconUrl: string;
  points: number;
  badgeId: string | null;
  certificateId: string | null;
  isPublic: boolean;
  createdDate: string;
}

export interface CredentialWallet {
  id: string;
  studentId: string;
  studentName: string;
  certificates: Certificate[];
  badges: DigitalBadge[];
  achievements: Achievement[];
  totalCredentials: number;
  activeCredentials: number;
  sharedCredentials: number;
  lastActivityDate: string;
  createdDate: string;
}

export interface AcademicTranscript {
  id: string;
  studentId: string;
  studentName: string;
  studentEmail: string;
  studentPhoto: string;
  enrollmentDate: string;
  completionDate: string | null;
  totalCourses: number;
  completedCourses: number;
  totalAttendancePercent: number;
  overallAssignmentScore: number;
  overallAssessmentScore: number;
  competenciesAchieved: number;
  totalCompetencies: number;
  achievementsCount: number;
  certificatesCount: number;
  totalLearningHours: number;
  overallPerformance: number;
  gpa: number;
  credits: number;
  courseRecords: TranscriptCourseRecord[];
  status: 'active' | 'completed' | 'graduated' | 'withdrawn';
}

export interface TranscriptCourseRecord {
  courseId: string;
  courseName: string;
  enrollmentDate: string;
  completionDate: string | null;
  attendancePercent: number;
  assignmentScore: number;
  assessmentScore: number;
  competenciesCount: number;
  learningHours: number;
  grade: string;
  gradePoint: number;
  status: 'in-progress' | 'completed' | 'certified' | 'failed';
}

export interface VerificationRecord {
  id: string;
  verificationCode: string;
  certificateId: string;
  certificateNumber: string;
  studentId: string;
  studentName: string;
  courseName: string;
  certificateType: CertificateType;
  issueDate: string;
  verificationDate: string;
  status: VerificationStatus;
  verifiedBy: string;
  verificationMethod: 'manual' | 'qr' | 'blockchain' | 'employer';
  notes: string;
  createdDate: string;
}

export interface CertificateAnalytics {
  totalCertificates: number;
  issuedCertificates: number;
  pendingCertificates: number;
  verifiedCertificates: number;
  revokedCertificates: number;
  totalBadges: number;
  totalAchievements: number;
  totalWallets: number;
  totalTranscripts: number;
  verificationRequests: number;
  certificatesByCourse: { courseName: string; count: number }[];
  certificatesByMonth: { month: string; count: number }[];
  achievementDistribution: { type: string; count: number }[];
  badgeDistribution: { type: string; count: number }[];
  verificationRate: number;
  completionRate: number;
}

export interface CertificateDashboard {
  certificatesIssued: number;
  pendingCertificates: number;
  verifiedCertificates: number;
  revokedCertificates: number;
  digitalBadges: number;
  achievements: number;
  credentialWallets: number;
  transcriptCount: number;
  verificationRequests: number;
  recentCertificates: Certificate[];
  recentAchievements: Achievement[];
}

export const CERTIFICATE_TYPE_LABELS: Record<CertificateType, string> = {
  'course-completion': 'Course Completion',
  'professional': 'Professional Certificate',
  'skill': 'Skill Certificate',
  'workshop': 'Workshop Certificate',
  'training': 'Training Certificate',
  'internship': 'Internship Certificate',
  'government-skill': 'Government Skill Certificate',
  'corporate-training': 'Corporate Training',
  'achievement': 'Achievement Certificate',
  'participation': 'Participation',
  'faculty': 'Faculty Certificate',
  'trainer': 'Trainer Certificate',
  'excellence-award': 'Excellence Award',
  'merit': 'Merit Certificate',
  'ai': 'AI Certificate',
};

export const CERTIFICATE_STATUS_LABELS: Record<CertificateStatus, string> = {
  'draft': 'Draft',
  'pending-approval': 'Pending Approval',
  'approved': 'Approved',
  'generated': 'Generated',
  'issued': 'Issued',
  'shared': 'Shared',
  'verified': 'Verified',
  'expired': 'Expired',
  'revoked': 'Revoked',
  'archived': 'Archived',
};

export const CERTIFICATE_STATUS_VARIANTS: Record<CertificateStatus, 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'draft': 'default',
  'pending-approval': 'warning',
  'approved': 'info',
  'generated': 'info',
  'issued': 'success',
  'shared': 'info',
  'verified': 'success',
  'expired': 'neutral',
  'revoked': 'danger',
  'archived': 'neutral',
};

export const ACHIEVEMENT_TYPE_LABELS: Record<AchievementType, string> = {
  'course-completed': 'Course Completed',
  'perfect-attendance': 'Perfect Attendance',
  'top-performer': 'Top Performer',
  'fast-learner': 'Fast Learner',
  'outstanding-project': 'Outstanding Project',
  'highest-marks': 'Highest Marks',
  'innovation-award': 'Innovation Award',
  'research-excellence': 'Research Excellence',
  'industry-ready': 'Industry Ready',
  'leadership-award': 'Leadership Award',
  'community-contributor': 'Community Contributor',
  'ai': 'AI Achievement',
};

export const BADGE_TYPE_LABELS: Record<BadgeType, string> = {
  'course': 'Course Badge',
  'skill': 'Skill Badge',
  'competency': 'Competency Badge',
  'assessment': 'Assessment Badge',
  'attendance': 'Attendance Badge',
  'leadership': 'Leadership Badge',
  'trainer': 'Trainer Badge',
  'corporate': 'Corporate Badge',
  'government': 'Government Badge',
  'nft': 'NFT Badge',
};

export const VERIFICATION_STATUS_LABELS: Record<VerificationStatus, string> = {
  'unverified': 'Unverified',
  'pending': 'Pending',
  'verified': 'Verified',
  'failed': 'Failed',
  'expired': 'Expired',
};

export const CREDENTIAL_STATUS_LABELS: Record<CredentialStatus, string> = {
  'active': 'Active',
  'shared': 'Shared',
  'revoked': 'Revoked',
  'expired': 'Expired',
  'archived': 'Archived',
};

export const CERTIFICATE_NAV_ITEMS = [
  { id: 'certificates', label: 'Certificate Dashboard', icon: 'award', description: 'Credential ecosystem overview' },
  { id: 'certificates/registry', label: 'Certificate Registry', icon: 'database', description: 'All certificates' },
  { id: 'certificates/wallet', label: 'Credential Wallet', icon: 'briefcase', description: 'Student credential wallet' },
  { id: 'certificates/achievements', label: 'Achievement Center', icon: 'star', description: 'Achievement management' },
  { id: 'certificates/badges', label: 'Digital Badges', icon: 'shield', description: 'Digital badge collection' },
  { id: 'certificates/transcript', label: 'Academic Transcript', icon: 'file-text', description: 'Academic transcript' },
  { id: 'certificates/verification', label: 'Verification Center', icon: 'check-circle', description: 'Certificate verification' },
  { id: 'certificates/analytics', label: 'Certificate Analytics', icon: 'bar-chart', description: 'Credential analytics' },
];

export const EMPTY_STATE_TYPES = [
  'noCertificates',
  'noBadges',
  'noAchievements',
  'noWallet',
  'noTranscript',
  'noSearchResults',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];
