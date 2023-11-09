package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;

/**
 * This interface defines methods for interacting with Lectures in the database.
 */
@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

	/**
	 * Retrieves a list of lectures scheduled within a specified date range.
	 *
	 * @param startDate The start date and time of the date range.
	 * @param endDate   The end date and time of the date range.
	 * @return A list of Lecture objects that fall within the specified date range.
	 */
	@Query("SELECT l FROM Lecture l WHERE l.startTime >= :startDate AND l.startTime <= :endDate")
	List<Lecture> findLecturesInRange(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Retrieves a list of lectures scheduled within a specified date range and
	 * containing a specific group.
	 *
	 * @param startDate The start date and time of the date range.
	 * @param endDate   The end date and time of the date range.
	 * @param groupId   The ID of the group to filter lectures by.
	 * @return A list of Lecture objects that meet the specified criteria.
	 */
	@Query("SELECT l FROM Lecture l " + "JOIN l.groups g "
			+ "WHERE l.startTime >= :startDate AND l.startTime <= :endDate " + "AND  g.id = :groupId ")
	List<Lecture> findLecturesInRangeAndContainingGroup(LocalDateTime startDate, LocalDateTime endDate, Long groupId);

	/**
	 * Retrieves a list of lectures scheduled within a specified date range and
	 * containing a specific course.
	 *
	 * @param startDate The start date and time of the date range.
	 * @param endDate   The end date and time of the date range.
	 * @param courseId  The ID of the course to filter lectures by.
	 * @return A list of Lecture objects that meet the specified criteria.
	 */
	@Query("SELECT l FROM Lecture l " + "WHERE l.startTime >= :startDate AND l.startTime <= :endDate "
			+ "AND l.course.id = :courseId")
	List<Lecture> findLecturesInRangeAndContainingCourse(LocalDateTime startDate, LocalDateTime endDate, Long courseId);

	/**
	 * Retrieves a list of lectures scheduled at a specific date and time.
	 *
	 * @param dateTime The date and time for which lectures are being queried.
	 * @return A list of Lecture objects scheduled for the specified date and time.
	 */
	List<Lecture> findAllByStartTime(LocalDateTime dateTime);

}
