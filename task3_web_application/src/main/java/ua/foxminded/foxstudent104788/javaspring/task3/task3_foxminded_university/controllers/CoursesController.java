package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

/**
 * Controller class for handling courses-related operations and requests.
 */
@Controller
@RequestMapping("/course")
@Log4j2
public class CoursesController {

	@Autowired
	private CourseService courseService;

	@Autowired
	private AdminService adminService;

	/**
	 * Handles the GET request for displaying course details.
	 */
	@GetMapping("/details")
	public String showCourseDetails(@RequestParam Long courseId, Model model) {
		log.info("Start showing details of course id=" + courseId);
		Optional<Course> course = courseService.getCourse(courseId);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "course/details";
		}
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}

	@GetMapping("/schedule")
	public String showCoursesSchedule(@RequestParam Long courseId, Model model) {
		log.info("Start showing schedule of Course: id=" + courseId);

		Optional<Course> courseOptional = courseService.getCourse(courseId);

		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();
			model.addAttribute("entityDataMap", ScheduleDataFactory.getCourseScheduleEntityMap(course));
			return "schedule";
		} else {
			model.addAttribute("reason", "Can't find Course with id=" + courseId);
			return "redirect:error";
		}
	}

	/**
	 * Handles the GET request for displaying the course editor.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/editor")
	public String showCourseEdit(@RequestParam Long courseId, Model model) {
		log.info("Start showing editor of course id=" + courseId);
		Optional<Course> course = courseService.getCourse(courseId);
		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			model.addAttribute("teachers", adminService.getAllTeachers());
			model.addAttribute("allowedImageCountArray", courseService.getAllowedImageCountArray(courseId));
			return "course/editor";
		}
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}

	/**
	 * Handles the POST request for changing course name.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/name")
	public String setNewName(@RequestParam("courseName") String courseName, @RequestParam("courseId") Long courseId,
			Model model) {
		log.info("Start change name for course id=" + courseId);

		boolean success = false;

		if (courseService.isCourseNameUnique(courseName)) {

			Course course = courseService.setNewCourseName(courseId, courseName);
			success = course != null;
		}

		model.addAttribute("courseId", courseId);
		if (success) {
			log.info("Success - changed name for course id=" + courseId);
			return "redirect:/course/editor?success&message=Course name was changed";
		} else {
			log.info("Error -can't change name for course id=" + courseId);
			return "redirect:/course/editor/?error&message=Can't change course name - probably isn't unique";
		}
	}

	/**
	 * Handles the POST request for changing course description.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/description")
	public String setNewDescription(@RequestParam("courseDescription") String courseDescription,
			@RequestParam("courseId") Long courseId, Model model) {
		log.info("Start change description for course id=" + courseId);

		boolean success = false;

		Course course = courseService.setNewCourseDescription(courseId, courseDescription);
		success = course != null;

		model.addAttribute("courseId", courseId);
		if (success) {
			log.info("Success - changed description for course id=" + courseId);
			return "redirect:/course/editor?success&message=Course description was changed";
		} else {
			log.info("Error -can't change description for course id=" + courseId);
			return "redirect:/course/editor?error&message=Can't change course description";
		}
	}

	/**
	 * Handles the POST request for changing course teacher.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/teacher")
	public String setNewTeacher(@RequestParam("teacherId") Long teacherId, @RequestParam("courseId") Long courseId,
			Model model) {
		log.info("Start change description for course id=" + courseId);

		boolean success = false;

		Course course = courseService.setNewTeacherToCourse(courseId, teacherId);

		success = course != null;

		model.addAttribute("courseId", courseId);
		if (success) {
			log.info("Success - changed description for course id=" + courseId);
			return "redirect:/course/editor?success&message=Course Teacher was changed";
		} else {
			log.info("Error -can't change description for course id=" + courseId);
			return "redirect:/course/editor?error&message=Can't change teacher";
		}
	}

	/**
	 * Handles the GET request for showing the course creation page.
	 */

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/creation")
	public String showCourseCreation(Model model) {
		log.info("get data for new coure");

		model.addAttribute("form", new RegistrationForm());
		model.addAttribute("teachers", adminService.getAllTeachers());

		return "course/creation";
	}

	/**
	 * Handles the POST request for creating a new course.
	 */

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/creation")
	public String createNewCourse(@ModelAttribute("form") RegistrationForm form, BindingResult result, Model model) {
		log.info("Start create new course " + form.getCourseName());

		if (result.hasErrors()) {
			return "redirect:/course/create-report?error";
		}

		boolean success = false;

		Course course = adminService.createNewCourse(form);

		success = course != null;

		model.addAttribute("courseName", form.getCourseName());
		model.addAttribute("courseDescription", form.getCourseDescription());

		if (success) {
			return "redirect:/course/create-report?success";
		} else {
			return "redirect:/course/create-report?error";
		}
	}

	/**
	 * Handles the GET request for showing the course creation report page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/create-report")
	public String createReport(Model model) {
		log.info("Show creation report");

		return "course/create-report";
	}

	/**
	 * Handles the GET request for showing the delete course confirmation page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/delete-course")
	public String showDeleteConfirm(@RequestParam("courseId") Long courseId, Model model) {
		log.info("Show delete confirmation for course id=" + courseId);

		Optional<Course> course = courseService.getCourse(courseId);

		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "course/delete-course";
		}
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}

	/**
	 * Handles the POST request for deleting a course.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/del-course")
	public String deleteCourse(@RequestParam("courseId") Long courseId) {
		log.info("Deleting course id=" + courseId);

		Optional<Course> course = courseService.getCourse(courseId);

		if (course.isPresent()) {
			courseService.deleteCourse(course.get());
		}
		return "redirect:/courses";
	}

	@GetMapping("/intro")
	public String showIntro(@RequestParam Long courseId, Model model) {
		log.info("Show intro page for course id=" + courseId);

		Optional<Course> course = courseService.getCourse(courseId);

		if (course.isPresent()) {
			model.addAttribute("course", course.get());
			return "course/intro";
		}
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/intro/images/remove")
	public String removeIntroImages(@RequestParam Long courseId, @RequestParam List<Long> selectedImages, Model model) {
		log.info("Remove selected Images from course id=" + courseId);

		Optional<Course> courseOptional = courseService.getCourse(courseId);

		if (courseOptional.isPresent()) {
			model.addAttribute("courseId", courseId);
			boolean success = courseService.removeImages(courseId, selectedImages);
			if (success) {
				log.info("Selected images was removed from course id=" + courseId);
				return "redirect:/course/editor?success&message=Images was removed";
			} else {
				log.info("Error - can't remove images from course id=" + courseId);
				return "redirect:/course/editor?error&message=Can't remove images";
			}

		}
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/intro/images/add")
	public String addIntroImages(@RequestParam Long courseId, @RequestParam List<MultipartFile> selectedImages,
			Model model) {
		log.info("Add selected Images for course id=" + courseId);

		Optional<Course> courseOptional = courseService.getCourse(courseId);

		if (courseOptional.isPresent()) {
			model.addAttribute("courseId", courseId);
			boolean success = courseService.addImages(courseId, selectedImages);
			if (success) {
				log.info("Selected images was added to course id=" + courseId);
				return "redirect:/course/editor?success&message=Images to intro was added";
			} else {
				log.info("Error - can't remove images from course id=" + courseId);
				return "redirect:/course/editor?error&message=Can't add images";
			}

		}
		log.error("Can't find course with courseId=" + courseId);
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/intro/text")
	public String editIntroText(@RequestParam Long courseId, @RequestParam String introText, Model model) {
		log.info("Edit Intro text for course id=" + courseId);
		log.info(introText);

		Optional<Course> courseOptional = courseService.getCourse(courseId);

		if (courseOptional.isPresent()) {
			model.addAttribute("courseId", courseId);
			boolean success = courseService.setIntroText(courseId, introText);
			if (success) {
				log.info("Intro text was changed to course id=" + courseId);
				return "redirect:/course/editor?success&message=Text to intro was changed";
			} else {
				log.info("Error - can't change intro text for course id=" + courseId);
				return "redirect:/course/editor?error&message=Can't change intro text";
			}

		}
		log.error("Can't find course with courseId=" + courseId);
		model.addAttribute("reason", "Can't find course with courseId=" + courseId);
		return "redirect:error";
	}
}
