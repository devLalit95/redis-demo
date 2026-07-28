import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  Plus, 
  Download, 
  Trash2,
  Search,
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
 * Set Operations Page - Interactive Redis set operations
 */
const SetOperations = () => {
  const [activeTab, setActiveTab] = useState('add');
  const [key, setKey] = useState('');
  const [value, setValue] = useState('');
  const [getKey, setGetKey] = useState('');
  const [getValue, setGetValue] = useState('');
  const [result, setResult] = useState(null);
  const queryClient = useQueryClient();
  
  // Add to set mutation
  const addToSetMutation = useMutation({
    mutationFn: (data) => redisApi.set.addToSet(data.key, data.value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Element added to set');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to add element to set');
    },
  });
  
  // Get set members mutation
  const getSetMembersMutation = useMutation({
    mutationFn: (key) => redisApi.set.getSetMembers(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Set members retrieved successfully');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to retrieve set members');
    },
  });
  
  // Check set member mutation
  const isSetMemberMutation = useMutation({
    mutationFn: ({ key, value }) => redisApi.set.isSetMember(key, value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Membership check completed');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to check membership');
    },
  });
  
  // Remove from set mutation
  const removeFromSetMutation = useMutation({
    mutationFn: ({ key, value }) => redisApi.set.removeFromSet(key, value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Element removed from set');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to remove element from set');
    },
  });
  
  const handleAddToSet = (e) => {
    e.preventDefault();
    if (!key || !value) {
      toast.error('Please provide both key and value');
      return;
    }
    addToSetMutation.mutate({ key, value });
  };
  
  const handleGetSetMembers = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    getSetMembersMutation.mutate(getKey);
  };
  
  const handleIsSetMember = () => {
    if (!getKey || !getValue) {
      toast.error('Please provide both key and value');
      return;
    }
    isSetMemberMutation.mutate({ key: getKey, value: getValue });
  };
  
  const handleRemoveFromSet = () => {
    if (!getKey || !getValue) {
      toast.error('Please provide both key and value');
      return;
    }
    removeFromSetMutation.mutate({ key: getKey, value: getValue });
  };
  
  const tabs = [
    {
      id: 'add',
      label: 'Add Member',
      icon: <Plus className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Add Element to Set (SADD)</CardTitle>
            </CardHeader>
            <CardBody>
              <form onSubmit={handleAddToSet} className="space-y-4">
                <Input
                  label="Set Key"
                  placeholder="Enter set key (e.g., tags)"
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
                  loading={addToSetMutation.isPending}
                  leftIcon={<Plus className="w-4 h-4" />}
                >
                  Add to Set
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
      id: 'getmembers',
      label: 'Get Members',
      icon: <Download className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Get All Set Members (SMEMBERS)</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Set Key"
                  placeholder="Enter set key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleGetSetMembers}
                  loading={getSetMembersMutation.isPending}
                  leftIcon={<Download className="w-4 h-4" />}
                >
                  Get Members
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
      id: 'checkmember',
      label: 'Check Member',
      icon: <Search className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Check if Element Exists (SISMEMBER)</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Set Key"
                  placeholder="Enter set key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Input
                  label="Value"
                  placeholder="Enter value to check"
                  value={getValue}
                  onChange={(e) => setGetValue(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleIsSetMember}
                  loading={isSetMemberMutation.isPending}
                  leftIcon={<Search className="w-4 h-4" />}
                >
                  Check Membership
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
      id: 'remove',
      label: 'Remove Member',
      icon: <Trash2 className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Remove Element from Set (SREM)</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Set Key"
                  placeholder="Enter set key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Input
                  label="Value"
                  placeholder="Enter value to remove"
                  value={getValue}
                  onChange={(e) => setGetValue(e.target.value)}
                  required
                />
                <Button
                  variant="danger"
                  onClick={handleRemoveFromSet}
                  loading={removeFromSetMutation.isPending}
                  leftIcon={<Trash2 className="w-4 h-4" />}
                >
                  Remove from Set
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
        <Heading level={1}>Set Operations</Heading>
        <Text>Perform Redis set operations interactively</Text>
      </div>
      
      {/* Main Content */}
      <Tabs tabs={tabs} defaultTab="add" onChange={setActiveTab} />
    </div>
  );
};

export default SetOperations;