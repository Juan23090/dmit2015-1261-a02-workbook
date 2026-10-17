# DMIT2015 Assignment 3 - Jakarta Persistence with CampusEvent and Integration Testing

## Student and Submission Information

* Name: Juan Rodriguez
* Repository Account Username: Juan23090
* Course Section: DMIT2015-1261-A02
* Repository URL: YOUR REPOSITORY URL
* Branch: YOUR BRANCH
* Final Commit Hash: YOUR FINAL COMMIT HASH

## Fall 2026 Starter Version

This repository is for the Fall 2026 version of Assignment 3.

* Use the required Assignment 3 fields: `title`, `location`, `eventDate`, `category`, `description`, and `organizerName`.
* Use a consistent identifier type throughout the entity, service, tests, backing bean, and page.
* Name the file-based database `DMIT2015-1261-A3-FirstnameLastname-DB`.
* Include your full name in at least one seed record and one integration-test record.

## Project Purpose

This starter project is configured with:

* Jakarta EE 11 Web Profile;
* WildFly Maven Plugin;
* Jakarta Faces;
* Jakarta Persistence;
* a file-based H2 database;
* Arquillian integration testing; and
* Maven Failsafe for `mvn verify`.

Complete the project using `CampusEvent`, `CampusEventService`, and `CampusEventJpaService`. Do not recreate the project from scratch or put persistence logic directly in the JSF backing bean.

## Running the Application

Start the application with:

```
mvn wildfly:dev
```

The first run may take several minutes while Maven downloads dependencies and WildFly starts. The `dev` goal remains active until you press `Ctrl+C`.

Document any additional setup required for your completed project here:

* Requires JDK 25 (the compiler release is set to 25).
* Open <http://localhost:8080/events/manage-campus-events> to manage campus events. The home page at <http://localhost:8080/> links to it.
* The H2 database file is created at `~/jdk/databases/h2/DMIT2015-1261-A3-JuanRodriguez-DB.mv.db` (in your home folder, outside the project), so it is never committed.
* On the first start, `ApplicationInitializer` seeds 11 sample records when the `CAMPUS_EVENT` table is empty. Later starts skip seeding, so no duplicates are added.

## Running the Integration Tests

Run:

```
mvn verify
```

Before submitting, confirm that the required Arquillian tests pass and that each test has a clear purpose you can explain during the demo.

How the tests are configured:

* `mvn verify` provisions a separate WildFly 41 server into `target/arquillian-server` and Arquillian starts it automatically. No local WildFly path needs to be set.
* To use an existing local WildFly installation instead, run `mvn verify -Darquillian.jboss.home=/path/to/wildfly-41.0.1.Final`.
* The test deployment uses its own in-memory H2 data source (`src/test/java/dmit2015/config/TestDataSourceConfig.java` and `src/test/resources/META-INF/test-persistence.xml`), so running the tests never changes the data in the application database. Each test also runs in a transaction that is rolled back.
* Stop `mvn wildfly:dev` before running `mvn verify`, because both use port 8080.

`CampusEventJpaServiceIT` contains these tests:

| Test | What it verifies |
| --- | --- |
| `shouldInjectCampusEventService` | The `CampusEventJpaService` bean is injected by CDI inside WildFly. |
| `givenNewCampusEvent_whenCreate_thenCampusEventIsPersisted` | `create()` persists a new event (`A3-Juan Rodriguez CampusEvent`) and the database assigns its id, version, and audit timestamps. |
| `givenExistingId_whenGetCampusEventById_thenReturnCampusEvent` | Finding by id returns the stored record with the same field values. |
| `givenNonExistingId_whenGetCampusEventById_thenReturnEmpty` | Finding an id that does not exist returns an empty `Optional`. |
| `givenMultipleCampusEvents_whenGetAllCampusEvents_thenReturnStoredRecords` | Find-all returns the seeded records plus every newly created record. |
| `givenExistingCampusEvent_whenUpdateCampusEvent_thenChangesAreSaved` | Update saves the edited fields and `@PreUpdate` refreshes `updateTime`. |
| `givenExistingId_whenDeleteCampusEventById_thenCampusEventIsDeleted` | Delete removes the record so it can no longer be found. |
| `givenInvalidCampusEvent_whenCreate_thenThrowValidationException` | Jakarta Validation rejects an event with a short title, missing location, or missing organizer name. |

## H2 Database Configuration

Use a file-based H2 database named:

```
DMIT2015-1261-A3-JuanRodriguez-DB
```

The data source is defined in `src/main/java/dmit2015/config/ApplicationConfig.java`.

During development, the project may use:

```
<property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create"/>
```

Before the final demo and submission, change it to:

```
<property name="jakarta.persistence.schema-generation.database.action" value="none"/>
```

The submitted application uses `schema-generation.database.action=none` so that data persists between application restarts.

If the project is run on a computer where your H2 database does not yet exist, temporarily change the schema-generation setting back to `drop-and-create` to initialize the database, then restore the required final `none` setting before the final demo and submission.

Confirm that a newly created record remains after a page refresh and an application restart or redeploy.

Do not commit the H2 database file to the repository.

## Required Implementation Checklist

* `CampusEvent` is a valid JPA entity.
* `@Id` and a valid identifier-generation strategy are configured.
* `@Version` provides optimistic locking.
* `createTime` and `updateTime` are set through lifecycle callbacks.
* Jakarta Validation is applied to at least four domain fields.
* At least 10 realistic records are seeded only when the database is empty.
* `CampusEventJpaService` implements `CampusEventService`.
* JPA persistence logic remains outside the JSF backing bean.
* Required Arquillian integration tests pass with `mvn verify`.
* The JSF page displays seeded records and creates a new record.
* Data persists after refresh and restart or redeploy.

## Implementation Notes and Known Issues

This section provides operational information needed to run or assess the project. It is not the required Reflection Post; submit the complete reflection directly in Brightspace.

### Identifier Type

State the identifier type used by `CampusEvent`:

`Long`, generated by the database with `@GeneratedValue(strategy = GenerationType.IDENTITY)`. The same `Long` id is used in `CampusEventService`, `CampusEventJpaService`, `CampusEventJpaServiceIT`, `CampusEventCrudView`, and `manage-campus-events.xhtml`.

### Known Issues

List issues that affect running or assessing the project:

None are known.

## Git Workflow Reminder

* Make multiple meaningful commits across at least two calendar days.
* Show substantive progress before the final day.
* Commit meaningful milestones for entity mapping, service implementation, integration testing, JSF work, seed data, and persistence testing.
* Use messages that identify what you added, changed, tested, corrected, or completed.
* Avoid generic messages such as `update`, `fix issue`, `changes`, or `done some stuff`.
* Push all required source, configuration, test, and documentation files before the deadline.

Do not create artificial commits solely to increase the commit count.

## Preparing the Source-Code ZIP

The source-code ZIP is a snapshot of your submitted repository. It is not a replacement for providing your repository URL, branch, and final commit hash.

After completing your assignment:

1. Commit and push all required work to GitHub.
2. Confirm that GitHub shows the correct final version of your project.
3. Confirm that you are viewing the branch identified in your submission information.
4. Record the final commit hash in this README and in the location required by Brightspace.
5. From the GitHub repository page, select Code and then Download ZIP.
6. Submit the downloaded ZIP file to the Assignment 3 activity in Brightspace.

Use the ZIP downloaded from GitHub rather than manually creating a ZIP of your local project.

The GitHub ZIP should represent the same source code as the repository branch and final commit identified in your submission.

Do not add your demo video, local H2 database, `target/` directory, local WildFly installation, or other files to the downloaded ZIP.

## Assignment and Submission Requirements

Refer to the official Assignment 3 specification and Brightspace activity for:

* the official due date and submission time;
* complete functional and technical requirements;
* repository setup and access instructions;
* source-code ZIP requirements;
* demo video and presenter-identification requirements;
* required submission items; and
* the marking rubric.

Pushing code to the repository does not constitute a Brightspace submission.

Your repository URL, branch, and final commit hash are required submission information. Uploading a source-code ZIP does not replace the repository requirement.

Submit the Reflection Post and AI Use Declaration directly in the designated Brightspace location. Do not place the required Reflection Post only in this README or elsewhere in the repository.
