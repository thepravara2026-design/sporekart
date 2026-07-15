import type { MarketplaceItem } from '../types';

export interface MarketplaceConfig {
  id: string;
  name: string;
  icon: string;
  url: string;
  color: string;
}

export const MARKETPLACE_CONFIGS: MarketplaceConfig[] = [
  { id: 'amazon', name: 'Amazon', icon: '🛒', url: 'https://sellercentral.amazon.in', color: '#FF9900' },
  { id: 'flipkart', name: 'Flipkart', icon: '🛍️', url: 'https://seller.flipkart.com', color: '#2874F0' },
  { id: 'agriBegri', name: 'AgriBegri', icon: '🌾', url: 'https://agribegri.com', color: '#4CAF50' },
  { id: 'indiaMART', name: 'IndiaMART', icon: '🏪', url: 'https://seller.indiamart.com', color: '#E65100' },
  { id: 'googleShopping', name: 'Google Shopping', icon: '🔍', url: 'https://merchant.google.com', color: '#4285F4' },
  { id: 'export', name: 'Export Ready', icon: '🌍', url: '#', color: '#9C27B0' },
];

export function getMarketplaceScore(items: MarketplaceItem[]): number {
  if (items.length === 0) return 0;
  return Math.round(items.reduce((sum, i) => sum + i.score, 0) / items.length);
}
