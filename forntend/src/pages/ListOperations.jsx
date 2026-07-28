import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  Plus, 
  Minus, 
  Download, 
  Trash2,
  ArrowLeft,
  ArrowRight,
  Clock,
} from 'lucide-react';
import { cn } from '../utils/cn';
import redisApi from '../services/api/redisApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Badge from '../components/ui/Badge';
import Tabs from '../components/ui/Tabs';
import { Heading, Text, Code as CodeBlock } from '../components/ui/Typography';
import toast from 'react-hot-toast';

/**
 * List Operations Page - Interactive Redis list operations
 */
const ListOperations = () => {
  const [activeTab, setActiveTab] = useState('leftpush');
  const [key, setKey] = useState('');
  const [value, setValue] = useState('');
  const [getKey, setGetKey] = useState('');
  const [result, setResult] = useState(null);
  const queryClient = useQueryClient();
  
  // Left push mutation
  const leftPushMutation = useMutation({
    mutationFn: (data) => redisApi.list.leftPush(data.key, data.value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Element added to left of list');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to add element to left');
    },
  });
  
  // Right push mutation
  const rightPushMutation = useMutation({
    mutationFn: (data) => redisApi.list.rightPush(data.key, data.value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Element added to right of list');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to add element to right');
    },
  });
  
  // Get list mutation
  const getListMutation = useMutation({
    mutationFn: (key) => redisApi.list.getList(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('List retrieved successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to retrieve list');
    },
  });
  
  // Left pop mutation
  const leftPopMutation = useMutation({
    mutationFn: (key) => redisApi.list.leftPop(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Element popped from left');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to pop from left');
    },
  });
  
  // Right pop mutation
  const rightPopMutation = useMutation({
    mutationFn: (key) => redisApi.list.rightPop(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Element popped from right');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to pop from right');
    },
  });
  
  const handleLeftPush = (e) => {
    e.preventDefault();
    if (!key || !value) {
      toast.error('Please provide both key and value');
      return;
    }
    leftPushMutation.mutate({ key, value });
  };
  
  const handleRightPush = (e) => {
    e.preventDefault();
    if (!key || !value) {
      toast.error('Please provide both key and value');
      return;
    }
    rightPushMutation.mutate({ key, value });
  };
  
  const handleGetList = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    getListMutation.mutate(getKey);
  };
  
  const handleLeftPop = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    leftPopMutation.mutate(getKey);
  };
  
  const handleRightPop = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    rightPopMutation.mutate(getKey);
  };
  
  const tabs = [
    {
      id: 'leftpush',
      label: 'Left Push',
      icon: <ArrowLeft className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Add Element to Left (LPUSH)</CardTitle>
            </CardHeader>
            <CardBody>
              <form onSubmit={handleLeftPush} className="space-y-4">
                <Input
                  label="List Key"
                  placeholder="Enter list key (e.g., queue)"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Input
                  label="Value"
                  placeholder="Enter value to add"
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  required
                />
                <Button
                  type="submit"
                  variant="primary"
                  loading={leftPushMutation.isPending}
                  leftIcon={<ArrowLeft className="w-4 h-4" />}
                >
                  Push to Left
                </Button>
              </form>
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
      id: 'rightpush',
      label: 'Right Push',
      icon: <ArrowRight className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Add Element to Right (RPUSH)</CardTitle>
            </CardHeader>
            <CardBody>
              <form onSubmit={handleRightPush} className="space-y-4">
                <Input
                  label="List Key"
                  placeholder="Enter list key"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Input
                  label="Value"
                  placeholder="Enter value to add"
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  required
                />
                <Button
                  type="submit"
                  variant="primary"
                  loading={rightPushMutation.isPending}
                  leftIcon={<ArrowRight className="w-4 h-4" />}
                >
                  Push to Right
                </Button>
              </form>
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
      id: 'getlist',
      label: 'Get List',
      icon: <Download className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Retrieve All List Elements (LRANGE)</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="List Key"
                  placeholder="Enter list key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleGetList}
                  loading={getListMutation.isPending}
                  leftIcon={<Download className="w-4 h-4" />}
                >
                  Get List
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
      id: 'leftpop',
      label: 'Left Pop',
      icon: <ArrowLeft className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Remove Element from Left (LPOP)</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="List Key"
                  placeholder="Enter list key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Button
                  variant="danger"
                  onClick={handleLeftPop}
                  loading={leftPopMutation.isPending}
                  leftIcon={<ArrowLeft className="w-4 h-4" />}
                >
                  Pop from Left
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
      id: 'rightpop',
      label: 'Right Pop',
      icon: <ArrowRight className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Remove Element from Right (RPOP)</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="List Key"
                  placeholder="Enter list key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Button
                  variant="danger"
                  onClick={handleRightPop}
                  loading={rightPopMutation.isPending}
                  leftIcon={<ArrowRight className="w-4 h-4" />}
                >
                  Pop from Right
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
  ];
  
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <Heading level={1}>List Operations</Heading>
        <Text>Perform Redis list operations interactively</Text>
      </div>
      
      {/* Main Content */}
      <Tabs tabs={tabs} defaultTab="leftpush" onChange={setActiveTab} />
    </div>
  );
};

export default ListOperations;