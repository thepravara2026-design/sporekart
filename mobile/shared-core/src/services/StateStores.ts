import { create } from "zustand";
import { Device, Session, User, AuthResponse } from "../types";

interface AuthState {
  user: User | null;
  device: Device | null;
  session: Session | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;

  // Actions
  setUser: (user: User | null) => void;
  setDevice: (device: Device | null) => void;
  setSession: (session: Session | null) => void;
  setAuthResponse: (response: AuthResponse) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  logout: () => void;
  clearError: () => void;
}

/**
 * Global authentication state management
 * Stores:
 * - Current user info
 * - Device info
 * - Session/token info
 * - Auth status and errors
 */
export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  device: null,
  session: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,

  setUser: (user) => set({ user, isAuthenticated: !!user }),
  setDevice: (device) => set({ device }),
  setSession: (session) => set({ session }),

  setAuthResponse: (response) =>
    set({
      user: response.user,
      device: response.device,
      session: {
        id: response.device.id,
        deviceId: response.device.id,
        userId: response.user.id,
        token: response.token,
        refreshToken: response.refreshToken,
        expiresAt: new Date(Date.now() + response.expiresIn * 1000).toISOString(),
        createdAt: new Date().toISOString(),
        isActive: true,
      },
      isAuthenticated: true,
    }),

  setLoading: (loading) => set({ isLoading: loading }),
  setError: (error) => set({ error }),
  logout: () =>
    set({
      user: null,
      device: null,
      session: null,
      isAuthenticated: false,
      error: null,
    }),
  clearError: () => set({ error: null }),
}));

interface SyncState {
  isSyncing: boolean;
  lastSyncTime: number | null;
  pendingCount: number;
  failedCount: number;
  syncError: string | null;

  // Actions
  startSync: () => void;
  endSync: (success: boolean, error?: string) => void;
  setPendingCount: (count: number) => void;
  setFailedCount: (count: number) => void;
}

/**
 * Offline sync state management
 * Tracks:
 * - Sync status and timing
 * - Pending/failed request counts
 * - Sync errors
 */
export const useSyncStore = create<SyncState>((set) => ({
  isSyncing: false,
  lastSyncTime: null,
  pendingCount: 0,
  failedCount: 0,
  syncError: null,

  startSync: () => set({ isSyncing: true, syncError: null }),
  endSync: (success, error) =>
    set({
      isSyncing: false,
      lastSyncTime: success ? Date.now() : undefined,
      syncError: error || null,
    }),
  setPendingCount: (count) => set({ pendingCount: count }),
  setFailedCount: (count) => set({ failedCount: count }),
}));

interface NotificationState {
  notifications: Array<{ id: string; title: string; body: string }>;
  unreadCount: number;
  lastNotificationTime: number | null;

  // Actions
  addNotification: (notification: {
    id: string;
    title: string;
    body: string;
  }) => void;
  clearNotifications: () => void;
  setUnreadCount: (count: number) => void;
}

/**
 * Notification state management
 * Tracks:
 * - Recent notifications
 * - Unread count
 * - Last notification time
 */
export const useNotificationStore = create<NotificationState>((set) => ({
  notifications: [],
  unreadCount: 0,
  lastNotificationTime: null,

  addNotification: (notification) =>
    set((state) => ({
      notifications: [notification, ...state.notifications].slice(0, 50),
      unreadCount: state.unreadCount + 1,
      lastNotificationTime: Date.now(),
    })),

  clearNotifications: () =>
    set({
      notifications: [],
      unreadCount: 0,
    }),

  setUnreadCount: (count) => set({ unreadCount: count }),
}));
