package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;

@Service
@Transactional
@Log4j2
public class AdminServiceImpl implements AdminService {

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private LoginDataService loginDataService;

	@Autowired
	private LectureService lectureService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Teacher> getAllTeachers() {

		return teacherService.getAll();
	}

	@Override
	public void setAdminRoleToSelectedTeachers(List<Long> selectedTeacherIds) {
		log.info("Start setting ADMIN role to selected teachers");
		Teacher teacher;
		Optional<Teacher> teOptional;
		for (Long id : selectedTeacherIds) {
			log.info("Set ADMIN role to teacher with id=" + id);
			teOptional = teacherService.getTeacher(id);
			if (teOptional.isPresent()) {
				teacher = teOptional.get();

				loginDataService.setNewRole(teacher.getLogin().getUserName(), Role.ROLE_ADMIN);
				log.info("Seted ADMIN role to teacher with id=" + id);
			} else {
				log.info("Teacher wasn't find with id=" + id);
			}
		}

	}

	@Override
	public void removeAdminRoleFromSelectedTeachers(List<Long> selectedTeacherIds) {
		log.info("Start removing ADMIN role from selected teachers");
		Teacher teacher;
		Optional<Teacher> teOptional;
		for (Long id : selectedTeacherIds) {
			log.info("Remove ADMIN role and set TEACHER role to teacher with id=" + id);
			teOptional = teacherService.getTeacher(id);
			if (teOptional.isPresent()) {
				teacher = teOptional.get();

				loginDataService.setNewRole(teacher.getLogin().getUserName(), Role.ROLE_TEACHER);
				log.info("Removed ADMIN role and seted TEACHER role to teacher with id=" + id);
			} else {
				log.info("Teacher wasn't find with id=" + id);
			}
		}

	}

	@Override
	public List<Group> getAllGroups() {

		return groupService.getAll();
	}

	@Override
	public List<Student> getAllStudents() {

		return studentService.getAll();
	}

	@Override
	public boolean registerNewTeacher(RegistrationForm form) {
		log.info("Start redistration New Teacher " + form);
		if (!loginDataService.getUser(form.getUserName()).isPresent()) {

			String userName = form.getUserName();

			LoginData login = LoginData.builder().userName(userName)
					.password(passwordEncoder.encode(form.getPassword())).role(Role.get(form.getRoleName())).build();

			login = loginDataService.saveNewUser(login);

			Teacher teacher = Teacher.builder().firstName(form.getFirstName()).lastName(form.getLastName()).build();

			teacher = teacherService.saveNewTeacher(teacher);

			teacher = teacherService.setLogin(teacher.getId(), userName);

			if (form.getCoursesIds() != null) {
				teacherService.addCoursesToTeacher(teacher.getId(), Arrays.asList(form.getCoursesIds()));
			}

			Optional<Teacher> teacherOpt = teacherService.getTeacher(teacher.getId());
			if (teacherOpt.isPresent()) {

				log.info("New Teacher registered" + teacher);
				return true;
			}
		}
		log.info("Something went wrong trying to register new Teacher");
		return false;
	}

	@Override
	public boolean registerNewStudent(RegistrationForm form) {
		log.info("Start redistration New Student " + form);
		if (!loginDataService.getUser(form.getUserName()).isPresent()) {

			LoginData login = LoginData.builder().userName(form.getUserName())
					.password(passwordEncoder.encode(form.getPassword())).role(Role.get(form.getRoleName())).build();

			login = loginDataService.saveNewUser(login);

			Student student = Student.builder().firstName(form.getFirstName()).lastName(form.getLastName()).build();

			student = studentService.saveNewStudent(student);

			studentService.setGroupToStudent(student.getId(), form.getGroupId());

			student = studentService.setLoginData(student.getId(), login.getUserName());

			Optional<Student> stOptional = studentService.getStudent(student.getId());

			if (stOptional.isPresent()) {
				log.info("New Student registered" + student);
				return true;
			}
		}
		log.info("Something went wrong trying to register new Student");
		return false;
	}

	@Override
	public void deleteUser(String userName) {
		log.info("Deleting user with userName=" + userName);
		Optional<LoginData> loginOpt = loginDataService.getUser(userName);
		if (loginOpt.isPresent()) {
			LoginData login = loginOpt.get();
			if (login.getStudent() != null) {
				log.info("");
				studentService.deleteStudent(login.getStudent());
			}
			if (login.getTeacher() != null) {
				log.info("");
				teacherService.deleteTeacher(login.getTeacher());
			}
			loginDataService.deleteUser(login);
		}
	}

	@Override
	public LoginData getUser(String userName) {
		log.info("Searching of login Data for user with userName=" + userName);

		Optional<LoginData> login = loginDataService.getUser(userName);
		if (login.isPresent()) {
			return login.get();
		}
		log.info("Login Data wasn't found userName=" + userName);

		return null;
	}

	@Override
	public Course createNewCourse(RegistrationForm form) {
		log.info("Creating new course from form =" + form);

		if (courseService.isCourseNameUnique(form.getCourseName())) {
			Course course = Course.builder().courseName(form.getCourseName())
					.courseDescription(form.getCourseDescription()).build();
			course = courseService.saveNewCourse(course);

			log.info("Saved new course=" + course);

			if (form.getTeacherId() != null) {
				courseService.setNewTeacherToCourse(course.getId(), form.getTeacherId());
			}
			return course;
		}
		log.info("Can't create: isn't unique courseName=" + form.getCourseName());

		return null;
	}

	@Override
	public List<Student> getStudentsWithoutGroup() {
		log.info("Looking for all student's without group");

		return studentService.getStudentsWithoutGroup();
	}

	@Override
	public boolean createNewGroup(RegistrationForm form) {
		log.info("Creating new group from form =" + form);

		if (groupService.isGroupNameUnique(form.getGroupName())) {
			Group group = Group.builder().groupName(form.getGroupName()).build();
			group = groupService.saveNewGroup(group);

			log.info("Saved new group=" + group);

			if (form.getStudentsIds() != null) {
				group = groupService.addStudentsToGroup(group.getId(), Arrays.asList(form.getStudentsIds()));
			}
			return true;
		}
		log.info("Can't create: isn't unique groupName=" + form.getGroupName());

		return false;
	}

	@Override
	public List<Course> getCoursesWithoutTeacher() {
		log.info("Collect Courses Without Teacher");
		return courseService.getCoursesWithoutTeacher();
	}

	@Override
	public boolean registerNewUser(RegistrationForm form) {

		if (form.getRoleName().equals(Role.ROLE_ADMIN.getRoleName())
				|| form.getRoleName().equals(Role.ROLE_TEACHER.getRoleName())) {
			return registerNewTeacher(form);
		}
		if (form.getRoleName().equals(Role.ROLE_STUDENT.getRoleName()) && form.getGroupId() != null) {
			return registerNewStudent(form);
		}
		return false;
	}

	@Override
	public boolean changePassword(String userName, String userDetail) {

		return loginDataService.setNewPassword(userName, passwordEncoder.encode(userDetail));
	}

	@Override
	public List<Group> getAllGroupsCanAddToLecture(Lecture lecture) {
		log.info("Get groups can add to lecture" + lecture);
		List<Group> group = getAllGroups();
		group.removeAll(lecture.getGroups());
		return group;
	}

	@Override
	public List<Course> getAllCourses() {
		log.info("Get All Courses");
		return courseService.getAll();
	}

	@Override
	public int getAmountGroupsCanAddToLecture(Lecture lecture) {
		log.info("Calculate Aloowed Amount of groups can add to lecture " + lecture);
		return Constants.MAX_ALLOWED_AMOUNT_GROUPS_FOR_LECTURE - lecture.getGroups().size();
	}

	@Override
	public Lecture createNewLecture(RegistrationForm form, LocalDateTime newDateTime) {
		log.info("Try create new Lecture ");

		if (!scheduleService.isCreateConflict(newDateTime, form.getCourseId(), form.getGroupsIds())) {
			boolean success = false;
			Lecture newLecture = Lecture.builder().startTime(newDateTime).build();

			newLecture = lectureService.saveNewLecture(newLecture);

			success = lectureService.setLectureNewCourse(newLecture.getId(), form.getCourseId());

			if (success) {
				success = lectureService.addGroupsToLecture(newLecture.getId(), Arrays.asList(form.getGroupsIds()));
				Optional<Lecture> lecture = lectureService.getLecture(newLecture.getId());
				if (success && lecture.isPresent()) {
					log.info("Created new Lecture id=" + newLecture.getId());

					return lecture.get();
				}
			}

			lectureService.deleteLecture(newLecture);
		}
		return null;
	}

}
