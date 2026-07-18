export interface Course {
  id: string;
  title: string;
  instructor: string;
  duration: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  enrolled: number;
  rating: number;
}

export const courses: Course[] = [
  { id: 'CRS-001', title: 'Organic Farming Fundamentals', instructor: 'Dr. Mehta', duration: '4 weeks', level: 'beginner', enrolled: 234, rating: 4.5 },
  { id: 'CRS-002', title: 'Advanced Irrigation Techniques', instructor: 'Prof. Singh', duration: '6 weeks', level: 'advanced', enrolled: 89, rating: 4.8 },
  { id: 'CRS-003', title: 'Soil Health Management', instructor: 'Dr. Gupta', duration: '3 weeks', level: 'intermediate', enrolled: 156, rating: 4.3 },
  { id: 'CRS-004', title: 'Pest Control & Management', instructor: 'Dr. Kumar', duration: '5 weeks', level: 'intermediate', enrolled: 198, rating: 4.6 },
];

export interface Student {
  id: string;
  name: string;
  courseId: string;
  progress: number;
  status: 'active' | 'completed' | 'dropped';
}

export const students: Student[] = [
  { id: 'STU-001', name: 'Ravi Sharma', courseId: 'CRS-001', progress: 75, status: 'active' },
  { id: 'STU-002', name: 'Priya Patel', courseId: 'CRS-003', progress: 100, status: 'completed' },
  { id: 'STU-003', name: 'Amit Singh', courseId: 'CRS-002', progress: 30, status: 'active' },
  { id: 'STU-004', name: 'Sunita Verma', courseId: 'CRS-004', progress: 60, status: 'active' },
];
