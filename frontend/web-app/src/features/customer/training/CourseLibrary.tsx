import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { INITIAL_COURSES } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

export const CourseLibrary: React.FC = () => {
  const navigate = useNavigate();
  const [courses] = useState(INITIAL_COURSES);
  
  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('all');
  const [levelFilter, setLevelFilter] = useState('all');
  const [sortBy, setSortBy] = useState('rating');

  // Filter & Sort logic
  const filteredCourses = courses.filter((c) => {
    const matchesSearch = c.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          c.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = categoryFilter === 'all' || c.category === categoryFilter;
    const matchesLevel = levelFilter === 'all' || c.difficulty === levelFilter;
    return matchesSearch && matchesCategory && matchesLevel;
  }).sort((a, b) => {
    if (sortBy === 'duration') return b.duration.localeCompare(a.duration); // basic compare
    return b.rating - a.rating;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Course Catalog
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Explore professional mycology courses. Learn from lab scientists and master growers.
        </p>
      </div>

      {/* Filters Bar */}
      <div 
        style={{ 
          display: 'flex', 
          justifyContent: 'space-between', 
          alignItems: 'center', 
          gap: '16px', 
          flexWrap: 'wrap',
          background: '#f8fafc',
          padding: '16px',
          borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)'
        }}
      >
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', flex: 1, minWidth: '280px' }}>
          {/* Search box */}
          <div style={{ position: 'relative', flex: 1, minWidth: '180px' }}>
            <span style={{ position: 'absolute', left: '10px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
              <Icon name="search" size={14} color="currentColor" />
            </span>
            <input
              type="text"
              placeholder="Search courses..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{
                width: '100%',
                padding: '6px 12px 6px 32px',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-md)',
                fontSize: 'var(--text-body-sm)',
                outline: 'none',
                background: 'var(--color-bg-surface-default)',
                color: 'var(--color-text-primary)'
              }}
            />
          </div>

          {/* Category filter */}
          <select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            style={{
              padding: '6px 12px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)',
              fontSize: 'var(--text-body-sm)',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-primary)',
              outline: 'none',
            }}
          >
            <option value="all">All Categories</option>
            <option value="Lab Work">Lab Work</option>
            <option value="Cultivation">Cultivation</option>
            <option value="Commercial">Commercial</option>
          </select>

          {/* Difficulty filter */}
          <select
            value={levelFilter}
            onChange={(e) => setLevelFilter(e.target.value)}
            style={{
              padding: '6px 12px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)',
              fontSize: 'var(--text-body-sm)',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-primary)',
              outline: 'none',
            }}
          >
            <option value="all">All Difficulty Levels</option>
            <option value="Beginner">Beginner</option>
            <option value="Intermediate">Intermediate</option>
            <option value="Advanced">Advanced</option>
          </select>
        </div>

        {/* Sort by */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Sort by</span>
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            style={{
              padding: '6px 12px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)',
              fontSize: 'var(--text-body-sm)',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-primary)',
              outline: 'none',
            }}
          >
            <option value="rating">Rating</option>
            <option value="duration">Duration</option>
          </select>
        </div>
      </div>

      {/* Grid List */}
      {filteredCourses.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>📚</div>
          <div>
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No courses match filters</h3>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Try adjusting your search queries.</p>
          </div>
        </div>
      ) : (
        <Grid columns="repeat(auto-fill, minmax(320px, 1fr))" gap="24px">
          {filteredCourses.map((c) => (
            <Card 
              key={c.id} 
              variant="outlined" 
              padding="none"
              onClick={() => navigate(`/dashboard/training/course/${c.id}`)}
              style={{ 
                cursor: 'pointer', 
                display: 'flex', 
                flexDirection: 'column', 
                height: '100%', 
                justifyContent: 'space-between',
                overflow: 'hidden'
              }}
            >
              {/* Banner Top */}
              <div 
                style={{ 
                  background: c.bannerGradient, 
                  height: '100px', 
                  padding: '16px', 
                  display: 'flex', 
                  alignItems: 'flex-end', 
                  position: 'relative' 
                }}
              >
                <span 
                  style={{ 
                    position: 'absolute', 
                    top: '12px', 
                    left: '12px', 
                    background: 'rgba(255,255,255,0.2)', 
                    color: '#fff', 
                    backdropFilter: 'blur(4px)',
                    padding: '2px 8px', 
                    borderRadius: 'var(--radius-sm)', 
                    fontSize: '10px', 
                    fontWeight: 'bold' 
                  }}
                >
                  {c.category}
                </span>

                <h3 style={{ color: '#fff', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', margin: 0, textShadow: '0 2px 4px rgba(0,0,0,0.2)' }}>
                  {c.title}
                </h3>
              </div>

              {/* Body */}
              <div style={{ padding: '16px', flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                
                <div>
                  <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '0 0 12px', lineHeight: '1.4', minHeight: '48px', display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                    {c.description}
                  </p>

                  <div style={{ display: 'flex', gap: '16px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginBottom: '16px', flexWrap: 'wrap' }}>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                      <Icon name="clock" size={12} color="currentColor" />
                      {c.duration}
                    </span>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                      <Icon name="award" size={12} color="currentColor" />
                      {c.difficulty}
                    </span>
                  </div>
                </div>

                {/* Bottom enrollment bar */}
                <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '12px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <span style={{ color: 'var(--color-text-warning, #eab308)', display: 'flex' }}><Icon name="star" size={12} color="currentColor" /></span>
                    <strong style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-primary)' }}>{c.rating}</strong>
                    <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)' }}>({c.reviewsCount})</span>
                  </div>

                  <div>
                    {c.enrolled ? (
                      c.progress === 100 ? (
                        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-success, #166534)', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '4px' }}>
                          ✓ Completed
                        </span>
                      ) : (
                        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-primary)', fontWeight: 'bold' }}>
                          In Progress ({c.progress}%)
                        </span>
                      )
                    ) : (
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', fontWeight: 'bold' }}>
                        Enroll Now &rarr;
                      </span>
                    )}
                  </div>
                </div>

              </div>
            </Card>
          ))}
        </Grid>
      )}

    </div>
  );
};
export default CourseLibrary;
