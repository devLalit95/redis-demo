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
  const { data: keysData, isLoading: keysLoading, error: keysError, refetch: refetchKeys } = useQuery({
    queryKey: ['redis-keys', searchQuery],
    queryFn: async () => {
      try {
        const response = await redisApi.key.getKeysByPattern(searchQuery || '*');
        console.log('Keys API response:', response);
        return response;
      } catch (error) {
        console.error('Keys API error:', error);
        throw error;
      }
    },
    retry: 2,
    retryDelay: 1000,
    staleTime: 10000, // 10 seconds
  });
  
  // Fetch database size and keys
  const { data: dbSize, isLoading: dbSizeLoading, error: dbSizeError } = useQuery({
    queryKey: ['redis-db-size'],
    queryFn: monitoringApi.getDatabaseSize,
    retry: 1,
    staleTime: 30000, // 30 seconds
  });
  
  // Fetch Redis info for statistics
  const { data: redisInfo, isLoading: redisInfoLoading, error: redisInfoError } = useQuery({
    queryKey: ['redis-info'],
    queryFn: monitoringApi.getRedisInfo,
    retry: 1,
    staleTime: 30000, // 30 seconds
  });
  
  // Delete key mutation
  const deleteKeyMutation = useMutation({
    mutationFn: async (key) => {
      console.log('Deleting key:', key);
      const response = await redisApi.key.deleteKey(key);
      console.log('Delete key response:', response);
      return response;
    },
    onSuccess: () => {
      toast.success('Key deleted successfully');
      queryClient.invalidateQueries(['redis-keys']);
      queryClient.invalidateQueries(['redis-db-size']);
    },
    onError: (error) => {
      console.error('Delete key error:', error);
      toast.error(error.userMessage || 'Failed to delete key');
    },
  });
  
  // String operations
  const stringMutation = useMutation({
    mutationFn: async ({ key, value }) => {
      console.log('Adding key:', key, 'value:', value);
      const response = await redisApi.string.setString(key, value);
      console.log('Add key response:', response);
      return response;
    },
    onSuccess: (data) => {
      console.log('Key added successfully, invalidating queries');
      toast.success('Key added successfully');
      queryClient.invalidateQueries(['redis-keys']);
      queryClient.invalidateQueries(['redis-db-size']);
      setAddModalOpen(false);
      setNewKeyName('');
      setNewKeyValue('');
    },
    onError: (error) => {
      console.error('Add key error:', error);
      toast.error(error.userMessage || 'Failed to add key');
    },
  });
  
  // Get key type mutation
  const getKeyTypeMutation = useMutation({
    mutationFn: async (key) => {
      console.log('Getting key type for:', key);
      try {
        const response = await redisApi.key.getKeyType(key);
        console.log('Key type response:', response);
        return response;
      } catch (error) {
        console.error('Get key type error:', error);
        // Default to string if type check fails
        return { data: { data: 'string' } };
      }
    },
  });
  
  // View key value based on type
  const viewKeyValueMutation = useMutation({
    mutationFn: async ({ key, type }) => {
      console.log('Viewing key:', key, 'type:', type);
      try {
        let response;
        switch (type) {
          case 'string':
            response = await redisApi.string.getString(key);
            break;
          case 'hash':
            response = await redisApi.hash.getAllHashFields(key);
            break;
          case 'list':
            response = await redisApi.list.getList(key);
            break;
          case 'set':
            response = await redisApi.set.getSetMembers(key);
            break;
          case 'zset':
            response = await redisApi.sortedSet.getSortedSet(key);
            break;
          default:
            // Try to get value directly
            response = await redisApi.key.getValue(key);
        }
        console.log('Key value response:', response);
        return response;
      } catch (error) {
        console.error('Error getting key value:', error);
        throw error;
      }
    },
    onSuccess: (data) => {
      console.log('Key value data:', data);
      setKeyValue(data);
      setViewModalOpen(true);
    },
    onError: (error) => {
      console.error('View key error:', error);
      toast.error(error.userMessage || 'Failed to retrieve key value');
    }
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
    try {
      setSelectedKey(key);
      const typeData = await getKeyTypeMutation.mutateAsync(key);
      const keyType = typeData?.data?.data || typeData?.data || 'string';
      console.log('Viewing key with type:', keyType);
      viewKeyValueMutation.mutate({ key, type: keyType });
    } catch (error) {
      console.error('Handle view key error:', error);
      toast.error('Failed to view key');
    }
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
    // Handle different response structures
    let keys = [];
    
    try {
      console.log('keysData:', keysData);
      console.log('keysData type:', typeof keysData);
      console.log('keysData.data:', keysData?.data);
      console.log('keysData.data type:', typeof keysData?.data);
      
      // The response is a full Axios object: { data: { data: [...], success: true, ... }, status: 200, ... }
      // So the actual keys array is at keysData.data.data
      if (keysData?.data?.data && Array.isArray(keysData.data.data)) {
        keys = keysData.data.data;
        console.log('Using keysData.data.data as array');
      } else if (keysData?.data && Array.isArray(keysData.data)) {
        keys = keysData.data;
        console.log('Using keysData.data as array');
      } else if (Array.isArray(keysData)) {
        keys = keysData;
        console.log('Using keysData as array');
      } else if (keysData && typeof keysData === 'object') {
        // Try to extract data from nested structure
        if (keysData.data !== undefined) {
          if (Array.isArray(keysData.data)) {
            keys = keysData.data;
            console.log('Using keysData.data as array (nested)');
          } else if (typeof keysData.data === 'object' && keysData.data !== null) {
            if (keysData.data.data && Array.isArray(keysData.data.data)) {
              keys = keysData.data.data;
              console.log('Using keysData.data.data as array (double nested)');
            } else {
              keys = Object.keys(keysData.data);
              console.log('Using Object.keys(keysData.data)');
            }
          }
        } else {
          // Check if keysData itself is the array
          if (Array.isArray(Object.values(keysData))) {
            const values = Object.values(keysData);
            keys = values.filter(v => typeof v === 'string');
            console.log('Using filtered Object.values as keys');
          }
        }
      }
      
      console.log('Final extracted keys:', keys);
      console.log('Keys is array:', Array.isArray(keys));
      console.log('Keys length:', keys.length);
      
      if (selectedType !== 'all') {
        // Filter by type would require getting type for each key
        // For now, we'll show all keys when a type is selected
        // In a real implementation, you'd want to batch type checks
      }
      
      if (searchQuery) {
        keys = keys.filter(key => key.toLowerCase().includes(searchQuery.toLowerCase()));
      }
      
      return keys;
    } catch (error) {
      console.error('Error in getKeysList:', error);
      return [];
    }
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
    <div className="space-y-3">
      {/* Page Header - Compact */}
      <div className="page-header">
        <Heading level={1} className="text-lg sm:text-xl">Redis Explorer</Heading>
        <Text className="text-xs sm:text-sm">Explore and manage your Redis data structures</Text>
      </div>
      
      {/* Statistics Bar - Compact */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center gap-3 sm:gap-4 p-2 sm:p-3 bg-white dark:bg-gray-800 rounded border border-gray-200 dark:border-gray-700"
      >
        <div className="flex items-center gap-2">
          <div className="p-1.5 sm:p-2 bg-primary-100 dark:bg-primary-900/20 rounded">
            <Database className="w-4 h-4 sm:w-5 sm:h-5 text-primary-600 dark:text-primary-400" />
          </div>
          <div>
            <p className="text-xs text-gray-600 dark:text-gray-400">Total Keys</p>
            {dbSizeLoading ? (
              <Loader size="xs" />
            ) : dbSizeError ? (
              <p className="text-sm font-semibold text-danger-600 dark:text-danger-400">Error</p>
            ) : (
              <p className="text-sm sm:text-lg font-semibold text-gray-900 dark:text-gray-100">
                {dbSize?.data?.data || dbSize?.data || 0}
              </p>
            )}
          </div>
        </div>
        
        <div className="h-6 sm:h-8 w-px bg-gray-200 dark:bg-gray-700 hidden sm:block" />
        
        <div className="flex items-center gap-2">
          <div className="p-1.5 sm:p-2 bg-success-100 dark:bg-success-900/20 rounded">
            <Activity className="w-4 h-4 sm:w-5 sm:h-5 text-success-600 dark:text-success-400" />
          </div>
          <div>
            <p className="text-xs text-gray-600 dark:text-gray-400">Memory Used</p>
            {redisInfoLoading ? (
              <Loader size="xs" />
            ) : redisInfoError ? (
              <p className="text-sm font-semibold text-danger-600 dark:text-danger-400">Error</p>
            ) : (
              <p className="text-sm sm:text-lg font-semibold text-gray-900 dark:text-gray-100">
                {redisInfo?.data?.data?.used_memory_human || redisInfo?.data?.used_memory_human || 'N/A'}
              </p>
            )}
          </div>
        </div>
        
        <div className="flex-1" />
        
        <Button
          variant="outline-primary"
          size="sm"
          onClick={() => {
            refetchKeys();
            toast.success('Keys refreshed');
          }}
          compact
        >
          Refresh
        </Button>
      </motion.div>
      
      {/* Main Content - Responsive Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-3">
        {/* Sidebar - Data Types */}
        <Card compact>
          <CardHeader compact>
            <CardTitle size="sm">Data Types</CardTitle>
          </CardHeader>
          <CardBody compact>
            <div className="space-y-1">
              <button
                onClick={() => setSelectedType('all')}
                className={cn(
                  'w-full flex items-center gap-2 px-2 py-1.5 rounded text-xs font-medium transition-colors',
                  selectedType === 'all'
                    ? 'bg-primary-50 text-primary-600 dark:bg-primary-900/20 dark:text-primary-400'
                    : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800'
                )}
              >
                <Layers className="w-4 h-4" />
                All Types ({Array.isArray(keysList) ? keysList.length : 0})
              </button>
              
              {dataTypes.map((type) => {
                const Icon = type.icon;
                return (
                  <button
                    key={type.id}
                    onClick={() => setSelectedType(type.id)}
                    className={cn(
                      'w-full flex items-center gap-2 px-2 py-1.5 rounded text-xs font-medium transition-colors',
                      selectedType === type.id
                        ? 'bg-primary-50 text-primary-600 dark:bg-primary-900/20 dark:text-primary-400'
                        : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800'
                    )}
                  >
                    <Icon className="w-4 h-4" />
                    {type.name}
                  </button>
                );
              })}
            </div>
          </CardBody>
        </Card>
        
        {/* Main Content Area */}
        <div className="lg:col-span-3 space-y-3">
          {/* Search and Actions - Compact */}
          <div className="flex items-center gap-2">
            <div className="flex-1 relative">
              <Search className="absolute left-2 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-gray-400" />
              <Input
                placeholder="Search keys..."
                value={searchQuery}
                onChange={handleSearch}
                className="pl-8 text-xs"
                compact
              />
            </div>
            <Button
              variant="primary"
              size="sm"
              onClick={() => handleAddKey('string')}
              compact
            >
              Add Key
            </Button>
          </div>
          
          {/* Keys List - Compact */}
          <Card compact>
            <CardHeader compact>
              <CardTitle size="sm">Keys ({Array.isArray(keysList) ? keysList.length : 0})</CardTitle>
            </CardHeader>
            <CardBody compact>
              {keysLoading ? (
                <div className="flex items-center justify-center py-6">
                  <Loader size="sm" text="Loading keys..." />
                </div>
              ) : keysError ? (
                <div className="text-center py-6">
                  <Database className="w-12 h-12 text-danger-300 dark:text-danger-600 mx-auto mb-3" />
                  <Heading level={3} className="text-gray-900 dark:text-gray-100 mb-2 text-sm">
                    Error Loading Keys
                  </Heading>
                  <Text className="text-gray-500 dark:text-gray-400 mb-3 text-xs">
                    {keysError.userMessage || 'Failed to load Redis keys'}
                  </Text>
                  <Button
                    variant="primary"
                    size="sm"
                    onClick={() => refetchKeys()}
                    compact
                  >
                    Retry
                  </Button>
                </div>
              ) : !Array.isArray(keysList) || keysList.length === 0 ? (
                <div className="text-center py-8">
                  <Database className="w-12 h-12 text-gray-300 dark:text-gray-600 mx-auto mb-3" />
                  <Heading level={3} className="text-gray-900 dark:text-gray-100 mb-2 text-sm">
                    No Keys Found
                  </Heading>
                  <Text className="text-gray-500 dark:text-gray-400 mb-4 text-xs">
                    {searchQuery ? 'Try a different search pattern' : 'Add your first key to get started'}
                  </Text>
                  {!searchQuery && (
                    <Button
                      variant="primary"
                      size="sm"
                      onClick={() => handleAddKey('string')}
                      compact
                    >
                      Add Your First Key
                    </Button>
                  )}
                </div>
              ) : (
                <div className="space-y-1">
                  {keysList.map((key, index) => (
                    <motion.div
                      key={key || index}
                      initial={{ opacity: 0, x: -10 }}
                      animate={{ opacity: 1, x: 0 }}
                      className="flex items-center gap-2 p-2 bg-gray-50 dark:bg-gray-900 rounded hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
                    >
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center gap-1.5">
                          <FileText className="w-3.5 h-3.5 text-gray-400 flex-shrink-0" />
                          <span className="font-medium text-gray-900 dark:text-gray-100 text-xs truncate">
                            {key}
                          </span>
                        </div>
                      </div>
                      
                      <div className="flex items-center gap-1">
                        <Button
                          variant="ghost"
                          size="xs"
                          onClick={() => handleViewKey(key)}
                        >
                          View
                        </Button>
                        <Button
                          variant="ghost"
                          size="xs"
                          onClick={() => handleDeleteKey(key)}
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
      
      {/* Add Key Modal - Compact */}
      <Modal
        isOpen={addModalOpen}
        onClose={() => setAddModalOpen(false)}
        title="Add New Key"
        size="md"
        compact
      >
        <div className="space-y-3">
          <form onSubmit={handleAddStringKey}>
            <Input
              label="Key Name"
              placeholder="Enter key name"
              value={newKeyName}
              onChange={(e) => setNewKeyName(e.target.value)}
              required
              compact
            />
            <Input
              label="Value"
              placeholder="Enter value"
              value={newKeyValue}
              onChange={(e) => setNewKeyValue(e.target.value)}
              required
              compact
            />
            <div className="flex justify-end gap-2 pt-2">
              <Button
                variant="ghost"
                onClick={() => setAddModalOpen(false)}
                type="button"
                size="sm"
                compact
              >
                Cancel
              </Button>
              <Button
                variant="primary"
                type="submit"
                loading={stringMutation.isPending}
                size="sm"
                compact
              >
                Add Key
              </Button>
            </div>
          </form>
        </div>
      </Modal>
      
      {/* View Key Modal - Compact */}
      <Modal
        isOpen={viewModalOpen}
        onClose={() => setViewModalOpen(false)}
        title={`Key: ${selectedKey}`}
        size="md"
        compact
      >
        <div className="space-y-3">
          {keyValue ? (
            <>
              <div className="flex items-center gap-2">
                <Badge variant={getKeyTypeColor('string')}>
                  {keyValue?.data?.data ? 'Data' : 'Empty'}
                </Badge>
                {keyValue?.data?.metadata?.executionTime && (
                  <span className="text-xs text-gray-500 dark:text-gray-400">
                    <Clock className="w-3 h-3 inline mr-1" />
                    {keyValue.data.metadata.executionTime}
                  </span>
                )}
              </div>
              <div className="bg-gray-50 dark:bg-gray-900 p-2 rounded">
                <pre className="text-xs text-gray-900 dark:text-gray-100 overflow-x-auto">
                  {JSON.stringify(keyValue?.data?.data, null, 2)}
                </pre>
              </div>
            </>
          ) : (
            <div className="text-center py-6">
              <Loader size="sm" text="Loading key value..." />
            </div>
          )}
          <div className="flex justify-end pt-2">
            <Button
              variant="primary"
              onClick={() => setViewModalOpen(false)}
              size="sm"
              compact
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