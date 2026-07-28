You are a Senior Software Architect, Senior Spring Boot Developer, Redis Expert, and Technical Mentor.

Your task is NOT just to build an application.

Your task is to create a production-quality learning project that teaches Redis from scratch to an intermediate level through practical implementation.

The project should be designed so that every feature teaches one Redis concept.

The architecture must be scalable, modular, clean, and easy to extend because I will continue adding new Redis concepts later.

========================================================
TECH STACK
========================================================

Backend
--------
Java 21
Spring Boot 3.x
Spring Data JPA
Spring Data Redis
MySQL
Redis
Maven
Lombok
Validation
MapStruct (optional)
Spring Boot Actuator
Spring AOP (optional)
Jackson

Frontend
---------
React JS (Vite)
Material UI
React Router
Axios
Recharts
React Query (optional)

========================================================
PROJECT GOAL
========================================================

I DO NOT want only CRUD.

I want to understand:

How Redis works internally.

Why Redis is fast.

How caching works.

Different ways of caching.

When to use which approach.

How cache invalidation works.

How response time changes.

How cache hit and cache miss happen.

How Redis stores objects.

Serialization.

TTL.

Eviction.

Everything should be visible from UI.

========================================================
ENTITY
========================================================

Use Student Entity.

Student

id

name

email

course

branch

semester

cgpa

city

phone

createdAt

updatedAt

========================================================
MODULES
========================================================

Create separate modules.

student

cache

dashboard

redis

performance

configuration

metrics

========================================================
PHASE 1
Simple CRUD
========================================================

Implement complete Student CRUD using MySQL only.

No Redis.

This is the baseline.

Measure response time.

Return response like

{
 responseTime,
 data
}

========================================================
PHASE 2
Redis Introduction
========================================================

Create APIs explaining Redis basics.

Store String

Store JSON

Store Hash

Store List

Store Set

Store Sorted Set

Increment Counter

Expire Keys

Delete Keys

Rename Keys

Check Existing Keys

TTL

Keys

Pattern Search

Explain every endpoint.

========================================================
PHASE 3
Manual Cache
========================================================

Implement manual caching WITHOUT @Cacheable.

Flow

API called

↓

Check Redis

↓

If found

Return Redis data

↓

Else

Fetch MySQL

↓

Store Redis

↓

Return Response

Show in logs

CACHE HIT

CACHE MISS

Show response time.

========================================================
PHASE 4
Spring Cache
========================================================

Implement

@EnableCaching

@Cacheable

@CachePut

@CacheEvict

@Caching

Examples for every annotation.

Explain

key

condition

unless

sync

cacheNames

value

multiple cache names

custom keys

SpEL examples

========================================================
PHASE 5
TTL
========================================================

Implement examples

Cache expires in

30 seconds

1 minute

5 minutes

Custom TTL

Show timer on frontend.

========================================================
PHASE 6
Serialization
========================================================

Demonstrate

JDK Serialization

Jackson JSON Serialization

GenericJackson2JsonRedisSerializer

Explain differences.

Show Redis values.

========================================================
PHASE 7
Performance Comparison
========================================================

Create APIs

Without Redis

With Manual Cache

With Cacheable

Show

Response Time

Database Query Count

Redis Hit

Redis Miss

Display charts.

========================================================
PHASE 8
Dashboard
========================================================

Build beautiful dashboard.

Cards

Total Students

Redis Keys

Cache Hits

Cache Misses

Average Response Time

Fastest Request

Slowest Request

Database Calls

Redis Calls

========================================================
CHARTS
========================================================

Response Time Graph

Cache Hit Ratio

Redis Memory Usage

Redis Operations

TTL Countdown

========================================================
PHASE 9
Redis Monitoring
========================================================

Create APIs

INFO

MEMORY

PING

CONFIG

CLIENT LIST

DBSIZE

FLUSHDB

FLUSHALL (development only)

========================================================
PHASE 10
Redis Data Structures Demo
========================================================

Separate UI page

Strings

Hashes

Lists

Sets

Sorted Sets

Streams (optional)

Bitmaps (optional)

HyperLogLog (optional)

Each should have

Create

Read

Update

Delete

Visualization

========================================================
PHASE 11
Cache Invalidation
========================================================

Demonstrate

Delete one cache

Delete all cache

Evict after Update

Evict after Delete

Refresh cache

Lazy loading

Write Through

Cache Aside Pattern

========================================================
PHASE 12
Common Problems
========================================================

Explain practically

Cache Penetration

Cache Avalanche

Cache Breakdown

Hot Keys

Cold Cache

Show simulation.

========================================================
PHASE 13
Logging
========================================================

Every request logs

API

Execution Time

Cache Hit

Cache Miss

Redis Time

DB Time

========================================================
PHASE 14
React UI
========================================================

Use Material UI.

Pages

Dashboard

Students

Redis Explorer

Cache Playground

Performance

Metrics

Configuration

Redis Data Structures

Logs Viewer

========================================================
CACHE PLAYGROUND
========================================================

This page should allow me to

Click

Fetch Student

↓

See

CACHE MISS

↓

Click again

↓

See

CACHE HIT

↓

Wait TTL

↓

See MISS again

Show timings.

========================================================
REDIS EXPLORER
========================================================

Like mini RedisInsight.

Show

Keys

TTL

Value

Type

Memory Usage

Delete Key

Refresh

Search Keys

========================================================
PERFORMANCE PAGE
========================================================

Compare

No Cache

Manual Cache

@Cacheable

Table

Bar Chart

Line Chart

========================================================
PROJECT STRUCTURE
========================================================

Follow production architecture.

controller

service

repository

entity

dto

mapper

config

cache

redis

metrics

performance

exception

util

constant

validator

========================================================
CODE QUALITY
========================================================

Use

SOLID

Clean Architecture principles

Constructor Injection

DTOs

Global Exception Handling

Validation

Meaningful package names

========================================================
DOCUMENTATION
========================================================

I want detailed comments.

Every important class should explain

WHY it exists.

Every Redis concept should explain

WHEN to use it.

Every cache implementation should explain

WHY it is implemented this way.

========================================================
LEARNING MODE
========================================================

This project is for learning.

Do NOT hide complexity.

Explain every concept.

Whenever a new Redis feature is implemented:

1. Explain theory.

2. Explain code.

3. Explain execution flow.

4. Explain interview questions.

5. Explain production use cases.

6. Explain common mistakes.

7. Explain debugging methods.

========================================================
BONUS FEATURES
========================================================

If possible also include

Redis Pub/Sub

Distributed Lock

Rate Limiter

Session Storage

Leaderboard

OTP Storage

Login Attempts Counter

API Rate Limiter

These should be modular so they can be enabled later.

========================================================
FINAL GOAL
========================================================

By completing this project I should understand Redis deeply enough to:

Explain it in interviews.

Implement caching correctly.

Debug cache issues.

Understand Cacheable annotations.

Understand manual caching.

Understand TTL.

Understand serialization.

Understand cache invalidation.

Know when NOT to use Redis.

Know production best practices.

The project should be scalable so I can continue adding advanced Redis topics later without changing the architecture.