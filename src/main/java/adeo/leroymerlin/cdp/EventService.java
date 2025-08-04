package adeo.leroymerlin.cdp;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Get all events
     * @return
     */
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    /**
     * Delete an event
     * @param id
     */
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }


    /**
     * Get event list from search criteria
     *         Get all events
     *          that have at least one band
     *          that has at least one member
     *          whose name contains the search query
     * @param query
     * @return a list of event
     */
    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAll();
        List<Event> filteredEvents = events.stream()
                .filter(event -> event.getBands().stream()
                        .anyMatch(band -> band.getMembers().stream()
                                .anyMatch(member -> member.getName()
                                        .toLowerCase().contains(query.toLowerCase()))
                        )
                ).collect(Collectors.toList());
        return filteredEvents;
    }

    /**
     * Update an event with new values
     * @param id
     * @param newValues
     */
    public void updateEvent(Long id, Event newValues) {;
        Event eventToUpdate = eventRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Event not found"));

        mergeEventWithNewValues(eventToUpdate, newValues);
        eventRepository.save(eventToUpdate);
    }

    /**
     * Update current event with new values
     *
     * @param eventToUpdate
     * @param newValues
     */
    private void mergeEventWithNewValues(Event eventToUpdate, Event newValues) {
        eventToUpdate.setComment(newValues.getComment());
        eventToUpdate.setNbStars(newValues.getNbStars());
    }


}
