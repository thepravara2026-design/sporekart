export interface StatItem {
  id: string;
  label: string;
  value: number;
  trend?: 'up' | 'down' | 'neutral';
  icon: string;
}

export interface QuickAction {
  id: string;
  label: string;
  description: string;
  icon: string;
}

export interface ActivityItem {
  id: string;
  type: 'course' | 'batch' | 'trainer' | 'student' | 'resource' | 'assessment' | 'certificate' | 'announcement';
  message: string;
  timestamp: string;
  user: string;
}

export interface UpcomingTraining {
  id: string;
  title: string;
  batch: string;
  trainer: string;
  venue: string;
  mode: 'online' | 'offline' | 'hybrid';
  capacity: number;
  seatsRemaining: number;
  status: 'scheduled' | 'in-progress' | 'completed' | 'cancelled';
  date: string;
}

export interface Announcement {
  id: string;
  title: string;
  content: string;
  date: string;
  priority: 'high' | 'normal' | 'low';
}

export interface TrainingDashboardData {
  stats: StatItem[];
  quickActions: QuickAction[];
  recentActivities: ActivityItem[];
  upcomingTrainings: UpcomingTraining[];
  announcements: Announcement[];
}

export const MOCK_DASHBOARD_DATA: TrainingDashboardData = {
  stats: [
    { id: 'total-courses', label: 'Total Courses', value: 124, trend: 'up', icon: 'book-open' },
    { id: 'active-courses', label: 'Active Courses', value: 47, trend: 'up', icon: 'play-circle' },
    { id: 'upcoming-batches', label: 'Upcoming Batches', value: 18, trend: 'neutral', icon: 'calendar' },
    { id: 'today-sessions', label: "Today's Sessions", value: 6, trend: 'up', icon: 'clock' },
    { id: 'students-enrolled', label: 'Students Enrolled', value: 2847, trend: 'up', icon: 'users' },
    { id: 'trainers', label: 'Trainers', value: 42, trend: 'neutral', icon: 'user-check' },
    { id: 'certificates-issued', label: 'Certificates Issued', value: 1523, trend: 'up', icon: 'award' },
    { id: 'pending-approvals', label: 'Pending Approvals', value: 12, trend: 'down', icon: 'clock' },
  ],
  quickActions: [
    { id: 'create-course', label: 'Create Course', description: 'Build a new training course', icon: 'book-plus' },
    { id: 'create-batch', label: 'Create Batch', description: 'Schedule a new training batch', icon: 'calendar-plus' },
    { id: 'register-student', label: 'Register Student', description: 'Enroll a new student', icon: 'user-plus' },
    { id: 'add-trainer', label: 'Add Trainer', description: 'Onboard a new trainer', icon: 'user-check' },
    { id: 'upload-resource', label: 'Upload Resource', description: 'Add learning materials', icon: 'upload' },
    { id: 'schedule-training', label: 'Schedule Training', description: 'Plan a training session', icon: 'clock' },
    { id: 'create-announcement', label: 'Create Announcement', description: 'Post an announcement', icon: 'message-circle' },
    { id: 'generate-report', label: 'Generate Report', description: 'Create training reports', icon: 'bar-chart' },
  ],
  recentActivities: [
    { id: 'act-1', type: 'course', message: 'Mushroom Cultivation 101 course was created', timestamp: '2 hours ago', user: 'Admin' },
    { id: 'act-2', type: 'batch', message: 'Batch B-2026-07 (Spawn Production) was published', timestamp: '3 hours ago', user: 'Admin' },
    { id: 'act-3', type: 'trainer', message: 'Dr. Rajesh Kumar was assigned to Commercial Cultivation', timestamp: '5 hours ago', user: 'Admin' },
    { id: 'act-4', type: 'student', message: 'Priya Sharma was registered for Batch B-2026-07', timestamp: '1 day ago', user: 'Admin' },
    { id: 'act-5', type: 'resource', message: 'Mushroom Disease Handbook was uploaded', timestamp: '1 day ago', user: 'Admin' },
    { id: 'act-6', type: 'assessment', message: 'Module 2 Assessment for Oyster Farming was created', timestamp: '2 days ago', user: 'Admin' },
    { id: 'act-7', type: 'certificate', message: 'Certificate issued to Amit Verma for Spawn Production', timestamp: '2 days ago', user: 'System' },
    { id: 'act-8', type: 'announcement', message: 'New FSSAI compliance training announced', timestamp: '3 days ago', user: 'Admin' },
  ],
  upcomingTrainings: [
    { id: 'ut-1', title: 'Mushroom Cultivation 101', batch: 'B-2026-07', trainer: 'Dr. Rajesh Kumar', venue: 'Training Hall A', mode: 'offline', capacity: 30, seatsRemaining: 8, status: 'scheduled', date: '2026-07-20' },
    { id: 'ut-2', title: 'Spawn Production Techniques', batch: 'B-2026-08', trainer: 'Prof. Sunita Patel', venue: 'Lab 3', mode: 'offline', capacity: 20, seatsRemaining: 3, status: 'scheduled', date: '2026-07-22' },
    { id: 'ut-3', title: 'Commercial Oyster Farming', batch: 'B-2026-09', trainer: 'Dr. Anand Desai', venue: 'Online', mode: 'online', capacity: 100, seatsRemaining: 42, status: 'scheduled', date: '2026-07-25' },
    { id: 'ut-4', title: 'Quality Control & Grading', batch: 'B-2026-10', trainer: 'Dr. Meera Nair', venue: 'Training Hall B', mode: 'hybrid', capacity: 50, seatsRemaining: 18, status: 'scheduled', date: '2026-07-28' },
    { id: 'ut-5', title: 'Mushroom Disease Management', batch: 'B-2026-11', trainer: 'Dr. Rajesh Kumar', venue: 'Online', mode: 'online', capacity: 80, seatsRemaining: 55, status: 'scheduled', date: '2026-08-01' },
    { id: 'ut-6', title: 'FSSAI Compliance Training', batch: 'B-2026-12', trainer: 'Mr. Vikram Joshi', venue: 'Conference Room', mode: 'offline', capacity: 25, seatsRemaining: 10, status: 'scheduled', date: '2026-08-05' },
  ],
  announcements: [
    { id: 'ann-1', title: 'New FSSAI Compliance Module', content: 'All trainers must complete the new FSSAI compliance training before conducting hands-on sessions.', date: '2026-07-15', priority: 'high' },
    { id: 'ann-2', title: 'Spawn Production Batch Update', content: 'B-2026-08 schedule has been updated. Please check the new timing.', date: '2026-07-14', priority: 'normal' },
    { id: 'ann-3', title: 'Platform Maintenance', content: 'The training platform will be under maintenance on July 18th from 2 AM to 4 AM.', date: '2026-07-13', priority: 'low' },
  ],
};
