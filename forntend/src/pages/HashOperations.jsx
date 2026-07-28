import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  Save, 
  Download, 
  Trash2, 
  Hash,
  Clock,
} from 'lucide-react';
import { cn } from '../utils/cn';
import redisApi from '../services/api/redisApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Badge from '../components/ui/Badge';
import Loader from '../components/ui/Loader';
import Tabs from '../components/ui/Tabs';
import { Heading, Text, Code as CodeBlock } from '../components/ui/Typography';
import toast from 'react-hot-toast';

/**
 * Hash Operations Page - Interactive Redis hash operations
 */
const HashOperations = () => {
  const [activeTab, setActiveTab] = useState('set');
  const [key, setKey] = useState('');
  const [field, setField] = useState('');
  const [value, setValue] = useState('');
  const [getKey, setGetKey] = useState('');
  const [getField, setGetField] = useState('');
  const [result, setResult] = useState(null);
  const queryClient = useQueryClient();
  
  // Set hash field mutation
  const setHashFieldMutation = useMutation({
    mutationFn: (data) => redisApi.hash.setHashField(data.key, data.field, data.value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Hash field stored successfully');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error('Failed to store hash field');
    },
  });
  
  // Get hash field mutation
  const getHashFieldMutation = useMutation({
    mutationFn: ({ key, field }) => redisApi.hash.getHashField(key, field),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Hash field retrieved successfully');
    },
    onError: (error) => {
      toast.error('Failed to retrieve hash field');
    },
  });
  
  // Get all hash fields mutation
  const getAllHashFieldsMutation = useMutation({
    mutationFn: (key) => redisApi.hash.getAllHashFields(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('All hash fields retrieved successfully');
    },
    onError: (error) => {
      toast.error('Failed to retrieve hash fields');
    },
  });
  
  // Delete hash field mutation
  const deleteHashFieldMutation = useMutation({
    mutationFn: ({ key, field }) => redisApi.hash.deleteHashField(key, field),
    onSuccess: (data) => {
      setResult(data);
      toast.success('Hash field deleted successfully');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error('Failed to delete hash field');
    },
  });
  
  const handleSetHashField = (e) => {
    e.preventDefault();
    if (!key || !field || !value) {
      toast.error('Please provide key, field, and value');
      return;
    }
    setHashFieldMutation.mutate({ key, field, value });
  };
  
  const handleGetHashField = () => {
    if (!getKey || !getField) {
      toast.error('Please provide key and field');
      return;
    }
    getHashFieldMutation.mutate({ key: getKey, field: getField });
  };
  
  const handleGetAllHashFields = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    getAllHashFieldsMutation.mutate(getKey);
  };
  
  const handleDeleteHashField = () => {
    if (!getKey || !getField) {
      toast.error('Please provide key and field');
      return;
    }
    deleteHashFieldMutation.mutate({ key: getKey, field: getField });
  };
  
  const tabs = [
    {
      id: 'set',
      label: 'Set Field',
      icon: <Save className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Store Hash Field</CardTitle>
            </CardHeader>
            <CardBody>
              <form onSubmit={handleSetHashField} className="space-y-4">
                <Input
                  label="Key"
                  placeholder="Enter hash key (e.g., user:1)"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Input
                  label="Field"
                  placeholder="Enter field name (e.g., name)"
                  value={field}
                  onChange={(e) => setField(e.target.value)}
                  required
                />
                <Input
                  label="Value"
                  placeholder="Enter field value"
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  required
                />
                <Button
                  type="submit"
                  variant="primary"
                  loading={setHashFieldMutation.isPending}
                  leftIcon={<Save className="w-4 h-4" />}
                >
                  Store Field
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
      id: 'get',
      label: 'Get Field',
      icon: <Download className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Retrieve Hash Field</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Key"
                  placeholder="Enter hash key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Input
                  label="Field"
                  placeholder="Enter field name"
                  value={getField}
                  onChange={(e) => setGetField(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleGetHashField}
                  loading={getHashFieldMutation.isPending}
                  leftIcon={<Download className="w-4 h-4" />}
                >
                  Get Field
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
      id: 'getall',
      label: 'Get All Fields',
      icon: <Hash className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Retrieve All Hash Fields</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Key"
                  placeholder="Enter hash key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleGetAllHashFields}
                  loading={getAllHashFieldsMutation.isPending}
                  leftIcon={<Hash className="w-4 h-4" />}
                >
                  Get All Fields
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
      id: 'delete',
      label: 'Delete Field',
      icon: <Trash2 className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Delete Hash Field</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Key"
                  placeholder="Enter hash key"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Input
                  label="Field"
                  placeholder="Enter field name"
                  value={getField}
                  onChange={(e) => setGetField(e.target.value)}
                  required
                />
                <Button
                  variant="danger"
                  onClick={handleDeleteHashField}
                  loading={deleteHashFieldMutation.isPending}
                  leftIcon={<Trash2 className="w-4 h-4" />}
                >
                  Delete Field
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
        <Heading level={1}>Hash Operations</Heading>
        <Text>Perform Redis hash operations interactively</Text>
      </div>
      
      {/* Main Content */}
      <Tabs tabs={tabs} defaultTab="set" onChange={setActiveTab} />
    </div>
  );
};

export default HashOperations;