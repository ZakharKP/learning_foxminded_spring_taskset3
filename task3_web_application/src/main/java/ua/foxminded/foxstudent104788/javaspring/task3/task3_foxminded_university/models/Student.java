package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Student class represents a student entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.STUDENT_TABLE_NAME)
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constants.STUDENT_COLUMN_NAME_ID)
	private Long id;

	@Column(name = Constants.STUDENT_COLUMN_NAME_FIRST_NAME)
	private String firstName;

	@Column(name = Constants.STUDENT_COLUMN_NAME_LAST_NAME)
	private String lastName;

	@OneToOne
	@JoinColumn(name = Constants.LOGIN_DATA_COLUMN_NAME_USER_NAME)
	private LoginData login;

	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(name = Constants.GROUP_COLUMN_NAME_ID)
	private Group group;

}