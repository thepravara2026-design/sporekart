import { useState, useMemo } from 'react';
import { useCertificates } from '../state/CertificateContext';
import { WalletSummary } from '../components/WalletSummary';
import { CertificateCard } from '../components/CertificateCard';
import { BadgeCard } from '../components/BadgeCard';
import { AchievementCard } from '../components/AchievementCard';
import { EmptyState } from '../components/EmptyStates';
import { WalletSkeleton } from '../components/Skeletons';
import { StudentSearchBar } from '../../components/StudentSearchFilter';

type Tab = 'certificates' | 'badges' | 'achievements';

export function CredentialWalletPage() {
  const { wallets } = useCertificates();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedStudent, setSelectedStudent] = useState<string>(wallets[0]?.studentId || '');
  const [activeTab, setActiveTab] = useState<Tab>('certificates');

  const currentWallet = useMemo(() => wallets.find((w) => w.studentId === selectedStudent), [wallets, selectedStudent]);

  const filteredStudents = useMemo(() => {
    if (!searchTerm) return wallets;
    const term = searchTerm.toLowerCase();
    return wallets.filter((w) => w.studentName.toLowerCase().includes(term) || w.studentId.toLowerCase().includes(term));
  }, [wallets, searchTerm]);

  if (!wallets.length) return <WalletSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Credential Wallet</h2>

      <div style={{ display: 'grid', gridTemplateColumns: '300px 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={setSearchTerm} />
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4, maxHeight: 400, overflowY: 'auto' }}>
            {filteredStudents.map((w) => (
              <button
                key={w.studentId}
                onClick={() => setSelectedStudent(w.studentId)}
                style={{
                  padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
                  textAlign: 'left', background: selectedStudent === w.studentId ? 'var(--color-bg-primary-subtle)' : 'transparent',
                  color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)',
                }}
              >
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{w.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{w.totalCredentials} credentials</div>
              </button>
            ))}
          </div>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          {currentWallet ? (
            <>
              <WalletSummary wallet={currentWallet} />

              <div style={{ display: 'flex', gap: 4, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', overflow: 'hidden', alignSelf: 'flex-start' }} role="tablist" aria-label="Wallet sections">
                {(['certificates', 'badges', 'achievements'] as Tab[]).map((tab) => (
                  <button key={tab} onClick={() => setActiveTab(tab)} role="tab" aria-selected={activeTab === tab}
                    style={{ padding: '6px 14px', border: 'none', cursor: 'pointer', background: activeTab === tab ? 'var(--color-bg-primary-subtle)' : 'transparent', color: activeTab === tab ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', textTransform: 'capitalize' }}>
                    {tab}
                  </button>
                ))}
              </div>

              {activeTab === 'certificates' && (
                currentWallet.certificates.length === 0 ? <EmptyState type="noCertificates" /> : (
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
                    {currentWallet.certificates.map((c) => (<CertificateCard key={c.id} certificate={c} />))}
                  </div>
                )
              )}
              {activeTab === 'badges' && (
                currentWallet.badges.length === 0 ? <EmptyState type="noBadges" /> : (
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
                    {currentWallet.badges.map((b) => (<BadgeCard key={b.id} badge={b} />))}
                  </div>
                )
              )}
              {activeTab === 'achievements' && (
                currentWallet.achievements.length === 0 ? <EmptyState type="noAchievements" /> : (
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
                    {currentWallet.achievements.map((a) => (<AchievementCard key={a.id} achievement={a} />))}
                  </div>
                )
              )}
            </>
          ) : (
            <EmptyState type="noWallet" />
          )}
        </div>
      </div>
    </div>
  );
}
