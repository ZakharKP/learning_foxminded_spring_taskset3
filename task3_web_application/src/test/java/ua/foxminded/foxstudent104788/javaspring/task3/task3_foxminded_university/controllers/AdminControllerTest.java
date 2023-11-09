package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AdminService adminService;

	@MockBean
	private LoginDataService loginDataService;
	
	@WithMockUser(roles = "ADMIN")
	@Test
	void testShowAccountInfoAny() throws Exception {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("TEST"));

		User user = new User("test", "test", authorities);

		when(loginDataService.loadUserByUsername(anyString())).thenReturn(user);
		mvc.perform(get("/admin/user-account-information").with(csrf())
				.param("userName", "TEST"))
				.andExpect(status().isOk())
				.andExpect(view().name("account-information"))
				.andExpect(model().attribute("userDetails", user));
		
		verify(loginDataService).loadUserByUsername("TEST");

	}
	
	
	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowManageAdmin() throws Exception {
		List<Teacher> teachers = ForTestsDataCreator.getTeachers();

		when(adminService.getAllTeachers()).thenReturn(teachers);

		mvc.perform(get("/admin/editor")).andExpect(status().isOk()).andExpect(view().name("admin/editor"))
				.andExpect(model().attribute("teachers", teachers))
				.andExpect(model().attribute("selectedTeachers", new ArrayList<Long>()));

		verify(adminService).getAllTeachers();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testManageAdmin_AddRole() throws Exception {
		List<Long> selectedTeacherIds = Arrays.asList(1L, 2L);
		String action = "add";

		mvc.perform(post("/admin/editor-post").with(csrf())
				.param("selectedTeachers", selectedTeacherIds.stream().map(String::valueOf).toArray(String[]::new))
				.param("action", action)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/editor"));

		verify(adminService).setAdminRoleToSelectedTeachers(selectedTeacherIds);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testManageAdmin_RemoveRole() throws Exception {
		List<Long> selectedTeacherIds = Arrays.asList(1L, 2L);
		String action = "remove";

		mvc.perform(post("/admin/editor-post").with(csrf()).param("selectedTeachers", "1")
				.param("selectedTeachers", "2").param("action", action)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/editor"));

		verify(adminService).removeAdminRoleFromSelectedTeachers(selectedTeacherIds);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowAddNewStudent() throws Exception {
		

		when(adminService.getAllGroups()).thenReturn(ForTestsDataCreator.getGroups());

		mvc.perform(get("/admin/signup/student"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/signup/student"))
			.andExpect(model().attribute("form", new RegistrationForm()))
			.andExpect(model().attribute("groups", ForTestsDataCreator.getGroups()));
		
		verify(adminService).getAllGroups();		
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowAddNewTeacher() throws Exception {

		mvc.perform(get("/admin/signup/teacher")).andExpect(status().isOk())
				.andExpect(view().name("admin/signup/teacher"))
				.andExpect(model().attribute("form", new RegistrationForm()));

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSignUpUserTeacher() throws Exception {

		RegistrationForm form = RegistrationForm.builder().firstName("Niko").lastName("Miko").password("****")
				.userName("test").roleName(Role.ROLE_ADMIN.getRoleName()).build();

		when(adminService.registerNewUser(form)).thenReturn(true);

		mvc.perform(post("/admin/signup").with(csrf()).flashAttr("form", form))
				.andExpect(model().attribute("roleName", form.getRoleName()))
				.andExpect(model().attribute("firstName", form.getFirstName()))
				.andExpect(model().attribute("lastName", form.getLastName()))
				.andExpect(model().attribute("userName", form.getUserName())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(
						"/admin/signup/report?success&roleName=Admin&firstName=Niko&lastName=Miko&userName=test"));

		verify(adminService).registerNewUser(form);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSignUpUserStudent() throws Exception {

		RegistrationForm form = RegistrationForm.builder().firstName("Niko").lastName("Miko").password("****")
				.userName("test").groupId(1L).roleName(Role.ROLE_STUDENT.getRoleName()).build();

		when(adminService.registerNewUser(form)).thenReturn(true);

		mvc.perform(post("/admin/signup").with(csrf()).flashAttr("form", form))
				.andExpect(model().attribute("roleName", form.getRoleName()))
				.andExpect(model().attribute("firstName", form.getFirstName()))
				.andExpect(model().attribute("lastName", form.getLastName()))
				.andExpect(model().attribute("userName", form.getUserName())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(
						"/admin/signup/report?success&roleName=Student&firstName=Niko&lastName=Miko&userName=test"));

		verify(adminService).registerNewUser(form);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testRegistrationReport() throws Exception {
		mvc.perform(get("/admin/signup/report")).andExpect(status().isOk())
				.andExpect(view().name("admin/signup/report"));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testDeleteConfirmation() throws Exception {
		LoginData login = ForTestsDataCreator.getTeachers().get(0).getLogin();

		when(adminService.getUser(anyString())).thenReturn(login);

		mvc.perform(get("/admin/delete-user").with(csrf()).param("userName", "test"))
				.andExpect(model().attribute("user", login.getTeacher())).andExpect(status().isOk())
				.andExpect(view().name("admin/delete-user"));

		verify(adminService).getUser(anyString());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testDeleteUser() throws Exception {
		doNothing().when(adminService).deleteUser(anyString());

		mvc.perform(post("/admin/del-user").with(csrf()).param("userName", "test"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));

		verify(adminService).deleteUser(anyString());
	}
	
	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowEditUserPassword() throws Exception {
		LoginData login = ForTestsDataCreator.getTeachers().get(0).getLogin();
		
		when(adminService.getUser(anyString())).thenReturn(login);
		
		mvc.perform(get("/admin/password-editor").with(csrf()).param("userName", "test"))
		.andExpect(model().attribute("user", login)).andExpect(status().isOk())
		.andExpect(view().name("admin/password-editor"));
		
		verify(adminService).getUser("test");
	}
	
	
	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditUserPassword() throws Exception {

		when(adminService.changePassword("test", "TEST")).thenReturn(true);
		
		mvc.perform(post("/admin/password-editor").with(csrf())
				.param("userName", "test")
				.param("userDetail", "TEST"))
		.andExpect(model().attribute("userName", "test")).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/admin/password-editor?success&message=Password succesfully changed&userName=test"));
		
		verify(adminService).changePassword("test", "TEST");
	}

}
