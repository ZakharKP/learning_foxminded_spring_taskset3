package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FullCalendarEvent {
	private Long id;
	private String title;
	private String start;
	private String end;
	private String backgroundColor;
	private String textColor;
	private List<String> extendedProps;

	// Constructors, getters, setters
}
