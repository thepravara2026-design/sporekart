export type ServiceStatus = 'operational' | 'degraded' | 'partial_outage' | 'major_outage' | 'maintenance';

export interface SystemService {
  id: string;
  label: string;
  status: ServiceStatus;
  description?: string;
  uptime?: number;
  latency?: number;
  lastIncident?: string;
}

export interface SystemStatusData {
  services: SystemService[];
  lastUpdated: string;
  overallStatus: ServiceStatus;
}
