import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import { 
  Save, 
  Download, 
  Trash2, 
  RefreshCw,
  FileText,
  Code,
  Clock,
} from 'lucide-react';
import { cn } from '../utils/cn';
import redisApi from '../services/api/redisApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Textarea from '../components/ui/Textarea';
import Badge from '../components/ui/Badge';
import Loader from '../components/ui/Loader';
import Tabs from '../components/ui/Tabs';
import { Heading, Text, Code as CodeBlock } from '../components/ui/Typography';
import toast from 'react-hot-toast';

/**
 * String Operations Page - Interactive Redis string operations
 */
const StringOperations = () => {
  const [activeTab, setActiveTab] = useState('set');
  const [key, setKey] = useState('');
  const [value, setValue] = useState('');
  const [getKey, setGetKey] = useState('');
  const [result, setResult] = useState(null);
  const queryClient = useQueryClient();
  
  // Set string mutation
  const setStringMutation = useMutation({
    mutationFn: (data) => redisApi.string.setString(data.key, data.value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('String stored successfully');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error('Failed to store string');
    },
  });
  
  // Get string mutation
  const getStringMutation = useMutation({
    mutationFn: (key) => redisApi.string.getString(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('String retrieved successfully');
    },
    onError: (error) => {
      toast.error('Failed to retrieve string');
    },
  });
  
  // Set JSON mutation
  const setJsonMutation = useMutation({
    mutationFn: (data) => redisApi.string.setJson(data.key, data.value),
    onSuccess: (data) => {
      setResult(data);
      toast.success('JSON stored successfully');
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error('Failed to store JSON');
    },
  });
  
  // Get JSON mutation
  const getJsonMutation = useMutation({
    mutationFn: (key) => redisApi.string.getJson(key),
    onSuccess: (data) => {
      setResult(data);
      toast.success('JSON retrieved successfully');
    },
    onError: (error) => {
      toast.error('Failed to retrieve JSON');
    },
  });
  
  const handleSetString = (e) => {
    e.preventDefault();
    if (!key || !value) {
      toast.error('Please provide both key and value');
      return;
    }
    setStringMutation.mutate({ key, value });
  };
  
  const handleGetString = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    getStringMutation.mutate(getKey);
  };
  
  const handleSetJson = (e) => {
    e.preventDefault();
    if (!key || !value) {
      toast.error('Please provide both key and JSON value');
      return;
    }
    try {
      const jsonValue = JSON.parse(value);
      setJsonMutation.mutate({ key, value: jsonValue });
    } catch (error) {
      toast.error('Invalid JSON format');
    }
  };
  
  const handleGetJson = () => {
    if (!getKey) {
      toast.error('Please provide a key');
      return;
    }
    getJsonMutation.mutate(getKey);
  };
  
  const tabs = [
    {
      id: 'set',
      label: 'Set String',
      icon: <Save className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Store String Value</CardTitle>
            </CardHeader>
            <CardBody>
              <form onSubmit={handleSetString} className="space-y-4">
                <Input
                  label="Key"
                  placeholder="Enter key name"
                  value={key}
                  onChange={(e) => setKey(e.target.value)}
                  required
                />
                <Textarea
                  label="Value"
                  placeholder="Enter string value"
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  rows={4}
                  required
                />
                <Button
                  type="submit"
                  variant="primary"
                  loading={setStringMutation.isPending}
                  leftIcon={<Save className="w-4 h-4" />}
                >
                  Store String
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
      label: 'Get String',
      icon: <Download className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>Retrieve String Value</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Key"
                  placeholder="Enter key name"
                  value={getKey}
                  onChange={(e) => setGetKey(e.target.value)}
                  required
                />
                <Button
                  variant="primary"
                  onClick={handleGetString}
                  loading={getStringMutation.isPending}
                  leftIcon={<Download className="w-4 h-4" />}
                >
                  Get String
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
      id: 'json',
      label: 'JSON Operations',
      icon: <Code className="w-4 h-4" />,
      content: (
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-6"
        >
          <Card>
            <CardHeader>
              <CardTitle>JSON Operations</CardTitle>
            </CardHeader>
            <CardBody>
              <Tabs
                tabs={[
                  {
                    id: 'set-json',
                    label: 'Set JSON',
                    content: (
                      <form onSubmit={handleSetJson} className="space-y-4">
                        <Input
                          label="Key"
                          placeholder="Enter key name"
                          value={key}
                          onChange={(e) => setKey(e.target.value)}
                          required
                        />
                        <Textarea
                          label="JSON Value"
                          placeholder='{"name": "John", "age": 30}'
                          value={value}
                          onChange={(e) => setValue(e.target.value)}
                          rows={6}
                          required
                        />
                        <Button
                          type="submit"
                          variant="primary"
                          loading={setJsonMutation.isPending}
                          leftIcon={<Save className="w-4 h-4" />}
                        >
                          Store JSON
                        </Button>
                      </form>
                    ),
                  },
                  {
                    id: 'get-json',
                    label: 'Get JSON',
                    content: (
                      <div className="space-y-4">
                        <Input
                          label="Key"
                          placeholder="Enter key name"
                          value={getKey}
                          onChange={(e) => setGetKey(e.target.value)}
                          required
                        />
                        <Button
                          variant="primary"
                          onClick={handleGetJson}
                          loading={getJsonMutation.isPending}
                          leftIcon={<Download className="w-4 h-4" />}
                        >
                          Get JSON
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
        <Heading level={1}>String Operations</Heading>
        <Text>Perform Redis string operations interactively</Text>
      </div>
      
      {/* Main Content */}
      <Tabs tabs={tabs} defaultTab="set" onChange={setActiveTab} />
    </div>
  );
};

export default StringOperations;