import type {
  AttendanceRecord, AttendanceSession, AttendanceSummary, CalendarMonth,
  LowAttendanceAlert, AttendanceTimelineEvent, AttendanceDashboardStats, AttendanceStatus,
} from '../types';
import { DEFAULT_POLICIES } from '../types';

const studentNames = [
  'Aarav Sharma', 'Priya Patel', 'Rohan Kumar', 'Ananya Singh', 'Vikram Verma',
  'Sneha Gupta', 'Arjun Reddy', 'Kavya Nair', 'Rahul Joshi', 'Isha Deshmukh',
  'Manish Tiwari', 'Neha Kapoor', 'Suresh Rao', 'Deepa Menon', 'Amit Thakur',
  'Pooja Agarwal', 'Kiran Desai', 'Meera Iyer', 'Vijay Chauhan', 'Ritu Saxena',
  'Ravi Singh', 'Divya Sharma', 'Sanjay Patel', 'Anita Gupta', 'Rajesh Kumar',
];
const courseNames = [
  'Mushroom Cultivation Foundation', 'Advanced Mushroom Production', 'Spawn Production Technology',
  'Value Added Mushroom Products', 'Organic Mushroom Farming', 'Commercial Mushroom Business',
  'Mushroom Processing & Preservation', 'Oyster Mushroom Specialist',
];
const batchNames = ['Batch 1 - Morning', 'Batch 2 - Evening', 'Batch 3 - Weekend', 'Batch 4 - Corporate', 'Batch 5 - Government', 'Batch 6 - Online'];
const trainers = ['Dr. Rajesh Agrawal', 'Prof. Sunita Patil', 'Mr. Amit Verma', 'Ms. Deepa Joshi', 'Dr. Kiran Deshmukh'];

function randItem<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randDate(start: string, end: string): string {
  const s = new Date(start).getTime(), e = new Date(end).getTime();
  return new Date(s + Math.random() * (e - s)).toISOString().split('T')[0];
}

function addDays(date: string, days: number): string {
  const d = new Date(date); d.setDate(d.getDate() + days); return d.toISOString().split('T')[0];
}

function genAttendanceStatus(): AttendanceStatus {
  const r = Math.random();
  if (r < 0.65) return 'present';
  if (r < 0.78) return 'absent';
  if (r < 0.85) return 'late';
  if (r < 0.90) return 'half-day';
  if (r < 0.94) return 'excused';
  if (r < 0.97) return 'medical-leave';
  return 'not-marked';
}

function generateAttendanceRecords(): AttendanceRecord[] {
  const records: AttendanceRecord[] = [];
  let id = 1;
  for (const studentName of studentNames) {
    const studentId = `STU-${String(2025 - (id % 2))}-${String(id).padStart(4, '0')}`;
    const courseName = randItem(courseNames);
    const batchName = randItem(batchNames);
    const batchId = `BATCH-${String(batchNames.indexOf(batchName) + 1).padStart(3, '0')}`;
    const sessions = 10 + Math.floor(Math.random() * 20);
    for (let s = 0; s < sessions; s++) {
      const trainingDate = randDate('2026-01-01', '2026-07-16');
      const status = genAttendanceStatus();
      const checkIn = status === 'present' || status === 'late' ? `09:${String(Math.floor(Math.random() * 45) + 10).padStart(2, '0')}` : null;
      const checkOut = checkIn ? `16:${String(Math.floor(Math.random() * 30)).padStart(2, '0')}` : null;
      records.push({
        id: `att-${id}-${s}`,
        attendanceId: `ATT-${String(id).padStart(5, '0')}-${String(s + 1).padStart(3, '0')}`,
        studentId,
        studentName,
        enrollmentId: `ENR-${String(id).padStart(5, '0')}`,
        courseId: `CRS-${String((id % 8) + 1).padStart(3, '0')}`,
        courseName,
        batchId,
        batchName,
        trainingDate,
        attendanceStatus: status,
        checkInTime: checkIn,
        checkOutTime: checkOut,
        trainingMode: randItem(['online', 'offline', 'hybrid']),
        trainerName: randItem(trainers),
        sessionName: `Session ${s + 1}`,
        remarks: status === 'absent' ? randItem(['No reason provided', 'Family emergency', 'Health issue', null]) as string | null : status === 'late' ? randItem(['Traffic', 'Personal delay', null]) as string | null : null,
        createdDate: trainingDate,
        lastUpdated: addDays(trainingDate, Math.floor(Math.random() * 3)),
      });
    }
    id++;
  }
  return records;
}

function generateSessions(records: AttendanceRecord[]): AttendanceSession[] {
  const grouped = new Map<string, AttendanceRecord[]>();
  records.forEach((r) => {
    const key = `${r.batchId}|${r.trainingDate}`;
    if (!grouped.has(key)) grouped.set(key, []);
    grouped.get(key)!.push(r);
  });
  return Array.from(grouped.entries()).map(([key, recs], i) => {
    const [batchId] = key.split('|');
    const date = recs[0].trainingDate;
    const present = recs.filter((r) => r.attendanceStatus === 'present').length;
    const absent = recs.filter((r) => r.attendanceStatus === 'absent').length;
    const late = recs.filter((r) => r.attendanceStatus === 'late').length;
    return {
      id: `sess-${i + 1}`,
      sessionName: `Session ${i + 1}`,
      batchId,
      batchName: recs[0].batchName,
      courseId: recs[0].courseId,
      courseName: recs[0].courseName,
      date,
      startTime: '09:00',
      endTime: '17:00',
      totalStudents: recs.length,
      markedCount: recs.filter((r) => r.attendanceStatus !== 'not-marked').length,
      presentCount: present,
      absentCount: absent,
      lateCount: late,
      status: new Date(date) < new Date('2026-07-16') ? 'completed' : 'scheduled',
    };
  });
}

function generateSummary(records: AttendanceRecord[]): AttendanceSummary {
  const present = records.filter((r) => r.attendanceStatus === 'present').length;
  const absent = records.filter((r) => r.attendanceStatus === 'absent').length;
  const late = records.filter((r) => r.attendanceStatus === 'late').length;
  const halfDay = records.filter((r) => r.attendanceStatus === 'half-day').length;
  const excused = records.filter((r) => r.attendanceStatus === 'excused' || r.attendanceStatus === 'medical-leave').length;
  const marked = records.filter((r) => r.attendanceStatus !== 'not-marked').length;
  const overallPercent = marked > 0 ? Math.round((present / marked) * 100) : 0;

  const courseGroups = new Map<string, { present: number; total: number }>();
  records.filter((r) => r.attendanceStatus !== 'not-marked').forEach((r) => {
    if (!courseGroups.has(r.courseName)) courseGroups.set(r.courseName, { present: 0, total: 0 });
    const g = courseGroups.get(r.courseName)!;
    g.total++;
    if (r.attendanceStatus === 'present') g.present++;
    g.present += r.attendanceStatus === 'late' || r.attendanceStatus === 'half-day' ? 0.5 : 0;
  });
  const courseWise = Array.from(courseGroups.entries()).map(([course, g]) => ({ course, percent: Math.round((g.present / g.total) * 100) }));

  const batchGroups = new Map<string, { present: number; total: number }>();
  records.filter((r) => r.attendanceStatus !== 'not-marked').forEach((r) => {
    if (!batchGroups.has(r.batchName)) batchGroups.set(r.batchName, { present: 0, total: 0 });
    const g = batchGroups.get(r.batchName)!;
    g.total++;
    if (r.attendanceStatus === 'present') g.present++;
  });
  const batchWise = Array.from(batchGroups.entries()).map(([batch, g]) => ({ batch, percent: Math.round((g.present / g.total) * 100) }));

  const monthlyTrend = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'].map((month) => ({
    month, percent: 65 + Math.floor(Math.random() * 30),
  }));

  return { totalSessions: new Set(records.map((r) => `${r.batchId}|${r.trainingDate}`)).size, totalPresent: present, totalAbsent: absent, totalLate: late, totalHalfDay: halfDay, totalExcused: excused, overallPercent, monthlyTrend, courseWise, batchWise };
}

const MOCK_RECORDS = generateAttendanceRecords();
const MOCK_SESSIONS = generateSessions(MOCK_RECORDS);
const MOCK_SUMMARY = generateSummary(MOCK_RECORDS);

export function getAttendanceRecords(): AttendanceRecord[] { return MOCK_RECORDS; }

export function getAttendanceSessions(): AttendanceSession[] { return MOCK_SESSIONS; }

export function getAttendanceSummary(): AttendanceSummary { return MOCK_SUMMARY; }

export function getAttendancePolicies() { return DEFAULT_POLICIES; }

export function getCalendarData(year: number, month: number): CalendarMonth {
  const daysInMonth = new Date(year, month, 0).getDate();
  const days: CalendarMonth['days'] = [];
  for (let d = 1; d <= daysInMonth; d++) {
    const date = `${year}-${String(month).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
    const dayOfWeek = new Date(year, month - 1, d).getDay();
    const isHoliday = dayOfWeek === 0;
    const dateRecords = MOCK_RECORDS.filter((r) => r.trainingDate === date);
    days.push({
      date, dayOfWeek, isTrainingDay: dateRecords.length > 0, isHoliday,
      hasSession: dateRecords.length > 0,
      attendancePercent: dateRecords.length > 0
        ? Math.round((dateRecords.filter((r) => r.attendanceStatus === 'present').length / dateRecords.length) * 100)
        : null,
      recordsCount: dateRecords.length,
    });
  }
  return { year, month, days };
}

export function getLowAttendanceAlerts(): LowAttendanceAlert[] {
  return studentNames.slice(0, 4).map((name, i) => ({
    studentId: `STU-2025-${String(i + 1).padStart(4, '0')}`,
    studentName: name,
    batchName: randItem(batchNames),
    courseName: randItem(courseNames),
    attendancePercent: 40 + Math.floor(Math.random() * 30),
    threshold: 75,
    consecutiveLowMonths: 1 + Math.floor(Math.random() * 3),
  }));
}

export function getAttendanceTimeline(): AttendanceTimelineEvent[] {
  return [
    { id: 'atl-1', studentId: 'STU-2025-0001', type: 'enrollment', label: 'Student Enrolled', description: 'Enrolled in Mushroom Cultivation Foundation', date: '2026-01-15', completed: true },
    { id: 'atl-2', studentId: 'STU-2025-0001', type: 'first-session', label: 'First Session Attended', description: 'Attended orientation and first training session', date: '2026-01-22', completed: true },
    { id: 'atl-3', studentId: 'STU-2025-0001', type: 'attendance-recorded', label: '15 Sessions Completed', description: 'Completed 15 training sessions with 80% attendance', date: '2026-03-10', completed: true },
    { id: 'atl-4', studentId: 'STU-2025-0001', type: 'leave', label: 'Medical Leave', description: 'Medical leave for 3 days (doctor certificate submitted)', date: '2026-04-05', completed: true },
    { id: 'atl-5', studentId: 'STU-2025-0001', type: 'attendance-milestone', label: '75% Attendance Milestone', description: 'Reached minimum attendance threshold for certification', date: '2026-05-20', completed: true },
    { id: 'atl-6', studentId: 'STU-2025-0001', type: 'course-completion', label: 'Course Completion', description: 'Completed all sessions with 82% attendance', date: '2026-07-01', completed: false },
  ];
}

export function getDashboardStats(): AttendanceDashboardStats {
  const today = new Date().toISOString().split('T')[0];
  const todayRecords = MOCK_RECORDS.filter((r) => r.trainingDate === today);
  return {
    todaySessions: new Set(todayRecords.map((r) => r.batchId)).size,
    studentsPresent: todayRecords.filter((r) => r.attendanceStatus === 'present').length,
    studentsAbsent: todayRecords.filter((r) => r.attendanceStatus === 'absent').length,
    lateArrivals: todayRecords.filter((r) => r.attendanceStatus === 'late').length,
    halfDay: todayRecords.filter((r) => r.attendanceStatus === 'half-day').length,
    overallPercent: MOCK_SUMMARY.overallPercent,
    monthlyTrend: MOCK_SUMMARY.monthlyTrend,
    lowAttendanceAlerts: getLowAttendanceAlerts(),
  };
}
