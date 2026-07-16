import { memo } from 'react';
import PlaceholderPage from './PlaceholderPage';

interface NavItemConfig {
  id: string;
  title: string;
  icon: string;
  description: string;
  future?: boolean;
}

const PLACEHOLDER_CONFIGS: NavItemConfig[] = [
  { id: 'courses', title: 'Courses', icon: 'book-open', description: 'Manage training courses — create, edit, publish, and organize course content across all training domains including Mushroom Training, Spawn Production, Commercial Cultivation, Farmer Training, and Corporate Training.' },
  { id: 'curriculum', title: 'Curriculum', icon: 'layers', description: 'Design and manage curriculum structures — define modules, lessons, learning objectives, prerequisites, and certification pathways.' },
  { id: 'batches', title: 'Training Batches', icon: 'calendar', description: 'Schedule and manage training batches — configure batch size, trainer assignment, venue, mode, and enrollment periods.' },
  { id: 'students', title: 'Students', icon: 'users', description: 'Manage student enrollments, profiles, progress tracking, and communication. Register new students and view learning histories.' },
  { id: 'trainers', title: 'Trainers', icon: 'user-check', description: 'Onboard and manage trainers — assign qualifications, track certifications, manage schedules, and evaluate performance.' },
  { id: 'attendance', title: 'Attendance', icon: 'check-circle', description: 'Track session attendance — mark attendance, view reports, manage absences, and generate attendance certificates.' },
  { id: 'assignments', title: 'Assignments', icon: 'file', description: 'Create and manage assignments — set deadlines, evaluate submissions, provide feedback, and track completion rates.' },
  { id: 'assessments', title: 'Assessments', icon: 'target', description: 'Design assessments and evaluations — create quizzes, exams, practical evaluations, and automated grading workflows.' },
  { id: 'certificates', title: 'Certificates', icon: 'award', description: 'Issue and manage certificates — configure templates, automate issuance, verify authenticity, and track certification records.' },
  { id: 'resources', title: 'Learning Resources', icon: 'folder', description: 'Manage learning materials — upload, organize, and distribute resources including documents, videos, presentations, and reference guides.' },
  { id: 'announcements', title: 'Announcements', icon: 'message-circle', description: 'Post and manage platform announcements — communicate with students, trainers, and stakeholders about updates and events.' },
  { id: 'reports', title: 'Reports', icon: 'bar-chart', description: 'Generate and view training reports — analyze enrollment data, completion rates, assessment scores, and trainer performance.' },
  { id: 'analytics', title: 'Analytics', icon: 'trending-up', description: 'Training analytics and insights — track KPIs, visualize trends, monitor engagement, and measure learning outcomes.' },
  { id: 'settings', title: 'Settings', icon: 'settings', description: 'Configure training workspace settings — manage preferences, notification settings, integrations, and platform configuration.' },
  { id: 'ai-assistant', title: 'AI Assistant', icon: 'sparkles', description: 'AI-powered training assistant coming soon — intelligent recommendations, automated assessments, and personalized learning paths.', future: true },
  { id: 'community', title: 'Community', icon: 'message-square', description: 'Training community coming soon — connect with peers, share knowledge, and collaborate on learning initiatives.', future: true },
  { id: 'discussions', title: 'Discussion Board', icon: 'message-circle', description: 'Discussion forums coming soon — engage in topic-based discussions, ask questions, and share insights.', future: true },
];

function createPlaceholderPage(config: NavItemConfig) {
  const Component = memo(function PlaceholderPageWrapper() {
    return (
      <PlaceholderPage
        title={config.title}
        description={config.description}
        icon={config.icon}
        future={config.future}
      />
    );
  });
  Component.displayName = `${config.title}Page`;
  return Component;
}

export const CoursesPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[0]);
export const CurriculumPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[1]);
export const BatchesPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[2]);
export const StudentsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[3]);
export const TrainersPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[4]);
export const AttendancePage = createPlaceholderPage(PLACEHOLDER_CONFIGS[5]);
export const AssignmentsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[6]);
export const AssessmentsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[7]);
export const CertificatesPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[8]);
export const ResourcesPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[9]);
export const AnnouncementsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[10]);
export const ReportsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[11]);
export const AnalyticsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[12]);
export const SettingsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[13]);
export const AiAssistantPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[14]);
export const CommunityPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[15]);
export const DiscussionsPage = createPlaceholderPage(PLACEHOLDER_CONFIGS[16]);
