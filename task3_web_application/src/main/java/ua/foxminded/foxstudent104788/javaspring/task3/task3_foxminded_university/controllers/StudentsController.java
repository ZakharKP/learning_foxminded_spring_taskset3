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
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

/**
 * Controller class for handling students-related operations and requests.
 */
@Controller
@RequestMapping("/student")
@Log4j2
public class StudentsController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private AdminService adminService;

	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	@GetMapping("/personal")
	public String showStudentsPersonal(@RequestParam String userName, Model model) {
		log.info("Start showing personal data of Student: userName=" + userName);
		Optional<Student> student = studentService.getStudentByUserName(userName);
		if (student.isPresent()) {
			model.addAttribute("student", student.get());
			return "student/personal";
		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}

	}

	@GetMapping("/courses")
	public String showStudentsCourses(@RequestParam String userName, Model model) {
		log.info("Start showing courses of Student: userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);

		if (studentOptional.isPresent()) {
			Student student = studentOptional.get();
			model.addAttribute("courses", studentService.getStudentsCourses(student.getId()));
			model.addAttribute("caller", "Student's(" + userName + ")");
			return "courses";

		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/group")
	public String showStudentsGroup(@RequestParam String userName, Model model) {
		log.info("Start showing groups of Student: userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);

		if (studentOptional.isPresent()) {
			Student student = studentOptional.get();
			model.addAttribute("group", student.getGroup());
			return "group/details";
		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/teachers")
	public String showStudentsTeachers(@RequestParam String userName, Model model) {
		log.info("Start showing teachers of Student: userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);

		if (studentOptional.isPresent()) {
			Student student = studentOptional.get();
			model.addAttribute("teachers", studentService.getStudentsTeachers(student.getId()));
			model.addAttribute("caller", "Student's(" + userName + ")");
			return "teachers";
		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/classmates")
	public String showStudentsClassmates(@RequestParam String userName, Model model) {
		log.info("Start showing classmates of Student: userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);

		if (studentOptional.isPresent()) {
			Student student = studentOptional.get();
			model.addAttribute("caller", "Classmates(" + userName + ")");
			model.addAttribute("students", student.getGroup().getStudents());
			return "students";
		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}
	}

	@GetMapping("/schedule")
	public String showStudentsSchedule(@RequestParam String userName, Model model) {
		log.info("Start showing schedule of Student: userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);

		if (studentOptional.isPresent()) {
			Student student = studentOptional.get();
			model.addAttribute("entityDataMap", ScheduleDataFactory.getStudentScheduleEntityMap(student));
			return "schedule";
		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/editor")
	public String showStudentsEditor(@RequestParam String userName, Model model) {
		log.info("Start showing editor of Student: userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);

		if (studentOptional.isPresent()) {
			Student student = studentOptional.get();
			model.addAttribute("student", student);
			model.addAttribute("groups", adminService.getAllGroups());
			return "student/editor";
		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/first_name")
	public String setStudentsFirstName(@RequestParam String userName, @RequestParam String firstName, Model model) {
		log.info("Start change firstName for Student userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);
		if (studentOptional.isPresent()) {

			boolean success = studentService.setFirstName(studentOptional.get().getId(), firstName);

			if (success) {
				log.info("Success - firstName for Student userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/student/editor?success&message=First Name Was Changed";
			} else {
				log.info("Error can't change firstName for Student userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/student/editor?error&message=First Name Wasn't Changed";
			}

		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/last_name")
	public String setStudentsLastName(@RequestParam String userName, @RequestParam String lastName, Model model) {
		log.info("Start change lastName for Student userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);
		if (studentOptional.isPresent()) {

			boolean success = studentService.setLastName(studentOptional.get().getId(), lastName);

			if (success) {
				log.info("Success - lastName for Student userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/student/editor?success&message=Last Name Was Changed";
			} else {
				log.info("Error can't change lastName for Student userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/student/editor?error&message=Last Name Wasn't Changed";
			}

		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/group")
	public String setStudentsGroup(@RequestParam String userName, @RequestParam Long groupId, Model model) {
		log.info("Start change group for Student userName=" + userName);

		Optional<Student> studentOptional = studentService.getStudentByUserName(userName);
		if (studentOptional.isPresent()) {

			boolean success = studentService.setGroupToStudent(studentOptional.get().getId(), groupId);

			if (success) {
				log.info("Success - group for Student userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/student/editor?success&message=Group Was Changed";
			} else {
				log.info("Error can't change group for Student userName=" + userName);
				model.addAttribute("userName", userName);
				return "redirect:/student/editor?error&message=Group Wasn't Changed";
			}

		} else {
			model.addAttribute("reason", "Can't find Student with userName=" + userName);
			return "redirect:error";
		}

	}
}
