package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.FullCalendarEvent;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ImageService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;

@RestController
@Log4j2
public class ApplicationRestController {

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private ImageService imageService;

	@GetMapping("/events")
	public Set<FullCalendarEvent> getEvents(@RequestParam Long entityId, @RequestParam String entityClass,
			@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		log.info("Get Events for {} id=" + entityId, entityClass);
		return scheduleService.getEntityShedule(entityId, entityClass, start, end);
	}

	@GetMapping("/events-range")
	public Set<FullCalendarEvent> getEventsRange(@RequestParam Long entityId, @RequestParam String entityClass,
			@RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		log.info("Get event's range for " + entityClass);

		return getEvents(entityId, entityClass, startDate, endDate);
	}

	@GetMapping("/images/{imageId}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
		log.info("Start searching image id=" + imageId);
		Optional<Image> imOptional = imageService.getImage(imageId);
		if (imOptional.isPresent()) {
			Image image = imOptional.get();
			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(imageService.getMediaType(image));
			return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);

		}
		log.info("Image not foung image id=" + imageId);

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
