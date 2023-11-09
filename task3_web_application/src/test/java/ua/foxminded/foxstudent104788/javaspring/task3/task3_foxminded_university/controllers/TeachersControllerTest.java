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
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@WebMvcTest(TeachersController.class)
class TeachersControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private AdminService adminService;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testTeacherPersonal() throws Exception {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);
		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));
		mvc.perform(get("/teacher/personal").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("teacher/personal")).andExpect(model().attribute("teacher", teacher));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowTeachersCourses() throws Exception {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);
		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));

		mvc.perform(get("/teacher/courses").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("courses")).andExpect(model().attribute("courses", teacher.getCourses()));

		verify(teacherService).getTeacherByUserName("test");
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowTeachersGroup() throws Exception {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);
		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));
		when(teacherService.getTeachersGroups(anyLong())).thenReturn(ForTestsDataCreator.getGroups());

		mvc.perform(get("/teacher/groups").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("groups"))
				.andExpect(model().attribute("groups", ForTestsDataCreator.getGroups()));

		verify(teacherService).getTeacherByUserName("test");
		verify(teacherService).getTeachersGroups(teacher.getId());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowTeachersStudents() throws Exception {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);
		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));
		when(teacherService.getTeachersStudents(anyLong())).thenReturn(ForTestsDataCreator.getStudents());

		mvc.perform(get("/teacher/students").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("students"))
				.andExpect(model().attribute("students", ForTestsDataCreator.getStudents()));

		verify(teacherService).getTeacherByUserName("test");
		verify(teacherService).getTeachersStudents(teacher.getId());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowTeachersShedule() throws Exception {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);

		try (MockedStatic<ScheduleDataFactory> mocked = mockStatic(ScheduleDataFactory.class)) {
			mocked.when(() -> ScheduleDataFactory.getTeacherScheduleEntityMap(teacher))
					.thenReturn(ForTestsDataCreator.getScheduleEntityMap());

			when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));

			mvc.perform(get("/teacher/schedule").with(csrf()).param("userName", "test")).andExpect(status().isOk())
					.andExpect(view().name("schedule"))
					.andExpect(model().attribute("entityDataMap", ForTestsDataCreator.getScheduleEntityMap()));

			verify(teacherService).getTeacherByUserName("test");

		}
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowTeachersEditor() throws Exception {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);
		List<Course> courses = ForTestsDataCreator.getCourses();

		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));
		when(adminService.getCoursesWithoutTeacher()).thenReturn(courses);

		mvc.perform(get("/teacher/editor").with(csrf()).param("userName", "test")).andExpect(status().isOk())
				.andExpect(view().name("teacher/editor")).andExpect(model().attribute("teacher", teacher))
				.andExpect(model().attribute("coursesWithoutTeacher", courses));

		verify(teacherService).getTeacherByUserName("test");
		verify(adminService).getCoursesWithoutTeacher();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetTeachersFirsName() throws Exception {
		String userName = "TEST";
		String firstName = "test";
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);

		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));
		when(teacherService.setFirstName(teacher.getId(), firstName)).thenReturn(true);

		mvc.perform(post("/teacher/editor/first_name").with(csrf()).param("userName", userName).param("firstName",
				firstName)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/teacher/editor?success&message=First Name Was Changed&userName=TEST"));

		verify(teacherService).getTeacherByUserName(userName);
		verify(teacherService).setFirstName(teacher.getId(), firstName);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetTeachersLastName() throws Exception {
		String userName = "TEST";
		String lastName = "test";
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);

		when(teacherService.getTeacherByUserName(anyString())).thenReturn(Optional.of(teacher));
		when(teacherService.setLastName(teacher.getId(), lastName)).thenReturn(true);

		mvc.perform(
				post("/teacher/editor/last_name").with(csrf()).param("userName", userName).param("lastName", lastName))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/teacher/editor?success&message=Last Name Was Changed&userName=TEST"));

		verify(teacherService).getTeacherByUserName(userName);
		verify(teacherService).setLastName(teacher.getId(), lastName);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditCourses_Add() throws Exception {
		Long teacherId = 1L;
		String action = "add";
		Long[] selectedCourses = { 1L, 2L };
		String message = "add courses to Teacher id=1";

		when(teacherService.getTeacher(teacherId)).thenReturn(Optional.of(ForTestsDataCreator.getTeachers().get(0)));
		when(teacherService.editCoursesToSelectedTeacher(teacherId, selectedCourses, action)).thenReturn(true);

		mvc.perform(post("/teacher/editor/courses").with(csrf()).param("selectedCourses", "1")
				.param("selectedCourses", "2").param("action", action).param("teacherId", teacherId.toString()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(
						"/teacher/editor?success&message=Success: " + message + "&teacherId=1&userName=user13"));

		verify(teacherService).getTeacher(teacherId);
		verify(teacherService).editCoursesToSelectedTeacher(teacherId, selectedCourses, action);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditCourses_Remove() throws Exception {
		Long teacherId = 1L;
		String action = "remove";
		Long[] selectedCourses = { 1L, 2L };
		String message = "remove courses to Teacher id=1";

		when(teacherService.getTeacher(teacherId)).thenReturn(Optional.of(ForTestsDataCreator.getTeachers().get(0)));
		when(teacherService.editCoursesToSelectedTeacher(teacherId, selectedCourses, action)).thenReturn(true);

		mvc.perform(post("/teacher/editor/courses").with(csrf()).param("selectedCourses", "1")
				.param("selectedCourses", "2").param("action", action).param("teacherId", teacherId.toString()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(
						"/teacher/editor?success&message=Success: " + message + "&teacherId=1&userName=user13"));

		verify(teacherService).getTeacher(teacherId);
		verify(teacherService).editCoursesToSelectedTeacher(teacherId, selectedCourses, action);
	}

}
