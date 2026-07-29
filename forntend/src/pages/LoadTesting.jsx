import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Typography from '../components/ui/Typography';
import Loader from '../components/ui/Loader';
import Progress from '../components/ui/Progress';
import { toast } from 'react-hot-toast';

// Use the proxy configuration for development
const API_BASE = '/api';

/**
 * Load Testing Page
 * 
 * This page provides load testing capabilities for performance testing.
 */
const LoadTesting = () => {
  const [requestCount, setRequestCount] = useState(100);
  const [threadCount, setThreadCount] = useState(5);
  const [selectedMode, setSelectedMode] = useState('COMPARISON');
  const [isRunning, setIsRunning] = useState(false);
  const [progress, setProgress] = useState(0);
  const queryClient = useQueryClient();

  // Load test mutations
  const noCacheMutation = useMutation({
    mutationFn: async (count) => {
      try {
        const response = await axios.post(`${API_BASE}/load-test/no-cache?requestCount=${count}`);
        return response.data;
      } catch (err) {
        console.log('Load test API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['load-test-results']);
      toast.success('No-cache load test completed');
      setIsRunning(false);
      setProgress(100);
    },
    onError: (error) => {
      toast.error('Load test failed: ' + error.message);
      setIsRunning(false);
      setProgress(0);
    }
  });

  const manualCacheMutation = useMutation({
    mutationFn: async (count) => {
      try {
        const response = await axios.post(`${API_BASE}/load-test/manual-cache?requestCount=${count}`);
        return response.data;
      } catch (err) {
        console.log('Load test API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['load-test-results']);
      toast.success('Manual cache load test completed');
      setIsRunning(false);
      setProgress(100);
    },
    onError: (error) => {
      toast.error('Load test failed: ' + error.message);
      setIsRunning(false);
      setProgress(0);
    }
  });

  const springCacheMutation = useMutation({
    mutationFn: async (count) => {
      try {
        const response = await axios.post(`${API_BASE}/load-test/spring-cache?requestCount=${count}`);
        return response.data;
      } catch (err) {
        console.log('Load test API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['load-test-results']);
      toast.success('Spring cache load test completed');
      setIsRunning(false);
      setProgress(100);
    },
    onError: (error) => {
      toast.error('Load test failed: ' + error.message);
      setIsRunning(false);
      setProgress(0);
    }
  });

  const comparisonMutation = useMutation({
    mutationFn: async (count) => {
      try {
        const response = await axios.post(`${API_BASE}/load-test/comparison?requestCount=${count}`);
        return response.data;
      } catch (err) {
        console.log('Load test API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['load-test-results']);
      toast.success('Comparison load test completed');
      setIsRunning(false);
      setProgress(100);
    },
    onError: (error) => {
      toast.error('Load test failed: ' + error.message);
      setIsRunning(false);
      setProgress(0);
    }
  });

  const concurrentMutation = useMutation({
    mutationFn: async ({ count, threads }) => {
      try {
        const response = await axios.post(
          `${API_BASE}/load-test/concurrent?requestCount=${count}&threadCount=${threads}`
        );
        return response.data;
      } catch (err) {
        console.log('Load test API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['load-test-results']);
      toast.success('Concurrent load test completed');
      setIsRunning(false);
      setProgress(100);
    },
    onError: (error) => {
      toast.error('Load test failed: ' + error.message);
      setIsRunning(false);
      setProgress(0);
    }
  });

  // Fetch latest results
  const { data: results } = useQuery({
    queryKey: ['load-test-results'],
    queryFn: async () => {
      try {
        const response = await axios.get(`${API_BASE}/load-test/comparison?requestCount=${requestCount}`);
        return response.data.data;
      } catch (err) {
        console.log('Load test API not available yet, returning mock data');
        return null;
      }
    },
    enabled: !isRunning
  });

  const handleRunTest = () => {
    setIsRunning(true);
    setProgress(0);

    // Simulate progress
    const progressInterval = setInterval(() => {
      setProgress(prev => {
        if (prev >= 90) {
          clearInterval(progressInterval);
          return 90;
        }
        return prev + 10;
      });
    }, 200);

    switch (selectedMode) {
      case 'NO_CACHE':
        noCacheMutation.mutate(requestCount);
        break;
      case 'MANUAL_CACHE':
        manualCacheMutation.mutate(requestCount);
        break;
      case 'SPRING_CACHE':
        springCacheMutation.mutate(requestCount);
        break;
      case 'COMPARISON':
        comparisonMutation.mutate(requestCount);
        break;
      case 'CONCURRENT':
        concurrentMutation.mutate({ count: requestCount, threads: threadCount });
        break;
      default:
        comparisonMutation.mutate(requestCount);
    }
  };

  return (
    <div className="space-y-6">
      <Typography variant="h2">Load Testing</Typography>

      <Card title="Test Configuration">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium mb-2">Request Count</label>
            <Input
              type="number"
              value={requestCount}
              onChange={(e) => setRequestCount(parseInt(e.target.value))}
              min="10"
              max="1000"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Thread Count</label>
            <Input
              type="number"
              value={threadCount}
              onChange={(e) => setThreadCount(parseInt(e.target.value))}
              min="1"
              max="20"
              disabled={selectedMode !== 'CONCURRENT'}
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Test Mode</label>
            <select
              value={selectedMode}
              onChange={(e) => setSelectedMode(e.target.value)}
              className="w-full p-2 border rounded"
            >
              <option value="COMPARISON">Full Comparison</option>
              <option value="NO_CACHE">No Cache Only</option>
              <option value="MANUAL_CACHE">Manual Cache Only</option>
              <option value="SPRING_CACHE">Spring Cache Only</option>
              <option value="CONCURRENT">Concurrent Test</option>
            </select>
          </div>
        </div>
        <div className="mt-4">
          <Button
            onClick={handleRunTest}
            disabled={isRunning}
            className="w-full"
          >
            {isRunning ? 'Running Test...' : 'Run Load Test'}
          </Button>
        </div>
        {isRunning && (
          <div className="mt-4">
            <Progress value={progress} />
          </div>
        )}
      </Card>

      {results && (
        <div className="space-y-4">
          <Typography variant="h3">Test Results</Typography>
          
          {results.noCache && (
            <Card title="No Cache Results">
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div>
                  <Typography variant="small">Avg Response Time</Typography>
                  <Typography variant="h4">{results.noCache.avgResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Min Response Time</Typography>
                  <Typography variant="h4">{results.noCache.minResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Max Response Time</Typography>
                  <Typography variant="h4">{results.noCache.maxResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Throughput</Typography>
                  <Typography variant="h4">{results.noCache.throughput}</Typography>
                </div>
              </div>
            </Card>
          )}

          {results.manualCache && (
            <Card title="Manual Cache Results">
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div>
                  <Typography variant="small">Avg Response Time</Typography>
                  <Typography variant="h4">{results.manualCache.avgResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Min Response Time</Typography>
                  <Typography variant="h4">{results.manualCache.minResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Max Response Time</Typography>
                  <Typography variant="h4">{results.manualCache.maxResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Throughput</Typography>
                  <Typography variant="h4">{results.manualCache.throughput}</Typography>
                </div>
              </div>
            </Card>
          )}

          {results.springCache && (
            <Card title="Spring Cache Results">
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div>
                  <Typography variant="small">Avg Response Time</Typography>
                  <Typography variant="h4">{results.springCache.avgResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Min Response Time</Typography>
                  <Typography variant="h4">{results.springCache.minResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Max Response Time</Typography>
                  <Typography variant="h4">{results.springCache.maxResponseTime}</Typography>
                </div>
                <div>
                  <Typography variant="small">Throughput</Typography>
                  <Typography variant="h4">{results.springCache.throughput}</Typography>
                </div>
              </div>
            </Card>
          )}

          {results.manualImprovement && (
            <Card title="Performance Improvement">
              <div className="grid grid-cols-2 gap-4">
                <div className="text-center p-4 bg-green-50 rounded">
                  <Typography variant="h4">Manual Cache</Typography>
                  <Typography variant="h2" className="text-green-600">
                    {results.manualImprovement}
                  </Typography>
                </div>
                <div className="text-center p-4 bg-blue-50 rounded">
                  <Typography variant="h4">Spring Cache</Typography>
                  <Typography variant="h2" className="text-blue-600">
                    {results.springImprovement}
                  </Typography>
                </div>
              </div>
            </Card>
          )}
        </div>
      )}
    </div>
  );
};

export default LoadTesting;