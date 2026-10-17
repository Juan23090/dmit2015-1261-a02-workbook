package dmit2015.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A campus event stored in the CAMPUS_EVENT table.
 */
@Entity
@Table(name = "CAMPUS_EVENT")
@Getter
@Setter
@NoArgsConstructor
public class CampusEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campus_event_id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 80, message = "Title must be between {min} and {max} characters")
    @Column(nullable = false, length = 80)
    private String title;

    @NotBlank(message = "Location is required")
    @Size(min = 3, max = 80, message = "Location must be between {min} and {max} characters")
    @Column(nullable = false, length = 80)
    private String location;

    @NotNull(message = "Event date is required")
    @Column(nullable = false)
    private LocalDate eventDate;

    // Optional: null is allowed, but a supplied value must be 3-40 characters
    @Size(min = 3, max = 40, message = "Category must be between {min} and {max} characters when supplied")
    @Column(length = 40)
    private String category;

    // Optional: null is allowed, but a supplied value must be 10-500 characters
    @Size(min = 10, max = 500, message = "Description must be between {min} and {max} characters when supplied")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "Organizer name is required")
    @Size(min = 3, max = 80, message = "Organizer name must be between {min} and {max} characters")
    @Column(nullable = false, length = 80)
    private String organizerName;

    @Version
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

    @PreUpdate
    void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public static CampusEvent of(String title, String location, LocalDate eventDate,
                                 String category, String description, String organizerName) {
        CampusEvent campusEvent = new CampusEvent();
        campusEvent.setTitle(title);
        campusEvent.setLocation(location);
        campusEvent.setEventDate(eventDate);
        campusEvent.setCategory(category);
        campusEvent.setDescription(description);
        campusEvent.setOrganizerName(organizerName);
        return campusEvent;
    }
}
