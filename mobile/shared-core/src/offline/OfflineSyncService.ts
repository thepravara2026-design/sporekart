import { OfflineQueue, SyncStatus } from "../types";

/**
 * Offline Sync Service
 * Manages:
 * - Offline request queuing
 * - Background synchronization
 * - Conflict resolution
 * - Retry logic
 */
export class OfflineSyncService {
  private queue: OfflineQueue[] = [];
  private isSyncing = false;

  /**
   * Add a request to the offline queue
   */
  addToQueue(
    method: string,
    endpoint: string,
    payload?: unknown,
    maxRetries: number = 3
  ): OfflineQueue {
    const queueItem: OfflineQueue = {
      id: this.generateId(),
      requestId: this.generateId(),
      method: method as any,
      endpoint,
      payload: payload as any,
      retryCount: 0,
      maxRetries,
      createdAt: new Date().toISOString(),
    };

    this.queue.push(queueItem);
    this.persistQueue();
    return queueItem;
  }

  /**
   * Get current sync status
   */
  getSyncStatus(): SyncStatus {
    const failedCount = this.queue.filter(
      (item) => item.retryCount >= item.maxRetries
    ).length;

    return {
      isOnline: true, // TODO: Check actual connectivity
      isSyncing: this.isSyncing,
      pendingCount: this.queue.length,
      failedCount,
    };
  }

  /**
   * Process pending queue items
   */
  async processPendingQueue(apiClient: any): Promise<void> {
    if (this.isSyncing || this.queue.length === 0) {
      return;
    }

    this.isSyncing = true;
    const itemsToProcess = [...this.queue];

    for (const item of itemsToProcess) {
      if (item.retryCount >= item.maxRetries) {
        continue; // Skip failed items
      }

      try {
        await this.processQueueItem(item, apiClient);
        this.queue = this.queue.filter((q) => q.id !== item.id);
      } catch (error) {
        item.retryCount++;
        item.lastError = (error as any).message;

        // Exponential backoff
        await new Promise((resolve) =>
          setTimeout(resolve, Math.pow(2, item.retryCount) * 1000)
        );
      }
    }

    this.isSyncing = false;
    this.persistQueue();
  }

  /**
   * Clear failed queue items
   */
  clearFailedItems(): void {
    this.queue = this.queue.filter(
      (item) => item.retryCount < item.maxRetries
    );
    this.persistQueue();
  }

  /**
   * Get pending queue items
   */
  getPendingItems(): OfflineQueue[] {
    return [...this.queue];
  }

  private async processQueueItem(
    item: OfflineQueue,
    apiClient: any
  ): Promise<void> {
    const method = item.method.toLowerCase();

    switch (method) {
      case "get":
        await apiClient.get(item.endpoint);
        break;
      case "post":
        await apiClient.post(item.endpoint, item.payload);
        break;
      case "put":
        await apiClient.put(item.endpoint, item.payload);
        break;
      case "patch":
        await apiClient.patch(item.endpoint, item.payload);
        break;
      case "delete":
        await apiClient.delete(item.endpoint);
        break;
      default:
        throw new Error(`Unknown HTTP method: ${method}`);
    }
  }

  private persistQueue(): void {
    // TODO: Persist to SQLite
  }

  private generateId(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }
}

export const offlineSyncService = new OfflineSyncService();
