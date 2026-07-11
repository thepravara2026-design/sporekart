import axios, { AxiosInstance, AxiosRequestConfig } from "axios";
import { MobileConfig } from "../types";

/**
 * API Client for mobile applications
 * Handles:
 * - Base URL configuration
 * - Request/response interceptors
 * - Token refresh
 * - Error handling
 * - Request timeout
 */
export class ApiClient {
  private client: AxiosInstance;
  private config: MobileConfig;

  constructor(config: MobileConfig) {
    this.config = config;
    this.client = axios.create({
      baseURL: config.apiBaseUrl,
      timeout: config.requestTimeout,
      headers: {
        "Content-Type": "application/json",
        "User-Agent": "SporeKart-Mobile/1.0",
      },
    });

    this.setupInterceptors();
  }

  private setupInterceptors(): void {
    // Request interceptor: Add auth token
    this.client.interceptors.request.use(
      (config) => {
        const token = this.getStoredToken();
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor: Handle 401, retry with refresh token
    this.client.interceptors.response.use(
      (response) => response,
      async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          const refreshToken = this.getStoredRefreshToken();

          if (refreshToken) {
            try {
              const response = await this.refreshAuthToken(refreshToken);
              this.storeToken(response.token, response.refreshToken);
              originalRequest.headers.Authorization = `Bearer ${response.token}`;
              return this.client(originalRequest);
            } catch (refreshError) {
              // Clear tokens and redirect to login
              this.clearTokens();
              // TODO: Emit logout event
              return Promise.reject(refreshError);
            }
          }
        }

        return Promise.reject(error);
      }
    );
  }

  async get<T>(endpoint: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.get<T>(endpoint, config);
    return response.data;
  }

  async post<T>(
    endpoint: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response = await this.client.post<T>(endpoint, data, config);
    return response.data;
  }

  async put<T>(
    endpoint: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response = await this.client.put<T>(endpoint, data, config);
    return response.data;
  }

  async delete<T>(endpoint: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.delete<T>(endpoint, config);
    return response.data;
  }

  async patch<T>(
    endpoint: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response = await this.client.patch<T>(endpoint, data, config);
    return response.data;
  }

  private async refreshAuthToken(
    refreshToken: string
  ): Promise<{ token: string; refreshToken: string }> {
    // This would call a specific endpoint that doesn't require interceptors
    const response = await axios.post(
      `${this.config.apiBaseUrl}/auth/refresh`,
      { refreshToken }
    );
    return response.data;
  }

  private getStoredToken(): string | null {
    // TODO: Get from secure storage
    return null;
  }

  private getStoredRefreshToken(): string | null {
    // TODO: Get from secure storage
    return null;
  }

  private storeToken(token: string, refreshToken: string): void {
    // TODO: Store in secure storage
  }

  private clearTokens(): void {
    // TODO: Clear from secure storage
  }
}
