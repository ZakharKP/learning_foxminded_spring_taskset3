package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");

		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");

		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
	}
}