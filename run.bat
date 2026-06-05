@echo off
REM --- Banking Management System Build Script (Windows) ---
REM Requirements: JDK 17+, MySQL running, mysql-connector-j-8.3.0.jar in lib\

set CONNECTOR=lib\mysql-connector-j-9.7.0.jar
set OUT=out

if not exist lib mkdir lib
if not exist %OUT% mkdir %OUT%

if not exist %CONNECTOR% (
    echo [!] Download mysql-connector-j-8.3.0.jar from:
    echo     https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar
    echo     Place it in the lib\ folder, then rerun.
    pause
    exit /b 1
)

echo [*] Compiling...
dir /s /b src\*.java > sources.txt
javac -cp %CONNECTOR% -d %OUT% @sources.txt
del sources.txt

if errorlevel 1 (
    echo [!] Compilation failed.
    pause
    exit /b 1
)

echo [*] Running...
java -cp "%OUT%;%CONNECTOR%" BankingSystem
