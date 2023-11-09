package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A data class representing a registration form for various entities like
 * students, teachers, groups, lectures and courses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegistrationForm {

	private String userName;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private String password;

	private String firstName;

	private String lastName;

	private Long groupId;

	private Long courseId;

	private String roleName;

	private String courseName;

	private String courseDescription;

	private Long teacherId;

	private String groupName;

	private Long[] studentsIds;

	private Long[] coursesIds;

	private Long[] groupsIds;
}
