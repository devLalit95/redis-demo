import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Typography from '../components/ui/Typography';
import Loader from '../components/ui/Loader';
import { toast } from 'react-hot-toast';

// Use the proxy configuration for development
const API_BASE = '/api';

/**
 * Performance Comparison Page
 * 
 * This page provides performance comparison between different caching strategies.
 */
const PerformanceComparison = () => {
  const [requestCount, setRequestCount] = useState(100);
  const [isRunning, setIsRunning] = useState(false);
  const queryClient = useQueryClient();

  // Load test comparison mutation
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
      queryClient.invalidateQueries(['performance-comparison']);
      toast.success('Load test completed successfully');
      setIsRunning(false);
    },
    onError: (error) => {
      toast.error('Load test failed: ' + error.message);
      setIsRunning(false);
    }
  });

  // Fetch comparison results
  const { data: comparisonData, isLoading } = useQuery({
    queryKey: ['performance-comparison'],
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

  const handleRunComparison = () => {
    setIsRunning(true);
    comparisonMutation.mutate(requestCount);
  };

  if (isLoading) return <Loader />;

  const comparison = comparisonData;

  return (
    <div className="space-y-6">
      <Typography variant="h2">Performance Comparison</Typography>

      <Card>
        <div className="flex gap-4 items-end">
          <div className="flex-1">
            <Input
              label="Request Count"
              type="number"
              value={requestCount}
              onChange={(e) => setRequestCount(parseInt(e.target.value))}
              min="10"
              max="1000"
            />
          </div>
          <Button
            onClick={handleRunComparison}
            disabled={isRunning || comparisonMutation.isPending}
          >
            {isRunning || comparisonMutation.isPending ? 'Running...' : 'Run Comparison'}
          </Button>
        </div>
      </Card>

      {comparison && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* No Cache Results */}
          <Card title="No Cache">
            <div className="space-y-2">
              <div className="flex justify-between">
                <span>Avg Response Time:</span>
                <span className="font-bold">{comparison.noCache?.avgResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Min Response Time:</span>
                <span>{comparison.noCache?.minResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Max Response Time:</span>
                <span>{comparison.noCache?.maxResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Throughput:</span>
                <span>{comparison.noCache?.throughput}</span>
              </div>
            </div>
          </Card>

          {/* Manual Cache Results */}
          <Card title="Manual Cache">
            <div className="space-y-2">
              <div className="flex justify-between">
                <span>Avg Response Time:</span>
                <span className="font-bold">{comparison.manualCache?.avgResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Min Response Time:</span>
                <span>{comparison.manualCache?.minResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Max Response Time:</span>
                <span>{comparison.manualCache?.maxResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Throughput:</span>
                <span>{comparison.manualCache?.throughput}</span>
              </div>
            </div>
          </Card>

          {/* Spring Cache Results */}
          <Card title="Spring Cache">
            <div className="space-y-2">
              <div className="flex justify-between">
                <span>Avg Response Time:</span>
                <span className="font-bold">{comparison.springCache?.avgResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Min Response Time:</span>
                <span>{comparison.springCache?.minResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Max Response Time:</span>
                <span>{comparison.springCache?.maxResponseTime}</span>
              </div>
              <div className="flex justify-between">
                <span>Throughput:</span>
                <span>{comparison.springCache?.throughput}</span>
              </div>
            </div>
          </Card>
        </div>
      )}

      {comparison && (
        <Card title="Performance Improvement">
          <div className="grid grid-cols-2 gap-4">
            <div className="text-center p-4 bg-green-50 rounded">
              <Typography variant="h3">Manual Cache Improvement</Typography>
              <Typography variant="h2" className="text-green-600">
                {comparison.manualImprovement}
              </Typography>
            </div>
            <div className="text-center p-4 bg-blue-50 rounded">
              <Typography variant="h3">Spring Cache Improvement</Typography>
              <Typography variant="h2" className="text-blue-600">
                {comparison.springImprovement}
              </Typography>
            </div>
          </div>
          <div className="mt-4 text-center text-gray-600">
            Total Test Time: {comparison.totalTestTime}
          </div>
        </Card>
      )}
    </div>
  );
};

export default PerformanceComparison;