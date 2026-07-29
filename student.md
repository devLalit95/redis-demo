You are a Senior Java Architect, Spring Boot Expert, Redis Expert, Performance Engineer, and Backend Solution Architect.

This project already exists.

DO NOT rebuild the project.

Your task is to extend the existing Student module and Dashboard to make it a Redis Performance Learning Module.

Everything must integrate cleanly with the existing architecture.

==================================================
OBJECTIVE
==================================================

The Student module should become a complete playground for learning Redis.

I want to compare:

• MySQL only
• Manual Redis Cache
• Spring Cache (@Cacheable)
• Cache Eviction
• Cache Update
• TTL Expiration

I should be able to visually understand

Which request went to MySQL

Which request came from Redis

How much time Redis saved

How many database queries were avoided

When cache becomes stale

How cache invalidation works

How TTL works

==================================================
STUDENT ENTITY
==================================================

Use Student as the primary entity.

Fields

id
rollNumber
name
email
phone
branch
course
semester
cgpa
city
address
status
createdAt
updatedAt

==================================================
CRUD MODULE
==================================================

Implement complete CRUD.

Create Student

Update Student

Delete Student

Get Student by ID

Get All Students

Search Student

Pagination

Sorting

Filtering

Bulk Insert

Bulk Delete

==================================================
THREE FETCH MODES
==================================================

Create three completely separate endpoints.

1.
WITHOUT REDIS

Always query MySQL.

No cache.

2.
MANUAL REDIS CACHE

Check Redis first.

If exists

Return Redis

Else

Query MySQL

Store Redis

Return Response

3.
SPRING CACHE

Use

@EnableCaching

@Cacheable

@CachePut

@CacheEvict

==================================================
RETURN RESPONSE FORMAT
==================================================

Every API should return

success

message

data

metadata

metadata should include

executionTime

databaseTime

redisTime

cacheHit

cacheMiss

cacheType

dataSource

timestamp

Example

{
 "success": true,
 "data": {},
 "metadata": {
   "executionTime":"34 ms",
   "databaseTime":"31 ms",
   "redisTime":"1 ms",
   "cacheHit":true,
   "cacheType":"MANUAL",
   "dataSource":"REDIS"
 }
}

==================================================
CACHE PLAYGROUND
==================================================

Create endpoints

GET Student Without Cache

GET Student Manual Cache

GET Student Cacheable

Refresh Cache

Delete Cache

Delete All Cache

Expire Cache

Warm Cache

==================================================
TTL DEMO
==================================================

Support multiple TTL values.

30 seconds

60 seconds

2 minutes

5 minutes

Custom TTL

Expose API

Current TTL

Remaining TTL

Reset TTL

==================================================
CACHE INVALIDATION
==================================================

Automatically invalidate cache when

Student Updated

Student Deleted

Bulk Delete

Bulk Update

Also provide manual invalidation APIs.

==================================================
CACHE WARMING
==================================================

Create endpoint

Warm entire Student cache.

Preload most requested students.

==================================================
CACHE STATISTICS
==================================================

Maintain statistics

Total Requests

Cache Hits

Cache Misses

Hit Ratio

Miss Ratio

Average Response Time

Redis Reads

Redis Writes

Database Reads

Database Writes

Evictions

Expired Keys

==================================================
PERFORMANCE METRICS
==================================================

Measure

Controller Time

Service Time

Repository Time

Redis Time

Serialization Time

Database Time

Total Time

==================================================
AOP PERFORMANCE LOGGER
==================================================

Use Spring AOP.

Automatically log

Method Name

Execution Time

Redis Calls

Database Calls

Thread Name

==================================================
WEBSOCKET
==================================================

If appropriate, use WebSocket.

Push live dashboard updates.

Examples

Cache Hit increased

Cache Miss increased

Student Added

Student Deleted

TTL Expired

Redis Memory Changed

Dashboard Metrics Updated

No page refresh required.

Use STOMP over WebSocket.

==================================================
LIVE DASHBOARD
==================================================

Dashboard should update in real time.

Cards

Total Students

Total Redis Keys

Redis Memory

Cache Hits

Cache Misses

Average Response Time

Database Queries

Redis Queries

Current Hit Ratio

Current Miss Ratio

==================================================
LIVE CHARTS
==================================================

Response Time

Requests Per Minute

Cache Hit Ratio

Cache Miss Ratio

Redis Reads

Database Reads

TTL Expiration Events

==================================================
PERFORMANCE COMPARISON PAGE
==================================================

Compare

Without Cache

Manual Cache

@Cacheable

Display

Average Time

Minimum Time

Maximum Time

Median

Number of DB Calls

Redis Calls

Hit Ratio

Miss Ratio

==================================================
REDIS EXPLORER
==================================================

Create APIs

List Keys

Key Type

TTL

Memory Usage

Value

Delete Key

Refresh

Search

==================================================
SIMULATION APIs
==================================================

Create APIs that simulate

Slow Database

Fast Redis

Cache Miss

Cache Hit

TTL Expired

Database Failure

Redis Down

These APIs are only for learning.

==================================================
LOAD TEST MODE
==================================================

Create endpoint

Generate

100 Requests

500 Requests

1000 Requests

Random Student Requests

Compare

Without Cache

Manual Cache

Cacheable

Return comparison report.

==================================================
SERIALIZATION DEMO
==================================================

Show

JDK Serialization

JSON Serialization

GenericJackson2JsonRedisSerializer

Compare

Object Size

Serialization Time

Deserialization Time

==================================================
LOGGING
==================================================

Log every request.

Example

API

Execution Time

Data Source

Redis Hit

Redis Miss

Database Query Count

TTL

==================================================
SWAGGER
==================================================

Every endpoint must have

Description

Purpose

Redis concept explained

==================================================
REACT FRONTEND REQUIREMENTS
==================================================

Extend the existing frontend.

Do NOT redesign everything.

Add the following pages.

Student CRUD

Redis Cache Playground

Performance Comparison

Redis Explorer

Live Dashboard

Request History

Load Testing

Metrics

==================================================
CACHE PLAYGROUND UI
==================================================

Buttons

Fetch Without Cache

Fetch Manual Cache

Fetch Cacheable

Update Student

Delete Student

Clear Cache

Warm Cache

Expire Cache

Each request should immediately show

Execution Time

Database Time

Redis Time

Cache Hit

Cache Miss

Data Source

TTL Remaining

==================================================
PERFORMANCE TABLE
==================================================

Columns

Mode

Execution Time

DB Time

Redis Time

Cache Hit

Cache Miss

DB Calls

Redis Calls

Difference (%)

Highlight fastest response.

==================================================
LIVE REQUEST HISTORY
==================================================

Show every request.

Timestamp

API

Execution Time

Source

Redis

MySQL

Hit

Miss

==================================================
LEARNING MODE
==================================================

Every page should explain

Why this Redis feature exists.

How it works internally.

When to use it.

Common interview questions.

Common production mistakes.

==================================================
ARCHITECTURE
==================================================

Keep the project modular.

Do not break existing code.

Follow SOLID.

Use DTOs.

Use Global Exception Handling.

Keep Redis logic isolated from business logic.

Design everything so advanced Redis topics can be added later without major refactoring.

The implementation should be suitable for both portfolio projects and enterprise-level learning.