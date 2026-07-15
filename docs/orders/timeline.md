# Lifecycle Milestone Timelines

Order states are modeled chronologically in a visual progress timeline.

## Timeline Milestones

1. **Order Created**: Timestamped when the purchase is generated.
2. **Payment Received**: Logged when payment gateway (Razorpay) confirms success.
3. **Processing**: Batching, cleanroom selection, and sterilization checks.
4. **Quality Check**: Cultivar visual checks for contam & moisture.
5. **Dispatched**: Handover to courier partner.
6. **In Transit**: Sorting hub event logs.
7. **Out for Delivery**: Local agent route updates.
8. **Delivered**: OTP confirmation on arrival.

## Status Exceptions

- **Cancelled**: If the order is cancelled, the progress line terminates, and the cancellation milestone is highlighted in high-contrast red (`#dc2626`).
- **Returned & Refunded**: Highlights milestones in amber (`#d97706`) with links to track refund transfers.
