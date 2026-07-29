import React from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import MainLayout from './components/layout/MainLayout';
import Dashboard from './pages/Dashboard';
import RedisExplorer from './pages/RedisExplorer';
import StringOperations from './pages/StringOperations';
import RedisMonitor from './pages/RedisMonitor';
import HashOperations from './pages/HashOperations';
import CounterOperations from './pages/CounterOperations';
import CachePlayground from './pages/CachePlayground';
import ListOperations from './pages/ListOperations';
import SetOperations from './pages/SetOperations';
import StudentCRUD from './pages/StudentCRUD';
import PerformanceComparison from './pages/PerformanceComparison';
import RequestHistory from './pages/RequestHistory';
import LoadTesting from './pages/LoadTesting';

// Create React Query client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        // Don't retry on CORS/network errors
        if (error.code === 'ERR_NETWORK' || error.message?.includes('Network Error')) {
          return false;
        }
        // Don't retry on 500 errors (server errors)
        if (error.status === 500) {
          return false;
        }
        // Don't retry on 4xx client errors
        if (error.status >= 400 && error.status < 500) {
          return false;
        }
        // Retry up to 2 times for other errors
        return failureCount < 2;
      },
      retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 3000), // Exponential backoff with max 3s
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000, // 5 minutes
      refetchOnReconnect: true,
      refetchOnMount: false,
    },
    mutations: {
      retry: (failureCount, error) => {
        // Don't retry on CORS/network errors
        if (error.code === 'ERR_NETWORK' || error.message?.includes('Network Error')) {
          return false;
        }
        // Don't retry on 500 errors
        if (error.status === 500) {
          return false;
        }
        // Don't retry on 4xx client errors
        if (error.status >= 400 && error.status < 500) {
          return false;
        }
        // Retry once for other errors
        return failureCount < 1;
      },
    },
  },
});

/**
 * Main App Component
 */
function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <MainLayout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/redis-explorer" element={<RedisExplorer />} />
            <Route path="/string-operations" element={<StringOperations />} />
            <Route path="/hash-operations" element={<HashOperations />} />
            <Route path="/list-operations" element={<ListOperations />} />
            <Route path="/set-operations" element={<SetOperations />} />
            <Route path="/counter-operations" element={<CounterOperations />} />
            <Route path="/cache-playground" element={<CachePlayground />} />
            <Route path="/redis-monitor" element={<RedisMonitor />} />
            <Route path="/student-crud" element={<StudentCRUD />} />
            <Route path="/performance-comparison" element={<PerformanceComparison />} />
            <Route path="/request-history" element={<RequestHistory />} />
            <Route path="/load-testing" element={<LoadTesting />} />
            {/* Add more routes as we implement them */}
          </Routes>
        </MainLayout>
      </BrowserRouter>
      <Toaster position="top-right" />
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}

export default App;