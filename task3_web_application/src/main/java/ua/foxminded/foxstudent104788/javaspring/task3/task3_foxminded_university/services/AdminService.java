package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Lecture;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;

/**
 * Service interface for administrative tasks.
 */
@Service
public interface AdminService {

	/**
	 * Retrieves a list of all teachers.
	 *
	 * @return A list of Teacher objects representing all teachers.
	 */
	List<Teacher> getAllTeachers();

	/**
	 * Sets the admin role to selected teachers.
	 *
	 * @param selectedTeacherIds List of IDs of the teachers to set as admin.
	 */
	void setAdminRoleToSelectedTeachers(List<Long> selectedTeacherIds);

	/**
	 * Removes the admin role from selected teachers.
	 *
	 * @param selectedTeacherIds List of IDs of the teachers to remove admin role
	 *                           from.
	 */
	void removeAdminRoleFromSelectedTeachers(List<Long> selectedTeacherIds);

	/**
	 * Retrieves a list of all groups.
	 *
	 * @return A list of Group objects representing all groups.
	 */
	List<Group> getAllGroups();

	/**
	 * Retrieves a list of all students.
	 *
	 * @return A list of Student objects representing all students.
	 */
	List<Student> getAllStudents();

	/**
	 * Registers a new teacher.
	 *
	 * @param form RegistrationForm object containing information for new teacher
	 *             registration.
	 * @return True if the registration was successful, false otherwise.
	 */
	boolean registerNewTeacher(RegistrationForm form);

	/**
	 * Registers a new student.
	 *
	 * @param form RegistrationForm object containing information for new student
	 *             registration.
	 * @return True if the registration was successful, false otherwise.
	 */
	boolean registerNewStudent(RegistrationForm form);

	/**
	 * Deletes a user based on their username.
	 *
	 * @param userName Username of the user to be deleted.
	 */
	void deleteUser(String userName);

	/**
	 * Retrieves login data for a user.
	 *
	 * @param userName Username of the user to retrieve login data for.
	 * @return LoginData object containing login information for the user.
	 */
	LoginData getUser(String userName);

	/**
	 * Creates a new course.
	 *
	 * @param form RegistrationForm object containing information for the new course
	 *             creation.
	 * @return Course object representing the newly created course.
	 */
	Course createNewCourse(RegistrationForm form);

	/**
	 * Retrieves a list of students where group==null.
	 *
	 * @return A list of Student objects representing students where group==null.
	 */
	List<Student> getStudentsWithoutGroup();

	/**
	 * Creates a new group.
	 *
	 * @param form RegistrationForm object containing information for the new group
	 *             creation.
	 * @return Group object representing the newly created course.
	 */
	boolean createNewGroup(RegistrationForm form);

	/**
	 * Retrieves a list of courses where Teacher==null.
	 *
	 * @return A list of Courses objects representing courses where teacher==null.
	 */
	List<Course> getCoursesWithoutTeacher();

	/**
	 * Registers a new user.
	 *
	 * @param form RegistrationForm object containing information for new user
	 *             registration.
	 * @return True if the registration was successful, false otherwise.
	 */
	boolean registerNewUser(RegistrationForm form);

	/**
	 * Sets a new login data(user name or password) for user.
	 *
	 * @param userName    The user name of the user to update.
	 * @param userDetails The new data to set for the user.
	 * @param action      The type of data to set for the user.
	 * @return true if password was changed.
	 */
	boolean changePassword(String userName, String userDetail);

	/**
	 * Retrieves a list of all groups that can be added to a lecture.
	 *
	 * @param lecture The lecture for which groups are being queried.
	 * @return A list of Group objects that can be added to the lecture.
	 */
	List<Group> getAllGroupsCanAddToLecture(Lecture lecture);

	/**
	 * Retrieves a list of all courses.
	 *
	 * @return A list of all available Course objects.
	 */
	List<Course> getAllCourses();

	/**
	 * Gets the number of groups that can be added to a lecture.
	 *
	 * @param lecture The lecture for which the group count is being queried.
	 * @return The number of groups that can be added to the lecture.
	 */
	int getAmountGroupsCanAddToLecture(Lecture lecture);

	/**
	 * Creates a new lecture based on the provided registration form, date, and
	 * time.
	 *
	 * @param form    The registration form containing lecture details.
	 * @param newDate The date of the new lecture.
	 * @param newTime The time of the new lecture.
	 * @return A newly created Lecture object.
	 */
	Lecture createNewLecture(RegistrationForm form, LocalDateTime newDateTime);

}
