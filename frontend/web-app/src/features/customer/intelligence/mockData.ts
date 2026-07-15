export interface GrowerProfile {
  name: string;
  tier: string;
  points: number;
  profileCompletion: number;
  verified: boolean;
  avatar: string;
}

export interface ActivityEvent {
  id: string;
  type: 'order' | 'training' | 'support' | 'wishlist' | 'loyalty';
  message: string;
  date: string;
  icon: string;
}

export interface AchievementBadge {
  id: string;
  title: string;
  description: string;
  icon: string;
  pointsReward: number;
  status: 'earned' | 'locked';
}

export interface RecommendedItem {
  id: string;
  name: string;
  price: string;
  description: string;
  category: 'spawn' | 'equipment' | 'lab';
  image: string;
}

export const PROFILE: GrowerProfile = {
  name: 'Jane Doe',
  tier: 'Master Grower',
  points: 2450,
  profileCompletion: 85,
  verified: true,
  avatar: '🍄'
};

export const ACTIVITY_LOG: ActivityEvent[] = [
  {
    id: 'act-1',
    type: 'order',
    message: 'Order #ORD-2026-8842 dispatched via Delhivery (in transit)',
    date: '2026-07-13 10:30 AM',
    icon: 'package'
  },
  {
    id: 'act-2',
    type: 'training',
    message: 'Marked lesson "Liquid Culture Inoculation" as Completed (100%)',
    date: '2026-07-13 09:15 AM',
    icon: 'book-open'
  },
  {
    id: 'act-3',
    type: 'support',
    message: 'Opened support ticket TKT-2026-4412 regarding logistics delays',
    date: '2026-07-12 04:00 PM',
    icon: 'message-circle'
  },
  {
    id: 'act-4',
    type: 'wishlist',
    message: 'Added "Premium Pink Oyster Spawn Bag" to your saved shelf',
    date: '2026-07-11 11:20 AM',
    icon: 'heart'
  },
  {
    id: 'act-5',
    type: 'loyalty',
    message: 'Earned 200 Loyalty Points for completing Sterile Techniques module',
    date: '2026-07-10 03:00 PM',
    icon: 'award'
  }
];

export const ACHIEVEMENTS: AchievementBadge[] = [
  {
    id: 'badge-1',
    title: 'Sterility Champion',
    description: 'Complete the Sterile Techniques course module with 100% scores.',
    icon: '🛡️',
    pointsReward: 500,
    status: 'earned'
  },
  {
    id: 'badge-2',
    title: 'Volume Cultivator',
    description: 'Purchase over 50kg of premium grain spawn bags.',
    icon: '📦',
    pointsReward: 1000,
    status: 'earned'
  },
  {
    id: 'badge-3',
    title: 'Webinar Veteran',
    description: 'Attend at least 3 live Q&A webinars with lead scientists.',
    icon: '🎙️',
    pointsReward: 300,
    status: 'locked'
  },
  {
    id: 'badge-4',
    title: 'Lab Founder',
    description: 'Set up custom cleanroom profiles and verify GSTIN tax details.',
    icon: '🧪',
    pointsReward: 400,
    status: 'locked'
  }
];

export const SUGGESTIONS: RecommendedItem[] = [
  {
    id: 'rec-1',
    name: 'Premium Pink Oyster Spawn Bag',
    price: '₹1,200',
    description: 'Aggressive colonizer, ideal for commercial straw beds.',
    category: 'spawn',
    image: '🌸'
  },
  {
    id: 'rec-2',
    name: 'Malt Extract Agar Powder (500g)',
    price: '₹2,400',
    description: 'Laboratory grade malt powder optimized for mycelium culture growth.',
    category: 'lab',
    image: '🧪'
  },
  {
    id: 'rec-3',
    name: 'Monotub Hydro-Mist Humidity Spray',
    price: '₹850',
    description: 'Continuous ultra-fine misting bottle to maintain casing moisture.',
    category: 'equipment',
    image: '💨'
  }
];

export const ANALYTICS_DATA = {
  monthlyYields: [
    { month: 'Mar', yieldKg: 120, targetKg: 100 },
    { month: 'Apr', yieldKg: 145, targetKg: 100 },
    { month: 'May', yieldKg: 180, targetKg: 150 },
    { month: 'Jun', yieldKg: 210, targetKg: 150 },
    { month: 'Jul', yieldKg: 245, targetKg: 200 }
  ],
  sterilitySuccessRate: 98.4,
  pointsRedemptionHistory: [
    { id: 'tx-1', reward: '₹500 Store Voucher', pointsSpent: 1000, date: '2026-07-01' }
  ]
};
