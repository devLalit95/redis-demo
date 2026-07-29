/**
 * formatTTL - Format TTL (Time To Live) to readable string
 * @param {number} seconds - TTL in seconds
 * @param {string} format - Format type ('short', 'long', 'detailed')
 * @returns {string} - Formatted TTL string
 */
export function formatTTL(seconds, format = 'short') {
  if (seconds === null || seconds === undefined) return 'N/A';
  
  if (typeof seconds !== 'number' || isNaN(seconds)) return 'Invalid';
  
  if (seconds === -1) return 'Persistent';
  if (seconds === -2) return 'Evicted';
  if (seconds <= 0) return 'Expired';
  
  const days = Math.floor(seconds / 86400);
  const hours = Math.floor((seconds % 86400) / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;
  
  if (format === 'detailed') {
    const parts = [];
    if (days > 0) parts.push(`${days}d`);
    if (hours > 0) parts.push(`${hours}h`);
    if (minutes > 0) parts.push(`${minutes}m`);
    if (secs > 0 || parts.length === 0) parts.push(`${secs}s`);
    return parts.join(' ');
  }
  
  if (format === 'long') {
    if (days > 0) return `${days} day${days > 1 ? 's' : ''}`;
    if (hours > 0) return `${hours} hour${hours > 1 ? 's' : ''}`;
    if (minutes > 0) return `${minutes} minute${minutes > 1 ? 's' : ''}`;
    return `${secs} second${secs > 1 ? 's' : ''}`;
  }
  
  // Short format
  if (days > 0) return `${days}d`;
  if (hours > 0) return `${hours}h`;
  if (minutes > 0) return `${minutes}m`;
  return `${secs}s`;
}

/**
 * formatTTLCountdown - Format TTL as countdown from expiration time
 * @param {number} expiresAt - Unix timestamp when key expires
 * @returns {string} - Formatted countdown string
 */
export function formatTTLCountdown(expiresAt) {
  if (expiresAt === null || expiresAt === undefined) return 'N/A';
  
  const now = Math.floor(Date.now() / 1000);
  const remaining = expiresAt - now;
  
  if (remaining <= 0) return 'Expired';
  
  return formatTTL(remaining, 'detailed');
}

/**
 * getTTLColor - Get color based on TTL value
 * @param {number} seconds - TTL in seconds
 * @returns {string} - Color class or hex value
 */
export function getTTLColor(seconds) {
  if (seconds === null || seconds === undefined) return '#9ca3af';
  if (seconds === -1) return '#22c55e'; // Green - Persistent
  if (seconds === -2) return '#ef4444'; // Red - Evicted
  if (seconds <= 0) return '#9ca3af'; // Gray - Expired
  if (seconds < 60) return '#ef4444'; // Red - Critical
  if (seconds < 300) return '#f59e0b'; // Orange - Warning
  if (seconds < 3600) return '#0ea5e9'; // Blue - Normal
  return '#22c55e'; // Green - Healthy
}

/**
 * getTTLStatus - Get status based on TTL value
 * @param {number} seconds - TTL in seconds
 * @returns {string} - Status string
 */
export function getTTLStatus(seconds) {
  if (seconds === null || seconds === undefined) return 'Unknown';
  if (seconds === -1) return 'Persistent';
  if (seconds === -2) return 'Evicted';
  if (seconds <= 0) return 'Expired';
  if (seconds < 60) return 'Critical';
  if (seconds < 300) return 'Warning';
  if (seconds < 3600) return 'Normal';
  return 'Healthy';
}