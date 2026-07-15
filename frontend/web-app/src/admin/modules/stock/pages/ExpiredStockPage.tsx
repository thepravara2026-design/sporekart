import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const ExpiredStockPage = memo(function ExpiredStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['expired'] }} title="Expired Stock" description="Stock past its expiry date. Flagged for disposal or return processing." />;
});
