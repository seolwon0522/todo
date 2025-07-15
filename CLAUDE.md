# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a full-stack Todo List application with focus time tracking and a points system. The backend is built with Spring Boot 3.5.3 using Java 17, and the frontend is a Next.js 15 application with TypeScript.

### Architecture

**Backend (Spring Boot)**
- **Entity Layer**: JPA entities for User, Todo, and TimeSession with MySQL database
- **Repository Layer**: Spring Data JPA repositories for data access
- **Service Layer**: Business logic including user authentication, todo management, and points calculation
- **Controller Layer**: REST API endpoints with session-based authentication
- **Security**: Spring Security with custom user details service

**Frontend (Next.js)**
- **Pages**: Authentication (login/register), dashboard, user profile, todo management
- **Components**: Reusable UI components including todo cards, navigation, and form components
- **Hooks**: Custom hooks for authentication, todo management, and API interactions
- **Services**: API client services for backend communication

### Key Features

- User authentication and session management
- Todo CRUD operations with status tracking (TODO, IN_PROGRESS, DONE)
- Focus time tracking with stopwatch functionality
- Points system (1 minute = 1 point) with leftover seconds accumulation
- Kanban-style board interface

## Development Commands

### Backend (Spring Boot)
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

### Frontend (Next.js)
```bash
# Navigate to frontend directory
cd frontend/todolist

# Install dependencies
npm install

# Development server (with Turbo)
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Lint code
npm run lint
```

## Database Configuration

The application uses MySQL database with the following configuration:
- Database: `todolist`
- Default connection: `localhost:3306`
- JPA auto-configuration creates tables automatically
- Connection details in `src/main/resources/application.properties`

For development, you can also use H2 in-memory database (configured in dependencies).

## API Structure

**Authentication Endpoints** (`/api/user`):
- POST `/register` - User registration
- POST `/login` - User login
- POST `/logout` - User logout
- GET `/me` - Get current user info

**Todo Endpoints** (`/api/todos`):
- GET `/` - Get user's todos
- POST `/` - Create new todo
- PUT `/{id}` - Update todo
- DELETE `/{id}` - Delete todo

**Points Endpoints** (`/api/points`):
- POST `/focus-session` - Record focus session and calculate points

## Key Implementation Details

### Points System
- 1 minute of focus time = 1 point
- Leftover seconds (< 60) are accumulated for next session
- Points calculation handled in `PointsService.java:42`

### Time Tracking
- Focus sessions stored in `TimeSession` entity
- Frontend stopwatch component tracks time per todo
- Time automatically converts to points when session ends

### Security
- Session-based authentication using Spring Session
- CORS configured for frontend communication
- Custom user details service for authentication

## Environment Setup

Create `.env.local` in `frontend/todolist/`:
```
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## File Structure Notes

- Backend controllers are in `src/main/java/com/example/demo/Controller/`
- Frontend pages follow Next.js 13+ app router structure in `src/app/`
- Shared UI components are in `src/components/`
- API services are organized in `src/lib/services/`