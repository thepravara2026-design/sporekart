# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: payment-validation.spec.ts >> Phase 8 — Security >> Dashboard accessible without auth (known defect)
- Location: tests\payment-validation.spec.ts:378:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/dashboard/orders
Call log:
  - navigating to "http://localhost:5173/dashboard/orders", waiting until "networkidle"

```

# Test source

```ts
  280 |     const resp = await page.request.post('/api/webhooks/payment/success', { data: {} });
  281 |     const body = await resp.text();
  282 |     const hasSuccessWebhook = body.includes('payment_captured') || body.includes('order_paid') || body.includes('success_webhook');
  283 |     expect(hasSuccessWebhook).toBe(false);
  284 |   });
  285 | 
  286 |   test('IMPLEMENTATION GAP: No payment failure webhook', async ({ page }) => {
  287 |     const resp = await page.request.post('/api/webhooks/payment/failed', { data: {} });
  288 |     const body = await resp.text();
  289 |     const hasFailureWebhook = body.includes('payment_failed') || body.includes('order_failed') || body.includes('failure_webhook');
  290 |     expect(hasFailureWebhook).toBe(false);
  291 |   });
  292 | 
  293 |   test('IMPLEMENTATION GAP: No webhook signature verification', async ({ page }) => {
  294 |     const resp = await page.request.get('/api/webhooks/signature');
  295 |     const body = await resp.text();
  296 |     const hasSignatureLogic = body.includes('signature') || body.includes('hmac') || body.includes('verify_webhook');
  297 |     expect(hasSignatureLogic).toBe(false);
  298 |   });
  299 | });
  300 | 
  301 | // ============================================================================
  302 | // PHASE 7 — ORDER SYNCHRONIZATION (DISPLAY ONLY)
  303 | // ============================================================================
  304 | test.describe('Phase 7 — Order Synchronization', () => {
  305 |   test.beforeEach(async ({ page }) => { await login(page); });
  306 | 
  307 |   test('Payment info accessible from orders area', async ({ page }) => {
  308 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  309 |     const t = await page.locator('body').innerText();
  310 |     expect(t.length).toBeGreaterThan(0);
  311 | 
  312 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  313 |     const detailText = await page.locator('body').innerText();
  314 |     expect(detailText.includes('UPI') || detailText.includes('Razorpay') || detailText.includes('Payment') || detailText.includes('Visa')).toBeTruthy();
  315 |   });
  316 | 
  317 |   test('Transaction ID displayed on order detail', async ({ page }) => {
  318 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  319 |     const t = await page.locator('body').innerText();
  320 |     expect(t.includes('TXN') || t.includes('Transaction') || t.includes('transaction')).toBeTruthy();
  321 |   });
  322 | 
  323 |   test('Amount consistency across orders', async ({ page }) => {
  324 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  325 |     const t = await page.locator('body').innerText();
  326 |     expect(t.includes('₹') || t.includes('INR')).toBeTruthy();
  327 |   });
  328 | 
  329 |   test('IMPLEMENTATION GAP: No payment-to-order real-time sync', async ({ page }) => {
  330 |     const resp = await page.request.get('/api/payments/sync-status');
  331 |     const body = await resp.text();
  332 |     const hasSyncLogic = body.includes('sync_status') || body.includes('payment_sync') || body.includes('real_time');
  333 |     expect(hasSyncLogic).toBe(false);
  334 |   });
  335 | });
  336 | 
  337 | // ============================================================================
  338 | // PHASE 8 — SECURITY
  339 | // ============================================================================
  340 | test.describe('Phase 8 — Security', () => {
  341 |   test('Mock environment does not expose production API keys', async ({ page }) => {
  342 |     // Check all script content for production Razorpay keys
  343 |     await page.goto('/');
  344 |     const html = await page.locator('html').innerHTML();
  345 |     const prodPatterns = ['rzp_live_', 'sk_live_', 'pk_live_'];
  346 |     for (const p of prodPatterns) {
  347 |       expect(html.includes(p)).toBe(false);
  348 |     }
  349 |   });
  350 | 
  351 |   test('No payment API keys in client-side source', async ({ page }) => {
  352 |     await page.goto('/');
  353 |     const scripts = await page.locator('script').allInnerTexts();
  354 |     for (const s of scripts) {
  355 |       if (s.includes('razorpay_key') || s.includes('RAZORPAY_KEY')) {
  356 |         const val = s.match(/['"]razorpay_key['"]\s*:\s*['"]([^'"]+)['"]/);
  357 |         if (val && val[1].startsWith('rzp_live_')) {
  358 |           throw new Error(`Production Razorpay key exposed: ${val[1]}`);
  359 |         }
  360 |       }
  361 |     }
  362 |   });
  363 | 
  364 |   test('IMPLEMENTATION GAP: No transaction tampering protection', async ({ page }) => {
  365 |     const resp = await page.request.post('/api/payments/verify', { data: { amount: 1, orderId: 'test' } });
  366 |     const body = await resp.text();
  367 |     const hasVerifyLogic = body.includes('signature_verified') || body.includes('payment_verified') || body.includes('razorpay_signature');
  368 |     expect(hasVerifyLogic).toBe(false);
  369 |   });
  370 | 
  371 |   test('IMPLEMENTATION GAP: No parameter tampering protection in payment init', async ({ page }) => {
  372 |     const resp = await page.request.post('/api/payments/create-order', { data: { amount: 0, currency: 'INVALID' } });
  373 |     const body = await resp.text();
  374 |     const hasValidationLogic = body.includes('invalid') || body.includes('error') || body.includes('validation_failed');
  375 |     expect(hasValidationLogic).toBe(false);
  376 |   });
  377 | 
  378 |   test('Dashboard accessible without auth (known defect)', async ({ page }) => {
  379 |     // Carryover from Part 3 — BUG-CHK-001
> 380 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/dashboard/orders
  381 |     expect(page.url().includes('login')).toBe(false);
  382 |   });
  383 | 
  384 |   test('Checkout accessible without auth (expected — placeholder)', async ({ page }) => {
  385 |     // Placeholder checkout page renders without auth
  386 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  387 |     const t = await page.locator('body').innerText();
  388 |     expect(t.length).toBeGreaterThan(0);
  389 |   });
  390 | });
  391 | 
  392 | // ============================================================================
  393 | // PHASE 9 — CROSS-BROWSER
  394 | // ============================================================================
  395 | test.describe('Phase 9 — Cross-Browser', () => {
  396 |   test('Order detail renders payment info at desktop', async ({ page }) => {
  397 |     await login(page);
  398 |     await page.setViewportSize({ width: 1440, height: 900 });
  399 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  400 |     const t = await page.locator('body').innerText();
  401 |     expect(t.includes('Payment') || t.includes('payment')).toBeTruthy();
  402 |   });
  403 | 
  404 |   test('Order detail renders payment info at mobile', async ({ page }) => {
  405 |     await login(page);
  406 |     await page.setViewportSize({ width: 375, height: 667 });
  407 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  408 |     const t = await page.locator('body').innerText();
  409 |     expect(t.includes('Payment') || t.includes('payment')).toBeTruthy();
  410 |   });
  411 | 
  412 |   test('Checkout placeholder renders at all viewports', async ({ page }) => {
  413 |     for (const vp of [{ w: 1440, h: 900 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  414 |       await page.setViewportSize({ width: vp.w, height: vp.h });
  415 |       await page.goto('/checkout', { waitUntil: 'networkidle' });
  416 |       expect((await page.locator('body').innerText()).length).toBeGreaterThan(0);
  417 |     }
  418 |   });
  419 | });
  420 | 
  421 | // ============================================================================
  422 | // PHASE 10 — ACCESSIBILITY
  423 | // ============================================================================
  424 | test.describe('Phase 10 — Accessibility', () => {
  425 |   test.beforeEach(async ({ page }) => { await login(page); });
  426 | 
  427 |   test('Payment info section has ARIA landmarks on order detail', async ({ page }) => {
  428 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  429 |     await expect(page.locator('main,[role="main"]').first()).toBeVisible({ timeout: 5000 });
  430 |   });
  431 | 
  432 |   test('Skip to content link on orders dashboard', async ({ page }) => {
  433 |     await page.goto('/dashboard/orders', { waitUntil: 'networkidle' });
  434 |     const l = page.locator('a[href="#main-content"],a[href="#content"],a[href="#main"],a:has-text("Skip"),[class*="skip"]');
  435 |     await expect(l.first()).toBeVisible({ timeout: 5000 });
  436 |   });
  437 | 
  438 |   test('Images have alt text on payment-related pages', async ({ page }) => {
  439 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  440 |     const imgs = page.locator('img');
  441 |     const c = await imgs.count();
  442 |     let missing = 0;
  443 |     for (let i = 0; i < c; i++) {
  444 |       const alt = await imgs.nth(i).getAttribute('alt');
  445 |       if (alt === null || alt === undefined) missing++;
  446 |     }
  447 |     expect(missing).toBe(0);
  448 |   });
  449 | 
  450 |   test('Checkout placeholder has descriptive heading', async ({ page }) => {
  451 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  452 |     const h1 = page.locator('h1');
  453 |     expect(await h1.isVisible()).toBeTruthy();
  454 |   });
  455 | });
  456 | 
  457 | // ============================================================================
  458 | // PHASE 11 — PERFORMANCE
  459 | // ============================================================================
  460 | test.describe('Phase 11 — Performance', () => {
  461 |   test('Order detail with payment info loads within 15s', async ({ page }) => {
  462 |     const start = Date.now();
  463 |     await login(page);
  464 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  465 |     expect(Date.now() - start).toBeLessThan(20000);
  466 |   });
  467 | 
  468 |   test('No console errors on order detail with payment info', async ({ page }) => {
  469 |     const errors = [];
  470 |     page.on('console', m => { if (m.type() === 'error') errors.push(m.text()); });
  471 |     await login(page);
  472 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  473 |     expect(errors.length).toBe(0);
  474 |   });
  475 | 
  476 |   test('No failed network requests on payment-related pages', async ({ page }) => {
  477 |     const fails = [];
  478 |     page.on('requestfailed', r => fails.push(r.url()));
  479 |     await login(page);
  480 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
```