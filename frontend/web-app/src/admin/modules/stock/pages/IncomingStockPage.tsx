import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const IncomingStockPage = memo(function IncomingStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['incoming'] }} title="Incoming Stock" description="Stock expected from purchase orders or transfers." />;
});
