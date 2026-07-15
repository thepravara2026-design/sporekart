# Shipping Extension Architecture

## Overview

The Shipping Extension Architecture provides provider-agnostic integration 
interfaces for the SporeKart Enterprise Inventory Platform. It defines 
extension points for external shipping providers without coupling the 
platform to any specific carrier.

## Design Principles

- **Provider Agnostic**: No provider-specific logic exists in the platform
- **Interface-Based**: All integrations go through `ShipmentProviderAdapter`
- **Event-Driven**: Shipment lifecycle changes are communicated via typed events
- **Factory Pattern**: Providers are registered via a `ShipmentProviderFactory`

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Inventory Platform                            │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                    Shipping Module                         │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐  │  │
│  │  │ Shipment Mgr │  │  Provider    │  │  Event Bus     │  │  │
│  │  │ (Orchestrator)│←→│  Factory    │←→│  (Publish/Sub) │  │  │
│  │  └──────┬───────┘  └──────┬───────┘  └───────┬────────┘  │  │
│  │         │                 │                   │            │  │
│  └─────────┼─────────────────┼───────────────────┼────────────┘  │
│            │                 │                   │                │
│  ┌─────────┴─────────────────┴───────────────────┴────────────┐  │
│  │                  Provider Adapters                           │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │  │
│  │  │Shiprocket│  │ Delhivery│  │ BlueDart │  │  DHL     │... │  │
│  │  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │  │
│  └─────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Components

### 1. Domain Models (`types.ts`)

- `Shipment` — Core shipment entity with full lifecycle tracking
- `ShipmentStatus` — State machine for shipment progression
- `ShipmentEvent` — Individual tracking events along the journey
- `ShipmentAddress`, `ShipmentContact`, `ShipmentPackage` — Supporting value objects

### 2. Provider Adapter Interface (`interfaces.ts`)

- `ShipmentProviderAdapter` — Contract all providers must implement:
  - `initialize(config)` — Configure provider credentials
  - `createShipment(input)` — Create a new shipment
  - `getQuote(packages, destination)` — Get shipping rate quotes
  - `trackShipment(trackingNumber)` — Track an existing shipment
  - `cancelShipment(shipmentId)` — Cancel a shipment
  - `validateAddress(address)` — Validate and suggest addresses
- `ShipmentProviderFactory` — Registry for provider discovery

### 3. Event Contracts (`events.ts`)

Typed domain events for shipment lifecycle:
- `shipment.created` — Raised when a shipment is created
- `shipment.status_changed` — Raised on any status transition
- `shipment.delivered` — Raised on successful delivery
- `shipment.exception` — Raised on delivery exceptions

## Provider Integration

To integrate a new shipping provider:

1. Implement `ShipmentProviderAdapter` interface
2. Register with `ShipmentProviderFactory.registerProvider(name, adapter)`
3. The platform discovers providers through the factory — no platform changes needed

### Supported Future Providers
- Shiprocket
- Delhivery
- Blue Dart
- DTDC
- Xpressbees
- Ecom Express
- India Post
- Shadowfax
- DHL
- FedEx

## Event Flow

```
Order Created → Shipment Created → Provider Confirms → Picked Up →
In Transit → Out for Delivery → Delivered (or Exception)
```

Each transition publishes a typed event that other platform modules 
(inventory, warehouse, analytics) can subscribe to.

## Boundaries

- The shipping module does NOT implement provider-specific HTTP clients
- Provider implementations live in separate adapter packages
- Platform code only depends on `ShipmentProviderAdapter` interface
- No shipping provider names appear outside adapter implementations
