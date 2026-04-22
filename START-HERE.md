# 🚀 GATLING PERFORMANCE TEST PROJECT - QUICK REFERENCE

## ✅ Project Ready!

Location: **c:\Users\tutru\gatling-test**

## 📦 What's Included

3 Complete Performance Test Simulations:
1. **GoogleHomepageSimulation** - Tests Google homepage (10-12 concurrent users)
2. **GoogleSearchSimulation** - Tests search workflow with pagination (15 users)  
3. **GoogleStressSimulation** - Spike/stress test (30-50 concurrent users)

## 🎯 5-Minute Quick Start

### Step 1: Open Command Prompt
```cmd
cd c:\Users\tutru\gatling-test
```

### Step 2: Run a Test
```cmd
run-tests.bat homepage
```

### Step 3: Wait for Results
Test takes ~40-60 seconds. Watch for "BUILD SUCCESS" message.

### Step 4: View Report
Open: `target/gatling/[latest folder]/index.html`

## 💻 Available Commands

```cmd
run-tests.bat homepage    # Homepage load test
run-tests.bat search      # Search functionality test
run-tests.bat stress      # Stress/spike test
run-tests.bat all         # Run all 3 simulations
```

## 📊 What You'll See

When you open the HTML report, you'll see:
- **Response Times**: How fast Google responds (should be < 5 seconds)
- **Throughput**: Requests per second
- **Active Users**: Current load during test
- **Success Rate**: Percentage of successful requests (should be > 95%)
- **Charts**: Visual graphs of all metrics

## 🔧 Customization Examples

### Make Test Harder (More Users)
Edit `src/test/java/com/example/simulations/GoogleHomepageSimulation.java`

Change line:
```java
rampUsers(10).during(Duration.ofSeconds(10))
```

To:
```scala
rampUsers(100).during(30.seconds)  // 100 users over 30 seconds
```

Then run: `run-tests.bat homepage`

### Test a Different URL
In the same file, change line:
```scala
.get("/")
```

To:
```scala
.get("/search?q=gatling")
```

## 📁 Project Structure

```
gatling-test/
├── run-tests.bat                    ← Click to run tests (Windows)
├── pom.xml                          ← Maven config (don't modify)
├── QUICKSTART.md                    ← Getting started guide
├── SETUP.md                         ← Installation & detailed guide
├── README.md                        ← Full documentation
│
└── src/test/java/com/example/simulations/
    ├── GoogleHomepageSimulation.java       ← Homepage test
    ├── GoogleSearchSimulation.java         ← Search test
    ├── GoogleStressSimulation.java         ← Stress test
    └── ReqResApiSimulation.java            ← API test
```

## 📋 Checklist

Before running tests, verify:
- [ ] Java installed: `java -version` (should be 11+)
- [ ] Maven installed: `mvn --version` (should be 3.6+)
- [ ] Internet connected (to reach google.com)
- [ ] In correct directory: `c:\Users\tutru\gatling-test`

## 🎓 Key Concepts

**What is Gatling?**
- Load testing framework that simulates multiple users
- Allows you to measure website performance under load
- Generates beautiful HTML reports with metrics

**What are Simulations?**
- Scala scripts that define test scenarios
- Each simulation defines user behavior and load patterns
- Can test different workflows (browsing, searching, etc.)

**What is a User?**
- Virtual user that simulates a real person
- Can perform multiple actions (click links, submit forms)
- Multiple users run concurrently

**What is Ramp-up?**
- Gradually increasing number of users
- More realistic than sudden spike
- Example: 10 users over 10 seconds = 1 new user/second

## 📈 Sample Results Expected

For **GoogleHomepageSimulation** on decent internet:
- Response Time: 1-3 seconds (mean)
- Success Rate: 98-100%
- Throughput: 20-50 requests/second
- Active Users: 10-12 peak

## 🆘 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| "mvn not found" | Install Maven, add to PATH |
| "java not found" | Install Java 11+, add to PATH |
| "Connection refused" | Check internet, verify google.com accessible |
| "BUILD FAILED" | Check internet, review error message |
| "Port already in use" | Kill process on port 3000 or wait 60s |
| Report files empty | Wait for all requests to complete before opening |

## 🔗 Important Files

| File | Purpose |
|------|---------|
| `run-tests.bat` | **Click this to run tests!** |
| `pom.xml` | Maven configuration - don't edit |
| `SETUP.md` | Full installation guide |
| `QUICKSTART.md` | Getting started guide |
| Simulation .java files | The actual test scripts |

## 📚 Documentation Files (Read These)

1. **START HERE**: QUICKSTART.md - Getting started in 5 minutes
2. **SETUP.md** - Comprehensive setup and reference
3. **README.md** - Full project documentation

## 🎯 Next Steps

1. ✅ Run: `run-tests.bat homepage`
2. 📊 Review the HTML report
3. 🔄 Try other tests: `run-tests.bat search`
4. 🛠️ Customize a simulation for your needs
5. 🚀 Run regularly to track performance

## 💡 Pro Tips

- Start with small user counts (5-10) before scaling up
- Run tests multiple times to get consistent results
- Compare results over time to track trends
- Check system resources while test is running
- Save baseline metrics to compare improvements

## ⚠️ Important

This project is for **testing systems you own or have permission to test**. 
Unauthorized load testing may violate laws and terms of service.

---

## 🎉 Ready to Test?

```cmd
cd c:\Users\tutru\gatling-test
run-tests.bat homepage
```

That's it! Your first performance test will start in a few seconds.

**Questions?** Check SETUP.md or QUICKSTART.md for detailed guides.
