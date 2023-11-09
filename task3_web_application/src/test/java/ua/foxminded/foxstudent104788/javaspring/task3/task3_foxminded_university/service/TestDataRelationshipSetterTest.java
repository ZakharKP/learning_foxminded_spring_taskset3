package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataRelationshipSetter;

class TestDataRelationshipSetterTest {

	private TestDataRelationshipSetter relationshipSetter = new TestDataRelationshipSetter();

	@Test
	void testSetGroupsToStudents() {
		List<Group> groups = ForTestsDataCreator.getNewGroups();
		List<Student> students = ForTestsDataCreator.getNewStudents();

		relationshipSetter.setGroupsToStudents(students, groups);

		List<Student> studentsWithoutGroup = new ArrayList<>();
		students.stream().filter(x -> x.getGroup() == null).forEach(studentsWithoutGroup::add);

		int expected = 1;
		int actual = -2;

		if (studentsWithoutGroup.isEmpty()) {
			actual++;
			if (students.get(1).getGroup() != null) {
				actual++;
				if (groups.contains(students.get(1).getGroup())) {
					actual++;
				}
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testSetStudentsToGroups() {
		List<Group> groups = ForTestsDataCreator.getNewGroups();
		List<Student> students = ForTestsDataCreator.getNewStudents();

		relationshipSetter.setGroupsToStudents(students, groups);

		relationshipSetter.setStudentsToGroups(students, groups);

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
	void testSetTeachersToCourses() {
		List<Teacher> teachers = ForTestsDataCreator.getNewTeachers();
		List<Course> courses = ForTestsDataCreator.getNewCourses();

		relationshipSetter.setTeachersToCourses(teachers, courses);

		List<Course> coursesWithoutTechers = new ArrayList<>();
		courses.stream().filter(x -> x.getTeacher() == null).forEach(coursesWithoutTechers::add);

		int expected = 1;
		int actual = -2;

		if (coursesWithoutTechers.isEmpty()) {
			actual++;
			if (courses.get(1).getTeacher() != null) {
				actual++;
				if (teachers.contains(courses.get(1).getTeacher())) {
					actual++;
				}
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testSetCoursesToTeachers() {
		List<Teacher> teachers = ForTestsDataCreator.getNewTeachers();
		List<Course> courses = ForTestsDataCreator.getNewCourses();

		relationshipSetter.setTeachersToCourses(teachers, courses);

		relationshipSetter.setCoursesToTeachers(courses, teachers);

		List<Teacher> teachersWithoutCourses = new ArrayList<>();
		teachers.stream().filter(x -> x.getCourses() == null || x.getCourses().isEmpty())
				.forEach(teachersWithoutCourses::add);

		int expected = 1;
		int actual = -1;

		if (teachersWithoutCourses.isEmpty()) {
			actual++;
			if (!(teachers.get(1).getCourses() == null || teachers.get(1).getCourses().isEmpty())) {
				actual++;
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testSetCourseAndGroupsToLectures() {
		List<Group> groups = ForTestsDataCreator.getNewGroups();
		List<Course> courses = ForTestsDataCreator.getNewCourses();
		List<Lecture> lectures = ForTestsDataCreator.getNewLectures();

		relationshipSetter.setCourseAndGroupsToLectures(courses, groups, lectures);

		List<Lecture> lecturesWithoutCourse = new ArrayList<>();
		List<Lecture> lecturesWithoutGroups = new ArrayList<>();

		lectures.stream().filter(x -> x.getCourse() == null).forEach(lecturesWithoutCourse::add);
		lectures.stream().filter(x -> x.getGroups() == null || x.getGroups().isEmpty())
				.forEach(lecturesWithoutGroups::add);

		int expected = 1;
		int actual = -1;

		if (lecturesWithoutCourse.isEmpty()) {
			actual++;
			if (lecturesWithoutGroups.isEmpty()) {
				actual++;
			}
		}
		assertEquals(expected, actual);
	}

	@Test
	void testSetLecturesToCourses() {
		List<Group> groups = ForTestsDataCreator.getNewGroups();
		List<Course> courses = ForTestsDataCreator.getNewCourses();
		List<Lecture> lectures = ForTestsDataCreator.getNewLectures();

		relationshipSetter.setCourseAndGroupsToLectures(courses, groups, lectures);

		relationshipSetter.setLecturesToCourses(lectures, courses);

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
	void testSetLecturesToCoursesAndGroups() {
		List<Group> groups = ForTestsDataCreator.getNewGroups();
		List<Course> courses = ForTestsDataCreator.getNewCourses();
		List<Lecture> lectures = ForTestsDataCreator.getNewLectures();

		relationshipSetter.setCourseAndGroupsToLectures(courses, groups, lectures);

		relationshipSetter.setLecturesToGroups(lectures, groups);

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

}
