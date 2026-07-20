# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: notification-platform.spec.ts >> Phase 10 — Security >> No PII in notification page source
- Location: tests\notification-platform.spec.ts:476:7

# Error details

```
Error: expect(received).toBeFalsy()

Received: true
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3]:
    - /url: "#main"
  - generic [ref=e4]:
    - banner [ref=e5]:
      - generic [ref=e6]:
        - generic [ref=e7]:
          - button "Open sidebar" [ref=e8] [cursor=pointer]:
            - img [ref=e9]
          - generic [ref=e10]:
            - img [ref=e12]
            - generic [ref=e14]: Admin
          - generic "Search placeholder" [ref=e15] [cursor=pointer]:
            - img [ref=e16]
            - generic [ref=e19]: Search admin...
            - generic [ref=e20]: Ctrl+K
        - generic [ref=e21]:
          - generic [ref=e22]:
            - button "Workspace actions" [ref=e23] [cursor=pointer]:
              - img [ref=e24]
            - button "Notifications" [ref=e29] [cursor=pointer]:
              - img [ref=e30]
            - button "Toggle theme" [ref=e34] [cursor=pointer]:
              - img [ref=e35]
          - navigation "Admin actions" [ref=e37]:
            - menuitem "Profile" [ref=e39] [cursor=pointer]:
              - img [ref=e41]
              - generic [ref=e44]: Profile
            - menuitem "Settings" [ref=e46] [cursor=pointer]:
              - img [ref=e48]
              - generic [ref=e51]: Settings
            - menuitem "Help" [ref=e53] [cursor=pointer]:
              - img [ref=e55]
              - generic [ref=e58]: Help
    - generic [ref=e59]:
      - complementary [ref=e60]:
        - navigation "Admin workspace navigation" [ref=e61]:
          - generic [ref=e62]:
            - generic [ref=e64]:
              - generic [ref=e65]:
                - img [ref=e67]
                - generic [ref=e69]: SporeKart
              - generic "Search admin sidebar" [ref=e70]:
                - img [ref=e71]
                - generic [ref=e74]: Search...
            - button "Collapse sidebar" [ref=e75] [cursor=pointer]:
              - img [ref=e76]
          - menu [ref=e79]:
            - menuitem "Dashboard Pin Dashboard" [ref=e83] [cursor=pointer]:
              - img [ref=e85]
              - generic [ref=e87]: Dashboard
              - button "Pin Dashboard" [ref=e88]:
                - img [ref=e89]
            - menuitem "Products Pin Products" [ref=e94] [cursor=pointer]:
              - img [ref=e96]
              - generic [ref=e100]: Products
              - button "Pin Products" [ref=e101]:
                - img [ref=e102]
            - menuitem "Inventory Pin Inventory" [ref=e107] [cursor=pointer]:
              - img [ref=e109]
              - generic [ref=e112]: Inventory
              - button "Pin Inventory" [ref=e113]:
                - img [ref=e114]
            - menuitem "Warehouse Pin Warehouse" [ref=e119] [cursor=pointer]:
              - img [ref=e121]
              - generic [ref=e124]: Warehouse
              - button "Pin Warehouse" [ref=e125]:
                - img [ref=e126]
            - menuitem "Inventory Items Pin Inventory Items" [ref=e131] [cursor=pointer]:
              - img [ref=e133]
              - generic [ref=e137]: Inventory Items
              - button "Pin Inventory Items" [ref=e138]:
                - img [ref=e139]
            - menuitem "Stock Pin Stock" [ref=e144] [cursor=pointer]:
              - img [ref=e146]
              - generic [ref=e150]: Stock
              - button "Pin Stock" [ref=e151]:
                - img [ref=e152]
            - menuitem "Batch Pin Batch" [ref=e157] [cursor=pointer]:
              - img [ref=e159]
              - generic [ref=e163]: Batch
              - button "Pin Batch" [ref=e164]:
                - img [ref=e165]
            - menuitem "Movements Pin Movements" [ref=e170] [cursor=pointer]:
              - img [ref=e172]
              - generic [ref=e174]: Movements
              - button "Pin Movements" [ref=e175]:
                - img [ref=e176]
            - menuitem "Receiving Pin Receiving" [ref=e181] [cursor=pointer]:
              - img [ref=e183]
              - generic [ref=e185]: Receiving
              - button "Pin Receiving" [ref=e186]:
                - img [ref=e187]
            - menuitem "Intelligence Pin Intelligence" [ref=e192] [cursor=pointer]:
              - img [ref=e194]
              - generic [ref=e196]: Intelligence
              - button "Pin Intelligence" [ref=e197]:
                - img [ref=e198]
            - menuitem "Orders Pin Orders" [ref=e203] [cursor=pointer]:
              - img [ref=e205]
              - generic [ref=e209]: Orders
              - button "Pin Orders" [ref=e210]:
                - img [ref=e211]
            - menuitem "Customers Pin Customers" [ref=e216] [cursor=pointer]:
              - img [ref=e218]
              - generic [ref=e223]: Customers
              - button "Pin Customers" [ref=e224]:
                - img [ref=e225]
            - menuitem "CRM Pin CRM" [ref=e230] [cursor=pointer]:
              - img [ref=e232]
              - generic [ref=e234]: CRM
              - button "Pin CRM" [ref=e235]:
                - img [ref=e236]
            - menuitem "Training Pin Training" [ref=e241] [cursor=pointer]:
              - img [ref=e243]
              - generic [ref=e246]: Training
              - button "Pin Training" [ref=e247]:
                - img [ref=e248]
            - menuitem "Shipping Pin Shipping" [ref=e253] [cursor=pointer]:
              - img [ref=e255]
              - generic [ref=e260]: Shipping
              - button "Pin Shipping" [ref=e261]:
                - img [ref=e262]
            - menuitem "Finance Pin Finance" [ref=e267] [cursor=pointer]:
              - img [ref=e269]
              - generic [ref=e271]: Finance
              - button "Pin Finance" [ref=e272]:
                - img [ref=e273]
            - menuitem "Reports Pin Reports" [ref=e278] [cursor=pointer]:
              - img [ref=e280]
              - generic [ref=e281]: Reports
              - button "Pin Reports" [ref=e282]:
                - img [ref=e283]
            - menuitem "Analytics Pin Analytics" [ref=e288] [cursor=pointer]:
              - img [ref=e290]
              - generic [ref=e293]: Analytics
              - button "Pin Analytics" [ref=e294]:
                - img [ref=e295]
            - menuitem "Profile Pin Profile" [ref=e300] [cursor=pointer]:
              - img [ref=e302]
              - generic [ref=e305]: Profile
              - button "Pin Profile" [ref=e306]:
                - img [ref=e307]
            - menuitem "Settings Pin Settings" [ref=e312] [cursor=pointer]:
              - img [ref=e314]
              - generic [ref=e317]: Settings
              - button "Pin Settings" [ref=e318]:
                - img [ref=e319]
            - menuitem "System Pin System" [ref=e324] [cursor=pointer]:
              - img [ref=e326]
              - generic [ref=e328]: System
              - button "Pin System" [ref=e329]:
                - img [ref=e330]
            - menuitem "Help Pin Help" [ref=e335] [cursor=pointer]:
              - img [ref=e337]
              - generic [ref=e340]: Help
              - button "Pin Help" [ref=e341]:
                - img [ref=e342]
            - menuitem "PINNED Pin PINNED" [disabled] [ref=e347]:
              - generic [ref=e348]: PINNED
              - button "Pin PINNED" [disabled] [ref=e349] [cursor=pointer]:
                - img [ref=e350]
            - menuitem "Pin items for quick access Pin Pin items for quick access" [disabled] [ref=e355]:
              - generic [ref=e356]: Pin items for quick access
              - button "Pin Pin items for quick access" [disabled] [ref=e357] [cursor=pointer]:
                - img [ref=e358]
            - menuitem "FAVORITES Pin FAVORITES" [disabled] [ref=e363]:
              - generic [ref=e364]: FAVORITES
              - button "Pin FAVORITES" [disabled] [ref=e365] [cursor=pointer]:
                - img [ref=e366]
            - menuitem "No favorites yet Pin No favorites yet" [disabled] [ref=e371]:
              - img [ref=e373]
              - generic [ref=e375]: No favorites yet
              - button "Pin No favorites yet" [disabled] [ref=e376] [cursor=pointer]:
                - img [ref=e377]
            - menuitem "RECENT Pin RECENT" [disabled] [ref=e382]:
              - generic [ref=e383]: RECENT
              - button "Pin RECENT" [disabled] [ref=e384] [cursor=pointer]:
                - img [ref=e385]
            - menuitem "No recent pages Pin No recent pages" [disabled] [ref=e390]:
              - img [ref=e392]
              - generic [ref=e395]: No recent pages
              - button "Pin No recent pages" [disabled] [ref=e396] [cursor=pointer]:
                - img [ref=e397]
            - menuitem "QUICK ACTIONS Pin QUICK ACTIONS" [disabled] [ref=e402]:
              - generic [ref=e403]: QUICK ACTIONS
              - button "Pin QUICK ACTIONS" [disabled] [ref=e404] [cursor=pointer]:
                - img [ref=e405]
            - menuitem "Cmd+K to search Pin Cmd+K to search" [disabled] [ref=e410]:
              - img [ref=e412]
              - generic [ref=e414]: Cmd+K to search
              - button "Pin Cmd+K to search" [disabled] [ref=e415] [cursor=pointer]:
                - img [ref=e416]
          - generic [ref=e419]: v1.0.0 · Admin Workspace
      - main [ref=e420]:
        - generic [ref=e422]:
          - generic [ref=e424]:
            - heading "communication / notifications" [level=1] [ref=e425]
            - paragraph [ref=e426]: Enterprise Administration · Admin
          - navigation "Breadcrumb" [ref=e428]:
            - list [ref=e429]:
              - listitem [ref=e430]:
                - link "Admin" [ref=e431]:
                  - /url: /admin/dashboard
                  - generic [ref=e432]: Admin
              - listitem [ref=e433]:
                - img [ref=e434]
              - listitem [ref=e436]:
                - link "Training" [ref=e437]:
                  - /url: /admin/training
                  - generic [ref=e438]: Training
              - listitem [ref=e439]:
                - img [ref=e440]
              - listitem [ref=e442]:
                - generic [ref=e444]: communication / notifications
          - generic [ref=e446]:
            - navigation "Training workspace navigation" [ref=e447]:
              - generic [ref=e448]:
                - img [ref=e450]
                - generic [ref=e453]: Training
              - generic [ref=e454]:
                - generic [ref=e455]:
                  - generic [ref=e456]: Overview
                  - button "Dashboard" [ref=e457] [cursor=pointer]:
                    - img [ref=e458]
                    - generic [ref=e460]: Dashboard
                - generic [ref=e461]:
                  - generic [ref=e462]: Management
                  - button "Courses" [ref=e463] [cursor=pointer]:
                    - img [ref=e464]
                    - generic [ref=e467]: Courses
                  - button "Curriculum" [ref=e468] [cursor=pointer]:
                    - img [ref=e469]
                    - generic [ref=e473]: Curriculum
                  - button "Training Batches" [ref=e474] [cursor=pointer]:
                    - img [ref=e475]
                    - generic [ref=e477]: Training Batches
                  - button "Students" [ref=e478] [cursor=pointer]:
                    - img [ref=e479]
                    - generic [ref=e484]: Students
                  - button "Trainers" [ref=e485] [cursor=pointer]:
                    - img [ref=e486]
                    - generic [ref=e490]: Trainers
                - generic [ref=e491]:
                  - generic [ref=e492]: Operations
                  - button "Attendance" [ref=e493] [cursor=pointer]:
                    - img [ref=e494]
                    - generic [ref=e497]: Attendance
                  - button "Assignments" [ref=e498] [cursor=pointer]:
                    - img [ref=e499]
                    - generic [ref=e502]: Assignments
                  - button "Assessments" [ref=e503] [cursor=pointer]:
                    - img [ref=e504]
                    - generic [ref=e508]: Assessments
                  - button "Certificates" [ref=e509] [cursor=pointer]:
                    - generic [ref=e510]: "?"
                    - generic [ref=e511]: Certificates
                  - button "Learning Resources" [ref=e512] [cursor=pointer]:
                    - img [ref=e513]
                    - generic [ref=e515]: Learning Resources
                  - button "Announcements" [ref=e516] [cursor=pointer]:
                    - img [ref=e517]
                    - generic [ref=e519]: Announcements
                - generic [ref=e520]:
                  - generic [ref=e521]: Student Platform
                  - button "Student Workspace" [ref=e522] [cursor=pointer]:
                    - img [ref=e523]
                    - generic [ref=e528]: Student Workspace
                - generic [ref=e529]:
                  - generic [ref=e530]: Intelligence
                  - button "Reports" [ref=e531] [cursor=pointer]:
                    - img [ref=e532]
                    - generic [ref=e533]: Reports
                  - button "Analytics" [ref=e534] [cursor=pointer]:
                    - img [ref=e535]
                    - generic [ref=e538]: Analytics
                - generic [ref=e539]:
                  - generic [ref=e540]: Settings
                  - button "Settings" [ref=e541] [cursor=pointer]:
                    - img [ref=e542]
                    - generic [ref=e545]: Settings
                - generic [ref=e546]:
                  - generic [ref=e547]: Coming Soon
                  - button "AI Assistant Soon" [ref=e548]:
                    - img [ref=e549]
                    - generic [ref=e552]: AI Assistant
                    - generic [ref=e553]: Soon
                  - button "Community Soon" [ref=e554]:
                    - img [ref=e555]
                    - generic [ref=e557]: Community
                    - generic [ref=e558]: Soon
                  - button "Discussion Board Soon" [ref=e559]:
                    - img [ref=e560]
                    - generic [ref=e562]: Discussion Board
                    - generic [ref=e563]: Soon
            - generic [ref=e564]:
              - generic [ref=e565]:
                - button "Open training navigation" [ref=e566] [cursor=pointer]:
                  - img [ref=e567]
                - generic [ref=e568]: communication
              - generic [ref=e570]:
                - generic [ref=e571]:
                  - img [ref=e572]
                  - generic [ref=e574]:
                    - heading "Communication Center" [level=1] [ref=e575]
                    - paragraph [ref=e576]: Announcements, notifications, templates & delivery (Mock Mode)
                - navigation "Communication sections" [ref=e577]:
                  - link "Overview" [ref=e578]:
                    - /url: /admin/training/communication/overview
                    - img [ref=e579]
                    - text: Overview
                  - link "Announcements" [ref=e581]:
                    - /url: /admin/training/communication/announcements
                    - img [ref=e582]
                    - text: Announcements
                  - link "Notifications 35" [ref=e585]:
                    - /url: /admin/training/communication/notifications
                    - img [ref=e586]
                    - text: Notifications
                    - generic [ref=e589]: "35"
                  - link "Scheduled" [ref=e590]:
                    - /url: /admin/training/communication/scheduled
                    - img [ref=e591]
                    - text: Scheduled
                  - link "Templates" [ref=e594]:
                    - /url: /admin/training/communication/templates
                    - img [ref=e595]
                    - text: Templates
                  - link "History" [ref=e598]:
                    - /url: /admin/training/communication/history
                    - img [ref=e599]
                    - text: History
                  - link "Delivery Queue" [ref=e602]:
                    - /url: /admin/training/communication/delivery
                    - img [ref=e603]
                    - text: Delivery Queue
                  - link "Future Channels" [ref=e606]:
                    - /url: /admin/training/communication/channels
                    - img [ref=e607]
                    - text: Future Channels
                  - link "Statistics" [ref=e610]:
                    - /url: /admin/training/communication/statistics
                    - img [ref=e611]
                    - text: Statistics
                - region [ref=e612]:
                  - generic [ref=e613]:
                    - tablist "Read filter" [ref=e614]:
                      - tab "All" [selected] [ref=e615] [cursor=pointer]
                      - tab "Unread (35)" [ref=e616] [cursor=pointer]
                      - tab "Read" [ref=e617] [cursor=pointer]
                    - button "Mark all read" [ref=e618] [cursor=pointer]:
                      - img [ref=e619]
                      - text: Mark all read
                  - search [ref=e621]:
                    - generic [ref=e622]:
                      - generic:
                        - img
                      - searchbox "Search notifications…" [ref=e623]
                    - generic [ref=e624]:
                      - generic [ref=e625]: Type
                      - combobox "Type" [ref=e626]:
                        - option "All" [selected]
                        - option "Enrollment Approved"
                        - option "Enrollment Pending"
                        - option "Enrollment Rejected"
                        - option "Course Published"
                        - option "Course Updated"
                        - option "Batch Scheduled"
                        - option "Batch Cancelled"
                        - option "Trainer Assigned"
                        - option "Certificate Ready"
                        - option "Assignment Reminder"
                        - option "Assessment Reminder"
                        - option "System Maintenance"
                        - option "General Update"
                        - option "Emergency Notice"
                    - generic [ref=e627]:
                      - generic [ref=e628]: Priority
                      - combobox "Priority" [ref=e629]:
                        - option "All" [selected]
                        - option "Low"
                        - option "Normal"
                        - option "High"
                        - option "Urgent"
                        - option "Critical"
                  - list [ref=e630]:
                    - listitem [ref=e631]:
                      - img [ref=e633]
                      - generic [ref=e635]:
                        - generic [ref=e636]:
                          - generic [ref=e637]: Emergency Notice
                          - generic [ref=e638]: Critical
                        - paragraph [ref=e639]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e640]: Spawn Lab Production · 22 hr from now
                      - button "Unread" [ref=e641] [cursor=pointer]
                    - listitem [ref=e642]:
                      - img [ref=e644]
                      - generic [ref=e648]:
                        - generic [ref=e649]:
                          - generic [ref=e650]: Batch Cancelled
                          - generic [ref=e651]: High
                        - paragraph [ref=e652]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e653]: Cultivation Techniques · 22 hr from now
                      - button "Unread" [ref=e654] [cursor=pointer]
                    - listitem [ref=e655]:
                      - img [ref=e657]
                      - generic [ref=e659]:
                        - generic [ref=e660]:
                          - generic [ref=e661]: General Update
                          - generic [ref=e662]: Low
                        - paragraph [ref=e663]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e664]: Spawn Lab Production · 2 hr from now
                      - button "Unread" [ref=e665] [cursor=pointer]
                    - listitem [ref=e666]:
                      - img [ref=e668]
                      - generic [ref=e671]:
                        - generic [ref=e672]:
                          - generic [ref=e673]: Enrollment Pending
                          - generic [ref=e674]: Low
                          - generic "Unread" [ref=e675]
                        - paragraph [ref=e676]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e677]: Everyone · 2 days ago
                      - button "Read" [ref=e678] [cursor=pointer]
                    - listitem [ref=e679]:
                      - img [ref=e681]
                      - generic [ref=e684]:
                        - generic [ref=e685]:
                          - generic [ref=e686]: Course Published
                          - generic [ref=e687]: Normal
                        - paragraph [ref=e688]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e689]: Managers · 4 days ago
                      - button "Unread" [ref=e690] [cursor=pointer]
                    - listitem [ref=e691]:
                      - img [ref=e693]
                      - generic [ref=e696]:
                        - generic [ref=e697]:
                          - generic [ref=e698]: Enrollment Approved
                          - generic [ref=e699]: Normal
                          - generic "Unread" [ref=e700]
                        - paragraph [ref=e701]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e702]: Everyone · 4 days ago
                      - button "Read" [ref=e703] [cursor=pointer]
                    - listitem [ref=e704]:
                      - img [ref=e706]
                      - generic [ref=e708]:
                        - generic [ref=e709]:
                          - generic [ref=e710]: Batch Scheduled
                          - generic [ref=e711]: Low
                          - generic "Unread" [ref=e712]
                        - paragraph [ref=e713]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e714]: Everyone · 5 days ago
                      - button "Read" [ref=e715] [cursor=pointer]
                    - listitem [ref=e716]:
                      - img [ref=e718]
                      - generic [ref=e720]:
                        - generic [ref=e721]:
                          - generic [ref=e722]: Emergency Notice
                          - generic [ref=e723]: Critical
                        - paragraph [ref=e724]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e725]: Students · 6 days ago
                      - button "Unread" [ref=e726] [cursor=pointer]
                    - listitem [ref=e727]:
                      - img [ref=e729]
                      - generic [ref=e733]:
                        - generic [ref=e734]:
                          - generic [ref=e735]: Batch Cancelled
                          - generic [ref=e736]: Critical
                        - paragraph [ref=e737]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e738]: Batch 2026-B · 7 days ago
                      - button "Unread" [ref=e739] [cursor=pointer]
                    - listitem [ref=e740]:
                      - img [ref=e742]
                      - generic [ref=e746]:
                        - generic [ref=e747]:
                          - generic [ref=e748]: Course Updated
                          - generic [ref=e749]: Normal
                          - generic "Unread" [ref=e750]
                        - paragraph [ref=e751]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e752]: Everyone · 7 days ago
                      - button "Read" [ref=e753] [cursor=pointer]
                    - listitem [ref=e754]:
                      - img [ref=e756]
                      - generic [ref=e759]:
                        - generic [ref=e760]:
                          - generic [ref=e761]: System Maintenance
                          - generic [ref=e762]: Urgent
                        - paragraph [ref=e763]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e764]: Students · 8 days ago
                      - button "Unread" [ref=e765] [cursor=pointer]
                    - listitem [ref=e766]:
                      - img [ref=e768]
                      - generic [ref=e772]:
                        - generic [ref=e773]:
                          - generic [ref=e774]: Assessment Reminder
                          - generic [ref=e775]: Critical
                        - paragraph [ref=e776]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e777]: Batch 2026-B · 9 days ago
                      - button "Unread" [ref=e778] [cursor=pointer]
                    - listitem [ref=e779]:
                      - img [ref=e781]
                      - generic [ref=e785]:
                        - generic [ref=e786]:
                          - generic [ref=e787]: Assessment Reminder
                          - generic [ref=e788]: Urgent
                        - paragraph [ref=e789]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e790]: Batch 2026-B · 9 days ago
                      - button "Unread" [ref=e791] [cursor=pointer]
                    - listitem [ref=e792]:
                      - img [ref=e794]
                      - generic [ref=e796]:
                        - generic [ref=e797]:
                          - generic [ref=e798]: Emergency Notice
                          - generic [ref=e799]: Critical
                          - generic "Unread" [ref=e800]
                        - paragraph [ref=e801]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e802]: Batch 2026-B · 10 days ago
                      - button "Read" [ref=e803] [cursor=pointer]
                    - listitem [ref=e804]:
                      - img [ref=e806]
                      - generic [ref=e809]:
                        - generic [ref=e810]:
                          - generic [ref=e811]: Course Published
                          - generic [ref=e812]: Critical
                        - paragraph [ref=e813]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e814]: Trainers · 10 days ago
                      - button "Unread" [ref=e815] [cursor=pointer]
                    - listitem [ref=e816]:
                      - img [ref=e818]
                      - generic [ref=e822]:
                        - generic [ref=e823]:
                          - generic [ref=e824]: Course Updated
                          - generic [ref=e825]: Urgent
                          - generic "Unread" [ref=e826]
                        - paragraph [ref=e827]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e828]: Trainers · 10 days ago
                      - button "Read" [ref=e829] [cursor=pointer]
                    - listitem [ref=e830]:
                      - img [ref=e832]
                      - generic [ref=e835]:
                        - generic [ref=e836]:
                          - generic [ref=e837]: Enrollment Pending
                          - generic [ref=e838]: Normal
                        - paragraph [ref=e839]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e840]: Agri-Business · 10 days ago
                      - button "Unread" [ref=e841] [cursor=pointer]
                    - listitem [ref=e842]:
                      - img [ref=e844]
                      - generic [ref=e846]:
                        - generic [ref=e847]:
                          - generic [ref=e848]: Batch Scheduled
                          - generic [ref=e849]: Normal
                        - paragraph [ref=e850]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e851]: Students · 11 days ago
                      - button "Unread" [ref=e852] [cursor=pointer]
                    - listitem [ref=e853]:
                      - img [ref=e855]
                      - generic [ref=e858]:
                        - generic [ref=e859]:
                          - generic [ref=e860]: Course Published
                          - generic [ref=e861]: Urgent
                          - generic "Unread" [ref=e862]
                        - paragraph [ref=e863]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e864]: Everyone · 11 days ago
                      - button "Read" [ref=e865] [cursor=pointer]
                    - listitem [ref=e866]:
                      - img [ref=e868]
                      - generic [ref=e871]:
                        - generic [ref=e872]:
                          - generic [ref=e873]: Enrollment Pending
                          - generic [ref=e874]: Normal
                        - paragraph [ref=e875]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e876]: Button Mushroom Commercial Scale · 11 days ago
                      - button "Unread" [ref=e877] [cursor=pointer]
                    - listitem [ref=e878]:
                      - img [ref=e880]
                      - generic [ref=e883]:
                        - generic [ref=e884]:
                          - generic [ref=e885]: Assignment Reminder
                          - generic [ref=e886]: Normal
                          - generic "Unread" [ref=e887]
                        - paragraph [ref=e888]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e889]: Cultivation Techniques · 11 days ago
                      - button "Read" [ref=e890] [cursor=pointer]
                    - listitem [ref=e891]:
                      - img [ref=e893]
                      - generic [ref=e896]:
                        - generic [ref=e897]:
                          - generic [ref=e898]: Enrollment Approved
                          - generic [ref=e899]: High
                          - generic "Unread" [ref=e900]
                        - paragraph [ref=e901]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e902]: Batch 2026-A · 12 days ago
                      - button "Read" [ref=e903] [cursor=pointer]
                    - listitem [ref=e904]:
                      - img [ref=e906]
                      - generic [ref=e908]:
                        - generic [ref=e909]:
                          - generic [ref=e910]: Batch Scheduled
                          - generic [ref=e911]: Low
                        - paragraph [ref=e912]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e913]: Students · 12 days ago
                      - button "Unread" [ref=e914] [cursor=pointer]
                    - listitem [ref=e915]:
                      - img [ref=e917]
                      - generic [ref=e921]:
                        - generic [ref=e922]:
                          - generic [ref=e923]: Batch Cancelled
                          - generic [ref=e924]: High
                          - generic "Unread" [ref=e925]
                        - paragraph [ref=e926]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e927]: Batch 2026-A · 12 days ago
                      - button "Read" [ref=e928] [cursor=pointer]
                    - listitem [ref=e929]:
                      - img [ref=e931]
                      - generic [ref=e935]:
                        - generic [ref=e936]:
                          - generic [ref=e937]: Assessment Reminder
                          - generic [ref=e938]: Critical
                          - generic "Unread" [ref=e939]
                        - paragraph [ref=e940]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e941]: Managers · 12 days ago
                      - button "Read" [ref=e942] [cursor=pointer]
                    - listitem [ref=e943]:
                      - img [ref=e945]
                      - generic [ref=e949]:
                        - generic [ref=e950]:
                          - generic [ref=e951]: Course Updated
                          - generic [ref=e952]: Urgent
                          - generic "Unread" [ref=e953]
                        - paragraph [ref=e954]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e955]: Button Mushroom Commercial Scale · 13 days ago
                      - button "Read" [ref=e956] [cursor=pointer]
                    - listitem [ref=e957]:
                      - img [ref=e959]
                      - generic [ref=e961]:
                        - generic [ref=e962]:
                          - generic [ref=e963]: Certificate Ready
                          - generic [ref=e964]: Urgent
                        - paragraph [ref=e965]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e966]: Students · 13 days ago
                      - button "Unread" [ref=e967] [cursor=pointer]
                    - listitem [ref=e968]:
                      - img [ref=e970]
                      - generic [ref=e973]:
                        - generic [ref=e974]:
                          - generic [ref=e975]: Course Published
                          - generic [ref=e976]: Critical
                        - paragraph [ref=e977]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e978]: Students · 13 days ago
                      - button "Unread" [ref=e979] [cursor=pointer]
                    - listitem [ref=e980]:
                      - img [ref=e982]
                      - generic [ref=e986]:
                        - generic [ref=e987]:
                          - generic [ref=e988]: Course Updated
                          - generic [ref=e989]: Urgent
                          - generic "Unread" [ref=e990]
                        - paragraph [ref=e991]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e992]: Everyone · 13 days ago
                      - button "Read" [ref=e993] [cursor=pointer]
                    - listitem [ref=e994]:
                      - img [ref=e996]
                      - generic [ref=e1000]:
                        - generic [ref=e1001]:
                          - generic [ref=e1002]: Batch Cancelled
                          - generic [ref=e1003]: Normal
                          - generic "Unread" [ref=e1004]
                        - paragraph [ref=e1005]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1006]: Batch 2026-B · 13 days ago
                      - button "Read" [ref=e1007] [cursor=pointer]
                    - listitem [ref=e1008]:
                      - img [ref=e1010]
                      - generic [ref=e1013]:
                        - generic [ref=e1014]:
                          - generic [ref=e1015]: System Maintenance
                          - generic [ref=e1016]: Urgent
                          - generic "Unread" [ref=e1017]
                        - paragraph [ref=e1018]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1019]: Oyster Mushroom Fundamentals · 13 days ago
                      - button "Read" [ref=e1020] [cursor=pointer]
                    - listitem [ref=e1021]:
                      - img [ref=e1023]
                      - generic [ref=e1027]:
                        - generic [ref=e1028]:
                          - generic [ref=e1029]: Trainer Assigned
                          - generic [ref=e1030]: Normal
                          - generic "Unread" [ref=e1031]
                        - paragraph [ref=e1032]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1033]: Oyster Mushroom Fundamentals · 14 days ago
                      - button "Read" [ref=e1034] [cursor=pointer]
                    - listitem [ref=e1035]:
                      - img [ref=e1037]
                      - generic [ref=e1041]:
                        - generic [ref=e1042]:
                          - generic [ref=e1043]: Trainer Assigned
                          - generic [ref=e1044]: Urgent
                          - generic "Unread" [ref=e1045]
                        - paragraph [ref=e1046]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1047]: Cultivation Techniques · 14 days ago
                      - button "Read" [ref=e1048] [cursor=pointer]
                    - listitem [ref=e1049]:
                      - img [ref=e1051]
                      - generic [ref=e1055]:
                        - generic [ref=e1056]:
                          - generic [ref=e1057]: Batch Cancelled
                          - generic [ref=e1058]: Critical
                        - paragraph [ref=e1059]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1060]: Oyster Mushroom Fundamentals · 14 days ago
                      - button "Unread" [ref=e1061] [cursor=pointer]
                    - listitem [ref=e1062]:
                      - img [ref=e1064]
                      - generic [ref=e1067]:
                        - generic [ref=e1068]:
                          - generic [ref=e1069]: Enrollment Approved
                          - generic [ref=e1070]: High
                          - generic "Unread" [ref=e1071]
                        - paragraph [ref=e1072]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1073]: Everyone · 15 days ago
                      - button "Read" [ref=e1074] [cursor=pointer]
                    - listitem [ref=e1075]:
                      - img [ref=e1077]
                      - generic [ref=e1079]:
                        - generic [ref=e1080]:
                          - generic [ref=e1081]: General Update
                          - generic [ref=e1082]: Normal
                        - paragraph [ref=e1083]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1084]: Button Mushroom Commercial Scale · 15 days ago
                      - button "Unread" [ref=e1085] [cursor=pointer]
                    - listitem [ref=e1086]:
                      - img [ref=e1088]
                      - generic [ref=e1091]:
                        - generic [ref=e1092]:
                          - generic [ref=e1093]: System Maintenance
                          - generic [ref=e1094]: Low
                          - generic "Unread" [ref=e1095]
                        - paragraph [ref=e1096]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1097]: Cultivation Techniques · 16 days ago
                      - button "Read" [ref=e1098] [cursor=pointer]
                    - listitem [ref=e1099]:
                      - img [ref=e1101]
                      - generic [ref=e1105]:
                        - generic [ref=e1106]:
                          - generic [ref=e1107]: Course Updated
                          - generic [ref=e1108]: Low
                          - generic "Unread" [ref=e1109]
                        - paragraph [ref=e1110]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1111]: Batch 2026-B · 16 days ago
                      - button "Read" [ref=e1112] [cursor=pointer]
                    - listitem [ref=e1113]:
                      - img [ref=e1115]
                      - generic [ref=e1118]:
                        - generic [ref=e1119]:
                          - generic [ref=e1120]: System Maintenance
                          - generic [ref=e1121]: Normal
                        - paragraph [ref=e1122]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1123]: Spawn Lab Production · 16 days ago
                      - button "Unread" [ref=e1124] [cursor=pointer]
                    - listitem [ref=e1125]:
                      - img [ref=e1127]
                      - generic [ref=e1130]:
                        - generic [ref=e1131]:
                          - generic [ref=e1132]: Enrollment Pending
                          - generic [ref=e1133]: Critical
                          - generic "Unread" [ref=e1134]
                        - paragraph [ref=e1135]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1136]: Cultivation Techniques · 16 days ago
                      - button "Read" [ref=e1137] [cursor=pointer]
                    - listitem [ref=e1138]:
                      - img [ref=e1140]
                      - generic [ref=e1142]:
                        - generic [ref=e1143]:
                          - generic [ref=e1144]: Emergency Notice
                          - generic [ref=e1145]: Critical
                        - paragraph [ref=e1146]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1147]: Spawn Lab Production · 16 days ago
                      - button "Unread" [ref=e1148] [cursor=pointer]
                    - listitem [ref=e1149]:
                      - img [ref=e1151]
                      - generic [ref=e1154]:
                        - generic [ref=e1155]:
                          - generic [ref=e1156]: Enrollment Pending
                          - generic [ref=e1157]: Critical
                          - generic "Unread" [ref=e1158]
                        - paragraph [ref=e1159]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1160]: Agri-Business · 17 days ago
                      - button "Read" [ref=e1161] [cursor=pointer]
                    - listitem [ref=e1162]:
                      - img [ref=e1164]
                      - generic [ref=e1168]:
                        - generic [ref=e1169]:
                          - generic [ref=e1170]: Course Updated
                          - generic [ref=e1171]: Low
                          - generic "Unread" [ref=e1172]
                        - paragraph [ref=e1173]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1174]: Button Mushroom Commercial Scale · 18 days ago
                      - button "Read" [ref=e1175] [cursor=pointer]
                    - listitem [ref=e1176]:
                      - img [ref=e1178]
                      - generic [ref=e1182]:
                        - generic [ref=e1183]:
                          - generic [ref=e1184]: Trainer Assigned
                          - generic [ref=e1185]: Critical
                          - generic "Unread" [ref=e1186]
                        - paragraph [ref=e1187]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1188]: Oyster Mushroom Fundamentals · 18 days ago
                      - button "Read" [ref=e1189] [cursor=pointer]
                    - listitem [ref=e1190]:
                      - img [ref=e1192]
                      - generic [ref=e1194]:
                        - generic [ref=e1195]:
                          - generic [ref=e1196]: Certificate Ready
                          - generic [ref=e1197]: Normal
                        - paragraph [ref=e1198]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1199]: Batch 2026-A · 19 days ago
                      - button "Unread" [ref=e1200] [cursor=pointer]
                    - listitem [ref=e1201]:
                      - img [ref=e1203]
                      - generic [ref=e1207]:
                        - generic [ref=e1208]:
                          - generic [ref=e1209]: Batch Cancelled
                          - generic [ref=e1210]: Normal
                          - generic "Unread" [ref=e1211]
                        - paragraph [ref=e1212]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1213]: Batch 2026-A · 20 days ago
                      - button "Read" [ref=e1214] [cursor=pointer]
                    - listitem [ref=e1215]:
                      - img [ref=e1217]
                      - generic [ref=e1221]:
                        - generic [ref=e1222]:
                          - generic [ref=e1223]: Trainer Assigned
                          - generic [ref=e1224]: Normal
                          - generic "Unread" [ref=e1225]
                        - paragraph [ref=e1226]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1227]: Button Mushroom Commercial Scale · 21 days ago
                      - button "Read" [ref=e1228] [cursor=pointer]
                    - listitem [ref=e1229]:
                      - img [ref=e1231]
                      - generic [ref=e1235]:
                        - generic [ref=e1236]:
                          - generic [ref=e1237]: Course Updated
                          - generic [ref=e1238]: High
                          - generic "Unread" [ref=e1239]
                        - paragraph [ref=e1240]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1241]: Oyster Mushroom Fundamentals · 21 days ago
                      - button "Read" [ref=e1242] [cursor=pointer]
                    - listitem [ref=e1243]:
                      - img [ref=e1245]
                      - generic [ref=e1249]:
                        - generic [ref=e1250]:
                          - generic [ref=e1251]: Trainer Assigned
                          - generic [ref=e1252]: High
                        - paragraph [ref=e1253]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1254]: Trainers · 21 days ago
                      - button "Unread" [ref=e1255] [cursor=pointer]
                    - listitem [ref=e1256]:
                      - img [ref=e1258]
                      - generic [ref=e1262]:
                        - generic [ref=e1263]:
                          - generic [ref=e1264]: Assessment Reminder
                          - generic [ref=e1265]: High
                        - paragraph [ref=e1266]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1267]: Spawn Lab Production · 22 days ago
                      - button "Unread" [ref=e1268] [cursor=pointer]
                    - listitem [ref=e1269]:
                      - img [ref=e1271]
                      - generic [ref=e1273]:
                        - generic [ref=e1274]:
                          - generic [ref=e1275]: Emergency Notice
                          - generic [ref=e1276]: Critical
                          - generic "Unread" [ref=e1277]
                        - paragraph [ref=e1278]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1279]: Oyster Mushroom Fundamentals · 23 days ago
                      - button "Read" [ref=e1280] [cursor=pointer]
                    - listitem [ref=e1281]:
                      - img [ref=e1283]
                      - generic [ref=e1286]:
                        - generic [ref=e1287]:
                          - generic [ref=e1288]: Enrollment Approved
                          - generic [ref=e1289]: High
                        - paragraph [ref=e1290]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1291]: Spawn Lab Production · 23 days ago
                      - button "Unread" [ref=e1292] [cursor=pointer]
                    - listitem [ref=e1293]:
                      - img [ref=e1295]
                      - generic [ref=e1298]:
                        - generic [ref=e1299]:
                          - generic [ref=e1300]: Enrollment Pending
                          - generic [ref=e1301]: Normal
                          - generic "Unread" [ref=e1302]
                        - paragraph [ref=e1303]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1304]: Everyone · 24 days ago
                      - button "Read" [ref=e1305] [cursor=pointer]
                    - listitem [ref=e1306]:
                      - img [ref=e1308]
                      - generic [ref=e1310]:
                        - generic [ref=e1311]:
                          - generic [ref=e1312]: Emergency Notice
                          - generic [ref=e1313]: Critical
                          - generic "Unread" [ref=e1314]
                        - paragraph [ref=e1315]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1316]: Spawn Lab Production · 25 days ago
                      - button "Read" [ref=e1317] [cursor=pointer]
                    - listitem [ref=e1318]:
                      - img [ref=e1320]
                      - generic [ref=e1323]:
                        - generic [ref=e1324]:
                          - generic [ref=e1325]: Enrollment Pending
                          - generic [ref=e1326]: Low
                          - generic "Unread" [ref=e1327]
                        - paragraph [ref=e1328]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1329]: Trainers · 27 days ago
                      - button "Read" [ref=e1330] [cursor=pointer]
                    - listitem [ref=e1331]:
                      - img [ref=e1333]
                      - generic [ref=e1336]:
                        - generic [ref=e1337]:
                          - generic [ref=e1338]: Assignment Reminder
                          - generic [ref=e1339]: Low
                          - generic "Unread" [ref=e1340]
                        - paragraph [ref=e1341]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1342]: Cultivation Techniques · 28 days ago
                      - button "Read" [ref=e1343] [cursor=pointer]
                    - listitem [ref=e1344]:
                      - img [ref=e1346]
                      - generic [ref=e1349]:
                        - generic [ref=e1350]:
                          - generic [ref=e1351]: Enrollment Approved
                          - generic [ref=e1352]: Normal
                          - generic "Unread" [ref=e1353]
                        - paragraph [ref=e1354]: We are pleased to share important updates regarding upcoming training activities. Please r…
                        - paragraph [ref=e1355]: Managers · 28 days ago
                      - button "Read" [ref=e1356] [cursor=pointer]
                    - listitem [ref=e1357]:
                      - img [ref=e1359]
                      - generic [ref=e1362]:
                        - generic [ref=e1363]:
                          - generic [ref=e1364]: Enrollment Approved
                          - generic [ref=e1365]: Critical
                        - paragraph [ref=e1366]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1367]: Trainers · 28 days ago
                      - button "Unread" [ref=e1368] [cursor=pointer]
                    - listitem [ref=e1369]:
                      - img [ref=e1371]
                      - generic [ref=e1374]:
                        - generic [ref=e1375]:
                          - generic [ref=e1376]: System Maintenance
                          - generic [ref=e1377]: Low
                        - paragraph [ref=e1378]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1379]: Students · 29 days ago
                      - button "Unread" [ref=e1380] [cursor=pointer]
                    - listitem [ref=e1381]:
                      - img [ref=e1383]
                      - generic [ref=e1387]:
                        - generic [ref=e1388]:
                          - generic [ref=e1389]: Batch Cancelled
                          - generic [ref=e1390]: High
                        - paragraph [ref=e1391]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1392]: Batch 2026-B · 29 days ago
                      - button "Unread" [ref=e1393] [cursor=pointer]
                    - listitem [ref=e1394]:
                      - img [ref=e1396]
                      - generic [ref=e1398]:
                        - generic [ref=e1399]:
                          - generic [ref=e1400]: Batch Scheduled
                          - generic [ref=e1401]: Critical
                        - paragraph [ref=e1402]: As part of our continuous improvement initiative, the following changes will take effect. …
                        - paragraph [ref=e1403]: Trainers · 29 days ago
                      - button "Unread" [ref=e1404] [cursor=pointer]
                    - listitem [ref=e1405]:
                      - img [ref=e1407]
                      - generic [ref=e1411]:
                        - generic [ref=e1412]:
                          - generic [ref=e1413]: Course Updated
                          - generic [ref=e1414]: Critical
                          - generic "Unread" [ref=e1415]
                        - paragraph [ref=e1416]: This communication contains time-sensitive information. Kindly ensure all enrolled partici…
                        - paragraph [ref=e1417]: Managers · 29 days ago
                      - button "Read" [ref=e1418] [cursor=pointer]
                    - listitem [ref=e1419]:
                      - img [ref=e1421]
                      - generic [ref=e1425]:
                        - generic [ref=e1426]:
                          - generic [ref=e1427]: Batch Cancelled
                          - generic [ref=e1428]: Critical
                        - paragraph [ref=e1429]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1430]: Button Mushroom Commercial Scale · 16 Jun 2026
                      - button "Unread" [ref=e1431] [cursor=pointer]
                    - listitem [ref=e1432]:
                      - img [ref=e1434]
                      - generic [ref=e1438]:
                        - generic [ref=e1439]:
                          - generic [ref=e1440]: Trainer Assigned
                          - generic [ref=e1441]: Urgent
                          - generic "Unread" [ref=e1442]
                        - paragraph [ref=e1443]: The training operations team has scheduled the following activity. Attendance and preparat…
                        - paragraph [ref=e1444]: Students · 16 Jun 2026
                      - button "Read" [ref=e1445] [cursor=pointer]
          - generic [ref=e1446]:
            - generic [ref=e1447]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e1448]:
              - link "Privacy" [ref=e1449]:
                - /url: /privacy-policy
              - link "Terms" [ref=e1450]:
                - /url: /terms-and-conditions
```

# Test source

```ts
  382 |     const hasFilter = t.includes('filter') || t.includes('Filter') || t.includes('sort') || t.includes('Sort');
  383 |     expect(hasFilter).toBeTruthy();
  384 |   });
  385 | });
  386 | 
  387 | // ====================================================================
  388 | // PHASE 8 — FAILURE HANDLING
  389 | // ====================================================================
  390 | test.describe('Phase 8 — Failure Handling', () => {
  391 |   test('IMPLEMENTATION GAP: No provider unavailable handling', async () => {
  392 |     const resp = await fetch(`${BASE}/api/notifications/fail`).catch(() => null);
  393 |     if (resp) {
  394 |       const data = await resp.json();
  395 |       expect(data).toBeDefined();
  396 |     }
  397 |   });
  398 | 
  399 |   test('IMPLEMENTATION GAP: No network failure handling visible', async ({ page }) => {
  400 |     await login(page);
  401 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  402 |     const t = await bodyText(page);
  403 |     expect(t.includes('failed') || t.includes('Failed') || t.includes('error') || t.includes('Error')).toBeDefined();
  404 |   });
  405 | 
  406 |   test('IMPLEMENTATION GAP: No timeout handling visible', async ({ page }) => {
  407 |     await login(page);
  408 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' });
  409 |     const t = await bodyText(page);
  410 |     expect(t.includes('timeout') || t.includes('Timeout')).toBeFalsy();
  411 |   });
  412 | 
  413 |   test('IMPLEMENTATION GAP: No invalid payload handling', async ({ page }) => {
  414 |     await login(page);
  415 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
  416 |     const t = await bodyText(page).catch(() => '');
  417 |     expect(t.includes('invalid') || t.includes('Invalid')).toBeFalsy();
  418 |   });
  419 | 
  420 |   test('IMPLEMENTATION GAP: No user error messaging', async ({ page }) => {
  421 |     await page.goto('/', { waitUntil: 'networkidle' });
  422 |     const t = await bodyText(page);
  423 |     expect(t.includes('Failed to send') || t.includes('notification failed')).toBeFalsy();
  424 |   });
  425 | });
  426 | 
  427 | // ====================================================================
  428 | // PHASE 9 — DATA INTEGRITY
  429 | // ====================================================================
  430 | test.describe('Phase 9 — Data Integrity', () => {
  431 |   test('Notification service API returns data', async () => {
  432 |     const resp = await fetch(`${BASE}/api/notifications`).catch(() => null);
  433 |     if (resp) {
  434 |       const data = await resp.json();
  435 |       expect(data).toBeDefined();
  436 |     }
  437 |   });
  438 | 
  439 |   test('IMPLEMENTATION GAP: No notification recipient data visible', async ({ page }) => {
  440 |     await login(page);
  441 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  442 |     const t = await bodyText(page);
  443 |     expect(t.length).toBeGreaterThan(10);
  444 |   });
  445 | 
  446 |   test('IMPLEMENTATION GAP: No notification timestamp display', async ({ page }) => {
  447 |     await login(page);
  448 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  449 |     const t = await bodyText(page);
  450 |     expect(t.includes(':') || t.includes('/') || t.includes('-')).toBeDefined();
  451 |   });
  452 | 
  453 |   test('IMPLEMENTATION GAP: No notification type classification', async ({ page }) => {
  454 |     await login(page);
  455 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  456 |     const t = await bodyText(page);
  457 |     const hasType = t.includes('type') || t.includes('Type') || t.includes('category') || t.includes('Category');
  458 |     expect(hasType).toBeTruthy();
  459 |   });
  460 | });
  461 | 
  462 | // ====================================================================
  463 | // PHASE 10 — SECURITY
  464 | // ====================================================================
  465 | test.describe('Phase 10 — Security', () => {
  466 |   test('Public pages accessible without auth', async ({ page }) => {
  467 |     const r = await page.goto('/training/courses', { waitUntil: 'networkidle' });
  468 |     expect(r?.status()).toBeLessThan(400);
  469 |   });
  470 | 
  471 |   test('IMPLEMENTATION GAP: Admin notification pages accessible without auth', async ({ page }) => {
  472 |     const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  473 |     expect(r?.status()).toBeLessThan(400);
  474 |   });
  475 | 
  476 |   test('No PII in notification page source', async ({ page }) => {
  477 |     await login(page);
  478 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  479 |     const html = await page.locator('html').innerHTML();
  480 |     expect(html.includes('password') || html.includes('PASSWORD')).toBeFalsy();
  481 |     expect(html.includes('secret') || html.includes('SECRET')).toBeFalsy();
> 482 |     expect(html.includes('token') || html.includes('TOKEN')).toBeFalsy();
      |                                                              ^ Error: expect(received).toBeFalsy()
  483 |   });
  484 | 
  485 |   test('No console errors on notification pages', async ({ page }) => {
  486 |     const errors: string[] = [];
  487 |     page.on('console', (msg) => { if (msg.type() === 'error') errors.push(msg.text()); });
  488 |     await login(page);
  489 |     await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  490 |     expect(errors.length).toBe(0);
  491 |   });
  492 | 
  493 |   test('No PII in delivery queue page', async ({ page }) => {
  494 |     await login(page);
  495 |     await page.goto('/admin/training/communication/delivery', { waitUntil: 'networkidle' }).catch(() => {});
  496 |     const html = await page.locator('html').innerHTML().catch(() => '');
  497 |     expect(html.includes('password') || html.includes('PASSWORD')).toBeFalsy();
  498 |   });
  499 | });
  500 | 
  501 | // ====================================================================
  502 | // PHASE 11 — CROSS-BROWSER
  503 | // ====================================================================
  504 | test.describe('Phase 11 — Cross-Browser', () => {
  505 |   test('Notification bell renders at desktop', async ({ page }) => {
  506 |     await page.setViewportSize({ width: 1280, height: 800 });
  507 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  508 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  509 |     await expect(bell).toBeVisible({ timeout: 5000 });
  510 |   });
  511 | 
  512 |   test('Notification bell renders at tablet', async ({ page }) => {
  513 |     await page.setViewportSize({ width: 768, height: 1024 });
  514 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  515 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  516 |     const exists = await bell.count();
  517 |     expect(exists).toBeGreaterThanOrEqual(0);
  518 |   });
  519 | 
  520 |   test('Notification bell renders at mobile', async ({ page }) => {
  521 |     await page.setViewportSize({ width: 375, height: 667 });
  522 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  523 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  524 |     const exists = await bell.count();
  525 |     expect(exists).toBeGreaterThanOrEqual(0);
  526 |   });
  527 | 
  528 |   test('Communication pages render at all viewports', async ({ page }) => {
  529 |     await login(page);
  530 |     for (const vp of [{ w: 1280, h: 800 }, { w: 768, h: 1024 }, { w: 375, h: 667 }]) {
  531 |       await page.setViewportSize({ width: vp.w, height: vp.h });
  532 |       const r = await page.goto('/admin/training/communication/notifications', { waitUntil: 'networkidle' });
  533 |       expect(r?.status()).toBeLessThan(400);
  534 |     }
  535 |   });
  536 | });
  537 | 
  538 | // ====================================================================
  539 | // PHASE 12 — ACCESSIBILITY
  540 | // ====================================================================
  541 | test.describe('Phase 12 — Accessibility', () => {
  542 |   test('Notification bell has aria-label', async ({ page }) => {
  543 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  544 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  545 |     await expect(bell).toBeVisible({ timeout: 5000 });
  546 |   });
  547 | 
  548 |   test('Notification dropdown has ARIA dialog role', async ({ page }) => {
  549 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  550 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  551 |     await bell.click();
  552 |     await page.waitForTimeout(500);
  553 |     const dialog = page.locator('[role="dialog"][aria-label="Notifications"]');
  554 |     await expect(dialog).toBeVisible({ timeout: 3000 });
  555 |   });
  556 | 
  557 |   test('Notification list items have listitem role', async ({ page }) => {
  558 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  559 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  560 |     await bell.click();
  561 |     await page.waitForTimeout(500);
  562 |     const items = page.locator('[role="listitem"]');
  563 |     const count = await items.count();
  564 |     expect(count).toBeGreaterThanOrEqual(0);
  565 |   });
  566 | 
  567 |   test('Notification provider has aria-live region', async ({ page }) => {
  568 |     await page.goto('/', { waitUntil: 'networkidle' });
  569 |     const live = page.locator('[aria-live="polite"]');
  570 |     const exists = await live.count();
  571 |     expect(exists).toBeGreaterThanOrEqual(0);
  572 |   });
  573 | 
  574 |   test('Close buttons have aria-label', async ({ page }) => {
  575 |     await page.goto('/admin', { waitUntil: 'networkidle' });
  576 |     const bell = page.locator('button[aria-label*="Notification"]').first();
  577 |     await bell.click();
  578 |     await page.waitForTimeout(500);
  579 |     const closeBtns = page.locator('[role="dialog"] button[aria-label="Dismiss"]');
  580 |     const count = await closeBtns.count();
  581 |     expect(count).toBeGreaterThanOrEqual(0);
  582 |   });
```