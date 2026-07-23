import { useState, useCallback } from 'react';
import type {
  Lesson, Assessment, TrainingBatch, BatchAnalytics,
  AnalyticsSummary, Certification, PracticalGuide, KnowledgeResult, ChatMessage,
} from './types/trainer';

interface TrainerCopilotState {
  messages: ChatMessage[];
  lessons: Lesson[];
  assessments: Assessment[];
  batches: TrainingBatch[];
  batchAnalytics: Record<string, BatchAnalytics>;
  analytics: AnalyticsSummary | null;
  cultivationGuides: PracticalGuide[];
  knowledgeResults: KnowledgeResult[];
  loading: boolean;
  streaming: boolean;
  error: string | null;
}

export function useTrainerCopilot() {
  const [state, setState] = useState<TrainerCopilotState>({
    messages: [],
    lessons: [],
    assessments: [],
    batches: [],
    batchAnalytics: {},
    analytics: null,
    cultivationGuides: [],
    knowledgeResults: [],
    loading: false,
    streaming: false,
    error: null,
  });

  const setLoading = useCallback((v: boolean) => setState(s => ({ ...s, loading: v })), []);
  const setError = useCallback((e: string | null) => setState(s => ({ ...s, error: e })), []);

  const sendMessage = useCallback(async (content: string) => {
    const userMsg: ChatMessage = {
      id: crypto.randomUUID(), role: 'user', content, timestamp: new Date().toISOString(),
    };
    setState(s => ({ ...s, messages: [...s.messages, userMsg], loading: true, error: null }));
    try {
      const assistantMsg: ChatMessage = {
        id: crypto.randomUUID(), role: 'assistant',
        content: 'This is a simulated response. Connect to your API endpoint.',
        timestamp: new Date().toISOString(),
      };
      setState(s => ({ ...s, messages: [...s.messages, assistantMsg], loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const sendStreamMessage = useCallback(async (content: string) => {
    const userMsg: ChatMessage = {
      id: crypto.randomUUID(), role: 'user', content, timestamp: new Date().toISOString(),
    };
    setState(s => ({ ...s, messages: [...s.messages, userMsg], streaming: true, error: null }));
    try {
      const assistantMsg: ChatMessage = {
        id: crypto.randomUUID(), role: 'assistant',
        content: 'Streaming response placeholder. Connect SSE endpoint here.',
        timestamp: new Date().toISOString(),
      };
      setState(s => ({ ...s, messages: [...s.messages, assistantMsg], streaming: false }));
    } catch (err) {
      setState(s => ({ ...s, streaming: false, error: (err as Error).message }));
    }
  }, []);

  const generateLesson = useCallback(async (prompt: string) => {
    setLoading(true); setError(null);
    try {
      const lesson: Lesson = {
        lessonId: crypto.randomUUID(), moduleId: '', title: `Lesson: ${prompt.slice(0, 40)}`,
        content: 'Generated lesson content will appear here.', lessonType: 'LECTURE',
        durationMinutes: 60, learningObjectives: ['Objective 1', 'Objective 2'],
        materials: ['Material A', 'Material B'], activities: ['Activity 1'],
        difficulty: 'INTERMEDIATE',
      };
      setState(s => ({ ...s, lessons: [...s.lessons, lesson], loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const generateAssessment = useCallback(async (prompt: string) => {
    setLoading(true); setError(null);
    try {
      const assessment: Assessment = {
        assessmentId: crypto.randomUUID(), moduleId: '', title: `Assessment: ${prompt.slice(0, 40)}`,
        type: 'MCQ', difficulty: 'INTERMEDIATE',
        questions: [], totalMarks: 100, passingMarks: 40,
      };
      setState(s => ({ ...s, assessments: [...s.assessments, assessment], loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const fetchBatches = useCallback(async () => {
    setLoading(true); setError(null);
    try {
      const now = new Date();
      const fmt = (d: Date) => d.toISOString().slice(0, 10);
      const later = (days: number) => fmt(new Date(now.getTime() + days * 86400000));
      const earlier = (days: number) => fmt(new Date(now.getTime() - days * 86400000));
      const batches: TrainingBatch[] = [
        { batchId: 'b1', batchName: 'Morning A', courseName: 'Mushroom Cultivation 101', startDate: earlier(30), endDate: later(60), status: 'IN_PROGRESS', capacity: 30, enrolledCount: 28, trainerName: 'Dr. Sharma', location: 'Lab 1' },
        { batchId: 'b2', batchName: 'Evening B', courseName: 'Advanced Mycology', startDate: later(15), endDate: later(105), status: 'UPCOMING', capacity: 25, enrolledCount: 18, trainerName: 'Dr. Patel', location: 'Greenhouse A' },
      ];
      setState(s => ({ ...s, batches, loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const fetchBatchAnalytics = useCallback(async (batchId: string) => {
    setLoading(true); setError(null);
    try {
      const ba: BatchAnalytics = {
        batchId, batchName: `Batch ${batchId}`, avgAttendance: 85, avgScore: 72,
        completedModules: 3, totalModules: 8, topStudents: [], atRiskStudents: [],
      };
      setState(s => ({ ...s, batchAnalytics: { ...s.batchAnalytics, [batchId]: ba }, loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const fetchAnalytics = useCallback(async () => {
    setLoading(true); setError(null);
    try {
      const analytics: AnalyticsSummary = {
        totalStudents: 156, totalBatches: 8, activeBatches: 3, completedBatches: 5,
        avgAttendanceAcrossBatches: 82, avgScoreAcrossBatches: 74, atRiskStudents: 12,
        totalCertificationsIssued: 45, pendingCertifications: 8,
      };
      setState(s => ({ ...s, analytics, loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const evaluateCertification = useCallback(async (studentId: string, courseName: string) => {
    setLoading(true); setError(null);
    try {
      const cert: Certification = {
        certificationId: crypto.randomUUID(), studentId, studentName: 'Student Name',
        courseName, overallScore: 78,
        recommendation: 'PASS', status: 'PENDING',
      };
      return cert;
    } finally {
      setState(s => ({ ...s, loading: false }));
    }
  }, []);

  const fetchCultivationGuide = useCallback(async (category?: string) => {
    setLoading(true); setError(null);
    try {
      const guide: PracticalGuide = {
        guideId: crypto.randomUUID(), title: 'Oyster Mushroom Cultivation',
        category: category ?? 'Mushroom', difficulty: 'BEGINNER',
        steps: ['Prepare substrate', 'Sterilize', 'Inoculate', 'Incubate', 'Fruit', 'Harvest'],
        requiredMaterials: ['Spray bottle', 'Gloves', 'Substrate bag', 'Spores'],
        safetyPrecautions: ['Wear mask', 'Sterilize tools', 'Ventilate area'],
        commonMistakes: ['Overwatering', 'Poor sterilization', 'Low humidity'],
      };
      setState(s => ({ ...s, cultivationGuides: [...s.cultivationGuides, guide], loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  const searchKnowledge = useCallback(async (query: string) => {
    setLoading(true); setError(null);
    try {
      const result: KnowledgeResult = {
        id: crypto.randomUUID(), title: `Result for "${query}"`,
        content: 'Detailed knowledge base result will be displayed here.',
        source: 'Knowledge Base', relevanceScore: 0.92, citation: 'Source citation',
      };
      setState(s => ({ ...s, knowledgeResults: [...s.knowledgeResults, result], loading: false }));
    } catch (err) {
      setState(s => ({ ...s, loading: false, error: (err as Error).message }));
    }
  }, []);

  return {
    ...state,
    sendMessage, sendStreamMessage,
    generateLesson, generateAssessment,
    fetchBatches, fetchBatchAnalytics,
    fetchAnalytics, evaluateCertification,
    fetchCultivationGuide, searchKnowledge,
  };
}
