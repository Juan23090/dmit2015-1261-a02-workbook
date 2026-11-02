# DMIT2015 Lesson 15 - Functional UI Testing with Selenium WebDriver

This practice project is used for Lesson 15: **Functional UI Testing with Selenium WebDriver**.

The goal of this activity is to learn how to automate a web browser using Selenium WebDriver and verify that a Jakarta Faces page works from a user's perspective.

Functional UI testing follows this pattern:

```text
Open page → find element → interact → wait → assert visible result
```

You will complete a set of Selenium practice activities using the provided page:

```text
selenium-practice.xhtml
```

---

## How to Use This Repository

This repository is a public starter project for the Lesson 15 Functional UI Testing with Selenium WebDriver practice activity.

Do **not** clone the original repository directly unless you only want a read-only copy. If you clone the original repository, you will not be able to push your changes back to GitHub.

Use the following workflow:

1. Click **Fork** on the GitHub repository page.
2. Create the fork under your own GitHub account.
3. Clone **your fork** into IntelliJ IDEA.
4. Complete the TaskManager JPA practice activity.
5. Commit and push your changes to **your fork**.
6. Use what you learned to complete Assignment 4 in your separate Assignment 4 GitHub Classroom repository.

Important:

- This practice repository is **not** your Assignment 4 submission.
- Assignment 4 must be completed in your separate GitHub Classroom Assignment 4 repository.
- Do not submit your Lesson 15 practice fork for Assignment 4.

## Learning Outcomes

By the end of this practice activity, you should be able to:

* open a browser using Selenium WebDriver
* navigate to a Jakarta Faces page
* locate HTML elements using Selenium locators
* enter text into input fields
* click buttons and links
* read visible page content
* verify expected results using assertions
* use `WebDriverWait` instead of `Thread.sleep()`
* understand how Jakarta Faces generated IDs affect Selenium locators

---

## Project Pages

The starter project includes the following pages:

| Page                      | Purpose                                    |
| ------------------------- | ------------------------------------------ |
| `index.xhtml`             | Home page used for navigation testing      |
| `selenium-practice.xhtml` | Practice page used for Selenium activities |

---

## Practice Page Features

The `selenium-practice.xhtml` page includes:

* a page heading
* a search term input field
* a search mode dropdown
* an "Only show stores with stock" checkbox
* a Submit button
* a Clear button
* a result panel
* a validation message area
* a Go Home button or link

You will use Selenium WebDriver to interact with these page elements.

---

## Before You Start

Make sure:

1. The project builds successfully.
2. The application is deployed and running.
3. You can open the practice page manually in your browser.
4. You can run the provided Selenium test class.

Example local URL:

```text
http://localhost:8080/selenium-practice.xhtml
```

Your actual URL may be different depending on your project name and server configuration.

---

## Running the Tests

You can run the Selenium test class from IntelliJ IDEA.

You can also run tests from the terminal using Maven:

```bash
mvn verify
```

If the browser opens and closes automatically, Selenium WebDriver is working.

---

## Important Rule: Do Not Use `Thread.sleep()`

Do not use `Thread.sleep()` in your final practice tests.

Use `WebDriverWait` instead.

Good:

```java
WebElement resultPanel = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("practiceForm:resultPanel"))
);
```

Avoid:

```java
Thread.sleep(3000);
```

A fixed delay may work on your machine once, but it can make tests slow and unreliable.

---

# Practice Activities

## Required Checkpoint

Complete Activities 1 to 3 first.

These activities are the minimum goal for this lesson.

---

## Activity 1 - Open the Browser and Verify the Page

### Goal

Confirm that Selenium can open the practice page and read content from it.

### Tasks

Write a test that:

1. Opens `selenium-practice.xhtml`.
2. Verifies that the page title contains:

```text
Selenium Practice
```

3. Finds the page heading.
4. Verifies that the heading contains:

```text
Functional UI Testing Practice
```

### Hints

Useful Selenium methods:

```java
driver.get(url);
driver.getTitle();
driver.findElement(By.id("..."));
element.getText();
```

Useful assertion:

```java
assertThat(driver.getTitle()).contains("Selenium Practice");
```

---

## Activity 2 - Locate Page Elements

### Goal

Learn how to inspect the page and identify stable Selenium locators.

### Tasks

Use your browser's Inspect tool to find the IDs for the following elements:

* search term input field
* search mode dropdown
* in-stock checkbox
* submit button
* clear button
* result panel
* Go Home button or link

Record the locators in comments in your test class.

Example:

```java
// Search term field: practiceForm:searchTerm
// Submit button: practiceForm:submitButton
```

### Important Note About Jakarta Faces IDs

Jakarta Faces often generates client IDs that include the parent form ID.

Example:

```text
practiceForm:searchTerm
```

When using `By.id()`, this is fine:

```java
By.id("practiceForm:searchTerm")
```

When using CSS selectors, the colon can cause issues. Use an attribute selector:

```java
By.cssSelector("[id='practiceForm:searchTerm']")
```

---

## Activity 3 - Enter Text, Click Submit, and Verify the Result

### Goal

Automate a simple user workflow.

### Tasks

Write a test that:

1. Opens `selenium-practice.xhtml`.
2. Enters the search term:

```text
keyboard
```

3. Clicks the Submit button.
4. Verifies that the result panel contains:

```text
keyboard
```

### Hints

Useful Selenium methods:

```java
element.clear();
element.sendKeys("keyboard");
element.click();
element.getText();
```

Example structure:

```java
WebElement searchTermField = driver.findElement(By.id("practiceForm:searchTerm"));
searchTermField.clear();
searchTermField.sendKeys("keyboard");

WebElement submitButton = driver.findElement(By.id("practiceForm:submitButton"));
submitButton.click();

WebElement resultPanel = driver.findElement(By.id("practiceForm:resultPanel"));
assertThat(resultPanel.getText()).contains("keyboard");
```

This version may work, but it can be improved using `WebDriverWait` in Activity 4.

---

# Target Checkpoint

Complete Activities 4 and 5 after you finish the required checkpoint.

---

## Activity 4 - Add WebDriverWait

### Goal

Make the test more reliable by waiting for the browser result before asserting.

### Tasks

Update your Activity 3 test so it:

1. Clicks the Submit button.
2. Waits until the result panel is visible.
3. Verifies the result panel contains the submitted search term.

### Hints

Create a wait:

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
```

Wait for the result panel:

```java
WebElement resultPanel = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("practiceForm:resultPanel"))
);
```

Then assert:

```java
assertThat(resultPanel.getText()).contains("keyboard");
```

### Key Idea

Do not wait for a number of seconds.

Wait for the browser condition you expect.

---

## Activity 5 - Test Navigation Back to Home Page

### Goal

Verify that Selenium can test page navigation.

### Tasks

Write a test that:

1. Opens `selenium-practice.xhtml`.
2. Clicks the Go Home button or link.
3. Verifies that the browser navigated to the home page.

### Possible Assertions

You can verify the URL:

```java
assertThat(driver.getCurrentUrl()).contains("index.xhtml");
```

Or verify visible page content:

```java
WebElement heading = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("homeHeading"))
);

assertThat(heading.getText()).contains("Home");
```

Visible page content is usually a stronger assertion than only checking the URL.

---

# Extension Checkpoint

Complete Activities 6 and 7 if you finish early.

---

## Activity 6 - Test Validation Feedback

### Goal

Verify that the page displays an error message when required input is missing.

### Tasks

Write a test that:

1. Opens `selenium-practice.xhtml`.
2. Leaves the search term field empty.
3. Clicks Submit.
4. Verifies that a validation message is displayed.

### Expected Message

The page should display a message similar to:

```text
Search term is required.
```

### Hints

Wait for the message element before asserting.

Example:

```java
WebElement message = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("practiceForm:searchTermMessage"))
);

assertThat(message.getText()).contains("Search term is required");
```

---

## Activity 7 - Test Clear or Reset

### Goal

Verify that the Clear button resets the main form input.

### Tasks

Write a test that:

1. Opens `selenium-practice.xhtml`.
2. Enters the search term `monitor`.
3. Selects a search mode.
4. Optionally checks "Only show stores with stock".
5. Clicks Submit.
6. Verifies that a result is displayed.
7. Clicks Clear.
8. Verifies that the search term field is empty.

### Optional Extension

Also verify one or more of the following:

- the selected search mode resets to the default value
- the checkbox is unchecked
- the result panel is cleared or no longer displayed

# Optional Challenge - Apply Selenium to Your Own Assignment 3 Page

If you completed Assignment 3, try writing one Selenium test for your own Jakarta Faces CRUD page.

Suggested workflow:

1. Open your create page.
2. Enter valid form data.
3. Click Create or Save.
4. Verify that a success message appears.
5. Navigate to the list page.
6. Verify that the created item appears in the table.

This is optional practice only.

---

# Suggested Test Class Structure

Your test class may follow this structure:

```java
class SeleniumPracticeSeleniumIT {

    private WebDriver driver;
    private WebDriverWait wait;

    private final String baseUrl = "http://localhost:8080;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldOpenPracticePage() {
        // Activity 1
    }

    @Test
    void shouldSubmitSearchRequest() {
        // Activities 3 and 4
    }

    @Test
    void shouldNavigateHome() {
        // Activity 5
    }

    @Test
    void shouldDisplayValidationMessage() {
        // Activity 6
    }

    @Test
    void shouldClearSearchRequest() {
        // Activity 7
    }
}
```

---

# Common Problems

## Browser does not open

Check that the Selenium WebDriver dependency and WebDriverManager dependency are included in `pom.xml`.

---

## Element cannot be found

Check the actual generated HTML in the browser using Inspect.

Jakarta Faces IDs may include the form ID:

```text
practiceForm:searchTerm
```

---

## CSS selector fails when using JSF IDs

This may fail because of the colon:

```java
By.cssSelector("#practiceForm:searchTerm")
```

Use this instead:

```java
By.cssSelector("[id='practiceForm:searchTerm']")
```

---

## Test fails because result is not visible yet

Use `WebDriverWait`.

```java
WebElement resultPanel = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("practiceForm:resultPanel"))
);
```

---

## Test passes sometimes and fails sometimes

This usually means the test has a timing issue.

Replace immediate assertions with explicit waits.

---

# Completion Checklist

Required:

* [ ] Activity 1 completed
* [ ] Activity 2 completed
* [ ] Activity 3 completed

Target:

* [ ] Activity 4 completed
* [ ] Activity 5 completed

Extension:

* [ ] Activity 6 completed
* [ ] Activity 7 completed

Optional:

* [ ] Selenium test created for your own Assignment 3 page

---

# Key Takeaway

Functional UI testing verifies that a user can complete a workflow in the browser.

Remember the pattern:

```text
Open page → find element → interact → wait → assert visible result
```

---
