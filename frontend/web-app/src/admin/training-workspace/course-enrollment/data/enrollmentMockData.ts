// ==========================================================================================================
// Enterprise Training Pricing, Enrollment & Capacity Management — Mock Data Layer
// Mock Mode only. No payment gateway, no APIs, no database, no real transactions.
// ==========================================================================================================

export type EnrollmentSection =
  | 'overview'
  | 'pricing'
  | 'enrollment'
  | 'capacity'
  | 'policies'
  | 'eligibility'
  | 'lifecycle'
  | 'waitlist'
  | 'payment';

// ---- Pricing models -------------------------------------------------------------------------------------

export type PricingModel =
  | 'free'
  | 'paid'
  | 'premium'
  | 'workshop'
  | 'corporate'
  | 'institution'
  | 'government'
  | 'scholarship'
  | 'internal'
  | 'subscription'
  | 'membership'
  | 'bundle'
  | 'dynamic';

export type Currency = 'INR' | 'USD' | 'EUR';
export type PricingVisibility = 'public' | 'private' | 'internal';

export interface PricingConfig {
  model: PricingModel;
  baseFee: number;
  enrollmentFee: number;
  registrationFee: number;
  currency: Currency;
  visibility: PricingVisibility;
  discountPlaceholder: number;
  gstPlaceholder: number;
  earlyBirdPlaceholder: number;
  lateRegistrationPlaceholder: number;
  refundPolicyPlaceholder: string;
  cancellationPolicyPlaceholder: string;
  installmentsSupported: boolean;
}

// ---- Enrollment policies --------------------------------------------------------------------------------

export type EnrollmentPolicyType =
  | 'open'
  | 'admin-approval'
  | 'invitation'
  | 'corporate-approval'
  | 'institution-approval';

export interface EnrollmentPolicy {
  type: EnrollmentPolicyType;
  eligibilityRules: string[];
  enrollmentWindowStart: string;
  enrollmentWindowEnd: string;
  registrationDeadline: string;
  prerequisiteValidation: boolean;
  maxAttemptsPlaceholder: number;
}

// ---- Capacity -------------------------------------------------------------------------------------------

export interface CapacityConfig {
  maxSeats: number;
  minSeats: number;
  availableSeats: number;
  reservedSeats: number;
  occupiedSeats: number;
  blockedSeats: number;
  overflowSupported: boolean;
  multiBatchSupported: boolean;
  parallelBatchSupported: boolean;
}

// ---- Registration lifecycle -----------------------------------------------------------------------------

export type RegistrationStatus =
  | 'not-open'
  | 'open'
  | 'pending'
  | 'approved'
  | 'confirmed'
  | 'waitlisted'
  | 'rejected'
  | 'cancelled'
  | 'completed'
  | 'expired'
  | 'archived';

export const REGISTRATION_LIFECYCLE: RegistrationStatus[] = [
  'not-open', 'open', 'pending', 'approved', 'confirmed',
  'waitlisted', 'rejected', 'cancelled', 'completed', 'expired', 'archived',
];

// ---- Eligibility engine ---------------------------------------------------------------------------------

export type EligibilityCriterion =
  | 'age'
  | 'education'
  | 'previous-course'
  | 'experience'
  | 'certification'
  | 'location'
  | 'corporate'
  | 'institution'
  | 'prerequisites';

export interface EligibilityRule {
  id: string;
  criterion: EligibilityCriterion;
  label: string;
  condition: string;
  required: boolean;
  mockResult: 'pass' | 'fail' | 'warning';
}

// ---- Course commerce record -----------------------------------------------------------------------------

export type DeliveryMode = 'online' | 'offline' | 'hybrid' | 'self-paced';

export interface CourseCommerce {
  id: string;
  courseId: string;
  courseName: string;
  code: string;
  category: string;
  language: string;
  deliveryMode: DeliveryMode;
  durationWeeks: number;
  pricing: PricingConfig;
  policy: EnrollmentPolicy;
  capacity: CapacityConfig;
  registrationStatus: RegistrationStatus;
  enrollmentRequests: number;
  approvedCount: number;
  pendingCount: number;
  waitlistedCount: number;
  cancelledCount: number;
  popularity: number;
  revenuePlaceholder: number;
}

// ---- Enrollment request record --------------------------------------------------------------------------

export interface EnrollmentRequest {
  id: string;
  courseId: string;
  courseName: string;
  applicantName: string;
  applicantType: 'individual' | 'corporate' | 'institution' | 'government';
  requestedDate: string;
  status: RegistrationStatus;
  pricingModel: PricingModel;
  amountPlaceholder: number;
  eligibilityResult: 'pass' | 'fail' | 'warning';
}

// ---- Waitlist -------------------------------------------------------------------------------------------

export interface WaitlistEntry {
  id: string;
  courseId: string;
  courseName: string;
  applicantName: string;
  position: number;
  priority: 'high' | 'medium' | 'low';
  addedDate: string;
  expiresDate: string;
  promotionMode: 'automatic' | 'manual';
}

// ==========================================================================================================
// Labels & options
// ==========================================================================================================

export const PANEL_LABELS: Record<EnrollmentSection, string> = {
  overview: 'Commerce Overview',
  pricing: 'Pricing',
  enrollment: 'Enrollment',
  capacity: 'Capacity',
  policies: 'Enrollment Policies',
  eligibility: 'Eligibility Rules',
  lifecycle: 'Registration Lifecycle',
  waitlist: 'Waitlist',
  payment: 'Payment Readiness',
};

export const ENROLLMENT_SECTIONS: EnrollmentSection[] = [
  'overview', 'pricing', 'enrollment', 'capacity', 'policies', 'eligibility', 'lifecycle', 'waitlist', 'payment',
];

export const PRICING_MODEL_LABELS: Record<PricingModel, string> = {
  free: 'Free',
  paid: 'Paid',
  premium: 'Premium',
  workshop: 'Workshop',
  corporate: 'Corporate',
  institution: 'Institution',
  government: 'Government Sponsored',
  scholarship: 'Scholarship',
  internal: 'Internal Training',
  subscription: 'Subscription (Future)',
  membership: 'Membership (Future)',
  bundle: 'Bundle (Future)',
  dynamic: 'Dynamic (Future)',
};

export const REGISTRATION_STATUS_LABELS: Record<RegistrationStatus, string> = {
  'not-open': 'Not Open',
  open: 'Open',
  pending: 'Pending',
  approved: 'Approved',
  confirmed: 'Confirmed',
  waitlisted: 'Waitlisted',
  rejected: 'Rejected',
  cancelled: 'Cancelled',
  completed: 'Completed',
  expired: 'Expired',
  archived: 'Archived',
};

export const ENROLLMENT_POLICY_LABELS: Record<EnrollmentPolicyType, string> = {
  open: 'Open Enrollment',
  'admin-approval': 'Admin Approval',
  invitation: 'Invitation Only',
  'corporate-approval': 'Corporate Approval',
  'institution-approval': 'Institution Approval',
};

export const ELIGIBILITY_LABELS: Record<EligibilityCriterion, string> = {
  age: 'Age',
  education: 'Education',
  'previous-course': 'Previous Course',
  experience: 'Experience',
  certification: 'Certification',
  location: 'Location',
  corporate: 'Corporate',
  institution: 'Institution',
  prerequisites: 'Prerequisites',
};

export const STATUS_BADGE_VARIANT: Record<RegistrationStatus, 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'not-open': 'neutral',
  open: 'info',
  pending: 'warning',
  approved: 'success',
  confirmed: 'success',
  waitlisted: 'warning',
  rejected: 'danger',
  cancelled: 'danger',
  completed: 'success',
  expired: 'neutral',
  archived: 'neutral',
};

export const PRICING_TYPE_OPTIONS = [
  { value: 'all', label: 'All Pricing Types' },
  ...Object.keys(PRICING_MODEL_LABELS).map((m) => ({ value: m, label: PRICING_MODEL_LABELS[m as PricingModel] })),
];

export const REGISTRATION_STATUS_OPTIONS = [
  { value: 'all', label: 'All Registration Status' },
  ...REGISTRATION_LIFECYCLE.map((s) => ({ value: s, label: REGISTRATION_STATUS_LABELS[s] })),
];

export const DELIVERY_MODE_OPTIONS = [
  { value: 'all', label: 'All Delivery Modes' },
  { value: 'online', label: 'Online' },
  { value: 'offline', label: 'Offline' },
  { value: 'hybrid', label: 'Hybrid' },
  { value: 'self-paced', label: 'Self-paced' },
];

export const LANGUAGE_OPTIONS = [
  { value: 'all', label: 'All Languages' },
  { value: 'English', label: 'English' },
  { value: 'Kannada', label: 'Kannada' },
  { value: 'Hindi', label: 'Hindi' },
  { value: 'Tamil', label: 'Tamil' },
  { value: 'Telugu', label: 'Telugu' },
];

export const AVAILABILITY_OPTIONS = [
  { value: 'all', label: 'All Availability' },
  { value: 'available', label: 'Seats Available' },
  { value: 'full', label: 'Full' },
  { value: 'waitlist', label: 'Waitlist Only' },
];

export function formatCurrency(amount: number, currency: Currency = 'INR'): string {
  const symbol = currency === 'INR' ? '\u20B9' : currency === 'USD' ? '$' : '\u20AC';
  if (amount === 0) return 'Free';
  return `${symbol}${amount.toLocaleString('en-IN')}`;
}

// ==========================================================================================================
// Mock data
// ==========================================================================================================

function pricing(partial: Partial<PricingConfig> & { model: PricingModel; baseFee: number }): PricingConfig {
  return {
    enrollmentFee: 0,
    registrationFee: 0,
    currency: 'INR',
    visibility: 'public',
    discountPlaceholder: 0,
    gstPlaceholder: 18,
    earlyBirdPlaceholder: 0,
    lateRegistrationPlaceholder: 0,
    refundPolicyPlaceholder: 'Standard 7-day refund window (mock)',
    cancellationPolicyPlaceholder: 'Free cancellation before start (mock)',
    installmentsSupported: false,
    ...partial,
  };
}

function policy(partial: Partial<EnrollmentPolicy> & { type: EnrollmentPolicyType }): EnrollmentPolicy {
  return {
    eligibilityRules: [],
    enrollmentWindowStart: '2026-01-01',
    enrollmentWindowEnd: '2026-03-31',
    registrationDeadline: '2026-03-25',
    prerequisiteValidation: false,
    maxAttemptsPlaceholder: 3,
    ...partial,
  };
}

function capacity(max: number, occupied: number, reserved = 0, blocked = 0): CapacityConfig {
  const available = Math.max(0, max - occupied - reserved - blocked);
  return {
    maxSeats: max,
    minSeats: Math.ceil(max * 0.2),
    availableSeats: available,
    reservedSeats: reserved,
    occupiedSeats: occupied,
    blockedSeats: blocked,
    overflowSupported: false,
    multiBatchSupported: false,
    parallelBatchSupported: false,
  };
}

export const MOCK_COURSE_COMMERCE: CourseCommerce[] = [
  {
    id: 'ce-1', courseId: 'c-101', courseName: 'Oyster Mushroom Cultivation', code: 'OMC-101',
    category: 'Cultivation', language: 'English', deliveryMode: 'hybrid', durationWeeks: 4,
    pricing: pricing({ model: 'paid', baseFee: 4999, enrollmentFee: 500, registrationFee: 250, earlyBirdPlaceholder: 500 }),
    policy: policy({ type: 'open', eligibilityRules: ['age', 'location'] }),
    capacity: capacity(40, 28, 4, 1),
    registrationStatus: 'open', enrollmentRequests: 52, approvedCount: 28, pendingCount: 12, waitlistedCount: 8, cancelledCount: 4,
    popularity: 92, revenuePlaceholder: 139972,
  },
  {
    id: 'ce-2', courseId: 'c-102', courseName: 'Spawn Production Masterclass', code: 'SPM-201',
    category: 'Spawn Production', language: 'English', deliveryMode: 'offline', durationWeeks: 6,
    pricing: pricing({ model: 'premium', baseFee: 12999, enrollmentFee: 1000, registrationFee: 500, visibility: 'public' }),
    policy: policy({ type: 'admin-approval', eligibilityRules: ['previous-course', 'experience'], prerequisiteValidation: true }),
    capacity: capacity(20, 20, 0, 0),
    registrationStatus: 'confirmed', enrollmentRequests: 34, approvedCount: 20, pendingCount: 6, waitlistedCount: 8, cancelledCount: 0,
    popularity: 88, revenuePlaceholder: 259980,
  },
  {
    id: 'ce-3', courseId: 'c-103', courseName: 'Home Mushroom Farming Basics', code: 'HMF-100',
    category: 'Cultivation', language: 'Kannada', deliveryMode: 'self-paced', durationWeeks: 2,
    pricing: pricing({ model: 'free', baseFee: 0 }),
    policy: policy({ type: 'open' }),
    capacity: capacity(500, 312, 0, 0),
    registrationStatus: 'open', enrollmentRequests: 312, approvedCount: 312, pendingCount: 0, waitlistedCount: 0, cancelledCount: 18,
    popularity: 97, revenuePlaceholder: 0,
  },
  {
    id: 'ce-4', courseId: 'c-104', courseName: 'Corporate Agri-Skilling Program', code: 'CAS-300',
    category: 'Business', language: 'English', deliveryMode: 'online', durationWeeks: 8,
    pricing: pricing({ model: 'corporate', baseFee: 89999, enrollmentFee: 0, registrationFee: 0, visibility: 'private' }),
    policy: policy({ type: 'corporate-approval', eligibilityRules: ['corporate', 'certification'], prerequisiteValidation: true }),
    capacity: capacity(60, 35, 10, 5),
    registrationStatus: 'pending', enrollmentRequests: 48, approvedCount: 35, pendingCount: 13, waitlistedCount: 0, cancelledCount: 2,
    popularity: 74, revenuePlaceholder: 3149965,
  },
  {
    id: 'ce-5', courseId: 'c-105', courseName: 'Government Rural Livelihood Training', code: 'GRL-400',
    category: 'Government', language: 'Hindi', deliveryMode: 'offline', durationWeeks: 3,
    pricing: pricing({ model: 'government', baseFee: 0, visibility: 'public' }),
    policy: policy({ type: 'institution-approval', eligibilityRules: ['location', 'institution'] }),
    capacity: capacity(120, 96, 12, 0),
    registrationStatus: 'approved', enrollmentRequests: 140, approvedCount: 96, pendingCount: 20, waitlistedCount: 24, cancelledCount: 6,
    popularity: 81, revenuePlaceholder: 0,
  },
  {
    id: 'ce-6', courseId: 'c-106', courseName: 'Advanced Lab Techniques Workshop', code: 'ALT-250',
    category: 'Laboratory', language: 'English', deliveryMode: 'offline', durationWeeks: 1,
    pricing: pricing({ model: 'workshop', baseFee: 3499, enrollmentFee: 0, registrationFee: 199, lateRegistrationPlaceholder: 500 }),
    policy: policy({ type: 'invitation', eligibilityRules: ['certification', 'experience'] }),
    capacity: capacity(15, 12, 2, 1),
    registrationStatus: 'waitlisted', enrollmentRequests: 28, approvedCount: 12, pendingCount: 3, waitlistedCount: 13, cancelledCount: 0,
    popularity: 69, revenuePlaceholder: 41988,
  },
  {
    id: 'ce-7', courseId: 'c-107', courseName: 'Scholarship Youth Program', code: 'SYP-150',
    category: 'Cultivation', language: 'Tamil', deliveryMode: 'hybrid', durationWeeks: 5,
    pricing: pricing({ model: 'scholarship', baseFee: 0, visibility: 'public' }),
    policy: policy({ type: 'admin-approval', eligibilityRules: ['age', 'education'] }),
    capacity: capacity(50, 40, 5, 0),
    registrationStatus: 'open', enrollmentRequests: 88, approvedCount: 40, pendingCount: 18, waitlistedCount: 30, cancelledCount: 3,
    popularity: 85, revenuePlaceholder: 0,
  },
  {
    id: 'ce-8', courseId: 'c-108', courseName: 'Staff Onboarding: Cultivation SOPs', code: 'INT-010',
    category: 'Internal', language: 'English', deliveryMode: 'online', durationWeeks: 2,
    pricing: pricing({ model: 'internal', baseFee: 0, visibility: 'internal' }),
    policy: policy({ type: 'invitation' }),
    capacity: capacity(30, 22, 0, 0),
    registrationStatus: 'completed', enrollmentRequests: 22, approvedCount: 22, pendingCount: 0, waitlistedCount: 0, cancelledCount: 0,
    popularity: 55, revenuePlaceholder: 0,
  },
];

export const MOCK_ENROLLMENT_REQUESTS: EnrollmentRequest[] = [
  { id: 'er-1', courseId: 'c-101', courseName: 'Oyster Mushroom Cultivation', applicantName: 'Ravi Kumar', applicantType: 'individual', requestedDate: '2026-01-12', status: 'pending', pricingModel: 'paid', amountPlaceholder: 5749, eligibilityResult: 'pass' },
  { id: 'er-2', courseId: 'c-102', courseName: 'Spawn Production Masterclass', applicantName: 'Anita Desai', applicantType: 'individual', requestedDate: '2026-01-10', status: 'approved', pricingModel: 'premium', amountPlaceholder: 14499, eligibilityResult: 'pass' },
  { id: 'er-3', courseId: 'c-104', courseName: 'Corporate Agri-Skilling Program', applicantName: 'GreenFarms Pvt Ltd', applicantType: 'corporate', requestedDate: '2026-01-08', status: 'pending', pricingModel: 'corporate', amountPlaceholder: 89999, eligibilityResult: 'warning' },
  { id: 'er-4', courseId: 'c-105', courseName: 'Government Rural Livelihood Training', applicantName: 'Sunita Rao', applicantType: 'government', requestedDate: '2026-01-15', status: 'approved', pricingModel: 'government', amountPlaceholder: 0, eligibilityResult: 'pass' },
  { id: 'er-5', courseId: 'c-106', courseName: 'Advanced Lab Techniques Workshop', applicantName: 'Dr. Meera Nair', applicantType: 'individual', requestedDate: '2026-01-11', status: 'waitlisted', pricingModel: 'workshop', amountPlaceholder: 3698, eligibilityResult: 'pass' },
  { id: 'er-6', courseId: 'c-107', courseName: 'Scholarship Youth Program', applicantName: 'Karthik S', applicantType: 'individual', requestedDate: '2026-01-14', status: 'pending', pricingModel: 'scholarship', amountPlaceholder: 0, eligibilityResult: 'warning' },
  { id: 'er-7', courseId: 'c-101', courseName: 'Oyster Mushroom Cultivation', applicantName: 'Farm Collective Trust', applicantType: 'institution', requestedDate: '2026-01-09', status: 'confirmed', pricingModel: 'paid', amountPlaceholder: 5749, eligibilityResult: 'pass' },
  { id: 'er-8', courseId: 'c-104', courseName: 'Corporate Agri-Skilling Program', applicantName: 'AgroTech Solutions', applicantType: 'corporate', requestedDate: '2026-01-07', status: 'rejected', pricingModel: 'corporate', amountPlaceholder: 89999, eligibilityResult: 'fail' },
  { id: 'er-9', courseId: 'c-103', courseName: 'Home Mushroom Farming Basics', applicantName: 'Priya Menon', applicantType: 'individual', requestedDate: '2026-01-16', status: 'confirmed', pricingModel: 'free', amountPlaceholder: 0, eligibilityResult: 'pass' },
  { id: 'er-10', courseId: 'c-102', courseName: 'Spawn Production Masterclass', applicantName: 'Vikram Patil', applicantType: 'individual', requestedDate: '2026-01-13', status: 'cancelled', pricingModel: 'premium', amountPlaceholder: 14499, eligibilityResult: 'pass' },
];

export const MOCK_WAITLIST: WaitlistEntry[] = [
  { id: 'wl-1', courseId: 'c-102', courseName: 'Spawn Production Masterclass', applicantName: 'Deepak Shetty', position: 1, priority: 'high', addedDate: '2026-01-05', expiresDate: '2026-02-05', promotionMode: 'automatic' },
  { id: 'wl-2', courseId: 'c-102', courseName: 'Spawn Production Masterclass', applicantName: 'Lakshmi Iyer', position: 2, priority: 'medium', addedDate: '2026-01-06', expiresDate: '2026-02-06', promotionMode: 'automatic' },
  { id: 'wl-3', courseId: 'c-106', courseName: 'Advanced Lab Techniques Workshop', applicantName: 'Arjun Reddy', position: 1, priority: 'high', addedDate: '2026-01-04', expiresDate: '2026-01-28', promotionMode: 'manual' },
  { id: 'wl-4', courseId: 'c-107', courseName: 'Scholarship Youth Program', applicantName: 'Neha Gupta', position: 1, priority: 'high', addedDate: '2026-01-07', expiresDate: '2026-02-10', promotionMode: 'automatic' },
  { id: 'wl-5', courseId: 'c-107', courseName: 'Scholarship Youth Program', applicantName: 'Suresh Babu', position: 2, priority: 'low', addedDate: '2026-01-08', expiresDate: '2026-02-10', promotionMode: 'manual' },
  { id: 'wl-6', courseId: 'c-105', courseName: 'Government Rural Livelihood Training', applicantName: 'Kavya Nair', position: 1, priority: 'medium', addedDate: '2026-01-09', expiresDate: '2026-02-12', promotionMode: 'automatic' },
];

export const MOCK_ELIGIBILITY_RULES: EligibilityRule[] = [
  { id: 'el-1', criterion: 'age', label: 'Minimum Age 18', condition: 'applicant.age >= 18', required: true, mockResult: 'pass' },
  { id: 'el-2', criterion: 'education', label: 'Class 10 Completed', condition: 'applicant.education >= SSLC', required: false, mockResult: 'pass' },
  { id: 'el-3', criterion: 'previous-course', label: 'Completed Basics Course', condition: 'applicant.completed(HMF-100)', required: true, mockResult: 'warning' },
  { id: 'el-4', criterion: 'experience', label: '1 Year Field Experience', condition: 'applicant.experienceYears >= 1', required: false, mockResult: 'pass' },
  { id: 'el-5', criterion: 'certification', label: 'Lab Safety Certified', condition: 'applicant.hasCert(LAB-SAFETY)', required: true, mockResult: 'fail' },
  { id: 'el-6', criterion: 'location', label: 'Within Service Region', condition: 'applicant.region in servicedRegions', required: true, mockResult: 'pass' },
  { id: 'el-7', criterion: 'corporate', label: 'Verified Corporate Account', condition: 'applicant.corporateVerified', required: false, mockResult: 'pass' },
  { id: 'el-8', criterion: 'institution', label: 'Registered Institution', condition: 'applicant.institutionId != null', required: false, mockResult: 'warning' },
  { id: 'el-9', criterion: 'prerequisites', label: 'All Prerequisites Met', condition: 'applicant.prerequisitesMet', required: true, mockResult: 'pass' },
];
