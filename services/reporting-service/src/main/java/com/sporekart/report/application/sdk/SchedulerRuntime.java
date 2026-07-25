package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.ReportSchedule;
import com.sporekart.report.domain.model.ScheduleFrequency;
import com.sporekart.report.infrastructure.scheduler.MockSchedulerService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerRuntime {
    private final MockSchedulerService schedulerService;

    public SchedulerRuntime(MockSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public ReportSchedule createSchedule(String name, String reportId, String reportTitle,
                                          ScheduleFrequency frequency, String cronExpression,
                                          String exportFormat, String recipientEmail) {
        return schedulerService.createSchedule(name, reportId, reportTitle, frequency,
            cronExpression, exportFormat, recipientEmail);
    }

    public ReportSchedule getSchedule(String scheduleId) {
        return schedulerService.getSchedule(scheduleId);
    }

    public List<ReportSchedule> getAllSchedules() {
        return schedulerService.getAllSchedules();
    }

    public List<ReportSchedule> getActiveSchedules() {
        return schedulerService.getActiveSchedules();
    }

    public ReportSchedule pauseSchedule(String scheduleId) {
        return schedulerService.pauseSchedule(scheduleId);
    }

    public ReportSchedule resumeSchedule(String scheduleId) {
        return schedulerService.resumeSchedule(scheduleId);
    }

    public void deleteSchedule(String scheduleId) {
        schedulerService.deleteSchedule(scheduleId);
    }

    public ReportSchedule executeNow(String scheduleId) {
        return schedulerService.executeNow(scheduleId);
    }
}
