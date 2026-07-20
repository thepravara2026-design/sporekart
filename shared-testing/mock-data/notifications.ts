export interface Notification {
  id: string;
  userId: string;
  type: 'order' | 'training' | 'promotion' | 'alert';
  title: string;
  message: string;
  read: boolean;
  createdAt: string;
}

export const notifications: Notification[] = [
  {
    id: 'NOTIF-001',
    userId: 'CUST-001',
    type: 'order',
    title: 'Order Delivered',
    message: 'Your order ORD-001 has been delivered successfully.',
    read: true,
    createdAt: '2026-07-13T10:30:00Z',
  },
  {
    id: 'NOTIF-002',
    userId: 'CUST-001',
    type: 'order',
    title: 'Order Shipped',
    message: 'Your order ORD-003 has been shipped.',
    read: false,
    createdAt: '2026-07-16T14:00:00Z',
  },
  {
    id: 'NOTIF-003',
    userId: 'CUST-002',
    type: 'training',
    title: 'Course Completed',
    message: 'Congratulations! You completed Soil Health Management.',
    read: false,
    createdAt: '2026-07-15T09:00:00Z',
  },
];
