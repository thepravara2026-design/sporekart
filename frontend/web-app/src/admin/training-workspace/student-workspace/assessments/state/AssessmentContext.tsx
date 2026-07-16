import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { Assessment, QuestionCategory, Result, AssessmentTimelineEvent, AssessmentDashboardStats, AcademicAnalytics, AssessmentStatus, AssessmentType } from '../types';
import {
  getAssessments, getQuestionCategories, getResults, getTimelineEvents,
  getDashboardStats, getAnalytics,
} from '../data/mockData';

interface AssessmentState {
  assessments: Assessment[];
  questionCategories: QuestionCategory[];
  results: Result[];
  timelineEvents: AssessmentTimelineEvent[];
  dashboardStats: AssessmentDashboardStats;
  analytics: AcademicAnalytics;
  searchTerm: string;
  statusFilter: AssessmentStatus | 'all';
  typeFilter: AssessmentType | 'all';
}

interface AssessmentContextValue extends AssessmentState {
  setSearchTerm: (term: string) => void;
  setStatusFilter: (status: AssessmentStatus | 'all') => void;
  setTypeFilter: (type: AssessmentType | 'all') => void;
  getFilteredAssessments: () => Assessment[];
}

const AssessmentContext = createContext<AssessmentContextValue | undefined>(undefined);

export function AssessmentProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<AssessmentState>(() => ({
    assessments: getAssessments(),
    questionCategories: getQuestionCategories(),
    results: getResults(),
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

  const setStatusFilter = useCallback((statusFilter: AssessmentStatus | 'all') => {
    setState((prev) => ({ ...prev, statusFilter }));
  }, []);

  const setTypeFilter = useCallback((typeFilter: AssessmentType | 'all') => {
    setState((prev) => ({ ...prev, typeFilter }));
  }, []);

  const getFilteredAssessments = useCallback(() => {
    let result = state.assessments;
    if (state.statusFilter !== 'all') {
      result = result.filter((a) => a.status === state.statusFilter);
    }
    if (state.typeFilter !== 'all') {
      result = result.filter((a) => a.assessmentType === state.typeFilter);
    }
    if (state.searchTerm) {
      const term = state.searchTerm.toLowerCase();
      result = result.filter(
        (a) => a.title.toLowerCase().includes(term) ||
          a.assessmentCode.toLowerCase().includes(term) ||
          a.courseName.toLowerCase().includes(term) ||
          a.batchName.toLowerCase().includes(term),
      );
    }
    return result;
  }, [state.assessments, state.searchTerm, state.statusFilter, state.typeFilter]);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setStatusFilter, setTypeFilter, getFilteredAssessments }),
    [state, setSearchTerm, setStatusFilter, setTypeFilter, getFilteredAssessments],
  );

  return <AssessmentContext.Provider value={value}>{children}</AssessmentContext.Provider>;
}

export function useAssessments(): AssessmentContextValue {
  const ctx = useContext(AssessmentContext);
  if (!ctx) throw new Error('useAssessments must be used within AssessmentProvider');
  return ctx;
}
