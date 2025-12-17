package com.example.gestiondepresence.helpers;

import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsHelper {

    public static class StudentStats {
        public String studentName;
        public int totalPresent;
        public int totalAbsent;
        public int totalRetard;
        public int totalSessions;
        public double attendanceRate;

        public StudentStats(String name) {
            this.studentName = name;
            this.totalPresent = 0;
            this.totalAbsent = 0;
            this.totalRetard = 0;
            this.totalSessions = 0;
            this.attendanceRate = 0.0;
        }

        public void calculate() {
            if (totalSessions > 0) {
                attendanceRate = (totalPresent * 100.0) / totalSessions;
            }
        }
    }

    public static Map<Integer, StudentStats> calculateStatistics(
            List<Student> students,
            List<Attendance> attendances) {

        Map<Integer, StudentStats> statsMap = new HashMap<>();

        // Initialize stats for each student
        for (Student student : students) {
            statsMap.put(student.getId(),
                    new StudentStats(student.getName() + " " + student.getSurname()));
        }

        // Count attendance records
        for (Attendance attendance : attendances) {
            StudentStats stats = statsMap.get(attendance.getStudentId());
            if (stats != null) {
                stats.totalSessions++;

                switch (attendance.getStatus()) {
                    case "present":
                        stats.totalPresent++;
                        break;
                    case "absent":
                        stats.totalAbsent++;
                        break;
                    case "retard":
                        stats.totalRetard++;
                        break;
                }
            }
        }

        // Calculate rates
        for (StudentStats stats : statsMap.values()) {
            stats.calculate();
        }

        return statsMap;
    }
}