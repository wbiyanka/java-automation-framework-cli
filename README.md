# Web Automation Testing Framework

A **beginner-friendly** Selenium WebDriver automation framework built with Java, Maven, and JUnit 5. This framework tests the [SauceDemo](https://www.saucedemo.com/) demo e-commerce website using the **Page Object Model (POM)** design pattern.

---

## 📚 Table of Contents

1. [What is this project?](#-what-is-this-project)
2. [Prerequisites (what you need to install)](#-prerequisites-what-you-need-to-install)
3. [Project Structure (folder layout)](#-project-structure-folder-layout)
4. [Key Concepts (explained simply)](#-key-concepts-explained-simply)
5. [How to Set Up the Project](#-how-to-set-up-the-project)
6. [How to Run the Tests](#-how-to-run-the-tests)
7. [Test Scenarios (what each test does)](#-test-scenarios-what-each-test-does)
8. [Configuration Guide](#-configuration-guide)
9. [Troubleshooting Common Issues](#-troubleshooting-common-issues)
10. [Extending the Framework](#-extending-the-framework)

---

## 🎯 What is this project?

This is a **web test automation framework** that automatically tests a website using code instead of a human clicking around. Think of it as a robot that can:

- Open a browser (Chrome or Firefox)
- Type usernames and passwords
- Click buttons and links
- Check if things appear correctly
- Report if something is wrong

The website we test is **SauceDemo** - a fake online store specifically designed for practicing test automation. It has login, products, a shopping cart, and a checkout flow.

### Technologies Used

| Technology | What it does |
|---|---|
| **Java 11** | The programming language used to write the tests |
| **Maven** | Build tool that downloads libraries and runs tests |
| **Selenium WebDriver 4** | The library that controls the browser programmatically |
| **WebDriverManager** | Automatically downloads the correct browser driver (no manual setup!) |
| **JUnit 5** | The testing framework that runs test methods and reports results |
| **Page Object Model (POM)** | A design pattern that keeps tests clean and maintainable |

---

## 🔧 Prerequisites (what you need to install)

Before you can run the tests, you need these installed on your computer:

### 1. Java Development Kit (JDK) 11 or higher

**What is it?** Java is the language the tests are written in. The JDK includes tools to compile and run Java code.

**How to check if you have it:**
```bash
java -version
javac -version
```

Both should show version 11 or higher.

**Where to download:** [Adoptium Temurin JDK](https://adoptium.net/) (choose version 11 or 17, LTS).

### 2. Apache Maven

**What is it?** Maven is a build tool that downloads all the libraries (dependencies) our project needs and runs the tests.

**How to check if you have it:**
```bash
mvn --version
```

**Where to download:** [Maven Download Page](https://maven.apache.org/download.cgi) - follow the [installation guide](https://maven.apache.org/install.html).

### 3. A Web Browser (Chrome or Firefox)

The tests run in a real browser. You need Chrome or Firefox installed.

- **Chrome** - https://www.google.com/chrome/
- **Firefox** - https://www.mozilla.org/firefox/

> **No need to download chromedriver or geckodriver!** WebDriverManager handles that automatically.

### 4. A Code Editor (recommended but not required)

To view and edit the code, we recommend:
- **IntelliJ IDEA Community Edition** (free) - https://www.jetbrains.com/idea/download/
- **VS Code** (free) - https://code.visualstudio.com/

---

## 📁 Project Structure (folder layout)

Here's how the project files are organized. Understanding this structure is key to working with the framework.

```
java-automation-framework-cli/
│
├── pom.xml                              # Maven configuration file (libraries, build settings)
├── README.md                            # This file - documentation
├── .gitignore                           # Tells Git which files to ignore
├── LICENSE                              # Apache License 2.0
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── demo/
│                   └── automation/
│                       │
│                       ├── base/
│                       │   └── BasePage.java        # ★ Parent of ALL page objects
│                       │
│                       ├── driver/
│                       │   └── DriverManager.java   # ★ Creates and manages the browser
│                       │
│                       ├── pages/
│                       │   ├── LoginPage.java       # Login page actions
│                       │   ├── InventoryPage.java   # Products page actions
│                       │   ├── CartPage.java        # Shopping cart actions
│                       │   └── CheckoutPage.java    # Checkout flow actions
│                       │
│                       └── utils/
│                           ├── ConfigReader.java    # Reads config.properties file
│                           └── WaitHelper.java      # Wait utilities for reliable tests
│
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── demo/
        │           └── automation/
        │               │
        │               ├── base/
        │               │   └── BaseTest.java        # ★ Parent of ALL test classes
        │               │
        │               └── tests/
        │                   ├── LoginTest.java        # Tests for login feature
        │                   ├── InventoryTest.java    # Tests for products page
        │                   ├── CartTest.java         # Tests for shopping cart
        │                   └── CheckoutTest.java     # Tests for checkout flow
        │
        └── resources/
            └── config.properties                    # ★ Configuration settings (URL, browser, etc.)
```

### Legend

| Icon | Meaning |
|---|---|
| ★ | **Start here** - these are the most important files to understand |
| `.java` | Java source code files |
| `.xml` | Configuration files (Maven) |
| `.properties` | Key-value configuration file |
| `src/main/` | Framework code (reusable components) |
| `src/test/` | Test code (the actual tests) |

---

## 🧠 Key Concepts (explained simply)

### What is the Page Object Model (POM)?

Imagine you're testing a website manually. You have a checklist:

1. Go to the login page
2. Type username "standard_user"
3. Type password "secret_sauce"
4. Click the login button
5. Verify the products page appears

The **Page Object Model** turns each web page into a Java class. Instead of repeating code like:
```java
driver.findElement(By.id("user-name")).sendKeys("standard_user");
```

You write:
```java
loginPage.enterUsername("standard_user");
```

This is cleaner, easier to read, and if the website changes (e.g., the login button ID changes), you only update ONE file instead of all your tests.

### What are Explicit Waits?

Websites don't load instantly. Images, buttons, and text appear at different times. If Selenium tries to click a button before it's visible, the test fails.

**Explicit Waits** tell Selenium: "Wait for this specific element to be ready before interacting with it." This makes tests more reliable.

**Bad approach** (don't do this):
```java
Thread.sleep(3000);  // Always waits 3 seconds - wasteful and flaky!
```

**Good approach** (what we use):
```java
// Waits up to 10 seconds, but continues as soon as the button is ready
wait.until(ExpectedConditions.elementToBeClickable(loginButton));
```

### What is Inheritance?

Inheritance means a class can "borrow" features from another class.

```
BasePage (has: click, sendKeys, getText, isDisplayed)
    ↑
    │  (inherits all of the above)
    │
LoginPage (only writes: login-specific methods)
```

This avoids duplicating common code. Our page classes only contain page-specific code because BasePage provides the shared functionality.

### What is the Test Lifecycle?

JUnit runs tests in a specific order:

```
  setUp()       ← @BeforeEach: Opens browser, goes to URL
     ↓
  testMethod()  ← @Test: Your actual test logic
     ↓
  tearDown()    ← @AfterEach: Closes browser, cleans up
```

Each test gets a **fresh browser** so tests don't interfere with each other.

---

## 🚀 How to Set Up the Project

### Step 1: Open a Terminal/Command Prompt

- **Windows**: Press `Win + R`, type `cmd`, press Enter (or use PowerShell)
- **macOS**: Press `Cmd + Space`, type `Terminal`, press Enter
- **Linux**: Press `Ctrl + Alt + T`

### Step 2: Navigate to the Project Folder

```bash
cd C:\Users\HP\Repos\java-automation-framework-cli
```

### Step 3: Verify Maven and Java are Installed

```bash
mvn --version
java -version
```

Both should show version numbers. If not, see the [Prerequisites](#-prerequisites-what-you-need-to-install) section.

### Step 4: Download Dependencies (First Time Only)

```bash
mvn dependency:resolve
```

This downloads Selenium, JUnit, and WebDriverManager libraries. You'll see a lot of output - that's normal! The files get stored in your local Maven repository (`~/.m2/repository/`).

### Step 5: Compile the Project

```bash
mvn compile
```

You should see `BUILD SUCCESS`. This means the framework code compiles without errors.

---

## 🏃 How to Run the Tests

### Run ALL Tests

```bash
mvn test
```

This will:
1. Compile the framework code and test code
2. Open a Chrome browser
3. Run all test methods one by one
4. Close the browser after each test
5. Show a summary of passed/failed tests

### Run Tests from a Specific Class

```bash
mvn test -Dtest=LoginTest
```

Replace `LoginTest` with any test class name:
- `InventoryTest`
- `CartTest`
- `CheckoutTest`

### Run a Single Test Method

```bash
mvn test -Dtest=LoginTest#testValidLogin
```

Use the class name, then `#`, then the method name.

### Run Tests with Firefox

1. Open `src/test/resources/config.properties`
2. Change `browser = chrome` to `browser = firefox`
3. Run `mvn test`

### Watch Tests Run in Real Time

When you run `mvn test`, watch your browser! You'll see:
1. A browser window opens automatically
2. The SauceDemo website loads
3. Text gets typed, buttons get clicked
4. The browser closes
5. Another test starts with a fresh browser

---

## 📋 Test Scenarios (what each test does)

### LoginTest (3 tests)

| Test | What it checks |
|---|---|
| `testValidLogin` | A standard user can log in with correct username and password |
| `testInvalidLogin` | Wrong credentials show an error message and don't log in |
| `testLockedOutUser` | A locked-out user sees a "locked out" error message |

### InventoryTest (3 tests)

| Test | What it checks |
|---|---|
| `testAddItemToCart` | Clicking "Add to Cart" makes the cart badge show "1" |
| `testAddMultipleItemsToCart` | Adding 3 items shows "3" on the cart badge |
| `testInventoryPageDisplaysProducts` | The products page has a title "Products" and shows at least one product |

### CartTest (3 tests)

| Test | What it checks |
|---|---|
| `testCartDisplaysItems` | Items added to cart appear correctly on the cart page |
| `testRemoveItemFromCart` | Removing an item updates the cart count correctly |
| `testContinueShopping` | "Continue Shopping" button returns to the products page |

### CheckoutTest (3 tests)

| Test | What it checks |
|---|---|
| `testCompleteCheckout` | Full purchase flow works: fill form → review → confirm |
| `testCheckoutWithoutFirstName` | Empty first name prevents proceeding (validation) |
| `testCheckoutWithDifferentCustomer` | Checkout works with different customer data |

### Total: 12 test cases covering the complete user journey

---

## ⚙️ Configuration Guide

All configuration is in `src/test/resources/config.properties`. Here's what each setting does:

```properties
# The URL of the website to test
app.url = https://www.saucedemo.com/

# Which browser to use: "chrome" or "firefox"
browser = chrome

# How long (seconds) to wait for elements to appear
# Increase this if tests fail with timeout errors
explicit.wait = 10

# SauceDemo test credentials
username = standard_user
password = secret_sauce
```

### Changing Configuration Examples

**To test on a different URL:**
```properties
app.url = https://my-own-test-site.com/
```

**To make tests wait longer (for slow websites):**
```properties
explicit.wait = 20
```

**To use a different test user:**
```properties
username = performance_glitch_user
```

---

## 🔍 Troubleshooting Common Issues

### "Maven is not recognized as a command"

Maven is not installed or not in your PATH.
- **Solution**: Install Maven or restart your terminal after installation.

### "Java is not recognized"

Java JDK is not installed or not in your PATH.
- **Solution**: Install JDK 11+ and set the `JAVA_HOME` environment variable.

### Build fails with "cannot find symbol"

- **Solution**: Delete the `target/` folder and run `mvn clean compile` or `mvn clean test`.

### Tests fail with "timeout" errors

Elements aren't appearing within the wait time.
- **Solution**: Increase `explicit.wait` in `config.properties` (try 15 or 20 seconds).

### Browser opens but no window appears

Tests might be running in headless mode.
- **Solution**: Check if `--headless` is uncommented in `DriverManager.java`.

### "Cannot find Chrome binary" error

Chrome is not installed.
- **Solution**: Install Google Chrome from https://www.google.com/chrome/

### "SessionNotCreatedException" error

Your Chrome browser version and chromedriver version don't match. This rarely happens with WebDriverManager, but if it does:
- **Solution**: Update Chrome to the latest version.

### Tests pass on my machine but fail on another

- **Solution**: Ensure both machines have the same browser version and Java version.

---

## 🧩 Extending the Framework

### Adding a New Page Object

1. Create a new class in `src/main/java/com/demo/automation/pages/`
2. Extend `BasePage`
3. Add element locators using `@FindBy`
4. Add action methods

Example:
```java
package com.demo.automation.pages;

import com.demo.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class YourNewPage extends BasePage {
    
    @FindBy(id = "element-id")
    private WebElement someElement;
    
    public YourNewPage(WebDriver driver) {
        super(driver);
    }
    
    public void clickSomeElement() {
        click(someElement);
    }
}
```

### Adding a New Test Class

1. Create a new class in `src/test/java/com/demo/automation/tests/`
2. Extend `BaseTest`
3. Add methods annotated with `@Test`

```java
package com.demo.automation.tests;

import com.demo.automation.base.BaseTest;
import org.junit.jupiter.api.Test;

public class MyNewTest extends BaseTest {
    
    @Test
    public void testSomething() {
        // Your test code here
        // "driver" is available from BaseTest
    }
}
```

### Adding Browser Support

In `DriverManager.java`, add a new `case` in the switch statement:

```java
case "edge":
    WebDriverManager.edgedriver().setup();
    EdgeOptions edgeOptions = new EdgeOptions();
    driver = new EdgeDriver(edgeOptions);
    break;
```

Don't forget to import:
```java
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
```

And add the Edge dependency to `pom.xml` (Selenium Java includes it by default).

---

## 📝 Summary

This framework demonstrates:

| Concept | Where it's implemented |
|---|---|
| **Page Object Model** | All classes in `src/main/java/pages/` |
| **Inheritance** | `BasePage` → page classes, `BaseTest` → test classes |
| **Encapsulation** | Elements are `private`, methods are `public` |
| **Explicit Waits** | `WaitHelper.java` and `BasePage.java` click/sendKeys/getText |
| **Configuration Management** | `ConfigReader.java` reads `config.properties` |
| **Singleton Pattern** | `DriverManager.java` manages a single WebDriver instance |
| **Method Chaining** | Page methods return `this` for fluent test code |
| **Convenience Methods** | `loginAs()`, `fillCheckoutInfo()` combine multiple steps |
| **JUnit 5 Lifecycle** | `@BeforeEach`, `@AfterEach`, `@Test`, `@DisplayName` |
| **Assertions** | `Assertions.assertEquals()`, `Assertions.assertTrue()` |

---

*Happy Testing! 🧪*
