# Enterprise Course Builder Architecture

## Overview

The Enterprise Course Builder is a modular, multi-panel authoring workspace for creating and managing training courses within the SporeKart Enterprise LMS. It provides progressive navigation across 11 panels without page reloads, real-time preview, and version-ready architecture.

## Builder Workflow

```
Overview → Information → Objectives → Prerequisites → Media → Resources → SEO → Settings → Preview → Publishing
```

Users progress through panels via the sidebar navigation. Each panel shows a completion badge when data is entered.

## Folder Structure

```
course-builder/
├── data/
│   └── builderMockData.ts          # Types, mock data, option arrays, panel config
├── state/
│   ├── useBuilderState.ts          # useReducer-based state management (11 slices)
│   └── BuilderContext.tsx           # React context provider
├── components/
│   ├── CourseBuilderLayout.tsx       # Main layout: header + sidebar + panel content
│   ├── shared/
│   │   ├── BuilderSidebar.tsx       # Navigation rail with 11 panel entries
│   │   └── SaveIndicator.tsx        # Unsaved changes indicator + save button
│   ├── panels/
│   │   ├── OverviewPanel.tsx        # Builder overview with progress tracking
│   │   ├── CourseInfoPanel.tsx      # Title, code, description, classification, duration
│   │   ├── LearningObjectivesPanel.tsx  # Add/edit/delete/reorder objectives
│   │   ├── PrerequisitesPanel.tsx   # Knowledge, equipment, reading, course, experience
│   │   ├── CurriculumStructurePanel.tsx # Architecture-only: Course → Modules → Lessons → Activities
│   │   ├── MediaPlaceholdersPanel.tsx # Thumbnail, banner, video, gallery placeholders
│   │   ├── ResourcesPanel.tsx       # PDF, SOP, research, document placeholders
│   │   ├── SeoPanel.tsx             # SEO title, description, slug, OG tags, structured data
│   │   ├── SettingsPanel.tsx        # Visibility, enrollment, status, featured, language
│   │   └── PublishingPanel.tsx      # Publishing checklist + publish action
│   └── preview/
│       └── LivePreview.tsx          # Desktop/tablet/mobile + light/dark theme preview
└── pages/
    (routes point directly to CourseBuilderLayout)
```

## Component Inventory

| Component | Panel | Key Features |
|-----------|-------|-------------|
| OverviewPanel | Overview | Progress bar, section completion cards, navigation |
| CourseInfoPanel | Information | Full metadata form (16 fields), array input for tags/keywords |
| LearningObjectivesPanel | Objectives | Inline add/edit/delete/reorder, priority + category badges |
| PrerequisitesPanel | Prerequisites | Type-based prerequisite management |
| CurriculumStructurePanel | Curriculum | Architecture hierarchy diagram, future builder preview |
| MediaPlaceholdersPanel | Media | Placeholder cards for 8 media types |
| ResourcesPanel | Resources | Placeholder cards for 7 resource types |
| SeoPanel | SEO | Full SEO configuration with character count and social preview |
| SettingsPanel | Settings | Visibility, enrollment, publish/archive, featured toggle |
| PublishingPanel | Publishing | 10-point checklist, publish gate, progress bar |
| LivePreview | Preview | 3 device sizes + 2 themes with dynamic course rendering |

## State Management Summary

Managed via `useReducer` + React Context in `useBuilderState.ts`:

- **BuilderPanel** — Current active panel
- **BuilderCourseInfo** — All metadata fields
- **LearningObjective[]** — Reorderable objectives array
- **Prerequisite[]** — Prerequisites array
- **MediaPlaceholder[]** — Media placeholders
- **ResourceAttachment[]** — Resource placeholders
- **SeoConfig** — Full SEO configuration
- **CourseSettingsData** — Visibility, enrollment, status, featured
- **UnsavedChanges** — Dirty flag for save indicator
- **VersionNumber** — Incrementing version counter
- **PreviewDevice** — Desktop/tablet/mobile
- **PreviewTheme** — Light/dark

## Preview System Summary

The LivePreview component renders a responsive simulation of the public course view:

- **Desktop**: 1280px width, full layout
- **Tablet**: 768px width, reduced padding
- **Mobile**: 375px width, compact layout with rounded corners
- **Light/Dark themes**: Toggle background and text colors
- Renders: title, code, status badge, description, difficulty/delivery/duration badges, objectives list, prerequisites list, SEO snippet

## Routing

```
/admin/training/courses/builder           → CourseBuilderLayout (new course)
/admin/training/courses/builder/new       → CourseBuilderLayout
/admin/training/courses/builder/:courseId → CourseBuilderLayout (edit existing)
```

## Engineering Standards

- **SOLID/DRY/KISS**: Single-responsibility panels, shared mock data layer, no inline business logic
- **Design System**: All components use existing Card, Badge, Button, Input, Select, Stack, Inline, Grid
- **Strict Typing**: Full TypeScript with explicit interfaces for all data structures
- **Zero duplicates**: No reimplementation of design system components
- **Mock isolation**: All mock data in dedicated `data/builderMockData.ts`
- **Accessibility**: ARIA labels, roles, keyboard navigation, semantic HTML
- **Performance**: Lazy-loaded via `React.lazy()`, memoized context value, no unnecessary re-renders

## Future Integration Interfaces

The architecture prepares for:

- **CurriculumBuilder**: Full module/lesson/activity authoring
- **LessonBuilder**: Rich text editor, media embedding, interactive elements
- **AssessmentBuilder**: Quizzes, assignments, grading rubrics
- **CertificateEngine**: Certificate templates, auto-generation
- **BatchManagement**: Link courses to training batches
- **Enrollment**: Student enrollment management
- **ResourceLibrary**: Centralized file storage and management
- **AICourseAuthor**: AI-assisted content generation
- **TrainingCommerce**: Course pricing, subscription, marketplace
