# Gatling Google.com Performance Testing Project

A complete, production-ready Gatling performance testing project for load testing google.com with multiple simulation scenarios.

## 📋 Project Overview

This project provides three complete Gatling test scenarios:
- **Homepage Test**: Tests Google homepage accessibility and response times
- **Search Test**: Simulates realistic search workflow (navigate → search → paginate)
- **Stress Test**: High-load spike testing to measure resilience

## 🚀 Quick Start (30 seconds)

### Prerequisites
```cmd
java -version    # Should be 11 or higher
mvn --version    # Should be 3.6.0 or higher
```

### Run Your First Test
```cmd
cd c:\Users\tutru\gatling-test
run-tests.bat homepage
```

Results will be in: `target/gatling/[timestamp]/index.html`

## 📁 Project Structure

```
gatling-test/
├── pom.xml                              # Maven configuration
├── README.md                            # Main documentation
├── QUICKSTART.md                        # Getting started guide
├── SETUP.md                             # This file
├── run-tests.bat                        # Windows test runner
├── run-tests.sh                         # Unix test runner
│
├── src/test/
│   ├── scala/com/example/simulations/
│   │   ├── GoogleHomepageSimulation.scala      # Homepage test (10-12 users)
│   │   ├── GoogleSearchSimulation.scala        # Search workflow test (15 users)
│   │   └── GoogleStressSimulation.scala        # Stress test (30-50 concurrent)
│   │
│   └── resources/
│       └── gatling.conf                        # Gatling configuration
│
├── target/
│   └── gatling/                        # Test results (generated)
│       └── [timestamp]/
│           ├── index.html              # Main report
│           ├── stats.json              # Raw data
│           └── [other reports]
└── .gitignore                          # Git ignore file
```

## 🛠️ Installation Steps

### 1. Install Java 11+
Download from: https://www.oracle.com/java/technologies/downloads/
```cmd
java -version
```

### 2. Install Maven 3.6.0+
Download from: https://maven.apache.org/download.cgi
```cmd
mvn --version
```

### 3. Verify Installation
```cmd
cd c:\Users\tutru\gatling-test
mvn help:active-profiles
```

## ▶️ Running Tests

### Method 1: Batch Script (Windows - Recommended)
```cmd
cd c:\Users\tutru\gatling-test

# Run specific simulation
run-tests.bat homepage      # Tests Google homepage
run-tests.bat search        # Tests search functionality
run-tests.bat stress        # Tests under stress/spike load
run-tests.bat all           # Runs all simulations
```

### Method 2: Shell Script (Mac/Linux)
```bash
cd ~/gatling-test

# Make script executable
chmod +x run-tests.sh

# Run simulations
./run-tests.sh homepage
./run-tests.sh search
./run-tests.sh stress
./run-tests.sh all
```

### Method 3: Direct Maven Commands
```cmd
# Homepage simulation only
mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleHomepageSimulation

# Search simulation only
mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleSearchSimulation

# Stress simulation only
mvn clean gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleStressSimulation

# All simulations
mvn clean gatling:test
```

## 📊 Test Simulations Details

### Homepage Simulation
- **Purpose**: Validate homepage accessibility and baseline performance
- **Users**: 10 ramp-up + 2 sustained
- **Duration**: ~40 seconds
- **Assertions**:
  - Max response time < 10 seconds
  - Success rate > 95%
- **Expectations**: Should see < 2-5 second response times

### Search Simulation
- **Purpose**: Test realistic user search workflow
- **Users**: 15 ramp-up + 3 sustained  
- **Workflow**: Homepage → Search → Results → Next Page
- **Duration**: ~55 seconds
- **Assertions**:
  - 95th percentile < 5 seconds
  - Success rate > 90%
- **Expectations**: Search results in 2-4 seconds with pagination

### Stress Simulation
- **Purpose**: Test system resilience under spike load
- **Users**: 30 immediate + 20 ramping over 10 seconds
- **Load Pattern**: Sudden spike followed by sustained load
- **Duration**: 3 minutes max
- **Assertions**:
  - Max response time < 15 seconds
  - Success rate > 85%
- **Note**: May see 429 (Too Many Requests) responses - this is captured as "acceptable"

## 📈 Understanding Reports

### View Results
After test completes, open: `target/gatling/[timestamp]/index.html`

### Key Metrics

| Metric | Importance | Good | Excellent |
|--------|-----------|------|-----------|
| Response Time (Mean) | High | < 2 sec | < 1 sec |
| Response Time (P95) | Very High | < 5 sec | < 2 sec |
| Response Time (Max) | High | < 10 sec | < 5 sec |
| Throughput (req/s) | Medium | 50+ | 100+ |
| Success Rate | Critical | 99%+ | 99.9%+ |
| Error Rate | Critical | < 1% | < 0.1% |

### Report Sections
1. **Global Stats**: Overall test metrics
2. **Response Time Distribution**: Histogram of response times
3. **Requests per Second**: Throughput over time
4. **Users Count**: Active user load over time
5. **Response Time Percentiles**: P50, P75, P95, P99
6. **Errors**: Failed requests and error codes

## 🔧 Customization

### Modify Test Load
Edit simulation file, e.g., `GoogleHomepageSimulation.scala`:

```scala
// Change from: rampUsers(10).during(10.seconds)
// To: rampUsers(50).during(30.seconds)  // 50 users over 30 seconds
```

### Add New Test URL
```scala
exec(
  http("Search for Testing")
    .get("/search?q=performance%20testing")
    .check(status.is(200))
    .check(responseTime.lessThan(3000))
)
```

### Change Assertions
```scala
.assertions(
  global.responseTime.mean.lt(1000),        // Mean < 1 second
  forAll.failedRequests.count.lt(5),        // Less than 5 failed requests
  global.successfulRequests.percent.gt(99)  // 99% success
)
```

### Add User Think Time
```scala
.pause(2, 5)  // Random pause 2-5 seconds between requests
```

## 🐛 Troubleshooting

### Maven Command Not Found
```cmd
# Add Maven to PATH (Windows)
setx PATH "%PATH%;C:\apache-maven-3.9.X\bin"

# Restart terminal and verify
mvn --version
```

### Java Not Found
```cmd
# Install Java 11+ from oracle.com
# Add to PATH (Windows)
setx PATH "%PATH%;C:\Program Files\Java\jdk-11\bin"

# Restart and verify
java -version
```

### Connection Timeout
- Check internet connection
- Verify google.com is accessible: `ping google.com`
- Increase timeout in `src/test/resources/gatling.conf` if needed

### Cannot Access Port 3000
- Gatling uses port 3000 for results
- Kill process: `netstat -ano | findstr :3000`
- Or change port in configuration

### Build Fails with Scala Errors
```cmd
# Clear Maven cache and rebuild
mvn clean install -o
# Or
mvn -U clean compile
```

### OutOfMemory Error
```cmd
# Increase heap size
set MAVEN_OPTS=-Xmx2048m
mvn clean gatling:test
```

## 📝 Project Files Explained

| File | Purpose |
|------|---------|
| `pom.xml` | Maven build configuration with Gatling dependencies |
| `run-tests.bat` | Windows batch script for easy test execution |
| `run-tests.sh` | Unix shell script for macOS/Linux users |
| `GoogleHomepageSimulation.scala` | Baseline performance test - homepage access |
| `GoogleSearchSimulation.scala` | Realistic workflow test - search scenario |
| `GoogleStressSimulation.scala` | Stress/spike test - load resilience |
| `gatling.conf` | Gatling engine configuration and timeouts |
| `README.md` | Main project documentation |
| `QUICKSTART.md` | Getting started guide |
| `SETUP.md` | This file - installation and usage |

## 🔗 Useful Commands

```cmd
# Clean all build artifacts
mvn clean

# Just compile (no tests)
mvn compile

# List available tests
mvn help:describe -Dplugin=io.gatling.maven:gatling-maven-plugin

# Run with verbose output
mvn gatling:test -X

# Run with custom JVM options
set MAVEN_OPTS=-Xmx2048m -XX:+UseG1GC
mvn gatling:test

# Check dependencies
mvn dependency:tree

# Update all dependencies
mvn versions:display-dependency-updates

# Verify project (build, test, package checks)
mvn verify
```

## 📚 Resources

- **Gatling Docs**: https://gatling.io/docs/gatling/latest/
- **Maven Guide**: https://maven.apache.org/guides/
- **Scala for Gatling**: https://www.scala-lang.org/documentation/
- **HTTP Status Codes**: https://httpwg.org/specs/rfc7231.html

## ⚙️ Performance Tuning

### Increase Load safely
```scala
// Conservative ramp-up
rampUsers(100).during(60.seconds)

// Hold sustained load
constantUsersPerSec(5).during(300.seconds)

// Realistic spike test
atOnceUsers(200), rampUsers(100).during(30.seconds)
```

### Add Think Time (Realistic)
```scala
.pause(1, 3)  // 1-3 second random pause between requests
```

### Handle Failures Gracefully
```scala
.check(
  status.in(200, 304)  // Accept 200 or 304 (cached)
)
```

## 🚨 Important Notes

### ⚠️ Legal Disclaimer
- Only test systems you own or have explicit permission to test
- Respect robots.txt and server rate limits
- Do NOT use for malicious purposes
- Unauthorized load testing may violate laws and terms of service

### Best Practices
1. Start with low user counts (5-10)
2. Gradually increase to find breaking point
3. Monitor server health during tests
4. Analyze results thoroughly
5. Document baseline performance
6. Track improvements over time

### Ethics
- Inform stakeholders before testing
- Avoid testing during business hours
- Have rollback plans ready
- Monitor system logs during tests

## ✅ Verification Checklist

After setup, verify:
- [ ] Java installed and in PATH
- [ ] Maven installed and in PATH
- [ ] Project builds: `mvn clean compile`
- [ ] First test runs: `run-tests.bat homepage`
- [ ] Can view HTML report at target/gatling/
- [ ] All simulations execute

## 🎯 Next Steps

1. **Run baseline test**: `run-tests.bat homepage`
2. **Review results**: Open HTML report
3. **Document baseline**: Save initial performance metrics
4. **Create custom scenarios**: Edit simulation files for your needs
5. **Monitor trends**: Run tests regularly and compare results

## 📞 Support

For issues:
1. Check Gatling documentation: https://gatling.io/
2. Review troubleshooting section above
3. Check Maven configuration in pom.xml
4. Verify Java/Maven versions match requirements

---

**Project Version**: 1.0
**Last Updated**: April 2026
**Gatling Version**: 3.9.5
**Java Requirements**: 11+
**Maven Requirements**: 3.6.0+
