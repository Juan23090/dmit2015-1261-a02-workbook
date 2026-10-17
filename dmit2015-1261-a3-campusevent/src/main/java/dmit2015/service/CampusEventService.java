package dmit2015.service;

import dmit2015.model.CampusEvent;

import java.util.List;
import java.util.Optional;

public interface CampusEventService {

    CampusEvent create(CampusEvent newEvent);

    List<CampusEvent> getAllCampusEvents();

    Optional<CampusEvent> getCampusEventById(Long id);

    CampusEvent updateCampusEvent(CampusEvent existingEvent);

    void deleteCampusEventById(Long id);
}
