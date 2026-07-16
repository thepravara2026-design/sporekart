import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { LearningProgress, Competency, SkillMatrix, Milestone, LearningTimelineEvent, CertificationReadiness, LearningHealthDashboard, LearningAnalytics, ProgressStatus } from '../types';
import {
  getProgress, getCompetencies, getSkillMatrices, getMilestones,
  getTimelineEvents, getCertificationReadiness, getHealthDashboard, getAnalytics,
} from '../data/mockData';

interface ProgressState {
  progressRecords: LearningProgress[];
  competencies: Competency[];
  skillMatrices: SkillMatrix[];
  milestones: Milestone[];
  timelineEvents: LearningTimelineEvent[];
  certificationReadiness: CertificationReadiness[];
  healthDashboard: LearningHealthDashboard;
  analytics: LearningAnalytics;
  searchTerm: string;
  statusFilter: ProgressStatus | 'all';
}

interface ProgressContextValue extends ProgressState {
  setSearchTerm: (term: string) => void;
  setStatusFilter: (status: ProgressStatus | 'all') => void;
  getFilteredProgress: () => LearningProgress[];
}

const ProgressContext = createContext<ProgressContextValue | undefined>(undefined);

export function LearningProgressProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<ProgressState>(() => ({
    progressRecords: getProgress(),
    competencies: getCompetencies(),
    skillMatrices: getSkillMatrices(),
    milestones: getMilestones(),
    timelineEvents: getTimelineEvents(),
    certificationReadiness: getCertificationReadiness(),
    healthDashboard: getHealthDashboard(),
    analytics: getAnalytics(),
    searchTerm: '',
    statusFilter: 'all',
  }));

  const setSearchTerm = useCallback((searchTerm: string) => {
    setState((prev) => ({ ...prev, searchTerm }));
  }, []);

  const setStatusFilter = useCallback((statusFilter: ProgressStatus | 'all') => {
    setState((prev) => ({ ...prev, statusFilter }));
  }, []);

  const getFilteredProgress = useCallback(() => {
    let result = state.progressRecords;
    if (state.statusFilter !== 'all') {
      result = result.filter((p) => p.status === state.statusFilter);
    }
    if (state.searchTerm) {
      const term = state.searchTerm.toLowerCase();
      result = result.filter(
        (p) => p.studentName.toLowerCase().includes(term) ||
          p.courseName.toLowerCase().includes(term) ||
          p.progressCode.toLowerCase().includes(term),
      );
    }
    return result;
  }, [state.progressRecords, state.searchTerm, state.statusFilter]);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setStatusFilter, getFilteredProgress }),
    [state, setSearchTerm, setStatusFilter, getFilteredProgress],
  );

  return <ProgressContext.Provider value={value}>{children}</ProgressContext.Provider>;
}

export function useProgress(): ProgressContextValue {
  const ctx = useContext(ProgressContext);
  if (!ctx) throw new Error('useProgress must be used within LearningProgressProvider');
  return ctx;
}
