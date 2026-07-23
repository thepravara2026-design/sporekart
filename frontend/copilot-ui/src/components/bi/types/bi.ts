export interface CompanyHealthScore { overall: number; revenueScore: number; customerScore: number; trainingScore: number; inventoryScore: number; operationsScore: number; growthScore: number; trend: string; factors: HealthFactor[]; }
export interface HealthFactor { name: string; score: number; status: string; description: string; }
export interface RevenueAnalytics { grossRevenue: number; netRevenue: number; averageOrderValue: number; revenueGrowth: number; byCategory: Record<string,number>; byRegion: Record<string,number>; byChannel: Record<string,number>; orderCount: number; }
export interface CustomerAnalytics { totalCustomers: number; newCustomers: number; returningCustomers: number; churnedCustomers: number; retentionRate: number; churnRate: number; customerLifetimeValue: number; topCustomers: TopCustomer[]; bySegment: Record<string,number>; }
export interface TopCustomer { customerId: string; name: string; totalSpent: number; orderCount: number; }
export interface ProductAnalytics { topProducts: ProductPerformance[]; worstProducts: ProductPerformance[]; fastMovers: ProductPerformance[]; categoryPerformance: Record<string,number>; }
export interface ProductPerformance { productId: string; name: string; category: string; revenue: number; unitsSold: number; growth: number; profitMargin: number; }
export interface InventoryAnalytics { totalStock: number; lowStockItems: number; deadStockItems: number; turnoverRate: number; inventoryRisk: number; restockingPriority: RestockingItem[]; }
export interface RestockingItem { productId: string; name: string; currentStock: number; recommendedOrder: number; urgency: string; }
export interface TrainingAnalytics { totalBatches: number; totalStudents: number; averageAttendance: number; averageScore: number; completionRate: number; certificationsIssued: number; trainerPerformance: TrainerPerformance[]; }
export interface TrainerPerformance { trainerId: string; name: string; batches: number; students: number; avgScore: number; completionRate: number; }
export interface BusinessForecast { forecastId: string; metric: string; method: string; points: ForecastPoint[]; confidenceInterval: number; accuracy: number; seasonality: string; trend: string; recommendations: string; }
export interface ForecastPoint { period: string; value: number; lowerBound: number; upperBound: number; }
export interface DecisionRecommendation { recommendationId: string; title: string; description: string; category: string; priority: string; expectedImpact: string; confidenceScore: number; rationale: string; actionItems: string[]; }
export interface RiskAlert { riskId: string; riskType: string; severity: string; title: string; description: string; probability: number; impact: number; recommendedAction: string; status: string; }
export interface BusinessInsight { insightId: string; title: string; description: string; category: string; severity: string; confidenceScore: number; businessImpact: string; actionItems: string[]; }
export interface VisualizationConfig { vizId: string; type: string; title: string; labels: string[]; values: number[]; colors: string[]; }
export interface ExecutiveSummary { healthScore: CompanyHealthScore; revenue: RevenueAnalytics; customers: CustomerAnalytics; products: ProductAnalytics; inventory: InventoryAnalytics; training: TrainingAnalytics; insights: BusinessInsight[]; recommendations: DecisionRecommendation[]; risks: RiskAlert[]; }
export interface ChatMessage { id: string; role: 'user' | 'assistant'; content: string; timestamp: string; suggestions?: Suggestion[]; }
export interface Suggestion { label: string; action: string; payload?: Record<string,unknown>; }
export interface KpiEntry { id: string; name: string; value: number; changePercent: number; trend: string; unit: string; status: string; }
