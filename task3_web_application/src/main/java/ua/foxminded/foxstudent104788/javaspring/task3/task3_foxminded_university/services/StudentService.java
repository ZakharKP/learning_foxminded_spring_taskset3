package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

/**
 * Service interface for managing students.
 */
@Service
public interface StudentService {

	/**
	 * Adds an student.
	 *
	 * @param student The student to be added.
	 * @return The new entity if the student was added successfully, or null if an
	 *         error occurred.
	 */
	Student saveNewStudent(Student student);

	/**
	 * Adds List of students.
	 *
	 * @param List of students The students to be added.
	 * @return The List of saved Students if the student was added successfully, or
	 *         empty List if an error occurred.
	 */
	List<Student> saveAllStudents(List<Student> students);

	/**
	 * Gets an student by its ID.
	 *
	 * @param id The ID of the student.
	 * @return An Optional containing the student if found, or an empty Optional if
	 *         not found.
	 */
	Optional<Student> getStudent(Long id);

	/**
	 * Retrieves all students.
	 *
	 * @return A list of all students.
	 */
	List<Student> getAll();

	/**
	 * Update an student.
	 *
	 * @param student The student to be updated.
	 * @return The d entity if the student was updated successfully, or null if an
	 *         error occurred.
	 */
	Student updateStudent(Student student);

	/**
	 * Deletes an student.
	 *
	 * @param student The student to be deleted.
	 */
	void deleteStudent(Student student);

	/**
	 * Deletes List of students..
	 *
	 * @param List of students to be deleted.
	 */
	void deleteListOfStudents(List<Student> students);

	/**
	 * Counts the number of students in the system.
	 *
	 * @return The number of students in the system.
	 */
	long countStudents();

	/**
	 * Retrieves a student by their username.
	 *
	 * @param userName The username of the student.
	 * @return An Optional containing the student if found, or an empty Optional if
	 *         not found.
	 */
	Optional<Student> getStudentByUserName(String userName);

	/**
	 * Sets the login information for a student.
	 *
	 * @param id       The ID of the student to update.
	 * @param userName The username to set for the student's login.
	 * @return The updated Student entity.
	 */
	Student setLoginData(Long id, String userName);

	/**
	 * Sets a group for a student.
	 *
	 * @param studentId The ID of the student to update.
	 * @param groupId   The ID of the group to set for the student.
	 * @return true if new group was setted.
	 */
	boolean setGroupToStudent(Long studentId, Long groupId);

	/**
	 * Gets List of students by their IDs.
	 *
	 * @param id The ID List of the students.
	 * @return List containing the students if found, or an empty List if not found.
	 */
	List<Student> getStudentsByIds(List<Long> selectedStudents);

	/**
	 * Gets List of students without group.
	 *
	 *
	 * @return List containing the students if found, or an empty List if not found.
	 */
	List<Student> getStudentsWithoutGroup();

	/**
	 * Gets List of courses of selected student without group.
	 *
	 * @param id ID of the selected student.
	 * @return List containing courses if found, or an empty List if not found.
	 */
	List<Course> getStudentsCourses(Long id);

	/**
	 * Gets List of teachers of selected student without group.
	 *
	 * @param id ID of the selected student.
	 * @return List containing teachers if found, or an empty List if not found.
	 */
	List<Teacher> getStudentsTeachers(Long id);

	/**
	 * Sets a new First Name for a student.
	 *
	 * @param studentId The ID of the student to update.
	 * @param firstName The new First name to set for the student.
	 * @return boolean true if new name was seted.
	 */
	boolean setFirstName(Long studentId, String firstName);

	/**
	 * Sets a new First Name for a student.
	 *
	 * @param studentId The ID of the student to update.
	 * @param firstName The new First name to set for the student.
	 * @return boolean -true is new last name was setted.
	 */
	boolean setLastName(Long id, String lastName);

}
