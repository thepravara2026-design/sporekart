# Communication Platform — Notification Lifecycle

## Lifecycle Stages

```
Draft → Scheduled → Queued → Sent → Delivered → Read → Archived
                                                        ↓
                                                  Expired / Cancelled → Failed
```

## Stage Details

| Status | Description |
|---|---|
| Draft | Notification being composed, not yet finalized |
| Scheduled | Scheduled for future delivery |
| Queued | Waiting in delivery queue |
| Sent | Dispatched to delivery channel |
| Delivered | Successfully delivered to recipient |
| Read | Recipient has opened/viewed the notification |
| Archived | Moved to archive by recipient |
| Expired | Time-sensitive notification past its expiry |
| Cancelled | Manually cancelled before delivery |
| Failed | Delivery failed (placeholder for future integration) |

## Transitions

- Draft → Scheduled (when send time is in future)
- Draft → Queued (when sent immediately)
- Scheduled → Queued (at scheduled time)
- Queued → Sent (dispatched to channel)
- Sent → Delivered (confirmed delivery)
- Delivered → Read (recipient views)
- Any → Archived (manual action)
- Any → Cancelled (manual action)
- Sent/Delivered → Failed (delivery error)
