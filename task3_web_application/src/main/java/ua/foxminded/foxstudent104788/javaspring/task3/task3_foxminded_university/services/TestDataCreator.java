
package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.http.MediaType;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.FilesServiceUtil;

/**
 * The TestDataCreator class is responsible for creating test data for groups,
 * courses, students, and enrollments.
 */

@Log4j2
public class TestDataCreator {

	private static final Random RANDOM = new Random();

	private static final int STUDENTS_AMOUNT = 200;
	private static final int GROUPS_AMOUNT = 10;
	private static final int TEACHERS_AMOUNT = 4;
	//private static final LocalDateTime START_PERIOD = LocalDateTime.of(2023, 9, 1, 8, 0, 0);
	private static final LocalDateTime START_PERIOD = LocalDateTime.now();
	//private static final LocalDateTime STOP_PERIOD = LocalDateTime.of(2023, 12, 31, 16, 10, 0);
	private static final LocalDateTime STOP_PERIOD = START_PERIOD.plus(Period.ofMonths(6));
	private static final Path RESOURSE_DIR = Paths.get("src", "main", "resources", "testData");
	private static final String COURSE_INTRO_DIR = "course_intro";
	private static final String INTRO_IMAGES_DIR = "images";
	private static final Path COURSE_INTRO_TEXT = Paths.get("text", "text.txt");

	/**
	 * Generates a list of groups with randomly generated names.
	 *
	 * @return The list of generated groups.
	 */

	public List<Group> getNewGroups() {

		List<Group> groups = new ArrayList<>();

		for (int i = 0; i < GROUPS_AMOUNT; i++) {

			Group group = getGroup();

			while (groups.contains(group)) {
				group = getGroup();
			}
			groups.add(group);
		}

		log.info("Groups was generated");
		return groups;
	}

	private Group getGroup() {
		String groupName;

		char randomChar1 = (char) (RANDOM.nextInt('z' - 'a' + 1) + 'a');
		char randomChar2 = (char) (RANDOM.nextInt('z' - 'a' + 1) + 'a');
		int randomInt = RANDOM.nextInt(100);

		groupName = String.valueOf(randomChar1) + String.valueOf(randomChar2) + "-" + String.valueOf(randomInt);
		return Group.builder().groupName(groupName).build();
	}

	/**
	 * Generates a list of courses by reading from a file.
	 *
	 * @return The list of generated courses.
	 */
	public List<Course> getNewCourses() {

		List<Course> courses = new ArrayList<>();

		for (Map.Entry<String, String> course : FilesServiceUtil
				.getMapFromFile(Paths.get(RESOURSE_DIR.toString(), "courses.properties")).entrySet()) {

			courses.add(Course.builder().courseName(course.getKey()).courseDescription(course.getValue()).build());

		}

		log.info("Courses was generated");
		return courses;
	}

	/**
	 * Generates a list of students with randomly generated first names and last
	 * names.
	 *
	 * @return The list of generated students.
	 */

	public List<Student> getNewStudents() {
		List<Student> students = new ArrayList<>();

		List<String> firstNames = FilesServiceUtil
				.getStringListFromFile(Paths.get(RESOURSE_DIR.toString(), "firstNames.txt"));
		List<String> lastNames = FilesServiceUtil
				.getStringListFromFile(Paths.get(RESOURSE_DIR.toString(), "lastNames.txt"));

		for (int i = 0; i < STUDENTS_AMOUNT; i++) {
			Student student = createStudent(firstNames, lastNames);
			while (students.contains(student)) {
				student = createStudent(firstNames, lastNames);
			}
			students.add(student);
		}

		log.info("Students was gererated");
		return students;
	}

	private Student createStudent(List<String> firstNames, List<String> lastNames) {

		int firstNameIndex = RANDOM.nextInt(firstNames.size());
		int lastNameIndex = RANDOM.nextInt(lastNames.size());

		return Student.builder().firstName(firstNames.get(firstNameIndex)).lastName(lastNames.get(lastNameIndex))
				.build();
	}

	public List<Lecture> getNewLectures() {
		List<Lecture> lectures = new ArrayList<>();
		List<LocalDateTime> startTimes = getLecturesStartTimes();

		startTimes.stream().forEach(x -> lectures.add(Lecture.builder().startTime(x).build()));

		return lectures;
	}

	private List<LocalDateTime> getLecturesStartTimes() {
		List<LocalDateTime> result = new ArrayList<>();
		LocalDateTime current = START_PERIOD;

		while (current.isBefore(STOP_PERIOD)) {
			int hour = current.getHour();
			if (current.getDayOfWeek().getValue() >= 1 && current.getDayOfWeek().getValue() <= 5 // Monday to Friday
					&& hour >= 8 && hour <= 16) { // Between 08:00 and 16:00
				result.add(current);
			}
			current = current.plus(2, ChronoUnit.HOURS);
		}
		return result;
	}

	/**
	 * Generates a list of teachers with randomly generated first names and last
	 * names.
	 *
	 * @return The list of generated teachers.
	 */
	public List<Teacher> getNewTeachers() {
		List<Teacher> teachers = new ArrayList<>();

		List<String> firstNames = FilesServiceUtil
				.getStringListFromFile(Paths.get(RESOURSE_DIR.toString(), "firstNames.txt"));
		List<String> lastNames = FilesServiceUtil
				.getStringListFromFile(Paths.get(RESOURSE_DIR.toString(), "lastNames.txt"));

		for (int i = 0; i < TEACHERS_AMOUNT; i++) {
			Teacher teacher = createTeacher(firstNames, lastNames);
			while (teachers.contains(teacher)) {
				teacher = createTeacher(firstNames, lastNames);
			}
			teachers.add(teacher);
		}

		log.info("Teachers was gererated");
		return teachers;
	}

	private Teacher createTeacher(List<String> firstNames, List<String> lastNames) {

		int firstNameIndex = RANDOM.nextInt(firstNames.size());
		int lastNameIndex = RANDOM.nextInt(lastNames.size());

		return Teacher.builder().firstName(firstNames.get(firstNameIndex)).lastName(lastNames.get(lastNameIndex))
				.build();
	}

	public List<Image> getImagesForCourse(String courseName)  {
		List<Image> images = new ArrayList<>();
		try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream( Paths.get(RESOURSE_DIR.toString(),COURSE_INTRO_DIR, courseName, INTRO_IMAGES_DIR)  )) {
			for(Path imagePath: directoryStream) {
				
				images.add(Image.builder()
						.fileName(imagePath.getFileName().toString())
						.mediaType(MediaType.parseMediaType(new MimetypesFileTypeMap().getContentType(imagePath.getFileName().toFile())).toString())
						.data(FilesServiceUtil.loadImageToByteArray(imagePath.toString()))
						.build());
			}
			return images;
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		
	}

	public String getCourseIntroText(String courseName) {
		return String.join("", FilesServiceUtil.getStringListFromFile(Paths.get(RESOURSE_DIR.toString(),COURSE_INTRO_DIR, courseName, COURSE_INTRO_TEXT.toString())));
	}

}
