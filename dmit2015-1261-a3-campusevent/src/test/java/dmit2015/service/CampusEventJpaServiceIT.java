package dmit2015.service;

import dmit2015.config.ApplicationInitializer;
import dmit2015.config.TestDataSourceConfig;
import dmit2015.model.CampusEvent;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for CampusEventJpaService running inside WildFly with Arquillian.
 * Each test runs in its own transaction that is rolled back afterwards, so tests do not affect each other.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ArquillianTest
public class CampusEventJpaServiceIT {

    private static final String FULL_NAME = "Juan Rodriguez";

    @Deployment
    public static WebArchive createDeployment() {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        return ShrinkWrap.create(WebArchive.class, "campus-event-test.war")
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.h2database:h2").withTransitivity().asFile())
                .addClasses(TestDataSourceConfig.class, ApplicationInitializer.class)
                .addPackages(true, "dmit2015.model", "dmit2015.service")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    @Named("jakartaPersistenceCampusEventService")
    private CampusEventService campusEventService;

    @Resource
    private UserTransaction beanManagedTransaction;

    @BeforeEach
    void beforeEachTestMethod() throws SystemException, NotSupportedException {
        beanManagedTransaction.begin();
    }

    @AfterEach
    void afterEachTestMethod() throws SystemException {
        beanManagedTransaction.rollback();
    }

    private static CampusEvent newPersonalizedEvent() {
        return CampusEvent.of(
                "A3-" + FULL_NAME + " CampusEvent",
                "NAIT Main Campus, Room W213",
                LocalDate.of(2026, 11, 20),
                "Testing",
                "Integration test record created by " + FULL_NAME + ".",
                FULL_NAME);
    }

    @Test
    @Order(1)
    void shouldInjectCampusEventService() {
        assertThat(campusEventService).isNotNull();
    }

    /** Verifies create() persists a new CampusEvent and the database assigns an id, version, and audit times. */
    @Test
    @Order(2)
    void givenNewCampusEvent_whenCreate_thenCampusEventIsPersisted() {
        CampusEvent newEvent = newPersonalizedEvent();

        CampusEvent createdEvent = campusEventService.create(newEvent);

        assertThat(createdEvent.getId()).isNotNull();
        assertThat(createdEvent.getVersion()).isNotNull();
        assertThat(createdEvent.getCreateTime()).isNotNull();
        assertThat(createdEvent.getUpdateTime()).isEqualTo(createdEvent.getCreateTime());
        assertThat(campusEventService.getCampusEventById(createdEvent.getId())).isPresent();
    }

    /** Verifies getCampusEventById() returns the stored record with the same field values. */
    @Test
    @Order(3)
    void givenExistingId_whenGetCampusEventById_thenReturnCampusEvent() {
        CampusEvent createdEvent = campusEventService.create(newPersonalizedEvent());

        Optional<CampusEvent> optionalEvent = campusEventService.getCampusEventById(createdEvent.getId());

        assertThat(optionalEvent).isPresent();
        CampusEvent foundEvent = optionalEvent.orElseThrow();
        assertThat(foundEvent.getTitle()).isEqualTo("A3-Juan Rodriguez CampusEvent");
        assertThat(foundEvent.getOrganizerName()).isEqualTo(FULL_NAME);
        assertThat(foundEvent.getEventDate()).isEqualTo(LocalDate.of(2026, 11, 20));
    }

    /** Verifies getCampusEventById() returns an empty Optional for an id that does not exist. */
    @Test
    @Order(4)
    void givenNonExistingId_whenGetCampusEventById_thenReturnEmpty() {
        assertThat(campusEventService.getCampusEventById(Long.MAX_VALUE)).isEmpty();
    }

    /** Verifies getAllCampusEvents() returns every stored record, including the seed data and new records. */
    @ParameterizedTest
    @Order(5)
    @CsvSource({"3"})
    void givenMultipleCampusEvents_whenGetAllCampusEvents_thenReturnStoredRecords(int newRecordCount) {
        int existingCount = campusEventService.getAllCampusEvents().size();
        // The ApplicationInitializer seeds at least 10 records when the database starts empty
        assertThat(existingCount).isGreaterThanOrEqualTo(10);

        for (int counter = 1; counter <= newRecordCount; counter++) {
            CampusEvent currentEvent = newPersonalizedEvent();
            currentEvent.setTitle("A3-" + FULL_NAME + " CampusEvent " + counter);
            campusEventService.create(currentEvent);
        }

        List<CampusEvent> allEvents = campusEventService.getAllCampusEvents();

        assertThat(allEvents).hasSize(existingCount + newRecordCount);
        assertThat(allEvents)
                .extracting(CampusEvent::getTitle)
                .contains("A3-Juan Rodriguez CampusEvent 1",
                        "A3-Juan Rodriguez CampusEvent " + newRecordCount);
    }

    /** Verifies updateCampusEvent() saves edited field values and refreshes updateTime. */
    @Test
    @Order(6)
    void givenExistingCampusEvent_whenUpdateCampusEvent_thenChangesAreSaved() throws InterruptedException {
        CampusEvent createdEvent = campusEventService.create(newPersonalizedEvent());
        var originalUpdateTime = createdEvent.getUpdateTime();
        Thread.sleep(5);

        createdEvent.setTitle("A3-Juan Rodriguez CampusEvent Updated");
        createdEvent.setLocation("Shaw Theatre");
        createdEvent.setCategory("Workshop");
        CampusEvent updatedEvent = campusEventService.updateCampusEvent(createdEvent);
        // Flush so @PreUpdate runs before checking updateTime
        campusEventService.getAllCampusEvents();

        CampusEvent foundEvent = campusEventService.getCampusEventById(updatedEvent.getId()).orElseThrow();
        assertThat(foundEvent.getTitle()).isEqualTo("A3-Juan Rodriguez CampusEvent Updated");
        assertThat(foundEvent.getLocation()).isEqualTo("Shaw Theatre");
        assertThat(foundEvent.getCategory()).isEqualTo("Workshop");
        assertThat(foundEvent.getUpdateTime()).isAfter(originalUpdateTime);
    }

    /** Verifies deleteCampusEventById() removes the record so it can no longer be found. */
    @Test
    @Order(7)
    void givenExistingId_whenDeleteCampusEventById_thenCampusEventIsDeleted() {
        CampusEvent createdEvent = campusEventService.create(newPersonalizedEvent());

        campusEventService.deleteCampusEventById(createdEvent.getId());

        assertThat(campusEventService.getCampusEventById(createdEvent.getId())).isEmpty();
    }

    /** Verifies Jakarta Validation rejects a CampusEvent with invalid field values. */
    @ParameterizedTest
    @Order(8)
    @CsvSource(value = {
            "AB, NAIT Main Campus, Juan Rodriguez, Title must be between 3 and 80 characters",
            "Career Fair, null, Juan Rodriguez, Location is required",
            "Career Fair, NAIT Main Campus, null, Organizer name is required",
    }, nullValues = {"null"})
    void givenInvalidCampusEvent_whenCreate_thenThrowValidationException(
            String title, String location, String organizerName, String expectedMessage) {
        CampusEvent invalidEvent = CampusEvent.of(title, location, LocalDate.of(2026, 11, 20),
                null, null, organizerName);

        assertThatThrownBy(() -> campusEventService.create(invalidEvent))
                .hasStackTraceContaining(expectedMessage);
    }
}
