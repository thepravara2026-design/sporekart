import { useEffect, useState, lazy, Suspense } from 'react';
import { Route, Routes, Navigate, useLocation } from 'react-router-dom';
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
import { Icon } from './design-system/icons/Icon';

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
// ---- Phase 11 · Sprint 26 · Part 8: Enterprise Course Discovery Platform (LMS) ----
const CourseCatalogPage = lazy(() => import('./public-website/course-discovery/pages/CourseCatalogPage').then((m) => ({ default: m.CourseCatalogPage })));
const CourseDetailsPage = lazy(() => import('./public-website/course-discovery/pages/CourseDetailsPage').then((m) => ({ default: m.CourseDetailsPage })));
const CourseCategoryPage = lazy(() => import('./public-website/course-discovery/pages/CourseCategoryPage').then((m) => ({ default: m.CourseCategoryPage })));
const LearningPathsPage = lazy(() => import('./public-website/course-discovery/pages/LearningPathsPage').then((m) => ({ default: m.LearningPathsPage })));
const CourseComparisonPage = lazy(() => import('./public-website/course-discovery/pages/CourseComparisonPage').then((m) => ({ default: m.CourseComparisonPage })));
const MarketingLandingPage = lazy(() => import('./public-website/course-discovery/pages/MarketingLandingPage').then((m) => ({ default: m.MarketingLandingPage })));
const BlogPage = lazy(() => import('./public-website/pages/BlogPage'));
const ArticlePage = lazy(() => import('./public-website/pages/ArticlePage'));
const BlogCategoryPage = lazy(() => import('./public-website/pages/BlogCategoryPage'));
const BlogTagPage = lazy(() => import('./public-website/pages/BlogTagPage'));
const SearchPage = lazy(() => import('./public-website/pages/SearchPage'));
const BlogPreview = lazy(() => import('./public-website/preview/BlogPreview').then((m) => ({ default: m.BlogPreview })));
const ContactPage = lazy(() => import('./public-website/pages/ContactPage'));
const SupportPage = lazy(() => import('./public-website/pages/SupportPage'));
const FaqPage = lazy(() => import('./public-website/pages/FaqPage'));
const CertificationsPage = lazy(() => import('./public-website/pages/CertificationsPage'));
const LegalPage = lazy(() => import('./public-website/pages/LegalPage'));
const InnerPagesPreview = lazy(() => import('./public-website/preview/InnerPagesPreview'));
const ExperiencePreview = lazy(() => import('./public-website/preview/ExperiencePreview'));

// ---- Authentication Experience (Phase 6 · Sprint 21 · Part 7) ----
const LoginPage = lazy(() => import('./features/auth/pages/LoginPage'));
const RegisterPage = lazy(() => import('./features/auth/pages/RegisterPage'));
const ForgotPasswordPage = lazy(() => import('./features/auth/pages/ForgotPasswordPage'));
const VerifyOtpPage = lazy(() => import('./features/auth/pages/VerifyOtpPage'));
const AuthLoadingPage = lazy(() => import('./features/auth/pages/SessionPages').then((m) => ({ default: m.AuthLoadingPage })));
const SessionExpiredPage = lazy(() => import('./features/auth/pages/SessionPages').then((m) => ({ default: m.SessionExpiredPage })));
const AccessDeniedPage = lazy(() => import('./features/auth/pages/SessionPages').then((m) => ({ default: m.AccessDeniedPage })));
const AuthErrorsGallery = lazy(() => import('./features/auth/pages/ErrorGallery'));
const LoginPreview = lazy(() => import('./features/auth/preview/LoginPreview'));
const RegisterPreview = lazy(() => import('./features/auth/preview/RegisterPreview'));
const SessionPreview = lazy(() => import('./features/auth/preview/SessionPreview'));
const AuthOtpPreview = lazy(() => import('./features/auth/preview/OtpPreview'));
const AuthErrorsPreview = lazy(() => import('./features/auth/preview/AuthErrorsPreview'));

// ---- Customer Workspace (Phase 7 · Sprint 22 · Part 2) ----
const CustomerLayout = lazy(() => import('./features/customer/CustomerLayout'));
const DashboardPage = lazy(() => import('./features/customer/pages/DashboardPage'));
const PlaceholderPage = lazy(() => import('./features/customer/pages/PlaceholderPage'));
const ProfileDashboard = lazy(() => import('./features/customer/pages/ProfileDashboard'));
const DashboardPreview = lazy(() => import('./features/customer/preview/DashboardPreview'));
const MobileDashboardPreview = lazy(() => import('./features/customer/preview/MobileDashboardPreview'));
const TabletDashboardPreview = lazy(() => import('./features/customer/preview/TabletDashboardPreview'));
const CustomerSidebarPreview = lazy(() => import('./features/customer/preview/SidebarPreview'));

// ---- Customer Orders Feature (Phase 7 · Sprint 22 · Part 3) ----
const OrdersDashboard = lazy(() => import('./features/customer/orders/OrdersDashboard'));
const OrderDetailsPage = lazy(() => import('./features/customer/orders/OrderDetailsPage'));
const ShipmentTrackingPage = lazy(() => import('./features/customer/orders/ShipmentTrackingPage'));
const ReturnsRefundsPage = lazy(() => import('./features/customer/orders/ReturnsRefundsPage'));

const OrdersPreview = lazy(() => import('./features/customer/orders/preview/OrdersPreviews').then((m) => ({ default: m.OrdersPreview })));
const OrderDetailsPreview = lazy(() => import('./features/customer/orders/preview/OrdersPreviews').then((m) => ({ default: m.OrderDetailsPreview })));
const OrderTrackingPreview = lazy(() => import('./features/customer/orders/preview/OrdersPreviews').then((m) => ({ default: m.OrderTrackingPreview })));
const OrderRefundsPreview = lazy(() => import('./features/customer/orders/preview/OrdersPreviews').then((m) => ({ default: m.OrderRefundsPreview })));
const MobileOrdersPreview = lazy(() => import('./features/customer/orders/preview/OrdersPreviews').then((m) => ({ default: m.MobileOrdersPreview })));
const TabletOrdersPreview = lazy(() => import('./features/customer/orders/preview/OrdersPreviews').then((m) => ({ default: m.TabletOrdersPreview })));

// ---- Customer Engagement Feature (Phase 7 · Sprint 22 · Part 4) ----
const WishlistPage = lazy(() => import('./features/customer/engagement/WishlistPage'));
const NotificationsPage = lazy(() => import('./features/customer/engagement/NotificationsPage'));
const EngagementHubPage = lazy(() => import('./features/customer/engagement/EngagementHubPage'));

const WishlistPreview = lazy(() => import('./features/customer/engagement/preview/EngagementPreviews').then((m) => ({ default: m.WishlistPreview })));
const RecommendationsPreview = lazy(() => import('./features/customer/engagement/preview/EngagementPreviews').then((m) => ({ default: m.RecommendationsPreview })));
const EngagementNotificationsPreview = lazy(() => import('./features/customer/engagement/preview/EngagementPreviews').then((m) => ({ default: m.NotificationsPreview })));
const EngagementPreview = lazy(() => import('./features/customer/engagement/preview/EngagementPreviews').then((m) => ({ default: m.EngagementPreview })));

// ---- Customer Training Feature (Phase 7 · Sprint 22 · Part 5) ----
const TrainingDashboard = lazy(() => import('./features/customer/training/TrainingDashboard'));
const CourseLibrary = lazy(() => import('./features/customer/training/CourseLibrary'));
const CourseDetails = lazy(() => import('./features/customer/training/CourseDetails'));
const VideoLearningPage = lazy(() => import('./features/customer/training/VideoLearningPage'));
const MyLearningPage = lazy(() => import('./features/customer/training/MyLearningPage'));
const CertificatesPage = lazy(() => import('./features/customer/training/CertificatesPage'));
const TrainingSchedulePage = lazy(() => import('./features/customer/training/TrainingSchedulePage'));

const TrainingPreview = lazy(() => import('./features/customer/training/preview/TrainingPreviews').then((m) => ({ default: m.TrainingPreview })));
const CoursePreview = lazy(() => import('./features/customer/training/preview/TrainingPreviews').then((m) => ({ default: m.CoursePreview })));
const ClassroomPreview = lazy(() => import('./features/customer/training/preview/TrainingPreviews').then((m) => ({ default: m.ClassroomPreview })));
const MobileTrainingPreview = lazy(() => import('./features/customer/training/preview/TrainingPreviews').then((m) => ({ default: m.MobileTrainingPreview })));

// ---- Customer Support Feature (Phase 7 · Sprint 22 · Part 6) ----
const SupportDashboard = lazy(() => import('./features/customer/support/SupportDashboard'));
const TicketsPage = lazy(() => import('./features/customer/support/TicketsPage'));
const KnowledgeBasePage = lazy(() => import('./features/customer/support/KnowledgeBasePage'));
const FaqCenterPage = lazy(() => import('./features/customer/support/FaqCenterPage'));
const ContactSupportPage = lazy(() => import('./features/customer/support/ContactSupportPage'));
const FeedbackPage = lazy(() => import('./features/customer/support/FeedbackPage'));

const SupportDashboardPreview = lazy(() => import('./features/customer/support/preview/SupportPreviews').then((m) => ({ default: m.SupportDashboardPreview })));
const TicketsPreview = lazy(() => import('./features/customer/support/preview/SupportPreviews').then((m) => ({ default: m.TicketsPreview })));
const KBPreview = lazy(() => import('./features/customer/support/preview/SupportPreviews').then((m) => ({ default: m.KBPreview })));
const MobileSupportPreview = lazy(() => import('./features/customer/support/preview/SupportPreviews').then((m) => ({ default: m.MobileSupportPreview })));

// ---- Customer Intelligence Feature (Phase 7 · Sprint 22 · Part 7) ----
const PersonalizedHome = lazy(() => import('./features/customer/intelligence/PersonalizedHome'));
const CustomerInsights = lazy(() => import('./features/customer/intelligence/CustomerInsights'));
const ActivityFeed = lazy(() => import('./features/customer/intelligence/ActivityFeed'));
const RecommendationHub = lazy(() => import('./features/customer/intelligence/RecommendationHub'));
const AchievementCenter = lazy(() => import('./features/customer/intelligence/AchievementCenter'));
const ProgressCenter = lazy(() => import('./features/customer/intelligence/ProgressCenter'));
const AnalyticsDashboard = lazy(() => import('./features/customer/intelligence/AnalyticsDashboard'));

const InsightsPreview = lazy(() => import('./features/customer/intelligence/preview/IntelligencePreviews').then((m) => ({ default: m.InsightsPreview })));
const PersonalizedPreview = lazy(() => import('./features/customer/intelligence/preview/IntelligencePreviews').then((m) => ({ default: m.PersonalizedPreview })));
const ActivityPreview = lazy(() => import('./features/customer/intelligence/preview/IntelligencePreviews').then((m) => ({ default: m.ActivityPreview })));
const AnalyticsPreview = lazy(() => import('./features/customer/intelligence/preview/IntelligencePreviews').then((m) => ({ default: m.AnalyticsPreview })));
const MobileIntelligencePreview = lazy(() => import('./features/customer/intelligence/preview/IntelligencePreviews').then((m) => ({ default: m.MobileIntelligencePreview })));

// ---- Enterprise Admin (Phase 8 · Sprint 23 · Part 1) ----
const AdminLayout = lazy(() => import('./admin/AdminLayout'));
const AdminDashboard = lazy(() => import('./admin/pages/AdminDashboard'));
const AdminProfile = lazy(() => import('./admin/pages/AdminProfile'));
const AdminSettings = lazy(() => import('./admin/pages/AdminSettings'));
const AdminSystem = lazy(() => import('./admin/pages/AdminSystem'));
const AdminHelp = lazy(() => import('./admin/pages/AdminHelp'));

// ---- Enterprise Mock Modules (Phase 8 · Sprint 23 · Part 8) ----
const AdminProductsPage = lazy(() => import('./admin/modules/products/ProductsPage').then((m) => ({ default: m.ProductsPage })));
const AdminInventoryPage = lazy(() => import('./admin/modules/inventory/InventoryPage').then((m) => ({ default: m.InventoryPage })));
const AdminWarehousePage = lazy(() => import('./admin/modules/warehouse/WarehousePage').then((m) => ({ default: m.WarehousePage })));
const AdminOrdersPage = lazy(() => import('./admin/modules/orders/OrdersPage').then((m) => ({ default: m.OrdersPage })));
const AdminCustomersPage = lazy(() => import('./admin/modules/customers/CustomersPage').then((m) => ({ default: m.CustomersPage })));
const AdminCrmPage = lazy(() => import('./admin/modules/crm/CrmPage').then((m) => ({ default: m.CrmPage })));
// ---- Phase 11 · Sprint 26 · Part 1: Enterprise Training Workspace (LMS) ----
const TrainingWorkspaceRoute = lazy(() => import('./admin/training-workspace/TrainingWorkspaceRoute'));
const TrainingDashboardPage = lazy(() => import('./admin/training-workspace/pages/TrainingDashboardPage'));
const TrainingCoursesPage = lazy(() => import('./admin/training-workspace/courses/pages/CourseRegistryPage'));
const TrainingCurriculumPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.CurriculumPage })));
const TrainingBatchesPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.BatchesPage })));
const TrainingStudentsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.StudentsPage })));
const TrainingTrainersPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.TrainersPage })));
const TrainingAttendancePage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.AttendancePage })));
const TrainingAssignmentsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.AssignmentsPage })));
const TrainingAssessmentsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.AssessmentsPage })));
const TrainingCertificatesPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.CertificatesPage })));
const TrainingAnnouncementsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.AnnouncementsPage })));
const TrainingReportsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.ReportsPage })));
const TrainingAnalyticsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.AnalyticsPage })));
const TrainingSettingsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.SettingsPage })));
const TrainingAiAssistantPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.AiAssistantPage })));
const TrainingCommunityPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.CommunityPage })));
const TrainingDiscussionsPage = lazy(() => import('./admin/training-workspace/pages/allPlaceholders').then(m => ({ default: m.DiscussionsPage })));
const TrainingCourseDraftsPage = lazy(() => import('./admin/training-workspace/courses/pages/CourseDraftsPage'));
const TrainingCourseArchivedPage = lazy(() => import('./admin/training-workspace/courses/pages/CourseArchivedPage'));
const TrainingCoursePublishedPage = lazy(() => import('./admin/training-workspace/courses/pages/CoursePublishedPage'));
const TrainingCourseDetailPage = lazy(() => import('./admin/training-workspace/courses/pages/CourseDetailPage'));
const TrainingCourseBuilderPage = lazy(() => import('./admin/training-workspace/course-builder/components/CourseBuilderLayout').then(m => ({ default: m.CourseBuilderLayout })));
const TrainingTaxonomyPage = lazy(() => import('./admin/training-workspace/course-taxonomy/components/TaxonomyLayout').then(m => ({ default: m.TaxonomyLayout })));
const TrainingCurriculumBuilderPage = lazy(() => import('./admin/training-workspace/course-curriculum/components/CurriculumLayout').then(m => ({ default: m.CurriculumLayout })));
const TrainingResourceLibraryPage = lazy(() => import('./admin/training-workspace/learning-resources/components/ResourceLibraryLayout').then(m => ({ default: m.ResourceLibraryLayout })));
const TrainingEnrollmentPage = lazy(() => import('./admin/training-workspace/course-enrollment/components/EnrollmentLayout').then(m => ({ default: m.EnrollmentLayout })));

const AnalyticsWorkspaceRoute = lazy(() => import('./admin/training-workspace/analytics/pages/AnalyticsWorkspaceRoute'));
const AnalyticsExecutivePage = lazy(() => import('./admin/training-workspace/analytics/pages/ExecutiveDashboardPage'));
const AnalyticsCoursePage = lazy(() => import('./admin/training-workspace/analytics/pages/CourseAnalyticsPage'));
const AnalyticsEnrollmentPage = lazy(() => import('./admin/training-workspace/analytics/pages/EnrollmentAnalyticsPage'));
const AnalyticsCurriculumPage = lazy(() => import('./admin/training-workspace/analytics/pages/CurriculumAnalyticsPage'));
const AnalyticsResourcePage = lazy(() => import('./admin/training-workspace/analytics/pages/ResourceAnalyticsPage'));
const AnalyticsSavedPage = lazy(() => import('./admin/training-workspace/analytics/pages/SavedDashboardsPage'));
const CommunicationWorkspaceRoute = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationWorkspaceRoute'));
const StudentWorkspaceRoute = lazy(() => import('./admin/training-workspace/student-workspace/StudentWorkspaceRoute'));
const StudentDashboardPage = lazy(() => import('./admin/training-workspace/student-workspace/pages/StudentDashboardPage'));
const StudentRegistryPage = lazy(() => import('./admin/training-workspace/student-workspace/pages/StudentRegistryPage'));
const StudentDirectoryPage = lazy(() => import('./admin/training-workspace/student-workspace/pages/StudentDirectoryPage'));
const StudentArchivedPage = lazy(() => import('./admin/training-workspace/student-workspace/pages/StudentArchivedPage'));
const StudentProfilePage = lazy(() => import('./admin/training-workspace/student-workspace/profile/pages/ProfileIndex'));
const StudentEnrollmentPage = lazy(() => import('./admin/training-workspace/student-workspace/enrollment/pages/EnrollmentIndex'));
const StudentAttendancePage = lazy(() => import('./admin/training-workspace/student-workspace/attendance/pages/AttendanceIndex'));
const StudentAssignmentPage = lazy(() => import('./admin/training-workspace/student-workspace/assignments/pages/AssignmentIndex'));
const StudentAssessmentPage = lazy(() => import('./admin/training-workspace/student-workspace/assessments/pages/AssessmentIndex'));
const StudentLearningProgressPage = lazy(() => import('./admin/training-workspace/student-workspace/learning-progress/pages/LearningProgressIndex'));
const CommunicationOverviewPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationOverviewPage'));
const CommunicationAnnouncementsPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationAnnouncementsPage'));
const CommunicationNotificationsPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationNotificationsPage'));
const CommunicationScheduledPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationScheduledPage'));
const CommunicationTemplatesPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationTemplatesPage'));
const CommunicationHistoryPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationHistoryPage'));
const CommunicationDeliveryQueuePage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationDeliveryQueuePage'));
const CommunicationFutureChannelsPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationFutureChannelsPage'));
const CommunicationStatisticsPage = lazy(() => import('./admin/training-workspace/communication/pages/CommunicationStatisticsPage'));

const AdminShippingPage = lazy(() => import('./admin/modules/shipping/ShippingPage').then((m) => ({ default: m.ShippingPage })));
const AdminFinancePage = lazy(() => import('./admin/modules/finance/FinancePage').then((m) => ({ default: m.FinancePage })));
const AdminReportsPage = lazy(() => import('./admin/modules/reports/ReportsPage').then((m) => ({ default: m.ReportsPage })));
const AdminAnalyticsPage = lazy(() => import('./admin/modules/analytics/AnalyticsPage').then((m) => ({ default: m.AnalyticsPage })));

// ---- Phase 9 · Sprint 24 · Part 5: Enterprise Media Management / DAM ----
const AdminMediaPage = lazy(() => import('./admin/modules/media/MediaPage').then((m) => ({ default: m.MediaPage })));
const MediaPreviewApp = lazy(() => import('./admin/modules/media/preview/MediaPreviewApp').then((m) => ({ default: m.MediaPreviewApp })));

// ---- Phase 9 · Sprint 24 · Part 6: Enterprise Product Organization / Taxonomy ----
const OrganizationPreviewApp = lazy(() => import('./admin/modules/products/organization/preview/OrganizationPreviewApp').then((m) => ({ default: m.OrganizationPreviewApp })));

// ---- Phase 9 · Sprint 24 · Part 7: Enterprise Pricing & Commercial Rules ----
const PricingPreviewApp = lazy(() => import('./admin/modules/products/pricing/preview/PricingPreviewApp').then((m) => ({ default: m.PricingPreviewApp })));

// ---- Phase 9 · Sprint 24 · Part 8: Enterprise Product Variants, SKU, Packaging & Attributes ----
const VariantPreviewApp = lazy(() => import('./admin/modules/products/variants/preview/VariantPreviewApp').then((m) => ({ default: m.VariantPreviewApp })));

// ---- Phase 9 · Sprint 24 · Part 9: Enterprise SEO, Marketplace & Publishing ----
const SeoPreviewApp = lazy(() => import('./admin/modules/products/seo/preview/SeoPreviewApp').then((m) => ({ default: m.SeoPreviewApp })));

// ---- Phase 9 · Sprint 24 · Part 10: Enterprise Product Validation, QA & Compliance ----
const ValidationPreviewApp = lazy(() => import('./admin/modules/products/validation/preview/ValidationPreviewApp').then((m) => ({ default: m.ValidationPreviewApp })));

// ---- Phase 9 · Sprint 24 · Part 11: Enterprise Product Analytics, Intelligence & Catalog Insights ----
const AnalyticsPreviewApp = lazy(() => import('./admin/modules/products/analytics/preview/AnalyticsPreviewApp').then((m) => ({ default: m.AnalyticsPreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 1: Enterprise Inventory & Warehouse Management (foundation) ----
const InventoryPreviewApp = lazy(() => import('./admin/modules/inventory/preview/InventoryPreviewApp').then((m) => ({ default: m.InventoryPreviewApp })));
const WarehousePreviewApp = lazy(() => import('./admin/modules/warehouse/preview/WarehousePreviewApp').then((m) => ({ default: m.WarehousePreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 3: Enterprise Inventory Item Management (Product Mapping & SKU Association) ----
const AdminInventoryItemPage = lazy(() => import('./admin/modules/inventory-items/InventoryItemPage').then((m) => ({ default: m.InventoryItemPage })));
const InventoryItemPreviewApp = lazy(() => import('./admin/modules/inventory-items/preview/InventoryItemPreviewApp').then((m) => ({ default: m.InventoryItemPreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 4: Enterprise Stock Management Engine & Inventory State Management ----
const AdminStockPage = lazy(() => import('./admin/modules/stock/StockPage').then((m) => ({ default: m.StockPage })));
const StockPreviewApp = lazy(() => import('./admin/modules/stock/preview/StockPreviewApp').then((m) => ({ default: m.StockPreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 5: Enterprise Batch Management, Lot Tracking & Expiry Management Platform ----
const AdminBatchPage = lazy(() => import('./admin/modules/batch/BatchPage').then((m) => ({ default: m.BatchPage })));
const BatchPreviewApp = lazy(() => import('./admin/modules/batch/preview/BatchPreviewApp').then((m) => ({ default: m.BatchPreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 6: Enterprise Inventory Movements, Transfers & Transaction History ----
const AdminMovementPage = lazy(() => import('./admin/modules/movement/MovementPage').then((m) => ({ default: m.MovementPage })));
const MovementPreviewApp = lazy(() => import('./admin/modules/movement/preview/MovementPreviewApp').then((m) => ({ default: m.MovementPreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 7: Enterprise Purchase Receiving, Goods Receipt, Quality Inspection & Inbound Operations ----
const AdminReceivingPage = lazy(() => import('./admin/modules/receiving/ReceivingPage').then((m) => ({ default: m.ReceivingPage })));
const ReceivingPreviewApp = lazy(() => import('./admin/modules/receiving/preview/ReceivingPreviewApp').then((m) => ({ default: m.ReceivingPreviewApp })));

// ---- Phase 10 · Sprint 25 · Part 8: Enterprise Inventory Intelligence, Forecasting & Analytics ----
const AdminIntelligencePage = lazy(() => import('./admin/modules/intelligence').then((m) => ({ default: m.IntelligenceWorkspace })));
const IntelligencePreviewApp = lazy(() => import('./admin/modules/intelligence').then((m) => ({ default: m.IntelligencePreview })));

const AdminLayoutPreview = lazy(() => import('./admin/preview/AdminPreviews').then((m) => ({ default: m.AdminLayoutPreview })));
const AdminHeaderPreview = lazy(() => import('./admin/preview/AdminPreviews').then((m) => ({ default: m.AdminHeaderPreview })));
const AdminDashboardPreview = lazy(() => import('./admin/dashboard/preview/DashboardPreview').then((m) => ({ default: m.DashboardPreview })));
const AdminMobilePreview = lazy(() => import('./admin/preview/AdminPreviews').then((m) => ({ default: m.AdminMobilePreview })));
const ExtendedNavPreview = lazy(() => import('./admin/navigation/preview/ExtendedNavPreview').then((m) => ({ default: m.ExtendedNavPreview })));
const NavSidebarPreview = lazy(() => import('./admin/navigation/preview/SidebarPreview').then((m) => ({ default: m.SidebarPreview })));
const NavigationPreview = lazy(() => import('./admin/navigation/preview/NavigationPreview').then((m) => ({ default: m.NavigationPreview })));
const AdminCmdPalettePreview = lazy(() => import('./admin/navigation/preview/CommandPalettePreview').then((m) => ({ default: m.CommandPalettePreview })));
const WorkspacePreview = lazy(() => import('./admin/navigation/preview/WorkspacePreview').then((m) => ({ default: m.WorkspacePreview })));

const SecurityPreview = lazy(() => import('./admin/preview/part6/SecurityPreview').then((m) => ({ default: m.SecurityPreview })));
const PermissionsPreview = lazy(() => import('./admin/preview/part6/PermissionsPreview').then((m) => ({ default: m.PermissionsPreview })));
const ErrorStatesPreview = lazy(() => import('./admin/preview/part6/ErrorStatesPreview').then((m) => ({ default: m.ErrorStatesPreview })));
const MaintenancePreview = lazy(() => import('./admin/preview/part6/MaintenancePreview').then((m) => ({ default: m.MaintenancePreview })));
const OfflinePreview = lazy(() => import('./admin/preview/part6/OfflinePreview').then((m) => ({ default: m.OfflinePreview })));

const AdminComponentsIndex = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.ComponentsIndex })));
const AdminButtonsPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.ButtonsPreview })));
const AdminFormsPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.FormsPreview })));
const AdminCardsPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.CardsPreview })));
const AdminTablesPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.TablesPreview })));
const AdminDialogsPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.DialogsPreview })));
const AdminLoadingPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.LoadingPreview })));
const AdminBadgesPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.BadgesPreview })));
const AdminTabsPreview = lazy(() => import('./admin/components/preview/AdminComponentPreviews').then((m) => ({ default: m.TabsPreview })));

const AdminDataGridPreview = lazy(() => import('./admin/components/preview/DataGridPreviews').then((m) => ({ default: m.DataGridPreview })));
const AdminSearchPreview = lazy(() => import('./admin/components/preview/DataGridPreviews').then((m) => ({ default: m.SearchPreview })));
const AdminFilterPreview = lazy(() => import('./admin/components/preview/DataGridPreviews').then((m) => ({ default: m.FilterPreview })));
const AdminPaginationPreview = lazy(() => import('./admin/components/preview/DataGridPreviews').then((m) => ({ default: m.PaginationPreview })));
const AdminTablePreview = lazy(() => import('./admin/components/preview/DataGridPreviews').then((m) => ({ default: m.TablePreview })));

const ProductPreviewApp = lazy(() => import('./admin/modules/products/routing/ProductPreviewApp').then((m) => ({ default: m.ProductPreviewApp })));

// Note: CUSTOMER_ROUTES array is replaced with nested <Route> elements in the JSX router structure below.

const PUBLIC_PREVIEW_ROUTES = [
  { path: '/preview/public-layout', Component: PublicLayoutPreview },
  { path: '/preview/public-header', Component: PublicHeaderPreview },
  { path: '/preview/public-footer', Component: PublicFooterPreview },
  { path: '/preview/public-navigation', Component: PublicNavigationPreview },
  { path: '/preview/public-seo', Component: PublicSeoPreview },
  { path: '/preview/blog', Component: BlogPreview },
  { path: '/preview/blog/article', Component: BlogPreview },
  { path: '/preview/blog/search', Component: BlogPreview },
  { path: '/preview/contact', Component: ExperiencePreview },
  { path: '/preview/support', Component: ExperiencePreview },
  { path: '/preview/faq', Component: ExperiencePreview },
  { path: '/preview/legal', Component: ExperiencePreview },
];

function isNonEnterpriseRoute(pathname: string): boolean {
  if (isPublicWebsiteRoute(pathname)) {
    return true;
  }
  if (pathname.startsWith('/preview/')) {
    return true;
  }
  if (pathname.startsWith('/dashboard') || pathname.startsWith('/dashboard/')) {
    return true;
  }
  if (pathname.startsWith('/admin') || pathname.startsWith('/admin/')) {
    return true;
  }
  const AUTH_ROUTES = new Set([
    '/login',
    '/register',
    '/forgot-password',
    '/verify-otp',
    '/session-expired',
    '/access-denied',
    '/auth-error',
  ]);
  if (AUTH_ROUTES.has(pathname) || pathname.startsWith('/auth/')) {
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
            <Route path="/training/courses" element={<CourseCatalogPage />} />
            <Route path="/training/courses/category/:slug" element={<CourseCategoryPage />} />
            <Route path="/training/courses/compare" element={<CourseComparisonPage />} />
            <Route path="/training/courses/:slug" element={<CourseDetailsPage />} />
            <Route path="/training/learning-paths" element={<LearningPathsPage />} />
            <Route path="/training/learn/:slug" element={<MarketingLandingPage />} />
            <Route path="/blog" element={<BlogPage />} />
            <Route path="/blog/category/:slug" element={<BlogCategoryPage />} />
            <Route path="/blog/tag/:slug" element={<BlogTagPage />} />
            <Route path="/blog/:slug" element={<ArticlePage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/contact" element={<ContactPage />} />
            <Route path="/support" element={<SupportPage />} />
            <Route path="/faq" element={<FaqPage />} />
            <Route path="/certifications" element={<CertificationsPage />} />
            <Route path="/privacy-policy" element={<LegalPage />} />
            <Route path="/terms-and-conditions" element={<LegalPage />} />
            <Route path="/refund-policy" element={<LegalPage />} />
            <Route path="/shipping-policy" element={<LegalPage />} />
            <Route path="/cookie-policy" element={<LegalPage />} />
            <Route path="/disclaimer" element={<LegalPage />} />
            <Route path="/auth" element={<LoginPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/forgot-password" element={<ForgotPasswordPage />} />
            <Route path="/verify-otp" element={<VerifyOtpPage />} />
            <Route path="/auth/loading" element={<AuthLoadingPage />} />
            <Route path="/session-expired" element={<SessionExpiredPage />} />
            <Route path="/access-denied" element={<AccessDeniedPage />} />
            <Route path="/auth-error" element={<AuthErrorsGallery />} />
            <Route path="/preview/login" element={<LoginPreview />} />
            <Route path="/preview/register" element={<RegisterPreview />} />
            <Route path="/preview/otp" element={<AuthOtpPreview />} />
            <Route path="/preview/session" element={<SessionPreview />} />
            <Route path="/preview/auth-errors" element={<AuthErrorsPreview />} />
            <Route path="/dashboard" element={<CustomerLayout />}>
              <Route index element={<DashboardPage />} />
              <Route path="orders" element={<OrdersDashboard />} />
              <Route path="orders/:id" element={<OrderDetailsPage />} />
              <Route path="orders/:id/track" element={<ShipmentTrackingPage />} />
              <Route path="orders/:id/refund" element={<ReturnsRefundsPage />} />
              <Route path="wishlist" element={<WishlistPage />} />
              <Route path="saved-items" element={<WishlistPage />} />
              <Route path="recently-viewed" element={<WishlistPage />} />
              <Route path="personalized" element={<PersonalizedHome />} />
              <Route path="insights" element={<CustomerInsights />} />
              <Route path="activity" element={<ActivityFeed />} />
              <Route path="recommendations" element={<RecommendationHub />} />
              <Route path="achievements" element={<AchievementCenter />} />
              <Route path="progress" element={<ProgressCenter />} />
              <Route path="analytics" element={<AnalyticsDashboard />} />
              <Route path="training" element={<TrainingDashboard />} />
              <Route path="training/courses" element={<CourseLibrary />} />
              <Route path="training/course/:id" element={<CourseDetails />} />
              <Route path="training/classroom/:id" element={<VideoLearningPage />} />
              <Route path="training/my-learning" element={<MyLearningPage />} />
              <Route path="training/certificates" element={<CertificatesPage />} />
              <Route path="training/schedule" element={<TrainingSchedulePage />} />
              <Route path="products" element={<PlaceholderPage title="Products" description="Browse available cultivars and supplies." icon={<Icon name="package" size={32} color="currentColor" />} primaryAction={{ label: 'View Catalog', href: '/dashboard/products' }} />} />
              <Route path="addresses" element={<PlaceholderPage title="Addresses" description="Manage shipping and billing addresses." icon={<Icon name="map-pin" size={32} color="currentColor" />} primaryAction={{ label: 'Add Address', href: '/dashboard/addresses/new' }} />} />
              <Route path="support" element={<SupportDashboard />} />
              <Route path="support/dashboard" element={<SupportDashboard />} />
              <Route path="support/tickets" element={<TicketsPage />} />
              <Route path="support/tickets/:id" element={<TicketsPage />} />
              <Route path="support/faq" element={<FaqCenterPage />} />
              <Route path="support/help-center" element={<KnowledgeBasePage />} />
              <Route path="support/contact" element={<ContactSupportPage />} />
              <Route path="support/feedback" element={<FeedbackPage />} />
              <Route path="notifications" element={<NotificationsPage />} />
              <Route path="engagement" element={<EngagementHubPage />} />
              <Route path="profile" element={<ProfileDashboard />} />
              <Route path="profile/edit" element={<PlaceholderPage title="Edit Profile" description="Update your personal information and profile details." icon={<Icon name="edit" size={32} color="currentColor" />} primaryAction={{ label: 'Save Changes', href: '/dashboard/profile' }} secondaryActions={[{ label: 'Cancel', href: '/dashboard/profile' }]} />} />
              <Route path="profile/security" element={<PlaceholderPage title="Security & Privacy" description="Manage your account security settings, verification, and connected devices." icon={<Icon name="shield" size={32} color="currentColor" />} primaryAction={{ label: 'Update Security', href: '/dashboard/profile' }} />} />
              <Route path="profile/preferences" element={<PlaceholderPage title="Preferences" description="Configure your notification, communication, and display preferences." icon={<Icon name="settings" size={32} color="currentColor" />} primaryAction={{ label: 'Save Preferences', href: '/dashboard/profile' }} />} />
              <Route path="profile/privacy" element={<PlaceholderPage title="Privacy Settings" description="Manage your data privacy, consent, and account deletion preferences." icon={<Icon name="lock" size={32} color="currentColor" />} primaryAction={{ label: 'Update Privacy', href: '/dashboard/profile' }} />} />
              <Route path="profile/activity" element={<PlaceholderPage title="Activity History" description="View your recent account activities, login history, and updates." icon={<Icon name="activity" size={32} color="currentColor" />} />} />
              <Route path="profile/sessions" element={<PlaceholderPage title="Active Sessions" description="Manage your logged-in devices and sessions across different platforms." icon={<Icon name="monitor" size={32} color="currentColor" />} />} />
              <Route path="profile/account-status" element={<PlaceholderPage title="Account Status" description="View your account health, verification status, and membership details." icon={<Icon name="info" size={32} color="currentColor" />} />} />
              <Route path="settings" element={<PlaceholderPage title="Settings" description="Configure notifications, privacy, and preferences." icon={<Icon name="settings" size={32} color="currentColor" />} />} />
            </Route>

            <Route path="/admin" element={<AdminLayout />}>
              <Route index element={<Navigate to="/admin/dashboard" replace />} />
              <Route path="dashboard" element={<AdminDashboard />} />
              <Route path="products" element={<AdminProductsPage />} />
              <Route path="media" element={<AdminMediaPage />} />
              <Route path="inventory" element={<AdminInventoryPage />} />
              <Route path="warehouse" element={<AdminWarehousePage />} />
              <Route path="inventory-items" element={<AdminInventoryItemPage />} />
              <Route path="stock" element={<AdminStockPage />} />
              <Route path="batch" element={<AdminBatchPage />} />
              <Route path="movements" element={<AdminMovementPage />} />
              <Route path="receiving" element={<AdminReceivingPage />} />
              <Route path="intelligence" element={<AdminIntelligencePage />} />
              <Route path="orders" element={<AdminOrdersPage />} />
              <Route path="customers" element={<AdminCustomersPage />} />
              <Route path="crm" element={<AdminCrmPage />} />
              <Route path="training" element={<Navigate to="/admin/training/dashboard" replace />} />
              <Route path="training/*" element={<TrainingWorkspaceRoute />}>
                <Route path="dashboard" element={<TrainingDashboardPage />} />
                <Route path="courses" element={<TrainingCoursesPage />} />
              <Route path="courses/drafts" element={<TrainingCourseDraftsPage />} />
              <Route path="courses/archived" element={<TrainingCourseArchivedPage />} />
              <Route path="courses/published" element={<TrainingCoursePublishedPage />} />
              <Route path="courses/:courseId" element={<TrainingCourseDetailPage />} />
              <Route path="courses/builder" element={<TrainingCourseBuilderPage />} />
              <Route path="courses/builder/new" element={<TrainingCourseBuilderPage />} />
              <Route path="courses/builder/:courseId" element={<TrainingCourseBuilderPage />} />
              <Route path="taxonomy" element={<TrainingTaxonomyPage />} />
              <Route path="taxonomy/:section" element={<TrainingTaxonomyPage />} />
              <Route path="curriculum" element={<TrainingCurriculumBuilderPage />} />
              <Route path="curriculum/:section" element={<TrainingCurriculumBuilderPage />} />
                <Route path="curriculum" element={<TrainingCurriculumPage />} />
                <Route path="batches" element={<TrainingBatchesPage />} />
                <Route path="students" element={<TrainingStudentsPage />} />
                <Route path="trainers" element={<TrainingTrainersPage />} />
                <Route path="attendance" element={<TrainingAttendancePage />} />
                <Route path="assignments" element={<TrainingAssignmentsPage />} />
                <Route path="assessments" element={<TrainingAssessmentsPage />} />
                <Route path="certificates" element={<TrainingCertificatesPage />} />
                <Route path="resources" element={<TrainingResourceLibraryPage />} />
                <Route path="resources/:section" element={<TrainingResourceLibraryPage />} />
                <Route path="enrollment" element={<TrainingEnrollmentPage />} />
                <Route path="enrollment/:section" element={<TrainingEnrollmentPage />} />
                <Route path="announcements" element={<TrainingAnnouncementsPage />} />
                <Route path="reports" element={<TrainingReportsPage />} />
                <Route path="analytics" element={<TrainingAnalyticsPage />} />
                <Route path="lms-analytics" element={<Navigate to="/admin/training/lms-analytics/executive" replace />} />
                <Route path="lms-analytics/*" element={<AnalyticsWorkspaceRoute />}>
                  <Route index element={<Navigate to="/admin/training/lms-analytics/executive" replace />} />
                  <Route path="executive" element={<AnalyticsExecutivePage />} />
                  <Route path="course" element={<AnalyticsCoursePage />} />
                  <Route path="enrollment" element={<AnalyticsEnrollmentPage />} />
                  <Route path="curriculum" element={<AnalyticsCurriculumPage />} />
                  <Route path="resource" element={<AnalyticsResourcePage />} />
                  <Route path="saved" element={<AnalyticsSavedPage />} />
                </Route>
                <Route path="communication" element={<Navigate to="/admin/training/communication/overview" replace />} />
                <Route path="communication/*" element={<CommunicationWorkspaceRoute />}>
                  <Route index element={<Navigate to="/admin/training/communication/overview" replace />} />
                  <Route path="overview" element={<CommunicationOverviewPage />} />
                  <Route path="announcements" element={<CommunicationAnnouncementsPage />} />
                  <Route path="notifications" element={<CommunicationNotificationsPage />} />
                  <Route path="scheduled" element={<CommunicationScheduledPage />} />
                  <Route path="templates" element={<CommunicationTemplatesPage />} />
                  <Route path="history" element={<CommunicationHistoryPage />} />
                  <Route path="delivery" element={<CommunicationDeliveryQueuePage />} />
                  <Route path="channels" element={<CommunicationFutureChannelsPage />} />
                  <Route path="statistics" element={<CommunicationStatisticsPage />} />
                </Route>
                <Route path="student-workspace" element={<Navigate to="/admin/training/student-workspace/overview" replace />} />
                <Route path="student-workspace/*" element={<StudentWorkspaceRoute />}>
                  <Route index element={<Navigate to="/admin/training/student-workspace/overview" replace />} />
                  <Route path="overview" element={<StudentDashboardPage />} />
                  <Route path="registry" element={<StudentRegistryPage />} />
                  <Route path="directory" element={<StudentDirectoryPage />} />
                  <Route path="archived" element={<StudentArchivedPage />} />
                  <Route path="profile" element={<StudentProfilePage />} />
                  <Route path="profile/*" element={<StudentProfilePage />} />
                  <Route path="enrollment" element={<StudentEnrollmentPage />} />
                  <Route path="enrollment/*" element={<StudentEnrollmentPage />} />
                  <Route path="attendance" element={<StudentAttendancePage />} />
                  <Route path="attendance/*" element={<StudentAttendancePage />} />
                  <Route path="assignments" element={<StudentAssignmentPage />} />
                  <Route path="assignments/*" element={<StudentAssignmentPage />} />
                  <Route path="assessments" element={<StudentAssessmentPage />} />
                  <Route path="assessments/*" element={<StudentAssessmentPage />} />
                  <Route path="learning-progress" element={<StudentLearningProgressPage />} />
                  <Route path="learning-progress/*" element={<StudentLearningProgressPage />} />
                </Route>
                <Route path="settings" element={<TrainingSettingsPage />} />
                <Route path="ai-assistant" element={<TrainingAiAssistantPage />} />
                <Route path="community" element={<TrainingCommunityPage />} />
                <Route path="discussions" element={<TrainingDiscussionsPage />} />
              </Route>
              <Route path="shipping" element={<AdminShippingPage />} />
              <Route path="finance" element={<AdminFinancePage />} />
              <Route path="reports" element={<AdminReportsPage />} />
              <Route path="analytics" element={<AdminAnalyticsPage />} />
              <Route path="profile" element={<AdminProfile />} />
              <Route path="settings" element={<AdminSettings />} />
              <Route path="system" element={<AdminSystem />} />
              <Route path="help" element={<AdminHelp />} />
            </Route>

            <Route path="/preview/admin" element={<AdminLayoutPreview />} />
            <Route path="/preview/admin/sidebar" element={<NavSidebarPreview />} />
            <Route path="/preview/admin/navigation" element={<NavigationPreview />} />
            <Route path="/preview/admin/command-palette" element={<AdminCmdPalettePreview />} />
            <Route path="/preview/admin/workspace" element={<WorkspacePreview />} />
            <Route path="/preview/admin/header" element={<AdminHeaderPreview />} />
            <Route path="/preview/admin/dashboard" element={<AdminDashboardPreview />} />
            <Route path="/preview/admin/mobile" element={<AdminMobilePreview />} />
            <Route path="/preview/admin/extended-nav" element={<ExtendedNavPreview />} />

            <Route path="/preview/products/*" element={<ProductPreviewApp />} />
            <Route path="/preview/media/*" element={<MediaPreviewApp />} />
            <Route path="/preview/products/organization/*" element={<OrganizationPreviewApp />} />
            <Route path="/preview/products/pricing/*" element={<PricingPreviewApp />} />
            <Route path="/preview/products/variants/*" element={<VariantPreviewApp />} />
            <Route path="/preview/products/seo/*" element={<SeoPreviewApp />} />
            <Route path="/preview/products/validation/*" element={<ValidationPreviewApp />} />
            <Route path="/preview/products/analytics/*" element={<AnalyticsPreviewApp />} />
        <Route path="/preview/inventory/*" element={<InventoryPreviewApp />} />
        <Route path="/preview/warehouse/*" element={<WarehousePreviewApp />} />
        <Route path="/preview/inventory/items/*" element={<InventoryItemPreviewApp />} />
        <Route path="/preview/stock/*" element={<StockPreviewApp />} />
        <Route path="/preview/batch/*" element={<BatchPreviewApp />} />
        <Route path="/preview/inventory/movements/*" element={<MovementPreviewApp />} />
        <Route path="/preview/inventory/transfers/*" element={<MovementPreviewApp />} />
        <Route path="/preview/inventory/adjustments/*" element={<MovementPreviewApp />} />
        <Route path="/preview/inventory/history/*" element={<MovementPreviewApp />} />
        <Route path="/preview/inventory/transactions/*" element={<MovementPreviewApp />} />
        <Route path="/preview/inventory/audit/*" element={<MovementPreviewApp />} />
        <Route path="/preview/receiving/dashboard/*" element={<ReceivingPreviewApp />} />
        <Route path="/preview/receiving/registry/*" element={<ReceivingPreviewApp />} />
        <Route path="/preview/receiving/workflow/*" element={<ReceivingPreviewApp />} />
        <Route path="/preview/receiving/inspection/*" element={<ReceivingPreviewApp />} />
        <Route path="/preview/receiving/profile/*" element={<ReceivingPreviewApp />} />
        <Route path="/preview/receiving/analytics/*" element={<ReceivingPreviewApp />} />
        <Route path="/preview/analytics/dashboard/*" element={<IntelligencePreviewApp />} />
        <Route path="/preview/analytics/kpis/*" element={<IntelligencePreviewApp />} />
        <Route path="/preview/analytics/warehouse/*" element={<IntelligencePreviewApp />} />
        <Route path="/preview/analytics/forecast/*" element={<IntelligencePreviewApp />} />
        <Route path="/preview/analytics/reports/*" element={<IntelligencePreviewApp />} />
        <Route path="/preview/analytics/insights/*" element={<IntelligencePreviewApp />} />

            <Route path="/preview/admin/security" element={<SecurityPreview />} />
            <Route path="/preview/admin/permissions" element={<PermissionsPreview />} />
            <Route path="/preview/admin/error-states" element={<ErrorStatesPreview />} />
            <Route path="/preview/admin/maintenance" element={<MaintenancePreview />} />
            <Route path="/preview/admin/offline" element={<OfflinePreview />} />

            <Route path="/preview/admin/components" element={<AdminComponentsIndex />} />
            <Route path="/preview/admin/components/buttons" element={<AdminButtonsPreview />} />
            <Route path="/preview/admin/components/forms" element={<AdminFormsPreview />} />
            <Route path="/preview/admin/components/cards" element={<AdminCardsPreview />} />
            <Route path="/preview/admin/components/tables" element={<AdminTablesPreview />} />
            <Route path="/preview/admin/components/dialogs" element={<AdminDialogsPreview />} />
            <Route path="/preview/admin/components/loading" element={<AdminLoadingPreview />} />
            <Route path="/preview/admin/components/badges" element={<AdminBadgesPreview />} />
            <Route path="/preview/admin/components/tabs" element={<AdminTabsPreview />} />

            <Route path="/preview/admin/data-grid" element={<AdminDataGridPreview />} />
            <Route path="/preview/admin/search" element={<AdminSearchPreview />} />
            <Route path="/preview/admin/filter" element={<AdminFilterPreview />} />
            <Route path="/preview/admin/pagination" element={<AdminPaginationPreview />} />
            <Route path="/preview/admin/table" element={<AdminTablePreview />} />

            <Route path="/preview/dashboard" element={<DashboardPreview />} />
            <Route path="/preview/dashboard/mobile" element={<MobileDashboardPreview />} />
            <Route path="/preview/dashboard/tablet" element={<TabletDashboardPreview />} />
            <Route path="/preview/dashboard/sidebar" element={<CustomerSidebarPreview />} />

            <Route path="/preview/orders" element={<OrdersPreview />} />
            <Route path="/preview/orders/details" element={<OrderDetailsPreview />} />
            <Route path="/preview/orders/tracking" element={<OrderTrackingPreview />} />
            <Route path="/preview/orders/refunds" element={<OrderRefundsPreview />} />
            <Route path="/preview/orders/mobile" element={<MobileOrdersPreview />} />
            <Route path="/preview/orders/tablet" element={<TabletOrdersPreview />} />

            <Route path="/preview/wishlist" element={<WishlistPreview />} />
            <Route path="/preview/recommendations" element={<RecommendationsPreview />} />
            <Route path="/preview/notifications" element={<EngagementNotificationsPreview />} />
            <Route path="/preview/engagement" element={<EngagementPreview />} />

            <Route path="/preview/training" element={<TrainingPreview />} />
            <Route path="/preview/training/dashboard" element={<TrainingPreview />} />
            <Route path="/preview/training/course" element={<CoursePreview />} />
            <Route path="/preview/training/classroom" element={<ClassroomPreview />} />
            <Route path="/preview/training/mobile" element={<MobileTrainingPreview />} />

            <Route path="/preview/support" element={<SupportDashboardPreview />} />
            <Route path="/preview/support/dashboard" element={<SupportDashboardPreview />} />
            <Route path="/preview/support/tickets" element={<TicketsPreview />} />
            <Route path="/preview/support/help-center" element={<KBPreview />} />
            <Route path="/preview/support/mobile" element={<MobileSupportPreview />} />

            <Route path="/preview/dashboard/insights" element={<InsightsPreview />} />
            <Route path="/preview/dashboard/personalized" element={<PersonalizedPreview />} />
            <Route path="/preview/dashboard/activity" element={<ActivityPreview />} />
            <Route path="/preview/dashboard/analytics" element={<AnalyticsPreview />} />
            <Route path="/preview/dashboard/mobile" element={<MobileIntelligencePreview />} />
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
                  <Route path="/design-system/sidebar" element={<CustomerSidebarPreview />} />
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
                  <Route path="/products/*" element={<ProductPreviewApp />} />
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
