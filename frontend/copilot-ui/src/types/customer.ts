export interface ProductItem {
  id: string;
  name: string;
  description: string;
  category: string;
  price: number;
  currency: string;
  imageUrl?: string;
  stockLevel: number;
  isAvailable: boolean;
  rating: number;
  tags: string[];
}

export interface ProductRecommendation {
  product: ProductItem;
  score: number;
  reason: string;
  recommendationType: string;
}

export interface CustomerOrder {
  orderId: string;
  customerId: string;
  items: ShoppingCartItem[];
  status: string;
  totalAmount: number;
  currency: string;
  placedAt: string;
  estimatedDelivery?: string;
  trackingUrl?: string;
}

export interface TrainingCourse {
  id: string;
  title: string;
  description: string;
  category: string;
  level: string;
  duration: string;
  instructor: string;
  nextBatchDate?: string;
  enrollmentStatus: string;
}

export interface KnowledgeArticle {
  id: string;
  title: string;
  snippet: string;
  content: string;
  category: string;
  source: string;
  relevanceScore: number;
  url?: string;
}

export interface ShoppingCartItem {
  productId: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}
