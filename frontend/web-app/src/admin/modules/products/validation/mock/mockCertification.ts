import type { Certification } from '../types';

export const MOCK_CERTIFICATIONS: Certification[] = [
  { id: 'cert-001', productId: 'prod-001', productName: 'Fresh Organic Tomatoes', level: 'gold', badge: '🥇', completionDate: '2026-06-15', notes: 'Organic certification verified. All compliance checks passed.', validUntil: '2027-06-15', issuedBy: 'Quality Assurance Team' },
  { id: 'cert-002', productId: 'prod-003', productName: 'Premium Basmati Rice 5kg', level: 'enterprise', badge: '🏆', completionDate: '2026-06-20', notes: 'Enterprise certified. Meets all marketplace requirements.', validUntil: '2027-06-20', issuedBy: 'Enterprise Governance' },
  { id: 'cert-003', productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit', level: 'silver', badge: '🥈', completionDate: '2026-07-01', notes: 'Silver certification. Minor SEO improvements recommended.', validUntil: '2027-07-01', issuedBy: 'QA Team' },
  { id: 'cert-004', productId: 'prod-009', productName: 'Hydroponic Starter Kit', level: 'bronze', badge: '🥉', completionDate: '2026-07-05', notes: 'Bronze certification. Multiple areas need improvement before marketplace publishing.', validUntil: '2027-01-05', issuedBy: 'QA Team' },
  { id: 'cert-005', productId: 'prod-015', productName: 'Button Mushroom', level: 'silver', badge: '🥈', completionDate: '2026-06-28', notes: 'Silver certification. SEO metadata needs optimization.', validUntil: '2027-06-28', issuedBy: 'QA Team' },
  { id: 'cert-006', productId: 'prod-007', productName: 'Organic Compost 25kg', level: 'gold', badge: '🥇', completionDate: '2026-07-10', notes: 'Gold certification. Ready for all sales channels.', validUntil: '2027-07-10', issuedBy: 'Quality Assurance Team' },
  { id: 'cert-007', productId: 'prod-008', productName: 'Advanced Shiitake Log Kit', level: 'gold', badge: '🥇', completionDate: '2026-06-25', notes: 'Gold certification. Excellent compliance and quality scores.', validUntil: '2027-06-25', issuedBy: 'Quality Assurance Team' },
  { id: 'cert-008', productId: 'prod-010', productName: 'Home Cultivation Masterclass', level: 'enterprise', badge: '🏆', completionDate: '2026-07-08', notes: 'Enterprise certified. Top-tier validation across all categories.', validUntil: '2027-07-08', issuedBy: 'Enterprise Governance' },
];
