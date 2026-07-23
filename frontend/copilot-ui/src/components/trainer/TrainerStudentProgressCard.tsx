import type { StudentProgress } from './types/trainer';

export function TrainerStudentProgressCard({ student }: { student: StudentProgress }) {
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: 16, background: 'var(--cp-color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
        <h3 style={{ margin: 0, fontSize: 15, color: 'var(--cp-color-text)' }}>{student.studentName}</h3>
        <span style={{ fontSize: 12, color: 'var(--cp-color-text-muted)' }}>ID: {student.studentId}</span>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, marginBottom: 12 }}>
        <Metric label="Attendance" value={`${student.attendancePercent}%`} color={student.attendancePercent >= 75 ? 'var(--cp-color-success)' : student.attendancePercent >= 50 ? 'var(--cp-color-warning)' : 'var(--cp-color-danger)'} />
        <Metric label="Overall Score" value={`${student.overallScore}%`} color={student.overallScore >= 70 ? 'var(--cp-color-success)' : student.overallScore >= 40 ? 'var(--cp-color-warning)' : 'var(--cp-color-danger)'} />
        <Metric label="Assignments" value={`${student.completedAssignments}/${student.totalAssignments}`} />
        <Metric label="Practicals" value={`${student.completedPracticals}/${student.totalPracticals}`} />
      </div>
      {student.weakTopics.length > 0 && (
        <div style={{ marginBottom: 8 }}>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-danger)', marginBottom: 4 }}>Weak Topics</div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
            {student.weakTopics.map((t, i) => <Tag key={i} color="var(--cp-color-danger)">{t}</Tag>)}
          </div>
        </div>
      )}
      {student.strongTopics.length > 0 && (
        <div>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-success)', marginBottom: 4 }}>Strong Topics</div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
            {student.strongTopics.map((t, i) => <Tag key={i} color="var(--cp-color-success)">{t}</Tag>)}
          </div>
        </div>
      )}
    </div>
  );
}

function Metric({ label, value, color }: { label: string; value: string; color?: string }) {
  return (
    <div style={{ fontSize: 12 }}>
      <span style={{ color: 'var(--cp-color-text-muted)' }}>{label}</span>
      <div style={{ fontWeight: 600, fontSize: 14, color: color || 'var(--cp-color-text)' }}>{value}</div>
    </div>
  );
}

function Tag({ color, children }: { color: string; children: string }) {
  return (
    <span style={{ background: `${color}18`, color, padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 500 }}>
      {children}
    </span>
  );
}
