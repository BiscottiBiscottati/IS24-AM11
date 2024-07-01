# SOFTWARE ENGINEERING FINAL PROJECT 2024

## Team Members

- [Zining Chen](mailto:zining.chen@mail.polimi.it)
- [Edoardo Bergamo](mailto:edoardo.bergamo@mail.polimi.it)
- [Osama Atiqi](mailto:osama.atiqi@mail.polimi.it)
- [Ferdinando Maria Cioffi](mailto:ferdinandomaria.cioffi@mail.polimi.it)

## Project Description

The project consists of the development of the board
game "[Codex Naturalis](https://www.craniocreations.it/prodotto/codex-naturalis)"
in a digital version.

You can download the rules here:

[Codex Naturalis Rules (ENG)](https://studiobombyx.com/en/rules/codex/)

[Codex Naturalis Rules (ITA)](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf)

## Project Requirements

### Functional Requirements

| Requirement      | Status |
|------------------|--------|
| Complete Ruleset | ✅      |
| Model            | ✅      |
| Controller       | ✅      |
| RMI              | ✅      |
| Socket           | ✅      |
| TUI              | ✅      |
| GUI              | ✅      |

### Additional Features

| Requirement    | Status |
|----------------|--------|
| Persistence    | ✅      |
| Resilience     | ✅      |
| Chat           | ✅      |
| Multiple Games | ❌      |

### Non-Functional Requirements

| Requirement  | Status |
|--------------|--------|
| UML Diagrams | ✅      |
| Javadoc      | ✅      |
| Peer Review  | ✅      |

## Project Structure

The project is structured as follows:

- **src**: contains the source code of the project
- **javadoc**: contains the javadoc of the project
- **UML**: contains the UML class and sequence diagrams of the project
- **Peer Review**: contains the peer review of another team design

## How to run the project

To run the project, you need to first package it with maven and then run the jar file.

The resulting jar will be a shaded jar that contains all the dependencies.

Requirements:

- Java 21
- Maven

Run as server:

```bash
java -jar <jar-file> server [-rmi <port>] [-socket <port>]
```

Run as client:

```bash
java -jar <jar-file> client [-ui <tui|gui>]
```

## Additional Notes

The project uses:

- Sqlite as a database for saving the game state
- both Java RMI and Socket for the communication between server and client
- JavaFX for the GUI
- Maven for building and packaging
- JUnit5 for testing
- Slf4j for logging implemented with Logback
