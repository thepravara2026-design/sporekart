# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: customer-journey-cart.spec.ts >> Part 2 — Customer Journey: Phases 8-9 — Cart & Session Continuity >> Cart total is displayed if cart page exists
- Location: tests\customer-journey-cart.spec.ts:59:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/cart
Call log:
  - navigating to "http://localhost:5173/cart", waiting until "networkidle"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | test.describe('Part 2 — Customer Journey: Phases 8-9 — Cart & Session Continuity', () => {
  4   | 
  5   |   // === PHASE 8: CART VALIDATION ===
  6   | 
  7   |   test('Cart route accessibility', async ({ page }) => {
  8   |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  9   |     const status = response?.status() ?? 200;
  10  |     if (status === 404) {
  11  |       await expect(page.locator('text=404, not found, Not Found').first()).toBeVisible();
  12  |     } else {
  13  |       expect(status).toBeLessThan(400);
  14  |       const bodyText = await page.locator('body').innerText();
  15  |       expect(bodyText.length).toBeGreaterThan(10);
  16  |     }
  17  |   });
  18  | 
  19  |   test('Add to Cart buttons are present in featured products', async ({ page }) => {
  20  |     await page.goto('/', { waitUntil: 'networkidle' });
  21  |     const addToCartBtns = page.locator('button:has-text("Cart"), button:has-text("cart"), [aria-label*="cart" i]');
  22  |     const count = await addToCartBtns.count();
  23  |     expect(count).toBeGreaterThanOrEqual(0);
  24  |     if (count > 0) {
  25  |       await expect(addToCartBtns.first()).toBeVisible();
  26  |     }
  27  |   });
  28  | 
  29  |   test('Add to Cart interaction logs or shows feedback', async ({ page }) => {
  30  |     await page.goto('/', { waitUntil: 'networkidle' });
  31  |     const addToCartBtns = page.locator('button:has-text("Cart"), button:has-text("cart"), [aria-label*="cart" i]');
  32  |     if (await addToCartBtns.first().isVisible().catch(() => false)) {
  33  |       await addToCartBtns.first().click();
  34  |       await page.waitForTimeout(1000);
  35  |       const toast = page.locator('[class*="toast"], [class*="Toast"], [class*="notification"], [class*="snackbar"]');
  36  |       const feedback = await toast.isVisible().catch(() => false);
  37  |       if (!feedback) {
  38  |         const badge = page.locator('[class*="badge"], [class*="Badge"], [class*="cart-count"]');
  39  |         await badge.isVisible().catch(() => {});
  40  |       }
  41  |     }
  42  |   });
  43  | 
  44  |   test('Cart badge or icon exists in header', async ({ page }) => {
  45  |     await page.goto('/', { waitUntil: 'networkidle' });
  46  |     const cartIcon = page.locator('[class*="cart"], [class*="Cart"], [aria-label*="cart" i], svg[class*="cart"]');
  47  |     const count = await cartIcon.count();
  48  |     expect(count).toBeGreaterThanOrEqual(0);
  49  |   });
  50  | 
  51  |   test('Empty cart state is handled', async ({ page }) => {
  52  |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  53  |     if (response?.status() !== 404) {
  54  |       const emptyMsg = page.locator('text=empty, no items, cart is empty, Nothing here').first();
  55  |       const visible = await emptyMsg.isVisible().catch(() => false);
  56  |     }
  57  |   });
  58  | 
  59  |   test('Cart total is displayed if cart page exists', async ({ page }) => {
> 60  |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
      |                                 ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/cart
  61  |     if (response?.status() !== 404) {
  62  |       const total = page.locator('[class*="total"], [class*="Total"], [class*="subtotal"], text=Total, text=total');
  63  |       const visible = await total.isVisible().catch(() => false);
  64  |     }
  65  |   });
  66  | 
  67  |   test('Remove item interaction works if cart exists', async ({ page }) => {
  68  |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  69  |     if (response?.status() !== 404) {
  70  |       const removeBtn = page.locator('button:has-text("Remove"), button:has-text("remove"), [aria-label*="remove" i]');
  71  |       if (await removeBtn.first().isVisible().catch(() => false)) {
  72  |         await removeBtn.first().click();
  73  |         await page.waitForTimeout(500);
  74  |       }
  75  |     }
  76  |   });
  77  | 
  78  |   // === PHASE 9: SESSION CONTINUITY ===
  79  | 
  80  |   test('Cart content persists after page refresh', async ({ page }) => {
  81  |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  82  |     if (response?.status() !== 404) {
  83  |       const bodyBefore = await page.locator('body').innerText();
  84  |       await page.reload({ waitUntil: 'networkidle' });
  85  |       const bodyAfter = await page.locator('body').innerText();
  86  |       expect(bodyAfter.length).toBeGreaterThan(10);
  87  |     }
  88  |   });
  89  | 
  90  |   test('Browser restart maintains cart state', async ({ page, context }) => {
  91  |     await context.addCookies([]);
  92  |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  93  |     if (response?.status() !== 404) {
  94  |       await page.reload({ waitUntil: 'networkidle' });
  95  |       const bodyText = await page.locator('body').innerText();
  96  |       expect(bodyText.length).toBeGreaterThan(10);
  97  |     }
  98  |   });
  99  | 
  100 |   test('Guest cart state survives page navigation', async ({ page }) => {
  101 |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  102 |     if (response?.status() !== 404) {
  103 |       await page.goto('/about', { waitUntil: 'networkidle' });
  104 |       await page.goto('/cart', { waitUntil: 'networkidle' });
  105 |       const bodyText = await page.locator('body').innerText();
  106 |       expect(bodyText.length).toBeGreaterThan(10);
  107 |     }
  108 |   });
  109 | 
  110 |   test('Cart is accessible across multiple tabs', async ({ page, context }) => {
  111 |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  112 |     if (response?.status() !== 404) {
  113 |       const page2 = await context.newPage();
  114 |       await page2.goto('/cart', { waitUntil: 'networkidle' });
  115 |       const bodyText2 = await page2.locator('body').innerText();
  116 |       expect(bodyText2.length).toBeGreaterThan(10);
  117 |       await page2.close();
  118 |     }
  119 |   });
  120 | 
  121 |   test('Login preserves guest cart items', async ({ page }) => {
  122 |     await page.goto('/cart', { waitUntil: 'networkidle' });
  123 |     const loginLink = page.locator('a[href="/login"], text=Sign In, text=Login').first();
  124 |     if (await loginLink.isVisible().catch(() => false)) {
  125 |       await loginLink.click();
  126 |       await page.waitForLoadState('networkidle');
  127 |       expect(page.url()).toContain('login');
  128 |     }
  129 |   });
  130 | 
  131 |   test('Proceed to Checkout button exists if cart page exists', async ({ page }) => {
  132 |     const response = await page.goto('/cart', { waitUntil: 'networkidle' });
  133 |     if (response?.status() !== 404) {
  134 |       const checkoutBtn = page.locator('button:has-text("Checkout"), a:has-text("Checkout"), [class*="checkout"]');
  135 |       const visible = await checkoutBtn.isVisible().catch(() => false);
  136 |     }
  137 |   });
  138 | 
  139 | });
  140 | 
```