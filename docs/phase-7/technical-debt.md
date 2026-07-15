# Technical Debt Register

Summary of code debt and areas for refactoring.

## Debt Items

1. **State Persistence**: Customer selections (such as ticket conversation logs, certificate prints, and webinar registrations) are stored in local react state. Production will require database connections.
2. **Debounce Searches**: The search inputs in the knowledge base and FAQ directory should run on debounced triggers in production.
3. **Responsive charts**: Harvest yield charts use CSS and HTML5 progress bars. High-density charts will require lightweight canvas or SVG charting engines in later phases.
