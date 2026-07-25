# Workflow API

## Overview
REST API exposed at port 8097 under `/workflows`. All endpoints return standard HTTP responses with JSON bodies.

## Endpoints

### Health
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/health` | Service health check |

### Definitions
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/definitions` | List all definitions |
| GET | `/workflows/definitions/{id}` | Get definition by ID |
| GET | `/workflows/definitions/active` | List active definitions |
| POST | `/workflows/definitions/generate` | Generate all pre-defined workflows |

### Instances
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/instances` | List all instances |
| GET | `/workflows/instances/{id}` | Get instance by ID |
| GET | `/workflows/instances/state/{state}` | Filter by state |

### Execution
| Method | Path | Description |
|---|---|---|
| POST | `/workflows/start` | Start a workflow |
| POST | `/workflows/simulate` | Start a simulation |
| POST | `/workflows/{id}/pause` | Pause workflow |
| POST | `/workflows/{id}/resume` | Resume workflow |
| POST | `/workflows/{id}/cancel` | Cancel workflow |
| POST | `/workflows/{id}/retry` | Retry failed workflow |
| POST | `/workflows/{id}/complete` | Complete workflow |
| POST | `/workflows/{id}/fail` | Fail workflow |

### Simulation
| Method | Path | Description |
|---|---|---|
| POST | `/workflows/{id}/simulate` | Execute simulation |
| POST | `/workflows/{id}/rollback` | Simulate rollback |
| POST | `/workflows/dry-run` | Dry run a definition |

### Audit
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/audits` | List all audits |
| GET | `/workflows/audits/{instanceId}/trail` | Get audit trail |
| GET | `/workflows/audits/summary` | Audit summary |

### Queue
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/queue` | List queue items |
| GET | `/workflows/queue/pending` | Pending items |
| GET | `/workflows/queue/metrics` | Queue metrics |
| DELETE | `/workflows/queue` | Clear queue |

### Approvals
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/approvals` | List approvals |
| GET | `/workflows/approvals/status/{status}` | Filter by status |
| POST | `/workflows/{id}/approve` | Approve workflow |
| POST | `/workflows/{id}/reject` | Reject workflow |

### State Machine
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/state-machine/transitions?state=` | Allowed transitions |
| GET | `/workflows/state-machine/validate?from=&to=` | Validate transition |
| GET | `/workflows/state-machine/path?from=&to=` | Transition path |

### Telemetry
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/telemetry` | Telemetry metrics |
| GET | `/workflows/telemetry/history` | Telemetry history |
| GET | `/workflows/telemetry/health` | Workflow health |

### Cache
| Method | Path | Description |
|---|---|---|
| GET | `/workflows/cache` | Cache info |
| DELETE | `/workflows/cache` | Clear cache |
