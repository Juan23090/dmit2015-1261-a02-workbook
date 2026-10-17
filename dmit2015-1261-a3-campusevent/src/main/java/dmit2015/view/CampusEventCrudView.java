package dmit2015.view;

import dmit2015.model.CampusEvent;
import dmit2015.service.CampusEventService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

/**
 * Backing bean for manage-campus-events.xhtml.
 * All data access goes through CampusEventService; this class has no EntityManager or JPA code.
 */
@Named("currentCampusEventCrudView")
@ViewScoped
public class CampusEventCrudView implements Serializable {

    @Inject
    @Named("jakartaPersistenceCampusEventService")
    private CampusEventService campusEventService;

    /** The CampusEvent being created, edited, or deleted. */
    @Getter
    @Setter
    private CampusEvent selectedCampusEvent;

    /** The id of the selected CampusEvent; null when creating a new record. */
    @Getter
    @Setter
    private Long selectedId;

    @Getter
    private List<CampusEvent> campusEvents;

    @PostConstruct
    public void init() {
        try {
            campusEvents = campusEventService.getAllCampusEvents();
        } catch (Exception ex) {
            Messages.addGlobalError("Error getting campus events: {0}", ex.getMessage());
        }
    }

    /** Event handler for the New button. */
    public void onOpenNew() {
        selectedCampusEvent = new CampusEvent();
        selectedId = null;
    }

    /** Event handler for the Save button to create or update a CampusEvent. */
    public void onSave() {
        try {
            if (selectedId == null) {
                CampusEvent createdEvent = campusEventService.create(selectedCampusEvent);
                Messages.addGlobalInfo("Created campus event \"{0}\" with id {1}",
                        createdEvent.getTitle(), createdEvent.getId());
            } else {
                campusEventService.updateCampusEvent(selectedCampusEvent);
                Messages.addGlobalInfo("Updated campus event with id {0}", selectedId);
            }
            selectedCampusEvent = null;
            selectedId = null;

            campusEvents = campusEventService.getAllCampusEvents();
            PrimeFaces.current().ajax().update("form:dt-campus-events");
            PrimeFaces.current().executeScript("PF('manageCampusEventDialog').hide()");
        } catch (Exception ex) {
            Messages.addGlobalError("Save not successful: {0}", rootCauseMessage(ex));
        }
    }

    /** Event handler for the Delete confirmation to delete the selected CampusEvent. */
    public void onDelete() {
        try {
            Long deleteId = selectedCampusEvent.getId();
            campusEventService.deleteCampusEventById(deleteId);
            Messages.addGlobalInfo("Deleted campus event with id {0}", deleteId);
            selectedCampusEvent = null;
            selectedId = null;

            campusEvents = campusEventService.getAllCampusEvents();
            PrimeFaces.current().ajax().update("form:dt-campus-events");
        } catch (Exception ex) {
            Messages.addGlobalError("Delete not successful: {0}", rootCauseMessage(ex));
        }
    }

    private String rootCauseMessage(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}
