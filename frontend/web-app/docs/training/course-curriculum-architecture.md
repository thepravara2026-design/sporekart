# Enterprise Curriculum Builder Platform — Architecture

## Overview

The Enterprise Curriculum Builder is the **permanent academic foundation** of the SporeKart LMS. It organizes how knowledge is delivered via a recursive hierarchy: Course → Curriculum → Module → Lesson → Topic → Learning Activity → Assignment → Assessment → Completion. Every future module (Lessons, Assessments, Assignments, Certificates, Analytics) inherits from this architecture.

## Folder Structure

```
course-curriculum/
├── data/curriculumMockData.ts       # Types, mock data, option arrays, templates
├── state/
│   ├── useCurriculumState.ts        # useReducer-based state (10 slices)
│   └── CurriculumContext.tsx        # React context provider
├── components/
│   ├── CurriculumLayout.tsx         # Header + sidebar + panel content
│   ├── shared/CurriculumSidebar.tsx # 12-section navigation rail
│   ├── panels/
│   │   ├── OverviewPanel.tsx        # Dashboard widgets + tree preview
│   │   ├── CurriculumBuilderPanel.tsx # Recursive tree + add/reorder/duplicate/delete
│   │   ├── ModuleEditorPanel.tsx    # Generic node editor (module/lesson/topic/activity)
│   │   ├── LessonOverviewPanel.tsx  # Lesson type architecture
│   │   ├── TopicOrgPanel.tsx        # Topic organization
│   │   ├── ActivitiesPanel.tsx      # Learning activity architecture
│   │   ├── TemplatesPanel.tsx       # 9 curriculum templates
│   │   ├── LearningPathsPanel.tsx   # Learning path architecture
│   │   ├── CompletionRulesPanel.tsx # Completion rule hooks
│   │   ├── ResourceMappingPanel.tsx # Resource type placeholders
│   │   ├── DragDropPanel.tsx        # Drag-and-drop architecture
│   │   └── PreviewPanel.tsx         # Live preview + map
│   ├── visualization/
│   │   ├── CurriculumTree.tsx       # Recursive ARIA tree (unlimited depth)
│   │   ├── CurriculumBreadcrumbs.tsx# Ancestor path breadcrumb
│   │   ├── CurriculumPreview.tsx    # Device + student/trainer preview
│   │   └── CurriculumMap.tsx        # Future learning-flow map placeholder
│   └── widgets/CurriculumDashboardWidgets.tsx # Reusable stat widgets
└── pages/  (routes point directly to CurriculumLayout)
```

## Academic Hierarchy Design

```
Course (registry link)
└─ Curriculum
   ├─ Module  (title, goal, duration, sequence, prerequisites, resources, status, difficulty)
   │  ├─ Lesson (type: Theory/Practical/Workshop/Video/Reading/Discussion/Case Study/Lab/Business/Live/Webinar/AI)
   │  │  ├─ Topic (name, goal, duration, priority, order, deps, relationships)
   │  │  │  └─ Activity (Reading/Video/Practical/Observation/Field/Lab/Discussion/Exercise/Project/AI)
   │  │  ├─ Assignment
   │  │  └─ Assessment
   │  └─ Lesson ...
   └─ Completion & Certificate
```

Implemented as a unified recursive `CurriculumNode` with `type` discriminator and `children: CurriculumNode[]` — **unlimited depth**. Reducer helpers (`mapNode`/`removeNode`/`addNode`/`cloneNode`) operate recursively; `DUPLICATE_NODE` clones subtrees with fresh ids.

## Component Inventory

| Component | Section | Key Features |
|-----------|---------|-------------|
| CurriculumTree | Builder/Overview | Recursive `role="tree"`, keyboard nav, duplicate/delete handles |
| CurriculumBreadcrumbs | Builder | Ancestor path |
| CurriculumPreview | Preview | Desktop/Tablet/Mobile × Student/Trainer, lesson list |
| CurriculumMap | Preview | Future map scaffold |
| CurriculumDashboardWidgets | Overview | 12 stat cards + module breakdown |
| Builder/Module/Lesson/Topic/Activities/Templates/LearningPaths/Completion/Resources/DragDrop panels | respective | CRUD + architecture |

## Builder Flow

1. Overview → dashboard metrics + hierarchy preview
2. Curriculum Builder → select node, add child of allowed type, publish/unpublish/delete
3. Module Editor → edit selected node fields
4. Templates → pick starting structure
5. Preview → device + role simulation

## State Management Report

`useReducer` + Context (`useCurriculumState.ts` / `CurriculumContext.tsx`), 10 slices: section, tree, expandedIds, selectedNodeId, templates, previewDevice, previewRole, search, filterType, selectedTemplateId. Actions: SET_SECTION, SET_TREE, ADD_NODE (with parentId), UPDATE_NODE, REMOVE_NODE, DUPLICATE_NODE, TOGGLE_EXPAND, SET_SELECTED, SET_TEMPLATES, ADD_TEMPLATE, SET_PREVIEW_DEVICE, SET_PREVIEW_ROLE, SET_SEARCH, SET_FILTER, SET_SELECTED_TEMPLATE.

## Responsive Validation Report

Explorer grid `320px + 1fr`; tree indents `depth*18px`; `Grid columns={2|3}` wraps on tablet/mobile; preview widths 1100/768/375 with mobile rounding. No overflow.

## Accessibility Report

`role="tree"`/`treeitem`/`group`, `aria-expanded`/`aria-selected`, keyboard Enter/Space on nodes and duplicate/delete handles; `role="tab"` sidebar; `nav aria-label="Breadcrumb"`; `aria-label` on all icon buttons; semantic HTML + focus management.

## Performance Optimization Report

Lazy route (`React.lazy`); memoized context (`useMemo`); recursive tree renders only expanded nodes; pure reducer helpers; no side effects.

## Future Integration Readiness Report

Prepared interfaces: Lesson Builder, Assessment Builder, Assignment Engine, Attendance, Certificates, Student Progress, Trainer Workspace, Analytics, Notifications, AI Curriculum Generator, Inventory-linked Practical Kits, Training Commerce. All consume the `CurriculumNode` hierarchy without modifying its core.

## Routing

```
/admin/training/curriculum           → CurriculumLayout
/admin/training/curriculum/:section  → CurriculumLayout (route scaffolding)
```

## Engineering Standards

SOLID/DRY/KISS, feature-first, atomic composition. Design system reused (Card, Badge, Button, Input, Select, Stack, Inline, Grid). Strict typing. Zero duplicate components, zero hardcoded data outside mock layer. Only `App.tsx` modified among existing files; `tsc --noEmit` passes with zero errors.
