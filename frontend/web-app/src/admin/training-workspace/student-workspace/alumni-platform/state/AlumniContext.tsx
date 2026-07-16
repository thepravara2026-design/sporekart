import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type {
  StudentCareerProfile, JobOpportunity, PlacementDrive, CompanyPartnership,
  CareerCounselingSession, InterviewPreparation, SkillGapAssessment,
  AlumniProfile, AlumniMentorship, AlumniContribution, AlumniEvent,
  AlumniAnalytics, PlacementDashboard,
  PlacementStatus, CompanyPartnershipTier, AlumniEngagementLevel, JobOpportunityType,
  PlacementDriveStage,
} from '../types';
import {
  getStudentCareerProfiles, getJobOpportunities, getPlacementDrives, getCompanyPartnerships,
  getCounselingSessions, getInterviewPreparations, getSkillGapAssessments,
  getAlumniProfiles, getAlumniMentorships, getAlumniContributions, getAlumniEvents,
  getAlumniAnalytics, getPlacementDashboard,
} from '../data/mockData';

export type SortKey = 'name' | 'date' | 'status' | 'company' | 'tier' | 'engagement' | 'score' | 'salary' | 'skill';
export type SortDirection = 'asc' | 'desc';

interface AlumniFilters {
  search: string;
  status: PlacementStatus | 'all';
  type: JobOpportunityType | 'all';
  company: string;
  tier: CompanyPartnershipTier | 'all';
  engagement: AlumniEngagementLevel | 'all';
  driveStage: PlacementDriveStage | 'all';
  dateFrom: string;
  dateTo: string;
  sortKey: SortKey;
  sortDirection: SortDirection;
}

interface AlumniContextValue {
  studentProfiles: StudentCareerProfile[];
  jobOpportunities: JobOpportunity[];
  placementDrives: PlacementDrive[];
  companies: CompanyPartnership[];
  counselingSessions: CareerCounselingSession[];
  interviewPrep: InterviewPreparation[];
  skillGaps: SkillGapAssessment[];
  alumni: AlumniProfile[];
  mentorships: AlumniMentorship[];
  contributions: AlumniContribution[];
  events: AlumniEvent[];
  analytics: AlumniAnalytics;
  dashboard: PlacementDashboard;
  filters: AlumniFilters;
  page: number;
  pageSize: number;
  setSearch: (s: string) => void;
  setStatusFilter: (s: PlacementStatus | 'all') => void;
  setTypeFilter: (t: JobOpportunityType | 'all') => void;
  setCompanyFilter: (c: string) => void;
  setTierFilter: (t: CompanyPartnershipTier | 'all') => void;
  setEngagementFilter: (e: AlumniEngagementLevel | 'all') => void;
  setDriveStageFilter: (s: PlacementDriveStage | 'all') => void;
  setDateFrom: (d: string) => void;
  setDateTo: (d: string) => void;
  setSort: (key: SortKey) => void;
  setPage: (p: number) => void;
  setPageSize: (s: number) => void;
  getFilteredProfiles: () => StudentCareerProfile[];
  getFilteredJobs: () => JobOpportunity[];
  getFilteredDrives: () => PlacementDrive[];
  getFilteredCompanies: () => CompanyPartnership[];
  getFilteredAlumni: () => AlumniProfile[];
  getFilteredEvents: () => AlumniEvent[];
  getFilteredContributions: () => AlumniContribution[];
  getFilteredMentorships: () => AlumniMentorship[];
}

const Ctx = createContext<AlumniContextValue | undefined>(undefined);

const PRIORITY_MAP: Record<string, number> = { platinum: 0, gold: 1, silver: 2, bronze: 3, strategic: 4, academic: 5, government: 6 };

function sortItems<T>(items: T[], key: SortKey, dir: SortDirection): T[] {
  return [...items].sort((a, b) => {
    const aVal = String((a as Record<string, unknown>)[key] ?? '');
    const bVal = String((b as Record<string, unknown>)[key] ?? '');
    let cmp: number;
    if (key === 'tier') {
      cmp = (PRIORITY_MAP[aVal] ?? 99) - (PRIORITY_MAP[bVal] ?? 99);
    } else {
      cmp = aVal.localeCompare(bVal);
    }
    return dir === 'asc' ? cmp : -cmp;
  });
}

export function AlumniProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState(() => ({
    studentProfiles: getStudentCareerProfiles(),
    jobOpportunities: getJobOpportunities(),
    placementDrives: getPlacementDrives(),
    companies: getCompanyPartnerships(),
    counselingSessions: getCounselingSessions(),
    interviewPrep: getInterviewPreparations(),
    skillGaps: getSkillGapAssessments(),
    alumni: getAlumniProfiles(),
    mentorships: getAlumniMentorships(),
    contributions: getAlumniContributions(),
    events: getAlumniEvents(),
    analytics: getAlumniAnalytics(),
    dashboard: getPlacementDashboard(),
    filters: {
      search: '', status: 'all' as PlacementStatus | 'all', type: 'all' as JobOpportunityType | 'all', company: 'all' as string,
      tier: 'all' as CompanyPartnershipTier | 'all', engagement: 'all' as AlumniEngagementLevel | 'all', driveStage: 'all' as PlacementDriveStage | 'all',
      dateFrom: '', dateTo: '', sortKey: 'date' as SortKey, sortDirection: 'desc' as SortDirection,
    },
    page: 1, pageSize: 10,
  }));

  const updFilter = useCallback((updates: Partial<AlumniFilters>) => setState((p) => ({ ...p, filters: { ...p.filters, ...updates }, page: 1 })), []);
  const setSearch = useCallback((s: string) => updFilter({ search: s }), [updFilter]);
  const setStatusFilter = useCallback((s: PlacementStatus | 'all') => updFilter({ status: s }), [updFilter]);
  const setTypeFilter = useCallback((t: JobOpportunityType | 'all') => updFilter({ type: t }), [updFilter]);
  const setCompanyFilter = useCallback((c: string) => updFilter({ company: c }), [updFilter]);
  const setTierFilter = useCallback((t: CompanyPartnershipTier | 'all') => updFilter({ tier: t }), [updFilter]);
  const setEngagementFilter = useCallback((e: AlumniEngagementLevel | 'all') => updFilter({ engagement: e }), [updFilter]);
  const setDriveStageFilter = useCallback((s: PlacementDriveStage | 'all') => updFilter({ driveStage: s }), [updFilter]);
  const setDateFrom = useCallback((d: string) => updFilter({ dateFrom: d }), [updFilter]);
  const setDateTo = useCallback((d: string) => updFilter({ dateTo: d }), [updFilter]);
  const setSort = useCallback((sortKey: SortKey) => setState((p) => ({ ...p, filters: { ...p.filters, sortKey, sortDirection: p.filters.sortKey === sortKey && p.filters.sortDirection === 'asc' ? 'desc' : 'asc' }, page: 1 })), []);
  const setPage = useCallback((page: number) => setState((p) => ({ ...p, page })), []);
  const setPageSize = useCallback((pageSize: number) => setState((p) => ({ ...p, pageSize, page: 1 })), []);

  const getFilteredProfiles = useCallback(() => {
    let result = state.studentProfiles;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((p) => p.studentName.toLowerCase().includes(t) || p.targetRole.toLowerCase().includes(t));
    }
    if (state.filters.status !== 'all') result = result.filter((p) => p.placementStatus === state.filters.status);
    if (state.filters.dateFrom) result = result.filter((p) => p.createdDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((p) => p.createdDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.studentProfiles, state.filters]);

  const getFilteredJobs = useCallback(() => {
    let result = state.jobOpportunities;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((j) => j.title.toLowerCase().includes(t) || j.companyName.toLowerCase().includes(t));
    }
    if (state.filters.type !== 'all') result = result.filter((j) => j.jobType === state.filters.type);
    if (state.filters.company !== 'all') result = result.filter((j) => j.companyId === state.filters.company);
    if (state.filters.tier !== 'all') result = result.filter((j) => j.partnerTier === state.filters.tier);
    if (state.filters.dateFrom) result = result.filter((j) => j.postedDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((j) => j.postedDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.jobOpportunities, state.filters]);

  const getFilteredDrives = useCallback(() => {
    let result = state.placementDrives;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((d) => d.name.toLowerCase().includes(t) || d.companyName.toLowerCase().includes(t));
    }
    if (state.filters.driveStage !== 'all') result = result.filter((d) => d.stage === state.filters.driveStage);
    if (state.filters.company !== 'all') result = result.filter((d) => d.companyId === state.filters.company);
    if (state.filters.dateFrom) result = result.filter((d) => d.createdDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((d) => d.createdDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.placementDrives, state.filters]);

  const getFilteredCompanies = useCallback(() => {
    let result = state.companies;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((c) => c.companyName.toLowerCase().includes(t));
    }
    if (state.filters.tier !== 'all') result = result.filter((c) => c.tier === state.filters.tier);
    if (state.filters.dateFrom) result = result.filter((c) => c.partnershipDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((c) => c.partnershipDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.companies, state.filters]);

  const getFilteredAlumni = useCallback(() => {
    let result = state.alumni;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((a) => a.fullName.toLowerCase().includes(t) || a.currentCompany.toLowerCase().includes(t) || a.currentPosition.toLowerCase().includes(t));
    }
    if (state.filters.engagement !== 'all') result = result.filter((a) => a.engagementLevel === state.filters.engagement);
    if (state.filters.company !== 'all') result = result.filter((a) => a.currentCompany.toLowerCase().includes(state.filters.company === 'all' ? '' : state.filters.company === 'company-1' ? 'Google' : state.filters.company === 'company-2' ? 'Microsoft' : state.filters.company === 'company-3' ? 'Amazon' : ''));
    if (state.filters.dateFrom) result = result.filter((a) => a.lastActiveDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((a) => a.lastActiveDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.alumni, state.filters]);

  const getFilteredEvents = useCallback(() => {
    let result = state.events;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((e) => e.title.toLowerCase().includes(t));
    }
    if (state.filters.status !== 'all') result = result.filter((e) => e.status === (state.filters.status as string));
    if (state.filters.dateFrom) result = result.filter((e) => e.date >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((e) => e.date <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.events, state.filters]);

  const getFilteredContributions = useCallback(() => {
    let result = state.contributions;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((c) => c.alumniName.toLowerCase().includes(t) || c.description.toLowerCase().includes(t));
    }
    if (state.filters.dateFrom) result = result.filter((c) => c.date >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((c) => c.date <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.contributions, state.filters]);

  const getFilteredMentorships = useCallback(() => {
    let result = state.mentorships;
    if (state.filters.search) {
      const t = state.filters.search.toLowerCase();
      result = result.filter((m) => m.mentorName.toLowerCase().includes(t) || m.menteeStudentName.toLowerCase().includes(t));
    }
    if (state.filters.status !== 'all') {
      result = result.filter((m) => m.status === state.filters.status as unknown as AlumniMentorship['status']);
    }
    if (state.filters.dateFrom) result = result.filter((m) => m.startDate >= state.filters.dateFrom);
    if (state.filters.dateTo) result = result.filter((m) => m.startDate <= state.filters.dateTo);
    return sortItems(result, state.filters.sortKey, state.filters.sortDirection);
  }, [state.mentorships, state.filters]);

  const value = useMemo(() => ({
    ...state, setSearch, setStatusFilter, setTypeFilter, setCompanyFilter,
    setTierFilter, setEngagementFilter, setDriveStageFilter,
    setDateFrom, setDateTo, setSort, setPage, setPageSize,
    getFilteredProfiles, getFilteredJobs, getFilteredDrives, getFilteredCompanies,
    getFilteredAlumni, getFilteredEvents, getFilteredContributions, getFilteredMentorships,
  }), [state, setSearch, setStatusFilter, setTypeFilter, setCompanyFilter,
      setTierFilter, setEngagementFilter, setDriveStageFilter,
      setDateFrom, setDateTo, setSort, setPage, setPageSize,
      getFilteredProfiles, getFilteredJobs, getFilteredDrives, getFilteredCompanies,
      getFilteredAlumni, getFilteredEvents, getFilteredContributions, getFilteredMentorships]);

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useAlumni(): AlumniContextValue {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error('useAlumni must be used within AlumniProvider');
  return ctx;
}
