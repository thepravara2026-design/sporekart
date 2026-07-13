import { useEffect, useState, lazy, Suspense } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import { AppContext } from './context';
import type { Role } from './config/roles';
import { getAllPages } from './config/navigation';
import { isPublicWebsiteRoute } from './public-website/config';
import Header from './components/layout/Header';
import Sidebar from './components/layout/Sidebar';
import BreadcrumbBar from './components/layout/BreadcrumbBar';
import CommandPalette from './components/layout/CommandPalette';
import WorkspacePage from './pages/WorkspacePage';
import NotFound from './pages/NotFound';

const DemoIndex = lazy(() => import('./pages/DemoIndex'));
const ResponsiveDemo = lazy(() => import('./pages/ResponsiveDemo'));
const KeyboardDemo = lazy(() => import('./pages/KeyboardDemo'));
const LoadingDemo = lazy(() => import('./pages/LoadingDemo'));
const ErrorsDemo = lazy(() => import('./pages/ErrorsDemo'));
const EmptyStatesDemo = lazy(() => import('./pages/EmptyStatesDemo'));
const FormsDemo = lazy(() => import('./pages/FormsDemo'));
const MicrocopyDemo = lazy(() => import('./pages/MicrocopyDemo'));
const ButtonsPreview = lazy(() => import('./design-system/playground/pages/ButtonsPreview'));
const LinksPreview = lazy(() => import('./design-system/playground/pages/LinksPreview'));
const InputsPreview = lazy(() => import('./design-system/playground/pages/InputsPreview'));
const SearchPreview = lazy(() => import('./design-system/playground/pages/SearchPreview'));
const PasswordPreview = lazy(() => import('./design-system/playground/pages/PasswordPreview'));
const OtpPreview = lazy(() => import('./design-system/playground/pages/OtpPreview'));
const CheckboxPreview = lazy(() => import('./design-system/playground/pages/CheckboxPreview'));
const RadioPreview = lazy(() => import('./design-system/playground/pages/RadioPreview'));
const SwitchPreview = lazy(() => import('./design-system/playground/pages/SwitchPreview'));
const FormsIndex = lazy(() => import('./design-system/playground/pages/FormsIndex'));
const FormLayoutsPreview = lazy(() => import('./design-system/playground/pages/FormLayoutsPreview'));
const FormValidationPreview = lazy(() => import('./design-system/playground/pages/FormValidationPreview'));
const AddressPreview = lazy(() => import('./design-system/playground/pages/AddressPreview'));
const UploadPreview = lazy(() => import('./design-system/playground/pages/UploadPreview'));
const SelectPreview = lazy(() => import('./design-system/playground/pages/SelectPreview'));
const CardsPreview = lazy(() => import('./design-system/playground/pages/CardsPreview'));
const TablesPreview = lazy(() => import('./design-system/playground/pages/TablesPreview'));
const ListsPreview = lazy(() => import('./design-system/playground/pages/ListsPreview'));
const BadgesPreview = lazy(() => import('./design-system/playground/pages/BadgesPreview'));
const ChipsPreview = lazy(() => import('./design-system/playground/pages/ChipsPreview'));
const AvatarsPreview = lazy(() => import('./design-system/playground/pages/AvatarsPreview'));
const EmptyStatesPreview = lazy(() => import('./design-system/playground/pages/EmptyStatesPreview'));
const SkeletonsPreview = lazy(() => import('./design-system/playground/pages/SkeletonsPreview'));
const NavigationIndex = lazy(() => import('./design-system/playground/pages/NavigationIndex'));
const HeaderPreview = lazy(() => import('./design-system/playground/pages/HeaderPreview'));
const SidebarPreview = lazy(() => import('./design-system/playground/pages/SidebarPreview'));
const BreadcrumbPreview = lazy(() => import('./design-system/playground/pages/BreadcrumbPreview'));
const MenuPreview = lazy(() => import('./design-system/playground/pages/MenuPreview'));
const TabsPreview = lazy(() => import('./design-system/playground/pages/TabsPreview'));
const PaginationPreview = lazy(() => import('./design-system/playground/pages/PaginationPreview'));
const StepperPreview = lazy(() => import('./design-system/playground/pages/StepperPreview'));
const LayoutsPreview = lazy(() => import('./design-system/playground/pages/LayoutsPreview'));
const CommandPalettePreview = lazy(() => import('./design-system/playground/pages/CommandPalettePreview'));
const DialogsPreview = lazy(() => import('./design-system/playground/pages/DialogsPreview'));
const ModalsPreview = lazy(() => import('./design-system/playground/pages/ModalsPreview'));
const ToastsPreview = lazy(() => import('./design-system/playground/pages/ToastsPreview'));
const NotificationsPreview = lazy(() => import('./design-system/playground/pages/NotificationsPreview'));
const AlertsPreview = lazy(() => import('./design-system/playground/pages/AlertsPreview'));
const TooltipsPreview = lazy(() => import('./design-system/playground/pages/TooltipsPreview'));
const PopoversPreview = lazy(() => import('./design-system/playground/pages/PopoversPreview'));
const ProgressPreview = lazy(() => import('./design-system/playground/pages/ProgressPreview'));
const LoadingPreview = lazy(() => import('./design-system/playground/pages/LoadingPreview'));
const StatusPreview = lazy(() => import('./design-system/playground/pages/StatusPreview'));
const ChartsPreview = lazy(() => import('./design-system/playground/pages/ChartsPreview'));
const KPIsPreview = lazy(() => import('./design-system/playground/pages/KPIsPreview'));
const TimelinesPreview = lazy(() => import('./design-system/playground/pages/TimelinesPreview'));
const CalendarsPreview = lazy(() => import('./design-system/playground/pages/CalendarsPreview'));
const DataFiltersPreview = lazy(() => import('./design-system/playground/pages/DataFiltersPreview'));
const ExportPreview = lazy(() => import('./design-system/playground/pages/ExportPreview'));
const DesignPlayground = lazy(() => import('./design-system/playground/DesignPlayground'));
const ComponentCatalog = lazy(() => import('./design-system/playground/ComponentCatalog'));
const ComponentDetailPage = lazy(() => import('./design-system/playground/ComponentDetailPage'));
const TokenExplorer = lazy(() => import('./design-system/playground/TokenExplorer'));
const TokenCategoryPage = lazy(() => import('./design-system/playground/TokenCategoryPage'));
const IconLibrary = lazy(() => import('./design-system/playground/pages/IconLibrary'));
const AccessibilityCenter = lazy(() => import('./design-system/playground/pages/AccessibilityCenter'));
const DocumentationCenter = lazy(() => import('./design-system/playground/pages/DocumentationCenter'));
const QualityDashboard = lazy(() => import('./design-system/playground/pages/QualityDashboard'));

const PublicRoutePage = lazy(() => import('./public-website/routes/PublicRoutePage'));
const PublicLayoutPreview = lazy(() =>
  import('./public-website/preview/PublicPreviews').then((m) => ({ default: m.PublicLayoutPreview })),
);
const PublicHeaderPreview = lazy(() =>
  import('./public-website/preview/PublicPreviews').then((m) => ({ default: m.PublicHeaderPreview })),
);
const PublicFooterPreview = lazy(() =>
  import('./public-website/preview/PublicPreviews').then((m) => ({ default: m.PublicFooterPreview })),
);
const PublicNavigationPreview = lazy(() =>
  import('./public-website/preview/PublicPreviews').then((m) => ({ default: m.PublicNavigationPreview })),
);
const PublicSeoPreview = lazy(() =>
  import('./public-website/preview/PublicPreviews').then((m) => ({ default: m.PublicSeoPreview })),
);

const HomePage = lazy(() => import('./public-website/home/HomePage'));
const HomepagePreview = lazy(() => import('./public-website/preview/HomepagePreview'));

const AboutPage = lazy(() => import('./public-website/pages/AboutPage'));
const ProductsPage = lazy(() => import('./public-website/pages/ProductsPage'));
const TrainingPage = lazy(() => import('./public-website/pages/TrainingPage'));
const BlogPage = lazy(() => import('./public-website/pages/BlogPage'));
const ArticlePage = lazy(() => import('./public-website/pages/ArticlePage'));
const BlogCategoryPage = lazy(() => import('./public-website/pages/BlogCategoryPage'));
const BlogTagPage = lazy(() => import('./public-website/pages/BlogTagPage'));
const SearchPage = lazy(() => import('./public-website/pages/SearchPage'));
const BlogPreview = lazy(() => import('./public-website/preview/BlogPreview').then((m) => ({ default: m.BlogPreview })));
const ContactPage = lazy(() => import('./public-website/pages/ContactPage'));
const FaqPage = lazy(() => import('./public-website/pages/FaqPage'));
const CertificationsPage = lazy(() => import('./public-website/pages/CertificationsPage'));
const LegalPage = lazy(() => import('./public-website/pages/LegalPage'));
const InnerPagesPreview = lazy(() => import('./public-website/preview/InnerPagesPreview'));

const PUBLIC_PREVIEW_ROUTES = [
  { path: '/preview/public-layout', Component: PublicLayoutPreview },
  { path: '/preview/public-header', Component: PublicHeaderPreview },
  { path: '/preview/public-footer', Component: PublicFooterPreview },
  { path: '/preview/public-navigation', Component: PublicNavigationPreview },
  { path: '/preview/public-seo', Component: PublicSeoPreview },
  { path: '/preview/blog', Component: BlogPreview },
  { path: '/preview/blog/article', Component: BlogPreview },
  { path: '/preview/blog/search', Component: BlogPreview },
];

function isNonEnterpriseRoute(pathname: string): boolean {
  if (isPublicWebsiteRoute(pathname)) {
    return true;
  }
  if (pathname.startsWith('/preview/')) {
    return true;
  }
  return false;
}

export default function App() {
  const [activeRole, setActiveRole] = useState<Role>('administrator');
  const [paletteOpen, setPaletteOpen] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key.toLowerCase() === 'k') {
        e.preventDefault();
        setPaletteOpen((v) => !v);
      }
    };
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, []);

  const pages = getAllPages();
  const location = useLocation();
  const nonEnterprise = isNonEnterpriseRoute(location.pathname);

  if (nonEnterprise) {
    return (
      <AppContext.Provider value={{ activeRole, setActiveRole, paletteOpen, setPaletteOpen }}>
        <a href="#main" className="sk-skip">Skip to content</a>
        <Suspense fallback={<div className="sk-skeleton-page"><div className="sk-skeleton-header"></div><div className="sk-skeleton-title-row"><div className="sk-skeleton-title"></div></div></div>}>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/products" element={<ProductsPage />} />
            <Route path="/training" element={<TrainingPage />} />
            <Route path="/blog" element={<BlogPage />} />
            <Route path="/blog/category/:slug" element={<BlogCategoryPage />} />
            <Route path="/blog/tag/:slug" element={<BlogTagPage />} />
            <Route path="/blog/:slug" element={<ArticlePage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/contact" element={<ContactPage />} />
            <Route path="/faq" element={<FaqPage />} />
            <Route path="/certifications" element={<CertificationsPage />} />
            <Route path="/privacy-policy" element={<LegalPage />} />
            <Route path="/terms-and-conditions" element={<LegalPage />} />
            <Route path="/refund-policy" element={<LegalPage />} />
            <Route path="/shipping-policy" element={<LegalPage />} />
            <Route path="/auth" element={<PublicRoutePage />} />
            {PUBLIC_PREVIEW_ROUTES.map(({ path, Component }) => (
              <Route key={path} path={path} element={<Component />} />
            ))}
            <Route path="/preview/homepage" element={<HomepagePreview defaultViewport="desktop" />} />
            <Route path="/preview/homepage/desktop" element={<HomepagePreview defaultViewport="desktop" />} />
            <Route path="/preview/homepage/tablet" element={<HomepagePreview defaultViewport="tablet" />} />
            <Route path="/preview/homepage/mobile" element={<HomepagePreview defaultViewport="mobile" />} />
            <Route path="/preview/inner-pages" element={<InnerPagesPreview />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Suspense>
      </AppContext.Provider>
    );
  }


  return (
    <AppContext.Provider value={{ activeRole, setActiveRole, paletteOpen, setPaletteOpen }}>
      <a href="#main" className="sk-skip">Skip to content</a>
      <div className="sk-shell">
        <Header onToggleSidebar={() => setSidebarOpen((v) => !v)} />
        <div className="sk-shell__body">
          <Sidebar open={sidebarOpen} onNavigate={() => setSidebarOpen(false)} />
          <div className="sk-shell__main">
            <BreadcrumbBar />
<main id="main" className="sk-content" tabIndex={-1}>
                <Suspense fallback={<div className="sk-skeleton-page"><div className="sk-skeleton-header"></div><div className="sk-skeleton-title-row"><div className="sk-skeleton-title"></div></div></div>}>
<Routes>
                  {pages.map((p) => (
                    <Route key={p.path} path={p.path} element={<WorkspacePage />} />
                  ))}
                  <Route path="/demo" element={<DemoIndex />} />
                  <Route path="/demo/responsive" element={<ResponsiveDemo />} />
                  <Route path="/demo/keyboard" element={<KeyboardDemo />} />
                  <Route path="/demo/loading" element={<LoadingDemo />} />
                  <Route path="/demo/errors" element={<ErrorsDemo />} />
                  <Route path="/demo/empty" element={<EmptyStatesDemo />} />
                  <Route path="/demo/forms" element={<FormsDemo />} />
                  <Route path="/demo/microcopy" element={<MicrocopyDemo />} />
                  <Route path="/design-system" element={<DesignPlayground />} />
                  <Route path="/design-system/catalog" element={<ComponentCatalog />} />
                  <Route path="/design-system/component/:id" element={<ComponentDetailPage />} />
                  <Route path="/design-system/tokens" element={<TokenExplorer />} />
                  <Route path="/design-system/tokens/:category" element={<TokenCategoryPage />} />
                  <Route path="/design-system/icons" element={<IconLibrary />} />
                  <Route path="/design-system/accessibility" element={<AccessibilityCenter />} />
                  <Route path="/design-system/docs" element={<DocumentationCenter />} />
                  <Route path="/design-system/quality" element={<QualityDashboard />} />
                  <Route path="/design-system/buttons" element={<ButtonsPreview />} />
                  <Route path="/design-system/links" element={<LinksPreview />} />
                  <Route path="/design-system/inputs" element={<InputsPreview />} />
                  <Route path="/design-system/search" element={<SearchPreview />} />
                  <Route path="/design-system/password" element={<PasswordPreview />} />
                  <Route path="/design-system/otp" element={<OtpPreview />} />
                  <Route path="/design-system/checkbox" element={<CheckboxPreview />} />
                  <Route path="/design-system/radio" element={<RadioPreview />} />
                  <Route path="/design-system/switch" element={<SwitchPreview />} />
                  <Route path="/design-system/forms" element={<FormsIndex />} />
                  <Route path="/design-system/forms/layouts" element={<FormLayoutsPreview />} />
                  <Route path="/design-system/forms/validation" element={<FormValidationPreview />} />
                  <Route path="/design-system/forms/address" element={<AddressPreview />} />
                  <Route path="/design-system/forms/upload" element={<UploadPreview />} />
                  <Route path="/design-system/forms/select" element={<SelectPreview />} />
                  <Route path="/design-system/cards" element={<CardsPreview />} />
                  <Route path="/design-system/tables" element={<TablesPreview />} />
                  <Route path="/design-system/lists" element={<ListsPreview />} />
                  <Route path="/design-system/badges" element={<BadgesPreview />} />
                  <Route path="/design-system/chips" element={<ChipsPreview />} />
                  <Route path="/design-system/avatars" element={<AvatarsPreview />} />
                  <Route path="/design-system/empty-states" element={<EmptyStatesPreview />} />
                  <Route path="/design-system/skeletons" element={<SkeletonsPreview />} />
                  <Route path="/design-system/navigation" element={<NavigationIndex />} />
                  <Route path="/design-system/header" element={<HeaderPreview />} />
                  <Route path="/design-system/sidebar" element={<SidebarPreview />} />
                  <Route path="/design-system/breadcrumb" element={<BreadcrumbPreview />} />
                  <Route path="/design-system/menu" element={<MenuPreview />} />
                  <Route path="/design-system/tabs" element={<TabsPreview />} />
                  <Route path="/design-system/pagination" element={<PaginationPreview />} />
                  <Route path="/design-system/stepper" element={<StepperPreview />} />
                  <Route path="/design-system/layouts" element={<LayoutsPreview />} />
                  <Route path="/design-system/command-palette" element={<CommandPalettePreview />} />
                  <Route path="/design-system/dialogs" element={<DialogsPreview />} />
                  <Route path="/design-system/modals" element={<ModalsPreview />} />
                  <Route path="/design-system/toasts" element={<ToastsPreview />} />
                  <Route path="/design-system/notifications" element={<NotificationsPreview />} />
                  <Route path="/design-system/alerts" element={<AlertsPreview />} />
                  <Route path="/design-system/tooltips" element={<TooltipsPreview />} />
                  <Route path="/design-system/popovers" element={<PopoversPreview />} />
                  <Route path="/design-system/progress" element={<ProgressPreview />} />
                  <Route path="/design-system/loading" element={<LoadingPreview />} />
                  <Route path="/design-system/status" element={<StatusPreview />} />
                  <Route path="/design-system/charts" element={<ChartsPreview />} />
                  <Route path="/design-system/kpis" element={<KPIsPreview />} />
                  <Route path="/design-system/timelines" element={<TimelinesPreview />} />
                  <Route path="/design-system/calendars" element={<CalendarsPreview />} />
                  <Route path="/design-system/data-filters" element={<DataFiltersPreview />} />
                  <Route path="/design-system/export" element={<ExportPreview />} />
                  <Route path="*" element={<NotFound />} />
                </Routes>
              </Suspense>
            </main>
          </div>
        </div>
        <footer className="sk-footer" role="contentinfo">
          <span>SporeKart Enterprise · Navigation Prototype (Sprint 19 Part 1B)</span>
          <span className="sk-footer__links">
            <a href="/support/kb">Help</a>
            <a href="/">Terms</a>
            <a href="/">Privacy</a>
            <a href="/">Status</a>
          </span>
        </footer>
      </div>
      <CommandPalette />
    </AppContext.Provider>
  );
}
