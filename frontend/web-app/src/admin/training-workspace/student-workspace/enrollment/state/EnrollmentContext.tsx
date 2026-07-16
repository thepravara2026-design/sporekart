import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { EnrollmentRequest, Batch, CapacityInfo, EnrollmentDashboardStats, EnrollmentStatus, ApprovalAction } from '../types';
import {
  getEnrollmentRequests, getBatches, getCapacityInfo, getDashboardStats,
  getApprovalActions, getPendingApprovals, getArchivedEnrollments,
} from '../data/mockData';

interface EnrollmentState {
  requests: EnrollmentRequest[];
  batches: Batch[];
  capacityInfo: CapacityInfo[];
  dashboardStats: EnrollmentDashboardStats;
  approvalActions: ApprovalAction[];
  pendingApprovals: EnrollmentRequest[];
  archivedEnrollments: EnrollmentRequest[];
  currentRequest: EnrollmentRequest | null;
  isLoading: boolean;
  searchTerm: string;
  statusFilter: EnrollmentStatus | 'all';
}

interface EnrollmentContextValue extends EnrollmentState {
  setSearchTerm: (term: string) => void;
  setStatusFilter: (status: EnrollmentStatus | 'all') => void;
  selectRequest: (id: string) => void;
  clearSelection: () => void;
}

const initialState: EnrollmentState = {
  requests: [],
  batches: [],
  capacityInfo: [],
  dashboardStats: { totalApplications: 0, pendingApproval: 0, approved: 0, rejected: 0, reservedSeats: 0, availableSeats: 0, activeBatches: 0, capacityUtilization: 0, studentsAssigned: 0, enrolledStudents: 0 },
  approvalActions: [],
  pendingApprovals: [],
  archivedEnrollments: [],
  currentRequest: null,
  isLoading: false,
  searchTerm: '',
  statusFilter: 'all',
};

const EnrollmentContext = createContext<EnrollmentContextValue | undefined>(undefined);

export function EnrollmentProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<EnrollmentState>(() => ({
    ...initialState,
    requests: getEnrollmentRequests(),
    batches: getBatches(),
    capacityInfo: getCapacityInfo(),
    dashboardStats: getDashboardStats(),
    approvalActions: getApprovalActions(),
    pendingApprovals: getPendingApprovals(),
    archivedEnrollments: getArchivedEnrollments(),
  }));

  const setSearchTerm = useCallback((searchTerm: string) => {
    setState((prev) => ({ ...prev, searchTerm }));
  }, []);

  const setStatusFilter = useCallback((statusFilter: EnrollmentStatus | 'all') => {
    setState((prev) => ({ ...prev, statusFilter }));
  }, []);

  const selectRequest = useCallback((id: string) => {
    const req = state.requests.find((r) => r.id === id || r.enrollmentId === id) ?? null;
    setState((prev) => ({ ...prev, currentRequest: req }));
  }, [state.requests]);

  const clearSelection = useCallback(() => {
    setState((prev) => ({ ...prev, currentRequest: null }));
  }, []);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setStatusFilter, selectRequest, clearSelection }),
    [state, setSearchTerm, setStatusFilter, selectRequest, clearSelection],
  );

  return <EnrollmentContext.Provider value={value}>{children}</EnrollmentContext.Provider>;
}

export function useEnrollment(): EnrollmentContextValue {
  const ctx = useContext(EnrollmentContext);
  if (!ctx) throw new Error('useEnrollment must be used within EnrollmentProvider');
  return ctx;
}
