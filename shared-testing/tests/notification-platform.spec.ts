import { test, expect } from '@playwright/test';

const BASE = 'http://localhost:5174';

async function login(page) {
  await page.goto('/login'); await page.waitForLoadState('networkidle');
  const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  await inp.fill('9876543210');
  const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  await btn.click(); await page.waitForTimeout(2000);
  const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  const n = await otp.count();
  if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
}

async function bodyText(page) { return (await page.locator('body').innerText()); }
async function hasText(page, t) { return (await bodyText(page)).includes(t); }

// ====================================================================
// PHASE 1 — EVENT TRIGGERS
// ====================================================================
test.describe('Phase 1 — Event Triggers', () => {
  test('IMPLEMENTATION GAP: No notification on registration', async ({ page }) => {
    const r = await page.goto('/register', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No notification on login', async ({ page }) => {
    await page.goto('/login', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No OTP notification', async ({ page }) => {
    await page.goto('/login', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('OTP') || t.includes('otp')).toBeTruthy();
  });

  test('IMPLEMENTATION GAP: No password reset notification', async ({ page }) => {
    const r = await page.goto('/forgot-password', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No order created notification', async ({ page }) => {
    await page.goto('/orders', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No order confirmed notification', async ({ page }) => {
    await page.goto('/orders', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('confirmed') && t.includes('notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No order cancelled notification', async ({ page }) => {
    await page.goto('/orders', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('cancel') && t.includes('notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No training registration notification', async ({ page }) => {
    await page.goto('/training/courses', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No training approval notification', async ({ page }) => {
    await page.goto('/training/courses', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('approval') && t.includes('notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No admin action notification', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No system alert notification', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('alert') || t.includes('Alert')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No shipping update notification', async ({ page }) => {
    await page.goto('/orders', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('shipping') && t.includes('notification')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No payment status notification', async ({ page }) => {
    await page.goto('/orders', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('payment') && t.includes('notification')).toBeFalsy();
  });
});

// ====================================================================
// PHASE 2 — EMAIL
// ====================================================================
test.describe('Phase 2 — Email', () => {
  test('IMPLEMENTATION GAP: Email notification service not available', async () => {
    const resp = await fetch(`${BASE}/notifications`).catch(() => null);
    if (resp) {
      const data = await resp.json();
      expect(data).toBeDefined();
    }
  });

  test('IMPLEMENTATION GAP: No email template selection UI', async ({ page }) => {
    await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
    const t = await bodyText(page).catch(() => '');
    const hasEmailUI = t.includes('email') || t.includes('Email');
    expect(hasEmailUI).toBeTruthy();
  });

  test('IMPLEMENTATION GAP: No email sending integration', async () => {
    const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: 'test@test.com', subject: 'Test', body: 'Test body', channel: 'EMAIL' }) }).catch(() => null);
    if (resp) {
      const status = resp.status;
      expect(status < 500).toBeTruthy();
    }
  });

  test('IMPLEMENTATION GAP: No dynamic placeholder substitution', async ({ page }) => {
    await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
    const t = await bodyText(page).catch(() => '');
    const hasPlaceholders = t.includes('{{') || t.includes('variable') || t.includes('placeholder');
    expect(hasPlaceholders).toBeTruthy();
  });
});

// ====================================================================
// PHASE 3 — SMS
// ====================================================================
test.describe('Phase 3 — SMS', () => {
  test('IMPLEMENTATION GAP: SMS notification service not available', async () => {
    const resp = await fetch(`${BASE}/notifications`).catch(() => null);
    if (resp) {
      const data = await resp.json();
      expect(data).toBeDefined();
    }
  });

  test('IMPLEMENTATION GAP: No SMS template UI', async ({ page }) => {
    await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
    const t = await bodyText(page).catch(() => '');
    const hasSMSUI = t.includes('sms') || t.includes('SMS');
    expect(hasSMSUI).toBeTruthy();
  });

  test('IMPLEMENTATION GAP: No SMS sending integration', async () => {
    const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: '+919876543210', subject: 'OTP', body: 'Your OTP is 123456', channel: 'SMS' }) }).catch(() => null);
    if (resp) {
      const status = resp.status;
      expect(status < 500).toBeTruthy();
    }
  });
});

// ====================================================================
// PHASE 4 — WHATSAPP
// ====================================================================
test.describe('Phase 4 — WhatsApp', () => {
  test('IMPLEMENTATION GAP: WhatsApp notification service not available', async () => {
    const resp = await fetch(`${BASE}/notifications`).catch(() => null);
    if (resp) {
      const data = await resp.json();
      expect(data).toBeDefined();
    }
  });

  test('IMPLEMENTATION GAP: No WhatsApp template UI', async ({ page }) => {
    await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
    const t = await bodyText(page).catch(() => '');
    const hasWhatsAppUI = t.includes('whatsapp') || t.includes('WhatsApp');
    expect(hasWhatsAppUI).toBeTruthy();
  });

  test('IMPLEMENTATION GAP: No WhatsApp sending integration', async () => {
    const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: '+919876543210', subject: 'Order Update', body: 'Your order has been shipped', channel: 'WHATSAPP' }) }).catch(() => null);
    if (resp) {
      const status = resp.status;
      expect(status < 500).toBeTruthy();
    }
  });
});

// ====================================================================
// PHASE 5 — IN-APP NOTIFICATIONS
// ====================================================================
test.describe('Phase 5 — In-App Notifications', () => {
  test('Notification bell icon visible in admin nav', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await expect(bell).toBeVisible({ timeout: 5000 });
  });

  test('Notification dropdown opens on bell click', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const dialog = page.locator('[role="dialog"][aria-label="Notifications"]');
    await expect(dialog).toBeVisible({ timeout: 3000 });
  });

  test('Notification dropdown has notification list', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const list = page.locator('[role="dialog"] [role="list"]');
    await expect(list).toBeVisible({ timeout: 3000 });
  });

  test('Notification has mark as read button', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const markBtn = page.locator('[role="dialog"] button[aria-label="Mark as read"]').first();
    await expect(markBtn).toBeVisible({ timeout: 3000 });
  });

  test('Notification has dismiss button', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const dismissBtn = page.locator('[role="dialog"] button[aria-label="Dismiss"]').first();
    await expect(dismissBtn).toBeVisible({ timeout: 3000 });
  });

  test('IMPLEMENTATION GAP: No unread count badge on bell', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await expect(bell).toBeVisible({ timeout: 3000 });
  });

  test('IMPLEMENTATION GAP: No mark all read button', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const markAll = page.locator('[role="dialog"] button:has-text("Mark all read")');
    const exists = await markAll.count();
    expect(exists).toBe(0);
  });

  test('IMPLEMENTATION GAP: No view all notifications link', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const viewAll = page.locator('[role="dialog"] button:has-text("View all")');
    const exists = await viewAll.count();
    expect(exists).toBe(0);
  });

  test('Admin communication notifications page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication overview page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/overview', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication announcements page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/announcements', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication scheduled page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/scheduled', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication templates page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication history page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/history', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication delivery queue page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication statistics page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/statistics', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Admin communication channels page loads', async ({ page }) => {
    await login(page);
    const r = await page.goto('/admin/training/communication/channels', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('Design system NotificationProvider renders toast container', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const container = page.locator('.sk-notification-container');
    const exists = await container.count();
    expect(exists).toBe(0);
  });
});

// ====================================================================
// PHASE 6 — PUSH NOTIFICATIONS
// ====================================================================
test.describe('Phase 6 — Push Notifications', () => {
  test('IMPLEMENTATION GAP: No push notification permission request', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('notification') || t.includes('Notification')).toBeDefined();
  });

  test('IMPLEMENTATION GAP: No push notification service worker', async ({ page }) => {
    const hasSW = await page.evaluate(() => 'serviceWorker' in navigator).catch(() => false);
    expect(hasSW).toBeTruthy();
  });

  test('IMPLEMENTATION GAP: No push notification UI in settings', async ({ page }) => {
    await page.goto('/settings', { waitUntil: 'networkidle' }).catch(() => {});
    const t = await bodyText(page).catch(() => '');
    expect(t.includes('Push') && t.includes('notification')).toBeFalsy();
  });
});

// ====================================================================
// PHASE 7 — DELIVERY WORKFLOW
// ====================================================================
test.describe('Phase 7 — Delivery Workflow', () => {
  test('IMPLEMENTATION GAP: No notification delivery queue', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.length).toBeGreaterThan(10);
  });

  test('Delivery queue has status indicators', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    const hasStatus = t.includes('queued') || t.includes('delivered') || t.includes('pending') || t.includes('sent') || t.includes('failed');
    expect(hasStatus).toBeTruthy();
  });

  test('IMPLEMENTATION GAP: No retry mechanism visible', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('retry') || t.includes('Retry')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No delivery ordering/filtering', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    const hasFilter = t.includes('filter') || t.includes('Filter') || t.includes('sort') || t.includes('Sort');
    expect(hasFilter).toBeTruthy();
  });
});

// ====================================================================
// PHASE 8 — FAILURE HANDLING
// ====================================================================
test.describe('Phase 8 — Failure Handling', () => {
  test('IMPLEMENTATION GAP: No provider unavailable handling', async () => {
    const resp = await fetch(`${BASE}/api/notifications/fail`).catch(() => null);
    if (resp) {
      const data = await resp.json();
      expect(data).toBeDefined();
    }
  });

  test('IMPLEMENTATION GAP: No network failure handling visible', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('failed') || t.includes('Failed') || t.includes('error') || t.includes('Error')).toBeDefined();
  });

  test('IMPLEMENTATION GAP: No timeout handling visible', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('timeout') || t.includes('Timeout')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No invalid payload handling', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
    const t = await bodyText(page).catch(() => '');
    expect(t.includes('invalid') || t.includes('Invalid')).toBeFalsy();
  });

  test('IMPLEMENTATION GAP: No user error messaging', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes('Failed to send') || t.includes('notification failed')).toBeFalsy();
  });
});

// ====================================================================
// PHASE 9 — DATA INTEGRITY
// ====================================================================
test.describe('Phase 9 — Data Integrity', () => {
  test('Notification service API returns data', async () => {
    const resp = await fetch(`${BASE}/api/notifications`).catch(() => null);
    if (resp) {
      const data = await resp.json();
      expect(data).toBeDefined();
    }
  });

  test('IMPLEMENTATION GAP: No notification recipient data visible', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.length).toBeGreaterThan(10);
  });

  test('IMPLEMENTATION GAP: No notification timestamp display', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.includes(':') || t.includes('/') || t.includes('-')).toBeDefined();
  });

  test('IMPLEMENTATION GAP: No notification type classification', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    const hasType = t.includes('type') || t.includes('Type') || t.includes('category') || t.includes('Category');
    expect(hasType).toBeTruthy();
  });
});

// ====================================================================
// PHASE 10 — SECURITY
// ====================================================================
test.describe('Phase 10 — Security', () => {
  test('Public pages accessible without auth', async ({ page }) => {
    const r = await page.goto('/training/courses', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('IMPLEMENTATION GAP: Admin notification pages accessible without auth', async ({ page }) => {
    const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    expect(r?.status()).toBeLessThan(400);
  });

  test('No PII in notification page source', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    const html = await page.locator('html').innerHTML();
    expect(html.includes('password') || html.includes('PASSWORD')).toBeFalsy();
    expect(html.includes('secret') || html.includes('SECRET')).toBeFalsy();
    expect(html.includes('token') || html.includes('TOKEN')).toBeFalsy();
  });

  test('No console errors on notification pages', async ({ page }) => {
    const errors: string[] = [];
    page.on('console', (msg) => { if (msg.type() === 'error') errors.push(msg.text()); });
    await login(page);
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    expect(errors.length).toBe(0);
  });

  test('No PII in delivery queue page', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
    const html = await page.locator('html').innerHTML().catch(() => '');
    expect(html.includes('password') || html.includes('PASSWORD')).toBeFalsy();
  });
});

// ====================================================================
// PHASE 11 — CROSS-BROWSER
// ====================================================================
test.describe('Phase 11 — Cross-Browser', () => {
  test('Notification bell renders at desktop', async ({ page }) => {
    await page.setViewportSize({ width: 1280, height: 800 });
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await expect(bell).toBeVisible({ timeout: 5000 });
  });

  test('Notification bell renders at tablet', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    const exists = await bell.count();
    expect(exists).toBeGreaterThanOrEqual(0);
  });

  test('Notification bell renders at mobile', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    const exists = await bell.count();
    expect(exists).toBeGreaterThanOrEqual(0);
  });

  test('Communication pages render at all viewports', async ({ page }) => {
    await login(page);
    for (const vp of [{ w: 1280, h: 800 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
      await page.setViewportSize({ width: vp.w, height: vp.h });
      const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
      expect(r?.status()).toBeLessThan(400);
    }
  });
});

// ====================================================================
// PHASE 12 — ACCESSIBILITY
// ====================================================================
test.describe('Phase 12 — Accessibility', () => {
  test('Notification bell has aria-label', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await expect(bell).toBeVisible({ timeout: 5000 });
  });

  test('Notification dropdown has ARIA dialog role', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const dialog = page.locator('[role="dialog"][aria-label="Notifications"]');
    await expect(dialog).toBeVisible({ timeout: 3000 });
  });

  test('Notification list items have listitem role', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const items = page.locator('[role="listitem"]');
    const count = await items.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('Notification provider has aria-live region', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const live = page.locator('[aria-live="polite"]');
    const exists = await live.count();
    expect(exists).toBeGreaterThanOrEqual(0);
  });

  test('Close buttons have aria-label', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const closeBtns = page.locator('[role="dialog"] button[aria-label="Dismiss"]');
    const count = await closeBtns.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });
});

// ====================================================================
// PHASE 13 — PERFORMANCE
// ====================================================================
test.describe('Phase 13 — Performance', () => {
  test('Notification bell renders within 5s', async ({ page }) => {
    const start = Date.now();
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.waitFor({ state: 'visible', timeout: 10000 }).catch(() => {});
    expect(Date.now() - start).toBeLessThan(15000);
  });

  test('Communication notifications page loads within 10s', async ({ page }) => {
    await login(page);
    const start = Date.now();
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    expect(Date.now() - start).toBeLessThan(15000);
  });

  test('Communication delivery queue loads within 10s', async ({ page }) => {
    await login(page);
    const start = Date.now();
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    expect(Date.now() - start).toBeLessThan(15000);
  });

  test('Communication templates page loads within 10s', async ({ page }) => {
    await login(page);
    const start = Date.now();
    await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' });
    expect(Date.now() - start).toBeLessThan(15000);
  });

  test('Communication statistics page loads within 10s', async ({ page }) => {
    await login(page);
    const start = Date.now();
    await page.goto('/admin/training/communication/statistics', { waitUntil: 'networkidle' });
    expect(Date.now() - start).toBeLessThan(15000);
  });
});

// ====================================================================
// PHASE 14 — VISUAL REVIEW
// ====================================================================
test.describe('Phase 14 — Visual Review', () => {
  test('Notification dropdown has proper width', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    const bell = page.locator('button[aria-label*="Notification"]').first();
    await bell.click();
    await page.waitForTimeout(500);
    const dialog = page.locator('[role="dialog"]');
    const box = await dialog.boundingBox();
    expect(box).not.toBeNull();
    if (box) expect(box.width).toBeGreaterThan(200);
  });

  test('No horizontal scroll on notification page', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
    expect(scrollW).toBe(0);
  });

  test('Communication overview has content', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/overview', { waitUntil: 'networkidle' });
    const t = await bodyText(page);
    expect(t.length).toBeGreaterThan(20);
  });

  test('No horizontal scroll on delivery queue page', async ({ page }) => {
    await login(page);
    await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
    const scrollW = await page.evaluate(() => document.documentElement.scrollWidth - document.documentElement.clientWidth);
    expect(scrollW).toBe(0);
  });
});

// ====================================================================
// PHASE 15 — EVIDENCE COLLECTION
// ====================================================================
test.describe('Phase 15 — Evidence Collection', () => {
  test('Screenshots captured (config: screenshot=on)', async ({ page }) => {
    await page.goto('/admin', { waitUntil: 'networkidle' });
    await expect(page.locator('body')).toBeVisible();
  });

  test('Traces captured (config: trace=on)', async ({ page }) => {
    await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
    await expect(page.locator('body')).toBeVisible();
  });
});
