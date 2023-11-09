package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

/**
 * Controller class for handling lectures-related operations and requests.
 */
@Controller
@RequestMapping("/lecture")
@Log4j2
public class LecturesController {

	private static final String CANT_FIND_LECTURE_WITH_ID = "Can't find Lecture with id=";

	private static final String REASON = "reason";

	private static final String REDIRECT_ERROR = "redirect:/error";

	@Autowired
	private LectureService lectureService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ScheduleService scheduleService;

	private static final String REDIRECT_LECTURE_EDITOR_ERROR = "redirect:/lecture/editor?error";

	private static final String REDIRECT_LECTURE_EDITOR_SUCCESS = "redirect:/lecture/editor?success";

	private static final String MESSAGE = "message";

	private static final String LECTURE_ID = "lectureId";

	/**
	 * Handles the GET request for displaying the lecture details. This method
	 * retrieves lectures from the lecture service and adds it to the model. also
	 * retrieves lecture's student's amount from the lecture service and adds it to
	 * the model.
	 *
	 * @param model     The model to which the list of lectures will be added.
	 * @param lectureId The ID of lectures wich will be displaying.
	 * @return The name of the view that will display the list of lectures.
	 */
	@GetMapping("/details")
	public String showLectureDetails(@RequestParam(LECTURE_ID) Long lectureId, Model model) {
		log.info("Start show Details for Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {
			model.addAttribute("lecture", leOptional.get());
			model.addAttribute("studentAmount", lectureService.getStudentsAmount(lectureId));
			return "lecture/details";
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}

	}

	/**
	 * Handles the GET request for displaying the lecture's students. This method
	 * retrieves lectures's students list from the lecture service and adds it to
	 * the model.
	 *
	 * @param model     The model to which the list of lectures will be added.
	 * @param lectureId The ID of lectures which will be displaying.
	 * @return The name of the view that will display the list of lectures.
	 */
	@GetMapping("/students")
	public String showLecturesStudentsList(@RequestParam(LECTURE_ID) Long lectureId, Model model) {
		log.info("Start show Students for Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {
			model.addAttribute("students", lectureService.getLectureStudents(lectureId));
			model.addAttribute("caller", "Lecture id=" + lectureId);

			return "students";

		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}

	}

	/**
	 * Handles the GET request for displaying the lecture's editor page. This method
	 * retrieves lectures from the lecture service and adds it to the model. also.
	 * This method retrieves students list from the course service and adds it to
	 * the model. also. This method retrieves groups list from the group service and
	 * adds it to the model. also.
	 *
	 * @param model     The model to which the view data will be added.
	 * @param lectureId The ID of lectures which will be displaying.
	 *
	 * @return The name of the view that will display the list of lectures.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/editor")
	public String showLectureEditor(@RequestParam(LECTURE_ID) Long lectureId, Model model) {
		log.info("Start show Students for Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {
			Lecture lecture = leOptional.get();

			int groupsCanAddAmount = adminService.getAmountGroupsCanAddToLecture(lecture);

			model.addAttribute("lecture", lecture);
			model.addAttribute("courses", adminService.getAllCourses());
			model.addAttribute("allowedTime", ScheduleDataFactory.getAllowedTime());
			model.addAttribute("groups", adminService.getAllGroupsCanAddToLecture(lecture));
			model.addAttribute("maxGroupsCanAdd", groupsCanAddAmount);
			return "lecture/editor";
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}
	}

	/**
	 * Handles the POST request for editing the lecture's start Date and Time. This
	 * method sets a new Start date time and gets an edit message answer from the
	 * lecture service, adding it to the model.
	 *
	 * @param model     The model to which the view data will be added.
	 * @param lectureId The ID of the lecture that will be displayed.
	 * @param newDate   The new date for the lecture, formatted as ISO DATE.
	 * @param newTime   The new time for the lecture, formatted as ISO TIME.
	 * @return The name of the view that will display the list of lectures.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/date_time")
	public String edtitDateTime(@RequestParam(LECTURE_ID) Long lectureId,
			@RequestParam("newDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate,
			@RequestParam("newTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime newTime, Model model) {
		log.info("Start set new start time for Lecture id=" + lectureId);

		LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);
		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {

			boolean success = scheduleService.setLectureNewDateTime(lectureId, newDateTime);

			model.addAttribute(LECTURE_ID, lectureId);

			if (success) {
				log.info("Seted new time for Lecture id=" + lectureId);

				model.addAttribute(MESSAGE, "Date and Time was changed");

				return REDIRECT_LECTURE_EDITOR_SUCCESS;
			} else {
				List<String> conflictReport = scheduleService.getScheduleEditConflictReport(lectureId, newDateTime);

				log.info("Can't set new time for Lecture id=" + lectureId + String.join("||", conflictReport));

				model.addAttribute(MESSAGE, conflictReport);
				return REDIRECT_LECTURE_EDITOR_ERROR;
			}
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}
	}

	/**
	 * Handles the POST request for editing the lecture's course. This method sets a
	 * new Course and gets an edit message answer from the lecture service, adding
	 * it to the model.
	 *
	 * @param model     The model to which the view data will be added.
	 * @param lectureId The ID of the lecture that will be displayed.
	 * @param courseId  The new Course ID
	 * @return The name of the view that will display the list of lectures.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/course")
	public String editCourse(@RequestParam(LECTURE_ID) Long lectureId, @RequestParam Long courseId, Model model) {
		log.info("Start set new course for Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {

			boolean success = scheduleService.setLectureNewCourse(lectureId, courseId);

			model.addAttribute(LECTURE_ID, lectureId);

			if (success) {
				log.info("Seted new Course for Lecture id=" + lectureId);

				model.addAttribute(MESSAGE, "Course was Changed");

				return REDIRECT_LECTURE_EDITOR_SUCCESS;

			} else {
				List<String> conflictReport = scheduleService.getScheduleEditConflictReport(lectureId, courseId);

				log.info("Can't set new course for Lecture id=" + lectureId + String.join("||", conflictReport));
				model.addAttribute(MESSAGE, conflictReport);
				return REDIRECT_LECTURE_EDITOR_ERROR;
			}
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}
	}

	/**
	 * Handles the POST request for editing the lecture's groups. This method remove
	 * a groups from Lecture.
	 *
	 * @param model          The model to which the view data will be added.
	 * @param lectureId      The ID of the lecture that will be displayed.
	 * @param selectedGroups List represent group's IDs selected groups to remove
	 * @return The name of the view that will display the list of lectures.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/groups/remove")
	public String removeGroupsFromLecture(@RequestParam Long lectureId, @RequestParam List<Long> selectedGroups,
			Model model) {
		log.info("Start remove groups from Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {

			boolean success = lectureService.removeGroupsFromLecture(lectureId, selectedGroups);

			model.addAttribute(LECTURE_ID, lectureId);

			if (success) {
				log.info("Removed groups from Lecture id=" + lectureId);

				model.addAttribute(MESSAGE, "Groups was removed");
				return REDIRECT_LECTURE_EDITOR_SUCCESS;
			} else {
				log.info("Can't remowe groups from Lecture id=" + lectureId);

				model.addAttribute(MESSAGE, "Groups can't be removed - unknown error");
				return REDIRECT_LECTURE_EDITOR_ERROR;
			}
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}
	}

	/**
	 * Handles the POST request for editing the lecture's groups. This method add a
	 * groups to the Lecture.
	 *
	 * @param model          The model to which the view data will be added.
	 * @param lectureId      The ID of the lecture that will be displayed.
	 * @param selectedGroups List represent group's IDs selected groups to add
	 * @return The name of the view that will display the list of lectures.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/groups/add")
	public String addGroupsToLecture(@RequestParam Long lectureId, @RequestParam List<Long> selectedGroups,
			Model model) {
		log.info("Start add groups from Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {

			boolean success = scheduleService.addGroupsToLecture(lectureId, selectedGroups);

			model.addAttribute(LECTURE_ID, lectureId);

			if (success) {
				log.info("Added groups to Lecture id=" + lectureId);

				model.addAttribute(MESSAGE, "Groups was added");

				return REDIRECT_LECTURE_EDITOR_SUCCESS;

			} else {
				List<String> conflictReport = scheduleService.getScheduleEditConflictReport(lectureId, selectedGroups);

				log.info("Can't add groups from Lecture id=" + lectureId + String.join("||", conflictReport));

				model.addAttribute(MESSAGE, conflictReport);
				return REDIRECT_LECTURE_EDITOR_ERROR;
			}
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}
	}

	/**
	 * Handles the GET request for displaying the lecture's creation page.
	 *
	 * @param model The model to which the view data will be added.
	 *
	 * @return The name of the view that will display the lectures creation page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/creation")
	public String showCreation(Model model) {
		log.info("Start show lecture cretion page");

		model.addAttribute("form", new RegistrationForm());
		model.addAttribute("maxNewGroups", Constants.MAX_ALLOWED_AMOUNT_GROUPS_FOR_LECTURE);
		model.addAttribute("courses", adminService.getAllCourses());
		model.addAttribute("groups", adminService.getAllGroups());
		model.addAttribute("allowedTime", ScheduleDataFactory.getAllowedTime());
		return "lecture/creation";
	}

	/**
	 * Handles the POST request for creating new lecture. This method create new
	 * lecture with lectureService.
	 *
	 * @param model   The model to which the view data will be added.
	 * @param newDate The new date for the lecture, formatted as ISO DATE.
	 * @param newTime The new time for the lecture, formatted as ISO TIME.
	 * @param form    The RegistrationForm contains data for lecture creating.
	 * @return The name of the view that will display the lectures creation page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/create")
	public String addNewLecture(@ModelAttribute("form") RegistrationForm form,
			@RequestParam("newDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate,
			@RequestParam("newTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime newTime, Model model) {
		log.info("Creating new Lecture");

		LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);

		Lecture lecture = adminService.createNewLecture(form, newDateTime);
		if (lecture != null) {

			log.info("Created new Lecture id=" + lecture.getId());

			model.addAttribute(LECTURE_ID, lecture.getId());
			return "redirect:/lecture/details";
		} else {
			List<String> conflictReport = scheduleService.getScheduleCreateConflictReport(newDateTime,
					form.getCourseId(), form.getGroupsIds());

			log.info("Can't create Lecture" + String.join("||", conflictReport));
			model.addAttribute(MESSAGE, conflictReport);

			return "redirect:/lecture/creation?error";
		}

	}

	/**
	 * Handles the GET request for displaying the lecture's delete confirmation
	 * page.
	 *
	 * @param model     The model to which the view data will be added.
	 * @param lectureId The id of lecture to delete.
	 *
	 * @return The name of the view that will display the lectures creation page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/delete")
	public String showDeleteConfirmation(@RequestParam Long lectureId, Model model) {
		log.info("Start show Delete confirmation page for Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {
			model.addAttribute("lecture", leOptional.get());
			return "lecture/delete";
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}

	}

	/**
	 * Handles the POST request for the lecture's delete.
	 *
	 * @param lectureId The id of lecture to delete.
	 *
	 * @return The name of the view that will display the lectures creation page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/del")
	public String delLecture(@RequestParam Long lectureId, Model model) {
		log.info("Start show Delete confirmation page for Lecture id=" + lectureId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);

		if (leOptional.isPresent()) {
			lectureService.deleteLecture(leOptional.get());
			return "redirect:/schedule";
		} else {
			log.error(CANT_FIND_LECTURE_WITH_ID + lectureId);
			model.addAttribute(REASON, CANT_FIND_LECTURE_WITH_ID + lectureId);
			return REDIRECT_ERROR;
		}

	}
}
