
# Cineverse

**A comprehensive movie rating and community platform** built for film enthusiasts. Cineverse empowers users to discover cinema, rate and review films, engage in thoughtful discussions with other movie lovers, track personal watchlists, and build a curated collection of favorite movies. Whether you're a casual viewer seeking recommendations or a dedicated cinephile looking to share your passion, Cineverse provides a complete ecosystem to explore the world of film, exchange insights with the community, and keep your movie journey organized. With an intuitive desktop interface and a robust backend, Cineverse transforms how audiences experience and interact with movies.

## Features

✨ **Movie Discovery & Info** – Browse a library of films with details (title, director, actors, plot, release date, rating).

⭐ **Rate & Review** – Leave star ratings (1–5) and write detailed reviews for any movie. View community reviews and ratings.

🎬 **Community Posts** – Create discussion posts about movies, reply to other posts, and like/engage with the community.

💬 **Post & Reply System** – Start conversations about films, reply to discussions, like posts and replies to show support.

❤️ **Favorites & Watchlist** – Mark movies as favorites and add them to your personal watchlist to track what you want to watch.

👤 **User Profiles** – Register, log in, manage your profile, and track your personal movie activity.

🏷️ **Genre Filtering** – Browse and filter movies by genre.

## Tech Stack

- **Backend**: Spring Boot 3.x, Spring Data JPA, MySQL, REST API
- **Frontend**: JavaFX (desktop GUI)
- **Build**: Maven with included wrappers (`mvnw`)
- **Language**: Java 24

## Requirements

- JDK 24 installed and JAVA_HOME set
- MySQL server (or compatible) for the backend
- At least 2GB free memory for build/run

Note: both `backend/` and `frontend/` include Maven wrapper scripts (`mvnw`) so you do not need a system-installed Maven.

## Quick setup

1. Clone the repository (if you haven't already):

```bash
git clone https://github.com/Bahaa011/Cineverse.git
cd Cineverse
```

2. Prepare the database. By default the backend expects MySQL at `localhost:3307` with a database named `cineverse_sb` and username/password `root`/`root`. You can change these in `backend/src/main/resources/application.properties`.

Example SQL (run in MySQL shell or a client):

```sql
CREATE DATABASE IF NOT EXISTS cineverse_sb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- If you need to create or set a user (optional):
-- CREATE USER 'cineuser'@'localhost' IDENTIFIED BY 'strong_password';
-- GRANT ALL PRIVILEGES ON cineverse_sb.* TO 'cineuser'@'localhost';
-- FLUSH PRIVILEGES;
```

If you keep the defaults in `application.properties`, make sure MySQL listens on port `3307` or update the URL to the port your MySQL instance uses.

## Running the backend

Move into the backend folder and run with the included wrapper:

```bash
cd backend
./mvnw spring-boot:run
```

This runs the Spring Boot application (main class: `com.example.cineverse.CineverseApplication`). The app will read the datasource configuration from `backend/src/main/resources/application.properties`.

To build a runnable jar instead:

```bash
./mvnw -DskipTests package
java -jar target/*.jar
```

API docs (if enabled) are provided by springdoc OpenAPI at `/swagger-ui.html` or `/swagger-ui/index.html` when the backend is running.

## Running the frontend (JavaFX)

The frontend is a JavaFX desktop client. From the repo root or the `frontend/` folder run:

```bash
cd frontend
./mvnw javafx:run
```

The `pom.xml` contains a `javafx-maven-plugin` configuration that launches the `com.example.frontend.HelloApplication` main class. Ensure your JAVA_HOME points to a JDK 24 installation that is compatible with JavaFX.

Notes:
- If you run into JavaFX module issues, confirm the JDK and JavaFX plugin versions. The project currently references JavaFX 17 modules pulled by the Maven plugin.
- The frontend communicates with the backend APIs. Start the backend first so the client can fetch movie and user data.

## Building for production

- Backend: `cd backend && ./mvnw -DskipTests package` produces a jar in `backend/target/`.
- Frontend: packaging a native image or jlink distribution is possible via the `javafx-maven-plugin` configuration in `frontend/pom.xml` but may require additional tooling and a matching JDK.

## Tests

Run backend tests:

```bash
cd backend
./mvnw test
```

Run frontend tests (if any):

```bash
cd frontend
./mvnw test
```

## Project structure (high level)

- `backend/src/main/java/com/example/cineverse/` — Spring Boot app, controllers, services, repositories, models, dto
- `backend/src/main/resources/application.properties` — DB and Spring configuration
- `frontend/src/main/java/com/example/frontend/` — JavaFX app, clients to call backend APIs, controllers and views
- `frontend/src/main/resources/` — static assets (css, images, fxml, etc.)

## Troubleshooting

- Database connection errors: check `application.properties` and ensure MySQL is running and reachable at the configured host/port. Verify database user credentials and that the database exists.
- JavaFX errors: ensure JAVA_HOME points to JDK 24 and the JavaFX runtime is compatible with your JDK. Consider installing the matching JavaFX SDK or using a JDK bundled with JavaFX.
- Port conflicts: Spring Boot defaults to port 8080. If another service uses that port, set `server.port` in `application.properties`.

## Contributing

Contributions are welcome. Please open issues or PRs against the `main` branch. Include a description of the change, rationale, and any setup needed to test it.

## License & Contact

This repository does not include an explicit license file. If you plan to publish or accept external contributions, add a LICENSE file (for example MIT or Apache-2.0).

For questions, open an issue in the repository or contact the maintainer through the GitHub profile.

---

## Summary

Cineverse is a full-stack Java application for movie enthusiasts to discover, rate, review, and discuss films. It combines a Spring Boot REST backend with a JavaFX desktop frontend, backed by MySQL for persistence. Perfect for learning modern Java web development or as a foundation for a movie community platform.
