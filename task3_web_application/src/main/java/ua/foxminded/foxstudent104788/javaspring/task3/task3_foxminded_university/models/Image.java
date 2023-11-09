package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = Constants.IMAGE_TABLE_NAME)
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = Constants.IMAGE_COLUMN_NAME_ID)
	private Long id;

	@Column(name = Constants.IMAGE_COLUMN_NAME_FILE_NAME)
	private String fileName;

	@Column(name = Constants.IMAGE_COLUMN_NAME_DATA)
	private byte[] data;

	@Column(name = Constants.IMAGE_COLUMN_NAME_MEDIA_TYPE)
	private String mediaType;

	@ManyToOne
	@JoinColumn(name = Constants.COURSE_COLUMN_NAME_ID)
	private Course course;

}
