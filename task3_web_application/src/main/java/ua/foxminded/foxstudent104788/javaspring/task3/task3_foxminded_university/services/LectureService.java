package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;

/**
 * Service interface for managing lectures.
 */
@Service
public interface LectureService {

	/**
	 * Adds a new lecture to the system.
	 *
	 * @param lecture The lecture to be added.
	 * @return The new entity if the lecture was added successfully, or null if an
	 *         error occurred.
	 */
	Lecture saveNewLecture(Lecture lecture);

	/**
	 * Adds a list of lectures to the system.
	 *
	 * @param lectures The list of lectures to be added.
	 * @return The List of saved Lectures if all lectures were added successfully,
	 *         or an empty List if an error occurred.
	 */
	List<Lecture> saveAllLectures(List<Lecture> lectures);

	/**
	 * Retrieves a lecture by its ID.
	 *
	 * @param id The ID of the lecture.
	 * @return An Optional containing the lecture if found, or an empty Optional if
	 *         not found.
	 */
	Optional<Lecture> getLecture(Long id);

	/**
	 * Retrieves all lectures from the system.
	 *
	 * @return A list of all lectures.
	 */
	List<Lecture> getAll();

	/**
	 * Updates an existing lecture in the system.
	 *
	 * @param lecture The lecture to be updated.
	 * @return The updated entity if the lecture was updated successfully, or null
	 *         if an error occurred.
	 */
	Lecture updateLecture(Lecture lecture);

	/**
	 * Deletes a lecture from the system.
	 *
	 * @param lecture The lecture to be deleted.
	 */
	void deleteLecture(Lecture lecture);

	/**
	 * Deletes a list of lectures from the system.
	 *
	 * @param lectures The list of lectures to be deleted.
	 */
	void deleteListOfLectures(List<Lecture> lectures);

	/**
	 * Counts the number of lectures in the system.
	 *
	 * @return The number of lectures in the system.
	 */
	long countLectures();

	/**
	 * 
	 * Counts the number of lecture's students
	 * 
	 * @param lectureId
	 * @return students amount
	 */
	Integer getStudentsAmount(Long lectureId);

	/**
	 * 
	 * Set of lecture's students
	 * 
	 * @param lectureId
	 * @return students set
	 */
	Set<Student> getLectureStudents(Long lectureId);

	/**
	 * 
	 * Set new start time for lecture
	 * 
	 * @param lectureId
	 * @param newDate
	 * @param newTime
	 * @return true if date was changed
	 */
	boolean setLectureNewDateTime(Long lectureId, LocalDateTime newDateTime);

	/**
	 * 
	 * Set new start time for lecture
	 * 
	 * @param lectureId
	 * @param courseId
	 * 
	 * @return true if course was set
	 */
	boolean setLectureNewCourse(Long lectureId, Long courseId);

	/**
	 * 
	 * Remove groups from lecture
	 * 
	 * @param lectureId
	 * @param selectedGroups List represent group's IDs selected groups to remove
	 * 
	 * @return true if groups was removed
	 */
	boolean removeGroupsFromLecture(Long lectureId, List<Long> selectedGroups);

	/**
	 * 
	 * Add groups from lecture
	 * 
	 * @param lectureId
	 * @param selectedGroups List represent group's IDs selected groups to add
	 * 
	 * @return true if groups was added
	 */
	boolean addGroupsToLecture(Long lectureId, List<Long> selectedGroups);

	/**
	 * Retrieves a list of lectures within a specific date range that contain a
	 * specific group.
	 *
	 * @param groupId The ID of the group to filter lectures by.
	 * @param start   The start date and time of the date range.
	 * @param end     The end date and time of the date range.
	 * @return A list of Lecture objects that meet the specified criteria.
	 */
	List<Lecture> getLecturesForRangeWithGroup(Long groupId, LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a list of lectures within a specific date range that belong to a
	 * specific course.
	 *
	 * @param courseId The ID of the course to filter lectures by.
	 * @param start    The start date and time of the date range.
	 * @param end      The end date and time of the date range.
	 * @return A list of Lecture objects that meet the specified criteria.
	 */
	List<Lecture> getLecturesForRangeWithCourse(Long courseId, LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a list of lectures within a specific date range.
	 *
	 * @param start The start date and time of the date range.
	 * @param end   The end date and time of the date range.
	 * @return A list of Lecture objects that fall within the specified date range.
	 */
	List<Lecture> getLecturesForRange(LocalDateTime start, LocalDateTime end);

	/**
	 * Retrieves a list of lectures scheduled for a specific date and time.
	 *
	 * @param dateTime The date and time for which lectures are being queried.
	 * @return A list of Lecture objects scheduled for the specified date and time.
	 */
	List<Lecture> getLecturesForDateTime(LocalDateTime dateTime);

}
