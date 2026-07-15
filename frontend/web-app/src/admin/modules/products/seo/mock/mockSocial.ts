import type { SocialProfileLink } from '../types';

export const MOCK_SOCIAL_PROFILES: SocialProfileLink[] = [
  { platform: 'instagram', url: 'https://instagram.com/sporekart', icon: '📸', label: 'Instagram', enabled: true },
  { platform: 'facebook', url: 'https://facebook.com/sporekart', icon: '👍', label: 'Facebook', enabled: true },
  { platform: 'twitter', url: 'https://twitter.com/sporekart', icon: '🐦', label: 'X (Twitter)', enabled: true },
  { platform: 'linkedin', url: 'https://linkedin.com/company/sporekart', icon: '💼', label: 'LinkedIn', enabled: true },
  { platform: 'youtube', url: 'https://youtube.com/@sporekart', icon: '▶️', label: 'YouTube', enabled: false },
  { platform: 'pinterest', url: 'https://pinterest.com/sporekart', icon: '📌', label: 'Pinterest', enabled: false },
];

export const MOCK_SOCIAL_ACTIVITY = [
  { id: 'soc-001', platform: 'instagram', action: 'Connected Instagram business account', user: 'Marketing Manager', timestamp: '2026-07-10T10:00:00Z' },
  { id: 'soc-002', platform: 'instagram', action: 'Posted product story for Fresh Organic Tomatoes', user: 'Marketing Manager', timestamp: '2026-07-11T14:00:00Z' },
  { id: 'soc-003', platform: 'facebook', action: 'Scheduled Facebook Shop sync', user: 'Marketing Manager', timestamp: '2026-07-12T09:00:00Z' },
  { id: 'soc-004', platform: 'twitter', action: 'Updated Twitter card for Premium Basmati Rice', user: 'SEO Manager', timestamp: '2026-07-13T11:00:00Z' },
  { id: 'soc-005', platform: 'linkedin', action: 'Shared product update on LinkedIn page', user: 'Marketing Manager', timestamp: '2026-07-14T08:00:00Z' },
];
