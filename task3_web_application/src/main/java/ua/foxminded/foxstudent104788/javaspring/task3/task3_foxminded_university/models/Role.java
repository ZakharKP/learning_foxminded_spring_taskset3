package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.util.Arrays;

/**
 * Enum representing different roles in the system.
 */
public enum Role {

	ROLE_STUDENT("Student"),

	ROLE_TEACHER("Teacher"),

	ROLE_ADMIN("Admin");

	private String roleName;

	/**
	 * Constructor to set the roleName for each role.
	 *
	 * @param roleName The name of the role.
	 */
	Role(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Get the name of the role.
	 *
	 * @return The role name.
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Get the Role enum based on the provided role name.
	 *
	 * @param roleName The role name.
	 * @return The corresponding Role enum value.
	 */
	public static Role get(String roleName) {
		return Arrays.stream(Role.values()).filter(x -> x.getRoleName().equals(roleName)).findFirst().orElse(null);
	}
}