import { useState, useMemo } from 'react';
import { useCertificates } from '../state/CertificateContext';
import { TranscriptSummary } from '../components/TranscriptSummary';
import { TranscriptTable } from '../components/TranscriptTable';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';

export function AcademicTranscriptPage() {
  const { transcripts } = useCertificates();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedId, setSelectedId] = useState<string>(transcripts[0]?.id || '');

  const filtered = useMemo(() => {
    if (!searchTerm) return transcripts;
    const term = searchTerm.toLowerCase();
    return transcripts.filter((t) => t.studentName.toLowerCase().includes(term) || t.studentId.toLowerCase().includes(term));
  }, [transcripts, searchTerm]);

  const current = useMemo(() => transcripts.find((t) => t.id === selectedId), [transcripts, selectedId]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Academic Transcript</h2>

      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={setSearchTerm} />
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4, maxHeight: 400, overflowY: 'auto' }}>
            {filtered.map((t) => (
              <button key={t.id} onClick={() => setSelectedId(t.id)}
                style={{ padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer', textAlign: 'left', background: selectedId === t.id ? 'var(--color-bg-primary-subtle)' : 'transparent', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{t.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{t.completedCourses}/{t.totalCourses} courses</div>
              </button>
            ))}
          </div>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          {current ? (
            <>
              <TranscriptSummary transcript={current} />
              <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
                <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Course Records</h3>
                <TranscriptTable transcript={current} />
              </div>
            </>
          ) : (
            <EmptyState type="noTranscript" />
          )}
        </div>
      </div>
    </div>
  );
}
