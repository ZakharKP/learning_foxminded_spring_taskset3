package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

/**
 * Controller class for handling welcome page-related operations and requests.
 */
@Controller
@Log4j2
public class WelcomeController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private CourseService courseService;

	/**
	 * Handles the GET request for displaying the welcome page.
	 */
	@GetMapping("/")
	public String showWelcomePage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			User userDetails = (User) principal;

			log.info("Full user details: Username={}, Authorities={}", userDetails.getUsername(),
					userDetails.getAuthorities());
			model.addAttribute("userDetails", userDetails);
		}
		return "index";
	}

	/**
	 * Handles the GET request for displaying the account information page.
	 */
	@GetMapping("/account-information")
	public String showAccountInfo(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			User userDetails = (User) principal;

			log.info("Full user details: Username={}, Authorities={}", userDetails.getUsername(),
					userDetails.getAuthorities());
			model.addAttribute("userDetails", userDetails);
		}
		return "account-information"; // Return the name of the view template to display user information

	}

	/**
	 * Handles the GET request for displaying the list of students. This method
	 * retrieves all students from the student service and adds them to the model.
	 * The view "students_list" will be used to render the list of students.
	 *
	 * @param model The model to which the list of students will be added.
	 * @return The name of the view that will display the list of students.
	 */
	@GetMapping("/students")
	public String showStudentsList(Model model) {
		List<Student> students = studentService.getAll();
		model.addAttribute("students", students);
		model.addAttribute("caller", "All");
		return "students";
	}

	/**
	 * Handles the GET request for displaying the list of teachers. This method
	 * retrieves all teachers from the teacher service and adds them to the model.
	 * The view "teachers_list" will be used to render the list of teachers.
	 *
	 * @param model The model to which the list of teachers will be added.
	 * @return The name of the view that will display the list of teachers.
	 */
	@GetMapping("/teachers")
	public String showTeachersList(Model model) {
		List<Teacher> teachers = teacherService.getAll();
		model.addAttribute("teachers", teachers);
		model.addAttribute("caller", "All");
		return "teachers";
	}

	/**
	 * Handles the GET request for displaying the list of groups. This method
	 * retrieves all groups from the group service and adds them to the model. The
	 * view "groups_list" will be used to render the list of groups.
	 *
	 * @param model The model to which the list of groups will be added.
	 * @return The name of the view that will display the list of groups.
	 */
	@GetMapping("/groups")
	public String showGroupsList(Model model) {
		log.info("Start showing list of groups");
		List<Group> groups = groupService.getAll();
		model.addAttribute("groups", groups);
		model.addAttribute("caller", "All");
		return "groups";
	}

	/**
	 * Handles the GET request for displaying the list of courses. This method
	 * retrieves all courses from the course service and adds them to the model. The
	 * view "courses_list" will be used to render the list of courses.
	 *
	 * @param model The model to which the list of courses will be added.
	 * @return The name of the view that will display the list of courses.
	 */
	@GetMapping("/courses")
	public String showCoursesList(Model model) {
		log.info("Start showing list of courses");
		List<Course> courses = courseService.getAll();
		model.addAttribute("courses", courses);
		model.addAttribute("caller", "All");
		return "courses";
	}

	/**
	 * Handles the GET request for displaying the University Shedule. This method
	 * retrieves University entityDataMap from the ScheduleDataFactory and adds them
	 * to the model. The view "shedule" will be used to render the Shedule.
	 *
	 * @param model The model to which the entityData be added.
	 * @return The name of the view that will display the Shedule.
	 */
	@GetMapping("/schedule")
	public String showUniversityShedule(Model model) {
		log.info("Start University Full Schedule");

		model.addAttribute("entityDataMap", ScheduleDataFactory.getUniversityScheduleEntityMap());
		return "schedule";
	}

}
