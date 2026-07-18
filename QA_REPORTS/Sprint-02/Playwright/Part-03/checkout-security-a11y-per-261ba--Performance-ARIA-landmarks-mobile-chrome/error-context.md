# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-security-a11y-perf.spec.ts >> Checkout Security, A11y, Performance >> ARIA landmarks
- Location: tests\checkout-security-a11y-perf.spec.ts:20:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator:  locator('nav,[role="navigation"]').first()
Expected: visible
Received: hidden
Timeout:  5000ms

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('nav,[role="navigation"]').first()
    13 × locator resolved to <nav aria-label="Primary" class="sk-public-nav">…</nav>
       - unexpected value "hidden"

```

```yaml
- link "Skip to content":
  - /url: "#main"
- region "Announcement":
  - text: Free shipping on your first cultivation kit — shop the catalog today.
  - link "Shop now":
    - /url: /products
  - button "Dismiss announcement":
    - img "Dismiss announcement"
- banner:
  - button "Open navigation menu":
    - img "Open navigation menu"
  - link "SporeKart home":
    - /url: /
    - text: SporeKart
  - button "Search":
    - img "Search"
  - link "Sign in Sign In":
    - /url: /auth
    - img "Sign in"
    - text: Sign In
- main:
  - progressbar "Page scroll progress"
  - region "Grow premium mushrooms with confidence.":
    - img "SporeKart fresh mushrooms and cultivation products"
    - img "Natural"
    - text: India’s organic & natural mushroom ecosystem
    - heading "Grow premium mushrooms with confidence." [level=1]:
      - text: Grow premium mushrooms with
      - emphasis: confidence.
    - paragraph: From lab-verified spawn to harvest-ready kits and expert training, SporeKart gives every cultivator — home grower, farm, or enterprise — the tools, knowledge, and support to succeed. Technology-driven agriculture, made farmer-friendly.
    - link "Shop Spawn & Kits Shop":
      - /url: /products
      - text: Shop Spawn & Kits
      - img "Shop"
    - link "Training Explore Training":
      - /url: /training
      - img "Training"
      - text: Explore Training
    - list "Why growers choose SporeKart":
      - listitem: Lab-verified spawn Purity-guaranteed cultures
      - listitem: Pan-India delivery Cold-chain fresh
      - listitem: Expert training Farmer-first courses
      - listitem: Grower support 7 days a week
  - region "Featured products":
    - text: Catalog
    - heading "Featured products" [level=2]
    - paragraph: From your first spawn to a full harvest — everything a cultivator needs.
    - tablist "Product categories":
      - button "Spawn Seeds" [pressed]
      - button "Fresh Mushrooms"
      - button "Dry Mushrooms"
      - button "Growing Kits"
      - button "Accessories"
      - button "Knowledge"
    - tabpanel "Spawn Seeds products": Oyster Mushroom Spawn ₹249 Button Mushroom Spawn ₹299 Shiitake Spawn Bag ₹349 King Oyster Spawn ₹279
  - region "Learn to grow — with farmers who’ve done it":
    - text: Training
    - heading "Learn to grow — with farmers who’ve done it" [level=2]
    - paragraph: Our upcoming training programs take you from fundamentals to commercial cultivation — built with working farmers and mycologists.
    - list:
      - listitem:
        - img "Live, expert-led sessions"
        - text: Live, expert-led sessions
      - listitem:
        - img "Hands-on cultivation labs"
        - text: Hands-on cultivation labs
      - listitem:
        - img "Measurable yield outcomes"
        - text: Measurable yield outcomes
    - button "Browse training Register":
      - text: Browse training
      - img "Register"
  - region "Proven where it matters most":
    - paragraph: Why cultivators trust SporeKart
    - heading "Proven where it matters most" [level=2]
    - text: 0 Years of experience(placeholder) 0 Farmers served(placeholder) 0 Training programs(placeholder) 0 Products delivered(placeholder) 0 Quality commitment(placeholder) ISO Quality certifications(placeholder) 0 Years of experience(placeholder) 0 Farmers served(placeholder) 0 Training programs(placeholder) 0 Products delivered(placeholder) 0 Quality commitment(placeholder) ISO Quality certifications(placeholder)
  - region "Farmers growing with SporeKart":
    - text: Success stories
    - heading "Farmers growing with SporeKart" [level=2]
    - figure "Meena R. Smallholder farmer, Maharashtra":
      - blockquote: “SporeKart’s spawn gave us our first successful flush in weeks. The support team felt like part of our farm.”
      - text: Meena R. Smallholder farmer, Maharashtra
    - button "Previous testimonial":
      - img "Previous"
    - button "Next testimonial":
      - img "Next"
    - button "Read more stories"
  - region "From first spore to thriving business":
    - text: The journey
    - heading "From first spore to thriving business" [level=2]
    - heading "1.Learn" [level=3]
    - paragraph: Explore training and playbooks to master the basics.
    - heading "2.Buy Spawn" [level=3]
    - paragraph: Order lab-verified spawn and starter kits.
    - heading "3.Cultivate" [level=3]
    - paragraph: Follow guided steps from inoculation to fruiting.
    - heading "4.Harvest" [level=3]
    - paragraph: Pick at peak with our freshness and drying guidance.
    - heading "5.Sell" [level=3]
    - paragraph: List and supply through local and partner channels.
    - heading "6.Grow Business" [level=3]
    - paragraph: Scale beds and yields with ongoing support.
  - region "Cultivation is a craft — and a cause":
    - text: Our story
    - heading "Cultivation is a craft — and a cause" [level=2]
    - paragraph: We started SporeKart to remove the guesswork from mushroom farming. Here is the belief that drives everything we build.
    - heading "Who we are" [level=3]
    - paragraph: SporeKart is a mushroom cultivation ecosystem built by growers, for growers — pairing science with practical farm know-how.
    - heading "Why mushrooms matter" [level=3]
    - paragraph: Mushrooms are nutritious, resource-light, and climate-friendly. They turn agricultural waste into livelihood.
    - heading "Why we exist" [level=3]
    - paragraph: We make premium spawn, training, and support accessible so any cultivator can grow with confidence.
  - region "Recognition and certifications":
    - paragraph: Recognized by partners & certified to standards
    - text: State Agri Board Krishi Vikas Myco Labs FarmCoop AgriTech Hub Rural Collective State Agri Board Krishi Vikas Myco Labs FarmCoop AgriTech Hub Rural Collective ISO 22000 FSSAI Organic NPOP GAP Lab-Verified ISO 22000 FSSAI Organic NPOP GAP Lab-Verified
  - region "Built for cultivators, end to end":
    - text: Why SporeKart
    - heading "Built for cultivators, end to end" [level=2]
    - paragraph: Six reasons thousands of growers across India build their farms with SporeKart.
    - article:
      - img "Premium Quality"
      - heading "Premium Quality" [level=3]
      - paragraph: Lab-verified spawn and strict contamination controls at every step.
    - article:
      - img "Research-Led"
      - heading "Research-Led" [level=3]
      - paragraph: Breeding and techniques informed by working mycologists.
    - article:
      - img "Grower Support"
      - heading "Grower Support" [level=3]
      - paragraph: Guidance from our cultivation team whenever you need it.
    - article:
      - img "Fast Delivery"
      - heading "Fast Delivery" [level=3]
      - paragraph: Pan-India cold-chain shipping that protects viability.
    - article:
      - img "Education First"
      - heading "Education First" [level=3]
      - paragraph: Training and playbooks that shorten your learning curve.
    - article:
      - img "Sustainable"
      - heading "Sustainable" [level=3]
      - paragraph: Low-waste kits and responsible sourcing for cleaner farms.
  - region "Knowledge, free and open to every grower":
    - text: Resources
    - heading "Knowledge, free and open to every grower" [level=2]
    - article "Getting started with oyster mushrooms":
      - img "Blog"
      - text: Blog
      - heading "Getting started with oyster mushrooms" [level=3]
      - paragraph: A beginner-friendly walkthrough for your first grow.
    - article "Substrate preparation, step by step":
      - img "Guide"
      - text: Guide
      - heading "Substrate preparation, step by step" [level=3]
      - paragraph: How to pasteurize and pack substrate safely at home.
    - article "What you’ll learn in the masterclass":
      - img "Training"
      - text: Training
      - heading "What you’ll learn in the masterclass" [level=3]
      - paragraph: Syllabus and outcomes from our flagship program.
    - article "SporeKart expands pan-India delivery":
      - img "News"
      - text: News
      - heading "SporeKart expands pan-India delivery" [level=3]
      - paragraph: Faster cold-chain shipping to more Pin codes.
    - button "All resources All":
      - text: All resources
      - img "All"
  - region "Frequently asked questions":
    - text: FAQ
    - heading "Frequently asked questions" [level=2]
    - heading "What is included in a growing kit? Collapse" [level=3]:
      - button "What is included in a growing kit? Collapse" [expanded]:
        - text: What is included in a growing kit?
        - img "Collapse"
    - region "What is included in a growing kit? Collapse": Each kit ships with prepared substrate, verified spawn, and step-by-step instructions so you can start cultivating in days.
    - heading "Do you ship across India? Expand" [level=3]:
      - button "Do you ship across India? Expand":
        - text: Do you ship across India?
        - img "Expand"
    - heading "Is training suitable for beginners? Expand" [level=3]:
      - button "Is training suitable for beginners? Expand":
        - text: Is training suitable for beginners?
        - img "Expand"
    - heading "How do I get cultivation support? Expand" [level=3]:
      - button "How do I get cultivation support? Expand":
        - text: How do I get cultivation support?
        - img "Expand"
    - button "View all FAQs All":
      - text: View all FAQs
      - img "All"
  - region "Join the SporeKart community":
    - heading "Join the SporeKart community" [level=2]
    - paragraph: Get cultivation tips, new-strain drops, and grower event invites — straight to your inbox.
    - text: Email address
    - textbox "Email address":
      - /placeholder: you@farm.example
    - button "Subscribe Subscribe":
      - text: Subscribe
      - img "Subscribe"
    - list:
      - listitem:
        - img "Cultivation guides & playbooks"
        - text: Cultivation guides & playbooks
      - listitem:
        - img "Early access to new strains"
        - text: Early access to new strains
      - listitem:
        - img "Invites to grower community events"
        - text: Invites to grower community events
- contentinfo:
  - text: SporeKart
  - paragraph: Mushroom cultivation, simplified.
  - link "Email":
    - /url: https://example.com
    - img "Email"
  - link "Phone":
    - /url: https://example.com
    - img "Phone"
  - link "Location":
    - /url: https://example.com
    - img "Location"
  - navigation "Explore":
    - heading "Explore" [level=2]
    - link "Products":
      - /url: /products
    - link "Training":
      - /url: /training
    - link "Blog":
      - /url: /blog
    - link "About":
      - /url: /about
    - link "FAQ":
      - /url: /faq
    - link "Certifications":
      - /url: /certifications
  - navigation "Company":
    - heading "Company" [level=2]
    - link "Contact":
      - /url: /contact
    - link "Privacy Policy":
      - /url: /privacy-policy
    - link "Terms & Conditions":
      - /url: /terms-and-conditions
  - navigation "Policies":
    - heading "Policies" [level=2]
    - link "Refund Policy":
      - /url: /refund-policy
    - link "Shipping Policy":
      - /url: /shipping-policy
    - link "Sign In":
      - /url: /auth
  - text: © 2026 SporeKart. All rights reserved.
  - img "Secure"
  - text: Secure & compliant Design & Developed by Pravara Media
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
  14 | test.describe('Checkout Security, A11y, Performance', () => {
  15 |   test('Unauth /admin/orders redirects',async({page})=>{await page.goto('/admin/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  16 |   test('Unauth /dashboard/addresses redirects',async({page})=>{await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  17 |   test('Session-expired page renders',async({page})=>{await page.goto('/session-expired',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  18 |   test('Access-denied page renders',async({page})=>{await page.goto('/access-denied',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  19 |   test('Skip to content link',async({page})=>{await page.goto('/');const l=page.locator('a[href="#main-content"],a[href="#content"],a:has-text("Skip"),[class*="skip"]');await expect(l.first()).toBeVisible({timeout:5000});});
> 20 |   test('ARIA landmarks',async({page})=>{await page.goto('/');await expect(page.locator('main,[role="main"]').first()).toBeVisible({timeout:5000});await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({timeout:5000});await expect(page.locator('footer,[role="contentinfo"]').first()).toBeVisible({timeout:5000});});
     |                                                                                                                                                                                                                 ^ Error: expect(locator).toBeVisible() failed
  21 |   test('Images have alt text',async({page})=>{await page.goto('/');const imgs=page.locator('img');const c=await imgs.count();let missing=0;for(let i=0;i<c;i++){const alt=await imgs.nth(i).getAttribute('alt');if(alt===null||alt===undefined)missing++;}expect(missing).toBe(0);});
  22 |   test('Performance: orders load <10s',async({page})=>{const s=Date.now();await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(Date.now()-s).toBeLessThan(15000);});
  23 |   test('No console errors on orders',async({page})=>{const errs=[];page.on('console',m=>{if(m.type()==='error')errs.push(m.text())});await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(errs.length).toBe(0);});
  24 |   test('No failed network requests',async({page})=>{const fails=[];page.on('requestfailed',r=>fails.push(r.url()));await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(fails.length).toBe(0);});
  25 | });
  26 | 
```