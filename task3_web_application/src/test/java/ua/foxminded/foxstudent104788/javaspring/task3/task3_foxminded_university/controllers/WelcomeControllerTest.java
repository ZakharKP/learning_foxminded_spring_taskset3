package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.LoginDataServiceImpl;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@WebMvcTest(WelcomeController.class)
class WelcomeControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private LoginDataServiceImpl loginDataServiceImpl;

	@MockBean
	private CourseService courseService;

	@MockBean
	private GroupService groupService;

	@MockBean
	private StudentService studentService;

	@MockBean
	private TeacherService teacherService;

	@WithMockUser(roles = "ADMIN")
	@Test
	void testShowWelcomePage() throws Exception {

		mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@WithMockUser(roles = "STUDENT")
	@Test
	void testShowAccountInfo() throws Exception {
		mvc.perform(get("/account-information")).andExpect(status().isOk())
				.andExpect(view().name("account-information")).andExpect(model().attributeExists("userDetails"));

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowStudentsList() throws Exception {

		List<Student> students = ForTestsDataCreator.getStudents();

		when(studentService.getAll()).thenReturn(students);

		mvc.perform(get("/students")).andExpect(status().isOk()).andExpect(view().name("students"))
				.andExpect(model().attribute("students", students));

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowTeachersList() throws Exception {
		List<Teacher> teachers = ForTestsDataCreator.getTeachers();

		when(teacherService.getAll()).thenReturn(teachers);

		mvc.perform(get("/teachers")).andExpect(status().isOk()).andExpect(view().name("teachers"))
				.andExpect(model().attribute("teachers", teachers));
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowGroupsList() throws Exception {
		List<Group> groups = ForTestsDataCreator.getGroups();

		when(groupService.getAll()).thenReturn(groups);

		mvc.perform(get("/groups")).andExpect(status().isOk()).andExpect(view().name("groups"))
				.andExpect(model().attribute("groups", groups));
	}

	@WithMockUser(roles = { "TEACHER" })
	@Test
	void testShowCoursesList() throws Exception {
		List<Course> courses = ForTestsDataCreator.getCourses();

		when(courseService.getAll()).thenReturn(courses);

		mvc.perform(get("/courses")).andExpect(status().isOk()).andExpect(view().name("courses"))
				.andExpect(model().attribute("courses", courses));

		verify(courseService).getAll();
	}

	@WithMockUser(roles = { "STUDENT" })
	@Test
	void testShowUniversitySchedule() throws Exception {

		try (MockedStatic<ScheduleDataFactory> mocked = mockStatic(ScheduleDataFactory.class)) {
			mocked.when(() -> ScheduleDataFactory.getUniversityScheduleEntityMap())
					.thenReturn(ForTestsDataCreator.getScheduleEntityMap());

			mvc.perform(get("/schedule")).andExpect(status().isOk()).andExpect(view().name("schedule"))
					.andExpect(model().attribute("entityDataMap", ForTestsDataCreator.getScheduleEntityMap()));

		}
	}

}