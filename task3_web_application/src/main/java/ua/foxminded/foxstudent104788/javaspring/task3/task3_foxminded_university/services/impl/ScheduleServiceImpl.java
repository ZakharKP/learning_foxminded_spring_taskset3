package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.FullCalendarEvent;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LectureService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ScheduleService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@Service
@Transactional
@Log4j2
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private LectureService lectureService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private GroupService groupService;

	@Override
	public Set<FullCalendarEvent> getEntityShedule(Long entityId, String entityClass, LocalDateTime start,
			LocalDateTime end) {
		log.info("Start prepare shedule for {} with id={}, for peiod start={}, end={}", entityClass, entityId, start,
				end);
		switch (entityClass) {
		case "University":
			return getRangeUnivesityShedule(start, end);
		case "Teacher":
			return getRangeTeacherShedule(entityId, start, end);
		case "Student":
			return getRangeStudentShedule(entityId, start, end);
		case "Course":
			return getRangeCourseShedule(entityId, start, end);
		case "Group":
			return getRangeGroupShedule(entityId, start, end);
		default:
			return Collections.emptySet();
		}

	}

	@Override
	public Set<FullCalendarEvent> getRangeUnivesityShedule(LocalDateTime start, LocalDateTime end) {
		log.info("Start prepare shedule for University, for peiod start={}, end={}", start, end);

		Set<Lecture> lectures = getLecturesForRange(start, end);

		if (!lectures.isEmpty()) {
			log.info("Have lectures for University, for peiod start={}, end={}", start, end);
			return getSchedule(lectures);
		}

		log.info("Can't find lectures for University, for peiod start={}, end={}", start, end);
		return Collections.emptySet();
	}

	private Set<FullCalendarEvent> getRangeGroupShedule(Long entityId, LocalDateTime start, LocalDateTime end) {
		log.info("Start prepare shedule for Group with id={}, for peiod start={}, end={}", entityId, start, end);

		Set<Lecture> lectures = getGroupLecturesForRange(entityId, start, end);

		if (!lectures.isEmpty()) {
			log.info("Have lectures for Group with id={}, for peiod start={}, end={}", entityId, start, end);
			return getSchedule(lectures);
		}

		log.info("Can't find lectures for Group with id={}, for peiod start={}, end={}", entityId, start, end);
		return Collections.emptySet();
	}

	private Set<FullCalendarEvent> getRangeCourseShedule(Long entityId, LocalDateTime start, LocalDateTime end) {
		log.info("Start prepare shedule for Course with id={}, for peiod start={}, end={}", entityId, start, end);

		Set<Lecture> lectures = getCourseLecturesForRange(entityId, start, end);

		if (!lectures.isEmpty()) {
			log.info("Have lectures for Course with id={}, for peiod start={}, end={}", entityId, start, end);
			return getSchedule(lectures);
		}

		log.info("Can't find lectures for Course with id={}, for peiod start={}, end={}", entityId, start, end);
		return Collections.emptySet();
	}

	private Set<FullCalendarEvent> getRangeStudentShedule(Long entityId, LocalDateTime start, LocalDateTime end) {
		log.info("Start prepare shedule for Student with id={}, for peiod start={}, end={}", entityId, start, end);

		Set<Lecture> lectures = getStudentLecturesForRange(entityId, start, end);

		if (!lectures.isEmpty()) {
			log.info("Have lectures for Student with id={}, for peiod start={}, end={}", entityId, start, end);
			return getSchedule(lectures);
		}

		log.info("Can't find lectures for Student with id={}, for peiod start={}, end={}", entityId, start, end);
		return Collections.emptySet();
	}

	private Set<FullCalendarEvent> getRangeTeacherShedule(Long entityId, LocalDateTime start, LocalDateTime end) {
		log.info("Start prepare shedule for Teacher with id={}, for peiod start={}, end={}", entityId, start, end);

		Set<Lecture> lectures = getTeacherLecturesForRange(entityId, start, end);

		if (!lectures.isEmpty()) {
			log.info("Have lectures for Teacher with id={}, for peiod start={}, end={}", entityId, start, end);
			return getSchedule(lectures);
		}

		log.info("Can't find lectures for Teacher with id={}, for peiod start={}, end={}", entityId, start, end);
		return Collections.emptySet();
	}

	@Override
	public Set<FullCalendarEvent> getSchedule(Set<Lecture> lectures) {
		Set<FullCalendarEvent> shedule = new HashSet<>();
		Map<String, String> colors = new HashMap<>();
		FullCalendarEvent event = null;
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

		for (Lecture lecture : lectures) {

			event = FullCalendarEvent.builder().id(lecture.getId()).title(ScheduleDataFactory.getEventTitle(lecture))
					.start(lecture.getStartTime().format(formatter))
					.end(lecture.getStartTime().plus(Constants.LECTURE_DURATION).format(formatter)).textColor("black")
					.extendedProps(ScheduleDataFactory.getEventProperties(lecture,
							lectureService.getStudentsAmount(lecture.getId())))
					.build();
			if (!colors.containsKey(event.getTitle())) {
				colors.put(event.getTitle(), ScheduleDataFactory.generateLightColorCode(event.getTitle()));
			}
			event.setBackgroundColor(colors.get(event.getTitle()));
			shedule.add(event);

		}

		return shedule;
	}

	@Override
	public Set<Lecture> getGroupLecturesForRange(Long groupId, LocalDateTime start, LocalDateTime end) {
		log.info("get Lectures for range with group id=" + groupId);

		return lectureService.getLecturesForRangeWithGroup(groupId, start, end).stream().collect(Collectors.toSet());
	}

	@Override
	public Set<Lecture> getCourseLecturesForRange(Long courseId, LocalDateTime start, LocalDateTime end) {
		log.info("get Lectures for range with group id=" + courseId);

		return lectureService.getLecturesForRangeWithCourse(courseId, start, end).stream().collect(Collectors.toSet());
	}

	@Override
	public Set<Lecture> getTeacherLecturesForRange(Long teacherId, LocalDateTime start, LocalDateTime end) {
		log.info("get Lectures for range with Teacher id=" + teacherId);

		Set<Lecture> lectures = new HashSet<>();

		Optional<Teacher> teacher = teacherService.getTeacher(teacherId);
		if (teacher.isPresent()) {
			log.info("Teacher was finded start collect lectures");
			for (Course course : teacher.get().getCourses()) {
				lectures.addAll(lectureService.getLecturesForRangeWithCourse(course.getId(), start, end));
			}
		}
		return lectures;
	}

	@Override
	public Set<Lecture> getStudentLecturesForRange(Long studentId, LocalDateTime start, LocalDateTime end) {
		log.info("get Lectures for range with Student id=" + studentId);

		Set<Lecture> lectures = new HashSet<>();

		Optional<Student> student = studentService.getStudent(studentId);
		if (student.isPresent()) {
			log.info("Student was finded start collect lectures");

			lectures.addAll(lectureService.getLecturesForRangeWithGroup(student.get().getGroup().getId(), start, end));

		}
		return lectures;
	}

	@Override
	public Set<Lecture> getLecturesForRange(LocalDateTime start, LocalDateTime end) {
		log.info("get Lectures for range ");

		return lectureService.getLecturesForRange(start, end).stream().collect(Collectors.toSet());
	}

	@Override
	public List<String> getConflictReport(List<Lecture> lecturesFromCheckedDateTime, Long courseId,
			List<Long> groupsIds) {
		log.info("Creating Conflict report");

		List<String> conflictReport = new ArrayList<>();

		if (isCourseConflict(lecturesFromCheckedDateTime, courseId)) {
			conflictReport.add("Course: Have another Lecture with that Course in same time");
		} else {
			conflictReport.add("Course: Lecture with that Course in that time is allowed");
		}

		List<Group> groups = groupService.getGroupsByIds(groupsIds);

		for (Group group : groups) {
			if (isGroupConflict(lecturesFromCheckedDateTime, group)) {
				conflictReport.add(group.getGroupName() + ": Have another Lecture with that Group in same time");
			} else {
				conflictReport.add(group.getGroupName() + ": Lecture with that Group in that time is allowed");
			}
		}

		return conflictReport;
	}

	@Override
	public List<String> getScheduleEditConflictReport(Long lectureId, LocalDateTime newDateTime) {
		log.info("Creating Conflict report for new Date Time");

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);
		if (leOptional.isPresent()) {
			Lecture lecture = leOptional.get();

			List<Lecture> lectures = lectureService.getLecturesForDateTime(newDateTime);
			lectures = lectures.stream().filter(x -> !x.getId().equals(lectureId)).collect(Collectors.toList());

			return getConflictReport(lectures, lecture.getCourse().getId(),
					lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList()));
		}

		log.error("Can't find lecture id=" + lectureId);
		return Arrays.asList("Error! Can't find lecture");
	}

	@Override
	public List<String> getScheduleEditConflictReport(Long lectureId, Long newCourseId) {
		log.info("Creating Conflict report for new Course id=" + newCourseId);

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);
		if (leOptional.isPresent()) {
			Lecture lecture = leOptional.get();

			List<Lecture> lectures = lectureService.getLecturesForDateTime(lecture.getStartTime());
			lectures = lectures.stream().filter(x -> !x.getId().equals(lectureId)).collect(Collectors.toList());

			return getConflictReport(lectures, newCourseId,
					lecture.getGroups().stream().map(Group::getId).collect(Collectors.toList()));
		}

		return Arrays.asList("Error! Can't find lecture");
	}

	@Override
	public List<String> getScheduleEditConflictReport(Long lectureId, List<Long> selectedGroups) {
		log.info("Creating Conflict report for selected groups");

		Optional<Lecture> leOptional = lectureService.getLecture(lectureId);
		if (leOptional.isPresent()) {
			Lecture lecture = leOptional.get();

			List<Lecture> lectures = lectureService.getLecturesForDateTime(lecture.getStartTime());
			lectures = lectures.stream().filter(x -> !x.getId().equals(lectureId)).collect(Collectors.toList());

			return getConflictReport(lectures, lecture.getCourse().getId(), selectedGroups);
		}

		return Arrays.asList("Error! Can't find lecture");
	}

	@Override
	public List<String> getScheduleCreateConflictReport(LocalDateTime newDateTime, Long courseId, Long[] groupsIds) {

		List<Lecture> lectures = lectureService.getLecturesForDateTime(newDateTime);

		return getConflictReport(lectures, courseId, Arrays.asList(groupsIds));
	}

	@Override
	public boolean isCreateConflict(LocalDateTime newDateTime, Long courseId, Long[] groupsIds) {
		log.info("Checking is create conflict conflict for lecturs");

		List<Lecture> lectures = lectureService.getLecturesForDateTime(newDateTime);

		return isCourseConflict(lectures, courseId) || groupService.getGroupsByIds(Arrays.asList(groupsIds)).stream()
				.anyMatch(x -> isGroupConflict(lectures, x));
	}

	@Override
	public boolean isEditConflict(Long lectureId, LocalDateTime dateTime, Long courseId, List<Long> groupsIds) {
		log.info("Checking is edit conflict conflict for lecture");

		List<Lecture> lectures = lectureService.getLecturesForDateTime(dateTime).stream()
				.filter(x -> !x.getId().equals(lectureId)).collect(Collectors.toList());

		return isCourseConflict(lectures, courseId)
				|| groupService.getGroupsByIds(groupsIds).stream().anyMatch(x -> isGroupConflict(lectures, x));
	}

	@Override
	public boolean setLectureNewDateTime(Long lectureId, LocalDateTime newDateTime) {
		log.info("Try set new Date Tame to lecture id=" + lectureId);
		Optional<Lecture> lecture = lectureService.getLecture(lectureId);
		if (lecture.isPresent() && !isEditConflict(lectureId, newDateTime, lecture.get().getCourse().getId(),
				lecture.get().getGroups().stream().map(Group::getId).collect(Collectors.toList()))) {
			return lectureService.setLectureNewDateTime(lectureId, newDateTime);

		}
		log.info("Can't set new Date Tame to lecture id=" + lectureId);

		return false;
	}

	@Override
	public boolean setLectureNewCourse(Long lectureId, Long courseId) {
		log.info("Try set new Course to lecture id=" + lectureId);
		Optional<Lecture> lecture = lectureService.getLecture(lectureId);
		if (lecture.isPresent() && !isEditConflict(lectureId, lecture.get().getStartTime(), courseId,
				lecture.get().getGroups().stream().map(Group::getId).collect(Collectors.toList()))) {
			return lectureService.setLectureNewCourse(lectureId, courseId);

		}
		log.info("Can't set new Course to lecture id=" + lectureId);

		return false;
	}

	@Override
	public boolean addGroupsToLecture(Long lectureId, List<Long> selectedGroups) {
		log.info("Try setadd new Groups to lecture id=" + lectureId);
		Optional<Lecture> lecture = lectureService.getLecture(lectureId);
		if (lecture.isPresent() && !isEditConflict(lectureId, lecture.get().getStartTime(),
				lecture.get().getCourse().getId(), selectedGroups)) {
			return lectureService.addGroupsToLecture(lectureId, selectedGroups);

		}
		log.info("Can't add new Groups to lecture id=" + lectureId);

		return false;
	}

	private boolean isGroupConflict(List<Lecture> lectures, Group group) {
		log.info("Checking is groups conflict for lectures");

		return !lectures.stream().filter(x -> x.getGroups().contains(group)).collect(Collectors.toList()).isEmpty();
	}

	private boolean isCourseConflict(List<Lecture> lectures, Long courseId) {
		log.info("Checking is course conflict for lectures");

		return !lectures.stream().filter(x -> x.getCourse().getId().equals(courseId)).collect(Collectors.toList())
				.isEmpty();
	}

}
