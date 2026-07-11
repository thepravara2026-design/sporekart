import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet } from 'react-native';

interface CourseInfo {
  id: string;
  title: string;
  instructor: string;
  progress: number;
  duration: string;
}

/**
 * Grower App Training Dashboard Screen
 */
export const GrowerTrainingScreen: React.FC = () => {
  const [courses, setCourses] = useState<CourseInfo[]>([]);

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    // TODO: Load from API
    setCourses([
      { id: '1', title: 'Organic Farming 101', instructor: 'Dr. Kumar', progress: 75, duration: '4 weeks' },
      { id: '2', title: 'Soil Health Management', instructor: 'Sharma Sir', progress: 40, duration: '3 weeks' },
      { id: '3', title: 'Pest Management', instructor: 'Dr. Patel', progress: 0, duration: '2 weeks' },
    ]);
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.header}>My Training Courses</Text>
      
      {courses.map((course) => (
        <TouchableOpacity key={course.id} style={styles.courseCard}>
          <Text style={styles.courseName}>{course.title}</Text>
          <Text style={styles.instructor}>{course.instructor}</Text>
          <View style={styles.progressContainer}>
            <View style={[styles.progressBar, { width: `${course.progress}%` }]} />
          </View>
          <Text style={styles.progressText}>{course.progress}% Complete</Text>
          <TouchableOpacity style={styles.continueButton}>
            <Text style={styles.buttonText}>Continue Learning</Text>
          </TouchableOpacity>
        </TouchableOpacity>
      ))}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    paddingHorizontal: 16,
  },
  header: {
    fontSize: 24,
    fontWeight: 'bold',
    marginVertical: 16,
    color: '#333',
  },
  courseCard: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    marginBottom: 12,
  },
  courseName: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  instructor: {
    fontSize: 12,
    color: '#666',
    marginBottom: 12,
  },
  progressContainer: {
    height: 8,
    backgroundColor: '#e0e0e0',
    borderRadius: 4,
    overflow: 'hidden',
    marginBottom: 8,
  },
  progressBar: {
    height: '100%',
    backgroundColor: '#2ecc71',
  },
  progressText: {
    fontSize: 12,
    color: '#666',
    marginBottom: 12,
  },
  continueButton: {
    backgroundColor: '#3498db',
    paddingVertical: 10,
    borderRadius: 6,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontWeight: '600',
  },
});
