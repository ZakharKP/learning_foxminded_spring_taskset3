package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.models.Image;

@Service
public interface ImageService {

	Optional<Image> getImage(Long imageId);

	Image saveImage(Image image);

	MediaType getMediaType(Image image);

	List<Image> getImagesbyIds(List<Long> selectedImages);

	void delete(Image image);

	Image saveImageFromFile(MultipartFile imageFile);

	List<Image> saveImages(List<Image> images);
}
