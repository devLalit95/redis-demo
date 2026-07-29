/**
 * Application Configuration
 * Centralized configuration for the entire application
 */

import API_CONFIG from './api.js';

const APP_CONFIG = {
  // Application metadata
  APP_NAME: 'Redis Learning Dashboard',
  APP_VERSION: '1.0.0',
  APP_DESCRIPTION: 'Interactive Redis learning platform with visual demonstrations',
  
  // API configuration
  API: API_CONFIG,
  
  // Application settings
  SETTINGS: {
    // Theme
    DEFAULT_THEME: 'light',
    THEME_STORAGE_KEY: 'redis-dashboard-theme',
    
    // Language
    DEFAULT_LANGUAGE: 'en',
    LANGUAGE_STORAGE_KEY: 'redis-dashboard-language',
    
    // Pagination
    DEFAULT_PAGE_SIZE: 10,
    PAGE_SIZE_OPTIONS: [5, 10, 25, 50, 100],
    
    // Date format
    DEFAULT_DATE_FORMAT: 'short',
    
    // Timezone
    DEFAULT_TIMEZONE: 'UTC',
  },
  
  // Cache configuration
  CACHE: {
    // React Query cache settings
    STALE_TIME: 5 * 60 * 1000, // 5 minutes
    CACHE_TIME: 10 * 60 * 1000, // 10 minutes
    RETRY_COUNT: 3,
    RETRY_DELAY: 1000,
    
    // Local storage cache
    LOCAL_CACHE_PREFIX: 'redis-dashboard-cache-',
    LOCAL_CACHE_TTL: 24 * 60 * 60 * 1000, // 24 hours
  },
  
  // Performance monitoring
  PERFORMANCE: {
    // Enable performance monitoring
    ENABLED: true,
    
    // Sample rate (0-1)
    SAMPLE_RATE: 1.0,
    
    // Thresholds
    SLOW_API_THRESHOLD: 1000, // 1 second
    VERY_SLOW_API_THRESHOLD: 3000, // 3 seconds
  },
  
  // Feature flags
  FEATURES: {
    // Enable dark mode
    DARK_MODE: true,
    
    // Enable animations
    ANIMATIONS: true,
    
    // Enable charts
    CHARTS: true,
    
    // Enable real-time updates
    REAL_TIME: true,
    
    // Enable experimental features
    EXPERIMENTAL: false,
  },
  
  // UI configuration
  UI: {
    // Sidebar
    SIDEBAR_WIDTH: 256,
    SIDEBAR_COLLAPSED_WIDTH: 64,
    SIDEBAR_STORAGE_KEY: 'redis-dashboard-sidebar',
    
    // Navbar
    NAVBAR_HEIGHT: 64,
    
    // Toast notifications
    TOAST_DURATION: 3000,
    TOAST_POSITION: 'top-right',
    
    // Modal
    MODAL_ANIMATION_DURATION: 200,
    
    // Charts
    CHART_ANIMATION_DURATION: 500,
    CHART_COLORS: {
      primary: '#0ea5e9',
      secondary: '#a855f7',
      success: '#22c55e',
      danger: '#ef4444',
      warning: '#f59e0b',
    },
  },
  
  // Validation rules
  VALIDATION: {
    // Redis key
    REDIS_KEY: {
      MIN_LENGTH: 1,
      MAX_LENGTH: 250,
      PATTERN: /^[a-zA-Z0-9_\-:.]+$/,
    },
    
    // Redis value
    REDIS_VALUE: {
      MAX_SIZE: 512 * 1024 * 1024, // 512MB
    },
    
    // TTL
    TTL: {
      MIN: 0,
      MAX: 365 * 24 * 60 * 60, // 1 year in seconds
    },
  },
  
  // Error messages
  ERROR_MESSAGES: {
    NETWORK_ERROR: 'Network error. Please check your connection.',
    SERVER_ERROR: 'Server error. Please try again later.',
    NOT_FOUND: 'Resource not found.',
    UNAUTHORIZED: 'Unauthorized access.',
    FORBIDDEN: 'Access forbidden.',
    VALIDATION_ERROR: 'Validation error. Please check your input.',
    TIMEOUT_ERROR: 'Request timeout. Please try again.',
    UNKNOWN_ERROR: 'An unknown error occurred.',
  },
  
  // Success messages
  SUCCESS_MESSAGES: {
    CREATED: 'Created successfully.',
    UPDATED: 'Updated successfully.',
    DELETED: 'Deleted successfully.',
    COPIED: 'Copied to clipboard.',
    SAVED: 'Saved successfully.',
  },
};

export default APP_CONFIG;