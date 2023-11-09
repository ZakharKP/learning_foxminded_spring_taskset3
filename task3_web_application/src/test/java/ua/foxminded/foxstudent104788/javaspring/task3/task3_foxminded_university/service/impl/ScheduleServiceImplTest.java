package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.FullCalendarEvent;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.ScheduleServiceImpl;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@SpringBootTest(classes = { ScheduleServiceImpl.class })
class ScheduleServiceImplTest {

	@Autowired
	private ScheduleServiceImpl scheduleService;

	@MockBean
	private LectureService lectureService;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private StudentService studentService;

	@MockBean
	private GroupService groupService;

	@Test
	void testGetEntitySheduleEmpty() {
		Set<FullCalendarEvent> expected = Collections.emptySet();

		Set<FullCalendarEvent> actual = scheduleService.getEntityShedule(1L, "TEST", null, null);

		assertEquals(expected, actual);
	}

	@Test
	void testGetRangeUnivesityShedule() {
		when(lectureService.getLecturesForRange(any(LocalDateTime.class), any(LocalDateTime.class)))
			.thenReturn(ForTestsDataCreator.getLectures());
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		
		scheduleService.getRangeUnivesityShedule(start, end);
		
		verify(lectureService).getLecturesForRange(start, end);
	}

	@Test
	void testGetShedule() {
		Set<Lecture> lectures = ForTestsDataCreator.getLectures().stream().collect(Collectors.toSet());

		try (MockedStatic<ScheduleDataFactory> mocked = mockStatic(ScheduleDataFactory.class)) {
			mocked.when(() -> ScheduleDataFactory.getEventTitle(any(Lecture.class))).thenReturn("test");
			mocked.when(() -> ScheduleDataFactory.getEventProperties(any(Lecture.class), anyInt()))
					.thenReturn(Arrays.asList("TEST, TEST"));
			mocked.when(() -> ScheduleDataFactory.generateLightColorCode(anyString())).thenReturn("color TEST");

			scheduleService.getSchedule(lectures);
			
			mocked.verify(() -> ScheduleDataFactory.getEventTitle(any(Lecture.class)), times(lectures.size()));
			mocked.verify(() -> ScheduleDataFactory.getEventProperties(any(Lecture.class), anyInt()),
					times(lectures.size()));
			mocked.verify(() -> ScheduleDataFactory.generateLightColorCode(anyString()));
		}
	}

	@Test
	void testGetGroupLecturesForRange() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		when(lectureService.getLecturesForRangeWithGroup(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(ForTestsDataCreator.getLectures());

		scheduleService.getGroupLecturesForRange(1L, start, end);
		
		verify(lectureService).getLecturesForRangeWithGroup(1L, start, end);	
	}

	@Test
	void testGetCourseLecturesForRange() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		when(lectureService.getLecturesForRangeWithCourse(anyLong(), any(LocalDateTime.class),
				any(LocalDateTime.class))).thenReturn(ForTestsDataCreator.getLectures());

		scheduleService.getCourseLecturesForRange(1L, start, end);
		
		verify(lectureService).getLecturesForRangeWithCourse(1L, start, end);
	}

	@Test
	void testGetTeacherLecturesForRange() {
		Teacher teacher = ForTestsDataCreator.getTeachers().get(0);
		
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		when(teacherService.getTeacher(1L)).thenReturn(Optional.of(teacher));
		when(lectureService.getLecturesForRangeWithCourse(anyLong(), any(LocalDateTime.class),
				any(LocalDateTime.class))).thenReturn(ForTestsDataCreator.getLectures());

		scheduleService.getTeacherLecturesForRange(1L, start, end);
		
		verify(teacherService).getTeacher(1L);
		verify(lectureService, times(teacher.getCourses().size()))
        .getLecturesForRangeWithCourse(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
	}

	@Test
	void testGetStudentLecturesForRange() {
		Student student = ForTestsDataCreator.getStudents().get(0);
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		when(studentService.getStudent(1L)).thenReturn(Optional.of(student));
		when(lectureService.getLecturesForRangeWithGroup(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(ForTestsDataCreator.getLectures());

		scheduleService.getStudentLecturesForRange(1L, start, end);
		
		verify(studentService).getStudent(1L);
		verify(lectureService).getLecturesForRangeWithGroup(student.getId(), start, end);
	}

	@Test
	void testGetLecturesForRange() {
		when(lectureService.getLecturesForRange(any(LocalDateTime.class), any(LocalDateTime.class)))
			.thenReturn(ForTestsDataCreator.getLectures());
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		
		scheduleService.getLecturesForRange(start, end);
		
		verify(lectureService).getLecturesForRange(start, end);
	}

	@Test
	void testGetConflictReportSuccess() {
		
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		
		List<Group> groups = ForTestsDataCreator.getGroups();

		groups.stream().forEach(x -> x.setId(10l));
		List<Long> groupsIds = groups.stream().map(Group::getId).collect(Collectors.toList());
	
		when(groupService.getGroupsByIds(anyList())).thenReturn(groups);
		
		List<String> expected = new ArrayList<>();
		expected.add("Course: Lecture with that Course in that time is allowed");
		expected.add("xx-11: Lecture with that Group in that time is allowed");
		expected.add("xy-15: Lecture with that Group in that time is allowed");
		expected.add("xz-16: Lecture with that Group in that time is allowed");
		
		List<String> actual = scheduleService.getConflictReport(lectures, 10L, groupsIds);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetConflictReportError() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		
		List<Group> groups = ForTestsDataCreator.getGroups();

		List<Long> groupsIds = groups.stream().map(Group::getId).collect(Collectors.toList());
	
		when(groupService.getGroupsByIds(anyList())).thenReturn(groups);
		
		List<String> expected = new ArrayList<>();
		expected.add("Course: Have another Lecture with that Course in same time");
		expected.add("xx-11: Have another Lecture with that Group in same time");
		expected.add("xy-15: Have another Lecture with that Group in same time");
		expected.add("xz-16: Have another Lecture with that Group in same time");
		
		List<String> actual = scheduleService.getConflictReport(lectures, 1L, groupsIds);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetScheduleEditConflictReportNewDateTime(){
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		LocalDateTime newDateTime = LocalDateTime.now();
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		List<String> expected = new ArrayList<>();
		expected.add("Course: Have another Lecture with that Course in same time");
		expected.add("xx-11: Have another Lecture with that Group in same time");
		
		List<String> actual = scheduleService.getScheduleEditConflictReport(lecture.getId(), newDateTime);
		
		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).getLecturesForDateTime(newDateTime);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetScheduleEditConflictReportNewCourse() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
	
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		List<String> expected = new ArrayList<>();
		expected.add("Course: Have another Lecture with that Course in same time");
		expected.add("xx-11: Have another Lecture with that Group in same time");
		
		List<String> actual = scheduleService.getScheduleEditConflictReport(lecture.getId(), course.getId());
		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).getLecturesForDateTime(lecture.getStartTime());
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetScheduleEditConflictReportGroups() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		List<String> expected = new ArrayList<>();
		expected.add("Course: Have another Lecture with that Course in same time");
		expected.add("xx-11: Have another Lecture with that Group in same time");
		
		List<String> actual = scheduleService.getScheduleEditConflictReport(lecture.getId(), groupsIds);
		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).getLecturesForDateTime(lecture.getStartTime());
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetScheduleCreateConflictReport() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		LocalDateTime newDateTime = LocalDateTime.now();
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		List<String> expected = new ArrayList<>();
		expected.add("Course: Have another Lecture with that Course in same time");
		expected.add("xx-11: Have another Lecture with that Group in same time");
		
		List<String> actual = scheduleService.getScheduleCreateConflictReport(newDateTime, course.getId(), groupsIds.stream().toArray(Long[] :: new));
		
		verify(lectureService).getLecturesForDateTime(newDateTime);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}

	@Test
	void testIsCreateConflict() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		LocalDateTime newDateTime = LocalDateTime.now();
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		boolean expected = true;
		boolean actual = scheduleService.isCreateConflict(newDateTime, 10L, groupsIds.stream().toArray(Long[] :: new));
		
		verify(lectureService).getLecturesForDateTime(newDateTime);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testIsEditConflict(){
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		LocalDateTime newDateTime = LocalDateTime.now();
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		boolean expected = true;
		boolean actual = scheduleService.isEditConflict(lecture.getId(), newDateTime, 10L, groupsIds);
	
		verify(lectureService).getLecturesForDateTime(newDateTime);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}
			

	@Test
	void testSetLectureNewDateTime() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		LocalDateTime newDateTime = LocalDateTime.now();
		
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		
		boolean expected = false;
		boolean actual = scheduleService.setLectureNewDateTime(lecture.getId(), newDateTime);
		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).getLecturesForDateTime(newDateTime);
		
		assertEquals(expected, actual);
	}

	@Test
	void testSetLectureNewCourse() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		LocalDateTime newDateTime = LocalDateTime.now();
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		boolean expected = false;
		boolean actual = scheduleService.setLectureNewCourse(lecture.getId(), course.getId());
		verify(lectureService).getLecture(lecture.getId());
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}

	@Test
	void addGroupsToLecture() {
		List<Lecture> lectures = ForTestsDataCreator.getLectures();
		Lecture lecture = ForTestsDataCreator.getLectures().get(0);
		lecture.getCourse().setId(10L);
		lecture.getGroups().stream().forEach(x -> x.setId(10L));
		LocalDateTime newDateTime = LocalDateTime.now();
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Long> groupsIds = lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList());
		
		when(lectureService.getLecture(anyLong())).thenReturn(Optional.of(lecture));
		when(lectureService.getLecturesForDateTime(any(LocalDateTime.class))).thenReturn(lectures);
		when(lectureService.addGroupsToLecture(anyLong(), anyList())).thenReturn(true);
		when(groupService.getGroupsByIds(anyList())).thenReturn(lecture.getGroups().stream().collect(Collectors.toList()));
		
		boolean expected = true;
		boolean actual = scheduleService.addGroupsToLecture(lecture.getId(), groupsIds);
		verify(lectureService).getLecture(lecture.getId());
		verify(lectureService).getLecturesForDateTime(lecture.getStartTime());
		verify(lectureService).addGroupsToLecture(lecture.getId(), groupsIds);
		verify(groupService).getGroupsByIds(groupsIds);
		
		assertEquals(expected, actual);
	}
}
