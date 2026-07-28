You are a Senior React Architect, Senior Frontend Engineer, UI/UX Engineer, and Software Architect.

Your job is NOT to build a simple React application.

Your goal is to build a production-ready, scalable, reusable, maintainable frontend architecture for a Spring Boot + Redis Learning Project.

The frontend should be designed so that I can continue adding new Redis features later without changing the project structure.

==================================================
TECH STACK
==================================================

React 19
Vite
JavaScript (No TypeScript)

Tailwind CSS Latest

React Router DOM

Axios

React Hook Form

Zod Validation

React Query (TanStack Query)

React Hot Toast

Framer Motion

Recharts

Lucide React Icons

clsx

tailwind-merge

==================================================
PROJECT GOAL
==================================================

The frontend should teach Redis visually.

Everything should be interactive.

Every API should have its own UI.

Every Redis concept should be understandable from the frontend itself.

This should NOT look like a student project.

It should look like an Admin Dashboard built by an experienced frontend engineer.

==================================================
DESIGN SYSTEM
==================================================

Create a complete Design System.

Everything must be reusable.

Never repeat UI code.

Follow Atomic Design principles.

Atoms

Buttons

Input

Textarea

Select

Checkbox

Radio

Badge

Avatar

Loader

Spinner

Tooltip

Divider

Typography

Icon Wrapper

Skeleton

Progress Bar

Toggle

Switch

Modal

Drawer

Tabs

Breadcrumb

Pagination

Toast

Status Chip

==================================================
MOLECULES
==================================================

Search Box

Filter Bar

Student Card

Metric Card

Info Card

API Status

Response Card

Table Header

Confirmation Dialog

Form Fields

Stat Widget

Navbar Item

Sidebar Item

Key Value Pair

==================================================
ORGANISMS
==================================================

Navbar

Sidebar

Dashboard

Student Table

Redis Explorer

Cache Playground

Performance Panel

Metrics Panel

Configuration Panel

Charts Section

Logs Viewer

CRUD Forms

==================================================
TAILWIND STRUCTURE
==================================================

Do NOT use inline styles.

Everything should use Tailwind.

Create reusable utility classes.

Use CSS variables where required.

Use centralized design tokens.

Colors

Spacing

Radius

Typography

Shadow

Transitions

Animation

Dark Mode

Everything should be configurable.

==================================================
GLOBAL CSS
==================================================

Create centralized CSS files.

base.css

theme.css

animations.css

utilities.css

scrollbar.css

forms.css

tables.css

cards.css

buttons.css

layout.css

variables.css

Never duplicate CSS.

==================================================
PROJECT STRUCTURE
==================================================

src/

components/

ui/

layout/

common/

forms/

tables/

cards/

charts/

redis/

dashboard/

student/

cache/

performance/

metrics/

pages/

hooks/

services/

api/

utils/

constants/

config/

styles/

context/

assets/

routes/

==================================================
API LAYER
==================================================

Create a professional API layer.

Never call axios directly inside components.

Create

axiosInstance

request interceptor

response interceptor

error interceptor

authentication handler

retry handler

timeout handler

==================================================
API FILES
==================================================

studentApi.js

cacheApi.js

redisApi.js

performanceApi.js

metricsApi.js

dashboardApi.js

configApi.js

healthApi.js

Every backend endpoint must have its own API function.

==================================================
ERROR HANDLING
==================================================

No application crash should happen.

Handle

null

undefined

empty array

empty object

invalid object

network failure

500 error

404

400

CORS failure

timeout

invalid JSON

unexpected response

Every API response should be validated before rendering.

Never assume data exists.

Always use optional chaining.

Always provide fallback values.

Show user-friendly error messages.

==================================================
DATA VALIDATION
==================================================

Validate every API response.

Check

Array.isArray()

typeof

null

undefined

object

number

string

boolean

date

Never directly map over data.

Always validate first.

==================================================
REACT QUERY
==================================================

Use React Query everywhere.

Implement

Query Cache

Mutation

Invalidation

Retry

Loading State

Error State

Refetch

Background Refetch

==================================================
LOADING STATES
==================================================

Every page should have

Skeleton Loader

Spinner

Empty State

Error State

No Data State

==================================================
DASHBOARD
==================================================

Dashboard should include

Total Students

Cache Hits

Cache Misses

Average Response Time

Redis Keys

Database Calls

Redis Calls

TTL Status

Memory Usage

==================================================
CHARTS
==================================================

Response Time

Cache Hit Ratio

Memory Usage

TTL Countdown

Redis Operations

CRUD Statistics

Daily Requests

API Response Comparison

==================================================
CACHE PLAYGROUND
==================================================

Interactive page.

Buttons

Fetch Student

Fetch Again

Update Student

Delete Student

Clear Cache

Refresh Cache

Expire Cache

Show

Cache Hit

Cache Miss

TTL Countdown

Execution Time

Redis Time

Database Time

==================================================
REDIS EXPLORER
==================================================

Mini RedisInsight.

Display

Keys

TTL

Type

Value

Memory

Delete

Refresh

Search

Filter

Pagination

==================================================
PERFORMANCE PAGE
==================================================

Compare

Without Cache

Manual Cache

Cacheable

Display

Cards

Table

Charts

Average Time

Fastest

Slowest

==================================================
STUDENT PAGE
==================================================

Professional Data Table.

Sorting

Filtering

Searching

Pagination

CRUD

Bulk Delete

Bulk Selection

Responsive

==================================================
FORMS
==================================================

Reusable Form Components.

React Hook Form

Zod

Validation

Error Messages

Helper Text

Success Messages

==================================================
ROUTING
==================================================

Protected Layout

Main Layout

Nested Routes

404 Page

Loading Route

==================================================
RESPONSIVENESS
==================================================

Mobile First

Tablet

Laptop

Desktop

Ultra Wide

No layout breaking.

==================================================
ANIMATIONS
==================================================

Framer Motion

Smooth Page Transition

Card Hover

Button Ripple

Fade

Slide

Scale

Accordion

Modal Animation

==================================================
TABLES
==================================================

Reusable Data Table Component.

Features

Sorting

Searching

Filtering

Pagination

Column Visibility

Loading

Empty State

Actions

==================================================
BUTTONS
==================================================

Create reusable buttons.

Primary

Secondary

Danger

Success

Outline

Ghost

Loading

Icon Button

Small

Medium

Large

Disabled

==================================================
CARDS
==================================================

Reusable Cards

Metric Card

Information Card

Chart Card

API Card

Student Card

Status Card

Glass Card

==================================================
MODALS
==================================================

Reusable Modal System.

Confirmation Modal

Success Modal

Error Modal

Form Modal

==================================================
HOOKS
==================================================

Create reusable hooks.

useApi

useDebounce

usePagination

useSearch

useSort

useLocalStorage

useTheme

useCacheStatus

useTTL

usePerformance

==================================================
UTILITIES
==================================================

Create reusable utilities.

formatDate()

formatBytes()

formatResponseTime()

formatPercentage()

formatTTL()

capitalize()

truncate()

copyToClipboard()

==================================================
CONSTANTS
==================================================

Centralize

Routes

API URLs

Colors

Icons

Messages

Validation Rules

Redis Types

==================================================
CONFIG
==================================================

Environment based configuration.

.env

API Base URL

Timeout

Retry Count

Application Name

==================================================
CORS
==================================================

Assume backend runs on different port.

Properly configure Axios.

Support

http://localhost:8080

http://localhost:5173

Never hardcode URLs.

==================================================
CODE QUALITY
==================================================

Follow

SOLID principles

Clean Code

DRY

KISS

Reusable Components

Separation of Concerns

Meaningful Naming

Small Components

Reusable Logic

==================================================
COMMENTS
==================================================

Explain every important component.

Explain folder structure.

Explain reusable architecture.

Explain API flow.

Explain React Query usage.

==================================================
FINAL GOAL
==================================================

The frontend should look like a professional SaaS Admin Dashboard.

Every Redis feature should have its own UI.

Every backend API should already have frontend integration.

The code should be modular enough that adding new backend APIs later only requires creating one API file and one page without changing existing code.

The entire project should be production-ready, scalable, reusable, responsive, optimized, easy to maintain, and suitable for portfolio as well as real-world enterprise applications.