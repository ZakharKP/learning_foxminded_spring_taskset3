package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.configuration.PgSQLEnumType;

/**
 * The Student class represents a student entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.LOGIN_DATA_TABLE_NAME)
@TypeDef(name = "role", typeClass = PgSQLEnumType.class)
public class LoginData {
	@Id
	@Column(name = Constants.LOGIN_DATA_COLUMN_NAME_USER_NAME)
	private String userName;

	@Column(name = Constants.LOGIN_DATA_COLUMN_NAME_PASSWORD)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private String password;

	@OneToOne(mappedBy = "login", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Student student;

	@OneToOne(mappedBy = "login", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Teacher teacher;

	@Column(name = Constants.LOGIN_DATA_COLUMN_NAME_ROLE)
	@Enumerated(EnumType.STRING)
	@Type(type = "role")
	private Role role;

}
