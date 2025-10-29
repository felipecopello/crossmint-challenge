# Crossmint Challenge

## Overview

This is a **Spring Boot** client application built to interact with the **Crossmint Challenge API**.  
It supports placing “Astral Objects” (Polyanet, Soloon, Cometh) on a map grid, retrieving the goal map, and managing those objects via HTTP calls.

The project uses **Java 21**, **Spring Boot**, **Spring Retry**, and **Lombok** to keep the code clean and maintainable.

This app will retrieve the goal and solve it in a single run. This implementation solves both task 1 and 2.

---

## Key Features

- REST client for the Crossmint API (`create`, `delete`, etc.).
- Fetches and parses the goal map (2D JSON grid).
- Handles retries automatically for rate limits (HTTP `429 Too Many Requests`).
- Reads configuration from `application.yml` (`base-url`, `candidate-id`, retry settings).
- Clear separation of layers:
    - **Handler → Service → HTTP Client**
- Uses **Lombok** for boilerplate reduction.
- Automatically shuts down after execution (via `CommandLineRunner`).

---

## Project Structure

```plaintext
├── build.gradle.kts
├── settings.gradle.kts
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/crossmint/
│   │   │       ├── config/                     # Configuration classes (application.yml mapping)
│   │   │       ├── client/                     # Low-level HTTP communication (RestTemplate)
│   │   │       ├── service/                    # Business logic, retryable operations
│   │   │       ├── handler/                    # Orchestrates logic on startup (CommandLineRunner)
│   │   │       ├── model/                      # Domain models (AstralObject, Goal, etc.)
│   │   │       └── CrossmintApplication.java   # Main entry point
│   └── resources/
│       └── application.yml                     # App configuration (API, retry, logging)

| Package   | Purpose                                                                    |
|-----------|----------------------------------------------------------------------------|
| `config`  | Maps configuration from `application.yml` (API URLs, retry, candidate ID). |
| `client`  | Encapsulates HTTP operations using `RestTemplate`.                         |
| `service` | Contains business logic that coordinates retries and builds requests.      |
| `handler` | Entry point that triggers API interactions and gracefully shuts down.      |
| `model`   | Domain models: `AstralObject` (and subclasses), `Goal`, `AstralType`, etc. |
```

## Technologies Used

| Technology              | Purpose                                                   |
|-------------------------|-----------------------------------------------------------|
| **Java 21**             | Modern language features (pattern matching, records, etc.) |
| **Spring Boot**         | Framework for configuration, dependency injection, and execution |
| **Spring Retry**        | Retry failed HTTP calls (e.g., on `429 Too Many Requests`) |
| **Lombok**              | Generates boilerplate (getters, setters, constructors)     |
| **RestTemplate**        | Performs HTTP requests to Crossmint API                    |
| **Gradle (Kotlin DSL)** | Build system and dependency management                    |
| **Spotless**            | Automatic code formatting using Google Java Style         |
| **YAML configuration**  | For base URL, candidate ID, retry settings, and logging    |

---

## Usage

1. Configure your `application.yml` with your own **candidate ID**:

   ```yaml
   crossmint:
     api:
       base-url: https://challenge.crossmint.io/api/
       candidate-id: your-candidate-id
   ```
2. Build and run the project:

   ```bash
   ./gradlew runApp
Alternatively, you can run the main method directly from your IDE by executing the Application.java class.