import { memo } from 'react';
import type { CredentialWallet } from '../types';

interface WalletSummaryProps {
  wallet: CredentialWallet;
}

export const WalletSummary = memo(function WalletSummary({ wallet }: WalletSummaryProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 12,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{wallet.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>ID: {wallet.studentId}</div>
        </div>
        <div style={{
          width: 44, height: 44, borderRadius: '50%',
          background: 'var(--color-bg-primary-subtle)',
          display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 22,
        }}>💼</div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 8 }}>
        <div style={{ textAlign: 'center', padding: '8px', background: '#f0fdf4', borderRadius: 'var(--radius-sm)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{wallet.certificates.length}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Certificates</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: '#eff6ff', borderRadius: 'var(--radius-sm)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{wallet.badges.length}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Badges</div>
        </div>
        <div style={{ textAlign: 'center', padding: '8px', background: '#fefce8', borderRadius: 'var(--radius-sm)' }}>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{wallet.achievements.length}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Achievements</div>
        </div>
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{wallet.activeCredentials} active</span>
        <span>{wallet.sharedCredentials} shared</span>
        <span>Total: {wallet.totalCredentials}</span>
      </div>
    </div>
  );
});
