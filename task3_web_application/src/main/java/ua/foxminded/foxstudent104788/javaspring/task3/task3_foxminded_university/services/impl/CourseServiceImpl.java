package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Constants;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.CoursesRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.CourseService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ImageService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;

/**
 * Implementation of service interface for managing courses.
 */
@Log4j2
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

	private static final String CAN_T_FIND_COURSE_WITH_ID = "Can't find course with id=";

	@Autowired
	private CoursesRepository repository;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private ImageService imageService;

	@Override
	public Course saveNewCourse(Course course) {
		if (course == null) {
			log.info("Can't save - course is NULL");
			return null;
		}
		log.info("Start saving new course: " + course.toString());

		return repository.save(course);
	}

	@Override
	public List<Course> saveAllCourses(List<Course> courses) {
		if (courses == null) {
			log.info("Can't save - courses is NULL");
			return Collections.emptyList();
		}
		log.info(String.format("Start saving List of %s courses:", courses.size()));

		List<Course> savedCourses = repository.saveAll(courses);

		log.info(savedCourses.size() + " new courses was saved");
		return savedCourses;

	}

	@Override
	public Optional<Course> getCourse(Long id) {
		log.info("Start searching of Course by id=" + id);
		return repository.findById(id);
	}

	@Override
	public List<Course> getAll() {
		log.info("Start research of All Courses");
		return repository.findAll();
	}

	@Override
	public Course updateCourse(Course course) {
		if (course == null) {
			log.info("Can't update - course is NULL");
			return null;
		}
		log.info("Starting update " + course.toString());
		return repository.save(course);
	}

	@Override
	public void deleteCourse(Course course) {
		if (course == null) {
			log.info("Can't delete - course is NULL");
			return;
		}
		log.info("Start deleting course: " + course.toString());

		repository.delete(course);
	}

	@Override
	public void deleteListOfCourses(List<Course> courses) {
		if (courses == null) {
			log.info("Can't delete - list of courses is NULL");
			return;
		}
		log.info(String.format("Start deleting list of %s courses", courses.size()));

		repository.deleteAll(courses);
	}

	@Override
	public long countCourses() {
		log.info("Counting courses");
		return repository.count();
	}

	@Override
	public boolean isCourseNameUnique(String name) {
		Optional<Course> course = repository.findByCourseNameContaining(name);

		return !course.isPresent();
	}

	@Override
	public Course setNewCourseName(Long courseId, String courseName) {
		Optional<Course> courseOptional = repository.findById(courseId);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();
			course.setCourseName(courseName);

			return updateCourse(course);
		}

		return null;
	}

	@Override
	public Course setNewCourseDescription(Long courseId, String courseDescription) {
		Optional<Course> courseOptional = repository.findById(courseId);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();
			course.setCourseDescription(courseDescription);

			return updateCourse(course);
		}

		return null;
	}

	@Override
	public Course setNewTeacherToCourse(Long courseId, Long teacherId) {
		Optional<Course> courseOptional = repository.findById(courseId);
		Optional<Teacher> teacherOptional = teacherService.getTeacher(teacherId);
		if (courseOptional.isPresent() && teacherOptional.isPresent()) {
			Course course = courseOptional.get();
			Teacher teacher = teacherOptional.get();

			if (course.getTeacher() != null && course.getTeacher().getCourses() != null) {
				course.getTeacher().getCourses().remove(course);
			}

			course.setTeacher(teacher);

			if (teacher.getCourses() == null) {
				teacher.setCourses(new HashSet<>());
			}
			teacher.getCourses().add(course);

			return updateCourse(course);
		}

		return null;
	}

	@Override
	public List<Course> getCoursesWithoutTeacher() {

		return repository.findAllByTeacherIsNull();
	}

	@Override
	public boolean removeImages(Long courseId, List<Long> selectedImages) {
		log.info("Start removing selected images from Course id=" + courseId);
		Optional<Course> courseOptional = repository.findById(courseId);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();

			if (course.getImages() != null) {
				for (Image image : imageService.getImagesbyIds(selectedImages)) {
					if (course.getImages().contains(image)) {
						course.getImages().remove(image);
						imageService.delete(image);
					}

				}
				log.info("Selected Images was removed from Course id=" + courseId);

				updateCourse(course);
				return true;
			}
			log.info(" Course don't contain images course id=" + courseId);
			return false;
		}
		log.error(CAN_T_FIND_COURSE_WITH_ID + courseId);
		return false;
	}

	@Override
	public boolean addImages(Long courseId, List<MultipartFile> selectedImages) {
		log.info("Start saving process selected images to Course id=" + courseId);
		Optional<Course> courseOptional = repository.findById(courseId);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();

			if (course.getImages() == null) {
				course.setImages(new HashSet<>());
			}
			for (MultipartFile imageFile : selectedImages) {

				if (!imageFile.isEmpty()) {
					Image image = imageService.saveImageFromFile(imageFile);
					if (image != null) {
						course.getImages().add(image);
						image.setCourse(course);
					} else {
						log.error("Can't add Image from file=" + imageFile);
					}
				}
			}
			log.info("Selected Images was Saved to Course id=" + courseId);

			updateCourse(course);
			return true;

		}
		log.error(CAN_T_FIND_COURSE_WITH_ID + courseId);
		return false;
	}

	@Override
	public boolean setIntroText(Long courseId, String introText) {
		log.info("Start saving introText to Course id=" + courseId);

		Optional<Course> courseOptional = repository.findById(courseId);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();

			course.setIntroText(introText);
			updateCourse(course);
			return true;
		}
		log.error(CAN_T_FIND_COURSE_WITH_ID + courseId);
		return false;
	}

	@Override
	public Object[] getAllowedImageCountArray(Long courseId) {
		log.info("Start counting countArray size for Course id=" + courseId);

		int countArraySize = 0;
		Optional<Course> courseOptional = repository.findById(courseId);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();

			countArraySize = Constants.MAX_ALLOWED_AMOUNT_INTRO_IMAGES_FOR_COURSE - course.getImages().size();
		}

		log.error(CAN_T_FIND_COURSE_WITH_ID + courseId);
		return new Object[countArraySize];
	}

	@Override
	public void setImages(Long id, List<Long> imagesIds) {
		Optional<Course> courseOptional = repository.findById(id);
		if (courseOptional.isPresent()) {
			Course course = courseOptional.get();

			if (course.getImages() == null) {
				course.setImages(new HashSet<>());
			}
			List<Image> images = imageService.getImagesbyIds(imagesIds);
			for (Image image : images) {
				course.getImages().add(image);
				image.setCourse(course);

			}
			updateCourse(course);
		}
	}

}
