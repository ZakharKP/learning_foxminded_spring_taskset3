package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.repositories.ImagesRepository;
import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services.ImageService;

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {

	@Autowired
	private ImagesRepository imagesRepository;

	@Override
	public Optional<Image> getImage(Long imageId) {
		log.info("Start search of image with id=" + imageId);

		return imagesRepository.findById(imageId);
	}

	@Override
	public MediaType getMediaType(Image image) {
		log.info("Parsing Media type for Image with id=" + image.getId());

		return MediaType.parseMediaType(image.getMediaType());
	}

	@Override
	public List<Image> getImagesbyIds(List<Long> selectedImages) {
		log.info("Start search of List if images by ids");

		return imagesRepository.findAllById(selectedImages);
	}

	@Override
	public void delete(Image image) {
		log.info("Deleting Image with id=" + image.getId());

		imagesRepository.delete(image);
	}

	@Override
	public Image saveImageFromFile(MultipartFile imageFile) {
		log.info("Sawe new image from file=" + imageFile);
		try {
			Image image = Image.builder().fileName(imageFile.getOriginalFilename())
					.mediaType(imageFile.getContentType()).data(imageFile.getBytes()).build();

			return imagesRepository.save(image);
		} catch (IOException e) {
			log.error("Error trying save file: " + e.getStackTrace());
		}

		return null;
	}

	@Override
	public Image saveImage(Image image) {
		return imagesRepository.save(image);
	}

	@Override
	public List<Image> saveImages(List<Image> images) {
		return imagesRepository.saveAll(images);
	}

}
