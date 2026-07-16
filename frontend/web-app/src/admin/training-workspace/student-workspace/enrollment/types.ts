export type EnrollmentStatus =
  | 'draft'
  | 'submitted'
  | 'under-review'
  | 'pending-approval'
  | 'approved'
  | 'rejected'
  | 'waitlisted'
  | 'seat-reserved'
  | 'batch-assigned'
  | 'enrolled'
  | 'cancelled'
  | 'archived';

export type AdmissionType =
  | 'regular'
  | 'corporate'
  | 'government'
  | 'institution'
  | 'scholarship'
  | 'partner-organization'
  | 'franchise'
  | 'online'
  | 'offline'
  | 'hybrid';

export type TrainingMode = 'online' | 'offline' | 'hybrid';

export type ApprovalStatus = 'pending-review' | 'under-review' | 'approved' | 'rejected' | 'needs-info';

export type EnrollmentPriority = 'low' | 'normal' | 'high' | 'urgent';

export type BatchSlot = 'morning' | 'evening' | 'weekend' | 'corporate' | 'government' | 'institution' | 'online' | 'offline' | 'hybrid';

export interface EnrollmentRequest {
  id: string;
  enrollmentId: string;
  applicationNumber: string;
  studentId: string;
  studentName: string;
  courseId: string;
  courseName: string;
  batchId: string | null;
  batchName: string | null;
  trainingMode: TrainingMode;
  admissionType: AdmissionType;
  enrollmentSource: string;
  applicationDate: string;
  approvalStatus: ApprovalStatus;
  enrollmentStatus: EnrollmentStatus;
  priority: EnrollmentPriority;
  referralSource: string | null;
  governmentScheme: string | null;
  corporateProgram: string | null;
  seatReserved: boolean;
  enrolledDate: string | null;
  lastUpdated: string;
  notes: string | null;
}

export interface Batch {
  id: string;
  batchId: string;
  batchName: string;
  courseId: string;
  courseName: string;
  capacity: number;
  availableSeats: number;
  reservedSeats: number;
  filledSeats: number;
  slot: BatchSlot;
  trainingMode: TrainingMode;
  startDate: string;
  endDate: string;
  status: 'active' | 'filling' | 'full' | 'completed' | 'cancelled';
  assignedStudents: number;
}

export interface CapacityInfo {
  batchId: string;
  batchName: string;
  maxCapacity: number;
  reservedSeats: number;
  availableSeats: number;
  occupiedSeats: number;
  utilizationPercent: number;
}

export interface EnrollmentTimelineEvent {
  id: string;
  enrollmentId: string;
  type: 'application-created' | 'application-reviewed' | 'approval' | 'seat-reserved' | 'batch-assigned' | 'enrollment-completed' | 'student-activated' | 'rejected' | 'cancelled' | 'waitlisted' | 'archived';
  label: string;
  description: string;
  date: string;
  completed: boolean;
}

export interface AdmissionPipelineStage {
  id: string;
  label: string;
  status: 'completed' | 'active' | 'pending' | 'skipped' | 'failed';
  order: number;
  date: string | null;
}

export interface EnrollmentDashboardStats {
  totalApplications: number;
  pendingApproval: number;
  approved: number;
  rejected: number;
  reservedSeats: number;
  availableSeats: number;
  activeBatches: number;
  capacityUtilization: number;
  studentsAssigned: number;
  enrolledStudents: number;
}

export interface ApprovalAction {
  id: string;
  enrollmentId: string;
  action: 'approve' | 'reject' | 'request-info' | 'reserve-seat' | 'assign-batch' | 'complete-enrollment';
  performedBy: string;
  performedAt: string;
  notes: string | null;
}

export const ENROLLMENT_STATUS_LABELS: Record<EnrollmentStatus, string> = {
  'draft': 'Draft',
  'submitted': 'Submitted',
  'under-review': 'Under Review',
  'pending-approval': 'Pending Approval',
  'approved': 'Approved',
  'rejected': 'Rejected',
  'waitlisted': 'Waitlisted',
  'seat-reserved': 'Seat Reserved',
  'batch-assigned': 'Batch Assigned',
  'enrolled': 'Enrolled',
  'cancelled': 'Cancelled',
  'archived': 'Archived',
};

export const ENROLLMENT_STATUS_VARIANTS: Record<EnrollmentStatus, 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'draft': 'neutral',
  'submitted': 'info',
  'under-review': 'warning',
  'pending-approval': 'warning',
  'approved': 'success',
  'rejected': 'danger',
  'waitlisted': 'default',
  'seat-reserved': 'primary',
  'batch-assigned': 'primary',
  'enrolled': 'success',
  'cancelled': 'default',
  'archived': 'neutral',
};

export const ADMISSION_TYPE_LABELS: Record<AdmissionType, string> = {
  'regular': 'Regular',
  'corporate': 'Corporate',
  'government': 'Government',
  'institution': 'Institution',
  'scholarship': 'Scholarship',
  'partner-organization': 'Partner Organization',
  'franchise': 'Franchise',
  'online': 'Online',
  'offline': 'Offline',
  'hybrid': 'Hybrid',
};

export const BATCH_STATUS_LABELS: Record<Batch['status'], string> = {
  'active': 'Active',
  'filling': 'Filling',
  'full': 'Full',
  'completed': 'Completed',
  'cancelled': 'Cancelled',
};

export const APPROVAL_STATUS_LABELS: Record<ApprovalStatus, string> = {
  'pending-review': 'Pending Review',
  'under-review': 'Under Review',
  'approved': 'Approved',
  'rejected': 'Rejected',
  'needs-info': 'Needs Info',
};

export const ENROLLMENT_NAV_ITEMS = [
  { id: 'enrollment', label: 'Enrollment Dashboard', icon: 'layout', description: 'Enrollment overview dashboard' },
  { id: 'enrollment/requests', label: 'Enrollment Requests', icon: 'file-text', description: 'All enrollment requests' },
  { id: 'enrollment/approval', label: 'Approval Queue', icon: 'check-circle', description: 'Pending approval queue' },
  { id: 'enrollment/batches', label: 'Batch Management', icon: 'layers', description: 'Batch allocation management' },
  { id: 'enrollment/capacity', label: 'Capacity Dashboard', icon: 'bar-chart', description: 'Capacity utilization' },
  { id: 'enrollment/timeline', label: 'Enrollment Timeline', icon: 'clock', description: 'Enrollment activity timeline' },
  { id: 'enrollment/archived', label: 'Archived', icon: 'archive', description: 'Archived enrollments' },
];

export const ADMISSION_PIPELINE: { id: string; label: string; description: string }[] = [
  { id: 'prospective', label: 'Prospective Student', description: 'Initial interest recorded' },
  { id: 'course-selected', label: 'Course Selected', description: 'Training program chosen' },
  { id: 'application-submitted', label: 'Application Submitted', description: 'Enrollment request filed' },
  { id: 'application-review', label: 'Application Review', description: 'Administrative review in progress' },
  { id: 'approval-pending', label: 'Approval Pending', description: 'Awaiting approval decision' },
  { id: 'approved', label: 'Approved', description: 'Application approved' },
  { id: 'seat-reserved', label: 'Seat Reserved', description: 'Capacity allocated' },
  { id: 'batch-assigned', label: 'Batch Assigned', description: 'Batch allocation completed' },
  { id: 'enrollment-completed', label: 'Enrollment Completed', description: 'Full enrollment processed' },
  { id: 'student-activated', label: 'Student Activated', description: 'Active in student registry' },
];
