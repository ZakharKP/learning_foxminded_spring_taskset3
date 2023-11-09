package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		StudentService.class, LoginDataService.class, TestDataGenerator.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
class StudentServiceImplTest {

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LoginDataService loginDataService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TestDataGenerator dataGenerator;

	@BeforeEach
	void beforeEach() {
		when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->{
			return invocation.getArgument(0);
		});
		dataGenerator.run();
	}

	@Test
	void testGetStudentByUserName() {
		Student expected = studentService.getStudent((long) 3).get();

		Student actual = studentService.getStudentByUserName(expected.getLogin().getUserName()).get();

		assertEquals(expected, actual);
	}

	@Test
	void testSetLoginData() {
		LoginData expected = ForTestsDataCreator.getNewLogin();
		loginDataService.saveNewUser(expected);

		Student student = studentService.getStudent((long) 3).get();
		studentService.setLoginData(student.getId(), expected.getUserName());

		LoginData actual = student.getLogin();

		assertEquals(expected, actual);
	}

	@Test
	void testSetGroupToStudent() {
		Group expected = ForTestsDataCreator.getNewGroup();
		expected = groupService.saveNewGroup(expected);

		Student student = studentService.getStudent((long) 3).get();
		studentService.setGroupToStudent(student.getId(), expected.getId());

		Group actual = student.getGroup();

		assertEquals(expected, actual);
	}

	@Test
	void testGetStudentsWithoutGroup() {
		List<Student> expected = studentService.saveAllStudents(ForTestsDataCreator.getNewStudents());

		List<Student> actual = studentService.getStudentsWithoutGroup();

		assertEquals(expected, actual);
	}

	@Test
	void testGetStudentsCourses() {

		List<Course> courses = studentService.getStudentsCourses(1L);

		assertNotNull(courses);
	}

	@Test
	void testGetStudentsTeachers() {
		List<Teacher> teachers = studentService.getStudentsTeachers(1L);

		assertNotNull(teachers);

	}

	@Test
	void testSetFirstName() {
		String expected = "test";
		String actual = "notTest";
		
		if(studentService.setFirstName(1l, expected)) {
			actual = studentService.getStudent(1L).get().getFirstName();
		}
		
		assertEquals(expected, actual);
	}

	@Test
	void testSetLastName() {
		String expected = "test";
		String actual = "notTest";
		
		if(studentService.setLastName(1l, expected)) {
			actual = studentService.getStudent(1L).get().getLastName();
		}
		
		assertEquals(expected, actual);
	}

}
