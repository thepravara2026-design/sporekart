# Configuration Lifecycle

## Lifecycle States

```mermaid
stateDiagram-v2
    [*] --> DEFINED: Config key created
    DEFINED --> LOADED: Application startup
    LOADED --> VALIDATED: Validation pass
    VALIDATED --> RESOLVED: Property resolved
    RESOLVED --> CACHED: Cache write
    CACHED --> OVERRIDDEN: Environment override
    OVERRIDDEN --> RESOLVED: Re-resolve
    CACHED --> INVALIDATED: TTL expiry
    INVALIDATED --> LOADED: Reload
    OVERRIDDEN --> DEPRECATED: Config removed
    DEPRECATED --> [*]: Cleanup
    VALIDATED --> FAILED: Validation error
    FAILED --> DEFINED: Fix and restart
```

## Resolution Flow

```mermaid
sequenceDiagram
    participant App as Application
    participant Loader as ConfigurationLoader
    participant Providers as Configuration Providers
    participant Validator as Validator
    participant Cache as ConfigurationCache
    participant Env as Environment Variables
    participant Secret as Secret Provider

    App->>Loader: load(configKey)
    Loader->>Cache: get(configKey)
    alt Cache Hit
        Cache-->>Loader: cached ConfigValue
        Loader-->>App: ConfigValue
    else Cache Miss
        Loader->>Providers: resolve(configKey)
        Providers->>Env: check environment
        alt Found in Env
            Env-->>Providers: value
        else Not Found
            Providers->>Secret: check secrets
            Secret-->>Providers: value or null
        end
        Providers-->>Loader: ConfigValue
        Loader->>Validator: validate(key, value)
        alt Valid
            Validator-->>Loader: success
            Loader->>Cache: put(key, value)
            Loader-->>App: ConfigValue
        else Invalid
            Validator-->>Loader: error
            Loader-->>App: default value + warning
        end
    end
```

## Hot Reload Flow

```mermaid
sequenceDiagram
    participant Scheduler as ConfigScheduler
    participant Loader as ConfigurationLoader
    participant Cache as ConfigurationCache
    participant App as Application

    loop Every hotReloadInterval
        Scheduler->>Loader: reloadAll()
        Loader->>Cache: invalidateAll()
        Loader->>Loader: re-resolve all keys
        Loader-->>Scheduler: reload complete
        Scheduler->>App: notify config changed
    end
```

## Cache Strategy by Environment

| Environment | Strategy | TTL | Versioning |
|-------------|----------|-----|------------|
| Local | CACHE_ASIDE | 30m | No |
| Dev | CACHE_ASIDE | 10m | No |
| Test | None (disabled) | N/A | N/A |
| Stage | CACHE_ASIDE | 5m | Yes |
| Prod | REFRESH_AHEAD | 2m | Yes |
| Docker | CACHE_ASIDE | 5m | No |
| Cloud | REFRESH_AHEAD | 2m | Yes |
