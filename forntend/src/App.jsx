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

// Create React Query client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        // Don't retry on CORS/network errors
        if (error.code === 'ERR_NETWORK' || error.message?.includes('Network Error')) {
          return false;
        }
        // Retry up to 3 times for other errors
        return failureCount < 3;
      },
      retryDelay: 1000,
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
    mutations: {
      retry: (failureCount, error) => {
        // Don't retry on CORS/network errors
        if (error.code === 'ERR_NETWORK' || error.message?.includes('Network Error')) {
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
            <Route path="/counter-operations" element={<CounterOperations />} />
            <Route path="/cache-playground" element={<CachePlayground />} />
            <Route path="/redis-monitor" element={<RedisMonitor />} />
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