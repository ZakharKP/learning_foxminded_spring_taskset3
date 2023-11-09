package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Student;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.StudentsRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		StudentsRepository.class, TestDataGenerator.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")
class StudentsRepositoryTest {

	@Autowired
	StudentsRepository repository;
	
	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TestDataGenerator dataGenerator;

	@BeforeEach
	void beforeEach() {
		when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->{
			return invocation.getArgument(0);
		});
		dataGenerator.run();
	}
	
	
	@Test
	void testFindAllByGroupIsNull() {
		List<Student> expected = ForTestsDataCreator.getNewStudents();
		
		expected = repository.saveAll(expected);
		
		List<Student> actual = repository.findAllByGroupIsNull();
		
		assertEquals(expected, actual);
	}

}
