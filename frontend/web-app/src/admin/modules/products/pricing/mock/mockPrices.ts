import type { PricingEntity, PriceEntry, PriceTier } from '../types';

const products = [
  { id: 'prod-001', name: 'Fresh Organic Tomatoes', sku: 'SKU-TOMATO-001', category: 'Fresh Produce', categoryId: 'cat-fresh', brand: 'GreenLeaf Farms', brandId: 'brand-001' },
  { id: 'prod-002', name: 'Organic Red Chillies', sku: 'SKU-CHILLI-002', category: 'Fresh Produce', categoryId: 'cat-fresh', brand: 'GreenLeaf Farms', brandId: 'brand-001' },
  { id: 'prod-003', name: 'Premium Basmati Rice 5kg', sku: 'SKU-RICE-003', category: 'Dried Goods', categoryId: 'cat-dried', brand: 'Royal Harvest', brandId: 'brand-002' },
  { id: 'prod-004', name: 'Organic Moong Dal 1kg', sku: 'SKU-DAL-004', category: 'Dried Goods', categoryId: 'cat-dried', brand: 'Royal Harvest', brandId: 'brand-002' },
  { id: 'prod-005', name: 'Oyster Mushroom Grow Kit', sku: 'SKU-MUSH-005', category: 'Spawn & Culture', categoryId: 'cat-spawn', brand: 'MycoPrime', brandId: 'brand-003' },
  { id: 'prod-006', name: 'Shiitake Spawn Bags 10pk', sku: 'SKU-SPAWN-006', category: 'Spawn & Culture', categoryId: 'cat-spawn', brand: 'MycoPrime', brandId: 'brand-003' },
  { id: 'prod-007', name: 'Organic Compost 25kg', sku: 'SKU-COMP-007', category: 'Value Added', categoryId: 'cat-value', brand: 'EcoGrow Solutions', brandId: 'brand-004' },
  { id: 'prod-008', name: 'Coco Peat Block 5kg', sku: 'SKU-COCO-008', category: 'Value Added', categoryId: 'cat-value', brand: 'EcoGrow Solutions', brandId: 'brand-004' },
  { id: 'prod-009', name: 'Hydroponic Starter Kit', sku: 'SKU-HYDRO-009', category: 'Training', categoryId: 'cat-training', brand: 'AgriGuru', brandId: 'brand-007' },
  { id: 'prod-010', name: 'Mushroom Farming Guide Book', sku: 'SKU-BOOK-010', category: 'Training', categoryId: 'cat-training', brand: 'AgriGuru', brandId: 'brand-007' },
  { id: 'prod-011', name: 'Organic Vermicompost 10kg', sku: 'SKU-VERMI-011', category: 'Value Added', categoryId: 'cat-value', brand: 'EcoGrow Solutions', brandId: 'brand-004' },
  { id: 'prod-012', name: 'Neem Oil Spray 500ml', sku: 'SKU-NEEM-012', category: 'Value Added', categoryId: 'cat-value', brand: 'AgriFresh', brandId: 'brand-006' },
  { id: 'prod-013', name: 'Dried Red Kidney Beans 2kg', sku: 'SKU-BEANS-013', category: 'Dried Goods', categoryId: 'cat-dried', brand: 'Royal Harvest', brandId: 'brand-002' },
  { id: 'prod-014', name: 'Fresh Coriander Bunch', sku: 'SKU-CORI-014', category: 'Fresh Produce', categoryId: 'cat-fresh', brand: 'GreenLeaf Farms', brandId: 'brand-001' },
  { id: 'prod-015', name: 'Button Mushroom 500g', sku: 'SKU-BUTTON-015', category: 'Fresh Produce', categoryId: 'cat-fresh', brand: 'MycoPrime', brandId: 'brand-003' },
];

const tiers: PriceTier[] = ['mrp', 'selling', 'wholesale', 'distributor', 'dealer', 'retail', 'farmer', 'training', 'bundle'];

function generatePrices(basePrice: number): PriceEntry[] {
  return tiers.map((tier) => {
    const multipliers: Record<PriceTier, number> = {
      mrp: 1.0,
      selling: 0.85,
      wholesale: 0.70,
      distributor: 0.60,
      dealer: 0.65,
      retail: 0.90,
      farmer: 0.55,
      training: 0.50,
      bundle: 0.75,
    };
    return {
      tier,
      amount: Math.round(basePrice * multipliers[tier] * 100) / 100,
      currency: 'INR',
    };
  });
}

const basePrices = [40, 120, 850, 180, 650, 1200, 350, 280, 2500, 450, 420, 320, 380, 25, 160];

export const MOCK_PRICING_ENTITIES: PricingEntity[] = products.map((p, i) => ({
  id: `price-${p.id}`,
  productId: p.id,
  productName: p.name,
  sku: p.sku,
  category: p.category,
  categoryId: p.categoryId,
  brand: p.brand,
  brandId: p.brandId,
  image: undefined,
  prices: generatePrices(basePrices[i]),
  gstPercentage: [0, 5, 12, 18, 28][i % 5],
  gstCategory: ['Nil', 'Food', 'Food', 'Standard', 'Luxury'][i % 5],
  hsnCode: ['0702', '0904', '1006', '0713', '0602', '0602', '3101', '1404', '8479', '4901', '3101', '3808', '0713', '0709', '0709'][i % 15],
  status: i < 12 ? 'active' : i === 12 ? 'draft' : i === 13 ? 'pending' : 'archived',
  approved: i < 10 || i === 13,
  updatedAt: new Date(Date.now() - i * 86400000).toISOString(),
  updatedBy: ['Admin User', 'Pricing Manager', 'Finance Team', 'System'][i % 4],
  notes: i % 3 === 0 ? `Base pricing for ${p.name}` : undefined,
}));

export function getPriceForTier(entity: PricingEntity, tier: PriceTier): number {
  return entity.prices.find((p) => p.tier === tier)?.amount ?? 0;
}

export function getDiscountPercent(entity: PricingEntity): number {
  const mrp = getPriceForTier(entity, 'mrp');
  const selling = getPriceForTier(entity, 'selling');
  if (mrp === 0) return 0;
  return Math.round(((mrp - selling) / mrp) * 100);
}

export function getGstAmount(amount: number, gstPercent: number): number {
  return Math.round((amount * gstPercent) / 100 * 100) / 100;
}

export function getFinalPrice(entity: PricingEntity): number {
  const selling = getPriceForTier(entity, 'selling');
  const gst = getGstAmount(selling, entity.gstPercentage);
  return Math.round((selling + gst) * 100) / 100;
}
