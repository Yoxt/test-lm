package cdp;

import adeo.leroymerlin.cdp.Event;
import adeo.leroymerlin.cdp.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AdeoLeroyMerlinCDPRecruitmentApplicationTests.class)
@AutoConfigureMockMvc
@Sql(value = "classpath:/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/data-common.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EventControllerIT {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private EventRepository eventRepository;

  @Test
  @DisplayName("Should retourn a list of 5 events and OK status")
  public void testGetAllEvents() throws Exception {
    this.mvc.perform(MockMvcRequestBuilders.get("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$.length()").value(5))
            .andExpect(jsonPath("$[0].title").value("GrasPop Metal Meeting"));
  }


  @Test
  @DisplayName("Should delete event id 1000 and return OK")
  public void testDeleteEvent() throws Exception {
    Long idToDelete = 1000L;
    Event initialEvent = eventRepository.findById(idToDelete).orElse(null);

    mvc.perform(MockMvcRequestBuilders.delete("/api/events/{id}", idToDelete))
            .andExpect(status().isOk());;

    Event eventDeleted = eventRepository.findById(idToDelete).orElse(null);

    Assertions.assertNotNull(initialEvent);
    Assertions.assertNull(eventDeleted);

  }

  @Test
  @DisplayName("Should update stars numbers and comment of the event")
  public void testUpdateEvent() throws Exception {
    Long idToUpdate = 1003L;

    Event eventBeforeUpdate = eventRepository.findById(idToUpdate).orElse(null);

    String updatedEventJson = """
            {
                "id": 1003,
                "title": "Download Festival",
                "imgUrl": "img/1003.jpeg",
                "bands": [],
                "nbStars": 4,
                "comment": "A very very very very cool comment :D"
            }
        """;

    mvc.perform(MockMvcRequestBuilders.put("/api/events/{id}", idToUpdate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedEventJson))
            .andExpect(status().isOk());

    Event eventUpdated = eventRepository.findById(idToUpdate).orElse(null);

    Assertions.assertNotNull(eventBeforeUpdate);
    Assertions.assertNull(eventBeforeUpdate.getComment());
    Assertions.assertNull(eventBeforeUpdate.getNbStars());

    Assertions.assertNotNull(eventUpdated);
    Assertions.assertEquals("A very very very very cool comment :D", eventUpdated.getComment());
    Assertions.assertEquals(4,eventUpdated.getNbStars());

  }

}