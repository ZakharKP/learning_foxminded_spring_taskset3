package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.service.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.model.ForTestsDataCreator;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl.ImageServiceImpl;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = {
		ImageServiceImpl.class }))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ComponentScan("ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl")

class ImageServiceImplTest {

	@Autowired
	private ImageServiceImpl imageService;
	
	@Test
	void testGetMediaType() {
		Image image =ForTestsDataCreator.getImages().get(0);
		image.setId(null);
		image = imageService.saveImage(ForTestsDataCreator.getImages().get(0));
		MediaType expected = MediaType.parseMediaType(image.getMediaType());
		MediaType actual = imageService.getMediaType(image);

		assertEquals(expected, actual);
	}

	@Test
	void testSaveImageFromFile() throws IOException {
		
		MultipartFile imagefromFile = ForTestsDataCreator.getMultipartFiles().get(0);
		Image image = imageService.saveImageFromFile(imagefromFile);
		
		byte[] expected = imagefromFile.getBytes();
		byte[] actual = image.getData();

		assertEquals(expected, actual);
	}

}
