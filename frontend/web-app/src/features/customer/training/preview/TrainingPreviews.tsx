import React from 'react';
import { PreviewScaffold } from '../../preview/PreviewScaffold';

export const TrainingPreview: React.FC = () => {
  const a11y = [
    'Quick action widgets have explicit focus outlines and support tab indices.',
    'Metrics indicators use readable values and are marked with descriptive text labels.',
    'Video progress bars have standard controls for keyboard focus.'
  ];
  const resp = [
    'Metrics block scales down to 2x2 grid on tablet viewports.',
    'Webinar calendars and AI assistant columns stack vertically below the active course panel.'
  ];
  return (
    <PreviewScaffold
      title="Training Center Dashboard"
      subtitle="Interactive client panel listing enrolled progress, webinars calendar, and quick study routes."
      route="/dashboard/training"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const CoursePreview: React.FC = () => {
  const a11y = [
    'Category filter selects are linked to form labels.',
    'Catalog grids implement keyboard navigation for focus items.',
    'Enroll status tags are announced as readable texts.'
  ];
  const resp = [
    'Syllabus headers shrink gracefully on smaller mobile viewports.',
    'Outcomes list collapses to single line points.'
  ];
  return (
    <PreviewScaffold
      title="Course Catalog Library"
      subtitle="Comprehensive study catalog listing intermediate and advanced spawn growing courses."
      route="/dashboard/training/courses"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const ClassroomPreview: React.FC = () => {
  const a11y = [
    'Video player custom controls (play, pause, elapsed slider) are keyboard accessible.',
    'Transcript tab content uses large, high-contrast readable prose.',
    'Notes text area has associated focus rings and descriptive labels.'
  ];
  const resp = [
    'Split pane workspace folds into single vertical layout below 1024px.',
    'Curriculum sidebar checklist stays accessible at bottom of active video player.'
  ];
  return (
    <PreviewScaffold
      title="Interactive Video Classroom"
      subtitle="Split screen video lecture interface with notes log tabs and syllabus node selector."
      route="/dashboard/training/classroom/course-sterile-techniques"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};

export const MobileTrainingPreview: React.FC = () => {
  const a11y = [
    'Touch area targets are expanded to at least 48x48px on mobile devices.',
    'Skip links are active under focus states.'
  ];
  const resp = [
    'Course detail banner folds to single vertical column.',
    'Video player keeps aspect ratio (16:9) on portrait screens.'
  ];
  return (
    <PreviewScaffold
      title="Mobile View — Training Center"
      subtitle="Training center mobile view under 390px viewport."
      route="/dashboard/training"
      accessibilityNotes={a11y}
      responsiveNotes={resp}
      approvalStatus="Approved"
    />
  );
};
export default TrainingPreview;
