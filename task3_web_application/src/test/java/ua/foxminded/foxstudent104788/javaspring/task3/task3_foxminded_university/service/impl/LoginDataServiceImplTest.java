package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.LoginData;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Role;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.LoginDataService;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.TestDataGenerator;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		LoginDataService.class, TestDataGenerator.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")

class LoginDataServiceImplTest {

	@Autowired
	private LoginDataService loginDataService;

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
	void testSetNewRole() {

		LoginData loginData = ForTestsDataCreator.getNewLogin();
		loginData = loginDataService.saveNewUser(loginData);
		loginDataService.setNewRole(loginData.getUserName(), Role.ROLE_ADMIN);

		assertTrue(loginData.getRole().equals(Role.ROLE_ADMIN));

	}
	
	@Test
	void testIsUserNameUnique() {
		assertTrue(loginDataService.isUserNameUnique("test"));
	}

	@Test
	void testSetNewPassword() {
		LoginData loginData = loginDataService.getAll().get(0);
				
		assertTrue(loginDataService.setNewPassword(loginData.getUserName(), "test"));
	}
}
