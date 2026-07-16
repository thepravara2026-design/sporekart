import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { Certificate, DigitalBadge, Achievement, CredentialWallet, AcademicTranscript, VerificationRecord, CertificateAnalytics, CertificateDashboard, CertificateStatus, CertificateType } from '../types';
import {
  getCertificates, getBadges, getAchievements, getWallets,
  getTranscripts, getVerificationRecords, getAnalytics, getDashboard,
} from '../data/mockData';

interface CertificateState {
  certificates: Certificate[];
  badges: DigitalBadge[];
  achievements: Achievement[];
  wallets: CredentialWallet[];
  transcripts: AcademicTranscript[];
  verificationRecords: VerificationRecord[];
  analytics: CertificateAnalytics;
  dashboard: CertificateDashboard;
  searchTerm: string;
  statusFilter: CertificateStatus | 'all';
  typeFilter: CertificateType | 'all';
}

interface CertificateContextValue extends CertificateState {
  setSearchTerm: (term: string) => void;
  setStatusFilter: (status: CertificateStatus | 'all') => void;
  setTypeFilter: (type: CertificateType | 'all') => void;
  getFilteredCertificates: () => Certificate[];
}

const Ctx = createContext<CertificateContextValue | undefined>(undefined);

export function CertificateProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<CertificateState>(() => ({
    certificates: getCertificates(),
    badges: getBadges(),
    achievements: getAchievements(),
    wallets: getWallets(),
    transcripts: getTranscripts(),
    verificationRecords: getVerificationRecords(),
    analytics: getAnalytics(),
    dashboard: getDashboard(),
    searchTerm: '',
    statusFilter: 'all',
    typeFilter: 'all',
  }));

  const setSearchTerm = useCallback((searchTerm: string) => {
    setState((prev) => ({ ...prev, searchTerm }));
  }, []);

  const setStatusFilter = useCallback((statusFilter: CertificateStatus | 'all') => {
    setState((prev) => ({ ...prev, statusFilter }));
  }, []);

  const setTypeFilter = useCallback((typeFilter: CertificateType | 'all') => {
    setState((prev) => ({ ...prev, typeFilter }));
  }, []);

  const getFilteredCertificates = useCallback(() => {
    let result = state.certificates;
    if (state.statusFilter !== 'all') {
      result = result.filter((c) => c.status === state.statusFilter);
    }
    if (state.typeFilter !== 'all') {
      result = result.filter((c) => c.certificateType === state.typeFilter);
    }
    if (state.searchTerm) {
      const term = state.searchTerm.toLowerCase();
      result = result.filter(
        (c) => c.studentName.toLowerCase().includes(term) ||
          c.courseName.toLowerCase().includes(term) ||
          c.certificateNumber.toLowerCase().includes(term) ||
          c.title.toLowerCase().includes(term),
      );
    }
    return result;
  }, [state.certificates, state.searchTerm, state.statusFilter, state.typeFilter]);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setStatusFilter, setTypeFilter, getFilteredCertificates }),
    [state, setSearchTerm, setStatusFilter, setTypeFilter, getFilteredCertificates],
  );

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useCertificates(): CertificateContextValue {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error('useCertificates must be used within CertificateProvider');
  return ctx;
}
