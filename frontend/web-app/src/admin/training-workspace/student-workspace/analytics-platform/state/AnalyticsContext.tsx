import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { StudentAnalytics, CourseMetrics, TrainerMetrics, BatchMetrics, ExecutiveDashboard, KPIData, AcademicInsights, LearningIntelligence } from '../types';
import {
  getStudentAnalytics, getCourseMetrics, getTrainerMetrics, getBatchMetrics,
  getExecutiveDashboard, getKPIs, getAcademicInsights, getLearningIntelligence,
} from '../data/mockData';

interface AnalyticsState {
  studentAnalytics: StudentAnalytics[];
  courseMetrics: CourseMetrics[];
  trainerMetrics: TrainerMetrics[];
  batchMetrics: BatchMetrics[];
  executiveDashboard: ExecutiveDashboard;
  kpis: KPIData[];
  insights: AcademicInsights;
  intelligence: LearningIntelligence;
  searchTerm: string;
  courseFilter: string;
  batchFilter: string;
  performanceFilter: string;
}

interface AnalyticsContextValue extends AnalyticsState {
  setSearchTerm: (term: string) => void;
  setCourseFilter: (course: string) => void;
  setBatchFilter: (batch: string) => void;
  setPerformanceFilter: (level: string) => void;
  getFilteredStudents: () => StudentAnalytics[];
}

const Ctx = createContext<AnalyticsContextValue | undefined>(undefined);

export function AnalyticsProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<AnalyticsState>(() => ({
    studentAnalytics: getStudentAnalytics(),
    courseMetrics: getCourseMetrics(),
    trainerMetrics: getTrainerMetrics(),
    batchMetrics: getBatchMetrics(),
    executiveDashboard: getExecutiveDashboard(),
    kpis: getKPIs(),
    insights: getAcademicInsights(),
    intelligence: getLearningIntelligence(),
    searchTerm: '',
    courseFilter: 'all',
    batchFilter: 'all',
    performanceFilter: 'all',
  }));

  const setSearchTerm = useCallback((searchTerm: string) => setState((p) => ({ ...p, searchTerm })), []);
  const setCourseFilter = useCallback((courseFilter: string) => setState((p) => ({ ...p, courseFilter })), []);
  const setBatchFilter = useCallback((batchFilter: string) => setState((p) => ({ ...p, batchFilter })), []);
  const setPerformanceFilter = useCallback((performanceFilter: string) => setState((p) => ({ ...p, performanceFilter })), []);

  const getFilteredStudents = useCallback(() => {
    let result = state.studentAnalytics;
    if (state.courseFilter !== 'all') result = result.filter((s) => s.courseId === state.courseFilter);
    if (state.batchFilter !== 'all') result = result.filter((s) => s.batchId === state.batchFilter);
    if (state.performanceFilter !== 'all') result = result.filter((s) => s.performanceLevel === state.performanceFilter);
    if (state.searchTerm) {
      const t = state.searchTerm.toLowerCase();
      result = result.filter((s) => s.studentName.toLowerCase().includes(t) || s.courseName.toLowerCase().includes(t));
    }
    return result;
  }, [state.studentAnalytics, state.searchTerm, state.courseFilter, state.batchFilter, state.performanceFilter]);

  const value = useMemo(() => ({ ...state, setSearchTerm, setCourseFilter, setBatchFilter, setPerformanceFilter, getFilteredStudents }), [state, setSearchTerm, setCourseFilter, setBatchFilter, setPerformanceFilter, getFilteredStudents]);

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useAnalytics(): AnalyticsContextValue {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error('useAnalytics must be used within AnalyticsProvider');
  return ctx;
}
