import { useState } from 'react';

export default function LoadingDemo() {
  const [offline, setOffline] = useState(false);
  const [retryCount, setRetryCount] = useState(0);
  const [showSkeleton, setShowSkeleton] = useState(true);

  const simulateLoad = () => {
    setShowSkeleton(true);
    setRetryCount(0);
    setTimeout(() => setShowSkeleton(false), 2000);
  };

  const simulateRetry = async () => {
    setRetryCount(c => c + 1);
    await new Promise(r => setTimeout(r, 1000 * retryCount + 500));
    if (retryCount >= 2) alert('Max retries reached');
  };

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Loading Experience Demo</h1>
          <p className="sk-content__subtitle">Skeletons, progressive loading, optimistic UI, offline, retry patterns.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar">
        <button className="sk-primary-action" onClick={simulateLoad} disabled={showSkeleton}>{showSkeleton ? 'Loading…' : 'Simulate Route Load'}</button>
        <label className="sk-toggle"><input type="checkbox" checked={offline} onChange={e => setOffline(e.target.checked)} /> Offline Mode</label>
        <button className="sk-secondary-action" onClick={simulateRetry} disabled={retryCount === 0}>Retry ({retryCount}/3)</button>
      </div>

      {offline && (
        <div className="sk-banner sk-banner--offline" role="status" aria-live="polite">
          <span>🌐</span>
          <span>Working offline — changes will sync when reconnected.</span>
          <button className="sk-link-button" onClick={() => setOffline(false)}>Go Online</button>
        </div>
      )}

      <section className="sk-panel" aria-labelledby="route-title">
        <h2 id="route-title">Route Skeleton (Page Shell)</h2>
        <p className="sk-hint">Shows immediately on navigation; replaces with content when ready.</p>
        <div className={`sk-skeleton-page ${showSkeleton ? '' : 'sk-loaded'}`}>
          <div className="sk-skeleton-header"></div>
          <div className="sk-skeleton-breadcrumb"></div>
          <div className="sk-skeleton-title-row">
            <div className="sk-skeleton-title"></div>
            <div className="sk-skeleton-action"></div>
          </div>
          <div className="sk-skeleton-panel">
            <div className="sk-skeleton-metric"></div>
            <div className="sk-skeleton-metric"></div>
            <div className="sk-skeleton-metric"></div>
            <div className="sk-skeleton-chart"></div>
            <div className="sk-skeleton-table">
              <div className="sk-skeleton-row"></div>
              <div className="sk-skeleton-row"></div>
              <div className="sk-skeleton-row"></div>
              <div className="sk-skeleton-row"></div>
              <div className="sk-skeleton-row"></div>
            </div>
          </div>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="component-title">
        <h2 id="component-title">Component Skeletons</h2>
        <p className="sk-hint">Match final dimensions exactly. No layout shift on swap.</p>
        <div className="sk-skeleton-grid">
          <div className="sk-skeleton-card">
            <div className="sk-skeleton-image"></div>
            <div className="sk-skeleton-title-sm"></div>
            <div className="sk-skeleton-text"></div>
            <div className="sk-skeleton-text-short"></div>
            <div className="sk-skeleton-action"></div>
          </div>
          <div className="sk-skeleton-card">
            <div className="sk-skeleton-image"></div>
            <div className="sk-skeleton-title-sm"></div>
            <div className="sk-skeleton-text"></div>
            <div className="sk-skeleton-text-short"></div>
            <div className="sk-skeleton-action"></div>
          </div>
          <div className="sk-skeleton-card">
            <div className="sk-skeleton-image"></div>
            <div className="sk-skeleton-title-sm"></div>
            <div className="sk-skeleton-text"></div>
            <div className="sk-skeleton-text-short"></div>
            <div className="sk-skeleton-action"></div>
          </div>
          <div className="sk-skeleton-card">
            <div className="sk-skeleton-image"></div>
            <div className="sk-skeleton-title-sm"></div>
            <div className="sk-skeleton-text"></div>
            <div className="sk-skeleton-text-short"></div>
            <div className="sk-skeleton-action"></div>
          </div>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="action-title">
        <h2 id="action-title">Action Loading (Optimistic UI)</h2>
        <p className="sk-hint">Button shows inline spinner; immediate visual feedback; rollback on error.</p>
        <div className="sk-action-demo">
          <button className="sk-primary-action" disabled>Create Order <span className="sk-spinner-sm"></span></button>
          <button className="sk-secondary-action" disabled>Save <span className="sk-spinner-sm"></span></button>
          <button className="sk-destructive-action" disabled>Delete <span className="sk-spinner-sm"></span></button>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="offline-title">
        <h2 id="offline-title">Offline Banner & Queue</h2>
        <p className="sk-hint">Shows when offline; mutations queued in IndexedDB; sync indicator on reconnect.</p>
        <div className="sk-offline-demo">
          <div className="sk-banner sk-banner--offline" role="status">
            <span>🌐 Working offline</span>
            <span>3 changes queued</span>
          </div>
          <div className="sk-banner sk-banner--sync" role="status">
            <span>✅ Synced 3 changes</span>
          </div>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="retry-title">
        <h2 id="retry-title">Retry with Exponential Backoff</h2>
        <p className="sk-hint">1s → 2s → 4s; max 3 attempts; user sees attempt count.</p>
        <div className="sk-retry-demo">
          <div className="sk-retry-state" role="alert">
            <span>⚠️ Request timed out</span>
            <span>Attempt 2 of 3</span>
            <button className="sk-primary-action">Retry Now</button>
          </div>
        </div>
      </section>

      <section className="sk-panel" aria-labelledby="image-title">
        <h2 id="image-title">Image Loading (Blurhash → LQIP → HQ)</h2>
        <p className="sk-hint">Aspect-ratio box prevents CLS. Blurhash placeholder → low-quality image → high-quality.</p>
        <div className="sk-image-loading-demo">
          <div className="sk-img-box sk-img-blurhash" aria-label="Blurhash placeholder"></div>
          <div className="sk-img-box sk-img-lqip" aria-label="Low quality image"></div>
          <div className="sk-img-box sk-img-hq" aria-label="High quality image"></div>
        </div>
      </section>
    </div>
  );
}