import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  Database, 
  Activity, 
  Clock, 
  HardDrive, 
  TrendingUp,
  AlertCircle,
} from 'lucide-react';
import { cn } from '../utils/cn';
import dashboardApi from '../services/api/dashboardApi';
import monitoringApi from '../services/api/monitoringApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Loader from '../components/ui/Loader';
import Badge from '../components/ui/Badge';

/**
 * Dashboard Page - Main dashboard with metrics and system health
 */
const Dashboard = () => {
  // Fetch dashboard metrics
  const { data: metrics, isLoading: metricsLoading, error: metricsError } = useQuery({
    queryKey: ['dashboard-metrics'],
    queryFn: dashboardApi.getDashboardMetrics,
    refetchInterval: 5000, // Refresh every 5 seconds
  });
  
  // Fetch system health
  const { data: health, isLoading: healthLoading } = useQuery({
    queryKey: ['system-health'],
    queryFn: dashboardApi.getSystemHealth,
    refetchInterval: 10000, // Refresh every 10 seconds
  });
  
  // Fetch Redis info
  const { data: redisInfo, isLoading: redisInfoLoading } = useQuery({
    queryKey: ['redis-info'],
    queryFn: monitoringApi.getRedisInfo,
    refetchInterval: 15000, // Refresh every 15 seconds
  });
  
  const isLoading = metricsLoading || healthLoading || redisInfoLoading;
  
  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader size="lg" text="Loading dashboard..." />
      </div>
    );
  }
  
  if (metricsError) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <AlertCircle className="w-12 h-12 text-danger-500 mx-auto mb-4" />
          <p className="text-gray-600 dark:text-gray-400">Failed to load dashboard data</p>
          <Button 
            variant="primary" 
            onClick={() => window.location.reload()}
            className="mt-4"
          >
            Retry
          </Button>
        </div>
      </div>
    );
  }
  
  const metricCards = [
    {
      title: 'Total Keys',
      value: metrics?.data?.totalKeys || dbSize?.data || 0,
      icon: Database,
      color: 'primary',
      change: '+12%',
    },
    {
      title: 'Cache Hit Rate',
      value: `${metrics?.data?.cacheHitRate || 0}%`,
      icon: Activity,
      color: 'success',
      change: '+5%',
    },
    {
      title: 'Avg Response Time',
      value: `${metrics?.data?.avgResponseTime || 0}ms`,
      icon: Clock,
      color: 'warning',
      change: '-8%',
    },
    {
      title: 'Memory Usage',
      value: `${redisInfo?.data?.used_memory_human || '0B'}`,
      icon: HardDrive,
      color: 'secondary',
      change: '+2%',
    },
  ];
  
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Dashboard</h1>
        <p className="page-description">
          Monitor your Redis instance and system performance
        </p>
      </div>
      
      {/* Metric Cards */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
      >
        {metricCards.map((card, index) => {
          const Icon = card.icon;
          const colorClasses = {
            primary: 'text-primary-600 bg-primary-100 dark:bg-primary-900/20',
            secondary: 'text-secondary-600 bg-secondary-100 dark:bg-secondary-900/20',
            success: 'text-success-600 bg-success-100 dark:bg-success-900/20',
            warning: 'text-warning-600 bg-warning-100 dark:bg-warning-900/20',
          };
          
          return (
            <motion.div
              key={card.title}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              <Card interactive>
                <CardBody>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <p className="text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                        {card.title}
                      </p>
                      <p className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                        {card.value}
                      </p>
                      <div className="flex items-center gap-1 mt-2">
                        <TrendingUp className="w-4 h-4 text-success-500" />
                        <span className="text-sm text-success-600 dark:text-success-400">
                          {card.change}
                        </span>
                        <span className="text-sm text-gray-500 dark:text-gray-400">
                          from last hour
                        </span>
                      </div>
                    </div>
                    <div className={cn('p-3 rounded-lg', colorClasses[card.color])}>
                      <Icon className="w-6 h-6" />
                    </div>
                  </div>
                </CardBody>
              </Card>
            </motion.div>
          );
        })}
      </motion.div>
      
      {/* System Status */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.4 }}
        className="grid grid-cols-1 lg:grid-cols-2 gap-6"
      >
        <Card>
          <CardHeader>
            <CardTitle>System Health</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Redis Status</span>
                <Badge variant={health?.data?.redis?.status === 'up' ? 'success' : 'danger'}>
                  {health?.data?.redis?.status || 'Unknown'}
                </Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Database Status</span>
                <Badge variant={health?.data?.database?.status === 'up' ? 'success' : 'danger'}>
                  {health?.data?.database?.status || 'Unknown'}
                </Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Uptime</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.uptime_in_days || 0} days
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Connected Clients</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.connected_clients || 0}
                </span>
              </div>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-3">
              <Button variant="primary" block className="justify-start">
                <Database className="w-4 h-4 mr-2" />
                Explore Redis Data
              </Button>
              <Button variant="secondary" block className="justify-start">
                <Activity className="w-4 h-4 mr-2" />
                View Performance Metrics
              </Button>
              <Button variant="outline-primary" block className="justify-start">
                <Clock className="w-4 h-4 mr-2" />
                Cache Management
              </Button>
            </div>
          </CardBody>
        </Card>
      </motion.div>
    </div>
  );
};

export default Dashboard;