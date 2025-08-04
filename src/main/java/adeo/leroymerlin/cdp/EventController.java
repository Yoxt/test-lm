package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * get all event
     * @return a list of events
     */
    @GetMapping(value = "/")
    public List<Event> findEvents() {
        return eventService.getEvents();
    }

    /**
     * Find event using query filter
     * @param query
     * @return a list of events
     */
    @GetMapping(value = "/search/{query}")
    public List<Event> findEvents(@PathVariable String query) {
        return eventService.getFilteredEvents(query);
    }

    /**
     * Delete event using id
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }

    /**
     * Update event using id and new values
     * @param id
     * @param event
     */
    @PutMapping(value = "/{id}")
    public void updateEvent(@PathVariable Long id, @RequestBody Event event) {
        eventService.updateEvent(id, event);
    }
}
