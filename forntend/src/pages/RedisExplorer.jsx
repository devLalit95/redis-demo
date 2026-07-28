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
  Eye,
  Clock,
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
import toast from 'react-hot-toast';

/**
 * Redis Explorer Page - Interactive Redis data structure explorer
 */
const RedisExplorer = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedType, setSelectedType] = useState('all');
  const [addModalOpen, setAddModalOpen] = useState(false);
  const [selectedKey, setSelectedKey] = useState(null);
  const [viewModalOpen, setViewModalOpen] = useState(false);
  const [keyValue, setKeyValue] = useState(null);
  const [newKeyName, setNewKeyName] = useState('');
  const [newKeyValue, setNewKeyValue] = useState('');
  const queryClient = useQueryClient();
  
  // Fetch all keys
  const { data: keysData, isLoading: keysLoading, refetch: refetchKeys } = useQuery({
    queryKey: ['redis-keys', searchQuery],
    queryFn: () => redisApi.key.getKeysByPattern(searchQuery || '*'),
  });
  
  // Fetch database size and keys
  const { data: dbSize } = useQuery({
    queryKey: ['redis-db-size'],
    queryFn: monitoringApi.getDatabaseSize,
  });
  
  // Fetch Redis info for statistics
  const { data: redisInfo } = useQuery({
    queryKey: ['redis-info'],
    queryFn: monitoringApi.getRedisInfo,
  });
  
  // Delete key mutation
  const deleteKeyMutation = useMutation({
    mutationFn: (key) => redisApi.key.deleteKey(key),
    onSuccess: () => {
      toast.success('Key deleted successfully');
      queryClient.invalidateQueries(['redis-keys']);
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to delete key');
    },
  });
  
  // String operations
  const stringMutation = useMutation({
    mutationFn: ({ key, value }) => redisApi.string.setString(key, value),
    onSuccess: () => {
      toast.success('Key added successfully');
      queryClient.invalidateQueries(['redis-keys']);
      queryClient.invalidateQueries(['redis-db-size']);
      setAddModalOpen(false);
      setNewKeyName('');
      setNewKeyValue('');
    },
    onError: (error) => {
      toast.error(error.userMessage || 'Failed to add key');
    },
  });
  
  // Get key type mutation
  const getKeyTypeMutation = useMutation({
    mutationFn: (key) => redisApi.key.getKeyType(key),
  });
  
  // View key value based on type
  const viewKeyValueMutation = useMutation({
    mutationFn: ({ key, type }) => {
      switch (type) {
        case 'string':
          return redisApi.string.getString(key);
        case 'hash':
          return redisApi.hash.getAllHashFields(key);
        case 'list':
          return redisApi.list.getList(key);
        case 'set':
          return redisApi.set.getSetMembers(key);
        case 'zset':
          return redisApi.sortedSet.getSortedSet(key);
        default:
          return Promise.resolve({ data: null });
      }
    },
    onSuccess: (data) => {
      setKeyValue(data);
      setViewModalOpen(true);
    },
    onError: () => {
      toast.error('Failed to retrieve key value');
    },
  });
  
  const handleAddKey = (type) => {
    setSelectedType(type);
    setAddModalOpen(true);
  };
  
  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
  };
  
  const handleDeleteKey = (key) => {
    if (window.confirm(`Are you sure you want to delete key "${key}"?`)) {
      deleteKeyMutation.mutate(key);
    }
  };
  
  const handleViewKey = async (key) => {
    setSelectedKey(key);
    const typeData = await getKeyTypeMutation.mutateAsync(key);
    const keyType = typeData?.data || 'string';
    viewKeyValueMutation.mutate({ key, type: keyType });
  };
  
  const handleAddStringKey = (e) => {
    e.preventDefault();
    if (!newKeyName || !newKeyValue) {
      toast.error('Please provide both key name and value');
      return;
    }
    stringMutation.mutate({ key: newKeyName, value: newKeyValue });
  };
  
  const getKeysList = () => {
    if (!keysData?.data) return [];
    let keys = Array.from(keysData.data);
    
    if (selectedType !== 'all') {
      // Filter by type would require getting type for each key
      // For now, we'll show all keys when a type is selected
      // In a real implementation, you'd want to batch type checks
    }
    
    if (searchQuery) {
      keys = keys.filter(key => key.toLowerCase().includes(searchQuery.toLowerCase()));
    }
    
    return keys;
  };
  
  const getKeyTypeColor = (type) => {
    switch (type) {
      case 'string': return 'primary';
      case 'hash': return 'secondary';
      case 'list': return 'success';
      case 'set': return 'warning';
      case 'zset': return 'danger';
      default: return 'gray';
    }
  };
  
  const dataTypes = [
    { id: 'string', name: 'String', icon: FileText, color: 'primary' },
    { id: 'hash', name: 'Hash', icon: Hash, color: 'secondary' },
    { id: 'list', name: 'List', icon: List, color: 'success' },
    { id: 'set', name: 'Set', icon: Layers, color: 'warning' },
    { id: 'sortedset', name: 'Sorted Set', icon: BarChart3, color: 'danger' },
  ];
  
  const keysList = getKeysList();
  
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
          onClick={() => {
            refetchKeys();
            toast.success('Keys refreshed');
          }}
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
                All Types ({keysList.length})
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
          
          {/* Keys List */}
          <Card>
            <CardHeader>
              <CardTitle>Keys ({keysList.length})</CardTitle>
            </CardHeader>
            <CardBody>
              {keysLoading ? (
                <div className="flex items-center justify-center py-8">
                  <Loader size="md" text="Loading keys..." />
                </div>
              ) : keysList.length === 0 ? (
                <div className="text-center py-12">
                  <Database className="w-16 h-16 text-gray-300 dark:text-gray-600 mx-auto mb-4" />
                  <Heading level={3} className="text-gray-900 dark:text-gray-100 mb-2">
                    No Keys Found
                  </Heading>
                  <Text className="text-gray-500 dark:text-gray-400 mb-6">
                    {searchQuery ? 'Try a different search pattern' : 'Add your first key to get started'}
                  </Text>
                  {!searchQuery && (
                    <Button
                      variant="primary"
                      onClick={() => handleAddKey('string')}
                      leftIcon={<Plus className="w-4 h-4" />}
                    >
                      Add Your First Key
                    </Button>
                  )}
                </div>
              ) : (
                <div className="space-y-2">
                  {keysList.map((key) => (
                    <motion.div
                      key={key}
                      initial={{ opacity: 0, x: -10 }}
                      animate={{ opacity: 1, x: 0 }}
                      className="flex items-center gap-3 p-3 bg-gray-50 dark:bg-gray-900 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
                    >
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center gap-2">
                          <FileText className="w-4 h-4 text-gray-400 flex-shrink-0" />
                          <span className="font-medium text-gray-900 dark:text-gray-100 truncate">
                            {key}
                          </span>
                        </div>
                      </div>
                      
                      <div className="flex items-center gap-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleViewKey(key)}
                          leftIcon={<Eye className="w-4 h-4" />}
                        >
                          View
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDeleteKey(key)}
                          leftIcon={<Trash2 className="w-4 h-4 text-danger-500" />}
                        >
                          Delete
                        </Button>
                      </div>
                    </motion.div>
                  ))}
                </div>
              )}
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
          <form onSubmit={handleAddStringKey}>
            <Input
              label="Key Name"
              placeholder="Enter key name"
              value={newKeyName}
              onChange={(e) => setNewKeyName(e.target.value)}
              required
            />
            <Input
              label="Value"
              placeholder="Enter value"
              value={newKeyValue}
              onChange={(e) => setNewKeyValue(e.target.value)}
              required
            />
            <div className="flex justify-end gap-3 pt-4">
              <Button
                variant="ghost"
                onClick={() => setAddModalOpen(false)}
                type="button"
              >
                Cancel
              </Button>
              <Button
                variant="primary"
                type="submit"
                loading={stringMutation.isPending}
              >
                Add Key
              </Button>
            </div>
          </form>
        </div>
      </Modal>
      
      {/* View Key Modal */}
      <Modal
        isOpen={viewModalOpen}
        onClose={() => setViewModalOpen(false)}
        title={`Key: ${selectedKey}`}
        size="lg"
      >
        <div className="space-y-4">
          {keyValue ? (
            <>
              <div className="flex items-center gap-2">
                <Badge variant={getKeyTypeColor('string')}>
                  {keyValue?.data ? 'Data' : 'Empty'}
                </Badge>
                {keyValue?.responseTime && (
                  <span className="text-sm text-gray-500 dark:text-gray-400">
                    <Clock className="w-3 h-3 inline mr-1" />
                    {keyValue.responseTime}ms
                  </span>
                )}
              </div>
              <div className="bg-gray-50 dark:bg-gray-900 p-4 rounded-lg">
                <pre className="text-sm text-gray-900 dark:text-gray-100 overflow-x-auto">
                  {JSON.stringify(keyValue?.data, null, 2)}
                </pre>
              </div>
            </>
          ) : (
            <div className="text-center py-8">
              <Loader size="md" text="Loading key value..." />
            </div>
          )}
          <div className="flex justify-end pt-4">
            <Button
              variant="primary"
              onClick={() => setViewModalOpen(false)}
            >
              Close
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default RedisExplorer;