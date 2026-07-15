export interface AuditEntry {
  id: string;
  action: string;
  performedBy: string;
  timestamp: string;
  details?: string;
  resource?: string;
  resourceId?: string;
}

export interface AuditMeta {
  createdBy: string;
  createdAt: string;
  updatedBy?: string;
  updatedAt?: string;
  version?: number;
}
