import { memo } from 'react';
import type { AcademicTranscript } from '../types';

interface TranscriptTableProps {
  transcript: AcademicTranscript;
}

export const TranscriptTable = memo(function TranscriptTable({ transcript }: TranscriptTableProps) {
  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)', textAlign: 'left' }}>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Course</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Attendance</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Assignments</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Assessment</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Hours</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Grade</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {transcript.courseRecords.map((rec) => (
            <tr key={rec.courseId} style={{ borderBottom: '1px solid var(--color-border-subtle)' }}>
              <td style={{ padding: '10px 12px', fontWeight: 'var(--weight-medium)' }}>{rec.courseName}</td>
              <td style={{ padding: '10px 12px' }}>
                <span style={{ color: rec.attendancePercent >= 80 ? '#16a34a' : rec.attendancePercent >= 60 ? '#ca8a04' : '#dc2626' }}>{rec.attendancePercent}%</span>
              </td>
              <td style={{ padding: '10px 12px' }}>{rec.assignmentScore}%</td>
              <td style={{ padding: '10px 12px' }}>{rec.assessmentScore}%</td>
              <td style={{ padding: '10px 12px' }}>{rec.learningHours}h</td>
              <td style={{ padding: '10px 12px', fontWeight: 'var(--weight-bold)' }}>{rec.grade}</td>
              <td style={{ padding: '10px 12px' }}>
                <span style={{
                  padding: '2px 6px', borderRadius: 'var(--radius-xs)',
                  fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
                  color: rec.status === 'completed' || rec.status === 'certified' ? '#16a34a' : rec.status === 'in-progress' ? '#2563eb' : '#dc2626',
                  background: (rec.status === 'completed' || rec.status === 'certified' ? '#16a34a' : rec.status === 'in-progress' ? '#2563eb' : '#dc2626') + '18',
                }}>
                  {rec.status === 'certified' ? 'Certified' : rec.status.charAt(0).toUpperCase() + rec.status.slice(1)}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
