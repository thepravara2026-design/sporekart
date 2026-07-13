import { } from 'react';

const ERROR_DEMOS = [
  { id: '404', title: '404 — Page Not Found', icon: '🔍', component: NotFoundError },
  { id: '403', title: '403 — Access Restricted', icon: '🔒', component: ForbiddenError },
  { id: '401', title: '401 — Session Expired', icon: '🔐', component: UnauthorizedError },
  { id: '500', title: '500 — Server Error', icon: '⚠️', component: ServerError },
  { id: 'network', title: 'Network — Offline', icon: '🌐', component: NetworkError },
  { id: 'timeout', title: 'Timeout — Request Timed Out', icon: '⏱️', component: TimeoutError },
  { id: 'validation', title: 'Validation — Form Errors', icon: '✏️', component: ValidationError },
  { id: 'conflict', title: '409 — Conflict', icon: '⚔️', component: ConflictError },
  { id: 'rate-limit', title: '429 — Rate Limited', icon: '🚦', component: RateLimitError },
];

function NotFoundError() {
  return (
    <div className="sk-error-page" role="alert">
      <div className="sk-error-page__icon" aria-hidden="true">🔍</div>
      <h2>Page not found</h2>
      <p>The page you're looking for doesn't exist or has moved.</p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action">Go to Home</button>
        <button className="sk-secondary-action">Search</button>
      </div>
      <p className="sk-error-page__suggestions">Or try: <button className="sk-link">/products</button> · <button className="sk-link">/training</button> · <button className="sk-link">/support/kb</button></p>
      <p className="sk-error-page__ref">Reference: ERR-20260712-ABC123</p>
    </div>
  );
}

function ForbiddenError() {
  return (
    <div className="sk-error-page" role="alert">
      <div className="sk-error-page__icon" aria-hidden="true">🔒</div>
      <h2>Access restricted</h2>
      <p>This page requires the <strong>Distributor</strong> role. Your current role: <strong>Customer</strong>.</p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action">Request Access</button>
        <button className="sk-secondary-action">Go to Dashboard</button>
      </div>
    </div>
  );
}

function UnauthorizedError() {
  return (
    <div className="sk-error-page" role="alert">
      <div className="sk-error-page__icon" aria-hidden="true">🔐</div>
      <h2>Session expired</h2>
      <p>Please sign in to continue. We'll bring you back here.</p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action">Sign In</button>
      </div>
    </div>
  );
}

function ServerError() {
  return (
    <div className="sk-error-page" role="alert">
      <div className="sk-error-page__icon" aria-hidden="true">⚠️</div>
      <h2>Something went wrong</h2>
      <p>Our team has been notified. Reference: <strong>ERR-20260712-XYZ789</strong></p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action">Retry</button>
        <button className="sk-secondary-action">Contact Support</button>
      </div>
    </div>
  );
}

function NetworkError() {
  return (
    <div className="sk-error-page" role="status">
      <div className="sk-error-page__icon" aria-hidden="true">🌐</div>
      <h2>Connection lost</h2>
      <p>Working offline — changes will sync when reconnected.</p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action">Retry Now</button>
        <button className="sk-secondary-action">Work Offline</button>
      </div>
      <p className="sk-hint">3 changes queued</p>
    </div>
  );
}

function TimeoutError() {
  return (
    <div className="sk-error-page" role="alert">
      <div className="sk-error-page__icon" aria-hidden="true">⏱️</div>
      <h2>Request timed out</h2>
      <p>Your data is saved. The server took too long to respond.</p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action">Retry</button>
        <button className="sk-secondary-action">Save as Draft</button>
      </div>
    </div>
  );
}

function ValidationError() {
  return (
    <form className="sk-form sk-validation-demo" noValidate>
      <div className="sk-form-field">
        <label htmlFor="email">Email address *</label>
        <input id="email" type="email" value="invalid" aria-invalid="true" aria-describedby="email-error" />
        <span id="email-error" className="sk-error" role="alert">Enter a valid email address</span>
      </div>
      <div className="sk-form-field">
        <label htmlFor="quantity">Quantity (kg) *</label>
        <input id="quantity" type="number" value="500" aria-invalid="true" aria-describedby="qty-error" />
        <span id="qty-error" className="sk-error" role="alert">Quantity must be between 1 and 100</span>
      </div>
      <div className="sk-form-field">
        <label htmlFor="pincode">PIN Code *</label>
        <input id="pincode" type="text" value="123" aria-invalid="true" aria-describedby="pin-error" />
        <span id="pin-error" className="sk-error" role="alert">PIN must be 6 digits</span>
      </div>
      <div className="sk-form-field">
        <label htmlFor="password">Password *</label>
        <input id="password" type="password" value="123" aria-invalid="true" aria-describedby="pwd-error" />
        <span id="pwd-error" className="sk-error" role="alert">Password must be at least 8 characters</span>
      </div>
      <div className="sk-form-actions">
        <button type="submit" className="sk-primary-action">Submit</button>
      </div>
    </form>
  );
}

function ConflictError() {
  return (
    <div className="sk-error-inline" role="alert">
      <div className="sk-error-inline__icon" aria-hidden="true">⚔️</div>
      <div>
        <strong>Order already exists</strong>
        <p>An order with reference ORD-12345 already exists.</p>
        <div className="sk-error-inline__actions">
          <button className="sk-primary-action">View Existing</button>
          <button className="sk-secondary-action">Edit Instead</button>
        </div>
      </div>
    </div>
  );
}

function RateLimitError() {
  return (
    <div className="sk-error-page" role="alert">
      <div className="sk-error-page__icon" aria-hidden="true">🚦</div>
      <h2>Too many requests</h2>
      <p>Please wait before trying again.</p>
      <div className="sk-error-page__actions">
        <button className="sk-primary-action" disabled>Retry in 30s</button>
      </div>
    </div>
  );
}

export default function ErrorsDemo() {
  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Error Pages & Inline Errors</h1>
          <p className="sk-content__subtitle">All error patterns: 404, 403, 401, 500, network, timeout, validation, conflict, rate limit.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar">
        <span className="sk-toolbar-label">Test all error patterns. Check focus management, screen reader announcements, recovery actions.</span>
      </div>

      <div className="sk-error-gallery" role="list" aria-label="Error demonstrations">
        {ERROR_DEMOS.map(({ id, title, icon, component: Component }) => (
          <article key={id} className="sk-error-card" role="listitem">
            <header className="sk-error-card__header">
              <span className="sk-error-card__icon" aria-hidden="true">{icon}</span>
              <h3>{title}</h3>
            </header>
            <div className="sk-error-card__content">
              <Component />
            </div>
          </article>
        ))}
      </div>
    </div>
  );
}