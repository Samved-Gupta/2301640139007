URL Shortener Microservice - Design Document
1. Overview
This document outlines the design and architecture of the HTTP URL Shortener microservice. The service provides core functionality for creating, redirecting, and retrieving analytics for shortened URLs, built as a robust, production-ready application.

2. Technology Stack & Justification
The technology stack was chosen to ensure high performance, scalability, and maintainability.

Framework: Java 17 with Spring Boot 3

Justification: Spring Boot provides a rapid, convention-over-configuration approach, ideal for building standalone microservices. Its extensive ecosystem includes first-class support for web development, data access, and dependency management, which accelerates development time.

Database: Redis

Justification: A URL shortener is a read-heavy application where lookup speed is critical. Redis, as an in-memory key-value store, offers extremely low-latency read operations. Its native support for setting a Time-To-Live (TTL) on keys (EXPIRE) is a perfect and highly efficient mechanism for handling the link validity requirement.

Build Tool: Apache Maven

Justification: Maven is the standard build automation and dependency management tool in the Java ecosystem. A multi-module project structure was used to cleanly separate the reusable logging-middleware from the main backend-test-submission application, promoting code reusability and separation of concerns.

Asynchronous Processing: Spring WebFlux / Project Reactor

Justification: A reactive, non-blocking approach was used for interacting with Redis (ReactiveRedisTemplate) and for the logging client (WebClient). This ensures that the application remains highly responsive and can handle a large number of concurrent connections efficiently by not blocking threads on I/O operations.

3. Architecture & Design
The system is designed as a single microservice with a modular internal structure.

Multi-Module Project: The project is split into two distinct Maven modules:

logging-middleware: A reusable library responsible for all communication with the external logging service. It is designed to be self-contained and could be used by other microservices in the future.

backend-test-submission: The primary Spring Boot application that contains all business logic for the URL shortener and consumes the logging-middleware as a dependency.

Layered Architecture: The main application follows a standard layered architecture:

Controller Layer: Handles incoming HTTP requests, performs basic validation, and delegates to the service layer.

Service Layer: Contains the core business logic, including shortcode generation, validation, and orchestration of database operations.

Exception Handling: A global exception handler (@RestControllerAdvice) intercepts runtime exceptions to provide consistent, well-formed JSON error responses with appropriate HTTP status codes.

4. Data Model (Redis)
A key-value approach in Redis was used to model all data, optimized for performance.

URL Mapping: Stores the primary shortcode-to-URL mapping.

Key: url:{shortcode} (e.g., url:aBcd1)

Type: String

Value: The original long URL.

TTL: Set dynamically based on the user's validity input (default 30 minutes).

Statistics Metadata: Stores analytics metadata for each link.

Key: stats:{shortcode} (e.g., stats:aBcd1)

Type: Hash

Fields:

originalUrl: The original long URL.

createdAt: ISO 8601 timestamp.

expiresAt: ISO 8601 timestamp.

totalClicks: A counter, incremented atomically using HINCRBY.

TTL: Same as the URL mapping key.

Click Details: Stores a time-series log of individual click events.

Key: clicks:{shortcode} (e.g., clicks:aBcd1)

Type: List

Value: A list of JSON strings, where each string contains timestamp, referrer, and location. This is populated using LPUSH.

TTL: Set on each update to ensure click data for expired links is eventually cleaned up.

5. API Endpoints
The service exposes three RESTful API endpoints.

Method

Route

Description

POST

/shorturls

Creates a new shortened URL.

GET

/{shortcode}

Redirects to the original long URL.

GET

/shorturls/{shortcode}

Retrieves usage statistics for a short URL.

6. Assumptions
Geolocation: The requirement for "coarse-grained geographical location" per click was implemented with a placeholder value of "Unknown", as integrating a reliable IP-to-geolocation service was outside the scope of this evaluation.

Base URL: The shortened URL is generated with a hardcoded base URL of http://localhost:8080. In a production environment, this would be dynamically configured.

Shortcode Generation: The auto-generated shortcode is a 6-byte, URL-safe Base64 string. The service attempts to regenerate up to 5 times in case of a collision, which is statistically improbable.