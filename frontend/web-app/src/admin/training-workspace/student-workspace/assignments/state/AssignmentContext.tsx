import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { Assignment, Project, Submission, Evaluation, AssignmentTimelineEvent, AssignmentDashboardStats, AssignmentAnalytics, AssignmentStatus, AssignmentType } from '../types';
import {
  getAssignments, getProjects, getSubmissions, getEvaluations,
  getTimelineEvents, getDashboardStats, getAnalytics,
} from '../data/mockData';

interface AssignmentState {
  assignments: Assignment[];
  projects: Project[];
  submissions: Submission[];
  evaluations: Evaluation[];
  timelineEvents: AssignmentTimelineEvent[];
  dashboardStats: AssignmentDashboardStats;
  analytics: AssignmentAnalytics;
  searchTerm: string;
  statusFilter: AssignmentStatus | 'all';
  typeFilter: AssignmentType | 'all';
}

interface AssignmentContextValue extends AssignmentState {
  setSearchTerm: (term: string) => void;
  setStatusFilter: (status: AssignmentStatus | 'all') => void;
  setTypeFilter: (type: AssignmentType | 'all') => void;
  getFilteredAssignments: () => Assignment[];
  getFilteredSubmissions: () => Submission[];
}

const AssignmentContext = createContext<AssignmentContextValue | undefined>(undefined);

export function AssignmentProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<AssignmentState>(() => ({
    assignments: getAssignments(),
    projects: getProjects(),
    submissions: getSubmissions(),
    evaluations: getEvaluations(),
    timelineEvents: getTimelineEvents(),
    dashboardStats: getDashboardStats(),
    analytics: getAnalytics(),
    searchTerm: '',
    statusFilter: 'all',
    typeFilter: 'all',
  }));

  const setSearchTerm = useCallback((searchTerm: string) => {
    setState((prev) => ({ ...prev, searchTerm }));
  }, []);

  const setStatusFilter = useCallback((statusFilter: AssignmentStatus | 'all') => {
    setState((prev) => ({ ...prev, statusFilter }));
  }, []);

  const setTypeFilter = useCallback((typeFilter: AssignmentType | 'all') => {
    setState((prev) => ({ ...prev, typeFilter }));
  }, []);

  const getFilteredAssignments = useCallback(() => {
    let result = state.assignments;
    if (state.statusFilter !== 'all') {
      result = result.filter((a) => a.status === state.statusFilter);
    }
    if (state.typeFilter !== 'all') {
      result = result.filter((a) => a.assignmentType === state.typeFilter);
    }
    if (state.searchTerm) {
      const term = state.searchTerm.toLowerCase();
      result = result.filter(
        (a) => a.title.toLowerCase().includes(term) ||
          a.assignmentCode.toLowerCase().includes(term) ||
          a.courseName.toLowerCase().includes(term) ||
          a.batchName.toLowerCase().includes(term) ||
          a.lessonName.toLowerCase().includes(term),
      );
    }
    return result;
  }, [state.assignments, state.searchTerm, state.statusFilter, state.typeFilter]);

  const getFilteredSubmissions = useCallback(() => {
    let result = state.submissions;
    if (state.searchTerm) {
      const term = state.searchTerm.toLowerCase();
      result = result.filter(
        (s) => s.studentName.toLowerCase().includes(term) ||
          s.assignmentTitle.toLowerCase().includes(term) ||
          s.submissionCode.toLowerCase().includes(term),
      );
    }
    return result;
  }, [state.submissions, state.searchTerm]);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setStatusFilter, setTypeFilter, getFilteredAssignments, getFilteredSubmissions }),
    [state, setSearchTerm, setStatusFilter, setTypeFilter, getFilteredAssignments, getFilteredSubmissions],
  );

  return <AssignmentContext.Provider value={value}>{children}</AssignmentContext.Provider>;
}

export function useAssignments(): AssignmentContextValue {
  const ctx = useContext(AssignmentContext);
  if (!ctx) throw new Error('useAssignments must be used within AssignmentProvider');
  return ctx;
}
