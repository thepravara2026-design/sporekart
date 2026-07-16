import { useMemo } from 'react';
import { useEnrollmentContext } from '../../state/EnrollmentContext';
import type { CourseCommerce } from '../../data/enrollmentMockData';

export function matchAvailability(course: CourseCommerce, availability: string): boolean {
  if (availability === 'all') return true;
  if (availability === 'available') return course.capacity.availableSeats > 0;
  if (availability === 'full') return course.capacity.availableSeats === 0;
  if (availability === 'waitlist') return course.capacity.availableSeats === 0 && course.waitlistedCount > 0;
  return true;
}

export function useFilteredCourses() {
  const { state } = useEnrollmentContext();
  const { courses, search, filters } = state;

  const filtered = useMemo(() => {
    const q = search.toLowerCase();
    return courses.filter((c) => {
      if (filters.pricingType !== 'all' && c.pricing.model !== filters.pricingType) return false;
      if (filters.registrationStatus !== 'all' && c.registrationStatus !== filters.registrationStatus) return false;
      if (filters.deliveryMode !== 'all' && c.deliveryMode !== filters.deliveryMode) return false;
      if (filters.language !== 'all' && c.language !== filters.language) return false;
      if (!matchAvailability(c, filters.availability)) return false;
      if (q && !`${c.courseName} ${c.code} ${c.category} ${c.pricing.model}`.toLowerCase().includes(q)) return false;
      return true;
    });
  }, [courses, search, filters]);

  const paged = useMemo(() => {
    const start = (state.page - 1) * state.pageSize;
    return filtered.slice(start, start + state.pageSize);
  }, [filtered, state.page, state.pageSize]);

  return { filtered, paged, total: filtered.length, page: state.page, pageSize: state.pageSize };
}
