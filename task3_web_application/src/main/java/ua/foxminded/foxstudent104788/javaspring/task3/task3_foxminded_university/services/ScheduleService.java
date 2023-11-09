package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.FullCalendarEvent;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;

@Service
public interface ScheduleService {

	Set<FullCalendarEvent> getEntityShedule(Long entityId, String entityClass, LocalDateTime start, LocalDateTime end);

	Set<FullCalendarEvent> getRangeUnivesityShedule(LocalDateTime start, LocalDateTime end);

	Set<FullCalendarEvent> getSchedule(Set<Lecture> lectures);
	
	/**
	 * Retrieves a set of lectures for a specific group within a given date and time range.
	 *
	 * @param groupId The unique identifier of the group.
	 * @param start   The start date and time of the range.
	 * @param end     The end date and time of the range.
	 * @return A set of Lecture objects that match the criteria.
	 */
	Set<Lecture> getGroupLecturesForRange(Long groupId, LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a set of lectures for a specific course within a given date and time range.
	 *
	 * @param courseId The unique identifier of the course.
	 * @param start    The start date and time of the range.
	 * @param end      The end date and time of the range.
	 * @return A set of Lecture objects that match the criteria.
	 */
	Set<Lecture> getCourseLecturesForRange(Long courseId, LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a set of lectures for a specific teacher within a given date and time range.
	 *
	 * @param teacherId The unique identifier of the teacher.
	 * @param start     The start date and time of the range.
	 * @param end       The end date and time of the range.
	 * @return A set of Lecture objects that match the criteria.
	 */
	Set<Lecture> getTeacherLecturesForRange(Long teacherId, LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a set of lectures for a specific student within a given date and time range.
	 *
	 * @param studentId The unique identifier of the student.
	 * @param start     The start date and time of the range.
	 * @param end       The end date and time of the range.
	 * @return A set of Lecture objects that match the criteria.
	 */
	Set<Lecture> getStudentLecturesForRange(Long studentId, LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a set of lectures within a given date and time range.
	 *
	 * @param start The start date and time of the range.
	 * @param end   The end date and time of the range.
	 * @return A set of Lecture objects that match the criteria.
	 */
	Set<Lecture> getLecturesForRange(LocalDateTime start, LocalDateTime end);
	
	/**
	 * Retrieves a report List with answers can schedule be changed for a specific date and course or group.
	 *
	 * @param lecturesFromCheckedDateTime The List of lectures to check.
	 * @param courseId    The unique identifier of the course.
	 * @param groupsIds   An array of group identifiers.
	 * @return A map where keys are messages of validate can that data be added and values indicate if they answered.
	 */
	public List<String> getConflictReport(List<Lecture> lecturesFromCheckedDateTime, Long courseId,	List<Long> groupsIds);
	

	/**
	 * Retrieves a report List with answers with answers about conflict can be if lecture be changed for a specific date
	 *
	 * @param lectureId The id of lecture to be edit.
	 * @param newDateTime The date and time for which answers are needed.
	 * @return A report for any conflict which are present or not.
	 */
	List<String> getScheduleEditConflictReport(Long lectureId, LocalDateTime newDateTime);

	/**
	 * Retrieves a report List with answers with answers about conflict can be if lecture be changed for a specific course
	 *
	 * @param lectureId The id of lecture to be edit.
	 * @param courseId    The unique identifier of the course.
	 * @return A report for any conflict which are present or not.
	 */
	List<String> getScheduleEditConflictReport(Long lectureId, Long newCourseId);
	
	/**
	 * Retrieves a report List with answers about conflict can be if lecture be changed for a specific groups
	 *
	 * @param lectureId The id of lecture to be edit.
	  * @param groupsIds   A List of group identifiers.
	 * @return A report for any conflict which are present or not.
	 */
	List<String> getScheduleEditConflictReport(Long lectureId, List<Long> selectedGroups);

	/**
	 * Retrieves a report map with answers conflict can be if lecture will created with that data.
	 *
	 * @param newDateTime The date and time for which answers are needed.
	 * @param courseId    The unique identifier of the course.
	 * @param groupsIds   An array of group identifiers.
	 * @return A map where keys are messages of validate can that data be added and values indicate if they answered.
	 */
	List<String> getScheduleCreateConflictReport(LocalDateTime newDateTime, Long courseId, Long[] groupsIds);

	boolean isCreateConflict(LocalDateTime newDateTime, Long courseId, Long[] groupsIds);
	
	boolean isEditConflict(Long lectureId, LocalDateTime dateTime, Long courseId, List<Long> groupsIds);

	boolean setLectureNewDateTime(Long lectureId, LocalDateTime newDateTime);

	boolean setLectureNewCourse(Long lectureId, Long courseId);

	boolean addGroupsToLecture(Long lectureId, List<Long> selectedGroups);

}