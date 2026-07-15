import type { CurrencySetting } from '../types';

export const MOCK_CURRENCIES: CurrencySetting[] = [
  {
    code: 'INR',
    symbol: '₹',
    name: 'Indian Rupee',
    precision: 2,
    format: 'symbol amount',
    isDefault: true,
    exchangeRate: 1,
    status: 'active',
  },
  {
    code: 'USD',
    symbol: '$',
    name: 'US Dollar',
    precision: 2,
    format: 'symbol amount',
    isDefault: false,
    exchangeRate: 0.012,
    status: 'active',
  },
  {
    code: 'EUR',
    symbol: '€',
    name: 'Euro',
    precision: 2,
    format: 'symbol amount',
    isDefault: false,
    exchangeRate: 0.011,
    status: 'active',
  },
  {
    code: 'GBP',
    symbol: '£',
    name: 'British Pound',
    precision: 2,
    format: 'symbol amount',
    isDefault: false,
    exchangeRate: 0.0095,
    status: 'active',
  },
  {
    code: 'AED',
    symbol: 'د.إ',
    name: 'UAE Dirham',
    precision: 2,
    format: 'symbol amount',
    isDefault: false,
    exchangeRate: 0.044,
    status: 'active',
  },
  {
    code: 'SGD',
    symbol: 'S$',
    name: 'Singapore Dollar',
    precision: 2,
    format: 'symbol amount',
    isDefault: false,
    exchangeRate: 0.016,
    status: 'active',
  },
  {
    code: 'JPY',
    symbol: '¥',
    name: 'Japanese Yen',
    precision: 0,
    format: 'symbol amount',
    isDefault: false,
    exchangeRate: 1.8,
    status: 'inactive',
  },
];

export function getDefaultCurrency(): CurrencySetting {
  return MOCK_CURRENCIES.find((c) => c.isDefault)!;
}
