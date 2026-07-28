# Redis Demo Backend - API Testing Guide

## Base URL
```
http://localhost:8080
```

## Response Format
All APIs return a standard response format:
```json
{
  "success": true,
  "data": <response data>,
  "responseTime": <time in ms>,
  "message": <optional message>
}
```

---

## 1. Redis Operations API
**Base Path:** `/api/redis`

### String Operations

#### Store String Value
```http
POST /api/redis/string
Content-Type: application/json

{
  "key": "mykey",
  "value": "myvalue"
}
```

#### Get String Value
```http
GET /api/redis/string/{key}
```

#### Store JSON Object
```http
POST /api/redis/json
Content-Type: application/json

{
  "key": "user:1",
  "value": {
    "name": "John",
    "age": 30
  }
}
```

#### Get JSON Object
```http
GET /api/redis/json/{key}
```

### Hash Operations

#### Store Hash Field
```http
POST /api/redis/hash
Content-Type: application/json

{
  "key": "user:1",
  "field": "name",
  "value": "John"
}
```

#### Get Hash Field
```http
GET /api/redis/hash/{key}/{field}
```

#### Get All Hash Fields
```http
GET /api/redis/hash/{key}
```

#### Delete Hash Field
```http
DELETE /api/redis/hash/{key}/{field}
```

### List Operations

#### Add Element to Left (LPUSH)
```http
POST /api/redis/list/left
Content-Type: application/json

{
  "key": "activity",
  "value": "login"
}
```

#### Add Element to Right (RPUSH)
```http
POST /api/redis/list/right
Content-Type: application/json

{
  "key": "queue",
  "value": "task1"
}
```

#### Get All List Elements
```http
GET /api/redis/list/{key}
```

#### Pop Element from Left (LPOP)
```http
DELETE /api/redis/list/left/{key}
```

#### Pop Element from Right (RPOP)
```http
DELETE /api/redis/list/right/{key}
```

### Set Operations

#### Add Element to Set
```http
POST /api/redis/set
Content-Type: application/json

{
  "key": "tags",
  "value": "java"
}
```

#### Get All Set Members
```http
GET /api/redis/set/{key}
```

#### Check Set Membership
```http
GET /api/redis/set/{key}/{value}
```

#### Remove Element from Set
```http
DELETE /api/redis/set/{key}/{value}
```

### Sorted Set Operations

#### Add Element to Sorted Set
```http
POST /api/redis/sortedset
Content-Type: application/json

{
  "key": "leaderboard",
  "value": "player1",
  "score": 100
}
```

#### Get Sorted Set (Ascending)
```http
GET /api/redis/sortedset/{key}
```

#### Get Sorted Set (Descending)
```http
GET /api/redis/sortedset/{key}/reverse
```

#### Get Element Rank
```http
GET /api/redis/sortedset/{key}/{value}/rank
```

### Counter Operations

#### Increment Counter
```http
POST /api/redis/counter/increment
Content-Type: application/json

{
  "key": "views"
}
```

#### Increment Counter by Amount
```http
POST /api/redis/counter/incrementby
Content-Type: application/json

{
  "key": "points",
  "delta": 10
}
```

#### Decrement Counter
```http
POST /api/redis/counter/decrement
Content-Type: application/json

{
  "key": "inventory"
}
```

---

## 2. Cache Invalidation API
**Base Path:** `/api/cache-invalidation/students`

#### Evict Student Cache by ID
```http
DELETE /api/cache-invalidation/students/{id}
```

#### Evict Student Cache by Email
```http
DELETE /api/cache-invalidation/students/email/{email}
```

#### Evict All Student Cache
```http
DELETE /api/cache-invalidation/students/all
```

#### Update Student with Cache Evict
```http
PUT /api/cache-invalidation/students/{id}
Content-Type: application/json

{
  "name": "Updated Name",
  "email": "updated@example.com"
}
```

#### Delete Student with Cache Evict
```http
DELETE /api/cache-invalidation/students/{id}/delete
```

#### Refresh Student Cache
```http
POST /api/cache-invalidation/students/{id}/refresh
```

#### Lazy Load Student into Cache
```http
GET /api/cache-invalidation/students/{id}/lazy
```

#### Write Through Caching
```http
POST /api/cache-invalidation/students/{id}/write-through
Content-Type: application/json

{
  "name": "Student Name",
  "email": "student@example.com"
}
```

#### Cache Aside Pattern
```http
GET /api/cache-invalidation/students/{id}/cache-aside
```

#### Get Cache Invalidation Explanation
```http
GET /api/cache-invalidation/students/explanation
```

---

## 3. Dashboard API
**Base Path:** `/api/dashboard`

#### Get Dashboard Metrics
```http
GET /api/dashboard/metrics
```

#### Get Performance Statistics
```http
GET /api/dashboard/performance
```

#### Get System Health
```http
GET /api/dashboard/health
```

#### Reset Dashboard Metrics
```http
POST /api/dashboard/reset
```

---

## 4. Redis Data Structures API
**Base Path:** `/api/redis/data-structures`

### String Operations

#### String Demo
```http
POST /api/redis/data-structures/string?key=mykey&value=myvalue
```

#### Get All String Keys
```http
GET /api/redis/data-structures/string/keys
```

### Hash Operations

#### Hash Demo
```http
POST /api/redis/data-structures/hash?key=user:1&field=name&value=John
```

#### Get All Hash Keys
```http
GET /api/redis/data-structures/hash/keys
```

#### Get Hash Fields
```http
GET /api/redis/data-structures/hash/{key}
```

### List Operations

#### List Demo
```http
POST /api/redis/data-structures/list?key=mylist&value=item1
```

#### Get All List Keys
```http
GET /api/redis/data-structures/list/keys
```

#### Get List Elements
```http
GET /api/redis/data-structures/list/{key}
```

### Set Operations

#### Set Demo
```http
POST /api/redis/data-structures/set?key=myset&value=value1
```

#### Get All Set Keys
```http
GET /api/redis/data-structures/set/keys
```

#### Get Set Members
```http
GET /api/redis/data-structures/set/{key}
```

### Sorted Set Operations

#### Sorted Set Demo
```http
POST /api/redis/data-structures/sortedset?key=leaderboard&value=player1&score=100
```

#### Get All Sorted Set Keys
```http
GET /api/redis/data-structures/sortedset/keys
```

#### Get Sorted Set Members
```http
GET /api/redis/data-structures/sortedset/{key}
```

#### Get Data Structures Explanation
```http
GET /api/redis/data-structures/explanation
```

---

## 5. Redis Monitoring API
**Base Path:** `/api/redis/monitoring`

#### Get Redis Server Information
```http
GET /api/redis/monitoring/info
```

#### Get Redis Memory Information
```http
GET /api/redis/monitoring/memory
```

#### Ping Redis Server
```http
GET /api/redis/monitoring/ping
```

#### Get Redis Configuration
```http
GET /api/redis/monitoring/config?pattern=*
```

#### Get Connected Clients List
```http
GET /api/redis/monitoring/clients
```

#### Get Database Size
```http
GET /api/redis/monitoring/dbsize
```

#### Flush Current Database ⚠️
```http
POST /api/redis/monitoring/flushdb
```
**Warning:** Deletes all keys in current database

#### Flush All Databases ⚠️
```http
POST /api/redis/monitoring/flushall
```
**Warning:** Deletes all keys from all databases

#### Get Monitoring Explanation
```http
GET /api/redis/monitoring/explanation
```

---

## Testing Examples

### Example 1: Basic String Operations
```bash
# Store a value
curl -X POST http://localhost:8080/api/redis/string \
  -H "Content-Type: application/json" \
  -d '{"key": "test", "value": "hello"}'

# Retrieve the value
curl http://localhost:8080/api/redis/string/test
```

### Example 2: Hash Operations
```bash
# Store user information
curl -X POST http://localhost:8080/api/redis/hash \
  -H "Content-Type: application/json" \
  -d '{"key": "user:1", "field": "name", "value": "John"}'

# Store email
curl -X POST http://localhost:8080/api/redis/hash \
  -H "Content-Type: application/json" \
  -d '{"key": "user:1", "field": "email", "value": "john@example.com"}'

# Get all user fields
curl http://localhost:8080/api/redis/hash/user:1
```

### Example 3: List Operations (Queue)
```bash
# Add tasks to queue
curl -X POST http://localhost:8080/api/redis/list/right \
  -H "Content-Type: application/json" \
  -d '{"key": "tasks", "value": "task1"}'

curl -X POST http://localhost:8080/api/redis/list/right \
  -H "Content-Type: application/json" \
  -d '{"key": "tasks", "value": "task2"}'

# Process task (FIFO)
curl -X DELETE http://localhost:8080/api/redis/list/left/tasks
```

### Example 4: Set Operations
```bash
# Add tags
curl -X POST http://localhost:8080/api/redis/set \
  -H "Content-Type: application/json" \
  -d '{"key": "tags", "value": "java"}'

curl -X POST http://localhost:8080/api/redis/set \
  -H "Content-Type: application/json" \
  -d '{"key": "tags", "value": "redis"}'

# Get all tags
curl http://localhost:8080/api/redis/set/tags
```

### Example 5: Sorted Set (Leaderboard)
```bash
# Add players
curl -X POST http://localhost:8080/api/redis/sortedset \
  -H "Content-Type: application/json" \
  -d '{"key": "leaderboard", "value": "player1", "score": 100}'

curl -X POST http://localhost:8080/api/redis/sortedset \
  -H "Content-Type: application/json" \
  -d '{"key": "leaderboard", "value": "player2", "score": 200}'

# Get leaderboard (descending)
curl http://localhost:8080/api/redis/sortedset/leaderboard/reverse
```

### Example 6: Counter Operations
```bash
# Increment view counter
curl -X POST http://localhost:8080/api/redis/counter/increment \
  -H "Content-Type: application/json" \
  -d '{"key": "page:views"}'

# Increment by 10
curl -X POST http://localhost:8080/api/redis/counter/incrementby \
  -H "Content-Type: application/json" \
  -d '{"key": "points", "delta": 10}'
```

### Example 7: Redis Monitoring
```bash
# Check Redis connection
curl http://localhost:8080/api/redis/monitoring/ping

# Get Redis info
curl http://localhost:8080/api/redis/monitoring/info

# Get memory usage
curl http://localhost:8080/api/redis/monitoring/memory

# Get database size
curl http://localhost:8080/api/redis/monitoring/dbsize
```

### Example 8: Dashboard Metrics
```bash
# Get dashboard metrics
curl http://localhost:8080/api/dashboard/metrics

# Get performance stats
curl http://localhost:8080/api/dashboard/performance

# Get system health
curl http://localhost:8080/api/dashboard/health
```

---

## Configuration

### Server Configuration
- **Port:** 8080
- **Redis Host:** localhost
- **Redis Port:** 6379
- **Redis Database:** 0

### Cache Configuration
- **TTL:** 600000ms (10 minutes)
- **Key Prefix:** redis_demo::
- **Cache Null Values:** false

---

## Notes

1. **Disabled Controllers:** The following controllers are disabled for Redis-only testing:
   - Student Controller
   - Manual Cache Controller
   - Spring Cache Controller
   - TTL Controller
   - Performance Controller
   - Serialization Controller
   - Common Problems Controller

2. **Response Time:** All APIs include response time measurement for performance monitoring.

3. **Logging:** The application is configured with DEBUG logging for detailed request/response logging.

4. **Warning Operations:** Use caution with `flushdb` and `flushall` operations as they delete data.