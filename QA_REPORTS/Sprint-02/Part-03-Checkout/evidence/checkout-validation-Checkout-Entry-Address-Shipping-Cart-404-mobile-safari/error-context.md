# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-validation.spec.ts >> Checkout Entry,Address,Shipping >> Cart 404
- Location: tests\checkout-validation.spec.ts:15:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3]:
    - /url: "#main"
  - generic [ref=e4]:
    - banner [ref=e5]:
      - generic [ref=e6]:
        - button "Toggle navigation" [ref=e7] [cursor=pointer]: ☰
        - link "SporeKart home" [ref=e8]:
          - /url: /
          - generic [ref=e9]: ❖
          - generic [ref=e10]: SporeKart
        - generic "Current workspace" [ref=e11]: 🌐 Public
      - generic [ref=e12]:
        - button "Quick action" [ref=e13] [cursor=pointer]: ＋
        - button "Notifications" [ref=e14] [cursor=pointer]: 🔔
        - button "AI assistant" [ref=e15] [cursor=pointer]: ✨
        - generic [ref=e16]:
          - generic [ref=e17]: Review role
          - combobox "Switch review role" [ref=e18]:
            - option "Guest"
            - option "Customer"
            - option "Grower"
            - option "Trainer"
            - option "Distributor"
            - option "Support"
            - option "Administrator" [selected]
            - option "Business Owner"
            - option "Governance Manager"
        - button "Account menu" [ref=e19] [cursor=pointer]:
          - generic [ref=e20]: A
    - generic [ref=e21]:
      - navigation "Workspaces" [ref=e22]:
        - generic [ref=e23]:
          - paragraph [ref=e24]: Discover
          - list [ref=e25]:
            - listitem [ref=e26]:
              - link "Public" [ref=e27]:
                - /url: /
                - generic [ref=e28]: 🌐
                - generic [ref=e29]: Public
              - list [ref=e30]:
                - listitem [ref=e31]:
                  - link "Search" [ref=e32]:
                    - /url: /search
                - listitem [ref=e33]:
                  - link "Products" [ref=e34]:
                    - /url: /products
                - listitem [ref=e35]:
                  - link "Product detail" [ref=e36]:
                    - /url: /products/:id
                - listitem [ref=e37]:
                  - link "Cart" [ref=e38]:
                    - /url: /cart
                - listitem [ref=e39]:
                  - link "Checkout" [ref=e40]:
                    - /url: /checkout
        - generic [ref=e41]:
          - paragraph [ref=e42]: Operate
          - list [ref=e43]:
            - listitem [ref=e44]:
              - link "Orders" [ref=e45]:
                - /url: /orders
                - generic [ref=e46]: 📦
                - generic [ref=e47]: Orders
              - list [ref=e48]:
                - listitem [ref=e49]:
                  - link "Order detail" [ref=e50]:
                    - /url: /orders/:id
                - listitem [ref=e51]:
                  - link "Fulfill" [ref=e52]:
                    - /url: /orders/:id/fulfill
            - listitem [ref=e53]:
              - link "Products" [ref=e54]:
                - /url: /catalog
                - generic [ref=e55]: 🧪
                - generic [ref=e56]: Products
              - list [ref=e57]:
                - listitem [ref=e58]:
                  - link "Product / SKU" [ref=e59]:
                    - /url: /catalog/:id
                - listitem [ref=e60]:
                  - link "New product" [ref=e61]:
                    - /url: /catalog/new
            - listitem [ref=e62]:
              - link "Training" [ref=e63]:
                - /url: /training
                - generic [ref=e64]: 🎓
                - generic [ref=e65]: Training
              - list [ref=e66]:
                - listitem [ref=e67]:
                  - link "Session detail" [ref=e68]:
                    - /url: /training/:id
                - listitem [ref=e69]:
                  - link "Create session" [ref=e70]:
                    - /url: /training/create
        - generic [ref=e71]:
          - paragraph [ref=e72]: Intelligence
          - list [ref=e73]:
            - listitem [ref=e74]:
              - link "AI Workspace" [ref=e75]:
                - /url: /ai
                - generic [ref=e76]: ✨
                - generic [ref=e77]: AI Workspace
              - list [ref=e78]:
                - listitem [ref=e79]:
                  - link "Conversations" [ref=e80]:
                    - /url: /ai/chat
                - listitem [ref=e81]:
                  - link "Prompt library" [ref=e82]:
                    - /url: /ai/prompts
                - listitem [ref=e83]:
                  - link "Knowledge" [ref=e84]:
                    - /url: /ai/knowledge
            - listitem [ref=e85]:
              - link "Governance" [ref=e86]:
                - /url: /governance
                - generic [ref=e87]: 🛡️
                - generic [ref=e88]: Governance
              - list [ref=e89]:
                - listitem [ref=e90]:
                  - link "Policies" [ref=e91]:
                    - /url: /governance/policies
                - listitem [ref=e92]:
                  - link "Approvals" [ref=e93]:
                    - /url: /governance/approvals
                - listitem [ref=e94]:
                  - link "Compliance" [ref=e95]:
                    - /url: /governance/compliance
                - listitem [ref=e96]:
                  - link "Access control" [ref=e97]:
                    - /url: /governance/access
            - listitem [ref=e98]:
              - link "Analytics" [ref=e99]:
                - /url: /analytics
                - generic [ref=e100]: 📈
                - generic [ref=e101]: Analytics
              - list [ref=e102]:
                - listitem [ref=e103]:
                  - link "Sales" [ref=e104]:
                    - /url: /analytics/sales
                - listitem [ref=e105]:
                  - link "Operations" [ref=e106]:
                    - /url: /analytics/operations
        - generic [ref=e107]:
          - paragraph [ref=e108]: Platform
          - list [ref=e109]:
            - listitem [ref=e110]:
              - link "Administration" [ref=e111]:
                - /url: /admin
                - generic [ref=e112]: ⚙️
                - generic [ref=e113]: Administration
              - list [ref=e114]:
                - listitem [ref=e115]:
                  - link "Users" [ref=e116]:
                    - /url: /admin/users
                - listitem [ref=e117]:
                  - link "Content" [ref=e118]:
                    - /url: /admin/content
                - listitem [ref=e119]:
                  - link "Config" [ref=e120]:
                    - /url: /admin/config
                - listitem [ref=e121]:
                  - link "Monitoring" [ref=e122]:
                    - /url: /admin/monitoring
            - listitem [ref=e123]:
              - link "CMS" [ref=e124]:
                - /url: /cms
                - generic [ref=e125]: 📝
                - generic [ref=e126]: CMS
              - list [ref=e127]:
                - listitem [ref=e128]:
                  - link "Pages" [ref=e129]:
                    - /url: /cms/pages
                - listitem [ref=e130]:
                  - link "Media" [ref=e131]:
                    - /url: /cms/media
            - listitem [ref=e132]:
              - link "Support" [ref=e133]:
                - /url: /support
                - generic [ref=e134]: 💬
                - generic [ref=e135]: Support
              - list [ref=e136]:
                - listitem [ref=e137]:
                  - link "Tickets" [ref=e138]:
                    - /url: /support/tickets
                - listitem [ref=e139]:
                  - link "Knowledge base" [ref=e140]:
                    - /url: /support/kb
            - listitem [ref=e141]:
              - link "Settings" [ref=e142]:
                - /url: /settings
                - generic [ref=e143]: 🔧
                - generic [ref=e144]: Settings
              - list [ref=e145]:
                - listitem [ref=e146]:
                  - link "Profile" [ref=e147]:
                    - /url: /settings/profile
                - listitem [ref=e148]:
                  - link "Security" [ref=e149]:
                    - /url: /settings/security
                - listitem [ref=e150]:
                  - link "Workspace" [ref=e151]:
                    - /url: /settings/workspace
                - listitem [ref=e152]:
                  - link "Billing" [ref=e153]:
                    - /url: /settings/billing
            - listitem [ref=e154]:
              - link "Demo" [ref=e155]:
                - /url: /demo
                - generic [ref=e156]: 🧪
                - generic [ref=e157]: Demo
              - list [ref=e158]:
                - listitem [ref=e159]:
                  - link "Responsive Layout" [ref=e160]:
                    - /url: /demo/responsive
                - listitem [ref=e161]:
                  - link "Keyboard Navigation" [ref=e162]:
                    - /url: /demo/keyboard
                - listitem [ref=e163]:
                  - link "Loading Experience" [ref=e164]:
                    - /url: /demo/loading
                - listitem [ref=e165]:
                  - link "Error Pages" [ref=e166]:
                    - /url: /demo/errors
                - listitem [ref=e167]:
                  - link "Empty States" [ref=e168]:
                    - /url: /demo/empty
                - listitem [ref=e169]:
                  - link "Form Patterns" [ref=e170]:
                    - /url: /demo/forms
                - listitem [ref=e171]:
                  - link "Microcopy Gallery" [ref=e172]:
                    - /url: /demo/microcopy
            - listitem [ref=e173]:
              - link "Design System" [ref=e174]:
                - /url: /design-system
                - generic [ref=e175]: 🎨
                - generic [ref=e176]: Design System
              - list [ref=e177]:
                - listitem [ref=e178]:
                  - link "Buttons Preview" [ref=e179]:
                    - /url: /design-system/buttons
                - listitem [ref=e180]:
                  - link "Links Preview" [ref=e181]:
                    - /url: /design-system/links
                - listitem [ref=e182]:
                  - link "Icon Library" [ref=e183]:
                    - /url: /design-system/icons
                - listitem [ref=e184]:
                  - link "Inputs Preview" [ref=e185]:
                    - /url: /design-system/inputs
                - listitem [ref=e186]:
                  - link "Search Preview" [ref=e187]:
                    - /url: /design-system/search
                - listitem [ref=e188]:
                  - link "Password Preview" [ref=e189]:
                    - /url: /design-system/password
                - listitem [ref=e190]:
                  - link "OTP Preview" [ref=e191]:
                    - /url: /design-system/otp
                - listitem [ref=e192]:
                  - link "Checkbox Preview" [ref=e193]:
                    - /url: /design-system/checkbox
                - listitem [ref=e194]:
                  - link "Radio Preview" [ref=e195]:
                    - /url: /design-system/radio
                - listitem [ref=e196]:
                  - link "Switch Preview" [ref=e197]:
                    - /url: /design-system/switch
                - listitem [ref=e198]:
                  - link "Forms Index" [ref=e199]:
                    - /url: /design-system/forms
                - listitem [ref=e200]:
                  - link "Form Layouts" [ref=e201]:
                    - /url: /design-system/forms/layouts
                - listitem [ref=e202]:
                  - link "Form Validation" [ref=e203]:
                    - /url: /design-system/forms/validation
                - listitem [ref=e204]:
                  - link "Address Form" [ref=e205]:
                    - /url: /design-system/forms/address
                - listitem [ref=e206]:
                  - link "File Upload" [ref=e207]:
                    - /url: /design-system/forms/upload
                - listitem [ref=e208]:
                  - link "Select Preview" [ref=e209]:
                    - /url: /design-system/forms/select
                - listitem [ref=e210]:
                  - link "Cards Preview" [ref=e211]:
                    - /url: /design-system/cards
                - listitem [ref=e212]:
                  - link "Tables Preview" [ref=e213]:
                    - /url: /design-system/tables
                - listitem [ref=e214]:
                  - link "Lists Preview" [ref=e215]:
                    - /url: /design-system/lists
                - listitem [ref=e216]:
                  - link "Badges Preview" [ref=e217]:
                    - /url: /design-system/badges
                - listitem [ref=e218]:
                  - link "Chips Preview" [ref=e219]:
                    - /url: /design-system/chips
                - listitem [ref=e220]:
                  - link "Avatars Preview" [ref=e221]:
                    - /url: /design-system/avatars
                - listitem [ref=e222]:
                  - link "Empty States Preview" [ref=e223]:
                    - /url: /design-system/empty-states
                - listitem [ref=e224]:
                  - link "Skeletons Preview" [ref=e225]:
                    - /url: /design-system/skeletons
                - listitem [ref=e226]:
                  - link "Nav Index" [ref=e227]:
                    - /url: /design-system/navigation
                - listitem [ref=e228]:
                  - link "Header Preview" [ref=e229]:
                    - /url: /design-system/header
                - listitem [ref=e230]:
                  - link "Sidebar Preview" [ref=e231]:
                    - /url: /design-system/sidebar
                - listitem [ref=e232]:
                  - link "Breadcrumb Preview" [ref=e233]:
                    - /url: /design-system/breadcrumb
                - listitem [ref=e234]:
                  - link "Menu Preview" [ref=e235]:
                    - /url: /design-system/menu
                - listitem [ref=e236]:
                  - link "Tabs Preview" [ref=e237]:
                    - /url: /design-system/tabs
                - listitem [ref=e238]:
                  - link "Pagination Preview" [ref=e239]:
                    - /url: /design-system/pagination
                - listitem [ref=e240]:
                  - link "Stepper Preview" [ref=e241]:
                    - /url: /design-system/stepper
                - listitem [ref=e242]:
                  - link "Layouts Preview" [ref=e243]:
                    - /url: /design-system/layouts
                - listitem [ref=e244]:
                  - link "Command Palette Preview" [ref=e245]:
                    - /url: /design-system/command-palette
                - listitem [ref=e246]:
                  - link "Dialogs Preview" [ref=e247]:
                    - /url: /design-system/dialogs
                - listitem [ref=e248]:
                  - link "Modals Preview" [ref=e249]:
                    - /url: /design-system/modals
                - listitem [ref=e250]:
                  - link "Toasts Preview" [ref=e251]:
                    - /url: /design-system/toasts
                - listitem [ref=e252]:
                  - link "Notifications Preview" [ref=e253]:
                    - /url: /design-system/notifications
                - listitem [ref=e254]:
                  - link "Alerts Preview" [ref=e255]:
                    - /url: /design-system/alerts
                - listitem [ref=e256]:
                  - link "Tooltips Preview" [ref=e257]:
                    - /url: /design-system/tooltips
                - listitem [ref=e258]:
                  - link "Popovers Preview" [ref=e259]:
                    - /url: /design-system/popovers
                - listitem [ref=e260]:
                  - link "Progress Preview" [ref=e261]:
                    - /url: /design-system/progress
                - listitem [ref=e262]:
                  - link "Loading Preview" [ref=e263]:
                    - /url: /design-system/loading
                - listitem [ref=e264]:
                  - link "Status Preview" [ref=e265]:
                    - /url: /design-system/status
                - listitem [ref=e266]:
                  - link "Charts Preview" [ref=e267]:
                    - /url: /design-system/charts
                - listitem [ref=e268]:
                  - link "KPIs Preview" [ref=e269]:
                    - /url: /design-system/kpis
                - listitem [ref=e270]:
                  - link "Timelines Preview" [ref=e271]:
                    - /url: /design-system/timelines
                - listitem [ref=e272]:
                  - link "Calendars Preview" [ref=e273]:
                    - /url: /design-system/calendars
                - listitem [ref=e274]:
                  - link "Data Filters Preview" [ref=e275]:
                    - /url: /design-system/data-filters
                - listitem [ref=e276]:
                  - link "Export Preview" [ref=e277]:
                    - /url: /design-system/export
                - listitem [ref=e278]:
                  - link "Component Catalog" [ref=e279]:
                    - /url: /design-system/catalog
                - listitem [ref=e280]:
                  - link "Token Explorer" [ref=e281]:
                    - /url: /design-system/tokens
                - listitem [ref=e282]:
                  - link "Accessibility Center" [ref=e283]:
                    - /url: /design-system/accessibility
                - listitem [ref=e284]:
                  - link "Docs Center" [ref=e285]:
                    - /url: /design-system/docs
                - listitem [ref=e286]:
                  - link "Quality Dashboard" [ref=e287]:
                    - /url: /design-system/quality
      - generic [ref=e288]:
        - navigation "Breadcrumb" [ref=e289]:
          - list [ref=e290]:
            - listitem [ref=e291]:
              - link "Public" [ref=e292]:
                - /url: /
              - generic [ref=e293]: /
            - listitem [ref=e294]:
              - generic [ref=e295]: Cart
        - main [ref=e296]:
          - generic [ref=e297]:
            - generic [ref=e298]:
              - generic [ref=e299]:
                - heading "Cart" [level=1] [ref=e300]
                - paragraph [ref=e301]: Shopping cart.
              - button "Checkout" [ref=e302] [cursor=pointer]
            - region "Cart — empty panel" [ref=e303]:
              - generic [ref=e304]:
                - generic [ref=e305]: ◌
                - paragraph [ref=e306]: Cart — empty panel
                - paragraph [ref=e307]: This is a navigation prototype. Production content, tables, forms, and charts arrive in later Sprint 19 parts.
                - button "Checkout" [ref=e308] [cursor=pointer]
    - contentinfo [ref=e309]:
      - generic [ref=e310]: SporeKart Enterprise · Navigation Prototype (Sprint 19 Part 1B)
      - generic [ref=e311]:
        - link "Help" [ref=e312]:
          - /url: /support/kb
        - link "Terms" [ref=e313]:
          - /url: /
        - link "Privacy" [ref=e314]:
          - /url: /
        - link "Status" [ref=e315]:
          - /url: /
```

# Test source

```ts
  1  | ﻿import { test, expect } from '@playwright/test';
  2  | const PHONE = '9876543210';
  3  | async function login(page) {
  4  |   await page.goto('/login'); await page.waitForLoadState('networkidle');
  5  |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  6  |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  7  |   await inp.fill(PHONE);
  8  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  9  |   await btn.click(); await page.waitForTimeout(2000);
  10 |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  11 |   const n = await otp.count();
  12 |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  13 | }
  14 | test.describe('Checkout Entry,Address,Shipping', () => {
> 15 |   test('Cart 404',async({page})=>{await page.goto('/cart',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
     |                                                                                                                                                                                        ^ Error: expect(received).toBeTruthy()
  16 |   test('Checkout 404',async({page})=>{await page.goto('/checkout',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
  17 |   test('Unauth redirect',async({page})=>{await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  18 |   test('Login works',async({page})=>{await login(page);expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  19 |   test('Orders dashboard',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.length).toBeGreaterThan(50);expect(t.includes('ORD')||t.includes('Order')).toBeTruthy();});
  20 |   test('Order details',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  21 |   test('Shipment tracking',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842/track',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  22 |   test('Returns refunds',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842/refund',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  23 |   test('Tab filters',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(await page.locator('button:has-text("All"),button:has-text("Active")').first().isVisible().catch(()=>false)).toBeTruthy();});
  24 |   test('Search input',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});const s=page.locator('input[type="search"],input[placeholder*="search" i]');if(await s.isVisible({timeout:3000}).catch(()=>false)){await s.fill('ORD-2026');expect(await s.inputValue()).toBe('ORD-2026');}});
  25 |   test('Address placeholder',async({page})=>{await login(page);await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.length).toBeGreaterThan(10);expect(t.includes('Address')||t.includes('address')).toBeTruthy();});
  26 |   test('Address new route',async({page})=>{await login(page);await page.goto('/dashboard/addresses/new',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  27 |   test('Admin shipping',async({page})=>{await login(page);await page.goto('/admin/shipping',{waitUntil:'domcontentloaded'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  28 | });
  29 | 
```