package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.CoursesRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.GroupsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.LectureRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;

/**
 * Implementation of service interface for managing lectures.
 */
@Log4j2
@Service
@Transactional
public class LectureServiceImpl implements LectureService {

	@Autowired
	private LectureRepository lectureRepository;

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private GroupsRepository groupsRepository;

	@Override
	public Lecture saveNewLecture(Lecture lecture) {
		if (lecture == null) {
			log.info("Can't save - lecture is NULL");
			return null;
		}
		log.info("Start saving new lecture: " + lecture.toString());

		return lectureRepository.save(lecture);
	}

	@Override
	public List<Lecture> saveAllLectures(List<Lecture> lectures) {
		if (lectures == null) {
			log.info("Can't save - lectures is NULL");
			return Collections.emptyList();
		}
		log.info(String.format("Start saving List of %s lectures:", lectures.size()));

		List<Lecture> savedLectures = lectureRepository.saveAll(lectures);

		log.info(savedLectures.size() + " new lectures was saved");
		return savedLectures;

	}

	@Override
	public Optional<Lecture> getLecture(Long id) {
		log.info("Start searching of Lecture by id=" + id);
		return lectureRepository.findById(id);
	}

	@Override
	public List<Lecture> getAll() {
		log.info("Start research of All Lectures");
		return lectureRepository.findAll();
	}

	@Override
	public Lecture updateLecture(Lecture lecture) {
		if (lecture == null) {
			log.info("Can't update - lecture is NULL");
			return null;
		}
		log.info("Starting update " + lecture.toString());
		return lectureRepository.save(lecture);
	}

	@Override
	public void deleteLecture(Lecture lecture) {
		if (lecture == null) {
			log.info("Can't delete - lecture is NULL");
			return;
		}
		log.info("Start deleting lecture: " + lecture.toString());

		lectureRepository.delete(lecture);
	}

	@Override
	public void deleteListOfLectures(List<Lecture> lectures) {
		if (lectures == null) {
			log.info("Can't delete - list of lectures is NULL");
			return;
		}
		log.info(String.format("Start deleting list of %s lectures", lectures.size()));

		lectureRepository.deleteAll(lectures);
	}

	@Override
	public long countLectures() {
		log.info("Counting lectures");
		return lectureRepository.count();
	}

	@Override
	public Integer getStudentsAmount(Long lectureId) {
		log.info("Count Students of Lecture id=" + lectureId);

		return getLectureStudents(lectureId).size();
	}

	@Override
	public Set<Student> getLectureStudents(Long lectureId) {
		log.info("Get Students of Lecture id=" + lectureId);

		Optional<Lecture> lecture = getLecture(lectureId);
		if (lecture.isPresent()) {
			return lecture.get().getGroups().stream().map(Group::getStudents).flatMap(Set::stream)
					.collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

	@Override
	public boolean setLectureNewDateTime(Long lectureId, LocalDateTime newDateTime) {
		log.info("Try set new DateTime for Lecture id=" + lectureId);

		Optional<Lecture> lectureOptional = getLecture(lectureId);
		if (lectureOptional.isPresent()) {
			log.info("Set new DateTime for Lecture id=" + lectureId);

			Lecture lecture = lectureOptional.get();
			lecture.setStartTime(newDateTime);
			updateLecture(lecture);

			return true;
		} else {
			log.info("Can't set new DateTime for Lecture id=" + lectureId);
			log.error("No lecture with id=" + lectureId);

			return false;
		}
	}

	@Override
	public boolean setLectureNewCourse(Long lectureId, Long courseId) {
		log.info("Try set new Course for Lecture id=" + lectureId);

		Optional<Lecture> lectureOptional = getLecture(lectureId);
		Optional<Course> courseOptional = coursesRepository.findById(courseId);

		if (lectureOptional.isPresent() && courseOptional.isPresent()) {
			log.info("Set new Course for Lecture id=" + lectureId);

			Lecture lecture = lectureOptional.get();
			Course course = courseOptional.get();
			if (lecture.getCourse() != null) {
				lecture.getCourse().getLectures().remove(lecture);
			}
			lecture.setCourse(course);

			if (course.getLectures() == null) {
				course.setLectures(new HashSet<>());
			}
			course.getLectures().add(lecture);
			updateLecture(lecture);

			return true;
		} else {
			log.error("No lecture with id={} or no course with id={}", lectureId, courseId);

			return false;
		}
	}

	@Override
	public boolean removeGroupsFromLecture(Long lectureId, List<Long> selectedGroups) {
		log.info("Try remove choosed Groups from Lecture id=" + lectureId);

		Optional<Lecture> lectureOptional = getLecture(lectureId);

		if (lectureOptional.isPresent()) {
			log.info("Remove choosed Groups from Lecture id=" + lectureId);

			Lecture lecture = lectureOptional.get();

			for (Group group : groupsRepository.findAllById(selectedGroups)) {
				group.getLectures().remove(lecture);
				lecture.getGroups().remove(group);
			}
			updateLecture(lecture);

			return true;
		} else {
			log.error("No lecture with id={}", lectureId);

			return false;
		}
	}

	@Override
	public boolean addGroupsToLecture(Long lectureId, List<Long> selectedGroups) {
		log.info("Try add choosed Groups to Lecture id=" + lectureId);

		Optional<Lecture> lectureOptional = getLecture(lectureId);

		if (lectureOptional.isPresent()) {
			log.info("Add selected Groups to Lecture id=" + lectureId);
			Lecture lecture = lectureOptional.get();

			if (lecture.getGroups() == null) {
				lecture.setGroups(new HashSet<>());
			}
			for (Group group : groupsRepository.findAllById(selectedGroups)) {
				if (group.getLectures() == null) {
					group.setLectures(new HashSet<>());
				}
				group.getLectures().add(lecture);
				lecture.getGroups().add(group);
			}
			updateLecture(lecture);

			return true;
		} else {
			log.error("No lecture with id={}", lectureId);
			log.info("Can't add selected groups to Lecture id=" + lectureId);

			return false;
		}
	}

	@Override
	public List<Lecture> getLecturesForRangeWithGroup(Long groupId, LocalDateTime start, LocalDateTime end) {
		log.info("Find lectures for range with Group id=" + groupId);

		return lectureRepository.findLecturesInRangeAndContainingGroup(start, end, groupId);
	}

	@Override
	public List<Lecture> getLecturesForRangeWithCourse(Long courseId, LocalDateTime start, LocalDateTime end) {
		log.info("Find lectures for range with Course id=" + courseId);

		return lectureRepository.findLecturesInRangeAndContainingCourse(start, end, courseId);
	}

	@Override
	public List<Lecture> getLecturesForRange(LocalDateTime start, LocalDateTime end) {
		log.info("Find lectures for range");

		return lectureRepository.findLecturesInRange(start, end);
	}

	@Override
	public List<Lecture> getLecturesForDateTime(LocalDateTime dateTime) {
		log.info("Find lectures for dateTime=" + dateTime);

		return lectureRepository.findAllByStartTime(dateTime);
	}

}
