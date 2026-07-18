# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: performance-validation.spec.ts >> Phase 9 — Responsive Performance >> Desktop viewport renders all key pages
- Location: tests\performance-validation.spec.ts:723:7

# Error details

```
Test timeout of 30000ms exceeded.
```

```
TimeoutError: page.waitForLoadState: Timeout 30000ms exceeded.
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3]:
    - /url: "#main"
  - generic [ref=e4]:
    - banner [ref=e5]:
      - generic [ref=e6]:
        - link "SporeKart home" [ref=e7]:
          - /url: /
          - generic [ref=e8]: ❖
          - generic [ref=e9]: SporeKart
        - generic "Current workspace" [ref=e10]: 🌐 Public
      - button "Open search and command palette" [ref=e12]:
        - generic [ref=e13]: 🔍
        - text: Search or jump to…
        - generic [ref=e14]: ⌘K
      - generic [ref=e15]:
        - button "Quick action" [ref=e16] [cursor=pointer]: ＋
        - button "Notifications" [ref=e17] [cursor=pointer]: 🔔
        - button "AI assistant" [ref=e18] [cursor=pointer]: ✨
        - generic [ref=e19]:
          - generic [ref=e20]: Review role
          - combobox "Switch review role" [ref=e21]:
            - option "Guest"
            - option "Customer"
            - option "Grower"
            - option "Trainer"
            - option "Distributor"
            - option "Support"
            - option "Administrator" [selected]
            - option "Business Owner"
            - option "Governance Manager"
        - button "Account menu" [ref=e22] [cursor=pointer]:
          - generic [ref=e23]: A
    - generic [ref=e24]:
      - navigation "Workspaces" [ref=e25]:
        - generic [ref=e26]:
          - paragraph [ref=e27]: Discover
          - list [ref=e28]:
            - listitem [ref=e29]:
              - link "Public" [ref=e30]:
                - /url: /
                - generic [ref=e31]: 🌐
                - generic [ref=e32]: Public
              - list [ref=e33]:
                - listitem [ref=e34]:
                  - link "Search" [ref=e35]:
                    - /url: /search
                - listitem [ref=e36]:
                  - link "Products" [ref=e37]:
                    - /url: /products
                - listitem [ref=e38]:
                  - link "Product detail" [ref=e39]:
                    - /url: /products/:id
                - listitem [ref=e40]:
                  - link "Cart" [ref=e41]:
                    - /url: /cart
                - listitem [ref=e42]:
                  - link "Checkout" [ref=e43]:
                    - /url: /checkout
        - generic [ref=e44]:
          - paragraph [ref=e45]: Operate
          - list [ref=e46]:
            - listitem [ref=e47]:
              - link "Orders" [ref=e48]:
                - /url: /orders
                - generic [ref=e49]: 📦
                - generic [ref=e50]: Orders
              - list [ref=e51]:
                - listitem [ref=e52]:
                  - link "Order detail" [ref=e53]:
                    - /url: /orders/:id
                - listitem [ref=e54]:
                  - link "Fulfill" [ref=e55]:
                    - /url: /orders/:id/fulfill
            - listitem [ref=e56]:
              - link "Products" [ref=e57]:
                - /url: /catalog
                - generic [ref=e58]: 🧪
                - generic [ref=e59]: Products
              - list [ref=e60]:
                - listitem [ref=e61]:
                  - link "Product / SKU" [ref=e62]:
                    - /url: /catalog/:id
                - listitem [ref=e63]:
                  - link "New product" [ref=e64]:
                    - /url: /catalog/new
            - listitem [ref=e65]:
              - link "Training" [ref=e66]:
                - /url: /training
                - generic [ref=e67]: 🎓
                - generic [ref=e68]: Training
              - list [ref=e69]:
                - listitem [ref=e70]:
                  - link "Session detail" [ref=e71]:
                    - /url: /training/:id
                - listitem [ref=e72]:
                  - link "Create session" [ref=e73]:
                    - /url: /training/create
        - generic [ref=e74]:
          - paragraph [ref=e75]: Intelligence
          - list [ref=e76]:
            - listitem [ref=e77]:
              - link "AI Workspace" [ref=e78]:
                - /url: /ai
                - generic [ref=e79]: ✨
                - generic [ref=e80]: AI Workspace
              - list [ref=e81]:
                - listitem [ref=e82]:
                  - link "Conversations" [ref=e83]:
                    - /url: /ai/chat
                - listitem [ref=e84]:
                  - link "Prompt library" [ref=e85]:
                    - /url: /ai/prompts
                - listitem [ref=e86]:
                  - link "Knowledge" [ref=e87]:
                    - /url: /ai/knowledge
            - listitem [ref=e88]:
              - link "Governance" [ref=e89]:
                - /url: /governance
                - generic [ref=e90]: 🛡️
                - generic [ref=e91]: Governance
              - list [ref=e92]:
                - listitem [ref=e93]:
                  - link "Policies" [ref=e94]:
                    - /url: /governance/policies
                - listitem [ref=e95]:
                  - link "Approvals" [ref=e96]:
                    - /url: /governance/approvals
                - listitem [ref=e97]:
                  - link "Compliance" [ref=e98]:
                    - /url: /governance/compliance
                - listitem [ref=e99]:
                  - link "Access control" [ref=e100]:
                    - /url: /governance/access
            - listitem [ref=e101]:
              - link "Analytics" [ref=e102]:
                - /url: /analytics
                - generic [ref=e103]: 📈
                - generic [ref=e104]: Analytics
              - list [ref=e105]:
                - listitem [ref=e106]:
                  - link "Sales" [ref=e107]:
                    - /url: /analytics/sales
                - listitem [ref=e108]:
                  - link "Operations" [ref=e109]:
                    - /url: /analytics/operations
        - generic [ref=e110]:
          - paragraph [ref=e111]: Platform
          - list [ref=e112]:
            - listitem [ref=e113]:
              - link "Administration" [ref=e114]:
                - /url: /admin
                - generic [ref=e115]: ⚙️
                - generic [ref=e116]: Administration
              - list [ref=e117]:
                - listitem [ref=e118]:
                  - link "Users" [ref=e119]:
                    - /url: /admin/users
                - listitem [ref=e120]:
                  - link "Content" [ref=e121]:
                    - /url: /admin/content
                - listitem [ref=e122]:
                  - link "Config" [ref=e123]:
                    - /url: /admin/config
                - listitem [ref=e124]:
                  - link "Monitoring" [ref=e125]:
                    - /url: /admin/monitoring
            - listitem [ref=e126]:
              - link "CMS" [ref=e127]:
                - /url: /cms
                - generic [ref=e128]: 📝
                - generic [ref=e129]: CMS
              - list [ref=e130]:
                - listitem [ref=e131]:
                  - link "Pages" [ref=e132]:
                    - /url: /cms/pages
                - listitem [ref=e133]:
                  - link "Media" [ref=e134]:
                    - /url: /cms/media
            - listitem [ref=e135]:
              - link "Support" [ref=e136]:
                - /url: /support
                - generic [ref=e137]: 💬
                - generic [ref=e138]: Support
              - list [ref=e139]:
                - listitem [ref=e140]:
                  - link "Tickets" [ref=e141]:
                    - /url: /support/tickets
                - listitem [ref=e142]:
                  - link "Knowledge base" [ref=e143]:
                    - /url: /support/kb
            - listitem [ref=e144]:
              - link "Settings" [ref=e145]:
                - /url: /settings
                - generic [ref=e146]: 🔧
                - generic [ref=e147]: Settings
              - list [ref=e148]:
                - listitem [ref=e149]:
                  - link "Profile" [ref=e150]:
                    - /url: /settings/profile
                - listitem [ref=e151]:
                  - link "Security" [ref=e152]:
                    - /url: /settings/security
                - listitem [ref=e153]:
                  - link "Workspace" [ref=e154]:
                    - /url: /settings/workspace
                - listitem [ref=e155]:
                  - link "Billing" [ref=e156]:
                    - /url: /settings/billing
            - listitem [ref=e157]:
              - link "Demo" [ref=e158]:
                - /url: /demo
                - generic [ref=e159]: 🧪
                - generic [ref=e160]: Demo
              - list [ref=e161]:
                - listitem [ref=e162]:
                  - link "Responsive Layout" [ref=e163]:
                    - /url: /demo/responsive
                - listitem [ref=e164]:
                  - link "Keyboard Navigation" [ref=e165]:
                    - /url: /demo/keyboard
                - listitem [ref=e166]:
                  - link "Loading Experience" [ref=e167]:
                    - /url: /demo/loading
                - listitem [ref=e168]:
                  - link "Error Pages" [ref=e169]:
                    - /url: /demo/errors
                - listitem [ref=e170]:
                  - link "Empty States" [ref=e171]:
                    - /url: /demo/empty
                - listitem [ref=e172]:
                  - link "Form Patterns" [ref=e173]:
                    - /url: /demo/forms
                - listitem [ref=e174]:
                  - link "Microcopy Gallery" [ref=e175]:
                    - /url: /demo/microcopy
            - listitem [ref=e176]:
              - link "Design System" [ref=e177]:
                - /url: /design-system
                - generic [ref=e178]: 🎨
                - generic [ref=e179]: Design System
              - list [ref=e180]:
                - listitem [ref=e181]:
                  - link "Buttons Preview" [ref=e182]:
                    - /url: /design-system/buttons
                - listitem [ref=e183]:
                  - link "Links Preview" [ref=e184]:
                    - /url: /design-system/links
                - listitem [ref=e185]:
                  - link "Icon Library" [ref=e186]:
                    - /url: /design-system/icons
                - listitem [ref=e187]:
                  - link "Inputs Preview" [ref=e188]:
                    - /url: /design-system/inputs
                - listitem [ref=e189]:
                  - link "Search Preview" [ref=e190]:
                    - /url: /design-system/search
                - listitem [ref=e191]:
                  - link "Password Preview" [ref=e192]:
                    - /url: /design-system/password
                - listitem [ref=e193]:
                  - link "OTP Preview" [ref=e194]:
                    - /url: /design-system/otp
                - listitem [ref=e195]:
                  - link "Checkbox Preview" [ref=e196]:
                    - /url: /design-system/checkbox
                - listitem [ref=e197]:
                  - link "Radio Preview" [ref=e198]:
                    - /url: /design-system/radio
                - listitem [ref=e199]:
                  - link "Switch Preview" [ref=e200]:
                    - /url: /design-system/switch
                - listitem [ref=e201]:
                  - link "Forms Index" [ref=e202]:
                    - /url: /design-system/forms
                - listitem [ref=e203]:
                  - link "Form Layouts" [ref=e204]:
                    - /url: /design-system/forms/layouts
                - listitem [ref=e205]:
                  - link "Form Validation" [ref=e206]:
                    - /url: /design-system/forms/validation
                - listitem [ref=e207]:
                  - link "Address Form" [ref=e208]:
                    - /url: /design-system/forms/address
                - listitem [ref=e209]:
                  - link "File Upload" [ref=e210]:
                    - /url: /design-system/forms/upload
                - listitem [ref=e211]:
                  - link "Select Preview" [ref=e212]:
                    - /url: /design-system/forms/select
                - listitem [ref=e213]:
                  - link "Cards Preview" [ref=e214]:
                    - /url: /design-system/cards
                - listitem [ref=e215]:
                  - link "Tables Preview" [ref=e216]:
                    - /url: /design-system/tables
                - listitem [ref=e217]:
                  - link "Lists Preview" [ref=e218]:
                    - /url: /design-system/lists
                - listitem [ref=e219]:
                  - link "Badges Preview" [ref=e220]:
                    - /url: /design-system/badges
                - listitem [ref=e221]:
                  - link "Chips Preview" [ref=e222]:
                    - /url: /design-system/chips
                - listitem [ref=e223]:
                  - link "Avatars Preview" [ref=e224]:
                    - /url: /design-system/avatars
                - listitem [ref=e225]:
                  - link "Empty States Preview" [ref=e226]:
                    - /url: /design-system/empty-states
                - listitem [ref=e227]:
                  - link "Skeletons Preview" [ref=e228]:
                    - /url: /design-system/skeletons
                - listitem [ref=e229]:
                  - link "Nav Index" [ref=e230]:
                    - /url: /design-system/navigation
                - listitem [ref=e231]:
                  - link "Header Preview" [ref=e232]:
                    - /url: /design-system/header
                - listitem [ref=e233]:
                  - link "Sidebar Preview" [ref=e234]:
                    - /url: /design-system/sidebar
                - listitem [ref=e235]:
                  - link "Breadcrumb Preview" [ref=e236]:
                    - /url: /design-system/breadcrumb
                - listitem [ref=e237]:
                  - link "Menu Preview" [ref=e238]:
                    - /url: /design-system/menu
                - listitem [ref=e239]:
                  - link "Tabs Preview" [ref=e240]:
                    - /url: /design-system/tabs
                - listitem [ref=e241]:
                  - link "Pagination Preview" [ref=e242]:
                    - /url: /design-system/pagination
                - listitem [ref=e243]:
                  - link "Stepper Preview" [ref=e244]:
                    - /url: /design-system/stepper
                - listitem [ref=e245]:
                  - link "Layouts Preview" [ref=e246]:
                    - /url: /design-system/layouts
                - listitem [ref=e247]:
                  - link "Command Palette Preview" [ref=e248]:
                    - /url: /design-system/command-palette
                - listitem [ref=e249]:
                  - link "Dialogs Preview" [ref=e250]:
                    - /url: /design-system/dialogs
                - listitem [ref=e251]:
                  - link "Modals Preview" [ref=e252]:
                    - /url: /design-system/modals
                - listitem [ref=e253]:
                  - link "Toasts Preview" [ref=e254]:
                    - /url: /design-system/toasts
                - listitem [ref=e255]:
                  - link "Notifications Preview" [ref=e256]:
                    - /url: /design-system/notifications
                - listitem [ref=e257]:
                  - link "Alerts Preview" [ref=e258]:
                    - /url: /design-system/alerts
                - listitem [ref=e259]:
                  - link "Tooltips Preview" [ref=e260]:
                    - /url: /design-system/tooltips
                - listitem [ref=e261]:
                  - link "Popovers Preview" [ref=e262]:
                    - /url: /design-system/popovers
                - listitem [ref=e263]:
                  - link "Progress Preview" [ref=e264]:
                    - /url: /design-system/progress
                - listitem [ref=e265]:
                  - link "Loading Preview" [ref=e266]:
                    - /url: /design-system/loading
                - listitem [ref=e267]:
                  - link "Status Preview" [ref=e268]:
                    - /url: /design-system/status
                - listitem [ref=e269]:
                  - link "Charts Preview" [ref=e270]:
                    - /url: /design-system/charts
                - listitem [ref=e271]:
                  - link "KPIs Preview" [ref=e272]:
                    - /url: /design-system/kpis
                - listitem [ref=e273]:
                  - link "Timelines Preview" [ref=e274]:
                    - /url: /design-system/timelines
                - listitem [ref=e275]:
                  - link "Calendars Preview" [ref=e276]:
                    - /url: /design-system/calendars
                - listitem [ref=e277]:
                  - link "Data Filters Preview" [ref=e278]:
                    - /url: /design-system/data-filters
                - listitem [ref=e279]:
                  - link "Export Preview" [ref=e280]:
                    - /url: /design-system/export
                - listitem [ref=e281]:
                  - link "Component Catalog" [ref=e282]:
                    - /url: /design-system/catalog
                - listitem [ref=e283]:
                  - link "Token Explorer" [ref=e284]:
                    - /url: /design-system/tokens
                - listitem [ref=e285]:
                  - link "Accessibility Center" [ref=e286]:
                    - /url: /design-system/accessibility
                - listitem [ref=e287]:
                  - link "Docs Center" [ref=e288]:
                    - /url: /design-system/docs
                - listitem [ref=e289]:
                  - link "Quality Dashboard" [ref=e290]:
                    - /url: /design-system/quality
      - generic [ref=e291]:
        - navigation "Breadcrumb" [ref=e292]:
          - list [ref=e293]:
            - listitem [ref=e294]:
              - link "Public" [ref=e295]:
                - /url: /
              - generic [ref=e296]: /
            - listitem [ref=e297]:
              - generic [ref=e298]: Cart
        - main [ref=e299]:
          - generic [ref=e300]:
            - generic [ref=e301]:
              - generic [ref=e302]:
                - heading "Cart" [level=1] [ref=e303]
                - paragraph [ref=e304]: Shopping cart.
              - button "Checkout" [ref=e305] [cursor=pointer]
            - region "Cart — empty panel" [ref=e306]:
              - generic [ref=e307]:
                - generic [ref=e308]: ◌
                - paragraph [ref=e309]: Cart — empty panel
                - paragraph [ref=e310]: This is a navigation prototype. Production content, tables, forms, and charts arrive in later Sprint 19 parts.
                - button "Checkout" [ref=e311] [cursor=pointer]
    - contentinfo [ref=e312]:
      - generic [ref=e313]: SporeKart Enterprise · Navigation Prototype (Sprint 19 Part 1B)
      - generic [ref=e314]:
        - link "Help" [ref=e315]:
          - /url: /support/kb
        - link "Terms" [ref=e316]:
          - /url: /
        - link "Privacy" [ref=e317]:
          - /url: /
        - link "Status" [ref=e318]:
          - /url: /
```

# Test source

```ts
  628 | });
  629 | 
  630 | // ===================================================================
  631 | // PHASE 8 — CONCURRENCY
  632 | // ===================================================================
  633 | test.describe('Phase 8 — Concurrency', () => {
  634 | 
  635 |   test('Multiple tabs can load simultaneously', async ({ browser }) => {
  636 |     const page1 = await browser.newPage();
  637 |     const page2 = await browser.newPage();
  638 |     const start = Date.now();
  639 |     await Promise.all([
  640 |       page1.goto('/').then(() => page1.waitForLoadState('networkidle')),
  641 |       page2.goto('/products').then(() => page2.waitForLoadState('networkidle')),
  642 |     ]);
  643 |     const totalTime = Date.now() - start;
  644 |     expect(totalTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad * 2);
  645 |     await page1.close();
  646 |     await page2.close();
  647 |   });
  648 | 
  649 |   test('Rapid clicking does not break navigation', async ({ page }) => {
  650 |     await page.goto('/');
  651 |     await page.waitForLoadState('networkidle');
  652 |     const errors: string[] = [];
  653 |     page.on('pageerror', err => errors.push(err.message));
  654 |     const clickPromises = [];
  655 |     for (let i = 0; i < 5; i++) {
  656 |       clickPromises.push(page.goto(KEY_ROUTES[i % KEY_ROUTES.length].path).catch(() => {}));
  657 |     }
  658 |     await Promise.all(clickPromises);
  659 |     expect(errors.length).toBeLessThan(3);
  660 |     await page.waitForLoadState('networkidle');
  661 |     await expect(page.locator('body')).toBeVisible();
  662 |   });
  663 | 
  664 |   test('Simultaneous navigation requests are handled', async ({ page }) => {
  665 |     const errors: string[] = [];
  666 |     page.on('pageerror', err => errors.push(err.message));
  667 |     await Promise.allSettled([
  668 |       page.goto('/products').then(() => page.waitForLoadState('networkidle')),
  669 |       page.goto('/cart').then(() => page.waitForLoadState('networkidle')),
  670 |     ]);
  671 |     await page.waitForLoadState('networkidle');
  672 |     expect(errors.length).toBeLessThan(3);
  673 |   });
  674 | 
  675 |   test('Repeated identical requests do not degrade performance', async ({ page }) => {
  676 |     const start = Date.now();
  677 |     for (let i = 0; i < 5; i++) {
  678 |       await page.goto('/');
  679 |       await page.waitForLoadState('networkidle');
  680 |     }
  681 |     const avgLoadTime = (Date.now() - start) / 5;
  682 |     expect(avgLoadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  683 |   });
  684 | 
  685 |   test('Tab switching preserves state', async ({ browser }) => {
  686 |     const page1 = await browser.newPage();
  687 |     const page2 = await browser.newPage();
  688 |     await page1.goto('/products');
  689 |     await page1.waitForLoadState('networkidle');
  690 |     await page2.goto('/cart');
  691 |     await page2.waitForLoadState('networkidle');
  692 |     await page1.bringToFront();
  693 |     await page1.waitForTimeout(500);
  694 |     expect(page1.url()).toContain('products');
  695 |     await page2.bringToFront();
  696 |     await page2.waitForTimeout(500);
  697 |     expect(page2.url()).toContain('cart');
  698 |     await page1.close();
  699 |     await page2.close();
  700 |   });
  701 | 
  702 |   test('Concurrent browser contexts remain independent', async ({ browser }) => {
  703 |     const ctx1 = await browser.newContext();
  704 |     const ctx2 = await browser.newContext();
  705 |     const p1 = await ctx1.newPage();
  706 |     const p2 = await ctx2.newPage();
  707 |     await Promise.all([
  708 |       p1.goto('/').then(() => p1.waitForLoadState('networkidle')),
  709 |       p2.goto('/dashboard').then(() => p2.waitForLoadState('networkidle')),
  710 |     ]);
  711 |     expect(p1.url()).not.toContain('dashboard');
  712 |     expect(p2.url()).toContain('dashboard');
  713 |     await ctx1.close();
  714 |     await ctx2.close();
  715 |   });
  716 | });
  717 | 
  718 | // ===================================================================
  719 | // PHASE 9 — RESPONSIVE PERFORMANCE
  720 | // ===================================================================
  721 | test.describe('Phase 9 — Responsive Performance', () => {
  722 | 
  723 |   test('Desktop viewport renders all key pages', async ({ page }) => {
  724 |     await page.setViewportSize({ width: 1920, height: 1080 });
  725 |     for (const route of ['/', '/products', '/cart', '/orders', '/dashboard']) {
  726 |       const start = Date.now();
  727 |       await page.goto(route);
> 728 |       await page.waitForLoadState('networkidle');
      |                  ^ TimeoutError: page.waitForLoadState: Timeout 30000ms exceeded.
  729 |       const loadTime = Date.now() - start;
  730 |       expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  731 |     }
  732 |   });
  733 | 
  734 |   test('Tablet viewport renders all key pages', async ({ page }) => {
  735 |     await page.setViewportSize({ width: 768, height: 1024 });
  736 |     for (const route of ['/', '/products', '/cart', '/dashboard', '/settings']) {
  737 |       const start = Date.now();
  738 |       await page.goto(route);
  739 |       await page.waitForLoadState('networkidle');
  740 |       const loadTime = Date.now() - start;
  741 |       expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  742 |     }
  743 |   });
  744 | 
  745 |   test('Mobile viewport renders all key pages', async ({ page }) => {
  746 |     await page.setViewportSize({ width: 375, height: 812 });
  747 |     for (const route of ['/', '/products', '/login', '/search', '/dashboard']) {
  748 |       const start = Date.now();
  749 |       await page.goto(route);
  750 |       await page.waitForLoadState('networkidle');
  751 |       const loadTime = Date.now() - start;
  752 |       expect(loadTime).toBeLessThan(PERFORMANCE_THRESHOLDS.pageLoad);
  753 |     }
  754 |   });
  755 | 
  756 |   test('Scrolling performance on list pages', async ({ page }) => {
  757 |     await page.setViewportSize({ width: 375, height: 812 });
  758 |     await page.goto('/products');
  759 |     await page.waitForLoadState('networkidle');
  760 |     const scrollStart = Date.now();
  761 |     await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
  762 |     await page.waitForTimeout(500);
  763 |     await page.evaluate(() => window.scrollTo(0, 0));
  764 |     await page.waitForTimeout(500);
  765 |     const scrollTime = Date.now() - scrollStart;
  766 |     expect(scrollTime).toBeLessThan(3000);
  767 |   });
  768 | 
  769 |   test('No layout shifts on viewport changes', async ({ page }) => {
  770 |     await page.goto('/');
  771 |     await page.waitForLoadState('networkidle');
  772 |     const shifts = await page.evaluate(() => {
  773 |       return new Promise<number>((resolve) => {
  774 |         let count = 0;
  775 |         const observer = new PerformanceObserver((list) => {
  776 |           for (const entry of list.getEntries()) {
  777 |             if ((entry as any).hadRecentInput) continue;
  778 |             count++;
  779 |           }
  780 |         });
  781 |         observer.observe({ type: 'layout-shift', buffered: true });
  782 |         setTimeout(() => {
  783 |           observer.disconnect();
  784 |           resolve(count);
  785 |         }, 500);
  786 |       });
  787 |     });
  788 |     expect(shifts).toBeGreaterThanOrEqual(0);
  789 |   });
  790 | });
  791 | 
  792 | // ===================================================================
  793 | // PHASE 10 — CROSS-BROWSER
  794 | // ===================================================================
  795 | test.describe('Phase 10 — Cross-Browser Performance', () => {
  796 | 
  797 |   test('Browser capabilities detected', async ({ page }) => {
  798 |     const info = await page.evaluate(() => ({
  799 |       userAgent: navigator.userAgent,
  800 |       vendor: navigator.vendor,
  801 |       platform: navigator.platform,
  802 |       language: navigator.language,
  803 |       cookieEnabled: navigator.cookieEnabled,
  804 |     }));
  805 |     expect(info.userAgent.length).toBeGreaterThan(0);
  806 |     expect(info.language.length).toBeGreaterThan(0);
  807 |   });
  808 | 
  809 |   test('Performance API available across browsers', async ({ page }) => {
  810 |     const hasPerformanceAPI = await page.evaluate(() => {
  811 |       return typeof performance !== 'undefined'
  812 |         && typeof performance.getEntriesByType === 'function'
  813 |         && typeof performance.now === 'function';
  814 |     });
  815 |     expect(hasPerformanceAPI).toBe(true);
  816 |   });
  817 | 
  818 |   test('Render engine reports timing data', async ({ page }) => {
  819 |     await page.goto('/');
  820 |     await page.waitForLoadState('networkidle');
  821 |     const timing = await measurePageTiming(page);
  822 |     expect(timing.domComplete).toBeGreaterThan(0);
  823 |     expect(timing.responseEnd).toBeGreaterThan(0);
  824 |   });
  825 | });
  826 | 
  827 | // ===================================================================
  828 | // PHASE 11 — ACCESSIBILITY IMPACT
```