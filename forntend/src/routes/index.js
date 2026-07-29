/**
 * Application Routes
 * Centralized route configuration for React Router
 */

import { APP_ROUTES } from '../constants';

// Route configuration with lazy loading
export const ROUTES = [
  {
    path: APP_ROUTES.HOME,
    element: () => import('../pages/Dashboard.jsx'),
    label: 'Dashboard',
    icon: 'LayoutDashboard',
    showInNav: true,
  },
  {
    path: APP_ROUTES.DASHBOARD,
    element: () => import('../pages/Dashboard.jsx'),
    label: 'Dashboard',
    icon: 'LayoutDashboard',
    showInNav: false, // Redirects to home
  },
  {
    path: APP_ROUTES.CACHE_PLAYGROUND,
    element: () => import('../pages/CachePlayground.jsx'),
    label: 'Cache Playground',
    icon: 'Play',
    showInNav: true,
  },
  {
    path: APP_ROUTES.REDIS_EXPLORER,
    element: () => import('../pages/RedisExplorer.jsx'),
    label: 'Redis Explorer',
    icon: 'Database',
    showInNav: true,
  },
  {
    path: APP_ROUTES.PERFORMANCE,
    element: () => import('../pages/Performance.jsx'),
    label: 'Performance',
    icon: 'Gauge',
    showInNav: true,
  },
  {
    path: APP_ROUTES.METRICS,
    element: () => import('../pages/Metrics.jsx'),
    label: 'Metrics',
    icon: 'BarChart3',
    showInNav: true,
  },
  {
    path: APP_ROUTES.CONFIGURATION,
    element: () => import('../pages/Configuration.jsx'),
    label: 'Configuration',
    icon: 'Settings',
    showInNav: true,
  },
  // Redis operations routes
  {
    path: APP_ROUTES.STRING_OPERATIONS,
    element: () => import('../pages/StringOperations.jsx'),
    label: 'String Operations',
    icon: 'Type',
    showInNav: true,
    category: 'Redis Operations',
  },
  {
    path: APP_ROUTES.HASH_OPERATIONS,
    element: () => import('../pages/HashOperations.jsx'),
    label: 'Hash Operations',
    icon: 'Hash',
    showInNav: true,
    category: 'Redis Operations',
  },
  {
    path: APP_ROUTES.LIST_OPERATIONS,
    element: () => import('../pages/ListOperations.jsx'),
    label: 'List Operations',
    icon: 'List',
    showInNav: true,
    category: 'Redis Operations',
  },
  {
    path: APP_ROUTES.SET_OPERATIONS,
    element: () => import('../pages/SetOperations.jsx'),
    label: 'Set Operations',
    icon: 'Layers',
    showInNav: true,
    category: 'Redis Operations',
  },
  {
    path: APP_ROUTES.SORTED_SET_OPERATIONS,
    element: () => import('../pages/SortedSetOperations.jsx'),
    label: 'Sorted Set Operations',
    icon: 'ArrowUpDown',
    showInNav: true,
    category: 'Redis Operations',
  },
  {
    path: APP_ROUTES.COUNTER_OPERATIONS,
    element: () => import('../pages/CounterOperations.jsx'),
    label: 'Counter Operations',
    icon: 'PlusMinus',
    showInNav: true,
    category: 'Redis Operations',
  },
  // Monitoring routes
  {
    path: APP_ROUTES.REDIS_MONITOR,
    element: () => import('../pages/RedisMonitor.jsx'),
    label: 'Redis Monitor',
    icon: 'Activity',
    showInNav: true,
    category: 'Monitoring',
  },
  {
    path: APP_ROUTES.CACHE_MONITOR,
    element: () => import('../pages/CacheMonitor.jsx'),
    label: 'Cache Monitor',
    icon: 'RefreshCw',
    showInNav: true,
    category: 'Monitoring',
  },
  {
    path: APP_ROUTES.API_MONITOR,
    element: () => import('../pages/ApiMonitor.jsx'),
    label: 'API Monitor',
    icon: 'Network',
    showInNav: true,
    category: 'Monitoring',
  },
  // Settings routes
  {
    path: APP_ROUTES.SETTINGS,
    element: () => import('../pages/Settings.jsx'),
    label: 'Settings',
    icon: 'Settings',
    showInNav: true,
    category: 'Settings',
  },
  {
    path: APP_ROUTES.SETTINGS_THEME,
    element: () => import('../pages/SettingsTheme.jsx'),
    label: 'Theme Settings',
    icon: 'Palette',
    showInNav: false,
    category: 'Settings',
  },
  {
    path: APP_ROUTES.SETTINGS_API,
    element: () => import('../pages/SettingsApi.jsx'),
    label: 'API Settings',
    icon: 'Server',
    showInNav: false,
    category: 'Settings',
  },
  {
    path: APP_ROUTES.SETTINGS_CACHE,
    element: () => import('../pages/SettingsCache.jsx'),
    label: 'Cache Settings',
    icon: 'HardDrive',
    showInNav: false,
    category: 'Settings',
  },
  // Error routes
  {
    path: APP_ROUTES.NOT_FOUND,
    element: () => import('../pages/NotFound.jsx'),
    label: 'Not Found',
    showInNav: false,
  },
  {
    path: APP_ROUTES.ERROR,
    element: () => import('../pages/Error.jsx'),
    label: 'Error',
    showInNav: false,
  },
];

// Navigation routes (filtered for sidebar/navbar)
export const NAV_ROUTES = ROUTES.filter(route => route.showInNav);

// Route categories for organized navigation
export const ROUTE_CATEGORIES = {
  MAIN: 'Main',
  'REDIS_OPERATIONS': 'Redis Operations',
  MONITORING: 'Monitoring',
  SETTINGS: 'Settings',
};

// Get routes by category
export function getRoutesByCategory(category) {
  return ROUTES.filter(route => route.category === category);
}

// Get route by path
export function getRouteByPath(path) {
  return ROUTES.find(route => route.path === path);
}

// Get breadcrumb items for a path
export function getBreadcrumbs(path) {
  const breadcrumbs = [];
  const segments = path.split('/').filter(Boolean);
  
  let currentPath = '';
  segments.forEach((segment, index) => {
    currentPath += `/${segment}`;
    const route = getRouteByPath(currentPath);
    if (route) {
      breadcrumbs.push({
        path: currentPath,
        label: route.label,
      });
    }
  });
  
  return breadcrumbs;
}

// Default route
export const DEFAULT_ROUTE = APP_ROUTES.HOME;

// Protected routes (require authentication - future implementation)
export const PROTECTED_ROUTES = [
  APP_ROUTES.SETTINGS,
  APP_ROUTES.SETTINGS_THEME,
  APP_ROUTES.SETTINGS_API,
  APP_ROUTES.SETTINGS_CACHE,
  APP_ROUTES.CONFIGURATION,
];

// Public routes
export const PUBLIC_ROUTES = [
  APP_ROUTES.HOME,
  APP_ROUTES.DASHBOARD,
  APP_ROUTES.CACHE_PLAYGROUND,
  APP_ROUTES.REDIS_EXPLORER,
  APP_ROUTES.PERFORMANCE,
  APP_ROUTES.METRICS,
  APP_ROUTES.STRING_OPERATIONS,
  APP_ROUTES.HASH_OPERATIONS,
  APP_ROUTES.LIST_OPERATIONS,
  APP_ROUTES.SET_OPERATIONS,
  APP_ROUTES.SORTED_SET_OPERATIONS,
  APP_ROUTES.COUNTER_OPERATIONS,
  APP_ROUTES.REDIS_MONITOR,
  APP_ROUTES.CACHE_MONITOR,
  APP_ROUTES.API_MONITOR,
];