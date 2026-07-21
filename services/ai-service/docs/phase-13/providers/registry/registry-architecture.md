# Enterprise AI Provider Registry Architecture

## Overview

The Provider Registry is the single source of truth for every AI provider inside SporeKart. Every provider must register itself here before use. The Gateway discovers providers only through this Registry.

## Architecture Diagram

```mermaid
graph TB
    GW[AI Gateway]
    PR[Provider Registry]
    PE[Provider Discovery Engine]
    PC[Provider Catalog]
    PM[Provider Metadata]
    PA[Provider Adapter]
    PV[Provider]

    GW -->|discovers| PR
    PR -->|manages| PC
    PR -->|discovers via| PE
    PE -->|indexes| PC
    PC -->|contains| PM
    PM -->|maps to| PA
    PA -->|connects| PV

    subgraph Registry_Components
        PR
        PE
        PC
    end

    subgraph Provider_Layer
        PM
        PA
        PV
    end
```

## Component Architecture

```mermaid
graph LR
    subgraph Registry_Core
        PR[ProviderRegistry]
        PC[ProviderCatalog]
        PE[ProviderDiscoveryEngine]
    end

    subgraph Registry_Management
        RM[RegistrationManager]
        AM[ActivationManager]
        VM[VersionManager]
    end

    subgraph Registry_Support
        LS[LookupService]
        PD[ProviderDirectory]
        PS[ProviderSnapshot]
    end

    Registry_Core --> Registry_Management
    Registry_Core --> Registry_Support
```

## Key Design Principles

- **Single Source of Truth**: All provider metadata is managed here
- **Discovery-Only Gateway Access**: Gateway never accesses providers directly
- **Pluggable Discovery Sources**: New discovery mechanisms can be added
- **Versioned Catalog**: Every provider entry is version-tracked
- **Health-Aware**: Registry tracks provider health for intelligent routing
