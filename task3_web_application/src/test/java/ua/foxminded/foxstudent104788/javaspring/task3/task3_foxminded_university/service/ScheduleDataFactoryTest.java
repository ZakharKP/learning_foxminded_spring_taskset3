package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

class ScheduleDataFactoryTest {

	@Test
	void testGetEventTitle() {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		String expected = "biology for xx-11";

		String actual = ScheduleDataFactory.getEventTitle(lecture);

		assertEquals(expected, actual);
	}

	@Test
	void testGetEventProperties() {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		List<String> expected = new ArrayList<>();
		expected.add("Course: biology");
		expected.add("Teacher: Namas Ruko");
		expected.add("Group: xx-11");
		expected.add("Student's amount: 15");

		List<String> actual = ScheduleDataFactory.getEventProperties(lecture, 15);

		assertEquals(expected, actual);
	}

	@Test
	void testGenerateLightColorCode() {

		String expected = "#FFC4B6";

		String actual = ScheduleDataFactory.generateLightColorCode("test");

		assertEquals(expected, actual);
	}

	@Test
	void testGetUniversityScheduleEntityMap() {
		Map<String, String> expected = new HashMap<>();
		expected.put("id", "0");
		expected.put("title", "University Fox Minded");
		expected.put("class", "University");

		Map<String, String> actual = ScheduleDataFactory.getUniversityScheduleEntityMap();

		assertEquals(expected, actual);
	}

	@Test
	void testGetTeacherScheduleEntityMap() {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);

		Map<String, String> expected = new HashMap<>();
		expected.put("id", "1");
		expected.put("title", "Teacher: Namas Ruko");
		expected.put("class", "Teacher");

		Map<String, String> actual = ScheduleDataFactory.getTeacherScheduleEntityMap(teacher);

		assertEquals(expected, actual);
	}

	@Test
	void testGetStudentScheduleEntityMap() {
		Student student = ForTestsDataCreator.getStudents().get(0);

		Map<String, String> expected = new HashMap<>();
		expected.put("id", "1");
		expected.put("title", "Student: Pavel Pashkin");
		expected.put("class", "Student");

		Map<String, String> actual = ScheduleDataFactory.getStudentScheduleEntityMap(student);

		assertEquals(expected, actual);
	}

	@Test
	void testGetGroupScheduleEntityMap() {
		Group group = ForTestsDataCreator.getGroups().get(0);

		Map<String, String> expected = new HashMap<>();
		expected.put("id", "1");
		expected.put("title", "Group: xx-11");
		expected.put("class", "Group");

		Map<String, String> actual = ScheduleDataFactory.getGroupScheduleEntityMap(group);

		assertEquals(expected, actual);
	}

	@Test
	void testGetCourseScheduleEntityMap() {
		Course course = ForTestsDataCreator.getCourses().get(0);

		Map<String, String> expected = new HashMap<>();
		expected.put("id", "1");
		expected.put("title", "Course: biology");
		expected.put("class", "Course");

		Map<String, String> actual = ScheduleDataFactory.getCourseScheduleEntityMap(course);

		assertEquals(expected, actual);
	}

	@Test
	void testGetAllowedTime() {
		List<LocalTime> expected = new ArrayList<>();
		expected.add(LocalTime.of(8, 0));
		expected.add(LocalTime.of(10, 0));
		expected.add(LocalTime.of(12, 0));
		expected.add(LocalTime.of(14, 0));
		expected.add(LocalTime.of(16, 0));

		List<LocalTime> actual = ScheduleDataFactory.getAllowedTime();

		assertEquals(expected, actual);
	}

}
