# Enterprise Communication Platform — Future Channels

**Sprint 26 · Part 10.** Provider-agnostic roadmap and how future integrations are represented
without any real provider code, credentials, or network calls.

## 1. Design Intent

The platform is built **provider-agnostic**. Delivery, templates, and audience targeting are
modelled against abstract contracts so that new channels can be activated by configuration and
a provider adapter — not by rewriting UI. In Mock Mode no provider exists; future channels are
communication artefacts only.

## 2. Future Delivery Channels

`DeliveryChannel = 'in-app' | 'email' | 'whatsapp' | 'sms' | 'push'`

- `in-app` is the only **active** channel.
- `email`, `whatsapp`, `sms`, `push` are listed in `FUTURE_CHANNELS` and appear only as
  placeholders. `ChannelChip` accepts a `future` flag that renders a dashed border and muted
  tone, and sets `title="… (future integration)"`.

## 3. Future Template Kinds

`TemplateKind` includes channel-specific placeholders `email | whatsapp | sms | push`
(collected in `FUTURE_TEMPLATE_KINDS`). The seed defines four future templates
(`Email Welcome`, `WhatsApp Reminder`, `SMS Alert`, `Push Notification`) with `future: true`.
`TemplateCard` renders these with a dashed border, a "Future" tag, "Not active" footer, and a
disabled Preview button.

## 4. Integration Providers (Interfaces Only)

`IntegrationProvider` records (exported as `INTEGRATION_PROVIDERS`) describe the roadmap:

| id | name | category | status | description |
| --- | --- | --- | --- | --- |
| `int-email` | Email (SMTP / Provider) | `email` | `planned` | Transactional/bulk email once configured. |
| `int-whatsapp` | WhatsApp Business | `whatsapp` | `planned` | Rich messaging via WhatsApp Business API. |
| `int-sms` | SMS Gateway | `sms` | `planned` | Short text alerts via SMS aggregator. |
| `int-push` | Push Notifications | `push` | `planned` | Web/mobile push via a push service. |
| `int-calendar` | Calendar Sync | `calendar` | `planned` | Publish schedules to external calendars. |
| `int-crm` | CRM Sync | `crm` | `planned` | Sync audiences/engagement with a CRM. |

`IntegrationCategory` additionally names `erp` and `commerce` for forward compatibility, though
no provider records use them yet.

## 5. How the Roadmap Is Communicated

- **`FutureChannelCard`** (`components/FutureChannelCard.tsx`): renders each provider with a
  "Planned" pill, a muted icon, description, and a disabled "Configure (unavailable)" button
  (`disabled` + `aria-disabled`). `aria-label` includes "(planned integration)".
- **`CommunicationFutureChannelsPage`**: shows the roadmap banner explaining provider-agnostic
  design and that no external delivery occurs in Mock Mode, then a `minmax(300px, 1fr)` grid of
  `FutureChannelCard`s.
- **`AudienceSelector`**: future audience scopes (`role`, `geography`, `organization` from
  `FUTURE_AUDIENCE_SCOPES`) render as disabled, `aria-pressed` toggles with reduced opacity and
  `not-allowed` cursor.

## 6. Guarantees

- **No provider code**: there are no implementation modules, SDK imports, or adapters for any
  channel beyond the data records above.
- **No credentials**: no keys, tokens, env vars, or secrets are read anywhere in the feature.
- **No network calls**: no `fetch`/WebSocket/SMTP; the Delivery Queue explicitly states external
  channels are placeholders and never dispatch.
- **Disabled affordances**: every future control is `disabled`/`aria-disabled`, so users cannot
  trigger an unsupported action.

The seam for real integration is the `data/` layer: when a provider is added, its
`IntegrationProvider.status` becomes active, `ChannelChip`/`TemplateCard` drop the `future`
treatment, and a delivery adapter replaces the mock queue — without page/component rewrites.
