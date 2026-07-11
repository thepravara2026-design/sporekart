// Device and Authentication Types

export interface Device {
  id: string;
  deviceName: string;
  deviceType: "iOS" | "Android" | "Web";
  osVersion: string;
  appVersion: string;
  fcmToken: string;
  isActive: boolean;
  isTrusted: boolean;
  lastLogin: ISO8601;
  createdAt: ISO8601;
}

export interface Session {
  id: string;
  deviceId: string;
  userId: string;
  token: string;
  refreshToken: string;
  expiresAt: ISO8601;
  createdAt: ISO8601;
  isActive: boolean;
}

export interface AuthCredentials {
  phone: string;
  otp?: string;
  biometric?: boolean;
  deviceId: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  device: Device;
  user: User;
  expiresIn: number;
}

export interface User {
  id: string;
  name: string;
  email: string;
  phone: string;
  role: "CUSTOMER" | "GROWER" | "TRAINER" | "DEALER" | "DISTRIBUTOR" | "ADMIN" | "SUPPORT";
  avatar?: string;
  createdAt: ISO8601;
}

// Offline Sync Types

export interface OfflineSyncLog {
  id: string;
  deviceId: string;
  status: "PENDING" | "IN_PROGRESS" | "SUCCESS" | "FAILED";
  startedAt: ISO8601;
  completedAt?: ISO8601;
  errorMessage?: string;
  recordsSync: number;
  bytesSync: number;
}

export interface OfflineQueue {
  id: string;
  requestId: string;
  method: "GET" | "POST" | "PUT" | "DELETE" | "PATCH";
  endpoint: string;
  payload?: Record<string, any>;
  retryCount: number;
  maxRetries: number;
  lastError?: string;
  createdAt: ISO8601;
}

export interface SyncStatus {
  isOnline: boolean;
  isSyncing: boolean;
  lastSyncAt?: ISO8601;
  pendingCount: number;
  failedCount: number;
}

// Push Notification Types

export interface PushNotification {
  id: string;
  deviceId: string;
  title: string;
  body: string;
  data?: Record<string, string>;
  isRead: boolean;
  isArchived: boolean;
  type: "ORDER" | "TRAINING" | "ALERT" | "APPROVAL" | "SYSTEM";
  actionUrl?: string;
  createdAt: ISO8601;
}

export interface NotificationPreferences {
  userId: string;
  allowOrderNotifications: boolean;
  allowTrainingNotifications: boolean;
  allowSystemNotifications: boolean;
  allowMarketingNotifications: boolean;
  quietHoursStart?: string; // HH:mm format
  quietHoursEnd?: string;
}

// API Response Types

export interface ApiResponse<T> {
  data: T;
  meta?: {
    timestamp: ISO8601;
    requestId: string;
  };
}

export interface ProblemDetails {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance?: string;
  timestamp: ISO8601;
  errors?: Record<string, string[]>;
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: {
    page: number;
    pageSize: number;
    total: number;
    totalPages: number;
  };
}

// Mobile Configuration

export interface MobileConfig {
  apiBaseUrl: string;
  firebaseProjectId: string;
  firebaseMessagingSenderId: string;
  firebaseApiKey: string;
  biometricEnabled: boolean;
  offlineSyncInterval: number; // milliseconds
  offlineQueueMaxRetries: number;
  requestTimeout: number;
  cacheExpiry: Record<string, number>;
}

// Feature Flags

export interface FeatureFlags {
  offlineModeEnabled: boolean;
  biometricAuthEnabled: boolean;
  pushNotificationsEnabled: boolean;
  aiAssistantEnabled: boolean;
  qrScannerEnabled: boolean;
  barcodeScannerEnabled: boolean;
  locationTrackingEnabled: boolean;
  imageCaptureEnabled: boolean;
}

// Common Types

export type ISO8601 = string;

export interface Timestamp {
  createdAt: ISO8601;
  updatedAt: ISO8601;
  deletedAt?: ISO8601;
}

export interface ErrorResponse {
  error: ProblemDetails;
}

export interface SuccessResponse<T> {
  data: T;
}
