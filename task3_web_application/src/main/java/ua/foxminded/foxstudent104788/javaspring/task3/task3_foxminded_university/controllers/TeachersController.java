package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

/**
 * Controller class for handling teachers-related operations and requests.
 */
@Controller
@RequestMapping("/teacher")
@Log4j2
public class TeachersController {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private AdminService adminService;

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	@GetMapping("/personal")
	public String showTeachersPersonal(@RequestParam String userName, Model model) {
		log.info("Start showing personal data of Teacher: userName=" + userName);

		Optional<Teacher> teacher = teacherService.getTeacherByUserName(userName);

		if (teacher.isPresent()) {
			model.addAttribute("teacher", teacher.get());
			return "teacher/personal";
		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/courses")
	public String showTeachersCourses(@RequestParam String userName, Model model) {
		log.info("Start showing courses of Teacher: userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);

		if (teacherOptional.isPresent()) {
			Teacher teacher = teacherOptional.get();
			model.addAttribute("courses", teacher.getCourses());
			model.addAttribute("caller", "Teacher's(" + userName + ")");
			return "courses";
		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/groups")
	public String showTeachersGroups(@RequestParam String userName, Model model) {
		log.info("Start showing groups of Teacher: userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);

		if (teacherOptional.isPresent()) {
			Teacher teacher = teacherOptional.get();
			model.addAttribute("groups", teacherService.getTeachersGroups(teacher.getId()));
			model.addAttribute("caller", "Teacher's(" + userName + ")");
			return "groups";
		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/students")
	public String showTeachersStudents(@RequestParam String userName, Model model) {
		log.info("Start showing students of Teacher: userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);

		if (teacherOptional.isPresent()) {
			Teacher teacher = teacherOptional.get();
			model.addAttribute("students", teacherService.getTeachersStudents(teacher.getId()));
			model.addAttribute("caller", "Teacher's(" + userName + ")");
			return "students";
		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/schedule")
	public String showTeachersSchedule(@RequestParam String userName, Model model) {
		log.info("Start showing Schedule of Teacher: userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);

		if (teacherOptional.isPresent()) {
			Teacher teacher = teacherOptional.get();
			model.addAttribute("entityDataMap", ScheduleDataFactory.getTeacherScheduleEntityMap(teacher));
			return "schedule";
		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/editor")
	public String showTeachersEditor(@RequestParam String userName, Model model) {
		log.info("Start showing editor of Teacher: userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);

		if (teacherOptional.isPresent()) {
			Teacher teacher = teacherOptional.get();
			model.addAttribute("teacher", teacher);
			model.addAttribute("coursesWithoutTeacher", adminService.getCoursesWithoutTeacher());
			return "teacher/editor";
		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/first_name")
	public String setTeachersFirstName(@RequestParam String userName, @RequestParam String firstName, Model model) {
		log.info("Start change firstName for Teacher userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);
		if (teacherOptional.isPresent()) {

			boolean success = teacherService.setFirstName(teacherOptional.get().getId(), firstName);

			if (success) {
				log.info("Success - firstName for Teacher userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/teacher/editor?success&message=First Name Was Changed";
			} else {
				log.info("Error can't change firstName for Teacher userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/teacher/editor?error&message=First Name Wasn't Changed";
			}

		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/last_name")
	public String setTeachersLastName(@RequestParam String userName, @RequestParam String lastName, Model model) {
		log.info("Start change lastName for Teacher userName=" + userName);

		Optional<Teacher> teacherOptional = teacherService.getTeacherByUserName(userName);
		if (teacherOptional.isPresent()) {

			boolean success = teacherService.setLastName(teacherOptional.get().getId(), lastName);

			if (success) {
				log.info("Success - lastName for Teacher userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/teacher/editor?success&message=Last Name Was Changed";
			} else {
				log.info("Error can't change lastName for Teacher userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/teacher/editor?error&message=Last Name Wasn't Changed";
			}

		} else {
			model.addAttribute("reason", "Can't find Teacher with userName=" + userName);
			return "redirect:error";
		}
	}

	/**
	 * Handles the POST request for changing teacher description.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/courses")
	public String editTeacherCourses(@RequestParam("selectedCourses") Long[] selectedCourses,
			@RequestParam("teacherId") Long teacherId, @RequestParam String action, Model model) {

		log.info("Start " + action + " courses for teacher id=" + teacherId);
		model.addAttribute("teacherId", teacherId);

		Optional<Teacher> teacherOptional = teacherService.getTeacher(teacherId);
		if (teacherOptional.isPresent()) {

			Teacher teacher = teacherOptional.get();

			String message = action + " courses to Teacher id=" + teacherId;

			boolean success = teacherService.editCoursesToSelectedTeacher(teacherId, selectedCourses, action);

			model.addAttribute("userName", teacher.getLogin().getUserName());
			if (success) {
				log.info("Success - " + message);
				return "redirect:/teacher/editor?success&message=Success: " + message;
			} else {
				log.info("Error -" + message);
				return "redirect:/teacher/editor?error&message=Error: " + message;
			}
		}
		model.addAttribute("reason", "Can't find Teacher with id=" + teacherId);
		return "redirect:error";
	}

}
