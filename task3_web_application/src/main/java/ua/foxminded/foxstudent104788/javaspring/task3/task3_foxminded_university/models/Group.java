package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Group class represents a group entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.GROUP_TABLE_NAME)
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constants.GROUP_COLUMN_NAME_ID)
	private Long id;

	@Column(name = Constants.GROUP_COLUMN_NAME_NAME)
	private String groupName;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private Set<Student> students;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
	private Set<Lecture> lectures;

}