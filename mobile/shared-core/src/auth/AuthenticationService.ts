import { AuthCredentials, AuthResponse } from "../types";

/**
 * Authentication Service
 * Handles:
 * - OTP login flow
 * - JWT token management
 * - Refresh token rotation
 * - Biometric authentication
 * - Device registration
 */
export class AuthenticationService {
  private apiClient: any;
  private biometricEnabled: boolean;

  constructor(apiClient: any, biometricEnabled: boolean = false) {
    this.apiClient = apiClient;
    this.biometricEnabled = biometricEnabled;
  }

  /**
   * Request OTP for phone number
   */
  async requestOtp(phone: string): Promise<{ otpId: string; expiresIn: number }> {
    return this.apiClient.post("/auth/request-otp", {
      phone,
      channel: "SMS",
    });
  }

  /**
   * Verify OTP and authenticate
   */
  async verifyOtpAndLogin(
    credentials: AuthCredentials
  ): Promise<AuthResponse> {
    return this.apiClient.post("/auth/verify-otp", {
      phone: credentials.phone,
      otp: credentials.otp,
      deviceId: credentials.deviceId,
    });
  }

  /**
   * Biometric authentication
   * Requires prior enrollment
   */
  async biometricLogin(
    credentials: AuthCredentials
  ): Promise<AuthResponse> {
    if (!this.biometricEnabled) {
      throw new Error("Biometric authentication is not enabled");
    }

    // TODO: Verify biometric with device
    return this.apiClient.post("/auth/biometric-login", {
      deviceId: credentials.deviceId,
      biometricToken: "MOCK_BIOMETRIC_TOKEN",
    });
  }

  /**
   * Register device for push notifications
   */
  async registerDevice(
    deviceName: string,
    deviceType: string,
    osVersion: string,
    fcmToken: string
  ): Promise<{ deviceId: string; isTrusted: boolean }> {
    return this.apiClient.post("/mobile/register-device", {
      deviceName,
      deviceType,
      osVersion,
      fcmToken,
    });
  }

  /**
   * Mark device as trusted (skip OTP next time)
   */
  async trustDevice(deviceId: string): Promise<void> {
    await this.apiClient.put(`/mobile/devices/${deviceId}/trust`);
  }

  /**
   * Refresh authentication tokens
   */
  async refreshToken(refreshToken: string): Promise<AuthResponse> {
    return this.apiClient.post("/auth/refresh", {
      refreshToken,
    });
  }

  /**
   * Logout and invalidate session
   */
  async logout(deviceId: string): Promise<void> {
    await this.apiClient.post("/auth/logout", {
      deviceId,
    });
  }

  /**
   * Enable biometric for this device
   */
  async enrollBiometric(
    deviceId: string,
    biometricToken: string
  ): Promise<void> {
    await this.apiClient.post("/auth/enroll-biometric", {
      deviceId,
      biometricToken,
    });
  }

  /**
   * Check if token is expired
   */
  isTokenExpired(expiresAt: string): boolean {
    return new Date(expiresAt) <= new Date();
  }

  /**
   * Get token expiry time in seconds
   */
  getTokenExpiryIn(expiresAt: string): number {
    const now = Date.now();
    const expiry = new Date(expiresAt).getTime();
    return Math.max(0, Math.floor((expiry - now) / 1000));
  }
}
