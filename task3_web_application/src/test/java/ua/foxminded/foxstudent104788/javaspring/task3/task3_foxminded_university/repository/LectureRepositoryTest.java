package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.CoursesRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.GroupsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.LectureRepository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		LectureRepository.class, CoursesRepository.class, GroupsRepository.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LectureRepositoryTest {
	
	@Autowired
	private LectureRepository repository;
	
	@Autowired
	private GroupsRepository groupsRepository;
	
	@Autowired
	private CoursesRepository coursesRepository;
	
	@Test
	void testFindLecturesInRange() {
		repository.saveAll(ForTestsDataCreator.getNewLectures());
		List<Lecture> expected = repository.findAll().stream().filter(x -> x.getStartTime().isBefore(LocalDateTime.of(2023, 9, 2, 0, 0, 0)) 
				&& x.getStartTime().isAfter(LocalDateTime.of(2023, 9, 1, 0, 0, 0))).collect(Collectors.toList());
		
		List<Lecture> actual = repository.findLecturesInRange(LocalDateTime.of(2023, 9, 1, 0, 0, 0), LocalDateTime.of(2023, 9, 2, 0, 0, 0));
		
		assertEquals(expected, actual);
	}

	@Test
	void testFindLecturesInRangeAndContainingGroup() {
		
		List<Lecture> lectures = repository.saveAll(ForTestsDataCreator.getNewLectures());
		List<Group> groups = groupsRepository.saveAll(ForTestsDataCreator.getNewGroups());
		int groupInd = 0;
		for(int i = 0; i < lectures.size(); i++) {
			if(groups.get(groupInd).getLectures() == null) {
				groups.get(groupInd).setLectures(new HashSet<>());
			}
			if(lectures.get(i).getGroups() == null) {
				lectures.get(i).setGroups(new HashSet<>());
			}
			groups.get(groupInd).getLectures().add(lectures.get(i));
			lectures.get(i).getGroups().add(groups.get(groupInd));
			groupInd++;
			if(groupInd >= groups.size()) {
				groupInd = 0;
			}
		}
		repository.saveAll(lectures);
		
		Group group = groupsRepository.findById(1L).get();
		
		List<Lecture> expected = repository.findAll().stream().filter(x -> x.getStartTime().isBefore(LocalDateTime.of(2023, 9, 2, 0, 0, 0))
				&& x.getStartTime().isAfter(LocalDateTime.of(2023, 9, 1, 0, 0, 0))).filter(y -> y.getGroups().contains(group)).collect(Collectors.toList());
		
		List<Lecture> actual = repository.findLecturesInRangeAndContainingGroup(LocalDateTime.of(2023, 9, 1, 0, 0, 0), LocalDateTime.of(2023, 9, 2, 0, 0, 0), 1L);
		
		assertEquals(expected, actual);
	}

	@Test
	void testFindLecturesInRangeAndContainingCourse() {
		List<Lecture> lectures = repository.saveAll(ForTestsDataCreator.getNewLectures());
		List<Course> courses = coursesRepository.saveAll(ForTestsDataCreator.getNewCourses());
		int courseInd = 0;
		for(int i = 0; i < lectures.size(); i++) {
			if(courses.get(courseInd).getLectures() == null) {
				courses.get(courseInd).setLectures(new HashSet<>());
			}
			
			courses.get(courseInd).getLectures().add(lectures.get(i));
			lectures.get(i).setCourse(courses.get(courseInd));
			courseInd++;
			if(courseInd >= courses.size()) {
				courseInd = 0;
			}
		}
		repository.saveAll(lectures);
		
		Course course = coursesRepository.findById(1L).get();
		
		List<Lecture> expected = repository.findAll().stream().filter(x -> x.getStartTime().isBefore(LocalDateTime.of(2023, 9, 2, 0, 0, 0))
				&& x.getStartTime().isAfter(LocalDateTime.of(2023, 9, 1, 0, 0, 0))).filter(y -> y.getCourse().equals(course)).collect(Collectors.toList());
		
		List<Lecture> actual = repository.findLecturesInRangeAndContainingCourse(LocalDateTime.of(2023, 9, 1, 0, 0, 0), LocalDateTime.of(2023, 9, 2, 0, 0, 0), 1L);
		
		assertEquals(expected, actual);
	}

	@Test
	void testFindAllByStartTimeContaining() {
		repository.saveAll(ForTestsDataCreator.getNewLectures());
		repository.saveAll(ForTestsDataCreator.getNewLectures());
		repository.saveAll(ForTestsDataCreator.getNewLectures());
		repository.saveAll(ForTestsDataCreator.getNewLectures());
		
		List<Lecture> expected = repository.findAll();
		expected = expected.stream().filter(x -> x.getStartTime().equals(LocalDateTime.of(2023, 9, 1, 10, 0, 0))).collect(Collectors.toList());
		
		List<Lecture> actual = repository.findAllByStartTime(LocalDateTime.of(2023, 9, 1, 10, 0, 0));
		
		assertEquals(expected, actual);
	}

}
