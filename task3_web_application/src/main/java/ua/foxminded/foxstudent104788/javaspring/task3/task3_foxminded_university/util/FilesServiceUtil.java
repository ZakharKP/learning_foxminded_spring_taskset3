package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lombok.extern.log4j.Log4j2;

/**
 * Utility class for handling files and extracting data from them.
 */
@Log4j2
public class FilesServiceUtil {

	/**
	 * Reads a properties file from the specified path and returns its content as a
	 * Map.
	 *
	 * @param path The path to the properties file.
	 * @return A Map containing key-value pairs from the properties file.
	 * @throws RuntimeException If there is an error while reading the file.
	 */
	public static Map<String, String> getMapFromFile(Path path) {
		log.info("Starting to execute map from " + path.toString());

		Map<String, String> map = new HashMap<>();
		Properties properties = new Properties();

		try (InputStream inputStream = Files.newInputStream(path)) {
			properties.load(inputStream);
		} catch (IOException e) {
			log.error("Error occurred while reading the file.");
			throw new RuntimeException(e);
		}

		for (String key : properties.stringPropertyNames()) {
			map.put(key, properties.getProperty(key));
		}
		return map;
	}

	/**
	 * Reads a file line by line and returns the content as a List of strings.
	 *
	 * @param path The path to the file.
	 * @return A List of strings containing the lines read from the file.
	 * @throws RuntimeException If there is an error while reading the file.
	 */
	public static List<String> getStringListFromFile(Path path) {
		log.info("Starting to execute List from " + path.toString());
		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			log.error("Error occurred while reading the file.");
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] loadImageToByteArray(String imagePath) {
		File imageFile = new File(imagePath);
		try (FileInputStream fis = new FileInputStream(imageFile)) {
			byte[] imageBytes = new byte[(int) imageFile.length()];
			fis.read(imageBytes);
			return imageBytes;
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Return null in case of an error
		}
	}
}
