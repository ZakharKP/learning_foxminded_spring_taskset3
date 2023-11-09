package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	private static final String STUDENT = "STUDENT";
	private static final String TEACHER = "TEACHER";
	private static final String ADMIN = "ADMIN";

	@Bean
	public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.authorizeHttpRequests().antMatchers("/css/**", "/webjars/**", "/images/**").permitAll()
				.antMatchers("/teacher/personal", "/student/personal").hasAnyRole(ADMIN, TEACHER, STUDENT)
				.antMatchers("/admin/**").hasRole(ADMIN).antMatchers("/teacher/**").hasAnyRole(ADMIN, TEACHER)
				.antMatchers("/student/**").hasAnyRole(ADMIN, STUDENT).antMatchers("/", "/courses", "/course/intro")
				.permitAll().anyRequest().authenticated().and().formLogin().and().build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

}