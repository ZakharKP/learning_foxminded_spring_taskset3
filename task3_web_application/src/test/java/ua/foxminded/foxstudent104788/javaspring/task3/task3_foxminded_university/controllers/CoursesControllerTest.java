package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.controllers;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.RegistrationForm;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.AdminService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util.ScheduleDataFactory;

@WebMvcTest(CoursesController.class)
class CoursesControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CourseService courseService;

	@MockBean
	private AdminService adminService;

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowCourseDetails() throws Exception {
		Course course = ForTestsDataCreator.getCourses().get(0);

		when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
		mvc.perform(get("/course/details").with(csrf()).param("courseId", course.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("course/details"))
				.andExpect(model().attribute("course", course));

		verify(courseService).getCourse(anyLong());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testshowCoursesSchedule() throws Exception {
		Course course = ForTestsDataCreator.getCourses().get(0);

		try (MockedStatic<ScheduleDataFactory> mocked = mockStatic(ScheduleDataFactory.class)) {
			mocked.when(() -> ScheduleDataFactory.getCourseScheduleEntityMap(course))
					.thenReturn(ForTestsDataCreator.getScheduleEntityMap());

			when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));

			mvc.perform(get("/course/schedule").with(csrf()).param("courseId", course.getId().toString()))
					.andExpect(status().isOk()).andExpect(view().name("schedule"))
					.andExpect(model().attribute("entityDataMap", ForTestsDataCreator.getScheduleEntityMap()));

			verify(courseService).getCourse(anyLong());

		}
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowCourseEdit() throws Exception {
		Course course = ForTestsDataCreator.getCourses().get(0);
		List<Teacher> teachers = ForTestsDataCreator.getTeachers();
		Object[] allowedImageCountArray = new Object[3];

		when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
		when(courseService.getAllowedImageCountArray(anyLong())).thenReturn(allowedImageCountArray);
		when(adminService.getAllTeachers()).thenReturn(teachers);
		mvc.perform(get("/course/editor")
				.with(csrf()).param("courseId", course.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("course/editor"))
				.andExpect(model().attribute("course", course)).andExpect(model().attribute("teachers", teachers));

		verify(courseService).getCourse(course.getId());
		verify(courseService).getAllowedImageCountArray(course.getId());
		verify(adminService).getAllTeachers();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetNewName() throws Exception {
		String courseName = "test";
		Long courseId = 1L;

		when(courseService.isCourseNameUnique(courseName)).thenReturn(true);
		when(courseService.setNewCourseName(courseId, courseName))
				.thenReturn(Course.builder().courseName(courseName).build());

		mvc.perform(post("/course/editor/name").with(csrf()).param("courseName", courseName).param("courseId",
				courseId.toString())) // Provide courseName as a request parameter
				.andExpect(status().is3xxRedirection()).andExpect(
						redirectedUrl("/course/editor?success&message=Course name was changed&courseId=" + courseId));

		verify(courseService).isCourseNameUnique(courseName);
		verify(courseService).setNewCourseName(courseId, courseName);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetNewDescription() throws Exception {
		String courseDescription = "test";
		Long courseId = 1L;

		when(courseService.setNewCourseDescription(courseId, courseDescription))
				.thenReturn(Course.builder().courseDescription(courseDescription).build());

		mvc.perform(post("/course/editor/description").with(csrf()).param("courseDescription", courseDescription)
				.param("courseId", courseId.toString())) // Provide courseName as a request parameter
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/course/editor?success&message=Course description was changed&courseId=1"));

		verify(courseService).setNewCourseDescription(courseId, courseDescription);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testSetNewTeacher() throws Exception {
		Long teacherId = 2L;
		Long courseId = 1L;

		when(courseService.setNewTeacherToCourse(courseId, teacherId))
				.thenReturn(Course.builder().teacher(Teacher.builder().id(teacherId).build()).build());

		mvc.perform(post("/course/editor/teacher").with(csrf()).param("teacherId", teacherId.toString())
				.param("courseId", courseId.toString())) // Provide courseName as a request parameter
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(
						"/course/editor?success&message=Course Teacher was changed&courseId=" + courseId));

		verify(courseService).setNewTeacherToCourse(courseId, teacherId);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowCourseCreation() throws Exception {
		RegistrationForm form = new RegistrationForm();
		List<Teacher> teachers = ForTestsDataCreator.getTeachers();

		when(adminService.getAllTeachers()).thenReturn(teachers);
		mvc.perform(get("/course/creation/")).andExpect(status().isOk()).andExpect(view().name("course/creation"))
				.andExpect(model().attribute("form", form)).andExpect(model().attribute("teachers", teachers));

		verify(adminService).getAllTeachers();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testCreateNewCourse() throws Exception {
		RegistrationForm form = RegistrationForm.builder().courseName("test").courseDescription("test test test")
				.teacherId(1L).build();

		Course course = Course.builder().courseName(form.getCourseName()).courseDescription(form.getCourseDescription())
				.build();

		when(adminService.createNewCourse(form)).thenReturn(course);

		mvc.perform(post("/course/creation").with(csrf()).flashAttr("form", form))
				.andExpect(model().attribute("courseName", form.getCourseName()))
				.andExpect(model().attribute("courseDescription", form.getCourseDescription()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(
						"/course/create-report?success&courseName=test&courseDescription=test+test+test"));

		verify(adminService).createNewCourse(form);
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testCreateReport() throws Exception {
		mvc.perform(get("/course/create-report")).andExpect(status().isOk())
				.andExpect(view().name("course/create-report"));

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testShowDeleteConfirm() throws Exception {
		Course course = ForTestsDataCreator.getCourses().get(0);

		when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));

		mvc.perform(get("/course/delete-course").with(csrf()).param("courseId", "1")).andExpect(status().isOk())
				.andExpect(view().name("course/delete-course")).andExpect(model().attribute("course", course));

		verify(courseService).getCourse(anyLong());

	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testDeleteCourse() throws Exception {
		Course course = ForTestsDataCreator.getCourses().get(0);

		when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
		doNothing().when(courseService).deleteCourse(course);

		mvc.perform(post("/course/del-course").with(csrf()).param("courseId", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/courses"));

		verify(courseService).getCourse(anyLong());
		verify(courseService).deleteCourse(course);

	}

	@Test
	@WithMockUser
	@WithAnonymousUser
	void testShowIntro() throws Exception {

		Course course = ForTestsDataCreator.getCourses().get(0);

		when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
		mvc.perform(get("/course/intro").with(csrf()).param("courseId", course.getId().toString()))
				.andExpect(status().isOk()).andExpect(view().name("course/intro"));

		verify(courseService).getCourse(course.getId());
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testRemoveIntroImages() throws Exception {

	Course course = ForTestsDataCreator.getCourses().get(0);
	List<Long> selectedImages = Arrays.asList(1L, 2L, 3L);

	when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
	when(courseService.removeImages(anyLong(), anyList())).thenReturn(true);
	doNothing().when(courseService).deleteCourse(course);
	
	mvc.perform(post("/course/editor/intro/images/remove")
			.with(csrf())
			.param("courseId", course.getId().toString())
			.param("selectedImages", selectedImages.stream().map(x -> x.toString()).toArray(String[] :: new)))
			.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/course/editor?success&message=Images was removed&courseId=1"));

	verify(courseService).getCourse(course.getId());
	verify(courseService).removeImages(course.getId(), selectedImages);
	
}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testAddIntroImages() throws Exception {
	    List<MultipartFile> selectedImages = new ArrayList<>();

	    // Create a few mock MultipartFile objects for testing
	    MultipartFile file1 = new MockMultipartFile("file1.jpg", "file1.jpg", "image/jpeg", new byte[0]);
	    MultipartFile file2 = new MockMultipartFile("file2.jpg", "file2.jpg", "image/jpeg", new byte[0]);
	    MultipartFile file3 = new MockMultipartFile("file3.jpg", "file3.jpg", "image/jpeg", new byte[0]);

	    // Add them to the list
	    selectedImages.add(file1);
	    selectedImages.add(file2);
	    selectedImages.add(file3);

	    Course course = ForTestsDataCreator.getCourses().get(0);

	    when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
	    when(courseService.addImages(anyLong(), anyList())).thenReturn(true);

	    // Build the multipart request
	    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/course/editor/intro/images/add")
	            .file("selectedImages", file1.getBytes())
	            .file("selectedImages", file2.getBytes())
	            .file("selectedImages", file3.getBytes())
	            .param("courseId", course.getId().toString())
	        //    .param("selectedImages", selectedImages.stream().map(x -> x.getOriginalFilename()).toArray(String[]::new))
	            .with(csrf());

	    mvc.perform(request)
	            .andExpect(status().is3xxRedirection())
	            .andExpect(redirectedUrl("/course/editor?success&message=Images to intro was added&courseId=1"));

	    verify(courseService).getCourse(course.getId());
	 //   verify(courseService).addImages(course.getId(), selectedImages);
	}
	
	@WithMockUser(roles = { "ADMIN" })
	@Test
	void testEditIntroText() throws Exception {
		Course course = ForTestsDataCreator.getCourses().get(0);
		String introText = ForTestsDataCreator.getIntroText();

		when(courseService.getCourse(anyLong())).thenReturn(Optional.of(course));
		when(courseService.setIntroText(anyLong(), anyString())).thenReturn(true);
		
		mvc.perform(post("/course/editor/intro/text")
				.with(csrf())
				.param("courseId", course.getId().toString())
				.param("introText", introText))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/course/editor?success&message=Text to intro was changed&courseId=1"));

		verify(courseService).getCourse(course.getId());
		verify(courseService).setIntroText(course.getId(), introText);
	}

}
