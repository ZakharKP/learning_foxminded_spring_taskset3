package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

public class ScheduleDataFactory {

	private static final String ID = "id";
	private static final String CLASS = "class";
	private static final String TITLE = "title";

	public static String getEventTitle(Lecture lecture) {
		String[] groups = lecture.getGroups().stream().map(Group::getGroupName).toArray(String[]::new);
		return lecture.getCourse().getCourseName() + " for " + String.join(", ", groups);
	}

	public static List<String> getEventProperties(Lecture lecture, int studentsAmount) {
		List<String> properties = new ArrayList<>();
		properties.add("Course: " + lecture.getCourse().getCourseName());
		properties.add("Teacher: " + lecture.getCourse().getTeacher().getFirstName() + " "
				+ lecture.getCourse().getTeacher().getLastName());
		lecture.getGroups().forEach(x -> properties.add("Group: " + x.getGroupName()));
		properties.add("Student's amount: " + studentsAmount);
		return properties;
	}

	public static String generateLightColorCode(String input) {
		int hash = input.hashCode();
		int r = (hash & 0xFF) + 128; // Ensure a minimum value of 128 for lightness
		int g = ((hash >> 8) & 0xFF) + 128;
		int b = ((hash >> 16) & 0xFF) + 128;

		// Limit values to be within the valid color range
		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);

		return String.format("#%02X%02X%02X", r, g, b);
	}

	public static Map<String, String> getUniversityScheduleEntityMap() {
		Map<String, String> entityDataMap = new HashMap<>();
		entityDataMap.put(TITLE, "University Fox Minded");
		entityDataMap.put(CLASS, "University");
		entityDataMap.put(ID, "0");

		return entityDataMap;
	}

	public static Map<String, String> getTeacherScheduleEntityMap(Teacher teacher) {
		Map<String, String> entityDataMap = new HashMap<>();
		entityDataMap.put(TITLE, String.format("%s: %s %s", teacher.getClass().getSimpleName(), teacher.getFirstName(),
				teacher.getLastName()));
		entityDataMap.put(CLASS, teacher.getClass().getSimpleName());
		entityDataMap.put(ID, teacher.getId().toString());

		return entityDataMap;
	}

	public static Map<String, String> getStudentScheduleEntityMap(Student student) {
		Map<String, String> entityDataMap = new HashMap<>();
		entityDataMap.put(TITLE, String.format("%s: %s %s", student.getClass().getSimpleName(), student.getFirstName(),
				student.getLastName()));
		entityDataMap.put(CLASS, student.getClass().getSimpleName());
		entityDataMap.put(ID, student.getId().toString());

		return entityDataMap;
	}

	public static Map<String, String> getGroupScheduleEntityMap(Group group) {
		Map<String, String> entityDataMap = new HashMap<>();
		entityDataMap.put(TITLE, String.format("%s: %s", group.getClass().getSimpleName(), group.getGroupName()));
		entityDataMap.put(CLASS, group.getClass().getSimpleName());
		entityDataMap.put(ID, group.getId().toString());

		return entityDataMap;
	}

	public static Map<String, String> getCourseScheduleEntityMap(Course course) {
		Map<String, String> entityDataMap = new HashMap<>();
		entityDataMap.put(TITLE, String.format("%s: %s", course.getClass().getSimpleName(), course.getCourseName()));
		entityDataMap.put(CLASS, course.getClass().getSimpleName());
		entityDataMap.put(ID, course.getId().toString());

		return entityDataMap;
	}

	public static List<LocalTime> getAllowedTime() {
		List<LocalTime> allowedTimes = new ArrayList<>();
		allowedTimes.add(LocalTime.of(8, 0));
		allowedTimes.add(LocalTime.of(10, 00));
		allowedTimes.add(LocalTime.of(12, 00));
		allowedTimes.add(LocalTime.of(14, 00));
		allowedTimes.add(LocalTime.of(16, 00));
		return allowedTimes;
	}

}
