// Shipment domain models — provider-agnostic

export type ShipmentStatus =
  | 'pending'
  | 'confirmed'
  | 'picked_up'
  | 'in_transit'
  | 'out_for_delivery'
  | 'delivered'
  | 'failed'
  | 'cancelled'
  | 'returned';

export type ShipmentEventType =
  | 'created'
  | 'confirmed'
  | 'picked_up'
  | 'arrived_at_hub'
  | 'departed_hub'
  | 'out_for_delivery'
  | 'delivered'
  | 'failed'
  | 'exception'
  | 'returned';

export interface ShipmentAddress {
  line1: string;
  line2?: string;
  city: string;
  state: string;
  pincode: string;
  country: string;
  coordinates?: { lat: number; lng: number };
}

export interface ShipmentContact {
  name: string;
  phone: string;
  email?: string;
}

export interface ShipmentPackage {
  id: string;
  description: string;
  weight: number;
  weightUnit: 'kg' | 'g' | 'lb';
  length?: number;
  width?: number;
  height?: number;
  dimensionUnit?: 'cm' | 'in';
  value?: number;
  currency?: string;
}

export interface ShipmentTrackingEvent {
  id: string;
  type: ShipmentEventType;
  timestamp: string;
  location?: string;
  description: string;
  metadata?: Record<string, string>;
}

export interface Shipment {
  id: string;
  orderId: string;
  status: ShipmentStatus;
  provider: string;
  providerShipmentId?: string;
  trackingNumber?: string;
  trackingUrl?: string;
  origin: ShipmentAddress;
  destination: ShipmentAddress;
  sender: ShipmentContact;
  recipient: ShipmentContact;
  packages: ShipmentPackage[];
  events: ShipmentTrackingEvent[];
  estimatedDelivery?: string;
  actualDelivery?: string;
  createdAt: string;
  updatedAt: string;
  metadata?: Record<string, string>;
}

export interface ShipmentQuote {
  id: string;
  provider: string;
  serviceName: string;
  estimatedCost: number;
  currency: string;
  estimatedDays: number;
  guaranteed?: boolean;
  available: boolean;
}
