import { useState, useMemo } from 'react';
import { useCertificates } from '../state/CertificateContext';
import { VerificationCard } from '../components/VerificationCard';
import { CertificateTimeline } from '../components/CertificateTimeline';
import { CredentialSharePanel } from '../components/CredentialSharePanel';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { VERIFICATION_STATUS_LABELS } from '../types';
import type { VerificationStatus } from '../types';

export function VerificationCenterPage() {
  const { verificationRecords, certificates } = useCertificates();
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<VerificationStatus | 'all'>('all');
  const [selectedCertId, setSelectedCertId] = useState<string | null>(null);
  const [page, setPage] = useState(1);
  const perPage = 10;

  const selectedCertificate = useMemo(() => certificates.find((c) => c.id === selectedCertId), [certificates, selectedCertId]);

  const filtered = useMemo(() => {
    let result = verificationRecords;
    if (statusFilter !== 'all') result = result.filter((r) => r.status === statusFilter);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((r) => r.studentName.toLowerCase().includes(term) || r.certificateNumber.toLowerCase().includes(term) || r.courseName.toLowerCase().includes(term));
    }
    return result;
  }, [verificationRecords, searchTerm, statusFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => { setSearchTerm(''); setStatusFilter('all'); setPage(1); };

  const verifiedCount = verificationRecords.filter((r) => r.status === 'verified').length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Verification Center</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Requests" value={verificationRecords.length} variant="default" subtitle="All time" />
        <DashboardWidget label="Verified" value={verifiedCount} variant="success" subtitle={`${Math.round((verifiedCount / Math.max(verificationRecords.length, 1)) * 100)}% rate`} />
        <DashboardWidget label="Pending" value={verificationRecords.filter((r) => r.status === 'pending').length} variant="warning" subtitle="Awaiting verification" />
        <DashboardWidget label="Failed" value={verificationRecords.filter((r) => r.status === 'failed').length} variant={verificationRecords.filter((r) => r.status === 'failed').length > 0 ? 'danger' : 'default'} subtitle="Failed checks" />
      </div>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select aria-label="Filter by verification status" value={statusFilter} onChange={(e) => { setStatusFilter(e.target.value as VerificationStatus | 'all'); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Statuses</option>
          {Object.entries(VERIFICATION_STATUS_LABELS).map(([k, v]) => (<option key={k} value={k}>{v}</option>))}
        </select>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: 'var(--space-component-gap)' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          {filtered.length === 0 ? (
            <EmptyState type={searchTerm || statusFilter !== 'all' ? 'noSearchResults' : 'noCertificates'} onClearFilters={handleClearFilters} />
          ) : (
            <>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Showing {paginated.length} of {filtered.length} verification records</div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {paginated.map((r) => (
                  <div key={r.id} onClick={() => setSelectedCertId(r.certificateId)} style={{ cursor: 'pointer' }}>
                    <VerificationCard record={r} />
                  </div>
                ))}
              </div>
              {totalPages > 1 && (
                <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={perPage} onPageChange={setPage} onPageSizeChange={() => {}} pageSizeOptions={[10, 20, 50]} />
              )}
            </>
          )}
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          {selectedCertificate && (
            <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
              <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Certificate Lifecycle</h3>
              <CertificateTimeline certificate={selectedCertificate} />
            </div>
          )}
          <CredentialSharePanel />
        </div>
      </div>
    </div>
  );
}
