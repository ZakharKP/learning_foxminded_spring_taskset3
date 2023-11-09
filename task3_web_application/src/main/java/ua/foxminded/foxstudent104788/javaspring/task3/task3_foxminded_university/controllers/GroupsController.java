package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

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

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

/**
 * Controller class for handling groups-related operations and requests.
 */

@Controller
@RequestMapping("/group")
@Log4j2
public class GroupsController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private AdminService adminService;

	/**
	 * Handles the GET request for displaying the list of students of choosen group.
	 *
	 * @param model The model to which the list of groups will be added.
	 * @return The name of the view that will display the list of groups.
	 */
	@GetMapping("/students")
	public String showStudentsForThatGroup(@RequestParam("groupId") Long groupId, Model model) {
		log.info("looking for all students of group with id=" + groupId);
		Optional<Group> group = groupService.getGroup(groupId);
		if (group.isPresent()) {
			Set<Student> students = group.get().getStudents();
			model.addAttribute("students", students != null ? students : Collections.EMPTY_SET);
			model.addAttribute("caller", "Group(" + group.get().getGroupName() + ")");
		} else {
			model.addAttribute("students", Collections.EMPTY_SET);
		}
		return "students";
	}

	/**
	 * Handles the GET request for displaying group details.
	 */
	@GetMapping("/details")
	public String showGroupDetails(@RequestParam Long groupId, Model model) {
		log.info("Start showing details of group id=" + groupId);

		Optional<Group> group = groupService.getGroup(groupId);

		if (group.isPresent()) {
			model.addAttribute("group", group.get());
			return "group/details";
		}
		model.addAttribute("reason", "Can't find group with groupId=" + groupId);
		return "redirect:error";
	}

	@GetMapping("/schedule")
	public String showGroupsSchedule(@RequestParam Long groupId, Model model) {
		log.info("Start showing schedule of Group: id=" + groupId);

		Optional<Group> groupOptional = groupService.getGroup(groupId);

		if (groupOptional.isPresent()) {
			Group group = groupOptional.get();
			model.addAttribute("entityDataMap", ScheduleDataFactory.getGroupScheduleEntityMap(group));
			return "schedule";
		} else {
			model.addAttribute("reason", "Can't find Group with id=" + groupId);
			return "redirect:/error";
		}
	}

	/**
	 * Handles the GET request for displaying the group editor.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/editor")
	public String showGroupEdit(@RequestParam("groupId") Long groupId, Model model) {
		log.info("Start showing editor of group id=" + groupId);

		Optional<Group> group = groupService.getGroup(groupId);

		if (group.isPresent()) {
			model.addAttribute("group", group.get());
			model.addAttribute("studentsWithoutGroup", adminService.getStudentsWithoutGroup());
			return "group/editor";
		}
		model.addAttribute("reason", "Can't find group with groupId=" + groupId);
		return "redirect:error";
	}

	/**
	 * Handles the POST request for changing group name.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/name")
	public String setNewName(@RequestParam("groupName") String groupName, @RequestParam("groupId") Long groupId,
			Model model) {
		log.info("Start change name for group id=" + groupId);

		boolean success = groupService.setNewGroupName(groupId, groupName);
		model.addAttribute("groupId", groupId);

		if (success) {
			log.info("Success - changed name for group id=" + groupId);
			return "redirect:/group/editor?success&message=Group name was changed";
		} else {
			log.info("Error - can't change name for group id=" + groupId);
			return "redirect:/group/editor?error&message=Can't change group name - probably isn't unique";
		}
	}

	/**
	 * Handles the POST request for changing group description.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/editor/students")
	public String editGroupStudents(@RequestParam("selectedStudents") Long[] selectedStudents,
			@RequestParam("groupId") Long groupId, @RequestParam String action, Model model) {
		log.info("Start " + action + " students for group id=" + groupId);

		String message = action + " students to group id=" + groupId;

		boolean success = groupService.editStudentsForThatGroup(groupId, selectedStudents, action);
		model.addAttribute("groupId", groupId);
		if (success) {
			log.info("Success - " + message + " id=" + groupId);
			return "redirect:/group/editor?success&message=Success: " + message;
		} else {
			log.info("Error -" + message + " id=" + groupId);
			return "redirect:/group/editor?error&message=Error: " + message;
		}

	}

	/**
	 * Handles the GET request for showing the group creation page.
	 */

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/creation")
	public String showGroupCreation(Model model) {
		log.info("get data for new group");

		model.addAttribute("form", new RegistrationForm());
		model.addAttribute("studentsWithoutGroup", adminService.getStudentsWithoutGroup());

		return "group/creation";
	}

	/**
	 * Handles the POST request for creating a new group.
	 */

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/creation")
	public String createNewGroup(@ModelAttribute("form") RegistrationForm form, BindingResult result, Model model) {
		log.info("New Group creation");

		if (result.hasErrors()) {
			return "redirect:/group/creation-report?error";
		}

		boolean success = adminService.createNewGroup(form);

		model.addAttribute("groupName", form.getGroupName());
		model.addAttribute("studentsAmount", form.getStudentsIds() != null ? form.getStudentsIds().length : 0);

		if (success) {

			return "redirect:/group/creation-report?success";
		} else {
			return "redirect:/group/creation-report?error";
		}
	}

	/**
	 * Handles the GET request for showing the group creation report page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/creation-report")
	public String creationReport(Model model) {
		log.info("Show creation report");

		return "group/creation-report";
	}

	/**
	 * Handles the GET request for showing the delete group confirmation page.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/delete")
	public String showDeleteConfirm(@RequestParam("groupId") Long groupId, Model model) {
		log.info("Show delete confitmation");

		Optional<Group> group = groupService.getGroup(groupId);

		if (group.isPresent()) {
			model.addAttribute("group", group.get());
			return "group/delete";
		}
		model.addAttribute("reason", "Can't find group with groupId=" + groupId);
		return "redirect:error";
	}

	/**
	 * Handles the POST request for deleting a group.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/del-group")
	public String deleteGroup(@RequestParam("groupId") Long groupId, Model model) {
		Optional<Group> group = groupService.getGroup(groupId);

		if (group.isPresent()) {
			groupService.deleteGroup(group.get());
			return "redirect:/groups";
		}

		model.addAttribute("reason", "Can't find group with groupId=" + groupId);
		return "redirect:error";
	}

}
