package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Course class represents a course entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.COURSE_TABLE_NAME)
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constants.COURSE_COLUMN_NAME_ID)
	private Long id;

	@Column(name = Constants.COURSE_COLUMN_NAME_NAME)
	private String courseName;

	@Column(name = Constants.COURSE_COLUMN_NAME_DESCRIPTION)
	private String courseDescription;

	@Column(name = Constants.COURSE_COLUMN_NAME_INTRO_TEXT)
	private String introText;

	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(name = Constants.TEACHER_COLUMN_NAME_ID)
	private Teacher teacher;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private Set<Lecture> lectures;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private Set<Image> images;

}
