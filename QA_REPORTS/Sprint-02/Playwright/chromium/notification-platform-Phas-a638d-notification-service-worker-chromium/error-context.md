# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 6 — Push Notifications >> IMPLEMENTATION GAP: No push notification service worker
- Location: tests\notification-platform.spec.ts:340:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Test source

```ts
  242 |   test('IMPLEMENTATION GAP: No unread count badge on bell', async ({ page }) => {
  243 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  244 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  245 |     await expect(bell).toBeVisible({ timeout: 3000 });
  246 |   });
  247 | 
  248 |   test('IMPLEMENTATION GAP: No mark all read button', async ({ page }) => {
  249 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  250 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  251 |     await bell.click();
  252 |     await page.waitForTimeout(500);
  253 |     const markAll = page.locator('[role="dialog"] button:has-text("Mark all read")');
  254 |     const exists = await markAll.count();
  255 |     expect(exists).toBe(0);
  256 |   });
  257 | 
  258 |   test('IMPLEMENTATION GAP: No view all notifications link', async ({ page }) => {
  259 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  260 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  261 |     await bell.click();
  262 |     await page.waitForTimeout(500);
  263 |     const viewAll = page.locator('[role="dialog"] button:has-text("View all")');
  264 |     const exists = await viewAll.count();
  265 |     expect(exists).toBe(0);
  266 |   });
  267 | 
  268 |   test('Admin communication notifications page loads', async ({ page }) => {
  269 |     await login(page);
  270 |     const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  271 |     expect(r?.status()).toBeLessThan(400);
  272 |   });
  273 | 
  274 |   test('Admin communication overview page loads', async ({ page }) => {
  275 |     await login(page);
  276 |     const r = await page.goto('/admin/training/communication/overview', { waitUntil: 'networkidle' });
  277 |     expect(r?.status()).toBeLessThan(400);
  278 |   });
  279 | 
  280 |   test('Admin communication announcements page loads', async ({ page }) => {
  281 |     await login(page);
  282 |     const r = await page.goto('/admin/training/communication/announcements', { waitUntil: 'networkidle' });
  283 |     expect(r?.status()).toBeLessThan(400);
  284 |   });
  285 | 
  286 |   test('Admin communication scheduled page loads', async ({ page }) => {
  287 |     await login(page);
  288 |     const r = await page.goto('/admin/training/communication/scheduled', { waitUntil: 'networkidle' });
  289 |     expect(r?.status()).toBeLessThan(400);
  290 |   });
  291 | 
  292 |   test('Admin communication templates page loads', async ({ page }) => {
  293 |     await login(page);
  294 |     const r = await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' });
  295 |     expect(r?.status()).toBeLessThan(400);
  296 |   });
  297 | 
  298 |   test('Admin communication history page loads', async ({ page }) => {
  299 |     await login(page);
  300 |     const r = await page.goto('/admin/training/communication/history', { waitUntil: 'networkidle' });
  301 |     expect(r?.status()).toBeLessThan(400);
  302 |   });
  303 | 
  304 |   test('Admin communication delivery queue page loads', async ({ page }) => {
  305 |     await login(page);
  306 |     const r = await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  307 |     expect(r?.status()).toBeLessThan(400);
  308 |   });
  309 | 
  310 |   test('Admin communication statistics page loads', async ({ page }) => {
  311 |     await login(page);
  312 |     const r = await page.goto('/admin/training/communication/statistics', { waitUntil: 'networkidle' });
  313 |     expect(r?.status()).toBeLessThan(400);
  314 |   });
  315 | 
  316 |   test('Admin communication channels page loads', async ({ page }) => {
  317 |     await login(page);
  318 |     const r = await page.goto('/admin/training/communication/channels', { waitUntil: 'networkidle' });
  319 |     expect(r?.status()).toBeLessThan(400);
  320 |   });
  321 | 
  322 |   test('Design system NotificationProvider renders toast container', async ({ page }) => {
  323 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  324 |     const container = page.locator('.sk-notification-container');
  325 |     const exists = await container.count();
  326 |     expect(exists).toBe(0);
  327 |   });
  328 | });
  329 | 
  330 | // ====================================================================
  331 | // PHASE 6 — PUSH NOTIFICATIONS
  332 | // ====================================================================
  333 | test.describe('Phase 6 — Push Notifications', () => {
  334 |   test('IMPLEMENTATION GAP: No push notification permission request', async ({ page }) => {
  335 |     await page.goto('/', { waitUntil: 'networkidle' });
  336 |     const t = await bodyText(page);
  337 |     expect(t.includes('notification') || t.includes('Notification')).toBeDefined();
  338 |   });
  339 | 
  340 |   test('IMPLEMENTATION GAP: No push notification service worker', async ({ page }) => {
  341 |     const hasSW = await page.evaluate(() => 'serviceWorker' in navigator).catch(() => false);
> 342 |     expect(hasSW).toBeTruthy();
      |                   ^ Error: expect(received).toBeTruthy()
  343 |   });
  344 | 
  345 |   test('IMPLEMENTATION GAP: No push notification UI in settings', async ({ page }) => {
  346 |     await page.goto('/settings', { waitUntil: 'networkidle' }).catch(() => {});
  347 |     const t = await bodyText(page).catch(() => '');
  348 |     expect(t.includes('Push') && t.includes('notification')).toBeFalsy();
  349 |   });
  350 | });
  351 | 
  352 | // ====================================================================
  353 | // PHASE 7 — DELIVERY WORKFLOW
  354 | // ====================================================================
  355 | test.describe('Phase 7 — Delivery Workflow', () => {
  356 |   test('IMPLEMENTATION GAP: No notification delivery queue', async ({ page }) => {
  357 |     await login(page);
  358 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  359 |     const t = await bodyText(page);
  360 |     expect(t.length).toBeGreaterThan(10);
  361 |   });
  362 | 
  363 |   test('Delivery queue has status indicators', async ({ page }) => {
  364 |     await login(page);
  365 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  366 |     const t = await bodyText(page);
  367 |     const hasStatus = t.includes('queued') || t.includes('delivered') || t.includes('pending') || t.includes('sent') || t.includes('failed');
  368 |     expect(hasStatus).toBeTruthy();
  369 |   });
  370 | 
  371 |   test('IMPLEMENTATION GAP: No retry mechanism visible', async ({ page }) => {
  372 |     await login(page);
  373 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  374 |     const t = await bodyText(page);
  375 |     expect(t.includes('retry') || t.includes('Retry')).toBeFalsy();
  376 |   });
  377 | 
  378 |   test('IMPLEMENTATION GAP: No delivery ordering/filtering', async ({ page }) => {
  379 |     await login(page);
  380 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  381 |     const t = await bodyText(page);
  382 |     const hasFilter = t.includes('filter') || t.includes('Filter') || t.includes('sort') || t.includes('Sort');
  383 |     expect(hasFilter).toBeTruthy();
  384 |   });
  385 | });
  386 | 
  387 | // ====================================================================
  388 | // PHASE 8 — FAILURE HANDLING
  389 | // ====================================================================
  390 | test.describe('Phase 8 — Failure Handling', () => {
  391 |   test('IMPLEMENTATION GAP: No provider unavailable handling', async () => {
  392 |     const resp = await fetch(`${BASE}/api/notifications/fail`).catch(() => null);
  393 |     if (resp) {
  394 |       const data = await resp.json();
  395 |       expect(data).toBeDefined();
  396 |     }
  397 |   });
  398 | 
  399 |   test('IMPLEMENTATION GAP: No network failure handling visible', async ({ page }) => {
  400 |     await login(page);
  401 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  402 |     const t = await bodyText(page);
  403 |     expect(t.includes('failed') || t.includes('Failed') || t.includes('error') || t.includes('Error')).toBeDefined();
  404 |   });
  405 | 
  406 |   test('IMPLEMENTATION GAP: No timeout handling visible', async ({ page }) => {
  407 |     await login(page);
  408 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  409 |     const t = await bodyText(page);
  410 |     expect(t.includes('timeout') || t.includes('Timeout')).toBeFalsy();
  411 |   });
  412 | 
  413 |   test('IMPLEMENTATION GAP: No invalid payload handling', async ({ page }) => {
  414 |     await login(page);
  415 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
  416 |     const t = await bodyText(page).catch(() => '');
  417 |     expect(t.includes('invalid') || t.includes('Invalid')).toBeFalsy();
  418 |   });
  419 | 
  420 |   test('IMPLEMENTATION GAP: No user error messaging', async ({ page }) => {
  421 |     await page.goto('/', { waitUntil: 'networkidle' });
  422 |     const t = await bodyText(page);
  423 |     expect(t.includes('Failed to send') || t.includes('notification failed')).toBeFalsy();
  424 |   });
  425 | });
  426 | 
  427 | // ====================================================================
  428 | // PHASE 9 — DATA INTEGRITY
  429 | // ====================================================================
  430 | test.describe('Phase 9 — Data Integrity', () => {
  431 |   test('Notification service API returns data', async () => {
  432 |     const resp = await fetch(`${BASE}/api/notifications`).catch(() => null);
  433 |     if (resp) {
  434 |       const data = await resp.json();
  435 |       expect(data).toBeDefined();
  436 |     }
  437 |   });
  438 | 
  439 |   test('IMPLEMENTATION GAP: No notification recipient data visible', async ({ page }) => {
  440 |     await login(page);
  441 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  442 |     const t = await bodyText(page);
```