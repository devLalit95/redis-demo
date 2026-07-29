/**
 * Application Constants
 * Centralized constants for the entire application
 */

// Redis data types
export const REDIS_DATA_TYPES = {
  STRING: 'string',
  HASH: 'hash',
  LIST: 'list',
  SET: 'set',
  SORTED_SET: 'zset',
  JSON: 'json',
  STREAM: 'stream',
  BITMAP: 'bitmap',
  HYPERLOGLOG: 'hyperloglog',
  GEO: 'geo',
};

// Redis data type labels
export const REDIS_DATA_TYPE_LABELS = {
  [REDIS_DATA_TYPES.STRING]: 'String',
  [REDIS_DATA_TYPES.HASH]: 'Hash',
  [REDIS_DATA_TYPES.LIST]: 'List',
  [REDIS_DATA_TYPES.SET]: 'Set',
  [REDIS_DATA_TYPES.SORTED_SET]: 'Sorted Set',
  [REDIS_DATA_TYPES.JSON]: 'JSON',
  [REDIS_DATA_TYPES.STREAM]: 'Stream',
  [REDIS_DATA_TYPES.BITMAP]: 'Bitmap',
  [REDIS_DATA_TYPES.HYPERLOGLOG]: 'HyperLogLog',
  [REDIS_DATA_TYPES.GEO]: 'Geospatial',
};

// Cache status
export const CACHE_STATUS = {
  HIT: 'hit',
  MISS: 'miss',
  EXPIRED: 'expired',
  EVICTED: 'evicted',
  PENDING: 'pending',
  ERROR: 'error',
};

// Cache status labels
export const CACHE_STATUS_LABELS = {
  [CACHE_STATUS.HIT]: 'Cache Hit',
  [CACHE_STATUS.MISS]: 'Cache Miss',
  [CACHE_STATUS.EXPIRED]: 'Expired',
  [CACHE_STATUS.EVICTED]: 'Evicted',
  [CACHE_STATUS.PENDING]: 'Pending',
  [CACHE_STATUS.ERROR]: 'Error',
};

// Cache status colors
export const CACHE_STATUS_COLORS = {
  [CACHE_STATUS.HIT]: '#22c55e',
  [CACHE_STATUS.MISS]: '#f59e0b',
  [CACHE_STATUS.EXPIRED]: '#ef4444',
  [CACHE_STATUS.EVICTED]: '#ef4444',
  [CACHE_STATUS.PENDING]: '#0ea5e9',
  [CACHE_STATUS.ERROR]: '#ef4444',
};

// API endpoints
export const API_ENDPOINTS = {
  // Student operations
  STUDENT: {
    GET_ALL: '/api/students',
    GET_BY_ID: '/api/students/:id',
    CREATE: '/api/students',
    UPDATE: '/api/students/:id',
    DELETE: '/api/students/:id',
    BULK_DELETE: '/api/students/bulk',
  },
  
  // Cache operations
  CACHE: {
    GET: '/api/cache/:key',
    SET: '/api/cache',
    DELETE: '/api/cache/:key',
    CLEAR: '/api/cache/clear',
    REFRESH: '/api/cache/refresh',
    EXPIRE: '/api/cache/:key/expire',
    STATS: '/api/cache/stats',
  },
  
  // Redis operations
  REDIS: {
    INFO: '/api/redis-explorer/info',
    KEYS: '/api/redis-explorer/keys',
    GET: '/api/redis-explorer/keys/:key/value',
    SET: '/api/redis-explorer/keys/:key',
    DELETE: '/api/redis-explorer/keys/:key',
    FLUSHDB: '/api/redis-explorer/flush',
    FLUSHALL: '/api/redis/monitoring/flushall',
    STRING: '/api/cache-playground/string',
    HASH: '/api/cache-playground/hash',
    LIST: '/api/cache-playground/list',
    SET: '/api/cache-playground/set',
    SORTED_SET: '/api/cache-playground/sortedset',
    COUNTER: '/api/cache-playground/counter',
  },
  
  // Performance operations
  PERFORMANCE: {
    BENCHMARK: '/api/performance/benchmark',
    COMPARE: '/api/performance/compare',
    METRICS: '/api/performance/metrics',
    HISTORY: '/api/performance/history',
  },
  
  // Metrics operations
  METRICS: {
    DASHBOARD: '/api/metrics/dashboard',
    REDIS: '/api/metrics/redis',
    CACHE: '/api/metrics/cache',
    API: '/api/metrics/api',
  },
  
  // Dashboard operations
  DASHBOARD: {
    OVERVIEW: '/api/dashboard/overview',
    STATISTICS: '/api/dashboard/statistics',
    RECENT: '/api/dashboard/recent',
  },
  
  // Configuration operations
  CONFIG: {
    GET: '/api/config',
    UPDATE: '/api/config',
    RESET: '/api/config/reset',
  },
  
  // Health operations
  HEALTH: {
    CHECK: '/api/health',
    DEEP_CHECK: '/api/health/deep',
    REDIS: '/api/health/redis',
    DATABASE: '/api/health/database',
  },
};

// Application routes
export const APP_ROUTES = {
  HOME: '/',
  DASHBOARD: '/dashboard',
  CACHE_PLAYGROUND: '/cache-playground',
  REDIS_EXPLORER: '/redis-explorer',
  PERFORMANCE: '/performance',
  METRICS: '/metrics',
  CONFIGURATION: '/configuration',
  
  // Redis operations
  STRING_OPERATIONS: '/redis/string',
  HASH_OPERATIONS: '/redis/hash',
  LIST_OPERATIONS: '/redis/list',
  SET_OPERATIONS: '/redis/set',
  SORTED_SET_OPERATIONS: '/redis/sorted-set',
  COUNTER_OPERATIONS: '/redis/counter',
  
  // Monitoring
  REDIS_MONITOR: '/monitoring/redis',
  CACHE_MONITOR: '/monitoring/cache',
  API_MONITOR: '/monitoring/api',
  
  // Settings
  SETTINGS: '/settings',
  SETTINGS_THEME: '/settings/theme',
  SETTINGS_API: '/settings/api',
  SETTINGS_CACHE: '/settings/cache',
  
  // Error pages
  NOT_FOUND: '/404',
  ERROR: '/error',
};

// HTTP methods
export const HTTP_METHODS = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  PATCH: 'PATCH',
  DELETE: 'DELETE',
  HEAD: 'HEAD',
  OPTIONS: 'OPTIONS',
};

// HTTP status codes
export const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  METHOD_NOT_ALLOWED: 405,
  CONFLICT: 409,
  UNPROCESSABLE_ENTITY: 422,
  INTERNAL_SERVER_ERROR: 500,
  BAD_GATEWAY: 502,
  SERVICE_UNAVAILABLE: 503,
  GATEWAY_TIMEOUT: 504,
};

// Sort directions
export const SORT_DIRECTION = {
  ASC: 'asc',
  DESC: 'desc',
};

// Pagination defaults
export const PAGINATION = {
  DEFAULT_PAGE: 1,
  DEFAULT_PAGE_SIZE: 10,
  PAGE_SIZE_OPTIONS: [5, 10, 25, 50, 100],
};

// Theme options
export const THEME = {
  LIGHT: 'light',
  DARK: 'dark',
  AUTO: 'auto',
};

// Time units
export const TIME_UNITS = {
  SECOND: 'second',
  MINUTE: 'minute',
  HOUR: 'hour',
  DAY: 'day',
  WEEK: 'week',
  MONTH: 'month',
  YEAR: 'year',
};

// Time unit conversions (in seconds)
export const TIME_UNIT_SECONDS = {
  [TIME_UNITS.SECOND]: 1,
  [TIME_UNITS.MINUTE]: 60,
  [TIME_UNITS.HOUR]: 3600,
  [TIME_UNITS.DAY]: 86400,
  [TIME_UNITS.WEEK]: 604800,
  [TIME_UNITS.MONTH]: 2592000,
  [TIME_UNITS.YEAR]: 31536000,
};

// Chart types
export const CHART_TYPES = {
  LINE: 'line',
  BAR: 'bar',
  PIE: 'pie',
  AREA: 'area',
  SCATTER: 'scatter',
};

// Chart colors
export const CHART_COLORS = {
  PRIMARY: '#0ea5e9',
  SECONDARY: '#a855f7',
  SUCCESS: '#22c55e',
  DANGER: '#ef4444',
  WARNING: '#f59e0b',
  INFO: '#3b82f6',
  GRAY: '#6b7280',
};

// Validation patterns
export const VALIDATION_PATTERNS = {
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  URL: /^https?:\/\/.+/,
  NUMBER: /^\d+$/,
  DECIMAL: /^\d+\.?\d*$/,
  REDIS_KEY: /^[a-zA-Z0-9_\-:.]+$/,
  IP_ADDRESS: /^(?:[0-9]{1,3}\.){3}[0-9]{1,3}$/,
};

// Local storage keys
export const STORAGE_KEYS = {
  THEME: 'redis-dashboard-theme',
  SIDEBAR: 'redis-dashboard-sidebar',
  PREFERENCES: 'redis-dashboard-preferences',
  CACHE: 'redis-dashboard-cache',
  TOKEN: 'redis-dashboard-token',
};

// Event names
export const EVENTS = {
  THEME_CHANGE: 'theme-change',
  SIDEBAR_TOGGLE: 'sidebar-toggle',
  CACHE_UPDATE: 'cache-update',
  API_ERROR: 'api-error',
  DATA_REFRESH: 'data-refresh',
};

// Debounce delays (in milliseconds)
export const DEBOUNCE_DELAYS = {
  SEARCH: 300,
  INPUT: 500,
  RESIZE: 200,
  SCROLL: 100,
  API_CALL: 1000,
};

// Animation durations (in milliseconds)
export const ANIMATION_DURATIONS = {
  FAST: 150,
  NORMAL: 200,
  SLOW: 300,
  VERY_SLOW: 500,
};

// Breakpoints (in pixels)
export const BREAKPOINTS = {
  XS: 0,
  SM: 640,
  MD: 768,
  LG: 1024,
  XL: 1280,
  '2XL': 1536,
};

// Z-index layers
export const Z_INDEX = {
  DROPDOWN: 1000,
  STICKY: 1020,
  FIXED: 1030,
  MODAL_BACKDROP: 1040,
  MODAL: 1050,
  POPOVER: 1060,
  TOOLTIP: 1070,
  NOTIFICATION: 1080,
};