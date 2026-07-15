import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const AllocatedStockPage = memo(function AllocatedStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['allocated'] }} title="Allocated Stock" description="Stock allocated to orders but not yet picked." />;
});
