/**
 * formatBytes - Format bytes to human-readable string
 * @param {number} bytes - Bytes to format
 * @param {number} decimals - Number of decimal places
 * @returns {string} - Formatted bytes string
 */
export function formatBytes(bytes, decimals = 2) {
  if (bytes === 0 || bytes === null || bytes === undefined) return '0 Bytes';
  
  if (typeof bytes !== 'number' || isNaN(bytes)) return 'Invalid';
  
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  
  const i = Math.floor(Math.log(Math.abs(bytes)) / Math.log(k));
  
  if (i >= sizes.length) return `${bytes.toFixed(dm)} Bytes`;
  
  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`;
}

/**
 * formatBits - Format bits to human-readable string
 * @param {number} bits - Bits to format
 * @param {number} decimals - Number of decimal places
 * @returns {string} - Formatted bits string
 */
export function formatBits(bits, decimals = 2) {
  if (bits === 0 || bits === null || bits === undefined) return '0 bps';
  
  if (typeof bits !== 'number' || isNaN(bits)) return 'Invalid';
  
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['bps', 'Kbps', 'Mbps', 'Gbps', 'Tbps'];
  
  const i = Math.floor(Math.log(Math.abs(bits)) / Math.log(k));
  
  if (i >= sizes.length) return `${bits.toFixed(dm)} bps`;
  
  return `${parseFloat((bits / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`;
}