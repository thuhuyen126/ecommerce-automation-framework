# E-Commerce Automation Framework

![Java](https://img.shields.io/badge/Java-25-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-green)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red)
![Maven](https://img.shields.io/badge/Maven-3.9-blue)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

> End-to-end UI Automation Testing Framework built with Java, Selenium WebDriver, TestNG, and Maven.

---

# Overview

This project is a scalable automation testing framework designed for the SauceDemo e-commerce website.

The framework simulates a real-world QA Automation environment and covers core business workflows including:

* Authentication
* Product browsing and sorting
* Shopping cart management
* Checkout process
* Validation and error handling

The framework is built using the **Page Object Model (POM)** design pattern with reusable utilities, reporting, retry mechanisms, and CI/CD integration.

---

# Website Under Test

**SauceDemo**
https://www.saucedemo.com

### Test Accounts

| Username                | Password     | Description           |
| ----------------------- | ------------ | --------------------- |
| standard_user           | secret_sauce | Standard valid user   |
| locked_out_user         | secret_sauce | Locked user           |
| problem_user            | secret_sauce | User with UI issues   |
| performance_glitch_user | secret_sauce | Slow performance user |

---

# Test Coverage

| Module                             | Automated Test Cases |
| ---------------------------------- | -------------------- |
| Login / Logout                     | 7                    |
| Products / Sorting                 | 6                    |
| Shopping Cart                      | 6                    |
| Checkout                           | 7                    |
| Data-driven & Validation Scenarios | 7                    |
| **Total**                          | **33**               |

---

# Tech Stack

| Technology         | Version | Purpose                             |
| ------------------ | ------- | ----------------------------------- |
| Java               | 25 LTS  | Main programming language           |
| Selenium WebDriver | 4.18.1  | Browser automation                  |
| TestNG             | 7.9.0   | Test framework                      |
| Maven              | 3.9+    | Build and dependency management     |
| WebDriverManager   | 5.7.0   | Automatic browser driver management |
| Extent Reports     | 5.1.1   | HTML reporting                      |
| DataFaker          | 2.1.0   | Random test data generation         |
| Jackson            | 2.16.1  | JSON data parsing                   |
| Apache POI         | 5.2.5   | Excel data handling                 |
| Log4j2             | 2.22.1  | Logging                             |

---

# Framework Architecture

This framework follows the **Page Object Model (POM)** architecture.

## Architecture Layers

### Test Layer

Contains business test scenarios using:

* TestNG
* Assertions
* Data Providers

### Page Layer

Encapsulates:

* Web element locators
* UI interactions
* Page-specific actions

### Utilities Layer

Reusable helper classes for:

* Explicit waits
* Screenshot capture
* Retry mechanisms
* Faker test data
* JSON readers

### Configuration Layer

Handles:

* Environment configuration
* Browser setup
* Test execution settings

### Reporting Layer

Provides:

* Extent HTML Reports
* Screenshot attachments
* Execution logs

---

# Project Structure

```text
ecommerce-automation/
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   └── BaseTest.java
│   │   │
│   │   ├── config/
│   │   │   └── ConfigReader.java
│   │   │
│   │   ├── pages/
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductsPage.java
│   │   │   ├── CartPage.java
│   │   │   └── CheckoutPage.java
│   │   │
│   │   └── utils/
│   │       ├── ScreenshotUtil.java
│   │       ├── ExtentReportListener.java
│   │       ├── RetryAnalyzer.java
│   │       ├── RetryListener.java
│   │       ├── JsonDataReader.java
│   │       ├── FakerUtil.java
│   │       └── WaitUtil.java
│   │
│   └── test/java/
│       └── tests/
│           ├── LoginTest.java
│           ├── SearchTest.java
│           ├── CartTest.java
│           └── CheckoutTest.java
│
├── src/test/resources/
│   ├── config.properties
│   ├── testng.xml
│   ├── log4j2.xml
│   └── testdata/
│       ├── login_data.json
│       └── checkout_data.json
│
├── .github/workflows/
│   └── selenium-tests.yml
│
├── reports/
├── screenshots/
├── pom.xml
└── README.md
```

---

# Features

| Feature                | Description                         |
| ---------------------- | ----------------------------------- |
| Page Object Model      | Separates locators and test logic   |
| ThreadLocal Driver     | Supports parallel execution safely  |
| Cross-browser Testing  | Chrome, Edge, Firefox               |
| Data-driven Testing    | JSON + TestNG DataProvider          |
| Retry Analyzer         | Automatically retries flaky tests   |
| Screenshot on Failure  | Captures screenshots automatically  |
| Extent Reports         | Detailed HTML reporting             |
| Faker Integration      | Dynamic test data generation        |
| WebDriverManager       | No manual driver setup              |
| Log4j2 Logging         | Console and file logging            |
| CI/CD Integration      | GitHub Actions pipeline             |
| Explicit Wait Strategy | Stable and reliable synchronization |

---

# Installation

## Prerequisites

* Java JDK 25+
* Maven 3.9+
* Chrome / Edge / Firefox

---

# Clone Repository

```bash
git clone https://github.com/your-username/ecommerce-automation.git
cd ecommerce-automation
```

---

# Install Dependencies

```bash
mvn clean install
```

---

# Run Tests

## Run Full Test Suite

```bash
mvn test
```

## Run with Specific Browser

```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=edge
mvn test -Dbrowser=firefox
```

## Run in Headless Mode

```bash
mvn test -Dheadless=true
```

## Run Specific Test Class

```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=CartTest
mvn test -Dtest=CheckoutTest
```

## Run with Custom Environment

```bash
mvn test -Denv=qa
mvn test -Denv=uat
```

## Combined Execution Example

```bash
mvn test -Dbrowser=chrome -Dheadless=true -Denv=qa
```

---

# Test Reports

After test execution, open:

```text
reports/extent-report.html
```

The report includes:

* Pass/Fail summary
* Detailed test steps
* Execution screenshots
* Browser and environment information
* Execution time and logs

---

# CI/CD with GitHub Actions

The automation pipeline runs automatically when:

* Code is pushed to `main` or `develop`
* Pull Requests are created
* Scheduled execution is triggered

Pipeline features:

* Automated test execution
* Maven build validation
* Artifact upload
* HTML report generation

---

# Why This Project

This project was built to simulate a real-world automation framework used in enterprise QA teams.

Main objectives:

* Practice scalable automation framework design
* Apply Page Object Model architecture
* Implement reusable automation utilities
* Support cross-browser execution
* Integrate CI/CD workflows
* Improve maintainability and readability

---

# Future Improvements

* [ ] Allure Reports integration
* [ ] REST API testing with RestAssured
* [ ] Performance testing with JMeter
* [ ] Docker + Selenium Grid
* [ ] Cucumber BDD implementation
* [ ] Email notifications for failed tests
* [ ] Database validation
* [ ] Jenkins pipeline integration

---

# Screenshots

## Extent Report

(Add screenshot here)

## GitHub Actions Pipeline

(Add screenshot here)

## Test Execution

(Add screenshot here)

---

# Author

**Vu Thi Thu Huyen**
Fresher QA Automation Engineer
Ho Chi Minh City, Vietnam

GitHub: 
LinkedIn: 
