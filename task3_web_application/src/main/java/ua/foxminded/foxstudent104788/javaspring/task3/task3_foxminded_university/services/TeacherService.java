package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

/**
 * Service interface for managing teachers.
 */
@Service
public interface TeacherService {

	/**
	 * Adds an teacher.
	 *
	 * @param teacher The teacher to be added.
	 * @return The new entity if the teacher was added successfully, or null if an
	 *         error occurred.
	 */
	Teacher saveNewTeacher(Teacher teacher);

	/**
	 * Adds List of teachers.
	 *
	 * @param List of teachers The teachers to be added.
	 * @return The List of saved Teachers if the teacher was added successfully, or
	 *         empty List if an error occurred.
	 */
	List<Teacher> saveAllTeachers(List<Teacher> teachers);

	/**
	 * Gets an teacher by its ID.
	 *
	 * @param id The ID of the teacher.
	 * @return An Optional containing the teacher if found, or an empty Optional if
	 *         not found.
	 */
	Optional<Teacher> getTeacher(Long id);

	/**
	 * Retrieves all teachers.
	 *
	 * @return A list of all teachers.
	 */
	List<Teacher> getAll();

	/**
	 * Update an teacher.
	 *
	 * @param teacher The teacher to be updated.
	 * @return The d entity if the teacher was updated successfully, or null if an
	 *         error occurred.
	 */
	Teacher updateTeacher(Teacher teacher);

	/**
	 * Deletes an teacher.
	 *
	 * @param teacher The teacher to be deleted.
	 */
	void deleteTeacher(Teacher teacher);

	/**
	 * Deletes List of teachers..
	 *
	 * @param List of teachers to be deleted.
	 */
	void deleteListOfTeachers(List<Teacher> teachers);

	/**
	 * Counts the number of teachers in the system.
	 *
	 * @return The number of teachers in the system.
	 */
	long countTeachers();

	/**
	 * Retrieves a teacher by their username.
	 *
	 * @param userName The username of the teacher.
	 * @return An Optional containing the teacher if found, or an empty Optional if
	 *         not found.
	 */
	Optional<Teacher> getTeacherByUserName(String userName);

	/**
	 * Sets the login information for a teacher.
	 *
	 * @param teacherId The ID of the teacher to update.
	 * @param userName  The username to set for the teacher's login.
	 * @return The updated Teacher entity.
	 */
	Teacher setLogin(Long teacherId, String userName);

	/**
	 * Retrieves all groups what teacher learn.
	 *
	 * @param id teacher ID
	 * @return A list of teachers group.
	 */
	List<Group> getTeachersGroups(Long id);

	/**
	 * Retrieves List of all students what teacher learn.
	 *
	 * @param id teacher ID
	 * @return A list of teachers Students.
	 */
	List<Student> getTeachersStudents(Long id);

	/**
	 * Sets a new First Name for a teacher.
	 *
	 * @param teacherId The ID of the teacher to update.
	 * @param firstName The new First name to set for the teacher.
	 * @return true if updated Teacher entity.
	 */
	boolean setFirstName(Long teacherId, String firstName);

	/**
	 * Sets a new First Name for a teacher.
	 *
	 * @param teacherId The ID of the teacher to update.
	 * @param firstName The new First name to set for the teacher.
	 * @return true if was updated Teacher entity.
	 */
	boolean setLastName(Long id, String lastName);

	/**
	 * Add new courses to selected teacher
	 * 
	 * @param teacherId
	 * @param List      of Course's ids
	 * @return true if was updated teacher
	 */
	boolean addCoursesToTeacher(Long teacherId, List<Long> selectedCourses);

	/**
	 * Remove selected courses from selected teacher
	 * 
	 * @param teacherId
	 * @param List      of Course's ids
	 * @return true if was updated teacher
	 */
	boolean removeCoursesFromTeacher(Long teacherId, List<Long> selectedCourses);

	/**
	 * Edit selected courses for selected teacher
	 * 
	 * @param teacherId
	 * @param List      of Course's ids
	 * @return boolean if was updated teacher
	 */
	boolean editCoursesToSelectedTeacher(Long teacherId, Long[] selectedCourses, String action);

}
