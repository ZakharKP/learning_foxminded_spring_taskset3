package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ImageService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.CourseServiceImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		TeacherService.class, CourseServiceImpl.class, TestDataGenerator.class, ImageService.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
class CourseServiceImplTest {

	@Autowired
	private CourseService courseService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private TestDataGenerator dataGenerator;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void beforeEach() {
		when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->{
			return invocation.getArgument(0);
		});
		dataGenerator.run();
	}

	@Test
	void testIsCourseNameUniqueTrue() {
		assertTrue(courseService.isCourseNameUnique("TEST"));
	}

	@Test
	void testIsCourseNameUniqueFalse() {
		assertFalse(courseService.isCourseNameUnique("biology"));
	}

	@Test
	void testSetNewCourseName() {
		String expected = "TEST";
		Course course = courseService.getAll().stream().findAny().get();

		course = courseService.setNewCourseName(course.getId(), expected);

		String actual = course.getCourseName();

		assertEquals(expected, actual);
	}

	@Test
	void testSetNewCourseDescription() {
		String expected = "TEST TEST TEST";

		Course course = courseService.getAll().stream().findAny().get();

		course = courseService.setNewCourseDescription(course.getId(), expected);

		String actual = course.getCourseDescription();

		assertEquals(expected, actual);
	}

	@Test
	void testSetNewTeacherToCourse() {
		Teacher expected = teacherService.saveNewTeacher(ForTestsDataCreator.getNewTeacher());

		Course course = courseService.getAll().stream().findAny().get();

		course = courseService.setNewTeacherToCourse(course.getId(), expected.getId());

		Teacher actual = course.getTeacher();

		assertEquals(expected, actual);
	}

	@Test
	void testGetCoursesWithoutTeacher() {
		List<Course> expected = courseService.saveAllCourses(ForTestsDataCreator.getNewCourses());

		List<Course> actual = courseService.getCoursesWithoutTeacher();

		assertEquals(expected, actual);
	}

	@Test
	void testRemoveImages() {
		List<MultipartFile> selectedImages = ForTestsDataCreator.getMultipartFiles();
		Course course = courseService.getAll().stream().findAny().get();
		courseService.addImages(course.getId(), selectedImages);

		course = courseService.getCourse(course.getId()).get();

		List<Long> images = course.getImages().stream().map(Image::getId).collect(Collectors.toList());

		int expected = 1;
		int actual = -1;

		if (courseService.removeImages(course.getId(), images)) {
			actual++;
			course = courseService.getCourse(course.getId()).get();
			if (course.getImages() == null || course.getImages().isEmpty()) {
				actual++;
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testAddImages() {
		List<MultipartFile> selectedImages = ForTestsDataCreator.getMultipartFiles();
		Course course = courseService.getAll().stream().findAny().get();

		int expectedCourseImagesSize = course.getImages().size() + selectedImages.size();
		int expected = 1;
		int actual = -1;

		if (courseService.addImages(course.getId(), selectedImages)) {
			actual++;
			if (courseService.getCourse(course.getId()).get().getImages().size() == expectedCourseImagesSize) {
				actual++;
			}
		}

		assertEquals(expected, actual);
	}

	@Test
	void testSetIntroText() {
		Course course = courseService.getAll().stream().findAny().get();

		String text = ForTestsDataCreator.getIntroText();

		int expected = 1;
		int actual = -1;

		if (courseService.setIntroText(course.getId(), text)) {
			actual++;
			course = courseService.getCourse(course.getId()).get();
			if (course.getIntroText().equals(text)) {
				actual++;
			}

		}

		assertEquals(expected, actual);

	}

	@Test
	void testGetAllowedImageCountArray() {
		Course course = courseService.getAll().stream().findAny().get();
		int expected = Constants.MAX_ALLOWED_AMOUNT_INTRO_IMAGES_FOR_COURSE - course.getImages().size();
		int actual = courseService.getAllowedImageCountArray(course.getId()).length;

		assertEquals(expected, actual);

	}
}
