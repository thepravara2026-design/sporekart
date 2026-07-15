import type { Shipment, ShipmentQuote, ShipmentAddress, ShipmentPackage } from './types';

export interface ShipmentProviderConfig {
  apiKey?: string;
  apiSecret?: string;
  baseUrl?: string;
  webhookSecret?: string;
  settings?: Record<string, unknown>;
}

export interface CreateShipmentInput {
  orderId: string;
  origin: ShipmentAddress;
  destination: ShipmentAddress;
  sender: { name: string; phone: string; email?: string };
  recipient: { name: string; phone: string; email?: string };
  packages: ShipmentPackage[];
}

export interface TrackShipmentResult {
  trackingNumber: string;
  status: string;
  events: Array<{ timestamp: string; location?: string; description: string; status: string }>;
  estimatedDelivery?: string;
  actualDelivery?: string;
}

export interface ShipmentProviderAdapter {
  readonly providerName: string;
  initialize(config: ShipmentProviderConfig): Promise<void>;
  createShipment(input: CreateShipmentInput): Promise<Shipment>;
  getQuote(packages: ShipmentPackage[], destination: ShipmentAddress): Promise<ShipmentQuote[]>;
  trackShipment(trackingNumber: string): Promise<TrackShipmentResult>;
  cancelShipment(shipmentId: string): Promise<boolean>;
  validateAddress(address: ShipmentAddress): Promise<{ valid: boolean; suggestions?: ShipmentAddress[] }>;
}

export interface ShipmentProviderFactory {
  getProvider(name: string): ShipmentProviderAdapter | undefined;
  registerProvider(name: string, adapter: ShipmentProviderAdapter): void;
  listProviders(): string[];
}
