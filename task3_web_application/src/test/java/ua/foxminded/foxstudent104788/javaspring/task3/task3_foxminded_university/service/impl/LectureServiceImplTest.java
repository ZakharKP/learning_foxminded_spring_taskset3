package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.CoursesRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.GroupsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.LectureServiceImpl;
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		CoursesRepository.class, GroupsRepository.class, TestDataGenerator.class, LectureServiceImpl.class }))
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class LectureServiceImplTest {

	@Autowired
	private LectureServiceImpl lectureService;

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private GroupsRepository groupsRepository;
	
	@Autowired
	private TestDataGenerator dataGenerator;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void beforeEach() {
		when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->{
			return invocation.getArgument(0);
		});
		dataGenerator.run();
	}
	
	@Test
	void testGetStudentsAmount() {
		Lecture lecture = lectureService.getAll().stream().findAny().get();
		
		int expected = lecture.getGroups().stream().map(Group :: getStudents).flatMap(Set :: stream).collect(Collectors.toSet()).size();
		
		int actual = lectureService.getStudentsAmount(lecture.getId());
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLectureStudents() {
		Lecture lecture = lectureService.getAll().stream().findAny().get();
		
		Set<Student> expected = lecture.getGroups().stream().map(Group :: getStudents).flatMap(Set :: stream).collect(Collectors.toSet());
		
		Set<Student> actual = lectureService.getLectureStudents(lecture.getId());
		
		assertEquals(expected, actual);
	}

	@Test
	void testSetLectureNewDateTime() {
		Lecture lecture = lectureService.getAll().stream().findAny().get();
		
		LocalDateTime expected = LocalDateTime.now();
		
		boolean status = lectureService.setLectureNewDateTime(lecture.getId(), expected);
		
		LocalDateTime actual = lectureService.getLecture(lecture.getId()).get().getStartTime();
		
		assertTrue(status);
		assertEquals(expected, actual);
	}

	@Test
	void testSetLectureNewCourse() {
		Lecture lecture = lectureService.getAll().stream().findAny().get();
		
		Course expected = coursesRepository.findAll().stream().filter(x -> !x.equals(lecture.getCourse())).findAny().get();
		
		boolean status = lectureService.setLectureNewCourse(lecture.getId(), expected.getId());
		
		Course actual = lectureService.getLecture(lecture.getId()).get().getCourse();
		
		assertTrue(status);
		assertEquals(expected, actual);
	}

	@Test
	void testRemoveGroupsFromLecture() {
		Lecture lecture = lectureService.getAll().stream().filter(x -> x.getGroups().size() > 1).findAny().get();
		
		Group groupToRemove = lecture.getGroups().stream().findAny().get();
		
		Set<Group> expected = lecture.getGroups();
		
		expected.remove(groupToRemove);
		
		List<Long> selectedGroups = new ArrayList<>();
		selectedGroups.add(groupToRemove.getId());
				
		boolean status = lectureService.removeGroupsFromLecture(lecture.getId(), selectedGroups);
		
		Set<Group> actual = lectureService.getLecture(lecture.getId()).get().getGroups();
		
		assertTrue(status);
		assertEquals(expected, actual);
	}

	@Test
	void testAddGroupsToLecture() {
		Lecture lecture = lectureService.getAll().stream().findAny().get();
		
		Group groupToAdd = groupsRepository.findAll().stream().filter(x -> !lecture.getGroups().contains(x)).findAny().get();
		
		Set<Group> expected = lecture.getGroups();
		
		expected.add(groupToAdd);
		
		List<Long> selectedGroups = new ArrayList<>();
		selectedGroups.add(groupToAdd.getId());
				
		boolean status = lectureService.addGroupsToLecture(lecture.getId(), selectedGroups);
		
		Set<Group> actual = lectureService.getLecture(lecture.getId()).get().getGroups();
		
		assertTrue(status);
		assertEquals(expected, actual);
	}

	@Test
	void testGetLecturesForRangeWithGroup() {
		
		LocalDateTime start = LocalDateTime.of(2023, 9, 10, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2023, 10, 10, 0, 0, 0);
		
		Group group = groupsRepository.findAll().stream()
				.filter(x -> x.getLectures() != null || !x.getLectures()
				.isEmpty()).findAny().get();
		
		List<Lecture> expected = lectureService.getAll()
				.stream().filter(x -> x.getStartTime().isAfter(start) && x.getStartTime().isBefore(end))
				.filter(y -> y.getGroups().contains(group)).collect(Collectors.toList());
			
		List<Lecture> actual = lectureService.getLecturesForRangeWithGroup(group.getId(), start, end);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLecturesForRangeWithCourse() {
		LocalDateTime start = LocalDateTime.of(2023, 9, 10, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2023, 10, 10, 0, 0, 0);
		
		Course course = coursesRepository.findAll().stream()
				.filter(x -> x.getLectures() != null || !x.getLectures()
				.isEmpty()).findAny().get();
		
		List<Lecture> expected = lectureService.getAll()
				.stream().filter(x -> x.getStartTime().isAfter(start) && x.getStartTime().isBefore(end))
				.filter(y -> y.getCourse().equals(course)).collect(Collectors.toList());
			
		List<Lecture> actual = lectureService.getLecturesForRangeWithCourse(course.getId(), start, end);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLecturesForRange() {
		LocalDateTime start = LocalDateTime.of(2023, 9, 10, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(2023, 10, 10, 0, 0, 0);
		
		List<Lecture> expected = lectureService.getAll()
				.stream().filter(x -> x.getStartTime().isAfter(start) && x.getStartTime().isBefore(end))
				.collect(Collectors.toList());
			
		List<Lecture> actual = lectureService.getLecturesForRange(start, end);
		
		assertEquals(expected, actual);
	}

	@Test
	void testGetLecturesForDateTime() {
		LocalDateTime dateTime = lectureService.getAll().stream().findAny().get().getStartTime();
		
		List<Lecture> expected = lectureService.getAll()
				.stream().filter(x -> x.getStartTime().equals(dateTime))
				.collect(Collectors.toList());
			
		List<Lecture> actual = lectureService.getLecturesForDateTime(dateTime);
		
		assertEquals(expected, actual);
	}

}
