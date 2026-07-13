package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.ReportSchedule;
import com.sporekart.ai.analytics.domain.ScheduleFrequency;
import java.util.List;
import java.util.UUID;

public interface ScheduledReportService {
    ReportSchedule createSchedule(ReportSchedule schedule);
    ReportSchedule updateSchedule(UUID id, ReportSchedule schedule);
    void deleteSchedule(UUID id);
    List<ReportSchedule> getSchedulesByFrequency(ScheduleFrequency frequency);
    List<ReportSchedule> getAllActiveSchedules();
}
