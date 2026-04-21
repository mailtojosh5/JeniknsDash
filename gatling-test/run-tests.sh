#!/bin/bash

# Gatling Performance Test Runner for google.com

echo "====================================================="
echo "  Gatling Performance Test - Google.com"
echo "====================================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven not found. Please install Maven and add to PATH"
    exit 1
fi

echo "Available Simulations:"
echo "1. GoogleHomepageSimulation   - Tests Google homepage"
echo "2. GoogleSearchSimulation     - Tests search functionality"
echo "3. GoogleStressSimulation     - Stress/spike test"
echo "4. All Simulations            - Run all tests"
echo ""

if [ -z "$1" ]; then
    echo "Usage: $0 [homepage|search|stress|all]"
    echo ""
    echo "Example:"
    echo "  $0 homepage"
    echo "  $0 all"
    exit 1
fi

SIMULATION=$1

case $SIMULATION in
    homepage)
        echo "Running Google Homepage Simulation..."
        mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleHomepageSimulation
        ;;
    search)
        echo "Running Google Search Simulation..."
        mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleSearchSimulation
        ;;
    stress)
        echo "Running Google Stress Simulation..."
        mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleStressSimulation
        ;;
    all)
        echo "Running All Simulations..."
        mvn clean gatling:test
        ;;
    *)
        echo "Unknown simulation: $SIMULATION"
        echo "Valid options: homepage, search, stress, all"
        exit 1
        ;;
esac

if [ $? -eq 0 ]; then
    echo ""
    echo "====================================================="
    echo "  Test completed successfully!"
    echo "  Check 'target/gatling/' for HTML reports"
    echo "====================================================="
else
    echo ""
    echo "====================================================="
    echo "  Test failed!"
    echo "====================================================="
    exit 1
fi
