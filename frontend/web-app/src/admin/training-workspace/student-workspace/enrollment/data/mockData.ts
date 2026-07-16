import type {
  EnrollmentRequest, Batch, CapacityInfo, EnrollmentTimelineEvent,
  EnrollmentDashboardStats, ApprovalAction, EnrollmentStatus, AdmissionType,
  TrainingMode, ApprovalStatus, BatchSlot,
} from '../types';

const courseNames = [
  'Mushroom Cultivation Foundation', 'Advanced Mushroom Production',
  'Spawn Production Technology', 'Value Added Mushroom Products',
  'Organic Mushroom Farming', 'Commercial Mushroom Business',
  'Mushroom Processing & Preservation', 'Oyster Mushroom Specialist',
  'Button Mushroom Cultivation', 'Medicinal Mushroom Cultivation',
];
const studentNames = [
  'Aarav Sharma', 'Priya Patel', 'Rohan Kumar', 'Ananya Singh',
  'Vikram Verma', 'Sneha Gupta', 'Arjun Reddy', 'Kavya Nair',
  'Rahul Joshi', 'Isha Deshmukh', 'Manish Tiwari', 'Neha Kapoor',
  'Suresh Rao', 'Deepa Menon', 'Amit Thakur', 'Pooja Agarwal',
  'Kiran Desai', 'Meera Iyer', 'Vijay Chauhan', 'Ritu Saxena',
];
const sources = ['Website', 'Referral', 'Walk-in', 'Camp', 'Government Camp', 'Partner NGO', 'Corporate HR', 'Marketing'];
const schemes = ['PM-KMY', 'PMFBY', 'NABARD Agri', 'State Agri Subsidy', null, 'Skill India', null, null];
const corporatePrograms = ['AgriCorp Training', null, null, null, null, 'FoodCo Skill', null, null];
const referralSources = ['Rajesh Verma', 'Sunita Patel', null, null, 'Kiran Desai', null, null, null];

function randItem<T>(arr: T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

function randDate(startYear: number, endYear: number): string {
  const year = startYear + Math.floor(Math.random() * (endYear - startYear + 1));
  const month = String(Math.floor(Math.random() * 12) + 1).padStart(2, '0');
  const day = String(Math.floor(Math.random() * 28) + 1).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

function addDays(date: string, days: number): string {
  const d = new Date(date);
  d.setDate(d.getDate() + days);
  return d.toISOString().split('T')[0];
}

function generateEnrollmentStatus(): { enrollmentStatus: EnrollmentStatus; approvalStatus: ApprovalStatus; seatReserved: boolean; enrolledDate: string | null } {
  const roll = Math.random();
  if (roll < 0.1) return { enrollmentStatus: 'draft', approvalStatus: 'pending-review', seatReserved: false, enrolledDate: null };
  if (roll < 0.2) return { enrollmentStatus: 'submitted', approvalStatus: 'pending-review', seatReserved: false, enrolledDate: null };
  if (roll < 0.3) return { enrollmentStatus: 'under-review', approvalStatus: 'under-review', seatReserved: false, enrolledDate: null };
  if (roll < 0.4) return { enrollmentStatus: 'pending-approval', approvalStatus: 'under-review', seatReserved: false, enrolledDate: null };
  if (roll < 0.55) return { enrollmentStatus: 'approved', approvalStatus: 'approved', seatReserved: false, enrolledDate: null };
  if (roll < 0.65) return { enrollmentStatus: 'seat-reserved', approvalStatus: 'approved', seatReserved: true, enrolledDate: null };
  if (roll < 0.75) return { enrollmentStatus: 'batch-assigned', approvalStatus: 'approved', seatReserved: true, enrolledDate: null };
  if (roll < 0.88) return { enrollmentStatus: 'enrolled', approvalStatus: 'approved', seatReserved: true, enrolledDate: randDate(2025, 2026) };
  if (roll < 0.94) return { enrollmentStatus: 'rejected', approvalStatus: 'rejected', seatReserved: false, enrolledDate: null };
  if (roll < 0.97) return { enrollmentStatus: 'cancelled', approvalStatus: 'rejected', seatReserved: false, enrolledDate: null };
  return { enrollmentStatus: 'archived', approvalStatus: 'approved', seatReserved: true, enrolledDate: null };
}

function generateTimeline(enrollmentId: string, appDate: string, status: EnrollmentStatus): EnrollmentTimelineEvent[] {
  const events: EnrollmentTimelineEvent[] = [];
  const push = (type: EnrollmentTimelineEvent['type'], label: string, description: string, date: string, completed: boolean) => {
    events.push({ id: `${enrollmentId}-tl-${events.length + 1}`, enrollmentId, type, label, description, date, completed });
  };
  push('application-created', 'Application Created', 'Enrollment request created', appDate, true);
  if (status === 'submitted' || status === 'under-review' || status === 'pending-approval' || status === 'approved' || status === 'seat-reserved' || status === 'batch-assigned' || status === 'enrolled' || status === 'rejected' || status === 'cancelled' || status === 'archived') {
    push('application-reviewed', 'Application Reviewed', 'Initial application review completed', addDays(appDate, 2), true);
  }
  if (status === 'under-review' || status === 'pending-approval' || status === 'approved' || status === 'seat-reserved' || status === 'batch-assigned' || status === 'enrolled' || status === 'archived') {
    push('approval', 'Approval', 'Application approved by administrator', addDays(appDate, 5), true);
  }
  if (status === 'seat-reserved' || status === 'batch-assigned' || status === 'enrolled' || status === 'archived') {
    push('seat-reserved', 'Seat Reserved', 'Student seat reserved in batch', addDays(appDate, 7), true);
  }
  if (status === 'batch-assigned' || status === 'enrolled' || status === 'archived') {
    push('batch-assigned', 'Batch Assigned', 'Student assigned to training batch', addDays(appDate, 10), true);
  }
  if (status === 'enrolled' || status === 'archived') {
    push('enrollment-completed', 'Enrollment Completed', 'Full enrollment processed', addDays(appDate, 14), true);
    push('student-activated', 'Student Activated', 'Student activated in registry', addDays(appDate, 15), true);
  }
  if (status === 'rejected') {
    push('rejected', 'Rejected', 'Application was not approved', addDays(appDate, 6), true);
  }
  if (status === 'cancelled') {
    push('cancelled', 'Cancelled', 'Student cancelled enrollment', addDays(appDate, 8), true);
  }
  if (status === 'archived') {
    push('archived', 'Archived', 'Enrollment record archived', addDays(appDate, 30), true);
  }
  return events;
}

function generateEnrollmentRequests(count: number): EnrollmentRequest[] {
  const requests: EnrollmentRequest[] = [];
  for (let i = 0; i < count; i++) {
    const studentName = randItem(studentNames);
    const studentId = `STU-${2025}-${String(i + 1).padStart(4, '0')}`;
    const courseName = randItem(courseNames);
    const courseId = `CRS-${String((i % 20) + 1).padStart(3, '0')}`;
    const admissionType = randItem<AdmissionType>(['regular', 'corporate', 'government', 'institution', 'online', 'offline', 'hybrid']);
    const trainingMode = admissionType === 'online' ? 'online' : admissionType === 'offline' ? 'offline' : admissionType === 'hybrid' ? 'hybrid' : randItem<TrainingMode>(['online', 'offline', 'hybrid']);
    const applicationDate = randDate(2025, 2026);
    const statusData = generateEnrollmentStatus();
    const batchId = statusData.seatReserved ? `BATCH-${String((i % 8) + 1).padStart(3, '0')}` : null;
    const batchName = batchId ? `Batch ${String((i % 8) + 1)} - ${['Morning', 'Evening', 'Weekend', 'Corporate'][i % 4]}` : null;
    const priority = i % 5 === 0 ? 'urgent' : i % 4 === 0 ? 'high' : i % 3 === 0 ? 'low' : 'normal';

    requests.push({
      id: `enr-${i + 1}`,
      enrollmentId: `ENR-${String(2025 - Math.floor(i / 12))}-${String(i + 1).padStart(5, '0')}`,
      applicationNumber: `APP-${String(2025 - Math.floor(i / 12))}-${String(i + 1).padStart(5, '0')}`,
      studentId,
      studentName,
      courseId,
      courseName,
      batchId,
      batchName,
      trainingMode,
      admissionType,
      enrollmentSource: randItem(sources),
      applicationDate,
      approvalStatus: statusData.approvalStatus,
      enrollmentStatus: statusData.enrollmentStatus,
      priority,
      referralSource: randItem(referralSources),
      governmentScheme: randItem(schemes),
      corporateProgram: randItem(corporatePrograms),
      seatReserved: statusData.seatReserved,
      enrolledDate: statusData.enrolledDate,
      lastUpdated: addDays(applicationDate, Math.floor(Math.random() * 20) + 1),
      notes: i % 3 === 0 ? null : `Special consideration for ${randItem(['scheme', 'corporate training', 'early enrollment', 'scholarship'])}` as string | null,
    });
  }
  return requests;
}

function generateBatches(): Batch[] {
  const slots: BatchSlot[] = ['morning', 'evening', 'weekend', 'corporate', 'government', 'institution', 'online', 'offline', 'hybrid'];
  return slots.map((slot, i) => {
    const capacity = i === 0 ? 30 : i === 1 ? 25 : i === 2 ? 20 : i === 3 ? 40 : i === 4 ? 35 : i === 5 ? 30 : i === 6 ? 100 : i === 7 ? 30 : 25;
    const filled = Math.floor(capacity * (0.3 + Math.random() * 0.6));
    const reserved = Math.floor((capacity - filled) * 0.3);
    const available = capacity - filled - reserved;
    return {
      id: `batch-${i + 1}`,
      batchId: `BATCH-${String(i + 1).padStart(3, '0')}`,
      batchName: `Batch ${i + 1} - ${slot.charAt(0).toUpperCase() + slot.slice(1)}`,
      courseId: `CRS-${String((i % 10) + 1).padStart(3, '0')}`,
      courseName: randItem(courseNames),
      capacity,
      availableSeats: available,
      reservedSeats: reserved,
      filledSeats: filled,
      slot,
      trainingMode: i >= 6 ? 'online' : i >= 4 ? 'hybrid' : 'offline',
      startDate: randDate(2026, 2026),
      endDate: `2026-${String(6 + Math.floor(Math.random() * 6)).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
      status: filled >= capacity ? 'full' : filled >= capacity * 0.8 ? 'filling' : 'active',
      assignedStudents: filled,
    };
  });
}

function generateCapacityInfo(batches: Batch[]): CapacityInfo[] {
  return batches.map((b) => ({
    batchId: b.batchId,
    batchName: b.batchName,
    maxCapacity: b.capacity,
    reservedSeats: b.reservedSeats,
    availableSeats: b.availableSeats,
    occupiedSeats: b.filledSeats,
    utilizationPercent: Math.round((b.filledSeats / b.capacity) * 100),
  }));
}

function generateDashboardStats(requests: EnrollmentRequest[], batches: Batch[]): EnrollmentDashboardStats {
  return {
    totalApplications: requests.length,
    pendingApproval: requests.filter((r) => r.enrollmentStatus === 'pending-approval' || r.enrollmentStatus === 'under-review').length,
    approved: requests.filter((r) => r.enrollmentStatus === 'approved' || r.enrollmentStatus === 'seat-reserved' || r.enrollmentStatus === 'batch-assigned' || r.enrollmentStatus === 'enrolled').length,
    rejected: requests.filter((r) => r.enrollmentStatus === 'rejected').length,
    reservedSeats: batches.reduce((s, b) => s + b.reservedSeats, 0),
    availableSeats: batches.reduce((s, b) => s + b.availableSeats, 0),
    activeBatches: batches.filter((b) => b.status === 'active' || b.status === 'filling').length,
    capacityUtilization: Math.round(
      (batches.reduce((s, b) => s + b.filledSeats, 0) / batches.reduce((s, b) => s + b.capacity, 0)) * 100,
    ),
    studentsAssigned: requests.filter((r) => r.enrollmentStatus === 'batch-assigned' || r.enrollmentStatus === 'enrolled').length,
    enrolledStudents: requests.filter((r) => r.enrollmentStatus === 'enrolled').length,
  };
}

function generateApprovalActions(requests: EnrollmentRequest[]): ApprovalAction[] {
  const actions: ApprovalAction[] = [];
  const adminNames = ['Rajesh Admin', 'Priya Admin', 'Amit Admin'];
  requests.slice(0, 20).forEach((r) => {
    if (r.enrollmentStatus === 'enrolled' || r.enrollmentStatus === 'batch-assigned' || r.enrollmentStatus === 'seat-reserved') {
      actions.push({
        id: `aa-${r.id}-approve`,
        enrollmentId: r.enrollmentId,
        action: 'approve',
        performedBy: randItem(adminNames),
        performedAt: addDays(r.applicationDate, 3),
        notes: null,
      });
      actions.push({
        id: `aa-${r.id}-reserve`,
        enrollmentId: r.enrollmentId,
        action: 'reserve-seat',
        performedBy: randItem(adminNames),
        performedAt: addDays(r.applicationDate, 7),
        notes: null,
      });
    }
    if (r.enrollmentStatus === 'batch-assigned' || r.enrollmentStatus === 'enrolled') {
      actions.push({
        id: `aa-${r.id}-batch`,
        enrollmentId: r.enrollmentId,
        action: 'assign-batch',
        performedBy: randItem(adminNames),
        performedAt: addDays(r.applicationDate, 10),
        notes: 'Batch assigned based on availability',
      });
    }
    if (r.enrollmentStatus === 'enrolled') {
      actions.push({
        id: `aa-${r.id}-complete`,
        enrollmentId: r.enrollmentId,
        action: 'complete-enrollment',
        performedBy: randItem(adminNames),
        performedAt: addDays(r.applicationDate, 14),
        notes: 'Enrollment completed successfully',
      });
    }
  });
  return actions;
}

const MOCK_ENROLLMENT_REQUESTS = generateEnrollmentRequests(40);
const MOCK_BATCHES = generateBatches();
const MOCK_CAPACITY = generateCapacityInfo(MOCK_BATCHES);
const MOCK_DASHBOARD_STATS = generateDashboardStats(MOCK_ENROLLMENT_REQUESTS, MOCK_BATCHES);
const MOCK_APPROVAL_ACTIONS = generateApprovalActions(MOCK_ENROLLMENT_REQUESTS);

export function getEnrollmentRequests(): EnrollmentRequest[] {
  return MOCK_ENROLLMENT_REQUESTS;
}

export function getEnrollmentRequestById(id: string): EnrollmentRequest | undefined {
  return MOCK_ENROLLMENT_REQUESTS.find((r) => r.id === id || r.enrollmentId === id);
}

export function getBatches(): Batch[] {
  return MOCK_BATCHES;
}

export function getBatchById(id: string): Batch | undefined {
  return MOCK_BATCHES.find((b) => b.id === id || b.batchId === id);
}

export function getCapacityInfo(): CapacityInfo[] {
  return MOCK_CAPACITY;
}

export function getDashboardStats(): EnrollmentDashboardStats {
  return MOCK_DASHBOARD_STATS;
}

export function getApprovalActions(): ApprovalAction[] {
  return MOCK_APPROVAL_ACTIONS;
}

export function getTimelineForEnrollment(enrollmentId: string): EnrollmentTimelineEvent[] {
  const req = MOCK_ENROLLMENT_REQUESTS.find((r) => r.enrollmentId === enrollmentId);
  if (!req) return [];
  return generateTimeline(req.enrollmentId, req.applicationDate, req.enrollmentStatus);
}

export function getEnrollmentRequestsByStatus(status: EnrollmentStatus): EnrollmentRequest[] {
  return MOCK_ENROLLMENT_REQUESTS.filter((r) => r.enrollmentStatus === status);
}

export function getPendingApprovals(): EnrollmentRequest[] {
  return MOCK_ENROLLMENT_REQUESTS.filter(
    (r) => r.enrollmentStatus === 'pending-approval' || r.enrollmentStatus === 'under-review',
  );
}

export function getArchivedEnrollments(): EnrollmentRequest[] {
  return MOCK_ENROLLMENT_REQUESTS.filter((r) => r.enrollmentStatus === 'archived');
}
