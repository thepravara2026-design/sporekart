import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const ReservedStockPage = memo(function ReservedStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['reserved'] }} title="Reserved Stock" description="Stock reserved for orders, transfers or production." />;
});
