export interface OrderItem {
  id: string;
  name: string;
  quantity: number;
  price: number;
  image: string;
  sku: string;
}

export interface TrackingMilestone {
  status: string;
  detail: string;
  timestamp: string;
  completed: boolean;
}

export interface RefundMilestone {
  status: string;
  detail: string;
  timestamp: string;
  completed: boolean;
}

export interface Order {
  id: string;
  date: string;
  status: 'Processing' | 'Dispatched' | 'In Transit' | 'Out for Delivery' | 'Delivered' | 'Cancelled' | 'Returned' | 'Refunded';
  total: number;
  subtotal: number;
  tax: number;
  shipping: number;
  discount: number;
  couponCode?: string;
  paymentMethod: string;
  paymentStatus: 'Paid' | 'Pending' | 'Refunded' | 'Failed';
  transactionId: string;
  paymentDate: string;
  estimatedDelivery: string;
  courierName: string;
  trackingNumber: string;
  shippingAddress: {
    name: string;
    line1: string;
    line2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    phone: string;
  };
  billingAddress: {
    name: string;
    line1: string;
    line2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    phone: string;
  };
  items: OrderItem[];
  milestones: TrackingMilestone[];
  refundStatus?: 'Refund Initiated' | 'Approved' | 'Credited';
  refundMilestones?: RefundMilestone[];
  returnEligible: boolean;
  returnReason?: string;
  returnNotes?: string;
}

export const MOCK_ORDERS: Order[] = [
  {
    id: 'ORD-2026-8842',
    date: '2026-07-11',
    status: 'In Transit',
    total: 3450.00,
    subtotal: 3200.00,
    tax: 150.00,
    shipping: 200.00,
    discount: 100.00,
    couponCode: 'SPOREGROW100',
    paymentMethod: 'UPI (Razorpay)',
    paymentStatus: 'Paid',
    transactionId: 'TXN-99884210398',
    paymentDate: '2026-07-11 14:32:01',
    estimatedDelivery: '2026-07-14',
    courierName: 'Delhivery',
    trackingNumber: 'DEL-2026-8842099',
    shippingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    billingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    items: [
      {
        id: 'item-1',
        name: 'Pink Oyster Mushroom Grain Spawn (2kg)',
        quantity: 2,
        price: 1100.00,
        image: '🍄',
        sku: 'SKU-PO-GRN-2KG',
      },
      {
        id: 'item-2',
        name: 'Autoclavable Spawn Bags (Pack of 10)',
        quantity: 1,
        price: 1000.00,
        image: '🛍️',
        sku: 'SKU-BAG-ACV-10P',
      },
    ],
    milestones: [
      { status: 'Order Created', detail: 'Order received and verified.', timestamp: '2026-07-11 14:30', completed: true },
      { status: 'Payment Received', detail: 'Payment of ₹3,450.00 processed successfully via UPI.', timestamp: '2026-07-11 14:32', completed: true },
      { status: 'Processing', detail: 'Spawn batch checked, bags sterilized and prepared.', timestamp: '2026-07-12 09:15', completed: true },
      { status: 'Quality Check', detail: 'Passed visual inspection for contamination & moisture level.', timestamp: '2026-07-12 11:30', completed: true },
      { status: 'Dispatched', detail: 'Handed over to courier partner Delhivery.', timestamp: '2026-07-12 15:45', completed: true },
      { status: 'In Transit', detail: 'Shipment has departed sorting facility in Bengaluru Hub.', timestamp: '2026-07-13 18:20', completed: true },
      { status: 'Out for Delivery', detail: 'Courier will deliver to HSR Layout area.', timestamp: '--', completed: false },
      { status: 'Delivered', detail: 'Secure OTP-based delivery to recipient.', timestamp: '--', completed: false },
    ],
    returnEligible: false,
  },
  {
    id: 'ORD-2026-7715',
    date: '2026-06-25',
    status: 'Delivered',
    total: 2150.00,
    subtotal: 1950.00,
    tax: 100.00,
    shipping: 100.00,
    discount: 0.00,
    paymentMethod: 'Credit Card (Visa)',
    paymentStatus: 'Paid',
    transactionId: 'TXN-7715092109',
    paymentDate: '2026-06-25 10:15:33',
    estimatedDelivery: '2026-06-28',
    courierName: 'Shiprocket (Delhivery)',
    trackingNumber: 'SR-771508920',
    shippingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    billingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    items: [
      {
        id: 'item-3',
        name: "Lion's Mane Mushroom Grow Kit",
        quantity: 1,
        price: 1450.00,
        image: '🦁',
        sku: 'SKU-LM-KIT-STD',
      },
      {
        id: 'item-4',
        name: 'Mushroom Spray Mister (300ml)',
        quantity: 1,
        price: 500.00,
        image: '💦',
        sku: 'SKU-MIS-300ML',
      },
    ],
    milestones: [
      { status: 'Order Created', detail: 'Order received.', timestamp: '2026-06-25 10:12', completed: true },
      { status: 'Payment Received', detail: 'Payment completed successfully.', timestamp: '2026-06-25 10:15', completed: true },
      { status: 'Processing', detail: 'Item packed and QA passed.', timestamp: '2026-06-25 17:00', completed: true },
      { status: 'Dispatched', detail: 'Shipped from hub.', timestamp: '2026-06-26 11:00', completed: true },
      { status: 'In Transit', detail: 'Arrived at local hub.', timestamp: '2026-06-27 09:30', completed: true },
      { status: 'Out for Delivery', detail: 'Out for delivery via agent Ramesh.', timestamp: '2026-06-28 10:00', completed: true },
      { status: 'Delivered', detail: 'Delivered at reception, signed by Jane.', timestamp: '2026-06-28 13:42', completed: true },
    ],
    returnEligible: true,
  },
  {
    id: 'ORD-2026-5541',
    date: '2026-05-18',
    status: 'Refunded',
    total: 1200.00,
    subtotal: 1100.00,
    tax: 50.00,
    shipping: 50.00,
    discount: 0.00,
    paymentMethod: 'UPI (GPay)',
    paymentStatus: 'Refunded',
    transactionId: 'TXN-5541992019',
    paymentDate: '2026-05-18 19:42:11',
    estimatedDelivery: '2026-05-21',
    courierName: 'Delhivery',
    trackingNumber: 'DEL-55419201',
    shippingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    billingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    items: [
      {
        id: 'item-5',
        name: 'Pre-poured MEA Agar Plates (Pack of 20)',
        quantity: 1,
        price: 1100.00,
        image: '🧫',
        sku: 'SKU-AGR-MEA-20P',
      },
    ],
    milestones: [
      { status: 'Order Created', detail: 'Order received.', timestamp: '2026-05-18 19:40', completed: true },
      { status: 'Payment Received', detail: 'Payment completed.', timestamp: '2026-05-18 19:42', completed: true },
      { status: 'Delivered', detail: 'Delivered to resident.', timestamp: '2026-05-21 14:00', completed: true },
      { status: 'Returned', detail: 'Return requested: Contaminated agar plates on arrival.', timestamp: '2026-05-22 10:15', completed: true },
      { status: 'Refunded', detail: 'Refund processed to original source.', timestamp: '2026-05-24 16:30', completed: true },
    ],
    refundStatus: 'Credited',
    refundMilestones: [
      { status: 'Refund Initiated', detail: 'Refund request registered for ₹1,200.00.', timestamp: '2026-05-22 10:15', completed: true },
      { status: 'Approved', detail: 'Returned photos checked; refund approved by support.', timestamp: '2026-05-23 11:30', completed: true },
      { status: 'Credited', detail: 'Refund successfully credited to bank account via Razorpay.', timestamp: '2026-05-24 16:30', completed: true },
    ],
    returnEligible: false,
    returnReason: 'Contamination',
    returnNotes: '3 out of 20 agar plates showed mold growth on arrival. Provided photo verification.',
  },
  {
    id: 'ORD-2026-9922',
    date: '2026-07-13',
    status: 'Processing',
    total: 4800.00,
    subtotal: 4500.00,
    tax: 200.00,
    shipping: 100.00,
    discount: 0.00,
    paymentMethod: 'Netbanking',
    paymentStatus: 'Paid',
    transactionId: 'TXN-992209180',
    paymentDate: '2026-07-13 11:00:00',
    estimatedDelivery: '2026-07-16',
    courierName: 'Delhivery',
    trackingNumber: 'PENDING',
    shippingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    billingAddress: {
      name: 'Jane Doe',
      line1: 'Flat 402, Green Meadows',
      line2: 'HSR Layout, Sector 3',
      city: 'Bengaluru',
      state: 'Karnataka',
      postalCode: '560102',
      country: 'India',
      phone: '+91 98765 43210',
    },
    items: [
      {
        id: 'item-6',
        name: 'Premium Cultivar Starter Kit',
        quantity: 1,
        price: 4500.00,
        image: '🍄',
        sku: 'SKU-KIT-PRM-STR',
      },
    ],
    milestones: [
      { status: 'Order Created', detail: 'Order received.', timestamp: '2026-07-13 10:55', completed: true },
      { status: 'Payment Received', detail: 'Payment verified.', timestamp: '2026-07-13 11:00', completed: true },
      { status: 'Processing', detail: 'Spawn inoculations selected from clean room.', timestamp: '2026-07-13 16:30', completed: true },
      { status: 'Dispatched', detail: 'Label generated. Waiting for courier pickup.', timestamp: '--', completed: false },
    ],
    returnEligible: false,
  },
];
