# Folder Structure

```
student-workspace/
  assessments/
    types.ts                                  — Domain types (18 types, 11 statuses, 14 question types)
    data/
      mockData.ts                             — Mock data generators
    state/
      AssessmentContext.tsx                    — Context + provider + hook
    components/
      AssessmentStatusBadge.tsx               — Status badge (11 variants)
      AssessmentTable.tsx                     — Registry table (8 columns)
      AssessmentCard.tsx                      — Card view
      DashboardWidget.tsx                     — Stat widget
      EmptyStates.tsx                         — 7 empty states
      Skeletons.tsx                           — Loading skeletons
      AssessmentTimeline.tsx                  — Timeline component (8 stages)
      AnalyticsPanel.tsx                      — Analytics view
      ResultCard.tsx                          — Result display card
      QuestionCategoryCard.tsx                — Question category card
    pages/
      AssessmentIndex.tsx                     — Wrapper + sub-nav
      AssessmentDashboardPage.tsx             — Dashboard
      AssessmentRegistryPage.tsx              — Registry
      AssessmentBuilderPage.tsx               — Builder
      QuestionBankPage.tsx                    — Question categories
      ExaminationCenterPage.tsx               — Exam management
      ResultCenterPage.tsx                    — Results
      AssessmentAnalyticsPage.tsx             — Analytics
      AssessmentArchivedPage.tsx              — Archived
    docs/
      16 documentation files
```

## File Count: 31 files (including docs)
