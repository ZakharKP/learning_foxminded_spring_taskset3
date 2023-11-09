package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		StudentService.class, GroupService.class, LectureService.class, CourseService.class, TeacherService.class,
		LoginDataService.class, TestDataGenerator.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories")
class TestDataGeneratorTest {

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LectureService lectureService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private LoginDataService loginDataService;

	@Autowired
	private TestDataGenerator dataGenerator;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void beforeEach() {
		when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->{
			return invocation.getArgument(0);
		});
		dataGenerator.run();
	}

	@Test
	void testRunAreStudentsPresent() {
		List<Student> students = studentService.getAll();

		int expected = 1;

		int actual = 0;

		if (students.size() == 200) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveStudentsGroups() {

		List<Student> students = studentService.getAll();

		List<Student> studentsWithoutGroup = new ArrayList<>();
		students.stream().filter(x -> x.getGroup() == null).forEach(studentsWithoutGroup::add);

		int expected = 1;
		int actual = 0;

		if (studentsWithoutGroup.isEmpty()) {
			actual++;
		}
		assertEquals(expected, actual);
	}

	@Test
	void testRunAreGroupsPresent() {
		List<Group> groups = groupService.getAll();

		int expected = 1;

		int actual = 0;

		if (!groups.isEmpty()) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveGroupsStudents() {
		List<Group> groups = groupService.getAll();

		List<Group> groupsWithoutStudents = new ArrayList<>();
		groups.stream().filter(x -> x.getStudents() == null || x.getStudents().isEmpty())
				.forEach(groupsWithoutStudents::add);

		int expected = 1;
		int actual = -1;

		if (groupsWithoutStudents.isEmpty()) {
			actual++;
			if (groups.get(1).getStudents() != null || groups.get(1).getStudents().isEmpty()) {
				actual++;
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunAreCoursesPresent() {
		List<Course> courses = courseService.getAll();

		int expected = 1;

		int actual = 0;

		if (courses.size() == 10) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveCoursesTeachers() {
		List<Course> courses = courseService.getAll();
		List<Course> coursesWithoutTechers = new ArrayList<>();
		courses.stream().filter(x -> x.getTeacher() == null).forEach(coursesWithoutTechers::add);

		int expected = 1;
		int actual = 0;

		if (coursesWithoutTechers.isEmpty()) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunAreTeachersPresent() {
		List<Teacher> teachers = teacherService.getAll();

		int expected = 1;

		int actual = 0;

		if (teachers.size() == 4) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveTeachersCourses() {
		List<Teacher> teachers = teacherService.getAll();

		List<Teacher> teachersWithoutCourses = new ArrayList<>();
		teachers.stream().filter(x -> x.getCourses() == null || x.getCourses().isEmpty())
				.forEach(teachersWithoutCourses::add);

		int expected = 1;
		int actual = 0;

		if (teachersWithoutCourses.isEmpty()) {
			actual++;

		}

		assertEquals(expected, actual);

	}

	@Test
	void testRunAreLecturesPresent() {
		List<Lecture> lectures = lectureService.getAll();

		int expected = 1;

		int actual = -1;

		if (!lectures.isEmpty()) {
			actual++;
			if (lectures.get(0).getStartTime().getHour() >= 8 && lectures.get(3).getStartTime().getHour() <= 16) {
				actual++;
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveLecturesCourses() {

		List<Lecture> lectures = lectureService.getAll();

		List<Lecture> lecturesWithoutCourse = new ArrayList<>();

		lectures.stream().filter(x -> x.getCourse() == null).forEach(lecturesWithoutCourse::add);

		int expected = 1;
		int actual = 0;

		if (lecturesWithoutCourse.isEmpty()) {
			actual++;

		}
		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveLecturesGroups() {

		List<Lecture> lectures = lectureService.getAll();

		List<Lecture> lecturesWithoutGroups = new ArrayList<>();

		lectures.stream().filter(x -> x.getGroups() == null || x.getGroups().isEmpty())
				.forEach(lecturesWithoutGroups::add);

		int expected = 1;
		int actual = 0;

		if (lecturesWithoutGroups.isEmpty()) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveCoursesLectures() {
		List<Course> courses = courseService.getAll();

		List<Course> coursesWithoutLectures = new ArrayList<>();
		courses.stream().filter(x -> x.getLectures() == null || x.getLectures().isEmpty())
				.forEach(coursesWithoutLectures::add);

		int expected = 1;
		int actual = 0;

		if (coursesWithoutLectures.isEmpty()) {
			actual++;

		}
		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveGroupsLectures() {
		List<Group> groups = groupService.getAll();

		List<Group> groupsWithoutLectures = new ArrayList<>();
		groups.stream().filter(x -> x.getLectures() == null || x.getLectures().isEmpty())
				.forEach(groupsWithoutLectures::add);

		int expected = 1;
		int actual = 0;

		if (groupsWithoutLectures.isEmpty()) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunAreLoginDatasPresent() {
		List<LoginData> loginDatas = loginDataService.getAll();

		int expected = 1;
		int actual = 0;

		if (!loginDatas.isEmpty()) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveStudentsLoginDatas() {
		List<Student> students = studentService.getAll();

		List<Student> studentsWithoutLogins = new ArrayList<>();
		students.stream().filter(x -> x.getLogin() == null).forEach(studentsWithoutLogins::add);

		int expected = 1;
		int actual = 0;

		if (studentsWithoutLogins.isEmpty()) {
			actual++;
		}
		assertEquals(expected, actual);
	}

	@Test
	void testRunHaveTeachersLoginDatas() {
		List<Teacher> teachers = teacherService.getAll();

		List<Teacher> teachersWithoutLogins = new ArrayList<>();
		teachers.stream().filter(x -> x.getLogin() == null).forEach(teachersWithoutLogins::add);

		int expected = 1;
		int actual = 0;

		if (teachersWithoutLogins.isEmpty()) {
			actual++;

		}

		assertEquals(expected, actual);

	}

	@Test
	void testRunHaveLoginDatasRoles() {
		List<LoginData> loginDatas = loginDataService.getAll();

		List<LoginData> loginsWithoutRoles = new ArrayList<>();
		loginDatas.stream().filter(x -> x.getRole() == null).forEach(loginsWithoutRoles::add);

		int expected = 1;
		int actual = 0;

		if (loginsWithoutRoles.isEmpty()) {
			actual++;

		}

		assertEquals(expected, actual);

	}

}
