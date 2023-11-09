package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models;

import java.time.Duration;

/**
 * This class contains constant values used throughout the application.
 */
public class Constants {

	// Constants related to the "groups" table
	public static final String GROUP_TABLE_NAME = "groups";
	public static final String GROUP_COLUMN_NAME_ID = "group_id";
	public static final String GROUP_COLUMN_NAME_NAME = "group_name";

	// Constants related to the "students" table
	public static final String STUDENT_TABLE_NAME = "students";
	public static final String STUDENT_COLUMN_NAME_ID = "student_id";
	public static final String STUDENT_COLUMN_NAME_FIRST_NAME = "student_first_name";
	public static final String STUDENT_COLUMN_NAME_LAST_NAME = "student_last_name";

	// Constants related to the "teachers" table
	public static final String TEACHER_TABLE_NAME = "teachers";
	public static final String TEACHER_COLUMN_NAME_ID = "teacher_id";
	public static final String TEACHER_COLUMN_NAME_FIRST_NAME = "teacher_first_name";
	public static final String TEACHER_COLUMN_NAME_LAST_NAME = "teacher_last_name";

	// Constants related to the "courses" table
	public static final String COURSE_TABLE_NAME = "courses";
	public static final String COURSE_COLUMN_NAME_ID = "course_id";
	public static final String COURSE_COLUMN_NAME_NAME = "course_name";
	public static final String COURSE_COLUMN_NAME_DESCRIPTION = "course_description";
	public static final String COURSE_COLUMN_NAME_INTRO_TEXT = "course_intro_text";

	// Constants related to the "lectures" table
	public static final String LECTURE_TABLE_NAME = "lectures";
	public static final String LECTURE_COLUMN_NAME_ID = "lecture_id";
	public static final String LECTURE_COLUMN_NAME_START_TIME = "start_time";

	// Constants related to the "lectures_groups" table
	public static final String LECTURES_GROUPS_TABLE_NAME = "lectures_groups";

	public static final String LOGIN_DATA_TABLE_NAME = "logins";
	public static final String LOGIN_DATA_COLUMN_NAME_USER_NAME = "user_name";
	public static final String LOGIN_DATA_COLUMN_NAME_PASSWORD = "password";
	public static final String LOGIN_DATA_COLUMN_NAME_ROLE = "role";

	// Duration constant for lecture duration (1 hour and 30 minutes)
	public static final Duration LECTURE_DURATION = Duration.ofHours(1).plusMinutes(30);
	public static final int MAX_ALLOWED_AMOUNT_GROUPS_FOR_LECTURE = 3;
	
	public static final String IMAGE_TABLE_NAME = "images";
	public static final String IMAGE_COLUMN_NAME_ID = "image_id";
	public static final String IMAGE_COLUMN_NAME_DATA = "image_data";
	public static final String IMAGE_COLUMN_NAME_MEDIA_TYPE = "image_media_type";
	public static final String IMAGE_COLUMN_NAME_FILE_NAME = "image_file_name";
	
	
	public static final int MAX_ALLOWED_AMOUNT_INTRO_IMAGES_FOR_COURSE = 3;


}