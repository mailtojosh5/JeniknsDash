# Gatling Performance Test Project - Google.com

This is a performance testing project using **Gatling** to load test google.com.

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

### Build the project
```bash
cd gatling-test
mvn clean gatling:test
```

### Run specific simulation
```bash
mvn gatling:test -Dgatling.simulationClass=com.example.simulations.GoogleHomepageSimulation
```

## Project Structure

```
gatling-test/
├── pom.xml                                       # Maven configuration
├── src/test/
│   ├── java/com/example/simulations/
│   │   ├── GoogleHomepageSimulation.java        # Homepage load test
│   │   ├── GoogleSearchSimulation.java          # Search functionality test
│   │   ├── GoogleStressSimulation.java          # Stress/spike test
│   │   └── ReqResApiSimulation.java             # API load test
│   └── resources/
│       └── gatling.conf                         # Gatling settings
└── README.md
```

## Simulations

### 1. Homepage Simulation
- Tests Google homepage accessibility
- 10 users ramping up over 10 seconds
- 2 constant users/second for 30 seconds
- Validates response time < 5 seconds

### 2. Search Simulation  
- Simulates search workflow (homepage → search → results → next page)
- 15 users ramping up with pauses between actions
- 3 constant users/second
- Validates 95th percentile response time < 5 seconds

### 3. Stress Simulation
- Tests system under heavy load
- Immediate spike of 30 users + 20 over 10 seconds
- Repeats requests 5 times per user
- Allows 429 (Too Many Requests) responses
- Max duration: 3 minutes

## Results

After running tests, view HTML reports in:
```
target/gatling/
```

Reports include:
- Response time analysis
- Request/response statistics
- Error tracking
- User load progression

## Configuration

Edit `src/test/resources/gatling.conf` to customize:
- Timeouts and connection settings
- Report generation options
- Charting preferences

## Customization

### Add new simulation
1. Create file in `src/test/java/com/example/simulations/New*Simulation.java`
2. Add `package com.example.simulations;` at the top
3. Extend from `Simulation` class
4. Run with `mvn gatling:test`

### Modify load profiles
Open simulation files and adjust:
- `rampUsers(N)` - number of users
- `.during(Xs)` - duration
- `constantUsersPerSec(N)` - sustained load
- `atOnceUsers(N)` - immediate load spike

## Troubleshooting

**Build fails**: Ensure Java 11+ and Maven 3.6+ are installed
**Connection refused**: Check internet connection to google.com
**Timeout errors**: Increase timeouts in `gatling.conf`

**Windows Jansi warning**: If you see `library.jansi.path` and a path with spaces, your Maven install is likely under `C:\Program Files`. Install Maven in a no-space path such as `C:\maven`, or use the short path equivalent.

## Important Notes

⚠️ **Legal Disclaimer**: This project is for educational and authorized testing only. Always ensure you have permission before conducting load tests on any external website.

## References

- [Gatling Documentation](https://gatling.io/docs/gatling/latest/)
- [Maven Documentation](https://maven.apache.org/)
