export type PlacementType =
  | 'on-campus' | 'off-campus' | 'virtual' | 'international'
  | 'corporate-tieup' | 'government-scheme' | 'entrepreneurial' | 'freelance';

export type PlacementStatus =
  | 'placement-ready' | 'resume-shortlisted' | 'interview-scheduled' | 'interview-completed'
  | 'selected' | 'offer-received' | 'offer-accepted' | 'joined'
  | 'rejected' | 'not-placed' | 'withdrawn' | 'deregistered';

export type InternshipStatus =
  | 'applied' | 'shortlisted' | 'interview-scheduled' | 'interview-completed'
  | 'offer-received' | 'offer-accepted' | 'ongoing' | 'completed'
  | 'terminated' | 'converted-to-placement' | 'withdrawn';

export type CareerDevelopmentStage =
  | 'exploration' | 'skill-building' | 'resume-preparation' | 'interview-preparation'
  | 'job-search' | 'placement' | 'post-placement' | 'alumni' | 'mentorship' | 'lifelong-learning';

export type CompanyPartnershipTier =
  | 'platinum' | 'gold' | 'silver' | 'bronze' | 'strategic' | 'academic' | 'government';

export type AlumniEngagementLevel =
  | 'active' | 'moderate' | 'low' | 'disengaged' | 'lifetime' | 'ambassador' | 'mentor' | 'donor';

export type JobOpportunityType =
  | 'full-time' | 'part-time' | 'internship' | 'contract' | 'freelance' | 'apprenticeship' | 'fellowship' | 'government-job';

export type PlacementDriveStage =
  | 'announced' | 'registrations-open' | 'registrations-closed' | 'scheduled'
  | 'in-progress' | 'results-pending' | 'completed' | 'cancelled';

export type AlumniContributionType =
  | 'monetary-donation' | 'equipment-donation' | 'scholarship-fund' | 'mentorship-hours'
  | 'guest-lecture' | 'industry-project' | 'curriculum-advisory' | 'placement-support'
  | 'event-participation' | 'media-testimonial';

export type AlumniEventType =
  | 'networking' | 'workshop' | 'webinar' | 'reunion' | 'mentorship-session'
  | 'industry-visit' | 'hackathon' | 'career-fair' | 'cultural' | 'sports'
  | 'annual-meet' | 'chapter-meet';

export type SkillCategory =
  | 'technical' | 'domain' | 'soft-skill' | 'leadership' | 'language' | 'certification' | 'tool' | 'methodology';

export interface StudentSkill {
  category: SkillCategory;
  name: string;
  proficiency: 'beginner' | 'intermediate' | 'advanced' | 'expert';
}

export interface StudentCareerProfile {
  id: string;
  studentId: string;
  studentName: string;
  studentPhoto: string;
  email: string;
  phone: string;
  dateOfBirth: string;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  overallScore: number;
  technicalScore: number;
  domainScore: number;
  softSkillScore: number;
  resumeUrl: string;
  portfolioUrl: string;
  linkedInUrl: string;
  githubUrl: string;
  careerObjective: string;
  targetRole: string;
  targetIndustry: string;
  preferredLocation: string;
  totalExperience: string;
  noticePeriod: string;
  currentCtc: string;
  expectedCtc: string;
  skills: StudentSkill[];
  placementStatus: PlacementStatus;
  careerStage: CareerDevelopmentStage;
  interviewCount: number;
  offerCount: number;
  placementDate: string | null;
  createdDate: string;
  lastUpdated: string;
}

export interface JobOpportunity {
  id: string;
  jobId: string;
  jobType: JobOpportunityType;
  companyId: string;
  companyName: string;
  companyLogo: string;
  title: string;
  description: string;
  requiredSkills: string[];
  preferredSkills: string[];
  location: string;
  salaryRange: string;
  experienceRequired: string;
  totalPositions: number;
  filledPositions: number;
  postedDate: string;
  applicationDeadline: string;
  startDate: string;
  status: 'active' | 'closed' | 'on-hold' | 'cancelled' | 'filled';
  placementDriveId: string | null;
  partnerTier: CompanyPartnershipTier;
  eligibilityCriteria: { courseIds: string[]; minScore: number; minAttendance: number };
}

export interface PlacementDrive {
  id: string;
  driveId: string;
  name: string;
  description: string;
  stage: PlacementDriveStage;
  companyId: string;
  companyName: string;
  companyLogo: string;
  jobOpportunities: JobOpportunity[];
  registrationsOpen: string;
  registrationsClose: string;
  driveDate: string;
  venue: string;
  mode: 'on-campus' | 'off-campus' | 'virtual';
  registeredStudents: number;
  shortlistedStudents: number;
  selectedStudents: number;
  totalRounds: number;
  currentRound: number;
  createdDate: string;
}

export interface CompanyPartnership {
  id: string;
  companyId: string;
  companyName: string;
  companyLogo: string;
  website: string;
  industry: string;
  tier: CompanyPartnershipTier;
  partnershipDate: string;
  lastActivityDate: string;
  totalHires: number;
  totalInternshipsOffered: number;
  totalPlacementDrives: number;
  contactPerson: string;
  contactEmail: string;
  contactPhone: string;
  status: 'active' | 'inactive' | 'suspended';
  notes: string;
}

export interface CareerCounselingSession {
  id: string;
  sessionId: string;
  studentId: string;
  studentName: string;
  counselorName: string;
  sessionDate: string;
  sessionType: 'one-on-one' | 'group' | 'workshop' | 'webinar';
  topic: string;
  notes: string;
  actionItems: string;
  status: 'scheduled' | 'completed' | 'cancelled' | 'no-show';
  feedback: string;
  rating: number;
  createdDate: string;
}

export interface InterviewPreparation {
  id: string;
  prepId: string;
  studentId: string;
  studentName: string;
  companyName: string;
  jobTitle: string;
  roundNumber: number;
  roundType: 'aptitude' | 'technical' | 'hr' | 'managerial' | 'group-discussion' | 'presentation';
  scheduledDate: string;
  status: 'upcoming' | 'completed' | 'cancelled' | 'rescheduled';
  preparationNotes: string;
  feedback: string;
  result: 'cleared' | 'failed' | 'awaiting' | null;
  createdDate: string;
}

export interface SkillGapAssessment {
  id: string;
  assessmentId: string;
  studentId: string;
  studentName: string;
  category: SkillCategory;
  skillName: string;
  currentLevel: number;
  requiredLevel: number;
  gap: number;
  priority: 'critical' | 'high' | 'medium' | 'low';
  suggestedResources: string;
  targetDate: string | null;
  status: 'identified' | 'in-progress' | 'addressed';
}

export interface AlumniProfile {
  id: string;
  alumniId: string;
  studentId: string | null;
  fullName: string;
  photo: string;
  email: string;
  phone: string;
  batchId: string;
  batchName: string;
  courseId: string;
  courseName: string;
  graduationYear: number;
  completionDate: string;
  currentCompany: string;
  currentPosition: string;
  industry: string;
  location: string;
  linkedInUrl: string;
  engagementLevel: AlumniEngagementLevel;
  mentorshipStatus: 'available' | 'active' | 'unavailable';
  totalMentorshipHours: number;
  contributionsCount: number;
  isAmbassador: boolean;
  isDonor: boolean;
  bio: string;
  achievements: string;
  createdDate: string;
  lastActiveDate: string;
}

export interface AlumniMentorship {
  id: string;
  mentorshipId: string;
  mentorAlumniId: string;
  mentorName: string;
  menteeStudentId: string;
  menteeStudentName: string;
  startDate: string;
  endDate: string | null;
  focusArea: 'career-guidance' | 'technical' | 'domain' | 'interview-prep' | 'entrepreneurship' | 'leadership';
  status: 'active' | 'completed' | 'paused' | 'cancelled';
  sessionsCompleted: number;
  totalHours: number;
  feedback: string;
  rating: number;
}

export interface AlumniContribution {
  id: string;
  contributionId: string;
  alumniId: string;
  alumniName: string;
  type: AlumniContributionType;
  description: string;
  date: string;
  value: string;
  associatedEventId: string | null;
  associatedProgram: string | null;
  status: 'acknowledged' | 'pending-acknowledgment' | 'featured';
  createdDate: string;
}

export interface AlumniEvent {
  id: string;
  eventId: string;
  eventType: AlumniEventType;
  title: string;
  description: string;
  date: string;
  time: string;
  venue: string;
  mode: 'online' | 'offline' | 'hybrid';
  organizer: string;
  maxAttendees: number;
  registeredCount: number;
  attendedCount: number;
  registrationDeadline: string;
  fee: string;
  status: 'draft' | 'announced' | 'open' | 'closed' | 'in-progress' | 'completed' | 'cancelled';
  createdBy: string;
  createdDate: string;
}

export interface AlumniAnalytics {
  totalAlumni: number;
  activeAlumni: number;
  engagedAlumni: number;
  totalMentors: number;
  activeMentorships: number;
  completedMentorships: number;
  totalContributions: number;
  totalDonations: string;
  totalEvents: number;
  totalEventRegistrations: number;
  placementRate: number;
  averageSalary: string;
  topRecruiters: { companyName: string; hires: number }[];
  alumniByBatch: { batchName: string; count: number }[];
  alumniByIndustry: { industry: string; count: number }[];
  alumniByLocation: { location: string; count: number }[];
  engagementTrends: { month: string; activeCount: number; contributionCount: number }[];
  placementTrends: { month: string; placedCount: number }[];
}

export interface PlacementDashboard {
  totalStudents: number;
  placementReady: number;
  shortlisted: number;
  placed: number;
  notPlaced: number;
  activeDrives: number;
  activeJobOpenings: number;
  partnerCompanies: number;
  placementPercentage: number;
  averagePackage: string;
  highestPackage: string;
  upcomingDrives: PlacementDrive[];
  recentPlacements: StudentCareerProfile[];
  topRecruiters: CompanyPartnership[];
}

export interface AlumniState {
  studentProfiles: StudentCareerProfile[];
  jobOpportunities: JobOpportunity[];
  placementDrives: PlacementDrive[];
  companies: CompanyPartnership[];
  counselingSessions: CareerCounselingSession[];
  interviewPrep: InterviewPreparation[];
  skillGaps: SkillGapAssessment[];
  alumni: AlumniProfile[];
  mentorships: AlumniMentorship[];
  contributions: AlumniContribution[];
  events: AlumniEvent[];
  analytics: AlumniAnalytics;
  dashboard: PlacementDashboard;
}

export const PLACEMENT_STATUS_LABELS: Record<PlacementStatus, string> = {
  'placement-ready': 'Placement Ready',
  'resume-shortlisted': 'Resume Shortlisted',
  'interview-scheduled': 'Interview Scheduled',
  'interview-completed': 'Interview Completed',
  'selected': 'Selected',
  'offer-received': 'Offer Received',
  'offer-accepted': 'Offer Accepted',
  'joined': 'Joined',
  'rejected': 'Rejected',
  'not-placed': 'Not Placed',
  'withdrawn': 'Withdrawn',
  'deregistered': 'Deregistered',
};

export const INTERNSHIP_STATUS_LABELS: Record<InternshipStatus, string> = {
  'applied': 'Applied',
  'shortlisted': 'Shortlisted',
  'interview-scheduled': 'Interview Scheduled',
  'interview-completed': 'Interview Completed',
  'offer-received': 'Offer Received',
  'offer-accepted': 'Offer Accepted',
  'ongoing': 'Ongoing',
  'completed': 'Completed',
  'terminated': 'Terminated',
  'converted-to-placement': 'Converted to Placement',
  'withdrawn': 'Withdrawn',
};

export const TIER_LABELS: Record<CompanyPartnershipTier, string> = {
  'platinum': 'Platinum',
  'gold': 'Gold',
  'silver': 'Silver',
  'bronze': 'Bronze',
  'strategic': 'Strategic',
  'academic': 'Academic',
  'government': 'Government',
};

export const ENGAGEMENT_LABELS: Record<AlumniEngagementLevel, string> = {
  'active': 'Active',
  'moderate': 'Moderate',
  'low': 'Low',
  'disengaged': 'Disengaged',
  'lifetime': 'Lifetime',
  'ambassador': 'Ambassador',
  'mentor': 'Mentor',
  'donor': 'Donor',
};

export const JOB_TYPE_LABELS: Record<JobOpportunityType, string> = {
  'full-time': 'Full Time',
  'part-time': 'Part Time',
  'internship': 'Internship',
  'contract': 'Contract',
  'freelance': 'Freelance',
  'apprenticeship': 'Apprenticeship',
  'fellowship': 'Fellowship',
  'government-job': 'Government Job',
};

export const DRIVE_STAGE_LABELS: Record<PlacementDriveStage, string> = {
  'announced': 'Announced',
  'registrations-open': 'Registrations Open',
  'registrations-closed': 'Registrations Closed',
  'scheduled': 'Scheduled',
  'in-progress': 'In Progress',
  'results-pending': 'Results Pending',
  'completed': 'Completed',
  'cancelled': 'Cancelled',
};

export const CONTRIBUTION_TYPE_LABELS: Record<AlumniContributionType, string> = {
  'monetary-donation': 'Monetary Donation',
  'equipment-donation': 'Equipment Donation',
  'scholarship-fund': 'Scholarship Fund',
  'mentorship-hours': 'Mentorship Hours',
  'guest-lecture': 'Guest Lecture',
  'industry-project': 'Industry Project',
  'curriculum-advisory': 'Curriculum Advisory',
  'placement-support': 'Placement Support',
  'event-participation': 'Event Participation',
  'media-testimonial': 'Media Testimonial',
};

export const EVENT_TYPE_LABELS: Record<AlumniEventType, string> = {
  'networking': 'Networking',
  'workshop': 'Workshop',
  'webinar': 'Webinar',
  'reunion': 'Reunion',
  'mentorship-session': 'Mentorship Session',
  'industry-visit': 'Industry Visit',
  'hackathon': 'Hackathon',
  'career-fair': 'Career Fair',
  'cultural': 'Cultural',
  'sports': 'Sports',
  'annual-meet': 'Annual Meet',
  'chapter-meet': 'Chapter Meet',
};

export const SKILL_CATEGORY_LABELS: Record<SkillCategory, string> = {
  'technical': 'Technical',
  'domain': 'Domain',
  'soft-skill': 'Soft Skill',
  'leadership': 'Leadership',
  'language': 'Language',
  'certification': 'Certification',
  'tool': 'Tool',
  'methodology': 'Methodology',
};

export const PLACEMENT_STATUS_VARIANTS: Record<string, { bg: string; color: string }> = {
  'placement-ready': { bg: '#eff6ff', color: '#2563eb' },
  'resume-shortlisted': { bg: '#fefce8', color: '#ca8a04' },
  'interview-scheduled': { bg: '#f0fdf4', color: '#16a34a' },
  'interview-completed': { bg: '#f0fdf4', color: '#16a34a' },
  'selected': { bg: '#f0fdf4', color: '#16a34a' },
  'offer-received': { bg: '#f0fdf4', color: '#059669' },
  'offer-accepted': { bg: '#f0fdf4', color: '#059669' },
  'joined': { bg: '#f0fdf4', color: '#059669' },
  'rejected': { bg: '#fef2f2', color: '#dc2626' },
  'not-placed': { bg: '#fef2f2', color: '#dc2626' },
  'withdrawn': { bg: '#f3f4f6', color: '#6b7280' },
  'deregistered': { bg: '#f3f4f6', color: '#6b7280' },
};

export const TIER_VARIANTS: Record<string, { bg: string; color: string }> = {
  'platinum': { bg: '#f0fdf4', color: '#059669' },
  'gold': { bg: '#fefce8', color: '#ca8a04' },
  'silver': { bg: '#f1f5f9', color: '#64748b' },
  'bronze': { bg: '#fff7ed', color: '#d97706' },
  'strategic': { bg: '#eff6ff', color: '#2563eb' },
  'academic': { bg: '#f0fdf4', color: '#16a34a' },
  'government': { bg: '#fef2f2', color: '#dc2626' },
};

export const ENGAGEMENT_VARIANTS: Record<string, { bg: string; color: string }> = {
  'active': { bg: '#f0fdf4', color: '#16a34a' },
  'moderate': { bg: '#fefce8', color: '#ca8a04' },
  'low': { bg: '#fef2f2', color: '#dc2626' },
  'disengaged': { bg: '#f3f4f6', color: '#6b7280' },
  'lifetime': { bg: '#f0fdf4', color: '#059669' },
  'ambassador': { bg: '#eff6ff', color: '#2563eb' },
  'mentor': { bg: '#f0fdf4', color: '#16a34a' },
  'donor': { bg: '#fff7ed', color: '#d97706' },
};

export const ALUMNI_NAV_ITEMS = [
  { id: 'alumni', label: 'Alumni Dashboard', icon: 'layout', description: 'Executive KPIs & placement overview' },
  { id: 'alumni/placement', label: 'Placement Hub', icon: 'briefcase', description: 'Placement drives lifecycle management' },
  { id: 'alumni/internships', label: 'Internship Center', icon: 'clipboard', description: 'Internship opportunities & applications' },
  { id: 'alumni/career', label: 'Career Development', icon: 'trending-up', description: 'Career profiles, counseling & skill gaps' },
  { id: 'alumni/companies', label: 'Company Partnerships', icon: 'building', description: 'Partner company registry & hiring stats' },
  { id: 'alumni/jobs', label: 'Job Opportunities', icon: 'briefcase', description: 'Active job listings & openings' },
  { id: 'alumni/directory', label: 'Alumni Directory', icon: 'users', description: 'Searchable alumni network directory' },
  { id: 'alumni/mentorship', label: 'Mentorship', icon: 'user-check', description: 'Alumni mentorship program' },
  { id: 'alumni/events', label: 'Alumni Events', icon: 'calendar', description: 'Event lifecycle management' },
  { id: 'alumni/contributions', label: 'Contributions', icon: 'gift', description: 'Alumni contribution tracking & recognition' },
  { id: 'alumni/analytics', label: 'Placement Analytics', icon: 'bar-chart', description: 'Placement trends & alumni engagement metrics' },
];

export const EMPTY_STATE_TYPES = [
  'noPlacements', 'noInternships', 'noJobs', 'noAlumni', 'noEvents', 'noSearchResults',
] as const;

export type EmptyStateType = typeof EMPTY_STATE_TYPES[number];
