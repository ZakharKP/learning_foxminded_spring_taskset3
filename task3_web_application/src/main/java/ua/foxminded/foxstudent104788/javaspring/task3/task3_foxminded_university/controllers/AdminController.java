package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;

/**
 * Controller class for handling administrative operations.
 */
@Controller
@RequestMapping("/admin")
@Log4j2
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private LoginDataService loginDataService;

	/**
	 * Handles the GET request for displaying account information for any user
	 * (admin only).
	 */
	@GetMapping("/user-account-information")
	public String showAccountInfoAny(@RequestParam String userName, Model model) {
		// Fetch user details based on the provided username
		UserDetails userDetails = loginDataService.loadUserByUsername(userName);

		log.info("Full user details: Username={}, Authorities={}", userDetails.getUsername(),
				userDetails.getAuthorities());
		model.addAttribute("userDetails", userDetails);
		return "account-information"; // Return the name of the view template to display user information

	}

	/**
	 * Displays the page where you can add admin role to teachers ant take it from.
	 */
	@GetMapping("/editor")
	public String showManageAdmin(Model model) {
		model.addAttribute("teachers", adminService.getAllTeachers());
		model.addAttribute("selectedTeachers", new ArrayList<Long>());

		return "admin/editor";
	}

	/**
	 * Handles the admin management action.
	 */
	@PostMapping("/editor-post")
	public String manageAdmin(@RequestParam("selectedTeachers") List<Long> selectedTeacherIds,
			@RequestParam String action) {

		if ("add".equals(action)) {
			adminService.setAdminRoleToSelectedTeachers(selectedTeacherIds);
		} else if ("remove".equals(action)) {
			adminService.removeAdminRoleFromSelectedTeachers(selectedTeacherIds);
		}

		return "redirect:/admin/editor";
	}

	/**
	 * Displays the page to add a new student.
	 */
	@GetMapping("/signup/student")
	public String showAddNewStudent(Model model) {
		model.addAttribute("form", new RegistrationForm());
		model.addAttribute("groups", adminService.getAllGroups());
		return "admin/signup/student";
	}

	/**
	 * Displays the page to add a new teacher.
	 */
	@GetMapping("/signup/teacher")
	public String showAddNewTeacher(Model model) {
		model.addAttribute("form", new RegistrationForm());
		model.addAttribute("coursesWithoutTeacher", adminService.getCoursesWithoutTeacher());
		return "admin/signup/teacher";
	}

	/**
	 * Handles user registration.
	 */
	@PostMapping("/signup")
	public String signUpUser(@ModelAttribute("form") RegistrationForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "redirect:/admin/signup/report?error";
		}

		boolean success = adminService.registerNewUser(form);

		model.addAttribute("roleName", form.getRoleName());
		model.addAttribute("firstName", form.getFirstName());
		model.addAttribute("lastName", form.getLastName());
		model.addAttribute("userName", form.getUserName());

		if (success) {

			return "redirect:/admin/signup/report?success";
		} else {
			return "redirect:/admin/signup/report?error";
		}
	}

	/**
	 * Displays the user registration report page.
	 */
	@GetMapping("/signup/report")
	public String registrationReport(Model model) {
		return "admin/signup/report";
	}

	/**
	 * Displays the confirmation page for user deletion.
	 */
	@GetMapping("/delete-user")
	public String deleteConfirmation(@RequestParam("userName") String userName, Model model) {
		LoginData login = adminService.getUser(userName);
		if (login != null) {
			if (login.getStudent() != null) {
				model.addAttribute("user", login.getStudent());
			} else if (login.getTeacher() != null) {
				model.addAttribute("user", login.getTeacher());
			}
			return "admin/delete-user";
		}

		log.info("Can't find user UserName=" + userName);
		model.addAttribute("reason", "Can't fing user with userName=" + userName);
		return "redirect:/error";
	}

	/**
	 * Handles user deletion.
	 */
	@PostMapping("/del-user")
	public String deleteUser(@RequestParam("userName") String userName) {
		adminService.deleteUser(userName);

		return "redirect:/";
	}

	@GetMapping("/password-editor")
	public String showEditUserPassword(@RequestParam String userName, Model model) {
		log.info("Show user-editor for user UserName=" + userName);
		LoginData user = adminService.getUser(userName);

		if (user != null) {
			model.addAttribute("user", user);
			return "admin/password-editor";
		} else {
			log.info("Can't find user UserName=" + userName);
			model.addAttribute("reason", "Can't fing user with userName=" + userName);
			return "redirect:/error";
		}

	}

	@PostMapping("/password-editor")
	public String editPassword(@RequestParam String userName, @RequestParam String userDetail, Model model) {
		log.info("Start change password for user UserName=" + userName);

		boolean success = adminService.changePassword(userName, userDetail);
		String message = null;

		model.addAttribute("userName", userName);
		if (success) {
			message = "Password succesfully changed";
			return "redirect:/admin/password-editor?success&message=" + message;
		} else {
			message = "Password can't be change";
			return "redirect:/admin/password-editor?error&message=" + message;
		}

	}

}
