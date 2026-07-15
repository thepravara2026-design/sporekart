import type { ComplianceResult, ComplianceCheck } from '../types';

function makeChecks(_productId: string, baseScore: number): ComplianceCheck[] {
  const pass = (id: string, label: string, cat: ComplianceCheck['category']) => ({ id, label, category: cat, status: 'pass' as const, message: `${label} verified` });
  const fail = (id: string, label: string, cat: ComplianceCheck['category'], msg: string) => ({ id, label, category: cat, status: 'fail' as const, message: msg });
  const warn = (id: string, label: string, cat: ComplianceCheck['category'], msg: string) => ({ id, label, category: cat, status: 'warning' as const, message: msg });
  const na = (id: string, label: string, cat: ComplianceCheck['category']) => ({ id, label, category: cat, status: 'na' as const, message: 'Not applicable' });

  const checks: ComplianceCheck[] = [
    pass('cl-001', 'Product Label', 'label'),
    pass('cl-002', 'Manufacturer', 'manufacturer'),
    pass('cl-003', 'Country of Origin', 'country'),
    baseScore >= 70 ? pass('cl-004', 'MRP', 'mrp') : warn('cl-004', 'MRP', 'mrp', 'MRP not clearly displayed'),
    baseScore >= 60 ? pass('cl-005', 'GST', 'gst') : fail('cl-005', 'GST', 'gst', 'GST rate not configured'),
    baseScore >= 50 ? pass('cl-006', 'HSN', 'hsn') : fail('cl-006', 'HSN', 'hsn', 'HSN code missing'),
    na('cl-007', 'Expiry Date', 'expiry'),
    na('cl-008', 'Batch Number', 'batch'),
    pass('cl-009', 'Legal Notices', 'legal'),
    baseScore >= 65 ? pass('cl-010', 'Consumer Info', 'consumer') : warn('cl-010', 'Consumer Info', 'consumer', 'Consumer information incomplete'),
    baseScore >= 60 ? pass('cl-011', 'Agri Requirements', 'agriculture') : warn('cl-011', 'Agri Requirements', 'agriculture', 'Agricultural compliance needs review'),
    na('cl-012', 'Food Safety', 'food_safety'),
    na('cl-013', 'FSSAI', 'fssai'),
    na('cl-014', 'Agri Compliance', 'agri'),
  ];
  return checks;
}

export const MOCK_COMPLIANCE_RESULTS: ComplianceResult[] = [
  { productId: 'prod-001', productName: 'Fresh Organic Tomatoes', overallScore: 85, checks: makeChecks('prod-001', 85), requiredApprovals: ['FSSAI (Future)', 'Agri Marketing (Future)'], warnings: ['Consumer information section could be more detailed'] },
  { productId: 'prod-003', productName: 'Premium Basmati Rice 5kg', overallScore: 90, checks: makeChecks('prod-003', 90), requiredApprovals: ['FSSAI (Future)', 'BIS (Future)'], warnings: [] },
  { productId: 'prod-005', productName: 'Oyster Mushroom Grow Kit', overallScore: 75, checks: makeChecks('prod-005', 75), requiredApprovals: ['Agri Marketing (Future)'], warnings: ['MRP label not prominent', 'GST category may need confirmation'] },
  { productId: 'prod-009', productName: 'Hydroponic Starter Kit', overallScore: 60, checks: makeChecks('prod-009', 60), requiredApprovals: ['BIS (Future)', 'Agri Marketing (Future)'], warnings: ['GST rate not configured', 'HSN code missing', 'MRP not clearly displayed'] },
  { productId: 'prod-015', productName: 'Button Mushroom', overallScore: 70, checks: makeChecks('prod-015', 70), requiredApprovals: ['FSSAI (Future)'], warnings: ['Consumer information incomplete'] },
  { productId: 'prod-006', productName: 'Shiitake Spawn Bags 10pk', overallScore: 50, checks: makeChecks('prod-006', 50), requiredApprovals: ['Agri Marketing (Future)', 'Export License (Future)'], warnings: ['Multiple compliance gaps identified', 'GST and HSN not configured'] },
  { productId: 'prod-007', productName: 'Organic Compost 25kg', overallScore: 80, checks: makeChecks('prod-007', 80), requiredApprovals: ['Agri Marketing (Future)', 'FSSAI (Future)'], warnings: [] },
];
