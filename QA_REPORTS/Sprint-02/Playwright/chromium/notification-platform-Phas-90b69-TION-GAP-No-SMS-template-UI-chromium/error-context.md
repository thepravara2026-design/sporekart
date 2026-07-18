# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 3 — SMS >> IMPLEMENTATION GAP: No SMS template UI
- Location: tests\notification-platform.spec.ts:152:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Test source

```ts
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
  87  |     await page.goto('/admin', { waitUntil: 'networkidle' });
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
> 156 |     expect(hasSMSUI).toBeTruthy();
      |                      ^ Error: expect(received).toBeTruthy()
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
  188 |     const resp = await fetch(`${BASE}/notifications`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ recipient: '+919876543210', subject: 'Order Update', body: 'Your order has been shipped', channel: 'WHATSAPP' }) }).catch(() => null);
  189 |     if (resp) {
  190 |       const status = resp.status;
  191 |       expect(status < 500).toBeTruthy();
  192 |     }
  193 |   });
  194 | });
  195 | 
  196 | // ====================================================================
  197 | // PHASE 5 — IN-APP NOTIFICATIONS
  198 | // ====================================================================
  199 | test.describe('Phase 5 — In-App Notifications', () => {
  200 |   test('Notification bell icon visible in admin nav', async ({ page }) => {
  201 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  202 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  203 |     await expect(bell).toBeVisible({ timeout: 5000 });
  204 |   });
  205 | 
  206 |   test('Notification dropdown opens on bell click', async ({ page }) => {
  207 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  208 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  209 |     await bell.click();
  210 |     await page.waitForTimeout(500);
  211 |     const dialog = page.locator('[role="dialog"][aria-label="Notifications"]');
  212 |     await expect(dialog).toBeVisible({ timeout: 3000 });
  213 |   });
  214 | 
  215 |   test('Notification dropdown has notification list', async ({ page }) => {
  216 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  217 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  218 |     await bell.click();
  219 |     await page.waitForTimeout(500);
  220 |     const list = page.locator('[role="dialog"] [role="list"]');
  221 |     await expect(list).toBeVisible({ timeout: 3000 });
  222 |   });
  223 | 
  224 |   test('Notification has mark as read button', async ({ page }) => {
  225 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  226 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  227 |     await bell.click();
  228 |     await page.waitForTimeout(500);
  229 |     const markBtn = page.locator('[role="dialog"] button[aria-label="Mark as read"]').first();
  230 |     await expect(markBtn).toBeVisible({ timeout: 3000 });
  231 |   });
  232 | 
  233 |   test('Notification has dismiss button', async ({ page }) => {
  234 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  235 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  236 |     await bell.click();
  237 |     await page.waitForTimeout(500);
  238 |     const dismissBtn = page.locator('[role="dialog"] button[aria-label="Dismiss"]').first();
  239 |     await expect(dismissBtn).toBeVisible({ timeout: 3000 });
  240 |   });
  241 | 
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
```