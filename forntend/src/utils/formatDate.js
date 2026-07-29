/**
 * formatDate - Format date to readable string
 * @param {Date|string|number} date - Date to format
 * @param {string} format - Format type ('short', 'long', 'time', 'full')
 * @returns {string} - Formatted date string
 */
export function formatDate(date, format = 'short') {
  if (!date) return 'N/A';
  
  const dateObj = new Date(date);
  
  if (isNaN(dateObj.getTime())) return 'Invalid Date';
  
  const options = {
    short: { month: 'short', day: 'numeric', year: 'numeric' },
    long: { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' },
    time: { hour: '2-digit', minute: '2-digit', second: '2-digit' },
    full: { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit' }
  };
  
  return dateObj.toLocaleDateString('en-US', options[format] || options.short);
}

/**
 * formatRelativeTime - Format date as relative time (e.g., "2 hours ago")
 * @param {Date|string|number} date - Date to format
 * @returns {string} - Relative time string
 */
export function formatRelativeTime(date) {
  if (!date) return 'N/A';
  
  const dateObj = new Date(date);
  const now = new Date();
  const diffMs = now - dateObj;
  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);
  
  if (diffSec < 60) return 'just now';
  if (diffMin < 60) return `${diffMin} minute${diffMin > 1 ? 's' : ''} ago`;
  if (diffHour < 24) return `${diffHour} hour${diffHour > 1 ? 's' : ''} ago`;
  if (diffDay < 7) return `${diffDay} day${diffDay > 1 ? 's' : ''} ago`;
  
  return formatDate(date, 'short');
}