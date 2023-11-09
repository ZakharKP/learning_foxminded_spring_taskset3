package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

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
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.StudentsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.GroupService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;

/**
 * Implementation of service interface for managing students.
 */
@Log4j2
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentsRepository repository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private LoginDataService loginDataService;

	@Override
	public Student saveNewStudent(Student student) {
		if (student == null) {
			log.info("Can't save - student is NULL");
			return null;
		}
		log.info("Start saving new student: " + student.toString());

		return repository.save(student);
	}

	@Override
	public List<Student> saveAllStudents(List<Student> students) {
		if (students == null) {
			log.info("Can't save - students is NULL");
			return Collections.emptyList();
		}
		log.info(String.format("Start saving List of %s students:", students.size()));

		List<Student> savedStudents = repository.saveAll(students);

		log.info(savedStudents.size() + " new students was saved");
		return savedStudents;

	}

	@Override
	public Optional<Student> getStudent(Long id) {
		log.info("Start searching of Student by id=" + id);
		return repository.findById(id);
	}

	@Override
	public List<Student> getAll() {
		log.info("Start research of All Students");
		return repository.findAll();
	}

	@Override
	public Student updateStudent(Student student) {
		if (student == null) {
			log.info("Can't update - student is NULL");
			return null;
		}
		log.info("Starting update " + student.toString());
		return repository.save(student);
	}

	@Override
	public void deleteStudent(Student student) {
		if (student == null) {
			log.info("Can't delete - student is NULL");
			return;
		}
		log.info("Start deleting student: " + student.toString());

		repository.delete(student);
	}

	@Override
	public void deleteListOfStudents(List<Student> students) {
		if (students == null) {
			log.info("Can't delete - list of students is NULL");
			return;
		}
		log.info(String.format("Start deleting list of %s students", students.size()));

		repository.deleteAll(students);
	}

	@Override
	public long countStudents() {
		log.info("Counting students");
		return repository.count();
	}

	@Override
	public Optional<Student> getStudentByUserName(String userName) {
		log.info("looking for student with userName=" + userName);
		Optional<LoginData> login = loginDataService.getUser(userName);
		if (login.isPresent()) {
			return getStudent(login.get().getStudent().getId());
		}

		return Optional.empty();
	}

	@Override
	public Student setLoginData(Long id, String userName) {
		log.info("set loginData to student with id=" + id + " userName=" + userName);

		Optional<Student> stOptional = getStudent(id);
		Optional<LoginData> loginOpt = loginDataService.getUser(userName);

		if (stOptional.isPresent() && loginOpt.isPresent()) {
			Student student = stOptional.get();
			LoginData login = loginOpt.get();
			login.setStudent(student);
			student.setLogin(login);
			return updateStudent(student);
		}
		throw new UsernameNotFoundException(
				String.format("Student with id = %s or User with userName = %s wasn't find", id, userName));
	}

	@Override
	public boolean setGroupToStudent(Long studentId, Long groupId) {
		log.info("Setting group with id={} to student with id={}", groupId, studentId);
		Optional<Student> stuOptional = getStudent(studentId);
		Optional<Group> grOptional = groupService.getGroup(groupId);
		if (stuOptional.isPresent() && grOptional.isPresent()) {
			Student student = stuOptional.get();
			Group group = grOptional.get();
			student.setGroup(group);
			group.getStudents().add(student);
			updateStudent(student);
			return true;
		}
		return false;
	}

	@Override
	public List<Student> getStudentsByIds(List<Long> selectedStudents) {
		log.info("Collect All Students to List");
		return repository.findAllById(selectedStudents);
	}

	@Override
	public List<Student> getStudentsWithoutGroup() {
		log.info("Collect All Students without group to List");

		return repository.findAllByGroupIsNull();
	}

	@Override
	public List<Course> getStudentsCourses(Long id) {
		log.info("Collect All Courses of Student id={} to List", id);

		Set<Course> courses = new HashSet<>();
		Optional<Student> student = getStudent(id);
		if (student.isPresent()) {
			student.get().getGroup().getLectures().stream().forEach(x -> courses.add(x.getCourse()));
		}
		return courses.stream().collect(Collectors.toList());
	}

	@Override
	public List<Teacher> getStudentsTeachers(Long id) {
		log.info("Collect All Teachers of Student id={} to List", id);
		Set<Teacher> teachers = new HashSet<>();
		Optional<Student> student = getStudent(id);
		if (student.isPresent()) {
			student.get().getGroup().getLectures().stream().forEach(x -> teachers.add(x.getCourse().getTeacher()));
		}
		return teachers.stream().collect(Collectors.toList());

	}

	@Override
	public boolean setFirstName(Long studentId, String firstName) {
		log.info("Set new first name - {} to student with id={}", firstName, studentId);
		Optional<Student> stuOptional = getStudent(studentId);

		if (stuOptional.isPresent()) {
			Student student = stuOptional.get();
			student.setFirstName(firstName);
			updateStudent(student);
			return true;
		}
		return false;
	}

	@Override
	public boolean setLastName(Long studentId, String lastName) {
		log.info("Set new last name - {} to student with id={}", lastName, studentId);

		Optional<Student> stuOptional = getStudent(studentId);

		if (stuOptional.isPresent()) {
			Student student = stuOptional.get();
			student.setLastName(lastName);
			updateStudent(student);
			return true;
		}
		return false;
	}
}
