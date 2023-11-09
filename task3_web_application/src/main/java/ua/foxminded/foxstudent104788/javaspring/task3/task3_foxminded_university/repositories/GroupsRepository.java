package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;

/**
 * The GroupsRepository interface provides methods for interacting with groups
 * in a database.
 */
@Repository
public interface GroupsRepository extends JpaRepository<Group, Long> {
	/**
	 * Retrieves a Group object by its group name.
	 *
	 * @param groupName the group name
	 * @return the Group object with the given name, or null if not found
	 */
	Optional<Group> findByGroupNameContaining(String groupName);
}
