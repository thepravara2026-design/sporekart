# Learning Resource Management Platform (LRMS) — Architecture

Sprint 26 · Part 6 — Enterprise Learning Resource Management Platform for the SporeKart LMS.
A centralized **Digital Knowledge Repository** for all learning assets. Runs entirely in **Mock Mode**
(no backend, APIs, database, cloud storage, or real uploads).

## Scope

The LRMS is a self-contained feature folder under `src/admin/training-workspace/learning-resources/`.
It reuses the existing enterprise design system and follows the same structural conventions as
Parts 1–5 (`useReducer` + React Context, sidebar `role="tab"` navigation, recursive tree patterns).

## Folder Structure

```
learning-resources/
  data/
    resourceMockData.ts        # types, enums, option lists, mock library (10 resources, folders, collections)
  state/
    useResourceState.ts        # useReducer state hook + action creators
    ResourceContext.tsx        # React Context provider + useResourceContext hook
  components/
    ResourceLibraryLayout.tsx  # top-level layout (header + sidebar + panel switch)
    shared/
      ResourceSidebar.tsx      # 10-section role="tab" navigation
    panels/
      OverviewPanel.tsx        # dashboard widgets
      LibraryExplorerPanel.tsx # search/filter/view toolbar + resource views
      CollectionsPanel.tsx     # curated bundles + drill-in
      FoldersPanel.tsx         # recursive folder tree + folder contents
      FavoritesPanel.tsx       # starred resources
      RecentPanel.tsx          # recently updated
      ArchivedPanel.tsx        # retired resources (restore/delete)
      PreviewPanel.tsx         # single resource detail preview
      LinkingPanel.tsx         # resource linking architecture (courses/curriculum/lessons/...)
      StoragePanel.tsx         # cloud storage readiness (abstraction interface)
    visualization/
      ResourceCard.tsx         # grid/list/compact card variants
      ResourceViews.tsx        # grid/list/compact/table/tree renderers
      FolderTree.tsx           # recursive role="tree" folder hierarchy
    widgets/
      ResourceDashboardWidgets.tsx # stat cards, by-type breakdown, most-used
  pages/                       # (reserved)
```

## State Management

`useResourceState` exposes a `useReducer` store with actions for:

- **Section navigation** — `setSection`
- **Resource CRUD** — `addResource`, `updateResource`, `removeResource`, `duplicateResource`
- **Flags** — `toggleFavorite`, `togglePin`, `toggleStarred`
- **Lifecycle** — `archiveResource`, `restoreResource`
- **Folders** — `setFolders`, `addFolder` (recursive tree insert), `toggleFolderExpand`, `setSelectedFolder`
- **Explorer** — `setView`, `setSearch`, `setFilters`
- **Collections** — `setSelectedCollection`
- **Preview** — `setPreviewResource`, `setSelectedResource`

`ResourceContext.tsx` wraps the hook and is consumed via `useResourceContext()`.

## Sections (10)

`overview`, `library`, `collections`, `folders`, `favorites`, `recent`, `archived`, `preview`, `linking`, `storage`.

## Resource Model

Each `ResourceItem` carries: id, name, code, description, `type` (18 resource types incl. SOPs,
manuals, research papers, government guidelines, business/marketing templates, interactive/VR/AR),
category/subcategory, tags, language, author, department, dates, version, `status`
(active/draft/archived), `visibility` (public/private/internal), usage count, file size, and folder id.

## Views

The Library Explorer supports **grid**, **list**, **compact**, and **table** views, plus a recursive
**tree** view via `FolderTree` in the Folders panel. Filtering is available by type, category,
department, language, status, and visibility with free-text search across name/code/description/tags.

## Linking Architecture

`LinkingPanel` documents the central-repository model: a single resource may be linked to any learning
entity — Courses, Curriculum Modules, Lessons, Assessments, and Batches. Associations are mock-only.

## Cloud Storage Readiness

`StoragePanel` defines a storage abstraction interface (`upload`, `download`, `delete`,
`getSignedUrl`, `listVersions`) with adapter stubs for Local Mock Store (active), AWS S3, Azure Blob,
Google Cloud Storage, and CDN delivery (all readiness-only, no connectivity).

## Routing

Wired in `src/App.tsx` under the training workspace:

- `/admin/training/resources` → `TrainingResourceLibraryPage` (ResourceLibraryLayout)
- `/admin/training/resources/:section` → same layout

`ResourceLibraryLayout` is lazy-loaded via `React.lazy()` + `.then(m => ({ default: m.ResourceLibraryLayout }))`.

## Constraints Honored

- **Mock Mode only** — no backend/API/DB/cloud/real uploads.
- Only `App.tsx` was modified among existing files.
- Design-system components used with supported props (Stack/Inline/Badge take no `style`/`justify`);
  plain `<div style>` wrappers used for layout.
- `tsc --noEmit` passes with zero errors.
