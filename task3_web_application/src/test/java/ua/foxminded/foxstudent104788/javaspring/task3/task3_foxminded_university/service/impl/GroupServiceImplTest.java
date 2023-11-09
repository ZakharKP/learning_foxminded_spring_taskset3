package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Group;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.StudentService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.GroupServiceImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		StudentService.class, GroupServiceImpl.class, TestDataGenerator.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
class GroupServiceImplTest {

	@Autowired
	private GroupServiceImpl groupService;

	@Autowired
	private StudentService studentService;

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
	void testIsGroupNameUniqueTrue() {
		assertTrue(groupService.isGroupNameUnique("TEST"));
	}

	@Test
	void testIsGroupNameUniqueFalse() {
		Optional<Group> group =  groupService.getAll().stream().findAny();
		assertFalse(groupService.isGroupNameUnique(group.get().getGroupName()));
	}

	@Test
	void testSetNewGroupName() {
		
		Group group = groupService.getAll().stream().findAny().get();
	
		assertTrue(groupService.setNewGroupName(group.getId(), "TEST"));
	}

	@Test
	void testAddStudentsToGroup() {
		List<Student> students = studentService.saveAllStudents(ForTestsDataCreator.getNewStudents());

		List<Long> studentsIds = new ArrayList<>();

		students.stream().forEach(x -> studentsIds.add(x.getId()));

		Optional<Group> groupOptional =  groupService.getAll().stream().findAny();

		Group group = groupService.addStudentsToGroup(groupOptional.get().getId(), studentsIds);

		assertTrue(group.getStudents().containsAll(students));
	}

	@Test
	void testRemoveStudentsFromGroup() {
		
		
		Optional<Group> groupOptional =  groupService.getAll().stream().findAny();
		Set<Student> students = groupOptional.get().getStudents();

		List<Long> studentsIds = new ArrayList<>();
		
		students.stream().forEach(x -> studentsIds.add(x.getId()));


		Group group = groupService.removeStudentsFromGroup(groupOptional.get().getId(), studentsIds);

		assertTrue(group.getStudents().isEmpty() || group.getStudents() == null);
	}
	
	@Test
	void testEditStudentsForThatGroup() {
		
		List<Student> students = studentService.saveAllStudents(ForTestsDataCreator.getNewStudents());

		Long[] studentsIds = students.stream().map(Student :: getId).toArray(Long[] :: new);
		
		int expected = 1;
		int actual = -3;
		
		if(groupService.editStudentsForThatGroup(1L, studentsIds, "add")) {
			actual++;
			if(groupService.getGroup(1L).get().getStudents().containsAll(students)) {
				actual++;
				if(groupService.editStudentsForThatGroup(1L, studentsIds, "remove")) {
					actual++;
					if(!groupService.getGroup(1L).get().getStudents().containsAll(students)) {
						actual++;
					}
				}
			}
		}

	}

}
