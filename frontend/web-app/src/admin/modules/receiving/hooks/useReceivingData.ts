import { useState } from 'react';
import { MOCK_RECEIPTS, MOCK_INSPECTIONS, MOCK_TIMELINE, MOCK_AUDIT, MOCK_ALLOCATIONS, MOCK_BATCH_ASSIGNMENTS, MOCK_ANALYTICS } from '../services/receivingMockService';
import type { GoodsReceiptRecord, InspectionRecord, ReceivingTimelineEvent, AuditRecord, AllocationRecord, BatchAssignmentRecord, AnalyticsData } from '../types';

export function useReceivingData() {
  const [receipts] = useState<GoodsReceiptRecord[]>(MOCK_RECEIPTS);
  const [inspections] = useState<InspectionRecord[]>(MOCK_INSPECTIONS);
  const [timeline] = useState<ReceivingTimelineEvent[]>(MOCK_TIMELINE);
  const [auditRecords] = useState<AuditRecord[]>(MOCK_AUDIT);
  const [allocations] = useState<AllocationRecord[]>(MOCK_ALLOCATIONS);
  const [batchAssignments] = useState<BatchAssignmentRecord[]>(MOCK_BATCH_ASSIGNMENTS);
  const [analytics] = useState<AnalyticsData>(MOCK_ANALYTICS);
  const [loading] = useState(false);
  return { receipts, inspections, timeline, auditRecords, allocations, batchAssignments, analytics, loading };
}
