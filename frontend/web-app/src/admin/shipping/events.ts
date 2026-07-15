export interface ShipmentCreatedEvent {
  type: 'shipment.created';
  payload: {
    shipmentId: string;
    orderId: string;
    provider: string;
    trackingNumber?: string;
    estimatedDelivery?: string;
    timestamp: string;
  };
}

export interface ShipmentStatusChangedEvent {
  type: 'shipment.status_changed';
  payload: {
    shipmentId: string;
    orderId: string;
    previousStatus: string;
    newStatus: string;
    provider: string;
    eventDescription: string;
    location?: string;
    timestamp: string;
  };
}

export interface ShipmentDeliveredEvent {
  type: 'shipment.delivered';
  payload: {
    shipmentId: string;
    orderId: string;
    provider: string;
    actualDelivery: string;
    recipientName?: string;
    signature?: string;
    timestamp: string;
  };
}

export interface ShipmentExceptionEvent {
  type: 'shipment.exception';
  payload: {
    shipmentId: string;
    orderId: string;
    provider: string;
    exceptionCode: string;
    exceptionDescription: string;
    resolution?: string;
    timestamp: string;
  };
}

export type ShipmentEvent =
  | ShipmentCreatedEvent
  | ShipmentStatusChangedEvent
  | ShipmentDeliveredEvent
  | ShipmentExceptionEvent;
