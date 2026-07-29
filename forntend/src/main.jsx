import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

// Import all CSS files
import './index.css'
import './styles/base.css'
import './styles/theme.css'
import './styles/animations.css'
import './styles/utilities.css'
import './styles/scrollbar.css'
import './styles/forms.css'
import './styles/tables.css'
import './styles/cards.css'
import './styles/buttons.css'
import './styles/layout.css'
import './styles/variables.css'

import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
