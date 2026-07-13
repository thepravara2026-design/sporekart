export default function MicrocopyDemo() {
  type BaseItem = { label: string; note?: string };

  type ButtonItem = BaseItem & { type: 'primary' | 'secondary' | 'destructive' | 'link' | 'success' };
  type LinkItem = BaseItem & { type: 'link' };
  type FormLabelItem = BaseItem & { helper: string };
  type PlaceholderItem = BaseItem & { field: string };
  type ErrorItem = BaseItem & { context: string };
  type SuccessItem = BaseItem & { type: 'success' };
  type ToastItem = BaseItem & { severity: 'success' | 'info' | 'warning' | 'error' };
  type EmptyStateItem = BaseItem & { body: string; cta: string; secondary?: string };
  type TooltipItem = BaseItem & { trigger: string };
  type DialogItem = BaseItem & { title: string; body: string; primary: string; secondary: string };
  type LoadingItem = BaseItem & { context: string };
  type TerminologyItem = BaseItem & { correct: string; incorrect: string };

  type MicrocopyItem =
    | ButtonItem
    | LinkItem
    | FormLabelItem
    | PlaceholderItem
    | ErrorItem
    | SuccessItem
    | ToastItem
    | EmptyStateItem
    | TooltipItem
    | DialogItem
    | LoadingItem
    | TerminologyItem;

  const sections = [
    {
      title: 'Buttons',
      desc: 'Primary: Verb + Noun. Secondary: Verb + Noun. Destructive: Delete [noun].',
      items: [
        { label: 'Place order', type: 'primary' as const },
        { label: 'Create product', type: 'primary' as const },
        { label: 'Register', type: 'primary' as const },
        { label: 'Save changes', type: 'primary' as const },
        { label: 'Cancel', type: 'secondary' as const },
        { label: 'Skip', type: 'secondary' as const },
        { label: 'Delete order', type: 'destructive' as const },
        { label: 'Remove address', type: 'destructive' as const },
        { label: 'Export…', type: 'secondary' as const, note: 'Ellipsis = opens dialog' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Links',
      desc: 'Descriptive, never "click here" or "read more".',
      items: [
        { label: 'View all orders', type: 'link' as const },
        { label: 'Read the pricing policy', type: 'link' as const },
        { label: 'Create your first order', type: 'link' as const, note: 'Empty state CTA' },
        { label: 'Privacy Policy', type: 'link' as const, note: 'Footer' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Form Labels & Helpers',
      desc: 'Sentence case. Helper = complete sentence explaining why/format.',
      items: [
        { label: 'Email address *', helper: 'We\'ll send a verification code to this email' },
        { label: 'Quantity (kg) *', helper: 'Enter a value between 1 and 100' },
        { label: 'PAN', helper: '10 characters, e.g., ABCDE1234F' },
        { label: 'Password *', helper: 'At least 8 characters, one number, one symbol' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Placeholders',
      desc: 'Example only. Never replace label.',
      items: [
        { label: 'you@example.com', field: 'Email' },
        { label: 'Enter 6-digit PIN', field: 'PIN Code' },
        { label: 'Search products, categories…', field: 'Search' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Error Messages',
      desc: 'Specific, actionable, no blame. "Enter a valid…" not "Invalid".',
      items: [
        { label: 'Email is required', context: 'Required' },
        { label: 'Enter a valid email address', context: 'Format' },
        { label: 'Password must be at least 8 characters', context: 'Length' },
        { label: 'This email is already registered', context: 'Conflict' },
        { label: 'No account found with this email', context: 'Not found' },
        { label: 'Quantity must be between 1 and 100', context: 'Range' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Success Messages',
      desc: 'Calm, brief. Toast auto-dismisses 3–5s. Include Undo for destructive.',
      items: [
        { label: 'Order placed', type: 'success' as const },
        { label: 'Profile saved', type: 'success' as const },
        { label: 'Order deleted · Undo', type: 'success' as const, note: 'With undo' },
        { label: '3 orders archived', type: 'success' as const },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Toast / Notifications',
      desc: 'Success (green, 4s), Warning (amber, 8s), Error (red, persist).',
      items: [
        { label: 'Order placed', severity: 'success' as const },
        { label: 'Sync complete: 12 items updated', severity: 'info' as const },
        { label: 'Low stock: 5 kg remaining. Reorder soon.', severity: 'warning' as const },
        { label: 'Payment failed. Please try again.', severity: 'error' as const },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Empty States (Explain → Guide → Act)',
      desc: 'Illustration + Headline + Body + Primary CTA.',
      items: [
        { label: 'No orders yet', body: 'When you place an order, it will appear here.', cta: 'Shop products' },
        { label: 'No products found', body: 'Try adjusting your filters or search terms.', cta: 'Clear filters' },
        { label: 'No conversations yet', body: 'Ask a question to start your first chat.', cta: 'Ask AI' },
        { label: 'You don\'t have access', body: 'This section requires Distributor permissions.', cta: 'Request Access' },
        { label: 'Unable to load analytics', body: 'Check your connection or try again.', cta: 'Retry' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Tooltips & Help Text',
      desc: 'One sentence. Explains control, constraint, or advanced feature.',
      items: [
        { label: 'Sort by most recent first', trigger: 'Sort button' },
        { label: 'PAN: 10 chars, e.g., ABCDE1234F', trigger: 'PAN field' },
        { label: 'Enables auto-reorder when stock < 10%', trigger: 'Auto-reorder toggle' },
        { label: 'Two-factor authentication adds a second verification step', trigger: '2FA help' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Confirmation Dialogs',
      desc: 'Title = "Verb [noun]?". Body = consequence + reversibility. Primary = action, Secondary = Cancel.',
      items: [
        { label: '', title: 'Delete this order?', body: 'This will permanently remove the order. This cannot be undone.', primary: 'Delete order', secondary: 'Cancel' },
        { label: '', title: 'Archive 3 orders?', body: 'Archived orders are hidden but can be restored.', primary: 'Archive', secondary: 'Cancel' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Loading Copy',
      desc: 'Action-oriented. Never "Loading..." or "Please wait".',
      items: [
        { label: 'Saving…', context: 'Button' },
        { label: 'Verifying…', context: 'OTP' },
        { label: 'Thinking…', context: 'AI' },
        { label: 'Searching…', context: 'Search' },
      ] satisfies MicrocopyItem[],
    },
    {
      title: 'Terminology (Locked)',
      desc: 'Use only these terms. Do not substitute.',
      items: [
        { label: '', correct: 'Order', incorrect: 'Purchase, Transaction, Sale' },
        { label: '', correct: 'Product', incorrect: 'Item, SKU, Good' },
        { label: '', correct: 'Customer', incorrect: 'Buyer, User, Client' },
        { label: '', correct: 'Training', incorrect: 'Course, Workshop, Class' },
        { label: '', correct: 'Catalog', incorrect: 'Store, Shop, Marketplace' },
        { label: '', correct: 'AI Assistant', incorrect: 'Bot, Chatbot, Helper' },
      ] satisfies MicrocopyItem[],
    },
  ];

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Microcopy Gallery</h1>
          <p className="sk-content__subtitle">All copy patterns: buttons, links, labels, errors, success, toasts, empty states, tooltips, dialogs, loading, terminology.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar">
        <span className="sk-toolbar-label">Searchable, copy-paste ready. Toggle language (EN/HI placeholder) in Part 1D.</span>
      </div>

      <div className="sk-microcopy-sections" role="list" aria-label="Microcopy patterns">
        {sections.map((section, i) => (
          <section key={i} className="sk-panel" aria-labelledby={`mc-${i}`}>
            <h2 id={`mc-${i}`}>{section.title}</h2>
            <p className="sk-hint">{section.desc}</p>
            <div className="sk-microcopy-grid">
              {section.items.map((item, j) => (
                <article key={j} className="sk-microcopy-card">
                  {(item as any).type === 'primary' && <span className="sk-badge sk-badge--primary">Primary</span>}
                  {(item as any).type === 'secondary' && <span className="sk-badge sk-badge--secondary">Secondary</span>}
                  {(item as any).type === 'destructive' && <span className="sk-badge sk-badge--destructive">Destructive</span>}
                  {(item as any).type === 'link' && <span className="sk-badge sk-badge--link">Link</span>}
                  {(item as any).type === 'success' && <span className="sk-badge sk-badge--success">Success</span>}
                  {(item as any).severity && <span className="sk-badge sk-badge--info">{(item as any).severity}</span>}
                  <h4>{(item as any).label || (item as any).correct || (item as any).title}</h4>
                  {(item as any).body && <p className="sk-microcopy-body">{(item as any).body}</p>}
                  {(item as any).helper && <p className="sk-hint">Helper: {(item as any).helper}</p>}
                  {(item as any).cta && <p className="sk-microcopy-cta">CTA: {(item as any).cta}</p>}
                  {(item as any).primary && <p className="sk-microcopy-cta">Primary: {(item as any).primary} · Secondary: {(item as any).secondary}</p>}
                  {(item as any).incorrect && <p className="sk-microcopy-wrong">✗ {(item as any).incorrect}</p>}
                  {(item as any).note && <p className="sk-hint">{(item as any).note}</p>}
                  {(item as any).field && <p className="sk-hint">Field: {(item as any).field}</p>}
                  {(item as any).trigger && <p className="sk-hint">Trigger: {(item as any).trigger}</p>}
                  {(item as any).context && <p className="sk-hint">Context: {(item as any).context}</p>}
                  {(item as any).note && <p className="sk-hint">{(item as any).note}</p>}
                  {(item as any).primary && <p className="sk-microcopy-cta">Primary: {(item as any).primary} · Secondary: {(item as any).secondary}</p>}
                  {(item as any).incorrect && <p className="sk-microcopy-wrong">✗ {(item as any).incorrect}</p>}
                </article>
              ))}
            </div>
          </section>
        ))}
      </div>
    </div>
  );
}