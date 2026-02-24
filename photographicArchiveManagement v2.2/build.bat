@echo off
REM Build script for Photographic Archive Project
REM This script compiles the Java project with Gson dependency

echo ===== Compiling Photographic Archive Project =====

REM Create output directory if it doesn't exist
if not exist "out" mkdir out

REM Compile all Java files
javac -encoding UTF-8 -cp "lib/gson-2.10.1.jar" -d out src/database/*.java src/gestione/*.java src/progettoarchivio/*.java src/gui/*.java src/gui/views/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===== Compilation Successful! =====
    echo.
    echo To run the application, use:
    echo   java -cp "out;lib/gson-2.10.1.jar" gui.MainApplication
) else (
    echo.
    echo ===== Compilation Failed! =====
)

pause
