import { useState } from 'react';
import { MOCK_TRANSACTIONS, MOCK_TRANSFERS, MOCK_ADJUSTMENTS, MOCK_GOODS_RECEIPTS, MOCK_GOODS_ISSUES, MOCK_TIMELINE, MOCK_AUDIT, MOCK_ANALYTICS } from '../services/movementMockService';
import type { TransactionRecord, TransferRecord, AdjustmentRecord, GoodsReceiptRecord, GoodsIssueRecord, MovementTimelineEvent, AuditRecord, AnalyticsData } from '../types';

export function useMovementData() {
  const [transactions] = useState<TransactionRecord[]>(MOCK_TRANSACTIONS);
  const [transfers] = useState<TransferRecord[]>(MOCK_TRANSFERS);
  const [adjustments] = useState<AdjustmentRecord[]>(MOCK_ADJUSTMENTS);
  const [goodsReceipts] = useState<GoodsReceiptRecord[]>(MOCK_GOODS_RECEIPTS);
  const [goodsIssues] = useState<GoodsIssueRecord[]>(MOCK_GOODS_ISSUES);
  const [timeline] = useState<MovementTimelineEvent[]>(MOCK_TIMELINE);
  const [auditRecords] = useState<AuditRecord[]>(MOCK_AUDIT);
  const [analytics] = useState<AnalyticsData>(MOCK_ANALYTICS);
  const [loading] = useState(false);
  return { transactions, transfers, adjustments, goodsReceipts, goodsIssues, timeline, auditRecords, analytics, loading };
}
