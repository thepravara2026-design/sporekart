# Analytics Platform — Data Model

## Performance Levels

`excellent | good | average | needs-attention | critical | outstanding | top-performer`

## Core Interfaces

### StudentAnalytics
- Per-student aggregate metrics: attendanceScore, assignmentScore, assessmentScore, learningProgress, competencyIndex, certificationReadiness, learningHours, engagementScore, activityFrequency, achievementCount
- Categorization: riskIndicator, performanceLevel
- Relations: studentId, courseId, batchId

### CourseMetrics
- Enrollment/completion counts, completionPercent
- Average scores (attendance, assignment, assessment, learningHours)
- certificationPercent, competencyGrowth
- Trends: enrollmentTrend[], completionTrend[], assessmentResults[]

### TrainerMetrics
- coursesDelivered, totalStudents
- Student outcomes: studentSuccessRate, averageAssessmentScore, completionPercent, certificationRate
- courseNames

### BatchMetrics
- totalStudents, activeStudents, atRiskCount
- Aggregates: completionPercent, averageAttendance, averageAssignmentScore, averageAssessmentScore, certificationRate, averageCompetencies, totalLearningHours

### ExecutiveDashboard
- Platform totals: activeStudents, totalCourses, activeTrainers, activeBatches
- Platform aggregates: courseCompletionPercent, averageAttendance, averageAssessmentScore, averageAssignmentScore, certificationRate
- Trends: enrollmentTrend[], completionTrend[], recentActivity[]

### KPIData
- label, value, change, changeType, icon, variant

### AcademicInsights
- Categorized students: topStudents, highPerformers, lowPerformers, attendanceRisks, completionRisks
- achievementTrends[], learningGaps[]

### LearningIntelligence
- studentTimeline[], engagementAnalysis[], performanceAnalysis[]
- competencyAnalysis[], achievementAnalysis[], learningHealth[]
- completionForecast[], certificationForecast[]
