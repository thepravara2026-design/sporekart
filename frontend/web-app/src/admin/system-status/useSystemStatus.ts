import { useState, useCallback } from 'react';
import type { SystemService, ServiceStatus, SystemStatusData } from './types';

const MOCK_SERVICES: SystemService[] = [
  { id: 'database', label: 'Database', status: 'operational', uptime: 99.97, latency: 12 },
  { id: 'api', label: 'API Server', status: 'operational', uptime: 99.99, latency: 45 },
  { id: 'payments', label: 'Payment Gateway', status: 'operational', uptime: 99.95, latency: 120 },
  { id: 'shipping', label: 'Shipping API', status: 'degraded', uptime: 98.5, latency: 320, description: 'Increased latency detected' },
  { id: 'storage', label: 'File Storage', status: 'operational', uptime: 100, latency: 8 },
  { id: 'notifications', label: 'Notification Service', status: 'operational', uptime: 99.9, latency: 35 },
  { id: 'email', label: 'Email Service', status: 'partial_outage', uptime: 97.2, latency: 1500, description: 'Delivery delays for some providers' },
  { id: 'background-jobs', label: 'Background Jobs', status: 'operational', uptime: 99.8, latency: 60 },
];

function computeOverall(services: SystemService[]): ServiceStatus {
  if (services.some((s) => s.status === 'major_outage')) return 'major_outage';
  if (services.some((s) => s.status === 'partial_outage')) return 'partial_outage';
  if (services.some((s) => s.status === 'degraded')) return 'degraded';
  if (services.some((s) => s.status === 'maintenance')) return 'maintenance';
  return 'operational';
}

export function useSystemStatus(initialServices?: SystemService[]) {
  const [services] = useState<SystemService[]>(initialServices ?? MOCK_SERVICES);

  const overallStatus = computeOverall(services);

  const getStatusData = useCallback((): SystemStatusData => ({
    services,
    lastUpdated: new Date().toISOString(),
    overallStatus,
  }), [services, overallStatus]);

  return { services, overallStatus, getStatusData };
}
