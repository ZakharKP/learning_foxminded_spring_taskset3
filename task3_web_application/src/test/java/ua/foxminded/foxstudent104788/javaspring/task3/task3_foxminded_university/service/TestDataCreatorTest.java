package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataCreator;

class TestDataCreatorTest {

	@Test
	void testGetNewGroups() {
		List<Group> groups = new TestDataCreator().getNewGroups();

		int expected = 1;

		int actual = 0;

		if (groups.size() == 10) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testGetNewCourses() {
		List<Course> courses = new TestDataCreator().getNewCourses();

		int expected = 1;

		int actual = 0;

		if (courses.size() == 10) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testGetNewStudents() {
		List<Student> students = new TestDataCreator().getNewStudents();

		int expected = 1;

		int actual = 0;

		if (students.size() == 200) {
			actual++;
		}

		assertEquals(expected, actual);
	}

	@Test
	void testGetNewLectures() {
		List<Lecture> lectures = new TestDataCreator().getNewLectures();

		int expected = 1;

		int actual = -1;

		if (!lectures.isEmpty()) {
			actual++;
			if (lectures.get(3).getStartTime().getHour() >= 8 && lectures.get(3).getStartTime().getHour() <= 16) {
				actual++;
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testGetNewTeachers() {
		List<Teacher> teachers = new TestDataCreator().getNewTeachers();

		int expected = 1;

		int actual = 0;

		if (teachers.size() == 4) {
			actual++;
		}

		assertEquals(expected, actual);
	}

}
