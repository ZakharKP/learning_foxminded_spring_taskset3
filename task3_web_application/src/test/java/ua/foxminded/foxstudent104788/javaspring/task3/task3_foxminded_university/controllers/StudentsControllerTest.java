package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@WebMvcTest(StudentsController.class)
class StudentsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StudentService studentService;

	@MockBean
	private AdminService adminService;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testStudentsPersonal() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);
		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		mvc.perform(get("/student/personal").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("student/personal")).andExpect(model().attribute("student", student));
	}

	@WithMockUser(roles = { "STUDENT" })
	@Test
	void testShowStudentsCourses() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);
		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		when(studentService.getStudentsCourses(anyLong())).thenReturn(ForTestsDataCreator.getCourses());

		mvc.perform(get("/student/courses").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("courses"))
				.andExpect(model().attribute("courses", ForTestsDataCreator.getCourses()));

		verify(studentService).getStudentByUserName("test");
		verify(studentService).getStudentsCourses(student.getId());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsGroup() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);
		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));

		mvc.perform(get("/student/group").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("group/details"));

		verify(studentService).getStudentByUserName("test");
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsTeachers() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);
		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		when(studentService.getStudentsTeachers(anyLong())).thenReturn(ForTestsDataCreator.getTeachers());

		mvc.perform(get("/student/teachers").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("teachers"))
				.andExpect(model().attribute("teachers", ForTestsDataCreator.getTeachers()));

		verify(studentService).getStudentByUserName("test");
		verify(studentService).getStudentsTeachers(student.getId());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsClassmates() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);

		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));

		mvc.perform(get("/student/classmates").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("students"))
				.andExpect(model().attribute("students", student.getGroup().getStudents()))
				.andExpect(model().attribute("caller", "Classmates(" + "test" + ")"));

		verify(studentService).getStudentByUserName("test");
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsShedule() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);

		try (MockedStatic<ScheduleDataFactory> mocked = mockStatic(ScheduleDataFactory.class)) {
			mocked.when(() -> ScheduleDataFactory.getStudentScheduleEntityMap(student))
					.thenReturn(ForTestsDataCreator.getScheduleEntityMap());

			when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));

			mvc.perform(get("/student/schedule").with(csrf()).param("userName", "test")).andExpect(status().isOk())
					.andExpect(view().name("schedule"))
					.andExpect(model().attribute("entityDataMap", ForTestsDataCreator.getScheduleEntityMap()));

			verify(studentService).getStudentByUserName("test");
		}
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsEditor() throws Exception {
		Student student = ForTestsDataCreator.getStudents().get(0);
		List<Group> groups = ForTestsDataCreator.getGroups();

		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		when(adminService.getAllGroups()).thenReturn(groups);

		mvc.perform(get("/student/editor").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("student/editor")).andExpect(model().attribute("student", student))
				.andExpect(model().attribute("groups", groups));

		verify(studentService).getStudentByUserName("test");
		verify(adminService).getAllGroups();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetStudentsFirsName() throws Exception {
		String userName = "TEST";
		String firstName = "test";
		Student student = ForTestsDataCreator.getStudents().get(0);

		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		when(studentService.setFirstName(student.getId(), firstName)).thenReturn(true);

		mvc.perform(post("/student/editor/first_name").with(csrf()).param("userName", userName).param("firstName",
				firstName)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/student/editor?success&message=First Name Was Changed&userName=TEST"));

		verify(studentService).getStudentByUserName(userName);
		verify(studentService).setFirstName(student.getId(), firstName);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetStudentsLastName() throws Exception {
		String userName = "TEST";
		String lastName = "test";
		Student student = ForTestsDataCreator.getStudents().get(0);

		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		when(studentService.setLastName(student.getId(), lastName)).thenReturn(true);

		mvc.perform(
				post("/student/editor/last_name").with(csrf()).param("userName", userName).param("lastName", lastName))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/student/editor?success&message=Last Name Was Changed&userName=TEST"));

		verify(studentService).getStudentByUserName(userName);
		verify(studentService).setLastName(student.getId(), lastName);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetStudentsGroup() throws Exception {
		String userName = "TEST";
		Long groupId = 1L;
		Student student = ForTestsDataCreator.getStudents().get(0);

		when(studentService.getStudentByUserName(anyString())).thenReturn(Optional.of(student));
		when(studentService.setGroupToStudent(student.getId(), groupId)).thenReturn(true);

		mvc.perform(post("/student/editor/group").with(csrf()).param("userName", userName).param("groupId",
				groupId.toString())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/student/editor?success&message=Group Was Changed&userName=TEST"));

		verify(studentService).getStudentByUserName(userName);
		verify(studentService).setGroupToStudent(student.getId(), groupId);
	}
}
