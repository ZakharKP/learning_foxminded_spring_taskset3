package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Course;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Teacher;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.CoursesRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.LoginDataRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.TeacherRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TeacherService;

/**
 * Implementation of service interface for managing teachers.
 */
@Log4j2
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private CoursesRepository coursesRepository;

	@Autowired
	private LoginDataRepository loginDataRepository;

	@Override
	public Teacher saveNewTeacher(Teacher teacher) {
		if (teacher == null) {
			log.info("Can't save - teacher is NULL");
			return null;
		}
		log.info("Start saving new teacher: " + teacher.toString());

		return teacherRepository.save(teacher);
	}

	@Override
	public List<Teacher> saveAllTeachers(List<Teacher> teachers) {
		if (teachers == null) {
			log.info("Can't save - teachers is NULL");
			return Collections.emptyList();
		}
		log.info(String.format("Start saving List of %s teachers:", teachers.size()));

		List<Teacher> savedTeachers = teacherRepository.saveAll(teachers);

		log.info(savedTeachers.size() + " new teachers was saved");
		return savedTeachers;

	}

	@Override
	public Optional<Teacher> getTeacher(Long id) {
		log.info("Start searching of Teacher by id=" + id);
		return teacherRepository.findById(id);
	}

	@Override
	public List<Teacher> getAll() {
		log.info("Start research of All Teachers");
		return teacherRepository.findAll();
	}

	@Override
	public Teacher updateTeacher(Teacher teacher) {
		if (teacher == null) {
			log.info("Can't update - teacher is NULL");
			return null;
		}
		log.info("Starting update " + teacher.toString());
		return teacherRepository.save(teacher);
	}

	@Override
	public void deleteTeacher(Teacher teacher) {
		if (teacher == null) {
			log.info("Can't delete - teacher is NULL");
			return;
		}
		log.info("Start deleting teacher: " + teacher.toString());

		teacherRepository.delete(teacher);
	}

	@Override
	public void deleteListOfTeachers(List<Teacher> teachers) {
		if (teachers == null) {
			log.info("Can't delete - list of teachers is NULL");
			return;
		}
		log.info(String.format("Start deleting list of %s teachers", teachers.size()));

		teacherRepository.deleteAll(teachers);
	}

	@Override
	public long countTeachers() {
		log.info("Counting teachers");
		return teacherRepository.count();
	}

	@Override
	public Optional<Teacher> getTeacherByUserName(String userName) {
		log.info("looking for tacher with userName=" + userName);
		Optional<LoginData> login = loginDataRepository.findById(userName);
		if (login.isPresent()) {
			return getTeacher(login.get().getTeacher().getId());
		}

		return Optional.empty();
	}

	@Override
	public Teacher setLogin(Long id, String userName) {
		log.info("set loginData to teacher with id=" + id + " userName=" + userName);

		Optional<Teacher> teacherOpt = getTeacher(id);
		Optional<LoginData> loginOpt = loginDataRepository.findById(userName);

		if (teacherOpt.isPresent() && loginOpt.isPresent()) {
			Teacher teacher = teacherOpt.get();
			LoginData login = loginOpt.get();
			login.setTeacher(teacher);
			teacher.setLogin(login);
			return updateTeacher(teacher);
		}
		log.info(String.format("Teacher id =%s or user userName = %s wasn't found with id = ", id, userName));
		throw new UsernameNotFoundException(
				String.format("Teacher id =%s or user userName = %s wasn't found with id = ", id, userName));
	}

	@Override
	public List<Group> getTeachersGroups(Long id) {
		log.info("Collect all groups of Teacher with id=" + id);
		Set<Group> groups = new HashSet<>();
		Optional<Teacher> teacher = getTeacher(id);
		if (teacher.isPresent()) {
			teacher.get().getCourses()
					.forEach(x -> x.getLectures().stream().forEach(y -> y.getGroups().stream().forEach(groups::add)));
		}

		return groups.stream().collect(Collectors.toList());
	}

	@Override
	public List<Student> getTeachersStudents(Long id) {
		log.info("Collect all groups of Teacher with id=" + id);
		Set<Student> students = new HashSet<>();
		Optional<Teacher> teacher = getTeacher(id);
		if (teacher.isPresent()) {
			teacher.get().getCourses().forEach(x -> x.getLectures().stream().forEach(
					y -> y.getGroups().stream().forEach(z -> z.getStudents().stream().forEach(students::add))));
		}

		return students.stream().collect(Collectors.toList());
	}

	@Override
	public boolean setFirstName(Long teacherId, String firstName) {
		log.info("Set new first name - {} to teacher with id={}", firstName, teacherId);
		Optional<Teacher> teOptional = getTeacher(teacherId);

		if (teOptional.isPresent()) {
			Teacher teacher = teOptional.get();
			teacher.setFirstName(firstName);
			updateTeacher(teacher);
			return true;
		}
		return false;
	}

	@Override
	public boolean setLastName(Long teacherId, String lastName) {
		log.info("Set new last name - {} to teacher with id={}", lastName, teacherId);

		Optional<Teacher> teOptional = getTeacher(teacherId);

		if (teOptional.isPresent()) {
			Teacher teacher = teOptional.get();
			teacher.setLastName(lastName);
			updateTeacher(teacher);
			return true;
		}
		return false;
	}

	@Override
	public boolean addCoursesToTeacher(Long teacherId, List<Long> selectedCourses) {
		log.info("Set new course - to teacher with id={}", teacherId);

		Optional<Teacher> teOptional = getTeacher(teacherId);

		if (teOptional.isPresent()) {
			Teacher teacher = teOptional.get();
			List<Course> courses = coursesRepository.findAllById(selectedCourses);

			for (Course course : courses) {
				course.setTeacher(teacher);
				if (teacher.getCourses() == null) {
					teacher.setCourses(new HashSet<>());
				}
				teacher.getCourses().add(course);
			}
			updateTeacher(teacher);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeCoursesFromTeacher(Long teacherId, List<Long> selectedCourses) {
		log.info("Set new course - to teacher with id={}", teacherId);

		Optional<Teacher> teOptional = getTeacher(teacherId);

		if (teOptional.isPresent()) {
			Teacher teacher = teOptional.get();
			List<Course> courses = coursesRepository.findAllById(selectedCourses);

			for (Course course : courses) {
				course.setTeacher(null);
				if (teacher.getCourses() == null) {
					teacher.setCourses(new HashSet<>());
				}
				teacher.getCourses().remove(course);
			}
			updateTeacher(teacher);
			return true;
		}
		return false;
	}

	@Override
	public boolean editCoursesToSelectedTeacher(Long teacherId, Long[] selectedCourses, String action) {
		if (action.equals("add")) {
			return addCoursesToTeacher(teacherId, Arrays.asList(selectedCourses));
		}
		if (action.equals("remove")) {
			return removeCoursesFromTeacher(teacherId, Arrays.asList(selectedCourses));
		}
		return false;
	}

}
