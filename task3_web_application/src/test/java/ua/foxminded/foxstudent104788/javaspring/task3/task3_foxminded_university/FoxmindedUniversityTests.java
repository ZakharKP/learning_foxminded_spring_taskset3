package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;

@SpringBootTest
class FoxmindedUniversityTests {

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private LectureService lectureService;

	@Autowired
	private TestDataGenerator dataGenerator;

	@Test
	void contextLoadsStudentService() {
		assertNotNull(studentService);
	}

	@Test
	void contextLoadsGroupService() {
		assertNotNull(groupService);
	}

	@Test
	void contextLoadsCourseService() {
		assertNotNull(courseService);
	}

	@Test
	void contextLoadsTeacherService() {
		assertNotNull(teacherService);
	}

	@Test
	void contextLoadsLectureService() {
		assertNotNull(lectureService);
	}

	@Test
	void contextLoadsTestDataGenerator() {
		assertNotNull(dataGenerator);
	}

}
