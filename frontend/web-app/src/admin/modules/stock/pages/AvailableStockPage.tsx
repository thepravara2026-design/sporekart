import { memo } from 'react';
import { StockRegistryPage } from './StockRegistryPage';

export const AvailableStockPage = memo(function AvailableStockPage() {
  return <StockRegistryPage defaultFilter={{ stockState: ['available'] }} title="Available Stock" description="Stock ready for use, sale or transfer. Filtered to show records with available quantities." />;
});
