import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const DamagedStockPage = memo(function DamagedStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['damaged'] }} title="Damaged Stock" description="Stock flagged as damaged. Review and process for write-off or return." />;
});
