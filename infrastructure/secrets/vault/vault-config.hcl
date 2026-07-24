# SporeKart Enterprise Vault Configuration
# HashiCorp Vault integration for centralized secrets management

## Vault Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Vault Cluster                      │
│                                                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐           │
│  │ Vault    │  │ Vault    │  │ Vault    │           │
│  │ Node 1   │  │ Node 2   │  │ Node 3   │           │
│  │ (active) │  │ (standby)│  │ (standby)│           │
│  └──────────┘  └──────────┘  └──────────┘           │
│                                                       │
│  Storage Backend: Raft (integrated)                  │
│  Auto-unseal: AWS KMS                                │
│  Auth: Kubernetes + JWT + Token                      │
└─────────────────────────────────────────────────────┘
```

## Secret Paths

```
secret/sporekart/
├── platform/
│   ├── jwt-secret
│   ├── database/
│   │   └── postgresql
│   └── redis
├── services/
│   ├── gateway/
│   │   └── jwt
│   ├── identity/
│   │   ├── jwt
│   │   ├── oauth
│   │   └── smtp
│   ├── payment/
│   │   └── razorpay
│   ├── notification/
│   │   └── smtp
│   └── ai/
│       ├── openai
│       ├── azure-openai
│       ├── gemini
│       └── claude
└── infrastructure/
    ├── supabase
    ├── sentry
    └── stripe
```

## Vault Policies

### Application Reader Policy
```hcl
path "secret/sporekart/*" {
  capabilities = ["read", "list"]
}

path "secret/sporekart/platform/jwt-secret" {
  capabilities = ["read"]
  control_group = {
    factor "authorizer" {
      identity {
        entity_ids = ["sre-lead", "cto"]
      }
      approvals = 1
    }
  }
}
```

### Deployment Policy
```hcl
path "secret/sporekart/*" {
  capabilities = ["read", "list", "create", "update"]
}

path "transit/*" {
  capabilities = ["encrypt", "decrypt"]
}

path "sys/*" {
  capabilities = ["read"]
}
```

### Audit Policy
```hcl
path "secret/sporekart/*" {
  capabilities = ["list"]
}

path "audit/*" {
  capabilities = ["read", "list"]
}
```

## Kubernetes Integration

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: vault-auth
  namespace: sporekart-platform
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: vault-auth-binding
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: system:auth-delegator
subjects:
  - kind: ServiceAccount
    name: vault-auth
    namespace: sporekart-platform
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: vault-agent-injector
  namespace: sporekart-platform
spec:
  replicas: 2
  selector:
    matchLabels:
      app: vault-agent-injector
  template:
    metadata:
      labels:
        app: vault-agent-injector
    spec:
      serviceAccountName: vault-auth
      containers:
        - name: vault-agent
          image: hashicorp/vault:1.18
          args:
            - agent
            - -config=/etc/vault/config.hcl
          volumeMounts:
            - name: config
              mountPath: /etc/vault
      volumes:
        - name: config
          configMap:
            name: vault-agent-config
```

## Secret Rotation Policy

| Secret Class | Rotation Period | Method | Approval Required |
|-------------|----------------|--------|-------------------|
| Critical (JWT, DB) | 30 days | Automated rotation script | SRE Lead |
| Standard (API keys) | 90 days | Manual rotation | Service Owner |
| Low (non-prod) | 180 days | Manual rotation | Developer |
