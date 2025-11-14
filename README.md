# Photographic Archive Management System

This is a Java-OOP based application for managing a photographic archive. The system allows for the cataloging of photographs, management of archives, and tracking of subjects depicted in the photos. This project was developed as an academic exercise in object-oriented programming.

## Project Overview

The system is designed to fulfill the requirements of a photographic archive manager. It provides functionalities to organize photographs into distinct archives, manage the personnel responsible for them, and catalog a wide variety of subjects including people, places, art, and objects. The core architecture is built on OOP principles like inheritance, polymorphism, and encapsulation to create a modular and extensible system.

## Features

The project is divided into two main modules, developed by two different contributors.

### Core Archive Management (Student A)

*   **Archive Management**: Create and manage multiple archives using the `Archivio` class.
*   **Photograph Handling**: Add, remove, and search for photographs (`Fotografia` and `FotoAColore`) within an archive.
*   **Data Persistence**: A singleton `GestoreArchivi` class handles saving all data to the disk and loading it back into the application, ensuring data is not lost between sessions.
*   **Personnel Management**: Assign a `Responsabile` to each archive to track ownership and contact details.
*   **Main Application Flow**: The main entry point (`ProgettoArchivio`) provides a text-based user interface to interact with the system's features.

### Subject Cataloging (Student B)

*   **Detailed Subject Modeling**: A flexible and abstract data model for different types of subjects (`Soggetto`).
*   **Rich Subject Hierarchy**:
    *   `Personaggio`: A generic person, which can be specialized into:
        *   `Politico`: A politician with party and role information.
        *   `Artista`: An artist with a specific primary activity.
    *   `OperaArte`: Represents a work of art, linked to an artist and a location.
    *   `Luogo`: Represents a geographical place.
    *   `Oggetto`: Represents a generic inanimate object.
*   **Centralized Catalog**: The `CatalogoSoggetti` class provides a central repository for all subjects, using a unique key for efficient lookup and management.

## How to Compile and Run

To compile and run the project, you need to have a Java Development Kit (JDK) installed.

1.  Open a terminal or command prompt in the root directory of the project.

2.  Compile all the Java source files using the following command:
    ```sh
    javac -d . src/gestione/*.java src/progettoarchivio/*.java
    ```
    This command compiles all `.java` files from the `src` directory and places the compiled `.class` files in the root project folder, respecting their package structure.

3.  Run the application with this command:
    ```sh
    java progettoarchivio.ProgettoArchivio
    ```
    This will start the application and display the main menu in the console.

## Project Structure

The source code is organized into two main packages inside the `src` directory:

*   `src/gestione/`: Contains all the classes related to the management of archives, photographs, and responsible personnel (Student A's work).
*   `src/progettoarchivio/`: Contains the data model for subjects and the cataloging system (mostly Student B's work), as well as the main application class `ProgettoArchivio.java` (Student A's work).

*Note: There are duplicate `.java` files in the root directory. These appear to be older or un-packaged versions of the classes inside `src/`. For compilation and execution, only the files within the `src/` directory are used.*

## Future Development

The following are potential future enhancements for the project:

*   **Graphical User Interface (GUI)**: Replace the current text-based interface with a modern GUI using a framework like JavaFX, including features like drag-and-drop for photos and image previews.
*   **Client-Server Architecture**: Evolve the system into a multi-user application with a central server managing the data and multiple clients connecting to it.
*   **Cloud Integration**: Add functionality to back up or sync archives with cloud storage services like Google Drive or AWS S3.
*   **Advanced Search**: Implement more complex search and filtering capabilities (e.g., search by date range, subject attributes, etc.).

## Contributors

*   **Student A**: Responsible for the archive management system, data persistence, and the main application logic.
*   **Student B**: Responsible for the detailed subject data model and the cataloging system.
