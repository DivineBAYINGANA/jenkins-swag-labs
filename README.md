# Swag Labs Test Automation with Jenkins CI/CD Pipeline

A comprehensive test automation project demonstrating a complete CI/CD pipeline setup using Jenkins, Docker, and automated testing for the Swag Labs application. This project includes both **UI automation tests** (Selenium/Selenide) and **API tests** (REST Assured), with automated reporting via Allure.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [Running Tests](#running-tests)
- [Jenkins Configuration](#jenkins-configuration)
- [Webhook Integration](#webhook-integration)
- [Notifications](#notifications)
- [CI/CD Flow](#cicd-flow)
- [Troubleshooting](#troubleshooting)

## Project Overview

This project helps QA engineers understand and practice setting up a Jenkins pipeline for automated test execution. It demonstrates:

- **Test Automation**: UI tests using Selenium/Selenide and API tests using REST Assured
- **Container Orchestration**: Docker and Docker Compose for test execution
- **CI/CD Pipeline**: Jenkins declarative pipeline for automated builds and testing
- **Reporting**: Allure Reports for comprehensive test results visualization
- **Webhooks**: GitHub integration for automatic job triggers on code push
- **Notifications**: Slack and email notifications for test results

## Features

✅ **UI Automation Tests** - Selenide-based page object model pattern
✅ **API Tests** - REST Assured for API endpoint testing
✅ **Docker Support** - Multi-stage Docker builds for optimized container images
✅ **Docker Compose** - Selenium Grid integration for distributed testing
✅ **Jenkins Pipeline** - Declarative pipeline with multiple stages
✅ **Allure Reports** - Beautiful test report visualization
✅ **Test Organization** - Smoke and regression test suites
✅ **CI/CD Integration** - Automated webhooks and notifications

## Prerequisites

### Local Development
- **Java 21 or higher**
- **Maven 3.9.6 or higher**
- **Docker & Docker Compose** (for containerized testing)
- **Git** (for version control)
- **Chrome/Chromium** browser (for Selenium tests)

### For Jenkins
- **Jenkins 2.450+ (LTS compatible)**
- **Docker & Docker Compose** on Jenkins server
- **Jenkins Plugins**:
  - Pipeline
  - Git
  - HTML Publisher
  - JUnit
  - Slack Notification
  - Build Name and Description Setter
  - Email Extension

### External Services
- **GitHub** account with webhook support
- **Slack Workspace** (optional, for notifications)

## Project Structure

```
jenkins-swag-labs/
├── src/
│   ├── main/
│   │   └── java/com/swaglabs/
│   │       ├── pages/           # Page Object Model (UI Tests)
│   │       │   ├── LoginPage.java
│   │       │   ├── ProductsPage.java
│   │       │   ├── CartPage.java
│   │       │   ├── CheckoutStepOnePage.java
│   │       │   ├── CheckoutOverviewPage.java
│   │       │   └── CheckoutCompletePage.java
│   │       ├── api/              # API Test Classes
│   │       │   └── SwaglabsApiClient.java
│   │       └── utils/
│   │           └── TestData.java
│   └── test/
│       ├── java/com/swaglabs/
│       │   ├── base/
│       │   │   └── BaseTest.java
│       │   ├── smoke/            # Smoke Tests
│       │   │   ├── LoginTest.java
│       │   │   └── ProductPageTest.java
│       │   ├── regression/       # Regression Tests
│       │   │   ├── CartTest.java
│       │   │   ├── CheckoutTest.java
│       │   │   └── ApiTest.java  # API Tests
│       │   └── resources/
│       │       └── junit-platform.properties
│       └── resources/
│           └── junit-platform.properties
├── target/                       # Build output (generated)
├── Dockerfile                    # Multi-stage Docker build
├── docker-compose.yml            # Selenium Grid setup
├── Jenkinsfile                   # Jenkins declarative pipeline
├── pom.xml                       # Maven configuration
└── README.md                     # This file
```

## Setup & Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/jenkins-swag-labs.git
cd jenkins-swag-labs
```

### 2. Build the Project

```bash
# Build and run tests locally with Maven
mvn clean install

# Run only smoke tests
mvn test -Dgroups=smoke

# Run only regression tests
mvn test -Dgroups=regression

# Run all tests
mvn test
```

### 3. Using Docker

#### Run Tests in Docker
```bash
# Build and run tests with Docker Compose (includes Selenium Grid)
docker compose up --build --abort-on-container-exit test-runner

# Run smoke tests only
SUITE=smoke docker compose up --build --abort-on-container-exit test-runner

# Run regression tests only
SUITE=regression docker compose up --build --abort-on-container-exit test-runner
```

#### View Allure Report
```bash
# Start the Allure report viewer (after tests complete)
docker compose up allure-report

# Open http://localhost:8080 in your browser
```

## Running Tests

### Local Execution

```bash
# All tests
mvn clean test

# Smoke tests only (fast, critical path)
mvn clean test -Dgroups=smoke

# Regression tests only (comprehensive)
mvn clean test -Dgroups=regression

# Specific test class
mvn clean test -Dtest=LoginTest

# With custom browser options
mvn clean test -Dbrowser=firefox -Dheadless=false
```

### Test Reports

After test execution, view the Allure report:

```bash
# Generate and open Allure report
mvn allure:report
mvn allure:serve
```

The report will open in your browser at `http://localhost:4050`

## Jenkins Configuration

### 1. Install Jenkins

#### Option A: Docker Desktop
```bash
docker run -d \
  --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

#### Option B: Traditional Installation
See [Jenkins Installation Guide](https://www.jenkins.io/doc/book/installing/)

### 2. Initial Setup

1. Navigate to `http://localhost:8080`
2. Retrieve the initial admin password:
   ```bash
   docker logs jenkins | grep "Initial Admin password"
   ```
3. Complete the setup wizard
4. Install suggested plugins

### 3. Install Required Plugins

1. Manage Jenkins → Plugin Manager → Available
2. Search and install:
   - **Pipeline** (Declarative Pipeline)
   - **Git**
   - **HTML Publisher**
   - **JUnit**
   - **Slack Notification** (optional)
   - **Email Extension Plugin** (optional)
   - **Build Name Setter**

### 4. Create a New Pipeline Job

1. **New Item** → Name: `swag-labs-tests` → **Pipeline** → OK
2. **Configuration**:
   - **Description**: "Automated Test Suite for Swag Labs Application"
   - **Build Triggers**: 
     - ✓ GitHub hook trigger for GITscm polling
   - **Pipeline**:
     - Definition: **Pipeline script from SCM**
     - SCM: **Git**
       - Repository URL: `https://github.com/yourusername/jenkins-swag-labs.git`
       - Branch Specifier: `*/main`
     - Script Path: `Jenkinsfile`
   - **Save**

### 5. Configure GitHub Repository

In your GitHub repository, register Jenkins:

1. **Settings** → **Webhooks** → **Add webhook**
2. **Payload URL**: `http://your-jenkins-server:8080/github-webhook/`
3. **Content type**: `application/json`
4. **Events**: Just the push event
5. **Active**: ✓
6. **Add webhook**

### 6. Configure Docker Access (if using Docker)

Ensure Jenkins can access Docker:

```bash
# Add Jenkins user to docker group
docker exec jenkins usermod -aG docker jenkins

# Restart Jenkins
docker restart jenkins
```

## Webhook Integration

### GitHub Webhook Setup

1. **Repository Settings** → **Webhooks** → **Add webhook**
2. Configure:
   - **Payload URL**: `http://<JENKINS_URL>:8080/github-webhook/`
   - **Content type**: `application/json`
   - **Which events would you like to trigger this webhook?**
     - Select "Just the push event"
   - **Active**: Checked
3. **Add webhook**

### Testing the Webhook

```bash
# Make a test commit and push
git add .
git commit -m "Test webhook"
git push origin main

# Jenkins job should trigger automatically
```

## Notifications

### Slack Integration

#### 1. Create a Slack App

1. Go to [Slack API](https://api.slack.com)
2. Create a new app → Select "From scratch"
3. Name: "Jenkins"
4. Select your workspace
5. **Features** → **Incoming Webhooks** → **Add New Webhook to Workspace**
6. Select a channel (e.g., #jenkins)
7. Copy the Webhook URL

#### 2. Configure Jenkins Slack Plugin

1. **Manage Jenkins** → **Configure System**
2. Scroll to **Slack**:
   - **Workspace**: Your Slack workspace URL (e.g., `myworkspace.slack.com`)
   - **Credential**: Click **Add** → **Jenkins**
     - Kind: Secret text
     - Secret: Paste your Slack bot token
     - ID: `slack-token`
   - **Default channel**: `#jenkins` (or your channel)
   - Click **Test Connection**
3. **Save**

#### 3. Update Jenkinsfile for Slack Notifications

The provided Jenkinsfile includes Slack notification configuration. Update:

```groovy
stage('Notify Slack') {
    steps {
        script {
            slackSend(
                channel: '#jenkins',
                color: currentBuild.result == 'SUCCESS' ? 'good' : 'danger',
                message: """
                    Test Results: ${currentBuild.result}
                    Build: <${env.BUILD_URL}|${env.JOB_NAME} #${env.BUILD_NUMBER}>
                    Tests: Passed: ${testPassed}, Failed: ${testFailed}
                """
            )
        }
    }
}
```

### Email Notifications

1. **Manage Jenkins** → **Configure System**
2. Scroll to **E-mail Notification**:
   - **SMTP server**: Your mail server (e.g., `smtp.gmail.com`)
   - **SMTP Authentication**: Enable and provide credentials
   - **TLS**: Enable
   - **Port**: 587
3. **Save**

## CI/CD Flow

```
┌─────────────────────────────────────────────────────────────┐
│  Developer pushes code to GitHub                            │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│  GitHub Webhook triggers Jenkins Job                        │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│  Jenkins Pipeline Stages:                                   │
│  1. Checkout - Clone repository                             │
│  2. Build - Compile with Maven                              │
│  3. Test - Execute test suite                               │
│  4. Report - Generate Allure report                         │
│  5. Archive - Store artifacts & reports                     │
│  6. Notify - Send Slack/Email notifications                 │
└────────────────┬────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────────────┐
│  Test Results Available in:                                 │
│  - Jenkins Build Page                                       │
│  - Allure Report                                            │
│  - Slack Channel                                            │
│  - Email Notification                                       │
└─────────────────────────────────────────────────────────────┘
```

## Environment Variables

Configure these in Jenkins Job Configuration:

| Variable | Default | Description |
|----------|---------|-------------|
| `BROWSER` | chrome | Browser for UI tests (chrome, firefox) |
| `HEADLESS` | true | Run browser in headless mode |
| `BASE_URL` | https://www.saucedemo.com | Application URL |
| `API_BASE_URL` | https://api.example.com | API endpoint base URL |
| `SELENIUM_HUB_URL` | http://localhost:4444 | Selenium Grid hub URL (Docker) |

## Troubleshooting

### Jenkins Job Not Triggering on Push

**Problem**: Webhook configured, but job doesn't trigger on push

**Solution**:
1. Verify webhook URL is correct and accessible
2. Check Jenkins logs: `Manage Jenkins` → `System Log`
3. In GitHub, go to webhook settings and check "Recent Deliveries"
4. Ensure "GitHub hook trigger for GITscm polling" is enabled in job configuration

### Docker: Permission Denied

**Problem**: `docker: permission denied while trying to connect to Docker daemon`

**Solution**:
```bash
# Add Jenkins user to docker group
docker exec jenkins usermod -aG docker jenkins
docker restart jenkins
```

### Tests Not Running in Docker

**Problem**: Tests pass locally but fail in Docker container

**Solution**:
1. Check Docker logs: `docker logs <container_id>`
2. Ensure dependency cache is fresh: `docker compose build --no-cache`
3. Verify Selenium Grid connectivity: Check docker-compose.yml
4. Set headless=false to see browser behavior (local debugging only)

### Allure Report Not Generated

**Problem**: Allure report not appearing in Jenkins

**Solution**:
1. Verify `target/allure-results` directory is populated
2. Check Maven build output for Allure plugin errors
3. In Jenkins, verify HTML Publisher plugin is installed
4. Check Jenkinsfile "Publish Reports" stage

### Slack Notifications Not Working

**Problem**: Jenkins isn't sending Slack messages

**Solution**:
1. Verify Slack webhook URL is correct
2. Test connection in Jenkins: `Manage Jenkins` → `Configure System` → **Test Slack Connection**
3. Check Jenkins logs for Slack plugin errors
4. Ensure bot has permissions in Slack channel

## Best Practices

1. **Test Organization**:
   - Use `@Tag("smoke")` for critical path tests
   - Use `@Tag("regression")` for comprehensive tests
   - Keep tests independent and idempotent

2. **CI/CD Pipeline**:
   - Run smoke tests first (fail fast principle)
   - Generate reports after every test run
   - Archive artifacts for historical tracking

3. **Docker Usage**:
   - Build images once, reuse across environments
   - Use Docker Compose for multi-service testing
   - Always clean up containers: `docker compose down`

4. **Security**:
   - Store credentials in Jenkins Credentials Store
   - Use environment-specific configuration
   - Never commit sensitive data to repository

## Resources

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Selenide Documentation](https://selenide.org/)
- [REST Assured Documentation](https://rest-assured.io/)
- [Allure Framework](https://docs.qameta.io/allure/)
- [Docker Documentation](https://docs.docker.com/)
- [GitHub Webhooks](https://docs.github.com/en/developers/webhooks-and-events/webhooks)

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add your feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Open a Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, questions, or contributions, please open an issue on the GitHub repository.

---

**Last Updated**: 2024
**Version**: 1.0.0
