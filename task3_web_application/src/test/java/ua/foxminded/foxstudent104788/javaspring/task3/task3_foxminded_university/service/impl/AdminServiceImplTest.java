package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.AdminServiceImpl;

@SpringBootTest(classes = { AdminServiceImpl.class })
class AdminServiceImplTest {

	@Autowired
	private AdminServiceImpl adminService;

	@MockBean
	private StudentService studentService;

	@MockBean
	private GroupService groupService;

	@MockBean
	private CourseService courseService;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private LectureService lectureService;

	@MockBean
	private LoginDataService loginDataService;
	
	@MockBean
	private ScheduleService scheduleService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	void testGetAllTeachers() {
		when(teacherService.getAll()).thenReturn(ForTestsDataCreator.getTeachers());
		
		List<Teacher> expected = ForTestsDataCreator.getTeachers();
		
		List<Teacher> actual = adminService.getAllTeachers();
		
		assertEquals(expected, actual);
		
	}

	@Test
	void testSetAdminRoleToSelectedTeachers() {
		List<Long> ids = new ArrayList<>();
		ids.add((long) 1);
		ids.add((long) 3);

		when(teacherService.getTeacher(anyLong())).thenReturn(Optional.of(ForTestsDataCreator.getTeachers().get(0)));

		when(loginDataService.setNewRole(anyString(), eq(Role.ROLE_ADMIN))).thenReturn(null);

		adminService.setAdminRoleToSelectedTeachers(ids);

		verify(loginDataService, times(2)).setNewRole(anyString(), eq(Role.ROLE_ADMIN));
	}

	@Test
	void testRemoveAdminRoleFromSelectedTeachers() {
		List<Long> ids = new ArrayList<>();
		ids.add((long) 1);
		ids.add((long) 3);

		when(teacherService.getTeacher(anyLong())).thenReturn(Optional.of(ForTestsDataCreator.getTeachers().get(0)));

		when(loginDataService.setNewRole(anyString(), eq(Role.ROLE_TEACHER))).thenReturn(null);

		adminService.removeAdminRoleFromSelectedTeachers(ids);

		verify(loginDataService, times(2)).setNewRole(anyString(), eq(Role.ROLE_TEACHER));

	}

	@Test
	void testGetAllGroups() {
		
		when(groupService.getAll()).thenReturn(ForTestsDataCreator.getGroups());
		
		List<Group> expected = ForTestsDataCreator.getGroups();
		
		List<Group> actual = adminService.getAllGroups();
		
		assertEquals(expected, actual);
		
	}

	@Test
	void testGetAllStudents() {
		
		when(studentService.getAll()).thenReturn(ForTestsDataCreator.getStudents());
		
		List<Student> expected = ForTestsDataCreator.getStudents();
		
		List<Student> actual = adminService.getAllStudents();
		
		assertEquals(expected, actual);
		
	}

	@Test
	void testRegisterNewTeacher() {

		Teacher teacher;
		LoginData login;
		List<Teacher> teachers = ForTestsDataCreator.getTeachers();
		List<LoginData> logins = ForTestsDataCreator.getNewLogins();

		int tindex = teachers.size();
		int lindex = logins.size();

		RegistrationForm form = RegistrationForm.builder().firstName("Niko").lastName("Miko").password("****")
				.userName("test").roleName(Role.ROLE_ADMIN.getRoleName()).build();

		/// save Login
		when(loginDataService.getUser(anyString())).thenAnswer(invocation -> {
			Optional<LoginData> logOptional = logins.stream()
					.filter(x -> x.getUserName().equals(invocation.getArgument(0))).findAny();

			return logOptional;
		});

		when(passwordEncoder.encode(anyString())).thenReturn("****");

		when(loginDataService.saveNewUser(any(LoginData.class))).thenAnswer(invocation -> {
			LoginData newLogin = invocation.getArgument(0);
			logins.add(newLogin);

			return newLogin;

		});

		// save Teacher
		when(teacherService.saveNewTeacher(any(Teacher.class))).thenAnswer(invocation -> {

			Teacher newTeacher = invocation.getArgument(0);
			newTeacher.setId((long) 4);
			teachers.add(newTeacher);

			return newTeacher;
		});

		when(teacherService.setLogin(anyLong(), anyString())).thenAnswer(invocation -> {
			Teacher newTeacher = teachers.get(tindex);
			LoginData newLogin = logins.get(lindex);

			newTeacher.setLogin(newLogin);
			newLogin.setTeacher(newTeacher);

			return newTeacher;
		});

		when(teacherService.getTeacher((long) 4)).thenAnswer(invocation -> {
			return Optional.of(teachers.get(tindex));
		});
		int expected = 1;
		int actual = -2;

		if (adminService.registerNewTeacher(form)) {
			teacher = teachers.get(tindex);
			login = logins.get(lindex);
			actual++;
			if (teacher.getLogin().equals(login)) {
				actual++;
				if (teachers.contains(teacher)) {
					actual++;
				}
			}
		}

		assertEquals(expected, actual);

	}

	@Test
	void testRegisterNewStudent() {

		RegistrationForm form = RegistrationForm.builder().firstName("Niko").lastName("Miko").password("****")
				.userName("test").groupId(1L).roleName(Role.ROLE_ADMIN.getRoleName()).build();

		Student student;

		LoginData login;
		List<Student> students = ForTestsDataCreator.getStudents();
		List<LoginData> logins = ForTestsDataCreator.getNewLogins();

		int sindex = students.size();
		int lindex = logins.size();

		/// save Login
		when(loginDataService.getUser(anyString())).thenAnswer(invocation -> {
			Optional<LoginData> logOptional = ForTestsDataCreator.getNewLogins().stream()
					.filter(x -> x.getUserName().equals(invocation.getArgument(0))).findAny();

			return logOptional;
		});

		when(passwordEncoder.encode(anyString())).thenReturn("****");

		when(loginDataService.saveNewUser(any(LoginData.class))).thenAnswer(invocation -> {
			LoginData newLogin = invocation.getArgument(0);
			logins.add(newLogin);

			return newLogin;
		});

		// save Teacher
		when(studentService.saveNewStudent(any(Student.class))).thenAnswer(invocation -> {

			Student newStudent = invocation.getArgument(0);
			newStudent.setId((long) 11);
			students.add(newStudent);

			return newStudent;
		});

		when(studentService.setGroupToStudent(anyLong(), anyLong())).thenAnswer(invocation -> {
			Student newStudent = students.get(sindex);
			newStudent.setGroup(ForTestsDataCreator.getNewGroup());
			return true;

		});

		when(studentService.setLoginData(anyLong(), anyString())).thenAnswer(invocation -> {
			Student newStudent = students.get(sindex);
			LoginData newLogin = logins.get(lindex);

			newStudent.setLogin(newLogin);
			newLogin.setStudent(newStudent);

			return newStudent;
		});

		when(studentService.getStudent((long) 11)).thenAnswer(invocation -> {
			return Optional.of(students.get(sindex));
		});

		int expected = 1;
		int actual = -2;

		if (adminService.registerNewStudent(form)) {
			student = students.get(sindex);
			login = logins.get(lindex);
			actual++;
			if (student.getLogin().equals(login)) {
				actual++;
				if (students.contains(student)) {
					actual++;
				}
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testDeleteUserTeacher() {
		LoginData user = ForTestsDataCreator.getNewLogin();
		user.setTeacher(ForTestsDataCreator.getNewTeacher());
		List<Teacher> teachers = ForTestsDataCreator.getTeachers();
		teachers.add(user.getTeacher());
		when(loginDataService.getUser(anyString())).thenReturn(Optional.of(user));

		doAnswer(invocation -> {

			teachers.remove(user.getTeacher());
			return null;

		}).when(teacherService).deleteTeacher(any(Teacher.class));

		int expected = 1;
		int actual = -2;

		if (!teachers.contains(user.getTeacher())) {
			actual++;

			assertEquals(expected, actual);
		}
	}

	@Test
	void testDeleteUserStudent() {
		LoginData user = ForTestsDataCreator.getNewLogin();
		user.setStudent(ForTestsDataCreator.getNewStudent());
		List<Student> students = ForTestsDataCreator.getStudents();
		students.add(user.getStudent());
		when(loginDataService.getUser(anyString())).thenReturn(Optional.of(user));

		doAnswer(invocation -> {

			students.remove(user.getStudent());
			return null;

		}).when(studentService).deleteStudent(any(Student.class));

		int expected = 1;
		int actual = -2;

		if (!students.contains(user.getStudent())) {
			actual++;

			assertEquals(expected, actual);
		}
	}

	@Test
	void testCreateNewCourse() {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(1);

		RegistrationForm form = RegistrationForm.builder().courseName("test").courseDescription("test test test")
				.teacherId(teacher.getId()).build();
		Course course = Course.builder().id(2L).courseName(form.getCourseName())
				.courseDescription(form.getCourseDescription()).teacher(teacher).build();

		when(courseService.isCourseNameUnique(form.getCourseName())).thenReturn(true);
		when(courseService.saveNewCourse(any(Course.class))).thenReturn(course);
		when(courseService.setNewTeacherToCourse(course.getId(), teacher.getId())).thenReturn(course);

		int expected = 1;
		int actual = 0;

		if (adminService.createNewCourse(form).equals(course)) {
			actual++;

			assertEquals(expected, actual);
		}

		verify(courseService).isCourseNameUnique(form.getCourseName());
		verify(courseService).saveNewCourse(any(Course.class));
		verify(courseService).setNewTeacherToCourse(course.getId(), teacher.getId());
	}

	@Test
	void testCreateNewGroup() {

		RegistrationForm form = RegistrationForm.builder().groupName("test").studentsIds(new Long[] { 1L, 2L }).build();
		Group group = Group.builder().id(2L).groupName(form.getGroupName()).build();

		List<Long> studentsIds = new ArrayList<>();
		for (Long id : form.getStudentsIds()) {
			studentsIds.add(id);
		}

		when(groupService.isGroupNameUnique(form.getGroupName())).thenReturn(true);
		when(groupService.saveNewGroup(any(Group.class))).thenReturn(group);
		when(groupService.addStudentsToGroup(group.getId(), studentsIds)).thenReturn(group);

		int expected = 1;
		int actual = 0;

		if (adminService.createNewGroup(form)) {
			actual++;

			assertEquals(expected, actual);
		}

		verify(groupService).isGroupNameUnique(form.getGroupName());
		verify(groupService).saveNewGroup(any(Group.class));
		verify(groupService).addStudentsToGroup(group.getId(), studentsIds);
	}

	@Test
	void testGetCoursesWithoutTeacher() {
		when(courseService.getCoursesWithoutTeacher()).thenReturn(ForTestsDataCreator.getNewCourses());
		
		assertFalse(adminService.getCoursesWithoutTeacher().isEmpty());
		
		verify(courseService).getCoursesWithoutTeacher();
		
		
	}

	@Test
	void testSetNewPassword() {
		
		when(loginDataService.setNewPassword("test", "TEST")).thenReturn(true);
		when(passwordEncoder.encode("TEST")).thenReturn("TEST");
		
		adminService.changePassword("test", "TEST");
		
		verify(loginDataService).setNewPassword("test", "TEST");
	}

	@Test
	void testGetAllGroupsCanAddToLecture() {
		List<Group> expected = ForTestsDataCreator.getGroups();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		
		when(groupService.getAll()).thenReturn(expected);
		
		List<Group> actual = adminService.getAllGroupsCanAddToLecture(lecture);
		
		verify(groupService).getAll();
		
		expected.removeAll(lecture.getGroups());
		assertEquals(expected, actual);
	}

	@Test
	void testGetAllCourses() {
		when(courseService.getAll()).thenReturn(ForTestsDataCreator.getCourses());
		adminService.getAllCourses();
		verify(courseService).getAll();
	}

	@Test
	void testGetAmountGroupsCanAddToLecture() {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		int expected = Constants.MAX_ALLOWED_AMOUNT_GROUPS_FOR_LECTURE - lecture.getGroups().size();

		int actual = adminService.getAmountGroupsCanAddToLecture(lecture);

		assertEquals(expected, actual);
	}

	@Test
	void testCreateNewLectureSuccess() {
		
		LocalDateTime newDateTime = LocalDateTime.now();
		Lecture lecture = Lecture.builder().id(1L).startTime(newDateTime).build();
		Lecture newLecture = Lecture.builder().startTime(newDateTime).build();
		
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = ForTestsDataCreator.getGroups().stream().map(Group :: getId).collect(Collectors.toList());
		RegistrationForm form = RegistrationForm.builder()
				.courseId(course.getId()).groupsIds(groupsIds.stream().toArray(Long[] :: new))
				.build();
		
		when(scheduleService.isCreateConflict(any(LocalDateTime.class), anyLong(), any(Long[].class))).thenReturn(false);
		when(lectureService.saveNewLecture(any(Lecture.class))).thenReturn(lecture);
		when(lectureService.setLectureNewCourse(anyLong(), anyLong())).thenReturn(true);
		when(lectureService.addGroupsToLecture(anyLong(), anyList())).thenReturn(true);
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		
		adminService.createNewLecture(form, newDateTime);
		
		verify(scheduleService).isCreateConflict(newDateTime, form.getCourseId(), form.getGroupsIds());
		verify(lectureService).saveNewLecture(newLecture);
		verify(lectureService).setLectureNewCourse(lecture.getId(), form.getCourseId());
		verify(lectureService).addGroupsToLecture(lecture.getId(), Arrays.asList(form.getGroupsIds()));
		verify(lectureService).getLecture(lecture.getId());
		
	}
	
	@Test
	void testCreateNewLectureError() {
		
		LocalDateTime newDateTime = LocalDateTime.now();
		Lecture lecture = Lecture.builder().id(1L).startTime(newDateTime).build();
		Lecture newLecture = Lecture.builder().startTime(newDateTime).build();
		
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = ForTestsDataCreator.getGroups().stream().map(Group :: getId).collect(Collectors.toList());
		RegistrationForm form = RegistrationForm.builder()
				.courseId(course.getId()).groupsIds(groupsIds.stream().toArray(Long[] :: new))
				.build();
		
		when(scheduleService.isCreateConflict(any(LocalDateTime.class), anyLong(), any(Long[].class))).thenReturn(false);
		when(lectureService.saveNewLecture(any(Lecture.class))).thenAnswer(invocation -> {
			lecture.setId(1L);
			return lecture;
		});
		when(lectureService.setLectureNewCourse(anyLong(), anyLong())).thenReturn(true);
		when(lectureService.addGroupsToLecture(anyLong(), anyList())).thenReturn(false);
		doNothing().when(lectureService).deleteLecture(any(Lecture.class));
		
		adminService.createNewLecture(form, newDateTime);
		
		verify(scheduleService).isCreateConflict(newDateTime, form.getCourseId(), form.getGroupsIds());
		verify(lectureService).saveNewLecture(newLecture);
		verify(lectureService).setLectureNewCourse(lecture.getId(), form.getCourseId());
		verify(lectureService).addGroupsToLecture(lecture.getId(), Arrays.asList(form.getGroupsIds()));
		verify(lectureService).deleteLecture(lecture);
		
	}

}
