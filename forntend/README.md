# Redis Demo Frontend

A production-ready, scalable React frontend for the Redis Demo learning platform.

## Tech Stack

- **React 19** - Latest React with concurrent features
- **Vite** - Fast build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **React Router DOM** - Client-side routing
- **Axios** - HTTP client with interceptors
- **React Hook Form** - Form management
- **Zod** - Schema validation
- **React Query (TanStack Query)** - Data fetching and caching
- **React Hot Toast** - Toast notifications
- **Framer Motion** - Animation library
- **Recharts** - Charting library
- **Lucide React** - Icon library
- **clsx & tailwind-merge** - Utility class management

## Project Structure

```
src/
├── components/
│   ├── ui/           # Reusable UI components (Atoms)
│   ├── layout/       # Layout components (Organisms)
│   ├── common/       # Common components (Molecules)
│   ├── forms/        # Form components
│   ├── tables/       # Table components
│   ├── cards/        # Card components
│   ├── charts/       # Chart components
│   ├── redis/        # Redis-specific components
│   ├── dashboard/    # Dashboard components
│   ├── student/      # Student components
│   ├── cache/        # Cache components
│   ├── performance/  # Performance components
│   └── metrics/      # Metrics components
├── pages/            # Page components
├── hooks/            # Custom React hooks
├── services/
│   └── api/          # API service layer
├── utils/            # Utility functions
├── constants/        # Constants
├── config/           # Configuration files
├── styles/           # Global styles
├── context/          # React context providers
├── assets/           # Static assets
└── routes/           # Route configurations
```

## Getting Started

### Prerequisites

- Node.js 18+ 
- Redis server running on localhost:6379
- Backend API running on localhost:8080

### Installation

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### Environment Variables

Create a `.env` file in the root directory:

```env
VITE_API_BASE_URL=http://localhost:8080
```

## Features

### Implemented (Part 1-3)

- ✅ Complete design system with reusable UI components
- ✅ Professional layout with responsive sidebar and navbar
- ✅ Dashboard with real-time metrics
- ✅ Redis Explorer for data structure visualization
- ✅ String Operations page with interactive forms
- ✅ Redis Monitor with real-time statistics
- ✅ Dark mode support
- ✅ API layer with axios interceptors
- ✅ React Query for data fetching and caching
- ✅ Toast notifications
- ✅ Loading states and error handling

### Planned (Part 4)

- ⏳ Hash Operations page
- ⏳ List Operations page
- ⏳ Set Operations page
- ⏳ Sorted Set Operations page
- ⏳ Counter Operations page
- ⏳ Cache Playground page
- ⏳ Performance comparison page
- ⏳ Advanced monitoring features
- ⏳ Data visualization with charts

## Design System

### Components

#### Atoms (UI Components)
- Button, Input, Textarea, Select, Checkbox, Switch
- Badge, Avatar, Loader, Skeleton, Progress
- Modal, Tabs, Tooltip, Divider
- Typography components

#### Molecules
- Search Box, Filter Bar, Form Fields
- Metric Card, Info Card, Response Card
- API Status, Key Value Pair

#### Organisms
- Navbar, Sidebar, Dashboard
- Redis Explorer, Cache Playground
- Performance Panel, Metrics Panel

### Styling

- **Tailwind CSS** for utility classes
- **CSS Variables** for theme customization
- **Dark Mode** support via class strategy
- **Responsive Design** - mobile-first approach
- **Animations** using Framer Motion

## API Integration

### API Layer

All API calls go through a centralized service layer:

```javascript
// Example: Redis API
import redisApi from '../services/api/redisApi';

// Set string
await redisApi.string.setString('key', 'value');

// Get string
await redisApi.string.getString('key');
```

### React Query

Data fetching with React Query for automatic caching, refetching, and loading states:

```javascript
const { data, isLoading, error } = useQuery({
  queryKey: ['redis-info'],
  queryFn: monitoringApi.getRedisInfo,
  refetchInterval: 5000,
});
```

## Architecture Principles

- **Atomic Design** - Component hierarchy
- **SOLID Principles** - Clean architecture
- **DRY** - Don't repeat yourself
- **Separation of Concerns** - Clear boundaries
- **Reusable Components** - Modular design
- **Error Handling** - Comprehensive error management
- **Performance** - Optimized rendering and caching

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

MIT