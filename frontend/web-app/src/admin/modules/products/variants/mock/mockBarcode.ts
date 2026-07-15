import type { BarcodeEntry } from '../types';

export const MOCK_BARCODES: BarcodeEntry[] = [
  { id: 'bc-001', sku: 'FR-MSH-200', variantId: 'var-001', barcode: '8901234567890', qrCode: 'qr-fr-msh-200', type: 'ean13', status: 'generated' },
  { id: 'bc-002', sku: 'FR-MSH-400', variantId: 'var-002', barcode: '8901234567891', qrCode: 'qr-fr-msh-400', type: 'ean13', status: 'generated' },
  { id: 'bc-003', sku: 'FR-MSH-1KG', variantId: 'var-003', barcode: '8901234567892', qrCode: 'qr-fr-msh-1kg', type: 'ean13', status: 'generated' },
  { id: 'bc-004', sku: 'DR-SHT-100', variantId: 'var-004', barcode: '8901234567893', qrCode: 'qr-dr-sht-100', type: 'ean13', status: 'generated' },
  { id: 'bc-005', sku: 'KIT-HYD-BEG', variantId: 'var-010', barcode: '8901234567894', qrCode: 'qr-kit-hyd-beg', type: 'code128', status: 'generated' },
  { id: 'bc-006', sku: 'KIT-HYD-PRM', variantId: 'var-012', barcode: undefined, qrCode: undefined, type: 'qr', status: 'pending' },
];
