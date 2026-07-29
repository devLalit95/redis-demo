import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { Card, CardHeader, CardTitle, CardBody } from '../components/ui/Card';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import Modal from '../components/ui/Modal';
import Typography from '../components/ui/Typography';
import Loader from '../components/ui/Loader';
import { toast } from 'react-hot-toast';
import { Clock, Database, Zap } from 'lucide-react';

// Use the proxy configuration for development
const API_BASE = '/api';

/**
 * Student CRUD Page
 * 
 * This page provides complete CRUD operations for students.
 * It demonstrates the integration with the backend Student module.
 */
const StudentCRUD = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingStudent, setEditingStudent] = useState(null);
  const [formData, setFormData] = useState({
    rollNumber: '',
    name: '',
    email: '',
    phone: '',
    course: '',
    branch: '',
    semester: '',
    cgpa: '',
    city: '',
    address: '',
    status: 'ACTIVE'
  });
  const [timingData, setTimingData] = useState({
    noCache: null,
    manualCache: null,
    springCache: null,
    comparison: null,
    lastUpdated: null
  });
  const [isLoadingTimings, setIsLoadingTimings] = useState(false);
  const [studentTimings, setStudentTimings] = useState({});

  const queryClient = useQueryClient();

  // Function to compare cache performance
  const compareCachePerformance = async () => {
    setIsLoadingTimings(true);
    try {
      console.log('Starting performance comparison...');
      
      // Measure SQL Database time
      const sqlStart = Date.now();
      const noCacheResponse = await axios.get(`${API_BASE}/cache-playground/no-cache/all`);
      const sqlTime = Date.now() - sqlStart;
      console.log('No cache response:', noCacheResponse.data);
      
      // Measure Redis Cache time
      const redisStart = Date.now();
      const manualCacheResponse = await axios.get(`${API_BASE}/cache-playground/manual-cache/all`);
      const redisTime = Date.now() - redisStart;
      console.log('Manual cache response:', manualCacheResponse.data);
      
      // Try to get backend execution time if available
      let noCacheTime = `${sqlTime} ms`;
      let manualCacheTime = `${redisTime} ms`;
      
      if (noCacheResponse.data?.metadata?.executionTime) {
        noCacheTime = noCacheResponse.data.metadata.executionTime;
      }
      
      if (manualCacheResponse.data?.metadata?.executionTime) {
        manualCacheTime = manualCacheResponse.data.metadata.executionTime;
      }
      
      // Parse times for comparison
      const parseTime = (timeStr) => {
        if (!timeStr) return 0;
        const match = timeStr.match(/(\d+)/);
        return match ? parseInt(match[1]) : 0;
      };
      
      const noCacheMs = parseTime(noCacheTime);
      const manualCacheMs = parseTime(manualCacheTime);
      
      console.log('Parsed times - SQL:', noCacheMs, 'ms, Redis:', manualCacheMs, 'ms');
      
      // Calculate speed improvements
      const manualSpeedup = noCacheMs > 0 ? ((noCacheMs - manualCacheMs) / noCacheMs * 100).toFixed(1) : 0;
      
      // Calculate time difference
      const manualDiff = noCacheMs - manualCacheMs;
      
      const comparisonData = {
        noCache: noCacheTime,
        manualCache: manualCacheTime,
        comparison: {
          manualSpeedup,
          fastest: manualCacheMs < noCacheMs ? 'Redis Cache' : 'SQL Database',
          fastestTime: Math.min(manualCacheMs, noCacheMs),
          manualDiff: `${manualDiff}ms`
        },
        lastUpdated: new Date().toLocaleTimeString()
      };
      
      console.log('Setting timing data:', comparisonData);
      setTimingData(comparisonData);
      
      toast.success(`Performance comparison completed: Redis is ${manualSpeedup}% faster`);
    } catch (error) {
      console.error('Error comparing performance:', error);
      console.error('Error details:', error.response?.data || error.message);
      
      // Set default values on error to show something happened
      setTimingData({
        noCache: 'Error',
        manualCache: 'Error',
        comparison: {
          manualSpeedup: 0,
          fastest: 'Unknown',
          fastestTime: 0,
          manualDiff: '0ms'
        },
        lastUpdated: new Date().toLocaleTimeString()
      });
      
      toast.error('Failed to compare performance: ' + (error.response?.data?.message || error.message));
    } finally {
      setIsLoadingTimings(false);
    }
  };

  // Fetch all students
  const { data: students, isLoading, error } = useQuery({
    queryKey: ['students'],
    queryFn: async () => {
      try {
        const response = await axios.get(`${API_BASE}/students`);
        // Backend returns the array directly, not wrapped in data.data
        return Array.isArray(response.data) ? response.data : response.data.data || [];
      } catch (err) {
        console.log('Students API not available yet, returning empty array');
        return [];
      }
    }
  });

  // Function to fetch individual student with timing comparison
  const fetchStudentWithTiming = async (studentId) => {
    try {
      setStudentTimings(prev => ({ ...prev, [studentId]: { loading: true } }));
      
      console.log('Testing student:', studentId);
      
      // Measure SQL Database time
      const sqlStart = Date.now();
      const noCacheResponse = await axios.get(`${API_BASE}/cache-playground/no-cache/${studentId}`);
      const sqlTime = Date.now() - sqlStart;
      console.log('No cache response for student:', noCacheResponse.data);
      
      // Measure Redis Cache time
      const redisStart = Date.now();
      const manualCacheResponse = await axios.get(`${API_BASE}/cache-playground/manual-cache/${studentId}`);
      const redisTime = Date.now() - redisStart;
      console.log('Manual cache response for student:', manualCacheResponse.data);
      
      // Try to get backend execution time if available
      let noCacheTime = `${sqlTime} ms`;
      let manualCacheTime = `${redisTime} ms`;
      
      if (noCacheResponse.data?.metadata?.executionTime) {
        noCacheTime = noCacheResponse.data.metadata.executionTime;
      }
      
      if (manualCacheResponse.data?.metadata?.executionTime) {
        manualCacheTime = manualCacheResponse.data.metadata.executionTime;
      }
      
      // Parse times for comparison
      const parseTime = (timeStr) => {
        if (!timeStr) return 0;
        const match = timeStr.match(/(\d+)/);
        return match ? parseInt(match[1]) : 0;
      };
      
      const noCacheMs = parseTime(noCacheTime);
      const manualCacheMs = parseTime(manualCacheTime);
      
      console.log('Student parsed times - SQL:', noCacheMs, 'ms, Redis:', manualCacheMs, 'ms');
      
      // Calculate speed improvement
      const speedup = noCacheMs > 0 ? ((noCacheMs - manualCacheMs) / noCacheMs * 100).toFixed(1) : 0;
      const timeDiff = noCacheMs - manualCacheMs;
      
      const studentTimingData = {
        loading: false,
        sqlTime: noCacheTime,
        redisTime: manualCacheTime,
        speedup,
        timeDiff: `${timeDiff}ms`,
        lastUpdated: new Date().toLocaleTimeString()
      };
      
      console.log('Setting student timing data:', studentTimingData);
      setStudentTimings({
        [studentId]: studentTimingData
      });
      
      toast.success(`SQL vs Redis comparison for student ${studentId}: Redis is ${speedup}% faster`);
    } catch (error) {
      console.error('Error fetching student timing:', error);
      console.error('Error details:', error.response?.data || error.message);
      
      setStudentTimings(prev => ({ 
        ...prev, 
        [studentId]: { loading: false, error: true, sqlTime: 'Error', redisTime: 'Error' } 
      }));
      
      toast.error('Failed to compare performance: ' + (error.response?.data?.message || error.message));
    }
  };

  // Create student mutation
  const createMutation = useMutation({
    mutationFn: async (studentData) => {
      try {
        const response = await axios.post(`${API_BASE}/students`, studentData);
        return response.data;
      } catch (err) {
        console.log('Create student API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['students']);
      toast.success('Student created successfully');
      setIsModalOpen(false);
      resetForm();
    },
    onError: (error) => {
      toast.error('Failed to create student: ' + error.message);
    }
  });

  // Update student mutation
  const updateMutation = useMutation({
    mutationFn: async ({ id, studentData }) => {
      try {
        const response = await axios.put(`${API_BASE}/students/${id}`, studentData);
        return response.data;
      } catch (err) {
        console.log('Update student API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['students']);
      toast.success('Student updated successfully');
      setIsModalOpen(false);
      setEditingStudent(null);
      resetForm();
    },
    onError: (error) => {
      toast.error('Failed to update student: ' + error.message);
    }
  });

  // Delete student mutation
  const deleteMutation = useMutation({
    mutationFn: async (id) => {
      try {
        await axios.delete(`${API_BASE}/students/${id}`);
      } catch (err) {
        console.log('Delete student API not available yet');
        throw new Error('API not available');
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries(['students']);
      toast.success('Student deleted successfully');
    },
    onError: (error) => {
      toast.error('Failed to delete student: ' + error.message);
    }
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    const studentData = {
      ...formData,
      semester: parseInt(formData.semester),
      cgpa: parseFloat(formData.cgpa)
    };

    if (editingStudent) {
      updateMutation.mutate({ id: editingStudent.id, studentData });
    } else {
      createMutation.mutate(studentData);
    }
  };

  const handleEdit = (student) => {
    setEditingStudent(student);
    setFormData({
      rollNumber: student.rollNumber || '',
      name: student.name || '',
      email: student.email || '',
      phone: student.phone || '',
      course: student.course || '',
      branch: student.branch || '',
      semester: student.semester?.toString() || '',
      cgpa: student.cgpa?.toString() || '',
      city: student.city || '',
      address: student.address || '',
      status: student.status || 'ACTIVE'
    });
    setIsModalOpen(true);
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this student?')) {
      deleteMutation.mutate(id);
    }
  };

  const resetForm = () => {
    setFormData({
      rollNumber: '',
      name: '',
      email: '',
      phone: '',
      course: '',
      branch: '',
      semester: '',
      cgpa: '',
      city: '',
      address: '',
      status: 'ACTIVE'
    });
    setEditingStudent(null);
  };

  const openModal = () => {
    resetForm();
    setIsModalOpen(true);
  };

  if (isLoading) return <Loader />;
  if (error) return <Typography variant="h3">Error loading students</Typography>;

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-3">
        <Typography variant="h2" className="text-lg sm:text-xl">Student Management</Typography>
        <Button 
          onClick={openModal}
          compact
          className="w-full sm:w-auto"
        >
          Add Student
        </Button>
      </div>

      {/* SQL vs Redis Performance Comparison Card */}
      <Card className="bg-gradient-to-r from-blue-50 to-purple-50 dark:from-blue-900/20 dark:to-purple-900/20 border-blue-200 dark:border-blue-800">
        <CardBody compact>
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center gap-2">
              <Zap className="w-4 h-4 text-yellow-600 dark:text-yellow-400" />
              <Typography variant="h4" className="text-sm text-blue-900 dark:text-blue-100">
                SQL vs Redis Performance
              </Typography>
            </div>
            <Button
              variant="primary"
              size="xs"
              onClick={compareCachePerformance}
              disabled={isLoadingTimings}
              compact
            >
              {isLoadingTimings ? 'Testing...' : 'Refresh'}
            </Button>
          </div>
          
          {timingData.comparison && timingData.noCache !== 'Error' ? (
            <>
              <div className="grid grid-cols-2 gap-2 mb-3">
                {/* SQL Database Time */}
                <div className="bg-white dark:bg-gray-800 rounded p-2 border border-gray-200 dark:border-gray-700">
                  <div className="flex items-center gap-1 mb-1">
                    <Database className="w-3 h-3 text-red-500" />
                    <span className="text-xs font-medium text-gray-700 dark:text-gray-300">SQL Database</span>
                  </div>
                  <div className="text-lg font-bold text-red-600 dark:text-red-400">
                    {timingData.noCache}
                  </div>
                  <div className="text-xs text-gray-500 dark:text-gray-400">
                    Direct database access
                  </div>
                </div>
                
                {/* Redis Cache Time */}
                <div className="bg-white dark:bg-gray-800 rounded p-2 border border-gray-200 dark:border-gray-700">
                  <div className="flex items-center gap-1 mb-1">
                    <Zap className="w-3 h-3 text-green-500" />
                    <span className="text-xs font-medium text-gray-700 dark:text-gray-300">Redis Cache</span>
                  </div>
                  <div className="text-lg font-bold text-green-600 dark:text-green-400">
                    {timingData.manualCache}
                  </div>
                  <div className="text-xs text-green-600 dark:text-green-400 font-medium">
                    {timingData.comparison.manualSpeedup}% faster
                  </div>
                </div>
              </div>
              
              {/* Time Difference Summary */}
              <div className="bg-white dark:bg-gray-800 rounded p-2 border border-gray-200 dark:border-gray-700">
                <div className="flex items-center justify-between mb-1">
                  <span className="text-xs font-medium text-gray-700 dark:text-gray-300">Time Difference</span>
                  <span className="text-xs text-gray-500 dark:text-gray-400">
                    Updated: {timingData.lastUpdated}
                  </span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-lg font-bold text-green-600 dark:text-green-400">
                    {timingData.comparison.manualDiff}
                  </span>
                  <span className="text-xs text-gray-600 dark:text-gray-400">
                    Redis is {timingData.comparison.manualSpeedup}% faster than SQL
                  </span>
                </div>
              </div>
            </>
          ) : timingData.comparison && timingData.noCache === 'Error' ? (
            <div className="text-center py-4">
              <p className="text-xs text-red-600 dark:text-red-400 mb-2">
                Error fetching performance data
              </p>
              <Button
                variant="danger"
                size="sm"
                onClick={compareCachePerformance}
                disabled={isLoadingTimings}
                compact
              >
                Retry
              </Button>
            </div>
          ) : (
            <div className="text-center py-4">
              <p className="text-xs text-gray-600 dark:text-gray-400 mb-2">
                Click "Refresh" to compare SQL vs Redis performance
              </p>
              <Button
                variant="primary"
                size="sm"
                onClick={compareCachePerformance}
                disabled={isLoadingTimings}
                compact
              >
                {isLoadingTimings ? 'Testing...' : 'Start Comparison'}
              </Button>
            </div>
          )}
        </CardBody>
      </Card>

      <Card compact>
        <CardHeader compact>
          <CardTitle size="sm">Student List</CardTitle>
        </CardHeader>
        <CardBody compact>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b">
                  <th className="text-left p-2 text-xs">Roll No</th>
                  <th className="text-left p-2 text-xs">Name</th>
                  <th className="text-left p-2 text-xs hidden sm:table-cell">Email</th>
                  <th className="text-left p-2 text-xs hidden md:table-cell">Course</th>
                  <th className="text-left p-2 text-xs hidden lg:table-cell">Branch</th>
                  <th className="text-left p-2 text-xs hidden md:table-cell">Sem</th>
                  <th className="text-left p-2 text-xs hidden lg:table-cell">CGPA</th>
                  <th className="text-left p-2 text-xs hidden sm:table-cell">Status</th>
                  <th className="text-left p-2 text-xs">Performance</th>
                  <th className="text-left p-2 text-xs">Actions</th>
                </tr>
              </thead>
              <tbody>
                {students?.map((student) => {
                  const timing = studentTimings[student.id];
                  return (
                    <tr key={student.id} className="border-b hover:bg-gray-50">
                      <td className="p-2 text-xs">{student.rollNumber}</td>
                      <td className="p-2 text-xs font-medium">{student.name}</td>
                      <td className="p-2 text-xs hidden sm:table-cell">{student.email}</td>
                      <td className="p-2 text-xs hidden md:table-cell">{student.course}</td>
                      <td className="p-2 text-xs hidden lg:table-cell">{student.branch}</td>
                      <td className="p-2 text-xs hidden md:table-cell">{student.semester}</td>
                      <td className="p-2 text-xs hidden lg:table-cell">{student.cgpa}</td>
                      <td className="p-2 text-xs hidden sm:table-cell">
                        <span className={`px-1.5 py-0.5 rounded text-xs ${
                          student.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {student.status}
                        </span>
                      </td>
                      <td className="p-2 text-xs">
                        {timing?.loading ? (
                          <div className="flex items-center gap-1">
                            <Loader size="xs" />
                            <span className="text-xs text-gray-500">Testing...</span>
                          </div>
                        ) : timing?.error ? (
                          <div className="text-xs text-red-600">
                            Error
                          </div>
                        ) : timing?.sqlTime ? (
                          <div className="text-xs">
                            <div className="flex items-center gap-0.5 mb-0.5">
                              <Database className="w-2.5 h-2.5 text-red-500" />
                              <span className="text-gray-600">SQL: {timing.sqlTime}</span>
                            </div>
                            <div className="flex items-center gap-0.5 mb-0.5">
                              <Zap className="w-2.5 h-2.5 text-green-500" />
                              <span className="text-green-600">Redis: {timing.redisTime}</span>
                            </div>
                            <div className="flex items-center gap-0.5">
                              <span className="text-green-600 font-medium">{timing.speedup}% faster</span>
                              <span className="text-gray-400">({timing.timeDiff})</span>
                            </div>
                          </div>
                        ) : (
                          <Button
                            size="xs"
                            variant="secondary"
                            onClick={() => fetchStudentWithTiming(student.id)}
                          >
                            Test
                          </Button>
                        )}
                      </td>
                      <td className="p-2 text-xs">
                        <div className="flex gap-1">
                          <Button
                            size="xs"
                            variant="secondary"
                            onClick={() => handleEdit(student)}
                          >
                            Edit
                          </Button>
                          <Button
                            size="xs"
                            variant="danger"
                            onClick={() => handleDelete(student.id)}
                          >
                            Delete
                          </Button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </CardBody>
      </Card>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} size="lg" compact>
        <CardHeader compact>
          <CardTitle size="sm">{editingStudent ? 'Edit Student' : 'Add Student'}</CardTitle>
        </CardHeader>
        <CardBody compact>
        <form onSubmit={handleSubmit} className="space-y-3">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Input
              label="Roll Number"
              name="rollNumber"
              value={formData.rollNumber}
              onChange={handleInputChange}
              required
              placeholder="Roll number"
              compact
            />
            <Input
              label="Name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
              placeholder="Full name"
              compact
            />
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Input
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleInputChange}
              required
              placeholder="student@example.com"
              compact
            />
            <Input
              label="Phone"
              name="phone"
              value={formData.phone}
              onChange={handleInputChange}
              required
              placeholder="Phone number"
              compact
            />
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Input
              label="Course"
              name="course"
              value={formData.course}
              onChange={handleInputChange}
              required
              placeholder="B.Tech, B.Sc"
              compact
            />
            <Input
              label="Branch"
              name="branch"
              value={formData.branch}
              onChange={handleInputChange}
              required
              placeholder="CSE, ECE, ME"
              compact
            />
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Input
              label="Semester"
              name="semester"
              type="number"
              min="1"
              max="8"
              value={formData.semester}
              onChange={handleInputChange}
              required
              placeholder="1-8"
              compact
            />
            <Input
              label="CGPA"
              name="cgpa"
              type="number"
              step="0.01"
              min="0"
              max="10"
              value={formData.cgpa}
              onChange={handleInputChange}
              required
              placeholder="0.00-10.00"
              compact
            />
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Input
              label="City"
              name="city"
              value={formData.city}
              onChange={handleInputChange}
              required
              placeholder="City"
              compact
            />
            <Input
              label="Address"
              name="address"
              value={formData.address}
              onChange={handleInputChange}
              placeholder="Address (optional)"
              compact
            />
          </div>
          
          <div className="flex items-center gap-2 pt-2 border-t border-gray-200 dark:border-gray-700">
            <Button 
              type="submit" 
              disabled={createMutation.isPending || updateMutation.isPending}
              className="flex-1 sm:flex-none"
              compact
            >
              {createMutation.isPending || updateMutation.isPending ? 'Saving...' : (editingStudent ? 'Update' : 'Create')}
            </Button>
            <Button
              type="button"
              variant="secondary"
              onClick={() => setIsModalOpen(false)}
              className="flex-1 sm:flex-none"
              compact
            >
              Cancel
            </Button>
          </div>
        </form>
        </CardBody>
      </Modal>
    </div>
  );
};

export default StudentCRUD;