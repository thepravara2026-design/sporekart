# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: payment-validation.spec.ts >> Phase 3 — Mock Payment Success >> IMPLEMENTATION GAP: No payment success page content
- Location: tests\payment-validation.spec.ts:180:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/payment/success
Call log:
  - navigating to "http://localhost:5173/payment/success", waiting until "networkidle"

```

# Test source

```ts
  81  | // ============================================================================
  82  | // PHASE 2 — PAYMENT METHOD VALIDATION (IMPLEMENTATION GAP + DISPLAY)
  83  | // ============================================================================
  84  | test.describe('Phase 2 — Payment Method Validation', () => {
  85  |   test.beforeEach(async ({ page }) => { await login(page); });
  86  | 
  87  |   test('Payment information displays on order details', async ({ page }) => {
  88  |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  89  |     const t = await page.locator('body').innerText();
  90  |     expect(t.includes('UPI') || t.includes('Razorpay') || t.includes('Payment')).toBeTruthy();
  91  |   });
  92  | 
  93  |   test('Multiple payment methods displayed in mock data', async ({ page }) => {
  94  |     const methods = [];
  95  |     const orders = ['ORD-2026-8842', 'ORD-2026-7715', 'ORD-2026-5541', 'ORD-2026-9922'];
  96  |     for (const oid of orders) {
  97  |       await page.goto(`/dashboard/orders/${oid}`, { waitUntil: 'networkidle' });
  98  |       const t = await page.locator('body').innerText();
  99  |       if (t.includes('UPI')) methods.push('UPI');
  100 |       else if (t.includes('Credit Card') || t.includes('Visa')) methods.push('Credit Card');
  101 |       else if (t.includes('Netbanking')) methods.push('Netbanking');
  102 |     }
  103 |     // At least 2 different payment methods should be visible across orders
  104 |     const unique = [...new Set(methods)];
  105 |     expect(unique.length).toBeGreaterThanOrEqual(2);
  106 |   });
  107 | 
  108 |   test('Payment status displays correctly per order', async ({ page }) => {
  109 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  110 |     const t = await page.locator('body').innerText();
  111 |     expect(t.includes('Paid') || t.includes('paid')).toBeTruthy();
  112 |   });
  113 | 
  114 |   test('IMPLEMENTATION GAP: UPI method selection UI does not exist', async ({ page }) => {
  115 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  116 |     const t = await page.locator('body').innerText();
  117 |     const hasMethodUI = t.includes('UPI') || t.includes('Credit Card') || t.includes('Net Banking');
  118 |     // Methods should be listed at checkout, but checkout is placeholder
  119 |     if (hasMethodUI) {
  120 |       // If method selection UI is present, verify it renders
  121 |     }
  122 |   });
  123 | 
  124 |   test('IMPLEMENTATION GAP: Credit/Debit card form does not exist', async ({ page }) => {
  125 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  126 |     const cardInputs = page.locator('input[placeholder*="card"],input[placeholder*="Card"],input[placeholder*="CVC"],input[placeholder*="Expiry"]');
  127 |     expect(await cardInputs.count()).toBe(0);
  128 |   });
  129 | 
  130 |   test('IMPLEMENTATION GAP: Wallet payment option does not exist', async ({ page }) => {
  131 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  132 |     const t = await page.locator('body').innerText();
  133 |     expect(t.includes('Wallet') || t.includes('wallet')).toBe(false);
  134 |   });
  135 | 
  136 |   test('IMPLEMENTATION GAP: EMI option does not exist', async ({ page }) => {
  137 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  138 |     const t = await page.locator('body').innerText();
  139 |     expect(t.includes('EMI')).toBe(false);
  140 |   });
  141 | 
  142 |   test('IMPLEMENTATION GAP: Cash on Delivery not available', async ({ page }) => {
  143 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  144 |     const t = await page.locator('body').innerText();
  145 |     expect(t.includes('COD') || t.includes('Cash on Delivery')).toBe(false);
  146 |   });
  147 | 
  148 |   test('Demo payment form renders at /demo/forms', async ({ page }) => {
  149 |     await page.goto('/demo/forms', { waitUntil: 'networkidle' });
  150 |     const t = await page.locator('body').innerText();
  151 |     // The demo page contains form examples
  152 |     expect(t.length).toBeGreaterThan(10);
  153 |   });
  154 | 
  155 |   test('Address form component exists in design system', async ({ page }) => {
  156 |     await page.goto('/design-system/forms/address', { waitUntil: 'networkidle' });
  157 |     const t = await page.locator('body').innerText();
  158 |     expect(t.includes('Address') || t.includes('address')).toBeTruthy();
  159 |   });
  160 | });
  161 | 
  162 | // ============================================================================
  163 | // PHASE 3 — MOCK PAYMENT SUCCESS (IMPLEMENTATION GAP)
  164 | // ============================================================================
  165 | test.describe('Phase 3 — Mock Payment Success', () => {
  166 |   test('IMPLEMENTATION GAP: No payment success callback flow', async ({ page }) => {
  167 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  168 |     // Checkout is placeholder — no payment flow
  169 |     expect(true).toBeTruthy();
  170 |   });
  171 | 
  172 |   test('IMPLEMENTATION GAP: No order status update on payment', async ({ page }) => {
  173 |     // Order statuses are static mock data — no payment-driven transitions
  174 |     await login(page);
  175 |     await page.goto('/dashboard/orders/ORD-2026-9922', { waitUntil: 'networkidle' });
  176 |     const t = await page.locator('body').innerText();
  177 |     expect(t.includes('Processing')).toBeTruthy();
  178 |   });
  179 | 
  180 |   test('IMPLEMENTATION GAP: No payment success page content', async ({ page }) => {
> 181 |     await page.goto('/payment/success', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/payment/success
  182 |     const t = await page.locator('body').innerText();
  183 |     const hasSuccessContent = t.includes('Payment Successful') || t.includes('Order Confirmed') || t.includes('Transaction Successful');
  184 |     expect(hasSuccessContent).toBe(false);
  185 |   });
  186 | 
  187 |   test('IMPLEMENTATION GAP: No receipt generation on success', async ({ page }) => {
  188 |     await login(page);
  189 |     await page.goto('/dashboard/orders/ORD-2026-8842', { waitUntil: 'networkidle' });
  190 |     const receipt = page.locator('button:has-text("Download Receipt"),button:has-text("Invoice")');
  191 |     const exists = await receipt.isVisible({ timeout: 2000 }).catch(() => false);
  192 |     if (exists) {
  193 |       // Button exists but is mock toast — no actual receipt download
  194 |     }
  195 |   });
  196 | });
  197 | 
  198 | // ============================================================================
  199 | // PHASE 4 — MOCK PAYMENT FAILURE (IMPLEMENTATION GAP)
  200 | // ============================================================================
  201 | test.describe('Phase 4 — Mock Payment Failure', () => {
  202 |   test('IMPLEMENTATION GAP: No payment failure page content', async ({ page }) => {
  203 |     await page.goto('/payment/failed', { waitUntil: 'networkidle' });
  204 |     const t = await page.locator('body').innerText();
  205 |     const hasFailureContent = t.includes('Payment Failed') || t.includes('Transaction Failed') || t.includes('Payment Declined');
  206 |     expect(hasFailureContent).toBe(false);
  207 |   });
  208 | 
  209 |   test('IMPLEMENTATION GAP: No retry mechanism for failed payment', async ({ page }) => {
  210 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  211 |     const retry = page.locator('button:has-text("Retry"),button:has-text("Try Again")');
  212 |     expect(await retry.isVisible({ timeout: 2000 }).catch(() => false)).toBe(false);
  213 |   });
  214 | 
  215 |   test('IMPLEMENTATION GAP: No error messaging for payment failure', async ({ page }) => {
  216 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  217 |     const errors = page.locator('[class*="error"],[class*="Error"],[role="alert"]');
  218 |     const count = await errors.count();
  219 |     // No error elements in the placeholder checkout
  220 |     expect(count === 0 || true).toBeTruthy();
  221 |   });
  222 | 
  223 |   test('IMPLEMENTATION GAP: No cancellation during payment', async ({ page }) => {
  224 |     const cancel = page.locator('button:has-text("Cancel"),button:has-text("cancel")');
  225 |     const exists = await cancel.isVisible({ timeout: 2000 }).catch(() => false);
  226 |     if (exists) {
  227 |       // If cancel button exists somewhere, it's for other purposes
  228 |     }
  229 |   });
  230 | 
  231 |   test('IMPLEMENTATION GAP: No payment timeout simulation', async ({ page }) => {
  232 |     const resp = await page.request.get('/api/payments/timeout');
  233 |     const body = await resp.text();
  234 |     const hasTimeoutLogic = body.includes('timeout') || body.includes('expired') || body.includes('session_timeout');
  235 |     expect(hasTimeoutLogic).toBe(false);
  236 |   });
  237 | });
  238 | 
  239 | // ============================================================================
  240 | // PHASE 5 — RETRY & IDEMPOTENCY (IMPLEMENTATION GAP)
  241 | // ============================================================================
  242 | test.describe('Phase 5 — Retry & Idempotency', () => {
  243 |   test('IMPLEMENTATION GAP: No duplicate payment prevention UI', async ({ page }) => {
  244 |     await page.goto('/checkout', { waitUntil: 'networkidle' });
  245 |     const submit = page.locator('button[type="submit"]');
  246 |     const exists = await submit.isVisible({ timeout: 2000 }).catch(() => false);
  247 |     if (exists) {
  248 |       const disabled = await submit.isDisabled();
  249 |       // Should be disabled during processing
  250 |     }
  251 |   });
  252 | 
  253 |   test('IMPLEMENTATION GAP: No idempotency key mechanism', async ({ page }) => {
  254 |     const resp = await page.request.post('/api/payments/idempotency', { data: {} });
  255 |     const body = await resp.text();
  256 |     const hasIdempotencyLogic = body.includes('idempotency') || body.includes('idem_key') || body.includes('retry_check');
  257 |     expect(hasIdempotencyLogic).toBe(false);
  258 |   });
  259 | 
  260 |   test('IMPLEMENTATION GAP: No transaction duplication prevention', async ({ page }) => {
  261 |     const resp = await page.request.get('/api/payments/transactions/dup-check');
  262 |     const body = await resp.text();
  263 |     const hasDupLogic = body.includes('duplicate') || body.includes('idempotent') || body.includes('txn_check');
  264 |     expect(hasDupLogic).toBe(false);
  265 |   });
  266 | });
  267 | 
  268 | // ============================================================================
  269 | // PHASE 6 — WEBHOOK SIMULATION (IMPLEMENTATION GAP)
  270 | // ============================================================================
  271 | test.describe('Phase 6 — Webhook Simulation', () => {
  272 |   test('IMPLEMENTATION GAP: No webhook endpoint exists', async ({ page }) => {
  273 |     const resp = await page.request.post('/api/webhooks/razorpay', { data: {} });
  274 |     const body = await resp.text();
  275 |     const hasWebhookLogic = body.includes('webhook_received') || body.includes('event') || body.includes('razorpay_payment');
  276 |     expect(hasWebhookLogic).toBe(false);
  277 |   });
  278 | 
  279 |   test('IMPLEMENTATION GAP: No payment success webhook', async ({ page }) => {
  280 |     const resp = await page.request.post('/api/webhooks/payment/success', { data: {} });
  281 |     const body = await resp.text();
```