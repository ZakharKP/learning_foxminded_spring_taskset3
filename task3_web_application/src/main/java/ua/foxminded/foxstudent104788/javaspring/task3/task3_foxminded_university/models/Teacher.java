package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Teacher class represents a teacher entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.TEACHER_TABLE_NAME)
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constants.TEACHER_COLUMN_NAME_ID)
	private Long id;

	@Column(name = Constants.TEACHER_COLUMN_NAME_FIRST_NAME)
	private String firstName;

	@Column(name = Constants.TEACHER_COLUMN_NAME_LAST_NAME)
	private String lastName;

	@OneToOne
	@JoinColumn(name = Constants.LOGIN_DATA_COLUMN_NAME_USER_NAME)
	private LoginData login;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "teacher")
	private Set<Course> courses;

}
