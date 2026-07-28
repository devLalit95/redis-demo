import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { motion } from 'framer-motion';
import {
  Search,
  Plus,
  Trash2,
  RefreshCw,
  FileText,
  Hash,
  List,
  Layers,
  BarChart3,
  ChevronRight,
  Database,
  Activity,
} from 'lucide-react';
import { cn } from '../utils/cn';
import monitoringApi from '../services/api/monitoringApi';
import redisApi from '../services/api/redisApi';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Badge from '../components/ui/Badge';
import Tabs from '../components/ui/Tabs';
import Modal from '../components/ui/Modal';
import Loader from '../components/ui/Loader';
import { Heading, Text } from '../components/ui/Typography';

/**
 * Redis Explorer Page - Interactive Redis data structure explorer
 */
const RedisExplorer = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedType, setSelectedType] = useState('all');
  const [addModalOpen, setAddModalOpen] = useState(false);
  const [selectedKey, setSelectedKey] = useState(null);
  const queryClient = useQueryClient();
  
  // Fetch database size and keys
  const { data: dbSize, isLoading: dbSizeLoading } = useQuery({
    queryKey: ['redis-db-size'],
    queryFn: monitoringApi.getDatabaseSize,
  });
  
  // Fetch Redis info for statistics
  const { data: redisInfo } = useQuery({
    queryKey: ['redis-info'],
    queryFn: monitoringApi.getRedisInfo,
  });
  
  // String operations
  const stringMutation = useMutation({
    mutationFn: ({ key, value }) => redisApi.string.setString(key, value),
    onSuccess: () => {
      queryClient.invalidateQueries(['redis-db-size']);
      setAddModalOpen(false);
    },
  });
  
  const handleAddKey = (type) => {
    setSelectedType(type);
    setAddModalOpen(true);
  };
  
  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
  };
  
  const dataTypes = [
    { id: 'string', name: 'String', icon: FileText, color: 'primary' },
    { id: 'hash', name: 'Hash', icon: Hash, color: 'secondary' },
    { id: 'list', name: 'List', icon: List, color: 'success' },
    { id: 'set', name: 'Set', icon: Layers, color: 'warning' },
    { id: 'sortedset', name: 'Sorted Set', icon: BarChart3, color: 'danger' },
  ];
  
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <Heading level={1}>Redis Explorer</Heading>
        <Text>Explore and manage your Redis data structures interactively</Text>
      </div>
      
      {/* Statistics Bar */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center gap-6 p-4 bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700"
      >
        <div className="flex items-center gap-3">
          <div className="p-2 bg-primary-100 dark:bg-primary-900/20 rounded-lg">
            <Database className="w-5 h-5 text-primary-600 dark:text-primary-400" />
          </div>
          <div>
            <p className="text-sm text-gray-600 dark:text-gray-400">Total Keys</p>
            <p className="text-lg font-semibold text-gray-900 dark:text-gray-100">
              {dbSize?.data || 0}
            </p>
          </div>
        </div>
        
        <div className="h-8 w-px bg-gray-200 dark:bg-gray-700" />
        
        <div className="flex items-center gap-3">
          <div className="p-2 bg-success-100 dark:bg-success-900/20 rounded-lg">
            <Activity className="w-5 h-5 text-success-600 dark:text-success-400" />
          </div>
          <div>
            <p className="text-sm text-gray-600 dark:text-gray-400">Memory Used</p>
            <p className="text-lg font-semibold text-gray-900 dark:text-gray-100">
              {redisInfo?.data?.used_memory_human || 'N/A'}
            </p>
          </div>
        </div>
        
        <div className="flex-1" />
        
        <Button
          variant="outline-primary"
          onClick={() => queryClient.invalidateQueries(['redis-db-size'])}
          leftIcon={<RefreshCw className="w-4 h-4" />}
        >
          Refresh
        </Button>
      </motion.div>
      
      {/* Main Content */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Sidebar - Data Types */}
        <Card className="lg:col-span-1">
          <CardHeader>
            <CardTitle>Data Types</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-2">
              <button
                onClick={() => setSelectedType('all')}
                className={cn(
                  'w-full flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors',
                  selectedType === 'all'
                    ? 'bg-primary-50 text-primary-600 dark:bg-primary-900/20 dark:text-primary-400'
                    : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800'
                )}
              >
                <Layers className="w-5 h-5" />
                All Types
              </button>
              
              {dataTypes.map((type) => {
                const Icon = type.icon;
                return (
                  <button
                    key={type.id}
                    onClick={() => setSelectedType(type.id)}
                    className={cn(
                      'w-full flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors',
                      selectedType === type.id
                        ? 'bg-primary-50 text-primary-600 dark:bg-primary-900/20 dark:text-primary-400'
                        : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800'
                    )}
                  >
                    <Icon className="w-5 h-5" />
                    {type.name}
                  </button>
                );
              })}
            </div>
          </CardBody>
        </Card>
        
        {/* Main Content Area */}
        <div className="lg:col-span-3 space-y-6">
          {/* Search and Actions */}
          <div className="flex items-center gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
              <Input
                placeholder="Search keys..."
                value={searchQuery}
                onChange={handleSearch}
                className="pl-10"
              />
            </div>
            <Button
              variant="primary"
              onClick={() => handleAddKey('string')}
              leftIcon={<Plus className="w-4 h-4" />}
            >
              Add Key
            </Button>
          </div>
          
          {/* Content Area */}
          <Card>
            <CardBody>
              <div className="text-center py-12">
                <Database className="w-16 h-16 text-gray-300 dark:text-gray-600 mx-auto mb-4" />
                <Heading level={3} className="text-gray-900 dark:text-gray-100 mb-2">
                  Redis Explorer
                </Heading>
                <Text className="text-gray-500 dark:text-gray-400 mb-6">
                  Select a data type or search for keys to get started
                </Text>
                <div className="flex justify-center gap-3">
                  {dataTypes.map((type) => {
                    const Icon = type.icon;
                    return (
                      <Button
                        key={type.id}
                        variant="outline-primary"
                        onClick={() => handleAddKey(type.id)}
                        leftIcon={<Icon className="w-4 h-4" />}
                      >
                        Add {type.name}
                      </Button>
                    );
                  })}
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      </div>
      
      {/* Add Key Modal */}
      <Modal
        isOpen={addModalOpen}
        onClose={() => setAddModalOpen(false)}
        title="Add New Key"
        size="md"
      >
        <div className="space-y-4">
          <Input
            label="Key Name"
            placeholder="Enter key name"
            required
          />
          <Input
            label="Value"
            placeholder="Enter value"
            required
          />
          <div className="flex justify-end gap-3 pt-4">
            <Button
              variant="ghost"
              onClick={() => setAddModalOpen(false)}
            >
              Cancel
            </Button>
            <Button
              variant="primary"
              onClick={() => stringMutation.mutate({ key: 'test', value: 'value' })}
              loading={stringMutation.isPending}
            >
              Add Key
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default RedisExplorer;