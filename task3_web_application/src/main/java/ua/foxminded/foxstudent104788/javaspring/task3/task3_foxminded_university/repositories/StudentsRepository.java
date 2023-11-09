package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;

/**
 * The StudentsRepository interface provides methods for interacting with
 * students in a database.
 */
@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {

	/**
	 * Retrieves a Student's List without group.
	 *
	 * 
	 * @return the Student's List without group
	 */

	List<Student> findAllByGroupIsNull();
}