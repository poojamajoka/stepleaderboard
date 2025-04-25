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

| Category       | Technologies                        |
|----------------|-------------------------------------|
| Backend        | Spring Boot 3, Java 21              |
| Security       | JWT, Spring Security                |
| Database       | SQL/H2 (dev)                        |
| Build Tool     | Gradle                              |
| Containerization | Docker                            |
| Version Control| GitHub                              |
| Microsoft Tech | Azure, Azure SQL, Azure App Service |




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

<img width="596" alt="Screenshot 2025-04-25 at 1 21 44 AM" src="https://github.com/user-attachments/assets/b7114684-1ab2-41b9-8d7b-c993099a8564" />
<img width="618" alt="Screenshot 2025-04-24 at 11 07 36 PM" src="https://github.com/user-attachments/assets/378ba825-850b-405a-affb-af76df8ad5a3" />
<img width="886" alt="Screenshot 2025-04-25 at 1 01 20 AM" src="https://github.com/user-attachments/assets/97d655f8-dc56-4a3e-a7aa-97c8fce5abfe" />
<img width="869" alt="Screenshot 2025-04-25 at 1 01 48 AM" src="https://github.com/user-attachments/assets/48b042a1-74a0-4c93-b58d-0c05d5e62b25" />
<img width="886" alt="Screenshot 2025-04-25 at 1 02 49 AM" src="https://github.com/user-attachments/assets/ffc32ef5-fbe2-458f-ab34-0e688ce5c542" />
<img width="878" alt="Screenshot 2025-04-25 at 1 05 22 AM" src="https://github.com/user-attachments/assets/eefbf7fc-9195-4443-ac0d-a80f9f7bd304" />
<img width="872" alt="Screenshot 2025-04-25 at 1 06 06 AM" src="https://github.com/user-attachments/assets/72b5c3b8-5e3f-4e56-86cd-098de04823ce" />
<img width="890" alt="Screenshot 2025-04-25 at 1 06 36 AM" src="https://github.com/user-attachments/assets/b51a0d67-f598-414f-8198-12345b34a75d" />
<img width="874" alt="Screenshot 2025-04-25 at 1 08 20 AM" src="https://github.com/user-attachments/assets/36aabb97-4db3-43b4-ba7f-0dfd470f9920" />














