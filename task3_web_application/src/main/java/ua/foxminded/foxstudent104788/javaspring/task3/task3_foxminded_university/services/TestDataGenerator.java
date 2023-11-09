
package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

/**
 * The TestDataGenerator class is responsible for filling in tables with test
 * data.
 */

@Service
@Log4j2
@Transactional
public class TestDataGenerator implements Runnable {

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
	private LoginDataService loginDataService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Generates test data and filling tables with data. if it is necessary
	 */
	@Override
	@PostConstruct
	public void run() {

		log.info("check if need generate new data");

		if (needGenerateData()) {
			log.info(" Start generating data");

			log.info("start filling in database");
			TestDataCreator testDataCreator = new TestDataCreator();
			fillInDataBaseWithNewData(testDataCreator);

			setLoginData(testDataCreator);

			TestDataRelationshipSetter relationshipSetter = new TestDataRelationshipSetter();
			setRelationships(relationshipSetter);

			log.info("database was update with new test data sucessfully");
		} else {
			log.info("no need generate new data");
		}
	}

	private void setAdmin() {
		Teacher admin = teacherService.getTeacher((long) 1).get();

		loginDataService.setNewRole(admin.getLogin().getUserName(), Role.ROLE_ADMIN);

	}

	private void setLoginData(TestDataCreator testDataCreator) {
		setStudentsLoginData(testDataCreator);
		setTeachersLoginData(testDataCreator);
		setAdmin();

	}

	private void setTeachersLoginData(TestDataCreator testDataCreator) {
		log.info("Start setting for teachers login data");

		for (Teacher teacher : teacherService.getAll()) {
			LoginData login = LoginData.builder().userName("teacher" + teacher.getId())
					.password(passwordEncoder.encode("password" + teacher.getId())).role(Role.ROLE_TEACHER).build();

			teacher.setLogin(login);
			login.setTeacher(teacher);
			loginDataService.saveNewUser(login);
			teacherService.updateTeacher(teacher);

		}
	}

	private void setStudentsLoginData(TestDataCreator testDataCreator) {
		log.info("Start setting for students login data");

		for (Student student : studentService.getAll()) {
			LoginData login = LoginData.builder().userName("student" + student.getId())
					.password(passwordEncoder.encode("password" + student.getId())).role(Role.ROLE_STUDENT).build();
			student.setLogin(login);
			login.setStudent(student);
			loginDataService.saveNewUser(login);
			studentService.updateStudent(student);
		}
	}

	/**
	 * Fills in the database tables with the provided data.
	 * 
	 * @param testDataCreator
	 */
	private void fillInDataBaseWithNewData(TestDataCreator testDataCreator) {

		if (coursesIsEmpty()) {
			fillInCoursesTable(testDataCreator.getNewCourses());
			setCoursesIntro(testDataCreator);
		}
		if (groupsIsEmpty()) {
			fillInGroupTable(testDataCreator.getNewGroups());
		}
		if (studentsIsEmpty()) {
			fillInStudentTable(testDataCreator.getNewStudents());
		}
		if (teachersIsEmpty()) {
			fillInTeacherTable(testDataCreator.getNewTeachers());
		}
		if (lecturesIsEmpty()) {
			fillInLectureTable(testDataCreator.getNewLectures());
		}
	}

	private void setCoursesIntro(TestDataCreator testDataCreator) {		
		for (Course course : courseService.getAll()) {
			setImagesForCourse(course, testDataCreator);
			setIntroTextForCourse(course, testDataCreator);
			courseService.updateCourse(course);
		}

	}

	private void setIntroTextForCourse(Course course, TestDataCreator testDataCreator) {
		course.setIntroText(testDataCreator.getCourseIntroText(course.getCourseName()));
		
	}

	private void setImagesForCourse(Course course, TestDataCreator testDataCreator) {
		List<Image> images = testDataCreator.getImagesForCourse(course.getCourseName());
		images = imageService.saveImages(images);
		courseService.setImages(course.getId(), images.stream().map(Image :: getId).collect(Collectors.toList()));
			
	}

	private void fillInCoursesTable(List<Course> newCourses) {

		log.info("Starting fill in courses table with ");
		courseService.saveAllCourses(newCourses);

	}

	private void fillInGroupTable(List<Group> newGroups) {

		log.info("Starting fill in groups table");
		groupService.saveAllGroups(newGroups);

	}

	private void fillInStudentTable(List<Student> newStudents) {
		log.info("Starting fill in students table");
		studentService.saveAllStudents(newStudents);

	}

	private void fillInTeacherTable(List<Teacher> newTeachers) {
		log.info("Starting fill in lectures table");
		teacherService.saveAllTeachers(newTeachers);

	}

	private void fillInLectureTable(List<Lecture> newLectures) {
		log.info("Starting fill in lectures table");
		lectureService.saveAllLectures(newLectures);

	}

	private void setRelationships(TestDataRelationshipSetter relationshipSetter) {
		setGroupsToStudents(relationshipSetter);
		setTeachersToCourses(relationshipSetter);
		setCoursesAndGroupToLectures(relationshipSetter);

	}

	private void setGroupsToStudents(TestDataRelationshipSetter relationshipSetter) {
		log.info("fill in students with groups");
		List<Student> students = studentService.getAll();
		List<Group> groups = groupService.getAll();
		relationshipSetter.setGroupsToStudents(students, groups);
		relationshipSetter.setStudentsToGroups(students, groups);
		studentService.saveAllStudents(students);
	}

	private void setTeachersToCourses(TestDataRelationshipSetter relationshipSetter) {
		log.info("fill in courses with teachers");
		List<Course> courses = courseService.getAll();
		List<Teacher> teachers = teacherService.getAll();
		relationshipSetter.setTeachersToCourses(teachers, courses);
		relationshipSetter.setCoursesToTeachers(courses, teachers);
		courseService.saveAllCourses(courses);
	}

	private void setCoursesAndGroupToLectures(TestDataRelationshipSetter relationshipSetter) {
		log.info("fill in lectures with groups and courses");
		List<Lecture> lectures = lectureService.getAll();
		List<Group> groups = groupService.getAll();
		List<Course> courses = courseService.getAll();
		relationshipSetter.setCourseAndGroupsToLectures(courses, groups, lectures);
		relationshipSetter.setLecturesToCourses(lectures, courses);
		relationshipSetter.setLecturesToGroups(lectures, groups);
		lectureService.saveAllLectures(lectures);
	}

	private boolean needGenerateData() {
		log.info("Checking if there are empty tables");
		return coursesIsEmpty() || lecturesIsEmpty() || teachersIsEmpty() || studentsIsEmpty() || groupsIsEmpty();
	}

	private boolean coursesIsEmpty() {
		log.info("Checking if courses table is empty");
		return courseService.countCourses() == 0;
	}

	private boolean lecturesIsEmpty() {
		log.info("Checking if lectures table is empty");
		return lectureService.countLectures() == 0;
	}

	private boolean teachersIsEmpty() {
		log.info("Checking if teachers table is empty");
		return teacherService.countTeachers() == 0;
	}

	private boolean studentsIsEmpty() {
		log.info("Checking if students table is empty");
		return studentService.countStudents() == 0;
	}

	private boolean groupsIsEmpty() {
		log.info("Checking if groups table is empty");
		return groupService.countGroups() == 0;
	}

}
