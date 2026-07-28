import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  RefreshCw, 
  Trash2, 
  Download,
  Clock,
  Layers,
  Zap,
} from 'lucide-react';
import { cn } from '../utils/cn';
import cacheApi from '../services/api/cacheApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Badge from '../components/ui/Badge';
import Tabs from '../components/ui/Tabs';
import { Heading, Text, Code as CodeBlock } from '../components/ui/Typography';
import toast from 'react-hot-toast';

/**
 * Cache Playground Page - Interactive cache invalidation demonstrations
 * Note: This page requires database/student functionality which is currently disabled
 * in Redis-only mode. The UI is available for demonstration purposes.
 */
const CachePlayground = () => {
  const [activeTab, setActiveTab] = useState('evict');
  const [studentId, setStudentId] = useState('');
  const [email, setEmail] = useState('');
  const [result, setResult] = useState(null);
  const queryClient = useQueryClient();
  
  const handleDisabledAction = () => {
    toast.error('Database functionality is disabled in Redis-only mode. This feature requires database integration.');
  };
  
  // Evict cache by ID mutation
  const evictCacheMutation = useMutation({
    mutationFn: (id) => cacheApi.evictStudentCache(id),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Cache evicted successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to evict cache');
    },
  });
  
  // Evict cache by email mutation
  const evictCacheByEmailMutation = useMutation({
    mutationFn: (email) => cacheApi.evictStudentCacheByEmail(email),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Cache evicted successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to evict cache');
    },
  });
  
  // Evict all cache mutation
  const evictAllCacheMutation = useMutation({
    mutationFn: () => cacheApi.evictAllStudentCache(),
    onSuccess: (data) => {
      setResult(data);
      toast.success('All cache evicted successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to evict all cache');
    },
  });
  
  // Refresh cache mutation
  const refreshCacheMutation = useMutation({
    mutationFn: (id) => cacheApi.refreshStudentCache(id),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Cache refreshed successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to refresh cache');
    },
  });
  
  // Lazy load mutation
  const lazyLoadMutation = useMutation({
    mutationFn: (id) => cacheApi.lazyLoadStudent(id),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Student lazy loaded successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to lazy load student');
    },
  });
  
  // Cache aside mutation
  const cacheAsideMutation = useMutation({
    mutationFn: (id) => cacheApi.cacheAsideStudent(id),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Cache aside pattern executed successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to execute cache aside pattern');
    },
  });
  
  const handleEvictCache = () => {
    handleDisabledAction();
  };
  
  const handleEvictCacheByEmail = () => {
    handleDisabledAction();
  };
  
  const handleEvictAllCache = () => {
    handleDisabledAction();
  };
  
  const handleRefreshCache = () => {
    handleDisabledAction();
  };
  
  const handleLazyLoad = () => {
    handleDisabledAction();
  };
  
  const handleCacheAside = () => {
    handleDisabledAction();
  };
  
  const tabs = [
    {
      id: 'evict',
      label: 'Evict Cache',
      icon: <Trash2 className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Cache Eviction</CardTitle>
            </CardHeader>
            <CardBody>
              <Tabs
                tabs={[
                  {
                    id: 'by-id',
                    label: 'By ID',
                    content: (
                      <div className="space-y-4">
                        <Input
                          label="Student ID"
                          type="number"
                          placeholder="Enter student ID"
                          value={studentId}
                          onChange={(e) => setStudentId(e.target.value)}
                          required
                        />
                        <Button
                          variant="danger"
                          onClick={handleEvictCache}
                          loading={evictCacheMutation.isPending}
                          leftIcon={<Trash2 className="w-4 h-4" />}
                        >
                          Evict Cache by ID
                        </Button>
                      </div>
                    ),
                  },
                  {
                    id: 'by-email',
                    label: 'By Email',
                    content: (
                      <div className="space-y-4">
                        <Input
                          label="Email"
                          type="email"
                          placeholder="Enter student email"
                          value={email}
                          onChange={(e) => setEmail(e.target.value)}
                          required
                        />
                        <Button
                          variant="danger"
                          onClick={handleEvictCacheByEmail}
                          loading={evictCacheByEmailMutation.isPending}
                          leftIcon={<Trash2 className="w-4 h-4" />}
                        >
                          Evict Cache by Email
                        </Button>
                      </div>
                    ),
                  },
                  {
                    id: 'all',
                    label: 'Evict All',
                    content: (
                      <div className="space-y-4">
                        <p className="text-sm text-gray-600 dark:text-gray-400">
                          This will evict all student cache entries. Use with caution.
                        </p>
                        <Button
                          variant="danger"
                          onClick={handleEvictAllCache}
                          loading={evictAllCacheMutation.isPending}
                          leftIcon={<Trash2 className="w-4 h-4" />}
                        >
                          Evict All Cache
                        </Button>
                      </div>
                    ),
                  },
                ]}
              />
            </CardBody>
          </Card>
          
          {result && (
            <Card>
              <CardHeader>
                <CardTitle>Response</CardTitle>
              </CardHeader>
              <CardBody>
                <div className="space-y-3">
                  <div className="flex items-center gap-2">
                    <Badge variant={result.success ? 'success' : 'danger'}>
                      {result.success ? 'Success' : 'Error'}
                    </Badge>
                    {result.responseTime && (
                      <span className="text-sm text-gray-500 dark:text-gray-400">
                        <Clock className="w-3 h-3 inline mr-1" />
                        {result.responseTime}ms
                      </span>
                    )}
                  </div>
                  <CodeBlock>{JSON.stringify(result, null, 2)}</CodeBlock>
                </div>
              </CardBody>
            </Card>
          )}
        </motion.div>
      ),
    },
    {
      id: 'refresh',
      label: 'Refresh Cache',
      icon: <RefreshCw className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Cache Refresh</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Student ID"
                  type="number"
                  placeholder="Enter student ID"
                  value={studentId}
                  onChange={(e) => setStudentId(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleRefreshCache}
                  loading={refreshCacheMutation.isPending}
                  leftIcon={<RefreshCw className="w-4 h-4" />}
                >
                  Refresh Cache
                </Button>
              </div>
            </CardBody>
          </Card>
          
          {result && (
            <Card>
              <CardHeader>
                <CardTitle>Response</CardTitle>
              </CardHeader>
              <CardBody>
                <div className="space-y-3">
                  <div className="flex items-center gap-2">
                    <Badge variant={result.success ? 'success' : 'danger'}>
                      {result.success ? 'Success' : 'Error'}
                    </Badge>
                    {result.responseTime && (
                      <span className="text-sm text-gray-500 dark:text-gray-400">
                        <Clock className="w-3 h-3 inline mr-1" />
                        {result.responseTime}ms
                      </span>
                    )}
                  </div>
                  <CodeBlock>{JSON.stringify(result, null, 2)}</CodeBlock>
                </div>
              </CardBody>
            </Card>
          )}
        </motion.div>
      ),
    },
    {
      id: 'patterns',
      label: 'Cache Patterns',
      icon: <Layers className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Cache Patterns</CardTitle>
            </CardHeader>
            <CardBody>
              <Tabs
                tabs={[
                  {
                    id: 'lazy',
                    label: 'Lazy Loading',
                    icon: <Download className="w-4 h-4" />,
                    content: (
                      <div className="space-y-4">
                        <p className="text-sm text-gray-600 dark:text-gray-400">
                          Load data into cache only when it's requested (cache-aside pattern)
                        </p>
                        <Input
                          label="Student ID"
                          type="number"
                          placeholder="Enter student ID"
                          value={studentId}
                          onChange={(e) => setStudentId(e.target.value)}
                          required
                        />
                        <Button
                          variant="primary"
                          onClick={handleLazyLoad}
                          loading={lazyLoadMutation.isPending}
                          leftIcon={<Download className="w-4 h-4" />}
                        >
                          Lazy Load
                        </Button>
                      </div>
                    ),
                  },
                  {
                    id: 'cache-aside',
                    label: 'Cache Aside',
                    icon: <Zap className="w-4 h-4" />,
                    content: (
                      <div className="space-y-4">
                        <p className="text-sm text-gray-600 dark:text-gray-400">
                          Application manages cache and database independently
                        </p>
                        <Input
                          label="Student ID"
                          type="number"
                          placeholder="Enter student ID"
                          value={studentId}
                          onChange={(e) => setStudentId(e.target.value)}
                          required
                        />
                        <Button
                          variant="primary"
                          onClick={handleCacheAside}
                          loading={cacheAsideMutation.isPending}
                          leftIcon={<Zap className="w-4 h-4" />}
                        >
                          Cache Aside
                        </Button>
                      </div>
                    ),
                  },
                ]}
              />
            </CardBody>
          </Card>
          
          {result && (
            <Card>
              <CardHeader>
                <CardTitle>Response</CardTitle>
              </CardHeader>
              <CardBody>
                <div className="space-y-3">
                  <div className="flex items-center gap-2">
                    <Badge variant={result.success ? 'success' : 'danger'}>
                      {result.success ? 'Success' : 'Error'}
                    </Badge>
                    {result.responseTime && (
                      <span className="text-sm text-gray-500 dark:text-gray-400">
                        <Clock className="w-3 h-3 inline mr-1" />
                        {result.responseTime}ms
                      </span>
                    )}
                  </div>
                  <CodeBlock>{JSON.stringify(result, null, 2)}</CodeBlock>
                </div>
              </CardBody>
            </Card>
          )}
        </motion.div>
      ),
    },
  ];
  
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <Heading level={1}>Cache Playground</Heading>
        <Text>Interactive cache invalidation and pattern demonstrations</Text>
      </div>
      
      {/* Database Disabled Notice */}
      <div className="bg-warning-50 dark:bg-warning-900/20 border border-warning-200 dark:border-warning-800 rounded-lg p-4">
        <div className="flex items-start gap-3">
          <div className="flex-shrink-0">
            <div className="w-10 h-10 bg-warning-100 dark:bg-warning-900/30 rounded-full flex items-center justify-center">
              <Layers className="w-5 h-5 text-warning-600 dark:text-warning-400" />
            </div>
          </div>
          <div className="flex-1">
            <h3 className="text-sm font-semibold text-warning-900 dark:text-warning-100 mb-1">
              Database Functionality Disabled
            </h3>
            <p className="text-sm text-warning-700 dark:text-warning-300">
              This feature requires database integration (MySQL) which is currently disabled in Redis-only mode. 
              The UI is available for demonstration purposes. To enable this feature, configure the database in 
              <code className="bg-warning-100 dark:bg-warning-900/40 px-1.5 py-0.5 rounded mx-1">application.properties</code>.
            </p>
          </div>
        </div>
      </div>
      
      {/* Main Content */}
      <Tabs tabs={tabs} defaultTab="evict" onChange={setActiveTab} />
    </div>
  );
};

export default CachePlayground;