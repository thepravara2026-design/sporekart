export type AttendanceStatus = 'present' | 'absent' | 'late' | 'half-day' | 'excused' | 'medical-leave' | 'holiday' | 'cancelled-session' | 'not-marked';

export type BatchSlot = 'morning' | 'evening' | 'weekend' | 'corporate' | 'institution' | 'online' | 'hybrid' | 'government';

export type ViewMode = 'daily' | 'weekly' | 'monthly';

export interface AttendanceRecord {
  id: string;
  attendanceId: string;
  studentId: string;
  studentName: string;
  enrollmentId: string;
  courseId: string;
  courseName: string;
  batchId: string;
  batchName: string;
  trainingDate: string;
  attendanceStatus: AttendanceStatus;
  checkInTime: string | null;
  checkOutTime: string | null;
  trainingMode: string;
  trainerName: string | null;
  sessionName: string | null;
  remarks: string | null;
  createdDate: string;
  lastUpdated: string;
}

export interface AttendancePolicy {
  id: string;
  policyName: string;
  minAttendancePercent: number;
  description: string;
  appliesTo: string[];
  isActive: boolean;
  consequences: string;
  gracePeriodMinutes: number;
  lateThresholdMinutes: number;
  halfDayThresholdMinutes: number;
}

export interface AttendanceSession {
  id: string;
  sessionName: string;
  batchId: string;
  batchName: string;
  courseId: string;
  courseName: string;
  date: string;
  startTime: string;
  endTime: string;
  totalStudents: number;
  markedCount: number;
  presentCount: number;
  absentCount: number;
  lateCount: number;
  status: 'scheduled' | 'in-progress' | 'completed' | 'cancelled';
}

export interface AttendanceSummary {
  totalSessions: number;
  totalPresent: number;
  totalAbsent: number;
  totalLate: number;
  totalHalfDay: number;
  totalExcused: number;
  overallPercent: number;
  monthlyTrend: { month: string; percent: number }[];
  courseWise: { course: string; percent: number }[];
  batchWise: { batch: string; percent: number }[];
}

export interface CalendarDay {
  date: string;
  dayOfWeek: number;
  isTrainingDay: boolean;
  isHoliday: boolean;
  hasSession: boolean;
  attendancePercent: number | null;
  recordsCount: number;
}

export interface CalendarMonth {
  year: number;
  month: number;
  days: CalendarDay[];
}

export interface LowAttendanceAlert {
  studentId: string;
  studentName: string;
  batchName: string;
  courseName: string;
  attendancePercent: number;
  threshold: number;
  consecutiveLowMonths: number;
}

export interface AttendanceTimelineEvent {
  id: string;
  studentId: string;
  type: 'enrollment' | 'first-session' | 'attendance-recorded' | 'leave' | 'attendance-milestone' | 'course-completion';
  label: string;
  description: string;
  date: string;
  completed: boolean;
}

export interface AttendanceDashboardStats {
  todaySessions: number;
  studentsPresent: number;
  studentsAbsent: number;
  lateArrivals: number;
  halfDay: number;
  overallPercent: number;
  monthlyTrend: { month: string; percent: number }[];
  lowAttendanceAlerts: LowAttendanceAlert[];
}

export const ATTENDANCE_STATUS_LABELS: Record<AttendanceStatus, string> = {
  'present': 'Present',
  'absent': 'Absent',
  'late': 'Late',
  'half-day': 'Half Day',
  'excused': 'Excused',
  'medical-leave': 'Medical Leave',
  'holiday': 'Holiday',
  'cancelled-session': 'Cancelled',
  'not-marked': 'Not Marked',
};

export const ATTENDANCE_STATUS_VARIANTS: Record<AttendanceStatus, 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral'> = {
  'present': 'success',
  'absent': 'danger',
  'late': 'warning',
  'half-day': 'warning',
  'excused': 'info',
  'medical-leave': 'info',
  'holiday': 'neutral',
  'cancelled-session': 'neutral',
  'not-marked': 'default',
};

export const DEFAULT_POLICIES: AttendancePolicy[] = [
  { id: 'pol-1', policyName: 'Standard Attendance', minAttendancePercent: 75, description: 'Minimum 75% attendance required for course completion', appliesTo: ['regular', 'online', 'hybrid'], isActive: true, consequences: 'Ineligible for certification and assessment', gracePeriodMinutes: 15, lateThresholdMinutes: 15, halfDayThresholdMinutes: 240 },
  { id: 'pol-2', policyName: 'Corporate Training', minAttendancePercent: 85, description: 'Higher attendance standard for corporate programs', appliesTo: ['corporate'], isActive: true, consequences: 'Employer notification and program review', gracePeriodMinutes: 10, lateThresholdMinutes: 10, halfDayThresholdMinutes: 180 },
  { id: 'pol-3', policyName: 'Government Program', minAttendancePercent: 80, description: 'Government scheme attendance compliance', appliesTo: ['government'], isActive: true, consequences: 'Affects scheme eligibility and stipend', gracePeriodMinutes: 20, lateThresholdMinutes: 20, halfDayThresholdMinutes: 240 },
  { id: 'pol-4', policyName: 'Medical Leave Policy', minAttendancePercent: 70, description: 'Medical leave count towards attendance with doctor certificate', appliesTo: ['regular', 'corporate', 'government'], isActive: true, consequences: 'Medical certificate required for leave > 3 consecutive days', gracePeriodMinutes: 30, lateThresholdMinutes: 30, halfDayThresholdMinutes: 240 },
];

export const ATTENDANCE_NAV_ITEMS = [
  { id: 'attendance', label: 'Attendance Dashboard', icon: 'layout', description: 'Attendance overview' },
  { id: 'attendance/register', label: 'Attendance Register', icon: 'list', description: 'Daily attendance records' },
  { id: 'attendance/calendar', label: 'Attendance Calendar', icon: 'calendar', description: 'Calendar view' },
  { id: 'attendance/analytics', label: 'Attendance Analytics', icon: 'bar-chart', description: 'Attendance analytics' },
  { id: 'attendance/timeline', label: 'Attendance Timeline', icon: 'clock', description: 'Attendance timeline' },
  { id: 'attendance/policies', label: 'Policy Center', icon: 'shield', description: 'Attendance policies' },
];
