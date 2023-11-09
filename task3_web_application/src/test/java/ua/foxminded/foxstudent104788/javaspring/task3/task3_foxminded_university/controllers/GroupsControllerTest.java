package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@WebMvcTest(GroupsController.class)
class GroupsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GroupService groupService;

	@MockBean
	private AdminService adminService;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsForThatGroup() throws Exception {

		Long groupId = 1L;

		Group group = ForTestsDataCreator.getStudents().get(0).getGroup();
		Set<Student> students = group.getStudents();

		when(groupService.getGroup(anyLong())).thenReturn(Optional.of(group));

		mvc.perform(get("/group/students").with(csrf()).param("groupId", groupId.toString())).andExpect(status().isOk())
				.andExpect(view().name("students")).andExpect(model().attribute("students", students));

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowGroupDetails() throws Exception {
		Long groupId = 1L;

		Group group = ForTestsDataCreator.getGroups().get(0);

		when(groupService.getGroup(anyLong())).thenReturn(Optional.of(group));
		mvc.perform(get("/group/details").with(csrf()).param("groupId", groupId.toString())).andExpect(status().isOk())
				.andExpect(view().name("group/details")).andExpect(model().attribute("group", group));

		verify(groupService).getGroup(anyLong());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowGroupsSchedule() throws Exception {
		Group group = ForTestsDataCreator.getGroups().get(0);

		try (MockedStatic<ScheduleDataFactory> mocked = mockStatic(ScheduleDataFactory.class)) {
			mocked.when(() -> ScheduleDataFactory.getGroupScheduleEntityMap(group))
					.thenReturn(ForTestsDataCreator.getScheduleEntityMap());

			when(groupService.getGroup(anyLong())).thenReturn(Optional.of(group));

			mvc.perform(get("/group/schedule").with(csrf()).param("groupId", group.getId().toString()))
					.andExpect(status().isOk()).andExpect(view().name("schedule"))
					.andExpect(model().attribute("entityDataMap", ForTestsDataCreator.getScheduleEntityMap()));

			verify(groupService).getGroup(anyLong());
		}
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowGroupEdit() throws Exception {
		Group group = ForTestsDataCreator.getGroups().get(0);
		List<Student> studentsWithoutGroups = ForTestsDataCreator.getNewStudents();
		Long groupId = 1L;

		when(groupService.getGroup(anyLong())).thenReturn(Optional.of(group));
		when(adminService.getStudentsWithoutGroup()).thenReturn(studentsWithoutGroups);
		mvc.perform(get("/group/editor").with(csrf()).param("groupId", groupId.toString())).andExpect(status().isOk())
				.andExpect(view().name("group/editor")).andExpect(model().attribute("group", group))
				.andExpect(model().attribute("studentsWithoutGroup", studentsWithoutGroups));

		verify(groupService).getGroup(anyLong());
		verify(adminService).getStudentsWithoutGroup();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetNewName() throws Exception {
		String groupName = "test";
		Long groupId = 1L;

		when(groupService.setNewGroupName(groupId, groupName)).thenReturn(true);

		mvc.perform(post("/group/editor/name").with(csrf()).param("groupName", groupName).param("groupId",
				groupId.toString())) // Provide groupName as a request parameter
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/group/editor?success&message=Group name was changed&groupId=1"));

		verify(groupService).setNewGroupName(groupId, groupName);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditStudents_Add() throws Exception {
		Long groupId = 1L;
		String action = "add";
		Long[] selectedStudents = { 1L, 2L };
		String message = "add students to group id=1";

		when(groupService.editStudentsForThatGroup(groupId, selectedStudents, action)).thenReturn(true);

		mvc.perform(post("/group/editor/students").with(csrf()).param("selectedStudents", "1")
				.param("selectedStudents", "2").param("action", action).param("groupId", groupId.toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/group/editor?success&message=Success: " + message + "&groupId=1"));

		verify(groupService).editStudentsForThatGroup(groupId, selectedStudents, action);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditStudents_Remove() throws Exception {
		Long groupId = 1L;
		String action = "remove";
		Long[] selectedStudents = { 1L, 2L };
		String message = "remove students to group id=1";

		when(groupService.editStudentsForThatGroup(groupId, selectedStudents, action)).thenReturn(true);

		mvc.perform(post("/group/editor/students").with(csrf()).param("selectedStudents", "1")
				.param("selectedStudents", "2").param("action", action).param("groupId", groupId.toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/group/editor?success&message=Success: " + message + "&groupId=1"));

		verify(groupService).editStudentsForThatGroup(groupId, selectedStudents, action);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowGroupCreation() throws Exception {
		RegistrationForm form = new RegistrationForm();
		List<Student> studentsWithoutGroups = ForTestsDataCreator.getNewStudents();

		when(adminService.getStudentsWithoutGroup()).thenReturn(studentsWithoutGroups);
		mvc.perform(get("/group/creation")).andExpect(status().isOk()).andExpect(view().name("group/creation"))
				.andExpect(model().attribute("form", form))
				.andExpect(model().attribute("studentsWithoutGroup", studentsWithoutGroups));

		verify(adminService).getStudentsWithoutGroup();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testCreateNewGroup() throws Exception {
		RegistrationForm form = RegistrationForm.builder().groupName("test").studentsIds(new Long[] { 1L, 2L }).build();

		when(adminService.createNewGroup(form)).thenReturn(true);

		mvc.perform(post("/group/creation").with(csrf()).flashAttr("form", form))
				.andExpect(model().attribute("groupName", form.getGroupName()))
				.andExpect(model().attribute("studentsAmount", form.getStudentsIds().length))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/group/creation-report?success&groupName=test&studentsAmount=2"));

		verify(adminService).createNewGroup(form);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testCreateReport() throws Exception {
		mvc.perform(get("/group/creation-report")).andExpect(status().isOk())
				.andExpect(view().name("group/creation-report"));

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowDeleteConfirm() throws Exception {
		Group group = ForTestsDataCreator.getGroups().get(0);

		when(groupService.getGroup(anyLong())).thenReturn(Optional.of(group));

		mvc.perform(get("/group/delete").with(csrf()).param("groupId", "1")).andExpect(status().isOk())
				.andExpect(view().name("group/delete")).andExpect(model().attribute("group", group));

		verify(groupService).getGroup(anyLong());

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testDeleteGroup() throws Exception {
		Group group = ForTestsDataCreator.getGroups().get(0);

		when(groupService.getGroup(anyLong())).thenReturn(Optional.of(group));
		doNothing().when(groupService).deleteGroup(group);

		mvc.perform(post("/group/del-group").with(csrf()).param("groupId", "1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/groups"));

		verify(groupService).getGroup(anyLong());
		verify(groupService).deleteGroup(group);

	}

}
