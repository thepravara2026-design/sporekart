# Domain Events Catalog

## Customer Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `customer.registered` | Customer registered | customerId, email |
| `customer.updated` | Customer profile updated | customerId, updatedFields |

## Order Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `order.created` | Order placed | orderId, customerId, totalAmount |
| `order.paid` | Order payment confirmed | orderId, amountPaid |
| `order.cancelled` | Order cancelled | orderId, reason |

## Payment Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `payment.succeeded` | Payment completed | paymentId, orderId, amount |
| `payment.failed` | Payment failed | paymentId, orderId, amount, failureReason |

## Inventory Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `inventory.reserved` | Inventory reserved for order | productId, quantity |
| `inventory.released` | Inventory released from cancelled order | productId, quantity |
| `inventory.low` | Stock below reorder point | productId, currentStock, reorderPoint |
| `inventory.updated` | Stock level changed | productId, newStock, delta |

## Cart Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `cart.created` | Shopping cart created | cartId, customerId |
| `cart.abandoned` | Cart abandoned | cartId, customerId |
| `cart.checked-out` | Cart converted to order | cartId, customerId, total |

## Catalog Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `product.created` | Product added to catalog | productId, name, price |
| `product.updated` | Product details updated | productId, updatedFields |
| `product.deleted` | Product removed | productId |

## Content Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `review.submitted` | Product review submitted | reviewId, productId, rating |
| `review.approved` | Review approved | reviewId, approvedBy |
| `review.rejected` | Review rejected | reviewId, rejectedBy, reason |

## Fulfillment Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `shipment.created` | Shipment created for order | shipmentId, orderId |
| `shipment.delivered` | Shipment delivered | shipmentId, orderId, deliveredAt |
| `shipment.delayed` | Shipment delayed | shipmentId, orderId, reason |

## Notification Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `notification.sent` | Notification delivered | notificationId, recipient, channel |

## Identity Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `user.registered` | User account created | userId, email |
| `user.logged-in` | User authenticated | userId, sessionId |
| `password.changed` | Password updated | userId |

## Training Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `training.registered` | Grower enrolled in training | trainingId, growerId |
| `training.completed` | Training completed | trainingId, growerId |
| `certificate.generated` | Certificate issued | certificateId, trainingId, growerId |

## Analytics Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `report.generated` | Analytics report created | reportId, reportType |

## Admin Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `admin.action.performed` | Admin action executed | adminId, action, target |

## Risk Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `risk.assessment.created` | Risk assessment completed | assessmentId, entityId, riskLevel |
| `fraud.detected` | Fraud detected | caseId, entityId, severity |

## Search Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `document.indexed` | Document indexed for search | documentId, entityType |

## Support Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `ticket.created` | Support ticket opened | ticketId, customerId, category |
| `ticket.resolved` | Support ticket resolved | ticketId, resolvedBy |

## AI Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `ai.conversation.started` | AI conversation initiated | conversationId, userId, copilotType |
| `ai.conversation.completed` | AI conversation ended | conversationId, summary |
| `ai.action.executed` | AI action performed | actionId, actionType, result |

## Knowledge Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `knowledge.created` | Knowledge entry added | knowledgeId, title |
| `knowledge.updated` | Knowledge entry modified | knowledgeId, updatedFields |

## Prompt Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `prompt.published` | Prompt version published | promptId, version |
| `prompt.updated` | Prompt version updated | promptId, newVersion |

## Memory Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `memory.updated` | Memory entry updated | memoryId, entityType, entityId |

## Copilot Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `copilot.action.executed` | Copilot action performed | actionId, copilotType, action |
| `copilot.suggestion.generated` | Copilot suggestion created | suggestionId, copilotType, suggestionType |

## Plugin Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `plugin.installed` | Plugin installed | pluginId, pluginName |
| `plugin.uninstalled` | Plugin removed | pluginId, pluginName |

## Marketing Domain

| Event | Description | Payload |
|-------|-------------|---------|
| `coupon.applied` | Coupon applied to order | couponId, orderId, discount |
| `campaign.started` | Marketing campaign launched | campaignId, campaignName |
