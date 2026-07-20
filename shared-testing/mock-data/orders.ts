export interface OrderItem {
  productId: string;
  productName: string;
  quantity: number;
  price: number;
}

export interface Order {
  id: string;
  customerId: string;
  customerName: string;
  items: OrderItem[];
  total: number;
  status: 'pending' | 'confirmed' | 'shipped' | 'delivered' | 'cancelled';
  createdAt: string;
  address: string;
}

export const orders: Order[] = [
  {
    id: 'ORD-001',
    customerId: 'CUST-001',
    customerName: 'Ravi Sharma',
    items: [
      { productId: 'PROD-001', productName: 'Organic Wheat Seeds', quantity: 5, price: 249 },
      { productId: 'PROD-003', productName: 'NPK Fertilizer', quantity: 2, price: 599 },
    ],
    total: 2443,
    status: 'delivered',
    createdAt: '2026-07-10T10:30:00Z',
    address: '123 Green Field, Pune',
  },
  {
    id: 'ORD-002',
    customerId: 'CUST-002',
    customerName: 'Priya Patel',
    items: [
      { productId: 'PROD-005', productName: 'Neem Oil Pesticide', quantity: 1, price: 449 },
    ],
    total: 449,
    status: 'shipped',
    createdAt: '2026-07-14T14:00:00Z',
    address: '456 Garden Road, Mumbai',
  },
  {
    id: 'ORD-003',
    customerId: 'CUST-001',
    customerName: 'Ravi Sharma',
    items: [
      { productId: 'PROD-007', productName: 'Garden Trowel Set', quantity: 2, price: 299 },
      { productId: 'PROD-008', productName: 'Pruning Shears', quantity: 1, price: 449 },
    ],
    total: 1047,
    status: 'pending',
    createdAt: '2026-07-16T09:15:00Z',
    address: '123 Green Field, Pune',
  },
];

export function getOrderById(id: string): Order | undefined {
  return orders.find((o) => o.id === id);
}
