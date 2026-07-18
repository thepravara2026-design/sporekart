export interface Product {
  id: string;
  name: string;
  category: string;
  price: number;
  unit: string;
  inStock: boolean;
  image: string;
}

export const products: Product[] = [
  { id: 'PROD-001', name: 'Organic Wheat Seeds', category: 'Seeds', price: 249, unit: 'kg', inStock: true, image: '/images/seeds/wheat.jpg' },
  { id: 'PROD-002', name: 'Hybrid Rice Seeds', category: 'Seeds', price: 349, unit: 'kg', inStock: true, image: '/images/seeds/rice.jpg' },
  { id: 'PROD-003', name: 'NPK Fertilizer 20-20-20', category: 'Fertilizers', price: 599, unit: 'bag', inStock: true, image: '/images/fertilizers/npk.jpg' },
  { id: 'PROD-004', name: 'Organic Compost', category: 'Fertilizers', price: 399, unit: 'bag', inStock: true, image: '/images/fertilizers/compost.jpg' },
  { id: 'PROD-005', name: 'Neem Oil Pesticide', category: 'Pesticides', price: 449, unit: 'L', inStock: true, image: '/images/pesticides/neem.jpg' },
  { id: 'PROD-006', name: 'Drip Irrigation Kit', category: 'Equipment', price: 1299, unit: 'set', inStock: false, image: '/images/equipment/drip.jpg' },
  { id: 'PROD-007', name: 'Garden Trowel Set', category: 'Tools', price: 299, unit: 'set', inStock: true, image: '/images/tools/trowel.jpg' },
  { id: 'PROD-008', name: 'Pruning Shears', category: 'Tools', price: 449, unit: 'piece', inStock: true, image: '/images/tools/shears.jpg' },
];

export function getProductById(id: string): Product | undefined {
  return products.find((p) => p.id === id);
}
