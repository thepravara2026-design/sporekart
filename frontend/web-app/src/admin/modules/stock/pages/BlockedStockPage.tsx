import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const BlockedStockPage = memo(function BlockedStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['blocked'] }} title="Blocked Stock" description="Stock blocked from operations due to quality, compliance or legal holds." />;
});
