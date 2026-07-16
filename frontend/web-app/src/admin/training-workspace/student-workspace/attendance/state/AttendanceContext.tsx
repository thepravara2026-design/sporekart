import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { AttendanceRecord, AttendanceSession, AttendanceSummary, AttendancePolicy, AttendanceDashboardStats, AttendanceStatus } from '../types';
import {
  getAttendanceRecords, getAttendanceSessions, getAttendanceSummary,
  getAttendancePolicies, getDashboardStats,
} from '../data/mockData';

interface AttendanceState {
  records: AttendanceRecord[];
  sessions: AttendanceSession[];
  summary: AttendanceSummary;
  policies: AttendancePolicy[];
  dashboardStats: AttendanceDashboardStats;
  searchTerm: string;
  statusFilter: AttendanceStatus | 'all';
}

interface AttendanceContextValue extends AttendanceState {
  setSearchTerm: (term: string) => void;
  setStatusFilter: (status: AttendanceStatus | 'all') => void;
  getFilteredRecords: () => AttendanceRecord[];
}

const AttendanceContext = createContext<AttendanceContextValue | undefined>(undefined);

export function AttendanceProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<AttendanceState>(() => ({
    records: getAttendanceRecords(),
    sessions: getAttendanceSessions(),
    summary: getAttendanceSummary(),
    policies: getAttendancePolicies(),
    dashboardStats: getDashboardStats(),
    searchTerm: '',
    statusFilter: 'all',
  }));

  const setSearchTerm = useCallback((searchTerm: string) => {
    setState((prev) => ({ ...prev, searchTerm }));
  }, []);

  const setStatusFilter = useCallback((statusFilter: AttendanceStatus | 'all') => {
    setState((prev) => ({ ...prev, statusFilter }));
  }, []);

  const getFilteredRecords = useCallback(() => {
    let result = state.records;
    if (state.statusFilter !== 'all') {
      result = result.filter((r) => r.attendanceStatus === state.statusFilter);
    }
    if (state.searchTerm) {
      const term = state.searchTerm.toLowerCase();
      result = result.filter(
        (r) => r.studentName.toLowerCase().includes(term) ||
          r.attendanceId.toLowerCase().includes(term) ||
          r.courseName.toLowerCase().includes(term) ||
          r.batchName.toLowerCase().includes(term),
      );
    }
    return result;
  }, [state.records, state.searchTerm, state.statusFilter]);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setStatusFilter, getFilteredRecords }),
    [state, setSearchTerm, setStatusFilter, getFilteredRecords],
  );

  return <AttendanceContext.Provider value={value}>{children}</AttendanceContext.Provider>;
}

export function useAttendance(): AttendanceContextValue {
  const ctx = useContext(AttendanceContext);
  if (!ctx) throw new Error('useAttendance must be used within AttendanceProvider');
  return ctx;
}
