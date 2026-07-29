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
 * Request History Page
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
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <Typography variant="h2">Request History</Typography>
        <div className="flex gap-2">
          <Button
            variant={filter === 'ALL' ? 'primary' : 'secondary'}
            onClick={() => setFilter('ALL')}
          >
            All
          </Button>
          <Button
            variant={filter === 'HIT' ? 'primary' : 'secondary'}
            onClick={() => setFilter('HIT')}
          >
            Cache Hits
          </Button>
          <Button
            variant={filter === 'MISS' ? 'primary' : 'secondary'}
            onClick={() => setFilter('MISS')}
          >
            Cache Misses
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card title="Total Requests">
          <Typography variant="h2">{stats?.totalRequests || 0}</Typography>
        </Card>
        <Card title="Cache Hits">
          <Typography variant="h2" className="text-green-600">{stats?.cacheHits || 0}</Typography>
        </Card>
        <Card title="Cache Misses">
          <Typography variant="h2" className="text-red-600">{stats?.cacheMisses || 0}</Typography>
        </Card>
        <Card title="Hit Ratio">
          <Typography variant="h2" className="text-blue-600">{stats?.hitRatio || '0%'}</Typography>
        </Card>
      </div>

      <Card title="Performance Metrics">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div className="text-center p-4 bg-gray-50 rounded">
            <Typography variant="h4">Average Response Time</Typography>
            <Typography variant="h3">{stats?.averageResponseTime || '0ms'}</Typography>
          </div>
          <div className="text-center p-4 bg-gray-50 rounded">
            <Typography variant="h4">Redis Reads</Typography>
            <Typography variant="h3">{stats?.redisReads || 0}</Typography>
          </div>
          <div className="text-center p-4 bg-gray-50 rounded">
            <Typography variant="h4">Redis Writes</Typography>
            <Typography variant="h3">{stats?.redisWrites || 0}</Typography>
          </div>
          <div className="text-center p-4 bg-gray-50 rounded">
            <Typography variant="h4">Database Reads</Typography>
            <Typography variant="h3">{stats?.databaseReads || 0}</Typography>
          </div>
        </div>
      </Card>

      <Card title="Cache Statistics by Type">
        <div className="space-y-4">
          {stats?.cacheTypeStats && Object.entries(stats.cacheTypeStats).map(([type, typeStats]) => (
            <div key={type} className="p-4 border rounded">
              <div className="flex justify-between items-center mb-2">
                <Typography variant="h4">{type}</Typography>
                <Typography variant="small">
                  Hit Ratio: {typeStats.hitRatio?.toFixed(2)}% |
                  Avg Time: {typeStats.averageResponseTime?.toFixed(2)}ms
                </Typography>
              </div>
              <div className="grid grid-cols-4 gap-2 text-sm">
                <div>Hits: {typeStats.hits}</div>
                <div>Misses: {typeStats.misses}</div>
                <div>Writes: {typeStats.writes}</div>
                <div>DB Writes: {typeStats.databaseWrites}</div>
              </div>
            </div>
          ))}
        </div>
      </Card>

      <div className="flex gap-2">
        <Button onClick={() => window.location.reload()}>Refresh</Button>
        <Button
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