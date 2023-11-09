package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;

/**
 * Service interface for managing courses.
 */
@Service
public interface CourseService {

	/**
	 * Adds a new course to the system.
	 *
	 * @param course The course to be added.
	 * @return The course entity with rows affected if the course was added
	 *         successfully, or null if an error occurred.
	 */
	Course saveNewCourse(Course course);

	/**
	 * Adds a list of courses to the system.
	 *
	 * @param courses The list of courses to be added.
	 * @return The List of saved courses if all courses were added successfully, or
	 *         an empty List if an error occurred.
	 */
	List<Course> saveAllCourses(List<Course> courses);

	/**
	 * Retrieves a course by its ID.
	 *
	 * @param id The ID of the course.
	 * @return An Optional containing the course if found, or an empty Optional if
	 *         the course is not found.
	 */
	Optional<Course> getCourse(Long id);

	/**
	 * Retrieves all courses from the system.
	 *
	 * @return A list of all courses.
	 */
	List<Course> getAll();

	/**
	 * Updates an existing course in the system.
	 *
	 * @param course The course to be updated.
	 * @return The updated entity if the course was updated successfully, or null if
	 *         an error occurred.
	 */
	Course updateCourse(Course course);

	/**
	 * Deletes a course from the system.
	 *
	 * @param course The course to be deleted.
	 */
	void deleteCourse(Course course);

	/**
	 * Deletes a list of courses from the system.
	 *
	 * @param courses The list of courses to be deleted.
	 */
	void deleteListOfCourses(List<Course> courses);

	/**
	 * Counts the number of courses in the system.
	 *
	 * @return The number of courses in the system.
	 */
	long countCourses();

	/**
	 * Checks if a given course name is unique in the system.
	 *
	 * @param name The course name to check for uniqueness.
	 * @return True if the course name is unique, false otherwise.
	 */
	boolean isCourseNameUnique(String name);

	/**
	 * Sets a new name for a course.
	 *
	 * @param courseId   The ID of the course to update.
	 * @param courseName The new name to set for the course.
	 * @return The updated Course entity.
	 */
	Course setNewCourseName(Long courseId, String courseName);

	/**
	 * Sets a new description for a course.
	 *
	 * @param courseId          The ID of the course to update.
	 * @param courseDescription The new description to set for the course.
	 * @return The updated Course entity.
	 */
	Course setNewCourseDescription(Long courseId, String courseDescription);

	/**
	 * Sets a new teacher for a course.
	 *
	 * @param courseId  The ID of the course to update.
	 * @param teacherId The ID of the teacher to set for the course.
	 * @return The updated Course entity.
	 */
	Course setNewTeacherToCourse(Long courseId, Long teacherId);

	/**
	 * Retrieves a list of courses where Teacher==null.
	 *
	 * @return A list of Courses objects representing courses where teacher==null.
	 */
	List<Course> getCoursesWithoutTeacher();

	boolean removeImages(Long courseId, List<Long> selectedImages);

	boolean addImages(Long courseId, List<MultipartFile> selectedImages);

	boolean setIntroText(Long courseId, String introText);

	Object[] getAllowedImageCountArray(Long courseId);

	void setImages(Long id, List<Long> imagesIds);
}
