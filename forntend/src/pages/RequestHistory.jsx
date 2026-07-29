import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import Typography from '../components/ui/Typography';
import Loader from '../components/ui/Loader';

// Use the proxy configuration for development
const API_BASE = '/api';

/**
 * Request History Page (compact layout)
 *
 * This page displays the history of API requests with performance metrics.
 */
const RequestHistory = () => {
  const [filter, setFilter] = useState('ALL');

  // Fetch cache statistics (this serves as our request history for now)
  const { data: stats, isLoading } = useQuery({
    queryKey: ['cache-statistics'],
    queryFn: async () => {
      try {
        const response = await axios.get(`${API_BASE}/cache-playground/statistics`);
        // Handle different response formats
        return response.data.data || response.data || {
          totalRequests: 0,
          cacheHits: 0,
          cacheMisses: 0,
          hitRatio: '0%',
          missRatio: '0%',
          averageResponseTime: '0ms',
          redisReads: 0,
          redisWrites: 0,
          databaseReads: 0,
          databaseWrites: 0,
          evictions: 0,
          expiredKeys: 0,
          cacheTypeStats: {}
        };
      } catch (err) {
        console.log('Cache statistics API not available yet, returning mock data');
        return {
          totalRequests: 0,
          cacheHits: 0,
          cacheMisses: 0,
          hitRatio: '0%',
          missRatio: '0%',
          averageResponseTime: '0ms',
          redisReads: 0,
          redisWrites: 0,
          databaseReads: 0,
          databaseWrites: 0,
          evictions: 0,
          expiredKeys: 0,
          cacheTypeStats: {}
        };
      }
    },
    refetchInterval: 5000 // Refetch every 5 seconds
  });

  if (isLoading) return <Loader />;

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex flex-wrap justify-between items-center gap-2">
        <Typography variant="h2" className="text-xl font-bold">Request History</Typography>
        <div className="flex gap-1.5">
          <Button
            size="sm"
            variant={filter === 'ALL' ? 'primary' : 'secondary'}
            onClick={() => setFilter('ALL')}
          >
            All
          </Button>
          <Button
            size="sm"
            variant={filter === 'HIT' ? 'primary' : 'secondary'}
            onClick={() => setFilter('HIT')}
          >
            Cache Hits
          </Button>
          <Button
            size="sm"
            variant={filter === 'MISS' ? 'primary' : 'secondary'}
            onClick={() => setFilter('MISS')}
          >
            Cache Misses
          </Button>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
        <Card title="Total Requests" className="p-3">
          <Typography variant="h2" className="text-xl font-bold">{stats?.totalRequests || 0}</Typography>
        </Card>
        <Card title="Cache Hits" className="p-3">
          <Typography variant="h2" className="text-xl font-bold text-green-600 dark:text-green-400">
            {stats?.cacheHits || 0}
          </Typography>
        </Card>
        <Card title="Cache Misses" className="p-3">
          <Typography variant="h2" className="text-xl font-bold text-red-600 dark:text-red-400">
            {stats?.cacheMisses || 0}
          </Typography>
        </Card>
        <Card title="Hit Ratio" className="p-3">
          <Typography variant="h2" className="text-xl font-bold text-blue-600 dark:text-blue-400">
            {stats?.hitRatio || '0%'}
          </Typography>
        </Card>
      </div>

      {/* Performance Metrics */}
      <Card title="Performance Metrics" className="p-3">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
          <div className="text-center p-2.5 bg-gray-50 dark:bg-gray-800 rounded-md">
            <Typography variant="h4" className="text-xs text-gray-500 dark:text-gray-400">
              Avg Response Time
            </Typography>
            <Typography variant="h3" className="text-base font-semibold text-gray-900 dark:text-gray-100">
              {stats?.averageResponseTime || '0ms'}
            </Typography>
          </div>
          <div className="text-center p-2.5 bg-gray-50 dark:bg-gray-800 rounded-md">
            <Typography variant="h4" className="text-xs text-gray-500 dark:text-gray-400">
              Redis Reads
            </Typography>
            <Typography variant="h3" className="text-base font-semibold text-gray-900 dark:text-gray-100">
              {stats?.redisReads || 0}
            </Typography>
          </div>
          <div className="text-center p-2.5 bg-gray-50 dark:bg-gray-800 rounded-md">
            <Typography variant="h4" className="text-xs text-gray-500 dark:text-gray-400">
              Redis Writes
            </Typography>
            <Typography variant="h3" className="text-base font-semibold text-gray-900 dark:text-gray-100">
              {stats?.redisWrites || 0}
            </Typography>
          </div>
          <div className="text-center p-2.5 bg-gray-50 dark:bg-gray-800 rounded-md">
            <Typography variant="h4" className="text-xs text-gray-500 dark:text-gray-400">
              Database Reads
            </Typography>
            <Typography variant="h3" className="text-base font-semibold text-gray-900 dark:text-gray-100">
              {stats?.databaseReads || 0}
            </Typography>
          </div>
        </div>
      </Card>

      {/* Cache Statistics by Type */}
      <Card title="Cache Statistics by Type" className="p-3">
        <div className="space-y-2">
          {stats?.cacheTypeStats && Object.entries(stats.cacheTypeStats).map(([type, typeStats]) => (
            <div key={type} className="p-2.5 border border-gray-200 dark:border-gray-700 rounded-md">
              <div className="flex justify-between items-center mb-1.5">
                <Typography variant="h4" className="text-sm font-semibold text-gray-900 dark:text-gray-100">
                  {type}
                </Typography>
                <Typography variant="small" className="text-xs text-gray-500 dark:text-gray-400">
                  Hit Ratio: {typeStats.hitRatio?.toFixed(2)}% | Avg Time: {typeStats.averageResponseTime?.toFixed(2)}ms
                </Typography>
              </div>
              <div className="grid grid-cols-4 gap-2 text-xs text-gray-600 dark:text-gray-400">
                <div>Hits: {typeStats.hits}</div>
                <div>Misses: {typeStats.misses}</div>
                <div>Writes: {typeStats.writes}</div>
                <div>DB Writes: {typeStats.databaseWrites}</div>
              </div>
            </div>
          ))}
        </div>
      </Card>

      {/* Actions */}
      <div className="flex gap-2">
        <Button size="sm" onClick={() => window.location.reload()}>Refresh</Button>
        <Button
          size="sm"
          variant="secondary"
          onClick={async () => {
            try {
              await axios.post(`${API_BASE}/cache-playground/statistics/reset`);
              window.location.reload();
            } catch (err) {
              console.log('Reset statistics API not available yet');
              toast.error('API not available');
            }
          }}
        >
          Reset Statistics
        </Button>
      </div>
    </div>
  );
};

export default RequestHistory;