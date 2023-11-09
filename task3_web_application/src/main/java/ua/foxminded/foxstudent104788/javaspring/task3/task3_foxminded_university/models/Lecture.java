package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Lecture class represents a lecture entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.LECTURE_TABLE_NAME)
public class Lecture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constants.LECTURE_COLUMN_NAME_ID)
	private Long id;

	@Column(name = Constants.LECTURE_COLUMN_NAME_START_TIME)
	private LocalDateTime startTime;

	@ManyToOne
	@JoinColumn(name = Constants.COURSE_COLUMN_NAME_ID)
	private Course course;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany
	@JoinTable(name = Constants.LECTURES_GROUPS_TABLE_NAME, joinColumns = @JoinColumn(name = Constants.LECTURE_COLUMN_NAME_ID), inverseJoinColumns = @JoinColumn(name = Constants.GROUP_COLUMN_NAME_ID))
	private Set<Group> groups;

}
