/**
 * formatResponseTime - Format response time in milliseconds to readable string
 * @param {number} ms - Response time in milliseconds
 * @param {string} unit - Unit to display ('ms', 's', 'auto')
 * @returns {string} - Formatted response time string
 */
export function formatResponseTime(ms, unit = 'auto') {
  if (ms === null || ms === undefined) return 'N/A';
  
  if (typeof ms !== 'number' || isNaN(ms)) return 'Invalid';
  
  if (unit === 'ms') {
    return `${ms.toFixed(2)} ms`;
  }
  
  if (unit === 's') {
    return `${(ms / 1000).toFixed(2)} s`;
  }
  
  // Auto unit
  if (ms < 1) {
    return `${(ms * 1000).toFixed(2)} μs`;
  } else if (ms < 1000) {
    return `${ms.toFixed(2)} ms`;
  } else {
    return `${(ms / 1000).toFixed(2)} s`;
  }
}

/**
 * getResponseTimeColor - Get color based on response time
 * @param {number} ms - Response time in milliseconds
 * @returns {string} - Color class or hex value
 */
export function getResponseTimeColor(ms) {
  if (ms === null || ms === undefined) return '#9ca3af';
  
  if (ms < 100) return '#22c55e'; // Green - Fast
  if (ms < 300) return '#0ea5e9'; // Blue - Good
  if (ms < 500) return '#f59e0b'; // Orange - Warning
  return '#ef4444'; // Red - Slow
}

/**
 * getResponseTimeStatus - Get status based on response time
 * @param {number} ms - Response time in milliseconds
 * @returns {string} - Status string
 */
export function getResponseTimeStatus(ms) {
  if (ms === null || ms === undefined) return 'Unknown';
  
  if (ms < 100) return 'Fast';
  if (ms < 300) return 'Good';
  if (ms < 500) return 'Warning';
  return 'Slow';
}