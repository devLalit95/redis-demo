/**
 * copyToClipboard - Copy text to clipboard
 * @param {string} text - Text to copy
 * @returns {Promise<boolean>} - Success status
 */
export async function copyToClipboard(text) {
  if (!text) return false;
  
  try {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      await navigator.clipboard.writeText(text);
      return true;
    } else {
      // Fallback for older browsers
      const textArea = document.createElement('textarea');
      textArea.value = text;
      textArea.style.position = 'fixed';
      textArea.style.left = '-999999px';
      textArea.style.top = '-999999px';
      document.body.appendChild(textArea);
      textArea.focus();
      textArea.select();
      
      try {
        document.execCommand('copy');
        document.body.removeChild(textArea);
        return true;
      } catch (err) {
        document.body.removeChild(textArea);
        return false;
      }
    }
  } catch (err) {
    console.error('Failed to copy to clipboard:', err);
    return false;
  }
}

/**
 * copyToClipboardWithFeedback - Copy text to clipboard with user feedback
 * @param {string} text - Text to copy
 * @param {Function} onSuccess - Callback on success
 * @param {Function} onError - Callback on error
 * @returns {Promise<boolean>} - Success status
 */
export async function copyToClipboardWithFeedback(text, onSuccess, onError) {
  const success = await copyToClipboard(text);
  
  if (success && onSuccess) {
    onSuccess();
  } else if (!success && onError) {
    onError();
  }
  
  return success;
}

/**
 * readFromClipboard - Read text from clipboard
 * @returns {Promise<string|null>} - Clipboard content or null
 */
export async function readFromClipboard() {
  try {
    if (navigator.clipboard && navigator.clipboard.readText) {
      return await navigator.clipboard.readText();
    }
    return null;
  } catch (err) {
    console.error('Failed to read from clipboard:', err);
    return null;
  }
}