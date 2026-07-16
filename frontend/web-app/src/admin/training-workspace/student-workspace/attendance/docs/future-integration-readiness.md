# Attendance Platform — Future Integration Readiness

All interfaces prepared. None implemented.

## Integration Points

| Platform | Interface | Trigger |
|----------|-----------|---------|
| QR Attendance | `AttendanceStatus` mapping | Student scans QR → status recorded |
| Face Recognition | `studentId` + `trainingDate` | Biometric match → attendance marked |
| GPS Attendance | `BatchSlot` + location data | Geo-fence check-in → present |
| NFC/RFID | `attendanceId` | Card tap → check-in/check-out |
| Biometric Devices | `studentId` + device ID | Fingerprint/iris → attendance |
| AI Monitoring | `AttendanceSummary.lowAttendanceAlerts` | Predictive alerts for at-risk students |
| Communication | `AttendanceStatus` transitions | Absent → SMS/Email/WhatsApp notification |
| Analytics | `AttendanceSummary` | Cross-module attendance analytics |
| Assessment | `attendancePercent >= minThreshold` | Assessment eligibility check |
| Certificate | `attendancePercent >= minThreshold` | Certificate eligibility gate |
| Learning Progress | `AttendanceRecord` per session | Progress tracking per course |
| Corporate HRMS | `AttendanceSummary` per batch | Corporate compliance reporting |
| Government Compliance | `AttendanceSummary` per program | Govt scheme attendance audit |
| ERP | `AttendanceSummary` | Enterprise reporting |
| CRM | `AttendanceStatus` changes | Customer lifecycle events |
