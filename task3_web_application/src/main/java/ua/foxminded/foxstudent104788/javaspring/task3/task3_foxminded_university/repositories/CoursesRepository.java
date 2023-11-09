package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;

/**
 * This interface defines methods for interacting with courses in the database.
 */
@Repository
public interface CoursesRepository extends JpaRepository<Course, Long> {

	/**
	 * Retrieves a Course object by its course name.
	 *
	 * @param courseName the course name
	 * @return the Course object with the given name, or null if not found
	 */
	Optional<Course> findByCourseNameContaining(String courseName);

	/**
	 * Retrieves a Courses List without teacher.
	 *
	 * 
	 * @return the Courses List without teacher
	 */

	List<Course> findAllByTeacherIsNull();
}
