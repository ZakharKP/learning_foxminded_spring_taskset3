package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

/**
 * The TestDataRelationshipSetter class is responsible for setting relationships
 * between test data entities.
 */
public class TestDataRelationshipSetter {

	private static final Random RANDOM = new Random();

	private static final int MAX_GROUPS_IN_LECTURE = 3;

	/**
	 * Sets groups to students randomly based on the number of students and groups.
	 *
	 * @param students The list of students.
	 * @param groups   The list of groups.
	 */
	public void setGroupsToStudents(List<Student> students, List<Group> groups) {
		List<Student> studentsWithoutGroup = new ArrayList<>();
		int studentsInGroup = students.size() / groups.size();
		students.stream().filter(x -> x.getGroup() == null).forEach(studentsWithoutGroup::add);

		while (!studentsWithoutGroup.isEmpty()) {

			for (int i = 0; i < groups.size(); i++) {

				if (!studentsWithoutGroup.isEmpty()) {
					setThatGroupToStudents(groups.get(i), studentsWithoutGroup,
							studentsInGroup < studentsWithoutGroup.size() ? studentsInGroup
									: studentsWithoutGroup.size());
					studentsWithoutGroup.clear();
					students.stream().filter(x -> x.getGroup() == null).forEach(studentsWithoutGroup::add);
				}
			}
		}
	}

	private void setThatGroupToStudents(Group group, List<Student> students, int studentsInGroup) {

		Collections.shuffle(students);
		for (int i = 0; i < studentsInGroup; i++) {
			students.get(i).setGroup(group);
		}

	}

	/**
	 * Sets students to groups based on the students' group assignments.
	 *
	 * @param students The list of students.
	 * @param groups   The list of groups.
	 */
	public void setStudentsToGroups(List<Student> students, List<Group> groups) {
		Set<Student> studentsOfGroup;
		for (Group group : groups) {
			studentsOfGroup = students.stream().filter(x -> x.getGroup().equals(group)).collect(Collectors.toSet());
			group.setStudents(studentsOfGroup);
		}
	}

	/**
	 * Sets teachers to courses based on the teachers' course assignments.
	 *
	 * @param teachers The list of teachers.
	 * @param courses  The list of courses.
	 */
	public void setTeachersToCourses(List<Teacher> teachers, List<Course> courses) {
		List<Course> coursesWithoutTeacher = new ArrayList<>();
		courses.stream().filter(x -> x.getTeacher() == null).forEach(coursesWithoutTeacher::add);
		while (!coursesWithoutTeacher.isEmpty()) {
			Collections.shuffle(coursesWithoutTeacher);
			for (int i = 0; i < teachers.size(); i++) {
				if (i < coursesWithoutTeacher.size()) {
					coursesWithoutTeacher.get(i).setTeacher(teachers.get(i));
				}
			}
			coursesWithoutTeacher.clear();
			courses.stream().filter(x -> x.getTeacher() == null).forEach(coursesWithoutTeacher::add);
		}
	}

	/**
	 * Sets courses to teachers based on the courses' teacher assignments.
	 *
	 * @param courses  The list of courses.
	 * @param teachers The list of teachers.
	 */
	public void setCoursesToTeachers(List<Course> courses, List<Teacher> teachers) {
		Set<Course> coursesOfTeacher;
		for (Teacher teacher : teachers) {
			coursesOfTeacher = courses.stream().filter(x -> x.getTeacher().equals(teacher)).collect(Collectors.toSet());
			teacher.setCourses(coursesOfTeacher);
		}
	}

	/**
	 * Sets courses and groups to lectures based on the available courses and
	 * groups.
	 *
	 * @param courses  The list of courses.
	 * @param groups   The list of groups.
	 * @param lectures The list of lectures.
	 */
	public void setCourseAndGroupsToLectures(List<Course> courses, List<Group> groups, List<Lecture> lectures) {

		List<Lecture> lecturesWithoutCourses = new ArrayList<>();
		lectures.stream().filter(x -> x.getCourse() == null).forEach(lecturesWithoutCourses::add);

		for (Lecture lecture : lectures) {
			setCourseForThatLecture(courses, lecture);
			setGroupsForThatLecture(groups, lecture);

		}

	}

	private void setCourseForThatLecture(List<Course> courses, Lecture lecture) {
		int courseIndex = RANDOM.nextInt(courses.size());
		lecture.setCourse(courses.get(courseIndex));
	}

	private void setGroupsForThatLecture(List<Group> groups, Lecture lecture) {
		Set<Group> thatLecturesGroups = new HashSet<>();
		int groupsForEach = RANDOM.nextInt(MAX_GROUPS_IN_LECTURE) + 1;

		for (int i = 0; i < groupsForEach; i++) {

			int groupIndex = RANDOM.nextInt(groups.size());

			Group group = groups.get(groupIndex);
			thatLecturesGroups.add(group);
		}

		lecture.setGroups(thatLecturesGroups);
	}

	/**
	 * Sets lectures to courses based on the lectures' course assignments.
	 *
	 * @param lectures The list of lectures.
	 * @param courses  The list of courses.
	 */
	public void setLecturesToCourses(List<Lecture> lectures, List<Course> courses) {
		Set<Lecture> coursesLectures;
		for (Course course : courses) {
			coursesLectures = lectures.stream().filter(x -> x.getCourse().equals(course)).collect(Collectors.toSet());
			course.setLectures(coursesLectures);
		}

	}

	/**
	 * Sets lectures to groups based on the lectures' group assignments.
	 *
	 * @param lectures The list of lectures.
	 * @param groups   The list of groups.
	 */
	public void setLecturesToGroups(List<Lecture> lectures, List<Group> groups) {
		Set<Lecture> groupLectures;
		for (Group group : groups) {
			groupLectures = lectures.stream().filter(x -> x.getGroups().contains(group)).collect(Collectors.toSet());
			group.setLectures(groupLectures);
		}

	}
}
