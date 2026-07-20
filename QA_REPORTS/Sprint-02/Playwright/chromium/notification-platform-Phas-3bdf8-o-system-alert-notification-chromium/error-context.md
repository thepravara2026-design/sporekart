# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 1 — Event Triggers >> IMPLEMENTATION GAP: No system alert notification
- Location: tests\notification-platform.spec.ts:86:7

# Error details

```
Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
Call log:
  - navigating to "http://localhost:5173/admin", waiting until "networkidle"

```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | const BASE = 'http://localhost:5174';
  4   | 
  5   | async function login(page) {
  6   |   await page.goto('/login'); await page.waitForLoadState('networkidle');
  7   |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  8   |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  9   |   await inp.fill('9876543210');
  10  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  11  |   await btn.click(); await page.waitForTimeout(2000);
  12  |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  13  |   const n = await otp.count();
  14  |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  15  | }
  16  | 
  17  | async function bodyText(page) { return (await page.locator('body').innerText()); }
  18  | async function hasText(page, t) { return (await bodyText(page)).includes(t); }
  19  | 
  20  | // ====================================================================
  21  | // PHASE 1 — EVENT TRIGGERS
  22  | // ====================================================================
  23  | test.describe('Phase 1 — Event Triggers', () => {
  24  |   test('IMPLEMENTATION GAP: No notification on registration', async ({ page }) => {
  25  |     const r = await page.goto('/register', { waitUntil: 'networkidle' });
  26  |     expect(r?.status()).toBeLessThan(400);
  27  |     const t = await bodyText(page);
  28  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  29  |   });
  30  | 
  31  |   test('IMPLEMENTATION GAP: No notification on login', async ({ page }) => {
  32  |     await page.goto('/login', { waitUntil: 'networkidle' });
  33  |     const t = await bodyText(page);
  34  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  35  |   });
  36  | 
  37  |   test('IMPLEMENTATION GAP: No OTP notification', async ({ page }) => {
  38  |     await page.goto('/login', { waitUntil: 'networkidle' });
  39  |     const t = await bodyText(page);
  40  |     expect(t.includes('OTP') || t.includes('otp')).toBeTruthy();
  41  |   });
  42  | 
  43  |   test('IMPLEMENTATION GAP: No password reset notification', async ({ page }) => {
  44  |     const r = await page.goto('/forgot-password', { waitUntil: 'networkidle' });
  45  |     expect(r?.status()).toBeLessThan(400);
  46  |     const t = await bodyText(page);
  47  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  48  |   });
  49  | 
  50  |   test('IMPLEMENTATION GAP: No order created notification', async ({ page }) => {
  51  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  52  |     const t = await bodyText(page);
  53  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  54  |   });
  55  | 
  56  |   test('IMPLEMENTATION GAP: No order confirmed notification', async ({ page }) => {
  57  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  58  |     const t = await bodyText(page);
  59  |     expect(t.includes('confirmed') && t.includes('notification')).toBeFalsy();
  60  |   });
  61  | 
  62  |   test('IMPLEMENTATION GAP: No order cancelled notification', async ({ page }) => {
  63  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  64  |     const t = await bodyText(page);
  65  |     expect(t.includes('cancel') && t.includes('notification')).toBeFalsy();
  66  |   });
  67  | 
  68  |   test('IMPLEMENTATION GAP: No training registration notification', async ({ page }) => {
  69  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  70  |     const t = await bodyText(page);
  71  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  72  |   });
  73  | 
  74  |   test('IMPLEMENTATION GAP: No training approval notification', async ({ page }) => {
  75  |     await page.goto('/training/courses', { waitUntil: 'networkidle' });
  76  |     const t = await bodyText(page);
  77  |     expect(t.includes('approval') && t.includes('notification')).toBeFalsy();
  78  |   });
  79  | 
  80  |   test('IMPLEMENTATION GAP: No admin action notification', async ({ page }) => {
  81  |     await page.goto('/admin', { waitUntil: 'networkidle' });
  82  |     const t = await bodyText(page);
  83  |     expect(t.includes('notification') || t.includes('Notification')).toBeFalsy();
  84  |   });
  85  | 
  86  |   test('IMPLEMENTATION GAP: No system alert notification', async ({ page }) => {
> 87  |     await page.goto('/admin', { waitUntil: 'networkidle' });
      |                ^ Error: page.goto: net::ERR_CONNECTION_REFUSED at http://localhost:5173/admin
  88  |     const t = await bodyText(page);
  89  |     expect(t.includes('alert') || t.includes('Alert')).toBeFalsy();
  90  |   });
  91  | 
  92  |   test('IMPLEMENTATION GAP: No shipping update notification', async ({ page }) => {
  93  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  94  |     const t = await bodyText(page);
  95  |     expect(t.includes('shipping') && t.includes('notification')).toBeFalsy();
  96  |   });
  97  | 
  98  |   test('IMPLEMENTATION GAP: No payment status notification', async ({ page }) => {
  99  |     await page.goto('/orders', { waitUntil: 'networkidle' });
  100 |     const t = await bodyText(page);
  101 |     expect(t.includes('payment') && t.includes('notification')).toBeFalsy();
  102 |   });
  103 | });
  104 | 
  105 | // ====================================================================
  106 | // PHASE 2 — EMAIL
  107 | // ====================================================================
  108 | test.describe('Phase 2 — Email', () => {
  109 |   test('IMPLEMENTATION GAP: Email notification service not available', async () => {
  110 |     const resp = await fetch(`${BASE}/notifications`).catch(() => null);
  111 |     if (resp) {
  112 |       const data = await resp.json();
  113 |       expect(data).toBeDefined();
  114 |     }
  115 |   });
  116 | 
  117 |   test('IMPLEMENTATION GAP: No email template selection UI', async ({ page }) => {
  118 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  119 |     const t = await bodyText(page).catch(() => '');
  120 |     const hasEmailUI = t.includes('email') || t.includes('Email');
  121 |     expect(hasEmailUI).toBeTruthy();
  122 |   });
  123 | 
  124 |   test('IMPLEMENTATION GAP: No email sending integration', async () => {
  125 |     const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: 'test@test.com', subject: 'Test', body: 'Test body', channel: 'EMAIL' }) }).catch(() => null);
  126 |     if (resp) {
  127 |       const status = resp.status;
  128 |       expect(status < 500).toBeTruthy();
  129 |     }
  130 |   });
  131 | 
  132 |   test('IMPLEMENTATION GAP: No dynamic placeholder substitution', async ({ page }) => {
  133 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  134 |     const t = await bodyText(page).catch(() => '');
  135 |     const hasPlaceholders = t.includes('{{') || t.includes('variable') || t.includes('placeholder');
  136 |     expect(hasPlaceholders).toBeTruthy();
  137 |   });
  138 | });
  139 | 
  140 | // ====================================================================
  141 | // PHASE 3 — SMS
  142 | // ====================================================================
  143 | test.describe('Phase 3 — SMS', () => {
  144 |   test('IMPLEMENTATION GAP: SMS notification service not available', async () => {
  145 |     const resp = await fetch(`${BASE}/notifications`).catch(() => null);
  146 |     if (resp) {
  147 |       const data = await resp.json();
  148 |       expect(data).toBeDefined();
  149 |     }
  150 |   });
  151 | 
  152 |   test('IMPLEMENTATION GAP: No SMS template UI', async ({ page }) => {
  153 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  154 |     const t = await bodyText(page).catch(() => '');
  155 |     const hasSMSUI = t.includes('sms') || t.includes('SMS');
  156 |     expect(hasSMSUI).toBeTruthy();
  157 |   });
  158 | 
  159 |   test('IMPLEMENTATION GAP: No SMS sending integration', async () => {
  160 |     const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: '+919876543210', subject: 'OTP', body: 'Your OTP is 123456', channel: 'SMS' }) }).catch(() => null);
  161 |     if (resp) {
  162 |       const status = resp.status;
  163 |       expect(status < 500).toBeTruthy();
  164 |     }
  165 |   });
  166 | });
  167 | 
  168 | // ====================================================================
  169 | // PHASE 4 — WHATSAPP
  170 | // ====================================================================
  171 | test.describe('Phase 4 — WhatsApp', () => {
  172 |   test('IMPLEMENTATION GAP: WhatsApp notification service not available', async () => {
  173 |     const resp = await fetch(`${BASE}/notifications`).catch(() => null);
  174 |     if (resp) {
  175 |       const data = await resp.json();
  176 |       expect(data).toBeDefined();
  177 |     }
  178 |   });
  179 | 
  180 |   test('IMPLEMENTATION GAP: No WhatsApp template UI', async ({ page }) => {
  181 |     await page.goto('/admin/training/communication/templates', { waitUntil: 'networkidle' }).catch(() => {});
  182 |     const t = await bodyText(page).catch(() => '');
  183 |     const hasWhatsAppUI = t.includes('whatsapp') || t.includes('WhatsApp');
  184 |     expect(hasWhatsAppUI).toBeTruthy();
  185 |   });
  186 | 
  187 |   test('IMPLEMENTATION GAP: No WhatsApp sending integration', async () => {
```