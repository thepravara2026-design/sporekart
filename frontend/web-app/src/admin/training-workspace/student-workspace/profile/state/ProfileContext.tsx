import React, { createContext, useContext, useState, useCallback, useMemo } from 'react';
import type { StudentProfile } from '../types';
import { getProfileByStudentId, getProfileById, generateMockProfiles } from '../data/mockData';

interface ProfileState {
  profiles: StudentProfile[];
  currentProfile: StudentProfile | null;
  isLoading: boolean;
  error: string | null;
  searchTerm: string;
  filterStatus: string;
}

interface ProfileContextValue extends ProfileState {
  setSearchTerm: (term: string) => void;
  setFilterStatus: (status: string) => void;
  selectProfile: (studentId: string) => void;
  selectProfileById: (id: string) => void;
  clearSelection: () => void;
  updateProfile: (updated: StudentProfile) => void;
  refreshProfiles: () => void;
}

const initialState: ProfileState = {
  profiles: [],
  currentProfile: null,
  isLoading: false,
  error: null,
  searchTerm: '',
  filterStatus: 'all',
};

const ProfileContext = createContext<ProfileContextValue | undefined>(undefined);

export function ProfileProvider({ children }: { children: React.ReactNode }) {
  const [state, setState] = useState<ProfileState>(initialState);

  const setSearchTerm = useCallback((searchTerm: string) => {
    setState((prev) => ({ ...prev, searchTerm }));
  }, []);

  const setFilterStatus = useCallback((filterStatus: string) => {
    setState((prev) => ({ ...prev, filterStatus }));
  }, []);

  const selectProfile = useCallback((studentId: string) => {
    setState((prev) => ({ ...prev, isLoading: true, error: null }));
    const profile = getProfileByStudentId(studentId);
    setState((prev) => ({
      ...prev,
      currentProfile: profile ?? null,
      isLoading: false,
      error: profile ? null : `Profile not found for ${studentId}`,
    }));
  }, []);

  const selectProfileById = useCallback((id: string) => {
    setState((prev) => ({ ...prev, isLoading: true, error: null }));
    const profile = getProfileById(id);
    setState((prev) => ({
      ...prev,
      currentProfile: profile ?? null,
      isLoading: false,
      error: profile ? null : `Profile not found for id ${id}`,
    }));
  }, []);

  const clearSelection = useCallback(() => {
    setState((prev) => ({ ...prev, currentProfile: null, error: null }));
  }, []);

  const updateProfile = useCallback((updated: StudentProfile) => {
    setState((prev) => ({
      ...prev,
      currentProfile: updated,
      profiles: prev.profiles.map((p) => (p.id === updated.id ? updated : p)),
    }));
  }, []);

  const refreshProfiles = useCallback(() => {
    setState((prev) => ({ ...prev, profiles: [], isLoading: true }));
    const fresh = generateMockProfiles(10);
    setState((prev) => ({
      ...prev,
      profiles: fresh,
      isLoading: false,
    }));
  }, []);

  const value = useMemo(
    () => ({ ...state, setSearchTerm, setFilterStatus, selectProfile, selectProfileById, clearSelection, updateProfile, refreshProfiles }),
    [state, setSearchTerm, setFilterStatus, selectProfile, selectProfileById, clearSelection, updateProfile, refreshProfiles],
  );

  return <ProfileContext.Provider value={value}>{children}</ProfileContext.Provider>;
}

export function useProfile(): ProfileContextValue {
  const ctx = useContext(ProfileContext);
  if (!ctx) throw new Error('useProfile must be used within ProfileProvider');
  return ctx;
}
