package cdp;


import adeo.leroymerlin.cdp.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should call findall and return 2 results")
    void testGetAllEvents() {
        // GIVEN
        List<Event> mockEvents = List.of(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(mockEvents);


        // WHEN
        List<Event> result = eventService.getEvents();

        // THEN
        assertEquals(2, result.size());
        verify(eventRepository).findAll();
    }

    @Test
    @DisplayName("should call deleteById ")
    void testDeleteEvent() {
        // GIVEN
        Long eventId = 1L;

        // when
        eventService.delete(eventId);

        // then
        verify(eventRepository).deleteById(eventId);
    }

    @Test
    @DisplayName("Should update event return by findById and call method save with this updated event")
    void testUpdateEventShouldUpdateCommentAndNbStars() {
        // GIVEN
        Long eventId = 1L;
        Event existingEvent = new Event();
        existingEvent.setComment(null);
        existingEvent.setNbStars(null);

        Event newValues = new Event();
        newValues.setComment("New comment");
        newValues.setNbStars(5);

        // WHEN
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        eventService.updateEvent(eventId, newValues);

        // THEN
        assertEquals("New comment", existingEvent.getComment());
        assertEquals(5, existingEvent.getNbStars());
        verify(eventRepository).save(existingEvent);
    }

    @Test
    @DisplayName(" Should throw EntityNotFound exception if event is not found")
    void testUpdateEventShouldThrowIfNotFound() {
        //GIVEN
        Long eventId = 999L;
        Event updated = new Event();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        //WHEN
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.updateEvent(eventId, updated);
        });

        //THEN
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retourn one event containing a member containing 'Dup' in his name ")
    void test_getFilteredEvent(){

        String filteredName = "Dup";
        prepareFilteredEventMock();

        // WHEN
        List<Event> events = eventService.getFilteredEvents(filteredName);

        // THEN
        List<Band> resultBand = events.getFirst().getBands().stream().toList();
        assertEquals(1, events.size());
        assertEquals(2, resultBand.size());
        assertMemberNameExists(resultBand,"Doe",true);
        assertMemberNameExists(resultBand,"Dupont",true);
        assertMemberNameExists(resultBand,"Eude",true);
        assertMemberNameExists(resultBand,"Johny",false);

    }




    @Test
    @DisplayName("Should return filtered event with [count] at end of title and band names")
    void test_getFilteredEventWithCountAtEndOfTitleAndNames() {
        // GIVEN
        String filteredName = "Dup";
        prepareFilteredEventMock();
        List<Event> events = eventService.getFilteredEvents(filteredName);

        // WHEN
        eventService.addCountToEventAndBrandTitle(events);

        // THEN
        assertEquals(1, events.size());

        Event event = events.getFirst();
        int bandsCount = event.getBands().size();
        assertEquals("Title event 1 [2]",event.getTitle());
        assertEquals(2,bandsCount);

        List<Band> bands = event.getBands().stream().toList();

        assertEquals("BadGroup ["+bands.get(0).getMembers().size()+"]", bands.get(0).getName());
        assertEquals("BadGroup ["+bands.get(1).getMembers().size()+"]", bands.get(1).getName());

    }

    private void assertMemberNameExists(List<Band> resultBand,String memberName,boolean memberExists) {
        assertEquals(memberExists,
                resultBand.stream().anyMatch(band ->
                band.getMembers().stream()
                        .anyMatch(member -> member.getName().equals(memberName)))
        );
    }

    private void prepareFilteredEventMock(){
        // GIVEN
        Member member1 = new Member();
        member1.setName("Eude");
        Member member2 = new Member();
        member2.setName("Dupont");
        Member member3 = new Member();
        member3.setName("Johny");
        Member member4 = new Member();
        member4.setName("Doe");

        Event event1 = new Event();
        Band band1 = new Band();
        Band band2 = new Band();

        band1.setName("BadGroup");
        band1.setMembers(Set.of(member1,member2));

        band2.setName("BadGroup");
        band2.setMembers(Set.of(member4));

        event1.setBands(Set.of(band1,band2));
        event1.setTitle("Title event 1");

        Event event2 = new Event();
        Band band3 = new Band();

        band3.setName("SuperGroup");
        band3.setMembers(Set.of(member3));

        event2.setBands(Set.of(band3));
        event2.setTitle("Title event 2");

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));
    }
}
