package com.sporekart.trainer.copilot.engine;

import com.sporekart.trainer.copilot.domain.TrainingBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BatchManagementEngineTest {

    private BatchManagementEngine engine;

    @BeforeEach
    void setUp() {
        engine = new BatchManagementEngine();
    }

    @Test
    void getBatchOverviewReturnsBatchDetails() {
        Map<String, Object> overview = engine.getBatchOverview("B001");

        assertNotNull(overview.get("batchId"));
        assertNotNull(overview.get("status"));
        assertNotNull(overview.get("duration"));
    }

    @Test
    void getUpcomingSessionsReturnsList() {
        List<Map<String, Object>> sessions = engine.getUpcomingSessions("B001");

        assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
        for (Map<String, Object> session : sessions) {
            assertNotNull(session.get("title"));
            assertNotNull(session.get("date"));
        }
    }

    @Test
    void getTrainerScheduleReturnsSchedule() {
        List<Map<String, Object>> schedule = engine.getTrainerSchedule("T001");

        assertNotNull(schedule);
        assertFalse(schedule.isEmpty());
        for (Map<String, Object> entry : schedule) {
            assertNotNull(entry.get("time"));
            assertNotNull(entry.get("activity"));
        }
    }

    @Test
    void getStudentRosterReturnsStudents() {
        List<Map<String, Object>> roster = engine.getStudentRoster("B001");

        assertNotNull(roster);
        assertFalse(roster.isEmpty());
        for (Map<String, Object> student : roster) {
            assertNotNull(student.get("studentId"));
            assertNotNull(student.get("name"));
        }
    }

    @Test
    void getBatchCapacityReturnsCapacityInfo() {
        Map<String, Object> capacity = engine.getBatchCapacity("B001");

        assertNotNull(capacity.get("capacity"));
        assertNotNull(capacity.get("enrolled"));
        assertNotNull(capacity.get("available"));
    }

    @Test
    void getAttendanceSummaryReturnsStats() {
        Map<String, Object> summary = engine.getAttendanceSummary("B001");

        assertNotNull(summary.get("averageAttendance"));
        assertNotNull(summary.get("totalSessions"));
        assertNotNull(summary.get("attendanceDistribution"));
    }

    @Test
    void getAllBatchesReturnsTrainingBatchList() {
        List<TrainingBatch> batches = engine.getAllBatches();

        assertNotNull(batches);
        assertFalse(batches.isEmpty());
        for (TrainingBatch batch : batches) {
            assertNotNull(batch.batchId());
            assertNotNull(batch.batchName());
        }
    }

    @Test
    void getAllBatchesContainsActiveAndCompleted() {
        List<TrainingBatch> batches = engine.getAllBatches();

        boolean hasActive = batches.stream().anyMatch(b -> "ACTIVE".equals(b.status()));
        boolean hasCompleted = batches.stream().anyMatch(b -> "COMPLETED".equals(b.status()));
        assertTrue(hasActive);
        assertTrue(hasCompleted);
    }

    @Test
    void getBatchOverviewForDifferentBatches() {
        Map<String, Object> overview1 = engine.getBatchOverview("B001");
        Map<String, Object> overview2 = engine.getBatchOverview("B002");

        assertEquals("B001", overview1.get("batchId"));
        assertEquals("B002", overview2.get("batchId"));
    }

    @Test
    void getBatchCapacityAvailableSlots() {
        Map<String, Object> capacity = engine.getBatchCapacity("B001");

        int cap = (Integer) capacity.get("capacity");
        int enrolled = (Integer) capacity.get("enrolled");
        int available = (Integer) capacity.get("available");
        assertEquals(cap - enrolled, available);
    }
}
