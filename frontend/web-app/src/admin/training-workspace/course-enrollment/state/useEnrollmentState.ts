import { useCallback, useReducer } from 'react';
import type {
  EnrollmentSection,
  CourseCommerce,
  EnrollmentRequest,
  WaitlistEntry,
  EligibilityRule,
  RegistrationStatus,
} from '../data/enrollmentMockData';
import {
  MOCK_COURSE_COMMERCE,
  MOCK_ENROLLMENT_REQUESTS,
  MOCK_WAITLIST,
  MOCK_ELIGIBILITY_RULES,
} from '../data/enrollmentMockData';

export interface EnrollmentFilters {
  pricingType: string;
  registrationStatus: string;
  deliveryMode: string;
  language: string;
  availability: string;
}

interface EnrollmentState {
  section: EnrollmentSection;
  courses: CourseCommerce[];
  requests: EnrollmentRequest[];
  waitlist: WaitlistEntry[];
  eligibilityRules: EligibilityRule[];
  search: string;
  filters: EnrollmentFilters;
  selectedCourseId: string | null;
  page: number;
  pageSize: number;
}

type EnrollmentAction =
  | { type: 'SET_SECTION'; payload: EnrollmentSection }
  | { type: 'SET_SEARCH'; payload: string }
  | { type: 'SET_FILTERS'; payload: Partial<EnrollmentFilters> }
  | { type: 'RESET_FILTERS' }
  | { type: 'SET_SELECTED_COURSE'; payload: string | null }
  | { type: 'SET_PAGE'; payload: number }
  | { type: 'SET_REQUEST_STATUS'; payload: { id: string; status: RegistrationStatus } }
  | { type: 'PROMOTE_WAITLIST'; payload: string };

const INITIAL_FILTERS: EnrollmentFilters = {
  pricingType: 'all',
  registrationStatus: 'all',
  deliveryMode: 'all',
  language: 'all',
  availability: 'all',
};

const INITIAL_STATE: EnrollmentState = {
  section: 'overview',
  courses: MOCK_COURSE_COMMERCE,
  requests: MOCK_ENROLLMENT_REQUESTS,
  waitlist: MOCK_WAITLIST,
  eligibilityRules: MOCK_ELIGIBILITY_RULES,
  search: '',
  filters: INITIAL_FILTERS,
  selectedCourseId: null,
  page: 1,
  pageSize: 6,
};

function reducer(state: EnrollmentState, action: EnrollmentAction): EnrollmentState {
  switch (action.type) {
    case 'SET_SECTION':
      return { ...state, section: action.payload };
    case 'SET_SEARCH':
      return { ...state, search: action.payload, page: 1 };
    case 'SET_FILTERS':
      return { ...state, filters: { ...state.filters, ...action.payload }, page: 1 };
    case 'RESET_FILTERS':
      return { ...state, filters: INITIAL_FILTERS, search: '', page: 1 };
    case 'SET_SELECTED_COURSE':
      return { ...state, selectedCourseId: action.payload };
    case 'SET_PAGE':
      return { ...state, page: action.payload };
    case 'SET_REQUEST_STATUS':
      return {
        ...state,
        requests: state.requests.map((r) => (r.id === action.payload.id ? { ...r, status: action.payload.status } : r)),
      };
    case 'PROMOTE_WAITLIST':
      return { ...state, waitlist: state.waitlist.filter((w) => w.id !== action.payload) };
    default:
      return state;
  }
}

export function useEnrollmentState() {
  const [state, dispatch] = useReducer(reducer, INITIAL_STATE);

  const setSection = useCallback((section: EnrollmentSection) => dispatch({ type: 'SET_SECTION', payload: section }), []);
  const setSearch = useCallback((search: string) => dispatch({ type: 'SET_SEARCH', payload: search }), []);
  const setFilters = useCallback((filters: Partial<EnrollmentFilters>) => dispatch({ type: 'SET_FILTERS', payload: filters }), []);
  const resetFilters = useCallback(() => dispatch({ type: 'RESET_FILTERS' }), []);
  const setSelectedCourse = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_COURSE', payload: id }), []);
  const setPage = useCallback((page: number) => dispatch({ type: 'SET_PAGE', payload: page }), []);
  const setRequestStatus = useCallback((id: string, status: RegistrationStatus) =>
    dispatch({ type: 'SET_REQUEST_STATUS', payload: { id, status } }), []);
  const promoteWaitlist = useCallback((id: string) => dispatch({ type: 'PROMOTE_WAITLIST', payload: id }), []);

  return {
    state,
    setSection,
    setSearch,
    setFilters,
    resetFilters,
    setSelectedCourse,
    setPage,
    setRequestStatus,
    promoteWaitlist,
  };
}
