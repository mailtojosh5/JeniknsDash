REM filepath: run-tests.bat
@echo off
REM Gatling Performance Test Runner for Java

echo =====================================================
echo   Gatling Performance Test - Java
echo =====================================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Maven not found. Please install Maven and add to PATH
    exit /b 1
)

for /f "delims=" %%A in ('where mvn') do set "MVN_BIN=%%A"
echo %MVN_BIN% | findstr /c:" " >nul
if %errorlevel% equ 0 (
    echo WARNING: Maven is installed in a path containing spaces.
    echo          This can trigger Windows Jansi propagation errors when running Gatling.
    echo          Install Maven in a folder without spaces, e.g. C:\maven, if you see runtime errors.
    echo.
)

echo Available Simulations:
echo 1. GoogleHomepageSimulation   - Tests Google homepage
echo 2. GoogleSearchSimulation     - Tests search functionality
echo 3. GoogleStressSimulation     - Stress/spike test
echo 4. ReqResApiSimulation        - Tests ReqRes API
echo 5. All Simulations            - Run all tests
echo.

if "%1"=="" (
    echo Usage: %0 [homepage^|search^|stress^|reqres^|all]
    echo.
    echo Example:
    echo   %0 homepage
    echo   %0 all
    exit /b 1
)

set SIMULATION=%1

if "%SIMULATION%"=="homepage" (
    echo Running Google Homepage Simulation...
    call mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleHomepageSimulation
) else if "%SIMULATION%"=="search" (
    echo Running Google Search Simulation...
    call mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleSearchSimulation
) else if "%SIMULATION%"=="stress" (
    echo Running Google Stress Simulation...
    call mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleStressSimulation
) else if "%SIMULATION%"=="reqres" (
    echo Running ReqRes API Simulation...
    call mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.ReqResApiSimulation
) else if "%SIMULATION%"=="all" (
    echo Running All Simulations...
    call mvn clean gatling:test
) else (
    echo Unknown simulation: %SIMULATION%
    echo Valid options: homepage, search, stress, reqres, all
    exit /b 1
)

if %errorlevel% equ 0 (
    echo.
    echo =====================================================
    echo   Test completed successfully!
    echo   Check 'target\gatling\' for HTML reports
    echo =====================================================
) else (
    echo.
    echo =====================================================
    echo   Test failed!
    echo =====================================================
    exit /b 1
)