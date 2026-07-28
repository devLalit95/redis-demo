import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import {
  Activity,
  HardDrive,
  Cpu,
  Clock,
  Server,
  Users,
  Database,
  RefreshCw,
  AlertTriangle,
} from 'lucide-react';
import { cn } from '../utils/cn';
import monitoringApi from '../services/api/monitoringApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Badge from '../components/ui/Badge';
import Loader from '../components/ui/Loader';
import Progress from '../components/ui/Progress';
import { Heading, Text } from '../components/ui/Typography';
import toast from 'react-hot-toast';

/**
 * Redis Monitor Page - Real-time Redis monitoring
 */
const RedisMonitor = () => {
  // Fetch Redis info
  const { data: redisInfo, isLoading: redisInfoLoading, refetch: refetchRedisInfo } = useQuery({
    queryKey: ['redis-info'],
    queryFn: monitoringApi.getRedisInfo,
    refetchInterval: 5000, // Refresh every 5 seconds
  });
  
  // Fetch memory info
  const { data: memoryInfo, isLoading: memoryLoading } = useQuery({
    queryKey: ['redis-memory'],
    queryFn: monitoringApi.getRedisMemoryInfo,
    refetchInterval: 5000,
  });
  
  // Fetch database size
  const { data: dbSize, isLoading: dbSizeLoading } = useQuery({
    queryKey: ['redis-db-size'],
    queryFn: monitoringApi.getDatabaseSize,
    refetchInterval: 5000,
  });
  
  // Ping Redis
  const { data: pingResult, isLoading: pingLoading } = useQuery({
    queryKey: ['redis-ping'],
    queryFn: monitoringApi.pingRedis,
    refetchInterval: 10000, // Refresh every 10 seconds
  });
  
  const isLoading = redisInfoLoading || memoryLoading || dbSizeLoading;
  
  const handleRefresh = () => {
    refetchRedisInfo();
    toast.success('Refreshed monitoring data');
  };
  
  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader size="lg" text="Loading monitoring data..." />
      </div>
    );
  }
  
  const memoryPercentage = memoryInfo?.data?.usedMemoryPercentage || 0;
  const isMemoryHigh = memoryPercentage > 80;
  
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <div className="flex items-center justify-between">
          <div>
            <Heading level={1}>Redis Monitor</Heading>
            <Text>Real-time Redis server monitoring and statistics</Text>
          </div>
          <Button
            variant="outline-primary"
            onClick={handleRefresh}
            leftIcon={<RefreshCw className="w-4 h-4" />}
          >
            Refresh
          </Button>
        </div>
      </div>
      
      {/* Connection Status */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center gap-4 p-4 bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700"
      >
        <div className="flex items-center gap-3">
          <div className={cn(
            'w-3 h-3 rounded-full',
            pingResult?.data === 'PONG' ? 'bg-success-500 animate-pulse' : 'bg-danger-500'
          )} />
          <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
            {pingResult?.data === 'PONG' ? 'Connected' : 'Disconnected'}
          </span>
        </div>
        
        <div className="h-6 w-px bg-gray-200 dark:bg-gray-700" />
        
        <div className="flex items-center gap-2">
          <Server className="w-4 h-4 text-gray-500" />
          <span className="text-sm text-gray-600 dark:text-gray-400">
            {redisInfo?.data?.redis_version || 'Unknown'}
          </span>
        </div>
        
        <div className="flex-1" />
        
        <Badge variant={isMemoryHigh ? 'danger' : 'success'} dot>
          {isMemoryHigh ? 'High Memory Usage' : 'Normal'}
        </Badge>
      </motion.div>
      
      {/* Main Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
        >
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-2 bg-primary-100 dark:bg-primary-900/20 rounded-lg">
                  <Database className="w-5 h-5 text-primary-600 dark:text-primary-400" />
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Total Keys</p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                    {dbSize?.data || 0}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </motion.div>
        
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
        >
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-2 bg-secondary-100 dark:bg-secondary-900/20 rounded-lg">
                  <Users className="w-5 h-5 text-secondary-600 dark:text-secondary-400" />
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Connected Clients</p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                    {redisInfo?.data?.connected_clients || 0}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </motion.div>
        
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-2 bg-success-100 dark:bg-success-900/20 rounded-lg">
                  <Clock className="w-5 h-5 text-success-600 dark:text-success-400" />
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Uptime</p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                    {redisInfo?.data?.uptime_in_days || 0}d
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </motion.div>
        
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
        >
          <Card>
            <CardBody>
              <div className="flex items-center gap-3">
                <div className="p-2 bg-warning-100 dark:bg-warning-900/20 rounded-lg">
                  <Activity className="w-5 h-5 text-warning-600 dark:text-warning-400" />
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Ops/sec</p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                    {redisInfo?.data?.instantaneous_ops_per_sec || 0}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </motion.div>
      </div>
      
      {/* Memory Usage */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.5 }}
      >
        <Card>
          <CardHeader>
            <CardTitle>Memory Usage</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <HardDrive className="w-5 h-5 text-gray-500" />
                  <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                    {redisInfo?.data?.used_memory_human || '0B'}
                  </span>
                </div>
                <Badge variant={isMemoryHigh ? 'danger' : 'success'}>
                  {memoryPercentage.toFixed(1)}%
                </Badge>
              </div>
              
              <Progress
                value={memoryPercentage}
                variant={isMemoryHigh ? 'danger' : 'primary'}
                showLabel
              />
              
              {isMemoryHigh && (
                <div className="flex items-center gap-2 p-3 bg-danger-50 dark:bg-danger-900/20 rounded-lg">
                  <AlertTriangle className="w-4 h-4 text-danger-600 dark:text-danger-400" />
                  <span className="text-sm text-danger-700 dark:text-danger-300">
                    High memory usage detected. Consider monitoring or cleaning up keys.
                  </span>
                </div>
              )}
              
              <div className="grid grid-cols-2 gap-4 pt-4">
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400 mb-1">Peak Memory</p>
                  <p className="text-lg font-semibold text-gray-900 dark:text-gray-100">
                    {redisInfo?.data?.used_memory_peak_human || '0B'}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400 mb-1">Memory Fragmentation</p>
                  <p className="text-lg font-semibold text-gray-900 dark:text-gray-100">
                    {redisInfo?.data?.mem_fragmentation_ratio?.toFixed(2) || '0'}x
                  </p>
                </div>
              </div>
            </div>
          </CardBody>
        </Card>
      </motion.div>
      
      {/* Server Information */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.6 }}
        className="grid grid-cols-1 lg:grid-cols-2 gap-6"
      >
        <Card>
          <CardHeader>
            <CardTitle>Server Information</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Redis Version</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.redis_version || 'Unknown'}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Operating System</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.os || 'Unknown'}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Architecture</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.arch_bits || 'Unknown'}-bit
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">TCP Port</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.tcp_port || 'Unknown'}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Run Mode</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.redis_mode || 'standalone'}
                </span>
              </div>
            </div>
          </CardBody>
        </Card>
        
        <Card>
          <CardHeader>
            <CardTitle>Performance Statistics</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Total Connections</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.total_connections_received || 0}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Total Commands</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.total_commands_processed || 0}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Keyspace Hits</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.keyspace_hits || 0}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Keyspace Misses</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {redisInfo?.data?.keyspace_misses || 0}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-600 dark:text-gray-400">Hit Rate</span>
                <span className="text-sm font-medium text-gray-900 dark:text-gray-100">
                  {((redisInfo?.data?.keyspace_hits || 0) / 
                    ((redisInfo?.data?.keyspace_hits || 0) + (redisInfo?.data?.keyspace_misses || 0)) * 100).toFixed(1)}%
                </span>
              </div>
            </div>
          </CardBody>
        </Card>
      </motion.div>
    </div>
  );
};

export default RedisMonitor;