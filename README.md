Dynamic Role-Based Authorization System (Spring Boot + JWT + MySQL)

This project is a dynamic Role-Based Access Control (RBAC) system built with Spring Boot. It provides authentication using JWT and authorization based on roles and permissions stored in a relational database.

The system is designed to dynamically evaluate user permissions against requested endpoints and HTTP methods. Instead of hardcoding access rules, permissions are managed through database tables, allowing flexible and scalable access control management.

🔹 Key Features

JWT-based authentication

Dynamic authorization based on roles and permissions

Many-to-Many relationship between:

Users and Roles

Roles and Permissions

Endpoint + HTTP method level permission control

Secure password handling using BCrypt

RESTful API structure

Clean service-based architecture

🔹 Architecture Overview

User → can have multiple Roles

Role → can have multiple Permissions

Permission → defines allowed endpoint and HTTP method

Authorization is evaluated dynamically by matching:

The requested endpoint (using AntPathMatcher)

The HTTP method

The user's assigned roles and their associated permissions

This design allows administrators to modify access rules without changing application code.

⚠️ Performance Consideration

Since this implementation evaluates permissions by querying the database on each request, it introduces performance limitations in high-traffic scenarios. Every authorization check may trigger database access, which can become inefficient at scale.

To address this, future improvements would include:

Embedding permissions directly into the JWT token

Caching authorization data

Implementing a more optimized authorization strategy

As part of continuing my learning journey, I will be starting a new project focused on building a more performance-optimized and production-ready authorization architecture.
