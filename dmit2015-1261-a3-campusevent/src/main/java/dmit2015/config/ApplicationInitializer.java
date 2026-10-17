package dmit2015.config;

import dmit2015.model.CampusEvent;
import dmit2015.service.CampusEventService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Seeds the CAMPUS_EVENT table with sample records when the application starts.
 * Records are added only when the table is empty, so later starts do not create duplicates.
 */
@ApplicationScoped
public class ApplicationInitializer {

    private final Logger logger = Logger.getLogger(ApplicationInitializer.class.getName());

    @Inject
    @Named("jakartaPersistenceCampusEventService")
    private CampusEventService campusEventService;

    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event) {
        if (!campusEventService.getAllCampusEvents().isEmpty()) {
            logger.info("CampusEvent data already exists; skipping seed data.");
            return;
        }

        try {
            List<CampusEvent> seedEvents = List.of(
                    CampusEvent.of("A3-Juan Rodriguez Java Study Session", "NAIT Main Campus, Room W213",
                            LocalDate.of(2026, 10, 22), "Academic",
                            "Peer study session on Jakarta Persistence hosted by Juan Rodriguez.",
                            "Juan Rodriguez"),
                    CampusEvent.of("Fall Career Fair", "Shaw Theatre Lobby",
                            LocalDate.of(2026, 10, 28), "Career",
                            "Meet employers from across Alberta and bring copies of your resume.",
                            "Career Services"),
                    CampusEvent.of("Halloween Costume Social", "Student Commons",
                            LocalDate.of(2026, 10, 30), "Social",
                            "Costume contest, snacks, and music in the Student Commons.",
                            "NAIT Students' Association"),
                    CampusEvent.of("Intro to Docker Workshop", "Computing Lab E121",
                            LocalDate.of(2026, 11, 4), "Workshop",
                            "Hands-on workshop covering containers, images, and Docker Compose.",
                            "Digital Media and IT Club"),
                    CampusEvent.of("Ooks Hockey Home Game", "Ice Arena",
                            LocalDate.of(2026, 11, 7), "Athletics",
                            "Cheer on the Ooks men's hockey team at their home opener.",
                            "NAIT Athletics"),
                    CampusEvent.of("Remembrance Day Ceremony", "Main Campus Atrium",
                            LocalDate.of(2026, 11, 11), "Community",
                            "A moment of silence and wreath laying to honour veterans.",
                            "Student Services"),
                    CampusEvent.of("Resume and LinkedIn Clinic", "Library Learning Commons",
                            LocalDate.of(2026, 11, 13), "Career",
                            "One-on-one feedback on resumes and LinkedIn profiles.",
                            "Career Services"),
                    CampusEvent.of("Exam Stress Wellness Day", "Wellness Centre",
                            LocalDate.of(2026, 11, 25), "Wellness",
                            "Therapy dogs, yoga, and study break activities before final exams.",
                            "Student Counselling"),
                    CampusEvent.of("Capstone Project Showcase", "Productivity and Innovation Centre",
                            LocalDate.of(2026, 12, 3), "Academic",
                            "Graduating DMIT students present their capstone projects.",
                            "School of ICT"),
                    CampusEvent.of("Holiday Food Drive", "Main Campus Entrance",
                            LocalDate.of(2026, 12, 9), "Volunteer",
                            "Donate non-perishable food items for the Edmonton Food Bank.",
                            "NAIT Students' Association"),
                    CampusEvent.of("Winter Term Orientation", "Shaw Theatre",
                            LocalDate.of(2027, 1, 6), "Orientation",
                            "Welcome session for new students starting in the winter term.",
                            "Student Services")
            );

            seedEvents.forEach(campusEventService::create);
            logger.info("Created " + campusEventService.getAllCampusEvents().size() + " CampusEvent records.");
        } catch (Exception ex) {
            logger.warning("Unable to seed CampusEvent data: " + ex.getMessage());
        }
    }
}
