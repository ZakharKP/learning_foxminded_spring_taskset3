package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.FullCalendarEvent;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

public class ForTestsDataCreator {

	public static Student getNewStudent() {
		return Student.builder().firstName("John").lastName("Doe").build();
	}

	public static Course getNewCourse() {
		return Course.builder().courseName("foxminded").courseDescription("course for abbitions").build();
	}

	public static Group getNewGroup() {
		return Group.builder().groupName("xx-11").students(new HashSet<>()).build();
	}

	public static Teacher getNewTeacher() {
		return Teacher.builder().firstName("Billy").lastName("Joe").build();
	}

	public static Lecture getNewLecture() {
		return Lecture.builder().startTime(LocalDateTime.now()).build();
	}

	public static LoginData getNewLogin() {
		return LoginData.builder().userName("user").password("****").build();
	}

	public static List<Student> getNewStudents() {
		List<Student> students = new ArrayList<>();

		students.add(Student.builder().firstName("Pavel").lastName("Pashkin").build());
		students.add(Student.builder().firstName("Piotr").lastName("Vasiuk").build());
		students.add(Student.builder().firstName("Tasas").lastName("Mirko").build());
		students.add(Student.builder().firstName("Tasas").lastName("Zubko").build());
		students.add(Student.builder().firstName("Sem").lastName("Milek").build());
		students.add(Student.builder().firstName("Marek").lastName("Rak").build());
		students.add(Student.builder().firstName("John").lastName("Doe").build());
		students.add(Student.builder().firstName("Nikolos").lastName("Flamel").build());
		students.add(Student.builder().firstName("Bernard").lastName("Verber").build());
		students.add(Student.builder().firstName("John").lastName("Snow").build());

		return students;
	}

	public static List<Course> getNewCourses() {
		List<Course> courses = new ArrayList<>();

		courses.add(Course.builder().courseName("biology").courseDescription("anatomia Course").build());
		courses.add(Course.builder().courseName("math").courseDescription("math Course").build());
		courses.add(Course.builder().courseName("art").courseDescription("art Course").build());

		return courses;
	}

	public static List<Group> getNewGroups() {
		List<Group> groups = new ArrayList<>();

		groups.add(Group.builder().groupName("xx-11").students(new HashSet<>()).build());
		groups.add(Group.builder().groupName("xy-15").students(new HashSet<>()).build());
		groups.add(Group.builder().groupName("xz-16").students(new HashSet<>()).build());

		return groups;
	}

	public static List<Teacher> getNewTeachers() {
		List<Teacher> teachers = new ArrayList<>();

		teachers.add(Teacher.builder().firstName("Namas").lastName("Ruko").build());
		teachers.add(Teacher.builder().firstName("Miko").lastName("Milek").build());
		teachers.add(Teacher.builder().firstName("Monek").lastName("Rakow").build());

		return teachers;
	}

	public static List<Lecture> getNewLectures() {
		List<Lecture> lectures = new ArrayList<>();

		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 8, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 10, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 12, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 14, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 16, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 2, 8, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 10, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 12, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 14, 0, 0)).build());
		lectures.add(Lecture.builder().startTime(LocalDateTime.of(2023, 9, 1, 16, 0, 0)).build());

		return lectures;
	}

	public static List<Course> getCourses() {
		List<Course> courses = new ArrayList<>();

		courses.add(new Course((long) 1, "biology", "anatomia Course", "testIntro", null, null, null));
		courses.add(new Course((long) 2, "math", "math Course","testIntro",null , null, null));
		courses.add(new Course((long) 3, "art", "art Course","testIntro",null , null, null));

		setTeachersToCourses(courses, getTeachers());
		return courses;
	}

	private static void setTeachersToCourses(List<Course> courses, List<Teacher> teachers) {
		for (int i = 0; i < courses.size(); i++) {
			if (i < teachers.size())
				courses.get(i).setTeacher(teachers.get(i));
		}
	}

	public static List<Student> getStudents() {
		List<Student> students = new ArrayList<>();

		students.add(Student.builder().id((long) 1).firstName("Pavel").lastName("Pashkin").build());
		students.add(Student.builder().id((long) 2).firstName("Piotr").lastName("Vasiuk").build());
		students.add(Student.builder().id((long) 3).firstName("Tasas").lastName("Mirko").build());
		students.add(Student.builder().id((long) 4).firstName("Tasas").lastName("Zubko").build());
		students.add(Student.builder().id((long) 5).firstName("Sem").lastName("Milek").build());
		students.add(Student.builder().id((long) 6).firstName("Marek").lastName("Rak").build());
		students.add(Student.builder().id((long) 7).firstName("John").lastName("Doe").build());
		students.add(Student.builder().id((long) 8).firstName("Nikolos").lastName("Flamel").build());
		students.add(Student.builder().id((long) 9).firstName("Bernard").lastName("Verber").build());
		students.add(Student.builder().id((long) 10).firstName("John").lastName("Snow").build());

		List<LoginData> loginDatas = getNewLogins();

		setGroupsToStudents(getNewGroups(), students);

		int j = 0;

		for (int i = 0; i < loginDatas.size(); i++) {
			if (j < students.size()) {
				students.get(j).setLogin(loginDatas.get(i));
				loginDatas.get(i).setStudent(students.get(j));
				loginDatas.get(i).setRole(Role.ROLE_STUDENT);
				j++;
			}
		}

		return students;
	}

	public static List<Teacher> getTeachers() {
		List<Teacher> teachers = new ArrayList<>();

		teachers.add(
				Teacher.builder().id((long) 1).firstName("Namas").lastName("Ruko").courses(new HashSet<>()).build());
		teachers.add(
				Teacher.builder().id((long) 2).firstName("Miko").lastName("Milek").courses(new HashSet<>()).build());
		teachers.add(
				Teacher.builder().id((long) 3).firstName("Monek").lastName("Rakow").courses(new HashSet<>()).build());

		List<LoginData> loginDatas = getNewLogins();

		int j = 0;

		for (int i = loginDatas.size() - 1; i >= 0; i--) {
			if (j < teachers.size()) {
				teachers.get(j).setLogin(loginDatas.get(i));
				loginDatas.get(i).setTeacher(teachers.get(j));
				loginDatas.get(i).setRole(Role.ROLE_TEACHER);
				j++;
			}
		}

		setTeachersToCourses(getNewCourses(), teachers);
		return teachers;
	}

	public static List<Group> getGroups() {
		List<Group> groups = new ArrayList<>();

		groups.add(Group.builder().id((long) 1).groupName("xx-11").students(new HashSet<>()).build());
		groups.add(Group.builder().id((long) 2).groupName("xy-15").students(new HashSet<>()).build());
		groups.add(Group.builder().id((long) 3).groupName("xz-16").students(new HashSet<>()).build());

		setGroupsToStudents(groups, getNewStudents());

		return groups;
	}

	public static List<LoginData> getNewLogins() {
		List<LoginData> logins = new ArrayList<>();
		logins.add(LoginData.builder().userName("user1").password("****").build());
		logins.add(LoginData.builder().userName("user2").password("****").build());
		logins.add(LoginData.builder().userName("user3").password("****").build());
		logins.add(LoginData.builder().userName("user4").password("****").build());
		logins.add(LoginData.builder().userName("user5").password("****").build());
		logins.add(LoginData.builder().userName("user6").password("****").build());
		logins.add(LoginData.builder().userName("user7").password("****").build());
		logins.add(LoginData.builder().userName("user8").password("****").build());
		logins.add(LoginData.builder().userName("user9").password("****").build());
		logins.add(LoginData.builder().userName("user10").password("****").build());
		logins.add(LoginData.builder().userName("user11").password("****").build());
		logins.add(LoginData.builder().userName("user12").password("****").build());
		logins.add(LoginData.builder().userName("user13").password("****").build());

		return logins;
	}

	private static List<Group> setGroupsToStudents(List<Group> groups, List<Student> students) {

		for (int i = 0; i < students.size(); i++) {
			if (i < 2) {
				Group group = groups.get(0);
				group.setId(1L);
				students.get(i).setGroup(group);
				group.getStudents().add(students.get(i));
			}
			if (i < 5 && i >= 2) {
				Group group = groups.get(1);
				group.setId(2L);
				students.get(i).setGroup(group);
				group.getStudents().add(students.get(i));
			}
			if (i >= 5) {
				Group group = groups.get(2);
				group.setId(3L);
				students.get(i).setGroup(group);
				group.getStudents().add(students.get(i));
			}
		}

		return groups;
	}

	public static List<Lecture> getLectures() {
		List<Lecture> lectures = getNewLectures();
		List<Course> courses = getCourses();
		int courseInd = 0;
		List<Group> groups = getGroups();
		int groupInd = 0;
		for (int i = 0; i < lectures.size(); i++) {
			lectures.get(i).setId((long) (i + 1));
			lectures.get(i).setCourse(courses.get(courseInd));
			lectures.get(i).setGroups(new HashSet<>());
			lectures.get(i).getGroups().add(groups.get(groupInd));

			courseInd++;
			if (courseInd >= courses.size())
				courseInd = 0;
			groupInd++;
			if (groupInd >= groups.size())
				groupInd = 0;
		}
		return lectures;
	}

	public static Set<FullCalendarEvent> getEvents() {
		Set<FullCalendarEvent> shedule = new HashSet<>();
		FullCalendarEvent event = null;
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		List<Lecture> lectures = getLectures();

		for (Lecture lecture : lectures) {

			event = FullCalendarEvent.builder().id(lecture.getId()).title(lecture.getCourse().getCourseName())
					.start(lecture.getStartTime().format(formatter))
					.end(lecture.getStartTime().plus(Constants.LECTURE_DURATION).format(formatter)).textColor("black")
					.extendedProps(new ArrayList<>()).build();
			shedule.add(event);

		}

		return shedule;
	}

	public static Map<String, String> getScheduleEntityMap() {
		Map<String, String> entityDataMap = new HashMap<>();
		entityDataMap.put("title", "University Fox Minded");
		entityDataMap.put("class", "University");
		entityDataMap.put("id", "0");

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

	public static List<String> getConflictReport() {
		List<String> conflictReport = new ArrayList<>();

		conflictReport.add("Everithing is ok");
		conflictReport.add("TEST TEST");
		conflictReport.add("test TEST test");

		return conflictReport;
	}

	public static List<Image> getImages() {
		List<Image> images = new ArrayList<>();
		images.add(Image.builder().id(1L).fileName("1")
				.data(loadImageToByteArray(
						Paths.get("src", "test", "resources", "testData", "course", "images", "images.jpg").toString()))
				.mediaType(MediaType.IMAGE_JPEG_VALUE).build());
		images.add(
				Image.builder().id(2L).fileName("2")
						.data(loadImageToByteArray(Paths
								.get("src", "test", "resources", "testData", "course", "images", "math-curriculum.webp")
								.toString()))
						.mediaType(MediaType.parseMediaType("image/webp").toString()).build());
		images.add(Image.builder().id(3L).fileName("3")
				.data(loadImageToByteArray(Paths.get("src", "test", "resources", "testData", "course", "images",
						"tablica-z-elementami-matematyki_1411-88.avif").toString()))
				.mediaType(MediaType.parseMediaType("image/avif").toString()).build());

		return images;
	}

	private static byte[] loadImageToByteArray(String imagePath) {
		File imageFile = new File(imagePath);
		try (FileInputStream fis = new FileInputStream(imageFile)) {
			byte[] imageBytes = new byte[(int) imageFile.length()];
			fis.read(imageBytes);
			return imageBytes;
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Return null in case of an error
		}
	}
	
	public static String getIntroText(){
		
		try {
			return String.join("", Files.readAllLines(Paths.get("src", "test", "resources", "testData", "course", "introText.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List<MultipartFile> getMultipartFiles() {
		List<MultipartFile> files = new ArrayList<>();
		files.add(getMultipartFile(Paths.get("src", "test", "resources", "testData", "course", "images", "images.jpg"), "image/jpg"));
		files.add(getMultipartFile(Paths.get("src", "test", "resources", "testData", "course", "images", "math-curriculum.webp"), "image/webp"));
		files.add(getMultipartFile(Paths.get("src", "test", "resources", "testData", "course", "images", "tablica-z-elementami-matematyki_1411-88.avif"), "image/avif"));
		
		
		return files;
	}
	
	private static MultipartFile getMultipartFile(Path path, String contentType) {
		
		String name = path.getFileName().toString();
		String originalFileName = path.getFileName().toString();
		
		byte[] content = null;
		try {
		    content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		return new MockMultipartFile(name,
		                     originalFileName, contentType, content);
	}
	

	
	}
