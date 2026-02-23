# Photographic Archive Management System

## Description
È un’applicazione console basata su Java che gestisce archivi fotografici.
Per la persistenza dei dati viene utilizzato un database JSON.


## Features
- Archive management (create, view)
- Photograph management (add, search, view)
- Subject catalog (Personaggio, Artista, Politico, Luogo, Oggetto, OperaArte)
- JSON-based data persistence

## Setup Instructions

### 1. Download Gson Library

**Option A - Manual Download:**
1. Download Gson JAR from: https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
2. Create a `lib` folder in the project root
3. Put `gson-2.10.1.jar` inside the `lib` folder

**Option B - Using PowerShell:**
```powershell
# Create lib folder and download Gson
New-Item -ItemType Directory -Force -Path "lib"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar" -OutFile "lib/gson-2.10.1.jar"
```

### 2. Compile the Project

```powershell
# Windows PowerShell
javac -cp "lib/gson-2.10.1.jar" -d out src/database/*.java src/gestione/*.java src/progettoarchivio/*.java
```

### 3. Run the Application

```powershell
# Windows PowerShell
java -cp "out;lib/gson-2.10.1.jar" progettoarchivio.ProgettoArchivio
```

## Project Structure
```
├── src/
│   ├── database/
│   │   ├── JsonDatabase.java       # JSON persistence utility
│   │   └── RuntimeTypeAdapterFactory.java  # Polymorphism support
│   ├── gestione/
│   │   ├── Archivio.java           # Archive class
│   │   ├── Fotografia.java         # Photograph base class
│   │   ├── FotoAColore.java        # Color photograph
│   │   ├── GestoreArchivi.java     # Archive manager (Singleton)
│   │   └── Responsabile.java       # Responsible person
│   └── progettoarchivio/
│       ├── Soggetto.java           # Abstract subject class
│       ├── Personaggio.java        # Person
│       ├── Artista.java            # Artist
│       ├── Politico.java           # Politician
│       ├── Luogo.java              # Place
│       ├── Oggetto.java            # Object
│       ├── OperaArte.java          # Artwork
│       ├── CatalogoSoggetti.java   # Subject catalog (Singleton)
│       └── ProgettoArchivio.java   # Main class
├── data/                           # JSON database files (auto-created)
│   ├── archivi.json
│   └── soggetti.json
└── lib/
    └── gson-2.10.1.jar             # Gson library
```

## JSON Database Location
Data is saved in the `data/` folder:
- `archivi.json` - All archives with their photographs
- `soggetti.json` - Subject catalog

## Usage
1. Run the application
2. Use menu options to manage archives and photographs
3. Select "4. Salva ed esci" to save all data to JSON files

