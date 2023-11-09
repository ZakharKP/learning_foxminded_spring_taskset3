package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.FullCalendarEvent;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ImageService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;

@WebMvcTest(ApplicationRestController.class)
class ApplicationRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ScheduleService scheduleService;

	@MockBean
	private ImageService imageService;

	@WithMockUser(roles = "STUDENT")
	@Test
	void testGetEvents() throws Exception {
		Set<FullCalendarEvent> events = ForTestsDataCreator.getEvents();
		when(scheduleService.getEntityShedule(anyLong(), anyString(), any(LocalDateTime.class),
				any(LocalDateTime.class))).thenReturn(events);

		mvc.perform(get("/events").with(csrf()).param("entityId", "1").param("entityClass", "TEST")
				.param("start", "2023-09-01T00:00:00").param("end", "2023-09-10T00:00:00")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		verify(scheduleService).getEntityShedule(1L, "TEST", LocalDateTime.of(2023, 9, 1, 0, 0, 0),
				LocalDateTime.of(2023, 9, 10, 0, 0, 0));

	}

	@WithMockUser(roles = "STUDENT")
	@Test
	void testGetEventsRange() throws Exception {
		Set<FullCalendarEvent> events = ForTestsDataCreator.getEvents();
		when(scheduleService.getEntityShedule(anyLong(), anyString(), any(LocalDateTime.class),
				any(LocalDateTime.class))).thenReturn(events);

		mvc.perform(get("/events-range").with(csrf()).param("entityId", "1").param("entityClass", "TEST")
				.param("startDate", "2023-09-01T00:00:00").param("endDate", "2023-09-10T00:00:00")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		verify(scheduleService).getEntityShedule(1L, "TEST", LocalDateTime.of(2023, 9, 1, 0, 0, 0),
				LocalDateTime.of(2023, 9, 10, 0, 0, 0));

	}

	@WithMockUser(roles = "STUDENT")
	@Test
	void testGetImage() throws Exception {

		Image image = ForTestsDataCreator.getImages().get(0);

		when(imageService.getImage(anyLong())).thenReturn(Optional.of(image));

		mvc.perform(get("/images/1").with(csrf()).param("imageId", "1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		verify(imageService).getImage(1L);

	}

}
