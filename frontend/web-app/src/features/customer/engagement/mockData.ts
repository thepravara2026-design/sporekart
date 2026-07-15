export interface WishlistItem {
  id: string;
  name: string;
  price: number;
  originalPrice?: number;
  image: string;
  availability: 'In Stock' | 'Low Stock' | 'Out of Stock';
  rating: number;
  reviewsCount: number;
}

export interface SavedItem {
  id: string;
  name: string;
  price: number;
  originalPrice?: number;
  image: string;
  availability: string;
  alertMessage?: string;
}

export interface RecentlyViewedItem {
  id: string;
  name: string;
  price: number;
  image: string;
  category: string;
  lastViewed: string;
}

export interface RecommendedItem {
  id: string;
  name: string;
  price: number;
  image: string;
  tag: 'Recommended' | 'Trending' | 'Popular Nearby' | 'Frequently Bought' | 'New' | 'Seasonal';
}

export interface EngagementNotification {
  id: string;
  type: 'order' | 'training' | 'update' | 'price' | 'promo' | 'system';
  title: string;
  description: string;
  timestamp: string;
  read: boolean;
}

export interface Achievement {
  id: string;
  title: string;
  description: string;
  icon: string;
  unlockedAt: string;
}

export interface RewardCoupon {
  id: string;
  code: string;
  description: string;
  pointsCost: number;
}

export const WISHLIST_ITEMS: WishlistItem[] = [
  {
    id: 'w-1',
    name: 'Pink Oyster Mushroom Spore Syringe (10ml)',
    price: 800.00,
    image: '💉',
    availability: 'In Stock',
    rating: 4.8,
    reviewsCount: 34,
  },
  {
    id: 'w-2',
    name: 'Golden Oyster Spawn Plug Logs (Pack of 100)',
    price: 950.00,
    originalPrice: 1100.00,
    image: '🪵',
    availability: 'Low Stock',
    rating: 4.6,
    reviewsCount: 19,
  },
  {
    id: 'w-3',
    name: 'SporeKart Enterprise Mushroom Grow Tent',
    price: 3200.00,
    image: '🎪',
    availability: 'Out of Stock',
    rating: 4.9,
    reviewsCount: 42,
  },
];

export const SAVED_ITEMS: SavedItem[] = [
  {
    id: 's-1',
    name: 'Premium Autoclave Indicator Tape',
    price: 300.00,
    originalPrice: 350.00,
    image: '🎗️',
    availability: 'In Stock',
    alertMessage: 'Price dropped by ₹50 since you saved it!',
  },
  {
    id: 's-2',
    name: 'High-Fine Spray Mister (500ml)',
    price: 600.00,
    image: '💦',
    availability: 'Low Stock',
    alertMessage: 'Only 3 items remaining in stock!',
  },
];

export const RECENTLY_VIEWED: RecentlyViewedItem[] = [
  {
    id: 'r-1',
    name: 'MEA Pre-poured Agar Plates (Pack of 20)',
    price: 1100.00,
    image: '🧫',
    category: 'Lab Equipment',
    lastViewed: '2 hours ago',
  },
  {
    id: 'r-2',
    name: "Lion's Mane Mushroom Grow Kit",
    price: 1450.00,
    image: '🦁',
    category: 'Grow Kits',
    lastViewed: 'Yesterday',
  },
  {
    id: 'r-3',
    name: 'Blue Oyster Spawn Bag (2kg)',
    price: 1100.00,
    image: '🍄',
    category: 'Grain Spawn',
    lastViewed: '3 days ago',
  },
];

export const RECOMMENDATIONS: RecommendedItem[] = [
  {
    id: 'rec-1',
    name: 'Liquid Culture Lids (Pack of 4)',
    price: 450.00,
    image: '🔩',
    tag: 'Recommended',
  },
  {
    id: 'rec-2',
    name: 'Sterile Scalpel Blades (Pack of 10)',
    price: 250.00,
    image: '🔪',
    tag: 'Frequently Bought',
  },
  {
    id: 'rec-3',
    name: 'Shiitake Sawdust Plug Spawn',
    price: 1200.00,
    image: '🍄',
    tag: 'Trending',
  },
  {
    id: 'rec-4',
    name: 'Coco Coir Block Grow Medium',
    price: 350.00,
    image: '🥥',
    tag: 'Popular Nearby',
  },
  {
    id: 'rec-5',
    name: 'SporeKart Lab Grade Alcohol Wipes',
    price: 200.00,
    image: '🧻',
    tag: 'New',
  },
  {
    id: 'rec-6',
    name: 'Monsoon Grow Heater Mat',
    price: 850.00,
    image: '🔌',
    tag: 'Seasonal',
  },
];

export const NOTIFICATIONS: EngagementNotification[] = [
  {
    id: 'n-1',
    type: 'order',
    title: 'Order #ORD-2026-8842 Dispatched',
    description: 'Delhivery courier has picked up your Pink Oyster Grain Spawn.',
    timestamp: '2 hours ago',
    read: false,
  },
  {
    id: 'n-2',
    type: 'price',
    title: 'Price Drop Alert',
    description: 'Premium Autoclave Indicator Tape in Saved items dropped by ₹50!',
    timestamp: '5 hours ago',
    read: false,
  },
  {
    id: 'n-3',
    type: 'training',
    title: 'Advanced Sterility Certificate Unlocked',
    description: 'You completed Sterile Techniques 101. Claim your badge!',
    timestamp: 'Yesterday',
    read: true,
  },
  {
    id: 'n-4',
    type: 'promo',
    title: 'Monsoon Spore Growers Sale Live',
    description: 'Get up to 25% off on all liquid cultures. Use coupon MONSOON25.',
    timestamp: '2 days ago',
    read: true,
  },
  {
    id: 'n-5',
    type: 'system',
    title: 'Scheduled System Maintenance',
    description: 'The platform checkout APIs will undergo updates on Sunday at 2 AM IST.',
    timestamp: '3 days ago',
    read: true,
  },
];

export const ACHIEVEMENTS: Achievement[] = [
  {
    id: 'a-1',
    title: 'Cleanroom Pioneer',
    description: 'Logged first sterile lab inoculation.',
    icon: '🧫',
    unlockedAt: '2026-05-18',
  },
  {
    id: 'a-2',
    title: 'First Harvest',
    description: 'Harvested 1kg of fresh Pink Oyster mushrooms.',
    icon: '🍄',
    unlockedAt: '2026-06-28',
  },
  {
    id: 'a-3',
    title: 'Sterility Guru',
    description: 'Completed 3 sterile lab modules with 100% scores.',
    icon: '🎓',
    unlockedAt: '2026-07-02',
  },
];

export const REWARDS_CATALOG: RewardCoupon[] = [
  { id: 'c-1', code: 'CUP-GROW-100', description: '₹100 Off Coupon on Spawn Products', pointsCost: 500 },
  { id: 'c-2', code: 'CUP-FREE-SHIP', description: 'Free Express Shipping Code', pointsCost: 300 },
  { id: 'c-3', code: 'CUP-MISTER-FREE', description: 'Free 300ml Spray Mister Item', pointsCost: 700 },
];
