import { memo } from 'react';
import type { AcademicTranscript } from '../types';

interface TranscriptSummaryProps {
  transcript: AcademicTranscript;
}

export const TranscriptSummary = memo(function TranscriptSummary({ transcript }: TranscriptSummaryProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
        <div>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{transcript.studentName}</h3>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{transcript.studentEmail}</div>
        </div>
        <span style={{
          padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          background: transcript.status === 'graduated' ? '#16a34a18' : transcript.status === 'active' ? '#2563eb18' : '#6b728018',
          color: transcript.status === 'graduated' ? '#16a34a' : transcript.status === 'active' ? '#2563eb' : '#6b7280',
        }}>
          {transcript.status === 'graduated' ? 'Graduated' : transcript.status.charAt(0).toUpperCase() + transcript.status.slice(1)}
        </span>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))', gap: 8 }}>
        <div style={{ textAlign: 'center', padding: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-subtle)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{transcript.overallPerformance}%</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Performance</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-subtle)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{transcript.gpa}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>GPA</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-subtle)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{transcript.credits}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Credits</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-subtle)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{transcript.completedCourses}/{transcript.totalCourses}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Courses</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-subtle)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{transcript.certificatesCount}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Certificates</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-subtle)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{transcript.totalLearningHours}h</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Hours</div>
        </div>
      </div>
    </div>
  );
});
