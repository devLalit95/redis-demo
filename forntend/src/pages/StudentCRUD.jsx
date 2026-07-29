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
import { Clock, Database, Zap, BarChart3 } from 'lucide-react';

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
    comparison: null
  });
  const [isLoadingTimings, setIsLoadingTimings] = useState(false);
  const [studentTimings, setStudentTimings] = useState({});

  const queryClient = useQueryClient();

  // Function to compare cache performance
  const compareCachePerformance = async () => {
    setIsLoadingTimings(true);
    try {
      const startTime = Date.now();
      
      // Fetch without cache
      const noCacheResponse = await axios.get(`${API_BASE}/cache-playground/no-cache/all`);
      const noCacheTime = noCacheResponse.data?.metadata?.executionTime || '0 ms';
      
      // Fetch with manual cache
      const manualCacheResponse = await axios.get(`${API_BASE}/cache-playground/manual-cache/all`);
      const manualCacheTime = manualCacheResponse.data?.metadata?.executionTime || '0 ms';
      
      // Fetch with Spring cache
      const springCacheResponse = await axios.get(`${API_BASE}/cache-playground/spring-cache/all`);
      const springCacheTime = springCacheResponse.data?.metadata?.executionTime || '0 ms';
      
      const endTime = Date.now();
      
      // Parse times for comparison
      const parseTime = (timeStr) => {
        const match = timeStr.match(/(\d+)/);
        return match ? parseInt(match[1]) : 0;
      };
      
      const noCacheMs = parseTime(noCacheTime);
      const manualCacheMs = parseTime(manualCacheTime);
      const springCacheMs = parseTime(springCacheTime);
      
      // Calculate speed improvements
      const manualSpeedup = noCacheMs > 0 ? ((noCacheMs - manualCacheMs) / noCacheMs * 100).toFixed(1) : 0;
      const springSpeedup = noCacheMs > 0 ? ((noCacheMs - springCacheMs) / noCacheMs * 100).toFixed(1) : 0;
      
      setTimingData({
        noCache: noCacheTime,
        manualCache: manualCacheTime,
        springCache: springCacheTime,
        comparison: {
          manualSpeedup,
          springSpeedup,
          fastest: manualCacheMs < springCacheMs ? 'Manual Cache' : 'Spring Cache',
          fastestTime: Math.min(manualCacheMs, springCacheMs)
        }
      });
      
      toast.success('Performance comparison completed');
    } catch (error) {
      console.error('Error comparing performance:', error);
      toast.error('Failed to compare performance');
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
      
      const startTime = Date.now();
      
      // Fetch without cache
      const noCacheResponse = await axios.get(`${API_BASE}/cache-playground/no-cache/${studentId}`);
      const noCacheTime = noCacheResponse.data?.metadata?.executionTime || '0 ms';
      
      // Fetch with manual cache
      const manualCacheResponse = await axios.get(`${API_BASE}/cache-playground/manual-cache/${studentId}`);
      const manualCacheTime = manualCacheResponse.data?.metadata?.executionTime || '0 ms';
      
      // Fetch with Spring cache
      const springCacheResponse = await axios.get(`${API_BASE}/cache-playground/spring-cache/${studentId}`);
      const springCacheTime = springCacheResponse.data?.metadata?.executionTime || '0 ms';
      
      const endTime = Date.now();
      
      // Parse times for comparison
      const parseTime = (timeStr) => {
        const match = timeStr.match(/(\d+)/);
        return match ? parseInt(match[1]) : 0;
      };
      
      const noCacheMs = parseTime(noCacheTime);
      const manualCacheMs = parseTime(manualCacheTime);
      const springCacheMs = parseTime(springCacheTime);
      
      // Calculate speed improvements
      const manualSpeedup = noCacheMs > 0 ? ((noCacheMs - manualCacheMs) / noCacheMs * 100).toFixed(1) : 0;
      const springSpeedup = noCacheMs > 0 ? ((noCacheMs - springCacheMs) / noCacheMs * 100).toFixed(1) : 0;
      
      setStudentTimings({
        [studentId]: {
          loading: false,
          noCache: noCacheTime,
          manualCache: manualCacheTime,
          springCache: springCacheTime,
          comparison: {
            manualSpeedup,
            springSpeedup,
            fastest: manualCacheMs < springCacheMs ? 'Manual Cache' : 'Spring Cache',
            fastestTime: Math.min(manualCacheMs, springCacheMs)
          }
        }
      });
      
      toast.success(`Performance comparison for student ${studentId} completed`);
    } catch (error) {
      console.error('Error fetching student timing:', error);
      setStudentTimings(prev => ({ 
        ...prev, 
        [studentId]: { loading: false, error: true } 
      }));
      toast.error('Failed to compare performance for this student');
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
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <Typography variant="h2">Student Management</Typography>
        <div className="flex gap-3">
          <Button 
            variant="secondary" 
            onClick={compareCachePerformance}
            disabled={isLoadingTimings}
            leftIcon={<BarChart3 className="w-4 h-4" />}
          >
            {isLoadingTimings ? 'Comparing...' : 'Compare Performance'}
          </Button>
          <Button onClick={openModal}>Add Student</Button>
        </div>
      </div>

      {/* Performance Comparison Card */}
      {timingData.comparison && (
        <Card className="bg-gradient-to-r from-blue-50 to-purple-50 dark:from-blue-900/20 dark:to-purple-900/20 border-blue-200 dark:border-blue-800">
          <CardBody>
            <div className="flex items-center gap-2 mb-4">
              <Zap className="w-5 h-5 text-yellow-600 dark:text-yellow-400" />
              <Typography variant="h4" className="text-blue-900 dark:text-blue-100">
                Performance Comparison
              </Typography>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
              {/* No Cache */}
              <div className="bg-white dark:bg-gray-800 rounded-lg p-4 border border-gray-200 dark:border-gray-700">
                <div className="flex items-center gap-2 mb-2">
                  <Database className="w-4 h-4 text-red-500" />
                  <span className="text-sm font-medium text-gray-700 dark:text-gray-300">No Cache</span>
                </div>
                <div className="text-2xl font-bold text-gray-900 dark:text-gray-100">
                  {timingData.noCache}
                </div>
                <div className="text-xs text-gray-500 dark:text-gray-400 mt-1">
                  Direct database access
                </div>
              </div>
              
              {/* Manual Cache */}
              <div className="bg-white dark:bg-gray-800 rounded-lg p-4 border border-gray-200 dark:border-gray-700">
                <div className="flex items-center gap-2 mb-2">
                  <Clock className="w-4 h-4 text-green-500" />
                  <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Manual Cache</span>
                </div>
                <div className="text-2xl font-bold text-green-600 dark:text-green-400">
                  {timingData.manualCache}
                </div>
                <div className="text-xs text-green-600 dark:text-green-400 mt-1 font-medium">
                  {timingData.comparison.manualSpeedup}% faster
                </div>
              </div>
              
              {/* Spring Cache */}
              <div className="bg-white dark:bg-gray-800 rounded-lg p-4 border border-gray-200 dark:border-gray-700">
                <div className="flex items-center gap-2 mb-2">
                  <Zap className="w-4 h-4 text-blue-500" />
                  <span className="text-sm font-medium text-gray-700 dark:text-gray-300">Spring Cache</span>
                </div>
                <div className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                  {timingData.springCache}
                </div>
                <div className="text-xs text-blue-600 dark:text-blue-400 mt-1 font-medium">
                  {timingData.comparison.springSpeedup}% faster
                </div>
              </div>
            </div>
            
            {/* Summary */}
            <div className="bg-white dark:bg-gray-800 rounded-lg p-3 border border-gray-200 dark:border-gray-700">
              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-700 dark:text-gray-300">
                  <span className="font-medium">Fastest:</span> {timingData.comparison.fastest}
                </span>
                <span className="text-sm text-gray-700 dark:text-gray-300">
                  <span className="font-medium">Speedup:</span> Up to {Math.max(timingData.comparison.manualSpeedup, timingData.comparison.springSpeedup)}%
                </span>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Student List</CardTitle>
        </CardHeader>
        <CardBody>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b">
                <th className="text-left p-3">Roll No</th>
                <th className="text-left p-3">Name</th>
                <th className="text-left p-3">Email</th>
                <th className="text-left p-3">Course</th>
                <th className="text-left p-3">Branch</th>
                <th className="text-left p-3">Semester</th>
                <th className="text-left p-3">CGPA</th>
                <th className="text-left p-3">Status</th>
                <th className="text-left p-3">Performance</th>
                <th className="text-left p-3">Actions</th>
              </tr>
            </thead>
            <tbody>
              {students?.map((student) => {
                const timing = studentTimings[student.id];
                return (
                  <tr key={student.id} className="border-b hover:bg-gray-50">
                    <td className="p-3">{student.rollNumber}</td>
                    <td className="p-3">{student.name}</td>
                    <td className="p-3">{student.email}</td>
                    <td className="p-3">{student.course}</td>
                    <td className="p-3">{student.branch}</td>
                    <td className="p-3">{student.semester}</td>
                    <td className="p-3">{student.cgpa}</td>
                    <td className="p-3">
                      <span className={`px-2 py-1 rounded ${
                        student.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      }`}>
                        {student.status}
                      </span>
                    </td>
                    <td className="p-3">
                      {timing?.loading ? (
                        <div className="flex items-center gap-2">
                          <Loader size="sm" />
                          <span className="text-xs text-gray-500">Testing...</span>
                        </div>
                      ) : timing?.comparison ? (
                        <div className="text-xs">
                          <div className="flex items-center gap-1 mb-1">
                            <Clock className="w-3 h-3 text-red-500" />
                            <span className="text-gray-600">{timing.noCache}</span>
                          </div>
                          <div className="flex items-center gap-1 mb-1">
                            <Zap className="w-3 h-3 text-green-500" />
                            <span className="text-green-600 font-medium">{timing.manualCache}</span>
                            <span className="text-green-600">({timing.comparison.manualSpeedup}% faster)</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <Database className="w-3 h-3 text-blue-500" />
                            <span className="text-blue-600 font-medium">{timing.springCache}</span>
                            <span className="text-blue-600">({timing.comparison.springSpeedup}% faster)</span>
                          </div>
                        </div>
                      ) : (
                        <Button
                          size="sm"
                          variant="secondary"
                          onClick={() => fetchStudentWithTiming(student.id)}
                        >
                          Test
                        </Button>
                      )}
                    </td>
                    <td className="p-3">
                      <Button
                        size="sm"
                        variant="secondary"
                        onClick={() => handleEdit(student)}
                        className="mr-2"
                      >
                        Edit
                      </Button>
                      <Button
                        size="sm"
                        variant="danger"
                        onClick={() => handleDelete(student.id)}
                      >
                        Delete
                      </Button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        </CardBody>
      </Card>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} size="lg">
        <CardHeader>
          <CardTitle>{editingStudent ? 'Edit Student' : 'Add Student'}</CardTitle>
        </CardHeader>
        <CardBody>
        <form onSubmit={handleSubmit} className="space-y-4 pb-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Roll Number"
              name="rollNumber"
              value={formData.rollNumber}
              onChange={handleInputChange}
              required
              placeholder="Enter roll number"
            />
            <Input
              label="Name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
              placeholder="Enter full name"
            />
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleInputChange}
              required
              placeholder="student@example.com"
            />
            <Input
              label="Phone"
              name="phone"
              value={formData.phone}
              onChange={handleInputChange}
              required
              placeholder="Enter phone number"
            />
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Course"
              name="course"
              value={formData.course}
              onChange={handleInputChange}
              required
              placeholder="e.g., B.Tech, B.Sc"
            />
            <Input
              label="Branch"
              name="branch"
              value={formData.branch}
              onChange={handleInputChange}
              required
              placeholder="e.g., CSE, ECE, ME"
            />
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
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
            />
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="City"
              name="city"
              value={formData.city}
              onChange={handleInputChange}
              required
              placeholder="Enter city"
            />
            <Input
              label="Address"
              name="address"
              value={formData.address}
              onChange={handleInputChange}
              placeholder="Enter address (optional)"
            />
          </div>
          
          <div className="flex items-center gap-3 pt-4 border-t border-gray-200 dark:border-gray-700 sticky bottom-0 bg-white dark:bg-gray-800 pb-0">
            <Button 
              type="submit" 
              disabled={createMutation.isPending || updateMutation.isPending}
              className="flex-1 md:flex-none"
            >
              {createMutation.isPending || updateMutation.isPending ? 'Saving...' : (editingStudent ? 'Update' : 'Create')}
            </Button>
            <Button
              type="button"
              variant="secondary"
              onClick={() => setIsModalOpen(false)}
              className="flex-1 md:flex-none"
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