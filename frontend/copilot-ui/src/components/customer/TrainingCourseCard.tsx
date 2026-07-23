import React from 'react';
import { TrainingCourse } from '../../types/customer';

interface TrainingCourseCardProps {
  course: TrainingCourse;
  onEnroll?: (course: TrainingCourse) => void;
  onViewDetails?: (course: TrainingCourse) => void;
}

const levelColors: Record<string, { color: string; bg: string }> = {
  BEGINNER: { color: '#166534', bg: '#dcfce7' },
  INTERMEDIATE: { color: '#92400e', bg: '#fef3c7' },
  ADVANCED: { color: '#991b1b', bg: '#fee2e2' },
};

export default function TrainingCourseCard({ course, onEnroll, onViewDetails }: TrainingCourseCardProps) {
  const lvl = levelColors[course.level.toUpperCase()] || levelColors.BEGINNER;

  return (
    <div style={{
      border: '1px solid #e5e7eb',
      borderRadius: '8px',
      padding: '14px',
      background: '#fff',
      fontFamily: 'system-ui, sans-serif',
      maxWidth: '300px',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '8px' }}>
        <span style={{
          background: '#dbeafe',
          color: '#1d4ed8',
          fontSize: '11px',
          padding: '2px 8px',
          borderRadius: '4px',
          fontWeight: 600,
        }}>
          {course.category}
        </span>
        <span style={{
          background: lvl.bg,
          color: lvl.color,
          fontSize: '11px',
          padding: '2px 8px',
          borderRadius: '4px',
          fontWeight: 600,
        }}>
          {course.level}
        </span>
      </div>

      <h3 style={{ margin: '0 0 4px', fontSize: '15px', fontWeight: 600, color: '#111827' }}>
        {course.title}
      </h3>

      <p style={{ margin: '0 0 10px', fontSize: '13px', color: '#6b7280', lineHeight: '1.4' }}>
        {course.description}
      </p>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', marginBottom: '10px', fontSize: '13px', color: '#374151' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <span style={{ color: '#6b7280' }}>Duration</span>
          <span style={{ fontWeight: 500 }}>{course.duration}</span>
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <span style={{ color: '#6b7280' }}>Instructor</span>
          <span style={{ fontWeight: 500 }}>{course.instructor}</span>
        </div>
        {course.nextBatchDate && (
          <div style={{ display: 'flex', justifyContent: 'space-between' }}>
            <span style={{ color: '#6b7280' }}>Next Batch</span>
            <span style={{ fontWeight: 500 }}>
              {new Date(course.nextBatchDate).toLocaleDateString()}
            </span>
          </div>
        )}
      </div>

      <div style={{ display: 'flex', gap: '8px' }}>
        {onEnroll && (
          <button
            onClick={() => onEnroll(course)}
            disabled={course.enrollmentStatus !== 'OPEN'}
            style={{
              flex: 1,
              padding: '8px 12px',
              border: 'none',
              borderRadius: '6px',
              background: course.enrollmentStatus === 'OPEN' ? '#059669' : '#d1d5db',
              color: '#fff',
              fontWeight: 600,
              fontSize: '13px',
              cursor: course.enrollmentStatus === 'OPEN' ? 'pointer' : 'not-allowed',
            }}
          >
            {course.enrollmentStatus === 'OPEN' ? 'Enroll Now' : course.enrollmentStatus}
          </button>
        )}
        {onViewDetails && (
          <button
            onClick={() => onViewDetails(course)}
            style={{
              padding: '8px 12px',
              border: '1px solid #d1d5db',
              borderRadius: '6px',
              background: '#fff',
              color: '#374151',
              fontWeight: 500,
              fontSize: '13px',
              cursor: 'pointer',
            }}
          >
            View Details
          </button>
        )}
      </div>
    </div>
  );
}
