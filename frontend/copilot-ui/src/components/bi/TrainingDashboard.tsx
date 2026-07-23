import React from 'react';
import type { TrainingAnalytics } from './types/bi';

interface TrainingDashboardProps { data: TrainingAnalytics; }

function KpiCard({ label, value, format = 'number' }: { label: string; value: number; format?: string }) {
  const formatted = format === 'percent' ? `${value.toFixed(1)}%` : value.toLocaleString();
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 4 }}>
      <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>{label}</span>
      <span style={{ fontSize: 24, fontWeight: 700, color: '#111827' }}>{formatted}</span>
    </div>
  );
}

export default function TrainingDashboard({ data }: TrainingDashboardProps) {
  const { totalBatches, totalStudents, averageAttendance, averageScore, completionRate, certificationsIssued, trainerPerformance } = data;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
        <KpiCard label="Total Students" value={totalStudents} />
        <KpiCard label="Total Batches" value={totalBatches} />
        <KpiCard label="Avg Attendance" value={averageAttendance} format="percent" />
        <KpiCard label="Avg Score" value={averageScore} format="percent" />
        <KpiCard label="Completion Rate" value={completionRate} format="percent" />
        <KpiCard label="Certifications" value={certificationsIssued} />
      </div>
      <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
        <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Trainer Performance</h3>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
          <thead><tr style={{ borderBottom: '1px solid #e5e7eb' }}>
            <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Trainer</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Batches</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Students</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Avg Score</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Completion</th>
          </tr></thead>
          <tbody>
            {trainerPerformance.map(t => (
              <tr key={t.trainerId} style={{ borderBottom: '1px solid #f3f4f6' }}>
                <td style={{ padding: '8px 12px', color: '#374151', fontWeight: 500 }}>{t.name}</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: '#6b7280' }}>{t.batches}</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: '#6b7280' }}>{t.students}</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: t.avgScore >= 80 ? '#22c55e' : t.avgScore >= 60 ? '#f97316' : '#ef4444', fontWeight: 600 }}>{t.avgScore.toFixed(1)}%</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: t.completionRate >= 80 ? '#22c55e' : t.completionRate >= 60 ? '#f97316' : '#ef4444', fontWeight: 600 }}>{t.completionRate.toFixed(1)}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
