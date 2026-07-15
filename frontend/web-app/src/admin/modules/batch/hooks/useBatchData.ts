import { useState } from 'react';
import { MOCK_BATCHES, MOCK_LOTS, MOCK_TIMELINE, MOCK_ANALYTICS } from '../services/batchMockService';
import type { BatchRecord, LotRecord, BatchTimelineEvent, AnalyticsMetric } from '../types';

export function useBatchData() {
  const [batches] = useState<BatchRecord[]>(MOCK_BATCHES);
  const [lots] = useState<LotRecord[]>(MOCK_LOTS);
  const [timeline] = useState<BatchTimelineEvent[]>(MOCK_TIMELINE);
  const [analytics] = useState<AnalyticsMetric>(MOCK_ANALYTICS);
  const [loading] = useState(false);
  return { batches, lots, timeline, analytics, loading };
}

