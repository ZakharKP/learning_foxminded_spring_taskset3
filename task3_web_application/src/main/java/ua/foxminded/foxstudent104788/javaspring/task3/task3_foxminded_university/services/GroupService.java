package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;

/**
 * Service interface for managing groups.
 */
@Service
public interface GroupService {

	/**
	 * Adds a new group to the system.
	 *
	 * @param group The group to be added.
	 * @return The new saved entity if the group was added successfully, or null if
	 *         an error occurred.
	 */
	Group saveNewGroup(Group group);

	/**
	 * Adds a list of groups to the system.
	 *
	 * @param groups The list of groups to be added.
	 * @return The List of saved Groups if all groups were added successfully, or an
	 *         empty List if an error occurred.
	 */
	List<Group> saveAllGroups(List<Group> groups);

	/**
	 * Retrieves a group by its ID.
	 *
	 * @param id The ID of the group.
	 * @return An Optional containing the group if found, or an empty Optional if
	 *         the group is not found.
	 */
	Optional<Group> getGroup(Long id);

	/**
	 * Retrieves all groups from the system.
	 *
	 * @return A list of all groups.
	 */
	List<Group> getAll();

	/**
	 * Updates an existing group in the system.
	 *
	 * @param group The group to be updated.
	 * @return The updated entity if the group was updated successfully, or null if
	 *         an error occurred.
	 */
	Group updateGroup(Group group);

	/**
	 * Deletes a group from the system.
	 *
	 * @param group The group to be deleted.
	 */
	void deleteGroup(Group group);

	/**
	 * Deletes a list of groups from the system.
	 *
	 * @param groups The list of groups to be deleted.
	 */
	void deleteListOfGroups(List<Group> groups);

	/**
	 * Counts the number of groups in the system.
	 *
	 * @return The number of groups in the system.
	 */
	long countGroups();

	/**
	 * Checks if a given group name is unique in the system.
	 *
	 * @param name The group name to check for uniqueness.
	 * @return True if the group name is unique, false otherwise.
	 */
	boolean isGroupNameUnique(String groupName);

	/**
	 * Sets a new name for a group.
	 *
	 * @param groupId   The ID of the group to update.
	 * @param groupName The new name to set for the group.
	 * @return The updated Group entity.
	 */
	boolean setNewGroupName(Long groupId, String groupName);

	/**
	 * Add new students to selected group
	 * 
	 * @param groupId
	 * @param List    of Student's ids
	 * @return updated group
	 */
	Group addStudentsToGroup(Long groupId, List<Long> selectedStudents);

	/**
	 * Remove selected students from selected group
	 * 
	 * @param groupId
	 * @param List    of Student's ids
	 * @return updated group
	 */
	Group removeStudentsFromGroup(Long groupId, List<Long> selectedStudents);

	/**
	 * Edit students for a group.
	 *
	 * @param groupId          The ID of the group to update.
	 * @param selectedStudents Array of selected student's ID.
	 * @param action           command - remove or add chooses students.
	 * @return The updated Group entity.
	 */
	boolean editStudentsForThatGroup(Long groupId, Long[] selectedStudents, String action);

	List<Group> getGroupsByIds(List<Long> asList);
}
