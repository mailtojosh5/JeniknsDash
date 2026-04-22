# Quick Start Guide for Gatling Performance Tests

## Installation

### Step 1: Install Java
Ensure Java 11 or higher is installed:
```bash
java -version
```

If not installed, download from: https://www.oracle.com/java/technologies/downloads/

### Step 2: Install Maven
Ensure Maven 3.6.0 or higher is installed:
```bash
mvn --version
```

If not installed, download from: https://maven.apache.org/download.cgi

## Running Tests

### Option 1: Using Script (Recommended)

**On Windows:**
```cmd
cd c:\Users\tutru\gatling-test
run-tests.bat homepage
run-tests.bat search
run-tests.bat stress
run-tests.bat all
```

**On Linux/Mac:**
```bash
cd ~/gatling-test
./run-tests.sh homepage
./run-tests.sh search
./run-tests.sh stress
./run-tests.sh all
```

### Option 2: Using Maven Directly

**Homepage Test:**
```bash
mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleHomepageSimulation
```

**Search Test:**
```bash
mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleSearchSimulation
```

**Stress Test:**
```bash
mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleStressSimulation
```

**All Tests:**
```bash
mvn clean gatling:test
```

## Understanding Results

### Report Location
After running tests, open the HTML report:
```
target/gatling/[timestamp]/index.html
```

### Key Metrics
- **Response Time**: How long requests took to complete
- **Throughput**: Requests per second
- **Error Rate**: Percentage of failed requests
- **Active Users**: Number of concurrent users during test

## Customizing Tests

### Modify Load Profile
Edit simulation files to change:
- User counts: `rampUsers(N)` 
- Duration: `.during(Xs)`
- Sustained load: `constantUsersPerSec(N)`
- Spike load: `atOnceUsers(N)`

### Add New Simulation
1. Create file: `src/test/java/com/example/simulations/MySimulation.java`
2. Add `package com.example.simulations;` at the top
3. Extend `Simulation` class
4. Run with Maven: `mvn gatling:test`

### Adjust Timeouts
Edit `src/test/resources/gatling.conf`:
```conf
http {
    connectionTimeout = 60000   # milliseconds
    requestTimeout = 60000
}
```

## Troubleshooting

**Maven command not found**
- Add Maven `bin` directory to system PATH

**Java not found**
- Install Java JDK 11+
- Add Java `bin` directory to system PATH

**Connection refused**
- Check internet connectivity to google.com
- verify if google.com is accessible in your region

**Port already in use**
- Gatling uses port 3000 for results
- Kill any process using that port

**Build fails**
- Run: `mvn clean`
- Try: `mvn -U clean compile`

## Project Structure

```
gatling-test/
├── pom.xml                           # Maven config
├── run-tests.bat                     # Windows batch script
├── run-tests.sh                      # Unix shell script
├── QUICKSTART.md                     # This file
├── src/test/
│   ├── java/com/example/simulations/
│   │   ├── GoogleHomepageSimulation.java
│   │   ├── GoogleSearchSimulation.java
│   │   ├── GoogleStressSimulation.java
│   │   └── ReqResApiSimulation.java
│   └── resources/
│       └── gatling.conf
└── target/
    └── gatling/                      # Test results (generated)
```

## Performance Standards

These simulations test against realistic expectations:
- **Excellent**: Response time < 1 second, 100% success
- **Good**: Response time < 2 seconds, 99%+ success
- **Acceptable**: Response time < 5 seconds, 95%+ success
- **Poor**: Response time > 5 seconds, < 95% success

## References

- [Gatling Documentation](https://gatling.io/docs/gatling/latest/)
- [Maven Documentation](https://maven.apache.org/)
- [Java Documentation](https://docs.oracle.com/en/java/)

## Legal Notice

⚠️ **Important**: Load testing should only be performed on systems you own or have explicit authorization to test. Unauthorized load testing may violate terms of service and applicable laws.

This project demonstrates performance testing concepts and should be used responsibly.
