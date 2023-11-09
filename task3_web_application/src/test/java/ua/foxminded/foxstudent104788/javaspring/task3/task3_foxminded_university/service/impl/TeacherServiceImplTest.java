package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.LoginDataRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.TeacherServiceImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		TeacherService.class, LoginDataService.class, TestDataGenerator.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
class TeacherServiceImplTest {

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TestDataGenerator dataGenerator;

	@Autowired
	private TeacherServiceImpl teacherService;

	@Autowired
	private LoginDataService loginDataService;

	@Autowired
	private CourseService courseService;

	@BeforeEach
	void beforeEach() {
		when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->{
			return invocation.getArgument(0);
		});
		dataGenerator.run();
	}

	@Test
	void testGetTeacherByUserName() {
		Teacher expected = teacherService.getTeacher((long) 3).get();

		Teacher actual = teacherService.getTeacherByUserName(expected.getLogin().getUserName()).get();

		assertEquals(expected, actual);
	}

	@Test
	void testSetLogin() {
		LoginData expected = ForTestsDataCreator.getNewLogin();
		expected = loginDataService.saveNewUser(expected);

		Teacher teacher = teacherService.getTeacher((long) 3).get();
		teacherService.setLogin(teacher.getId(), expected.getUserName());

		LoginData actual = teacher.getLogin();

		assertEquals(expected, actual);
	}

	@Test
	void testGetTeachersGroups() {
		List<Group> groups = teacherService.getTeachersGroups(1L);

		assertFalse(groups.isEmpty());
		;
	}

	@Test
	void testgetTeachersStudents() {
		List<Student> students = teacherService.getTeachersStudents(1L);

		assertNotNull(students);
		;
	}

	@Test
	void testSetFirstName() {
		String expected = "Test";

		Teacher teacher = teacherService.getTeacher(1L).get();

		String actual = null;
		if (teacherService.setFirstName(1l, expected)) {
			actual = teacher.getFirstName();
		}

		assertEquals(expected, actual);
	}

	@Test
	void testSetLastName() {
		String expected = "Test";

		Teacher teacher = teacherService.getTeacher(1L).get();

		String actual = null;
		if (teacherService.setLastName(1l, expected)) {
			actual = teacher.getLastName();
		}

		assertEquals(expected, actual);
	}

	@Test
	void teastAddCoursesToTeacher() {
		List<Course> courses = courseService.saveAllCourses(ForTestsDataCreator.getNewCourses());

		List<Long> coursesIds = courses.stream().map(Course::getId).collect(Collectors.toList());

		int expected = 1;
		int actual = -1;

		if (teacherService.addCoursesToTeacher(1L, coursesIds)) {
			actual++;
			if (teacherService.getTeacher(1L).get().getCourses().containsAll(courses)) {
				actual++;
			}
		}
		assertEquals(expected, actual);
	}

	@Test
	void removeCoursesFromTeacher() {
		Set<Course> courses = teacherService.getTeacher(1L).get().getCourses();

		List<Long> coursesIds = courses.stream().map(Course::getId).collect(Collectors.toList());

		int expected = 1;
		int actual = -1;

		if (teacherService.removeCoursesFromTeacher(1L, coursesIds)) {
			actual++;
			if (teacherService.getTeacher(1L).get().getCourses().isEmpty()) {
				actual++;
			}
		}
		assertEquals(expected, actual);
	}

	@Test
	void editCoursesToSelectedTeacher() {
		List<Course> courses = courseService.saveAllCourses(ForTestsDataCreator.getNewCourses());

		Long[] coursesIds = courses.stream().map(Course::getId).toArray(Long[]::new);

		int expected = 1;
		int actual = -3;

		if (teacherService.editCoursesToSelectedTeacher(1L, coursesIds, "add")) {
			actual++;
			if (teacherService.getTeacher(1L).get().getCourses().containsAll(courses)) {
				actual++;
				if (teacherService.editCoursesToSelectedTeacher(1L, coursesIds, "remove")) {
					actual++;
					if (!teacherService.getTeacher(1L).get().getCourses().containsAll(courses)) {
						actual++;
					}
				}
			}
		}
		assertEquals(expected, actual);
	}
}
