package adeo.leroymerlin.cdp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAll();
        // Filter the events list in pure JAVA here

        return events;
    }

    public void updateEvent(Long id, Event newValues) {;
        Event eventToUpdate = eventRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Event not found"));

        mergeOldAndNewEvent(eventToUpdate, newValues);
        eventRepository.save(eventToUpdate);
    }


    private void mergeOldAndNewEvent(Event old, Event newEvent) {
        old.setComment(newEvent.getComment());
        old.setNbStars(newEvent.getNbStars());
    }

}
