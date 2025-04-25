# Step Tracking API 🚶‍♂️📊

StepThrone is a fitness tracking application that helps users monitor their daily step counts and compete with others. Key features include:

📲 User accounts with secure JWT login

👣 Daily step logging with progress tracking

🏆 Competitive leaderboards (daily & all-time)

📅 Historical data viewing by date

📊 Personal analytics (calories, distance)

## Features ✨

- **JWT Authentication** 🔐
    - Secure registration/login
    - Token-based authorization
- **Step Tracking** 👣
    - Submit daily step counts
    - View personal history
- **Leaderboards** 🏆
    - Global rankings
    - Daily statistics
- **User Profiles** 👤
    - Health profile
    

## Tech Stack 💻

| Category      | Technologies           |
|---------------|------------------------|
| Backend       | Spring Boot 3, Java 21 |
| Security      | JWT, Spring Security   |
| Database      | SQL/H2 (dev)           | 
| Build Tool    | Gradle                 | 



## API Endpoints 🌐

### Authentication
| Method | Endpoint              | Description          |
|--------|-----------------------|----------------------|
| `POST` | `/api/v1/auth/register` | User registration  |
| `POST` | `/api/v1/auth/login`    | JWT token generation |

### Steps Tracking
| Method | Endpoint                    | Description                      |
|--------|-----------------------------|----------------------------------|
| `POST` | `/api/v1/steps`             | Submit daily steps               |
| `GET`  | `/api/v1/steps/history`     | Get user's step history          |
| `GET`  | `/api/v1/steps/user/daily`  | Get user steps for specific date |

### Leaderboards
| Method | Endpoint                          | Description                     |
|--------|-----------------------------------|---------------------------------|
| `GET`  | `/api/v1/steps/leaderboard/daily` | Daily rankings (requires date)  |
| `GET`  | `/api/v1/steps/leaderboard/global`| All-time rankings              |

