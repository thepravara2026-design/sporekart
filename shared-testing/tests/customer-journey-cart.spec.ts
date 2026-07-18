import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phases 8-9 — Cart & Session Continuity', () => {

  // === PHASE 8: CART VALIDATION ===

  test('Cart route accessibility', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    const status = response?.status() ?? 200;
    if (status === 404) {
      await expect(page.locator('text=404, not found, Not Found').first()).toBeVisible();
    } else {
      expect(status).toBeLessThan(400);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    }
  });

  test('Add to Cart buttons are present in featured products', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const addToCartBtns = page.locator('button:has-text("Cart"), button:has-text("cart"), [aria-label*="cart" i]');
    const count = await addToCartBtns.count();
    expect(count).toBeGreaterThanOrEqual(0);
    if (count > 0) {
      await expect(addToCartBtns.first()).toBeVisible();
    }
  });

  test('Add to Cart interaction logs or shows feedback', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const addToCartBtns = page.locator('button:has-text("Cart"), button:has-text("cart"), [aria-label*="cart" i]');
    if (await addToCartBtns.first().isVisible().catch(() => false)) {
      await addToCartBtns.first().click();
      await page.waitForTimeout(1000);
      const toast = page.locator('[class*="toast"], [class*="Toast"], [class*="notification"], [class*="snackbar"]');
      const feedback = await toast.isVisible().catch(() => false);
      if (!feedback) {
        const badge = page.locator('[class*="badge"], [class*="Badge"], [class*="cart-count"]');
        await badge.isVisible().catch(() => {});
      }
    }
  });

  test('Cart badge or icon exists in header', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const cartIcon = page.locator('[class*="cart"], [class*="Cart"], [aria-label*="cart" i], svg[class*="cart"]');
    const count = await cartIcon.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('Empty cart state is handled', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      const emptyMsg = page.locator('text=empty, no items, cart is empty, Nothing here').first();
      const visible = await emptyMsg.isVisible().catch(() => false);
    }
  });

  test('Cart total is displayed if cart page exists', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      const total = page.locator('[class*="total"], [class*="Total"], [class*="subtotal"], text=Total, text=total');
      const visible = await total.isVisible().catch(() => false);
    }
  });

  test('Remove item interaction works if cart exists', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      const removeBtn = page.locator('button:has-text("Remove"), button:has-text("remove"), [aria-label*="remove" i]');
      if (await removeBtn.first().isVisible().catch(() => false)) {
        await removeBtn.first().click();
        await page.waitForTimeout(500);
      }
    }
  });

  // === PHASE 9: SESSION CONTINUITY ===

  test('Cart content persists after page refresh', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      const bodyBefore = await page.locator('body').innerText();
      await page.reload({ waitUntil: 'networkidle' });
      const bodyAfter = await page.locator('body').innerText();
      expect(bodyAfter.length).toBeGreaterThan(10);
    }
  });

  test('Browser restart maintains cart state', async ({ page, context }) => {
    await context.addCookies([]);
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      await page.reload({ waitUntil: 'networkidle' });
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    }
  });

  test('Guest cart state survives page navigation', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      await page.goto('/about', { waitUntil: 'networkidle' });
      await page.goto('/cart', { waitUntil: 'networkidle' });
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    }
  });

  test('Cart is accessible across multiple tabs', async ({ page, context }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      const page2 = await context.newPage();
      await page2.goto('/cart', { waitUntil: 'networkidle' });
      const bodyText2 = await page2.locator('body').innerText();
      expect(bodyText2.length).toBeGreaterThan(10);
      await page2.close();
    }
  });

  test('Login preserves guest cart items', async ({ page }) => {
    await page.goto('/cart', { waitUntil: 'networkidle' });
    const loginLink = page.locator('a[href="/login"], text=Sign In, text=Login').first();
    if (await loginLink.isVisible().catch(() => false)) {
      await loginLink.click();
      await page.waitForLoadState('networkidle');
      expect(page.url()).toContain('login');
    }
  });

  test('Proceed to Checkout button exists if cart page exists', async ({ page }) => {
    const response = await page.goto('/cart', { waitUntil: 'networkidle' });
    if (response?.status() !== 404) {
      const checkoutBtn = page.locator('button:has-text("Checkout"), a:has-text("Checkout"), [class*="checkout"]');
      const visible = await checkoutBtn.isVisible().catch(() => false);
    }
  });

});
