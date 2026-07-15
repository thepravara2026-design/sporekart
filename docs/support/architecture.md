# Support Platform Architecture

This document maps the Information Architecture and components structure for the SporeKart Help & Support Center.

## Information Architecture (IA)

```mermaid
graph TD
  Dashboard["/dashboard/support"] -->|Link| Contact["/dashboard/support/contact (Form)"]
  Dashboard -->|Link| Tickets["/dashboard/support/tickets (List & Details)"]
  Dashboard -->|Link| Faq["/dashboard/support/faq (Accordion)"]
  Dashboard -->|Link| KB["/dashboard/support/help-center (Knowledge Base)"]
  Dashboard -->|Link| Feedback["/dashboard/support/feedback (Star NPS Form)"]
```

## Component Roles

1. **SupportDashboard**: Home hub showing overview metrics, latest tickets list, quick search hooks, and WhatsApp live chat redirection stubs.
2. **TicketsPage**: Manages list filters and loads individual timelines dynamically using useParams.
3. **KnowledgeBasePage**: Coordinates article queries and updates helpfulness counts in local states.
4. **FaqCenterPage**: Stores the active expanded question index, displaying collapsed accordions.
5. **ContactSupportPage**: Coordinates validations, preferred channels, and attachment placeholders.
6. **FeedbackPage**: Renders interactive hoverable rating stars.
