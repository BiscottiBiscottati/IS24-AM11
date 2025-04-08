# IS24-AM11: Codex Naturalis Digital Version

## Team Members

- [Zining Chen](mailto:zining.chen@mail.polimi.it)
- [Edoardo Bergamo](mailto:edoardo.bergamo@mail.polimi.it)
- [Osama Atiqi](mailto:osama.atiqi@mail.polimi.it)
- [Ferdinando Maria Cioffi](mailto:ferdinandomaria.cioffi@mail.polimi.it)

## Project Description

This project is a digital implementation of the board game ”[Codex Naturalis](https://www.craniocreations.it/prodotto/codex-naturalis)”.

You can find the official rules here:

- [Codex Naturalis Rules (ENG)](https://studiobombyx.com/en/rules/codex/)
- [Codex Naturalis Rules (ITA)](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf)

## Features

### Functional Requirements

| Requirement      | Status | Description                                  |
|------------------|--------|----------------------------------------------|
| Complete Ruleset | ✅      | Implements all rules of the base game.       |
| Model            | ✅      | Core game logic and state representation.    |
| Controller       | ✅      | Manages game flow and user interactions.     |
| RMI              | ✅      | Network communication via Java RMI.          |
| Socket           | ✅      | Network communication via Sockets.           |
| TUI              | ✅      | Text-based User Interface for gameplay.      |
| GUI              | ✅      | Graphical User Interface using JavaFX.       |

### Additional Features

| Requirement    | Status | Description                                      |
|----------------|--------|--------------------------------------------------|
| Persistence    | ✅      | Game state can be saved and potentially loaded.  |
| Resilience     | ✅      | Handles client disconnections and reconnections. |
| Chat           | ✅      | In-game chat functionality between players.      |
| Multiple Games | ❌      | Server currently supports only one game instance.|

### Non-Functional Requirements

| Requirement  | Status | Description                                      |
|--------------|--------|--------------------------------------------------|
| UML Diagrams | ✅      | Class and Sequence diagrams provided in `/UML`.  |
| Javadoc      | ✅      | Code documentation generated in `/javadoc`.      |
| Peer Review  | ✅      | Peer review documents available in `/Peer Review`.|

## Project Structure

- **`/src`**: Contains the Java source code (`main`) and tests (`test`).
- **`/javadoc`**: Contains the generated Javadoc API documentation.
- **`/UML`**: Contains UML diagrams (Class and Sequence).
- **`/Peer Review`**: Contains peer review documents.
- **`/target`**: Contains compiled code and the packaged JAR file (after build).
- **`pom.xml`**: Maven project configuration file.
- **`mvnw`/`mvnw.cmd`**: Maven wrapper scripts.
- **`README.md`**: This file.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or later.

### Building the Project

This project uses the Maven wrapper, so you don’t need a separate Maven installation.

1. **Clone the repository:**

    ```bash
    git clone <repository-url>
    cd IS24-AM11
    ```

2. **Package the application:**
    - On Linux/macOS:

      ```bash
      ./mvnw clean package
      ```

    - On Windows:

      ```bash
      .\mvnw.cmd clean package
      ```

    This command compiles the code, runs tests, and creates a runnable JAR file (`target/AM11-1.0-ALPHA.jar`) with all dependencies included.

### Running the Application

The application can be run in server mode or client mode.

**Run as Server:**

```bash
java -jar target/AM11-1.0-ALPHA.jar server [-rmi <rmi_port>] [-socket <socket_port>]
```

- Replace `<rmi_port>` and `<socket_port>` with desired port numbers (defaults will be used if omitted).

**Run as Client:**

```bash
java -jar target/AM11-1.0-ALPHA.jar client [-ui <tui|gui>] [-host <server_host>] [-rmi <rmi_port>] [-socket <socket_port>]
```

- `-ui`: Choose the interface (`tui` or `gui`, defaults to `gui` if omitted).
- `-host`: Specify the server’s hostname or IP address (defaults to localhost).
- `-rmi`/`-socket`: Specify the ports the server is listening on (if different from defaults).

## Technologies Used

- **Language:** Java 21
- **Build Tool:** Maven
- **Networking:** Java RMI, Sockets
- **GUI:** JavaFX
- **Database:** SQLite (for persistence)
- **Testing:** JUnit 5, Mockito
- **Logging:** SLF4j with Logback

## License

This project is licensed under the GNU General Public License v2.0. See the [LICENSE](javadoc/legal/LICENSE) file for details.
