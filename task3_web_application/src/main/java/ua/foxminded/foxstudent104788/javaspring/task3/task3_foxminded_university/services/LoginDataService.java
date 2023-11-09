package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;

@Service
public interface LoginDataService {
	/**
	 * Adds a new user to the system.
	 *
	 * @param loginData The user to be added.
	 * @return The user entity with rows affected if the user was added
	 *         successfully, or null if an error occurred.
	 */
	LoginData saveNewUser(LoginData loginData);

	/**
	 * Adds a list of users to the system.
	 *
	 * @param loginDatas The list of users to be added.
	 * @return The List of saved users if all users were added successfully, or an
	 *         empty List if an error occurred.
	 */
	List<LoginData> saveAllUsers(List<LoginData> loginDatas);

	/**
	 * Retrieves a user by its ID.
	 *
	 * @param id The ID of the user.
	 * @return An Optional containing the user if found, or an empty Optional if the
	 *         user is not found.
	 */
	Optional<LoginData> getUser(String userName);

	/**
	 * Retrieves all users from the system.
	 *
	 * @return A list of all users.
	 */
	List<LoginData> getAll();

	/**
	 * Updates an existing user in the system.
	 *
	 * @param loginData The user to be updated.
	 * @return The updated entity if the user was updated successfully, or null if
	 *         an error occurred.
	 */
	LoginData updateUser(LoginData loginData);

	/**
	 * Deletes a user from the system.
	 *
	 * @param loginData The user to be deleted.
	 */
	void deleteUser(LoginData loginData);

	/**
	 * Deletes a list of users from the system.
	 *
	 * @param loginDatas The list of users to be deleted.
	 */
	void deleteListOfUsers(List<LoginData> loginDatas);

	/**
	 * Counts the number of users in the system.
	 *
	 * @return The number of users in the system.
	 */
	long countUsers();

	/**
	 * Sets a new role for user.
	 *
	 * @param userName The user name of the user to update.
	 * @param role     The new role to set for the user.
	 * @return The updated LoginData entity.
	 */
	LoginData setNewRole(String userName, Role role);

	/**
	 * Retrieves a userDetails by user name.
	 *
	 * @param userName - user name of the user.
	 * @return An UserDetails containing the user if found or throw usernotfound
	 *         exception if user is not found.
	 */
	UserDetails loadUserByUsername(String username);

	/**
	 * Check is user name unique.
	 *
	 * @param userName - user name of the user.
	 * @return true if name is unique of false if not
	 */
	boolean isUserNameUnique(String userName);

	/**
	 * Sets a new user name for user.
	 *
	 * @param userName The user name of the user to update.
	 * @param password The new user name to set for the user.
	 * @return true is was updated LoginData entity.
	 */
	boolean setNewPassword(String userName, String password);
}
