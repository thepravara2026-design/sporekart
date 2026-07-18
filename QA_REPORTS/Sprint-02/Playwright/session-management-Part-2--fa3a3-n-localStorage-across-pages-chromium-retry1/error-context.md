# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: session-management.spec.ts >> Part 2 — Session Management Validation >> Theme preference persists in localStorage across pages
- Location: tests\session-management.spec.ts:213:7

# Error details

```
Error: expect(received).toBe(expected) // Object.is equality

Expected: "string"
Received: "object"
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3] [cursor=pointer]:
    - /url: "#main"
  - generic [ref=e4]:
    - region "Announcement" [ref=e5]:
      - generic [ref=e6]:
        - generic [ref=e7]: Free shipping on your first cultivation kit — shop the catalog today.
        - link "Shop now" [ref=e8] [cursor=pointer]:
          - /url: /products
        - button "Dismiss announcement" [ref=e9] [cursor=pointer]:
          - img "Dismiss announcement" [ref=e10]
    - banner [ref=e13]:
      - link "SporeKart home" [ref=e15] [cursor=pointer]:
        - /url: /
        - generic [ref=e16]: ❖
        - generic [ref=e17]: SporeKart
      - navigation "Primary" [ref=e19]:
        - list [ref=e20]:
          - listitem [ref=e21]:
            - link "Products" [ref=e22] [cursor=pointer]:
              - /url: /products
          - listitem [ref=e23]:
            - link "Courses" [ref=e24] [cursor=pointer]:
              - /url: /training/courses
          - listitem [ref=e25]:
            - link "Training" [ref=e26] [cursor=pointer]:
              - /url: /training
          - listitem [ref=e27]:
            - link "Blog" [ref=e28] [cursor=pointer]:
              - /url: /blog
          - listitem [ref=e29]:
            - link "About" [ref=e30] [cursor=pointer]:
              - /url: /about
          - listitem [ref=e31]:
            - link "Contact" [ref=e32] [cursor=pointer]:
              - /url: /contact
      - generic [ref=e33]:
        - button "Search" [ref=e34] [cursor=pointer]:
          - img "Search" [ref=e35]
        - link "Sign in Sign In" [ref=e38] [cursor=pointer]:
          - /url: /auth
          - img "Sign in" [ref=e39]
          - text: Sign In
    - main [ref=e42]:
      - progressbar "Page scroll progress"
      - region "Grow premium mushrooms with confidence." [ref=e43]:
        - img "SporeKart fresh mushrooms and cultivation products" [ref=e44]
        - generic [ref=e46]:
          - generic [ref=e47]:
            - generic [ref=e49]:
              - img "Natural" [ref=e50]
              - text: India’s organic & natural mushroom ecosystem
            - heading "Grow premium mushrooms with confidence." [level=1] [ref=e57]:
              - text: Grow premium mushrooms with
              - emphasis [ref=e58]: confidence.
            - paragraph [ref=e60]: From lab-verified spawn to harvest-ready kits and expert training, SporeKart gives every cultivator — home grower, farm, or enterprise — the tools, knowledge, and support to succeed. Technology-driven agriculture, made farmer-friendly.
            - generic [ref=e62]:
              - link "Shop Spawn & Kits Shop" [ref=e63] [cursor=pointer]:
                - /url: /products
                - text: Shop Spawn & Kits
                - img "Shop" [ref=e64]
              - link "Training Explore Training" [ref=e66] [cursor=pointer]:
                - /url: /training
                - img "Training" [ref=e67]
                - text: Explore Training
          - list "Why growers choose SporeKart" [ref=e71]:
            - listitem [ref=e72]:
              - img [ref=e74]
              - generic [ref=e76]:
                - generic [ref=e77]: Lab-verified spawn
                - generic [ref=e78]: Purity-guaranteed cultures
            - listitem [ref=e79]:
              - img [ref=e81]
              - generic [ref=e86]:
                - generic [ref=e87]: Pan-India delivery
                - generic [ref=e88]: Cold-chain fresh
            - listitem [ref=e89]:
              - img [ref=e91]
              - generic [ref=e94]:
                - generic [ref=e95]: Expert training
                - generic [ref=e96]: Farmer-first courses
            - listitem [ref=e97]:
              - img [ref=e99]
              - generic [ref=e102]:
                - generic [ref=e103]: Grower support
                - generic [ref=e104]: 7 days a week
      - region "Featured products" [ref=e106]:
        - generic [ref=e107]:
          - generic [ref=e108]:
            - text: Catalog
            - heading "Featured products" [level=2] [ref=e109]
            - paragraph [ref=e110]: From your first spawn to a full harvest — everything a cultivator needs.
          - tablist "Product categories" [ref=e111]:
            - button "Spawn Seeds" [pressed] [ref=e112] [cursor=pointer]:
              - img [ref=e113]
              - text: Spawn Seeds
            - button "Fresh Mushrooms" [ref=e117] [cursor=pointer]:
              - img [ref=e118]
              - text: Fresh Mushrooms
            - button "Dry Mushrooms" [ref=e124] [cursor=pointer]:
              - img [ref=e125]
              - text: Dry Mushrooms
            - button "Growing Kits" [ref=e128] [cursor=pointer]:
              - img [ref=e129]
              - text: Growing Kits
            - button "Accessories" [ref=e133] [cursor=pointer]:
              - img [ref=e134]
              - text: Accessories
            - button "Knowledge" [ref=e136] [cursor=pointer]:
              - img [ref=e137]
              - text: Knowledge
          - tabpanel "Spawn Seeds products" [ref=e140]:
            - generic [ref=e141] [cursor=pointer]:
              - img [ref=e144]
              - generic [ref=e148]:
                - generic [ref=e149]: Oyster Mushroom Spawn
                - generic [ref=e150]: ₹249
            - generic [ref=e151] [cursor=pointer]:
              - img [ref=e154]
              - generic [ref=e158]:
                - generic [ref=e159]: Button Mushroom Spawn
                - generic [ref=e160]: ₹299
            - generic [ref=e161] [cursor=pointer]:
              - img [ref=e164]
              - generic [ref=e168]:
                - generic [ref=e169]: Shiitake Spawn Bag
                - generic [ref=e170]: ₹349
            - generic [ref=e171] [cursor=pointer]:
              - img [ref=e174]
              - generic [ref=e178]:
                - generic [ref=e179]: King Oyster Spawn
                - generic [ref=e180]: ₹279
      - region "Learn to grow — with farmers who’ve done it" [ref=e182]:
        - generic [ref=e184]:
          - generic [ref=e185]:
            - text: Training
            - heading "Learn to grow — with farmers who’ve done it" [level=2] [ref=e186]
            - paragraph [ref=e187]: Our upcoming training programs take you from fundamentals to commercial cultivation — built with working farmers and mycologists.
            - list [ref=e188]:
              - listitem [ref=e189]:
                - img "Live, expert-led sessions" [ref=e190]
                - text: Live, expert-led sessions
              - listitem [ref=e195]:
                - img "Hands-on cultivation labs" [ref=e196]
                - text: Hands-on cultivation labs
              - listitem [ref=e199]:
                - img "Measurable yield outcomes" [ref=e200]
                - text: Measurable yield outcomes
            - button "Browse training Register" [ref=e204] [cursor=pointer]:
              - generic [ref=e205]: Browse training
              - img "Register" [ref=e207]
          - generic [ref=e209]:
            - figure [ref=e210]:
              - generic [ref=e211]:
                - img [ref=e212]
                - generic [ref=e217]: Training session
                - generic [ref=e218]: Image placeholder
              - generic [ref=e219]: Training photography placeholder
            - generic [ref=e220]:
              - img [ref=e221]
              - text: "Upcoming: Oyster Mushroom Masterclass"
            - paragraph [ref=e224]: A 4-week practical program covering substrate prep, inoculation, fruiting, and harvest.
            - generic [ref=e225]: Enrolling now
      - region "Proven where it matters most" [ref=e227]:
        - generic [ref=e229]:
          - paragraph [ref=e230]: Why cultivators trust SporeKart
          - heading "Proven where it matters most" [level=2] [ref=e231]
        - generic [ref=e233]:
          - generic [ref=e234]:
            - img [ref=e236]
            - generic [ref=e239]:
              - generic [ref=e240]: "0"
              - generic [ref=e241]: Years of experience(placeholder)
          - generic [ref=e242]:
            - img [ref=e244]
            - generic [ref=e249]:
              - generic [ref=e250]: "0"
              - generic [ref=e251]: Farmers served(placeholder)
          - generic [ref=e252]:
            - img [ref=e254]
            - generic [ref=e257]:
              - generic [ref=e258]: "0"
              - generic [ref=e259]: Training programs(placeholder)
          - generic [ref=e260]:
            - img [ref=e262]
            - generic [ref=e266]:
              - generic [ref=e267]: "0"
              - generic [ref=e268]: Products delivered(placeholder)
          - generic [ref=e269]:
            - img [ref=e271]
            - generic [ref=e274]:
              - generic [ref=e275]: "0"
              - generic [ref=e276]: Quality commitment(placeholder)
          - generic [ref=e277]:
            - img [ref=e279]
            - generic [ref=e281]:
              - generic [ref=e282]: ISO
              - generic [ref=e283]: Quality certifications(placeholder)
          - generic [ref=e284]:
            - img [ref=e286]
            - generic [ref=e289]:
              - generic [ref=e290]: "0"
              - generic [ref=e291]: Years of experience(placeholder)
          - generic [ref=e292]:
            - img [ref=e294]
            - generic [ref=e299]:
              - generic [ref=e300]: "0"
              - generic [ref=e301]: Farmers served(placeholder)
          - generic [ref=e302]:
            - img [ref=e304]
            - generic [ref=e307]:
              - generic [ref=e308]: "0"
              - generic [ref=e309]: Training programs(placeholder)
          - generic [ref=e310]:
            - img [ref=e312]
            - generic [ref=e316]:
              - generic [ref=e317]: "0"
              - generic [ref=e318]: Products delivered(placeholder)
          - generic [ref=e319]:
            - img [ref=e321]
            - generic [ref=e324]:
              - generic [ref=e325]: "0"
              - generic [ref=e326]: Quality commitment(placeholder)
          - generic [ref=e327]:
            - img [ref=e329]
            - generic [ref=e331]:
              - generic [ref=e332]: ISO
              - generic [ref=e333]: Quality certifications(placeholder)
      - region "Farmers growing with SporeKart" [ref=e335]:
        - generic [ref=e337]:
          - generic [ref=e338]:
            - img [ref=e340]
            - generic [ref=e343]: Customer story video placeholder
          - generic [ref=e344]:
            - text: Success stories
            - heading "Farmers growing with SporeKart" [level=2] [ref=e345]
            - figure "Meena R. Smallholder farmer, Maharashtra" [ref=e346]:
              - blockquote [ref=e347]: “SporeKart’s spawn gave us our first successful flush in weeks. The support team felt like part of our farm.”
              - generic [ref=e348]:
                - generic [ref=e349]: MR
                - generic [ref=e350]:
                  - generic [ref=e351]: Meena R.
                  - generic [ref=e352]: Smallholder farmer, Maharashtra
            - generic [ref=e353]:
              - button "Previous testimonial" [ref=e354] [cursor=pointer]:
                - img "Previous" [ref=e355]
              - button "Next testimonial" [ref=e357] [cursor=pointer]:
                - img "Next" [ref=e358]
            - button "Read more stories" [ref=e365] [cursor=pointer]:
              - generic [ref=e366]: Read more stories
      - region "From first spore to thriving business" [ref=e368]:
        - generic [ref=e369]:
          - generic [ref=e370]:
            - text: The journey
            - heading "From first spore to thriving business" [level=2] [ref=e371]
          - generic [ref=e372]:
            - generic [ref=e374]:
              - img [ref=e376]
              - generic [ref=e379]:
                - heading "1.Learn" [level=3] [ref=e380]:
                  - generic [ref=e381]: "1."
                  - text: Learn
                - paragraph [ref=e382]: Explore training and playbooks to master the basics.
            - generic [ref=e384]:
              - img [ref=e386]
              - generic [ref=e390]:
                - heading "2.Buy Spawn" [level=3] [ref=e391]:
                  - generic [ref=e392]: "2."
                  - text: Buy Spawn
                - paragraph [ref=e393]: Order lab-verified spawn and starter kits.
            - generic [ref=e395]:
              - img [ref=e397]
              - generic [ref=e403]:
                - heading "3.Cultivate" [level=3] [ref=e404]:
                  - generic [ref=e405]: "3."
                  - text: Cultivate
                - paragraph [ref=e406]: Follow guided steps from inoculation to fruiting.
            - generic [ref=e408]:
              - img [ref=e410]
              - generic [ref=e413]:
                - heading "4.Harvest" [level=3] [ref=e414]:
                  - generic [ref=e415]: "4."
                  - text: Harvest
                - paragraph [ref=e416]: Pick at peak with our freshness and drying guidance.
            - generic [ref=e418]:
              - img [ref=e420]
              - generic [ref=e422]:
                - heading "5.Sell" [level=3] [ref=e423]:
                  - generic [ref=e424]: "5."
                  - text: Sell
                - paragraph [ref=e425]: List and supply through local and partner channels.
            - generic [ref=e427]:
              - img [ref=e429]
              - generic [ref=e432]:
                - heading "6.Grow Business" [level=3] [ref=e433]:
                  - generic [ref=e434]: "6."
                  - text: Grow Business
                - paragraph [ref=e435]: Scale beds and yields with ongoing support.
      - region "Cultivation is a craft — and a cause" [ref=e437]:
        - generic [ref=e439]:
          - generic [ref=e440]:
            - text: Our story
            - heading "Cultivation is a craft — and a cause" [level=2] [ref=e441]
            - paragraph [ref=e442]: We started SporeKart to remove the guesswork from mushroom farming. Here is the belief that drives everything we build.
          - generic [ref=e443]:
            - generic [ref=e445]:
              - img [ref=e447]
              - heading "Who we are" [level=3] [ref=e452]
              - paragraph [ref=e453]: SporeKart is a mushroom cultivation ecosystem built by growers, for growers — pairing science with practical farm know-how.
            - generic [ref=e455]:
              - img [ref=e457]
              - heading "Why mushrooms matter" [level=3] [ref=e463]
              - paragraph [ref=e464]: Mushrooms are nutritious, resource-light, and climate-friendly. They turn agricultural waste into livelihood.
            - generic [ref=e466]:
              - img [ref=e468]
              - heading "Why we exist" [level=3] [ref=e470]
              - paragraph [ref=e471]: We make premium spawn, training, and support accessible so any cultivator can grow with confidence.
      - region "Recognition and certifications" [ref=e473]:
        - paragraph [ref=e476]: Recognized by partners & certified to standards
        - generic [ref=e477]:
          - generic [ref=e478]:
            - generic [ref=e479]: State Agri Board
            - generic [ref=e480]: Krishi Vikas
            - generic [ref=e481]: Myco Labs
            - generic [ref=e482]: FarmCoop
            - generic [ref=e483]: AgriTech Hub
            - generic [ref=e484]: Rural Collective
            - generic [ref=e485]: State Agri Board
            - generic [ref=e486]: Krishi Vikas
            - generic [ref=e487]: Myco Labs
            - generic [ref=e488]: FarmCoop
            - generic [ref=e489]: AgriTech Hub
            - generic [ref=e490]: Rural Collective
          - generic [ref=e491]:
            - generic [ref=e492]: ISO 22000
            - generic [ref=e493]: FSSAI
            - generic [ref=e494]: Organic NPOP
            - generic [ref=e495]: GAP
            - generic [ref=e496]: Lab-Verified
            - generic [ref=e497]: ISO 22000
            - generic [ref=e498]: FSSAI
            - generic [ref=e499]: Organic NPOP
            - generic [ref=e500]: GAP
            - generic [ref=e501]: Lab-Verified
      - region "Built for cultivators, end to end" [ref=e503]:
        - generic [ref=e504]:
          - generic [ref=e505]:
            - text: Why SporeKart
            - heading "Built for cultivators, end to end" [level=2] [ref=e506]
            - paragraph [ref=e507]: Six reasons thousands of growers across India build their farms with SporeKart.
          - generic [ref=e508]:
            - article [ref=e509]:
              - generic [ref=e510]:
                - img "Premium Quality" [ref=e512]
                - heading "Premium Quality" [level=3] [ref=e514]
                - paragraph [ref=e515]: Lab-verified spawn and strict contamination controls at every step.
            - article [ref=e516]:
              - generic [ref=e517]:
                - img "Research-Led" [ref=e519]
                - heading "Research-Led" [level=3] [ref=e521]
                - paragraph [ref=e522]: Breeding and techniques informed by working mycologists.
            - article [ref=e523]:
              - generic [ref=e524]:
                - img "Grower Support" [ref=e526]
                - heading "Grower Support" [level=3] [ref=e529]
                - paragraph [ref=e530]: Guidance from our cultivation team whenever you need it.
            - article [ref=e531]:
              - generic [ref=e532]:
                - img "Fast Delivery" [ref=e534]
                - heading "Fast Delivery" [level=3] [ref=e539]
                - paragraph [ref=e540]: Pan-India cold-chain shipping that protects viability.
            - article [ref=e541]:
              - generic [ref=e542]:
                - img "Education First" [ref=e544]
                - heading "Education First" [level=3] [ref=e547]
                - paragraph [ref=e548]: Training and playbooks that shorten your learning curve.
            - article [ref=e549]:
              - generic [ref=e550]:
                - img "Sustainable" [ref=e552]
                - heading "Sustainable" [level=3] [ref=e558]
                - paragraph [ref=e559]: Low-waste kits and responsible sourcing for cleaner farms.
      - region "Knowledge, free and open to every grower" [ref=e561]:
        - generic [ref=e562]:
          - generic [ref=e563]:
            - text: Resources
            - heading "Knowledge, free and open to every grower" [level=2] [ref=e564]
          - generic [ref=e565]:
            - article "Getting started with oyster mushrooms" [ref=e566] [cursor=pointer]:
              - generic [ref=e567]:
                - generic [ref=e568]:
                  - img "Blog" [ref=e569]
                  - text: Blog
                - heading "Getting started with oyster mushrooms" [level=3] [ref=e572]
                - paragraph [ref=e573]: A beginner-friendly walkthrough for your first grow.
            - article "Substrate preparation, step by step" [ref=e574] [cursor=pointer]:
              - generic [ref=e575]:
                - generic [ref=e576]:
                  - img "Guide" [ref=e577]
                  - text: Guide
                - heading "Substrate preparation, step by step" [level=3] [ref=e580]
                - paragraph [ref=e581]: How to pasteurize and pack substrate safely at home.
            - article "What you’ll learn in the masterclass" [ref=e582] [cursor=pointer]:
              - generic [ref=e583]:
                - generic [ref=e584]:
                  - img "Training" [ref=e585]
                  - text: Training
                - heading "What you’ll learn in the masterclass" [level=3] [ref=e588]
                - paragraph [ref=e589]: Syllabus and outcomes from our flagship program.
            - article "SporeKart expands pan-India delivery" [ref=e590] [cursor=pointer]:
              - generic [ref=e591]:
                - generic [ref=e592]:
                  - img "News" [ref=e593]
                  - text: News
                - heading "SporeKart expands pan-India delivery" [level=3] [ref=e596]
                - paragraph [ref=e597]: Faster cold-chain shipping to more Pin codes.
          - button "All resources All" [ref=e599] [cursor=pointer]:
            - generic [ref=e600]: All resources
            - img "All" [ref=e602]
      - region "Frequently asked questions" [ref=e605]:
        - generic [ref=e606]:
          - generic [ref=e607]:
            - text: FAQ
            - heading "Frequently asked questions" [level=2] [ref=e608]
          - generic [ref=e609]:
            - generic [ref=e610]:
              - heading "What is included in a growing kit? Collapse" [level=3] [ref=e611]:
                - button "What is included in a growing kit? Collapse" [expanded] [ref=e612] [cursor=pointer]:
                  - text: What is included in a growing kit?
                  - img "Collapse" [ref=e613]
              - region "What is included in a growing kit? Collapse" [ref=e615]: Each kit ships with prepared substrate, verified spawn, and step-by-step instructions so you can start cultivating in days.
            - heading "Do you ship across India? Expand" [level=3] [ref=e617]:
              - button "Do you ship across India? Expand" [ref=e618] [cursor=pointer]:
                - text: Do you ship across India?
                - img "Expand" [ref=e619]
            - heading "Is training suitable for beginners? Expand" [level=3] [ref=e622]:
              - button "Is training suitable for beginners? Expand" [ref=e623] [cursor=pointer]:
                - text: Is training suitable for beginners?
                - img "Expand" [ref=e624]
            - heading "How do I get cultivation support? Expand" [level=3] [ref=e627]:
              - button "How do I get cultivation support? Expand" [ref=e628] [cursor=pointer]:
                - text: How do I get cultivation support?
                - img "Expand" [ref=e629]
          - button "View all FAQs All" [ref=e632] [cursor=pointer]:
            - generic [ref=e633]: View all FAQs
            - img "All" [ref=e635]
      - region "Join the SporeKart community" [ref=e638]:
        - generic [ref=e640]:
          - generic [ref=e641]:
            - heading "Join the SporeKart community" [level=2] [ref=e642]
            - paragraph [ref=e643]: Get cultivation tips, new-strain drops, and grower event invites — straight to your inbox.
            - generic [ref=e644]:
              - generic [ref=e645]: Email address
              - textbox "Email address" [ref=e646]:
                - /placeholder: you@farm.example
              - button "Subscribe Subscribe" [ref=e647] [cursor=pointer]:
                - generic [ref=e648]: Subscribe
                - img "Subscribe" [ref=e650]
          - list [ref=e652]:
            - listitem [ref=e653]:
              - img "Cultivation guides & playbooks" [ref=e654]
              - text: Cultivation guides & playbooks
            - listitem [ref=e657]:
              - img "Early access to new strains" [ref=e658]
              - text: Early access to new strains
            - listitem [ref=e660]:
              - img "Invites to grower community events" [ref=e661]
              - text: Invites to grower community events
    - contentinfo [ref=e666]:
      - generic [ref=e667]:
        - generic [ref=e668]:
          - generic [ref=e669]:
            - generic [ref=e670]: ❖
            - text: SporeKart
          - paragraph [ref=e671]: Mushroom cultivation, simplified.
          - generic [ref=e672]:
            - link "Email" [ref=e673] [cursor=pointer]:
              - /url: https://example.com
              - img "Email" [ref=e674]
            - link "Phone" [ref=e677] [cursor=pointer]:
              - /url: https://example.com
              - img "Phone" [ref=e678]
            - link "Location" [ref=e680] [cursor=pointer]:
              - /url: https://example.com
              - img "Location" [ref=e681]
        - navigation "Explore" [ref=e684]:
          - heading "Explore" [level=2] [ref=e685]
          - link "Products" [ref=e686] [cursor=pointer]:
            - /url: /products
          - link "Training" [ref=e687] [cursor=pointer]:
            - /url: /training
          - link "Blog" [ref=e688] [cursor=pointer]:
            - /url: /blog
          - link "About" [ref=e689] [cursor=pointer]:
            - /url: /about
          - link "FAQ" [ref=e690] [cursor=pointer]:
            - /url: /faq
          - link "Certifications" [ref=e691] [cursor=pointer]:
            - /url: /certifications
        - navigation "Company" [ref=e692]:
          - heading "Company" [level=2] [ref=e693]
          - link "Contact" [ref=e694] [cursor=pointer]:
            - /url: /contact
          - link "Privacy Policy" [ref=e695] [cursor=pointer]:
            - /url: /privacy-policy
          - link "Terms & Conditions" [ref=e696] [cursor=pointer]:
            - /url: /terms-and-conditions
        - navigation "Policies" [ref=e697]:
          - heading "Policies" [level=2] [ref=e698]
          - link "Refund Policy" [ref=e699] [cursor=pointer]:
            - /url: /refund-policy
          - link "Shipping Policy" [ref=e700] [cursor=pointer]:
            - /url: /shipping-policy
          - link "Sign In" [ref=e701] [cursor=pointer]:
            - /url: /auth
      - generic [ref=e702]:
        - generic [ref=e703]: © 2026 SporeKart. All rights reserved.
        - generic [ref=e704]:
          - img "Secure" [ref=e705]
          - text: Secure & compliant
      - generic [ref=e707]: Design & Developed by Pravara Media
```

# Test source

```ts
  118 |   // SECTION 3: Storage & Data Persistence Validation
  119 |   // ==========================================================================
  120 | 
  121 |   test('localStorage inspection — no auth secrets stored', async ({ page }) => {
  122 |     await page.goto('/');
  123 |     await page.waitForLoadState('networkidle');
  124 | 
  125 |     const storage = await page.evaluate(() => {
  126 |       const keys: string[] = [];
  127 |       for (let i = 0; i < localStorage.length; i++) {
  128 |         keys.push(localStorage.key(i)!);
  129 |       }
  130 |       return keys;
  131 |     });
  132 | 
  133 |     const secretsFound = storage.filter(k =>
  134 |       k.toLowerCase().includes('token') ||
  135 |       k.toLowerCase().includes('auth') ||
  136 |       k.toLowerCase().includes('session') ||
  137 |       k.toLowerCase().includes('password') ||
  138 |       k.toLowerCase().includes('secret') ||
  139 |       k.toLowerCase().includes('credential') ||
  140 |       k.toLowerCase().includes('jwt') ||
  141 |       k.toLowerCase().includes('key')
  142 |     );
  143 | 
  144 |     expect(secretsFound).toEqual([]);
  145 |   });
  146 | 
  147 |   test('sessionStorage inspection — no auth secrets stored', async ({ page }) => {
  148 |     await page.goto('/');
  149 |     await page.waitForLoadState('networkidle');
  150 | 
  151 |     const storage = await page.evaluate(() => {
  152 |       const keys: string[] = [];
  153 |       for (let i = 0; i < sessionStorage.length; i++) {
  154 |         keys.push(sessionStorage.key(i)!);
  155 |       }
  156 |       return keys;
  157 |     });
  158 | 
  159 |     const secretsFound = storage.filter(k =>
  160 |       k.toLowerCase().includes('token') ||
  161 |       k.toLowerCase().includes('auth') ||
  162 |       k.toLowerCase().includes('password') ||
  163 |       k.toLowerCase().includes('secret') ||
  164 |       k.toLowerCase().includes('credential') ||
  165 |       k.toLowerCase().includes('jwt') ||
  166 |       k.toLowerCase().includes('key')
  167 |     );
  168 | 
  169 |     expect(secretsFound).toEqual([]);
  170 |   });
  171 | 
  172 |   test('localStorage values contain no plain-text credentials', async ({ page }) => {
  173 |     await page.goto('/');
  174 |     await page.waitForLoadState('networkidle');
  175 | 
  176 |     const hasSecrets = await page.evaluate(() => {
  177 |       for (let i = 0; i < localStorage.length; i++) {
  178 |         const key = localStorage.key(i)!;
  179 |         const val = localStorage.getItem(key);
  180 |         if (!val) continue;
  181 |         try {
  182 |           const parsed = JSON.parse(val);
  183 |           const str = JSON.stringify(parsed).toLowerCase();
  184 |           if (str.includes('password') || str.includes('token') || str.includes('secret')) {
  185 |             return { key, val: str.substring(0, 200) };
  186 |           }
  187 |         } catch {
  188 |           if (val.toLowerCase().includes('password') || val.toLowerCase().includes('token')) {
  189 |             return { key, val: val.substring(0, 200) };
  190 |           }
  191 |         }
  192 |       }
  193 |       return null;
  194 |     });
  195 | 
  196 |     expect(hasSecrets).toBeNull();
  197 |   });
  198 | 
  199 |   test('Cookies — no authentication cookies set by the app', async ({ page }) => {
  200 |     await page.goto('/login');
  201 |     await page.waitForLoadState('networkidle');
  202 | 
  203 |     const cookies = await page.context().cookies();
  204 |     const authCookies = cookies.filter(c =>
  205 |       c.name.toLowerCase().includes('auth') ||
  206 |       c.name.toLowerCase().includes('session') ||
  207 |       c.name.toLowerCase().includes('token') ||
  208 |       c.name.toLowerCase().includes('sid')
  209 |     );
  210 |     expect(authCookies).toEqual([]);
  211 |   });
  212 | 
  213 |   test('Theme preference persists in localStorage across pages', async ({ page }) => {
  214 |     await page.goto('/');
  215 |     await page.waitForLoadState('networkidle');
  216 | 
  217 |     const theme = await page.evaluate(() => localStorage.getItem('sporekart-theme'));
> 218 |     expect(typeof theme).toBe('string');
      |                          ^ Error: expect(received).toBe(expected) // Object.is equality
  219 |   });
  220 | 
  221 |   // ==========================================================================
  222 |   // SECTION 4: Navigation & Deep Link Validation
  223 |   // ==========================================================================
  224 | 
  225 |   test('Direct URL to session-expired renders page correctly', async ({ page }) => {
  226 |     await page.goto('/session-expired');
  227 |     await page.waitForLoadState('networkidle');
  228 |     await expect(page).toHaveURL(/\/session-expired/);
  229 |     await expect(page.locator('text=Your session expired')).toBeVisible();
  230 |   });
  231 | 
  232 |   test('Direct URL to access-denied renders page correctly', async ({ page }) => {
  233 |     await page.goto('/access-denied');
  234 |     await page.waitForLoadState('networkidle');
  235 |     await expect(page).toHaveURL(/\/access-denied/);
  236 |     await expect(page.locator('text=Access denied')).toBeVisible();
  237 |   });
  238 | 
  239 |   test('Back button from session-expired to login works', async ({ page }) => {
  240 |     await page.goto('/session-expired');
  241 |     await page.waitForLoadState('networkidle');
  242 |     await page.locator('button:has-text("Sign in again")').click();
  243 |     await page.waitForURL('**/login');
  244 |     expect(page.url()).toContain('/login');
  245 |   });
  246 | 
  247 |   test('Bookmark access to auth pages renders correctly', async ({ page }) => {
  248 |     const pages = [
  249 |       { path: '/login', expectPath: '/login' },
  250 |       { path: '/register', expectPath: '/register' },
  251 |       { path: '/forgot-password', expectPath: '/forgot-password' },
  252 |       { path: '/verify-otp', expectPath: '/verify-otp' },
  253 |       { path: '/session-expired', expectPath: '/session-expired' },
  254 |       { path: '/access-denied', expectPath: '/access-denied' },
  255 |       { path: '/auth/loading', expectPath: '/' },
  256 |     ];
  257 |     for (const { path, expectPath } of pages) {
  258 |       await page.goto(path);
  259 |       await page.waitForLoadState('networkidle');
  260 |       const url = page.url();
  261 |       expect(url.endsWith(expectPath) || url.includes(expectPath)).toBeTruthy();
  262 |       const bodyText = await page.locator('body').innerText();
  263 |       expect(bodyText.length).toBeGreaterThan(0);
  264 |     }
  265 |   });
  266 | 
  267 |   // ==========================================================================
  268 |   // SECTION 5: Role-Specific Navigation Validation
  269 |   // ==========================================================================
  270 | 
  271 |   test('Navigation shows different workspaces based on role', async ({ page }) => {
  272 |     // Visit home page as default
  273 |     await page.goto('/');
  274 |     await page.waitForLoadState('networkidle');
  275 |     const bodyText = await page.locator('body').innerText();
  276 |     expect(bodyText.length).toBeGreaterThan(0);
  277 |   });
  278 | 
  279 |   test('Admin dashboard route is accessible', async ({ page }) => {
  280 |     await page.goto('/admin/dashboard');
  281 |     await page.waitForLoadState('networkidle');
  282 |     expect(page.url()).toContain('/admin/dashboard');
  283 |     const bodyText = await page.locator('body').innerText();
  284 |     expect(bodyText.length).toBeGreaterThan(0);
  285 |   });
  286 | 
  287 |   test('Customer dashboard route is accessible', async ({ page }) => {
  288 |     await page.goto('/dashboard');
  289 |     await page.waitForLoadState('networkidle');
  290 |     await expect(page).toHaveURL(/\/dashboard/);
  291 |   });
  292 | 
  293 |   // ==========================================================================
  294 |   // SECTION 6: Session Error & Recovery UI
  295 |   // ==========================================================================
  296 | 
  297 |   test('LoggedOutPage component renders (exists but not routed)', async ({ page }) => {
  298 |     // The LoggedOutPage is not routed; verify 404 or redirect behavior
  299 |     const response = await page.goto('/logged-out');
  300 |     await page.waitForLoadState('networkidle');
  301 |     // Expect either a 404 or redirect to login/home
  302 |     const url = page.url();
  303 |     expect(url).not.toBeNull();
  304 |   });
  305 | 
  306 |   test('Auth error pages render correct status colors and actions', async ({ page }) => {
  307 |     await page.goto('/auth-error');
  308 |     await page.waitForLoadState('networkidle');
  309 |     await expect(page.locator('h1:has-text("401")')).toBeVisible();
  310 |     await expect(page.locator('button:has-text("Sign in")')).toBeVisible();
  311 |     await expect(page.locator('button:has-text("Back to home")')).toBeVisible();
  312 |     await expect(page.locator('button:has-text("Try again")')).toBeVisible();
  313 |     await expect(page.locator('button:has-text("Retry")')).toBeVisible();
  314 |   });
  315 | 
  316 |   test('Concurrent navigation to session pages shows consistent state', async ({ page }) => {
  317 |     const pages = ['/session-expired', '/access-denied', '/auth/loading'];
  318 |     for (const p of pages) {
```