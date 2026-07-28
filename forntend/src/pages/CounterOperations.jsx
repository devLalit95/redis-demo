import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  Plus, 
  Minus, 
  TrendingUp,
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
 * Counter Operations Page - Interactive Redis counter operations
 */
const CounterOperations = () => {
  const [activeTab, setActiveTab] = useState('increment');
  const [key, setKey] = useState('');
  const [delta, setDelta] = useState(1);
  const [result, setResult] = useState(null);
  const queryClient = useQueryClient();
  
  // Increment mutation
  const incrementMutation = useMutation({
    mutationFn: (key) => redisApi.counter.increment(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Counter incremented successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to increment counter');
    },
  });
  
  // Increment by mutation
  const incrementByMutation = useMutation({
    mutationFn: ({ key, delta }) => redisApi.counter.incrementBy(key, delta),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Counter incremented successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to increment counter');
    },
  });
  
  // Decrement mutation
  const decrementMutation = useMutation({
    mutationFn: (key) => redisApi.counter.decrement(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Counter decremented successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to decrement counter');
    },
  });
  
  const handleIncrement = () => {
    if (!key) {
      toast.error('Please provide a key');
      return;
    }
    incrementMutation.mutate(key);
  };
  
  const handleIncrementBy = () => {
    if (!key) {
      toast.error('Please provide a key');
      return;
    }
    incrementByMutation.mutate({ key, delta });
  };
  
  const handleDecrement = () => {
    if (!key) {
      toast.error('Please provide a key');
      return;
    }
    decrementMutation.mutate(key);
  };
  
  const tabs = [
    {
      id: 'increment',
      label: 'Increment',
      icon: <Plus className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Increment Counter</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Counter Key"
                  placeholder="Enter counter key (e.g., views)"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleIncrement}
                  loading={incrementMutation.isPending}
                  leftIcon={<Plus className="w-4 h-4" />}
                >
                  Increment (+1)
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
      id: 'incrementby',
      label: 'Increment By',
      icon: <TrendingUp className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Increment Counter by Amount</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Counter Key"
                  placeholder="Enter counter key"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Input
                  label="Delta"
                  type="number"
                  placeholder="Enter increment amount"
                  value={delta}
                  onChange={(e) => setDelta(parseInt(e.target.value) || 1)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleIncrementBy}
                  loading={incrementByMutation.isPending}
                  leftIcon={<TrendingUp className="w-4 h-4" />}
                >
                  Increment by {delta}
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
      id: 'decrement',
      label: 'Decrement',
      icon: <Minus className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Decrement Counter</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Counter Key"
                  placeholder="Enter counter key"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Button
                  variant="danger"
                  onClick={handleDecrement}
                  loading={decrementMutation.isPending}
                  leftIcon={<Minus className="w-4 h-4" />}
                >
                  Decrement (-1)
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
        <Heading level={1}>Counter Operations</Heading>
        <Text>Perform Redis counter operations interactively</Text>
      </div>
      
      {/* Main Content */}
      <Tabs tabs={tabs} defaultTab="increment" onChange={setActiveTab} />
    </div>
  );
};

export default CounterOperations;