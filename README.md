# ServiceSphere — Local Service Booking & Management System

ServiceSphere is a full-stack web application that connects customers
with local service providers and allows them to manage service bookings
through a single platform.

## Problem Statement

Finding reliable local professionals for household repairs and other
services can be difficult. Customers may struggle to find service
providers, schedule appointments, and track service requests.

ServiceSphere aims to simplify this process by providing a digital
platform for service discovery and booking management.

## Features

### Customer Features
- Customer registration and login
- Browse available services
- Search and filter services
- Create service bookings
- Select booking date, time, and urgency
- Provide service address and problem description
- View booking status and booking history

### Service Provider Features
- Provider registration and login
- Provider dashboard
- View incoming service requests
- Accept or reject bookings
- Update booking progress
- Mark services as completed
- Provider profile and availability APIs

### Backend Features
- REST API architecture
- User registration and login endpoints
- BCrypt password hashing
- Service category and service management
- Provider-service relationship management
- Booking management and status updates
- MySQL database integration
- Review and rating API

## Technology Stack

### Frontend
- React
- Vite
- JavaScript
- Axios
- CSS

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- REST APIs

### Database
- MySQL

## Project Architecture

React Frontend
      |
      | HTTP Requests using Axios
      v
Spring Boot REST API
      |
      | Spring Data JPA / Hibernate
      v
MySQL Database

## Project Structure

LocalManagementSystem/
├── backend/
│   └── Spring Boot application
└── frontend/
    └── React + Vite application

## Local Setup

### Requirements
- Java 17
- Node.js and npm
- MySQL
- Git

### Database Setup

Create the database:

CREATE DATABASE servicebooking;

The current local configuration expects MySQL on port 3307.

Configure DB_URL, DB_USERNAME, and DB_PASSWORD as environment
variables before starting the backend.

Never commit database passwords or other secrets.

### Start the Backend

Open a terminal in the backend directory:

mvnw.cmd spring-boot:run

The backend runs on:

http://localhost:8080

### Start the Frontend

Open another terminal in the frontend directory:

npm install
npm run dev

The frontend normally runs on:

http://localhost:5173

## Booking Workflow

1. A customer registers and logs in.
2. The customer browses available services.
3. The customer selects a service and provider.
4. The customer submits a booking request.
5. The provider views the incoming request.
6. The provider accepts or rejects the request.
7. The provider updates the service status.
8. The customer can view the updated booking status.

## Current Limitations and Future Enhancements

- Add production-grade authentication and authorization.
- Integrate customer reviews into the frontend.
- Replace manual provider ID entry with provider selection.
- Improve provider matching using skills, availability, distance,
  and customer ratings.
- Add actual problem-photo upload and storage.
- Add profile management and an admin dashboard.
- Add automated tests and deployment configuration.

## Security Notes

This is an academic prototype. The current frontend stores login
information in localStorage, so authentication and authorization
need further improvement before production deployment.

## Project Objective

The objective is to demonstrate full-stack web development,
REST API integration, relational database management, and
service booking workflow implementation.
