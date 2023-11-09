package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@WebMvcTest(LecturesController.class)
class LecturesControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LectureService lectureService;

	@MockBean
	private AdminService adminService;

	@MockBean
	private ScheduleService scheduleService;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowLectureDetails() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getStudentsAmount(anyLong())).thenReturn(15);

		mvc.perform(get("/lecture/details").with(csrf()).param("lectureId", lecture.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("lecture/details"))
				.andExpect(model().attribute("lecture", lecture)).andExpect(model().attribute("studentAmount", 15));

		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).getStudentsAmount(lecture.getId());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowLecturesStudentsList() throws Exception {
		Long lectureId = 1L;

		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		Set<Student> students = ForTestsDataCreator.getStudents().stream().collect(Collectors.toSet());

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getLectureStudents(anyLong())).thenReturn(students);

		mvc.perform(get("/lecture/students").with(csrf()).param("lectureId", lectureId.toString()))
				.andExpect(status().isOk()).andExpect(view().name("students"))
				.andExpect(model().attribute("students", students))
				.andExpect(model().attribute("caller", "Lecture id=" + lectureId));

		verify(lectureService).getLecture(lectureId);
		verify(lectureService).getLectureStudents(lectureId);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowLectureEditor() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		List<Group> groups = ForTestsDataCreator.getGroups();
		groups.removeAll(lecture.getGroups());

		int groupsCanAdd = Constants.MAX_ALLOWED_AMOUNT_GROUPS_FOR_LECTURE - lecture.getGroups().size();

		when(adminService.getAmountGroupsCanAddToLecture(any(Lecture.class))).thenReturn(groupsCanAdd);
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(adminService.getAllGroupsCanAddToLecture(any(Lecture.class))).thenReturn(groups);
		when(adminService.getAllCourses()).thenReturn(ForTestsDataCreator.getCourses());

		mvc.perform(get("/lecture/editor").with(csrf()).param("lectureId", lecture.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("lecture/editor"))
				.andExpect(model().attribute("lecture", lecture))
				.andExpect(model().attribute("courses", ForTestsDataCreator.getCourses()))
				.andExpect(model().attribute("allowedTime", ScheduleDataFactory.getAllowedTime()))
				.andExpect(model().attribute("groups", groups))
				.andExpect(model().attribute("maxGroupsCanAdd", groupsCanAdd));

		verify(lectureService).getLecture(lecture.getId());
		verify(adminService).getAmountGroupsCanAddToLecture(lecture);
		verify(adminService).getAllGroupsCanAddToLecture(lecture);
		verify(adminService).getAllCourses();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEdtitDateTimeSuccess() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		LocalDate newDate = LocalDate.of(2023, 9, 1);
		LocalTime newTime = LocalTime.of(0, 0, 0);
		LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(scheduleService.setLectureNewDateTime(anyLong(), any(LocalDateTime.class))).thenReturn(true);

		mvc.perform(post("/lecture/editor/date_time").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("newDate", newDate.format(dateFormatter)).param("newTime", newTime.format(timeFormatter)))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/lecture/editor?success&lectureId=1&message=Date+and+Time+was+changed"))
				.andExpect(model().attribute("lectureId", lecture.getId()));

		verify(lectureService).getLecture(lecture.getId());
		verify(scheduleService).setLectureNewDateTime(lecture.getId(), newDateTime);

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEdtitDateTimeError() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		List<String> conflictReport = ForTestsDataCreator.getConflictReport();
		LocalDate newDate = LocalDate.of(2023, 9, 1);
		LocalTime newTime = LocalTime.of(0, 0, 0);
		LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(scheduleService.getScheduleEditConflictReport(anyLong(), any(LocalDateTime.class)))
				.thenReturn(conflictReport);
		when(scheduleService.setLectureNewDateTime(anyLong(), any(LocalDateTime.class))).thenReturn(false);

		mvc.perform(post("/lecture/editor/date_time").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("newDate", newDate.format(dateFormatter)).param("newTime", newTime.format(timeFormatter)))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(
						"/lecture/editor?error&lectureId=1&message=Everithing+is+ok&message=TEST+TEST&message=test+TEST+test"))
				.andExpect(model().attribute("lectureId", lecture.getId()))
				.andExpect(model().attribute("message", conflictReport));

		verify(lectureService).getLecture(lecture.getId());
		verify(scheduleService).getScheduleEditConflictReport(lecture.getId(), newDateTime);
		verify(scheduleService).setLectureNewDateTime(lecture.getId(), newDateTime);

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditCourseSuccess() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		Course course = ForTestsDataCreator.getCourses().get(0);

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(scheduleService.setLectureNewCourse(anyLong(), anyLong())).thenReturn(true);

		mvc.perform(post("/lecture/editor/course").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("courseId", course.getId().toString())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/lecture/editor?success&lectureId=1&message=Course+was+Changed"))
				.andExpect(model().attribute("lectureId", lecture.getId()))
				.andExpect(model().attribute("message", "Course was Changed"));

		verify(lectureService).getLecture(lecture.getId());
		verify(scheduleService).setLectureNewCourse(lecture.getId(), course.getId());

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditCourseError() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		Course course = ForTestsDataCreator.getCourses().get(0);

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(scheduleService.setLectureNewCourse(anyLong(), anyLong())).thenReturn(false);
		when(scheduleService.getScheduleEditConflictReport(anyLong(), anyLong()))
				.thenReturn(ForTestsDataCreator.getConflictReport());

		mvc.perform(post("/lecture/editor/course").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("courseId", course.getId().toString())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(
						"/lecture/editor?error&lectureId=1&message=Everithing+is+ok&message=TEST+TEST&message=test+TEST+test"))
				.andExpect(model().attribute("lectureId", lecture.getId()))
				.andExpect(model().attribute("message", ForTestsDataCreator.getConflictReport()));

		verify(lectureService).getLecture(lecture.getId());
		verify(scheduleService).setLectureNewCourse(lecture.getId(), course.getId());
		verify(scheduleService).getScheduleEditConflictReport(lecture.getId(), course.getId());

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testRemoveGroupsFromLecture() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		List<Long> groupsIds = ForTestsDataCreator.getGroups().stream().map(Group::getId).collect(Collectors.toList());

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.removeGroupsFromLecture(anyLong(), anyList())).thenReturn(true);

		mvc.perform(post("/lecture/editor/groups/remove").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("selectedGroups", groupsIds.stream().map(id -> id.toString()).toArray(String[]::new)))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/lecture/editor?success&lectureId=1&message=Groups+was+removed"))
				.andExpect(model().attribute("lectureId", lecture.getId()))
				.andExpect(model().attribute("message", "Groups was removed"));

		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).removeGroupsFromLecture(lecture.getId(), groupsIds);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testAddGroupsToLectureSuccess() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		List<Long> groupsIds = ForTestsDataCreator.getGroups().stream().map(Group::getId).collect(Collectors.toList());

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(scheduleService.addGroupsToLecture(anyLong(), anyList())).thenReturn(true);

		mvc.perform(post("/lecture/editor/groups/add").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("selectedGroups", groupsIds.stream().map(id -> id.toString()).toArray(String[]::new)))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/lecture/editor?success&lectureId=1&message=Groups+was+added"))
				.andExpect(model().attribute("lectureId", lecture.getId()))
				.andExpect(model().attribute("message", "Groups was added"));

		verify(lectureService).getLecture(lecture.getId());
		verify(scheduleService).addGroupsToLecture(lecture.getId(), groupsIds);

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testAddGroupsToLectureError() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		List<String> conflictReport = ForTestsDataCreator.getConflictReport();
		List<Long> groupsIds = ForTestsDataCreator.getGroups().stream().map(Group::getId).collect(Collectors.toList());

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(scheduleService.addGroupsToLecture(anyLong(), anyList())).thenReturn(false);
		when(scheduleService.getScheduleEditConflictReport(anyLong(), anyList())).thenReturn(conflictReport);

		mvc.perform(post("/lecture/editor/groups/add").with(csrf()).param("lectureId", lecture.getId().toString())
				.param("selectedGroups", groupsIds.stream().map(id -> id.toString()).toArray(String[]::new)))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl(
						"/lecture/editor?error&lectureId=1&message=Everithing+is+ok&message=TEST+TEST&message=test+TEST+test"))
				.andExpect(model().attribute("lectureId", lecture.getId()))
				.andExpect(model().attribute("message", conflictReport));

		verify(lectureService).getLecture(lecture.getId());
		verify(scheduleService).addGroupsToLecture(lecture.getId(), groupsIds);
		verify(scheduleService).getScheduleEditConflictReport(lecture.getId(), groupsIds);

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowCreation() throws Exception {
		RegistrationForm form = new RegistrationForm();

		when(adminService.getAllGroups()).thenReturn(ForTestsDataCreator.getGroups());
		when(adminService.getAllCourses()).thenReturn(ForTestsDataCreator.getCourses());

		mvc.perform(get("/lecture/creation")).andExpect(status().isOk()).andExpect(view().name("lecture/creation"))
				.andExpect(model().attribute("form", form))
				.andExpect(model().attribute("courses", ForTestsDataCreator.getCourses()))
				.andExpect(model().attribute("allowedTime", ScheduleDataFactory.getAllowedTime()))
				.andExpect(model().attribute("groups", ForTestsDataCreator.getGroups()))
				.andExpect(model().attribute("maxNewGroups", Constants.MAX_ALLOWED_AMOUNT_GROUPS_FOR_LECTURE));

		verify(adminService).getAllGroups();
		verify(adminService).getAllCourses();

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testAddNewLectureSuccess() throws Exception {
		RegistrationForm form = RegistrationForm.builder().courseId(1L).groupsIds(new Long[] { 1L, 2L }).build();

		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		LocalDate newDate = LocalDate.of(2023, 9, 1);
		LocalTime newTime = LocalTime.of(0, 0, 0);
		LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;

		when(adminService.createNewLecture(any(RegistrationForm.class), any(LocalDateTime.class))).thenReturn(lecture);
		mvc.perform(post("/lecture/create").with(csrf()).flashAttr("form", form)
				.param("newDate", newDate.format(dateFormatter)).param("newTime", newTime.format(timeFormatter)))
				.andExpect(model().attribute("lectureId", lecture.getId())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/lecture/details?lectureId=1"));

		verify(adminService).createNewLecture(form, newDateTime);

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testAddNewLectureError() throws Exception {
		RegistrationForm form = RegistrationForm.builder().courseId(1L).groupsIds(new Long[] { 1L, 2L }).build();

		LocalDate newDate = LocalDate.of(2023, 9, 1);
		LocalTime newTime = LocalTime.of(0, 0, 0);
		LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;

		when(adminService.createNewLecture(any(RegistrationForm.class), any(LocalDateTime.class))).thenReturn(null);

		when(scheduleService.getScheduleCreateConflictReport(any(LocalDateTime.class), anyLong(), any(Long[].class)))
				.thenReturn(ForTestsDataCreator.getConflictReport());

		mvc.perform(post("/lecture/create").with(csrf()).flashAttr("form", form)
				.param("newDate", newDate.format(dateFormatter)).param("newTime", newTime.format(timeFormatter)))
				.andExpect(model().attribute("message", ForTestsDataCreator.getConflictReport()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(
						"/lecture/creation?error&message=Everithing+is+ok&message=TEST+TEST&message=test+TEST+test"));

		verify(adminService).createNewLecture(form, newDateTime);
		verify(scheduleService).getScheduleCreateConflictReport(newDateTime, form.getCourseId(), form.getGroupsIds());

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowDeleteConfirmation() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));

		mvc.perform(get("/lecture/delete").with(csrf()).param("lectureId", lecture.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("lecture/delete"))
				.andExpect(model().attribute("lecture", lecture));

		verify(lectureService).getLecture(anyLong());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testDelLecture() throws Exception {
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);

		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		doNothing().when(lectureService).deleteLecture(lecture);

		mvc.perform(post("/lecture/del").with(csrf()).param("lectureId", lecture.getId().toString()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/schedule"));

		verify(lectureService).getLecture(anyLong());
		verify(lectureService).deleteLecture(lecture);
	}
}
